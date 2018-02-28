package com.project.nanfang.leweiremotectrl;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by peng- on 2017-10-25.
 */

public class CtrlATLoader extends AsyncTaskLoader<String> {

    private String result = "";
    private String func = "";

    public CtrlATLoader(Context context, String function) {
        super(context);

        func = function;
    }


    @Override
    public String loadInBackground() {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.lewei50.com")
                .appendPath("api").appendPath("V1").appendPath("gateway").appendPath("excuteCommand")
                .appendPath("01")
                .appendQueryParameter("UserKey", "61ae3f8608624a17867efdb95252dc3e")
                .appendQueryParameter("f", func);
        String urlStr = builder.build().toString();

        URL url = null;

        String readStr = "";
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; c.harset=UTF-8");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            while ((readStr = reader.readLine()) != null) {
                buffer.append(readStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    @Override
    protected void onStartLoading() {
        if (result == "") {
            forceLoad();
        } else {
            deliverResult(result);
        }
     }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
        result = data;
    }
}
