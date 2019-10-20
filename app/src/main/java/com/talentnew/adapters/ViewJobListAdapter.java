package com.talentnew.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.talentnew.R;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.MyJob;
import com.talentnew.utilities.Utility;

import java.util.List;

public class ViewJobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<MyJob> mItemList;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public ViewJobListAdapter(Context context, List<MyJob> itemList) {
        this.context = context;
        this.mItemList = itemList;
    }

    public class MyJobsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView textTitle,textCloseDay, text_view_count, text_application_count;
        private LinearLayout linear_applicattion, linear_view;
        private ImageView imageView;
        private View rootView;

        public MyJobsListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            text_view_count = itemView.findViewById(R.id.text_view_count);
            text_application_count = itemView.findViewById(R.id.text_application_count);
            linear_applicattion = itemView.findViewById(R.id.linear_applicattion);
            linear_view = itemView.findViewById(R.id.linear_view);
            textCloseDay=itemView.findViewById(R.id.text_close);
            imageView=itemView.findViewById(R.id.image_view);
            linear_applicattion.setOnClickListener(this);
            linear_view.setOnClickListener(this);
            //rootView.setOnTouchListener(this);
            //imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == linear_applicattion){
                myItemClickListener.onItemClicked(getAdapterPosition(),1);
            }else if(view == linear_view){
               // myItemClickListener.onItemClicked(getAdapterPosition(),2);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("Adapter","onPressDown");
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                   /* MyShop shop = (MyShop) mItemList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ShopProductListActivity.class);
                    context.startActivity(intent);*/
                    zoomAnimation(false,rootView);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.view_job_list_item_layout, parent, false);
                viewHolder = new MyJobsListViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.view_job_list_item_layout, parent, false);
                viewHolder = new MyJobsListViewHolder(v);
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
        if(holder instanceof MyJobsListViewHolder){
            MyJob item = mItemList.get(position);
            MyJobsListViewHolder myViewHolder = (MyJobsListViewHolder)holder;
            myViewHolder.textTitle.setText(item.getTitle());
            myViewHolder.textCloseDay.setText("Close date: "+item.getCloseDate());
            myViewHolder.text_application_count.setText(""+item.getApplicationCount());
            myViewHolder.text_view_count.setText(""+item.getViewCount());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.dontTransform();
            requestOptions.override(Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size),
                    context), Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size), context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Log.i("Adapter","dimen "+(int)context.getResources().getDimension(R.dimen.job_list_image_size)+" "+
                    (int)context.getResources().getDimensionPixelOffset(R.dimen.job_list_image_size)+" "+
                    (int)context.getResources().getDimensionPixelSize(R.dimen.job_list_image_size));

            Glide.with(context)
                    .load(item.getImageUrl())
                    .apply(requestOptions)
                    .error(R.drawable.default_pic)
                    .into(myViewHolder.imageView);
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
