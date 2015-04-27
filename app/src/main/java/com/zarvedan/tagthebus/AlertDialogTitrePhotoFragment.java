package com.zarvedan.tagthebus;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AlertDialogTitrePhotoFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText titrePhoto = new EditText(getActivity());
        titrePhoto.setInputType(InputType.TYPE_CLASS_TEXT);

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Titre de la photo :")
                .setMessage("Donnez un titre à votre photo :")
                .setView(titrePhoto)
                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("OK", "ok");
                        ((MainActivity) getActivity()).enregistrerPhoto(titrePhoto.getText().toString());

                    }
                })
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}