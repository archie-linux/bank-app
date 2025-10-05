package com.example.banking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent intent = getIntent();
        ArrayList<AccountModel> accounts = (ArrayList<AccountModel>) intent.getSerializableExtra("accounts");


        final EditText toAccountEditText = findViewById(R.id.toAccountEditText);
        final TextView amountEditText = findViewById(R.id.amountEditText);
        final Spinner fromAccountSpinner = findViewById(R.id.fromAccountSpinner);
        final Button transferButton = findViewById(R.id.transferButton);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                AccountModel.getAccountTypes(accounts));

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        fromAccountSpinner.setAdapter(adapter);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int selectedIndex = fromAccountSpinner.getSelectedItemPosition();
                        String selectedItem = fromAccountSpinner.getSelectedItem().toString();

                        String fromAccountNumber = accounts.get(selectedIndex).getAccountNumber();
                        String toAccountNumber = toAccountEditText.getText().toString();
                        String amount = amountEditText.getText().toString();

                        JsonNode data = null;
                        String postData = "{\n    \"from_account_number\": \"" + fromAccountNumber + "\",\n    \"to_account_number\": \"" + toAccountNumber + "\",\n    \"amount\": \"" + amount + "\"\n}";

                        try {

                            APIClient client = new APIClient();
                            data = client.executePostRequest("/transfer", postData);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        String message = data.get("message").toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TransferActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
