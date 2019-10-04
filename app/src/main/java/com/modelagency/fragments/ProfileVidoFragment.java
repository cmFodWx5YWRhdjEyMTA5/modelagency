package com.modelagency.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.interfaces.OnFragmentInteractionListener;
import com.modelagency.utilities.Constants;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileVidoFragment} interface
 * to handle interaction events.
 * Use the {@link ProfileVidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileVidoFragment extends NetworkBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View itemView;
    private VideoView video_view;
    private MediaController mediaController;

    private OnFragmentInteractionListener mListener;

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

       video_view = itemView.findViewById(R.id.video_view);
       RelativeLayout rl_upload_video = itemView.findViewById(R.id.rl_upload_video);
       if(mParam1.equals("editProfile")){
            itemView.findViewById(R.id.rl_transparent).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.rl_upload_video).setVisibility(View.VISIBLE);
        }else{
           if(TextUtils.isEmpty(sharedPreferences.getString(Constants.VIDEO_FILE,""))){
               video_view.setVisibility(View.GONE);
               itemView.findViewById(R.id.rl_error_layout).setVisibility(View.VISIBLE);
               TextView textViewError = itemView.findViewById(R.id.tv_error);
               textViewError.setText("No video uploaded.");
           }
       }

        rl_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(null,0,8);
            }
        });

        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);
        video_view.requestFocus();
        return itemView;
    }

    public void showVideoSuccess(String path){
        Uri u = Uri.parse(path);
        video_view.setVideoURI(u);
        video_view.start();
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
}
