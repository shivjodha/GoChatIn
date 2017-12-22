package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gochatin.gochatin.AppConfig;
import com.gochatin.gochatin.R;
import com.gochatin.gochatin.Tab2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import halper.AppController;
import model.Message;
import model.Report;

/**
 * Created by Dell on 7/4/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private final Context context;
    private  List<Report> reports;
    private String[] mDataset;
    private ProgressDialog pDialog;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton mimagebtdelete;
        public CardView mCardView;
        public TextView mTextViewnote;
        public TextView mTextViewdate;
        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextViewnote = (TextView) v.findViewById(R.id.noteid);
            mTextViewdate = (TextView) v.findViewById(R.id.dateid);
            mimagebtdelete = (ImageButton)v.findViewById(R.id.deletbuttonnotes);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReportAdapter(List<Report> reports,Context context) {

        this.reports = reports;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);

        holder.mTextViewnote.setText(reports.get(position).getNote());
        holder.mTextViewdate.setText(reports.get(position).getNoteDate());
        holder.mimagebtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String currentValue = reports.get(position).;
               // Log.d("CardView", "CardView Clicked: " + currentValue);
                //Toast.makeText(context,reports.get(position).getId().toString(),Toast.LENGTH_LONG).show();


                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Deletenote(position);
                            }


                        })
                        .setNegativeButton("No", null)
                        .show();




            }
        });
    }

    private void Deletenote(final int position) {
        String tag_string_req = "delete_notes";

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
                        Toast.makeText(context,jObj.get("message").toString(),Toast.LENGTH_LONG).show();
                        reports.remove(position);
                        notifyDataSetChanged();

                    }else{
                        Toast.makeText(context,context.getResources().getString(R.string.failed_to_submit),Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("note_id",reports.get(position).getId());
                params.put("request","Dashboard_DeleteNote");

                Log.d("Report add data",params.toString());
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

    @Override
    public int getItemCount() {
        return reports.size();
    }
}
