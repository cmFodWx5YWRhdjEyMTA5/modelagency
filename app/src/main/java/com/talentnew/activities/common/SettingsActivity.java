package com.talentnew.activities.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talentnew.R;
import com.talentnew.activities.agency.MySubscriptionActivity;
import com.talentnew.activities.agency.UploadDocumentActivity;
import com.talentnew.activities.talent.BasicProfileActivity;
import com.talentnew.activities.talent.EditProfileActivity;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.adapters.SettingsAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.utilities.Constants;

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
        initFooter(this,4);
        init();
    }

    private void init(){
        itemList = new ArrayList<>();
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            itemList.add("Upload Documents");
        }else {
            itemList.add("Basic Details");
            itemList.add("Edit Portfolio");
            itemList.add("My jobs");
        }
        itemList.add("My Subscription");
        itemList.add("Notifications");
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
            intent.putExtra("flag","setting");
            startActivity(intent);
        }else if(name.equals("Edit Portfolio")){
            Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        }else if(name.equals("Upload Documents")){
            Intent intent = new Intent(SettingsActivity.this, UploadDocumentActivity.class);
            startActivity(intent);
        }else if(name.equals("My Subscription")){
            Intent intent = new Intent(SettingsActivity.this, MySubscriptionActivity.class);
            startActivity(intent);
        }
    }
}
