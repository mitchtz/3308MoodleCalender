package com.example.mitchtz.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainScreenActivity extends Activity {
    String AuthToken=null;

    //created to try and get a JSON from moodle though now it appears that we
    public String getAuthToken(String username,String password){
        InputStream is=null;
        String Auth=null;
        JSONObject jobj=null;
        String service="moodle_mobile_app";
        String url ="http://moodle.cs.colorado.edu";

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

    //created to do background network operations
    private class AuthTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            AuthToken=getAuthToken("glassc","CUcmg1989#");
            return AuthToken;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AuthToken==null){
            new AuthTask().execute("username","password");
            //TO-DO: make login unhidden

        }
        setContentView(R.layout.activity_main_screen);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

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

    /** Called when the user clicks the Send button */
    public void callMoodle(View view) {
        Intent intent = new Intent(this, DownloadCalendar.class);
        startActivity(intent);
    }

    public void viewEvents(View view){
        Intent intent = new Intent(this,Assignment_List.class);
        startActivity(intent);
    }
}
