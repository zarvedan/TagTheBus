package com.zarvedan.tagthebus;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListStationsFragment extends ListFragment {

    ArrayList<String> listStationsDeBusString = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static ListStationsFragment newInstance(String param1, String param2) {
       ListStationsFragment fragment = new ListStationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListStationsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("on create","List Stations fragment");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

      /* // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
                */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("on createview","List Stations fragment");
        View view =  inflater.inflate(R.layout.list_stations_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("on attach","List Stations fragment");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {

            Log.d("ListItemClick","pos:"+position+"id:"+id);
            String nomStationBus = (String)this.getListAdapter().getItem(position);

            ActionBarActivity myActionBarActivity = (ActionBarActivity) getActivity();
            myActionBarActivity.getSupportActionBar().hide();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ListPhotosStationsFragment fragment = new ListPhotosStationsFragment();
            fragment.recupererNomStation(nomStationBus);
            fragmentTransaction.replace(R.id.ListPhotosStationsContainer,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.show(fragment);

            fragmentTransaction.commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*Mise en place d'un timer pour attendre la fin de l'appel du webservice dans MyMapFragment.
        Préférer communiquer entre fragments via les Bundle ou mise en place d'un écouteur type notify()/wait() */
            CountDownTimer myCountDown = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    afficherListStations();
                }
            }.start();
    }



    public void afficherListStations(){

        listStationsDeBusString = ((MainActivity) getActivity()).recupererListeStations();
        Log.d("afficher", "Arraylist:" + listStationsDeBusString.toString());
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listStationsDeBusString);
        this.setListAdapter(listAdapter);

    }

    public void afficher(ArrayList<String> arrayList){
        Log.d("Afficher",":"+arrayList.toString());
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets) );
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, planetList);
        // Set the ArrayAdapter as the ListView's adapter.
        //  mainListView.setAdapter( listAdapter );
        this.setListAdapter(listAdapter);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteractionListStations(String id);
    }

}
