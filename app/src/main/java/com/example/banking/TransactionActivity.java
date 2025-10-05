package com.example.banking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent intent = getIntent();
        Integer accountId = intent.getIntExtra("accountId", 0);
        String accountType = intent.getStringExtra("accountType");
        String accountNumber = intent.getStringExtra("accountNumber");
        String accountBalance = intent.getStringExtra("accountBalance");

        final TextView accountTypeTextView = findViewById(R.id.accountTypeTextView);
        final TextView accountNumberTextView = findViewById(R.id.accountNumberTextView);
        final TextView accountBalanceTextView = findViewById(R.id.accountBalanceTextView);
        final Button filterTransactionsButton = findViewById(R.id.filterButton);

        accountTypeTextView.setText(accountType);
        accountNumberTextView.setText("Acc. No - " + accountNumber);
        accountBalanceTextView.setText(accountBalance);

        final EditText keywordsEditText = findViewById(R.id.keywordsEditText);
        final EditText fromDateEditText = findViewById(R.id.fromDateEditText);
        final EditText toDateEditText = findViewById(R.id.toDateEditText);
        final EditText minAmountEditText = findViewById(R.id.minAmountEditText);
        final EditText maxAmountEditText = findViewById(R.id.maxAmountEditText);

        APIClient client = new APIClient(getApplicationContext());
        JsonNode data = null;
        try {
            data = client.executeGetRequest("/account/" + accountId + "/transactions");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<TransactionModel> transactions = null;
        transactions = updateTransactionModel(data);

        ArrayAdapter adapter = new TransactionAdaptor(this, transactions);

        ListView listView;
        listView = (ListView) findViewById(R.id.accountTransactionsListView);
        listView.setAdapter(adapter);

        filterTransactionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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

                    String filterParamsQueryString = client.buildQueryParams(filterParams);

                    data = client.executeGetRequest(
                            "/account/" + accountId + "/transactions?"
                                        + filterParamsQueryString
                                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                List<TransactionModel> filteredTransactions = null;
                filteredTransactions = updateTransactionModel(data);
                updateAdapterWithFilteredTransactions(adapter, filteredTransactions);
            }
        });
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

    private void updateAdapterWithFilteredTransactions(ArrayAdapter adapter, List<TransactionModel> filteredTransactions) {
        // Clear the existing data in the adapter
        adapter.clear();

        // Add the new filtered data to the adapter
        adapter.addAll(filteredTransactions);

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
    }
}
