package adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gochatin.gochatin.R;
import com.gochatin.gochatin.VideoViewActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import halper.LoadImage;
import model.MessageV;

import static android.content.ContentValues.TAG;

/**
 * Created by Dell on 6/19/2017.
 */

public class MessagesListAdapter extends BaseAdapter  {

    private Context context;
    private List<MessageV> messagesItems;
    private ImageView videoview;
    private ImageButton Playbutton;
    private ProgressBar progressBar;
    String VideoURL = "";
    private TextView txtMsg;
    private LruCache<String, Bitmap> mMemoryCache;
    Bitmap image=null;
    private FrameLayout videoviewframe;

    public MessagesListAdapter(Context context, List<MessageV> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                Log.d("itswork","yesitis");
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;


    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */

        MessageV m = messagesItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if (messagesItems.get(position).isSelf()) {
            // message belongs to you, so load the right aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);
        } else {
            // message belongs to other person, load the left aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }




        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);
        ImageView imagemsg = (ImageView) convertView.findViewById(R.id.imgmsg);
        CardView cartdv = (CardView) convertView.findViewById(R.id.cardviewchat);
        TextView msgtext = (TextView) convertView.findViewById(R.id.fileextention);
        videoviewframe =(FrameLayout)convertView.findViewById(R.id.video_frame);
        videoview = (ImageView)convertView.findViewById(R.id.VideoView);
        Playbutton = (ImageButton)convertView.findViewById(R.id.play_button);
        progressBar = (ProgressBar)convertView.findViewById(R.id.progressbar);
        Playbutton.setTag(position);
        lblFrom.setText(m.getFromName());




      if(!m.getFile().equalsIgnoreCase("")){

          String string = m.getFile();
          Log.d("valuefinal",string);
          String[] parts = string.split("files\\/");
          String part1 = parts[0]; // 004
          String part2 = parts[1];
          Log.d("part1",part1);
          Log.d("part2",part2);
          String[] exten = part2.split(Pattern.quote("."));
         String part3 = exten[1];

          if(part3.equalsIgnoreCase("png")||part3.equalsIgnoreCase("jpg")||part3.equalsIgnoreCase("jpeg")){
              imagemsg.setVisibility(View.VISIBLE);
              txtMsg.setVisibility(View.GONE);
              Picasso.with(context).load(m.getFile()).into(imagemsg);
          }else if(part3.equalsIgnoreCase("mpeg")||part3.equalsIgnoreCase("3gp")||part3.equalsIgnoreCase("mp4")) {
              txtMsg.setVisibility(View.GONE);
              videoviewframe.setVisibility(View.VISIBLE);
              videoview.setVisibility(View.VISIBLE);
              Bitmap bmThumbnail;
              bmThumbnail = ThumbnailUtils.createVideoThumbnail(messagesItems.get(position).getFile(), MediaStore.Video.Thumbnails.MICRO_KIND);
              videoview.setImageBitmap(bmThumbnail);

          }else{
              txtMsg.setVisibility(View.GONE);
              cartdv.setVisibility(View.VISIBLE);
              msgtext.setText(part2);
          }


      }else{


         txtMsg.setVisibility(View.VISIBLE);
         imagemsg.setVisibility(View.GONE);
        // Spanned spanned = Html.fromHtml(source, this, null);
         //Spanned Spanned result=Html.fromHtml(m.getMessage(),new ImageGetter(), null);
          //txtMsg.setText(result);*/
          Log.d("Lastmessage",m.getMessage());
          String message = m.getMessage();
        String nessege2=  message.replace("\\r\\n", "<br>").replace ("\n", "<br>");
          Log.d("newm",message);
         txtMsg.setText(Html.fromHtml(nessege2,new ImageGetter(), null));
       // txtMsg.setText(spanned);
     }

       // imagemsg.setImageBitmap(m.getFile());


        Playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = Integer.parseInt(String.valueOf(v.getTag()));
                Playbutton.setVisibility(View.GONE);
                VideoURL=messagesItems.get(pos).getFile();

                Intent intent = new Intent(context, VideoViewActivity.class);
                intent.putExtra("Url",VideoURL);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    public class ImageGetter implements Html.ImageGetter {
        Bitmap bitmap = null;
        int id=0;
        private Drawable d;

        public Drawable getDrawable(String source) {

          //  Log.d("source",source);

            if(source!=null){
                try {

                    if (source.contains("emoji")) {
                        String[] parts = source.split("master/");
                        String part2 =parts[1];
                        String[] part3 =part2.split("\\.");
                        String part4 =part3[0];

                        id =context.getResources().getIdentifier(part4,"drawable",context.getPackageName());
                        d = context.getResources().getDrawable(id);
                        Log.d("resourcevalue",String.valueOf(id));
                    }else{
                        bitmap = getBitmapFromMemCache(source);

                        if (bitmap != null) {
                            d =new BitmapDrawable(context.getResources(),bitmap);
                            Log.d("chchereturned",bitmap.toString());
                        } else {
                            try {
                                bitmap =  new LoadImagelocal().execute(source).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            d =new BitmapDrawable(context.getResources(),bitmap);
                        }
                    }


            /*else  {

                try {
                    bitmap =  new LoadImagelocal().execute(source).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
              d =new BitmapDrawable(context.getResources(),bitmap);
            }*/



           /* if (source.equals("stack.jpg")) {
                id = R.drawable.stack;
            }
            else if (source.equals("overflow.jpg")) {
                id = R.drawable.overflow;
            }
            else {
                return null;
            }*/



        /*   new LoadImage(new LoadImage.AsyncResponse(){

                @Override
                public void processFinish(Bitmap output){
                    //Here you will receive the result fired from async class
                    //of onPostExecute(result) method.
                    Toast.makeText(context,output.toString(),Toast.LENGTH_LONG).show();
                    bitmap=output;
                   *//* CharSequence charSequence = txtMsg.getText();
                    txtMsg.setText(charSequence);*//*
                }
            }).execute(source);*/
           /* try {
                bitmap =  new LoadImage().execute(source).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
*/

                    // Drawable d =new BitmapDrawable(context.getResources(),bitmap);

                    if(source.contains("emoji")){
                        d.setBounds(0,0,40,40);
                    }else{
                        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }






            return d;
        }

    }



  class LoadImagelocal extends AsyncTask<String, Void, Bitmap> {


       private Bitmap bitmap1;



        @Override
        protected Bitmap doInBackground(String... params) {

            try {

                URL url=new URL(params[0]);
                // url = new URL("http://www.gravatar.com/avatar/a9317e7f0a78bb10a980cadd9dd035c9?s=32&d=identicon&r=PG");
                 bitmap1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                addBitmapToMemoryCache(String.valueOf(params[0]), bitmap1);

                // bitmap = Picasso.with(MainActivity.this).load("http://www.gravatar.com/avatar/a9317e7f0a78bb10a980cadd9dd035c9?s=32&d=identicon&r=PG").get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap1;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {

            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                Log.d(TAG, "onPostExecuteafter" + bitmap);
                // myMethod(bitmap);

                CharSequence charSequence = txtMsg.getText();
                txtMsg.setText(charSequence);

            }

        }
    }



    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


}
