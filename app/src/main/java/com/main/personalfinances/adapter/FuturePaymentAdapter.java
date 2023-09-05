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
import com.main.personalfinances.data.FuturePayment;

import java.util.ArrayList;
import java.util.List;

public class FuturePaymentAdapter extends RecyclerView.Adapter<FuturePaymentAdapter.FuturePaymentViewHolder>{

    private List<FuturePayment> futurePaymentList = new ArrayList<>();
    private LiveData<List<FuturePayment>> liveDataFuturePaymentList;

    public FuturePaymentAdapter(LiveData<List<FuturePayment>> liveDataFuturePaymentList) {
        this.liveDataFuturePaymentList = liveDataFuturePaymentList;

        liveDataFuturePaymentList.observeForever(new Observer<List<FuturePayment>>() {
            @Override
            public void onChanged(List<FuturePayment> futurePayments) {
                futurePaymentList.clear();
                futurePaymentList.addAll(futurePayments);
                notifyDataSetChanged();
            }
        });
    }


    @NonNull
    @Override
    public FuturePaymentAdapter.FuturePaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.futurepayment_item, parent, false);
        return new FuturePaymentAdapter.FuturePaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FuturePaymentAdapter.FuturePaymentViewHolder holder, int position) {
        FuturePayment futurePayment = futurePaymentList.get(position);
        holder.editCategory.setText(futurePayment.getCategory().toString());
        holder.editDueDate.setText(String.valueOf(futurePayment.getDueDate()));
        holder.editDescription.setText(futurePayment.getDescription());
    }

    @Override
    public int getItemCount() {
        return futurePaymentList.size();
    }

    public void setFuturePaymentList(List<FuturePayment> futurePaymentList) {
        this.futurePaymentList = futurePaymentList;
    }

    public List<FuturePayment> getFuturePaymentList() {
        return futurePaymentList;
    }

    public void addFuturePayment(FuturePayment futurePayment) {
        futurePaymentList.add(futurePayment);
    }
    class FuturePaymentViewHolder extends RecyclerView.ViewHolder {

        EditText editCategory;
        EditText editDescription;
        EditText editDueDate;

        public FuturePaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            editCategory = itemView.findViewById(R.id.editCategoryForFuturePayment);
            editDescription = itemView.findViewById(R.id.editDescriptionForFuturePayment);
            editDueDate = itemView.findViewById(R.id.editDueDateForFuturePayment);
        }
    }
}
