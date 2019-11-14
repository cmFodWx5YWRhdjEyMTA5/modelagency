package com.talentnew.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.talentnew.R;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.Album;
import com.talentnew.models.PortFolio;

import java.util.List;

public class PortFolioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Object> mItemList;
    private String type;
    private MyItemClickListener myItemClickListener;
    private MyItemLevelClickListener myItemLevelClickListener;

    public void setMyItemLevelClickListener(MyItemLevelClickListener myItemLevelClickListener) {
        this.myItemLevelClickListener = myItemLevelClickListener;
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public PortFolioAdapter(Context context, List<Object> itemList,String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textName;
        private RecyclerView recyclerView;
        private ImageView ivEdit,ivDelete;
        public MyRecyclerViewHolder(View itemView){
            super(itemView);
            textName=itemView.findViewById(R.id.tv_header);
            ivEdit=itemView.findViewById(R.id.iv_edit);
            ivDelete=itemView.findViewById(R.id.iv_delete);
            recyclerView=itemView.findViewById(R.id.recycler_view);

            ivEdit.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           if(view == ivEdit){
               myItemClickListener.onItemClicked(getAdapterPosition(),3);
           }else if(view == ivDelete){
               myItemClickListener.onItemClicked(getAdapterPosition(),4);
           }
        }

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
            rlAddPhoto.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PortFolio item = (PortFolio)mItemList.get(getAdapterPosition());
          if(type.equals("showProfile")){
              myItemLevelClickListener.onItemClicked(item.getPosition(),getAdapterPosition(),3);
          }else{
              if(view == rlAddPhoto){
                  myItemLevelClickListener.onItemClicked(item.getPosition(),getAdapterPosition(),0);
              }else{
                  if(item.isSelected()){
                      item.setSelected(false);
                      myItemLevelClickListener.onItemClicked(item.getPosition(),getAdapterPosition(),2);
                  }else{
                      item.setSelected(true);
                      myItemLevelClickListener.onItemClicked(item.getPosition(),getAdapterPosition(),1);
                  }
                  notifyItemChanged(getAdapterPosition());
              }

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
                View v0 = inflater.inflate(R.layout.recycle_view_with_header_layout, parent, false);
                viewHolder = new MyRecyclerViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.portfolio_item_layout, parent, false);
                viewHolder = new MyViewHolder(v1);
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
        Object ob = mItemList.get(position);
        if(ob instanceof Album)
        return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            PortFolio item = (PortFolio) mItemList.get(position);

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

                    Log.i("Base","image "+item.getImageUrl());

                    Glide.with(context)
                            .load(item.getImageUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
                            .error(R.drawable.model_2)
                            .into(myViewHolder.imageView);
                }
            }else{
                Log.i("Base","image "+item.getImageUrl());

                Glide.with(context)
                        .load(item.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .error(R.drawable.model_2)
                        .into(myViewHolder.imageView);
            }
        }else if(holder instanceof MyRecyclerViewHolder){
            MyRecyclerViewHolder myViewHolder = (MyRecyclerViewHolder)holder;
            Album album = (Album)mItemList.get(position);
            myViewHolder.textName.setText(album.getHeader());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false);
            myViewHolder.recyclerView.setLayoutManager(layoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            PortFolioAdapter itemAdapter=new PortFolioAdapter(context,album.getImageList(),type);
            itemAdapter.setMyItemClickListener(myItemClickListener);
            itemAdapter.setMyItemLevelClickListener(myItemLevelClickListener);
            myViewHolder.recyclerView.setAdapter(itemAdapter);
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
