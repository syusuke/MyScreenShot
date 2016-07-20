package com.gson8.myscreenshot.fileselector.dialog;

/*
 * MyScreenShot making by Syusuke/琴声悠扬 on 2016/6/7
 * E-Mail: Zyj7810@126.com
 * Package: com.gson8.myscreenshot.fileselector.dialog.FileDialog
 * Description: null
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gson8.myscreenshot.R;
import com.gson8.myscreenshot.fileselector.config.FileConfig;
import com.gson8.myscreenshot.fileselector.utils.FileSelectorConfig;
import com.gson8.myscreenshot.fileselector.utils.FileType;
import com.gson8.myscreenshot.fileselector.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileDialog {

    Context mContext;
    FileConfig mFileConfig;

    AlertDialog mDialog;
    AlertDialog.Builder builder;
    ListView listView;
    SelectFileDialogAdapter adapter;
    List<HashMap<String, Object>> mFiles;

    int prePosition = 0;

    OnFileSelectFinish mOnFileSelectFinish;

    public FileDialog(Context mContext, FileConfig mFileConfig) {
        this.mContext = mContext;
        this.mFileConfig = mFileConfig;

        builder = new AlertDialog.Builder(mContext);
        builder.setTitle("选择文件");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dimissDialog();
            }
        });
        listView = new ListView(mContext);
        mFiles = new ArrayList<>();
        adapter = new SelectFileDialogAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listviewItemClickListener);
        updateListView(FileConfig.startPath);
    }


    AdapterView.OnItemClickListener listviewItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String path = (String) mFiles.get(position).get(FileSelectorConfig.PATH);
                    if(path == null)
                        return;
                    int type = (int) mFiles.get(position).get(FileSelectorConfig.TYPE);
                    switch(type) {
                        case FileType.FOLDER:
                            updateListView(path);
                            listView.setSelection(0);
                            prePosition = position;
                            break;
                        case FileType.FILE:
                            if(mOnFileSelectFinish != null) {
                                mOnFileSelectFinish.onFinishSelected(path);
                            }
                            dimissDialog();
                            break;
                        case FileType.UPTO:
                            String parentPath = FileUtils.getParentPath(path);
                            updateListView(parentPath);
                            listView.setSelection(prePosition - 3);
                            break;
                    }
                }
            };

    private void updateListView(String path) {
        ArrayList<HashMap<String, Object>> temp = null;
        try {
            temp = FileUtils.getFileList(path);
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "打开失败", Toast.LENGTH_SHORT).show();
            return;
        }
        mFiles.clear();
        mFiles.addAll(temp);

        adapter.notifyDataSetChanged();
        //fileShowListView.setSelection(0);
    }

    public void showDialog() {

        mDialog = builder.create();
        mDialog.show();
    }

    public void dimissDialog() {
        if(mDialog == null)
            throw new RuntimeException("没有初始化");
        mDialog.dismiss();
    }

    class SelectFileDialogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.file_selector_dialog_item, parent, false);

                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.file_selector_item_icon);
                holder.text = (TextView) convertView.findViewById(R.id.file_selector_item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String name = mFiles.get(position).get(FileSelectorConfig.NAME).toString();
            int icon = (int) mFiles.get(position).get(FileSelectorConfig.ICON);

            if(name != null) {
                holder.text.setText(name);
            }

            if(icon != 0) {
                holder.icon.setImageResource(icon);
            }

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView text;
        }
    }


    public void setOnFileSelectFinish(OnFileSelectFinish mOnFileSelectFinish) {
        this.mOnFileSelectFinish = mOnFileSelectFinish;
    }

    public interface OnFileSelectFinish {
        void onFinishSelected(String path);
    }

}
