package contafe.snipsmash.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contafe.snipsmash.R;
import contafe.snipsmash.adapters.SnipsAdapter;
import contafe.snipsmash.defaults.Constants;
import contafe.snipsmash.defaults.TokenManager;
import contafe.snipsmash.models.SnipObject;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

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

        ArrayList<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Authorization", "Bearer " + tokenManager.getAccessToken()));
        headers.add(new BasicHeader("Content-Type", "application/json"));

        favSnipClient.get(
            SnipListActivity.this,
            Constants.API_URL + Constants.FAV_SNIPS,
            (Header[]) headers.toArray(),
            new RequestParams(),
            asyncHttpResponseHandler
        );

    }


}
