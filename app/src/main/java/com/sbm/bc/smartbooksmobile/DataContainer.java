package com.sbm.bc.smartbooksmobile;

import android.provider.ContactsContract;

import java.util.ArrayList;
//import java.util.List;
import java.util.Date;

/**
 * Singleton data container to group all DB data extracted from server during apps lifetime
 */

public class DataContainer {

    short numOfKids = 0; // Counter of kids


    // ------------------- Login Activity Data ----------------
    static public boolean hasLoginActivityData = false;
    // @brief This contains whole JSON data string returned after user is logged in (i.e. info about the customer)
    //public String mLAServerResponse    = "";
    static public class LoginActivityData
    {
        public String mNameOrEmail       = "";
        public String mPassword          = "";
        public String mFirtsNameCustomer = "";
        public String mLastNameCustomer  = "";
        public int mIdCustomer           = -1;
        public int mIdUser               = -1;
        public String mContact           = "";
        public String mEmail             = "";
    }
    LoginActivityData mLAData = new LoginActivityData();



    // ------------------- Main User Activity Data ----------------
    static boolean hasMainActivityData = false;
    // @brief This contains whole JSON returned line of user of type = 'CHILD'
    static public String mMAServerResponse      = "";
    static public class MainActivityData
    {
        // @brief This contains whole JSON data string returned after user is logged in (i.e. info about the customer)
        public int mIdUser               = -1;
        public String mNameOrEmail       = "";
        public String mFirtsNameUser     = "";
        public String mLastNameUser      = "";
        public String mContact           = "";
        public String mEmail             = "";
        public Date    mLoginAt          = null;
        public Date    mLastLoginAt      = null;
        public boolean mIsDeleted        = false;
        public boolean mIsAccepted       = false;
        public boolean mIsActivated      = false;

        //public int mIdCustomer           = -1;
        //public String mPassword          = "";
    }
    ArrayList<MainActivityData> mListMAData = new ArrayList<MainActivityData>();



    // ------------------- Learning Survey Fragment Data ----------------
    boolean hasLearningSurveyFgmData = false;
    // @brief This contains whole JSON returned line after learning report request of particular user
    public ArrayList<String> mKidsLearningSurvey = new ArrayList<>();



    // ------------------- Learning Survey Fragment Data ----------------
    boolean hasWeeklyViewFgmData = false;
    // @brief This contains whole JSON returned line after weekly view report request of particular user
    public ArrayList<String> mWeeklyViewSurvey = new ArrayList<>();


    // ------------ Singleton pattern implementation ------------
    // Lock c-tor
    private DataContainer() {}

    static private DataContainer mInstance = null;

    static DataContainer getInstnace()
    {
        if (mInstance == null)
        {
            mInstance = new DataContainer();
        }

        return mInstance;
    }

}
