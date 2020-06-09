package com.finance.hytone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Collections;

public class FacebookPermission extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookpermission);

        ShareDialog shareDialog;

        callbackManager = CallbackManager.Factory.create();

        //For facebook login
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
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
                FacebookPermission.this,
                Collections.singletonList("publish_actions"));
        AccessToken.getCurrentAccessToken().getPermissions();
        AccessToken.getCurrentAccessToken().getDeclinedPermissions();


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

        //ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        //shareButton.setShareContent(content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /** in order to express log in for other device same app
     LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
    @Override public void onCompleted(AccessToken accessToken) {
    // User was previously logged in, can log them in directly here.
    // If this callback is called, a popup notification appears that says
    // "Logged in as <User Name>"
    }
    @Override public void onFailure() {
    // No access token could be retrieved for the user
    }
    @Override public void onError(Exception exception) {
    // An error occurred
    }
    });
     */
}
