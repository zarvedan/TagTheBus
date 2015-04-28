package com.zarvedan.tagthebus;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.File;


public class AlertDialogSupprimerPhotoFragment extends DialogFragment {
    protected Photo photoASupprimer;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("bundle",":"+savedInstanceState);
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(android.R.drawable.ic_menu_delete)
                        // Set Dialog Title
                .setTitle("Supprimer "+photoASupprimer.getTitre()+" ?")
                        // Set Dialog Message
                .setMessage("Voulez-vous vraiment supprimer cette photo ?")

                        // Positive button
                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OK", "ok");
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fm.findFragmentById(R.id.ListPhotosStationsContainer);
                        //File dir = fragment.repertoirePhotos;
                        File file = new File(photoASupprimer.getFichierSource().getAbsolutePath());
                        Boolean deleted = file.delete();
                        Log.d("DELETED", "on a supprimé"+deleted);
                        fragment.afficherList();
                    }
                })
                        // Negative Button
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("KO", "ko");
                    }
                }).create();
    }

    public void setPhotoASupprimer(Photo photoASupprimer){
        this.photoASupprimer = photoASupprimer;
    }

}