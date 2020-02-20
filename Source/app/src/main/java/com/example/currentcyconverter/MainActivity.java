package com.example.currentcyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Spinner spinner1, spinner2;
    EditText editFrom, editTo;
    TextView dateView;


    public static JSONObject jsonData;

    private JSONObject currencies;
    private String base;
    private String date;

    ArrayList<String> spinnerItems;

    public AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        spinner1 = findViewById(R.id.spinnerFROM);
        spinner2 = findViewById(R.id.spinnerTO);
        dateView = findViewById(R.id.retrieveDate);

        editFrom = findViewById(R.id.editFROM);
        editTo = findViewById(R.id.editTO);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                F2T();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                F2T();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        editFrom.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                F2T();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        currenciesAPI api = new currenciesAPI(this);
        api.execute();

    }

    private void F2T() {
        System.out.println(editFrom.getText());


        if (editFrom.getText().toString().matches("")) {
            editTo.setText("");
            return;
        }


        if (spinner1.getSelectedItemPosition() == spinner2.getSelectedItemPosition()){
            editTo.setText(editFrom.getText());
        }

        double valueFrom = Double.parseDouble(editFrom.getText().toString());


        double usd = 0;
        double valueTo = 0;
        try {
            usd = valueFrom / currencies.getDouble(spinner1.getSelectedItem().toString());
            valueTo = usd * currencies.getDouble(spinner2.getSelectedItem().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String output = Double.toString(valueTo);
        output = round(output);

        editTo.setText(output);
    }

    private void T2F(){

    }

    public void swap(View view){

        view.animate().rotation(view.getRotation() + 180).setDuration(250).start();



        int ori = spinner1.getSelectedItemPosition();
        spinner1.setSelection(spinner2.getSelectedItemPosition());
        spinner2.setSelection(ori);

        F2T();
    }

    public void finished(JSONObject data){

        jsonData = data;

        try {
            date = jsonData.getString("date");
            currencies = jsonData.getJSONObject("rates");
            base = jsonData.getString("base");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String dateMessage = "Rates are from " + date;
        dateView.setText(dateMessage);
        setSpinners();
    }

    public void setSpinners(){

        try {
            spinnerItems = getCurrencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
    }



    public ArrayList<String> getCurrencies() throws JSONException {

        String s = currencies.toString();
        ArrayList<String> lst = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ':'){
                lst.add(s.substring(i-4,i-1));
            }
        }
        Collections.sort(lst,String.CASE_INSENSITIVE_ORDER);

        return lst;

    }


    public static String round(String num){
        int pos = num.indexOf('.');

        if (pos == -1 || pos+3 >= num.length()){
            return num;
        }

        String rounded = num.substring(0,pos+3);
        return rounded;
    }
}
