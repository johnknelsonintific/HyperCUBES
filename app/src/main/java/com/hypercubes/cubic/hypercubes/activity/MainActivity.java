package com.hypercubes.cubic.hypercubes.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.fragment.FraudList;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;
import com.hypercubes.cubic.hypercubes.service.FraudService;
import com.hypercubes.cubic.hypercubes.util.Util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.hypercubes.cubic.hypercubes.activity.FareEvasionActivity.FARE_EVASION_ARG;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener, FraudService.NotificationServiceListener {

    //Static tags
    private static final String TAG = "MainActivity";
    private static final String FARE_EVASION_NOTI_CHANNEL = "FareEvasionNotiChannel";
    private static final int FARE_EVASION_NOTI_ID = 0;


    // Member variables
    private OkHttpClient okHttpClient = null;
    private GsonBuilder mGsonBuilder = null;
    private Gson mGson = null;

    private ArrayList<FraudInstance> fraudHistory = null;
    private FraudService fraudService = null;

    private boolean mConnected = false;

    private boolean activityPaused = false;

    // UI
    @BindView(R.id.fraud_per_area_chart)
    LineChart fraudPerAreaChart;

    @BindView(R.id.progress_spinner)
    View progressBar;

    private ServiceConnection mNotificationServiceConnection = new ServiceConnection() {

        private static final String TAG = "MissionConnection";

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "Activity connected to service");
            FraudService.FraudServiceBinder binder = (FraudService.FraudServiceBinder) service;
            fraudService = binder.getService();

            mConnected = true;

            // Set callback
            fraudService.setmNotificationServiceListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i(TAG, "Service disconnected");
            fraudService = null;
            mConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGsonBuilder = new GsonBuilder();
        mGson = mGsonBuilder.create();
        okHttpClient = new OkHttpClient.Builder().build();

        // Start notification service
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, FraudService.class));
        } else {
            startService(new Intent(this, FraudService.class));
        }
        Log.i(TAG, "Starting service");

        // Get fraud instances
        getFraudInstances();

        setTitle("Analytics");
    }

    @Override
    protected void onPause(){
        super.onPause();

        // Set pause state
        activityPaused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Re-bind to the mission
        bindFraudService();

        // Set pause state
        activityPaused = false;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Re-bind to the mission
        unbindFraudService();
    }

    private void bindFraudService() {
        Intent intent = new Intent(this, FraudService.class);

        // Bind to the service
        Log.i(TAG, "Binding to mission service");
        bindService(intent, mNotificationServiceConnection, 0);
    }

    private void unbindFraudService() {
        Log.i(TAG, "Unbinding from mission service");
        if (mNotificationServiceConnection != null && mConnected) {
            unbindService(mNotificationServiceConnection);
        }
    }

    private void getFraudInstances(){
        // Show loading icon
        showLoadingIndicator();

        final Request historyRequest = new Request.Builder()
                .url("http://10.200.64.4:8080/history")
                .get()
                .header("Connection", "close")
                .build();

        Call call = okHttpClient.newCall(historyRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "Is this thing on?");

                // Stop loading icon
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String fraudListHtml = response.body().string();
                String fraudListJson = Util.removeHtmlFromJson(fraudListHtml);

                // Massage the information with GSON
                FraudList fraudList = mGson.fromJson(fraudListJson, FraudList.class);
                MainActivity.this.fraudHistory = fraudList.history;

                // Set chart with data
                setLineChartData(fraudPerAreaChart, fraudList.history);


                // Stop loading icon
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingIndicator();
                    }
                });
            }
        });

    }

    private void setLineChartData(final LineChart lineChart, final ArrayList<FraudInstance> fraudInstances){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HashMap<Integer, List<Entry>> busIdEntries = new HashMap<>();

                // Add each instance of fraud to the graph by
                for (FraudInstance fraud: fraudInstances) {
                    String timeString = fraud.timestamp;
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");// EX 2018-10-15 12:03:12
                    try {
                        Date date = format.parse(timeString);

                        Entry fraudEntry = new Entry(date.getDay(), fraud.vehicle_id);// Plot day of week on x, bus id on y

                        fraudEntry.setData(fraud);

                        // Get the line data set to add to
                        if(busIdEntries.get(fraud.vehicle_id) == null){
                            busIdEntries.put(fraud.vehicle_id, new ArrayList<Entry>());
                        }

                        busIdEntries.get(fraud.vehicle_id).add(fraudEntry);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

                int[] primaryColors = getResources().getIntArray(R.array.primaries);

                //TODO Update how we index into the hash map
                for(Integer key : busIdEntries.keySet()){
                    LineDataSet busIdSet = new LineDataSet(busIdEntries.get(key), "Bus ID " + key);// create a dataset
//                    busIdSet.setColors(primaryColors[key % 4]);
                    busIdSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                    busIdSet.setHighlightEnabled(true);
                    dataSets.add(busIdSet);
                }

                lineChart.setHighlightPerDragEnabled(false);
                lineChart.setHighlightPerTapEnabled(true);
                lineChart.setOnChartValueSelectedListener(MainActivity.this);

                LineData data = new LineData(dataSets);
                lineChart.setData(data);
                lineChart.invalidate(); // refresh
            }
        });


    }
    private void showFraudInstance(FraudInstance fraud){

        Intent fareEvasionInstanceIntent = new Intent(MainActivity.this, FareEvasionActivity.class);
        fareEvasionInstanceIntent.putExtra(FARE_EVASION_ARG, fraud);
//        fraudService.unbindService(mNotificationServiceConnection);
        startActivity(fareEvasionInstanceIntent);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
       FraudInstance fraudInstance = (FraudInstance) e.getData();
       showFraudInstance(fraudInstance);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onNotificationReceived(FraudInstance fraudInstance) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, FareEvasionActivity.class);
        intent.putExtra(FARE_EVASION_ARG, fraudInstance);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.squircle_masked);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FARE_EVASION_NOTI_CHANNEL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.fare_evasion_notification_title))
                .setContentText(getString(R.string.fare_evasion_notification_content))
                .setLargeIcon(bm)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Notification fareEvasionNotification = builder.build();

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(FARE_EVASION_NOTI_ID, fareEvasionNotification);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.e("screen on.................................", ""+isScreenOn);

        if(isScreenOn==false)
        {
            // Turn on the lock screen so we can see it
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        } else if (!activityPaused) {
            showFraudInstance(fraudInstance);
        }
    }


    private void showLoadingIndicator() {
//        Util.fadeInView(progressBar);
//        progressBar.setClickable(true);
    }

    private void hideLoadingIndicator() {
//        Util.fadeOutView(progressBar);
//        progressBar.setClickable(false);
    }
}
