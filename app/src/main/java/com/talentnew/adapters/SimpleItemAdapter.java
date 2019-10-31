package com.talentnew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talentnew.R;
import com.talentnew.interfaces.MyItemClickListener;

import java.util.List;

public class SimpleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> itemList;
    private Context context;
    private String type;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public SimpleItemAdapter(Context context, List<String> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;


        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myItemClickListener.onItemClicked(getAdapterPosition(),2);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.simple_center_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.simple_center_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHomeHeaderViewHolder){
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(itemList.get(position));
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
