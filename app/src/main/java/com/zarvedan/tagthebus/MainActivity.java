package com.zarvedan.tagthebus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,
        ListStationsFragment.OnFragmentInteractionListener,
        MyMapFragment.OnFragmentInteractionListener,
        ListPhotosStationsFragment.OnFragmentInteractionListener,
        PhotoPleinEcranFragment.OnFragmentInteractionListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public FragmentManager fm = getSupportFragmentManager();
    public ListStationsFragment myListStationsFragment;
    public ActionBar myActionBar;
    protected String mCurrentPhotoPath;
    //Variables Toast
    public Toast toast;
    public int duration = Toast.LENGTH_LONG;
    public ArrayList<String> listStationsBusString = new ArrayList<>();
    public File repertoirePhotos = Environment.getExternalStoragePublicDirectory("/TagTheBus");
    public String nomDuFichier;
    public File monFichier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();

        // Set up the action bar.
        myActionBar = getSupportActionBar();
        myActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        myActionBar.setCustomView(R.layout.action_bar_layout);
        myActionBar.setDisplayShowCustomEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
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

    public void sauvegarderList(ArrayList<String> arrayList){
        Log.d("h","Arraylist:"+arrayList.toString());
        listStationsBusString = arrayList;
    }

    /* public ArrayList<String> recupererListeStations(){
         return listStationsBusString;
     }
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void refreshList(View view){

    }

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
    public void onFragmentInteraction(Uri uri) {

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
                //return ListPhotosStationsFragment.newInstance("","");
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

    public void prendrePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        FragmentManager fragmentManager = getSupportFragmentManager();

        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fragmentManager.findFragmentById(R.id.ListPhotosStationsContainer);
        Log.d("VIEW",":"+fragment.nomStationBus);
        String nomStation = fragment.nomStationBus;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = creerFichier(nomStation);
            } catch (IOException e) {
                Log.d("Erreur creationFichier ",": "+e);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

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

    protected void enregistrerPhoto(String titrePhoto){
        if (monFichier==null){
            Log.d("enregistrerPhoto","NULL:"+titrePhoto);
              }
        File monFichierAvecTitre = null;
        try {
            monFichierAvecTitre = new File(repertoirePhotos, titrePhoto+"@"+monFichier.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean renommageReussi = monFichier.renameTo(monFichierAvecTitre);
        if (!renommageReussi){
            Log.d("enregistrerPhoto KO",":"+monFichierAvecTitre.getName());
        }else{
            Log.d("renommage ","OK");
        }
    }

    protected File creerFichier(String nomPhoto) throws IOException {

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = nomPhoto +"%"+ timeStamp+"*";
        nomDuFichier = imageFileName+".jpg";

        if (!repertoirePhotos.exists()) {
            Log.d("creerFichier","On crée le répertoire");
            Boolean repertoireCree = repertoirePhotos.mkdir();
            if (repertoireCree){
                Log.d("REPERTOIRE CREE","OK");
            }
        }
        monFichier = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                repertoirePhotos      /* directory */
        );

        Log.d("CREER FICHIER","nom de fichier temp :"+monFichier.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return monFichier;
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBackPressed", "BACK PRESSED ");

        ListPhotosStationsFragment fragment = (ListPhotosStationsFragment) fm.findFragmentById(R.id.ListPhotosStationsContainer);
        Boolean success = fragment.isVisible();
        if(success){
            Log.d("ListPhotosStationsFr", "non visible on demande a quitter ");
            FragmentTransaction transaction = fm.beginTransaction();
            AlertDialogQuitFragment alertdFragment = new AlertDialogQuitFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(alertdFragment, "Quit");
            ft.commit();
        }
    }*/
}
