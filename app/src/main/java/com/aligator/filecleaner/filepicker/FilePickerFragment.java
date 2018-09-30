package com.aligator.filecleaner.filepicker;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aligator.filecleaner.OnItemClickListener;
import com.aligator.filecleaner.R;
import com.aligator.filecleaner.adpater.FilePickerAdapter;

import java.io.File;
import java.util.List;

public class FilePickerFragment extends Fragment implements FilePickerContract.View {

    private RecyclerView mFileListRecyclerView;

    private FilePickerContract.Presenter mPresenter;

    private FilePickerAdapter mAdapter;

    private boolean mEdit;

    public static FilePickerFragment newInstance() {
        return new FilePickerFragment();
    }

    @Override
    public void setPresenter(FilePickerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View rootView = inflater.inflate(R.layout.fragment_file_picker_layout, container, false);
        mFileListRecyclerView = rootView.findViewById(R.id.file_picker_recycler_view);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mFileListRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FilePickerAdapter();
        mFileListRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnFileClickListener());
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = activity.getIntent();
        if (intent == null) {
            return;
        }

        String rootPath = intent.getStringExtra("rootPath");
        mPresenter.loadFileList(rootPath);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            File file = new File(rootPath);
            if (file == null || !file.exists() || !file.isDirectory()) {
                return;
            }
            actionBar.setTitle(file.getName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFileList(List<File> fileList) {
        mAdapter.setData(fileList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.file_picker_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.file_picker_menu_clean:
                break;
            case R.id.file_picker_menu_complete:
                Activity activity = getActivity();
                if (activity != null) {
                    getActivity().finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private class OnFileClickListener implements OnItemClickListener {

        @Override
        public void onClick(View view) {
            Object object = view.getTag();
            if (object instanceof File) {
                File file = (File) object;
                if (!file.isDirectory()) {
                    return;
                }
                String filePath = file.getAbsolutePath();
                Intent intent = new Intent(getActivity(), FilePickerActivity.class);
                intent.putExtra("rootPath", filePath);
                startActivity(intent);
            }
        }

        @Override
        public void onLongClick(View view) {
            if (mEdit) {
                setHasOptionsMenu(false);
            } else {
                mEdit = true;
                setHasOptionsMenu(true);
                mAdapter.setItemClickable(false);
                int count = mFileListRecyclerView.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = mFileListRecyclerView.getChildAt(i);
                    View checkbox = child.findViewById(R.id.file_picker_item_title_check_box);
                    checkbox.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
