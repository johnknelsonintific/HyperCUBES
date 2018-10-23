package com.hypercubes.cubic.hypercubes.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.activity.MainActivity;
import com.hypercubes.cubic.hypercubes.fraud.FraudInstance;
import com.hypercubes.cubic.hypercubes.util.NotificationId;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class FraudService extends Service {

    /* Static tags */
    private static final String TAG = "FraudService";
    private static final String CHANNEL = "NotificationChannel";
    private static final int NOTIFICATION_ID = NotificationId.getId();

    // Member variables

    /* okhttp websocket */
    OkHttpClient okHttpClient;
    WebSocket webSocket;

    com.neovisionaries.ws.client.WebSocket ws = null;


    private GsonBuilder mGsonBuilder = null;
    private Gson mGson = null;

    private FraudServiceBinder mBinder = new FraudServiceBinder();

    NotificationServiceListener mNotificationServiceListener = null;


    public FraudService(){
    }

    // Local binder
    public class FraudServiceBinder extends Binder {
        public FraudService getService() {
            return FraudService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setmNotificationServiceListener(NotificationServiceListener notificationServiceListener){
        this.mNotificationServiceListener = notificationServiceListener;
    }

    @Override
    public int onStartCommand (Intent intent,
                               int flags,
                               int startId){
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        mGsonBuilder = new GsonBuilder();
        mGson = mGsonBuilder.create();

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        openWebSocket();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is unbinding");
        webSocket.close(1000, "Log out of WatchDog app.");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "Service got rebind");
        openWebSocket();
    }

    private void openWebSocket(){
        Request request = new Request.Builder()
                .url("ws://10.200.64.4:8000/")
                .build();

//         Open up websocket to listen
        webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                webSocket.send("Hello world!");
            }

            // Receive ONE fraud message from this message
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);

                FraudInstance fraudInstance = mGson.fromJson(text, FraudInstance.class);
//                FraudInstance fraudInstance = new FraudInstance();
//                fraudInstance.setDefault();
                mNotificationServiceListener.onNotificationReceived(fraudInstance);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);

//                FraudInstance fraudInstance = mGson.fromJson(bytes.toString(), FraudInstance.class);
                FraudInstance fraudInstance = new FraudInstance();
                fraudInstance.setDefault();
                mNotificationServiceListener.onNotificationReceived(fraudInstance);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }
        });
    }

    /**
     * Show a notification and start the service.
     *
     * @param id Notification identifier.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void showNotificationAndStartForegroundService(int id) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationChannel notificationChannel = createNotificationChannel();

        Notification notification =
                new Notification.Builder(this, notificationChannel.getName().toString())
                        .setContentTitle(getText(R.string.listening_foreground_noti_title))
                        .setContentText(getText(R.string.listening_foreground_noti_body))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();

        this.startForeground(NOTIFICATION_ID, notification);

        // Send the notification
        // We use a string id because it is a unique number. We use it later to cancel.
        this.startForeground(id, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = CHANNEL;
        String description = CHANNEL;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        return channel;
    }

    public interface NotificationServiceListener{
        void onNotificationReceived(FraudInstance fraudInstance);
    }
}
