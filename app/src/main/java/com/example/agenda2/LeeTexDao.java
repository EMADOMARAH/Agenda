package com.example.agenda2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda2.Models.LeeTexModel;
import com.example.agenda2.Models.Masn3Model;

import java.util.List;
@Dao
public interface LeeTexDao {
    @Insert
    Void insert (LeeTexModel... u);

    @Query("SELECT * FROM LeeTexModel Where Type = :type")
    List<LeeTexModel> getLeeTexCutomers(String type);

    @Query("Update LeeTexModel Set Customer_Name = :name , Meters = :meters , Tape = :tape , Note = :note Where Customer_Name = :realName " +
            "And Meters = :realMeters And Tape = :realTape And Note = :realNote")
    void UpdateCustomer(String name,String meters, String tape, String note, String realName, String realMeters, String realTape, String realNote);

    @Update
    Void update(LeeTexModel leeTexModel);

    @Delete
    Void delete(LeeTexModel leeTexModel);
}
