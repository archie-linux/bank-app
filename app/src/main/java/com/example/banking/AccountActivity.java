package com.example.banking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;


public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        Integer customerId = intent.getIntExtra("customerId", 0);
        String  customerName = intent.getStringExtra("customerName");

        final TextView displayCustomerName = findViewById(R.id.displayCustomerNameTextView);
        displayCustomerName.setText("Hello, " + customerName);


        APIClient client = new APIClient();
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/customer/" + customerId + "/accounts");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<AccountModel> accounts = new ArrayList<>();

        for (JsonNode dataNode: data) {
            String accountType = dataNode.get("account_type").asText();
            String accountNumber = dataNode.get("account_number").asText();
            String accountBalance = "$" + dataNode.get("account_balance").asText();

            String formattedAccountType = accountType.substring(0, 1).toUpperCase() + accountType.substring(1)
                    + " - " + accountNumber.substring(accountNumber.length() - 4).toString();

            accounts.add(new AccountModel(formattedAccountType,
                    accountNumber,
                    accountBalance));
        }

        ArrayAdapter adapter = new AccountAdapter(this, accounts);

        ListView listView;
        listView = (ListView) findViewById(R.id.accountsListView);
        listView.setAdapter(adapter);
    }
}