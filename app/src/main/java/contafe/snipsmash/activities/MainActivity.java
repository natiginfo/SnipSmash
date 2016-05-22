package contafe.snipsmash.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import contafe.snipsmash.defaults.InternetDetector;
import contafe.snipsmash.defaults.TokenManager;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_STORAGE = 113;
    private Button loginButton;
    private EditText usernameET;
    private EditText passwordET;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }

        final InternetDetector internetDetector = new InternetDetector(this);

        loginProgress = (ProgressBar) findViewById(R.id.loginProgressBar);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameET = (EditText) findViewById(R.id.usernameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internetDetector.isConnectiongToInternet()) {
                    login();
                    mp.start();
                } else {
                    Toast.makeText(MainActivity.this, "You don't have an internet connection now. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                } else {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //reload my activity with permission granted or use the features what required the permission
                    boolean hasPermission2 = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission2) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_STORAGE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "The app was not allowed to READ your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void showProgress() {
        loginButton.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        loginButton.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
    }

    private void login() {
        final TokenManager tokenManager = new TokenManager(this);
        tokenManager.cleanTokens();

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
                    showProgress();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    dismissProgress();

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

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, jsonResponse.getString("error_description"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    dismissProgress();

                    Toast.makeText(MainActivity.this, "Something went wrong. Please retry again.", Toast.LENGTH_SHORT).show();
                    String response = new String(responseBody);
                    System.out.println("ERROR RESPONSE: " + response);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
