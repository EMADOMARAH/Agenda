package com.example.agenda2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda2.Models.OtherInfoModel;
import com.example.agenda2.Models.OthersTypesModel;
import com.example.agenda2.Models.TypeModel;

import java.util.List;

@Dao
public interface OtherTypesDao {
    @Insert
    Void insert(OtherInfoModel... otherInfoModels);

    @Query("Select * From OtherInfoModel Where Customer_Name = :name")
    List<OtherInfoModel> getOtherTypes(String name);

    @Query("Update OtherInfoModel Set Type = :type, Meters = :meters, Tape = :tape, Note = :note " +
            "Where Type = :realType And Meters = :realMeters And Tape = :realTape And Note = :realNote")
    void updateData(String type, String meters, String tape, String note, String realType, String realMeters, String realTape, String realNote);

    @Update
    Void update(OtherInfoModel otherInfoModel);

    @Delete
    Void delete(OtherInfoModel otherInfoModel);
}
