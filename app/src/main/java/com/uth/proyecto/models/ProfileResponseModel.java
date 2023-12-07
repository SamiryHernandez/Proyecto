package com.uth.proyecto.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileResponseModel {
    private final int id;
    private final String firstName;
    private final String lastName;
    private int countryId;
    private String countryName;
    private float weight;
    private String photoUrl;

    public ProfileResponseModel(int id, String firstName, String lastName, int countryId, float weight, String photoUrl, String countryName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryId = countryId;
        this.weight = weight;
        this.photoUrl = photoUrl;
        this.countryName = countryName;
    }

    public ProfileResponseModel(int id, String firstName, String lastName, String countryName, String photoUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryName = countryName;
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Nullable
    public static ProfileResponseModel fromJson(@NonNull JSONObject json) {
        try {
            JSONObject country = json.getJSONObject("country");

            return new ProfileResponseModel(json.getInt("id"), json.getString("first_name"),
                    json.getString("last_name"), json.getInt("country_id"),
                    (float) json.getDouble("weight"), json.getString("photo_url"), country.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

