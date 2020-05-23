package com.example.agenda2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda2.Models.Masn3Model;

import java.util.List;

@Dao
public interface Masn3Dao {

    @Insert
    Void insert (Masn3Model... u);

    @Query("SELECT * FROM Masn3Model Where Type == :type")
    List<Masn3Model> getMasn3Customers(String type);

    @Query("Update Masn3Model Set Customer_Name = :name , Meters = :meters , Tape = :tape , Note = :note Where Customer_Name = :realName " +
            "And Meters = :realMeters And Tape = :realTape And Note = :realNote")
    void UpdateCustomer(String name,String meters, String tape, String note, String realName, String realMeters, String realTape, String realNote);

    @Update
    Void update(Masn3Model customerModel);

    @Delete
    Void delete(Masn3Model customerModel);
}
