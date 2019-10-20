package com.talentnew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talentnew.R;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.Boost;

import java.util.List;

public class BoostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Object> mItemList;
    private String type;
    private MyItemClickListener myItemClickListener;


    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public BoostAdapter(Context context, List<Object> itemList,String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
    }

    public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_header,tv_pay;
        private RecyclerView recyclerView;
        public MyRecyclerViewHolder(View itemView){
            super(itemView);
            tv_header=itemView.findViewById(R.id.tv_header);
            tv_pay=itemView.findViewById(R.id.tv_pay);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View view) {

        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textName;
        public MyViewHolder(View itemView){
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
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
                View v0 = inflater.inflate(R.layout.boost_item_layout, parent, false);
                viewHolder = new MyRecyclerViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.simple_item_layout, parent, false);
                viewHolder = new MyViewHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.simple_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Object ob = mItemList.get(position);
        if(ob instanceof Boost)
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            myViewHolder.textName.setText((String)mItemList.get(position));
            myViewHolder.textName.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }else if(holder instanceof MyRecyclerViewHolder){
            MyRecyclerViewHolder myViewHolder = (MyRecyclerViewHolder)holder;
            Boost item = (Boost) mItemList.get(position);
            myViewHolder.tv_header.setText(item.getHeader());
            myViewHolder.tv_pay.setText(item.getPay());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(layoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            BoostAdapter itemAdapter=new BoostAdapter(context,item.getItemList(),type);
            itemAdapter.setMyItemClickListener(myItemClickListener);
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
