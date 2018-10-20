package com.hypercubes.cubic.hypercubes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fraud.FraudProviderInterface;

import java.util.ArrayList;

public class HeatMapFragment extends Fragment implements OnMapReadyCallback{

    // Static tags
    private static final String TAG = "HeatMapFragment";

    // UI
    private SupportMapFragment mapFragment;

    // Member variable
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    FraudProviderInterface mFraudProvider;

    public HeatMapFragment() {
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
        View view = inflater.inflate(R.layout.fragment_heat_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize map
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fraud_heat_map);
        mapFragment.getMapAsync(this);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // For now, create a bunch of random points
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        LatLng sanDiego = new LatLng(32.715736, -117.161087);
        list.add(sanDiego);
        for(int i = 0; i < 90; i++){
            float random1 = ((float) Math.random() % 100)/ 100;
            float random2 = ((float) Math.random() % 100)/ 100;
            LatLng randomLatLng = new LatLng(sanDiego.latitude + random1, sanDiego.longitude + random2);
            list.add(randomLatLng);
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }
}
