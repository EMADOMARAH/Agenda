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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda2.AppDatabase;
import com.example.agenda2.Models.LeeTexModel;
import com.example.agenda2.Models.Masn3Model;
import com.example.agenda2.Models.TypeModel;
import com.example.agenda2.R;

import java.util.List;

public class Masn3_LeeTex_Customers extends AppCompatActivity {
    
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    String type,add_CustomerName_txt,meters_txt,tape_txt,note_txt;
    int source;

    Adapter adapter;

    AppDatabase appDatabase;

    Masn3Model masn3Model;
    LeeTexModel leeTexModel;

    String editname,editmeters,edittape,editnote;


    Dialog dialog;

    List<Masn3Model> masn3ModelList;
    List<LeeTexModel> leeTexModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masn3__lee_tex__customers);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "test1").build();

        type = getIntent().getStringExtra("type");
        source = getIntent().getIntExtra("source", 0);

        if (source == 1) {
            new getMasna3Cutomers().execute();
        } else if (source == 2) {
            new getLeeTexCustomers().execute();
        }
        initViews();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(Masn3_LeeTex_Customers.this)
                        .setMessage("هل انت متاكد من المسح")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (source == 1) {
                                    new deleteMasn3().execute(adapter.getMasn3posion(viewHolder.getAdapterPosition()));
                                }else if (source == 2) {
                                    new deleteLeeTex().execute(adapter.getLeeTexposion(viewHolder.getAdapterPosition()));
                                }
                                Toast.makeText(getApplicationContext(),  "تم المسح بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (source == 1) {
                                    new getMasna3Cutomers().execute();
                                }else if (source == 2) {
                                    new getLeeTexCustomers().execute();
                                }
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
                new AlertDialog.Builder(Masn3_LeeTex_Customers.this)
                        .setMessage("متأكد انك عايز تعدل")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (source == 1) {
                                    masn3Model = adapter.getMasn3posion(viewHolder.getAdapterPosition());
                                    editname = masn3Model.getC_name();
                                    editmeters = masn3Model.getMeters();
                                    edittape = masn3Model.getTape();
                                    editnote = masn3Model.getNote();
                                } else if (source == 2) {
                                    leeTexModel = adapter.getLeeTexposion(viewHolder.getAdapterPosition());
                                    editname = leeTexModel.getC_name();
                                    editmeters = leeTexModel.getMeters();
                                    edittape = leeTexModel.getTape();
                                    editnote = leeTexModel.getNote();
                                }
                                dialog.setContentView(R.layout.add_customer_dialog);
                                dialog.setTitle("تعديل العميل");
                                Button dialogButton = dialog.findViewById(R.id.dialog_button);
                                final EditText customerName_field = dialog.findViewById(R.id.cutomer_txt);
                                final EditText meters_field = dialog.findViewById(R.id.meters);
                                final EditText tape_field = dialog.findViewById(R.id.tape);
                                final EditText note_field = dialog.findViewById(R.id.note);
                                customerName_field.setText(editname);
                                meters_field.setText(editmeters);
                                tape_field.setText(edittape);
                                note_field.setText(editnote);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        add_CustomerName_txt = customerName_field.getText().toString();
                                        meters_txt = meters_field.getText().toString();
                                        tape_txt = tape_field.getText().toString();
                                        note_txt = note_field.getText().toString();
                                        if(add_CustomerName_txt.equals("")){
                                            Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show();
                                            customerName_field.requestFocus();
                                            return;
                                        } else if (meters_txt.isEmpty()) {
                                            Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء ادخال عدد الامتار", Toast.LENGTH_SHORT).show();
                                            meters_field.requestFocus();
                                            return;
                                        }else if (tape_txt.isEmpty()) {
                                            Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء ادخال نوع الشريطة", Toast.LENGTH_SHORT).show();
                                            tape_field.requestFocus();
                                            return;
                                        } else if (note_txt.isEmpty()) {
                                            note_txt = "";
                                        }

                                        if (source == 1) {
                                            new UpdateMasn3().execute();
                                        }
                                        if (source == 2) {
                                            new UpdateLeeTex().execute();
                                        }

                                    }
                                });
                                dialog.show();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (source == 1) {
                                    new getMasna3Cutomers().execute();
                                }else if (source == 2) {
                                    new getLeeTexCustomers().execute();
                                }
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public class UpdateMasn3 extends AsyncTask <Masn3Model,Void,List<Masn3Model>>{

        @Override
        protected List<Masn3Model> doInBackground(Masn3Model... masn3Models) {
            appDatabase.masn3Dao().UpdateCustomer(add_CustomerName_txt,meters_txt,tape_txt,note_txt,editname,editmeters,edittape,editnote);
            return appDatabase.masn3Dao().getMasn3Customers(type);
        }

        @Override
        protected void onPostExecute(List<Masn3Model> masn3Models) {
            dialog.dismiss();
            adapter = new Adapter(masn3Models,1,type);
            recyclerView.setAdapter(adapter);
        }
    }
    public class UpdateLeeTex extends AsyncTask <LeeTexModel,Void,List<LeeTexModel>>{

        @Override
        protected List<LeeTexModel> doInBackground(LeeTexModel... leeTexModels) {
            appDatabase.leeTexDao().UpdateCustomer(add_CustomerName_txt,meters_txt,tape_txt,note_txt,editname,editmeters,edittape,editnote);
            return appDatabase.leeTexDao().getLeeTexCutomers(type);
        }

        @Override
        protected void onPostExecute(List<LeeTexModel> leeTexModels) {
            dialog.dismiss();
            adapter = new Adapter(2,leeTexModels,type);
            recyclerView.setAdapter(adapter);
        }
    }

    public class deleteMasn3 extends AsyncTask<Masn3Model,Void,List<Masn3Model>>
    {
        @Override
        protected List<Masn3Model> doInBackground(Masn3Model... masn3Models) {
            appDatabase.masn3Dao().delete(masn3Models[0]);
            return appDatabase.masn3Dao().getMasn3Customers(type);
        }

        @Override
        protected void onPostExecute(List<Masn3Model> masn3Models) {
            adapter = new Adapter(masn3Models,1,type);
            recyclerView.setAdapter(adapter);
        }
    }
    public class deleteLeeTex extends AsyncTask<LeeTexModel,Void,List<LeeTexModel>>
    {

        @Override
        protected List<LeeTexModel> doInBackground(LeeTexModel... leeTexModels) {
            appDatabase.leeTexDao().delete(leeTexModels[0]);
            return appDatabase.leeTexDao().getLeeTexCutomers(type);
        }

        @Override
        protected void onPostExecute(List<LeeTexModel> leeTexModels) {
            adapter = new Adapter(2,leeTexModels,type);
            recyclerView.setAdapter(adapter);
        }
    }



    private void initViews() {
        recyclerView                        = findViewById(R.id.recyclerview);
        layoutManager                       = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration               = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        dialog                              = new Dialog(Masn3_LeeTex_Customers.this);
    }

    public void addCustomer(View view) {
        dialog.setContentView(R.layout.add_customer_dialog);
        dialog.setTitle("Add Type");
        Button dialogButton = dialog.findViewById(R.id.dialog_button);
        final EditText customerName_field = dialog.findViewById(R.id.cutomer_txt);
        final EditText meters_field = dialog.findViewById(R.id.meters);
        final EditText tape_field = dialog.findViewById(R.id.tape);
        final EditText note_field = dialog.findViewById(R.id.note);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_CustomerName_txt = customerName_field.getText().toString();
                meters_txt = meters_field.getText().toString();
                tape_txt = tape_field.getText().toString();
                note_txt = note_field.getText().toString();
                if(add_CustomerName_txt.equals("")){
                    Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء إدخال اسم العميل", Toast.LENGTH_SHORT).show();
                    customerName_field.requestFocus();
                    return;
                } else if (meters_txt.isEmpty()) {
                    Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء ادخال عدد الامتار", Toast.LENGTH_SHORT).show();
                    meters_field.requestFocus();
                    return;
                }else if (tape_txt.isEmpty()) {
                    Toast.makeText(Masn3_LeeTex_Customers.this, "برجاء ادخال نوع الشريطة", Toast.LENGTH_SHORT).show();
                    tape_field.requestFocus();
                    return;
                } else if (note_txt.isEmpty()) {
                    note_txt = "";
                }
                if (source == 1) {
                    Masn3Model masn3Model = new Masn3Model(add_CustomerName_txt,type,meters_txt,tape_txt,note_txt);
                    new InsertMasn3().execute(masn3Model);
                }
                if (source == 2) {
                    LeeTexModel leeTexModel = new LeeTexModel(add_CustomerName_txt,type,meters_txt,tape_txt,note_txt);
                    new InsertLeeTex().execute(leeTexModel);
                }

            }
        });
        dialog.show();
    }

    public class InsertMasn3 extends AsyncTask<Masn3Model,Void,Void> {

        @Override
        protected Void doInBackground(Masn3Model... masn3Models) {
            appDatabase.masn3Dao().insert(masn3Models);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            new getMasna3Cutomers().execute();
        }
    }
    public class InsertLeeTex extends AsyncTask<LeeTexModel,Void,Void> {

        @Override
        protected Void doInBackground(LeeTexModel... leeTexModels) {
            appDatabase.leeTexDao().insert(leeTexModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            new getLeeTexCustomers().execute();
        }
    }

    public class getMasna3Cutomers extends AsyncTask<Void,Void,List<Masn3Model>>{

        @Override
        protected List<Masn3Model> doInBackground(Void... voids) {
            masn3ModelList = appDatabase.masn3Dao().getMasn3Customers(type);
            return masn3ModelList;
        }

        @Override
        protected void onPostExecute(List<Masn3Model> masn3Models) {
            adapter = new Adapter(masn3ModelList,1,type);
            recyclerView.setAdapter(adapter);
        }
    }
    public class getLeeTexCustomers extends AsyncTask<Void,Void,List<LeeTexModel>>{

        @Override
        protected List<LeeTexModel> doInBackground(Void... voids) {
            leeTexModelList = appDatabase.leeTexDao().getLeeTexCutomers(type);
            return leeTexModelList;
        }

        @Override
        protected void onPostExecute(List<LeeTexModel> leeTexModels) {
            adapter = new Adapter(2,leeTexModelList,type);
            recyclerView.setAdapter(adapter);
        }
    }
    
    public class Adapter extends RecyclerView.Adapter<Masn3_LeeTex_Customers.Adapter.VH>{
        List<Masn3Model> masn3Models;
        List<LeeTexModel> leeTexModels;
        int source;
        String type;

        public  LeeTexModel getLeeTexposion(int position)
        {
            return leeTexModels.get(position);
        }

        public  Masn3Model getMasn3posion(int position)
        {
            return masn3Models.get(position);
        }

        public Adapter(List<Masn3Model> masn3Models, int source , String type) {
            this.masn3Models = masn3Models;
            this.source = source;
            this.type = type;
        }

        public Adapter(int source, List<LeeTexModel> leeTexModels , String type) {
            this.leeTexModels = leeTexModels;
            this.source = source;
            this.type = type;
        }

        @NonNull
        @Override
        public Masn3_LeeTex_Customers.Adapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.type_item,null,false);
            return new Masn3_LeeTex_Customers.Adapter.VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Masn3_LeeTex_Customers.Adapter.VH holder, final int position) {
            if(source == 1){
                Masn3Model masn3Model = masn3Models.get(position);
                holder.name.setText(masn3Model.getC_name());
                holder.meters.setText(masn3Model.getMeters());
                holder.tape.setText(masn3Model.getTape());
                holder.note.setText(masn3Model.getNote());
            }else if(source == 2){
                LeeTexModel leeTexModel = leeTexModels.get(position);
                holder.name.setText(leeTexModel.getC_name());
                holder.meters.setText(leeTexModel.getMeters());
                holder.tape.setText(leeTexModel.getTape());
                holder.note.setText(leeTexModel.getNote());
            }
        }

        @Override
        public int getItemCount() {
            if (source == 1) {
                return masn3Models.size();
            } else if (source == 2) {
                return leeTexModels.size();
            }
            return 0;
        }

        public class VH extends RecyclerView.ViewHolder{

            TextView name,meters,tape,note;

            public VH(@NonNull View itemView) {
                super(itemView);
                name                    = itemView.findViewById(R.id.customer_name);
                meters                  = itemView.findViewById(R.id.meters);
                tape                    = itemView.findViewById(R.id.tape);
                note                    = itemView.findViewById(R.id.notes);
            }
        }
    }
}
