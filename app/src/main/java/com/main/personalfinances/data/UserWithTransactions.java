package com.main.personalfinances.data;

        import androidx.room.Embedded;
        import androidx.room.Relation;

        import java.util.List;

public class UserWithTransactions {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<Transaction> transactions;
}
