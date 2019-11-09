package com.mwigzell.places.network;


import android.content.Context;

import com.mwigzell.places.model.PlacesResponse;
import com.mwigzell.places.util.AndroidServices;
import com.mwigzell.places.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import timber.log.Timber;

public class ServiceCreator {
    public static final String API_BASE_URL = "https://maps.googleapis.com";
    public static String RETROFIT_CACHE = "RETROFIT_CACHE";

    @Inject
    FileUtils fileUtils;

    @Inject
    AndroidServices androidServices;
    
    @Inject
    Context context;

    private static OkHttpClient.Builder httpClient;
    //private static HttpLoggingInterceptor logging;
    private static Retrofit.Builder builder;
    private Cache cache;

    @Inject
    public ServiceCreator() {

    }

    public <S> S createService(Class<S> serviceClass) {
        cache = null;

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
        httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        //httpClient.addInterceptor(new AuthIntercepter(context));
        httpClient.addNetworkInterceptor(new REWRITE_RESPONSE_INTERCEPTOR());
        httpClient.addInterceptor(new OFFLINE_INTERCEPTOR(context));
        //httpClient.authenticator(new TokenAuthenticator(context));
        cache = createCacheForOkHTTP();
        httpClient.cache(cache);
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public <S> S createServiceForRefresh(Class<S> serviceClass) {
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
        httpClient = new OkHttpClient().newBuilder();

        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        //httpClient.addInterceptor(new AuthIntercepter(context));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private class REWRITE_RESPONSE_INTERCEPTOR implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header("Cache-Control");
            if (isChachableRequest(chain) && shouldChangeCacheParams(cacheControl)) {
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 60 * 15)
                            .build();
            } else {
                return originalResponse;
            }
        }
    };

    private boolean shouldChangeCacheParams(String cacheControl) {
        return cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0");
    }

    private class OFFLINE_INTERCEPTOR implements Interceptor {
        Context mContext;
        public OFFLINE_INTERCEPTOR(Context context) {
            mContext = context;
        }
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if (!androidServices.isNetwork() && isChachableRequest(chain)) {
                Timber.d("Rewriting request");

                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }

            return chain.proceed(request);
        }
    };

    private boolean isChachableRequest(Interceptor.Chain chain) {
        return chain.request().url().url().toString().contains("&parm=value");
    }

    private Cache createCacheForOkHTTP() {
        return new Cache(getDirectory(context), 1024 * 1024 * 10);
    }

    public void clearCacheForOkHTTP() {
        if (cache != null) {
            try {
                fileUtils.removeDir(new File(context.getExternalCacheDir(), RETROFIT_CACHE));
                cache.delete();
            } catch (IOException e) {
                Timber.e(e,"%s - Exception with message: %s",RETROFIT_CACHE,e.getMessage());
            }
        }
    }

    public void close() {
        httpClient = null;
        builder = null;
    }
    // returns the file to store cached details
    private File getDirectory(Context context) {
        return new File(context.getExternalCacheDir(), RETROFIT_CACHE);
    }

    public interface PlacesClient {
        @GET("/maps/api/place/nearbysearch/json")
        Observable<PlacesResponse> getPlaces(
                @Query("location") String location,
                @Query("radius") String radius,
                @Query("types") String types,
                @Query("key") String key);
    }

}
