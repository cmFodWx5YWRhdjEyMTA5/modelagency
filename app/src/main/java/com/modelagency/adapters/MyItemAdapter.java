package com.modelagency.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.models.MyItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    public MyItemAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;
        private CircleImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_header);
            textDesc=itemView.findViewById(R.id.text_desc);
            image=itemView.findViewById(R.id.image_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v10 = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyViewHolder(v10);
                break;
            case 1:
                View v0 = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("homeList")){
            return 1;
        }else if(type.equals("countries")){
            return 2;
        } else{
            return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          if(holder instanceof MyViewHolder){
              MyItem item = (MyItem) itemList.get(position);
              MyViewHolder myViewHolder = (MyViewHolder)holder;
              myViewHolder.textHeader.setText(item.getHeader());
              myViewHolder.textDesc.setText(item.getDesc());
          }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
