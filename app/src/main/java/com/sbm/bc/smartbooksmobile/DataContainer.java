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
        public String mFirstNameCustomer = "";
        public String mLastNameCustomer  = "";
        public int mIdCustomer           = -1;
        public int mIdUser               = -1;
        public String mContact           = "";
        public String mEmail             = "";
    }
    LoginActivityData mLAData = new LoginActivityData();



    // ------------------- Main User Activity Data ----------------
    static boolean hasMainActivityData     = false;
    // @brief This contains whole JSON returned line of user of type = 'CHILD'
    static public String mMAServerResponse = "";
    // @brief This contains parsed basic data about user (child of the logged customer)
    static public class MainActivityData
    {
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
    // @brief This contains whole JSON returned line after learning report request of particular user
    public String mLSFServerResponse      = "";
    // @brief A data container class to hold parsed report about learning
    static public class LearningSurveyFragmentData
    {
        //ToDo: Fill in data members - or just display report as web content formatted by PHP sever
        boolean hasLearningSurveyFgmData     = false;

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
    }
    ArrayList<LearningSurveyFragmentData> mListLSFData = new ArrayList<LearningSurveyFragmentData>();



    // ------------------- Knowledge By Subjects Fragment Data ----------------
    boolean hasKnowledgeBySubjectsFgmData = false;
    // @brief This contains whole JSON returned line after weekly view report request of particular user
    public ArrayList<String> mKnowledgeBySubjectsSurvey = new ArrayList<>();



    // ------------------- Weekly Survey Fragment Data ----------------
    boolean hasWeekReportFgmData = false;
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
