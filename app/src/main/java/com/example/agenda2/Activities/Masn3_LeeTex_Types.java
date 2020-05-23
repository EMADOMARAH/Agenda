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
import com.example.agenda2.Models.LeeTexModel;
import com.example.agenda2.Models.Masn3Model;
import com.example.agenda2.Models.TypeModel;
import com.example.agenda2.R;

import java.util.List;

public class Masn3_LeeTex_Types extends AppCompatActivity {

    //احنا جبنا الTypes وعرضناها بس هي لسة فاضية وعملنا اضافة لل Types بس لسة مجربناش حاجة
    //باقي نجرب .. و نعمل اضافة لل customers ونعرضهم و بعدين نعمل التعديل والحذف

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    List<String> masn3ModelList;
    List<String> leeTexModelList;
    Adapter adapter;
    Dialog dialog;

    String typename;

    AppDatabase appDatabase;
    int source;
    String addType_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masn3__lee_tex__types);

        source = getIntent().getIntExtra("source",0);

        appDatabase                         = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"test1").build();

        if (source == 1) {
            new getMasna3().execute();
        } else if (source == 2) {
            new getLeeTex().execute();
        }

        initView();

        //delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(Masn3_LeeTex_Types.this)
                        .setMessage("متأكد انك عايز تمسح")
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
                                    new getMasna3().execute();
                                }else if (source == 2) {
                                    new getLeeTex().execute();
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
                new AlertDialog.Builder(Masn3_LeeTex_Types.this)
                        .setMessage("متأكد انك عايز تعدل")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (source == 1) {
                                    typename = adapter.getMasn3posion(viewHolder.getAdapterPosition());
                                } else if (source == 2) {
                                    typename = adapter.getLeeTexposion(viewHolder.getAdapterPosition());
                                }
                                    dialog.setContentView(R.layout.types_custom_dialog);
                                    dialog.setTitle("تعديل الصنف");
                                    Button dialogButton = dialog.findViewById(R.id.dialog_button);
                                    final EditText dialogEditText = dialog.findViewById(R.id.type_txt);
                                    dialogEditText.setText(typename);
                                    dialogButton.setText("تعديل");
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addType_txt = dialogEditText.getText().toString();
                                            if(addType_txt.equals("")){
                                                Toast.makeText(Masn3_LeeTex_Types.this, "برجاء إدخال اسم الصنف", Toast.LENGTH_SHORT).show();
                                                dialogEditText.requestFocus();
                                                return;
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
                                    new getMasna3().execute();
                                }else if (source == 2) {
                                    new getLeeTex().execute();
                                }
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public class UpdateMasn3 extends AsyncTask <TypeModel,Void,List<String>>{

        @Override
        protected List<String> doInBackground(TypeModel... typeModels) {
            appDatabase.typesDao().updateRow(addType_txt,"Masn3",typename);
            appDatabase.typesDao().UpdateMasn3Big(addType_txt,typename);
            return appDatabase.typesDao().getMasn3Types();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            dialog.dismiss();
            adapter = new Adapter(strings,1);
            recyclerView.setAdapter(adapter);
        }
    }
    public class UpdateLeeTex extends AsyncTask <TypeModel,Void,List<String>>{

        @Override
        protected List<String> doInBackground(TypeModel... typeModels) {
            appDatabase.typesDao().updateRow(addType_txt,"LeeTex",typename);
            appDatabase.typesDao().UpdateLeeTexBig(addType_txt,typename);
            return appDatabase.typesDao().getLeeTexTypes();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            dialog.dismiss();
            adapter = new Adapter(2,strings);
            recyclerView.setAdapter(adapter);
        }
    }

    public class deleteMasn3 extends AsyncTask<String,Void,List<String>>
    {

        @Override
        protected List<String> doInBackground(String... strings) {
            appDatabase.typesDao().deleteRow(strings[0]);
            return appDatabase.typesDao().getMasn3Types();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            adapter = new Adapter(strings,1);
            recyclerView.setAdapter(adapter);
        }
    }
    public class deleteLeeTex extends AsyncTask<String,Void,List<String>>
    {
        @Override
        protected List<String> doInBackground(String... strings) {
            appDatabase.typesDao().deleteRow(strings[0]);
            return appDatabase.typesDao().getLeeTexTypes();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            adapter = new Adapter(strings,1);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initView() {
        recyclerView                        = findViewById(R.id.recyclerview);
        layoutManager                       = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration               = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        dialog = new Dialog(Masn3_LeeTex_Types.this);
    }

    public void addType(View view) {
        dialog.setContentView(R.layout.types_custom_dialog);
        dialog.setTitle("Add Type");
        Button dialogButton = dialog.findViewById(R.id.dialog_button);
        final EditText dialogEditText = dialog.findViewById(R.id.type_txt);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeModel typeModel;
                addType_txt = dialogEditText.getText().toString();
                if(addType_txt.equals("")){
                    Toast.makeText(Masn3_LeeTex_Types.this, "برجاء إدخال اسم الصنف", Toast.LENGTH_SHORT).show();
                    dialogEditText.requestFocus();
                    return;
                }
                if (source == 1) {
                    typeModel = new TypeModel("Masn3", addType_txt);
                    new Insert().execute(typeModel);
                }
                if (source == 2) {
                    typeModel = new TypeModel("LeeTex", addType_txt);
                    new Insert().execute(typeModel);
                }

            }
        });
        dialog.show();
    }

    public class Insert extends AsyncTask<TypeModel,Void,Void> {

        @Override
        protected Void doInBackground(TypeModel... typeModels) {
            appDatabase.typesDao().insert(typeModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            if (source == 1) {
                new getMasna3().execute();
            } else if (source == 2) {
                new getLeeTex().execute();
            }
        }
    }

    public class getMasna3 extends AsyncTask<Void,Void,List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            masn3ModelList = appDatabase.typesDao().getMasn3Types();
            return masn3ModelList;
        }

        @Override
        protected void onPostExecute(List<String> masn3Models) {
            adapter = new Adapter(masn3ModelList,1);
            recyclerView.setAdapter(adapter);
        }
    }
    public class getLeeTex extends AsyncTask<Void,Void,List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            leeTexModelList = appDatabase.typesDao().getLeeTexTypes();
            return leeTexModelList;
        }

        @Override
        protected void onPostExecute(List<String> leeTexModels) {
            adapter = new Adapter(2,leeTexModelList);
            recyclerView.setAdapter(adapter);
        }
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.VH>{
        List<String> masn3Models;
        List<String> leeTexModels;
        int source;

        public Adapter(List<String> masn3Models, int source) {
            this.masn3Models = masn3Models;
            this.source = source;
        }

        public Adapter(int source, List<String> leeTexModels) {
            this.leeTexModels = leeTexModels;
            this.source = source;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customer_item,null,false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, final int position) {
            if(source == 1){
                holder.name.setText(masn3Models.get(position));
                holder.id.setText(position + 1 + "");
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String typename = masn3Models.get(position);
                        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(),Masn3_LeeTex_Customers.class);
                                intent.putExtra("type",typename);
                                intent.putExtra("source",1);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }else if(source == 2){
                holder.name.setText(leeTexModels.get(position));
                holder.id.setText(position + 1 + "");
                final String typename = leeTexModels.get(position);
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),Masn3_LeeTex_Customers.class);
                        intent.putExtra("type",typename);
                        intent.putExtra("source",2);
                        startActivity(intent);
                    }
                });
            }
        }

        public  String getLeeTexposion(int position)
        {
            return leeTexModels.get(position);
        }

        public  String getMasn3posion(int position)
        {
            return masn3Models.get(position);
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
}