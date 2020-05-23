package com.example.agenda2.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Masn3Model {
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "Type")
    String type;
    @ColumnInfo(name = "Customer_Name")
    String c_name;
    @ColumnInfo(name = "Meters")
    String meters;
    @ColumnInfo(name = "Tape")
    String tape;
    @ColumnInfo(name = "Note")
    String note;

    @Ignore
    public Masn3Model(int id, String c_name, String type, String meters, String tape, String note) {
        this.id = id;
        this.c_name = c_name;
        this.type = type;
        this.meters = meters;
        this.tape = tape;
        this.note = note;
    }

    public Masn3Model(String c_name, String type, String meters, String tape, String note) {
        this.c_name = c_name;
        this.type = type;
        this.meters = meters;
        this.tape = tape;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMeters() {
        return meters;
    }

    public void setMeters(String meters) {
        this.meters = meters;
    }

    public String getTape() {
        return tape;
    }

    public void setTape(String tape) {
        this.tape = tape;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
