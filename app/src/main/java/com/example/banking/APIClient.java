package com.example.banking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIClient {

    private static final String GRAPHQL_ENDPOINT = "http://192.168.0.3:9001/graphql";

    private String result;

    public String executeGraphQLQuery(String query) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = null;
            url = new URL(GRAPHQL_ENDPOINT);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(query.getBytes());

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
        return result;
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
