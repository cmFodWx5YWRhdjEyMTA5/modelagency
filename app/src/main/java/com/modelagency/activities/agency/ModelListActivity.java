package com.modelagency.activities.agency;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.adapters.ModelListAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyModel;

import java.util.ArrayList;
import java.util.List;

public class ModelListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ModelListAdapter myItemAdapter;
    private List<MyModel> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);
        initFooter(this, 0);
        setToolbarDetails(this);
        init();
    }

    private void init(){
        myItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ModelListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        MyModel item = null;
        for(int i=0; i<20; i++){
            item = new MyModel();
            item.setCompany("Tech Model");
            item.setName("Jess");
            item.setDesignation("Model");
            item.setAddress("Delhi");
            item.setJoining_Date("28 Sep 2017");
            item.setEnding_Date("31 Dec 2018");
            item.setLocalImage(R.drawable.model);
            myItemList.add(item);
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(ModelListActivity.this, ProfileActivity.class);
        intent.putExtra("flag","ModelList");
        intent.putExtra("models",myItemList.get(position));
        startActivity(intent);
    }
}
