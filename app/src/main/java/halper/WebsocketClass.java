package halper;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gochatin.gochatin.ChatActivity;
import com.gochatin.gochatin.R;
import com.gochatin.gochatin.Users;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import model.MessageV;

/**
 * Created by Dell on 8/24/2017.
 */

public class WebsocketClass extends Service {
    private static String TAG = "MyService";

    private Runnable runnable;
    private final int runTime = 5000;
    public  WebSocketClient mWebSocketClient;
    private HashMap<String, String> user;

    @Override
    public void onCreate() {
        super.onCreate();

        connectWebSocket();
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // Toast.makeText(this,"stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(TAG, "onStart");
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://www.gochatin.com:9002/gcs/v1/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.d("recived message",s);
              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("new message-",message);
                        Toast.makeText(ChatActivity.this,message,Toast.LENGTH_LONG).show();
                        TextView textView = (TextView)findViewById(R.id.inputMsg);
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });*/


                String PREFERENCE_NOTI = "notifications_new_message";
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WebsocketClass.this);
                Boolean notifiprefrence = sharedPreferences.getBoolean(PREFERENCE_NOTI,true);
                Log.d("strr",notifiprefrence.toString());

                if(notifiprefrence){

                    SessionManager sessionManager = new SessionManager(WebsocketClass.this);
                    if(sessionManager.isLoggedIn()){
                        JSONObject jObj = null;

                        try {
                            jObj = new JSONObject(s);

                           if(jObj.getString("type").equals("instant_changes")&& jObj.get("mid").equals(user.get("mid"))){
                                createNotificationFornew(message);
                            } else if(jObj.length()>6){

                                if(jObj.get("mid").equals(user.get("mid"))){

                                    if(isAppIsInBackground(WebsocketClass.this)){
                                        Log.d("islive","background");

                                            createNotification(message);


                                    }else if(!jObj.getString("uid").equals(Users.visitor_id)) {
                                        Log.d("islive","forground");
                                        //  parseMessage(message);

                                            createNotification(message);

                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }



             //  parseMessage("message");

            }


            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }



    private void parseMessage(String value) {
        Log.d("sender", value);
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

  /*  public void createNotification(String message) {
        // Prepare intent which is triggered if the
        // notification is selected
        JSONObject jObj = null;
        try {
             jObj = new JSONObject(message);
            if(jObj.length()>6){

                Intent intent = new Intent(this, Users.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                // Build notification
                // Actions are just fake
                Notification noti = new Notification.Builder(this)
                        .setContentTitle("New message from " + jObj.getString("msg_from"))
                        .setContentText("Subject").setSmallIcon(R.drawable.notification)
                        .setContentIntent(pIntent)


                *//*.addAction(R.drawable.file_icon, "Call", pIntent)
                .addAction(R.drawable.file_icon, "More", pIntent)
                .addAction(R.drawable.file_icon, "And more", pIntent)*//*.build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;

                notificationManager.notify(m, noti);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }*/



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNotification(String message) {
        // Prepare intent which is triggered if the
        // notification is selected
        Log.d("before",message);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(message);



            if(jObj.length()>6){


                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("visitorid",jObj.getString("uid"));
                    bundle.putString("visitordep",jObj.getString("dept"));
                    bundle.putString("visitorname",jObj.getString("msg_from"));
                    intent.putExtras(bundle);

                    String msg;
                    if(!jObj.getString("message").equals("")){
                        // msg =jObj.getString("message");

                        msg=  Html.fromHtml(jObj.getString("message")).toString();
                    }else{

                        String file[] =jObj.getString("file").split("chat-files\\/");
                        String msg1 =file[1];
                        Log.d("ww",msg1);
                        String msg2[] = msg1.split("\\.");
                        msg = msg2[1]+" "+"file";
                        Log.d("ww",msg);
                    }


                    // String PREFERENCE_NOTI = "notifications_new_message";
                    String PREFERENCE_SOUND = "notifications_new_message_ringtone";
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String strRingtonePreference = sharedPreferences.getString(PREFERENCE_SOUND, "DEFAULT_SOUND");
                    //String notifiprefrence = sharedPreferences.getString(PREFERENCE_NOTI,"true");
                    // Log.d("strr",notifiprefrence);
                    Uri soundUri = Uri.parse(strRingtonePreference);

                    // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    // Uri soundUri =RingtoneManager.getValidRingtoneUri(this);
                    Log.d("sound",soundUri.toString());
                    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                    // Build notification
                    // Actions are just fake
                    Notification noti = new Notification.Builder(this)
                            .setContentTitle("New message from Service" + jObj.getString("msg_from"))
                            .setContentText(msg).setSmallIcon(R.drawable.notification)
                            .setContentIntent(pIntent)
                            .setSound(soundUri)
                            .setColor(getResources().getColor(R.color.appcolor))
                /*.addAction(R.drawable.file_icon, "Call", pIntent)
                .addAction(R.drawable.file_icon, "More", pIntent)
                .addAction(R.drawable.file_icon, "And more", pIntent)*/.build();
                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.notify(Integer.valueOf(jObj.getString("uid")), noti);

                }



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNotificationFornew(String message) {
        // Prepare intent which is triggered if the
        // notification is selected
        Log.d("before",message);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(message);


                    if(jObj.getString("for").equals("new_visitor_notification")){

                        Log.d("reached",jObj.toString());

                        Intent intent = new Intent(this, Users.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle bundle = new Bundle();
                        bundle.putString("mid",jObj.getString("mid"));

                        intent.putExtras(bundle);

                        // String PREFERENCE_NOTI = "notifications_new_message";
                        String PREFERENCE_SOUND = "notifications_new_message_ringtone";
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                        String strRingtonePreference = sharedPreferences.getString(PREFERENCE_SOUND, "DEFAULT_SOUND");
                        //String notifiprefrence = sharedPreferences.getString(PREFERENCE_NOTI,"true");
                        // Log.d("strr",notifiprefrence);
                        Uri soundUri = Uri.parse(strRingtonePreference);

                        // Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        // Uri soundUri =RingtoneManager.getValidRingtoneUri(this);
                        Log.d("sound",soundUri.toString());
                        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                        // Build notification
                        // Actions are just fake
                        Notification noti = new Notification.Builder(this)
                                .setContentTitle("New Visitor joined")
                                /*.setContentText("New Visitor joined")*/.setSmallIcon(R.drawable.notification)
                                .setContentIntent(pIntent)
                                .setSound(soundUri)
                                .setColor(getResources().getColor(R.color.appcolor))
                /*.addAction(R.drawable.file_icon, "Call", pIntent)
                .addAction(R.drawable.file_icon, "More", pIntent)
                .addAction(R.drawable.file_icon, "And more", pIntent)*/.build();
                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        // hide the notification after its selected
                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                        Random random = new Random(System.nanoTime());

                        int randomInt = random.nextInt(1000000000);

                        notificationManager.notify(randomInt, noti);

                    }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }






}
