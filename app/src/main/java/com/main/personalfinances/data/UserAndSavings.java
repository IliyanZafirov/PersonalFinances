package com.main.personalfinances.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class UserAndSavings {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public Savings savings;
}
