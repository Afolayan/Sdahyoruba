package com.jcedar.sdahyoruba.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jcedar.sdahyoruba.R;
import com.jcedar.sdahyoruba.gcm.RegisterApp;
import com.jcedar.sdahyoruba.helper.AccountUtils;
import com.jcedar.sdahyoruba.helper.AppHelper;
import com.jcedar.sdahyoruba.helper.PrefUtils;
import com.jcedar.sdahyoruba.helper.UIUtils;

import java.io.InputStream;

/**
 * Created by oluwafemi.bamisaye on 3/26/2016.
 */
public class GoogleSignIn extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {



    private static final String TAG = GoogleSignIn.class.getSimpleName();
    private Button checkPhoneNumber;
    private SignInButton btnSignIn;
//    CircularProgressButton btEnter;
    private static int RC_SIGN_IN = 0;
    private boolean isEmailChecked = false;
    ProgressDialog dialog;
    GoogleCloudMessaging gcm;
    String regid, email;
    Context context = GoogleSignIn.this;


    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    String personPhotoUrl,personName;
    static Bitmap bResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AccountUtils.isFirstRun(this)) {
            startActivity(new Intent(this, NewDashBoardActivity.class));
            finish();
        }
        setContentView(R.layout.activity_google_signin);

        dialog = new ProgressDialog(GoogleSignIn.this);
        //checkPhoneNumber = (Button) findViewById(R.id.checkPhoneNumber);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        //checkPhoneNumber.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            Log.d(TAG, "Got cached sign-in");

            Intent dashbIntent = new Intent(this, NewDashBoardActivity.class);
            dashbIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(dashbIntent);

        }
    }

    private void signIn() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Internet Connectivity detected! Check your Internet Connectivity settings")
                    .setCancelable(false)
                    .setPositiveButton("Check Settings", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return;

        }else
            startActivityForResult(signInIntent, RC_SIGN_IN);

        btnSignIn.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TEST", " is result code 0?: " + resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }

     private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            if( acct != null) {
                getUsersGoogleDetails(acct);
            }
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Ensure you are connected to a Network to be able to Sign In",Toast.LENGTH_SHORT).show();

            RC_SIGN_IN++;
            if (dialog != null){
                dialog.dismiss();
            }
            btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void getUsersGoogleDetails(GoogleSignInAccount acct) {
        String emailPref = acct.getEmail();

        Log.e(TAG, " Handle signIn email of user " + emailPref);
        PrefUtils.setEmail(this, emailPref);
        //save Acct Name
        String namePref = acct.getDisplayName();
        PrefUtils.setPersonKey(this, namePref);


        Uri mPhoto = acct.getPhotoUrl();
        if (mPhoto != null) {

            personPhotoUrl = mPhoto.toString();
            Log.e(TAG, "Profile Image " + personPhotoUrl);
            personPhotoUrl = personPhotoUrl.substring(0, personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;

            new LoadProfileImage().execute(personPhotoUrl);
        } else {
            Bitmap def = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default_user);

            UIUtils.setProfilePic(this, def);
            enterDashBoard();

        }

        Toast.makeText(getApplicationContext(), "Please wait while data is loaded", Toast.LENGTH_SHORT).show();
        AppHelper.pullAndSaveAllHymnData(context);

        //register app for gcm
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (UIUtils.checkPlayServices(GoogleSignIn.this)) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    regid = AccountUtils.getRegistrationId(getApplicationContext());
                    Log.e(TAG, regid);

                    if (regid.isEmpty()) {
                        new RegisterApp(getApplicationContext(), gcm, UIUtils.getAppVersion(getApplicationContext())).execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Device already Registered", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "No valid Google Play Services APK found.");
                }
            }
        }).start();


    }


    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap = GoogleSignIn.bResult;

        public LoadProfileImage() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Getting Required Data....");
            dialog.show();

        }

        protected Bitmap doInBackground(String... urls) {
             String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            Log.e(TAG, "Bitmap returned" + mIcon11);
            return mIcon11;

        }

        protected void onPostExecute(Bitmap result) {
            this.bitmap=result;

            Log.e(TAG, "ONPOSTEXECUTE result    " + result);
            if (result != null){
                UIUtils.setProfilePic(GoogleSignIn.this, result);


            }else {
            }
            enterDashBoard();

            try {
                if ((dialog != null) && dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                dialog = null;

            }
        }
    }




    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public void enterDashBoard() {
        startActivity(new Intent(GoogleSignIn.this, NewDashBoardActivity.class));
        AccountUtils.setFirstRun(false, GoogleSignIn.this);
        GoogleSignIn.this.finish();
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.btn_sign_in:
            signIn();
            break;

        }
    }

    private void signOutUser() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
//                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
        btnSignIn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
