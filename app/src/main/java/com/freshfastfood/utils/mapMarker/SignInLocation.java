package com.freshfastfood.utils.mapMarker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.freshfastfood.BuildConfig;
import com.freshfastfood.R;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SignInLocation extends DialogFragment implements OnMapReadyCallback {
    private ExampleDialogListener listener;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    GoogleMap mmap;
    String stremailid;
    Marker mMarker, marker1;
    AlertDialog dialog;
    EditText eText;
    private Location current;
    double latitude, longitude;
    double lat, longi;
    public String city, zip, land, state,topAddress;
    //SweetAlertDialog sweetAlertDialog;
    private LocationCallback locationCallback;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private Boolean mRequestingLocationUpdates;
    String newlati, newlongi;
    private String mLastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    LatLng destination;
    Button submit;
    ImageView pin;


    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        try {
            LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
            View v = inflater.inflate(R.layout.row_address, null, false);

            assert getFragmentManager() != null;
            SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
            mapFragment.getMapAsync(this);

            eText = (EditText) v.findViewById(R.id.eText);
          //  pin = (ImageView) v.findViewById(R.id.mappin);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mSettingsClient = LocationServices.getSettingsClient(getActivity());
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    // location is received
                    current = locationResult.getLastLocation();
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                    //System.out.println("location callback");
                    updateLocationUI();
                }
            };
            mRequestingLocationUpdates = false;

            mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
//        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            mLocationSettingsRequest = builder.build();
            getlocation();


            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  /*  Intent i = new Intent(getActivity(), SignupDetailsActivity.class);
                    i.putExtra("city",city);
                    i.putExtra("postalcode",zip);
                    i.putExtra("address",topAddress);
                    i.putExtra("latitude",lat);
                    i.putExtra("longitude",longi);
                    PrefUtils.setLatitude(String.valueOf(lat),getActivity());
                    PrefUtils.setLongitude(String.valueOf(longi),getActivity());
*/
//                    startActivity(i);
 /*                   PrefUtils.setaddress(city,getActivity());
                    PrefUtils.setLand(land,getActivity());
                    PrefUtils.setProvince(state,getActivity());
                    PrefUtils.setFinalAddress(land+", "+city+", "+state,getActivity());
                    PrefUtils.setPostalCode(zip,getActivity());
*/
                    listener.applyTexts(eText.getText().toString());

                    dialog.dismiss();
                    dismiss();
                }
            });

            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    getDialog().dismiss();
                }
            });
            builder1.setView(v);
            dialog = builder1.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.location);
            Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.BLACK);
            Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK);
        } catch (Exception e) {
            Log.d("exception", e.getMessage());
        }
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

/*        ExampleDialogListener listener;
        if(context instanceof ExampleDialogListener){
            listener = (ExampleDialogListener) context;
        }*/
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String address);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mmap = googleMap;
        mmap.getUiSettings().setZoomControlsEnabled(true);
        mmap.animateCamera(CameraUpdateFactory.zoomTo(12));
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (current != null) {
            latitude = current.getLatitude();
            longitude = current.getLongitude();
            //System.out.println(latitude);
            LatLng latLng = new LatLng(latitude, longitude);
            mMarker = mmap.addMarker(new MarkerOptions().position(latLng).title("Current Location").draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            getCompleteAddressString(latitude, longitude);

            mmap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    destination = mmap.getCameraPosition().target;
                    //mMarker = mmap.addMarker(new MarkerOptions().position(destination).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    //mmap.animateCamera(CameraUpdateFactory.newLatLng(destination));
                    getCompleteAddressString(destination.latitude, destination.longitude);
                }
            });
        }
    }

    //get address from latitude and longitude
    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
                eText.setVisibility(View.VISIBLE);
                eText.setText(strReturnedAddress.toString());
                city = returnedAddress.getLocality();
                zip = returnedAddress.getPostalCode();
                land = returnedAddress.getSubLocality();
                state = returnedAddress.getAdminArea();
                lat = returnedAddress.getLatitude();
                longi = returnedAddress.getLongitude();
                topAddress = strReturnedAddress.toString();



                // need to save user location
                System.out.println(city + " " + zip + " " + land + " " + state);
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private void getlocation() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                locationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {

                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +"location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                                } catch (IntentSender.SendIntentException sie) {
                                    //Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                System.out.println(errorMessage);
                                // Log.e(TAG, errorMessage);
                                //Toast.makeText(MechRegLocation.this, errorMessage, Toast.LENGTH_LONG).show();

                        }
                        updateLocationUI();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        //Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;

                }
                break;

        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {
            // pausing location updates
            //stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else if (getDialog() != null && getDialog().isShowing()) {
            getDialog().dismiss();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.mapfragment));
        FragmentTransaction ft = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        }
        ft.remove(fragment);
        ft.commit();
    }
}
