package com.freshfastfood.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.freshfastfood.R;
import com.freshfastfood.activity.AddressActivity;
import com.freshfastfood.activity.FirstActivity;
import com.freshfastfood.activity.LoginActivity;
import com.freshfastfood.model.ProductItem;
import com.freshfastfood.MyApplication;
import com.freshfastfood.model.User;
import com.freshfastfood.notification.MyNotificationManager;
import com.freshfastfood.retrofit.APIClient;
import com.freshfastfood.retrofit.GetResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class Utiles {
    public static boolean isRef=false;
    public static boolean isSelect=false;
    public static int seletAddress=0;
    public static int ISVARIFICATION =-1;
    public static boolean ISRATES =false;
    public static List<ProductItem> ProductDatum=new ArrayList<>();

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getNextDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        return dateFormat.format(tomorrow);
    }

    static String IMEI = "";
    public static String getIMEI(Context context) {

        String unique_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("unique_id", "-->" + unique_id);
        return unique_id;
    }
    public static boolean internetChack() {
        ConnectivityManager ConnectionManager = (ConnectivityManager) MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){
            ex.toString();
        }
    }

    public void initializeFirebase(Context ctx)
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("fcm_instance", "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                String msg = token;
                Log.e("fcm_token", msg);
                //Aqui debes llamar a tu metodo de actualizaciond e token en la base de datos
                udpateFcm(ctx,msg);
            }
        });
    }

    public void showNotification(Context ctx,String body,String title)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("1", "1",importance);
            mChannel.setDescription("1");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{0, 500, 250, 500, 250,500, 250});
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(mChannel);
            MyNotificationManager.getInstance(ctx).displayNotification(body,title);
        }else{
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx)
                    .setSmallIcon(R.drawable.firsticon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setVibrate(new long[]{0, 500, 250, 500, 250,500, 250})
                    .setPriority(importance)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager notificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    private void udpateFcm(Context ctx,String fcm) {
        SessionManager sessionManager = new SessionManager(ctx);
        User user = sessionManager.getUserDetails("");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fcm",fcm);
            jsonObject.put("uid",user.getId());
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().UpdateFcm((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.callForLogin(call,"1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
