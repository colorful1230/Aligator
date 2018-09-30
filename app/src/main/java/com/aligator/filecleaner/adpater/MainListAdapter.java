package com.aligator.filecleaner.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aligator.filecleaner.OnItemClickListener;
import com.aligator.filecleaner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.FileListViewHolder> {

    private List<File> mFileList;

    private OnItemClickListener mItemClickListener;

    public MainListAdapter() {
        mFileList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_file_list_item_layout, parent, false);
        return new FileListViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileListViewHolder holder, int position) {
        File file = mFileList.get(position);
        if (file == null) {
            return;
        }

        holder.fileNameTextView.setText(file.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(v);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onLongClick(v);
                }
                return true;
            }
        });
        holder.itemView.setTag(file);
    }

    @Override
    public int getItemCount() {
        return mFileList == null ? 0 : mFileList.size();
    }

    public void setFileList(@NonNull List<File> fileList) {
        this.mFileList.clear();
        this.mFileList.addAll(fileList);
    }

    public boolean removeItem(File file) {
        if (mFileList == null) {
            return false;
        }

        return mFileList.remove(file);
    }

    public void setOnItemClickListener(@NonNull OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    static class FileListViewHolder extends RecyclerView.ViewHolder {

        TextView fileNameTextView;

        public FileListViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.main_file_list_item_layout);
        }
    }
}
