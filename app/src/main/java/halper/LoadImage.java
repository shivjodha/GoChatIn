package halper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import adapter.MessagesListAdapter;

import static android.content.ContentValues.TAG;

/**
 * Created by Dell on 8/21/2017.
 */

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    private Bitmap bitmap1;


    public interface AsyncResponse {
        void processFinish(Bitmap output);
    }

    public LoadImage.AsyncResponse delegate = null;

    public LoadImage(LoadImage.AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {

            URL url=new URL(params[0]);
            // url = new URL("http://www.gravatar.com/avatar/a9317e7f0a78bb10a980cadd9dd035c9?s=32&d=identicon&r=PG");
            bitmap1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());


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

            delegate.processFinish(bitmap);

        }

    }
}
