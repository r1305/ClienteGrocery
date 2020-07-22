package com.freshfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.freshfastfood.Culqi.Culqi;
import com.freshfastfood.R;
import com.freshfastfood.Validation.Validation;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PagarconCulqi extends AppCompatActivity {

    Context ctx;
    Validation validation;

    ProgressDialog progress;

    TextView txtcardnumber, txtcvv, txtmonth, txtyear, txtemail, kind_card, result;
    Button btnPay;

    String amount="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagarcon_culqi);
        ctx = this;
        validation = new Validation();
        amount = getIntent().getStringExtra("amount");

        progress = new ProgressDialog(this);
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        txtcardnumber = (TextView) findViewById(R.id.txt_cardnumber);

        txtcvv = (TextView) findViewById(R.id.txt_cvv);

        txtmonth = (TextView) findViewById(R.id.txt_month);

        txtyear = (TextView) findViewById(R.id.txt_year);

        txtemail = (TextView) findViewById(R.id.txt_emaill);

        kind_card = (TextView) findViewById(R.id.kind_card);

        result = (TextView) findViewById(R.id.token_id);

        btnPay = (Button) findViewById(R.id.btn_payy);

        txtcvv.setEnabled(false);

        txtcardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    txtcvv.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtcardnumber.getText().toString();
                if(s.length() == 0) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                if(validation.luhn(text)) {
                    txtcardnumber.setBackgroundResource(R.drawable.border_sucess);
                } else {
                    txtcardnumber.setBackgroundResource(R.drawable.border_error);
                }

                int cvv = validation.bin(text, kind_card);
                if(cvv > 0) {
                    txtcvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    txtcvv.setEnabled(true);
                } else {
                    txtcvv.setEnabled(false);
                    txtcvv.setText("");
                }
            }
        });

        txtyear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtyear.setBackgroundResource(R.drawable.border);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtyear.getText().toString();
                if(validation.year(text)){
                    txtyear.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtyear.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        txtmonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txtmonth.setBackgroundResource(R.drawable.border);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = txtmonth.getText().toString();
                if(validation.month(text)){
                    txtmonth.setBackgroundResource(R.drawable.border_error);
                } else {
                    txtmonth.setBackgroundResource(R.drawable.border_sucess);
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                progress.show();
                String total = String.valueOf(Double.parseDouble(amount)*100);
                String str_name = "Cliente";
                String str_last_name = "Prueba";
                String email = txtemail.getText().toString();
                String str_card = txtcardnumber.getText().toString();
                String str_month = txtmonth.getText().toString();
                String str_year = txtyear.getText().toString();
                String str_ccv = txtcvv.getText().toString();
                sendCulqi(str_name,str_last_name,email,str_card,str_month,str_year,str_ccv,total);
            }
        });

    }

    public void sendCulqi(String first_name,String last_name,String email,String card,String month,String year,String ccv,String total)
    {
        String url=ctx.getApplicationContext().getString(R.string.base_url)+ctx.getApplicationContext().getString(R.string.pagoCulqi);
        Log.i("sendToken_url",url);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            result.setText(response);
                            progress.dismiss();
                            Culqi culqi = new Gson().fromJson(response,Culqi.class);
                            if(culqi.getObject().equals("charge")){
                                PagarconCulqi.this.finish();
                            }else{
                                Toast.makeText(ctx,culqi.getUser_message(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("sendToken_error: " + e.getMessage());
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("sendToken_error: " + error.getMessage());
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("email",email);
                params.put("card",card);
                params.put("ccv",ccv);
                params.put("month",month);
                params.put("year",year);
                params.put("total",total);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

}
