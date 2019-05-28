package com.example.saeedspc.logger_androidapp.Fragments;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;

import com.example.saeedspc.logger_androidapp.AggregationResultsTab;
import com.example.saeedspc.logger_androidapp.LiveResultsTab;
import com.example.saeedspc.logger_androidapp.R;
import com.example.saeedspc.logger_androidapp.SearchResultsTab;

public class EventManagementFragment extends Fragment {

    View view;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    ArrayAdapter<CharSequence> adapter1;
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;
    ArrayAdapter<CharSequence> adapter4;
    ArrayAdapter<CharSequence> adapter5;

    int parametersHeight;
    LinearLayout parameters;

    Button parametersButton, advanceSearch, searchProfile;


    public EventManagementFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Event Management");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_managment, container, false);

        spinner1 = view.findViewById(R.id.address_spinner);
        adapter1 = ArrayAdapter.createFromResource(getActivity(),R.array.operations,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        parameters = view.findViewById(R.id.parameters);
        parametersHeight = parameters.getLayoutParams().height;
        parameters.getLayoutParams().height = 0;


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2 = view.findViewById(R.id.port_spinner);
        adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.operations,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        parametersButton = view.findViewById(R.id.parameters_button);
        parametersButton.setOnClickListener(new View.OnClickListener() {
            int parametersState = 0;
            @Override
            public void onClick(View view) {

//                Drawable icon = getResources().getDrawable(R.drawable.button_back_minus);
//                if(parametersState == 0) {
//                    parametersButton.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    parametersState = 1;
//                    parameters.getLayoutParams().height = parametersHeight;
//                }
//                else if(parametersState == 1) {
//                    icon = getResources().getDrawable(R.drawable.button_back_plus);
//                    parametersButton.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    parametersState = 0;
//                    parameters.getLayoutParams().height = 0;
//                }
            }
        });

        spinner3 = view.findViewById(R.id.device_type_spinner);
        adapter3 = ArrayAdapter.createFromResource(getActivity(),R.array.device_type,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner4 = view.findViewById(R.id.category_spinner);
        adapter4 = ArrayAdapter.createFromResource(getActivity(),R.array.category,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter4);
        spinner4.setEnabled(false);

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner5 = view.findViewById(R.id.sub_category_spinner);
        adapter5 = ArrayAdapter.createFromResource(getActivity(),R.array.category,android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);
        spinner5.setEnabled(false);

        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        advanceSearch = view.findViewById(R.id.advance_search_button);
        advanceSearch.setOnClickListener(new View.OnClickListener() {
            int advanceSearchState = 0;
            @Override
            public void onClick(View view) {

//                Drawable icon = getResources().getDrawable(R.drawable.button_back_minus);
//                if(advanceSearchState == 0) {
//                    advanceSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    advanceSearchState = 1;
//                }
//                else if(advanceSearchState == 1) {
//                    icon = getResources().getDrawable(R.drawable.button_back_plus);
//                    advanceSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    advanceSearchState = 0;
//                }
            }
        });

        searchProfile = view.findViewById(R.id.search_profile_button);
        searchProfile.setOnClickListener(new View.OnClickListener() {
            int searchProfileState = 0;
            @Override
            public void onClick(View view) {

//                Drawable icon = getResources().getDrawable(R.drawable.button_back_minus);
//                if(searchProfileState == 0) {
//                    searchProfile.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    searchProfileState = 1;
//                }
//                else if(searchProfileState == 1) {
//                    icon = getResources().getDrawable(R.drawable.button_back_plus);
//                    searchProfile.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
//                    searchProfileState = 0;
//                }
            }
        });


        final TabHost tabHost = view.findViewById(android.R.id.tabhost);

        LocalActivityManager mlam = new LocalActivityManager(getActivity(), false);
        mlam.dispatchCreate(savedInstanceState);
        tabHost.setup(mlam);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Search Results");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Search Results");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Search Results");

        tab1.setIndicator("Search Results");
        tab2.setIndicator("Aggregation Results");
        tab3.setIndicator("Live Results");

        tab1.setContent(new Intent(getActivity(),SearchResultsTab.class));
        tab2.setContent(new Intent(getActivity(),AggregationResultsTab.class));
        tab3.setContent(new Intent(getActivity(),LiveResultsTab.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        final ScrollView scrollView = view.findViewById(R.id.scroll);
        scrollView.smoothScrollTo(0,0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                scrollView.smoothScrollTo(scrollView.getScrollX(),scrollView.getScrollY());
            }
        });





        return view;
    }

}
