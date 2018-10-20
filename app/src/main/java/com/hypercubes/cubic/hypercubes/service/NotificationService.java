package com.hypercubes.cubic.hypercubes.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hypercubes.cubic.hypercubes.R;
import com.hypercubes.cubic.hypercubes.activity.MainActivity;
import com.hypercubes.cubic.hypercubes.util.NotificationId;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class NotificationService extends Service {

    /* Static tags */
    private static final String TAG = "NotificationService";
    private static final String CHANNEL = "NotificationChannel";
    private static final int NOTIFICATION_ID = NotificationId.getId();

    // Member variables
    OkHttpClient okHttpClient;
    WebSocket webSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        okHttpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url("placeholder")
                .build();

        webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
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


    @Override
    public void onDestroy() {
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
}
