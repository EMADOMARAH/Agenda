package com.example.agenda2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda2.Models.OtherCustomersModel;

import java.util.List;
@Dao
public interface OtherCustomersDao {

    @Insert
    Void insert(OtherCustomersModel... u);

    @Query("SELECT CustomerName FROM OtherCustomersModel")
    List<String> getAll();

    @Query("Delete From OtherCustomersModel Where CustomerName = :name")
    void deleteRow(String name);

    @Query("Update OtherCustomersModel Set CustomerName = :name Where CustomerName = :realName")
    void updateData(String name, String realName);

    @Query("Update OtherInfoModel Set Customer_Name = :name Where Customer_Name = :realName")
    void updateBig(String name, String realName);

    @Update
    Void update(OtherCustomersModel otherModel);

    @Delete
    Void delete(OtherCustomersModel otherModel);

}