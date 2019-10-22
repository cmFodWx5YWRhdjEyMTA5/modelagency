package com.talentnew.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.talentnew.R;
import com.talentnew.adapters.PortFolioAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.interfaces.OnFragmentInteractionListener;
import com.talentnew.models.Album;
import com.talentnew.models.PortFolio;
import com.talentnew.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilePortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePortfolioFragment extends NetworkBaseFragment implements MyItemClickListener, MyItemLevelClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RecyclerView recyclerView;
    private PortFolioAdapter itemAdapter;
    private List<Object> itemList;
    private RelativeLayout rl_error_layout;
    private TextView tv_error;
    private AlertDialog alertDialog;
    private int counter,parentPosition,position;
    private ImageView iv_banner;
    private String modelId;

    private OnFragmentInteractionListener mListener;

    public ProfilePortfolioFragment() {
        // Required empty public constructor
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePortfolioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePortfolioFragment newInstance(String param1, String param2) {
        ProfilePortfolioFragment fragment = new ProfilePortfolioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile_portfolio, container, false);
        itemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_genre);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new PortFolioAdapter(getActivity(),itemList,mParam1);
        itemAdapter.setMyItemClickListener(this);
        itemAdapter.setMyItemLevelClickListener(this);
        recyclerView.setAdapter(itemAdapter);

        iv_banner = view.findViewById(R.id.iv_banner);
        rl_error_layout = view.findViewById(R.id.rl_error_layout);
        tv_error = view.findViewById(R.id.tv_error);

        RelativeLayout rl_header = view.findViewById(R.id.rl_header);
        RelativeLayout rl_create_album = view.findViewById(R.id.rl_create_album);
        if(mParam1.equals("editProfile")){
            rl_header.setVisibility(View.VISIBLE);
            rl_create_album.setVisibility(View.VISIBLE);
        }

        RelativeLayout rl_upload_banner = view.findViewById(R.id.rl_upload_banner);
        rl_upload_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   onButtonPressed(null,0,7);
            }
        });

        rl_create_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateAlbumDialog();
            }
        });

        Glide.with(getActivity())
                .load(sharedPreferences.getString(Constants.BANNER_PIC,""))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(iv_banner);

        getPortFolio();
        return view;
    }

    private void getPortFolio(){
       /* Album album = new Album();
        PortFolio item = null;
        List<Object > imageList = new ArrayList<>();
        for(int i=0; i<20; i++){
            item = new PortFolio();
            item.setPosition(itemList.size());
            imageList.add(item);
        }

        album.setHeader("Polaroid");
        album.setImageList(imageList);
        itemList.add(album);
        itemAdapter.notifyDataSetChanged();*/

        Map<String,String> params = new HashMap<>();
        String id = sharedPreferences.getString(Constants.USER_ID,"");
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency"))
            id = modelId;
        params.put("id", id);
        params.put("limit", ""+limit);
        params.put("offset", ""+offset);
        String url = getResources().getString(R.string.url)+Constants.GET_ALBUM;
        Log.d(TAG, params.toString());
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getAlbum");


       //showError(true,"No data...");
    }

    private void createAlbum(String title){

        Map<String,String> params = new HashMap<>();
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("title",title);
        String url = getResources().getString(R.string.url)+Constants.CREATE_ALBUM;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"createAlbum");

    }

    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("createAlbum")){
                alertDialog.dismiss();
                if(jsonObject.getBoolean("status")){
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    Album album = new Album();
                    album.setId(dataObject.getInt("id"));
                    album.setHeader(dataObject.getString("title"));
                    PortFolio item = new PortFolio();
                    List<Object > imageList = new ArrayList<>();
                    imageList.add(item);
                    item.setPosition(itemList.size());
                    album.setImageList(imageList);
                    itemList.add(0,album);
                    itemAdapter.notifyDataSetChanged();
                    showError(false,null);
                }
            }else if(apiName.equals("getAlbum")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null;
                    int len = jsonArray.length();
                    Album album = null;
                    int tempAlbumId = 0;
                    PortFolio item = null;
                    List<Object > imageList = null;
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        if(tempAlbumId != dataObject.getInt("id")){
                            album = new Album();
                            album.setId(dataObject.getInt("id"));
                            album.setHeader(dataObject.getString("title"));
                            imageList = new ArrayList<>();
                            if(mParam1.equals("editProfile")){
                                item = new PortFolio();
                                item.setPosition(0);
                                imageList.add(item);
                            }
                            item = new PortFolio();
                            item.setPosition(itemList.size());
                            if(!dataObject.getString("portFolio").equals("null")){
                                item.setImageUrl(dataObject.getJSONObject("portFolio").getString("photoUrl"));
                                imageList.add(item);
                            }
                            album.setImageList(imageList);
                            itemList.add(album);
                            tempAlbumId = dataObject.getInt("id");
                        }else{
                            album = getAlbumItem(dataObject.getInt("id"));
                            item = new PortFolio();
                            item.setPosition(itemList.size());
                            if(!dataObject.getString("portFolio").equals("null")){
                                item.setImageUrl(dataObject.getJSONObject("portFolio").getString("photoUrl"));
                            }
                            item.setPosition(itemList.size());
                            album.getImageList().add(item);
                        }
                    }

                    if(itemList.size() == 0){
                        showError(true,"No data...");
                    }else{
                        itemAdapter.notifyDataSetChanged();
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private Album getAlbumItem(int id){
        Album album = null;
        for(Object ob : itemList){
            album = (Album)ob;
            if(album.getId() == id){
                break;
            }
        }

        return album;
    }

    private void showCreateAlbumDialog(){
        int view=R.layout.create_album_dialog_layout;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        final Button btnCreate=(Button) alertDialog.findViewById(R.id.btn_create);
        final Button btnCancel=(Button) alertDialog.findViewById(R.id.btn_cancel);
        final EditText et_title=(EditText) alertDialog.findViewById(R.id.et_title);

       // Utility.setColorFilter(btnGallery.getBackground(),getResources().getColor(R.color.colorAccent));
      //  Utility.setColorFilter(btnCamera.getBackground(),getResources().getColor(R.color.colorAccentLight));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                if(TextUtils.isEmpty(title)){
                    showMyDialog("Please enter title.");
                    return;
                }

                createAlbum(title);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Object ob,int position,int type) {
        if (mListener != null) {
            mListener.onFragmentInteraction(ob,position,type);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void clearAll(){
        int i = 0;
        Album album = null;
        PortFolio portFolio = null;
        for(Object ob : itemList){
            album = (Album)ob;
            for(Object ob1 : album.getImageList()){
                portFolio = (PortFolio)ob1;
                if(portFolio.isSelected()){
                    portFolio.setSelected(false);
                }
            }
            itemAdapter.notifyItemChanged(i);
            i++;
        }
        counter = 0;
    }

    @Override
    public void onItemClicked(int position, int type) {
      if(type == 1){
          counter++;
          if(counter == 1){
              onButtonPressed(null,0,1);
          }
      }else if(type == 2){
          counter--;
          if(counter == 0){
              onButtonPressed(null,0,2);
          }
      }
    }

    @Override
    public void onItemClicked(int parentPosition, int position, int type) {
        this.parentPosition = parentPosition;
        this.position = position;
        if(type == 1){
            counter++;
            if(counter == 1){
                onButtonPressed(null,0,1);
            }
        }else if(type == 2){
            counter--;
            if(counter == 0){
                onButtonPressed(null,0,2);
            }
        }else if(type == 0){
            Album album = (Album)itemList.get(parentPosition);
            onButtonPressed(album,position,0);
        }
    }

    public void uploadSuccess(PortFolio portFolio){
        portFolio.setPosition(parentPosition);
        Album album = (Album)itemList.get(parentPosition);
        album.getImageList().add(portFolio);
        itemAdapter.notifyItemChanged(parentPosition);
    }

    public void showBannerSuccess(String url){
        Glide.with(getActivity())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(iv_banner);
    }

    public void uploadBannerSuccess(String url){
        editor.putString(Constants.BANNER_PIC,url);
        editor.commit();
    }

    public void showError(boolean show,String message){
        if(show){
            rl_error_layout.setVisibility(View.VISIBLE);
            tv_error.setText(message);
        }else{
            rl_error_layout.setVisibility(View.GONE);
        }
    }
}
