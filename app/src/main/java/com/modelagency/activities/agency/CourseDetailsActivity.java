package com.modelagency.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.adapters.CourseListAdapter;
import com.modelagency.adapters.MyItemAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.HomeListItem;
import com.modelagency.models.MyCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> myItemList;
    private MyCourse course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        initFooter(this, 2);
        setToolbarDetails(this);
        init();
    }

    private void init(){
        course = (MyCourse) getIntent().getSerializableExtra("course");
        TextView tv_course_section = findViewById(R.id.tv_course_section);
        tv_course_section.setText(course.getSection());
        TextView tv_course_titile = findViewById(R.id.tv_course_titile);
        tv_course_titile.setText(course.getTitle());

        myItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new MyItemAdapter(this,myItemList, "homeList");
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
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
                item.setSection("Fundamental of Modeling Modeling : 10"+i);
                item.setTitle("Academy of Film Fashion and Design");
                item.setProgress(50 * (i));
                item.setImage(R.drawable.udemy);
                listItems.add(item);
            }
            myItem.setItemList(listItems);
        }
        int size  = ((HomeListItem)myItemList.get(1)).getItemList().size();
        Log.d("size ", ""+size);
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(CourseDetailsActivity.this, ProfileActivity.class);
        intent.putExtra("flag","CourseDetails");
       // intent.putExtra("course",myItemList.get(position));
        startActivity(intent);
    }
}
