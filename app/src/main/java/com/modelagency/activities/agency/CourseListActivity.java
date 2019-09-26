package com.modelagency.activities.agency;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.adapters.CourseListAdapter;
import com.modelagency.adapters.ModelListAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyCourse;
import com.modelagency.models.MyModel;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private CourseListAdapter myItemAdapter;
    private List<MyCourse> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        initFooter(this, 2);
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
            myItemList.add(item);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(CourseListActivity.this, CourseDetailsActivity.class);
        intent.putExtra("flag","CourseList");
        intent.putExtra("course",myItemList.get(position));
        startActivity(intent);
    }
}
