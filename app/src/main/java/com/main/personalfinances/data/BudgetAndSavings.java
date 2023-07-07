package com.main.personalfinances.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BudgetAndSavings {
    @Embedded public Budget budget;
    @Relation(
            parentColumn = "id",
            entityColumn = "budgetId"
    )
    public Savings savings;
}
