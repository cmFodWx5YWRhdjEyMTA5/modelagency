package com.modelagency.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.PortFolio;

import java.util.List;

public class PortFolioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<PortFolio> mItemList;
    private String type;
    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public PortFolioAdapter(Context context, List<PortFolio> itemList,String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private TextView textName;
        private ImageView imageView;
        private RelativeLayout rlAddPhoto,rlSelected;
        public MyViewHolder(View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_image);
            rlAddPhoto=itemView.findViewById(R.id.rl_add_photo);
            rlSelected=itemView.findViewById(R.id.rl_selected);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
          if(type.equals("showProfile")){
              myItemClickListener.onItemClicked(getAdapterPosition(),3);
          }else{
              PortFolio item = mItemList.get(getAdapterPosition());
              if(item.isSelected()){
                  item.setSelected(false);
                  myItemClickListener.onItemClicked(getAdapterPosition(),2);
              }else{
                  item.setSelected(true);
                  myItemClickListener.onItemClicked(getAdapterPosition(),1);
              }
              notifyItemChanged(getAdapterPosition());
          }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.portfolio_item_layout, parent, false);
                viewHolder = new MyViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.portfolio_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
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
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            PortFolio item = mItemList.get(position);

            if(type.equals("editProfile")){
                if(position == 0){
                    myViewHolder.rlAddPhoto.setVisibility(View.VISIBLE);
                    myViewHolder.imageView.setVisibility(View.GONE);
                }else {
                    myViewHolder.rlAddPhoto.setVisibility(View.GONE);
                    myViewHolder.imageView.setVisibility(View.VISIBLE);

                    if (item.isSelected()) {
                        myViewHolder.rlSelected.setVisibility(View.VISIBLE);
                        myViewHolder.imageView.setBackgroundResource(R.drawable.accent_stroke_background);
                    } else {
                        myViewHolder.rlSelected.setVisibility(View.GONE);
                        myViewHolder.imageView.setBackgroundResource(0);
                    }
                }
            }
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
