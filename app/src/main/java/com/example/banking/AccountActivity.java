package com.example.banking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;


public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        Integer customerId = intent.getIntExtra("customerId", 0);
        String  customerName = intent.getStringExtra("customerName");

        final TextView displayCustomerName = findViewById(R.id.displayCustomerNameTextView);
        displayCustomerName.setText("Hello, " + customerName.split(" ")[0]);

        final Button transferButton = findViewById(R.id.transferButton);

        APIClient client = new APIClient();
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/customer/" + customerId + "/accounts");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ArrayList<AccountModel> accounts = new ArrayList<>();

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

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, TransferActivity.class);
                intent.putExtra("accounts", accounts);
                startActivity(intent);
            }
        });

    }
}