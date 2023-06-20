package com.main.personalfinances.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
