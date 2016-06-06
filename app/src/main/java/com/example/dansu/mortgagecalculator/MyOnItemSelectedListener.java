package com.example.dansu.mortgagecalculator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class MyOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
          String spinner=parent.getItemAtPosition(pos).toString();
            int terms=Integer.parseInt(spinner);

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }


    }

