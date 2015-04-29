package com.zarvedan.tagthebus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.plus.PlusOneButton;

//*******************************************************
//
//    Class PhotoPleinEcranFragment:
//      Affiche la photo sélectionnée en plein écran
//
//********************************************************

public class PhotoPleinEcranFragment extends Fragment {

    // The URL to +1.  Must be a valid URL.
    // URL par défaut : -> changement à prévoir
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private PlusOneButton mPlusOneButton;

    private OnFragmentInteractionListener mListener;
    protected String path;
    protected View view;
    private Bitmap rotatedBitmap;
    protected Photo photo;


    public static PhotoPleinEcranFragment newInstance(String param1, String param2) {
        PhotoPleinEcranFragment fragment = new PhotoPleinEcranFragment();
        return fragment;
    }

    public PhotoPleinEcranFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // On récupère le fichier dont l'adresse est "path" et on l'affiche
    // "path" est initialisé dans "afficheImage" appelé dans ListPhotosStationsFragment
    // On réduit la qualité et on pivote notre bitmap avant de l'afficher dans notre ImageView
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_photo_plein_ecran, container, false);
        ImageView photoPleinEcran = (ImageView) view.findViewById(R.id.photoPleinEcran);
        TextView titrePhoto = (TextView) view.findViewById(R.id.titrePhoto);

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // Réduction de la qualité pour conserver de la mémoire
            options.inSampleSize = 4;
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            Matrix matrix = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path,options);
            matrix.postRotate(90);
            rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
            // On libère de l'espace mémoire en recyclant le bitmap
            bitmap.recycle();

            photoPleinEcran.setImageBitmap(rotatedBitmap);
            titrePhoto.setText(this.photo.getTitre());

        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        return view;
    }

    // Méthode appelée dans ListPhotosStationsFragment
    // Permet d'initialiser "path" et "photo" utilisés dans onCreateView

    public void afficheImage(Photo photo){
        this.photo = photo;
        this.path = this.photo.getFichierSource().getAbsolutePath();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
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
        // On libère de l'espace mémoire en recyclant le bitmap
        rotatedBitmap.recycle();
        rotatedBitmap = null;

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
        public void onFragmentInteractionPhotoPleinEcran(Uri uri);
    }

}
