package com.bee.core.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;

import androidx.core.content.res.ResourcesCompat;

import com.bee.core.R;
import com.bee.core.base.BaseApplication;
import com.bee.core.widget.CustomTypefaceSpan;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public StringUtils() {
    }

    private static Typeface typeface = ResourcesCompat.getFont(BaseApplication.app, R.font.din_condensed_bold);

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null || s.equals("") || s.equals("null");
    }

    public static String getFiltedNullStrs(String... value) {
        StringBuilder sBuilder = new StringBuilder();
        String[] var2 = value;
        int var3 = value.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String child = var2[var4];
            sBuilder.append(getFiltedNullStr(child, ""));
        }

        return sBuilder.toString();
    }

    public static String getFiltedNullStr(String value) {
        return getFiltedNullStr(value, "");
    }

    public static String getFiltedNullStr(String value, String defValue) {
        return isNullOrEmpty(value) ? defValue : value;
    }

    public static final boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();

        for (int i = 0; i < ch.length; ++i) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHaveSpace(String str) {
        boolean isHave = false;
        if (!isNullOrEmpty(str) && str.contains(" ")) {
            isHave = true;
        }

        return isHave;
    }

    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static String sortString(String... items) {
        new StringBuilder();
        String[] var2 = items;
        int var3 = items.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String var10000 = var2[var4];
        }

        return "";
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static SpannableString getNumberToDINTypeface(Context context, String txt) {

        String showString = "";
        if (!isNullOrEmpty(txt)) {
            showString = txt;
        }
        SpannableString spannableString = new SpannableString(showString);

        for (int x = 0; x < showString.length(); x++) {
            if (showString.charAt(x) >= 48 && showString.charAt(x) <= 57) {
                CustomTypefaceSpan styleSpan_B = new CustomTypefaceSpan(null, getTypeface());
                spannableString.setSpan(styleSpan_B, x, x + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    /**
     * 获取连接中的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            return map;
        }
    }

    public static Typeface getTypeface() {
        if (typeface == null) {
            typeface = ResourcesCompat.getFont(BaseApplication.app, R.font.din_condensed_bold);
        }
        return typeface;
    }

    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String secToTimeStr(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            timeStr = "";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = minute + "分" + second + "秒";
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = hour + "时" + minute + "分" + second + "秒";
            }
        }
        return timeStr;
    }

}
