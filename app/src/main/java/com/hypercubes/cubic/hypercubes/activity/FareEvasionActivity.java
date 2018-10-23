package com.hypercubes.cubic.hypercubes.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FareEvasionActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Static tags
    private static final String TAG = "FareEvasionFragment";
    public static final String FARE_EVASION_ARG = "FareEvasionArg";

    // Member variables
    private FraudInstance fraudInstance;
    private MapFragment mapFragment;

//    @BindView(R.id.fare_evasion_image)
//    ImageView fareEvasionImage;

    @BindView(R.id.bus_line_text_view)
    TextView busLineTextView;

    @BindView(R.id.location_text_view)
    TextView locationTextView;

    @BindView(R.id.vehicle_id_text_view)
    TextView vehicleIdTextView;

    @BindView(R.id.operator_id_text_view)
    TextView operatorIdTextView;

//    @BindView(R.id.next_stop_text_view)
//    TextView nextStopTextView;

//    @BindView(R.id.lat_text_view)
//    TextView latTextView;
//
//    @BindView(R.id.lng_text_view)
//    TextView lngTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_evasion);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras.getParcelable(FARE_EVASION_ARG) != null){
            fraudInstance = extras.getParcelable(FARE_EVASION_ARG);
            Log.i(TAG, fraudInstance.toString());
        }

        // Set info fields
        if(fraudInstance.bus_line == null){
            busLineTextView.setText("Green");
        } else {
            busLineTextView.setText(fraudInstance.bus_line);
        }
        locationTextView.setText(fraudInstance.location);
        vehicleIdTextView.setText(fraudInstance.vehicle_id.toString());
        operatorIdTextView.setText(fraudInstance.operator_id.toString());
//        nextStopTextView.setText(fraudInstance.next_stop.toString());
//        vehicleIdTextView.setText(fraudInstance.vehicle_id.toString());
//        operatorIdTextView.setText(fraudInstance.operator_id.toString());

        //Try web view
        WebView wb = (WebView) findViewById(R.id.image_webview);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.loadUrl("http://10.200.64.4");

        // Set the picture
//        Picasso.get().setLoggingEnabled(true);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request newRequest = chain.request().newBuilder()
//                                .addHeader("Cache-Control", "max-age=0")
//                                .addHeader("Upgrade_Insecure-Requests", "1")
//                                .build();
//                        return chain.proceed(newRequest);
//                    }
//                })
//                .build();
//
//        Picasso picasso = new Picasso.Builder(this)
//                .downloader(new OkHttp3Downloader(client))
//                .build();
//        picasso.get().load("http://" + fraudInstance.pic_url).into(fareEvasionImage, new Callback() {
//            @Override
//            public void onSuccess() {
//                Log.i(TAG, "Wooooo!");
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e(TAG, "What's going on here?");
//            }
//        });

//        AsyncTask asyncTask = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                try {
//                    URL url = new URL("http://" + fraudInstance.pic_url);
//                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    fareEvasionImage.setImageBitmap(bmp);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
//
//        asyncTask.execute();

        //TODO This doesn't work but it's totally taking over mental bandwidth, come back to this later
        // Initialize map
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.fare_evasion_map);
//        mapFragment.getMapAsync(this);

        setTitle("Fare Evasion");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO Zoom to fragment

        LatLng fareEvasionLocation = new LatLng(fraudInstance.latitude, fraudInstance.longitude);
        googleMap.addMarker(new MarkerOptions().position(fareEvasionLocation)
                .title("Fare Evasion"));
    }
}
