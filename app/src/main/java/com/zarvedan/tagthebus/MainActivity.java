package com.zarvedan.tagthebus;

import java.util.ArrayList;
import java.util.Locale;

import android.net.Uri;
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


public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,
        ListStationsFragment.OnFragmentInteractionListener,
        MyMapFragment.OnFragmentInteractionListener,
        ListPhotosStationsFragment.OnFragmentInteractionListener
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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    public FragmentManager fm = getSupportFragmentManager();
    public ListStationsFragment myListStationsFragment;
    public ActionBar myActionBar;
public ArrayList<String> listStationsBusString = new ArrayList<>();
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


/*
        myListStationsFragment = (ListStationsFragment) fm.findFragmentById(R.id.ListStationsContainer);
        if (fm.findFragmentById(R.id.ListStationsContainer) == null) {
            Log.d("container","is null");
            fm.beginTransaction().add(R.id.ListStationsContainer, myListStationsFragment).commit();
        }
        if(myListStationsFragment == null){
            Log.d("fragment","is null");
        }else{
            Log.d("fragment","is not null");
        }*/
    }

    public void sauvegarderList(ArrayList<String> arrayList){
        Log.d("h","Arraylist:"+arrayList.toString());
        listStationsBusString = arrayList;
    }

    public ArrayList<String> recupererListeStations(){
        return listStationsBusString;
    }

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

}
