package contafe.snipsmash.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import contafe.snipsmash.R;
import contafe.snipsmash.defaults.Constants;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        usernameET = (EditText) findViewById(R.id.usernameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);

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

            StringEntity entity = new StringEntity(loginDetails.toString());

            loginClient.post(MainActivity.this, Constants.API_URL + "/me/login", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    //posting started

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    System.out.println("RESPONSE: " + response);
//                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    String response = new String(responseBody);
                    System.out.println("ERROR RESPONSE: " + responseBody);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
