package com.mwigzell.places.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mwigzell on 12/11/16.
 */

public class Place {
    public class Location {
        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;
    }

    public class Northeast {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;
    }

    public class Southwest {

        @SerializedName("lat")
        @Expose
        public Double lat;
        @SerializedName("lng")
        @Expose
        public Double lng;
    }

    public class Viewport {

        @SerializedName("northeast")
        @Expose
        public Northeast northeast;
        @SerializedName("southwest")
        @Expose
        public Southwest southwest;
    }

    public class Geometry {

        @SerializedName("location")
        @Expose
        public Location location;
        @SerializedName("viewport")
        @Expose
        public Viewport viewport;
    }

    public class OpeningHours {

        @SerializedName("open_now")
        @Expose
        public Boolean openNow;
        @SerializedName("weekday_text")
        @Expose
        public List<Object> weekdayText = null;
    }

    public class Photo {
        @SerializedName("height")
        @Expose
        public Integer height;
        @SerializedName("html_attributions")
        @Expose
        public List<String> htmlAttributions = null;
        @SerializedName("photo_reference")
        @Expose
        public String photoReference;
        @SerializedName("width")
        @Expose
        public Integer width;
    }

    @SerializedName("geometry")
    @Expose
    public Geometry geometry;
    @SerializedName("icon")
    @Expose
    public String icon;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("opening_hours")
    @Expose
    public OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    public List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("price_level")
    @Expose
    public Integer priceLevel;
    @SerializedName("rating")
    @Expose
    public Double rating;
    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("scope")
    @Expose
    public String scope;
    @SerializedName("types")
    @Expose
    public List<String> types = null;
    @SerializedName("vicinity")
    @Expose
    public String vicinity;
}
