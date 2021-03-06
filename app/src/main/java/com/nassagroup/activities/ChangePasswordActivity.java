package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nassagroup.R;
import com.nassagroup.tools.Constants;
import com.nassagroup.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A screen that allows the user to change his / her passwrord
 */
public class ChangePasswordActivity extends AppCompatActivity {

    // UI references.
    EditText editText_password1, editText_password2;
    UserInfo userInfo;

    /**
     * Method that sets up the form
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);

        // Mis en place du formulaire de changement de mot de passe.
        userInfo = new UserInfo(this);
        editText_password1 = (EditText) findViewById(R.id.change_password_password1);
        editText_password2 = (EditText) findViewById(R.id.change_password_password2);

        editText_password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attempChangePassword();
                    return true;
                }
                return false;
            }
        });

        Button mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mChangePasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attempChangePassword();
            }
        });
    }

    /**
     * Method that allows to change the user's password.
     * If the field is empty, string too short, etc
     *  an error message will display
     */
    public void attempChangePassword() {

        boolean pass = false;
        // Reinitialiser les Errors.
        editText_password1.setError(null);
        editText_password2.setError(null);

        // Enregistre les les valeurs des saisis dans un String.
        String password1 = editText_password1.getText().toString();
        String password2 = editText_password2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Verifie que le mot de passe est valide. Si l'utilisateur en a saisi.
        if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            editText_password1.setError(getString(R.string.error_invalid_password));
            focusView = editText_password1;
            cancel = true;
            pass = true;
        }

        // Verifie que le mot de passe est valide. Si l'utilisateur en a saisi.
        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
            editText_password2.setError(getString(R.string.error_invalid_password));
            focusView = editText_password2;
            cancel = true;
            pass = true;
        }

        if (!password1.equals(password2)){
            editText_password2.setError(getString(R.string.error_password_not_match));
            focusView = editText_password1;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else
            changePasswordUser(userInfo.getUserUsername(), password1);
    }

    /**
     * Method that checks if an e-mail is correct
     *
     * @param email
     * @return boolean
     */
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Method that checks if a password is strong or not.
     * If the password is not strong enough, an error message will display
     *
     * @param password
     * @return boolean
     */
    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Method that saves the user's new password in online database.
     *
     * @param username
     * @param password
     */
    public void changePasswordUser(final String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Changement de mot de passe en cours  ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHANGE_PASSWORD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso  = new JSONObject(response);

                            if (!jso.getBoolean("error")){
                                userInfo.setLoggedin(true);
                                userInfo.setUserInfo(String.valueOf(jso.getJSONObject("user")));
                                startActivity(new Intent(ChangePasswordActivity.this, SearchClientActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(ChangePasswordActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(ChangePasswordActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME, username);
                params.put(Constants.KEY_PASSWORD, password);
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private final List mBlockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP));

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mBlockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }
}

