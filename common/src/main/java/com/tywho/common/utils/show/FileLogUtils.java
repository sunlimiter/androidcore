package com.tywho.common.utils.show;

import android.util.Log;

import com.tywho.common.utils.CalendarUtils;
import com.tywho.config.CommonConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by limit on 2017/3/29.
 * 日志文件存储类
 */

public class FileLogUtils {
    public static boolean isDebug = CommonConfig.isDebug;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static String PATH = "/tywho/log/FrameLog__";    //log路径
    static String LINE_SEPARATOR = System.getProperty("line.separator"); //等价于"\n\r"，唯一的作用是能装逼
    static int JSON_INDENT = 4;

    private static String printLine(String tag, boolean isTop) {
        String top =
                "╔══════════════════════════════════════════ JSON ═══════════════════════════════════════";
        String bottom =
                "╚═══════════════════════════════════════════════════════════════════════════════════════";
        if (isTop) {
            Log.d(tag, top);
            return top;
        } else {
            Log.d(tag, bottom);
            return bottom;
        }
    }

    /**
     * 打印JSON
     */
    public static void j(String tag, String jsonStr) {
        if (isDebug) {
            String message;
            try {
                if (jsonStr.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    message = jsonObject.toString(JSON_INDENT); //这个是核心方法
                } else if (jsonStr.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    message = jsonArray.toString(JSON_INDENT);
                } else {
                    message = jsonStr;
                }
            } catch (JSONException e) {
                message = jsonStr;
            }

            message = LINE_SEPARATOR + message;
            String temp = "\n" + printLine(tag, true) + "\n";
            String[] lines = message.split(LINE_SEPARATOR);
            for (String line : lines) {
                temp += "║ " + line + "\n";
                Log.d(tag, "║ " + line);
            }
            temp += printLine(tag, false);
            writeLogToFile(tag, temp);
        }
    }

    // 下面四个是默认tag的函数
    public static void i(Object obj, String msg) {
        String TAG = getTag(obj);
        if (isDebug) {
            Log.i(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void d(Object obj, String msg) {
        String TAG = getTag(obj);
        if (isDebug) {
            Log.d(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void e(Object obj, String msg) {
        String TAG = getTag(obj);
        if (isDebug) {
            Log.e(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void v(Object obj, String msg) {
        String TAG = getTag(obj);
        if (isDebug) {
            Log.v(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
            writeLogToFile(TAG, msg);
        }
    }

    /**
     * 获取类名
     */
    private static String getTag(Object object) {
        Class<?> cls = object.getClass();
        String tag = cls.getName();
        String arrays[] = tag.split("\\.");
        tag = arrays[arrays.length - 1];
        return tag;
    }

    /**
     * 返回日志路径
     */
    public static String getLogPath() {
        String path = android.os.Environment.getExternalStorageDirectory().getPath()
                + File.separator
                + PATH
                + CalendarUtils.getData()
                + ".log";

        File log = new File(path);
        if (!log.getParentFile().exists()) {
            log.getParentFile().mkdirs();
        }
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 把日志记录到文件
     */
    private static int writeLogToFile(String tag, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CalendarUtils.getNowDataTime());
        stringBuffer.append("    ");
        stringBuffer.append(tag);
        stringBuffer.append("    ");
        stringBuffer.append(message);
        stringBuffer.append("\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(getLogPath(), true));
            writer.append(stringBuffer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return 0;
    }

    /**
     * 将异常信息转换为字符串
     */
    public static String getExceptionString(Throwable ex) {
        StringBuilder err = new StringBuilder();
        err.append("ExceptionDetailed:\n");
        err.append("====================Exception Info====================\n");
        err.append(ex.toString());
        err.append("\n");
        StackTraceElement[] stack = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : stack) {
            err.append(stackTraceElement.toString()).append("\n");
        }
        Throwable cause = ex.getCause();
        if (cause != null) {
            err.append("【Caused by】: ");
            err.append(cause.toString());
            err.append("\n");
            StackTraceElement[] stackTrace = cause.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                err.append(stackTraceElement.toString()).append("\n");
            }
        }
        err.append("===================================================");
        return err.toString();
    }
}
