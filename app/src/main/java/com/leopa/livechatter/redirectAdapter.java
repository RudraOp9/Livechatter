package com.leopa.livechatter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class redirectAdapter extends RecyclerView.Adapter<redirectAdapter.myViewHolder> {

    // data soureces
    private redirect[] listData;
    private ItemClickListener clickListener;

    public redirectAdapter(redirect[] listData) {
        this.listData = listData;
    }


    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    //2. View holder class
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView textView;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView100);
            this.textView = itemView.findViewById(R.id.textView6);

        }


        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.OnClick(view, getAdapterPosition());
                }
        }
    }

    //implementing the methods
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.redirect_recycle,parent,false);
        myViewHolder viewHolder = new myViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final redirect myListData = listData[position];
        holder.textView.setText(listData[position].getMsite());
        holder.imageView.setImageResource(listData[position].getMimg());
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }
}
