package com.example.currencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.currencyapp.Retrofit.RetrofitBuilder;
import com.example.currencyapp.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public boolean decimalDisplay;
    TextView txtCurrency1, txtCurrency2, txtInfoValue, txtInfoDate;
    Spinner spinnerCurrencyFrom;
    Spinner spinnerCurrencyTo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCurrency1 = findViewById(R.id.txtCurrency1);
        txtCurrency2 = findViewById(R.id.txtCurrency2);
        txtInfoDate = findViewById(R.id.txtInfoDate);
        txtInfoValue = findViewById(R.id.txtInfoValue);
        spinnerCurrencyFrom = (Spinner) findViewById(R.id.spinnerCurrencyFrom);
        spinnerCurrencyTo = (Spinner) findViewById(R.id.spinnerCurrencyTo);

//        Adding Spinner Value
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrencyFrom.setAdapter(adapter);
        spinnerCurrencyTo.setAdapter(adapter);
        SpinnerSetup();
        txtChange();
    }

    //Connection To GEt An API Result
    public void getCurrencies(){
        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
        Call<JsonObject> call = retrofitInterface.getExchangeCurrency(spinnerCurrencyFrom.getSelectedItem().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject res = response.body();
                    JsonObject rates = res.getAsJsonObject("rates");
                    double currency1 = Double.valueOf(txtCurrency1.getText().toString());
                    double currency2 = Double.valueOf(rates.get(spinnerCurrencyTo.getSelectedItem().toString()).toString());
                    double result = currency1 * currency2;
                    txtCurrency2.setText(String.valueOf(result));

                    String dataRes = "1 "+spinnerCurrencyFrom.getSelectedItem().toString() +" = "+ currency2+" "+spinnerCurrencyTo.getSelectedItem().toString();
                    String date = response.body().get("date").toString();;
                    txtInfoDate.setText(date);
                    txtInfoValue.setText(dataRes);
                }catch (Exception E){
                    txtCurrency1.setText("0");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

//    Textchange Calculate the Value
    private void txtChange(){
        txtCurrency1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    getCurrencies();
                }catch (Exception E){
                    Log.e("Error", String.valueOf(E));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
//Spinner Change Value Function
    private void SpinnerSetup(){
        spinnerCurrencyFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCurrencies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCurrencyTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCurrencies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //    Set NUmber
    @SuppressLint("SetTextI18n")
    private void setNum(String num){
        String Number = txtCurrency1.getText().toString();
            if(!Number.equals("0")){
                txtCurrency1.setText(Number+num);
            }else{
                txtCurrency1.setText(num);
            }

    }

    //   Setting OnclickListener

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn0:
                setNum("0");
                break;
            case R.id.btn1:
                setNum("1");
                break;
            case R.id.btn2:
                setNum("2");
                break;
            case R.id.btn3:
                setNum("3");
                break;
            case R.id.btn4:
                setNum("4");
                break;
            case R.id.btn5:
                setNum("5");
                break;
            case R.id.btn6:
                setNum("6");
                break;
            case R.id.btn7:
                setNum("7");
                break;
            case R.id.btn8:
                setNum("8");
                break;
            case R.id.btn9:
                setNum("9");
                break;
            case R.id.btnDot:
                if(!decimalDisplay){
                    setNum(".");
                    decimalDisplay = true;
                }
                break;
            case R.id.btnClearEntry:
                txtCurrency1.setText("0");
                decimalDisplay = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

// Delete Function
    public void onClickDelete(View view) {
        String v = txtCurrency1.getText().toString();
        if(!v.isEmpty()){
            txtCurrency1.setText(v.substring(0,v.length()-1));
        }else if(txtCurrency1.getText().length() <= 1){
            txtCurrency1.setText("0");
            getCurrencies();
            decimalDisplay = false;
        }
    }
}