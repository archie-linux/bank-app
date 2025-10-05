package com.example.banking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    private EditText keywordsEditText;
    private EditText fromDateEditText ;
    private EditText toDateEditText;
    private EditText minAmountEditText;
    private EditText maxAmountEditText;

    RecyclerView recyclerView;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int currentPage = 1;

    String accountId;
    private Boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent intent = getIntent();
        keywordsEditText = findViewById(R.id.keywordsEditText);
        fromDateEditText = findViewById(R.id.fromDateEditText);
        toDateEditText = findViewById(R.id.toDateEditText);
        minAmountEditText = findViewById(R.id.minAmountEditText);
        maxAmountEditText = findViewById(R.id.maxAmountEditText);

        accountId = intent.getStringExtra("accountId");
        String accountType = intent.getStringExtra("accountType");
        String accountNumber = intent.getStringExtra("accountNumber");
        String accountBalance = intent.getStringExtra("accountBalance");

        final TextView accountTypeTextView = findViewById(R.id.accountTypeTextView);
        final TextView accountNumberTextView = findViewById(R.id.accountNumberTextView);
        final TextView accountBalanceTextView = findViewById(R.id.accountBalanceTextView);
        final Button filterTransactionsButton = findViewById(R.id.filterButton);
        final Button prevPageButton = findViewById(R.id.prevPageButton);
        final Button nextPageButton = findViewById(R.id.nextPageButton);


        accountTypeTextView.setText(accountType);
        accountNumberTextView.setText("Acc. No - " + accountNumber);
        accountBalanceTextView.setText(accountBalance);

        APIClient client = new APIClient(getApplicationContext());
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/account/" + accountId + "/transactions");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<TransactionModel> transactions = null;
        transactions = updateTransactionModel(data);


        // Set the layout manager to the RecyclerView
        recyclerView = findViewById(R.id.accountTransactionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set view adapter
        TransactionAdaptor adapter = new TransactionAdaptor(this, transactions);
        recyclerView.setAdapter(adapter);

        filterTransactionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentPage = 1;
                JsonNode data = getFilteredTransactions();
                List<TransactionModel> filteredTransactions = null;
                filteredTransactions = updateTransactionModel(data);
                adapter.updateData(filteredTransactions);
            }
        });

        prevPageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentPage--;
                if (currentPage > 0) {
                    JsonNode data = getFilteredTransactions();
                    List<TransactionModel> filteredTransactions = null;
                    filteredTransactions = updateTransactionModel(data);
                    adapter.updateData(filteredTransactions);
                } else {
                    currentPage = 1;
                    Toast.makeText(TransactionActivity.this, "Reached the first page", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextPageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentPage++;
                JsonNode data = getFilteredTransactions();
                if (!data.isEmpty()) {
                    List<TransactionModel> filteredTransactions = null;
                    filteredTransactions = updateTransactionModel(data);
                    adapter.updateData(filteredTransactions);
                } else {
                    Toast.makeText(TransactionActivity.this, "No more data", Toast.LENGTH_SHORT).show();
                    // Go back to the first page
                    currentPage = 0;
                }
            }
        });
    }

    private JsonNode getFilteredTransactions() {
        String keyword = keywordsEditText.getText().toString();
        String fromDate = fromDateEditText.getText().toString();
        String toDate = toDateEditText.getText().toString();
        String minAmount = minAmountEditText.getText().toString();
        String maxAmount = maxAmountEditText.getText().toString();

        APIClient client = new APIClient(getApplicationContext());

        JsonNode data = null;
        try {

            Map<String, String> filterParams = new HashMap<>();
            filterParams.put("min_amount", minAmount);
            filterParams.put("max_amount", maxAmount);
            filterParams.put("min_date", fromDate);
            filterParams.put("max_date", toDate);
            filterParams.put("keyword", keyword);
            filterParams.put("page", Integer.toString(currentPage));

            String filterParamsQueryString = client.buildQueryParams(filterParams);

            data = client.executeGetRequest(
                    "/account/" + accountId + "/transactions?"
                            + filterParamsQueryString
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    private List<TransactionModel> updateTransactionModel(JsonNode data) {
        List<TransactionModel> transactions = new ArrayList<>();

        for (JsonNode dataNode : data) {
            String transactionType = dataNode.get("type").asText();
            String transactionDesc = dataNode.get("desc").asText();
            String timestamp = dataNode.get("timestamp").asText();

            String[] parts = timestamp.split(" ");

            String formattedDate = parts[1] + "-" + parts[2] + "-" + parts[3];

            String amount = "$" + dataNode.get("amount").asText();
            String currentBalance = dataNode.get("current_balance").asText();

            transactions.add(new TransactionModel(
                    transactionType,
                    transactionDesc,
                    formattedDate,
                    amount,
                    currentBalance
            ));
        }
        return transactions;
    }
}
