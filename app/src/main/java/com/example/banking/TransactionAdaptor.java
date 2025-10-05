package com.example.banking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdaptor extends RecyclerView.Adapter<TransactionAdaptor.ViewHolder> {
    private List<TransactionModel> transactions;
    private Context context;

    public TransactionAdaptor(Context context, List<TransactionModel> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        TextView desc;
        TextView amount;
        TextView currentBalance;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            timestamp = itemView.findViewById(R.id.transactionTimestampTextView);
            desc = itemView.findViewById(R.id.transactionDescriptionTextView);
            amount = itemView.findViewById(R.id.transactionAmountTextView);
            currentBalance = itemView.findViewById(R.id.accountCurrentBalanceTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transactions_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set item height
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = 150;
        holder.itemView.setLayoutParams(layoutParams);

        // Update transaction items
        TransactionModel data = transactions.get(position);
        holder.timestamp.setText(data.getTimestamp());
        holder.desc.setText(data.getTransactionDesc());
        holder.amount.setText(data.getAmount());
        holder.currentBalance.setText(data.getCurrentBalance());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateData(List<TransactionModel> newData) {
        transactions.clear();
        transactions.addAll(newData);
        notifyDataSetChanged();
    }
}