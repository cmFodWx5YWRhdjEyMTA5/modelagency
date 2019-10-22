package com.talentnew.activities.agency;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.adapters.CourseListAdapter;
import com.talentnew.adapters.ModelListAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.CourseSection;
import com.talentnew.models.HomeListItem;
import com.talentnew.models.MyCourse;
import com.talentnew.models.MyModel;
import com.talentnew.models.SectionVideo;
import com.talentnew.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private CourseListAdapter myItemAdapter;
    private List<MyCourse> myItemList;
    private int sectionCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
            initFooter(this, 2);
        }else{
            initFooter(this, 1);
        }

        setToolbarDetails(this);
        init();
    }

    private void init(){
        myItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new CourseListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        MyCourse item = null;
        for(int i=0; i<2; i++){
            item = new MyCourse();
            item.setSection("Modeling 101: Fundamental of Modeling");
            item.setTitle("Academy of Film Fashion and Design");
            item.setProgress(50*(i));
            item.setImage(R.drawable.udemy);
          //  myItemList.add(item);
        }

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("limit", ""+limit);
        params.put("offset", ""+offset);
        String url = getResources().getString(R.string.url)+Constants.GET_COURSES;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getCourses");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if(apiName.equals("getCourses")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int len = jsonArray.length();
                    JSONObject dataObject = null,sectionObject,sectionVideoObject;
                    int tempCourseId = 0,tempSectionId = 0;
                    MyCourse myCourse = null;
                    CourseSection section = null;
                    SectionVideo sectionVideo = null;
                    List<CourseSection> sectionList = null;
                    List<SectionVideo> sectionVideoList = null;
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        if(tempCourseId != dataObject.getInt("id")){
                            tempCourseId = dataObject.getInt("id");
                            myCourse = new MyCourse();
                            myCourse.setId(tempCourseId);
                            myCourse.setSection(dataObject.getJSONObject("courseSection").getString("description"));
                            myCourse.setTitle(dataObject.getString("title"));
                            myCourse.setProgress(50*(i));
                            myCourse.setImage(R.drawable.udemy);
                            sectionObject = dataObject.getJSONObject("courseSection");
                            sectionList = new ArrayList<>();
                            section = new CourseSection();
                            section.setId(sectionObject.getInt("id"));
                            section.setTitle("Section "+(sectionList.size()+1) +" : "+sectionObject.getString("description"));
                            section.setType(0);
                            tempSectionId = sectionObject.getInt("id");
                            sectionList.add(section);
                            sectionVideo = new SectionVideo();
                            sectionVideoList = new ArrayList<>();
                            sectionVideoObject = sectionObject.getJSONObject("courseSectionVideo");
                            sectionVideo.setId(sectionVideoObject.getInt("id"));
                            sectionVideo.setName(sectionVideoObject.getString("description"));
                            sectionVideo.setVideoUrl(sectionVideoObject.getString("videoUrl"));
                            sectionVideo.setDuration("04:23");
                            sectionCounter++;
                            sectionVideo.setParentPosition(sectionCounter-1);
                            Log.i(TAG,"section pos "+sectionVideo.getParentPosition());
                            if(i == 0){
                                sectionVideo.setPlaying(true);
                            }
                            sectionVideoList.add(sectionVideo);
                            section.setSectionVideoList(sectionVideoList);
                            myCourse.setSectionList(sectionList);
                            myItemList.add(myCourse);
                        }else{
                            myCourse = getCourse(tempCourseId);
                            sectionObject = dataObject.getJSONObject("courseSection");
                            if(tempSectionId != sectionObject.getInt("id")){
                                tempSectionId = sectionObject.getInt("id");
                                section = new CourseSection();
                                sectionList = myCourse.getSectionList();
                                section.setId(sectionObject.getInt("id"));
                                section.setTitle("Section "+(sectionList.size()+1) +" : "+sectionObject.getString("description"));
                                section.setType(0);

                                sectionVideo = new SectionVideo();
                                sectionVideoList = new ArrayList<>();
                                sectionVideoObject = sectionObject.getJSONObject("courseSectionVideo");
                                sectionVideo.setId(sectionVideoObject.getInt("id"));
                                sectionVideo.setName(sectionVideoObject.getString("description"));
                                sectionVideo.setVideoUrl(sectionVideoObject.getString("videoUrl"));
                                sectionVideo.setDuration("04:23");
                                sectionCounter++;
                                sectionVideo.setParentPosition(sectionCounter-1);
                                Log.i(TAG,"section pos "+sectionVideo.getParentPosition());
                                sectionVideoList.add(sectionVideo);
                                section.setSectionVideoList(sectionVideoList);
                                sectionList.add(section);
                            }else{
                                section = getCourseSection(myCourse,tempSectionId);
                                sectionVideo = new SectionVideo();
                                sectionVideoList = section.getSectionVideoList();
                                sectionVideoObject = sectionObject.getJSONObject("courseSectionVideo");
                                sectionVideo.setId(sectionVideoObject.getInt("id"));
                                sectionVideo.setName(sectionVideoObject.getString("description"));
                                sectionVideo.setVideoUrl(sectionVideoObject.getString("videoUrl"));
                                sectionVideo.setDuration("04:23");
                                sectionVideo.setParentPosition(sectionCounter-1);
                                Log.i(TAG,"section pos "+sectionVideo.getParentPosition());
                                sectionVideoList.add(sectionVideo);
                            }
                        }
                    }

                    myItemAdapter.notifyDataSetChanged();
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private MyCourse getCourse(int id){
        MyCourse item = null;
       for(MyCourse myCourse : myItemList){
           if(myCourse.getId() == id){
               item =  myCourse;
               break;
           }
       }

       return item;
    }

    private CourseSection getCourseSection(MyCourse myCourse,int id){
        CourseSection item = null;
        for(CourseSection section : myCourse.getSectionList()){
            if(section.getId() == id){
                item =  section;
                break;
            }
        }

        return item;
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(CourseListActivity.this, CourseDetailsActivity.class);
        intent.putExtra("flag","CourseList");
        intent.putExtra("course",myItemList.get(position));
        startActivity(intent);
    }
}
