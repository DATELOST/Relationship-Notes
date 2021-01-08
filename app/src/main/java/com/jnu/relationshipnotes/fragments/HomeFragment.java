package com.jnu.relationshipnotes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.jnu.relationshipnotes.MainActivity;
import com.jnu.relationshipnotes.R;


public class HomeFragment extends Fragment {
    CalendarView calendarView;
    TextView textView;
    String date;
    public HomeFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        textView=view.findViewById(R.id.text);
        calendarView=view.findViewById(R.id.calenderView);

        textView.setText(date);
        final Intent intent=new Intent(getContext(), MainActivity.class);
        intent.putExtra("date",date);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                //传递日期
                String date=year+"年"+(month+1)+"月"+dayOfMonth+"日";
                callBackValue.SendMessageValue(date);
                textView.setText(date);
            }
        });
        return view;
    }
    //通过回调函数返回日期
    CallBackValue callBackValue;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue =(CallBackValue) getActivity();
        Time t=new Time();
        t.setToNow();
        date=t.year+"年"+(t.month+1)+"月"+t.monthDay+"日";
        callBackValue.SendMessageValue(date);
    }
    public interface CallBackValue{void SendMessageValue(String strValue);}
}