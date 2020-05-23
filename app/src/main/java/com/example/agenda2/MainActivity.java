package com.example.agenda2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test1").build();
    }
}
