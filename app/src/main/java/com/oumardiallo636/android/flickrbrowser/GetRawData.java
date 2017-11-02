package com.oumardiallo636.android.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by oumar on 6/30/17.
 */

enum DownloadStatus {IDEL, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

public class GetRawData extends AsyncTask<String, Void , String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;

    private final OnDownloadComplete mCallback;

    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callBalk) {
        this.mDownloadStatus = DownloadStatus.IDEL;
        mCallback = callBalk;
    }

    void runOnsameThread(String uri){
        Log.d(TAG, "runOnsameThread: starts");
//        onPostExecute(doInBackground(uri));
        if(mCallback != null){
            String result = doInBackground(uri);
            mCallback.onDownloadComplete(result, DownloadStatus.OK);
        }

        Log.d(TAG, "runOnsameThread: Ends");
    }

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: parameter = "+s);
        if (mCallback != null){
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(strings == null){
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try{
            mDownloadStatus=DownloadStatus.PROCESSING;

            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int Response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Response code is "+ Response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //Here we are reading the text line by line.But when reading line by line the new line character
            //is usually removed that why we are appending it at the end
            for(String line = reader.readLine(); line!=null; line = reader.readLine()){
                result.append(line).append("\n");
            }

            //another way to read from the inputstream
//            String line;
//            while (null!=(line = reader.readLine())){
//                result.append(line).append("\n");
//            }
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+e.getMessage() );
        }catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception Reading Data "+e.getMessage());
        }catch (SecurityException e ){
            Log.e(TAG, "doInBackground: Security Exception. Needs Permission?" +e.getMessage() );
        }finally {
            if(connection != null){
                connection.disconnect();
            }

            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error close stream "+e.getMessage());
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

}
