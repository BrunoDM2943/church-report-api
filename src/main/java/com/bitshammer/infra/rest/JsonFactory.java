package com.bitshammer.infra.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class JsonFactory {
    private static Gson DEFAULT_GSON;

    public static Gson getGson() {
        if(DEFAULT_GSON == null) {
            DEFAULT_GSON = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                    ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDate()).create();
        }
        return DEFAULT_GSON;
    }
}
