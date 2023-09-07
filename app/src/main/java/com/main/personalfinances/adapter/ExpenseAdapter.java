package com.main.personalfinances.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.main.personalfinances.R;
import com.main.personalfinances.data.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * Populates the expenses RecyclerView with data about expenses.
 * It creates and manages individual expense item views. It's responsible for creating, binding
 * and recycling views as the user scrolls through the list
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final List<Expense> expenseList = new ArrayList<>();
    private final LiveData<List<Expense>> liveDataExpenseList;

    public ExpenseAdapter(LiveData<List<Expense>> liveDataExpenseList) {
        this.liveDataExpenseList = liveDataExpenseList;

        liveDataExpenseList.observeForever(new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                expenseList.clear();
                expenseList.addAll(expenses);
                notifyDataSetChanged();
            }
        });
    }


    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.editCategory.setText(expense.getCategory().toString());
        holder.editPrice.setText(String.valueOf(expense.getPrice()));
        holder.editDescription.setText(expense.getDescription());
        holder.editPurchaseDate.setText(expense.getDateAdded().toString());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    /**
     * Holds references to expense views in a single expense item layout. It acts as a cache for
     * these views, so we don't have to call findViewById() to look up the views.
     */
    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        EditText editCategory;
        EditText editPrice;
        EditText editDescription;
        EditText editPurchaseDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            editCategory = itemView.findViewById(R.id.editCategory);
            editPrice = itemView.findViewById(R.id.editPrice);
            editDescription = itemView.findViewById(R.id.editDescription);
            editPurchaseDate = itemView.findViewById(R.id.editPurchaseDate);
        }
    }
}
