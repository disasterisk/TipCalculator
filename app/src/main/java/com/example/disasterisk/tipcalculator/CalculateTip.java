package com.example.disasterisk.tipcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class CalculateTip extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    private EditText subtotalInput;
    private TextView tipPercentDisplay;
    private TextView taxDisplay;
    private TextView tipDisplay;
    private TextView totalDisplay;
    private TextView nameView;
    private Button plusButton;
    private Button minusButton;
    private SharedPreferences sharedPref;
    private String subtotal;
    private double taxRate = 0.096;
    private double tipRate = 0.15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_tip);

        String name = sharedPref.getString("name_preference", "Person A");
        subtotalInput = findViewById(R.id.SubtotalEnter);
        tipPercentDisplay = findViewById(R.id.PercentDisplay);
        taxDisplay = findViewById(R.id.TaxAmount);
        tipDisplay = findViewById(R.id.TipAmount);
        totalDisplay = findViewById(R.id.TotalAmount);
        nameView = findViewById(R.id.nameView);
        plusButton = findViewById(R.id.PlusButton);
        minusButton = findViewById(R.id.MinusButton);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        tipPercentDisplay.setText(NumberFormat.getPercentInstance().format(tipRate));
        taxDisplay.setText(NumberFormat.getCurrencyInstance().format(0));
        tipDisplay.setText(NumberFormat.getCurrencyInstance().format(0));
        totalDisplay.setText(NumberFormat.getCurrencyInstance().format(0));
        nameView.setText(name);

        subtotalInput.setOnEditorActionListener(this);
        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);

    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.tip_calc_menu, menu);
        return true;
    }
    @Override
    public void onPause(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("subtotal", subtotal);
        editor.putString("tipRate", Double.toString(tipRate));
        editor.commit();

        super.onPause();
    }
    public void onResume(){
        super.onResume();
        subtotal = sharedPref.getString("subtotal","");
        tipRate = Double.parseDouble(sharedPref.getString("tipRate", "0.15"));
        subtotalInput.setText(subtotal);
        tipPercentDisplay.setText(NumberFormat.getPercentInstance().format(tipRate));
        calculateDisplay();

    }
    public boolean onEditorAction(TextView text, int i, KeyEvent keyEvent){
        calculateDisplay();
        return false;
    }
    public void onClick(View view){
        switch(view.getId()){
            case R.id.PlusButton:
                tipRate = tipRate + 0.01f;
                break;
            case R.id.MinusButton:
                tipRate = tipRate - 0.01f;
                break;
        }
        tipPercentDisplay.setText(NumberFormat.getPercentInstance().format(tipRate));
        calculateDisplay();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuName:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void calculateDisplay(){
        subtotal = subtotalInput.getText().toString();
        float s;
        if(subtotal.equals("")){
            s = 0;
        }else{
            s = Float.parseFloat(subtotal);
        }
        double tax = s * taxRate;
        double tip = s * tipRate;
        double total = s + tax + tip;
        NumberFormat percent = NumberFormat.getPercentInstance();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipPercentDisplay.setText(percent.format(tipRate));
        taxDisplay.setText(currency.format(tax));
        tipDisplay.setText(currency.format(tip));
        totalDisplay.setText(currency.format(total));
    }
}
