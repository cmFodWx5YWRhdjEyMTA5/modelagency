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
import com.modelagency.adapters.SettingsAdapter;
import com.modelagency.interfaces.MyItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private List<String> itemList;
    private SettingsAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        setToolbarDetails(this);
        initFooter(this,10);
        init();
    }

    private void init(){
        itemList = new ArrayList<>();
        itemList.add("Basic Details");
        itemList.add("Notifications");
        itemList.add("My jobs");
        itemList.add("FAQs");
        itemList.add("Support");
        itemList.add("Privacy");
        itemList.add("Terms of use");
        itemList.add("Logout");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new SettingsAdapter(this,itemList);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onItemClicked(int position, int type) {
        String name = itemList.get(position);
        if(name.equals("Logout")){
            logout();
        }
    }
}
