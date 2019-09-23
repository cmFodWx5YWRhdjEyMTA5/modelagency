package com.modelagency.activities.model;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.MyItemAdapter;
import com.modelagency.models.HomeListItem;
import com.modelagency.models.MyModel;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends NetworkBaseActivity {
    private Typeface typeface;
    private RecyclerView recyclerView;
    private MyItemAdapter myItemAdapter;
    private List<Object> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        typeface = Utility.getFreeHandFont(this);
        initFooter(this, 4);
        initViews();
        getJoblist();
    }

    private void initViews(){
        recyclerView = findViewById(R.id.recycler_view);

    }


    public void volleyRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("", "");
        String url = getResources().getString(R.string.url) + "/Users/SignUp";
        jsonObjectApiRequest(Request.Method.POST, url, new JSONObject(params), "SignUp");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            if (apiName.equals("SignUp")) {
                if (response.getString("status").equals("true") || response.getString("status").equals(true)) {
                    JSONObject dataObject = response.getJSONObject("data");

                } else {
                    showMyDialog(response.getString("message"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(getResources().getString(R.string.json_parser_error) + e.toString());
        }
    }

    private void getJoblist(){
        myItemList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new MyItemAdapter(this,myItemList,"jobList");
        recyclerView.setAdapter(myItemAdapter);

        HomeListItem header = new HomeListItem();
        header.setTitle("Jobs");
        //header.setDesc("Festive Sales");
        header.setType(0);
        myItemList.add(header);

        MyModel myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);

        myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);

        myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);


        header = new HomeListItem();
        header.setTitle("Blogs");
        header.setType(1);
        myItemList.add(header);

        myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);

        myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);

        myItem = new MyModel();
        myItem.setName("Big Discount in Titan Watches");
        myItem.setCompany("Titan Stores");
        myItem.setAddress("Watches");
        myItem.setLocalImage(R.drawable.model);
        myItemList.add(myItem);

    }
}