package com.freshfastfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.visa.mvisa.QRCodeTag;
import com.visa.mvisa.merchantsdk.MVisaMerchantSDK;
import com.visa.mvisa.merchantsdk.MVisaMerchantSDKImpl;
import com.visa.mvisa.merchantsdk.exceptions.InputInvalidException;
import com.visa.mvisa.merchantsdk.models.facade.MVisaConfig;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        MVisaConfig config = new MVisaConfig.Builder(getApplicationContext()).backgroundColor("#34688D")
                .actionButtonColor("#5A9854")
                .actionButtonActiveColor("#80BF69")
                .build();

        MVisaMerchantSDK sdk = MVisaMerchantSDKImpl.getMerchantSDKInstance(getApplicationContext(),config);


        try {
            sdk.createQRCode(tagValueMap(),null);
        }catch (InputInvalidException e){
            e.printStackTrace();
        }

    }

    private HashMap<String,Object> tagValueMap(){

        HashMap<String,Object> tag = new HashMap<>();
        tag.put(QRCodeTag.PAYLOAD_FORMAT_INDICATOR.tagCode,"01");
        tag.put(QRCodeTag.MERCHANT_NAME.tagCode,"Luis");
        tag.put(QRCodeTag.MERCHANT_ID.tagCode,"4567");
        tag.put(QRCodeTag.CITY_NAME.tagCode,"Tacna");
        tag.put(QRCodeTag.COUNTRY_CODE.tagCode,"604");
        tag.put(QRCodeTag.CURRENCY_CODE.tagCode,"PEN");
        tag.put(QRCodeTag.TIP_AND_FEE_INDICATOR.tagCode,"02");
        tag.put(QRCodeTag.CONVENIENCE_FEE_AMOUNT.tagCode,"1");

        HashMap<String,String> secondValue = new HashMap<>();
//            secondValue.put(QRCodeTag.SubTag.MERCHANT_CITY_ALTERNATE_LANGUAGE.tagCode,"ID");
        return  tag;
    }

}