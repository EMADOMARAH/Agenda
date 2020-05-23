package com.example.agenda2.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class OthersTypesModel {
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "CustomerName")
    String source;
    @ColumnInfo(name = "Type")
    String typename;

    @Ignore
    public OthersTypesModel(int id, String source, String typename) {
        this.id = id;
        this.source = source;
        this.typename = typename;
    }

    public OthersTypesModel(String source, String typename) {
        this.source = source;
        this.typename = typename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
