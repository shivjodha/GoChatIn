package com.gochatin.gochatin;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

import halper.AppController;
import halper.SQLiteHandler;
import halper.SessionManager;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText EditEmail;
    private EditText EditPassword;
    private Button Loginbutton;
    private TextView ForgetPassword;
    private SQLiteHandler db;
    private Dialog forgetdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditEmail = (EditText)findViewById(R.id.input_email);
        EditPassword = (EditText)findViewById(R.id.input_password);
        Loginbutton = (Button)findViewById(R.id.btn_login);
        ForgetPassword = (TextView)findViewById(R.id.link_forgetpassword);



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn()){
            Intent intent = new Intent(MainActivity.this,Users.class);
            startActivity(intent);
        }


        db = new SQLiteHandler(getApplicationContext());

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(MainActivity.this,Users.class);
                startActivity(intent);*/

                String User = EditEmail.getText().toString().trim();
                String Password = EditPassword.getText().toString().trim();

                if(checkconnection()){
                    if(isValidEmail(User)){
                        checkLogin(User,Password);
                    }else{
                        Toast.makeText(MainActivity.this,"Please Enter Valid email",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Please check Your Network Connection",Toast.LENGTH_LONG).show();
                }





              /*  Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);*/
             /*  if (User.equalsIgnoreCase("")) {

                    Toast.makeText(MainActivity.this,"Please enter valid Email",Toast.LENGTH_LONG).show();
                }else if(Password.equalsIgnoreCase("")){
                    Toast.makeText(MainActivity.this,"Please Enter Valid Password",Toast.LENGTH_LONG).show();
                }else  {
                    checkLogin(User,Password);
                }
*/

            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            public EditText gmailtext;

            @Override
            public void onClick(View v) {
                // Create custom dialog object
                 forgetdialog = new Dialog(MainActivity.this);
                // Include dialog.xml file/**/
                forgetdialog.setContentView(R.layout.forgetpassword);
                // Set dialog title
                forgetdialog.setTitle("Forget Password");

                Button Cancel = (Button)forgetdialog.findViewById(R.id.cancelbutton);
                Button submit = (Button)forgetdialog.findViewById(R.id.submitbutton);
                 gmailtext = (EditText)forgetdialog.findViewById(R.id.input_email);

                // set values for custom dialog components - text, image and button
               /* TextView text = (TextView) dialog.findViewById(R.id.textDialog);
                text.setText("Custom dialog Android example.");*/
                /*ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
                image.setImageResource(R.drawable.image0);*/

                forgetdialog.show();

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = forgetdialog.getWindow();
                lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);

              //  Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

                // if decline button is clicked, close the custom dialog

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usergmail = gmailtext.getText().toString().trim();

                        if(checkconnection()){
                            if(isValidEmail(usergmail)){
                                setForgetPassword(usergmail);
                            }else{
                                Toast.makeText(MainActivity.this,"Please Enter Valid email",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this,"Please check Your Network Connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });



                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        forgetdialog.dismiss();
                    }
                });
            }
        });

    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();
              //  Toast.makeText(MainActivity.this,"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                   // boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                /*    if (!error) {*/
                        // user successfully logged in
                        // Create login session

                    if(jObj.get("status").equals("failed")){

                        Toast.makeText(MainActivity.this,jObj.getString("message")+" "+jObj.getString("param"),Toast.LENGTH_LONG).show();

                    }else{
                        session.setLogin(true);

                        // Now store the user in SQLite
                        //String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("param");
                        String name = user.getString("first_name") +user.getString("last_name");
                        String email = user.getString("email");
                        String user_type = user.getString("user_type");
                        String mid = user.getString("mid");
                        String agent_id =user.getString("agent_id");
                        if(user.getString("agent_id").equalsIgnoreCase("")){
                            agent_id="0";
                        }

                        String dept = user.getString("dept");
                        String logged_in = user.getString("logged_in");
                        String mobile =user.getString("mobile");
                        String code = user.getString("code");
                        String online_status = user.getString("online_status");

                        db.deleteUsers();
                        // Inserting row in users table
                        db.addUser(name, email, user_type, mid,agent_id,dept,logged_in,mobile,code,online_status);

                        // Launch main activity

                        //  db.getUserDetails();

                        Intent intent = new Intent(MainActivity.this,
                                Users.class);
                        startActivity(intent);
                        finish();
                  /*  } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }*/
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("request","Dashboard_Login");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void setForgetPassword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_password";

        pDialog.setMessage("please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "forgetpassword Response: " + response.toString());
                hideDialog();
                //  Toast.makeText(MainActivity.this,"Login done",Toast.LENGTH_LONG).show();
                forgetdialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    if(jObj.get("status").equals("success")){
                        Toast.makeText(MainActivity.this,jObj.get("message").toString(),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Passwored Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("request","Dashboard_ForgotPassword");

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

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean checkconnection(){
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online
            return true;
        } else {
            // notify user you are not online
            return false;
        }
    }

}
