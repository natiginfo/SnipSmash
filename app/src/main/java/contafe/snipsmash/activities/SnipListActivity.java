package contafe.snipsmash.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private Button mergeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snip_list);
        snipView = (RecyclerView) findViewById(R.id.recyclerView);
        snipView.setHasFixedSize(true);
        snips = new ArrayList<>();

        getFavouriteSnipList();
        mergeButton = (Button) findViewById(R.id.buttonMerge);
        mergeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merge();
            }
        });
    }

    private void merge() {
        List<SnipObject> snipsSelected = filterSelected();

    }

    private List<SnipObject> filterSelected() {
        ArrayList<SnipObject> selected = new ArrayList<>(snips.size());

        for (SnipObject snip : snips) {
            if (snip.isSelected()) {
                selected.add(snip);
            }
        }
        return selected;
    }

    protected void getFavouriteSnipList() {
        AsyncHttpClient favSnipClient = new AsyncHttpClient();
        TokenManager tokenManager = new TokenManager(this);
        AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray favSnipList = new JSONArray(new JSONTokener(new String(responseBody)));
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
                    System.out.println("The response JSON doesn't work: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(String.valueOf(statusCode) + ": " + new String(responseBody) + " with headers: " + headers.toString());
            }
        };

        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("Authorization", "Bearer " + tokenManager.getAccessToken());
        headers[1] = new BasicHeader("Content-Type", "application/json");

        favSnipClient.get(
                SnipListActivity.this,
                Constants.API_URL + Constants.FAV_SNIPS,
                headers,
                new RequestParams(),
                asyncHttpResponseHandler
        );

    }


}
