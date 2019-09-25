package com.modelagency.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.Genre;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Genre> mItemList;
    private String type;
    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public GenresAdapter(Context context, List<Genre> itemList,String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
    }

    public class MyGenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textName;

        public MyGenreViewHolder(View itemView){
            super(itemView);
            textName=itemView.findViewById(R.id.tv_name);

            if(type.equals("editProfile"))
            textName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Genre item = mItemList.get(getAdapterPosition());
            if(item.isSelected()){
                item.setSelected(false);
            }else{
                item.setSelected(true);
            }
            notifyItemChanged(getAdapterPosition());
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.genre_item_layout, parent, false);
                viewHolder = new MyGenreViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.genre_item_layout, parent, false);
                viewHolder = new MyGenreViewHolder(v);
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
        if(holder instanceof MyGenreViewHolder){
            MyGenreViewHolder myViewHolder = (MyGenreViewHolder)holder;
            Genre item = mItemList.get(position);
            myViewHolder.textName.setText(item.getName());

            if(type.equals("editProfile")){
                if(item.isSelected()){
                    myViewHolder.textName.setBackgroundResource(R.drawable.accent_solid_round_corner_background);
                    myViewHolder.textName.setTextColor(context.getResources().getColor(R.color.white));
                }else{
                    myViewHolder.textName.setBackgroundResource(R.drawable.grey_stroke_white_round_corner_background);
                    myViewHolder.textName.setTextColor(context.getResources().getColor(R.color.secondary_text_color));
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
