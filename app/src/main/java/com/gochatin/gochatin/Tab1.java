package com.gochatin.gochatin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import halper.AppController;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1 extends Fragment {


    private TextView btnCheckFalAction;
    private EditText testedit;
    private EditText Timezone;
    private EditText Signedup;
    private EditText Firstseen;
    private EditText Lastseen;
    private EditText LastContacted;
    private EditText Lastheaaredfrom;
    private EditText LastViewedurl;
    private EditText Websession;
    private EditText Browser;
    private EditText Browserlanguage;
    private EditText BrowserVersion;
    private EditText Laungvageoverride;
    private EditText Os;
    private EditText TwitterFollowers;
    private EditText Unsubscribefromemail;
    private EditText Unsubscribefromsim;
    private ProgressDialog pDialog;

    public Tab1() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.tab1, container, false);
       /* Bundle bundle = this.getArguments();

        if (bundle != null){
            String name=bundle.getString("name");
            testedit = (EditText)rootView.findViewById(R.id.timzoneid);
            testedit.setText(name);
        }*/

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


       /* pager = (ViewPager)getContext().findViewById(R.id.pager);*/
/*
        btnCheckFalAction = (TextView) rootView.findViewById(R.id.timzone); // you have to use rootview object..
      btnCheckFalAction .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
             // Profile.viewPager.setCurrentItem(2);

                Intent intent = new Intent(getActivity(),Users.class);
                startActivity(intent);
            }
        });*/




        Timezone = (EditText)rootView.findViewById(R.id.timzoneid);
        Signedup =(EditText)rootView.findViewById(R.id.signedupid);
        Firstseen =(EditText)rootView.findViewById(R.id.firstseenid);
        Lastseen =(EditText)rootView.findViewById(R.id.lasrseenid);
        LastContacted = (EditText)rootView.findViewById(R.id.lastcontacted);
        Lastheaaredfrom = (EditText)rootView.findViewById(R.id.lastheardfromid);
        LastViewedurl = (EditText)rootView.findViewById(R.id.lastviewedurl);
        Websession = (EditText)rootView.findViewById(R.id.websessionid);
        Browser =(EditText)rootView.findViewById(R.id.browserid);
        Browserlanguage =(EditText)rootView.findViewById(R.id.browserlanguageid);
        BrowserVersion = (EditText)rootView.findViewById(R.id.browserversionid);
        Laungvageoverride = (EditText)rootView.findViewById(R.id.languageoverrideid);
        Os = (EditText)rootView.findViewById(R.id.osid);
        TwitterFollowers = (EditText)rootView.findViewById(R.id.twitterfollowers);
        Unsubscribefromemail =(EditText)rootView.findViewById(R.id.unsubscribedemailsid);
        Unsubscribefromsim = (EditText)rootView.findViewById(R.id.unsubscribedsmsid);

        setprofiledata();


        return rootView;



    }

   /* public void setText(String text){
        TextView textView = (TextView) .findViewById(R.id.textView);
        textView.setText(text);
    }*/

    private void setprofiledata() {
        String tag_string_req = "req_profile";

        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Profile " + response.toString());
                hideDialog();
               // Toast.makeText(getActivity(),"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject user1 = jObj.getJSONObject("param");

                    if(!user1.has("tags")){

                        JSONObject user = jObj.getJSONObject("param");
                        JSONObject jsonObject = user.getJSONObject("communication");
                        Log.d("Personal info",jsonObject.toString());

                        LastViewedurl.setText(jsonObject.getString("Last viewed url"));
                        Browser.setText(jsonObject.getString("Browser"));
                        Os.setText(jsonObject.getString("OS"));
                        Signedup.setText(jsonObject.getString("Signed up"));
                        Firstseen.setText(jsonObject.getString("First seen"));
                        Lastseen.setText(jsonObject.getString("Last seen"));
                        Websession.setText(jsonObject.getString("Web Sessions"));
                        Lastheaaredfrom.setText(jsonObject.getString("Last heard from"));

                    }else{
                        JSONObject user = jObj.getJSONObject("param");

                        JSONObject jsonObject = user.getJSONObject("communication");

                        Log.d("Personal info",jsonObject.toString());


                        Timezone.setText(jsonObject.getString("Timezone"));
                        Signedup.setText(jsonObject.getString("Signed up"));
                        Firstseen.setText(jsonObject.getString("First seen"));
                        Lastseen.setText(jsonObject.getString("Last seen"));
                        LastContacted.setText(jsonObject.getString("Last Contacted"));
                        Lastheaaredfrom.setText(jsonObject.getString("Last heard from"));
                        LastViewedurl.setText(jsonObject.getString("Last viewed url"));
                        Websession.setText(jsonObject.getString("Web Sessions"));
                        Browser.setText(jsonObject.getString("Browser"));
                        Browserlanguage.setText(jsonObject.getString("Browser language"));
                        BrowserVersion.setText(jsonObject.getString("browser Version"));
                        Laungvageoverride.setText(jsonObject.getString("language Override"));
                        Os.setText(jsonObject.getString("OS"));
                        TwitterFollowers.setText(jsonObject.getString("Twitter followers"));
                        Unsubscribefromemail.setText(jsonObject.getString("Unsubscribed from emails"));
                        Unsubscribefromsim.setText(jsonObject.getString("Unsubscribed from sms"));
                    }

                  /*  Bundle bundle = new Bundle();
                    bundle.putString("name", name);
       // set Fragmentclass Arguments
                    TabFragment tab1 = new TabFragment();
                    tab1.setArguments(bundle);*/


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_VisitorInfo");

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

}
