package com.gochatin.gochatin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.goshiv.gochatlibrary.Webgochat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MessagesAdapter;
import halper.AppController;
import halper.DividerItemDecoration;
import halper.SQLiteHandler;
import halper.SessionManager;
import halper.Utils;
import halper.WebsocketClass;
import model.Message;


public class Users extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, MessagesAdapter.MessageAdapterListener {

    private static final int MY_SOCKET_TIMEOUT_MS = 30000;
    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private HashMap<String, String> user;
    public static String visitor_id="";
    public static String vissitor_department_Id="";
    public static String Visitor_name="";
    private ProgressDialog pDialog;
    private int Globleposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        visitor_id="";


        SQLiteHandler  db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        startService(new Intent(Users.this, WebsocketClass.class));
      //  new ChatActivity().connectWebSocket(Users.this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent ordersIntent=new Intent(Users.this,Webgochat.class);
                ordersIntent.putExtra("GMID", "M2Q1YzEwNzk2Yg==");
                ordersIntent.putExtra("name",user.get("name"));
                ordersIntent.putExtra("email",user.get("email"));
                ordersIntent.putExtra("code",user.get("code"));
                ordersIntent.putExtra("phone",user.get("mobile_number"));
                ordersIntent.putExtra("type","user_type");
                startActivity(ordersIntent);

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(Users.this, LinearLayoutManager.HORIZONTAL, false));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MessagesAdapter(this, messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
       // recyclerView.setAdapter(mAdapter);
        recyclerView.swapAdapter(mAdapter,true);

        actionModeCallback = new ActionModeCallback();



        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
    }

    /**
     * Fetches mail messages by making HTTP request
     * url: http://api.androidhive.info/json/inbox.json
     */
/*    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Message>> call = apiService.getInbox();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                // clear the inbox
                messages.clear();

                // add all the messages
                // messages.addAll(response.body());

                // TODO - avoid looping
                // the loop was performed to add colors to each message
                for (Message message : response.body()) {
                    // generate a random color
                    message.setColor(getRandomMaterialColor("400"));
                    messages.add(message);
                }

                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }*/

    private void getInbox() {
        swipeRefreshLayout.setRefreshing(true);

        messages.clear();

        String tag_string_req = "req_login2";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "User_List: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    if(jObj.getString("status").equals("failed")){
                        Toast.makeText(Users.this,"Visitor not found",Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }else {
                        JSONArray user = jObj.getJSONArray("param");

                        for(int n = 0; n < user.length(); n++)
                        {
                            JSONObject object = user.getJSONObject(n);

                            Message message = new Message();
                            message.setFrom(object.get("name").toString());
                            message.setAssignto(object.get("assigned_to").toString());
                            message.setVisitor_id(object.getString("visitor_id"));
                            message.setSessionid(object.getString("session_id"));
                            message.setDepartmentid(object.getString("dept"));
                            message.setDepartment(object.getString("department"));
                            message.setUnseenmsg(object.getString("is_read"));
                            // do some stuff....

                            message.setColor(getRandomMaterialColor("400"));
                            messages.add(message);
                        }

                   /* String name = user.getString("first_name") +user.getString("last_name");
                    String email = user.getString("email");
                    String user_type = user.getString("user_type");
                    String mid = user.getString("mid");
                    String agent_id =user.getString("agent_id");
                    String dept = user.getString("dept");
                    String logged_in = user.getString("logged_in");
                    String online_status = user.getString("online_status");

                    for (Message message : user()) {
                        // generate a random color
                        message.setColor(getRandomMaterialColor("400"));
                        messages.add(message);
                    }*/
                        // recyclerView.getRecycledViewPool().clear();

                        int prevSize = messages.size();
// some insert
                        mAdapter.notifyItemRangeInserted(prevSize, messages.size() -prevSize);
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
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
               /* Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid",user.get("mid"));
                params.put("dept", user.get("dept"));
                params.put("request","Dashboard_VisitorList");
                params.put("agent_id",user.get("agent_id"));
                Log.d("Users send data", params.toString());
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           // Toast.makeText(getApplicationContext(), "Search...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Users.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.idlogout){
            logout();
//            SessionManager sessionManager = new SessionManager(this);
//            sessionManager.setLogin(false);
//            Intent intent= new Intent(Users.this,MainActivity.class);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        getInbox();
    }

    @Override
    public void onIconClicked(int position) {
     /*   if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);*/
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Message message = messages.get(position);
        message.setImportant(!message.isImportant());
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
           // enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Message message = messages.get(position);
            message.setRead(true);
            visitor_id = message.getVisitor_id();
            Log.d("hello",visitor_id);
             vissitor_department_Id = message.getDepartmentid();
            Visitor_name=message.getFrom();
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();
            Globleposition = position;
            Utils utils = new Utils(Users.this);
            utils.storeSessionId(message.getSessionid().toString());
           // Toast.makeText(getApplicationContext(), "Read: " + message.getMessage(), Toast.LENGTH_SHORT).show();
            if(message.getAssignto().equalsIgnoreCase("null")){
                Assigntouser(position);
            }else{
                Intent intent = new Intent(Users.this,ChatActivity.class);
                startActivity(intent);
            }

        }
    }
 /* public void Removepositionclicked(){
      //messages.remove(Globleposition);
      mAdapter.notifyDataSetChanged();

    }*/




    private void Assigntouser(final int position) {
        String tag_string_req = "setassignto";

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        pDialog.setMessage("Please wait ...");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "GetAllagent: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                     if(jObj.getString("status").equalsIgnoreCase("success")){
                         Message message = messages.get(position);
                         message.setAssignto(user.get("agent_id"));
                         messages.set(position, message);
                         mAdapter.notifyDataSetChanged();

                         Intent intent = new Intent(Users.this,ChatActivity.class);
                         startActivity(intent);
                     }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(Users.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(Users.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",visitor_id);
                params.put("request","Dashboard_ChatAssigned");
                params.put("agent_id",user.get("agent_id"));
                params.put("forward_dept","0");
                params.put("forward_agent_id","0");
                Log.d("Tag add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
      //  enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();

        }
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void logout(){

            // Tag used to cancel the request
            String tag_string_req = "logout";

            pDialog.setMessage("please wait ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_LOGIN, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "Logout Response: " + response.toString());
                    hideDialog();
                    //  Toast.makeText(MainActivity.this,"Login done",Toast.LENGTH_LONG).show();



                    try {
                        JSONObject jObj = new JSONObject(response);

                       if(jObj.getString("status").equalsIgnoreCase("success")){
                       SessionManager sessionManager = new SessionManager(Users.this);
                       sessionManager.setLogin(false);

                           stopService(new Intent(Users.this,WebsocketClass.class));


                       Intent intent= new Intent(Users.this,MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
                   }else{
                       Toast.makeText(Users.this,jObj.getString("message"),Toast.LENGTH_LONG).show();
                   }



                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),  e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "LogOut Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    SQLiteHandler db = new SQLiteHandler(getApplicationContext());
                    user = new HashMap<String, String>();
                    user = db.getUserDetails();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mid", user.get("mid"));
                    params.put("agent_id", user.get("agent_id"));
                    params.put("request","Dashboard_Logout");

                   Log.d("Logout",params.toString());
                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to close this Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    public class WrapContentLinearLayoutManager extends LinearLayoutManager {


        public WrapContentLinearLayoutManager(Users users, int horizontal, boolean b) {
            super(users, horizontal, b);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

}