package com.aligator.filecleaner.mainpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aligator.filecleaner.R;
import com.aligator.filecleaner.utils.ActivityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainPageFragment fragment = MainPageFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                fragment, R.id.main_page_content_frame_layout);

        MainPagePresenter presenter = new MainPagePresenter(fragment);
    }
}
