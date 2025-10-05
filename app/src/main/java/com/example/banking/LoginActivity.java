package com.example.banking;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);

        TextView loginErrorMessageTextView = findViewById(R.id.loginErrorMessageTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            JsonNode data = null;
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final EditText editUsernameText = findViewById(R.id.editUsernameText);
                        final EditText editPasswordText = findViewById(R.id.editPasswordText);

                        String username = editUsernameText.getText().toString();
                        String password = editPasswordText.getText().toString();

                        APIClient client = new APIClient(getApplicationContext());

                        Map<String, Object> loginInfo = new HashMap<>();
                        loginInfo.put("username", username);
                        loginInfo.put("password", password);

                        String loginPostData = client.convertMapToJson(loginInfo);

                        // Print the JSON string
                        System.out.println("JSON String: " + loginPostData);

                        try {
                            data = client.executePostRequest("/login", loginPostData);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        String errorMessage = data.get("error").asText();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(errorMessage.isEmpty()) {
                                    Integer customerId = data.get("id").asInt();
                                    String customerName = data.get("name").asText();
                                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerName", customerName);
                                    startActivity(intent);
                                } else {
                                    loginErrorMessageTextView.setText(errorMessage);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
}