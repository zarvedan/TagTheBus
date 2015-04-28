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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    //WebService pour Barcelone mais peut-être changer pour une autre ville
    String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

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

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
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
         /*   CountDownTimer myCountDown = new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    afficherListStations();
                }
            }.start();
            */
        recupererListStations();
    }

    public void recupererListStations() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEBSERVICE LIST: ", "OK");
                        try {
                            remplirListeStationsBus(response);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEBSERVICE LIST: ", "ERREUR: " + error.toString());
            }
        }
        );
        queue.add(jsObjRequest);
    }

    public void remplirListeStationsBus(JSONObject response) throws Throwable {
        JSONObject jsObjData = null;
        JSONArray jsArrNearstations = null;
        JSONObject jsObjStationDeBus = null;

        try {
            jsObjData = response.getJSONObject("data");
            jsArrNearstations = jsObjData.getJSONArray("nearstations");
            Log.d("TEST_Array", jsArrNearstations.toString());
            for (int i = 0; i < jsArrNearstations.length(); i++) {
                jsObjStationDeBus = jsArrNearstations.getJSONObject(i);
                String nom = jsObjStationDeBus.getString("street_name");
                listStationsDeBusString.add(nom);
            }

        } catch (JSONException e) {
            Log.w("Remplir liste error", "CATCH exception :" + e.toString());
            Log.d("JSON Error", response.toString());
        }
        //((MainActivity) getActivity()).sauvegarderList(listStationsDeBusString);
afficherListStations();
    }


    public void afficherListStations(){

        //listStationsDeBusString = ((MainActivity) getActivity()).recupererListeStations();
        Log.d("afficher", "Arraylist:" + listStationsDeBusString.toString());
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listStationsDeBusString);
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
