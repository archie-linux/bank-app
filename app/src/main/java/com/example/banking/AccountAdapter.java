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

public class AccountAdapter extends ArrayAdapter<AccountModel> {
//    private JsonNode accountsInfo;
    public AccountAdapter(Context context, List<AccountModel> accounts) {
        super(context, 0, accounts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_accounts_layout, parent, false);
        }

        // Get the data for this position
        AccountModel item = getItem(position);

        TextView accountTypeTextView = convertView.findViewById(R.id.accountTypeTextView);
        accountTypeTextView.setText(item.getAccountType());

        TextView accountBalanceTextView = convertView.findViewById(R.id.accountBalanceTextView);
        accountBalanceTextView.setText(item.getAccountBalance().toString());

        Button viewAccountTransactionsButton = convertView.findViewById(R.id.viewAccountTransactionsButton);
        viewAccountTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                Context context = v.getContext();

                Intent intent = new Intent(context, TransactionActivity.class);
                intent.putExtra("accountId", item.getAccountId());
                intent.putExtra("accountType", item.getAccountType());
                intent.putExtra("accountNumber", item.getAccountNumber());
                intent.putExtra("accountBalance", item.getAccountBalance());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
