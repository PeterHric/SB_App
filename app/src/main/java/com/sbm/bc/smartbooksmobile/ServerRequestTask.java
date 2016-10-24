package com.sbm.bc.smartbooksmobile;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

/**
 * @brief Responsible for sending and retrieving data from server DB
 * Result is then available asynchronously, or synchronously via getServerRespose()
 */
public class ServerRequestTask extends AsyncTask <String, Void, String>
{

    // Callback to register from client class
    public interface TaskListener {
        public void onFinished(String result);
    }

    private final TaskListener mTaskListener;

    // C-tor with mandatory callback:
    public ServerRequestTask (TaskListener tl)
    {
        this.mTaskListener = tl;
    }

    String mServerResponse = "Initial";

    public String getServerRespose()
    {
        return mServerResponse;
    }

    @Override
    protected String doInBackground(String... stringJSONReq)
    {
        // Set X to denote network was inaccessible, or other failure.
        String serverResponse = "X";

        try
        {
            // Send req. to server via JSON
            serverResponse = JsonSender.postJsonText(JsonSender.phpServerUrl , stringJSONReq[0]);
        }
        catch (IOException ioe)
        {
            Log.println(Log.ERROR,"Exception caught !", "Exception on retrieval kids of parent via JSON : " + ioe.getMessage());
            // ToDo: Show problem to user, and provide mechanism to retry data acquisition !
        }

        return serverResponse;
    }

    @Override
    protected void onPostExecute(String response)
    {
        mServerResponse = response;
        this.mTaskListener.onFinished(mServerResponse);
    }

}
