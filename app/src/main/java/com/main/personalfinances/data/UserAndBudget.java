package com.main.personalfinances.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserAndBudget {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public Budget budget;
}
