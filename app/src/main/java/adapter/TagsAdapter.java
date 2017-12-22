package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gochatin.gochatin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.Tagsmodel;

/**
 * Created by Dell on 7/7/2017.
 */

public class TagsAdapter extends ArrayAdapter<Tagsmodel> {
private Context mContext;
private ArrayList<Tagsmodel> listState;
private TagsAdapter myAdapter;
private boolean isFromView = false;
     public static List<String> ids = new ArrayList<String>();
    public static String shiv="";

public TagsAdapter(Context context, int resource, List<Tagsmodel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<Tagsmodel>) objects;
        this.myAdapter = this;
        }

@Override
public View getDropDownView(int position, View convertView,
                            ViewGroup parent) {
        return getCustomView(position, convertView, parent);
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
        }

public View getCustomView(final int position, View convertView,
        ViewGroup parent) {

final ViewHolder holder;
        if (convertView == null) {
        LayoutInflater layoutInflator = LayoutInflater.from(mContext);
        convertView = layoutInflator.inflate(R.layout.spinner_item, null);
        holder = new ViewHolder();
        holder.mTextView = (TextView) convertView
        .findViewById(R.id.text);
        holder.mCheckBox = (CheckBox) convertView
        .findViewById(R.id.checkbox);
        convertView.setTag(holder);
        } else {
        holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
        holder.mCheckBox.setVisibility(View.VISIBLE);
        } else {
        holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

       @Override
       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int Position = (Integer) buttonView.getTag();
       // Toast.makeText(mContext,"Position"+ String.valueOf(Position),Toast.LENGTH_LONG).show();
      //  Toast.makeText(mContext,"list"+ids.toString(),Toast.LENGTH_LONG).show();
        if(ids.contains(listState.get(Position).getId())){
                int index = ids.indexOf(listState.get(Position).getId());
            ids.remove(index);
        }else{
            ids.add(listState.get(Position).getId());
        }

     shiv=  android.text.TextUtils.join(",", ids);
      // Toast.makeText(getContext(),shiv,Toast.LENGTH_LONG).show();


        }
        });
        return convertView;
        }

private class ViewHolder {
    private TextView mTextView;
    private CheckBox mCheckBox;
}
}
