package com.modelagency.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyCourse;
import com.modelagency.models.MyModel;
import com.modelagency.utilities.Utility;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<MyCourse> mItemList;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public CourseListAdapter(Context context, List<MyCourse> itemList) {
        this.context = context;
        this.mItemList = itemList;
    }

    public class MyCourseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView text_section,text_title,text_progress_value;
        private View view_progress;
        private ImageView imageView;
        private View rootView;
        private LinearLayout linear_view;
        private int width;


        public MyCourseListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            text_section=itemView.findViewById(R.id.text_section);
            text_title = itemView.findViewById(R.id.text_title);
            text_progress_value = itemView.findViewById(R.id.text_progress_value);
            linear_view = itemView.findViewById(R.id.linear_view);
            view_progress = itemView.findViewById(R.id.view_progress);
            imageView=itemView.findViewById(R.id.image_pic);
            rootView.setOnTouchListener(this);
        }

        private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    MyCourseListViewHolder.this.linear_view.getViewTreeObserver().removeGlobalOnLayoutListener(MyCourseListViewHolder.this.onGlobalLayoutListener);
                } else {
                    MyCourseListViewHolder.this.linear_view.getViewTreeObserver().removeOnGlobalLayoutListener(MyCourseListViewHolder.this.onGlobalLayoutListener);
                }
                width = linear_view.getWidth();
                MyCourse item = mItemList.get(getAdapterPosition());
                Log.d("progressWidth ", getCourseProgress(item.getProgress(), width)+"");
                item.setProgressWidth(getCourseProgress(item.getProgress(), width));
                notifyItemChanged(getAdapterPosition());
            }
        };

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
                View v0 = inflater.inflate(R.layout.course_list_item_layout, parent, false);
                viewHolder = new MyCourseListViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.course_list_item_layout, parent, false);
                viewHolder = new MyCourseListViewHolder(v);
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
        if(holder instanceof MyCourseListViewHolder){
            MyCourse item = mItemList.get(position);
            MyCourseListViewHolder myViewHolder = (MyCourseListViewHolder)holder;
            myViewHolder.text_section.setText(item.getSection());
            myViewHolder.text_title.setText(item.getTitle());
            if(item.getProgress()>0){
                myViewHolder.view_progress.setVisibility(View.VISIBLE);
                myViewHolder.text_progress_value.setText(item.getProgress() +" % complete");
                myViewHolder.view_progress.getLayoutParams().width = item.getProgressWidth();
                if(myViewHolder.width ==0)
                myViewHolder.linear_view.getViewTreeObserver().addOnGlobalLayoutListener(myViewHolder.onGlobalLayoutListener);

            }else myViewHolder.view_progress.setVisibility(View.GONE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.dontTransform();
            //requestOptions.override(Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size),
             //       context), Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size), context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Log.i("Adapter","dimen "+(int)context.getResources().getDimension(R.dimen.job_list_image_size)+" "+
                    (int)context.getResources().getDimensionPixelOffset(R.dimen.job_list_image_size)+" "+
                    (int)context.getResources().getDimensionPixelSize(R.dimen.job_list_image_size));

            Glide.with(context)
                    .load(item.getImage())
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

    private int getCourseProgress(int value, int totalWidth){
        Log.d("totalWidth ", ""+totalWidth);
        return ((value * totalWidth) / 100);
    }
}
