package com.example.dansu.mortgagecalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;

public class MortgagaeMainActivity extends AppCompatActivity{
    private static final String HOME_VALUE="HOME_VALUE";
    private static final String DOWN_PAYMENT="DOWN_PAYMENT";
    private static final String CUSTOM_INTEREST_RATE="CUSTOM_INTEREST_RATE";
    private static final String PROPERTY_TAX="PROPERTY_TAX";
    private static final String TERMS="TERMS";
    private Spinner spinner;

    private double homeValue;
    private EditText homeValueEditText;

    private double downPayment;
    private EditText downPaymentEditText;

    private double interestRate;
    private EditText interestRateEditText;

    private double propertyTax;
    private EditText propertyTaxEditText;

    private double terms;
    private Spinner termsSpinner;

    private  double monthlyPayment;
    private EditText monthlyPaymentText;

    private  double totalInterest;
    private EditText totalInterestText;

    private  double totalProp;
    private EditText totalPropText;

    private Date payOff;
    private EditText payOffText;

     Button calculateBt;
    Button reset;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        if (savedInstanceState==null)//the app just started running
        {
            homeValue=0.0;
            downPayment=0.0;
            terms=0;
            interestRate=0.0;
            propertyTax=0.0;
            monthlyPayment=0.0;
            totalInterest=0.0;
            totalProp=0.0;

        }
        else// app is being restored from memory, not exexcute from scratch
        {
            homeValue=savedInstanceState.getDouble(HOME_VALUE);
            downPayment=savedInstanceState.getDouble(DOWN_PAYMENT);
            terms=savedInstanceState.getInt(TERMS);
            interestRate=savedInstanceState.getDouble(CUSTOM_INTEREST_RATE);
            propertyTax=savedInstanceState.getDouble(PROPERTY_TAX);
            monthlyPayment=0.0;
            totalInterest=0.0;
            totalProp=0.0;

        }
        homeValueEditText=(EditText)findViewById(R.id.HomevalueText);
    //get the downPaymentEditText
    downPaymentEditText=(EditText)findViewById(R.id.downpaymentText);
    //get the interestRateEditText
    interestRateEditText=(EditText)findViewById(R.id.rateText);
   //get the propertyTaxEditText
    propertyTaxEditText=(EditText)findViewById(R.id.property_taxText);
    //get the Terms
    termsSpinner=(Spinner)findViewById(R.id.year_array);
    ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.year_array,
            android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    termsSpinner.setAdapter(adapter);


       addListenerOnCalculate();

        addListenerOnReset();
    }

    // calcuate monthly payment
private  double monthlyPaymentFormula(double homePrice, double downPay, double propertT, double tms,double intreresR )
{
    double n=tms*12;//total months
    double p=homePrice-downPay;//total loan or principle amount
    double i=intreresR/12/100;//monthly interest rate;
    double t=homePrice*propertT/12/100;//monthly propertax;
    return p*(i*Math.pow(1+i,n))/(Math.pow(1+i,n)-1)+t;//monthly payment


}
    private double totalInterestFormula(double interestR, double terms,double homeValue, double downPayment, double propertT){
        double n=terms*12;//total months
        double p=homeValue-downPayment;//total loan or principle amount
        double i=interestR/12/100;//monthly interest rate;

        double m= p*(i*Math.pow(1+i,n))/(Math.pow(1+i,n)-1);//monthly payment without ptoperty tax
        return m*n-p;
    }
    private double totalPropFormula(double protertR, double terms, double homeValue,double downPayment)
    {
        return homeValue*protertR*terms/100;
    }
    private String getPayOffDate(){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, (int) terms*12);
        Date date = calendar.getTime();
        String format = "MMM, yyyy";
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }


    public void result()
    {

        monthlyPayment=monthlyPaymentFormula(homeValue, downPayment, propertyTax, terms, interestRate);
        totalInterest=totalInterestFormula(interestRate, terms, homeValue,downPayment,propertyTax);
        totalProp =totalPropFormula(propertyTax, terms,homeValue, downPayment);
        //set monthly payment
       monthlyPaymentText=(EditText)findViewById(R.id.monthlypaymentText);

       monthlyPaymentText.setText("$" + String.format("%.2f", monthlyPayment));
        monthlyPaymentText.setEnabled(false);
        //set total interest paid
        totalInterestText=(EditText)findViewById(R.id.total_interest_paidText);
        totalInterestText.setText("$" + String.format("%.2f", totalInterest));
        totalInterestText.setEnabled(false);
        //set total property tax paid
        totalPropText=(EditText)findViewById(R.id.total_property_tax_paidText);
        totalPropText.setText("$" + String.format("%.2f", totalProp));
        totalPropText.setEnabled(false);
        //set terms


        //set payoff date
        payOffText=(EditText)findViewById(R.id.pay_off_dateText);
        payOffText.setText(getPayOffDate());
        payOffText.setEnabled(false);
    }

   //save values of parameters
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putDouble(HOME_VALUE,homeValue);
        outState.putDouble(DOWN_PAYMENT,downPayment);
        outState.putDouble(CUSTOM_INTEREST_RATE, interestRate);
        outState.putDouble(PROPERTY_TAX, propertyTax);
        outState.putDouble(TERMS, terms);


    }

public void addListenerOnCalculate(){
    calculateBt=(Button)findViewById(R.id.calculate);

    calculateBt.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (validateField(homeValueEditText, downPaymentEditText, interestRateEditText, propertyTaxEditText))
            {
                try{
                    homeValue=Double.parseDouble(homeValueEditText.getText().toString());
                    downPayment=Double.parseDouble(downPaymentEditText.getText().toString());
                    propertyTax=Double.parseDouble(propertyTaxEditText.getText().toString());
                    interestRate=Double.parseDouble(interestRateEditText.getText().toString());
                    terms=Double.parseDouble(termsSpinner.getSelectedItem().toString());
                }
                catch (Exception e)
                {
                    Context context=getApplicationContext();
                    Toast.makeText(context,"Data type confliction",Toast.LENGTH_LONG).show();
                }

                result();
            }

        }
    });

}

    private boolean validateField(EditText homeValue, EditText downPayment, EditText interestRate,
                                  EditText propertyTaxRate){
        //flags for field validation
        boolean hvFilled = false;//home value filled
        boolean dpFilled = false;//down payment filled
        boolean irFilled = false;//interest rate filled
        boolean ptFilled = false;//property tax filled

        if(homeValue.getText().toString().trim().equals("")){
            homeValue.setError("Home value is required!");
        }
        else{
            hvFilled=true;
        }

        if(downPayment.getText().toString().trim().equals("")){
            downPayment.setError("Down payment is required!");
        }
        else{
            dpFilled=true;
        }

        if(interestRate.getText().toString().trim().equals("")){
            interestRate.setError("Interest rate is required!");
        }
        else{
            irFilled=true;
        }

        if(propertyTaxRate.getText().toString().trim().equals("")){
            propertyTaxRate.setError("Property tax is required!");
        }
        else{
            ptFilled=true;
        }
        if(hvFilled==true&&dpFilled==true&&irFilled==true&&ptFilled==true)
            return true;
        else return false;
    }
    public void addListenerOnReset()

    {

        reset=(Button)findViewById(R.id.reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                homeValueEditText.setText("$0.00");
                downPaymentEditText.setText("$0.00");
                interestRateEditText.setText("$0.00");
                propertyTaxEditText.setText("$0.00");
                termsSpinner.setSelection(0);
                monthlyPaymentText.setText("$0.00");
                totalInterestText.setText("$0.00");
                totalPropText.setText("$0.00");
                payOffText.setText("$0.00");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mortgagae_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /** Called when the user clicks the Send button */

}

