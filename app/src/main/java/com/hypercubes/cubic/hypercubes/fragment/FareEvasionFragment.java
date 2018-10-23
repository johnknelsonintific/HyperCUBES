package com.hypercubes.cubic.hypercubes.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FareEvasionFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    // Static tags
    private static final String TAG = "FareEvasionFragment";
    public static final String FARE_EVASION_ARG = "FareEvasionArg";
    public static final String FARE_EVASION_DIALOG = "FareEvasionDialog";

    // Member variables
    private FraudInstance fraudInstance;
    private SupportMapFragment mapFragment;

    @BindView(R.id.fare_evasion_image)
    ImageView fareEvasionImage;

    public FareEvasionFragment() {
        // Required empty public constructor
    }

    public static FareEvasionFragment newInstance(FraudInstance fraudInstance) {
        Bundle args = new Bundle();
        args.putParcelable(FARE_EVASION_ARG, fraudInstance);

        FareEvasionFragment fragment = new FareEvasionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args.getParcelable(FARE_EVASION_ARG) != null){
            fraudInstance = args.getParcelable(FARE_EVASION_ARG);
            Log.i(TAG, fraudInstance.toString());
        }


        // Initialize map
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fare_evasion_location);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fare_evasion, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set UI
//        Picasso.get().load(fraudInstance.pic_url).into(fareEvasionImage);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng fareEvasionLocation = new LatLng(fraudInstance.latitude, fraudInstance.longitude);
        googleMap.addMarker(new MarkerOptions().position(fareEvasionLocation)
                .title("Fare Evasion"));
    }
}
