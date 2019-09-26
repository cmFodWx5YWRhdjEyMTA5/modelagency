package com.modelagency.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.modelagency.R;
import com.modelagency.adapters.GenresAdapter;
import com.modelagency.adapters.PortFolioAdapter;
import com.modelagency.adapters.ProfileInfoAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.interfaces.MyItemLevelClickListener;
import com.modelagency.interfaces.OnFragmentInteractionListener;
import com.modelagency.models.Album;
import com.modelagency.models.Genre;
import com.modelagency.models.InfoItem;
import com.modelagency.models.PortFolio;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilePortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePortfolioFragment extends Fragment implements MyItemClickListener, MyItemLevelClickListener {
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

    private int counter;

    private OnFragmentInteractionListener mListener;

    public ProfilePortfolioFragment() {
        // Required empty public constructor
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

        RelativeLayout rl_header = view.findViewById(R.id.rl_header);
        RelativeLayout rl_create_album = view.findViewById(R.id.rl_create_album);
        if(mParam1.equals("editProfile")){
            rl_header.setVisibility(View.VISIBLE);
            rl_create_album.setVisibility(View.VISIBLE);
        }

        getPortFolio();
        return view;
    }

    private void getPortFolio(){
        Album album = new Album();
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
        itemAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Object ob,int type) {
        if (mListener != null) {
            mListener.onFragmentInteraction(ob,type);
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
              onButtonPressed(null,1);
          }
      }else if(type == 2){
          counter--;
          if(counter == 0){
              onButtonPressed(null,2);
          }
      }
    }

    @Override
    public void onItemClicked(int parentPosition, int position, int type) {
        if(type == 1){
            counter++;
            if(counter == 1){
                onButtonPressed(null,1);
            }
        }else if(type == 2){
            counter--;
            if(counter == 0){
                onButtonPressed(null,2);
            }
        }
    }
}
