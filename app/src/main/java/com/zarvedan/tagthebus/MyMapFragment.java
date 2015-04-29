package com.zarvedan.tagthebus;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//***********************************
//
//    Class MyMapFragment:
//      Contient la map
//
//***********************************

public class MyMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener{

    protected LatLng barcelone = new LatLng(41.4, 2.19);
    GoogleMap mapBarcelone;
    private static Integer ZOOM_LEVEL = 14;

    private View view;

    //Adresse du webservice
    String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

    //Variables Toast
    public Toast toast;
    public int duration = Toast.LENGTH_LONG;


    private OnFragmentInteractionListener mListener;

    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
        return fragment;
    }

    public MyMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            try {
                mapFragment.getMapAsync(this);
            } catch (Exception e) {
                Log.d("catched error", "error");
                e.printStackTrace();
            }
            recupererListStations();
        }
    }

    // Méthode qui récupère le résultat de l'appel du webservice au format JSON
    // Utilisation de Volley pour la gestion des appels aux webservices en asynchrone
    public void recupererListStations() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEBSERVICE MAP: ", "OK");
                        try {
                            afficherMarkersStationsBus(response);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEBSERVICE MAP: ", "ERREUR: " + error.toString());
                toast = Toast.makeText(getActivity(), "Veuillez vérifier votre connexion internet.", duration);
                toast.show();
            }
        }
        );
        queue.add(jsObjRequest);
    }

    //Méthode qui affiche les markers sur la map  avec le titre et les coordonées récupéres
    // dans le JSON renvoyé par le webservice
    public void afficherMarkersStationsBus(JSONObject response) throws Throwable {
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
                String lat = jsObjStationDeBus.getString("lat");
                String lng = jsObjStationDeBus.getString("lon");

                if (mapBarcelone != null) {
                    mapBarcelone.addMarker(new MarkerOptions()
                            .title(nom)
                            .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
                }
            }

        } catch (JSONException e) {
            Log.w("Remplir liste error", "CATCH exception :" + e.toString());
            Log.d("JSON Error", response.toString());
        }
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

    @Override
    public void onMapReady(GoogleMap mMap) {
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelone, ZOOM_LEVEL));
        mapBarcelone = mMap;
        mapBarcelone.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mapBarcelone.setOnMapClickListener((GoogleMap.OnMapClickListener) this);
    }


    // Lors d'un clic sur un marker, on affiche un bouton en haut a droite, sous le bouton de géolocalisation
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("marker",":"+marker.getTitle());
        try {
            FrameLayout fl = (FrameLayout) view.findViewById(R.id.layoutBouton);
            fl.setVisibility(View.VISIBLE);
            ImageButton ib = (ImageButton) view.findViewById(R.id.boutonGallerie);
            ib.setVisibility(View.VISIBLE);
            // on initialise marker dans l'activité principale. Cette variable sera nécessaire  si l'utilisateur
            // clique sur le bouton qui appelera une méthode dans l'activité, en l'occurence "voirListePhotos"
            ((MainActivity) getActivity()).marker = marker;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lors d'un clic sur la map, on rend invisible le bouton affiché par la méthode ci-dessus
    @Override
    public void onMapClick(LatLng latLng) {
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.layoutBouton);
        fl.setVisibility(View.INVISIBLE);
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
        public void onFragmentInteractionMap(Uri uri);
    }

}
