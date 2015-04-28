package com.zarvedan.tagthebus;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener{

    //protected LatLng barcelone = new LatLng(41.4118235, 2.1853989);
    protected LatLng barcelone = new LatLng(41.4, 2.19);
    GoogleMap mapBarcelone;
    ListStationsFragment listStations = new ListStationsFragment();
    ArrayList<String> listStationsDeBusString = new ArrayList<>();
    ArrayList<Station> listStationsDeBus = new ArrayList<>();
    private static Integer ZOOM_LEVEL = 14;
    private View view;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyMapFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("MYMAPFRAGMENT","ON START");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            Log.d("MapFragment is null", "yo");
        }
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

    public void recupererListStations() {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEBSERVICE MAP: ", "OK");
                        try {
                            remplirListeStationsBus(response);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEBSERVICE MAP: ", "ERREUR: " + error.toString());
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
                String lat = jsObjStationDeBus.getString("lat");
                String lng = jsObjStationDeBus.getString("lon");

                listStationsDeBusString.add(jsObjStationDeBus.getString("street_name"));
                listStationsDeBus.add(new Station(nom,lat,lng));
                if(mapBarcelone == null){
                    Log.d("mapBarcelone"," is null");
                }
                mapBarcelone.addMarker(new MarkerOptions()
                        .title(nom)
                        .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));
            }

        } catch (JSONException e) {
            Log.w("Remplir liste error", "CATCH exception :" + e.toString());
            Log.d("JSON Error", response.toString());
        }
        ((MainActivity) getActivity()).sauvegarderList(listStationsDeBusString);

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("marker",":"+marker.getTitle());
        try {
            FrameLayout fl = (FrameLayout) view.findViewById(R.id.layoutBouton);
            fl.setVisibility(View.VISIBLE);
            ImageButton ib = (ImageButton) view.findViewById(R.id.boutonGallerie);
            fl.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).marker = marker;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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
