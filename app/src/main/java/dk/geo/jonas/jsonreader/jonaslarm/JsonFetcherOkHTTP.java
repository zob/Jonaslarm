package dk.geo.jonas.jsonreader.jonaslarm;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by lars on 09-07-15.
 */
public class JsonFetcherOkHTTP implements JsonFetcher {
    public static final String TAG = "JsonFetcherOkHTTP.java";
    OkHttpClient client = new OkHttpClient();
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    @Override
    public JSONObject getThisMofo(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String responseBodyString = response.body().string();

//        try {
//
////            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "n");
//            }
//            is.close();
//            json = sb.toString();
//
//        } catch (Exception e) {
//            Log.e(TAG, "Error converting result " + e.toString());
//        }

        // try parse the string to a JSON object
        try {
            Log.e(TAG, "Before parsing");
            jObj = new JSONObject(responseBodyString);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        // return JSON String
        Log.e(TAG, "Before returning jObj:" + jObj);
        return jObj;
    }
}
