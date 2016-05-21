package contafe.snipsmash.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import contafe.snipsmash.R;
import contafe.snipsmash.defaults.Constants;
import contafe.snipsmash.defaults.TokenManager;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameET;
    private EditText passwordET;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginProgress = (ProgressBar) findViewById(R.id.loginProgressBar);
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
        final TokenManager tokenManager = new TokenManager(this);
        System.out.println("LOGGED IN: " + tokenManager.isLoggedIn());
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
                    loginProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    loginProgress.setVisibility(View.GONE);

                    String response = new String(responseBody);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (!jsonResponse.has("error")) {
                            tokenManager.setAccessToken(jsonResponse.getString("access_token"));
                            tokenManager.setRefreshToken(jsonResponse.getString("refresh_token"));
                            tokenManager.save();

                            System.out.println("RESPONSE: " + jsonResponse.toString());
                            System.out.println("LOGGED IN: " + tokenManager.isLoggedIn());

                            Intent intent = new Intent(MainActivity.this, SnipListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, jsonResponse.getString("error_description"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    loginProgress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong. Please retry again.", Toast.LENGTH_SHORT).show();
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
