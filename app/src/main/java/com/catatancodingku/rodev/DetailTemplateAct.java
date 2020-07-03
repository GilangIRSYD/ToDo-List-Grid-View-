package com.catatancodingku.rodev;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailTemplateAct extends AppCompatActivity implements View.OnClickListener{

    TextView             tvUser;
    TextView             tvJudul;
    TextView             tvDeskripsi;
    LinearLayout         deleteTask;
    DatabaseReference    mDatabsae;
    private String       key;
    Button               btnEdit;

    Dialog               customDialog;
    Button               btnPop;
    ImageView            btnPopClose;

    ImageView            btnPopCloseEdit;
    EditText             edEditJudul;
    EditText             edEditDeskripsi;
    Button               btnEditSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_template);
        Intent getData   = getIntent();

        tvUser           = findViewById(R.id.tvDetailAuthor);
        tvJudul          = findViewById(R.id.tvDetailJudul);
        tvDeskripsi      = findViewById(R.id.tvDetailDeskrpisi);
        deleteTask       = findViewById(R.id.deleteTask);
        btnEdit          = findViewById(R.id.button);
        mDatabsae        = FirebaseDatabase.getInstance().getReference();

        tvJudul.setMovementMethod(new ScrollingMovementMethod());
        tvDeskripsi.setMovementMethod(new ScrollingMovementMethod());

        key              = getData.getStringExtra("KEY");
        Log.d("_cek", "KEY =" + key);



        tvUser.setText(getData.getStringExtra("USER"));
        tvJudul.setText(getData.getStringExtra("JUDUL"));
        tvDeskripsi.setText(getData.getStringExtra("DESKRIPSI"));


        deleteTask.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        customDialog = new Dialog(this);






    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case (R.id.deleteTask):

                showPopup();

            break;

            case (R.id.button):

                showPopupEdit();

            break;

        }
    }

    private void showPopupEdit() {
        final Intent getDataFromDetail = getIntent();

        customDialog.setContentView(R.layout.popup_edit);

        btnPopCloseEdit         = customDialog.findViewById(R.id.close);
        btnEditSimpan           = customDialog.findViewById(R.id.btnEditSimpan);
        edEditJudul             = customDialog.findViewById(R.id.edEditTaskJudul);
        edEditDeskripsi         = customDialog.findViewById(R.id.edEditTaskDeskripsi);

        edEditJudul.setText(getDataFromDetail.getStringExtra("JUDUL"));
        edEditDeskripsi.setText(getDataFromDetail.getStringExtra("DESKRIPSI"));


        btnPopCloseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        btnEditSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user             = getDataFromDetail.getStringExtra("USER");
                boolean status          = getDataFromDetail.getBooleanExtra("STATUS",false);
                String editJudul        = edEditJudul.getText().toString();
                String editDesc         = edEditDeskripsi.getText().toString();

                Log.d("_cek", "destail edit  user =" + user );

                if (TextUtils.isEmpty(editJudul) || TextUtils.isEmpty(editDesc)){
                    Toast.makeText(DetailTemplateAct.this,"Tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                    return;
                }

                Member newTask = new Member(user,editJudul,editDesc,status);
                mDatabsae.child("Task").child(key).setValue(newTask);
                Toast.makeText(getApplicationContext(),"Data tugas diubah",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();

    }

    private void showPopup() {

        customDialog.setContentView(R.layout.popup_delete);
        btnPopClose     = customDialog.findViewById(R.id.closeRed);
        btnPop          = customDialog.findViewById(R.id.button2);

        btnPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabsae.child("Task").child(key).removeValue();
                tvJudul.setText("");
                tvDeskripsi.setText("");
                finish();
            }
        });


        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
    }
}