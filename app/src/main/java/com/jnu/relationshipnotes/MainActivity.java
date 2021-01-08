package com.jnu.relationshipnotes;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.relationshipnotes.dataprocesser.DataBank;
import com.jnu.relationshipnotes.fragments.GiftInFragment;
import com.jnu.relationshipnotes.fragments.GiftOutFragment;
import com.jnu.relationshipnotes.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.CallBackValue {
    FloatingActionButton fab;       //悬浮球
    FloatingActionButton in_gift;   //收礼
    FloatingActionButton out_gift;  //随礼
    Toolbar toolbar;                //菜单栏
    BottomNavigationView mBottomNavigationView;//底部导航栏
    FragmentTransaction mTransaction;
    FragmentManager mFragmentManager;
    HomeFragment mHomeFragment;
    GiftInFragment giftInFragment;
    GiftOutFragment giftOutFragment;
    boolean fabVisible = true;      //判断悬浮球状态
    TextView textView;
    String date;        //日期
    DataBank dataBank;  //存储数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setListener();
    }
    void init(){
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment  = new HomeFragment();
        giftInFragment = new GiftInFragment();
        giftOutFragment= new GiftOutFragment();
        switchFragment(mHomeFragment);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        View layout = factory.inflate(R.layout.fragment_home,null);


        fab = findViewById(R.id.fab);
        in_gift = findViewById(R.id.in_gift);
        out_gift = findViewById(R.id.out_gift);
        toolbar = findViewById(R.id.toolbar);
        mBottomNavigationView = findViewById(R.id.nav_view);
        textView=layout.findViewById(R.id.text);

        setSupportActionBar(toolbar);
        mBottomNavigationView.setSelectedItemId(R.id.homef);
        dataBank=new DataBank(this);
        dataBank.Load(0);
        dataBank.Load(1);
    }
    void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabVisible) openFab();
                else closeFab();
            }
        });
        in_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"记录收礼",Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(MainActivity.this,
                        GiftAddInActivity.class);
                mainIntent.putExtra("date",date);
                startActivity(mainIntent);
                finish();
            }
        });
        out_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"记录随礼",Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(MainActivity.this,
                        GiftAddOutActivity.class);
                mainIntent.putExtra("date",date);
                startActivity(mainIntent);
                finish();
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.homef:
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.in:
                        switchFragment(giftInFragment);
                        break;
                    case R.id.out:
                        switchFragment(giftOutFragment);
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void SendMessageValue(String strValue) { date=strValue; }
    private Fragment lastFragment=null;
    private void switchFragment(Fragment homeFragment) {
        mTransaction = mFragmentManager.beginTransaction();
        if (!homeFragment.isAdded()) mTransaction.add(R.id.nav_host_fragment,homeFragment);
        else mTransaction.show(homeFragment);
        if (lastFragment !=null) mTransaction.hide(lastFragment);
        lastFragment =homeFragment;
        mTransaction.commit();
    }
    void openFab() {
        fabVisible = false;
        Animation animation=new RotateAnimation(0,45,84,84);
        animation.setDuration(500);
        animation.setFillAfter(true);
        fab.startAnimation(animation);
        int s=fab.getTop(),e=s*2/3,ee=e+e/4;
        doValueAnimation(s,e,in_gift);
        doValueAnimation(s,ee,out_gift);
    }
    void closeFab() {
        fabVisible = true;
        Animation animation=new RotateAnimation(45,0,84,84);
        animation.setDuration(500);
        animation.setFillAfter(true);
        fab.startAnimation(animation);
        int s=fab.getTop(),e=s*2/3,ee=e+e/4;
        doValueAnimation(e,s,in_gift);
        doValueAnimation(ee,s,out_gift);
    }
    void doValueAnimation(final int s,final int e,final FloatingActionButton fa){
        ValueAnimator animator = ValueAnimator.ofInt(s,e);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                fa.layout(fa.getLeft(),curValue,fa.getRight(),curValue+fa.getHeight());
            }
        });
        animator.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    static int cnt=1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_f:
                if(cnt==1) openFab();
                else closeFab();
                cnt^=1;
                break;
            case R.id.action_sum:
                dataBank.Load(0);
                dataBank.Load(1);
                String str="收礼总额: "+dataBank.getGiftSum()+"\n随礼总额: "+dataBank.getGiftOutSum();
                Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_histogram:
                calc_Gift(0);
                break;
            case R.id.action_histogram_out:
                calc_Gift(1);
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    void calc_Gift(final int op){
        Intent mainIntent = new Intent(MainActivity.this,
                HistogramActivity.class);
        mainIntent.putExtra("op",op);
        startActivity(mainIntent);
        finish();
    }
}