package com.talentnew.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.talentnew.R;
import com.talentnew.interfaces.OnFragmentInteractionListener;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileVidoFragment} interface
 * to handle interaction events.
 * Use the {@link ProfileVidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileVidoFragment extends NetworkBaseFragment implements YouTubePlayer.OnInitializedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View itemView;
    private EditText et_url;
    private Button btn_add_url,btn_youtube_url;

    private OnFragmentInteractionListener mListener;

    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;
    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
   // private YouTubePlayerFragment youTubePlayerFragment;

    public ProfileVidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileVidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileVidoFragment newInstance(String param1, String param2) {
        ProfileVidoFragment fragment = new ProfileVidoFragment();
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
        itemView =  inflater.inflate(R.layout.fragment_profile_vido, container, false);

       RelativeLayout rl_upload_video = itemView.findViewById(R.id.rl_upload_video);
       if(mParam1.equals("editProfile")){
            itemView.findViewById(R.id.rl_upload_video).setVisibility(View.VISIBLE);
           itemView.findViewById(R.id.rl_error_layout).setVisibility(View.GONE);
            et_url = itemView.findViewById(R.id.et_url);
            btn_add_url = itemView.findViewById(R.id.btn_add_url);
            Utility.setColorFilter(btn_add_url.getBackground(),getResources().getColor(R.color.colorAccent));
            btn_youtube_url = itemView.findViewById(R.id.btn_youtube_url);
           btn_add_url.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   attemptAddVideo();
               }
           });

           btn_youtube_url.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent webIntent = new Intent(Intent.ACTION_VIEW,
                           Uri.parse("https://www.youtube.com/"));
                   //webIntent.setPackage("com.google.android.youtube");
                   try {
                       startActivity(webIntent);
                   } catch (ActivityNotFoundException ex) {
                       ex.printStackTrace();
                   }
               }
           });
        }else{
           if(TextUtils.isEmpty(sharedPreferences.getString(Constants.VIDEO_FILE,""))){
               itemView.findViewById(R.id.rl_error_layout).setVisibility(View.VISIBLE);
               TextView textViewError = itemView.findViewById(R.id.tv_error);
               textViewError.setText("No video uploaded.");
           }else{
               initializeYoutubePlayer();
           }
       }

        return itemView;
    }

    /**
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private void initializeYoutubePlayer() {

        //youTubePlayerFragment = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_player_fragment);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize("AIzaSyCF8H9TnCyaloHJU4dMD0LGbkfEjWct_6A", new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    String videoUrl = sharedPreferences.getString(Constants.VIDEO_FILE,"");
                    if(videoUrl.contains("/")){
                        videoUrl = videoUrl.replace("/","");
                        editor.putString(Constants.VIDEO_FILE,"zJH9mLteskE");
                        editor.commit();
                    }
                    //videoUrl = "SWE5a0Ndiiw";
                    Log.i(TAG,"video id "+videoUrl);
                    youTubePlayer.cueVideo(videoUrl);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_player_fragment, youTubePlayerFragment).commit();
    }

    private void attemptAddVideo(){
        String videoUrl = et_url.getText().toString();
        if(TextUtils.isEmpty(videoUrl)){
            showMyDialog("Please add video url.");
            return;
        }/*else if(!videoUrl.contains("www.youtube.com/watch?v=")){
            showMyDialog("Please add valid youtube video url.");
            return;
        }*/

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.VIDEO_FILE_ID,"0"));
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("videoUrl",videoUrl);
        String url = getResources().getString(R.string.url)+Constants.ADD_VIDEO_URL;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"addVideo");

    }

    private void getVideoList(){

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        String url = getResources().getString(R.string.url)+Constants.GET_VIDEO+"?id="+sharedPreferences.getString(Constants.USER_ID,"");
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getVideo");

    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(jsonObject.getBoolean("status")){
                showMyDialog(jsonObject.getString("message"));
                String videoUrl = et_url.getText().toString();
                if(videoUrl.contains("=")){
                    editor.putString(Constants.VIDEO_FILE,videoUrl.split("=")[1]);
                    editor.putString(Constants.VIDEO_FILE_ID,"1");
                }else{
                    editor.putString(Constants.VIDEO_FILE_ID,"1");
                    editor.putString(Constants.VIDEO_FILE,videoUrl.substring(videoUrl.lastIndexOf("/")+1));
                }
                editor.commit();
            }else{
                showMyDialog(jsonObject.getString("message"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
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

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
