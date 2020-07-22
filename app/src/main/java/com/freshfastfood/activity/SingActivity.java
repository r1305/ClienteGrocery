package com.freshfastfood.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.freshfastfood.model.CCode;
import com.freshfastfood.model.Contry;
import com.freshfastfood.model.RestResponse;
import com.freshfastfood.model.User;
import com.freshfastfood.R;
import com.freshfastfood.utils.CustPrograssbar;
import com.freshfastfood.utils.SessionManager;
import com.freshfastfood.retrofit.APIClient;
import com.freshfastfood.retrofit.GetResult;
import com.freshfastfood.utils.mapMarker.SignInLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.freshfastfood.utils.Utiles.ISVARIFICATION;

public class SingActivity extends AppCompatActivity implements GetResult.MyListener, LocationListener {
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_alternatmob)
    EditText edAlternatmob;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.spinner)
    Spinner spinner;
    EditText ed_society;
    List<CCode> cCodes = new ArrayList<>();
    String codeSelect;
    EditText ed_type;
    DialogFragment df;
    ImageView ivLocation;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ivLocation = findViewById(R.id.ivLocation);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(SingActivity.this);
        custPrograssbar = new CustPrograssbar();
        GetCode();
        ed_society = findViewById(R.id.ed_society);
        ed_type = findViewById(R.id.ed_type);
        checkSelfPermission();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ed_society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void GetCode() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getCode((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    private void IsRegister() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", edAlternatmob.getText().toString());
            jsonObject.put("ccode", codeSelect);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getForgot((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.PrograssCreate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                custPrograssbar.ClosePrograssBar();
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    Toast.makeText(SingActivity.this, "Número de móvil ya registrado", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User();
                    user.setEmail(edEmail.getText().toString());
                    user.setMobile(edAlternatmob.getText().toString());
                    user.setName(edUsername.getText().toString());
                    user.setPassword(edPassword.getText().toString());
                    user.setCcode(codeSelect);
                    sessionManager.setUserDetails("", user);
                    ISVARIFICATION =1;
                    startActivity(new Intent(SingActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edAlternatmob.getText().toString()));

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getData();
                List<String> Arealist = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        Arealist.add(cCodes.get(i).getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinnercode_layout, Arealist);
                dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                spinner.setAdapter(dataAdapter);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.btn_sign, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign:
                if (validation()) {
                    IsRegister();
                }
                break;
            case R.id.btn_login:
                startActivity(new Intent(SingActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError("Ingrese su nombre");
            return false;
        }
        if (edEmail.getText().toString().isEmpty()) {
            edEmail.setError("Ingrese un email valido");
            return false;
        }
        if (edAlternatmob.getText().toString().isEmpty()) {
            edAlternatmob.setError("Ingrese el número de teléfono móvil válido");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Introducir la contraseña");
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SingActivity.this, LoginActivity.class));
        finish();
    }

    private void checkSelfPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionLocation = ContextCompat.checkSelfPermission(SingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    private void getLocation(){
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_HIGH);
                criteria.setPowerRequirement(Criteria.ACCURACY_HIGH);
                criteria.setAltitudeRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                criteria.setBearingRequired(false);


                assert locationManager != null;
                locationManager.requestLocationUpdates(300, 50000, criteria, (LocationListener) this, null);
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, this);
            } else {
                Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
                showGPSDisabledAlertToUser();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SingActivity.this, R.style.AppTheme);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



    @SuppressLint("SetTextI18n")
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

//            etLocation.setText(addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea() + ", "
//                    + addresses.get(0).getCountryName());

            String address = addresses.get(0).getLocality();
            String s1 = addresses.get(0).getAdminArea();
            String s2 = addresses.get(0).getCountryCode();
            String s3 = addresses.get(0).getCountryName();
            String s4 = addresses.get(0).getLocality();
            String s5 = addresses.get(0).getPhone();
            String s6 = addresses.get(0).getPostalCode();

            System.out.println(s1+s2+s3+s4+s5+s6);

            Toast.makeText(this, ""+s1+","+s2+s3+s4+s5+s6, Toast.LENGTH_SHORT).show();

    /*        String strLatitude = String.valueOf(addresses.get(0).getLatitude());
            String strLongitude = String.valueOf(addresses.get(0).getLongitude());*/

//            PrefUtils.setLatitude(strLatitude,SplashActivity.this);
            //          PrefUtils.setLongitude(strLongitude,SplashActivity.this);
            ed_type.setText(address);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Your Location is: ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

