package com.catatancodingku.rodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAct extends AppCompatActivity {

    EditText edJudul;
    EditText edDeskripsi;
    Button btnSave;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    public final String TAG = "_cek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edJudul         = findViewById(R.id.edTaskJudul);
        edDeskripsi     = findViewById(R.id.edTaskDesc);
        btnSave         = findViewById(R.id.btnSimpan);
        mAuth           = FirebaseAuth.getInstance();
        mDatabase       = FirebaseDatabase.getInstance().getReference();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpanTugas(mAuth.getCurrentUser());
            }
        });
    }

    private void simpanTugas(FirebaseUser mUser) {
        String judul        = edJudul.getText().toString();
        String deskripsi    = edDeskripsi.getText().toString();
        String user         = mUser.getDisplayName();
        boolean status      = false;

        if (TextUtils.isEmpty(judul) || TextUtils.isEmpty(deskripsi)){

            Toast.makeText(this,"Lengkapi form",Toast.LENGTH_SHORT).show();
            return;

        }

        Log.d(TAG, "simpanTugas: AddTask = "+judul+deskripsi+user);
        Member tugas = new Member(user,judul,deskripsi,status);
        mDatabase.child("Task").push().setValue(tugas)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddAct.this,"Berhasil ditambahkan",Toast.LENGTH_SHORT).show();
                            loading();
                        }else {
                            Toast.makeText(AddAct.this,"Gagal ditambahkan",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        edJudul.setText("");
        edDeskripsi.setText("");

    }

    private void loading() {
        Handler mhandler =  new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },500);
    }
}
