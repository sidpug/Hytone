package com.finance.hytone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doSmsWork();
        //donext();
    }
    public  void donext(){
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
//        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();
  //      Set<String> declinedPermissions = AccessToken.getCurrentAccessToken().getDeclinedPermissions();

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


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    ProgressDialog pd;
    private void doSmsWork() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Getting details1...");
        pd.show();
        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {
                SmsFetch sf = new SmsFetch();
                List<Sms> ss = sf.getAllSms(MainActivity.this);
                for (int i = 0; i < ss.size(); i++) {
                    Log.e("address" + i, ss.get(i).getAddress());
                    Log.e("msg" + i, ss.get(i).getMsg());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        doContactWork();
                    }
                });
            }
        });
        tt.start();

    }
private void doContactWork() {
    pd = new ProgressDialog(MainActivity.this);
    pd.setMessage("Getting details2...");
    pd.show();
    Thread tt = new Thread(new Runnable() {
        @Override
        public void run() {
            ContactFetch cf = new ContactFetch();
            List<ContactModel> cc = cf.getContacts(MainActivity.this);
            for (int i = 0; i < cc.size(); i++) {
                Log.e("contact" + i, "" + cc.get(i).mobileNumber + "," + cc.get(i).name);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    //doContactWork();
                }
            });
        }
    });
    tt.start();


}

    void signIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
    private void getChapterContents() {
//
        ErrorLogger.log("params", subscription_id);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<JsonObject> call = null;
        try {


            call = service.getChapterProducts(Helper.getToken(ChapterHome.this), subscription_id, chapter_id);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);

                    int rescode = response.code();
                    if (rescode != 200) {
                        HttpResponseUtils.showBasicResponseLogs(response);
                        HttpResponseUtils.processHeaderCode(ChapterHome.this, rescode);
                        return;
                    }
                    ErrorLogger.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogs(response);
                    try {
                        String resBody = response.body().toString();
                        //Toast.makeText(ChapterHome.this, resBody, Toast.LENGTH_SHORT).show();

                        JSONObject jo = new JSONObject(resBody);
                        int response_code = Integer.parseInt(jo.getString("response_code"));

                        // if (response_code == 200) {

                        JSONObject response_data = jo.getJSONObject("response_data");
                        JSONArray products = response_data.optJSONArray("products");
                        JSONObject chapter_details = response_data.optJSONObject("chapter");
                        JSONObject up_next = response_data.optJSONObject("up_next");
                        ErrorLogger.log("ss0", "" + products);
                        ErrorLogger.log("ss1", "" + chapter_details);
                        ErrorLogger.log("ss2", "" + up_next);

                        //imgTitle, llProgress, tvProgress;
                        //id  icon name  coverage_percent
                        String iconUrl = chapter_details.getString("icon");
                        if (iconUrl != null)
                            try {
                                Picasso.get().load(iconUrl).placeholder(R.drawable.ic_phyiscs_icon).into((ImageView) findViewById(R.id.imgTitle));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        float progress = Float.parseFloat(chapter_details.getString("coverage_percent"));
                        LinearLayout llProgress = findViewById(R.id.llProgress);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                progress
                        );
                        llProgress.setLayoutParams(param);
                        ((TextView) findViewById(R.id.tvProgress)).setText(progress + "%");

                        handleProducts(products);
                        //}
                        //  else
//                        {
//                            HttpResponseUtils.processBodyResponseCode(SubjectHomeActivity.this,response_code,jo);
//                        }

                    } catch (Exception e) {
                        Helper.showdialog(ChapterHome.this, false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    HttpResponseUtils.handleFailure(ChapterHome.this, call, t);
                }
            });

        } catch (Exception e) {
            Helper.showdialog(ChapterHome.this, false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }

}
