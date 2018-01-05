package com.tywho.common.utils;

import android.text.TextUtils;

import com.tywho.config.CommonConfig;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by limit on 2017/5/5/0005.
 */

public class StringUtils {
    public static boolean isHttp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        String regex = "^([hH][tT]{2}[pP]|[hH][tT]{2}[pP][sS])://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
        Pattern patt = Pattern. compile(regex );
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
////
//        Pattern pattern = Pattern
//                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
//        return pattern.matcher(url).matches();
    }

    public static String getUrl(String url) {
        if (isHttp(url)) return url;
        return CommonConfig.API_URL + url;
    }

    public static boolean isPhone(String phone) {
        String all = "[1][0-9]\\d{9}";
        boolean flag;// 存储匹配结果
        // 判断手机号码是否是11位
        if (phone.length() == 11) {

            if (phone.matches(all)) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    public static String formatPhoneSpace(String textWithSpace) {
        return getFormatedText(textWithSpace, 1);//trim掉可能是手机号的空格
    }

    public static String formatCardSpace(String textWithSpace) {
        return getFormatedText(textWithSpace, 2);//trim掉可能是手机号的空格
    }

    public static String getFormatedText(String textWithSpace, int inputType) {
        if (isEmpty(textWithSpace)) return null;
        textWithSpace = textWithSpace.replace(" ", "");
        if (inputType == 1) {
            //手机号，在最前面加一个空格
            textWithSpace = " " + textWithSpace;
        }
        String arrs[] = textWithSpace.split("");
        int len = arrs.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < len; i++) {
            sb.append(arrs[i]);
            if (i % 4 == 0) {
                sb.append(" ");
            }
        }
        if (len % 4 == 1) {
            sb.append(" ");
        }
        return sb.toString().trim();//trim掉可能是手机号的空格
    }

    public static String formatStr(int number) {
        return number + "";
    }

    public static String formatStr(double number) {
        return number + "";
    }

    public static String formatStr(long number) {
        return number + "";
    }

    public static String formatStr(String num) {
        return num.replaceAll(" ", "");
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个对象是否为空；
     */
    public final static boolean isEmpty(Object o) {
        return (o == null);
    }

    public final static boolean isEmpty(String[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(int[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(StringBuffer sb) {
        if (sb == null || sb.length() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(List list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(Set set) {
        if (set == null || set.size() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(Map map) {
        if (map == null || map.size() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(byte[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    /**
     * 判断指定数据是否存在于指定的数组中；
     */
    public final static boolean isContain(String[] array, String value) {
        if (isEmpty(array) || isEmpty(value))
            return false;

        int size = size(array);
        for (int i = 0; i < size; i++) {
            if (isSame(array[i], value))
                return true;
        }

        return false;
    }

    public final static boolean isContain(String content, String value) {
        if (isEmpty(content) || isEmpty(value))
            return false;

        return (content.indexOf(value) != -1);
    }

    public final static boolean isContain(List list, Object object) {
        if (isEmpty(list))
            return false;

        return list.contains(object);
    }

    /**
     * 获取指定集合的大小；
     */
    public final static int size(List list) {
        if (isEmpty(list))
            return 0;
        else
            return list.size();
    }

    public final static int size(Map map) {
        if (isEmpty(map))
            return 0;
        else
            return map.size();
    }

    public final static int size(String[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    public final static int size(Object[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    /**
     * 检测两个字符串是否相同；
     */
    public final static boolean isSame(String value1, String value2) {
        if (isEmpty(value1) && isEmpty(value2)) {
            return true;
        } else if (!isEmpty(value1) && !isEmpty(value2)) {
            return (value1.trim().equalsIgnoreCase(value2.trim()));
        } else {
            return false;
        }
    }

    public static String formatDate(Date dateDate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static long formatDate(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time+=" 00:00:00";
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (Exception e) {
            return new Date().getTime();
        }
    }

    public static String formatDate(Date dateDate) {
        return formatDate(dateDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date formatDate(Long time) {
        return formatDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date formatDate(Long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static long twoDateDistance(Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            return -1;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
//        if (timeLong < 60 * 1000)
//            return timeLong / 1000 + "秒前";
//        else if (timeLong < 60 * 60 * 1000) {
//            timeLong = timeLong / 1000 / 60;
//            return timeLong + "分钟前";
//        } else if (timeLong < 60 * 60 * 24 * 1000) {
//            timeLong = timeLong / 60 / 60 / 1000;
//            return timeLong + "小时前";
//        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
//            timeLong = timeLong / 1000 / 60 / 60 / 24;
//            return timeLong + "天前";
//        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
//            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
//            return timeLong + "周前";
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//            return sdf.format(startDate);
//        }
        timeLong = timeLong / 60 / 60 / 1000;
        return timeLong;
    }

    /**
     * 时间差 毫秒
     *
     * @param startDate "2008-01-02 11:30:24"
     * @param endDate   "2008-03-26 13:31:40"
     * @return 秒
     */
    public static long compareTime(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        return (endDate.getTime() - startDate.getTime()) / 1000;//除以1000是为了转换成秒
    }

    /**
     * 时间差 天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int compareDateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        return (int) timeLong / 1000 / 60 / 60 / 24;
    }

    public static int parseInt(String number) {
        if (isEmpty(number)) return -1;
        int result;
        try {
            result = Integer.parseInt(number);
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

    public static double parseDouble(String number) {
        if (isEmpty(number)) return -1;
        double result;
        try {
            result = Double.parseDouble(number);
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

    public static List<Integer> skipIds(Integer... ids) {
        return Arrays.asList(ids);
    }

    // 将手机号码第4位到第7位替换成*
    public static String phoneFormat(String phone) {
        if (isEmpty(phone)) return null;
        // 括号表示组，被替换的部分$n表示第n组的内容
        String tel = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return tel;
    }
}
