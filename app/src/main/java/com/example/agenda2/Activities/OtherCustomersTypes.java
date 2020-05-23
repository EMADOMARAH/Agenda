package com.example.agenda2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
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
import com.example.agenda2.Models.LeeTexModel;
import com.example.agenda2.Models.Masn3Model;
import com.example.agenda2.Models.OtherInfoModel;
import com.example.agenda2.Models.OthersTypesModel;
import com.example.agenda2.Models.TypeModel;
import com.example.agenda2.R;

import java.util.List;

public class OtherCustomersTypes extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    List<OtherInfoModel> otherTypes;
    Dialog dialog;

    AppDatabase appDatabase;

    OtherInfoModel otherInfoModel;

    Adapter adapter;

    String type_txt,customerName,meters_txt,tape_txt,note_txt,addType_txt;

    String editname,editmeters,edittape,editnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_customers_types);

        customerName                        = getIntent().getStringExtra("name");

        appDatabase                         = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test1").build();

        new getTypes().execute();

        initView();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(OtherCustomersTypes.this)
                        .setMessage("هل انت متاكد من المسح")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new deleteMasn3().execute(adapter.getPostion(viewHolder.getAdapterPosition()));
                                Toast.makeText(getApplicationContext(),  "تم المسح بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new getTypes().execute();
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
                new AlertDialog.Builder(OtherCustomersTypes.this)
                        .setMessage("متأكد انك عايز تعدل")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                otherInfoModel = adapter.getPostion(viewHolder.getAdapterPosition());
                                editname = otherInfoModel.getType();
                                editmeters = otherInfoModel.getMeters();
                                edittape = otherInfoModel.getTape();
                                editnote = otherInfoModel.getNote();

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

                                        type_txt = customerName_field.getText().toString();
                                        meters_txt = meters_field.getText().toString();
                                        tape_txt = tape_field.getText().toString();
                                        note_txt = note_field.getText().toString();
                                        if(type_txt.equals("")){
                                            Toast.makeText(OtherCustomersTypes.this, "برجاء إدخال اسم الصنف", Toast.LENGTH_SHORT).show();
                                            customerName_field.requestFocus();
                                            return;
                                        } else if (meters_txt.isEmpty()) {
                                            Toast.makeText(OtherCustomersTypes.this, "برجاء ادخال عدد الامتار", Toast.LENGTH_SHORT).show();
                                            meters_field.requestFocus();
                                            return;
                                        }else if (tape_txt.isEmpty()) {
                                            Toast.makeText(OtherCustomersTypes.this, "برجاء ادخال نوع الشريطة", Toast.LENGTH_SHORT).show();
                                            tape_field.requestFocus();
                                            return;
                                        } else if (note_txt.isEmpty()) {
                                            note_txt = "";
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

                                new getTypes().execute();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public class UpdateData extends AsyncTask <OtherInfoModel,Void,List<OtherInfoModel>>{

        @Override
        protected List<OtherInfoModel> doInBackground(OtherInfoModel... otherInfoModels) {
            appDatabase.otherTypesDao().updateData(type_txt,meters_txt,tape_txt,note_txt,editname,editmeters,edittape,editnote);
            return appDatabase.otherTypesDao().getOtherTypes(customerName);
        }

        @Override
        protected void onPostExecute(List<OtherInfoModel> otherInfoModels) {
            dialog.dismiss();
            adapter = new Adapter(otherInfoModels);
            recyclerView.setAdapter(adapter);
        }
    }

    public class deleteMasn3 extends AsyncTask<OtherInfoModel,Void,List<OtherInfoModel>>
    {
        @Override
        protected List<OtherInfoModel> doInBackground(OtherInfoModel... otherInfoModels) {
            appDatabase.otherTypesDao().delete(otherInfoModels[0]);
            return appDatabase.otherTypesDao().getOtherTypes(customerName);
        }

        @Override
        protected void onPostExecute(List<OtherInfoModel> otherInfoModels) {
            adapter = new Adapter(otherInfoModels);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initView() {
        recyclerView                        = findViewById(R.id.recyclerview);
        layoutManager                       = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration               = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        dialog = new Dialog(OtherCustomersTypes.this);
    }
    public void addOtherType(View view) {
        dialog.setContentView(R.layout.other_type_dialog);
        dialog.setTitle("اضافة صنف");
        Button dialogButton = dialog.findViewById(R.id.dialog_button);
        final EditText type_field = dialog.findViewById(R.id.type_txt);
        final EditText meter_field = dialog.findViewById(R.id.meters);
        final EditText tape_field = dialog.findViewById(R.id.tape);
        final EditText note_field = dialog.findViewById(R.id.note);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addType_txt = type_field.getText().toString();
                meters_txt = meter_field.getText().toString();
                tape_txt = tape_field.getText().toString();
                note_txt = note_field.getText().toString();
                if(addType_txt.equals("")){
                    Toast.makeText(OtherCustomersTypes.this, "برجاء إدخال اسم الصنف", Toast.LENGTH_SHORT).show();
                    type_field.requestFocus();
                    return;
                }else if(meters_txt.equals("")){
                    Toast.makeText(OtherCustomersTypes.this, "برجاء إدخال عدد الامتار", Toast.LENGTH_SHORT).show();
                    meter_field.requestFocus();
                    return;
                }else if(tape_txt.equals("")){
                    Toast.makeText(OtherCustomersTypes.this, "برجاء إدخال نوع الشريط", Toast.LENGTH_SHORT).show();
                    tape_field.requestFocus();
                    return;
                }else if(note_txt.equals("")){
                    note_txt = "";
                }
                OtherInfoModel otherInfoModel = new OtherInfoModel(customerName,addType_txt,meters_txt,tape_txt,note_txt);
                new Insert().execute(otherInfoModel);
            }
        });
        dialog.show();
    }
    public class Insert extends AsyncTask<OtherInfoModel,Void,Void> {

        @Override
        protected Void doInBackground(OtherInfoModel... otherInfoModels) {
            appDatabase.otherTypesDao().insert(otherInfoModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            new getTypes().execute();
        }
    }
    public class getTypes extends AsyncTask<Void,Void,List<OtherInfoModel>>{

        @Override
        protected List<OtherInfoModel> doInBackground(Void... voids) {
            otherTypes = appDatabase.otherTypesDao().getOtherTypes(customerName);
            return otherTypes;
        }

        @Override
        protected void onPostExecute(List<OtherInfoModel> otherType) {
            adapter = new Adapter(otherTypes);
            recyclerView.setAdapter(adapter);
        }
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.VH>{
        List<OtherInfoModel> otherTypes;

        public Adapter(List<OtherInfoModel> otherTypes) {
            this.otherTypes = otherTypes;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.other_type_item,null,false);
            return new VH(view);
        }

        public OtherInfoModel getPostion(int position)
        {
            return otherTypes.get(position);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, final int position) {
            OtherInfoModel otherInfoModel = otherTypes.get(position);
                holder.name.setText(otherInfoModel.getType());
                holder.meters.setText(otherInfoModel.getMeters());
                holder.tape.setText(otherInfoModel.getTape());
                holder.note.setText(otherInfoModel.getNote());
        }

        @Override
        public int getItemCount() {
            return otherTypes.size();
        }

        public class VH extends RecyclerView.ViewHolder{

            TextView name,meters,tape,note;

            public VH(@NonNull View itemView) {
                super(itemView);
                name                   = itemView.findViewById(R.id.customer_name);
                meters                 = itemView.findViewById(R.id.meters);
                tape                     = itemView.findViewById(R.id.tape);
                note                     = itemView.findViewById(R.id.notes);
            }
        }
    }
}
