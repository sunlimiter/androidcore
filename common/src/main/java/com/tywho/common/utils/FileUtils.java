package com.tywho.common.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.tywho.common.http.download.bean.DownInfoBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * Created by limit on 2017/3/30.
 */

public class FileUtils {
    /**
     * 写入文件
     *
     * @param file
     * @param info
     * @throws IOException
     */
    public static void writeCache(ResponseBody responseBody, File file, DownInfoBean info) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        long allLength;
        if (info.getCountLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getCountLength();
        }
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "rwd");
        channelOut = randomAccessFile.getChannel();
        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        if (channelOut != null) {
            channelOut.close();
        }
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    /**
     * 创建文件
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SD是否可用
     *
     * @return
     */
    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取文件的Content uri路径
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 图片保存到内部存储
     */
    public static void saveImageToApp(Context context, Bitmap bmp, String filename) throws Exception {
        File directory = context.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, filename);
        FileOutputStream fos = null;
        fos = new FileOutputStream(mypath);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
    }

    public static Bitmap getImageToApp(Context context, String filename) throws FileNotFoundException {
        Bitmap bitmap = null;
        File path1 = context.getDir("profile", Context.MODE_PRIVATE);
        File file = new File(path1, filename);
        if (file.exists()) {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        return bitmap;
    }


    public static void deleteImageToApp(Context context) {
        File path = context.getDir("profile", Context.MODE_PRIVATE);
        if (path.exists()) {
            path.delete();
        }
    }


    public static String getAppStartImagePath(Context context, String fileName) {
        File path = context.getDir("profile", Context.MODE_PRIVATE);
        File file = new File(path, fileName);
        return file.getAbsolutePath();
    }
}
