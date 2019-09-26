package com.modelagency.activities.common;

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
import com.modelagency.adapters.BoostAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.Boost;

import java.util.ArrayList;
import java.util.List;

public class BoostActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private BoostAdapter myItemAdapter;
    private List<Object> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setToolbarDetails(this);
        init();
        initFooter(this,3);
    }

    private void init(){
        myItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new BoostAdapter(this,myItemList,"homeList");
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        Boost item = null;
        List<Object> infoList = null;
        for(int i =0; i<3; i++){
            item = new Boost();
            infoList = new ArrayList<>();
            item.setHeader("Basic");
            item.setPay("INR 480.00/month");
            infoList.add("up to 3 albums");
            infoList.add("up to 50 photos");
            infoList.add("Basic Listed");
            infoList.add("apply to 15 job per month");
            item.setItemList(infoList);
            myItemList.add(item);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {

    }
}
