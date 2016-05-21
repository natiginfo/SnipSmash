package contafe.snipsmash.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import contafe.snipsmash.R;
import contafe.snipsmash.defaults.Constants;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        /*
          "username": "snipsmash",
  "password": "weloveunicorns",
  "grant_type": "password",
  "client_id": "rM7gvdLe6K5ZH6QC5pWWVUcHWZQYj4B3tiZyl488",
  "client_secret": "7hNoi8DxEQ03keLq6Fg4Aqya5noTEg3VlfM6OsPzgt1imchkLTKafjKV6RXfALfEfYd77fpIxHC9m2WSqasQFqld6iwZUK3pyZF3ywVA6PxCYHcf16ugfi3BRxHkhLW3"
}
         */
        AsyncHttpClient loginClient = new AsyncHttpClient();
        JSONObject loginDetails = new JSONObject();

        try {
            loginDetails.put("username", usernameET.getText().toString());
            loginDetails.put("password", passwordET.getText().toString());
            loginDetails.put("grant_type", "password");
            loginDetails.put("client_id", Constants.CLIENT_ID);
            loginDetails.put("client_secret", Constants.CLIENT_SECRET);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
