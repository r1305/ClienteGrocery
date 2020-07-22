package com.mpas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.freshfastfood.R;
import com.freshfastfood.utils.mapMarker.SignInLocation;

public class MainActivity1 extends AppCompatActivity implements SignInLocation.ExampleDialogListener {

    EditText etAddress;
    Button btnClick;
    DialogFragment df;
    FragmentManager fm=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnClick = findViewById(R.id.btnClick);
        etAddress = findViewById(R.id.etAddress123);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity1.this, "Clicked!", Toast.LENGTH_SHORT).show();
               SignInLocation sn = new SignInLocation();
               sn.show(fm,"Location");

            }
        });

    }

    @Override
    public void applyTexts(String address) {
        etAddress.setText(address);
    }
}