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

    static public String guardServerUrl = "http://www.brainycoach.com/guard/api/index.php";

    // JSON>> Build authentication string with received credentials
    static public String getLoginCredentialString (String nameOrEmail, String pwd)
    {
        return "{" +
                "\"serviceName\" : \"GuardService\"," +
                "\"methodName\" : \"checkLicence\"," +
                "\"parameters\" : [\"" + nameOrEmail + "\",\"" + pwd +
                "\"]" +
                "}";
    }


    // JSON>> Build string to retrieve all users (kids) associated with a customer (parent)
    static public String getKidsOfParentString (String customerEmailOrName)
    {
        return "{" +
                "\"serviceName\" : \"LicenceServiceMobile\"," + //ToDo: Find serviceName
                "\"methodName\" : \"getAllUserLicenciesForUser\"," +  //ToDo: Or use : getAllUserLicenciesByCustomer() instead ! Create own service and a method !
                "\"parameters\" : [\"" + customerEmailOrName + "\"]}";
    }


    // JSON>> Build string to retrieve all themes a kid has learned (and the relevant theme/learn info)
    static public String getListOfLearnedThemesString (String userId)
    {
        return "{" +
                "\"serviceName\" : \"LearningService\"," +
                "\"methodName\" : \"getThemesResultsInfoByUserID\"," +  // or use this method: LearningService->getLastLearnedThemesFromPackageInTime
                "\"parameters\" : [\"" + userId + "\"]}";
    }

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
            return "";
        }

    }

}
