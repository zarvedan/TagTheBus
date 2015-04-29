package com.zarvedan.tagthebus;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.File;

//********************************************
//
//  Class AlertDialogSupprimerPhotoFragment:
//      AlertDialog permettant à l'utilisateur
//      de supprimer une photo
//
//*************************************************

public class AlertDialogSupprimerPhotoFragment extends DialogFragment {
    protected Photo photoASupprimer;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_menu_delete)
                .setTitle("Supprimer "+photoASupprimer.getTitre()+" ?")
                .setMessage("Voulez-vous vraiment supprimer cette photo ?")

                .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OK", "ok");
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fm.findFragmentById(R.id.ListPhotosStationsContainer);
                        File file = new File(photoASupprimer.getFichierSource().getAbsolutePath());
                        Boolean deleted = file.delete();

                        //on rafraichi la liste de photos pour cette station
                        fragment.afficherList();
                    }
                })

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