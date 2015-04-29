package com.zarvedan.tagthebus;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


//********************************************
//
//  Class AlertDialogQuitFragment:
//      AlertDialog permettant à l'utilisateur
//      de confirmer la fermeture de l'appli
//
//*************************************************

public class AlertDialogQuitFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Quitter ?")
                .setMessage("Voulez-vous vraiment fermer Tag the Bus ?")

                .setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OK", "ok");
                        getActivity().finish();
                    }
                })

                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("KO", "ko");

                    }
                }).create();
    }

}