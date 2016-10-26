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

public class ActivitySwipeTabs extends AppCompatActivity {

    private int mIdActiveKid = 1100;

    // ---------------------------------------------------------------------------------------
    // Listener for the async task instance - wait to finish data retrieval
    private ServerRequestTask.TaskListener taskListener = new ServerRequestTask.TaskListener()
    {
        @Override
        public void onFinished(String serverResponse)
        {

            try
            {
                // ToDo: Maket his working !
                JSONObject jsnObj = new JSONObject(serverResponse);
                JSONArray jsnArray = jsnObj.getJSONArray("topicName");
                mIdActiveKid = jsnObj.getInt("userId");
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
        // ------------------------------------
        // -------- Make it Full Screen -------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ------------------------------------

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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
    public static class PlaceholderFragment extends Fragment {


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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            //View rootView = inflater.inflate(R.layout.fragment_activity_swipe_tabs, container, false);
            View rootView = inflater.inflate(R.layout.fragment_learning_survey, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView textView = (TextView) rootView.findViewById(R.id.textView5);
            //textView.setText(getString(R.string.prehlad, getArguments().getInt(ARG_SECTION_NUMBER)));
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            textView.setText( mSectionsPagerAdapter.getPageTitle(sectionNum) );

            return rootView;
        }
    } // End of class ActivitySwipeTabs

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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    getPageData();
                    return "Prehľad učenia";
                case 1:
                    return "Predmety - stav vedomostí.";
                case 2:
                    return "Časový prehľad";
            }
            return null;
        }

        private void getPageData ()
        {
            mServRqTask.execute(JsonSender.getListOfLearnedThemesString(mIdActiveKid));
        }
    }
}
