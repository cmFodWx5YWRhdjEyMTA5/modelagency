package com.talentnew.activities.agency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.talentnew.R;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.adapters.SectionAdapter;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.CourseSection;
import com.talentnew.models.MyCourse;
import com.talentnew.models.SectionVideo;
import com.talentnew.utilities.Constants;

import java.util.List;

public class CourseDetailsActivity extends YouTubeBaseActivity implements MyItemLevelClickListener {

    private RecyclerView recyclerView;
    private SectionAdapter sectionAdapter;
    private List<CourseSection> myItemList;
    private MyCourse course;
    private YouTubePlayerView youTubePlayerView;
    private static YouTubePlayer youTubePlayer;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    private int parentPosition,position;

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
        myItemList = course.getSectionList();
        myItemList.get(0).getSectionVideoList().get(0).setPlaying(true);
        initYoutubePlayer(myItemList.get(0).getSectionVideoList().get(0).getVideoUrl().split("=")[1]);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sectionAdapter =new SectionAdapter(this,myItemList, "homeList");
        sectionAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(sectionAdapter);
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


    @Override
    public void onItemClicked(int parentPosition, int position, int type) {

        Log.i("CourseDetail","parentPosition "+parentPosition+" position "+position+" type "+type);
        Log.i("CourseDetail","parentPosition "+this.parentPosition+" position "+this.position+" type "+type);


        CourseSection preCourse = myItemList.get(this.parentPosition);
        SectionVideo preSectionVideo = preCourse.getSectionVideoList().get(this.position);
        preSectionVideo.setPlaying(false);
        sectionAdapter.notifyItemChanged(this.parentPosition);

       // adapter.notifyItemChanged(position);

        this.parentPosition = parentPosition;
        this.position = position;

        CourseSection course = myItemList.get(parentPosition);
        SectionVideo sectionVideo = course.getSectionVideoList().get(position);
        sectionVideo.setPlaying(true);
        sectionAdapter.notifyItemChanged(parentPosition);


        String url = sectionVideo.getVideoUrl().split("=")[1];
        Log.i("CourseDetail","url "+url);
        youTubePlayer.loadVideo(url);
    }

    public void onItemClick(SectionVideo course, int type) {
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
                        intent.putExtra("flag","home");
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
