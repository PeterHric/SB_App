package com.sbm.bc.smartbooksmobile;

import android.content.Intent;
import android.content.pm.LabeledIntent;
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

import java.sql.Timestamp;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.LearningSurvey;


public class MainUserActivity  extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,
                                                                     LearningSurvey.OnFragmentInteractionListener
{
    // A reference to the data container - singleton existing all through apps lifetime.
    private DataContainer mDc = null;

    // ---------------------------------------------------------------------------------------
    // Listener for the async task instance - wait to finish data retrieval
    private ServerRequestTask.TaskListener taskListener = new ServerRequestTask.TaskListener()
    {
        @Override
        public void onFinished(String serverResponse)
        {
            // Store the server response for later usage
            mDc.mMAServerResponse = serverResponse;

            // Retrieve and set complete MainActivityData
            try {
                JSONArray jsnArray = new JSONArray(mDc.mMAServerResponse);

                // Exit on ill data
                if(jsnArray == null || jsnArray.length() == 0)  return;

                // Reset counter and clear data container
                mDc.numOfKids = 0;
                mDc.mListMAData.clear();

                // Retrieve data of all kids of the customer logged in
                JSONObject jsnObj  = null;
                for (int i = 0; i < jsnArray.length(); ++i)
                {
                    jsnObj= jsnArray.getJSONObject(i);
                    // Go next item on no data
                    if(jsnObj == null)  return;

                    DataContainer.MainActivityData mad = new DataContainer.MainActivityData();
                    mad.mIdUser        = jsnObj.getInt("id");
                    mad.mFirtsNameUser = jsnObj.getString("firstName");
                    mad.mLastNameUser  = jsnObj.getString("lastName");
                    mad.mIsAccepted    = jsnObj.getInt("isAccepted")  > 0 ? true : false;
                    mad.mIsActivated   = jsnObj.getInt("isActivated") > 0 ? true : false;
                    mad.mIsDeleted     = jsnObj.getInt("isDeleted")   > 0 ? true : false;
                    try
                    {
                        String sTimeStamp = jsnObj.getString("loginAt");
                        mad.mLoginAt      = Timestamp.valueOf(sTimeStamp == null ? "01.01.1900" : "sTimeStamp");
                        sTimeStamp        = jsnObj.getString("lastLoginAt");
                        mad.mLastLoginAt  = Timestamp.valueOf(sTimeStamp == null ? "01.01.1900" : "sTimeStamp" );
                    }
                    catch (Exception e)
                    {
                        Log.println(Log.ERROR, "Some Exception: ", e.toString());
                    }

                    mDc.mListMAData.add(mad);

                    // Add LearningSurvey Data container item for the new retrieved kid !
                    mDc.mListLSFData.add(new DataContainer.LearningSurveyFragmentData());
                    // ToDo: Add WeeklyView, and KnowledgeBySubject data container items

                    ++mDc.numOfKids; // Increment kids counter
                }

                // Mark that we have just set data
                mDc.hasMainActivityData = true;

                CharSequence toShow = "Mate registrovanych " + mDc.numOfKids + " deti.";

                Snackbar.make(findViewById(R.id.fab), toShow, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Toast toast = Toast.makeText(this, "Pocet deti: " +  mDc.numOfKids, Toast.LENGTH_SHORT);
                //Toast toast = Toast.makeText(this,  toShow, Toast.LENGTH_SHORT);
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

        // create/get reference to the data container object
        mDc = DataContainer.getInstnace();

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
            String serverResponse = extras.getString("ServerResponse");

            mDc.mLAData.mNameOrEmail = extras.getString("UserName");
            mDc.mLAData.mPassword    = extras.getString("Pwd");

            // ToDo: Remember credentials to internal DB - contacts/profiles somehow.
            // if (checkedRememberMeCheckBox)

            // Now store User Credential do random data
            try
            {
                JSONObject Jobject = new JSONObject(serverResponse);
                mDc.mLAData.mIdUser            = Jobject.getInt("id");
                mDc.mLAData.mIdCustomer        = Jobject.getInt("idCustomer");
                mDc.mLAData.mFirstNameCustomer = Jobject.getString("firstName");
                mDc.mLAData.mLastNameCustomer  = Jobject.getString("lastName");
                mDc.mLAData.mContact           = Jobject.getString("contact");
                mDc.mLAData.mEmail             = Jobject.getString("email");
                mDc.mLAData.mIdCustomer        = Jobject.getInt("idCustomer");
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

        //Set user name
        if ((mDc.mLAData.mFirstNameCustomer != "") || (mDc.mLAData.mLastNameCustomer != "")) {
            // ToDo: Why this returns null ?
            TextView nhun = (TextView) findViewById(R.id.NaviHeaderUserName);
            if(nhun != null)
                nhun.setText(mDc.mLAData.mFirstNameCustomer + mDc.mLAData.mLastNameCustomer);
        }

        hideSoftKeyboard();

        // Start server request to obtain data about customer's children
        if(!mDc.hasMainActivityData)
        {
            //mServRqTask.execute(JsonSender.getKidsOfCustomerString()); // This returns empty response. Is it no user logged in ?
            //mServRqTask.execute(JsonSender.getKidsOfParentString(dc.mLAData.mNameOrEmail));
            mServRqTask.execute(JsonSender.getKidsOfCustomerIdString(mDc.mLAData.mIdCustomer));
        }
    }

    // ToDo: Put this and internet access test to utilities
    // Hides the soft keyboard - but does not work somehow !
    private void hideSoftKeyboard()
    {
        int mysdk = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT >= 14)
        {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
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


        Intent i = null;

        switch(id)
        {
            case R.id.nav_camera:
            {
                // Handle the camera action
                //LearningSurvey ls = LearningSurvey.newInstance("User Name: " + mDc.mLAData.mNameOrEmail, "Password: " + mDc.mLAData.mPassword);
                //ls.onAttach(this);
                //ls.onCreateView(this.LAYOUT_INFLATER_SERVICE,null /*this.Container*/,false); //public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
                i = new Intent(getApplicationContext(), SurveyTabs.class);
                break;
            }
            case R.id.nav_gallery:
            case R.id.nav_slideshow:
            case R.id.nav_manage:
            case R.id.nav_share:
            case R.id.nav_send:

                i = new Intent(getApplicationContext(), ActivitySwipeTabs.class);
                i.putExtra("UserName", mDc.mLAData.mNameOrEmail);
                i.putExtra("Pwd", mDc.mLAData.mPassword);
                break;
        }

        // Start the selected activity
        if(null != i) startActivity(i);

        // Close the drawer after any menu item was touched
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
