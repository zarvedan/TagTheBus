package com.zarvedan.tagthebus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zarvedan on 28/04/2015.
 */

public class ArrayAdapterPersonalise extends ArrayAdapter<Photo> {

    private final Context context;
    private final ArrayList<Photo> arrayListPhotos;

    public ArrayAdapterPersonalise(Context context, ArrayList<Photo> arrayListPhotos) {
        super(context, R.layout.image_et_texte_layout, arrayListPhotos);
        this.context = context;
        this.arrayListPhotos = arrayListPhotos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View ligneListView;
        ligneListView = inflater.inflate(R.layout.image_et_texte_layout, parent, false);

        ImageView miniature = (ImageView) ligneListView.findViewById(R.id.miniature);
        Log.d("path", ": " + arrayListPhotos.get(position).getFichierSource().getAbsolutePath());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // Réduction de la qualité pour conserver de la mémoire
        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeFile(arrayListPhotos.get(position).getFichierSource().getAbsolutePath(),options);

        miniature.setImageBitmap(bitmap);

        TextView titreView = (TextView) ligneListView.findViewById(R.id.titre);
        TextView dateView = (TextView) ligneListView.findViewById(R.id.date);

     //   miniature.setImageResource(arrayListPhotos.get(position).getFichierSource().hashCode());
        titreView.setText(arrayListPhotos.get(position).getTitre());
        dateView.setText(arrayListPhotos.get(position).getDate());

        return ligneListView;
    }
}
