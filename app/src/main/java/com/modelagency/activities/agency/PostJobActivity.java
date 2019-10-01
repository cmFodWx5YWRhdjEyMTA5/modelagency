package com.modelagency.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.GenresAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.Genre;
import com.modelagency.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class PostJobActivity extends NetworkBaseActivity implements MyItemClickListener {

    private GenresAdapter itemAdapter;
    private RecyclerView recyclerView;
    private List<Genre> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        initFooter(this, 1);
        setToolbarDetails(this);
        initViews();
    }

    private void initViews(){
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

    @Override
    public void onItemClicked(int position, int type) {

    }
}
