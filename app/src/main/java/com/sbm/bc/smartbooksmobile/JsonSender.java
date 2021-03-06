package com.sbm.bc.smartbooksmobile;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by peter on 26.9.2016.
 */

public final class JsonSender {

    // Guard - Jano Vlk
    //static public String phpServerUrl = "http://www.brainycoach.com/guard/api/index.php";
    // Brainy - Mario Kahun -- Old stuff !
    // static public String phpServerUrl = "http://www.brainycoach.com/_api/api/bc/index.php";
    // Mobile service - Peto Hric 'n Silvo Adamik
    //static public String phpServerUrl = "http://www.brainycoach.com/bc-mobile-client-server/index.php";
    // Peter H. Local service:
    static public String phpServerUrl = "http://172.30.20.16/bc-mobile-client-server/index.php";
    //static public String phpServerUrl = "http://192.168.1.14/bc-mobile-client-server/index.php";

    // Tmp - for testing where Mario copied his new stuff:
    //static public String phpServerUrl = "http://www.brainycoach.com/app/api/index.php";

    // !! ###### THE ULTIMATE - Life - Sharp Server ####### !!
    //static public String phpServerUrl = "http://www.smartbooks.sk/app/api/index.php";

    // JSON>> Build authentication string with received credentials
    static public String getLoginCredentialString (String nameOrEmail, String pwd)
    {
        //  public function login($email, $password, $appkey, $code, $autoLogin)
        return "{" +
                "\"serviceName\" : \"SecurityService\"," +
                "\"methodName\" : \"login\"," +
                "\"parameters\" : [\"" + nameOrEmail + "\",\"" + pwd + "\",\"" + 123 /*appkey*/ + "\",\"" + 456 /*code*/ + "\",\"" + 0 /*autoLogin*/ +
                "\"]" +
                "}";
    }

/*
    // JSON>> Build string to retrieve all users (kids) associated with a customer (parent)
    static public String getKidsOfParentString (String customerEmailOrName)
    {
        return "{" +
                "\"serviceName\" : \"LicenceService\"," +
                "\"methodName\" : \"getAllUserLicenciesForUser\"" +  //ToDo: Or use : getAllUserLicenciesByCustomer() instead !
                //",\"parameters\" : [\"" + customerEmailOrName + "\"]}";
                "}";
    }

    // JSON>> Build string to retrieve all users (kids) associated with a customer (parent)
    static public String getKidsOfCustomerString ()
    {
        return "{" +
                "\"serviceName\" : \"LicenceService\"," +
                "\"methodName\" : \"getAllUserLicenciesByCustomer\"" +  //ToDo: Or use : getAllUsersDataOfCustomer() instead !
                "}";
    }
*/
    // JSON>> Build string to retrieve all users (kids) associated with a customer (parent)
    static public String getKidsOfCustomerIdString (int customerId)
    {
        return "{" +
                "\"serviceName\" : \"MobileReportService\"," +
                "\"methodName\" : \"getAllUsersDataOfCustomer\"" +
                ",\"parameters\" : [\"" + customerId + "\",\"" + 0 /*User Type = CHILD*/ + "\"]}";
    }

    // JSON>> Build string to retrieve all themes a kid has learned (and the relevant theme/learn info)
    static public String getListOfLearnedThemesString (int userId) {
        // ToDo: Cekuj a pouzivaj StatisticsPDO::Statistics($idCustomer, $statisticsEnum, $idUser, $idPackage, $idTheme, $idKnowledge, $date)
        return "{" +
                "\"serviceName\" : \"LearningService\"," +
                "\"methodName\" : \"getThemesResultsInfoByUserID\"," +  // or use this method: LearningService->getLastLearnedThemesFromPackageInTime
                "\"parameters\" : [\"" + userId + "\"]}";
    }

    // JSON>> Build string to retrieve knowledge by subject and topic per given kid
    static public String getKnowledgeBySubjectString(int userId)
    {
        // ToDo: Cekuj a pouzivaj StatisticsPDO::Statistics($idCustomer, $statisticsEnum, $idUser, $idPackage, $idTheme, $idKnowledge, $date)
        return "{" +
                "\"serviceName\" : \"LearningService\"," +
                "\"methodName\" : \"getThemesResultsInfoByUserID\"," +  // or use this method: LearningService->getLastLearnedThemesFromPackageInTime
                "\"parameters\" : [\"" + userId + "\"]}";
    }


    // JSON>> Build string to retrieve a report for last week for given kid
    static public String getWeekReportSurveyString(int userId)
    {
        // ToDo: Cekuj a pouzivaj StatisticsPDO::Statistics($idCustomer, $statisticsEnum, $idUser, $idPackage, $idTheme, $idKnowledge, $date)
        return "{" +
                "\"serviceName\" : \"StatisticsService\"," +
                "\"methodName\" : \"getThemesResultsInfoByUserID\"," +  // or use this method: LearningService->getLastLearnedThemesFromPackageInTime
                "\"parameters\" : [\"" + userId + "\"]}";
    }

    // ---------------------------------------------------------------------------------------------------------------------------------

    static public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

    static String postJsonText(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try
        {
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (IOException ioe)
        {
            // Toast.makeText(this.getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();
            Log.println(Log.ERROR , "EXCEPTION" , "Exception when trying to post JSON string: " + json + " Reason: " + ioe.getMessage());
            return "X";
        }

    }

}
