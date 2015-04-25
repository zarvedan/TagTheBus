package com.zarvedan.tagthebus;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zarvedan.tagthebus.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListStationsFragment extends ListFragment {

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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recupererListeStationsBarcelone();
    }
    public void recupererListeStationsBarcelone(){
        String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";
        Log.d("WEBSERVICE RETES", "webservice appelé");
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WEBSERVICE OK", "Liste proprietes recupérees "+response.toString());
                        try {
                            //remplirListeProprietes(response);
                            Log.d("respone",":"+response.toString());
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEBSERVICE ERROR", "ERREUR liste proprietes"+error.toString());
            }
        }
        );
        queue.add(jsObjRequest);
    }

  /*  public void remplirListeProprietes(JSONObject response) throws Throwable {
        JSONArray jsArrTemp = null;
        JSONObject jsObjTemp = null;
        Log.d("MES PROPRIETES", response.toString());

        try {
            jsArrTemp = response.getJSONArray("lieux");

            for (int i = 0; i < jsArrTemp.length(); i++) {
                jsObjTemp = jsArrTemp.getJSONObject(i);
                Log.d("UNE PROPRIETE", jsObjTemp.getString("nom_lieu"));
                Propriete prop = new Propriete(
                        jsObjTemp.getString("nom_lieu")/*,
                        jsObjTemp.getInt("prix_initial"),
                        jsObjTemp.getString("date_achat"),
                        jsObjTemp.getLong("lat"),
                        jsObjTemp.getLong("long"),
                        jsObjTemp.getInt("id_type_lieu"),
                        jsObjTemp.getInt("somme_gain"),
                        jsObjTemp.getInt("gain_recup")*/
    // );
                /*
                maListeDeProprietes.add(prop);

            }
        } catch (JSONException e) {
            Log.w("Remplir liste error", "CATCH exception :" + e.toString());
            Log.d("JSON Error", response.toString());
        }
        afficher();
    }*/

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
        public void onFragmentInteraction(String id);
    }

}
