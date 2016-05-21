package contafe.snipsmash.defaults;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Natig on 5/21/16.
 */
public class TokenManager {

    private String accessToken;
    private String refreshToken;
    private Context context;

    public TokenManager(Context context) {
        this.context = context;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        SharedPreferences myPrefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String token1 = myPrefs.getString("accessToken", "empty");
        return token1;
    }

    public String getRefreshToken() {
        SharedPreferences myPrefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String token1 = myPrefs.getString("refreshToken", "empty");
        return token1;
    }

    public void updateRefreshToken(String newToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("refreshToken", newToken);
        editor.commit();
    }

    public void save() {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("accessToken", accessToken);
        editor.putString("refreshToken", refreshToken);
        editor.commit();
    }

    public boolean isLoggedIn() {
        SharedPreferences myPrefs = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);

        String token1 = myPrefs.getString("accessToken", "empty");
        String token2 = myPrefs.getString("refreshToken", "empty");

        if (!(token1.equals("empty") && token2.equals("empty"))) {
            return true;
        }
        return false;
    }
}
