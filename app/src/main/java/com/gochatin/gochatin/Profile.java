package com.gochatin.gochatin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import halper.AppController;
import halper.SQLiteHandler;
import halper.SessionManager;
import halper.WebsocketClass;

public class Profile extends AppCompatActivity {

    private Toolbar toolbar;
    public static ViewPager viewPager;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/





        setupToolbar();
        setupViewPager();

        setupCollapsingToolbar();






        // setupDrawer();
    }



     /*private void setupDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }*/

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
    }

    private void setupViewPager() {
      //  final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
       viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Users.Visitor_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TabFragment(), "Personal");
        adapter.addFrag(new Tab1(), "Commnication");
        adapter.addFrag(new Tab2(), "Notes");

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

        pDialog.setMessage("Please wait ...");
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
                        SessionManager sessionManager = new SessionManager(Profile.this);
                        sessionManager.setLogin(false);

                        stopService(new Intent(Profile.this, WebsocketClass.class));


                        Intent intent= new Intent(Profile.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Profile.this,jObj.getString("message"),Toast.LENGTH_LONG).show();
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
                Log.e("TAG", "LogOut Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            public HashMap<String, String> user;

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

            Intent intent = new Intent(Profile.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id==R.id.idlogout){
            logout();
//            SessionManager sessionManager = new SessionManager(this);
//            sessionManager.setLogin(false);
//            Intent intent= new Intent(Users.this,MainActivity.class);
//            startActivity(intent);
            return true;
        }else if (id == R.id.home) {

            Intent intent = new Intent(Profile.this,Users.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
