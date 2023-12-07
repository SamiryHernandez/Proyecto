package com.uth.proyecto.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryResponseModel {
    private boolean status;
    private List<Country> data;

    public boolean isSuccess() {
        return status;
    }

    public List<Country> getData() {
        return data;
    }

    @NonNull
    public static CountryResponseModel fromJson(@NonNull JSONObject json) {
        CountryResponseModel model = new CountryResponseModel();
        model.data = new ArrayList<>();

        try {
            model.status = json.getBoolean("status");

            if (model.isSuccess()) {
                JSONArray jsonArray = json.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject countryObject = jsonArray.getJSONObject(i);
                    Country country = new Country();

                    country.setId(countryObject.getInt("id"));
                    country.setName(countryObject.getString("name"));

                    model.data.add(country);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static class Country {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }



}
