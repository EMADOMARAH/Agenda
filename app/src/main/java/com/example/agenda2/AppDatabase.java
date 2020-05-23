package com.example.agenda2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.agenda2.Models.LeeTexModel;
import com.example.agenda2.Models.Masn3Model;
import com.example.agenda2.Models.OtherCustomersModel;
import com.example.agenda2.Models.OtherInfoModel;
import com.example.agenda2.Models.OthersTypesModel;
import com.example.agenda2.Models.TypeModel;

@Database(entities = {Masn3Model.class, LeeTexModel.class, OtherInfoModel.class, TypeModel.class, OtherCustomersModel.class, OthersTypesModel.class},version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract Masn3Dao masn3Dao();
    public abstract LeeTexDao leeTexDao();
    public abstract TypesDao typesDao();
    public abstract OtherCustomersDao otherCustomersDao();
    public abstract OtherTypesDao otherTypesDao();
}
