package com.bidme.activity.aution;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bidme.R;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.activity.dashbord.Dashboard;
import com.bidme.adapter.ImageAdapterNew;
import com.bidme.databinding.ActivityDetailsAutionBinding;
import com.bidme.event.SingleUploadBroadcastReceiver;
import com.bidme.https.HttpsRequest;
import com.bidme.interfaces.Const;
import com.bidme.interfaces.Helper;
import com.bidme.interfaces.OnSpinerItemClick;
import com.bidme.model.AddAuctionDTO;
import com.bidme.model.CommonDTO;
import com.bidme.model.ImageDTO;
import com.bidme.model.MyAutionDTO;
import com.bidme.model.SubBrandsDTO;
import com.bidme.model.SubModelDTO;
import com.bidme.model.UserDTO;
import com.bidme.myauctionfregment.MyAuctions;
import com.bidme.network.NetworkManager;
import com.bidme.preferences.SharedPrefrence;
import com.bidme.utils.ImageCompression;
import com.bidme.utils.MainFragment;
import com.bidme.utils.ProjectUtils;
import com.bidme.utils.SpinnerDialog;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;

public class
AddAuctionNew extends AppCompatActivity implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate {
    private Context mContext;
    private String TAG = AddAuctionNew.class.getCanonicalName();
    private ArrayList<AddAuctionDTO> addAuctionDTOArrayList;
    private ActivityDetailsAutionBinding binding;
    private ArrayList<SubBrandsDTO> subBrandsDTOArrayList;
    private ArrayList<SubModelDTO> subModelDTOArrayList;
    ArrayList<CommonDTO> commonList = new ArrayList<>();
    ArrayList<CommonDTO> commonListModel = new ArrayList<>();
    AddAuctionDTO addAuctionDTO;
    SpinnerDialog spinnerDialog;
    SpinnerDialog spinnerDialogModel;
    private String modelName;
    MyAuctions myAuctions = new MyAuctions();
    private double lattitude;
    private double longitude;
    SharedPrefrence sharedPrefrence;
    private ArrayList<ImageDTO> imageDTOArrayList;
    private ImageAdapterNew addImageAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int PLACE_PICKER_REQUEST = 101;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    DatePickerDialog datePickerDialogADp;
    DatePickerDialog datePickerDialogEnd;


    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarAdop = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    boolean statusBrand = false;
    boolean imageCheck = false;
    AlertDialog.Builder builder1 ;
    private int checkValid =1;




    private Map<String, ArrayList<File>> paramsFile = new HashMap<>();
    ArrayList<Image> images = new ArrayList<>();
    //for image: -
    File file;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    int PICK_IMAGE_MULTIPLE = 5;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    SharedPrefrence prefrence;
    String brandid = "", brandName = "";
    String catid = "";
    String modelid = "";
    String subcatid = "";
    String user_pub_id = "";
    UserDTO userDTO;
    String image;
    int flag = 0;
    MyAutionDTO myAutionDTO;
    public ArrayList<File> files = new ArrayList<>();
    int startPrice=10;
    int price;


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String pictureFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.changeStatusBarColorNew(AddAuctionNew.this, R.color.topColor);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_aution);
        mContext = AddAuctionNew.this;
        sharedPrefrence = SharedPrefrence.getInstance(AddAuctionNew.this);
        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        user_pub_id = userDTO.getUser_pub_id();
        builder1=new AlertDialog.Builder(this);



        setData();
        // uploadReceiver.register(mContext);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvImage.setLayoutManager(layoutManager);
        prefrence = SharedPrefrence.getInstance(mContext);

        if (getIntent().hasExtra(Const.FLAG)) {
            flag = getIntent().getIntExtra(Const.FLAG, 0);
            if (flag == 1) {
                myAutionDTO = (MyAutionDTO) getIntent().getSerializableExtra(Const.MY_AUCTIONDTO);
                subcatid = myAutionDTO.getSub_cat_id();
                catid = myAutionDTO.getCat_id();
                binding.llImages.setVisibility(View.GONE);
                setData();
            }

        } else {
            subcatid = getIntent().getStringExtra(Const.GET_SUB_CAT_ID);
            catid = getIntent().getStringExtra(Const.GET_CAT_ID);
        }


        binding.btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (files.size() > 0) {
                    paramsFile.put(Const.IMAGE, files);



                }
                if (flag == 1) {
                    CheckEditFiled();
                } else {

                    checkAllFields();


                }


            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        setSubbrand();
        setUiAction();


    }

    private void setData() {
        try {

            binding.etBrandName.setText(myAutionDTO.getBrand_name() + "/" + myAutionDTO.getModel_name());
            binding.etTitle.setText(ProjectUtils.capWordFirstLetter(myAutionDTO.getTitle()));
            binding.etshortDescription.setText(myAutionDTO.getSort_description());

            //binding.etDescription.setText(myAutionDTO.getDescription());
            binding.etEndDate.setText(myAutionDTO.getE_date());
            binding.etSDate.setText(myAutionDTO.getS_date());
            binding.etAdress.setText(myAutionDTO.getAddress());
            binding.etPrice.setText(myAutionDTO.getPrice());
            binding.etOwner.setText(myAutionDTO.getNo_of_owner());

            if (myAutionDTO.getInsured().equals("0")) {
                binding.rbNo.setChecked(true);
            } else {
                binding.rbYes.setChecked(true);
            }

            Glide.with(mContext)
                    .load(myAutionDTO.getImage_cust().get(0).getImage())
                    .placeholder(R.drawable.dummy)
                    .into(binding.ivAddImages);


        } catch (Exception e) {

        }

    }


    public static boolean CheckDates(String d1, String d2) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(d1).equals(dfDate.parse(d2))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }


    private void CheckEditFiled() {
       /* if(ProjectUtils.getEditTextValue(binding.etBrandName).equalsIgnoreCase("")){
            showSickbar("Plese enter the brand Name and Model");
        }else*/
       price= Integer.parseInt(binding.etPrice.getText().toString().trim());
        if (ProjectUtils.getEditTextValue(binding.etTitle).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_the_title));
        } else if (ProjectUtils.getEditTextValue(binding.etshortDescription).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_shortDescription));
        }/* else if (ProjectUtils.getEditTextValue(binding.etDescription).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Description));
        }*/ else if (ProjectUtils.getEditTextValue(binding.etAdress).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Address));
        } else if (ProjectUtils.getEditTextValue(binding.etPrice).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Price));
        } else if (ProjectUtils.getEditTextValue(binding.etPrice).startsWith("0")) {
            showSickbar(getResources().getString(R.string.Please_Enter_valid_amountt));
        } else if (price<startPrice) {
            showSickbar(getResources().getString(R.string.Please_start_with_10dkr));
        } else if (ProjectUtils.getEditTextValue(binding.etSDate).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Select_Start_Date));
        } else if (ProjectUtils.getEditTextValue(binding.etEndDate).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Select_End_Date));
        } else if (ProjectUtils.getEditTextValue(binding.etSDate).equals(ProjectUtils.getEditTextValue(binding.etEndDate))) {
            showSickbar(getResources().getString(R.string.Please_selectEndStartDate));
        } else if (ProjectUtils.getEditTextValue(binding.etTitle).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_the_title));
        } else {

            if (NetworkManager.isConnectToInternet(mContext)) {
                editAuntion();
            } else {
                ProjectUtils.InternetAlertDialog(mContext);
            }
        }


    }

    private void checkAllFields() {
       /* if(ProjectUtils.getEditTextValue(binding.etBrandName).equalsIgnoreCase("")){
            showSickbar("Plese enter the brand Name and Model");
        }else*/
        price= Integer.parseInt(binding.etPrice.getText().toString().trim());

        if (files.size()==0) {
            showSickbar(getResources().getString(R.string.aution_image));
        } else if (ProjectUtils.getEditTextValue(binding.etTitle).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_the_title));
        } else if (ProjectUtils.getEditTextValue(binding.etshortDescription).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_shortDescription));
        }/* else if (ProjectUtils.getEditTextValue(binding.etDescription).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Description));
        }*/ else if (ProjectUtils.getEditTextValue(binding.etAdress).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Address));
        } else if (ProjectUtils.getEditTextValue(binding.etPrice).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_Price));
        } else if (ProjectUtils.getEditTextValue(binding.etPrice).startsWith("0")) {
            showSickbar(getResources().getString(R.string.Please_Enter_valid_amountt));
        } else if (price<startPrice) {
            showSickbar(getResources().getString(R.string.Please_start_with_10dkr));
        }   else if (ProjectUtils.getEditTextValue(binding.etPrice).startsWith("0")) {
            showSickbar(getResources().getString(R.string.Please_Enter_valid_amountt));
        } else if (ProjectUtils.getEditTextValue(binding.etSDate).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Select_Start_Date));
        } else if (ProjectUtils.getEditTextValue(binding.etEndDate).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Select_End_Date));
        } else if (ProjectUtils.getEditTextValue(binding.etSDate).equals(ProjectUtils.getEditTextValue(binding.etEndDate))) {
            showSickbar(getResources().getString(R.string.Please_selectEndStartDate));
        } else if (ProjectUtils.getEditTextValue(binding.etTitle).equalsIgnoreCase("")) {
            showSickbar(getResources().getString(R.string.Please_Enter_the_title));
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                Log.e("", "size------------ "+images.size());

                getAddAunctionDetail();
            } else {
                ProjectUtils.InternetAlertDialog(mContext);
            }
        }
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.rlSnackbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();


    }


    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        binding.etSDate.setText(sdf.format(myCalendar.getTime()));


    }

    private void setUiAction() {

        binding.ivAddImages.setOnClickListener(this);
        binding.etBrandName.setOnClickListener(this);
        binding.addMoreimages.setOnClickListener(this);
        binding.etAdress.setOnClickListener(this);
        binding.etSDate.setOnClickListener(this);
        binding.etEndDate.setOnClickListener(this);

        builder = new BottomSheet.Builder(AddAuctionNew.this).sheet(R.menu.menu_cards);
        builder.title("Please select image");
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                       /* if (ProjectUtils.hasPermissionInManifest(AddAuction.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(AddAuction.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        Log.e(TAG, "onClick:------------------------"+getApplicationContext().getPackageName() );
                                        picUri = FileProvider.getUriForFile(AddAuction.this, "com.samyotech.getrid" + ".fileprovider", file);
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
                        }*/


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
                        if (ProjectUtils.hasPermissionInManifest(AddAuctionNew.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(AddAuctionNew.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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

    private void getAddAunctionDetail() {

        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));

        new HttpsRequest(Const.ADD_AUCTION, getDetail(), paramsFile, mContext, mContext).multiImagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {

                        ProjectUtils.showToast(mContext, msg);
                        Intent in = new Intent(mContext, Dashboard.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.putExtra("index", 0);
                        startActivity(in);
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

    @Override
    public void onProgress(int progress) {
        //your implementation
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        //your implementation
    }

    @Override
    public void onError(Exception exception) {
        //your implementation
        ProjectUtils.pauseProgressDialog();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //your implementation
        ProjectUtils.pauseProgressDialog();
        try {
            String str = new String(serverResponseBody, "UTF-8");
            Log.e("-->res", str);

            JSONObject jsonObject = new JSONObject(str);

            ProjectUtils.showToast(mContext, jsonObject.getString("message"));

            if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                finish();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onCancelled() {
        //your implementation
        ProjectUtils.pauseProgressDialog();
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
        /*if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(mContext);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(
                            new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Glide.with(mContext).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivAddImages);
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                files.add(file);
                                setAda();
                                Log.e("image", imagePath);
                                // paramsFile.put(Const.IMAGE, file);

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
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
       /* if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {


         *//* try {


            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Const.IMAGE_URI_CAMERA));



                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Const.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
          }catch (Exception e){

          }

*//*


        }*/


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            try{
                File imgFile = new  File(pictureFilePath);
//            file = imgFile;
                file = new Compressor(AddAuctionNew.this).compressToFile(imgFile);

                files.add(file);

                if(imgFile.exists()) {
                    binding.ivAddImages.setImageURI(Uri.fromFile(imgFile));
                }

                setAda();

            }catch (IOException e){
                e.printStackTrace();
            }

            /*Glide.with(mContext).load(photo)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.ivAddImages);*/

           /* imageCompression = new ImageCompression(mContext);
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {

                    Glide.with(mContext).load(photo)
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.ivAddImages);
                    try {

                        file =ProjectUtils.getFileFromBitmap(photo,mContext);
                        files.add(file);
                        setAda();
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
//                    startCropping(tempUri, CROP_GALLERY_IMAGE);
//                        picUri = tempUri

                        try {
                            bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), tempUri);
                            binding.ivAddImages.setImageBitmap(bm);

                            pathOfImage = ProjectUtils.getPath(AddAuctionNew.this, tempUri);
                            if (pathOfImage != null) {
                                File f = new File(pathOfImage);
//                                picUri = Uri.fromFile(f);
//                                file = f;
                                file = new Compressor(AddAuctionNew.this).compressToFile(f);
                                files.add(file);
                            }

                            setAda();

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
        }
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            images = new ArrayList<>();
            images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);


            files = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                imageCompression = new ImageCompression(mContext);
                imageCompression.execute(String.valueOf(images.get(i).path));
                imageCompression.setOnTaskFinishedEvent( new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        files.add(new File(imagePath));
                    }
                });

            }
     /*      if (images.size() < 5) {
                binding.addMoreimages.setVisibility(View.VISIBLE);


            } else {
                binding.addMoreimages.setVisibility(View.GONE);
            }*/




        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
                        "com.samyotech.getrid.fileprovider",
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


    private void setAda() {

        binding.rvImage.setVisibility(View.VISIBLE);
        binding.llImages.setVisibility(View.VISIBLE);
        addImageAdapter = new ImageAdapterNew(AddAuctionNew.this, files);
        binding.rvImage.setAdapter(addImageAdapter);
        delete();



    }

    public void delete() {
        if(files.size()>4){
            binding.ivAddImages.setVisibility(View.GONE);
            Toast.makeText(mContext, "You can not add more images.", Toast.LENGTH_LONG).show();

        }else{
            binding.ivAddImages.setVisibility(View.VISIBLE);

        }
    }

    public void imageRemove(int pos) {

/*

        if (images.size() < 5) {
            binding.addMoreimages.setVisibility(View.VISIBLE);
//            checkAllFields();

        } else {
            binding.addMoreimages.setVisibility(View.GONE);
//            checkAllFields();
        }
*/


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

            binding.etAdress.setText(obj.getAddressLine(0));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(mContext, MainFragment.class);
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


    private HashMap<String, String> getDetail() {
        HashMap<String, String> getDetail = new HashMap<>();


        getDetail.put(Const.TITLE, ProjectUtils.getEditTextValue(binding.etTitle));
      //  getDetail.put(Const.DESCRIPTION, ProjectUtils.getEditTextValue(binding.etDescription));
        getDetail.put(Const.SHORT_DESCRIPTION, ProjectUtils.getEditTextValue(binding.etshortDescription));
        getDetail.put(Const.S_Date, ProjectUtils.getEditTextValue(binding.etSDate));
        getDetail.put(Const.ADDRESS, ProjectUtils.getEditTextValue(binding.etAdress));
        getDetail.put(Const.E_DATE, ProjectUtils.getEditTextValue(binding.etEndDate));
        getDetail.put(Const.NO_OF_OWNER, "1");
        getDetail.put(Const.PRICE, ProjectUtils.getEditTextValue(binding.etPrice));

        if (binding.rbYes.isChecked()) {
            getDetail.put(Const.INSURED, "1");

        } else if (binding.rbNo.isChecked()) {
            getDetail.put(Const.INSURED, "0");

        } else {
            Toast.makeText(mContext, "please check any radio button", Toast.LENGTH_SHORT).show();
        }


        getDetail.put(Const.GET_CAT_ID, catid);
        getDetail.put(Const.GET_SUB_CAT_ID, subcatid);
        getDetail.put(Const.MODEL_ID, modelid);
        getDetail.put(Const.GET_BRAND_ID, brandid);
        getDetail.put(Const.LATITUDE, lattitude + "");
        getDetail.put(Const.LONGITUDE, longitude + "");
        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();


        }else{
            getDetail.put(Const.USER_PUB_ID, user_pub_id);

        }

        return getDetail;


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddImages:

                builder.show();
              // multipleImages();
                break;

            case R.id.addMoreimages:
                multipleImages();
                break;
            case R.id.etBrandName:
                if (commonList.size() > 0) {
                    spinnerDialog.showSpinerDialog();
                }
                break;

            case R.id.etAdress:
                showlocation();
                break;
            case R.id.et_end_date:
                if (!binding.etSDate.getText().toString().equals("")) {
                    openDatePickerEnd();
                } else {
                    Toast.makeText(mContext, "Please select start date.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_S_date:
                openDatePickerStart();
                break;
        }

    }

    private void showlocation() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                .build(AddAuctionNew.this);

        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);

    }

    private void multipleImages() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    private void editAuntion() {
        new HttpsRequest(Const.UPDATE_AUNCTION, getEditDetail(), paramsFile, mContext, mContext).multiImagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    try {
                        ProjectUtils.showToast(mContext, msg);
                        finish();


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } else {
                    ProjectUtils.showToast(mContext, msg);

                }
            }
        });

    }

    private HashMap<String, String> getEditDetail() {
        HashMap<String, String> getDetail = new HashMap<>();
        getDetail.put(Const.TITLE, ProjectUtils.getEditTextValue(binding.etTitle));
        getDetail.put(Const.Pro_pub_id, myAutionDTO.getPro_pub_id());

        //getDetail.put(Const.DESCRIPTION, ProjectUtils.getEditTextValue(binding.etDescription));
        getDetail.put(Const.SHORT_DESCRIPTION, ProjectUtils.getEditTextValue(binding.etshortDescription));

        getDetail.put(Const.S_Date, ProjectUtils.getEditTextValue(binding.etSDate));
        getDetail.put(Const.ADDRESS, ProjectUtils.getEditTextValue(binding.etAdress));
        getDetail.put(Const.E_DATE, ProjectUtils.getEditTextValue(binding.etEndDate));
        getDetail.put(Const.NO_OF_OWNER, "1");
        getDetail.put(Const.PRICE, ProjectUtils.getEditTextValue(binding.etPrice));

        if (binding.rbYes.isChecked()) {
            getDetail.put(Const.INSURED, "1");

        } else if (binding.rbNo.isChecked()) {
            getDetail.put(Const.INSURED, "0");

        } else {
            Toast.makeText(mContext, "please check any radio button", Toast.LENGTH_SHORT).show();
        }


        getDetail.put(Const.GET_CAT_ID, catid);
        getDetail.put(Const.GET_SUB_CAT_ID, subcatid);
        getDetail.put(Const.MODEL_ID, modelid);
        getDetail.put(Const.GET_BRAND_ID, brandid);
        getDetail.put(Const.LATITUDE, lattitude + "");
        getDetail.put(Const.LONGITUDE, longitude + "");
        if(user_pub_id.equals(Const.GUEST_USER_PUB_ID)){
            alertGuestUser();


        }else{
            checkValid=2;
            getDetail.put(Const.USER_PUB_ID, user_pub_id);

        }

        return getDetail;


    }


    private void setSubbrand() {
        binding.tvbrandname.setVisibility(View.VISIBLE);
        binding.line1.setVisibility(View.VISIBLE);

        new HttpsRequest(Const.GET_All_BRANDS, getSubCat(), mContext).stringPost(TAG,
                new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        ProjectUtils.pauseProgressDialog();
                        commonList = new ArrayList<>();
                        subBrandsDTOArrayList = new ArrayList<>();
                        if (flag) {
                            try {
                                Type getpetDTO = new TypeToken<List<SubBrandsDTO>>() {

                                }.getType();
                                subBrandsDTOArrayList = (ArrayList<SubBrandsDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                                for (int i = 0; i < subBrandsDTOArrayList.size(); i++) {
                                    commonList.add(new CommonDTO(subBrandsDTOArrayList.get(i).getBrand_id(), subBrandsDTOArrayList.get(i).getBrand_name()));
                                }
                                spinnerDialog = new SpinnerDialog(AddAuctionNew.this, commonList, "Select or Search", "Close");// With No Animation

                                spinnerDialog.setCancellable(true); // for cancellable
                                spinnerDialog.setShowKeyboard(false);// for open keyboard by default


                                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                                    @Override
                                    public void onClick(String item, int position) {
                                        brandid = commonList.get(position).getId();
                                        brandName = commonList.get(position).getCatName();
                                        setSubModel();
                                    }
                                });


                                statusBrand = true;

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        } else {
                            //  ProjectUtils.showToast(mContext, msg);

                            binding.tvbrandname.setVisibility(View.GONE);
                            binding.line1.setVisibility(View.GONE);
                        }
                    }
                });

    }


    private void setSubModel() {

        ProjectUtils.showProgressDialog(mContext, false, "Please Wait...");
        new HttpsRequest(Const.GET_All_MODEL, getBrands(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                ProjectUtils.pauseProgressDialog();
                commonListModel = new ArrayList<>();
                subModelDTOArrayList = new ArrayList<>();


                if (flag) {
                    try {
                        Type getpetDTO = new TypeToken<List<SubModelDTO>>() {

                        }.getType();
                        subModelDTOArrayList = (ArrayList<SubModelDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                        for (int i = 0; i < subModelDTOArrayList.size(); i++) {
                            commonListModel.add(new CommonDTO(subModelDTOArrayList.get(i).getModel_id(), subModelDTOArrayList.get(i).getModel_name()));
                        }
                        spinnerDialogModel = new SpinnerDialog(AddAuctionNew.this, commonListModel, "Select or Search", "Close");// With No Animation

                        spinnerDialogModel.setCancellable(true); // for cancellable
                        spinnerDialogModel.setShowKeyboard(false);// for open keyboard by default


                        spinnerDialogModel.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                modelid = commonListModel.get(position).getId();

                                binding.etBrandName.setText(brandName + "/" + item);

                            }
                        });
                        if (commonListModel.size() > 0) {
                            spinnerDialogModel.showSpinerDialog();


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    //  ProjectUtils.showToast(mContext, msg);
                    binding.etBrandName.setText(brandName);

                }
            }
        });


    }


    private HashMap<String, String> getSubCat() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.GET_SUB_CAT_ID, subcatid);
        return params;
    }

    private HashMap<String, String> getBrands() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Const.GET_BRAND_ID, brandid);

        return params;
    }


    public void openDatePickerStart() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);


        datePickerDialogADp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendarAdop.set(Calendar.YEAR, y);
                myCalendarAdop.set(Calendar.MONTH, m);
                myCalendarAdop.set(Calendar.DAY_OF_MONTH, d);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.etSDate.setText(sdf.format(myCalendarAdop.getTime()));
            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialogADp.setTitle("Select Date");

        datePickerDialogADp.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialogADp.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() + (1000 * 60 * 60 * 24 * 15));
        datePickerDialogADp.show();
    }


    public void openDatePickerEnd() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);


        datePickerDialogEnd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendarEnd.set(Calendar.YEAR, y);
                myCalendarEnd.set(Calendar.MONTH, m);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, d);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.etEndDate.setText(sdf.format(myCalendarEnd.getTime()));


            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialogEnd.setTitle("Select Date");
        datePickerDialogEnd.getDatePicker().setMinDate(myCalendarAdop.getTimeInMillis()+(1000*60*60*24));
        datePickerDialogEnd.getDatePicker().setMaxDate(myCalendarAdop.getTimeInMillis() + (1000 * 60 * 60 * 24 * 7));
        datePickerDialogEnd.show();
    }

    private void alertGuestUser() {


        builder1.setMessage(getResources().getString(R.string.guestMsg))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent= new Intent(mContext,Sign_in.class);
                        startActivity(intent);
                        finishAffinity();
                        checkValid=1;

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        //Toast.makeText(getApplicationContext(), "you clicked NO",
                        // Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder1.create();

        alert.setTitle("GetRid");
        alert.setIcon(R.drawable.ic_logout1);
        alert.show();
    }


}
