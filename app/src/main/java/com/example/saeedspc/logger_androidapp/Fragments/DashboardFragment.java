package com.example.saeedspc.logger_androidapp.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saeedspc.logger_androidapp.MainActivity;
import com.example.saeedspc.logger_androidapp.R;
import com.example.saeedspc.logger_androidapp.SectionsPageAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private ViewPager mViewPager;
    private FragmentActivity myContext;
    private MainActivity mainActivity;


    public DashboardFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TabLayout tabLayout;
        mViewPager = view.findViewById(R.id.container);


        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        GetTabsInfo getTabsInfo = new GetTabsInfo(mainActivity);
        getTabsInfo.execute();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Dashboard");


    }

    private void setupViewPager(ViewPager viewPager, ArrayList<String> tabNamesArray) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(myContext.getSupportFragmentManager());
        for (int i = 0; i < tabNamesArray.size(); i++) {
            adapter.addFragment(new TabChartFragment(mainActivity, i + 1), tabNamesArray.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class GetTabsInfo extends AsyncTask<String, Void, Boolean> {

        private MainActivity mainAct;
        private String outputText;

        GetTabsInfo(MainActivity mainAct) {
            this.mainAct = mainAct;
        }

        protected void onPreExecute() {
        }


        @Override
        protected Boolean doInBackground(String... parameter) {
            try {

                URL url = new URL("http://"+mainActivity.getServerUrl()+"/"+getResources().getString(R.string.dashboard_tab_api).trim()+"?token=" + mainAct.getToken());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
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
                    outputText = responseOutput.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (outputText != null) {
                ArrayList<String> tabNames = new ArrayList<>();
                for (int i = 0; i < outputText.split("\"name\":").length - 1; i++) {
                    tabNames.add(i, outputText.split("\"name\":")[i + 1].split(",")[0].split("\"")[1]);
                }
                setupViewPager(mViewPager, tabNames);
            }
        }


        @Override
        protected void onCancelled() {
        }
    }
}
