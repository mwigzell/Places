package com.mwigzell.places.repository.api.network


import com.mwigzell.places.repository.api.PlacesResponse
import com.mwigzell.places.util.FileUtils
import io.reactivex.Observable
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import javax.inject.Named

class ServiceCreator @Inject
constructor(@Named("RetrofitCacheDir") val cacheDir: File,
            val fileUtils: FileUtils,
            private val networkStatus: NetworkStatus,
            @Named("RetrofitBaseUrl") val RETROFIT_BASE_URL: String) {

    private var cache: Cache? = null

    fun <S> createService(serviceClass: Class<S>): S {
        cache = null

        builder = Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        httpClient = OkHttpClient.Builder()
        httpClient!!.readTimeout(60, TimeUnit.SECONDS)
        httpClient!!.connectTimeout(60, TimeUnit.SECONDS)
        httpClient!!.addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR())
        httpClient!!.addInterceptor(OFFLINE_INTERCEPTOR())
        cache = createCacheForOkHTTP()
        httpClient!!.cache(cache)
        builder!!.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val retrofit = builder!!.client(httpClient!!.build()).build()
        return retrofit.create(serviceClass)
    }

    fun <S> createServiceForRefresh(serviceClass: Class<S>): S {
        builder = Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        httpClient = OkHttpClient().newBuilder()

        httpClient!!.readTimeout(60, TimeUnit.SECONDS)
        httpClient!!.connectTimeout(60, TimeUnit.SECONDS)
        builder!!.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val retrofit = builder!!.client(httpClient!!.build()).build()
        return retrofit.create(serviceClass)
    }

    private inner class REWRITE_RESPONSE_INTERCEPTOR : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            val cacheControl = originalResponse.header("Cache-Control")
            return if (isChachableRequest(chain) && shouldChangeCacheParams(cacheControl)) {
                originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + 60 * 15)
                        .build()
            } else {
                originalResponse
            }
        }
    }

    private fun shouldChangeCacheParams(cacheControl: String?): Boolean {
        return cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
    }

    private inner class OFFLINE_INTERCEPTOR() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()

            if (!networkStatus.isNetwork && isChachableRequest(chain)) {
                Timber.d("Rewriting request")

                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
            }

            return chain.proceed(request)
        }
    }

    private fun isChachableRequest(chain: Interceptor.Chain): Boolean {
        return chain.request().url.toString().contains("&parm=value")
    }

    private fun createCacheForOkHTTP(): Cache {
        return Cache(getDirectory(), (1024 * 1024 * 10).toLong())
    }

    fun clearCacheForOkHTTP() {
        cache?.let {
            try {
                fileUtils.removeDir(getDirectory())
                it.delete()
            } catch (e: IOException) {
                Timber.e(e, "%s - Exception with message: %s", RETROFIT_CACHE, e.message)
            }
        }
    }

    fun close() {
        httpClient = null
        builder = null
    }

    // returns the file to store cached details
    private fun getDirectory(): File {
        return File(cacheDir, RETROFIT_CACHE)
    }

    interface PlacesClient {
        @GET("/maps/api/place/nearbysearch/json")
        fun getPlaces(
                @Query("location") location: String,
                @Query("radius") radius: String,
                @Query("types") types: String,
                @Query("key") key: String): Observable<PlacesResponse>
    }

    companion object {
        var RETROFIT_CACHE = "RETROFIT_CACHE"

        private var httpClient: OkHttpClient.Builder? = null
        //private static HttpLoggingInterceptor logging;
        private var builder: Retrofit.Builder? = null
    }

}
