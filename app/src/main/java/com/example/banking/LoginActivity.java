package com.example.banking;

import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final EditText editUsernameText = findViewById(R.id.editUsernameText);
                        final EditText editPasswordText = findViewById(R.id.editPasswordText);

                        // Simulate the login logic; replace this with your actual login API request
                        String username = editUsernameText.getText().toString();
                        String password = editPasswordText.getText().toString();

                        String query = "{\"query\":\"query {\\nloginUser(userName: \\\"" + username + "\\\", password: \\\"" + password + "\\\") {\\n            success\\n            message\\n        }\\n}\\n\",\"variables\":{}}";

                        APIClient client = new APIClient();
                        String result = client.executeGraphQLQuery(query);
                        JsonNode data = null;
                        try {
                            data = client.getAPIResponse(result);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        Boolean isValid = data.get("data").get("loginUser").get("success").asBoolean();
                        String errMessage = data.get("data").get("loginUser").get("message").asText();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isValid) {
                                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

//    private class LoginThread extends Thread {
//
//        @Override
//        public void run() {
//            final EditText editUsernameText = findViewById(R.id.editUsernameText);
//            final EditText editPasswordText = findViewById(R.id.editPasswordText);
//
//            // Simulate the login logic; replace this with your actual login API request
//            String username = editUsernameText.getText().toString();
//            String password = editPasswordText.getText().toString();
//
//            String query = "{\"query\":\"query {\\nloginUser(userName: \\\"shs\\\", password: \\\"cscj\\\") {\\n            success\\n            message\\n        }\\n}\\n\",\"variables\":{}}";
//
//            APIClient client = new APIClient();
//            String result = client.executeGraphQLQuery(query);
//            try {
//                JsonNode data = client.getAPIResponse(result);
//                Boolean isValid = data.get("data").get("loginUser").get("success").asBoolean();
//                String errMessage = data.get("data").get("loginUser").get("message").asText();
//                System.out.println(data);
//                System.out.print(errMessage);
//                if (isValid) {
//                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
//                    startActivity(intent);
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }
}