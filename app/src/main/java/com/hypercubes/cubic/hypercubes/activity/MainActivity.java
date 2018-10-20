package com.hypercubes.cubic.hypercubes.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fragment.BarFragment;
import com.hypercubes.cubic.hypercubes.fragment.FraudPerAreaFragment;
import com.hypercubes.cubic.hypercubes.fragment.HeatMapFragment;
import com.hypercubes.cubic.hypercubes.fragment.PieFragment;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;
import com.hypercubes.cubic.hypercubes.fraud.FraudManager;
import com.hypercubes.cubic.hypercubes.fraud.FraudProviderInterface;

import java.io.IOException;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements FraudProviderInterface {

    //Static tags
    private static final String TAG = "MainActivity";

    private static final int ANALYTICS_BAR_FRAGMENT = 0;
    private static final int ANALYTICS_PIE_FRAGMENT = 1;
    private static final int ANALYTICS_FRAUD_PER_AREA_FRAGMENT = 2;
    private static final int ANALYTICS_HEAT_MAP_FRAGMENT = 3;

    // Member variables
    private FraudManager fraudManager = null;

    /**
     * The pager widget, which handles animation and allows swiping between analytic views
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * A simple pager adapter for our analytics UI fragments
     */
    private class AnalyticsPagerAdapter extends FragmentStatePagerAdapter {

        // Fragments for graphs
        BarFragment barFragment;
        PieFragment pieFragment;
        FraudPerAreaFragment fraudPerAreaChart;
        HeatMapFragment heatMapFragment;

        public AnalyticsPagerAdapter(FragmentManager fm,
                                     BarFragment barFragment,
                                     PieFragment pieFragment,
                                     FraudPerAreaFragment fraudPerAreaFragment,
                                     HeatMapFragment heatMapFragment) {
            super(fm);
            this.barFragment = barFragment;
            this.pieFragment = pieFragment;
            this.fraudPerAreaChart = fraudPerAreaFragment;
            this.heatMapFragment = heatMapFragment;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case ANALYTICS_BAR_FRAGMENT:
                    return barFragment;
                case ANALYTICS_PIE_FRAGMENT:
                    return pieFragment;
                case ANALYTICS_FRAUD_PER_AREA_FRAGMENT:
                    return fraudPerAreaChart;
                case ANALYTICS_HEAT_MAP_FRAGMENT:
                    return heatMapFragment;
                default:
                    return barFragment;
            }
        }

        @Override
        public int getCount() {
            return 4;//TODO Make this not hardcoded
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fraud information
        fraudManager = new FraudManager();
        try {
            fraudManager.initializeFraudManager();//
        } catch (IOException e) {
            // Handle bad call for fraud attempts
            e.printStackTrace();
        }

        // Create Analytics fragments
        BarFragment barFragment = new BarFragment();
        PieFragment pieFragment = new PieFragment();
        FraudPerAreaFragment fraudPerAreaFragment = new FraudPerAreaFragment();
        HeatMapFragment heatMapFragment = new HeatMapFragment();

        mPager = (ViewPager) findViewById(R.id.mission_pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case ANALYTICS_BAR_FRAGMENT:
                        setTitle("Fraud Frequency by Time of Day");
                        break;
                    case ANALYTICS_PIE_FRAGMENT:
                        setTitle("Fraud by Area");
                        break;
                    case ANALYTICS_FRAUD_PER_AREA_FRAGMENT:
                        setTitle("Fraud Instances by Time");
                        break;
                    case ANALYTICS_HEAT_MAP_FRAGMENT:
                        setTitle("Fare Evasion Heat Map");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mPagerAdapter = new AnalyticsPagerAdapter(getSupportFragmentManager(),
                barFragment,
                pieFragment,
                fraudPerAreaFragment,
                heatMapFragment);

        mPager.setAdapter(mPagerAdapter);
        setTitle("Analytics");
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        Log.i(TAG, "Starting service");
    }

    @Override
    public List<FraudInstance> getFraudInstances(){
        return fraudManager.getFraudInstances();
    }
}
