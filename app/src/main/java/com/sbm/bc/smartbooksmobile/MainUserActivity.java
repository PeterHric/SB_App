package com.sbm.bc.smartbooksmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import layout.LearningSurvey;


public class MainUserActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   LearningSurvey.OnFragmentInteractionListener {

    String userName = "";
    String pwd = "";

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        // ToDo:  What should be done here ?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ------------------------------------
        // -------- Make it Full Screen -------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ------------------------------------

        setContentView(R.layout.activity_main_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // String value = intent.getStringExtra("key"); //if it's a string you stored.
        Bundle extras = getIntent().getExtras();

        // Extract data sent by LoginActivity
        if(extras !=null)
        {
            userName = extras.getString("UserName");
            pwd      = extras.getString("Pwd");
            Log.println(Log.INFO , "UserName " , userName);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_user, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.nav_camera:
            {
                // Handle the camera action
                LearningSurvey ls = LearningSurvey.newInstance("User Name: " + userName,"Password: " + pwd);
                ls.onAttach(this);

                //ls.onCreateView(this.LAYOUT_INFLATER_SERVICE,null /*this.Container*/,false); //public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
                break;
            }
            case R.id.nav_gallery:
            case R.id.nav_slideshow:
            case R.id.nav_manage:
            case R.id.nav_share:
            case R.id.nav_send:

                Intent i = new Intent(getApplicationContext(), ActivitySwipeTabs.class);
                //i.putExtra("UserName", mNameOrEmail);
                //i.putExtra("Pwd", mPassword);
                startActivity(i);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
