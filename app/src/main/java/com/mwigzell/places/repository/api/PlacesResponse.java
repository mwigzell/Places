package com.mwigzell.places.repository.api;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mwigzell.places.model.Place;

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
