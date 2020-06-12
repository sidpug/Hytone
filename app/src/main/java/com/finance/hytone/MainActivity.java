package com.finance.hytone;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "Google_related";
    //private Context ctx;
    String auth;
    SignInButton signInButton;
    LoginButton loginButton;
    ShareDialog shareDialog;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmsFetch sf = new SmsFetch();
        List<Sms> ss = sf.getAllSms(MainActivity.this);
        for (int i = 0; i < ss.size(); i++) {
            Log.e("address" + i, ss.get(i).getAddress());
            Log.e("msg" + i, ss.get(i).getMsg());
        }
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.sign_in_fb);
        //loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        LoginManager.getInstance().logInWithPublishPermissions(
                MainActivity.this,
                Collections.singletonList("publish_actions"));
        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();
        Set<String> declinedPermissions = AccessToken.getCurrentAccessToken().getDeclinedPermissions();

        shareDialog = new ShareDialog(this);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .setShareHashtag(new ShareHashtag.Builder()
                            .setHashtag("#ConnectTheWorld")
                            .build())
                    .build();
            shareDialog.show(linkContent);
        }

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        /*ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);*/

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //startActivity(new Intent(MainActivity.this,FacebookPermission.class));

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        //Application context, Activity context Context docs read,

        ContactFetch cf = new ContactFetch();
        List<ContactModel> cc = cf.getContacts(MainActivity.this);
        for (int i = 0; i < cc.size(); i++) {
            Log.e("contact" + i, "" + cc.get(i).mobileNumber + "," + cc.get(i).name);
        }

    }

    void signIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        Log.e(TAG+"auth",auth);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null)
            Toast.makeText(MainActivity.this, "No Details Found", Toast.LENGTH_SHORT).show();
        else {
            Log.e("acctName", account.getDisplayName() + "");
            Log.e("acctEmail", account.getEmail() + "");
        }
    }


    public void installedApps() {

        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("App № " + i, appName);
            }
        }
    }
}
