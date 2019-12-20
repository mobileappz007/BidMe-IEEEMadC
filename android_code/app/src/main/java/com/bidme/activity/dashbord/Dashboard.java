package com.bidme.activity.dashbord;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.bidme.BuildConfig;
import com.bidme.R;
import com.bidme.activity.FilterActivity;
import com.bidme.activity.authentication.Sign_in;
import com.bidme.activity.authentication.UpdateProfile;
import com.bidme.activity.SearchActivity;
import com.bidme.activity.aution.ViewAuction;
import com.bidme.activity.social.AllSubscriptionHistory;
import com.bidme.adapter.MyChatsAdapter;
import com.bidme.fragment.dashboad.Browse;
import com.bidme.fragment.dashboad.MyAutions;
import com.bidme.fragment.dashboad.MyChat;
import com.bidme.fragment.dashboad.Notification;
import com.bidme.fragment.dashboad.Setting;
import com.bidme.fragment.dashboad.Subscription;
import com.bidme.interfaces.Const;
import com.bidme.model.UserDTO;
import com.bidme.preferences.SharedPrefrence;

import java.io.File;
import java.util.HashMap;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.bidme.utils.CustomTypeFaceSpan;
import com.bidme.utils.ProjectUtils;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    public static int navItemIndex = 0;
    BottomSheet.Builder builder;
    Button closeButton;
    AlertDialog.Builder builder1;
    private static final String TAG_BROWSE = "browse";
    private static final String TAG_CHATS = "chats";
    private static final String TAG_MYADS = "myads";
    private static final String TAG_MYAUCTION = "myauction";
    private static final String TAG_NOTIFICATION = "notification";
    private static final String TAG_SUBCRIPTION = "subcription";
    private static final String TAG_SETTING = "SETTING";
    private static final String TAG_VIEWAUCTION = "viewAuciton";

    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_SUBSCRIPTION = "subscription";
    public static String CURRENT_TAG = TAG_BROWSE;
    private HashMap<String, File> paramsFile = new HashMap<>();
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private ImageView ivMenu;
    private ImageView ivFilter, ivSearch, ivProfilpic, ivDash;
    private TextView tvtitle, tvusername;
    SharedPrefrence sharedPrefrence;
    private UserDTO userDTO;
    private Layout app_bar;
    private View actionheaderbar;
    private static final String TAG = Dashboard.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;
    private TextView subscribe, unsubscribe, userName;
    private ImageView ivDasBack1, ivHistory;
    private View heder;
    private ImageView ivBack, history;
    private TextView tvtitilemain;
    private SearchView svHistory;
    private MyChatsAdapter myChatsAdapter;
    public static int number = 1;
    public static String pro_pub_id;
    private static final float END_SCALE = 0.8f;
    View contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sharedPrefrence = SharedPrefrence.getInstance(Dashboard.this);


        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);

        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        ivMenu = findViewById(R.id.ivMenu);
        ivFilter = findViewById(R.id.ivFilter);
        ivSearch = findViewById(R.id.ivSearch1);
        ivDash = findViewById(R.id.ivDasBack1);
        tvusername = navHeader.findViewById(R.id.usrename);
        ivDasBack1 = navHeader.findViewById(R.id.ivDasBack1);
        actionheaderbar = findViewById(R.id.appbar);
        heder = actionheaderbar.findViewById(R.id.action_bar);
        tvtitilemain = findViewById(R.id.tvtitlemain);
        ivProfilpic = navHeader.findViewById(R.id.imageView);
        subscribe = navHeader.findViewById(R.id.subscribe);
        unsubscribe = navHeader.findViewById(R.id.unsubscribe);
        userName = navHeader.findViewById(R.id.usrename);
        ivBack = findViewById(R.id.ivBack);
        ivHistory = findViewById(R.id.ivHistory);
        contentView = findViewById(R.id.content);
        builder1 = new AlertDialog.Builder(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        ColorStateList csl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked}  // checked
                },
                new int[]{
                        Color.WHITE,
                        Color.RED
                }
        );


        if (getIntent() != null) {
            navItemIndex = getIntent().getIntExtra("index", 0);
            pro_pub_id = getIntent().getStringExtra(Const.Pro_pub_id);
            getHeaderName();
            // CURRENT_TAG = TAG_BROWSE;
            loadHomeFragment();
        }
        setUpNavigationView();


//        if (savedInstanceState == null) {
//            navItemIndex = 0;
//            getHeaderName();
//           // CURRENT_TAG = TAG_BROWSE;
//            loadHomeFragment();
//        }


        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {

                                         // Scale the View based on current slide offset
                                         final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                         final float offsetScale = 1 - diffScaledOffset;
                                         contentView.setScaleX(offsetScale);
                                         contentView.setScaleY(offsetScale);

                                         // Translate the View, accounting for the scaled width
                                         final float xOffset = drawerView.getWidth() * slideOffset;
                                         final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                         final float xTranslation = xOffset - xOffsetDiff;
                                         contentView.setTranslationX(xTranslation);
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                     }
                                 }
        );


        ivDasBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerOpen();

            }
        });
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this, FilterActivity.class);
                startActivity(in);


            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this, SearchActivity.class);
                startActivity(in);


            }


        });
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this, AllSubscriptionHistory.class);
                startActivity(in);
            }
        });


        ivProfilpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this, UpdateProfile.class);
                startActivity(in);

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);


        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();


        Menu m = navigationView.getMenu();


        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }

    public void getHeaderName() {
        if (navItemIndex == 0)
            CURRENT_TAG = TAG_BROWSE;
        else if (navItemIndex == 1)
            CURRENT_TAG = TAG_CHATS;
        else if (navItemIndex == 2)
            CURRENT_TAG = TAG_MYADS;
        else if (navItemIndex == 3)
            CURRENT_TAG = TAG_MYAUCTION;
        else if (navItemIndex == 4)
            CURRENT_TAG = TAG_NOTIFICATION;
        else if (navItemIndex == 5)
            CURRENT_TAG = TAG_SUBSCRIPTION;
        else if (navItemIndex == 6)
            CURRENT_TAG = TAG_SETTING;
        else if (navItemIndex == 7)
            CURRENT_TAG = TAG_VIEWAUCTION;


    }


    public void drawerOpen() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private void loadHomeFragment() {

        selectNavMenu();


        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();


            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                ivBack.setVisibility(View.GONE);
                ivSearch.setVisibility(View.VISIBLE);
                ivFilter.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.VISIBLE);

                ivHistory.setVisibility(View.GONE);

                tvtitilemain.setVisibility(View.VISIBLE);
                tvtitilemain.setText("Browse");
                Browse browse = new Browse();
                return browse;
            case 1:
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                tvtitilemain.setVisibility(View.VISIBLE);
                ivHistory.setVisibility(View.GONE);

                tvtitilemain.setText("Chats");
                MyChat chats = new MyChat();
                return chats;
            case 2:
                number = 2;
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                ivHistory.setVisibility(View.GONE);


                tvtitilemain.setText("My aunctions");
                Intent intent = new Intent(this, ViewAuction.class);
                intent.putExtra(Const.Pro_pub_id, pro_pub_id);
                intent.putExtra(Const.FLAG, "2");

                startActivity(intent);
                MyAutions myBid = new MyAutions();


                return myBid;
            case 3:
                number = 1;
                tvtitilemain.setText("My aunctions");
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                ivHistory.setVisibility(View.GONE);
                tvtitilemain.setVisibility(View.VISIBLE);
                tvtitilemain.setText("Aunctions");

                MyAutions myauction = new MyAutions();
                return myauction;
            case 4:
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                tvtitilemain.setVisibility(View.VISIBLE);
                ivHistory.setVisibility(View.GONE);


                tvtitilemain.setText("Notification");
                Notification notification = new Notification();
                return notification;

            case 5:
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                ivBack.setVisibility(View.VISIBLE);
                ivMenu.setVisibility(View.GONE);
                tvtitilemain.setVisibility(View.VISIBLE);
                ivHistory.setVisibility(View.VISIBLE);
                tvtitilemain.setText("Subscription");

                Subscription subscription = new Subscription();
                return subscription;


            case 6:
                ivSearch.setVisibility(View.GONE);
                ivFilter.setVisibility(View.GONE);
                ivMenu.setVisibility(View.GONE);
                ivBack.setVisibility(View.VISIBLE);
                ivHistory.setVisibility(View.GONE);
                tvtitilemain.setVisibility(View.VISIBLE);
                tvtitilemain.setText("Settings");
                Setting setting = new Setting();
                return setting;


            default:
                return new Browse();
        }
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);


    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                switch (menuItem.getItemId()) {

                    case R.id.nav_browse:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BROWSE;

                        break;
                    case R.id.nav_chats:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CHATS;
                        break;
                    case R.id.nav_myads:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MYADS;
                        break;
                    case R.id.nav_myauction:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MYAUCTION;
                        break;
                    case R.id.nav_notification:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        break;
                    case R.id.nav_subcription:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SUBSCRIPTION;
                        break;
                    case R.id.nav_setting:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_SETTING;
                        break;


                    case R.id.nav_logout:
                        logout();

                        break;


                    case R.id.ivDasBack1:
                        onBackPressed();

                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }

    private void logout() {

        builder1.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        builder1.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sharedPrefrence.clearAllPreferences();
                        Intent in = new Intent(Dashboard.this, Sign_in.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);

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

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }


        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_BROWSE;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Montserrat-SemiBold.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            myLocationUpdate();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                myLocationUpdate();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        myLocationUpdate();
                        break;
                }
                break;
        }
    }


    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");


                        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        myLocationUpdate();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Dashboard.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(Dashboard.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        myLocationUpdate();
                    }
                });
    }


    private void myLocationUpdate() {
        if (mCurrentLocation != null) {
            Log.e("LATI_Longi", "Lati--" + mCurrentLocation.getLatitude() + "_______ Longi--" + mCurrentLocation.getLongitude());

            sharedPrefrence.setValue(Const.LATITUDE, mCurrentLocation.getLatitude() + "");
            sharedPrefrence.setValue(Const.LONGITUDE, mCurrentLocation.getLongitude() + "");

        }
    }


    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mRequestingLocationUpdates = true;
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
        myLocationUpdate();

        userDTO = sharedPrefrence.getParentUser(Const.USER_DTO);
        Glide.with(Dashboard.this)
                .load(userDTO.getImage())
                .centerCrop()
                .placeholder(R.drawable.noimage)
                .into(ivProfilpic);
        userName.setText(ProjectUtils.capWordFirstLetter(userDTO.getName()));

    }

    @Override
    protected void onPause() {
        super.onPause();


        stopLocationUpdates();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Dashboard.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(Dashboard.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {

                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }
}



