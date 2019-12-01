package com.mwigzell.places.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mwigzell on 12/11/16.
 */

public class PlacesResponse {
    @SerializedName("html_attributions")
    @Expose
    public List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    public List<Place> results = null;
    @SerializedName("status")
    @Expose
    public String status;
}
