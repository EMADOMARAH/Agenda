package com.example.agenda2.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class OtherCustomersModel {

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "CustomerName")
    String name;

    @Ignore
    public OtherCustomersModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public OtherCustomersModel(String name) {
        this.name = name;
    }

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
