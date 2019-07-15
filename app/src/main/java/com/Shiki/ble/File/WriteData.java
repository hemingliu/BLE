package com.Shiki.ble.File;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;

public class WriteData{
    public void writeToFile(String a){
        try {
            Log.d("File",Environment.getExternalStorageDirectory().getPath());
            String Filepath = Environment.getExternalStorageDirectory().getPath()+"/BLE/";
            String FileName = "log.txt";

            File dictionaryFile = new File(Filepath);//这是目录路径
            if (!dictionaryFile.exists())
            {
                dictionaryFile.mkdirs();
            }

            File textFile = new File(Filepath + FileName);
            if (!textFile.exists())//如果不存在该目录
            {
                textFile.createNewFile();//这个方法是创建文件的方法
            }
            RandomAccessFile raf = new RandomAccessFile(textFile, "rwd");
            raf.seek(textFile.length());
            raf.writeUTF(a);
            raf.close();
        }
        catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
}
