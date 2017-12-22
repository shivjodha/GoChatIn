package com.gochatin.gochatin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import adapter.MessagesListAdapter;
import halper.AppController;
import halper.FileUtils;
import halper.SQLiteHandler;
import halper.SessionManager;
import halper.Utils;
import halper.WebsocketClass;
import model.Message;
import model.MessageV;
import model.Tagsmodel;
import model.User;

import static android.content.ContentValues.TAG;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int PICK_IMAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int FILE_SELECT_CODE = 0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 2;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT1 = 3;
    private static final int REQUEST_PERMISSION_SETTING = 2;

    private ImageButton btnSend;
    private EditText inputMsg;

   // private WebSocketClient client;

    // Chat messages list adapter
    private MessagesListAdapter adapter;
    private List<MessageV> listMessages;
    private ListView listViewMessages;

    private Utils utils;

    // Client name
    private String name = null;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";
    private int image;
    private ProgressDialog pDialog;
    private EditText newtag;
    private HashMap<String, String> user;
    private Dialog addnewdialog;
    private String ID;
    private String AgentID;
    private WebSocketClient mWebSocketClient;
    private TextView Typingstatus;
    private String msg_from;
    private boolean typingStarted;
    private Bitmap bitmap;

    private String mime;
    private byte[] filebytes;
    private String Convertedfiledata;
    private String displayName;
    private boolean sentToSettings;

    private String base64;
    private SharedPreferences permissionStatus;
    private String filepart2;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(Users.Visitor_name);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

           Log.d("before",Users.visitor_id);

        if(getIntent().getExtras()!=null){
            // Toast.makeText(this,getIntent().getStringExtra("buzz"), Toast.LENGTH_SHORT).show();

             Users.visitor_id=getIntent().getStringExtra("visitorid");
             Users.Visitor_name=getIntent().getStringExtra("visitorname");
             Users.vissitor_department_Id=getIntent().getStringExtra("visitordep");
            getSupportActionBar().setTitle(getIntent().getStringExtra("visitorname"));
            Log.d("notifi",Users.visitor_id);

        }


        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();


      /*  LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
*/
        getpreviouschat();

       connectWebSocket(ChatActivity.this);

        ImageButton Addimage = (ImageButton)findViewById(R.id.btn_attach);
        Addimage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/

    /* if(android.os.Build.VERSION.SDK_INT>=21){
           if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
            PackageManager.PERMISSION_GRANTED) {
               showFileChooser();

           } else {
               requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                  2);
           }
             }else{
         showFileChooser();
     }*/

                if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT1);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setTitle("Need Storage Permission");
                        builder.setMessage("This app needs storage permission.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        //just request the permission
                        ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT1);
                    }

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
                    editor.commit();


                } else {
                   showFileChooser();
                }


            }
        });
       // Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
      //  getIntent.setType("*/*");

      //  Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      //  pickIntent.setType("*/*");

    //    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
     //   chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

      //  startActivityForResult(chooserIntent, PICK_IMAGE);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnSend = (ImageButton) findViewById(R.id.btn_send);
        Typingstatus = (TextView) findViewById(R.id.typingbutton);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Sending message to web socket server
            /*   sendMessageToServer(utils.getSendMessageJSON(inputMsg.getText()
                        .toString()));*/

                // Clearing the input filed once message was sent



              /*
                image=1;
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.EXTRA_INITIAL_INTENTS);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);*/

                //working code
               /* image=1;
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image*//*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image*//*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);*/


                inputMsg = (EditText) findViewById(R.id.inputMsg);

               /* listViewMessages = (ListView) findViewById(R.id.list_view_messages);

                utils = new Utils(getApplicationContext());
                listMessages = new ArrayList<MessageV>();*/

                sendMessage(inputMsg.getText().toString().trim());
                inputMsg.setText("");
            }
        });

        inputMsg = (EditText) findViewById(R.id.inputMsg);

        listViewMessages = (ListView) findViewById(R.id.list_view_messages);

        utils = new Utils(getApplicationContext());

        // Getting the person name from previous screen
     /*   Intent i = getIntent();
        name = i.getStringExtra("name");*/
        listMessages = new ArrayList<MessageV>();
        adapter = new MessagesListAdapter(this, listMessages);
        listViewMessages.setAdapter(adapter);


       listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

             /* if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_GRANTED) {*/
                   url = listMessages.get(position).getFile();



                if(!url.equalsIgnoreCase("")){

                    String[] parts = url.split("files\\/");
                    String part1 = parts[0]; // 004
                    filepart2 = parts[1];

                    if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //Show Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                            builder.setTitle("Need Storage Permission");
                            builder.setMessage("This app needs storage permission.");
                            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sentToSettings = true;
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            //just request the permission
                            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }

                        SharedPreferences.Editor editor = permissionStatus.edit();
                        editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
                        editor.commit();


                    } else {
                        //You already have the permission, just go ahead.
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setDescription("Some descrition");
                        request.setTitle(filepart2);
// in order for this if to run, you must use the android 3.2 to compile your app
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filepart2);

// get download service and enqueue file
                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    }

                 /* } else {
                    requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            2);}*/
                }
            }
        });









       /* listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public VideoViewActivity videoview;

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

               videoview =(VideoViewActivity)view.findViewById(R.id.VideoViewActivity);
              String  VideoURL=listMessages.get(position).getFile();
                // VideoPlay();

                try {

                    MediaController mediacontroller = new MediaController(
                           ChatActivity.this);
                    mediacontroller.setAnchorView(videoview);
                    // Get the URL from String VideoURL
                    Uri video = Uri.parse(VideoURL);
                    videoview.setMediaController(mediacontroller);
                    videoview.setVideoURI(video);


                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                videoview.requestFocus();

                videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        //pDialog.dismiss();
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int h = displaymetrics.heightPixels;
                        int w = displaymetrics.widthPixels;
                        // Start the MediaController
                        videoview.setLayoutParams(new FrameLayout.LayoutParams(w, h));
                        videoview.start();
                    }
                });

            }
        });
*/

        inputMsg.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                    //Log.i(TAG, “typing started event…”);
                    typingStarted=true;
                    sendtypingstatusMessage("merchant_typing");
                    //send typing started status
                } else if (s.toString().trim().length() == 0 && typingStarted) {
                    //Log.i(TAG, “typing stopped event…”);
                  typingStarted = false;
                    //send typing stopped status
                 sendtypingstatusMessage("merchant_stop_typing");

                }
            }
        });




      /*  *//**//**
         * Creating web socket client. This will have callback methods
         * *//**//*
        client = new WebSocketClient(URI.create(WsConfig.URL_WEBSOCKET
                + URLEncoder.encode(name)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {

            }

            *//**//**
             * On receiving the message from web socket server
             * *//**//*
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));

                parseMessage(message);

            }

            @Override
            public void onMessage(byte[] data) {
                Log.d(TAG, String.format("Got binary message! %s",
                        bytesToHex(data)));

                // Message will be in JSON format
                parseMessage(bytesToHex(data));
            }

            *//**//**
             * Called when the connection is terminated
             * *//**//*
            @Override
            public void onDisconnect(int code, String reason) {

                String message = String.format(Locale.US,
                        "Disconnected! Code: %d Reason: %s", code, reason);

                showToast(message);

                // clear the session id from shared preferences
                utils.storeSessionId(null);
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error! : " + error);

                showToast("Error! : " + error);
            }

        }, null);

        client.connect();*/
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("requestcode",String.valueOf(requestCode));
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Some descrition");
                request.setTitle(filepart2);
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filepart2);

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

             showFileChooser();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }

    }





    /**
     * Method to send message to web socket server
     * */


  /*  private void sendMessageToServer(String message) {
        if (client != null && client.isConnected()) {
            client.send(message);
        }
    }*/



    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     * */
   private void parseMessage(final String msg,final  Activity context) {

        try {
            final JSONObject jObj = new JSONObject(msg);
            Boolean isself=false;


                Log.d("json value",jObj.toString());

            if(!jObj.getString("type").equals("system")){

                if(jObj.get("mid").equals(user.get("mid"))){

                    if(jObj.length()>6){
                        if(jObj.getString("uid").equalsIgnoreCase(Users.visitor_id)){
                            String typr =jObj.getString("type").toString();
                            String message = jObj.getString("message").toString();
                            String file = jObj.getString("file").toString();
                            String type_indicator = jObj.getString("type_indicator").toString();
                            String dept = jObj.getString("dept");
                            String utype = jObj.getString("utype");
                            String usess = jObj.getString("usess");
                            String uid = jObj.getString("uid");
                            String mid = jObj.getString("mid");
                            String msg_sent_type = jObj.getString("msg_sent_type");
                            msg_from = jObj.getString("msg_from");
                            String msg_sent_id = jObj.getString("msg_sent_id");
                            Bitmap sendImage = null;
                            if(utype.equalsIgnoreCase("M")){
                                isself=true;
                            }

              /*  MessageV m = new MessageV(msg_from, message, dept,file,mid,msg_sent_id,msg_sent_type,typr,type_indicator,uid,usess,utype,isself,sendImage);
                appendMessage(m);*/

                            if(type_indicator.equalsIgnoreCase("")){
                                MessageV m = new MessageV(msg_from, message, dept,file,mid,msg_sent_id,msg_sent_type,typr,type_indicator,uid,usess,utype,isself,sendImage);
                                appendMessage(m);
                            }else if(type_indicator.equalsIgnoreCase("stop_typing")){
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Typingstatus.setVisibility(View.GONE);

                                    }
                                });

                            }else{
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Typingstatus.setVisibility(View.VISIBLE);
                                        Typingstatus.setText(msg_from+" "+ "is typing ...");
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if(jObj.getString("type_indicator").equals("")){

                                            String PREFERENCE_NOTI = "notifications_new_message";
                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
                                            Boolean notifiprefrence = sharedPreferences.getBoolean(PREFERENCE_NOTI,true);
                                            Log.d("strr",notifiprefrence.toString());
                                            if(notifiprefrence){

                                                createNotification(msg,context);
                                            }


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }
                    }


                }


            }








            // JSON node 'flag'
          /*  String flag = jObj.getString("flag");



            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                String sessionId = jObj.getString("sessionId");

                // Save the session id in shared preferences
                utils.storeSessionId(sessionId);

                Log.e(TAG, "Your session id: " + utils.getSessionId());

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");

                showToast(name + message + ". Currently " + onlineCount
                        + " people online!");

            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(utils.getSessionId())) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

             MessageV m = new MessageV(fromName, message, isSelf);

                // Appending the message to chat list
                appendMessage(m);

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                showToast(name + message);
            }
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();

        if(client != null & client.isConnected()){
            client.disconnect();
        }
    }
*/
    /**
     * Appending message to list view
     * */
    private void appendMessage(final MessageV mi1) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.d("model value",mi1.toString());
                listMessages.add(mi1);
                Log.d("adapter value",listMessages.toString());
                adapter.notifyDataSetChanged();
                listViewMessages.setSelection(listViewMessages.getAdapter().getCount()-1);
                // Playing device's notification
               // playBeep();
            }
        });
    }

    private void showToast(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Plays device's default notification sound
     * */
/*    public void playBeep() {

        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                    notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }




    private void GetDepartmentlist() {


        String tag_string_req = "getdepartment";

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        pDialog.setMessage("Please wait ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "GetAllDepartment: " + response.toString());
                hideDialog();
                // Toast.makeText(getActivity(),"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equalsIgnoreCase("success")){
                       // Toast.makeText(ChatActivity.this,jObj.get("message").toString(),Toast.LENGTH_LONG).show();
                       // addnewdialog.dismiss();
                        JSONArray departmentsarray = jObj.getJSONArray("param");
                        //ArrayList<String> names = new ArrayList<>();
                         final String[] names = new String[departmentsarray.length()];
                         final String[] id = new String[departmentsarray.length()];
                        for (int i = 0; i < departmentsarray.length(); i++) {
                            JSONObject jsonObject = departmentsarray.getJSONObject(i);
                            names[i]=jsonObject.get("name").toString();
                            id[i]=jsonObject.get("id").toString();

                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setTitle("Choose a Department");
                        builder.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        }
                                );
// add a list

                        builder.setItems(names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                   /* switch (which) {
                                   case 0: Toast.makeText(ChatActivity.this,"horce",Toast.LENGTH_LONG).show();
                                    case 1: Toast.makeText(ChatActivity.this,"cow",Toast.LENGTH_LONG).show();
                                    case 2: Toast.makeText(ChatActivity.this,"cow",Toast.LENGTH_LONG).show();
                                    case 3: // sheep
                                    case 4: // goat
                                    }*/
                                    ID = id[which];

                                  GetAgentbyDepartment();

                                //   Toast.makeText(ChatActivity.this,ID,Toast.LENGTH_LONG).show();

                            }
                });

// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        ListView listView=dialog.getListView();
                        listView.setDivider(new ColorDrawable(Color.BLACK)); // set color
                        listView.setDividerHeight(2); // set height
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        dialog.show();


                    }else{
                        Toast.makeText(ChatActivity.this,getResources().getString(R.string.failed_to_submit),Toast.LENGTH_LONG).show();

                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(ChatActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid",user.get("mid"));
                params.put("request","Dashboard_Departments");
                Log.d("Tag add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);




       /* final Dialog dialog = new Dialog(this,R.style.Dialog);
        // Include dialog.xml file*//**//*
        dialog.setContentView(R.layout.assigntolayout);
        // Set dialog title
        dialog.setTitle(getResources().getString(R.string.add_new_tag));


        final ListView listview = (ListView) findViewById(R.id.assigntoid);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        listview.setAdapter();

        Button Cancel = (Button)dialog.findViewById(R.id.cancelbutton);
        Button Submit = (Button)dialog.findViewById(R.id.submitbutton);
        EditText newtag = (EditText)dialog.findViewById(R.id.editnewtag);


        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        //  Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendDataToServer();
            }
        });
        // if decline button is clicked, close the custom dialog
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });*/

    }



    public void connectWebSocket(final Activity context) {
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


                SessionManager sessionManager = new SessionManager(ChatActivity.this);
                if(sessionManager.isLoggedIn()){
                    parseMessage(message, context);
                }

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

    public void sendMessage(String message) {
       // EditText editText = (EditText)findViewById(R.id.);
      //  mWebSocketClient.send("hiiii");
       // parseMessage(message);
       // editText.setText("");
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();
        String Department;
        String MessageId;
        if(user.get("user_type").equals("M")){
           Department="0";
            MessageId=user.get("mid");
        }else{
           Department= Users.vissitor_department_Id;
            MessageId =user.get("agent_id");
        }

        Utils utils = new Utils(ChatActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept",Department);
            jsonObject.put("file","");
            jsonObject.put("message",message);
            jsonObject.put("mid",user.get("mid"));
            jsonObject.put("msg_from",user.get("name"));
            jsonObject.put("msg_sent_id",MessageId);
            jsonObject.put("type_indicator","");
            jsonObject.put("uid", Users.visitor_id);
            jsonObject.put("user_type",user.get("user_type"));
            jsonObject.put("usess",utils.getSessionId());
            jsonObject.put("utype","M");

        Log.d("message to send",jsonObject.toString());

          /*  WebsocketClass websocketClass = new WebsocketClass();
            websocketClass.sendmessage(jsonObject.toString());*/

            try{
                if(mWebSocketClient==null){

                    Toast.makeText(ChatActivity.this,R.string.Please_Check_Your_Network_Connection,Toast.LENGTH_LONG).show();
                }else{
                    mWebSocketClient.send(jsonObject.toString());
                }


            }catch (IllegalStateException e){
                e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void sendfileMessage(String message) {
        // EditText editText = (EditText)findViewById(R.id.);
        //  mWebSocketClient.send("hiiii");
        // parseMessage(message);
        // editText.setText("");
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();
        String Department;
        String MessageId;
        if(user.get("user_type").equals("M")){
            Department="0";
            MessageId=user.get("mid");
        }else{
            Department= Users.vissitor_department_Id;
            MessageId =user.get("agent_id");
        }

        Utils utils = new Utils(ChatActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept",Department);
            jsonObject.put("file",message);
            jsonObject.put("message","");
            jsonObject.put("mid",user.get("mid"));
            jsonObject.put("msg_from",user.get("name"));
            jsonObject.put("msg_sent_id",MessageId);
            jsonObject.put("type_indicator","");
            jsonObject.put("uid", Users.visitor_id);
            jsonObject.put("user_type",user.get("user_type"));
            jsonObject.put("usess",utils.getSessionId());
            jsonObject.put("utype","M");

            Log.d("message to send",jsonObject.toString());


            try{
                if(mWebSocketClient==null){
                    Toast.makeText(ChatActivity.this,R.string.Please_Check_Your_Network_Connection,Toast.LENGTH_LONG).show();
                }else{
                    mWebSocketClient.send(jsonObject.toString());
                }


            }catch (IllegalStateException e){
                e.printStackTrace();
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void sendtypingstatusMessage(String message) {
        // EditText editText = (EditText)findViewById(R.id.);
        //  mWebSocketClient.send("hiiii");
        // parseMessage(message);
        // editText.setText("");
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();
        String Department;
        String MessageId;
        if(user.get("user_type").equals("M")){
            Department="0";
            MessageId=user.get("mid");
        }else{
            Department= Users.vissitor_department_Id;
            MessageId =user.get("agent_id");
        }

        Utils utils = new Utils(ChatActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept",Department);
            jsonObject.put("file","");
            jsonObject.put("message","");
            jsonObject.put("mid",user.get("mid"));
            jsonObject.put("msg_from",user.get("name"));
            jsonObject.put("msg_sent_id",MessageId);
            jsonObject.put("type_indicator",message);
            jsonObject.put("uid", Users.visitor_id);
            jsonObject.put("user_type",user.get("user_type"));
            jsonObject.put("usess",utils.getSessionId());
            jsonObject.put("utype","M");

            Log.d("message to send",jsonObject.toString());

            try{
                if(mWebSocketClient==null){

                }else{
                    mWebSocketClient.send(jsonObject.toString());
                }


            }catch (IllegalStateException e){
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GetAgentbyDepartment() {

        String tag_string_req = "getagentbydepartment";

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        pDialog.setMessage("Please wait ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "GetAllagent: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equalsIgnoreCase("success")){
                       // Toast.makeText(ChatActivity.this,jObj.get("message").toString(),Toast.LENGTH_LONG).show();
                        // addnewdialog.dismiss();
                        JSONArray departmentsarray = jObj.getJSONArray("param");
                        //ArrayList<String> names = new ArrayList<>();
                        final String[] names = new String[departmentsarray.length()];
                        final String[] id = new String[departmentsarray.length()];
                        for (int i = 0; i < departmentsarray.length(); i++) {
                            JSONObject jsonObject = departmentsarray.getJSONObject(i);
                            names[i]=jsonObject.get("agent_name").toString();
                            id[i]=jsonObject.get("agent_id").toString();
                        }


                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        builder.setTitle("Choose a Department");
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
// add a list

                        builder.setItems(names, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AgentID = id[which];

                                finalassigntomethod();


                              //  Toast.makeText(ChatActivity.this,ID,Toast.LENGTH_LONG).show();

                            }
                        });

// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        ListView listView=dialog.getListView();
                        listView.setDivider(new ColorDrawable(Color.BLACK)); // set color
                        listView.setDividerHeight(2); // set height

                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        dialog.show();


                    }else{
                        Toast.makeText(ChatActivity.this,getResources().getString(R.string.failed_to_submit),Toast.LENGTH_LONG).show();

                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(ChatActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("dept",ID);
                params.put("request","Dashboard_AgentByDept");
                Log.d("Tag add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void finalassigntomethod() {

        String tag_string_req = "finalassigntask";

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        pDialog.setMessage("Please wait ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "finalforwerdagent: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equalsIgnoreCase("success")){
                      /*  Users users = new Users();
                        users.Removepositionclicked();*/
                       Intent intent = new Intent(ChatActivity.this,Users.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(ChatActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_ChatAssigned");
                params.put("agent_id",user.get("agent_id"));
                params.put("forward_dept",ID);
                params.put("forward_agent_id",AgentID);
                Log.d("Tag add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void AddnewTag() {


         addnewdialog = new Dialog(this,R.style.Dialog);
        // Include dialog.xml file*//**//*
        addnewdialog.setContentView(R.layout.addnewtag);
        // Set dialog title
        addnewdialog.setTitle(getResources().getString(R.string.add_new_tag));

        Button Cancel = (Button)addnewdialog.findViewById(R.id.cancelbutton);
        Button Submit = (Button)addnewdialog.findViewById(R.id.submitbutton);
         newtag = (EditText)addnewdialog.findViewById(R.id.editnewtag);


        addnewdialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = addnewdialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        //  Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendnewTagDataToServer();
            }
        });
        // if decline button is clicked, close the custom dialog
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                addnewdialog.dismiss();
            }
        });
    }

    private void SendnewTagDataToServer() {

        String tag_string_req = "add_newtag";

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        pDialog.setMessage("Please wait ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();
                // Toast.makeText(getActivity(),"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equalsIgnoreCase("success")){
                        Toast.makeText(ChatActivity.this,jObj.get("message").toString(),Toast.LENGTH_LONG).show();
                        addnewdialog.dismiss();

                    }else{
                        Toast.makeText(ChatActivity.this,getResources().getString(R.string.failed_to_submit),Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(ChatActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_AddTag");
                params.put("tag",newtag.getText().toString().trim());
                params.put("mid",user.get("mid"));
                Log.d("Tag add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null && image==1) {
            image=0;
            Uri uri = data.getData();
            try {
                Log.d("clicked properly","clicked");
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                adapter = new MessagesListAdapter(ChatActivity.this, listMessages);
                listViewMessages.setAdapter(adapter);
               *//* MessageV m = new MessageV("fromName", "message1", true,bitmap);
                appendMessage(m);*//*
               *//* MessageV m1 = new MessageV("fromName", "message2", false,bitmap);
                appendMessage(m1);*//*

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/


/*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == 1 && data != null) {
          Uri filePath = data.getData();
          try {
              bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              Log.d("bitmapvalue", getStringImage(bitmap));
          } catch (IOException e) {
              e.printStackTrace();
          }

          Log.v("TAG", data.getStringExtra("Note"));
          if (resultCode == RESULT_OK) {
              Uri selectedImage = data.getData();
              String[] filePathColumn = {MediaStore.Images.Media.DATA};
              Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
              if (cursor.moveToFirst()) {
                  int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                  String filePath1 = cursor.getString(columnIndex);
                  Log.d("filename", filePath1);
                  filePath1.substring(filePath1.lastIndexOf("."));
                  Bitmap bitmap = BitmapFactory.decodeFile(filePath1);
              }
          }
          if (resultCode == RESULT_CANCELED) {

          }

          if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             // Uri filePath = data.getData();
              //Getting the Bitmap from Gallery
              String path = data.getData().getPath();
              Log.d("filerecived", path);

              Uri selectedImage = data.getData();
              String[] filePathColumn = {MediaStore.Images.Media.DATA};
              Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
              if (cursor.moveToFirst()) {
                  int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                  String filePath1 = cursor.getString(columnIndex);
                  Log.d("filename", filePath1);
                  filePath1.substring(filePath1.lastIndexOf("."));
                  bitmap = BitmapFactory.decodeFile(filePath1);
              }


              // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //Setting the Bitmap to ImageView

          }


          Uri uri = data.getData();
          try {
              bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
          } catch (IOException e) {
              e.printStackTrace();
          }
          String result = null;
          if (uri.getScheme().equals("content")) {
              Cursor cursor = getContentResolver().query(uri, null, null, null, null);
              try {
                  if (cursor != null && cursor.moveToFirst()) {
                      result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                  }
              } finally {
                  cursor.close();
              }
          }
          if (result == null) {
              result = uri.getPath();
              int cut = result.lastIndexOf('/');
              if (cut != -1) {
                  result = result.substring(cut + 1);
              }
          }

          Log.d("finalresult", result);


      }
  }*/


   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
   public String DecodeString(){

       SQLiteHandler db = new SQLiteHandler(getApplicationContext());
       user = new HashMap<String, String>();
       user = db.getUserDetails();
       String merchantid = "GO_Chat-"+user.get("mid");

       byte[] data = merchantid.getBytes(StandardCharsets.UTF_8);

      base64 = Base64.encodeToString(data, Base64.DEFAULT);

// Receiving side
     /*  data = Base64.decode(base64, Base64.DEFAULT);
       String text = new String(data, StandardCharsets.UTF_8);*/
       Log.d("convertedvalue",base64);
       return base64;
    }




    public String getStringImage(Bitmap bmp){
        Log.d("bitmapimage",bmp.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public String getStringdata(byte[] bmp){


        String encodedImage = Base64.encodeToString(bmp, Base64.DEFAULT);
        Log.d("string converted byte",encodedImage);
        return encodedImage;
    }

  /*  private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }*/


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {



                 /*   if (requestCode == 0 && data != null) {
                       // Uri filePath = data.getData();
                       *//* try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            Log.d("bitmapvalue", getStringImage(bitmap));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*

                    //    Log.v("TAG", data.getStringExtra("Note"));
                        if (resultCode == RESULT_OK) {
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor.moveToFirst()) {
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String filePath1 = cursor.getString(columnIndex);
                                Log.d("filename", filePath1);
                                filePath1.substring(filePath1.lastIndexOf("."));
                                Bitmap bitmap = BitmapFactory.decodeFile(filePath1);
                            }
                        }
                        if (resultCode == RESULT_CANCELED) {

                        }
                    }
*/





                    // Get the Uri of the selected file
                   // Uri uri = data.getData();
                   /* String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path1 = myFile.getAbsolutePath();
                    Toast.makeText(ChatActivity.this,path1,Toast.LENGTH_LONG).show();
                    String displayName = null;*/



                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    mime = cr.getType(uri);
                   // Toast.makeText(ChatActivity.this,mime,Toast.LENGTH_LONG).show();
                    Log.d(TAG, "File Uri: " + uri.toString()+"   "+mime);
                    // Get the path



                  /*  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileInputStream fis;
                    try {
                        Log.d("path",uri.getPath());
                        fis = new FileInputStream(new File(uri.getPath()));
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = fis.read(buf)))
                            baos.write(buf, 0, n);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    byte[] bbytes = baos.toByteArray();
                  Log.d("bytearray",bbytes.toString());
*/
                 /*   if(mime.equalsIgnoreCase("image/jpeg")){
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            uploadImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        FileInputStream fis;
                        try {
                            fis = new FileInputStream(new File(uri.getPath()));
                            byte[] buf = new byte[1024];
                            int n;
                            while (-1 != (n = fis.read(buf)))
                                baos.write(buf, 0, n);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        filebytes = baos.toByteArray();
                        uploadImage();

                    }

                    String path = null;
                    try {
                        path = ChatActivity.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);*/
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload


                    String uriString = uri.toString();
                    Log.d("pathto show",uri.getPath());
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    Log.d("path",path);
                    displayName = null;


                    File file = new File(path);

// Get length of file in bytes
                    long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

                   Log.d("sizevideo",Long.toString(fileSizeInMB));





                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    FileInputStream fis;

                    try {
                        fis = new FileInputStream(FileUtils.getFile(ChatActivity.this,data.getData()));
                        byte[] buf = new byte[1024];
                        int n;
                        while (-1 != (n = fis.read(buf)))
                            baos.write(buf, 0, n);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    byte[] bbytes = baos.toByteArray();
                    Log.d("bbytes",bbytes.toString());



                 //String ext =   FileUtils.getExtension(data.getData().getPath());
                  //FileUtils.getThumbnail(ChatActivity.this,)

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = ChatActivity.this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                Log.d("displayname",displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        Log.d("filename",displayName);
                    }
                Convertedfiledata="";
                Convertedfiledata= getStringdata(bbytes);

                    uploadImage();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

 /*   private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                  return cursor.getString(column_index);

                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
*/



    private void getpreviouschat() {

        String tag_string_req = "chathistory";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Chathistory", "User_List: " + response.toString());
                listMessages.clear();
                try {
                    JSONObject jObj = new JSONObject(response);


                    if(jObj.getString("status").equals("failed")){
                       /* MessageV messageV = new MessageV();
                        listMessages.add(messageV);
                        adapter.notifyDataSetChanged();*/
                    }else{
                        JSONArray user = jObj.getJSONArray("param");

                        for(int n = 0; n < user.length(); n++)
                        {
                            JSONObject object = user.getJSONObject(n);
                            MessageV messageV = new MessageV();
                            messageV.setFromName(object.getString("name"));
                            messageV.setMessage(object.getString("message"));
                            messageV.setFile(object.getString("file"));
                            messageV.setDepartment(object.getString("department_id"));
                            messageV.setUsess(object.getString("chat_sess_id"));
                            messageV.setSendImage(null);
                            messageV.setType_indicator("");
                            messageV.setUtype(object.getString("messageby"));
                            if(object.getString("messageby").equalsIgnoreCase("M")){
                                messageV.setSelf(true);
                            }

                            listMessages.add(messageV);
                        }

                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_ViewChat");

                Log.d("get chat history", params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    String UPLOAD_URL ="https://www.gochatin.com/widget/dev-v1/GmChat/FileUploadMobile.php";
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String message = jsonObject.getString("file");
                            sendfileMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("image path",s);
                      //  Toast.makeText(ChatActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ChatActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String


                Map<String,String> params = new Hashtable<String, String>();

                String type[] =mime.split("/");
                String value =type[1];

                //Adding parameters
                String decvalue = DecodeString();

                String type1[] =decvalue.split("\n");
                decvalue =type1[0];

                Log.d("decvalue",decvalue);
                params.put("ext",value);
                params.put("mkey",decvalue);
                params.put("file",Convertedfiledata);
               // params.put("name",displayName);



               /* if(mime.equalsIgnoreCase("image/jpeg")){
                    String image = getStringImage(bitmap);
                    params.put("ch_img", image);
                }else{
                    String image = new String(filebytes);
                    params.put("ch_img", image);
                }*/
                //returning parameters
                Log.d("valueswithoutimg",base64+" "+displayName+" "+value);
                Log.d("imagedemo",params.toString());
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNotification(String message, Context context) {
        // Prepare intent which is triggered if the
        // notification is selected
        Log.d("before",message);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(message);
            if(jObj.length()>6){


                Intent intent = new Intent(context, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("visitorid",jObj.getString("uid"));
                bundle.putString("visitordep",jObj.getString("dept"));
                bundle.putString("visitorname",jObj.getString("msg_from"));
                intent.putExtras(bundle);



                String msg = null;
                if(!jObj.getString("message").equals("")){
                    // msg =jObj.getString("message");

                    msg=  Html.fromHtml(jObj.getString("message")).toString();
                }else {

                    String file[] =jObj.getString("file").split("chat-files\\/");
                    String msg1 =file[1];
                    Log.d("ww",msg1);
                    String msg2[] = msg1.split("\\.");
                    msg = msg2[1]+" "+"file";
                    Log.d("ww",msg);
                }

//if(!jObj.getString("file").equals(""))
             /*  if(!jObj.getString("file").equals("")){
                   String file[] =jObj.getString("file").split("chat-files\\/");
                    String msg1 =file[1];
                   Log.d("ww",msg1);
                   String msg2[] = msg1.split("\\.");
                   msg = msg2[1]+" "+"file";
                   Log.d("ww",msg);
                }else{
                   Log.d("itc","itc");
                   String msg1 =jObj.getString("message");
                   msg = Html.fromHtml(msg1).toString();

                }
*/


                String PREFERENCE_SOUND = "notifications_new_message_ringtone";
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String strRingtonePreference = sharedPreferences.getString(PREFERENCE_SOUND, "DEFAULT_SOUND");
                Uri soundUri = Uri.parse(strRingtonePreference);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

                // Build notification
                // Actions are just fake
                Notification noti = new Notification.Builder(context)
                        .setContentTitle("New message from " + jObj.getString("msg_from"))
                        .setContentText(msg).setSmallIcon(R.drawable.notification)
                        .setContentIntent(pIntent)
                        .setSound(soundUri)
                        .setColor(getResources().getColor(R.color.appcolor))
                /*.addAction(R.drawable.file_icon, "Call", pIntent)
                .addAction(R.drawable.file_icon, "More", pIntent)
                .addAction(R.drawable.file_icon, "And more", pIntent)*/.build();
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(Integer.valueOf(jObj.getString("uid")), noti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.viewcontact) {

            Intent intent = new Intent(ChatActivity.this,Profile.class);
            startActivity(intent);

            return true;
        }
        if(id== R.id.action_addnewcontact){

            AddnewTag();

        }

        if(id == R.id.action_assigito ){
            GetDepartmentlist();
        }

        if (id ==android.R.id.home) {
            ChatActivity.this.finish();
            Intent intent = new Intent(ChatActivity.this,Users.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        ChatActivity.this.finish();
        Intent intent = new Intent(ChatActivity.this,Users.class);
        startActivity(intent);

    }


   /* private BroadcastReceiverBroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            parseMessage(message);
        }
    };
*/



}