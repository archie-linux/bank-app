package com.example.banking;

import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

                        APIClient client = new APIClient();
                        String postData = "{\n    \"username\": \"" + username + "\",\n    \"password\": \"" + password + "\"\n}";
                        JsonNode data = null;
                        try {
                            data = client.executePostRequest("/login", postData);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        Integer customerId = data.get("id").asInt();
                        String customerName = data.get("name").asText();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (customerId != 0) {
                                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                                    intent.putExtra("customerId", customerId);
                                    intent.putExtra("customerName", customerName);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
}