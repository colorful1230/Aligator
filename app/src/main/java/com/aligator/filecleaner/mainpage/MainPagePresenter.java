package com.aligator.filecleaner.mainpage;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.aligator.filecleaner.adpater.MainListAdapter;
import com.aligator.filecleaner.utils.SafeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainPagePresenter implements MainPageContract.Presenter {

    private MainPageContract.View mView;

    private MainListAdapter mMainListAdapter;

    public MainPagePresenter(MainPageContract.View view) {
        this.mView = SafeUtils.checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadData();
    }

    @Override
    public void setAdapter(MainListAdapter adapter) {
        this.mMainListAdapter = adapter;
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        final File rootDir = Environment.getExternalStorageDirectory();
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {

                if (rootDir == null || !rootDir.exists()) {
                    emitter.onComplete();
                    return;
                }
                String[] files = rootDir.list();
                List<String> fileList = Arrays.asList(files);
                Arrays.sort(files);
                emitter.onNext(fileList);
            }
        }).subscribeOn(Schedulers.io()).concatMap(new Function<List<String>, ObservableSource<List<File>>>() {

            @Override
            public ObservableSource<List<File>> apply(List<String> strings) throws Exception {
                List<File> fileList = new ArrayList<>();
                for (String fileName : strings) {
                    File file = new File(rootDir + File.separator + fileName);
                    if (file == null || !file.exists()) {
                        continue;
                    }
                    fileList.add(file);
                }
                return Observable.fromArray(fileList);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<File>>() {

            @Override
            public void accept(List<File> fileList) throws Exception {
                if (mView != null) {
                    mView.showMainList(fileList);
                }
            }
        });
    }

    @Override
    public void deleteItem(File file) {
        if (mMainListAdapter == null) {
            return;
        }
        if (mMainListAdapter.removeItem(file)) {
            if (mView == null) {
                return;
            }
            mView.updateFileList();
        }
    }
}
