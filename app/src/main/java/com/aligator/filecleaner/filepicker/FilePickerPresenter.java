package com.aligator.filecleaner.filepicker;

import android.annotation.SuppressLint;

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

public class FilePickerPresenter implements FilePickerContract.Presenter {

    private FilePickerContract.View mView;

    public FilePickerPresenter(FilePickerContract.View view) {
        this.mView = SafeUtils.checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @SuppressLint("CheckResult")
    @Override
    public void loadFileList(final String rootPath) {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                File file = new File(rootPath);
                String[] files = file.list();
                emitter.onNext(Arrays.asList(files));
            }
        }).subscribeOn(Schedulers.io()).concatMap(new Function<List<String>, ObservableSource<List<File>>>() {
            @Override
            public ObservableSource<List<File>> apply(List<String> strings) throws Exception {
                List<File> fileList = new ArrayList<>();
                for (String name : strings) {
                    File file = new File(rootPath + File.separator + name);
                    if (file != null && file.exists()) {
                        fileList.add(file);
                    }
                }
                return Observable.fromArray(fileList);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<File>>() {
            @Override
            public void accept(List<File> fileList) throws Exception {
                mView.showFileList(fileList);
            }
        });
    }
}
