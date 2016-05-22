package contafe.snipsmash.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import contafe.snipsmash.R;
import contafe.snipsmash.defaults.Constants;
import contafe.snipsmash.defaults.TokenManager;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

public class MergedDubActivity extends AppCompatActivity {
    public ImageButton playButton;
    public ImageButton saveButton;
    private static File audioFile;
    private String ffmpegBinary;
    private File outputDir;
    Integer counter;
    Integer numUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merged_dub);
        Intent intent = getIntent();
        ArrayList<String> urls = intent.getStringArrayListExtra("data");
        playButton = (ImageButton) findViewById(R.id.playMergedButton);
        playButton.setImageResource(R.drawable.playbig);
        saveButton = (ImageButton) findViewById(R.id.saveMergedButton);
        saveButton.setImageResource(R.drawable.savebig);
        for (String url : urls) {
            System.out.println("URL: " + url);
        }

        JSONArray jsonArray = new JSONArray(Arrays.asList(urls));
        System.out.println(jsonArray.toString().substring(1, jsonArray.toString().length() - 1));
        RequestParams params = new RequestParams();
        params.add("data", jsonArray.toString().substring(1, jsonArray.toString().length() - 1));

        AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://contafe.com/hack/mergeFiles.php", params, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onStart() {
                //start progressbar
                System.out.println("STARTED");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                System.out.println("FAILURE");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                System.out.println("SUCCESS" + file.getName() + " + " + file.getAbsolutePath());
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
                mp.prepareAsync();

                audioFile = file;
            }
        });
    }

    private void createSnip() {

//        TokenManager tokenManager = new TokenManager(MergedDubActivity.this);
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity("Hello");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        asyncHttpClient.post(MergedDubActivity.this, Constants.API_URL, entity, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//
//                        String response = new String(responseBody);
//
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            if (!jsonResponse.has("error")) {
//                                System.out.println("RESPONSE: " + jsonResponse.toString());
//
//                            } else {
//                                Toast.makeText(MergedDubActivity.this, jsonResponse.getString("error_description"), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                        Toast.makeText(MergedDubActivity.this, "Something went wrong. Please retry again.", Toast.LENGTH_SHORT).show();
//                        String response = new String(responseBody);
//                        System.out.println("ERROR RESPONSE: " + response);
//                    }
//                });
    }
}