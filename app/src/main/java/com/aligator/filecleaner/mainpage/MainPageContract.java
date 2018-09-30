package com.aligator.filecleaner.mainpage;

import com.aligator.filecleaner.BasePresenter;
import com.aligator.filecleaner.BaseView;
import com.aligator.filecleaner.adpater.MainListAdapter;

import java.io.File;
import java.util.List;

public interface MainPageContract {

    interface Presenter extends BasePresenter {

        void loadData();

        void setAdapter(MainListAdapter adapter);

        void deleteItem(File file);
    }

    interface View extends BaseView<Presenter> {

        void showMainList(List<File> fileList);

        void updateFileList();
    }

}
