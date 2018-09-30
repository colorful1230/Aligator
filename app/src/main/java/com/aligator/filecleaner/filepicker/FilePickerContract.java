package com.aligator.filecleaner.filepicker;

import com.aligator.filecleaner.BasePresenter;
import com.aligator.filecleaner.BaseView;

import java.io.File;
import java.util.List;

interface FilePickerContract {

    interface Presenter extends BasePresenter {

        void loadFileList(String rootPath);
    }

    interface View extends BaseView<Presenter> {

        void showFileList(List<File> fileList);
    }
}
