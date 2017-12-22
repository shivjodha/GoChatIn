package com.gochatin.gochatin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ReportAdapter;
import halper.AppController;
import model.Message;
import model.Report;

/**
 * Created by Dell on 6/15/2017.
 */

public class Tab2 extends Fragment {

    private ProgressDialog pDialog;
    private List<Report> reports = new ArrayList<>();
    private ReportAdapter adapter;
    private EditText noteedittext;
    private Button notesubmitbutton;
    private String ContactID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        noteedittext = (EditText)v.findViewById(R.id.etMessageBox);
        notesubmitbutton = (Button)v.findViewById(R.id.btnotesubmit);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recycler_viewnotes);
        rv.setHasFixedSize(true);
        adapter = new ReportAdapter(reports,getContext());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        setreportdata();



        notesubmitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!noteedittext.getText().toString().equalsIgnoreCase("")){
                    SubmitNote();
                }else{
                    Toast.makeText(getContext(),"Not Can't be Blank",Toast.LENGTH_LONG).show();
                }

            }
        });

        return v;
    }




    private void SubmitNote() {

        String tag_string_req = "add_notes";

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
                        Toast.makeText(getContext(),jObj.get("message").toString(),Toast.LENGTH_LONG).show();
                        setreportdata();
                        noteedittext.setText("");


                    }else{
                       Toast.makeText(getContext(),getResources().getString(R.string.failed_to_submit),Toast.LENGTH_LONG).show();

                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                  //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
             /*   Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
               hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("contact_id",ContactID);
                params.put("request","Dashboard_AddNote");
                params.put("note",noteedittext.getText().toString());
                Log.d("Report add data",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void setreportdata() {
        String tag_string_req = "req_profile";

        pDialog.setMessage("Please wait ...");
       // showDialog();

        reports.clear();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Notes Response: " + response.toString());
              //  hideDialog();
               // Toast.makeText(getActivity(),"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject user1 = jObj.getJSONObject("param");

                    if(!user1.has("tags")){

                        adapter.notifyDataSetChanged();

                    }else{

                        JSONObject user = jObj.getJSONObject("param");
                        JSONObject jsonObject = user.getJSONObject("details");
                        ContactID =jsonObject.getString("contact_id");

                        JSONArray notesarray = user.getJSONArray("notes");

                        Log.d("Personal info",notesarray.toString());


                        for(int n = 0; n < notesarray.length(); n++)
                        {
                            JSONObject object = notesarray.getJSONObject(n);
                            Report report = new Report();
                            report.setId(object.getString("id"));
                            report.setContactnameId(object.getString("contact_name_id"));
                            report.setNote(object.getString("note"));
                            report.setNoteDate(object.getString("date"));

                            // do some stuff....
                            reports.add(report);
                        }

                        adapter.notifyDataSetChanged();

                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                   // Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
              /*  Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();*/
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_VisitorInfo");
                Log.d("getvisitordata",params.toString());
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