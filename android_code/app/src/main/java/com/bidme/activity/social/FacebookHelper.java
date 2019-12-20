package com.bidme.activity.social;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * FacebookHelper.java
 */
public class FacebookHelper {
    private Collection<String> permissions = Arrays.asList("public_profile ", "email", "user_birthday", "user_location");
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private ShareDialog shareDialog;
    private Activity activity;
    private Fragment fragment;
    private OnFbSignInListener fbSignInListener;

    /**
     * Interface to listen the Facebook login
     */
    public interface OnFbSignInListener {
        void OnFbSignInComplete(GraphResponse graphResponse, String error);
    }

    public FacebookHelper(Activity activity, OnFbSignInListener fbSignInListener) {
        this.activity = activity;
        this.fbSignInListener = fbSignInListener;
    }

    public FacebookHelper(Fragment fragment, OnFbSignInListener fbSignInListener) {
        this.fragment = fragment;
        this.fbSignInListener = fbSignInListener;
    }

    public FacebookHelper(Activity activity) {
        shareDialog = new ShareDialog(activity);
    }

    public FacebookHelper(Fragment fragment) {
        shareDialog = new ShareDialog(fragment);
    }


    public void connect() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        if (activity != null)
            loginManager.logInWithReadPermissions(activity, permissions);
        else
            loginManager.logInWithReadPermissions(fragment, permissions);
        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult != null) {
                            callGraphAPI(loginResult.getAccessToken());
                        }
                    }

                    @Override
                    public void onCancel() {
                        fbSignInListener.OnFbSignInComplete(null, "User cancelled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        fbSignInListener.OnFbSignInComplete(null, exception.getMessage());
                    }
                });

    }

    private void callGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        fbSignInListener.OnFbSignInComplete(response,null);
                    }
                });
        Bundle parameters = new Bundle();
        //Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,birthday,email,first_name,gender,last_name,link,location,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * To share the details in facebook wall.
     *
     * @param title       of the content
     * @param description of the content
     * @param url         link to share.
     */
    public void shareOnFBWall(String title, String description, String url) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(url))
                    .build();
            shareDialog.show(linkContent);
        }
    }


}