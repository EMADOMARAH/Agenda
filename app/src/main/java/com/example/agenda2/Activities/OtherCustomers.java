package com.example.agenda2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda2.AppDatabase;
import com.example.agenda2.Models.OtherCustomersModel;
import com.example.agenda2.Models.TypeModel;
import com.example.agenda2.R;

import java.util.List;

public class OtherCustomers extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    AppDatabase appDatabase;

    Dialog dialog;
    Adapter adapter;

    String addCustomer_txt,customeredit;

    List<String> otherCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_customers);

        appDatabase                         = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test1").build();

        new getCustomers().execute();

        initViews();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(OtherCustomers.this)
                        .setMessage("هل انت متاكد من المسح")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new deleteOthers().execute(adapter.getOthersPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(getApplicationContext(),  "تم المسح بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new getCustomers().execute();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        // Update
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(OtherCustomers.this)
                        .setMessage("متأكد انك عايز تعدل")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                customeredit = adapter.getOthersPosition(viewHolder.getAdapterPosition());
                                dialog.setContentView(R.layout.types_custom_dialog);
                                dialog.setTitle("تعديل اسم العميل");
                                Button dialogButton = dialog.findViewById(R.id.dialog_button);
                                final EditText dialogEditText = dialog.findViewById(R.id.type_txt);
                                dialogEditText.setText(customeredit);
                                dialogButton.setText("تعديل");
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addCustomer_txt = dialogEditText.getText().toString();
                                        if(addCustomer_txt.equals("")){
                                            Toast.makeText(OtherCustomers.this, "برجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show();
                                            dialogEditText.requestFocus();
                                            return;
                                        }
                                        new UpdateData().execute();

                                    }
                                });
                                dialog.show();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new getCustomers().execute();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public class UpdateData extends AsyncTask <TypeModel,Void,List<String>>{

        @Override
        protected List<String> doInBackground(TypeModel... typeModels) {
            appDatabase.otherCustomersDao().updateData(addCustomer_txt,customeredit);
            appDatabase.otherCustomersDao().updateBig(addCustomer_txt,customeredit);
            return appDatabase.otherCustomersDao().getAll();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            dialog.dismiss();
            adapter = new Adapter(strings);
            recyclerView.setAdapter(adapter);
        }
    }

    public class deleteOthers extends AsyncTask<String,Void,List<String>>
    {
        @Override
        protected List<String> doInBackground(String... strings) {
            appDatabase.otherCustomersDao().deleteRow(strings[0]);
            return appDatabase.otherCustomersDao().getAll();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            adapter = new Adapter(strings);
            recyclerView.setAdapter(adapter);
        }
    }
    private void initViews() {
        recyclerView                        = findViewById(R.id.recyclerview);
        layoutManager                       = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration               = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        dialog                              = new Dialog(OtherCustomers.this);
    }

    public void addCustomer(View view) {
        dialog.setContentView(R.layout.types_custom_dialog);
        dialog.setTitle("Add Customer");
        Button dialogButton = dialog.findViewById(R.id.dialog_button);
        final EditText dialogEditText = dialog.findViewById(R.id.type_txt);
        dialogEditText.setHint("اسم العميل");

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer_txt = dialogEditText.getText().toString();
                if(addCustomer_txt.equals("")){
                    Toast.makeText(OtherCustomers.this, "برجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show();
                    dialogEditText.requestFocus();
                    return;
                }

                OtherCustomersModel otherCustomersModel = new OtherCustomersModel(addCustomer_txt);
                new Insert().execute(otherCustomersModel);
            }
        });
        dialog.show();
    }

    public class Insert extends AsyncTask<OtherCustomersModel,Void,Void> {

        @Override
        protected Void doInBackground(OtherCustomersModel... otherCustomersModels) {
            appDatabase.otherCustomersDao().insert(otherCustomersModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            new getCustomers().execute();
        }
    }

    public class getCustomers extends AsyncTask<Void,Void,List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            otherCustomers = appDatabase.otherCustomersDao().getAll();
            return otherCustomers;
        }

        @Override
        protected void onPostExecute(List<String> otherCutomers) {
            adapter = new Adapter(otherCustomers);
            recyclerView.setAdapter(adapter);
        }
    }
    public class Adapter extends RecyclerView.Adapter<VH>{

        List<String> customersNames;

        public Adapter(List<String> otherCustomersModelList) {
            this.customersNames = otherCustomersModelList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customer_item,null,false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, final int position) {

            holder.name.setText(customersNames.get(position));
            holder.id.setText(position + 1 + "");
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String customername = customersNames.get(position);
                    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),OtherCustomersTypes.class);
                            intent.putExtra("name",customername);
                            startActivity(intent);
                        }
                    });
                }
            });
        }

        public  String getOthersPosition(int position)
        {
            return customersNames.get(position);
        }
        @Override
        public int getItemCount() {
            return customersNames.size();
        }
        }

        public class VH extends RecyclerView.ViewHolder{

            TextView id,name;
            LinearLayout linearLayout;

            public VH(@NonNull View itemView) {
                super(itemView);
                id                      = itemView.findViewById(R.id.id);
                name                    = itemView.findViewById(R.id.name);
                linearLayout            = itemView.findViewById(R.id.layout);
            }
        }
}
