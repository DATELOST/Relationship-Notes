package com.jnu.relationshipnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jnu.relationshipnotes.dataprocesser.DataBank;
import com.jnu.relationshipnotes.dataprocesser.Gift;

public class GiftAddInActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name;
    EditText reason;
    EditText money;
    private DataBank dataBank;      //存储数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_in);
        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name);
        reason = findViewById(R.id.reason);
        money = findViewById(R.id.money);
        Button buttonOk = findViewById(R.id.save);
        Button buttonReturn = findViewById(R.id.cancel);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("收礼");
        Intent intent=getIntent();
        final String date=intent.getStringExtra("date");
        dataBank=new DataBank(this);
        dataBank.Load(0);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer mon=0;
                Boolean flag=false;
                try{
                    mon=Integer.parseInt(money.getText().toString());
                    flag=true;
                }catch (NumberFormatException e){
                    Toast.makeText(getApplication(),"输入金额错误",Toast.LENGTH_SHORT).show();
                }
                if(mon<0)flag=false;
                if(flag){
                    dataBank.getGifts().add(new Gift(name.getText().toString(), date,
                            reason.getText().toString(), mon));
                    dataBank.Save(0);
                    finished();
                }
            }
        });
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finished();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_gift, menu);
        return true;
    }
    void finished(){
        Intent mainIntent = new Intent(getApplication(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}