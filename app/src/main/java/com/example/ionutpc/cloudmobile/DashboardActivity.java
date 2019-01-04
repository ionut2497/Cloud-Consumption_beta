package com.example.ionutpc.cloudmobile;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;


public class DashboardActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener ,SharedPreferences.OnSharedPreferenceChangeListener{

    private  DrawerLayout drawer;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        setupSharedPreferences();
        toggle.syncState();
        //avoid recreate on rotate
        if(savedInstanceState == null){
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ConsumptionFragment()).commit();
            nav.setCheckedItem(R.id.nav_dashboard);
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_dashboard:
                Log.d("ionut test","getting in fragment");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ConsumptionFragment()).commit();

                break;
            case R.id.nav_faq:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FAQFragment()).commit();
                break;
            case R.id.nav_settings:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public  void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
    }
    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //method here for locale
        Log.d("Value setup",sharedPreferences.getString(getString(R.string.lan_key),"en"));
        setLocale(sharedPreferences.getString(getString(R.string.lan_key),"en"));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.lan_key))) {
            Log.d("Value shared",sharedPreferences.getString(getString(R.string.lan_key),"en"));
            setLocale(sharedPreferences.getString(getString(R.string.lan_key),"en"));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister  OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
