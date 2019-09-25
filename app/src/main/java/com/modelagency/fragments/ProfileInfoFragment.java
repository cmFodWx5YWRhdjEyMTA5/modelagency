package com.modelagency.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modelagency.R;
import com.modelagency.adapters.GenresAdapter;
import com.modelagency.adapters.JobListAdapter;
import com.modelagency.adapters.ProfileInfoAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.interfaces.OnFragmentInteractionListener;
import com.modelagency.models.Genre;
import com.modelagency.models.InfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileInfoFragment extends Fragment implements MyItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private RecyclerView recyclerView,recyclerView1,recyclerView2;
    private GenresAdapter itemAdapter;
    private ProfileInfoAdapter profileInfoAdapter1,profileInfoAdapter2;
    private List<Genre> itemList;
    private List<InfoItem> infoItemList1,infoItemList2;

    private OnFragmentInteractionListener mListener;

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileInfoFragment newInstance(String param1, String param2) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
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
        view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        itemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_genre);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),3,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter=new GenresAdapter(getActivity(),itemList,mParam1);
        itemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        infoItemList1 = new ArrayList<>();
        recyclerView1 = view.findViewById(R.id.recycler_view_info_1);
        recyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        profileInfoAdapter1=new ProfileInfoAdapter(getActivity(),infoItemList1,mParam1+"1");
        profileInfoAdapter1.setMyItemClickListener(this);
        recyclerView1.setAdapter(profileInfoAdapter1);
        recyclerView1.setNestedScrollingEnabled(false);

        infoItemList2 = new ArrayList<>();
        recyclerView2 = view.findViewById(R.id.recycler_view_info_2);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        profileInfoAdapter2=new ProfileInfoAdapter(getActivity(),infoItemList2,mParam1+"2");
        profileInfoAdapter2.setMyItemClickListener(this);
        recyclerView2.setAdapter(profileInfoAdapter2);
        recyclerView2.setNestedScrollingEnabled(false);

        getItemList();

        return view;
    }

    private void getItemList(){
        Genre genre = new Genre();
        genre.setName("Beauty");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Fashion");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Casuals");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Catalogue");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Portrait");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Clothing");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Sport");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Fitness");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Lingerie");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Swimsuit");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Body Parts");
        itemList.add(genre);
        genre = new Genre();
        genre.setName("Hair");
        itemList.add(genre);

        InfoItem item = new InfoItem();
        item.setLabel("Height");
        item.setValue("175 cm | 9'10\"");
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Weight");
        item.setValue("57 kg | 126 lbs");
        infoItemList1.add(item);

        item = new InfoItem();
        item.setLabel("Ethnicity");
        item.setValue("Indian");
        infoItemList2.add(item);
        item = new InfoItem();
        item.setLabel("Skin Color");
        item.setValue("Dark");
        infoItemList2.add(item);

        itemAdapter.notifyDataSetChanged();
        profileInfoAdapter1.notifyDataSetChanged();
        profileInfoAdapter2.notifyDataSetChanged();
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

    @Override
    public void onItemClicked(int position, int type) {
       if(type == 1){
           onButtonPressed(infoItemList1.get(position),3);
       }else if(type == 2){
           onButtonPressed(infoItemList1.get(position),4);
       }
    }
}
