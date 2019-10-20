package com.talentnew.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talentnew.R;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.CourseSection;
import com.talentnew.utilities.Constants;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by suraj kumar singh on 09-03-2019.
 */

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<CourseSection> mItemList;
    private String type;
    private String flag;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int playingIndex, playigSection;


    public SectionAdapter(Context context, List<CourseSection> itemList, String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
        sharedPreferences = context.getSharedPreferences(Constants.MYPREFERENCEKEY, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private MyItemLevelClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemLevelClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }


    public void setFlag(String flag){
        this.flag = flag;
    }

    public class MyHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       private TextView textHeader;
        private RecyclerView recyclerView;
        public MyHeaderViewHolder(View itemView){
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.header_item_type_1_layout, parent, false);
                viewHolder = new MyHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.header_item_type_1_layout, parent, false);
                viewHolder = new MyHeaderViewHolder(v);
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
            if(holder instanceof MyHeaderViewHolder){
            CourseSection item = mItemList.get(position);
            MyHeaderViewHolder myViewHolder = (MyHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(layoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            SectionVideoAdapter myItemAdapter=new SectionVideoAdapter(context,item.getSectionVideoList(),"homeList");
            myItemAdapter.setMyItemClickListener(myItemClickListener);
            myViewHolder.recyclerView.setAdapter(myItemAdapter);
        }
    }

    @Override
    public int getItemCount() {
        if(mItemList ==null)
            return 0;
        else
            return mItemList.size();
    }

    private void zoomAnimation(boolean zoomOut,View view){
        if(zoomOut){
            Animation aniSlide = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }else{
            Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.zoom_in);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }
    }

    private int getTvColor(int position){
        int[] tvColor={context.getResources().getColor(R.color.light_blue500),
                context.getResources().getColor(R.color.yellow500),context.getResources().getColor(R.color.green500),
                context.getResources().getColor(R.color.orange500),context.getResources().getColor(R.color.red_500),
                context.getResources().getColor(R.color.teal_500),context.getResources().getColor(R.color.cyan500),
                context.getResources().getColor(R.color.deep_orange500),context.getResources().getColor(R.color.blue500),
                context.getResources().getColor(R.color.purple500),context.getResources().getColor(R.color.amber500),
                context.getResources().getColor(R.color.light_green500)};

        return tvColor[position];
    }
}
