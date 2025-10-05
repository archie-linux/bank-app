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
import java.util.List;


public class AccountActivity extends AppCompatActivity {
    Integer customerId;

    static ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        customerId = intent.getIntExtra("customerId", 0);
        String  customerName = intent.getStringExtra("customerName");

        final TextView displayCustomerName = findViewById(R.id.displayCustomerNameTextView);
        displayCustomerName.setText("Hello, " + customerName.split(" ")[0]);

        final Button transferButton = findViewById(R.id.transferButton);

        APIClient client = new APIClient(getApplicationContext());
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/customer/" + customerId + "/accounts");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ArrayList<AccountModel> accounts = updateAccountModel(data);

        adapter = new AccountAdapter(this, accounts);

        ListView listView;
        listView = (ListView) findViewById(R.id.accountsListView);
        listView.setAdapter(adapter);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, TransferActivity.class);
                intent.putExtra("accounts", accounts);
                intent.putExtra("customerId", customerId);
                startActivity(intent);
            }
        });
    }

    public static ArrayList<AccountModel> updateAccountModel(JsonNode data) {
        ArrayList<AccountModel> accounts = new ArrayList<>();

        for (JsonNode dataNode: data) {
            String accountId = dataNode.get("account_id").asText();
            String accountType = dataNode.get("account_type").asText();
            String accountNumber = dataNode.get("account_number").asText();
            String accountBalance = dataNode.get("account_balance").asText();

            String formattedAccountType = accountType.substring(0, 1).toUpperCase() + accountType.substring(1)
                    + " - " + accountNumber.substring(accountNumber.length() - 4).toString();

            accounts.add(new AccountModel(
                    accountId,
                    formattedAccountType,
                    accountNumber,
                    accountBalance));
        }
        return accounts;
    }
}