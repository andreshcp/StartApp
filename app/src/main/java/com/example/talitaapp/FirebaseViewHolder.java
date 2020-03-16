package com.example.talitaapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public TextView titleN, descriptionN;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        titleN = itemView.findViewById(R.id.title_name);
        descriptionN = itemView.findViewById(R.id.description_name);

    }
}
