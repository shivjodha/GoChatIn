package network;



/**
 * Created by Dell on 6/16/2017.
 */

public class ApiClient {
    public static final String BASE_URL = "http://api.androidhive.info/json/";
    public static final String URL_WEBSOCKET = "ws://192.168.0.102:8080/WebMobileGroupChatServer/chat?name=";
  /*  private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }*/
}
