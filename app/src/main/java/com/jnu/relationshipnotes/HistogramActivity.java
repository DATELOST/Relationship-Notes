package com.jnu.relationshipnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;

import com.jnu.relationshipnotes.dataprocesser.DataBank;
import com.jnu.relationshipnotes.dataprocesser.Gift;
import com.jnu.relationshipnotes.views.HistogramView;

import java.util.ArrayList;

public class HistogramActivity extends AppCompatActivity {
    HistogramView histogramView;
    DataBank dataBank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent=new Intent(HistogramActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        dataBank=new DataBank(this);
        histogramView=findViewById(R.id.histogram);
        Intent intent=getIntent();
        int op=intent.getIntExtra("op",0);
        if(op==0){
            dataBank.Load(0);
            calc(dataBank.getGifts());
        }
        else {
            dataBank.Load(1);
            calc(dataBank.getGiftsOut());
        }
    }
    void calc(ArrayList<Gift> gifts){
        Time t = new Time();
        t.setToNow();
        int[] mon=new int[13];
        for(int i=0;i<13;++i)mon[i]=0;
        try{
            for(int i=0;i<gifts.size();++i){
                String date = gifts.get(i).getDate();
                int l=0,r=1;
                for(;r<date.length();++r){
                    if(date.charAt(r)=='年'){
                        if(t.year!=Integer.parseInt(date.substring(l,r)))break;
                        l=r+1;
                    }
                    if(date.charAt(r)=='月'){
                        mon[Integer.parseInt(date.substring(l,r))]+=gifts.get(i).getMoney();
                        break;
                    }
                }
            }
        }catch(NumberFormatException e){}
        ArrayList<HistogramView.Data>data = new ArrayList<>();
        for(int i=1;i<13;++i)data.add(new HistogramView.Data(mon[i],i+"月"));
        histogramView.setData(data);
    }
}