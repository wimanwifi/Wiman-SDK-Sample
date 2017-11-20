package wiman.sampleapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import me.wiman.connect.utils.Availability;
import me.wiman.listener.WimanSDK;
import me.wiman.listener.WimanSdkEventsListener;
import me.wiman.logger.Logger;

class NotificationsAgent {

    private static final Object instanceLock = new Object();
    private static final long[] VIBRATION_PATTERN = new long[]{0L, 400L, 100L, 220L};
    private static final int ID_NOTIFICATION_CONNECT = 100001;
    private static NotificationsAgent instance;
    private final Context context;
    private final NotificationManager notificationManager;

    private NotificationsAgent(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationsAgent with(Context context) {
        if (context == null)
            throw new NullPointerException();

        synchronized (instanceLock) {
            if (instance == null)
                instance = new NotificationsAgent(context);
        }
        return instance;
    }

    private static Uri getRingtoneUri(Context context) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ringtone_connected);
    }

    private void showConnectNotification(@NonNull String ssid, @NonNull String bssid, boolean isWimanNetwork) {
        if (context != null) {
            Logger.logDebug("NotificationsAgent", "showConnectNotification - SSID: " + ssid + ", isOpenNetwork: " + isWimanNetwork);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String message = context.getString(R.string.Connected_to);
            builder.setTicker(ssid)
                    .setContentTitle(ssid + " " + context.getString(R.string.WiFi))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_signal_wifi_4_bar_black_24dp)
                    .setColor(ContextCompat.getColor(context, R.color.wiman_azure))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

            builder.setSound(getRingtoneUri(context), AudioManager.STREAM_NOTIFICATION);
            builder.setVibrate(VIBRATION_PATTERN);

            if (isWimanNetwork)
                notificationManager.notify(ID_NOTIFICATION_CONNECT, builder.build());
        }
    }

    private synchronized void hideNotificationConnect() {
        if (notificationManager != null)
            notificationManager.cancel(ID_NOTIFICATION_CONNECT);
    }

    void SDKlistener() {
        WimanSDK.addEventsListener(new WimanSdkEventsListener() {
            @Override
            public void onConnect(String ssid, String bssid, int i, Location location) {
                Logger.logDebug("NotificationsAgent", "onConnect:  - bssid: " + bssid);
                showConnectNotification(ssid, bssid, true);
            }


            @Override
            public void onError(String ssid, String bssid, Availability codeError) {


            }

            @Override
            public void onDisconnect(long byteTot, long duration, String bssid) {


                hideNotificationConnect();
            }


        });
    }


}