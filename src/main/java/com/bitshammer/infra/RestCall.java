package com.bitshammer.infra;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class RestCall {

    public String get(String uri) throws Exception {
        String response = null;
        int tries = 0;
        Exception e = null;
        do{
            try{
                response = doGet(uri);
                break;
            }catch(Exception ex){
                Logger.getGlobal().log(Level.WARNING, String.format("Attemps: %d Error getting %s: %s", ++tries, uri, ex.getMessage()));
                e = ex;
            }
        }
        while (tries <5);
        if (response == null) {
            throw e;
        }
        return response;
    }

    private String doGet(String uri) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        StringBuilder builder = new StringBuilder();
        String output = null;
        while ((output = br.readLine()) != null) {
            builder.append(output);
        }

        conn.disconnect();
        return builder.toString();
    }
}
