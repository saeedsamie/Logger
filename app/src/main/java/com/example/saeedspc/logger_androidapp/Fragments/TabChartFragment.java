package com.example.saeedspc.logger_androidapp.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saeedspc.logger_androidapp.MainActivity;
import com.example.saeedspc.logger_androidapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class TabChartFragment extends Fragment {

    int tabIdnt;
    View view;
    MainActivity mainActivity;
    View progressView;
    LinearLayout linearLayout;


    public TabChartFragment(MainActivity mainActivity, int tabId) {
        this.tabIdnt = tabId;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart_tab, container, false);

        linearLayout = view.findViewById(R.id.board);
        progressView = view.findViewById(R.id.loading_progress);
//        progressView.setVisibility(View.GONE);

        GetTabCharts getTabCharts = new GetTabCharts(mainActivity, tabIdnt);
        getTabCharts.execute();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class GetTabCharts extends AsyncTask<String, Void, Boolean> {

        private MainActivity mainAct;
        private String outputText;
        private int tabId;

        GetTabCharts(MainActivity mainAct, int tabId) {
            this.mainAct = mainAct;
            this.tabId = tabId;
        }

        protected void onPreExecute() {
            progressView.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(String... parameter) {
            try {

                URL url = new URL("http://" + mainActivity.getServerUrl() + "/" + getResources().getString(R.string.charts_info_api).trim() + "/" + tabId + "/diagrams?token=" + mainAct.getToken());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                int responseCode = connection.getResponseCode();
                //// TODO: 10/6/2017
//                int responseCode = 0;
//                try {
//                    responseCode = connection.getResponseCode();
//                } catch (Exception e) {
//                }
//                if (responseCode != 0) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                outputText = responseOutput.toString();
//                }
                String chartTittle;
                int totalCount = Integer.parseInt(outputText.split("\"total_count\":")[1].split(",")[0]);
                String backslash = ((char) 92) + "";
                for (int i = 0; i < totalCount; i++) {
                    chartTittle = outputText.split("\"title\"")[i + 1].split(",")[0].split("\"")[1];
                    String apiAdd =
                            outputText.split("\"api\":")[i + 1].split(",")[0].split("\"")[1].split("/")[1] + backslash +
                                    outputText.split("\"api\":")[i + 1].split(",")[0].split("\"")[1].split("/")[2] + backslash +
                                    outputText.split("\"api\":")[i + 1].split(",")[0].split("\"")[1].split("/")[3] + backslash +
                                    outputText.split("\"api\":")[i + 1].split(",")[0].split("\"")[1].split("/")[4];
                    GetChart getChart = new GetChart(mainAct, chartTittle);
                    getChart.execute(apiAdd);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            getProgressView().setVisibility(View.GONE);
//            textView.setText(outputText);
        }


        @Override
        protected void onCancelled() {
        }
    }

    public View getProgressView() {
        return progressView;
    }

    private class GetChart extends AsyncTask<String, Void, Boolean> {

        private MainActivity mainAct;
        private String outputText;
        private String tittle;

        StringBuilder responseOutput;

        GetChart(MainActivity mainAct, String tittle) {
            this.mainAct = mainAct;
            this.tittle = tittle;
        }

        protected void onPreExecute() {
//            progressView.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(String... apiAddress) {
            try {

                URL url = new URL("http://" + mainActivity.getServerUrl() + "/" + String.valueOf(apiAddress[0].replace("\\", "/").replace("//", "/")) + "?token=" + mainAct.getToken());
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

                int responseCode = connection.getResponseCode();
                //// TODO: 10/6/2017
//                int responseCode = 0;
//                try {
//                    responseCode = connection.getResponseCode();
//                } catch (Exception e) {
//                }
//                if (responseCode != 0) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                outputText = responseOutput.toString();
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            TextView chartsData = new TextView(mainAct.getBaseContext());
//            linearLayout.addView(chartsData);
//            chartsData.setText(outputText);
//            chartsData.setTextColor(Color.BLACK);
//            Random random = new Random();
//            chartsData.setBackgroundColor(Color.argb(100, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            LinearLayout thisLayout = new LinearLayout(mainAct.getBaseContext());
            thisLayout.setBackgroundColor(Color.argb(50, 135, 135, 135));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(20, 20, 20, 20);
            thisLayout.setLayoutParams(layoutParams);
            thisLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(thisLayout);

            TextView chartTittle = new TextView(mainAct.getBaseContext());
            chartTittle.setTextSize(20);
            chartTittle.setTextColor(Color.BLACK);
            chartTittle.setText(tittle);
            thisLayout.addView(chartTittle);


            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> ieEntryLabels = new ArrayList<>();


            try {
                JSONObject object = new JSONObject(outputText);
                JSONArray data = object.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    try {
                        //entry data add
                        entries.add(new Entry(Float.valueOf((data.get(i)).toString().replace("[", " ").replace("]", " ").split(",")[1]), i));

                        //ieEntryLabels data add
                        String string = String.valueOf(data.get(i)).split("\"")[1];
                        char[] str, strTemp = new char[14];
                        str = string.toCharArray();
                        if (str.length > 12) {
                            for (int j = 0; j < 12; j++) {
                                strTemp[j] = str[j];
                            }
                            strTemp[12]='.';
                            strTemp[13]='.';
                            str = null;
                            str = strTemp;
                            string = String.valueOf(str);
                        }
                        ieEntryLabels.add(string);

                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            addPieChart(entries, ieEntryLabels, thisLayout);
            addLineChart(entries, ieEntryLabels, thisLayout);
        }


        @Override
        protected void onCancelled() {
        }
    }

    private void addLineChart(ArrayList<Entry> entries,
                              ArrayList<String> ieEntryLabels, LinearLayout thisLayout) {

        LineChart lineChart = new LineChart(mainActivity.getBaseContext());
        lineChart.setMinimumHeight(600);
        lineChart.setClickable(false);
        thisLayout.addView(lineChart);


        lineChart.animateXY(2000, 2000);

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setValueTextSize(10);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            Random random = new Random();
            colors.add(Color.argb(50 + random.nextInt(200), 38, 166, 154));
        }

        dataSet.setColors(colors);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setComputedLabels(ieEntryLabels);

        lineChart.setDescription(" ");

//        for (int i = 0; colors.size() != ieEntryLabels.size(); i++) {
//            colors.remove(9 - i);
//        }
        if (colors.size() == ieEntryLabels.size())
            legend.setCustom(colors, ieEntryLabels);


        lineChart.setData(new LineData(ieEntryLabels, dataSet));

    }

    private void addPieChart(ArrayList<Entry> entries, ArrayList<String> ieEntryLabels, LinearLayout thisLayout) {

        PieChart pieChart = new PieChart(mainActivity.getBaseContext());
        pieChart.setMinimumHeight(800);
        thisLayout.addView(pieChart);


        pieChart.setRotationEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setHoleRadius(2f);
        pieChart.setClickable(false);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDescription(" ");
//        pieChart.animateXY(2000, 2000);

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(5);
        pieDataSet.setValueTextSize(10);
        pieDataSet.setDrawValues(true);
        pieDataSet.setLabel("hello");
//        pieDataSet.setHighlightEnabled(false);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            Random random = new Random();
            colors.add(Color.argb(50 + random.nextInt(200), 38, 166, 154));
        }
        pieDataSet.setColors(colors);


        ArrayList<String> nullArray = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++)
            nullArray.add(" ");

        pieChart.setData(new PieData(nullArray, pieDataSet));

        pieChart.invalidate();
        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
//        legend.setComputedLabels(ieEntryLabels);
//        for (int i = 0; colors.size() != ieEntryLabels.size(); i++) {
//            colors.remove(9 - i);
//        }
        if (colors.size() == ieEntryLabels.size())
            legend.setCustom(colors, ieEntryLabels);

    }

}
