package com.modelagency.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modelagency.R;
import com.modelagency.adapters.GenresAdapter;
import com.modelagency.adapters.PortFolioAdapter;
import com.modelagency.adapters.ProfileInfoAdapter;
import com.modelagency.interfaces.OnFragmentInteractionListener;
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
public class ProfilePortfolioFragment extends Fragment {
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
    private List<PortFolio> itemList;

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
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new PortFolioAdapter(getActivity(),itemList,mParam1);
      //  itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);
        getPortFolio();
        return view;
    }

    private void getPortFolio(){
        PortFolio item = new PortFolio();
        itemList.add(item);
        for(int i=0; i<20; i++){
            item = new PortFolio();
            itemList.add(item);
        }

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

}
