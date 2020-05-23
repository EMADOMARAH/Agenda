package com.example.agenda2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda2.Models.TypeModel;

import java.util.List;

@Dao
public interface TypesDao {

    @Insert
    Void insert(TypeModel... typesModels);

    @Query("Select Type From TypeModel Where Source = 'Masn3'")
    List<String> getMasn3Types();
    @Query("Select Type From TypeModel Where Source = 'LeeTex'")
    List<String> getLeeTexTypes();
    @Query("DELETE  From TypeModel Where Type = :type")
    void deleteRow(String type);

    @Query("Update TypeModel " +
            "Set Type = :type Where Source = :source And Type = :realname")
    void updateRow(String type,String source,String realname);

    @Query("Update LeeTexModel Set Type = :type Where Type = :realname")
    void UpdateLeeTexBig(String type,String realname);
    @Query("Update Masn3Model Set Type = :type Where Type = :realname")
    void UpdateMasn3Big(String type,String realname);


    @Update
    Void update(TypeModel typeModel);

    @Delete
    Void delete(TypeModel typeModel);
}
