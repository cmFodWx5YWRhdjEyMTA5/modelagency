package com.talentnew.activities.agency;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.talentnew.R;
import com.talentnew.activities.common.BaseImageActivity;
import com.talentnew.adapters.GenresAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.Genre;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.DialogAndToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostJobActivity extends BaseImageActivity implements MyItemClickListener {

    private GenresAdapter itemAdapter;
    private RecyclerView recyclerView;
    private List<Genre> itemList;
    private TextView tv_submit, tv_male, tv_female;
    private EditText et_job_title, et_job_location,et_job_money, et_job_close_day,et_job_description;
    private String genere, gender;
    private ImageView iv_upload_banner, banner_image;
    private String male, female;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        initFooter(this, 1);
        setToolbarDetails(this);
        initViews();
    }

    private void initViews(){
        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
        tv_male = findViewById(R.id.tv_male);
        tv_female  = findViewById(R.id.tv_female);
        et_job_title = findViewById(R.id.et_job_title);
        et_job_location = findViewById(R.id.et_job_location);
        et_job_money = findViewById(R.id.et_job_money);
        et_job_close_day = findViewById(R.id.et_job_close_day);
        et_job_description = findViewById(R.id.et_job_description);
        banner_image = findViewById(R.id.banner_image);
        iv_upload_banner = findViewById(R.id.iv_upload_banner);
        iv_upload_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        et_job_close_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEt_job_closing_date();
            }
        });
        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(male)) {
                    male = "Male";
                    tv_male.setBackgroundResource(R.drawable.accent_solid_round_corner_background);
                    tv_male.setTextColor(getResources().getColor(R.color.white));
                }else {
                    male = "";
                    tv_male.setTextColor(getResources().getColor(R.color.primary_text_color));
                    tv_male.setBackgroundResource(R.drawable.grey_stroke_white_round_corner_background);
                }
            }
        });
        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(female)) {
                    female = "Female";
                    tv_female.setBackgroundResource(R.drawable.accent_solid_round_corner_background);
                    tv_female.setTextColor(getResources().getColor(R.color.white));
                }else {
                    female = "";
                    tv_female.setTextColor(getResources().getColor(R.color.primary_text_color));
                    tv_female.setBackgroundResource(R.drawable.grey_stroke_white_round_corner_background);
                }
            }
        });
        itemList = new ArrayList<>();
        getGenre();
        recyclerView = findViewById(R.id.recycler_view_genre);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new GenresAdapter(this,itemList,"editProfile");
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void getGenre(){
        Genre genre = null;
        String genreArray[] = getResources().getStringArray(R.array.genre);
        String myGenre = sharedPreferences.getString(Constants.GENRE,"");
        for(String name : genreArray){
            genre = new Genre();
            genre.setName(name);
            if(myGenre.contains(name)){
                genre.setSelected(true);
            }
            itemList.add(genre);
        }
    }

    private void setEt_job_closing_date(){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                et_job_close_day.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void onSubmit(){
        String title, location, compensation, closeDay, description;
        title = et_job_title.getText().toString();
        location = et_job_location.getText().toString();
        compensation = et_job_money.getText().toString();
        closeDay = et_job_close_day.getText().toString();
        description = et_job_description.getText().toString();
        gender = male;
        if(!TextUtils.isEmpty(gender))
            gender = gender.concat(","+female);
        else gender = female;

        if(TextUtils.isEmpty(imagePath)){
            DialogAndToast.showDialog("Please Add Banner Image", this);
            return;
        }if(TextUtils.isEmpty(genere)){
            DialogAndToast.showDialog("Please Select Genere", this);
            return;
        }if(TextUtils.isEmpty(title)){
            DialogAndToast.showDialog("Please Title", this);
            return;
        }if(TextUtils.isEmpty(location)){
            DialogAndToast.showDialog("Please Enter Location", this);
            return;
        }if(TextUtils.isEmpty(gender)){
            DialogAndToast.showDialog("Please Select Gender", this);
            return;
        }if(TextUtils.isEmpty(compensation)){
            DialogAndToast.showDialog("Please Enter Compensation", this);
            return;
        }if(TextUtils.isEmpty(closeDay)){
            DialogAndToast.showDialog("Please Select Job Closing Date", this);
            return;
        }if(TextUtils.isEmpty(description)){
            DialogAndToast.showDialog("Please Enter Description", this);
            return;
        }

        Map<String,String> params = new HashMap<>();
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("agencyId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("bannerImageName", fileName);
        params.put("location",location);
        params.put("title",title);
        params.put("closeDate",closeDay);
        params.put("preferences",gender);
        params.put("genres",genere);
        params.put("bannerImage", convertToBase64(new File(imagePath)));
        params.put("description",description);
        params.put("compensation",compensation);
        String url = getResources().getString(R.string.url)+Constants.POST_JOBS;
        Log.d("params ", params.toString());

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"postJobs");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("postJobs")){
                if(jsonObject.getBoolean("status")){
                    DialogAndToast.showDialog(jsonObject.toString(), PostJobActivity.this);
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        if(type==3) {
            if(!TextUtils.isEmpty(genere))
            genere = genere.concat(","+itemList.get(position).getName());
            else genere = itemList.get(position).getName();
        }else if(type==4){
            if(!TextUtils.isEmpty(genere)) {
                if (TextUtils.isEmpty(genere.replace("," + itemList.get(position).getName(), "")))
                    genere = genere.replace("," + itemList.get(position).getName(), "");
                else genere = genere.replace(itemList.get(position).getName(), "");
            }
        }
    }

    @Override
    protected void imageAdded() {
        super.imageAdded();
        Glide.with(this)
                .load(imagePath)
                .into(banner_image);
    }
}
