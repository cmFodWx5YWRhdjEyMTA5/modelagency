package com.modelagency.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.InfoItem;

import java.util.List;

public class ProfileInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<InfoItem> mItemList;
    private String type;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public ProfileInfoAdapter(Context context, List<InfoItem> itemList,String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textLabel,textValue;

        public MyViewHolder(View itemView){
            super(itemView);
            textLabel=itemView.findViewById(R.id.tv_label);
            textValue=itemView.findViewById(R.id.tv_value);
        }

        @Override
        public void onClick(View view) {

        }

    }

    public class MyViewEditHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textLabel;
        private EditText etValue;
        public MyViewEditHolder(View itemView){
            super(itemView);
            textLabel=itemView.findViewById(R.id.tv_label);
            etValue=itemView.findViewById(R.id.et_value);
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
                View v0 = inflater.inflate(R.layout.info_item_layout, parent, false);
                viewHolder = new MyViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.info_edit_item_layout, parent, false);
                viewHolder = new MyViewEditHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.info_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("editProfile")){
            return 1;
        }else{
            return 0;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            InfoItem item = mItemList.get(position);
            myViewHolder.textLabel.setText(item.getLabel());
            myViewHolder.textValue.setText(item.getValue());
        }else if(holder instanceof MyViewEditHolder){
            MyViewEditHolder myViewHolder = (MyViewEditHolder)holder;
            InfoItem item = mItemList.get(position);
            myViewHolder.textLabel.setText(item.getLabel());
         //   myViewHolder.textValue.setText(item.getValue());
        }
    }

    @Override
    public int getItemCount() {
        if(mItemList ==null)
            return 0;
        else
            return mItemList.size();
    }
}
