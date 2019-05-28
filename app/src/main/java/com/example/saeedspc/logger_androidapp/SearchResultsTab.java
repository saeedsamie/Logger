package com.example.saeedspc.logger_androidapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.saeedspc.logger_androidapp.R;

import static com.example.saeedspc.logger_androidapp.R.drawable.cell_border;
import static com.example.saeedspc.logger_androidapp.R.drawable.custom_disabled_button;
import static com.example.saeedspc.logger_androidapp.R.drawable.custom_enabled_button;


@SuppressWarnings("FieldCanBeLocal")
public class SearchResultsTab extends AppCompatActivity {

    private int totalNumItems = 223;
    private int itemsPerPage = 1;
    private int itemsRemaining;
    private int totalPages;
    private int currentPage = 0;

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    TableLayout mainTableLayout;
    TableRow mainTableRow;
    Button nextBtn, previousBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_tab);

        spinner = (Spinner)findViewById(R.id.per_page_spinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.per_page,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinner.getSelectedItem().toString();
                itemsPerPage = Integer.parseInt(text);
                currentPage = 0;
                toggleButtons();
                generatePage(currentPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nextBtn = (Button)findViewById(R.id.next_button);
        previousBtn = (Button)findViewById(R.id.previous_button);
        previousBtn.setEnabled(false);

        mainTableLayout = (TableLayout) findViewById(R.id.results_table);
        mainTableRow = (TableRow) findViewById(R.id.table_row_titles);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                generatePage(currentPage);
                toggleButtons();
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                generatePage(currentPage);
                toggleButtons();
            }
        });

        generatePage(currentPage);

//        createRow("e1", "r1", "g1", "l1", "s1", "d1", "u1", "p1");
//        createRow("e2", "r2", "g2", "l2", "s2", "d2", "u2", "p2");
//        createRow("e3", "r3", "g3", "l3", "s3", "d1", "u3", "p3");
//        createRow("e4", "r4", "g4", "l4", "s4", "d1", "u4", "p4");
//        createRow("e5", "r5", "g5", "l5", "s5", "d1", "u5", "p5");
//        createRow("e6", "r6", "g6", "l6", "s6", "d1", "u6", "p6");
//        createRow("e7", "r7", "g7", "l7", "s7", "d1", "u7", "p7");
//        createRow("e8", "r8", "g8", "l8", "s8", "d1", "u8", "p8");
//        createRow("e9", "r9", "g9", "l9", "s9", "d1", "u9", "p9");
//        createRow("e10", "r10", "g10", "l10", "s10", "d10", "u10", "p10");
//        createRow("e11", "r11", "g11", "l11", "s11", "d11", "u11", "p11");
//        createRow("e12", "r12", "g12", "l12", "s12", "d12", "u12", "p12");
//        createRow("e13", "r13", "g13", "l13", "s13", "d13", "u13", "p13");

    }

    public void generatePage(int currentPage){

        TableRow tableRow;
        itemsRemaining = totalNumItems % itemsPerPage;
        totalPages = totalNumItems / itemsPerPage;

        int startItem = currentPage * itemsPerPage + 1;
        int numOfData = itemsPerPage;

        mainTableLayout.removeAllViewsInLayout();

        tableRow = createRow("Event Name", "Received At", "Generated At", "Log Source", "Source IP", "Destination IP", "Username", "Protocol");
        mainTableLayout.addView(tableRow);

        if(currentPage == totalPages && itemsRemaining > 0){
            for(int i = startItem;i < startItem + itemsRemaining;i++){
                tableRow = createRow("e"+i, "r"+i, "g"+i, "l"+i, "s"+i, "d"+i, "u"+i, "p"+i);
                mainTableLayout.addView(tableRow);
            }
        }else{
            for(int i = startItem;i < startItem + numOfData;i++){
                tableRow = createRow("e"+i, "r"+i, "g"+i, "l"+i, "s"+i, "d"+i, "u"+i, "p"+i);
                mainTableLayout.addView(tableRow);
            }
        }
    }

    public TableRow createRow(String eventName, String receivedAt, String generatedAt,
                              String logSource, String sourceIP, String destinationIP,
                              String username, String protocol) {

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(mainTableRow.getLayoutParams());

        TextView textView1 = buildTextView(eventName);
        TextView textView2 = buildTextView(receivedAt);
        TextView textView3 = buildTextView(generatedAt);
        TextView textView4 = buildTextView(logSource);
        TextView textView5 = buildTextView(sourceIP);
        TextView textView6 = buildTextView(destinationIP);
        TextView textView7 = buildTextView(username);
        TextView textView8 = buildTextView(protocol);

        tableRow.addView(textView1);
        tableRow.addView(textView2);
        tableRow.addView(textView3);
        tableRow.addView(textView4);
        tableRow.addView(textView5);
        tableRow.addView(textView6);
        tableRow.addView(textView7);
        tableRow.addView(textView8);

        return tableRow;
    }

    public void toggleButtons() {
        if (currentPage == totalPages) {
            previousBtn.setEnabled(true);
            nextBtn.setEnabled(false);
        } else if (currentPage == 0) {
            previousBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            previousBtn.setEnabled(true);
            nextBtn.setEnabled(true);
        }

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//            if(!nextBtn.isEnabled())
//                nextBtn.setBackgroundDrawable(getResources().getDrawable(custom_disabled_button));
//            if(nextBtn.isEnabled())
//                nextBtn.setBackgroundDrawable(getResources().getDrawable(custom_enabled_button));
//            if(!previousBtn.isEnabled())
//                previousBtn.setBackgroundDrawable(getResources().getDrawable(custom_disabled_button));
//            if(previousBtn.isEnabled())
//                previousBtn.setBackgroundDrawable(getResources().getDrawable(custom_enabled_button));

        }
        else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                if(!nextBtn.isEnabled())
//                    nextBtn.setBackground(getResources().getDrawable(custom_disabled_button));
//                if(nextBtn.isEnabled())
//                    nextBtn.setBackground(getResources().getDrawable(custom_enabled_button));
//                if(!previousBtn.isEnabled())
//                    previousBtn.setBackground(getResources().getDrawable(custom_disabled_button));
//                if(previousBtn.isEnabled())
//                    previousBtn.setBackground(getResources().getDrawable(custom_enabled_button));
//            }
        }


    }

    private TextView buildTextView(String s){

        TextView textView = new TextView(this);

        setPadding(textView);

        setBorder(textView);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        textView.setText(s);

        return textView;
    }

    private void setBorder(TextView textView){
//        final int sdk = Build.VERSION.SDK_INT;
//        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//            textView.setBackgroundDrawable(getResources().getDrawable(cell_border));
//        }
//        else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                textView.setBackground(getResources().getDrawable(cell_border));
//            }
//        }
    }

    private void setPadding(TextView textView){
        int left = dpToPx(10);
        int top = dpToPx(10);
        int right = dpToPx(10);
        int bottom = dpToPx(10);

        textView.setPadding(left, top, right, bottom);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}