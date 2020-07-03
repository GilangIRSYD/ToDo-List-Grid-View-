package com.catatancodingku.rodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class RegisterAct extends AppCompatActivity implements View.OnClickListener {

    private EditText edRegUsername,edRegEmail,edRegPassword,edRegConfirmPass;
    private Button btnRegister;
    private SharedPreferences msetting;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edRegUsername           = findViewById(R.id.edRegName);
        edRegEmail              = findViewById(R.id.edRegEmail);
        edRegPassword           = findViewById(R.id.edRegPassword);
        edRegConfirmPass        = findViewById(R.id.edRegConfirmPass);
        btnRegister             = findViewById(R.id.btnRegister);

        //action
        btnRegister.setOnClickListener(this);
        mAuth               = FirebaseAuth.getInstance();
        mDatabase           = FirebaseDatabase.getInstance().getReference();

        

    }

    @Override
    public void onClick(View v) {

        String name,email,password,confPassword;

        msetting = getApplicationContext().getSharedPreferences("Settings",0);
        editor   = msetting.edit();

        switch (v.getId()){

            case R.id.btnRegister:
                name = edRegUsername.getText().toString();
                email = edRegEmail.getText().toString();
                password = edRegPassword.getText().toString();
                confPassword = edRegConfirmPass.getText().toString();

                Log.d("cekpass",password);
                Log.d("cekpass",confPassword);

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confPassword)){
                    Toast.makeText(this,"Isi Form dengan benar",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edRegEmail.setError("Masukkan e-mail dengan benar");
                    return;
                }

                if ( password.length() < 8 ){
                    edRegPassword.setError("Terlalu pendek");
                    return;
                }

                if ( !password.equals(confPassword)){
                    Toast.makeText(this,"Passwod tidak cocok",Toast.LENGTH_SHORT);
                    edRegConfirmPass.setError("Ga cocok kaya jodoh :'v");
                    return;
                }



                editor.putString("USERNAME",name);
                editor.putString("EMAIL",email);
                editor.putString("PASSWORD",password);
                editor.commit();

                String mail = msetting.getString("EMAIL",null);
                String user = msetting.getString("USERNAME",null);
                String pass = msetting.getString("PASSWORD",null);

                createAccount(mail,pass,user);

                break;

        }

    }

    public void createAccount(String email, String password, final String memName){

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        Log.d("_cek", "buat akun = " + task.isSuccessful());

                        if (task.isSuccessful()){

                            FirebaseUser user = task.getResult().getUser();


                            onAuthSuccess(user,memName);


                        }else {

                            Log.d("_cekAc", "buat akun gagal");
                            onAuthFailed();


                        }

                    }
                });
    }

    public void onAuthSuccess(FirebaseUser user, String name){
        String memUser  = user.getEmail();
        String memId    = user.getUid();

        writeMember(memId,name,memUser);
        Toast.makeText(this,"Registrasi kamu berhasil",Toast.LENGTH_SHORT).show();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdate);
        mAuth.signOut();

        startActivity(new Intent(this, LoginAct.class));
        finish();

    }

    public void onAuthFailed(){
        Toast.makeText(this,"Registrasi gagal",Toast.LENGTH_SHORT).show();
    }

    private void writeMember( String id, String name ,String email){

        Member member = new Member(name,email);
        mDatabase.child("Member").child(id).setValue(member);
    }
}
