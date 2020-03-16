package com.example.talitaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 234;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor Desconocido";
    String emailExtra = "";
    ImageView imgView;
    EditText etEmail, etPass;
    String email, password;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_pass);
        email = etEmail.getText().toString();
        password = etPass.getText().toString();*/

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                emailExtra = user.getEmail().toString();
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("emailExtra",emailExtra);
                    startActivity(intent);

                    //startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    // already signed in
                }else{
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            /*new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.MicrosoftBuilder().build(),*/
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };

    }

    private void onSetDataUser(String userDisplayName, String email, String provider) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            if (resultCode==RESULT_OK){
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                emailExtra = user.getEmail().toString();
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                intent.putExtra("emailExtra",emailExtra);
                startActivity(intent);
            }else{
                Toast.makeText(this, "No se completar le proceso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_login:
               //onLogin(v);
            break;

            case R.id.btn_register:
                //onRegister(v);

            break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener!=null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}

