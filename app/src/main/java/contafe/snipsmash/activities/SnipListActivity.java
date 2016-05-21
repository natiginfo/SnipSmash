package contafe.snipsmash.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import contafe.snipsmash.R;

public class SnipListActivity extends AppCompatActivity {

    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snip_list);
    }

    protected JSONObject getFavouriteSnipList() {
        JSONObject favSnipList = new JSONObject();
        return favSnipList;
    }


}
