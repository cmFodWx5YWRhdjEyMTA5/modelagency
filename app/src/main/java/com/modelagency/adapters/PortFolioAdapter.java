package com.modelagency.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public class MyGenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //private TextView textName;
        private ImageView imageView;
        public MyGenreViewHolder(View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_image);

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
                View v0 = inflater.inflate(R.layout.portfolio_item_layout, parent, false);
                viewHolder = new MyGenreViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.portfolio_item_layout, parent, false);
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
            PortFolio item = mItemList.get(position);
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
