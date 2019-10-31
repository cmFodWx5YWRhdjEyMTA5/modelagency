package com.talentnew.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.adapters.BoostAdapter;
import com.talentnew.models.Boost;
import com.talentnew.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class MySubscriptionActivity extends NetworkBaseActivity {
    private BoostAdapter myItemAdapter;
    private List<Object> myItemList;
    private Boost boost;
    private RecyclerView recyclerView;
    private TextView tv_header,tv_pay;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscription);
        myItemList = new ArrayList<>();
        initViews();
    }


    private void initViews(){
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_header = findViewById(R.id.tv_header);
        tv_pay = findViewById(R.id.tv_pay);
        recyclerView = findViewById(R.id.recycler_view);
        getItemList();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new BoostAdapter(this,myItemList,"homeList");
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        boost = new Boost();
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){

            if(!sharedPreferences.getString(Constants.SUBSC_TITLE, "").equals("null") ){
                boost.setHeader(sharedPreferences.getString(Constants.SUBSC_TITLE, ""));
                boost.setScheme(sharedPreferences.getString(Constants.SUBSC_SCHEME, ""));
                boost.setAmount(Float.parseFloat(sharedPreferences.getString(Constants.SUBSC_AMOUNT, "")));
                boost.setValidity(sharedPreferences.getString(Constants.SUBSC_VALIDITY, ""));
                boost.setPay(sharedPreferences.getString(Constants.SUBSC_AMOUNT,""));

                //boost.setPay("INR "+ String.format("%.02f",Float.parseFloat(sharedPreferences.getString(Constants.SUBSC_AMOUNT, ""))+"/"+
                //       sharedPreferences.getString(Constants.SUBSC_SCHEME, "")));

                tv_header.setText(boost.getHeader());
                tv_pay.setText(boost.getPay());

                if(sharedPreferences.getInt(Constants.SUBSC_JOB_POST, 0) == 1){
                    myItemList.add("Post unlimited Jobs");
                }

                if(sharedPreferences.getInt(Constants.SUBSC_FB_SHOUTOUT, 0) == 1) {
                    myItemList.add("Enhanced your visibility on our facebook page");
                }

                if(sharedPreferences.getInt(Constants.SUBSC_EMIL_SHOUTOUT, 0) == 1) {
                    myItemList.add("Send Emails to talents");
                }

                if(sharedPreferences.getInt(Constants.SUBSC_BOOSTJOB, 0) == 1) {
                    myItemList.add("Boost Jobs");
                }

                if(sharedPreferences.getInt(Constants.SUBSC_CUSTOMADD, 0) == 1) {
                    myItemList.add("Advertise with custom Add");
                }

                if(sharedPreferences.getString(Constants.SUBSC_DEDICATED_MANAGER, "").equals("Y")) {
                    myItemList.add("Dedicated Manager");
                }

                if(sharedPreferences.getString(Constants.SUBSC_CONTACT_MODEL, "").equals("1")) {
                    myItemList.add("View Model Contacts");
                }

                if(sharedPreferences.getString(Constants.SUBSC_PRO_TAG, "").equals("Y")) {
                    myItemList.add("Pro Tag");
                }

                if(sharedPreferences.getString(Constants.SUBSC_VERIFIED_TAG, "").equals("Y")) {
                    myItemList.add("Verified Tag");
                }
            }

        }else{

            if(!sharedPreferences.getString(Constants.BOOST_TITLE, "").equals("null") ){
                boost.setHeader(sharedPreferences.getString(Constants.BOOST_TITLE, ""));
                boost.setScheme(sharedPreferences.getString(Constants.BOOST_SCHEME, ""));
                boost.setAmount(sharedPreferences.getFloat(Constants.BOOST_AMOUNT, 0f));
                boost.setValidity(sharedPreferences.getString(Constants.BOOST_VALIDITY, ""));
                boost.setPay("INR "+ String.format("%.02f",sharedPreferences.getFloat(Constants.BOOST_AMOUNT,0f))+"/"+
                        sharedPreferences.getString(Constants.BOOST_SCHEME, ""));
                //boost.setPay("INR "+ String.format("%.02f",Float.parseFloat(sharedPreferences.getString(Constants.SUBSC_AMOUNT, ""))+"/"+
                //       sharedPreferences.getString(Constants.SUBSC_SCHEME, "")));

                tv_header.setText(boost.getHeader());
                tv_pay.setText(boost.getPay());

                if(sharedPreferences.getInt(Constants.APPLY_JOB, 0) > 0){
                    myItemList.add("Apply jobs "+sharedPreferences.getInt(Constants.APPLY_JOB, 0));
                }

                if(!sharedPreferences.getString(Constants.BOOST_ONLINE_COURSE, "").equals("N") &&
                        !sharedPreferences.getString(Constants.BOOST_ONLINE_COURSE, "").equals("null")) {
                    myItemList.add("Online course "+sharedPreferences.getString(Constants.BOOST_ONLINE_COURSE, ""));
                }

                if(!sharedPreferences.getString(Constants.BOOST_EMAIL, "").equals("null") &&
                        !sharedPreferences.getString(Constants.BOOST_EMAIL, "").equals("N")) {
                    myItemList.add("Send emails to agency");
                }

                if(!sharedPreferences.getString(Constants.BOOST_FEATURE_TAG, "").equals("null") &&
                        !sharedPreferences.getString(Constants.BOOST_FEATURE_TAG, "").equals("N")) {
                    myItemList.add("Feature tag");
                }

                if(sharedPreferences.getInt(Constants.BOOST_PHOTO_SHOOT, 0) > 1) {
                    myItemList.add("Photo shoot "+sharedPreferences.getInt(Constants.BOOST_PHOTO_SHOOT, 0));
                }
            }

        }
    }
}
