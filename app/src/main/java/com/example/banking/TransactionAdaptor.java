package com.example.banking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TransactionAdaptor extends ArrayAdapter<TransactionModel> {
    public TransactionAdaptor(Context context, List<TransactionModel> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_transactions_layout, parent, false);
        }

        // Get the data for this position
        TransactionModel item = getItem(position);

        // Recent transactions
        TextView transactionTimestampTextView = convertView.findViewById(R.id.transactionTimestampTextView);
        transactionTimestampTextView.setText(item.getTimestamp());

        TextView transactionDescriptionTextView = convertView.findViewById(R.id.transactionDescriptionTextView);
        transactionDescriptionTextView.setText(item.getTransactionDesc());

        TextView transactionAmountTextView = convertView.findViewById(R.id.transactionAmountTextView);
        transactionAmountTextView.setText(item.getAmount());

        TextView accountCurrentBalanceTextView = convertView.findViewById(R.id.accountCurrentBalanceTextView);
        accountCurrentBalanceTextView.setText(item.getCurrentBalance());

        return convertView;
    }
}
