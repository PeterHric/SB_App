package com.sbm.bc.smartbooksmobile;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivitySwipeTabs extends AppCompatActivity
{
    ///< A reference to the data container - singleton existing all through apps lifetime.
    private DataContainer mDc = null;

    // ToDo: Should we put these to the global DataContainer ??
    private int mIdActiveKidIdx = 0;
    private int mIdActiveKidUserId = mDc.mListMAData.get(mIdActiveKidIdx).mIdUser;

    /**
     * @brief Types of fragments existing in this activity
     */
    enum FragmentName
    {
        LEARNING_SURVEY,
        KNOWLEDGE_BY_SUBJECT,
        WEEKLY_REPORT,

        FRAGMENT_MAX
    }

    /**
     * @brief This is to distinguish which fragment is currently active
     */
    private FragmentName mActiveFragment;


    ///< This is to lock access to the asynchronous task, which is a critical - shared resource.
    Lock mAsyncTaskLock = new ReentrantLock();

    // ---------------------------------------------------------------------------------------
    // Listener for the async task instance - wait to finish data retrieval
    private ServerRequestTask.TaskListener taskListener = new ServerRequestTask.TaskListener()
    {
        @Override
        public void onFinished(String serverResponse)
        {
            // Retrieve and set appropriate data - based on which fragment is active (has sent req.).
            switch (mActiveFragment)
            {
                case LEARNING_SURVEY:
                    mDc.mLSFServerResponse = serverResponse; // Store the server response for later usage
                    parseLearningSurveyData(serverResponse); // Call processing method
                    break;
                case KNOWLEDGE_BY_SUBJECT:
                    // Call processing method
                    break;
                case WEEKLY_REPORT:
                    // Call processing method
                    break;
                default:
                    break;
            }
        }
    };


    // And the async task instance itself - used for asynchronous data retrieval from server DB
    private ServerRequestTask mServRqTask = new ServerRequestTask(taskListener);
    // ---------------------------------------------------------------------------------------


    /*
    Can use these for quick messages to show on touch on day in weekly view,
    Or you can use Toast, or SnackBar messages

    showDialog("Downloaded " + result + " bytes");

    public void showMessage(String tag, String message)
    {
      String s = tag + ":" + message;
      Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
      toast.show();
      Log.d(tag,message);
    }
    */

    private void parseLearningSurveyData(String serverResponse)
    {
        try
        {
            JSONArray jsnArray = new JSONArray(serverResponse);

            // Exit on ill data
            if(jsnArray == null || jsnArray.length() == 0)  return;

            //mIdActiveKidUserId , mIdActiveKidIdx

            // Retrieve data the currently active kid
            JSONObject jsnObj  = null;
            for (int i = 0; i < jsnArray.length(); ++i)
            {
                jsnObj= jsnArray.getJSONObject(i);
                // Go next item on no data
                if(jsnObj == null)  return;

                DataContainer.LearningSurveyFragmentData lsfd = new DataContainer.LearningSurveyFragmentData();
                lsfd.mIdUser        = jsnObj.getInt("id");
                lsfd.mFirtsNameUser = jsnObj.getString("firstName");
                lsfd.mLastNameUser  = jsnObj.getString("lastName");
                lsfd.mIsAccepted    = jsnObj.getInt("isAccepted")  > 0 ? true : false;
                lsfd.mIsActivated   = jsnObj.getInt("isActivated") > 0 ? true : false;
                lsfd.mIsDeleted     = jsnObj.getInt("isDeleted")   > 0 ? true : false;
                lsfd.mLoginAt       = Timestamp.valueOf(jsnObj.getString("loginAt"));
                lsfd.mLastLoginAt   = Timestamp.valueOf(jsnObj.getString("lastLoginAt"));

                mDc.mListLSFData.add(lsfd);
            }

            // Mark that we have just set data for this kid
            mDc.mListLSFData.get(mIdActiveKidIdx).hasLearningSurveyFgmData = true;

            CharSequence toShow = "Zistene udaje ucenia pre dieta: " + mIdActiveKidIdx;

            Snackbar.make(findViewById(R.id.fab), toShow, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }
        catch (JSONException jsnEx)
        {
            Log.println(Log.ERROR, "JSONException: ", jsnEx.toString());
        }
        catch(Exception e)
        {
            Log.println(Log.ERROR, "Some Exception: ", e.toString());
        }
        finally
        {
            mAsyncTaskLock.unlock();
        }

    }


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    static private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Get instance reference
        mDc = DataContainer.getInstnace();

        // ------------------------------------
        // -------- Make it Full Screen -------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the (3?) primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tu bude skratka napr. refresh.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    // Add menu items here !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_swipe_tabs, menu);
        getMenuInflater().inflate(R.menu.menu_survey_tabs, menu);
        return true;
    }

    // Action bar events handling
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        /*  Custom Web View display
         * 	   String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
         *     webView.loadData(customHtml, "text/html", "UTF-8");
         * */
        private WebView webView;

        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "#";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        // Called when fragment is first time created
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;

            switch(sectionNum)
            {
                case 0: // ToDo: Create fragment_learning_survey.xml !
                    rootView = inflater.inflate(R.layout.fragment_choose_kid, container, false);
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_knowledge_by_subject, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_weekly_report, container, false);
                    break;
            }

            return rootView;
        }

        // Called each time when fragment goes to front
        @Override
        public void onStart()
        {
            super.onStart();

            // ToDo: Erase it. No need to set proper name of the fragment - this is done staticaly in the xml
            //TextView textView = (TextView) findViewById(R.id.section_label);
            //TextView textView = (TextView) findViewById(R.id.fragmentTitle);
            //textView.setText(getString(R.string.prehlad, getArguments().getInt(ARG_SECTION_NUMBER)));
            //textView.setText( mSectionsPagerAdapter.getPageTitle(sectionNum) );
            //textView.refreshDrawableState(); // ToDo: Do we need this at all ?

            // Initiate fragment's data retrieval from the server
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            String pageTitle = mSectionsPagerAdapter.getPageTitle(sectionNum);
        }

    } // End of class  PlaceholderFragment


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }


        @Override
        public int getCount() {
            // Show 3 total pages ~ fragments.
            return 3;
        }

        @Override
        public String getPageTitle(int position)
        {
            // We are entering critical section - access to async task - only one job allowed at a time
            mAsyncTaskLock.lock();

            String title = "";
            switch (position)
            {
                case 0:
                    mActiveFragment = FragmentName.LEARNING_SURVEY;
                    getLearningSurveyFragmentData();
                    title = "Celkový prehľad učenia.";
                    break;

                case 1:
                    mActiveFragment = FragmentName.KNOWLEDGE_BY_SUBJECT;
                    //getKnowledgeBySubjectsSurveyFragmentData();
                    title = "Predmety - stav vedomostí.";
                    break;

                case 2:
                    mActiveFragment = FragmentName.WEEKLY_REPORT;
                    //getWeekReportFragmentData();
                    title = "Týždňový prehľad naučených tém.";
                    break;

                default:
                    mActiveFragment = FragmentName.FRAGMENT_MAX;
                    title = "Neznamy fragment"; // This shall never occur though
                    break;
            }

            return title;
        }

        private void getLearningSurveyFragmentData()
        {
            if(!DataContainer.getInstnace().mListLSFData.get(mIdActiveKidIdx).hasLearningSurveyFgmData)
            { // Only do this, if needed data for the kid are not yet acquired
                mServRqTask.execute(JsonSender.getListOfLearnedThemesString(mIdActiveKidUserId));
            }
        }

        private void getKnowledgeBySubjectsSurveyFragmentData()
        {
            if(!DataContainer.getInstnace().hasKnowledgeBySubjectsFgmData)
            { // Only do this, if needed data are not yet acquired
                mServRqTask.execute(JsonSender.getKnowledgeBySubjectString(mIdActiveKidUserId));
            }
        }

        private void getWeekReportFragmentData()
        {
            if(!DataContainer.getInstnace().hasWeekReportFgmData)
            { // Only do this, if needed data are not yet acquired
                mServRqTask.execute(JsonSender.getWeekReportSurveyString(mIdActiveKidUserId));
            }
        }

    } // End of class SectionsPagerAdapter

} // End of class ActivitySwipeTabs
