package com.example.saeedspc.logger_androidapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;

    private SharedPreferences preferenceSetting;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mServerUrlView;
    private View mProgressView;
    private View mLoginFormView;
    private ProgressDialog waitingProgress;
    AlertDialog.Builder wrongPasswordError;
    String token = null;
    String name = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        preferenceSetting = getPreferences(PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSetting.edit();

        Intent intent = this.getIntent();

        try {
            preferenceEditor.putBoolean("isLoggedIn", intent.getBooleanExtra("isLoggedIn", false));
        } catch (Exception ignored) {
        }
        preferenceEditor.commit();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        waitingProgress = new ProgressDialog(this);
        waitingProgress.setMessage("Waiting...");

        wrongPasswordError = new AlertDialog.Builder(this);
        wrongPasswordError.setMessage("Wrong password");
        wrongPasswordError.setTitle("Error ");
        wrongPasswordError.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        wrongPasswordError.setCancelable(true);
        // Set up the login form.
        mUsernameView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mServerUrlView = findViewById(R.id.server_url);

        Button mUsernameSignInButton = findViewById(R.id.email_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingProgress.show();
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                attemptLogin();
            }
        });

        mServerUrlView.setText(preferenceSetting.getString("ServerUrl", ""));
        mUsernameView.setText(preferenceSetting.getString("Username", ""));
        mPasswordView.setText(preferenceSetting.getString("Password", ""));

        if (preferenceSetting.getBoolean("isLoggedIn", false)) {
            waitingProgress.show();
            View v = getCurrentFocus();
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            attemptLogin();
        }

    }


    private void attemptLogin() {


        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mServerUrlView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String serverUrl = mServerUrlView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username) || !isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid Url address.
        if (TextUtils.isEmpty(serverUrl) || !isServerUrlValid(serverUrl)) {
            mServerUrlView.setError(getString(R.string.error_field_required));
            focusView = mServerUrlView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            mAuthTask = new UserLoginTask(serverUrl, username, password, getApplicationContext());
            mAuthTask.execute();
            mAuthTask = null;

        }
    }

    private boolean isServerUrlValid(String serverUrl) {
        //// TODO: 10/6/2017
        return true;
    }


    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the Progress UI and hides the login form.
     */

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    private class UserLoginTask extends AsyncTask<String, Void, Boolean> {

        private final String mUser;
        private final String mPassword;
        private final String mUrl;
        private final Context context;

        UserLoginTask(String serverUrl, String user, String password, Context c) {
            this.context = c;
            mUser = user;
            mPassword = password;
            mUrl = serverUrl;
        }

        @Override
        protected Boolean doInBackground(String... parameter) {
            try {

                URL url = new URL("http://" + mUrl.trim() + "/"+getResources().getString(R.string.login_api).trim()+"?"+"username=" + mUser + "&password=" + mPassword);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "fizz=buzz";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                int responseCode = 0;

                try {
                    responseCode = connection.getResponseCode();
                } catch (Exception ignored) {
                }


                if (responseCode != 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder responseOutput = new StringBuilder();
                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();
                    String outputText = responseOutput.toString();
                    token = outputText.split(":")[1].split("\"")[1];
                    name = outputText.split(":")[4].split(",")[0].split("\"")[1];
                    return true;
                } else
                    return false;


            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            waitingProgress.dismiss();
            if (aBoolean) {
                if (token != null) {
                    showProgress(true);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("name", name);
                    intent.putExtra("username", mUser);
                    intent.putExtra("serverUrl", mUrl);
                    preferenceEditor.putString("Username", mUser);
                    preferenceEditor.putString("Password", mPassword);
                    preferenceEditor.putString("ServerUrl", mUrl);
                    preferenceEditor.putBoolean("isLoggedIn", true);
                    preferenceEditor.commit();

                    startActivity(intent);
                }else {
                    preferenceEditor.putString("Username", mUser);
                    preferenceEditor.putString("ServerUrl", mUrl);
                    wrongPasswordError.create().show();
                    preferenceEditor.commit();
                }
            }else {
                preferenceEditor.putString("Username", mUser);
                preferenceEditor.putString("ServerUrl", mUrl);
                wrongPasswordError.create().show();
                preferenceEditor.commit();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

