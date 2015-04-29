package com.zarvedan.tagthebus;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

//*****************************************************************************************************
//
// Class ListPhotosStationsFragment :
//      Affiche une ListView de toutes les photos prises à cette station :
//      miniature + titre+ date de prise de vue
//
//*****************************************************************************************************

public class ListPhotosStationsFragment extends ListFragment  {

    private OnFragmentInteractionListener mListener;
    protected String nomStationBus;

    // Le répertoire où seront stockées les photos, dans mon cas /storage/emulated/0/TagTheBus
    // Si l'utilisateur supprime l'appli, il conservera tout de même ses photos dans ce répertoire
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
        this.nomStationBus = str;
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
        // Lors d'un long clic sur un item, on affiche un AlertDialog pour demander
        // à l'utilisateur s'il souhaite supprimer la photo
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                Photo photo = (Photo) av.getItemAtPosition(pos);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ActionBarActivity myActionBarActivity = (ActionBarActivity) getActivity();
        myActionBarActivity.getSupportActionBar().show();
    }

    // Lors du clic sur un itme, on l'affiche en plein écran
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PhotoPleinEcranFragment fragment = new PhotoPleinEcranFragment();
            Photo photo = (Photo) l.getItemAtPosition(position);

            ft.replace(R.id.PhotoPleinEcranContainer,fragment);
            ft.addToBackStack(null);
            ft.show(fragment);
            ft.commit();
            fragment.afficheImage(photo);
        }
    }

    // Méthode qui affiche la liste des photos spécifiques à la station "nomDeStationBus"
    // "nomDeStationBus" est initialisée par recupererNomStation appelé dans ListStationsFragment
    public void afficherList(){
        File dir = new File(repertoirePhotos.getAbsolutePath());
        File[] files = dir.listFiles();
        ArrayList<Photo> listeFichiersComplete = new ArrayList<>();
        ArrayList<Photo> listeFichiersSpecifique = new ArrayList<>();
        if (files != null) {
            for (File inFile : files) {
                listeFichiersComplete.add(new Photo(inFile));
            }
            for (Photo photo : listeFichiersComplete) {
                photo.parserInfosFichierSource(photo.fichierSource);
            }
            for (Photo photo : listeFichiersComplete) {
                if (photo.getStation().equals(nomStationBus)) {
                    //Log.d("listphotosstations", "fichierSource: " + photo.getFichierSource());
                    //Log.d("listphotosstations", "titre: " + photo.getTitre());
                    //Log.d("listphotosstations", "station: " + photo.getStation());
                    //Log.d("listphotosstations", "date: " + photo.getDate());
                    //Log.d("listphotosstations", "heure: " + photo.getHeure());
                    listeFichiersSpecifique.add(photo);
                }
            }
        }
        // On transmet les infos nécessaires à notre adapter personnalisé pour qu'il puisse afficher la liste de photos
        ArrayAdapterPersonnalise adapter = new ArrayAdapterPersonnalise(getActivity(),listeFichiersSpecifique);
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
