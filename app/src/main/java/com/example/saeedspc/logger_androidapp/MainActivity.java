package com.example.saeedspc.logger_androidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saeedspc.logger_androidapp.Fragments.ContactUsFragment;
import com.example.saeedspc.logger_androidapp.Fragments.DashboardFragment;
import com.example.saeedspc.logger_androidapp.Fragments.EventManagementFragment;
import com.example.saeedspc.logger_androidapp.Fragments.SettingsFragment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String profileName, profileUser, serverUrl;
    private int cun = 0;
    public String token = "";
    AlertDialog.Builder logoutRsp;
    private ProgressDialog waitingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        waitingProgress = new ProgressDialog(this);
        waitingProgress.setMessage("Waiting...");

        logoutRsp = new AlertDialog.Builder(this);
        logoutRsp.setTitle("Logout Response");
        logoutRsp.setCancelable(true);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        profileName = intent.getStringExtra("name");
        profileUser = intent.getStringExtra("username");
        serverUrl = intent.getStringExtra("serverUrl");

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentPanel, new DashboardFragment(this))
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (profileUser != null || profileName != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            TextView Name = (TextView) findViewById(R.id.profileName);
            Name.setText(profileName);
            TextView position = (TextView) findViewById(R.id.profileUser);
            position.setText(profileUser);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        switch (id) {

            case R.id.nav_dashboard:
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new DashboardFragment(this))
                        .commit();
                break;
            case R.id.nav_setting:
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new SettingsFragment())
                        .commit();
                break;
            case R.id.nav_contact_us:
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new ContactUsFragment())
                        .commit();
                break;
            case R.id.nav_event_managment:
                getFragmentManager().beginTransaction()
                        .replace(R.id.contentPanel, new EventManagementFragment())
                        .commit();
                break;
            case R.id.nav_log_out:
                SendLogoutReport sendLogoutReport = new SendLogoutReport(this);
                sendLogoutReport.execute();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (cun % 2 == 1)
            finish();
        else {
            Toast.makeText(this, "Touch again to exit", Toast.LENGTH_SHORT).show();
        }
        cun++;
    }

    public String getToken() {
        return token;
    }


    private class SendLogoutReport extends AsyncTask<String, Void, Boolean> {

        private MainActivity mainAct;
        private String outputText;

        SendLogoutReport(MainActivity mainAct) {
            this.mainAct = mainAct;
        }

        protected void onPreExecute() {
            waitingProgress.show();
        }


        @Override
        protected Boolean doInBackground(String... parameter) {
            try {

                URL url = new URL("http://"+serverUrl.trim()+"/"+getResources().getString(R.string.logout_api).trim()+"?token=" + mainAct.getToken());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                int responseCode = 0;

                try {
                    responseCode = connection.getResponseCode();
                } catch (Exception ignored) {
                }


                if (responseCode != 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder responseOutput = new StringBuilder();
                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();
                    outputText = responseOutput.toString().split("\"")[3];


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            waitingProgress.dismiss();
            if (outputText != null) {
                logoutRsp.setMessage(outputText);
            } else {
                logoutRsp.setMessage("Connection lost");
            }

            logoutRsp.create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mainAct, LoginActivity.class);
                    intent.putExtra("isLoggedIn",false);
                    mainAct.startActivity(intent);
                }
            }, 2000);

        }

        @Override
        protected void onCancelled() {
        }
    }

    public String getServerUrl() {
        serverUrl = serverUrl.trim();
        return serverUrl;
    }
}
