package com.bidme.activity.authentication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.bidme.R;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.databinding.ActivityUpdateprofileBinding;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.model.UpdateProfileDTO;
import com.bidme.model.UserDTO;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ImageCompression;
import com.bidme.utils.MainFragment;
import com.bidme.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {
    private ActivityUpdateprofileBinding binding;
    private String TAG = UpdateProfile.class.getCanonicalName();
    private Sign_Up sign_up;
    private Context mContext;
    private HashMap<String, File> paramsFile = new HashMap<>();
    private SharedPreferences firebase;
    private SharedPrefrence prefrence;
    private UpdateProfileDTO updateProfileDTO;
    private UserDTO userDTO;
    private SharedPrefrence sharedPrefrence;
    private String city;
    private String postCode;
    private String country;
    private double lattitude;
    private double longitude;
    private String user_pub_id;

    File file;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    int PLACE_PICKER_REQUEST = 6;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
   private int checkValid =1;
    AlertDialog.Builder builder1 ;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String pictureFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_updateprofile);
        mContext = UpdateProfile.this;
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ProjectUtils.showLog(TAG + "---tokensss", ((SharedPreferences) firebase).getString(Const.DEVICE_TOKEN, ""));
        prefrence = SharedPrefrence.getInstance(mContext);
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        user_pub_id=userDTO.getUser_pub_id();

        builder1=new AlertDialog.Builder(this);



        setUiAction();

        showData();
        Glide.with(UpdateProfile.this)
                .load(userDTO.getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.civProfile);
        binding.cCodePicker.setCcpClickable(false);


    }

    private void setUiAction() {
        binding.btnsubmitdata.setOnClickListener(this);
        binding.civProfile1.setOnClickListener(this);
        binding.cetAddress.setOnClickListener(this);

        builder = new BottomSheet.Builder(UpdateProfile.this).sheet(R.menu.menu_cards);
        builder.title("Please select image");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        /*if (ProjectUtils.hasPermissionInManifest(UpdateProfile.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(UpdateProfile.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        picUri = FileProvider.getUriForFile(UpdateProfile.this, "com.samyotech.getrid" + ".fileprovider", file);
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
                                /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                                sendTakePictureIntent();

                            }
                        }


                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(UpdateProfile.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(UpdateProfile.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    ProjectUtils.pauseProgressDialog();

                    lattitude = data.getDoubleExtra(LATITUDE, 0.0);
                    longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                    Log.d("LONGITUDE****", String.valueOf(longitude));

                    getAddress(lattitude, longitude);

                } catch (Exception e) {

                }
            }
        }

        /*if (requestCode == PICK_FROM_CAMERA) {
            if (data != null) {
                picUri = Uri.parse(prefrence.getValue(Const.IMAGE_URI_CAMERA));
                //picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UpdateProfile.this.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(mContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.civProfile);
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

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {

            try{
                File imgFile = new  File(pictureFilePath);
//            file = imgFile;
                file = new Compressor(UpdateProfile.this).compressToFile(imgFile);
                paramsFile.put(Const.IMAGE, file);


                // files.add(file);

                if(imgFile.exists()) {
                    binding.civProfile.setImageURI(Uri.fromFile(imgFile));
                }

                //setAda();

            }catch (IOException e){
                e.printStackTrace();
            }

          /*  final Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageCompression = new ImageCompression(mContext);
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {
                    Glide.with(mContext).load(photo)
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.civProfile);
                    try {

                        // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                        file =ProjectUtils.getFileFromBitmap(photo,mContext);
                        paramsFile.put(Const.IMAGE, file);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


*/


        }


        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(mContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.civProfile);
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
        }*/
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {


            try {
                if (data != null) {


                    Uri tempUri = data.getData();
                    Log.e("front tempUri", "" + tempUri);
                    if (tempUri != null) {

                        try {
                            bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), tempUri);
                            binding.civProfile.setImageBitmap(bm);

                            pathOfImage = ProjectUtils.getPath(UpdateProfile.this, tempUri);
                            if (pathOfImage != null) {
                                File f = new File(pathOfImage);
//                                picUri = Uri.fromFile(f);
//                                file = f;
                                file = new Compressor(UpdateProfile.this).compressToFile(f);
                                paramsFile.put(Const.IMAGE, file);

                                // files.add(file);
                            }


           /* try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == getPackageManager().PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);

            binding.cetAddress.setText(obj.getAddressLine(0));
            city = String.valueOf(obj.getLocality());
            country = String.valueOf(obj.getCountryName());
            postCode = String.valueOf(obj.getPostalCode());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(mContext, MainFragment.class);
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


    private void setData() {

        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Const.UPDATE_PROFILE, getParamsSignUp(), paramsFile, mContext).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) throws JSONException {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    try {

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Const.USER_DTO);
                        // showData();
                         finish();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    if(checkValid==1){
                       // Toast.makeText(mContext, getResources().getString(R.string.First_login_ur_account), Toast.LENGTH_SHORT).show();


                    }else{
                        ProjectUtils.showToast(mContext, msg);
                    }

                }
            }
        });

    }

    private void showData() {
        binding.ctvName.setText(ProjectUtils.capWordFirstLetter(userDTO.getName()));
        binding.cetAddress.setText(userDTO.getAddress());
        binding.cetPhoneno.setText(userDTO.getMobile_no());
        binding.etEmail.setText(userDTO.getEmail());
        try {
            binding.cCodePicker.setCountryForPhoneCode(Integer.parseInt(userDTO.getCountry_code()));

            binding.cCodePicker.setDetectCountryWithAreaCode(Boolean.parseBoolean(userDTO.getCountry_code()+userDTO.getCountry()));
            binding.ctTown.setText(userDTO.getTown());
            binding.ctZipcode.setText(userDTO.getzip());
        }catch (Exception e){

        }


        Glide.with(UpdateProfile.this)
                .load(userDTO.getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(binding.civProfile);
        country = userDTO.getCountry();

        if (userDTO.getGender().equals("male")) {
            binding.male.setChecked(true);
        } else if (userDTO.getGender().equals("female")) {
            binding.female.setChecked(true);
        }

        if (!userDTO.getLatitude().equals("")) {
            lattitude = Double.parseDouble(userDTO.getLatitude());
            longitude = Double.parseDouble(userDTO.getLongitude());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileback:
                onBackPressed();
                break;

            case R.id.profilenotification:
                Intent in = new Intent(this, Dashboard.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("index",0);
                startActivity(in);
                break;

            case R.id.btnsubmitdata:
                validation();
                break;

            case R.id.civProfile1:
                builder.show();
                break;

            case R.id.cetAddress:
                showlocation();
                break;

        }

    }

    private void validation() {
        if (ProjectUtils.getEditTextValue(binding.ctvName).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter Name ", Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.getEditTextValue(binding.etEmail).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter the mail", Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.getEditTextValue(binding.cetPhoneno).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter Mobile number ", Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.getEditTextValue(binding.ctZipcode).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter Zipcode ", Toast.LENGTH_SHORT).show();
        }else if (ProjectUtils.getEditTextValue(binding.ctTown).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter Town ", Toast.LENGTH_SHORT).show();
        } else if (ProjectUtils.getEditTextValue(binding.cetAddress).equalsIgnoreCase("")) {
            Toast.makeText(mContext, "please enter Address ", Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                setData();

            } else {
                ProjectUtils.InternetAlertDialog(mContext);
            }
        }
    }

    private void showlocation() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                .build(UpdateProfile.this);

        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);
    }

    public HashMap<String, String> getParamsSignUp() {
        HashMap<String, String> paramsSignUp = new HashMap<>();

        paramsSignUp.put(Const.MOBILE_NO, ProjectUtils.getEditTextValue(binding.cetPhoneno));
        Log.e(TAG, "phonenumber---------------------------: "+ ProjectUtils.getEditTextValue(binding.cetPhoneno) );
        paramsSignUp.put(Const.NAME, ProjectUtils.getEditTextValue(binding.ctvName));

        paramsSignUp.put(Const.EMAIL, ProjectUtils.getEditTextValue(binding.etEmail));
        paramsSignUp.put(Const.ADDRESS, ProjectUtils.getEditTextValue(binding.cetAddress));
        paramsSignUp.put(Const.Town, ProjectUtils.getEditTextValue(binding.ctTown));
        paramsSignUp.put(Const.COUNTRY, country);
        paramsSignUp.put(Const.LATITUDE, lattitude + "");
        paramsSignUp.put(Const.LONGITUDE, longitude + "");
        paramsSignUp.put(Const.ZIP, ProjectUtils.getEditTextValue(binding.ctZipcode));
        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();


        }else{
            checkValid=2;
            paramsSignUp.put(Const.USER_PUB_ID,user_pub_id);

        }






        if (binding.male.isChecked()) {
            paramsSignUp.put(Const.GENDER, "male");

        } else if (binding.female.isChecked()) {
            paramsSignUp.put(Const.GENDER, "female");

        } else {
            Toast.makeText(mContext, "please check field", Toast.LENGTH_SHORT).show();
        }


        paramsSignUp.put(Const.DEVICE_TOKEN, firebase.getString(Const.DEVICE_TOKEN, "1"));
        paramsSignUp.put(Const.DEVICE_TYPE, "ANDROID");

        paramsSignUp.put(Const.COUNTRY_CODE, binding.cCodePicker.getSelectedCountryCode() + "");

        ProjectUtils.showLog(TAG + "---Params --->", paramsSignUp.toString());

        return paramsSignUp;
    }

    private void alertGuestUser() {


        builder1.setMessage(getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(mContext,Sign_in.class);
                        startActivity(intent);
                        finishAffinity();

                        checkValid=1;

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        //Toast.makeText(getApplicationContext(), "you clicked NO",
                        // Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle(Const.APP_NAME);
        alert.setIcon(R.drawable.ic_logout1);
        alert.show();
    }




}
