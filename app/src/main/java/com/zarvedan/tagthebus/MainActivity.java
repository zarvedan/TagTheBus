package com.zarvedan.tagthebus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.Marker;


//*******************************************************************
//
//                  TEST : Tag The Bus !
//
//
//
//                                      pour AQUAFADAS
//
//                                      par André Vaz
//                                      andrevaz1981@gmail.com
//                                      du 24/04/2015 au 29/04/2015
//
//*******************************************************************



public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,
        ListStationsFragment.OnFragmentInteractionListener,
        MyMapFragment.OnFragmentInteractionListener,
        ListPhotosStationsFragment.OnFragmentInteractionListener,
        PhotoPleinEcranFragment.OnFragmentInteractionListener{


    SectionsPagerAdapter mSectionsPagerAdapter;
    static final int REQUEST_TAKE_PHOTO = 1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public ActionBar myActionBar;

    // Le répertoire où seront stockées les photos, dans mon cas /storage/emulated/0/TagTheBus
    // Si l'utilisateur supprime l'appli, il conservera tout de même ses photos dans ce répertoire
    public File repertoirePhotos = Environment.getExternalStoragePublicDirectory("/TagTheBus");

    // Variable globale pour pouvoir la récupérer au moment de renommer
    // le fichier lorsque l'utilisateur aura saisi un nom pour sa photo
    public File monFichier;
    protected Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        myActionBar = getSupportActionBar();
        myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        myActionBar.setCustomView(R.layout.action_bar_layout);
        myActionBar.setDisplayShowCustomEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                myActionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            myActionBar.addTab(
                    myActionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //************************************
    //
    // Menu options : A prévoir
    //
    // *********************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //************************************
    //
    // Tabs methods
    //
    // *********************************

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return MyMapFragment.newInstance("", "");
                case 1:
                    return ListStationsFragment.newInstance("","");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    //********************************************************
    //
    // Méthodes appelées par clic bouton
    //
    // *******************************************************


    // Méthode appelée lors du clic sur le logo appareil photo (au-dessus de la liste de photos)
    public void prendrePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FragmentManager fm = getSupportFragmentManager();

        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fm.findFragmentById(R.id.ListPhotosStationsContainer);
        String nomStation = fragment.nomStationBus;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File fichierDestination = null;
            try {
                fichierDestination = creerFichier(nomStation);
            } catch (IOException e) {
                Log.d("Erreur créationFichier ",": "+e);
            }

            if (fichierDestination != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fichierDestination));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // Lors d'un clic sur un marker un bouton  apparait en haut a droite, sous le bouton de géolocalisation
    // Lorsque ce bouton est cliqué, cette méthode est appelée pour afficher la liste des photos de cette station
    public void voirListePhotos(View view){

        myActionBar = getSupportActionBar();
        myActionBar.hide();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ListPhotosStationsFragment fragment = new ListPhotosStationsFragment();
        try {
            fragment.recupererNomStation(marker.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ft.replace(R.id.ListPhotosStationsContainer,fragment);
        ft.addToBackStack(null);
        ft.show(fragment);
        ft.commit();
    }


    // Méthode appelée lorsque la photo est enregistrée par l'utilisateur
    //On ouvre un AlertDialog pour qu'il puisse saisir le titre souhaité
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                FragmentManager fm = getSupportFragmentManager();
                AlertDialogTitrePhotoFragment alertdFragment = new AlertDialogTitrePhotoFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(alertdFragment, "Titre");
                ft.commitAllowingStateLoss();
            }
        }
    }

    // Méthode appelée lorsque l'utilisateur a saisi le titre de sa photo
    protected void enregistrerPhoto(String titrePhoto){
        if (this.monFichier != null) {
            File monFichierAvecTitre = null;
            try {
                monFichierAvecTitre = new File(repertoirePhotos, titrePhoto + "@" + this.monFichier.getName());
                Boolean renommageReussi = this.monFichier.renameTo(monFichierAvecTitre);
                if (!renommageReussi) {
                    Log.d("enregistrerPhoto ", "KO:" + monFichierAvecTitre.getName());
                } else {
                    Log.d("enregistrerPhoto ", "OK");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Création du du fichier du type titre@station%date_heure*millisecondes
    // Les photos sont stockées dans le répertoire public source du téléphone android + TagTheBus
    // Dans mon cas /storage/emulated/0/TagTheBus
    // Si l'utilisateur supprime l'appli, il conservera tout de même ses photos dans ce répertoire
    protected File creerFichier(String nomPhoto) throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = nomPhoto +"%"+ timeStamp+"*";
        String nomDuFichier = imageFileName+".jpg";

        if (!repertoirePhotos.exists()) {
            Boolean repertoireCree = repertoirePhotos.mkdir();
            if (repertoireCree){
                Log.d("REPERTOIRE CREE","OK");
            }
        }
        this.monFichier = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                repertoirePhotos      /* directory */
        );
        // nom de fichier temp car l'utilisateur, en ajoutant un titre à la photo, va renommer le fichier
        Log.d("CREER FICHIER","nom de fichier temp :"+monFichier.getAbsolutePath());
        return monFichier;
    }


    // Gestion du bouton Back sur certains cas de figure
    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fm.findFragmentById(R.id.ListPhotosStationsContainer);

        if(fragment != null){
            //Le fragment existe, on laisse "back" fonctionner normalement
            // c'est-à-dire avec la gestion des backstack des fragments
            super.onBackPressed();
        }
        if(fragment == null) {
            //Le fragment n'existe pas, on affiche un AlertDialog pour demander
            // à l'utilisateur de confirmer la fermeture de l'appli
            AlertDialogQuitFragment alertdFragment = new AlertDialogQuitFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(alertdFragment, "Quit");
            ft.commit();
        }
    }

    //****************************************
    //
    // Méthodes d'interface à implémenter
    //
    //***************************************

    @Override
    public void onFragmentInteractionListStations(String id) {

    }

    @Override
    public void onFragmentInteractionMap(Uri uri) {

    }

    @Override
    public void onFragmentInteractionListPhotosStations(String id) {

    }

    @Override
    public void onFragmentInteractionPhotoPleinEcran(Uri uri) {

    }

}
