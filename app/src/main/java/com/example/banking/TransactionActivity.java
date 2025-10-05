package com.example.banking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent intent = getIntent();
        Integer accountId = intent.getIntExtra("accountId", 0);
        String  accountType = intent.getStringExtra("accountType");
        String  accountNumber = intent.getStringExtra("accountNumber");
        String  accountBalance = intent.getStringExtra("accountBalance");

        final TextView accountTypeTextView = findViewById(R.id.accountTypeTextView);
        final TextView accountNumberTextView = findViewById(R.id.accountNumberTextView);
        final TextView accountBalanceTextView = findViewById(R.id.accountBalanceTextView);

        accountTypeTextView.setText("Account Type: " + accountType);
        accountNumberTextView.setText("Account Number: " + accountNumber);
        accountBalanceTextView.setText(accountBalance);

        APIClient client = new APIClient();
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/account/" + accountId + "/transactions");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(data);

        List<TransactionModel> transactions = new ArrayList<>();

        for (JsonNode dataNode: data) {
            String transactionType = dataNode.get("type").asText();
            String transactionDesc = dataNode.get("desc").asText();
            String timestamp = dataNode.get("timestamp").asText();

            SimpleDateFormat inputDateTimeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            Date parsedDateTime = null;
            try {
                parsedDateTime = inputDateTimeFormat.parse(timestamp);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            int day = parsedDateTime.getDate();
            int month = parsedDateTime.getMonth() + 1;
            int year = parsedDateTime.getYear() + 1900;

            String formattedTimestamp = null;
            formattedTimestamp  =  year + "/" + month + "/" + day;

            String amount = "$" + dataNode.get("amount").asText();
            String currentBalance = dataNode.get("current_balance").asText();

            transactions.add(new TransactionModel(
                    transactionType,
                    transactionDesc,
                    formattedTimestamp.toString(),
                    amount,
                    currentBalance
            ));
        }

        ArrayAdapter adapter = new TransactionAdaptor(this, transactions);

        ListView listView;
        listView = (ListView) findViewById(R.id.accountTransactionsListView);
        listView.setAdapter(adapter);
    }
}
