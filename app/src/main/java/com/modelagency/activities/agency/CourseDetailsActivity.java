package com.modelagency.activities.agency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;

public class CourseDetailsActivity extends NetworkBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
    }
}
