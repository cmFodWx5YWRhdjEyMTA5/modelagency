package com.modelagency.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.modelagency.R;
import com.modelagency.activities.common.BaseActivity;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.JobListActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.adapters.CourseListAdapter;
import com.modelagency.adapters.MyItemAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.HomeListItem;
import com.modelagency.models.MyCourse;
import com.modelagency.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends YouTubeBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> myItemList;
    private MyCourse course;
    private YouTubePlayerView youTubePlayerView;
    private static YouTubePlayer youTubePlayer;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("Courses");
        init();
        initFooter(this, 2);
    }

    private void init(){
        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        course = (MyCourse) getIntent().getSerializableExtra("course");
        TextView tv_course_section = findViewById(R.id.tv_course_section);
        tv_course_section.setText(course.getSection());
        TextView tv_course_titile = findViewById(R.id.tv_course_titile);
        tv_course_titile.setText(course.getTitle());

        myItemList = new ArrayList<>();
        getItemList();
        MyCourse course = (MyCourse)((HomeListItem)myItemList.get(0)).getItemList().get(0);
        initYoutubePlayer(course.getVideoUrl());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new MyItemAdapter(this,myItemList, "homeList");
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initYoutubePlayer(final String myurl){
        youTubePlayerView = findViewById(R.id.youtube_player);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                CourseDetailsActivity.youTubePlayer = youTubePlayer;
                youTubePlayer.loadVideo(myurl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        youTubePlayerView.initialize("AIzaSyCF8H9TnCyaloHJU4dMD0LGbkfEjWct_6A", onInitializedListener);
    }

    private void getItemList(){
        MyCourse item = null;
        HomeListItem myItem;
        for(int j=0;j<3;j++) {
            myItem = new HomeListItem();
            myItem.setTitle("Section "+(j+1) +" : Modelling 101");
            myItem.setType(0);
            myItemList.add(myItem);
            List<Object> listItems = new ArrayList<>();
            for (int i = 0; i < 5-j; i++) {
                item = new MyCourse();
                item.setName("Introduction : 10"+i);
                item.setDuration("Video - 03:43 mins");
                item.setVideoUrl("1CSelx6UalY");
                item.setProgress(50 * (i));
                item.setImage(R.drawable.udemy);
                if(i==0 && j==0)
                    item.setPlaying(true);
                listItems.add(item);
            }
            myItem.setItemList(listItems);
        }
        int size  = ((HomeListItem)myItemList.get(1)).getItemList().size();
        Log.d("size ", ""+size);
    }

    @Override
    public void onItemClicked(int position, int type) {
        Toast.makeText(this, ((MyCourse)myItemList.get(position)).getVideoUrl(), Toast.LENGTH_SHORT).show();
        MyCourse course = ((MyCourse)myItemList.get(position));
        initYoutubePlayer("1CSelx6UalY");
    }

    public void onItemClick(MyCourse course, int type) {
       // Toast.makeText(this, course.getVideoUrl(), Toast.LENGTH_SHORT).show();
        if(youTubePlayerView!=null)
        youTubePlayer.loadVideo("1CSelx6UalY");
    }


    public void initFooter(final Context context, int type) {

        int backColor = getResources().getColor(R.color.white);
        int textColor = getResources().getColor(R.color.primary_text_color);
        int colorTheme = getResources().getColor(R.color.bottom_nav_back_color);

        findViewById(R.id.linear_footer).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_1).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_2).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_3).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_4).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_5).setBackgroundColor(backColor);

        RelativeLayout relativeLayoutFooter1 = findViewById(R.id.relative_footer_1);
        RelativeLayout relativeLayoutFooter2 = findViewById(R.id.relative_footer_2);
        RelativeLayout relativeLayoutFooter3 = findViewById(R.id.relative_footer_3);
        RelativeLayout relativeLayoutFooter4 = findViewById(R.id.relative_footer_4);
        RelativeLayout relativeLayoutFooter5 = findViewById(R.id.relative_footer_5);

        ImageView imageViewFooter1 = findViewById(R.id.image_footer_1);
        ImageView imageViewFooter2 = findViewById(R.id.image_footer_2);
        ImageView imageViewFooter3 = findViewById(R.id.image_footer_3);
        ImageView imageViewFooter4 = findViewById(R.id.image_footer_4);
        ImageView imageViewFooter5 = findViewById(R.id.image_footer_5);

        TextView textViewFooter1 = findViewById(R.id.text_footer_1);
        TextView textViewFooter2 = findViewById(R.id.text_footer_2);
        TextView textViewFooter3 = findViewById(R.id.text_footer_3);
        TextView textViewFooter4 = findViewById(R.id.text_footer_4);
        TextView textViewFooter5 = findViewById(R.id.text_footer_5);

        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            textViewFooter1.setText("Models");
            textViewFooter2.setText("Post Job");
            textViewFooter3.setText("Course");
            textViewFooter4.setText("Boost");
            textViewFooter5.setText("Profile");
        }

        View view1 = findViewById(R.id.separator_footer_1);
        View view2 = findViewById(R.id.separator_footer_2);
        View view3 = findViewById(R.id.separator_footer_3);
        View view4 = findViewById(R.id.separator_footer_4);
        View view5 = findViewById(R.id.separator_footer_5);

        switch (type) {
            case 0:
                imageViewFooter1.setColorFilter(colorTheme);
                textViewFooter1.setTextColor(colorTheme);
                view1.setBackgroundColor(colorTheme);
                break;
            case 1:
                imageViewFooter2.setColorFilter(colorTheme);
                textViewFooter2.setTextColor(colorTheme);
                view2.setBackgroundColor(colorTheme);
                break;
            case 2:
                imageViewFooter3.setColorFilter(colorTheme);
                textViewFooter3.setTextColor(colorTheme);
                view3.setBackgroundColor(colorTheme);
                break;
            case 3:
                imageViewFooter4.setColorFilter(colorTheme);
                textViewFooter4.setTextColor(colorTheme);
                view4.setBackgroundColor(colorTheme);
                break;
            case 4:
                imageViewFooter5.setColorFilter(colorTheme);
                textViewFooter5.setTextColor(colorTheme);
                view5.setBackgroundColor(colorTheme);
                break;
        }

        relativeLayoutFooter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof ModelListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(CourseDetailsActivity.this, ModelListActivity.class);
                        startActivity(intent);
                    }
                }else{
                    if (context instanceof JobListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(CourseDetailsActivity.this, JobListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        relativeLayoutFooter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (context instanceof CategoryListActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(BaseActivity.this, CategoryListActivity.class);
                    startActivity(intent);
                }*/
            }
        });
        relativeLayoutFooter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof CourseListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(CourseDetailsActivity.this, CourseListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        relativeLayoutFooter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (context instanceof SearchActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    //  DialogAndToast.showToast("Profile clicked in Search ",BaseActivity.this);
                    Intent intent = new Intent(BaseActivity.this, SearchActivity.class);
                    startActivity(intent);
                }*/
            }
        });

        relativeLayoutFooter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ProfileActivity) {
                    //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                } else {
                    Intent intent = new Intent(CourseDetailsActivity.this, ProfileActivity.class);
                    intent.putExtra("flag","model");
                    startActivity(intent);
                }
            }
        });

    }

}
