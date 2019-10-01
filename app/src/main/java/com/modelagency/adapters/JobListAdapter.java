package com.modelagency.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.HomeListItem;
import com.modelagency.models.MyBlog;
import com.modelagency.models.MyJob;
import com.modelagency.models.MyModel;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class JobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<MyJob> mItemList;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public JobListAdapter(Context context, List<MyJob> itemList) {
        this.context = context;
        this.mItemList = itemList;
    }

    public class MyJobsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView textTitle,textLocation, textCloseDay;
        private ImageView imageView;
        private View rootView;

        public MyJobsListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            textLocation = itemView.findViewById(R.id.text_location);
            textCloseDay=itemView.findViewById(R.id.text_close);
            imageView=itemView.findViewById(R.id.image_view);
            rootView.setOnTouchListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageView){
             /*   final MyShop shop = (MyShop) mItemList.get(getAdapterPosition());
               ((ShopListActivity)context).showLargeImageDialog(shop, imageView);*/
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
                    myItemClickListener.onItemClicked(getAdapterPosition(),1);
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
                View v0 = inflater.inflate(R.layout.job_list_item_layout, parent, false);
                viewHolder = new MyJobsListViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.job_list_item_layout, parent, false);
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
            myViewHolder.textLocation.setText(item.getLocation());
            myViewHolder.textCloseDay.setText("Close date: "+item.getCloseDate());

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
                    .load(item.getLocalImage())
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
