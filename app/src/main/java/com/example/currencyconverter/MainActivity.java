package com.example.currencyconverter;

import static android.view.animation.AnimationUtils.loadAnimation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView convertfromtxt, convertTotxt, convertedAm;
    EditText AmToConvert;
    ArrayList<String> arrayList;
    Dialog fromDialog;
    Dialog toDialog;
    Button ConvertBtn;
    String convertFromValue, convertToValue , conversionValue;
    String[] Country = {"AFN","ALL","DZD","USD","AOA","EUR","ARS","AMD","AWG","AUD","AZN","BSD","BHD","BDT","BBD","BYN","BZD","XOF","BMD","INR","BTN"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        convertfromtxt = findViewById(R.id.Convert_from_select);
        convertTotxt = findViewById(R.id.Convert_To_select);
        convertedAm = findViewById(R.id.converted_text);
        AmToConvert = findViewById(R.id.AmountToConvert);
        ConvertBtn = findViewById(R.id.convert);

        arrayList = new ArrayList<>();
        for (String i : Country){
            arrayList.add(i);
        }

        convertfromtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(720,1080);
                fromDialog.show();

                EditText editText=fromDialog.findViewById(R.id.edit_text1);
                ListView listView=fromDialog.findViewById(R.id.list_view1);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertfromtxt.setText(adapter.getItem(i));
                        fromDialog.dismiss();
                        convertFromValue = adapter.getItem(i);
                    }
                });
            }
        });

        convertTotxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toDialog = new Dialog(MainActivity.this);
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(720,1080);
                toDialog.show();

                EditText editText=toDialog.findViewById(R.id.edit_text2);
                ListView listView=toDialog.findViewById(R.id.list_view2);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertTotxt.setText(adapter.getItem(i));
                        toDialog.dismiss();
                        convertToValue = adapter.getItem(i);
                    }
                });

            }
        });


        ConvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double amountToConvert = Double.valueOf(MainActivity.this.AmToConvert.getText().toString());
                    getConversionRate(convertFromValue,convertToValue,amountToConvert);
                }
                catch (Exception e){

                }
            }


        });




    }

    public String getConversionRate(String convertFrom,String convertTo,Double amountToConvert){
        RequestQueue queue= Volley.newRequestQueue(this);


       String url= "https://free.currconv.com/api/v7/convert?q="+convertFrom+"_"+convertTo+"&compact=ultra&apiKey=2af4fbf832398e56a09a";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Double conversionRateValue = round(((Double) jsonObject.get(convertFrom + "_" + convertTo)), 2);
                    conversionValue=""+round((conversionRateValue * amountToConvert),2);
                    convertedAm.setText(conversionValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

        return null;
    }

    public static double round(double value,int places){
        if(places<0) throw new IllegalArgumentException();
        BigDecimal bd= BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}