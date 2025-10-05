package com.example.banking;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class APIClient {

    private static final String API_SERVER = "https://192.168.0.3:5001";
    HttpURLConnection urlConnection = null;
    private String requestMethod;
    private String result;

    private Context context;

    public APIClient(Context context)
    {
        this.context = context;
    };

    public JsonNode executeGetRequest(String endpoint) throws JsonProcessingException {
        try {
            URL url =  new URL(API_SERVER + endpoint);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            SSLContext sslContext = SSLUtils.createSSLContext(this.context);
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            InputStream APIResponse = urlConnection.getInputStream();
            System.out.println(APIResponse);
            result = readInputStream(APIResponse);

        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return getAPIResponse(result);
    }

    public JsonNode executePostRequest(String endpoint, String postData) throws JsonProcessingException {
        try {
            URL url =  new URL(API_SERVER + endpoint);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            SSLContext sslContext = SSLUtils.createSSLContext(this.context);
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            // Write the request body to the output stream
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postData.getBytes());

            InputStream APIResponse = urlConnection.getInputStream();
            result = readInputStream(APIResponse);


        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return getAPIResponse(result);
    }

    public static String convertMapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    public static String buildQueryParams(Map<String, String> params) {
        StringBuilder queryParams = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                String key = URLEncoder.encode(entry.getKey(), "UTF-8");
                String value = URLEncoder.encode(entry.getValue(), "UTF-8");

                if (queryParams.length() > 0) {
                    queryParams.append("&");
                }

                queryParams.append(key)
                        .append("=")
                        .append(value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(); // Handle the exception according to your needs
            }
        }

        return queryParams.toString();
    }

    public String readInputStream(InputStream ins) throws IOException {
        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        StringBuilder responseStringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            // add current line to return string
            responseStringBuilder.append(line);
        }
        // close the buffered reader when it is done
        reader.close();

        return responseStringBuilder.toString();
    }

    public JsonNode getAPIResponse(String apiResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(apiResponse);
    }
}
