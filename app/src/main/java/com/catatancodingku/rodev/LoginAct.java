package com.catatancodingku.rodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.PrivateKey;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {
    private static final String  TAG = "_cek";

    private TextView tvBuatAkun;
    private EditText edUsername,edPassword;
    private Button btLogin;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvBuatAkun      = findViewById(R.id.tvBuatAkun);
        edUsername      = findViewById(R.id.edUsername);
        edPassword      = findViewById(R.id.edPassword);
        btLogin         = findViewById(R.id.btnLogin);

        //Action
        tvBuatAkun.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        mauth = FirebaseAuth.getInstance();


        FirebaseUser user = mauth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(LoginAct.this, MainActivity.class));
            Toast.makeText(getApplicationContext(),"Hallo " + user.getDisplayName(),Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    @Override
    public void onClick(View v) {
        Intent pindah;

        switch (v.getId()){
            case R.id.tvBuatAkun:

                pindah = new Intent(this,RegisterAct.class);
                startActivity(pindah);
                break;

            case R.id.btnLogin:

                Log.d(TAG, "onClick: button login clicked");
                String mail = edUsername.getText().toString();
                String pass = edPassword.getText().toString();

                if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass) || pass.length() < 8){
                    onFailed();
                    return;
                }
                SignIn(mail,pass);
                break;

        }
    }

    public void SignIn(String email , String password){

        mauth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: Login = " + task.isSuccessful());

                        if (task.isSuccessful()) {
                            FirebaseUser account = task.getResult().getUser();
                            onSuccess(account);
                        }else{
                            onFailed();
                        }

                    }
                });

        }

    private void onSuccess(FirebaseUser account){

        String nama = account.getDisplayName();
        Toast.makeText(this, "Selamat datang " + nama,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

    private void onFailed(){

        Toast.makeText(this, "Pastikan ID Pass sudah benar",Toast.LENGTH_SHORT).show();
        return;
    }
}


