package com.example.mitchtz.webview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;


public class DownloadCalendar extends Activity {
    WebView ourBrow;
    public String GetAuthToken(String username,String password){
        InputStream is=null;
        String Auth=null;
        JSONObject jobj=null;
        String service="moodle_mobile_app";
        String url ="moodle.cs.colorado.edu";

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("service", service));
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse Response = httpClient.execute(httpPost);
            HttpEntity httpEntity = Response.getEntity();
            is= httpEntity.getContent();
        }catch (Exception e) {
            Log.e("HTTP", "Error in http connection " + e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jobj = new JSONObject(sb.toString());
            Auth=jobj.getString("token");
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }



        return Auth ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change title bar
        getActionBar().setTitle("Browser");


        //final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        final File destinationDir = new File(Environment.getExternalStorageDirectory(), getPackageName());
        if (!destinationDir.exists()) {
            destinationDir.mkdir(); // Don't forget to make the directory if it's not there
        }


        // Use a custom layout file
        setContentView(R.layout.download_calendar);
        ourBrow = (WebView) findViewById(R.id.webView1);
        ourBrow.getSettings().setJavaScriptEnabled(true);
        ourBrow.setInitialScale(50);
        ourBrow.getSettings().setUseWideViewPort(true);
        ourBrow.setVerticalScrollBarEnabled(false);
        ourBrow.setHorizontalScrollBarEnabled(false);
        ourBrow.loadUrl("https://moodle.cs.colorado.edu/calendar/export.php");
        ourBrow.setDownloadListener(new DownloadListener() {
            public void onDownloadStart (String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                finish();
                return;
                //startActivity(new Intent(this, MainScreenActivity.class));
            }
        });

        ourBrow.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Log.d("WEB_VIEW_TEST", "error code:" + errorCode + " - " + description);
            }
            File downloadLocation = Environment.getExternalStorageDirectory();

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // handle different requests for different type of files
                // this example handles downloads requests for .apk and .mp3 files
                // everything else the webview can handle normally
                boolean shouldOverride = false;
                if (url.endsWith(".ics")) {
                    shouldOverride = true;
                    Uri source = Uri.parse(url);
                    // Make a new request pointing to the .apk url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    // appears the same in Notification bar while downloading
                    request.setDescription("Downloading Calender");
                    request.setTitle("calender.ical");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }
                    // save the file in the "Downloads" folder of SDCARD
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(downloadLocation));
                    // get download service and enqueue file
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);


                }
                // if there is a link to anything else than .apk or .mp3 load the URL in the webview
                else view.loadUrl(url);
                return shouldOverride;
            }
        });
    }

    /** Called when the user clicks the Send button */
    /*public void sendMessage(View view) {

        // Do something in response to button
    }*/
}
