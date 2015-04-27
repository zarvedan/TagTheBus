package com.zarvedan.tagthebus;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class AlertDialogQuitFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.mipmap.ic_launcher)
                        // Set Dialog Title
                .setTitle("Quitter ?")
                        // Set Dialog Message
                .setMessage("Voulez-vous vraiment fermer Tag the Bus ?")

                        // Positive button
                .setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OK", "ok");
                        getActivity().finish();
                    }
                })
                        // Negative Button
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("KO", "ko");
                    }
                }).create();
    }

}