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

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link PhotoPleinEcranFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoPleinEcranFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoPleinEcranFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private PlusOneButton mPlusOneButton;

    private OnFragmentInteractionListener mListener;
    protected String path = new String();
    protected View view;
    private Bitmap bitmap;
    private Bitmap rotatedBitmap;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoPleinEcranFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoPleinEcranFragment newInstance(String param1, String param2) {
        PhotoPleinEcranFragment fragment = new PhotoPleinEcranFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoPleinEcranFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("PhotoPleinEcranFragment", "on create ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("PhotoPleinEcranFragment", "on createView ");
        View view =  inflater.inflate(R.layout.fragment_photo_plein_ecran, container, false);
        ImageView photoPleinEcran = (ImageView) view.findViewById(R.id.photoPleinEcran);

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // Réduction de la qualité pour conserver de la mémoire
            options.inSampleSize = 4;
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Matrix matrix = new Matrix();
            bitmap = BitmapFactory.decodeFile(path,options);
            matrix.postRotate(90);

            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,height,width,true);

            rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
            bitmap.recycle();
            bitmap = null;
            photoPleinEcran.setImageBitmap(rotatedBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("On check pathed", ": " + this.path);
        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        return view;
    }

    public void afficheImage(String path){
        this.path = path;
        Log.d("OK path afficher image", ": " + this.path);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        rotatedBitmap.recycle();
        Log.d("onDetach", "onDetach !!!!" +rotatedBitmap.isRecycled());
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
        public void onFragmentInteraction(Uri uri);
    }

}
