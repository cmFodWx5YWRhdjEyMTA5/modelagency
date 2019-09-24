package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.JobListAdapter;
import com.modelagency.adapters.MyItemAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyJob;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private JobListAdapter myItemAdapter;
    private List<MyJob> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

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
        myItemAdapter=new JobListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        MyJob item = null;
        for(int i=0; i<20; i++){
            item = new MyJob();
            item.setTitle("Youtiful is seeking real people of all types and ages for new campaign");
            item.setLocation("Delhi");
            item.setCloseDate("Sat, 28 Sep 2019");
            item.setLocalImage(R.drawable.model);
            myItemList.add(item);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(JobListActivity.this,JobDetailActivity.class);
        intent.putExtra("job",myItemList.get(position));
        startActivity(intent);
    }
}
