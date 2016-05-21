package contafe.snipsmash.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import contafe.snipsmash.R;
import contafe.snipsmash.adapters.SnipsAdapter;
import contafe.snipsmash.models.SnipObject;
import contafe.snipsmash.defaults.Constants;
import contafe.snipsmash.defaults.TokenManager;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SnipListActivity extends AppCompatActivity {

    private ArrayList<SnipObject> snips;
    private RecyclerView snipView;
    private SnipsAdapter snipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snip_list);
        snipView = (RecyclerView) findViewById(R.id.recyclerView);
        snipView.setHasFixedSize(true);

        getFavouriteSnipList();
    }

    protected void getFavouriteSnipList() {
        AsyncHttpClient favSnipClient = new AsyncHttpClient();
        JSONObject favSnipRequest = new JSONObject();
        TokenManager tokenManager = new TokenManager(this);
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray favSnipList = new JSONArray(new String(responseBody));
                    for (int i = 0; i < favSnipList.length(); i++) {
                        JSONObject snipJSON = favSnipList.getJSONObject(i);
                        SnipObject snip = new SnipObject(
                            snipJSON.getString("updated_at"),
                            snipJSON.getJSONObject("snip").getString("waveform_raw_data"),
                            snipJSON.getJSONObject("snip").getString("url_aac"),
                            snipJSON.getJSONObject("snip").getString("slug"),
                            snipJSON.getJSONObject("snip").getString("name"),
                            snipJSON.getJSONObject("snip").getString("url"),
                            snipJSON.getJSONObject("snip").getString("waveform_640x128")
                        );
                        snips.add(snip);
                    }
                    snipView.setLayoutManager(new LinearLayoutManager(SnipListActivity.this));
                    snipsAdapter = new SnipsAdapter(snips);
                    snipView.setAdapter(snipsAdapter);
                    snipsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    System.out.println("The response JSON doesn't work.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(String.valueOf(statusCode) + ": " + new String(responseBody) + " with headers: " + headers.toString());
            }
        };

        try {
            favSnipRequest.put("Authorization", "Bearer " + tokenManager.getAccessToken());

            StringEntity entity = new StringEntity(favSnipRequest.toString());

            favSnipClient.get(
                SnipListActivity.this,
                Constants.API_URL + Constants.FAV_SNIPS,
                entity,
                "application/json",
                asyncHttpResponseHandler
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


}
