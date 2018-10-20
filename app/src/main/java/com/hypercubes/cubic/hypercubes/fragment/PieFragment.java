package com.hypercubes.cubic.hypercubes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fraud.FraudProviderInterface;

import java.util.ArrayList;
import java.util.List;

public class PieFragment extends Fragment {

    // Static tags
    private static final String TAG = "PieFragment";

    // UI
    PieChart pieChart;

    FraudProviderInterface mFraudProvider;

    public PieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        setPieChartData();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FraudProviderInterface) {
            mFraudProvider = (FraudProviderInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void setPieChartData(){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(27f, "Hyde Park"));
        entries.add(new PieEntry(63f, "Windsor Park"));

        PieDataSet set = new PieDataSet(entries, "Fraud by Area");
        int[] pieChartColors = getActivity().getResources().getIntArray(R.array.primaries);
        set.setColors(pieChartColors);

        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
