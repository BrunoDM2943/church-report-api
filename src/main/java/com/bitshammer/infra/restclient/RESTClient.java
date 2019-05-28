package com.bitshammer.infra.restclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Named;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RESTClient {


    public static String get(String uri) throws Exception {
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

    public static <T>  Response post(String uri, T data)  {
        if(MockServer.MOCK_SERVER) {
            if (!MockServer.containsMock("POST", uri)) {
                throw new IllegalArgumentException("Mock not found for POST " + uri);
            }
            return MockServer.getMockedResponse("POST", uri);
        }
        Response response = null;
        int tries = 0;
        Exception e = null;
        do{
            try{
                response = doPost(uri, data);
                break;
            }catch(Exception ex){
                Logger.getGlobal().log(Level.WARNING, String.format("Attemps: %d Error getting %s: %s", ++tries, uri, ex.getMessage()));
                e = ex;
            }
        }
        while (tries <5);
        if (response == null) {
            return new Response(500, "Error calling Gson");
        }
        return response;
    }

    private static String doGet(String uri) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("X-Token", System.getenv("CHURCH_MEMBERS_ACCESS_TOKEN"));

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

    private static <T> Response doPost(String uri, T data) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("X-Token", System.getenv("CHURCH_MEMBERS_ACCESS_TOKEN"));
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        conn.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        if (data instanceof String) {
            wr.writeBytes((String)data);
        } else {
            wr.writeBytes(new Gson().toJson(data));
        }
        wr.flush();
        wr.close();

        BufferedReader reader = null;
        if(!"OK".equalsIgnoreCase(conn.getResponseMessage())) {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        Response r = new Response(conn.getResponseCode(), reader.lines().collect(Collectors.joining()));
        conn.disconnect();
        return r;
    }
}
