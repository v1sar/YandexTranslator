package com.v1sar.yandextranslator;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.v1sar.yandextranslator.Adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Translate"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        txtTranslated = (TextView) findViewById(R.id.txt_translated);
//        btnTranslate = (Button) findViewById(R.id.btn_translate);
//        txtEdit = (EditText) findViewById(R.id.text_to_translate);
//        leftSpinner = (AppCompatSpinner) findViewById(R.id.left_spinner);
//        rightSpinner = (AppCompatSpinner) findViewById(R.id.right_spinner);
//        btnTranslate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                translate();
//            }
//        });
//    }
//
//    private void translate() {
//        ApiService api = RetroClient.getApiService();
//        Call<Answer> call = api.getMyJSON(API_KEY, txtEdit.getText().toString(),
//                LanguageConverter.getInstance().convert(leftSpinner.getSelectedItem().toString())+"-"+
//                        LanguageConverter.getInstance().convert(rightSpinner.getSelectedItem().toString()));
//        call.enqueue(new Callback<Answer>() {
//            @Override
//            public void onResponse(Call<Answer> call, Response<Answer> response) {
//                if(response.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "GREAT", Toast.LENGTH_SHORT).show();
//                    txtTranslated.setText(response.body().getText()[0]);
//                } else {
//                    Toast.makeText(MainActivity.this, "NOT GREAT", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<Answer> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
