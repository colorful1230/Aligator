package com.aligator.filecleaner.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aligator.filecleaner.OnItemClickListener;
import com.aligator.filecleaner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePickerAdapter extends RecyclerView.Adapter<FilePickerAdapter.FileItemViewHolder> {

    private List<File> mDataList;

    private OnItemClickListener mOnItemClickListener;

    private boolean mItemClickable = true;

    public FilePickerAdapter() {
        this.mDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FileItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_picker_item_layout, parent, false);
        return new FileItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FileItemViewHolder holder, int position) {
        File file = mDataList.get(position);
        holder.itemView.setTag(file);
        holder.fileNameTextView.setText(file.getName());
        holder.fileSelectCheckBox.setChecked(holder.isSelected);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null && mItemClickable) {
                    mOnItemClickListener.onClick(v);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onLongClick(v);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void setData(List<File> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setItemClickable(boolean clickable) {
        this.mItemClickable = clickable;
    }

    static class FileItemViewHolder extends RecyclerView.ViewHolder {

        TextView fileNameTextView;

        CheckBox fileSelectCheckBox;

        boolean isSelected = false;

        FileItemViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.file_picker_item_title_text_view);
            fileSelectCheckBox = itemView.findViewById(R.id.file_picker_item_title_check_box);

            fileSelectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isSelected = isChecked;
                }
            });
        }
    }
}
