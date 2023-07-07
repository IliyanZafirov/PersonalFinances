package com.main.personalfinances.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class BudgetWithSavings {
    @Embedded public Budget budget;
    @Relation(
            parentColumn = "id",
            entityColumn = "budgetId"
    )
    public List<Expense> expenses;
}
