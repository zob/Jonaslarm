package dk.geo.jonas.jsonreader.jonaslarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class ServerPollService extends IntentService {

    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    private static URL BASE_URL;
    private static URL statusUrlGroup58;
    private static URL statusUrlGroup55TestUnavailable;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public ServerPollService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        // The URL from which to fetch content.


        try {
            BASE_URL = new URL("http://monitor.kortforsyningen.dk/Monitor/serviceapi/groups/");
            statusUrlGroup58 = new URL(BASE_URL, "58/status");
            statusUrlGroup55TestUnavailable = new URL(BASE_URL, "55/status");
//            statusUrl = new URL("http://monitor.kortforsyningen.dk/Monitor/serviceapi/groups/58/status");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String status = "";
        JsonFetcher fetcher = new JsonFetcherOkHTTP();
        JSONObject json = null;
        try {
            json = fetcher.getThisMofo(statusUrlGroup58.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//
        JSONObject jsonDataObject = null;
        String serverStatusString = "";
        try {
            jsonDataObject = json.getJSONObject("data");
            serverStatusString = jsonDataObject.getString("Status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerStatus serverStatus = ServerStatus.valueOf(serverStatusString);
        switch (serverStatus) {
            case AVAILABLE:
                sendNotification("Server is Available");
                Log.i(TAG, "Server is " + serverStatusString);
                break;
            case NOT_TESTED:
                sendNotification("Server is not tested");
                Log.i(TAG, "Server is " + serverStatusString);
                break;
            case UNAVAILABLE:
                sendNotification("Server is Unavailable");
                Log.i(TAG, "Server is " + serverStatusString);
                break;
            case OUT_OF_ORDER:
                sendNotification("Server is out of order");
                Log.i(TAG, "Server is " + serverStatusString);
                break;
            // This default case shouldn't happen - there should be no status other than the 4 above.
            default:
                sendNotification("WTF? " + serverStatus.toString());
                Log.i(TAG, "This should not be possible. Server is " + serverStatusString);
        }


    }

    // Post a notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.doodle_alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    /* ###########################################################################
    The below methods are not in use. Use them if you wish to show details of the server state
    ############################################################################### */
//    private String getMetadataFromJsonObject(JSONObject jsonObject) {
//        String metadata = "";
//
//        boolean succes = Boolean.parseBoolean(jsonObject.optString("success").toString());
//        String message = jsonObject.optString("message").toString();
//        metadata = "Succes: " + succes + "\nMessage: " + message + "\n\n";
//        return metadata;
//    }
//
//
//    private String getFullDataFromJsonObject(JSONObject jsonDataObject) {
//        String data = "";
//
//        int groupId = Integer.parseInt(jsonDataObject.optString("GroupId").toString());
//        String groupName = jsonDataObject.optString("GroupName").toString();
////            int requestId = Integer.parseInt(jsonDataObject.optString("RequestId").toString());
////            String requestName = jsonDataObject.optString("RequestName").toString();
//        String status = jsonDataObject.optString("Status").toString();
//
//        data = "GroupId: " + groupId + "\nGroupName: " + groupName + "\nStatus: " + status;
//
//        return data;
//    }
}
