package com.example.agenda2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agenda2.R;

public class AllCustomers extends AppCompatActivity {

    int source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);




    }

    public void masn3(View view) {
        source = 1;
        Intent intent = new Intent(getApplicationContext(), Masn3_LeeTex_Types.class);
        intent.putExtra("source",source);
        startActivity(intent);
    }

    public void leetex(View view) {
        source = 2;
        Intent intent = new Intent(getApplicationContext(), Masn3_LeeTex_Types.class);
        intent.putExtra("source",source);
        startActivity(intent);
    }

    public void other(View view) {
        startActivity(new Intent(getApplicationContext(),OtherCustomers.class));
    }
}
