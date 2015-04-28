package com.zarvedan.tagthebus;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zarvedan.tagthebus.dummy.DummyContent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ListPhotosStationsFragment extends ListFragment  {

    private OnFragmentInteractionListener mListener;
    protected String nomStationBus;
    public File repertoirePhotos = Environment.getExternalStoragePublicDirectory("/TagTheBus");

    public static ListPhotosStationsFragment newInstance() {
        ListPhotosStationsFragment fragment = new ListPhotosStationsFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListPhotosStationsFragment() {
    }

    public void recupererNomStation(String str) {
        nomStationBus = str;
        Log.d("nom station recupere ", ": "+str);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("listphotosstations","ONCREATE");
        afficherList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.list_photos_stations_fragment_layout, container, false);
        TextView nomStationTextView = (TextView) view.findViewById(R.id.nomStation);
        nomStationTextView.setText(nomStationBus);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
            {
                Log.d("LONG click", "");
                Photo photo = (Photo) av.getItemAtPosition(pos);
                Log.d("supprimer", "station: "+photo.getFichierSource().getAbsolutePath());

                FragmentManager fm = getActivity().getSupportFragmentManager();
                AlertDialogSupprimerPhotoFragment alertdFragment = new AlertDialogSupprimerPhotoFragment();
                alertdFragment.setPhotoASupprimer(photo);
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(alertdFragment, "Supprimer");
                ft.commitAllowingStateLoss();

                return true;
            }
        });
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
    public void onResume() {
        super.onResume();
        Log.d("ONRESUME", "ONRESUME: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ActionBarActivity myActionBarActivity = (ActionBarActivity) getActivity();
        myActionBarActivity.getSupportActionBar().show();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            PhotoPleinEcranFragment fragment = new PhotoPleinEcranFragment();
            Photo photo = (Photo) l.getItemAtPosition(position);

            fragmentTransaction.replace(R.id.PhotoPleinEcranContainer,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();

            Log.d("clicked",""+photo.getFichierSource().getAbsolutePath());
            fragment.afficheImage(photo.getFichierSource().getAbsolutePath());
            mListener.onFragmentInteractionListPhotosStations(DummyContent.ITEMS.get(position).id);
        }
    }

    public void afficherList(){
        File dir = new File(repertoirePhotos.getAbsolutePath());
        File[] files = dir.listFiles();
        ArrayList<Photo> listNomsDeFichierComplet = new ArrayList<>();
        ArrayList<Photo> listNomsDeFichierSpecifique = new ArrayList<>();
        for (File inFile : files) {
            listNomsDeFichierComplet.add(new Photo(inFile));
        }
        for (Photo photo : listNomsDeFichierComplet){
            photo.parserInfosFichierSource(photo.fichierSource);
        }
        for (Photo photo : listNomsDeFichierComplet) {
            Log.d("photo.getStation()",": "+photo.getStation()+"nomStationBus: "+nomStationBus);
            if(photo.getStation().equals(nomStationBus)) {
                Log.d("listphotosstations", "fichierSource: " + photo.getFichierSource());
                Log.d("listphotosstations", "titre: " + photo.getTitre());
               // titres.add(photo.getTitre());
                Log.d("listphotosstations", "station: " + photo.getStation());
                Log.d("listphotosstations", "date: " + photo.getDate());
                Log.d("listphotosstations", "heure: " + photo.getHeure());
                //dateEtHeure.add(photo.getDate() + " - " + photo.getHeure());
                listNomsDeFichierSpecifique.add(photo);
            }
        }

        ArrayAdapterPersonalise adapter = new ArrayAdapterPersonalise(getActivity(),listNomsDeFichierSpecifique);
        setListAdapter(adapter);
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
        public void onFragmentInteractionListPhotosStations(String id);
    }

}
