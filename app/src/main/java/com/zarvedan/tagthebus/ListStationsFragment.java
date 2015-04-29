package com.zarvedan.tagthebus;

import android.app.Activity;
import android.os.Bundle;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

//***************************************************************************************
//
// Class ListStationsFragment :
//      Affiche une ListView de toutes les stations de Barcelone
//
//*****************************************************************************************

public class ListStationsFragment extends ListFragment {

    //ArrayList qui stockera la liste des stations de bus de Barcelone renvoyée par le webservice
    ArrayList<String> listStationsDeBusString = new ArrayList<>();

    //Adresse du webservice
    String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

    //Variables Toast
    public Toast toast;
    public int duration = Toast.LENGTH_LONG;

    private OnFragmentInteractionListener mListener;

    public static ListStationsFragment newInstance(String param1, String param2) {
        ListStationsFragment fragment = new ListStationsFragment();
        Bundle args = new Bundle();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.list_stations_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    // Lors d'un clic sur la liste, on affiche le fragment ListPhotosStationsFragment
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            try {
                Log.d("ListItemClick","pos:"+position+"id:"+id);
                String nomStationBus = (String)this.getListAdapter().getItem(position);

                ActionBarActivity myActionBarActivity = (ActionBarActivity) getActivity();
                myActionBarActivity.getSupportActionBar().hide();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ListPhotosStationsFragment fragment = new ListPhotosStationsFragment();
                fragment.recupererNomStation(nomStationBus);
                ft.replace(R.id.ListPhotosStationsContainer,fragment);
                ft.addToBackStack(null);
                ft.show(fragment);
                ft.commit();
            } catch (Exception e) {
                toast = Toast.makeText(getActivity(), e.toString(), duration);
                toast.show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recupererListStations();
    }

    // Méthode qui récupère le résultat de l'appel du webservice au format JSON
    // Utilisation de Volley pour la gestion des appels aux webservices en asynchrone
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

    //Méthode qui remplit l'Arraylist des noms de stations de bus à parir du JSON renvoyé par le webservice
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
        afficherListStations();
    }


    public void afficherListStations(){
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
