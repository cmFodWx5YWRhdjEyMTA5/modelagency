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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.modelagency.R;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.HomeListItem;
import com.modelagency.models.MyBlog;
import com.modelagency.models.MyCourse;
import com.modelagency.models.MyModel;
import com.modelagency.utilities.Constants;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by suraj kumar singh on 09-03-2019.
 */

public class MyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Object> mItemList;
    private String type;
    private String flag;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int counter;


    public MyItemAdapter(Context context, List<Object> itemList, String type) {
        this.context = context;
        this.mItemList = itemList;
        this.type = type;
        sharedPreferences = context.getSharedPreferences(Constants.MYPREFERENCEKEY, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
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

    public class MyBlogsHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textHeader;
        public MyBlogsHeaderViewHolder(View itemView){
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class MyCourseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView textShopName,text_companyName, textAddress;
        private Button btn_more;
        private ImageView imageView;
        private View rootView;

        public MyCourseListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            textShopName=itemView.findViewById(R.id.text_name);
            text_companyName = itemView.findViewById(R.id.text_companyName);
            textAddress=itemView.findViewById(R.id.text_address);
            imageView=itemView.findViewById(R.id.image_view);
            btn_more =itemView.findViewById(R.id.btn_more);
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
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    public class MyBlogsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView textShopName,text_companyName, textAddress;
        private Button btn_more;
        private ImageView imageView;
        private View rootView;

        public MyBlogsListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            textShopName=itemView.findViewById(R.id.text_name);
            text_companyName = itemView.findViewById(R.id.text_companyName);
            textAddress=itemView.findViewById(R.id.text_address);
            imageView=itemView.findViewById(R.id.image_view);
            btn_more =itemView.findViewById(R.id.btn_more);
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
                View v0 = inflater.inflate(R.layout.header_item_type_1_layout, parent, false);
                viewHolder = new MyHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.header_item_type_2_layout, parent, false);
                viewHolder = new MyBlogsHeaderViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.list_item_type_4_layout, parent, false);
                viewHolder = new MyCourseListViewHolder(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.list_item_type_4_layout, parent, false);
                viewHolder = new MyBlogsListViewHolder(v3);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyHeaderViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("homeList")){
            Object item = mItemList.get(position);
            if(item instanceof HomeListItem){
                HomeListItem myItem = (HomeListItem) item;
                if(myItem.getType() == 0){
                    return 0;
                }else{
                    return 1;
                }
            }else if(item instanceof MyModel){
                return 2;
            }else if(item instanceof MyBlog){
                return 3;
            }else if(item instanceof MyCourse){
                return 2;
            } else{
                return 3;
            }
        } else{
            return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MyHeaderViewHolder){
            HomeListItem item = (HomeListItem) mItemList.get(position);
            MyHeaderViewHolder myViewHolder = (MyHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
            myViewHolder.recyclerView.setLayoutManager(layoutManagerHomeMenu);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            MyItemAdapter myItemAdapter=new MyItemAdapter(context,item.getItemList(),"homeList");
            myViewHolder.recyclerView.setAdapter(myItemAdapter);
        }else if(holder instanceof MyBlogsHeaderViewHolder){
            HomeListItem item = (HomeListItem) mItemList.get(position);
            MyBlogsHeaderViewHolder myViewHolder = (MyBlogsHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
        }else if(holder instanceof MyCourseListViewHolder){
                MyCourse item = (MyCourse) mItemList.get(position);
                MyCourseListViewHolder myViewHolder = (MyCourseListViewHolder)holder;
                myViewHolder.textShopName.setText(item.getName());
                myViewHolder.text_companyName.setText(item.getSection());
                myViewHolder.textAddress.setText(item.getTitle());
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.dontTransform();
                // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
                // requestOptions.centerCrop();
                requestOptions.skipMemoryCache(false);

                Glide.with(context)
                        .load(item.getImage())
                        .apply(requestOptions)
                        .error(R.drawable.default_pic)
                        .into(myViewHolder.imageView);
            }
            else if(holder instanceof MyBlogsListViewHolder){
                MyBlog item = (MyBlog) mItemList.get(position);
                MyBlogsListViewHolder myViewHolder = (MyBlogsListViewHolder)holder;
                myViewHolder.textShopName.setText(item.getName());
                myViewHolder.text_companyName.setText(item.getCompany());
                myViewHolder.textAddress.setText(item.getAddress());
                if(position==mItemList.size()-1)
                    myViewHolder.btn_more.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.dontTransform();
                // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
                // requestOptions.centerCrop();
                requestOptions.skipMemoryCache(false);

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
