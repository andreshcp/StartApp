package com.example.talitaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.HashMap;

import io.grpc.Context;

public class HomeActivity extends AppCompatActivity {

    EditText etTitle, etDescription;
    TextView tvEmail;
    Button btnSendData,btnUpload;
    Bundle extras;
    String mailUserExtra,titleName,descriptionName;
    private static final String empty = "";
    private StorageReference Folder;
    private RecyclerView recyclerView;
    private ArrayList<DataSetFire> arrayList;
    private FirebaseRecyclerOptions<DataSetFire> options;
    private FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private static final int ImageBack = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Folder = FirebaseStorage.getInstance().getReference().child("ImagesFolder").child("database");
        btnUpload = findViewById(R.id.btn_upload);
        btnSendData = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recycler_id);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        tvEmail = findViewById(R.id.tv_email);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        extras = getIntent().getExtras();
        String mailUserExtra = extras.getString("emailExtra");
        tvEmail.setText(mailUserExtra);
        arrayList = new ArrayList<DataSetFire>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //showDataUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("database").child("items");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<DataSetFire>().setQuery(databaseReference, DataSetFire.class).build();

        adapter = new FirebaseRecyclerAdapter<DataSetFire, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull final DataSetFire model) {

                holder.titleN.setText(model.getTitleName());
                holder.descriptionN.setText(model.getDescriptionName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("titleName", model.getTitleName().toString());
                        intent.putExtra("descriptionName", model.getDescriptionName().toString());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.row, parent, false));
            }
        };

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleName = etTitle.getText().toString();
                descriptionName = etDescription.getText().toString();
                //mAuth
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("titleName",titleName);
                hashMap.put("descriptionName",descriptionName);
                databaseReference.push().setValue(hashMap);
                clearData();
            }
        });



        recyclerView.setAdapter(adapter);

    }

    private void showDataUser() {

    }

    private void clearData() {
        if (etDescription.getText()!=null||etTitle.getText()!=null){
            etDescription.setText(empty);
            etTitle.setText(empty);
        }
    }

    public void onUpload(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,ImageBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==ImageBack){

            if (resultCode==RESULT_OK){

                Uri ImageData = data.getData();

                final StorageReference Imagename = Folder.child("image"+ImageData.getLastPathSegment());
                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Image");

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("image", String.valueOf(uri));
                                imagestore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HomeActivity.this, "Finalmente Completo..", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                        Toast.makeText(HomeActivity.this, "Se guardo el archivo..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(HomeActivity.this, "No se pudo almacenar el archivo", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }
}
