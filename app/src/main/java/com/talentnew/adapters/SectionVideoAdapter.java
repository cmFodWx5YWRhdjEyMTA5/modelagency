package com.talentnew.adapters;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talentnew.R;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.SectionVideo;
import com.talentnew.utilities.Constants;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SectionVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<SectionVideo> mItemList;
    private String type;
    private String flag;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int playingIndex, playigSection;


    public SectionVideoAdapter(Context context, List<SectionVideo> itemList, String type) {
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

    public void setPlayigSection(int playigSection) {
        this.playigSection = playigSection;
    }

    public void setFlag(String flag){
        this.flag = flag;
    }


    public class MyCourseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        private TextView textCourseNumber, textCourseName, textCourseDuration;
        private Button btn_more;
        private View rootView;

        public MyCourseListViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            textCourseName =itemView.findViewById(R.id.text_name);
            textCourseDuration = itemView.findViewById(R.id.text_duration);
            textCourseNumber = itemView.findViewById(R.id.text_cours_number);
            btn_more =itemView.findViewById(R.id.btn_more);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
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
                    SectionVideo course = (SectionVideo) mItemList.get(getAdapterPosition());
                    zoomAnimation(false,rootView);
                    myItemClickListener.onItemClicked(course.getParentPosition(),getAdapterPosition(),1);
                   // ((CourseDetailsActivity)context).onItemClick(course, 1);

                   // course.setPlaying(true);
                  //  ((SectionVideo) mItemList.get(playingIndex)).setPlaying(false);
                 //   Log.d("previous playing "+playingIndex, "current playing "+getAdapterPosition());
                 //   notifyItemChanged(getAdapterPosition());
                //    notifyItemChanged(playingIndex);
                //    playingIndex = getAdapterPosition();
                    //myItemClickListener.onItemClicked(getAdapterPosition(),1);
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
                View v0 = inflater.inflate(R.layout.list_item_type_4_layout, parent, false);
                viewHolder = new MyCourseListViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_type_4_layout, parent, false);
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
            SectionVideo item =  mItemList.get(position);
            Log.i("VideoAdapter","Item changed "+position+" parent "+item.getParentPosition());
            MyCourseListViewHolder myViewHolder = (MyCourseListViewHolder)holder;
            myViewHolder.textCourseName.setText(item.getName());
            myViewHolder.textCourseDuration.setText(item.getDuration());
            myViewHolder.textCourseNumber.setText(""+(position+1));
            if(item.isPlaying()){
                myViewHolder.textCourseName.setTextColor(context.getResources().getColor(R.color.blue600));
                myViewHolder.textCourseDuration.setTextColor(context.getResources().getColor(R.color.blue600));
                myViewHolder.textCourseNumber.setTextColor(context.getResources().getColor(R.color.blue600));
            }else {
                myViewHolder.textCourseName.setTextColor(context.getResources().getColor(R.color.black));
                myViewHolder.textCourseDuration.setTextColor(context.getResources().getColor(R.color.black));
                myViewHolder.textCourseNumber.setTextColor(context.getResources().getColor(R.color.black));
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
