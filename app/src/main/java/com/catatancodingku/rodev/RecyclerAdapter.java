package com.catatancodingku.rodev;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Member> listTugas;



    public RecyclerAdapter(ArrayList<Member> listTugas) {

        this.listTugas = listTugas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final String user      =   listTugas.get(position).getUser();
        final String judul     =   listTugas.get(position).getJudul();
        final String deskripsi =   listTugas.get(position).getDeskripsi();
        final String key       =   listTugas.get(position).getKey();
        final boolean status   =   listTugas.get(position).isStatus();

        holder.tvUser.setText(user);
        holder.tvJudul.setText(judul);
        holder.tvDeskripsi.setText(deskripsi);
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Vuser          = listTugas.get(holder.getAdapterPosition()).getUser();
                String Vjudul         = listTugas.get(holder.getAdapterPosition()).getJudul();
                String Vdeskripsi     = listTugas.get(holder.getAdapterPosition()).getDeskripsi();
                String Vkey           = listTugas.get(holder.getAdapterPosition()).getKey();
                boolean Vstatus       = listTugas.get(holder.getAdapterPosition()).isStatus();

                Intent pindah = new Intent(holder.itemView.getContext(),DetailTemplateAct.class);
                pindah.putExtra("USER",Vuser);
                pindah.putExtra("JUDUL",Vjudul);
                pindah.putExtra("DESKRIPSI",Vdeskripsi);
                pindah.putExtra("KEY",Vkey);
                pindah.putExtra("STATUS",Vstatus);
                view.getContext().startActivity(pindah);

            }

        });

        if(status == true){
            holder.ivCeklis.setVisibility(View.VISIBLE);
            holder.ivUnCeklis.setVisibility(View.GONE);
        }


        holder.ivUnCeklis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivCeklis.setVisibility(View.VISIBLE);
                holder.ivUnCeklis.setVisibility(View.GONE);

                listTugas.get(position).setStatus(true);
                boolean upStatus = listTugas.get(position).isStatus();
                Member updateTask = new Member(user,judul,deskripsi,upStatus);
                holder.mDatabase.child("Task")
                        .child(key)
                        .setValue(updateTask);
                Toast.makeText(holder.itemView.getContext(),"Tugas Selesai",Toast.LENGTH_SHORT).show();
            }


        });

        holder.ivCeklis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivCeklis.setVisibility(View.GONE);
                holder.ivUnCeklis.setVisibility(View.VISIBLE);

                listTugas.get(position).setStatus(false);
                boolean upStatus = listTugas.get(position).isStatus();
                Member updateTask = new Member(user,judul,deskripsi,upStatus);
                holder.mDatabase.child("Task")
                        .child(key)
                        .setValue(updateTask);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listTugas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvJudul;
        TextView tvDeskripsi;
        TextView tvUser;
        Button btnMore;
        ImageView ivCeklis;
        ImageView ivUnCeklis;
        DatabaseReference mDatabase;





        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul         = itemView.findViewById(R.id.tvItemJudul);
            tvDeskripsi     = itemView.findViewById(R.id.tvItemKeterangan);
            tvUser          = itemView.findViewById(R.id.tvItemUser);
            btnMore         = itemView.findViewById(R.id.btnMore);
            ivCeklis        = itemView.findViewById(R.id.ivCheck);
            ivUnCeklis      = itemView.findViewById(R.id.ivUncheck);
            mDatabase       = FirebaseDatabase.getInstance().getReference();

        }



    }

}
