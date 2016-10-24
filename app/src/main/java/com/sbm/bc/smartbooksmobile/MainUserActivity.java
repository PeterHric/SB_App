package com.sbm.bc.smartbooksmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import layout.LearningSurvey;


public class MainUserActivity  extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,
                                                                     LearningSurvey.OnFragmentInteractionListener
{
    private String mNameOrEmail       = "";
    private String mPassword          = "";
    private String mFirtsNameCustomer = "";
    private String mLastNameCustomer  = "";
    private int    mIdCustomer        = 0;
    private String mId                = "";
    private String mContact           = "";
    private String mEmail             = "";

    private String mServerResponse    = "";

    // ---------------------------------------------------------------------------------------
    // Listener for the async task instance - wait to finish data retrieval
    private ServerRequestTask.TaskListener taskListener = new ServerRequestTask.TaskListener()
    {
        @Override
        public void onFinished(String serverResponse)
        {
            mServerResponse = serverResponse;

            try {
                JSONObject jsnObj = new JSONObject(mServerResponse);
                JSONArray jsnArray = jsnObj.getJSONArray("firstName");
                String kids = "Vase deti:";
                for (int i = 0; i < jsnArray.length(); ++i)
                {
                    Log.println(Log.DEBUG,"Your children: ", jsnArray.getJSONObject(i).toString());
                    kids += "  " + jsnArray.getJSONObject(i).toString();
                }

                kids += " delete this line";
                //Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
                //toast.show();
            }
            catch (JSONException jsnEx)
            {
                Log.println(Log.ERROR, "JSONException: ", jsnEx.toString());
            }
            catch(Exception e)
            {
                Log.println(Log.ERROR, "Some Exception: ", e.toString());
            }
        }
    };

    // And the async task instance itself - used for asynchronous data retrieval from server DB
    private ServerRequestTask mServRqTask = new ServerRequestTask(taskListener);
    // ---------------------------------------------------------------------------------------

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
            mNameOrEmail = extras.getString("UserName");
            mPassword = extras.getString("Pwd");
            String serverResponse = extras.getString("ServerResponse");

            try
            {
                JSONObject Jobject = new JSONObject(serverResponse);
                mFirtsNameCustomer = Jobject.getString("firstName");
                mLastNameCustomer  = Jobject.getString("lastName");
                mIdCustomer        = Jobject.getInt("idCustomer"); //getString("idCustomer");
                mId                = Jobject.getString("id");
                mContact           = Jobject.getString("contact");
                mEmail             = Jobject.getString("email");
            }
            catch (JSONException jsEx)
            {
                Log.println(Log.ERROR,"Exception caught !", "Exception on parsing JSON Server Response : " + jsEx.getMessage());
            }

            //Log.println(Log.INFO, "UserName ", mNameOrEmail);
        }

        // ToDo: Retrieve info about children and display it
    }


    @Override
    protected void onStart() //onFinishInflate()
    {
        super.onStart();

        /*
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException iex)
        {

        }
        */

        //Set user name
        if ((mFirtsNameCustomer != "") || (mLastNameCustomer != "")) {
            // ToDo: Why this returns null ?
            TextView nhun = (TextView) findViewById(R.id.NaviHeaderUserName);
            if(nhun != null)
                nhun.setText(mFirtsNameCustomer + mLastNameCustomer);
        }

        hideSoftKeyboard();

        // Start server request to obtain children data
        //mServRqTask.execute(JsonSender.getKidsOfCustomerString()); // This returns empty response. Is it no user logged in ?
        //mServRqTask.execute(JsonSender.getKidsOfParentString(mNameOrEmail));
        mServRqTask.execute(JsonSender.getKidsOfCustomerIdString(mIdCustomer));
    }

    // Hides the soft keyboard - but does not work somehow !
    public void hideSoftKeyboard()
    {
        int mysdk = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT >= 14)
        {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            );
        }
        else if (Build.VERSION.SDK_INT >= 5)
        {
            if(getCurrentFocus()!=null)
            {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
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
                LearningSurvey ls = LearningSurvey.newInstance("User Name: " + mNameOrEmail, "Password: " + mPassword);
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
                i.putExtra("UserName", mNameOrEmail);
                i.putExtra("Pwd", mPassword);
                startActivity(i);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
