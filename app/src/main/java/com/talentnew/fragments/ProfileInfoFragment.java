package com.talentnew.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentnew.R;
import com.talentnew.adapters.GenresAdapter;
import com.talentnew.adapters.ProfileInfoAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.OnFragmentInteractionListener;
import com.talentnew.models.Genre;
import com.talentnew.models.InfoItem;
import com.talentnew.models.MyModel;
import com.talentnew.utilities.Constants;

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
public class ProfileInfoFragment extends BaseFragment implements MyItemClickListener {
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
    private int position ,type;
    private MyModel myModel;

    private OnFragmentInteractionListener mListener;

    public void setMyModel(MyModel myModel) {
        this.myModel = myModel;
    }

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

        if(mParam1.equals("editProfile")){
            infoItemList1 = dbHelper.getAllInfoItem(1);
            infoItemList2 = dbHelper.getAllInfoItem(2);
        }else{
            if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model")) {
                infoItemList1 = dbHelper.getAllInfoItem(1);
                infoItemList2 = dbHelper.getAllInfoItem(2);
            }else {
                setModelDetails();
            }
        }

        recyclerView1 = view.findViewById(R.id.recycler_view_info_1);
        recyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        profileInfoAdapter1=new ProfileInfoAdapter(getActivity(),infoItemList1,mParam1+"1");
        profileInfoAdapter1.setMyItemClickListener(this);
        recyclerView1.setAdapter(profileInfoAdapter1);
        recyclerView1.setNestedScrollingEnabled(false);


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

        if(mParam1.equals("editProfile")){

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
        }else{
            String genre = "";
            if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model")) {
                genre = sharedPreferences.getString(Constants.GENRE, "");
            }else {
                genre = myModel.getGenre();
            }
            Log.i(TAG, "genre " + genre);
            String[] arr = genre.split(",");
            Genre item = null;
            for (String gen : arr) {
                item = new Genre();
                item.setName(gen);
                item.setSelected(true);
                itemList.add(item);
            }
        }

        itemAdapter.notifyDataSetChanged();
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
    public void onItemClicked(int position, int type) {
        Log.i("Adapter","info clicked "+position+" type "+type);
        this.position = position;
        this.type = type;
       if(type == 1){
           onButtonPressed(infoItemList1.get(position),0,3);
       }else if(type == 2){
           onButtonPressed(infoItemList2.get(position),0,4);
       }else if(type == 3){
           onButtonPressed(itemList.get(position),0,5);
       }else if(type == 4){
           onButtonPressed(itemList.get(position),0,6);
       }
    }

    public void setInfoItemValue(InfoItem item){
        Log.i("adapter","set value "+item.getLabel()+" "+item.getValue());
        if(type == 1){
            profileInfoAdapter1.notifyItemChanged(position);
        }else if(type == 2){
            profileInfoAdapter2.notifyItemChanged(position);
        }
    }

    public void setModelDetails(){
        infoItemList1 = new ArrayList<>();
        infoItemList2 = new ArrayList<>();
        InfoItem item =null, item1 = null;
        item = new InfoItem();
        item.setLabel("Height");
        item.setShowLabel("Height");
        item.setValue(myModel.getHeight());
        item.setType(1);
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Weight");
        item.setShowLabel("Weight");
        item.setValue(myModel.getWeight());
        item.setType(1);
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Breast");
        item.setShowLabel("Breast");
        item.setValue(myModel.getBreast());
        item.setType(1);
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Waist");
        item.setShowLabel("Waist");
        item.setValue(myModel.getWaist());
        item.setType(1);
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Hip");
        item.setShowLabel("Hip");
        item.setValue(myModel.getHip());
        item.setType(1);
        infoItemList1.add(item);
        item = new InfoItem();
        item.setLabel("Experience");
        item.setShowLabel("Experience");
        item.setValue(myModel.getExperience());
        item.setType(1);
        infoItemList1.add(item);

        item1 = new InfoItem();
        item1.setLabel("Ethnicity");
        item1.setShowLabel("Ethnicity");
        item1.setValue(myModel.getEthnicity());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Skin Color");
        item1.setShowLabel("Skin Color");
        item1.setValue(myModel.getSkinColor());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Skin Color");
        item1.setShowLabel("Skin Color");
        item1.setValue(myModel.getSkinColor());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Eye Color");
        item1.setShowLabel("Eye Color");
        item1.setValue(myModel.getEyeColor());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Hair Color");
        item1.setShowLabel("Hair Color");
        item1.setValue(myModel.getHairColor());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Hair Length");
        item1.setShowLabel("Hair Length");
        item1.setValue(myModel.getHairLength());
        item1.setType(1);
        infoItemList2.add(item1);
        item1 = new InfoItem();
        item1.setLabel("Acting Education");
        item1.setShowLabel("Acting Education");
        item1.setValue(myModel.getActingEducation());
        item1.setType(1);
        infoItemList2.add(item1);
    }
}
