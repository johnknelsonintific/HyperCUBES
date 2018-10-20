package com.hypercubes.cubic.hypercubes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fraud.FraudProviderInterface;

import java.util.ArrayList;
import java.util.List;

public class FraudPerAreaFragment extends Fragment implements OnChartValueSelectedListener {

    // Static tags
    private static final String TAG = "FraudPerAreaFragment";

    // UI
    private LineChart chart;

    // Member variables
    private boolean imageViewShowing = false;
    private ImagePopup imagePopup;
    FraudProviderInterface mFraudProvider;

    public FraudPerAreaFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_fraud_per_area_chart, container, false);

        // Get a hold of layout members
        imagePopup = new ImagePopup(getActivity());

        // Initialize line chart
        chart = (LineChart) view.findViewById(R.id.fraud_per_area_chart);
        chart.setTouchEnabled(true);// Enable touch interaction

        SetLineChartData(chart);
        chart.setOnChartValueSelectedListener(this);

        // Add a touch listener so we can close image view if it's showing
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewShowing){
                }
            }
        });

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

    private void SetLineChartData(LineChart lineChart){
        List<Entry> areaOneEntries = new ArrayList<Entry>();
        List<Entry> areaTwoEntries = new ArrayList<Entry>();

        Entry c1e1 = new Entry(0f, 1f); // 0 == quarter 1
        areaOneEntries.add(c1e1);
        Entry c1e2 = new Entry(1f, 1); // 1 == quarter 2 ...
        areaOneEntries.add(c1e2);
        Entry c1e3 = new Entry(2f, 1f); // 1 == quarter 2 ...
        areaOneEntries.add(c1e3);


        Entry c2e1 = new Entry(0f, 2f); // 0 == quarter 1
        areaTwoEntries.add(c2e1);
        Entry c2e2 = new Entry(1f, 2f); // 1 == quarter 2 ...
        areaTwoEntries.add(c2e2);
        Entry c2e3 = new Entry(2f, 2f); // 1 == quarter 2 ...
        areaTwoEntries.add(c2e3);

        LineDataSet setComp1 = new LineDataSet(areaOneEntries, "Hyde Park");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(areaTwoEntries, "Windsor Park");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        // Enable highlighting
        setComp1.setHighlightEnabled(true);
        setComp2.setHighlightEnabled(true);

        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate(); // refresh
    }

    // Handle an instance of fraud being selected
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // TODO Show the image of this instance of fraud
        imagePopup.initiatePopup(ResourcesCompat.getDrawable(getResources(), R.drawable.bus_cctv, null));
        imagePopup.setFullScreen(true);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);
        imagePopup.setBackgroundColor(getResources().getColor(R.color.backgroundDark, null));
        imagePopup.viewPopup();
        // Set chart opacity to darken


    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "hello, is this thing on?");
    }
}
