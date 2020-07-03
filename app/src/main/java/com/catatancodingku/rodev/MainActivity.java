package com.catatancodingku.rodev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{


    ImageView btnAdd;
    ImageView btnLogOut;
    RecyclerView rvUtama;
    RecyclerView.Adapter adapter;
    DatabaseReference mDatabse;
    ArrayList<Member> listTugas;
    Member tugas;

    ImageView emptyState1;
    TextView emptyState2;
    TextView emptyState3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd          =   findViewById(R.id.btnAddTask);
        btnLogOut       =   findViewById(R.id.btnLogOut);
        rvUtama         =   findViewById(R.id.rvMain);
        mDatabse        =   FirebaseDatabase.getInstance().getReference();

        emptyState1     =   findViewById(R.id.empty_state);
        emptyState2     =   findViewById(R.id.tvEmpty1);
        emptyState3     =   findViewById(R.id.tvEmpty2);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddAct.class));

            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        getData();
        showRecycler();


    }

    private void logOut() {

        startActivity(new Intent(this,LoginAct.class));
        FirebaseAuth.getInstance().signOut();
        finish();

    }


    // Method

    private void showRecycler() {
        rvUtama.setLayoutManager(new GridLayoutManager(this,2));
        rvUtama.setHasFixedSize(true);

    }

    private void getData() {


        listTugas   = new ArrayList<>();

        mDatabse.child("Task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTugas.clear();


                for (DataSnapshot snapshot  : dataSnapshot.getChildren()){
                    tugas = snapshot.getValue(Member.class);
                    tugas.setKey(snapshot.getKey());

                    listTugas.add(tugas);
                }

                adapter = new RecyclerAdapter(listTugas);
                rvUtama.setAdapter(adapter);

                Log.d("_cek", "isEmty: "+ listTugas.isEmpty());

                if (listTugas.isEmpty() ){

                    rvUtama.setVisibility(View.GONE);
                    emptyState1.setVisibility(View.VISIBLE);
                    emptyState2.setVisibility(View.VISIBLE);
                    emptyState3.setVisibility(View.VISIBLE);

                }else{

                    rvUtama.setVisibility(View.VISIBLE);
                    emptyState1.setVisibility(View.GONE);
                    emptyState2.setVisibility(View.GONE);
                    emptyState3.setVisibility(View.GONE);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"ooopps, something wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}


