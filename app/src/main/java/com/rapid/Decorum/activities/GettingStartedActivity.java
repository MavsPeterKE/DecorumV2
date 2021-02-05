package com.rapid.Decorum.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rapid.Decorum.R;
import com.rapid.Decorum.adapter.SliderPagerAdapter;
import com.rapid.Decorum.appConstants.AppConstants;
import com.rapid.Decorum.preferencehelper.PreferenceHelper;

public class GettingStartedActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button button;
    private SliderPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // making activity full screen
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_getting_started);
        // hide action bar you can use NoAction theme as well
        getSupportActionBar().hide();
        // bind views
        viewPager = findViewById(R.id.pagerIntroSlider);
        TabLayout tabLayout = findViewById(R.id.tabs);
        button = findViewById(R.id.button);

        // init slider pager adapter
        adapter = new SliderPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // set adapter
        viewPager.setAdapter(adapter);

        // set dot indicators
        tabLayout.setupWithViewPager(viewPager);

        // make status bar transparent
        changeStatusBarColor();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItemCount = viewPager.getCurrentItem();
                if (currentItemCount < adapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    Log.e("onClick: ", currentItemCount + "");
                    if (currentItemCount == 3) {
                        startLoginActivity();
                    }
                }
            }
        });

        /**
         * Add a listener that will be invoked whenever the page changes
         * or is incrementally scrolled
         */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getCount() - 1) {
                    button.setText(R.string.get_started);
                    startLoginActivity();
                } else {
                    button.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void startLoginActivity() {
        if (ContextCompat.checkSelfPermission(GettingStartedActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startDeco();
        } else {
            ActivityCompat.requestPermissions(GettingStartedActivity.this, new String[]{Manifest.permission.CAMERA}, 11);
        }

    }


    private void startDeco() {
        if (new PreferenceHelper(GettingStartedActivity.this).getData(AppConstants.Userid).equalsIgnoreCase("")) {

            startActivity(new Intent(GettingStartedActivity.this, LoginActivity.class));
        } else {

            startActivity(new Intent(GettingStartedActivity.this, MainActivity.class));
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(GettingStartedActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startDeco();
        } else {

            ActivityCompat.requestPermissions(GettingStartedActivity.this, new String[]{Manifest.permission.CAMERA}, 11);
        }
    }
}
