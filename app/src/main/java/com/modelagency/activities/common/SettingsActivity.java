package com.modelagency.activities.common;

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
import com.modelagency.activities.talent.BasicProfileActivity;
import com.modelagency.activities.talent.JobListActivity;
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
        }else if(name.equals("FAQs") || name.equals("Support") || name.equals("Privacy") || name.equals("Terms of use")){
            Intent intent = new Intent(SettingsActivity.this,WebViewActivity.class);
            intent.putExtra("flag",name);
            startActivity(intent);
        }else if(name.equals("My jobs")){
            Intent intent = new Intent(SettingsActivity.this, JobListActivity.class);
            intent.putExtra("flag","applied");
            startActivity(intent);
        }else if(name.equals("Notifications")){
            Intent intent = new Intent(SettingsActivity.this, NotificationActivity.class);
            startActivity(intent);
        }else if(name.equals("Basic Details")){
            Intent intent = new Intent(SettingsActivity.this, BasicProfileActivity.class);
            startActivity(intent);
        }
    }
}
