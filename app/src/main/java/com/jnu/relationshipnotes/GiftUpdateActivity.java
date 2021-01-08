package com.jnu.relationshipnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GiftUpdateActivity extends AppCompatActivity {
    private int position;
    EditText name;
    EditText reason;
    EditText money;
    EditText date;
    String Name;
    String Reason;
    String Money;
    String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        position =getIntent().getIntExtra("position",0);
        Name     =getIntent().getStringExtra("name");
        Reason   =getIntent().getStringExtra("reason");
        Date     =getIntent().getStringExtra("date");
        Money    =getIntent().getStringExtra("money");

        name   = findViewById(R.id.name);
        reason = findViewById(R.id.reason);
        money  = findViewById(R.id.money);
        date   = findViewById(R.id.date);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("修改");

        if(null!=Name)  name.setText(Name);
        if(null!=Reason)reason.setText(Reason);
        if(null!=Money) money.setText(Money+"");
        if(null!=date)  date.setText(Date);

        Button buttonOk = (Button)findViewById(R.id.save);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("reason",reason.getText().toString());
                intent.putExtra("money",money.getText().toString());
                intent.putExtra("date",date.getText().toString());
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Button buttonCancel = (Button)findViewById(R.id.cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {finish();}
        });
    }
}
