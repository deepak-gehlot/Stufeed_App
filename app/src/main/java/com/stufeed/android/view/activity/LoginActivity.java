package com.stufeed.android.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.LoginResponse;
import com.stufeed.android.databinding.ActivityLoginBinding;
import com.stufeed.android.util.Extension;
import com.stufeed.android.util.PreferenceConnector;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.util.ValidationTemplate;
import com.stufeed.android.view.viewmodel.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        String verify = PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.VERIFY, "");
        boolean isLogin = PreferenceConnector.readBoolean(LoginActivity.this, PreferenceConnector.IS_LOGIN, false);
        if (verify.equals("0")) {
            startActivity(new Intent(LoginActivity.this, SelectCollegeActivity.class));
            finish();
        } else if (isLogin) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        LoginModel loginModel = new LoginModel();
        loginModel.setEmail("deepak@gmail.com");
        loginModel.setPassword("123456");
        loginModel.setDeviceToken("123456");
        binding.setModel(loginModel);
        Utility.printHashKey(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 454) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            if (resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            } else {
                Utility.showToast(LoginActivity.this, getString(R.string.wrong));
            }
        }
    }

    /**
     * Validate login button
     *
     * @param loginModel @LoginModel
     * @return true if valid else false
     */
    private boolean validate(LoginModel loginModel) {
        Extension extension = Extension.getInstance();
        if (TextUtils.isEmpty(loginModel.getEmail()) || TextUtils.isEmpty(loginModel.getPassword())) {
            Utility.showToast(LoginActivity.this, "Enter your email and password.");
            return false;
        } else if (!extension.executeStrategy(LoginActivity.this, loginModel.getEmail(), ValidationTemplate.EMAIL)) {
            Utility.showToast(LoginActivity.this, "Invalid email.");
            return false;
        } else if (!extension.executeStrategy(LoginActivity.this, "", ValidationTemplate.INTERNET)) {
            Utility.setNoInternetPopup(LoginActivity.this);
            return false;
        } else {
            return true;
        }
    }

    /**
     * On Login button click method
     *
     * @param loginModel @LoginModel
     */
    public void onLoginBtnClick(LoginModel loginModel) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        loginModel.setDeviceToken(refreshedToken);
        if (validate(loginModel)) {
            Api api = APIClient.getClient().create(Api.class);
            final Call<LoginResponse> responseCall = api.login(loginModel.getEmail(), loginModel.getPassword(),
                    loginModel
                            .getDeviceToken());
            ProgressDialog.getInstance().showProgressDialog(LoginActivity.this);
            responseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    responseCall.cancel();
                    ProgressDialog.getInstance().dismissDialog();
                    handleLoginResponse(response.body());
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    responseCall.cancel();
                    ProgressDialog.getInstance().dismissDialog();
                    Utility.showErrorMsg(LoginActivity.this);
                }
            });
        }
    }

    /**
     * On Register button click method
     */
    public void onRegisterBtnClick() {
        startRegisterActivity("", "");
    }

    public void onGoogleBtnClick() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 454);
    }

    public void onFacebookBtnClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Utility.showToast(LoginActivity.this, "success");
                        getFacebookData(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Utility.showToast(LoginActivity.this, "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Utility.showToast(LoginActivity.this, "exception");
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startRegisterActivity(account.getDisplayName(), account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * On Forgot Password button click method
     */
    public void onForgotPasswordBtnClick() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    /**
     * On Register button click method
     */
    public void onRegisterInstituteBtnClick() {
        startActivity(new Intent(LoginActivity.this, RegisterInstituteActivity.class));
    }


    /**
     * handle login response
     *
     * @param loginResponse @LoginResponse
     */
    private void handleLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResponseCode().equals(Api.SUCCESS)) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                PreferenceConnector.writeBoolean(LoginActivity.this, PreferenceConnector.IS_LOGIN, true);
                finish();
            } else if (loginResponse.getResponseMessage().equals("Account not verified.")) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.USER_DATA, new Gson()
                        .toJson(loginResponse.getUser()));
                PreferenceConnector.writeBoolean(LoginActivity.this, PreferenceConnector.IS_LOGIN, true);
                finish();
                /*startActivity(new Intent(LoginActivity.this, VerifyAccountActivity.class));
                finish();*/
            } else {
                Utility.showToast(LoginActivity.this, loginResponse.getResponseMessage());
            }
        } else {
            Utility.showErrorMsg(LoginActivity.this);
        }
    }


    private void getFacebookData(final LoginResult loginResult) {
        ProgressDialog.getInstance().showProgressDialog(LoginActivity.this);
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        ProgressDialog.getInstance().dismissDialog();
                        try {
                            Log.i("Response", response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");


                            Profile profile = Profile.getCurrentProfile();
                            String id = profile.getId();
                            String link = profile.getLinkUri().toString();
                            Log.i("Link", link);
                            if (Profile.getCurrentProfile() != null) {
                                Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                            }

                            Log.i("Login" + "Email", email);
                            Log.i("Login" + "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);

                            startRegisterActivity(firstName + " " + lastName, email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.showToast(LoginActivity.this, getString(R.string.wrong));
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void startRegisterActivity(String fullName, String email) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        intent.putExtra("full_name", fullName);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}