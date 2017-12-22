package com.gochatin.gochatin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.TagsAdapter;
import halper.AppController;
import halper.SQLiteHandler;
import model.Tagsmodel;

/**
 * Created by Dell on 6/21/2017.
 */

public class TabFragment extends Fragment {
    private ListAdapter mAdapter;

    private String mItemData = "Lorem Ipsum is simply dummy text of the printing and "
            + "typesetting industry Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
    private EditText nameedit;
    private EditText emailedit;
    private EditText contactedit;
    private EditText genderedit;
    private EditText addressedit;
    private EditText cityedit;
    private EditText editstate;
    private EditText zipedit;
    private EditText birthedit;
    private EditText statusedit;
    private ImageButton Saveeditbutton;
    private ImageButton editbutton;
    private ImageButton closebutton;
    private ProgressDialog pDialog;
    private Spinner genderspinner;
    private Spinner statusspinner;
    private Spinner connectsimid;
    private EditText contactesimdit;
    public String SpinnerGenderValue;
    public String SpinnerStatusValue;
    private String SpinnerConnectsimidValue;
    private ListView taglist;
    private LinearLayout ll;
    private Spinner tagsapinner;
    private Spinner spinner;
    private HashMap<String, String> user;
    private LinearLayout lll;
    private EditText editcountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
      /*  Bundle bundle = this.getArguments();


        if (bundle != null){
            String name=bundle.getString("name");

            nameedit.setText(name);
        }*/
     /*  RecyclerView recyclerView = (RecyclerView) view.findViewById(
                R.id.fragment_list_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        String[] listItems = mItemData.split(" ");

        List<String> list = new ArrayList<String>();
        Collections.addAll(list, listItems);

        mAdapter = new ListAdapter(list);
        recyclerView.setAdapter(mAdapter);*/
         ll = (LinearLayout)view.findViewById(R.id.linearlayout3);
         lll =(LinearLayout)view.findViewById(R.id.linearlayout4);

        nameedit = (EditText)view.findViewById(R.id.editnameid);
        emailedit = (EditText)view.findViewById(R.id.editemailid);
        contactedit =(EditText)view.findViewById(R.id.editcontactid);
        genderedit =(EditText)view.findViewById(R.id.editgenderid);
        addressedit =(EditText)view.findViewById(R.id.editaddressid);
        cityedit = (EditText)view.findViewById(R.id.editcityid);
        editstate = (EditText)view.findViewById(R.id.editstateid);
        editcountry = (EditText)view.findViewById(R.id.editcountry);
        zipedit = (EditText)view.findViewById(R.id.editzipid);
        birthedit = (EditText)view.findViewById(R.id.editbirthid);
        statusedit =(EditText)view.findViewById(R.id.editstatusid);
        contactesimdit = (EditText)view.findViewById(R.id.editconnectsinid);

        genderspinner =(Spinner)view.findViewById(R.id.spinnergender);
        statusspinner = (Spinner)view.findViewById(R.id.spinnerstatus);
        connectsimid = (Spinner)view.findViewById(R.id.spinnerconnectsinn);
      /*  tagsapinner = (Spinner)view.findViewById(R.id.tagspinner);
*/
       Saveeditbutton = (ImageButton) view.findViewById(R.id.save_button2);
       editbutton = (ImageButton) view.findViewById(R.id.edit_button1);
        closebutton = (ImageButton) view.findViewById(R.id.cancel);


        final String[] select_qualification = {
                "Select Qualification", "10th / Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
         spinner = (Spinner)view.findViewById(R.id.tagspinner);




        setprofiledata();


        birthedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeEditable();

            }
        });


        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uneditableview();
            }
        });


        Saveeditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameedit.getText().toString().equalsIgnoreCase("")){
                    UpdateProfile();
                }else{
                    Toast.makeText(getContext(),getResources().getString(R.string.please_enter_valid_name),Toast.LENGTH_LONG).show();
                }


            }
        });


        genderspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // SpinnerGenderValue = parent.getItemAtPosition(position).toString();

                if(position==1){
                    SpinnerGenderValue="male";
                }else if(position==2){
                    SpinnerGenderValue="female";
                }else{
                    SpinnerGenderValue="Select Gender";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  statusspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SpinnerStatusValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        connectsimid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerConnectsimidValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private void UpdateProfile() {

        String tag_string_req = "req_profileupdate";
        SQLiteHandler db = new SQLiteHandler(getContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();
        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.d("Personal info",jObj.toString());
                    if(jObj.getString("status").equalsIgnoreCase("success")){
                        Toast.makeText(getContext(),getResources().getString(R.string.profile_edited_successfully),Toast.LENGTH_LONG).show();
                        Uneditableview();
                    }else{
                        Toast.makeText(getContext(),getResources().getString(R.string.falied_to_update),Toast.LENGTH_LONG).show();
                    }


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
                params.put("mid",user.get("mid"));
                params.put("visitor_id",Users.visitor_id);
                params.put("request","Dashboard_UpdateVisitorInfo");
                params.put("name",nameedit.getText().toString());
                if(!emailedit.getText().toString().equalsIgnoreCase("")){
                    params.put("email",emailedit.getText().toString());
                }

               // params.put("code","91");
                if(!contactedit.getText().toString().trim().equals("")){
                    params.put("phone",contactedit.getText().toString().trim());
                }

                if(!SpinnerGenderValue.equals("Select Gender")){
                    params.put("gender",SpinnerGenderValue);
                }

                if(!TagsAdapter.shiv.equals("")){
                    params.put("tag",TagsAdapter.shiv);
                }


                if(!birthedit.getText().toString().equalsIgnoreCase("")){
                    params.put("birthdate",birthedit.getText().toString());
                }
                if(!addressedit.getText().toString().equalsIgnoreCase("")){
                    params.put("address",addressedit.getText().toString());
                }

                if(!cityedit.getText().toString().equalsIgnoreCase("")){
                    params.put("city",cityedit.getText().toString());
                }


                if(!editstate.getText().toString().equalsIgnoreCase("")){
                    params.put("state",editstate.getText().toString());
                }

                if(!editcountry.getText().toString().equalsIgnoreCase("")){
                   params.put("country",editcountry.getText().toString());
                }
              //  params.put("country",addressedit.getText().toString());
                if(!zipedit.getText().toString().equalsIgnoreCase("")){
                    params.put("zipcode",zipedit.getText().toString());
                }

                if(!SpinnerConnectsimidValue.equalsIgnoreCase("")){
                    params.put("type",SpinnerConnectsimidValue);
                }


                Log.d("send Parms Edit Profile",params.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    Calendar myCalendar = Calendar.getInstance();


    private void Uneditableview() {

        nameedit.setCursorVisible(false);
        nameedit.setClickable(false);
        nameedit.setFocusable(false);
        nameedit.setFocusableInTouchMode(false);

        emailedit.setCursorVisible(false);
        emailedit.setClickable(false);
        emailedit.setFocusable(false);
        emailedit.setFocusableInTouchMode(false);

        genderedit.setCursorVisible(false);
        genderedit.setClickable(false);
        genderedit.setFocusable(false);
        genderedit.setFocusableInTouchMode(false);
        genderedit.setVisibility(View.VISIBLE);
        genderspinner.setVisibility(View.GONE);


        contactedit.setCursorVisible(false);
        contactedit.setClickable(false);
        contactedit.setFocusable(false);
        contactedit.setFocusableInTouchMode(false);

        addressedit.setCursorVisible(false);
        addressedit.setClickable(false);
        addressedit.setFocusable(false);
        addressedit.setFocusableInTouchMode(false);

        cityedit.setCursorVisible(false);
        cityedit.setClickable(false);
        cityedit.setFocusable(false);
        cityedit.setFocusableInTouchMode(false);

        editstate.setCursorVisible(false);
        editstate.setClickable(false);
        editstate.setFocusable(false);
        editstate.setFocusableInTouchMode(false);

        editcountry.setCursorVisible(false);
        editcountry.setClickable(false);
        editcountry.setFocusable(false);
        editcountry.setFocusableInTouchMode(false);

        zipedit.setCursorVisible(false);
        zipedit.setClickable(false);
        zipedit.setFocusable(false);
        zipedit.setFocusableInTouchMode(false);

        birthedit.setCursorVisible(false);
        birthedit.setClickable(false);
        birthedit.setFocusable(false);
        birthedit.setFocusableInTouchMode(false);

        statusedit.setCursorVisible(false);
        statusedit.setClickable(false);
        statusedit.setFocusable(false);
        statusedit.setFocusableInTouchMode(false);
        statusedit.setVisibility(View.VISIBLE);
        statusspinner.setVisibility(View.GONE);

        contactesimdit.setCursorVisible(false);
        contactesimdit.setClickable(false);
        contactedit.setFocusable(false);
        contactedit.setFocusableInTouchMode(false);
        contactesimdit.setVisibility(View.VISIBLE);
        connectsimid.setVisibility(View.GONE);

        editbutton.setVisibility(View.VISIBLE);
        Saveeditbutton.setVisibility(View.GONE);
        closebutton.setVisibility(View.GONE);

        lll.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);

    }

    private void ChangeEditable() {

        nameedit.setCursorVisible(true);
        nameedit.setClickable(true);
        nameedit.setFocusable(true);
        nameedit.setFocusableInTouchMode(true);

        emailedit.setCursorVisible(true);
        emailedit.setClickable(true);
        emailedit.setFocusable(true);
        emailedit.setFocusableInTouchMode(true);

        genderedit.setCursorVisible(true);
        genderedit.setClickable(true);
        genderedit.setFocusable(true);
        genderedit.setFocusableInTouchMode(true);
        genderedit.setVisibility(View.GONE);
        genderspinner.setVisibility(View.VISIBLE);

        contactedit.setCursorVisible(true);
        contactedit.setClickable(true);
        contactedit.setFocusable(true);
        contactedit.setFocusableInTouchMode(true);

        addressedit.setCursorVisible(true);
        addressedit.setClickable(true);
        addressedit.setFocusable(true);
        addressedit.setFocusableInTouchMode(true);


        cityedit.setCursorVisible(true);
        cityedit.setClickable(true);
        cityedit.setFocusable(true);
        cityedit.setFocusableInTouchMode(true);

        editstate.setCursorVisible(true);
        editstate.setClickable(true);
        editstate.setFocusable(true);
        editstate.setFocusableInTouchMode(true);

        editcountry.setCursorVisible(true);
        editcountry.setClickable(true);
        editcountry.setFocusable(true);
        editcountry.setFocusableInTouchMode(true);

        zipedit.setCursorVisible(true);
        zipedit.setClickable(true);
        zipedit.setFocusable(true);
        zipedit.setFocusableInTouchMode(true);

        birthedit.setCursorVisible(true);
        birthedit.setClickable(true);
        birthedit.setFocusable(true);
        birthedit.setFocusableInTouchMode(true);

      /*  statusedit.setCursorVisible(true);
        statusedit.setClickable(true);
        statusedit.setVisibility(View.GONE);
        statusspinner.setVisibility(View.VISIBLE);
*/

        contactesimdit.setCursorVisible(true);
        contactesimdit.setClickable(true);
        contactedit.setFocusable(true);
        contactedit.setFocusableInTouchMode(true);
        contactesimdit.setVisibility(View.GONE);
        connectsimid.setVisibility(View.VISIBLE);


        Saveeditbutton.setVisibility(View.VISIBLE);
        closebutton.setVisibility(View.VISIBLE);

        ll.setVisibility(View.GONE);
        lll.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }






    private void setprofiledata() {
        String tag_string_req = "req_profile";

        pDialog.setMessage("please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Profile Response: " + response.toString());
                hideDialog();
              //  Toast.makeText(getActivity(),"Login done",Toast.LENGTH_LONG).show();

                try {
                    JSONObject jObj = new JSONObject(response);


                    JSONObject user1 = jObj.getJSONObject("param");

                    if(!user1.has("tags")){

                        JSONObject user = jObj.getJSONObject("param");
                        JSONObject jsonObject = user.getJSONObject("visitor");
                        JSONObject jsonObject1 = user.getJSONObject("details");
                        nameedit.setText(jsonObject.getString("name"));
                        emailedit.setText(jsonObject.getString("email"));
                        contactedit.setText(jsonObject.getString("phone"));
                        cityedit.setText(jsonObject1.getString("City"));
                        zipedit.setText(jsonObject1.getString("Zip"));
                        editcountry.setText(jsonObject1.getString("Country"));


                    }else{
                        JSONObject user = jObj.getJSONObject("param");
                        JSONArray tags = user.getJSONArray("tags");

                  /*      List<String> tagvalue = new ArrayList<String>();
                        String tagvalues[] ;

                        for(int i=0;i < tag.length();i++){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject=tag.getJSONObject(i);
                            tagvalue.add(jsonObject.getString("name"));
                            tagvalue.add("example");



                        }*/
                        ArrayList<Tagsmodel> listVOs = new ArrayList<>();

                       for (int i = 0; i < tags.length(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject=tags.getJSONObject(i);
                            Tagsmodel stateVO = new Tagsmodel();
                            stateVO.setTitle(jsonObject.getString("name"));
                            stateVO.setId(jsonObject.getString("id"));
                            stateVO.setSelected(false);
                            listVOs.add(stateVO);
                        }

                        TagsAdapter myAdapter = new TagsAdapter(getContext(), 0,
                                listVOs);
                        spinner.setAdapter(myAdapter);


                        ll.setVisibility(View.VISIBLE);
                        final TextView[] myTextViews = new TextView[tags.length()]; // create an empty array;

                        for (int i = 0; i < tags.length(); i++) {
                            // create a new textview
                            final TextView rowTextView = new TextView(getContext());

                            // set some properties of rowTextView or something
                            RoundRectShape rs = new RoundRectShape(new float[] { 10, 10, 10, 10, 10, 10, 10, 10 }, null, null);

                            ShapeDrawable sd =  new ShapeDrawable(rs);
                            rowTextView.setBackgroundDrawable(sd);
                            rowTextView.setGravity(Gravity.LEFT);
                            //rowTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            JSONObject jsonObject = new JSONObject();
                            jsonObject=tags.getJSONObject(i);
                            rowTextView.setText(jsonObject.getString("name"));
                            rowTextView.setPadding(15,15,15,15);


                            rowTextView.setTextColor(getResources().getColor(R.color.white));
                            rowTextView.setBackgroundColor(getResources().getColor(R.color.blue_500));

                            LinearLayout.LayoutParams lp = new
                                    LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);

                            lp.setMargins(0, 20, 0,0);
                            rowTextView.setLayoutParams(lp);

                            // add the textview to the linearlayout
                            ll.addView(rowTextView);

                            // save a reference to the textview for later
                            myTextViews[i] = rowTextView;
                        }


                    /*    Log.d("listvalue",tagvalue.toString());

                        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),
                                android.R.layout.simple_list_item_multiple_choice,
                                tagvalue);
*/


                        JSONObject jsonObject = user.getJSONObject("details");

                        Log.d("Personal info",jsonObject.toString());

                        String name = jsonObject.getString("Name");
                        String email = jsonObject.getString("Email");
                        String Contact = jsonObject.getString("Contact No.");
                        String gender = jsonObject.getString("Gender");
                        String Country =jsonObject.getString("Country");
                        String City = jsonObject.getString("City");
                        String State = jsonObject.getString("State");

                        String Zip = jsonObject.getString("Zip");
                        String Birth_Date = jsonObject.getString("Birth Date");
                        String Status = jsonObject.getString("Status");
                        String Connects_in= jsonObject.getString("Connects in");
                        String Address = jsonObject.getString("Address");

                        nameedit.setText(name);
                        emailedit.setText(email);
                        genderedit.setText(gender);
                        contactedit.setText(Contact);
                        addressedit.setText(Address);
                        cityedit.setText(City);
                        editstate.setText(State);
                        editcountry.setText(Country);
                        zipedit.setText(Zip);
                        birthedit.setText(Birth_Date);
                        statusedit.setText(Status);
                        contactesimdit.setText(Connects_in);

                        if(gender.equalsIgnoreCase("Male")){
                            genderspinner.setSelection(1);
                        }else if(gender.equalsIgnoreCase("Female")){
                            genderspinner.setSelection(2);
                        }else{
                            genderspinner.setSelection(0);
                        }

                 /*   if(Status.equalsIgnoreCase("Active")){
                        statusspinner.setSelection(1);
                    }else if(gender.equalsIgnoreCase("Inactive")){
                        statusspinner.setSelection(2);
                    }else{
                        statusspinner.setSelection(0);
                    }*/

                        if(Status.equalsIgnoreCase("Leads")){
                            connectsimid.setSelection(1);
                        }else{
                            connectsimid.setSelection(0);
                        }


                    }



                  /*  Bundle bundle = new Bundle();
                    bundle.putString("name", name);
// set Fragmentclass Arguments
                    TabFragment tab1 = new TabFragment();
                    tab1.setArguments(bundle);*/


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {

        String myFormat = "yy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthedit.setText(sdf.format(myCalendar.getTime()));
    }

}
