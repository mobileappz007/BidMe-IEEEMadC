package com.bidme.activity.authentication;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.bidme.R;
import com.bidme.activity.policy.PrivacyPolicy;
import com.bidme.databinding.ActivityPrivacyPolicyBinding;
import com.bidme.databinding.ActivitySignUpBinding;
import com.bidme.databinding.CommentBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ImageCompression;
import com.bidme.utils.MainFragment;
import com.bidme.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class
Sign_Up extends AppCompatActivity implements View.OnClickListener {

    private Context sContext;
    private String TAG = Sign_Up.class.getCanonicalName();
    private ActivitySignUpBinding binding;
    private SharedPreferences firebase;
    private SharedPrefrence prefrence;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private Dialog dialogPolicy,dialogTerms,dialogFQ;

    //for image
    File file;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    private HashMap<String, File> paramsFile = new HashMap<>();
    private boolean isHide = false;
    private boolean weburl=true;
    int check=1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    String pictureFilePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign__up);
        sContext = Sign_Up.this;
       /* firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ProjectUtils.showLog(TAG + "---tokensss", ((SharedPreferences) firebase).getString(Const.DEVICE_TOKEN, ""));*/

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log.w(TAG, "getInstanceId :====>" + sharedPreferences.getString(Const.DEVICE_TOKEN, ""));

        prefrence = SharedPrefrence.getInstance(sContext);
        setUiAction();
    }

    private void setUiAction() {
        binding.btnSignUp.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.cardViewProfile.setOnClickListener(this);
        binding.ivPass.setOnClickListener(this);
        binding.ivConfirmPass.setOnClickListener(this);


        builder = new BottomSheet.Builder(Sign_Up.this).sheet(R.menu.menu_cards);
        builder.title("Please select image");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                       /* if (ProjectUtils.hasPermissionInManifest(Sign_Up.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(Sign_Up.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(Sign_Up.this, "com.samyotech.getrid"+ ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

                                    prefrence.setValue(Const.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
*/

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) != getPackageManager().PERMISSION_GRANTED)
                            {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                            }
                            else
                            {
                               /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/

                                sendTakePictureIntent();

                            }
                        }
                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(Sign_Up.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(Sign_Up.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Please select image."), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(sContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(sContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivProfilePic);
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                Log.e("image", imagePath);
                                paramsFile.put(Const.IMAGE, file);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(sContext.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(sContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(sContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivProfilePic);
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                paramsFile.put(Const.IMAGE, file);
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
       /* if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Const.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Const.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
*/

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            try{
                File imgFile = new  File(pictureFilePath);
//            file = imgFile;
                file = new Compressor(Sign_Up.this).compressToFile(imgFile);
                paramsFile.put(Const.IMAGE, file);


                // files.add(file);

                if(imgFile.exists()) {
                    binding.ivProfilePic.setImageURI(Uri.fromFile(imgFile));
                }

                //setAda();

            }catch (IOException e){
                e.printStackTrace();
            }


          /*  final Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageCompression = new ImageCompression(sContext);
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {
                    Glide.with(sContext).load(photo)
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.ivProfilePic);
                    try {

                        // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                        file =ProjectUtils.getFileFromBitmap(photo,sContext);
                        paramsFile.put(Const.IMAGE, file);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
*/


        }

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {

            try {
                if (data != null) {


                    Uri tempUri = data.getData();
                    Log.e("front tempUri", "" + tempUri);
                    if (tempUri != null) {

                        try {
                            bm = MediaStore.Images.Media.getBitmap(sContext.getContentResolver(), tempUri);
                            binding.ivProfilePic.setImageBitmap(bm);

                            pathOfImage = ProjectUtils.getPath(Sign_Up.this, tempUri);
                            if (pathOfImage != null) {
                                File f = new File(pathOfImage);
//                                picUri = Uri.fromFile(f);
//                                file = f;
                                file = new Compressor(Sign_Up.this).compressToFile(f);
                                paramsFile.put(Const.IMAGE, file);

                                // files.add(file);
                            }

                            //setAda();

                            /*imageCompression = new ImageCompression(mContext);
                            imageCompression.execute(pathOfImage);
                            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                                @Override
                                public void processFinish(String imagePath) {
                                    Glide.with(mContext).load("file://" + imagePath)
                                            .thumbnail(0.5f)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(binding.ivAddImages);
                                    Log.e("image", imagePath);
                                    try {
                                        file = new File(imagePath);
                                        files.add(file);
                                        setAda();
                                        //  paramsFile.put(Const.IMAGE, file);
                                        Log.e("image", imagePath);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
       /*     try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }*/
        }
    }


    private void sendTakePictureIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(cameraIntent, CAMERA_REQUEST);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        Const.APP_PACKAGE+".fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ANDREE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(sContext, MainFragment.class);
        intent.putExtra(Const.FLAG,"1");
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Const.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Const.APP_NAME + timeStamp + ".png");

            imageName = Const.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignUp:
                clickForSubmit();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.cardViewProfile:
                builder.show();
                break;
            case R.id.ivPass:
                if (isHide) {
                    binding.ivPass.setImageResource(R.drawable.eye);
                    binding.etPassword.setTransformationMethod(null);
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivPass.setImageResource(R.drawable.passwordhide);
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etPassword.setSelection(binding.etPassword.getText().length());
                    isHide = true;
                }
                break;

                case R.id.ivConfirmPass:
                if (isHide) {
                    binding.ivConfirmPass.setImageResource(R.drawable.eye);
                    binding.etConfirmPassword.setTransformationMethod(null);
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivConfirmPass.setImageResource(R.drawable.passwordhide);
                    binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().length());
                    isHide = true;
                }
                break;
        }

    }

    private void signup() {

        ProjectUtils.showProgressDialog(sContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Const.SIGN_UP, getParamsSignUp(), paramsFile, sContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    Toast.makeText(sContext, "You are reigstered successfully please check your email for verification.", Toast.LENGTH_SHORT).show();

                   // ProjectUtils.showToast(sContext, msg);
                    check=1;

                    finish();
                } else {

                    ProjectUtils.showToast(sContext, msg);
                }
            }
        });


    }

    public HashMap<String, String> getParamsSignUp() {
        HashMap<String, String> paramsSignUp = new HashMap<>();
        paramsSignUp.put(Const.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        paramsSignUp.put(Const.MOBILE_NO, ProjectUtils.getEditTextValue(binding.etMobile));
        paramsSignUp.put(Const.NAME, ProjectUtils.getEditTextValue(binding.etName));
        paramsSignUp.put(Const.FIRST_NAME, ProjectUtils.getEditTextValue(binding.etFirstName));
        paramsSignUp.put(Const.LAST_NAME, ProjectUtils.getEditTextValue(binding.etLastName));
        paramsSignUp.put(Const.TOWN, ProjectUtils.getEditTextValue(binding.Town));
        paramsSignUp.put(Const.ZIP, ProjectUtils.getEditTextValue(binding.zipCode));
        paramsSignUp.put(Const.PASSWORD, ProjectUtils.getEditTextValue(binding.etPassword));
        paramsSignUp.put(Const.DEVICE_TOKEN, sharedPreferences.getString(Const.DEVICE_TOKEN, ""));
        paramsSignUp.put(Const.DEVICE_TYPE, "ANDROID");
        paramsSignUp.put(Const.COUNTRY_CODE, binding.cCodePicker.getSelectedCountryCode() + "");

        ProjectUtils.showLog(TAG + "---Params --->", paramsSignUp.toString());

        return paramsSignUp;
    }


    private void clickForSubmit() {
        if (file == null) {
            showSickbar(getResources().getString(R.string.please_upload));
        } else if (!validation(binding.etName, getResources().getString(R.string.val_name))) {
            return;
        }else if (!validation(binding.etFirstName, getResources().getString(R.string.val_first_name))) {
            return;
        }else if (!validation(binding.etLastName, getResources().getString(R.string.val_last_name))) {
            return;
        }else if (!validation(binding.Town, getResources().getString(R.string.val_town))) {
            return;
        } else if (!validation(binding.zipCode, getResources().getString(R.string.val_zip_code))) {
            return;
        } else if (!ProjectUtils.isEmailValid(binding.etEmail.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.valid_email_id));
        } else if (!ProjectUtils.isPhoneNumberValid(binding.etMobile.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_mobile));
        } else if (!ProjectUtils.isPasswordValid(binding.etPassword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_password));
        }else if (!ProjectUtils.isPasswordValid(binding.etConfirmPassword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.confirm_password));
        } else if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_confirm_password));
        } else {
            if (NetworkManager.isConnectToInternet(sContext)) {
                CheckUser();
            } else {
                ProjectUtils.InternetAlertDialog(sContext);
            }
        }
    }

    private void showSickbar(String msg) {

        Snackbar snackbar = Snackbar.make(binding.rlSnackbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    private boolean validation(EditText etName, String msg) {

        if (!ProjectUtils.isEditTextFilled(etName)) {
            Snackbar snackbar = Snackbar.make(binding.rlSnackbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }

    }


    public void dialogPolicy() {
        weburl=true;


        dialogPolicy = new Dialog(sContext/*, android.R.style.Theme_Dialog*/);
        dialogPolicy.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPolicy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final ActivityPrivacyPolicyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialogPolicy.getContext()),R.layout.activity_privacy_policy,null,false);
        dialogPolicy.setContentView(binding .getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
        dialogPolicy.show();
        dialogPolicy.setCancelable(true);

        binding. mWebView.setWebViewClient(new PrivacyPolicy.MyBrowser());
        binding. mWebView.getSettings().setLoadsImagesAutomatically(true);
        binding.mWebView.getSettings().setJavaScriptEnabled(true);
        binding.  mWebView.loadUrl(Const.PRIVACY_URL);
       binding.title.setText(getResources().getString(R.string.PrivacyPolicy));






        binding.checkPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    if(check==2){


                    binding.btnNext.setText("Sign Up");
                        binding.btnNext.setVisibility(View.VISIBLE);
                        binding.btnNext2.setVisibility(View.GONE);


                    }else{

                        binding.btnNext.setVisibility(View.VISIBLE);
                        binding.btnNext2.setVisibility(View.GONE);
                        binding.btnNext.setText("Next");


                    }


                }else{





                        binding.btnNext.setVisibility(View.GONE);
                        binding.checkPolicy.setChecked(false);
                        binding.btnNext2.setVisibility(View.VISIBLE);
                        binding.btnNext.setText("Next");





                }


            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(weburl){
                    binding.  mWebView.loadUrl(Const.TERMS_COND_URL);
                    binding.checkPolicy.setChecked(false);

                    binding.btnNext.setVisibility(View.GONE);
                    binding.btnNext2.setVisibility(View.VISIBLE);
                    binding.title.setText(getResources().getString(R.string.cookiePolicy));
                    weburl=false;
                    check=2;



                }else{
                    signup();

                }



            }
        });






        binding.rlclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weburl=true;
                check=1;
                dialogPolicy.dismiss();
            }
        });


    }


    private void CheckUser() {
        new HttpsRequest(Const.CHECKUSER, getParamsSignUp(), sContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    dialogPolicy();


                } else {


                        ProjectUtils.showToast(sContext, msg);

                }
            }
        });

    }

    public void dialogTermsCondition() {
        dialogTerms = new Dialog(sContext/*, android.R.style.Theme_Dialog*/);
        dialogTerms.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTerms.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final ActivityPrivacyPolicyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialogTerms.getContext()),R.layout.activity_privacy_policy,null,false);
        dialogTerms.setContentView(binding .getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
        dialogTerms.show();
        dialogTerms.setCancelable(true);

        binding. mWebView.setWebViewClient(new PrivacyPolicy.MyBrowser());
        binding. mWebView.getSettings().setLoadsImagesAutomatically(true);
        binding.mWebView.getSettings().setJavaScriptEnabled(true);
        binding.  mWebView.loadUrl(Const.TERMS_COND_URL);

        binding.title.setText(getResources().getString(R.string.cookiePolicy));






        binding.checkPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.btnNext2.setVisibility(View.GONE);

                }else{
                    binding.btnNext.setVisibility(View.GONE);
                    binding.checkPolicy.setChecked(false);
                    binding.btnNext2.setVisibility(View.VISIBLE);



                }


            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });



        binding.rlclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTerms.dismiss();
            }
        });


    }

 public void dialogFQ() {
     dialogFQ = new Dialog(sContext/*, android.R.style.Theme_Dialog*/);
     dialogFQ.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
     dialogFQ.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final ActivityPrivacyPolicyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(dialogTerms.getContext()),R.layout.activity_privacy_policy,null,false);
     dialogFQ.setContentView(binding .getRoot());
        //   final CommentBinding binding=DataBindingUtil.setContentView(this,R.layout.comment);
     dialogFQ.show();
     dialogFQ.setCancelable(true);

        binding. mWebView.setWebViewClient(new PrivacyPolicy.MyBrowser());
        binding. mWebView.getSettings().setLoadsImagesAutomatically(true);
        binding.mWebView.getSettings().setJavaScriptEnabled(true);
        binding.  mWebView.loadUrl(Const.FQ_URL);

        binding.title.setText(getResources().getString(R.string.FQ));






        binding.checkPolicy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.btnNext.setVisibility(View.VISIBLE);
                    binding.btnNext2.setVisibility(View.GONE);

                }else{
                    binding.btnNext.setVisibility(View.GONE);
                    binding.checkPolicy.setChecked(false);
                    binding.btnNext2.setVisibility(View.VISIBLE);



                }


            }
        });


        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        binding.rlclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFQ.dismiss();
            }
        });


    }







}
