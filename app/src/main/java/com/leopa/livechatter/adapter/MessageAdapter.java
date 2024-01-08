package com.leopa.livechatter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leopa.livechatter.R;
import com.leopa.livechatter.model.MessageReceive;
import com.leopa.livechatter.model.MessageSend;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ONE = 1;
    private static final int TYPE_TWO = 2;

    ArrayList<Object> combinedList = new ArrayList<>();



    public MessageAdapter(ArrayList<Object> combinedList) {
        this.combinedList = combinedList;
    }

    @Override
    public int getItemViewType(int position) {
        if (combinedList.get(position) instanceof MessageReceive) {
            return TYPE_ONE;
        } else {
            return TYPE_TWO;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case TYPE_ONE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_receive, parent, false);
                return new ViewHolderTypeOne(view);
            case TYPE_TWO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_send, parent, false);
                return new ViewHolderTypeTwo(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder.getItemViewType() == TYPE_ONE) {
            MessageReceive firstModel = (MessageReceive) combinedList.get(position);
            ((ViewHolderTypeOne) holder).setData(firstModel);
        } else {
            MessageSend secondModel = (MessageSend) combinedList.get(position);
            ((ViewHolderTypeTwo) holder).setData(secondModel);
        }

    }

    @Override
    public int getItemCount() {
        return combinedList == null ? 0 : combinedList.size();
    }

    public static class ViewHolderTypeOne extends RecyclerView.ViewHolder {
        // Your views for the first layout

        TextView textView;
        public ViewHolderTypeOne(View itemView) {
            super(itemView);
            // Initialize your views
            textView = itemView.findViewById(R.id.sendMsgText2);
        }

        public void setData(MessageReceive data) {
            // Bind data to your views
            textView.setText(data.getReceiveMsg());
        }
    }

    public static class ViewHolderTypeTwo extends RecyclerView.ViewHolder {
        // Your views for the second layout
        TextView textView;

        public ViewHolderTypeTwo(View itemView) {
            super(itemView);
            // Initialize your views
            textView = itemView.findViewById(R.id.sendMsgText);

        }

        public void setData(MessageSend data) {
            // Bind data to your views
            textView.setText(data.getSendMsg());
        }
    }
}


