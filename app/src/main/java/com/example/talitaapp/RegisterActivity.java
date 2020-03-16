package com.example.talitaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPass, etName;
    Button btnRegistrarse, btnRegistrado;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceUsers;
    String emailUser, passUser, nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("database").child("users");
        mAuth = FirebaseAuth.getInstance();
        etName = (EditText) findViewById(R.id.et_nombre);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPass = (EditText) findViewById(R.id.et_pass);
        btnRegistrado = (Button) findViewById(R.id.btn_registerr);
        btnRegistrarse = (Button) findViewById(R.id.btn_loginn);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }

    public void onRegist(View v) {
        onBackPressed();
    }
    public void onLoginn(View v){
        nameUser = etName.getText().toString();
        emailUser = etEmail.getText().toString();
        passUser = etPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(emailUser, passUser)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("message sucessfully", "createUserWithEmail:success" + task.getResult().toString());
                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<String,String> hashMapuser = new HashMap<>();
                            hashMapuser.put("nameUser",nameUser);
                            hashMapuser.put("emailUser",emailUser);
                            hashMapuser.put("passUser",passUser);
                            databaseReferenceUsers.push().setValue(hashMapuser);
                            Intent intentHome = new Intent(getApplicationContext(),HomeActivity.class);

                            startActivity(intentHome);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_loginn:

                break;
            case R.id.btn_registerr:

                break;

        }
    }
}
