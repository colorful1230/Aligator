package com.aligator.filecleaner.filepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aligator.filecleaner.R;
import com.aligator.filecleaner.utils.ActivityUtils;

public class FilePickerActivity extends BaseFilePickerActivity {

    private FilePickerContract.Presenter mPresenter;

    private FilePickerContract.View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker_layout);

        FilePickerFragment filePickerFragment = FilePickerFragment.newInstance();

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                filePickerFragment, R.id.file_picker_content_frame_layout);

        mPresenter = new FilePickerPresenter(filePickerFragment);
    }

}
