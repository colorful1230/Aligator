package com.aligator.filecleaner.mainpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aligator.filecleaner.OnItemClickListener;
import com.aligator.filecleaner.R;
import com.aligator.filecleaner.adpater.MainListAdapter;
import com.aligator.filecleaner.filepicker.FilePickerActivity;

import java.io.File;
import java.util.List;

public class MainPageFragment extends Fragment implements MainPageContract.View {

    private RecyclerView mFileListRecyclerView;

    private FloatingActionButton mAddFileButton;

    private MainListAdapter mAdapter;

    private MainPageContract.Presenter mPresenter;

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_page_layout, container, false);
        mFileListRecyclerView = rootView.findViewById(R.id.main_page_file_list_recycler_view);
        mAddFileButton = rootView.findViewById(R.id.main_page_add_file_floating_button);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mFileListRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainListAdapter();
        mFileListRecyclerView.setAdapter(mAdapter);
        mPresenter.setAdapter(mAdapter);

        mAddFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rootPath = Environment.getExternalStorageDirectory().getPath();
                Intent intent = new Intent(getActivity(), FilePickerActivity.class);
                intent.putExtra("rootPath", rootPath);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view) {
                Object object = view.getTag();
                if (object instanceof File) {
                    File file = (File) object;
                    Toast.makeText(view.getContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongClick(final View view) {
                new AlertDialog.Builder(view.getContext()).setMessage("delete?")
                        .setCancelable(true).setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mPresenter != null) {
                            Object object = view.getTag();
                            if (object instanceof File) {
                                File file = (File) object;
                                mPresenter.deleteItem(file);
                            }
                        }
                        dialog.dismiss();
                    }
                }).setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        mPresenter.loadData();
    }

    @Override
    public void setPresenter(MainPageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMainList(List<File> fileList) {
        mAdapter.setFileList(fileList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateFileList() {
        mAdapter.notifyDataSetChanged();
    }
}
