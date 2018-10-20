package com.hypercubes.cubic.hypercubes.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hypercubes.cubic.hypercubes.R;

import java.util.ArrayList;
import java.util.List;

public class BarFragment extends Fragment implements OnChartValueSelectedListener {

    // Static tags
    private static final String TAG = "BarFragment";

    public BarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bar, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.bar_chart);
        chart.setTouchEnabled(true);// Enable touch interaction

        SetBarChartData(chart);

        return view;
    }

    private void SetBarChartData(BarChart barChart){
        List<BarEntry> entries = new ArrayList<>();
//        BarEntry firstEntry = new BarEntry(0f, 30f);
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        int[] primaryColors = getActivity().getResources().getIntArray(R.array.primaries);
        set.setColors(primaryColors);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setOnChartValueSelectedListener(this);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "hello, is this thing on?");

    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "hello, is this thing on?");
    }
}
