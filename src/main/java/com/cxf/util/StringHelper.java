package com.cxf.util;

import java.net.URLEncoder;
import java.util.TreeMap;

import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

/**
 * 
 * @author cxf
 * 
 */
public class StringHelper {

    /**
     * HTML到STR的编码转换表
     */
    public static final TreeMap<String, String> HTML_TO_STR_TranMap = getHTML_TO_STR_TranMap();

    /**
     * STR到HTML的编码转换表
     */
    public static final TreeMap<String, String> STR_TO_HTML_TranMap = getSTR_TO_HTML_TranMap();

    /**
     * SBC到DBC的编码转换表
     */
    public static final TreeMap<String, String> SBC_TO_DBC_TranMap = getSBC_TO_DBC_TranMap();

    public final static String DY = "'";

    public static boolean isEmpty(String str) {
        if (str == null || str == "")
            return true;
        else
            return false;
    }

    public static boolean isNotEmpty(String str) {
        if (str == null || str == "")
            return false;
        else
            return true;
    }

    /**
     * 　* 把字符串转成utf8编码，保证中文文件名不会乱码
     */
    public static String toUtf8String(String s) {
        try {
            return URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            return s;
        }
    }

    /** 判断字符串是否为空或者内容为空 */
    public static boolean nullOrBlank(String string) {
        if (string == null)
            return true;
        if (string.trim().length() <= 0) {
            return true;
        }
        if ("null".equalsIgnoreCase(string)) {
            return true;
        }
        return false;
    }

    /** 判断字符串是否为空或者内容为空 */
    public static boolean nullOrBlankWithNoTrim(String string) {
        if (string == null)
            return true;
        if (string.equals("")) {
            return true;
        }
        if ("null".equalsIgnoreCase(string)) {
            return true;
        }
        return false;
    }

    private static TreeMap<String, String> getSTR_TO_HTML_TranMap() {
        TreeMap<String, String> result = new TreeMap<String, String>();
        result.put("\r\n", "<br>&nbsp;&nbsp;");
        result.put("\t", "<br>&nbsp;&nbsp;");
        result.put("\r", "<br>");
        result.put("\n", "<br>");
        result.put(" ", "&nbsp;");
        return result;
    }

    private static TreeMap<String, String> getHTML_TO_STR_TranMap() {
        TreeMap<String, String> result = new TreeMap<String, String>();
        result.put("<br>&nbsp;&nbsp;", "\t");
        result.put("<br>", "\n");
        result.put("&nbsp;", " ");
        return result;
    }

    private static TreeMap<String, String> getSBC_TO_DBC_TranMap() {
        TreeMap<String, String> result = new TreeMap<String, String>();
        result.put("０", "0");
        result.put("１", "1");
        result.put("２", "2");
        result.put("３", "3");
        result.put("４", "4");
        result.put("５", "5");
        result.put("６", "6");
        result.put("７", "7");
        result.put("８", "8");
        result.put("９", "9");
        return result;
    }

    /**
     * 更替当前str值
     */
    public static String getHtmlToStr(String src) {
        String result = nullToEmpty(src);
        if (nullOrBlank(result)) {
            return result;
        }
        for (String key : HTML_TO_STR_TranMap.keySet()) {
            result = replace(result, key, HTML_TO_STR_TranMap.get(key));
        }
        return result;
    }

    /**
     * 更替当前str值
     */
    public static String getStrToHtml(String src) {
        String result = nullToEmpty(src);
        if (nullOrBlank(result)) {
            return result;
        }
        for (String key : STR_TO_HTML_TranMap.keySet()) {
            result = replace(result, key, STR_TO_HTML_TranMap.get(key));
        }
        return result;
    }

    /**
     * 删改所有特殊字符
     */
    public static String getNoQuitStr(String src) {
        String result = nullToEmpty(src);
        if (nullOrBlank(result)) {
            return result;
        }
        for (String key : STR_TO_HTML_TranMap.keySet()) {
            result = replace(result, key, "");
        }
        for (String key : HTML_TO_STR_TranMap.keySet()) {
            result = replace(result, key, "");
        }
        return result;
    }

    /** 扩展的字符串替换 */
    public static String replace(String src, String from, String to) {
        int l = 0;
        if (nullOrBlankWithNoTrim(src)) {
            return src;
        }
        if (nullOrBlankWithNoTrim(from)) {
            return src;
        }
        if (nullOrBlankWithNoTrim(to)) {
            to = "";
        }
        String gRtnStr = src;
        while (true) {
            l = src.indexOf(from, l);
            if (l == -1)
                break;
            gRtnStr = src.substring(0, l) + to + src.substring(l + from.length());
            l += to.length();
            src = gRtnStr;
        }
        return gRtnStr.substring(0, gRtnStr.length());
    }

    /** 扩展的字符串替换 */
    public static String replacePath(String src, String from, String to) {
        try {
            src = java.net.URLDecoder.decode(src, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int l = 0;
        if (nullOrBlankWithNoTrim(src)) {
            return src;
        }
        if (nullOrBlankWithNoTrim(from)) {
            return src;
        }
        if (nullOrBlankWithNoTrim(to)) {
            to = "";
        }
        String gRtnStr = src;
        while (true) {
            l = src.indexOf(from, l);
            if (l == -1)
                break;
            gRtnStr = src.substring(0, l) + to + src.substring(l + from.length());
            l += to.length();
            src = gRtnStr;
        }
        return gRtnStr.substring(0, gRtnStr.length());
    }

    /**
     * 格式化对象
     */
    public static String formatObject(Object obj, String[] displayNames) throws Exception {
        String result = "";
        if (obj == null || displayNames == null || displayNames.length <= 0) {
            return result;
        }
        for (String valueName : displayNames) {
            try {
                result += (Ognl.getValue(valueName, obj) + "->");
            } catch (OgnlException e) {
                e.printStackTrace();
                return "";
            }
        }
        return result.substring(0, result.length() - 2);
    }

    public static String nullToEmpty(String temp) {
        if (temp == null) {
            return "";
        } else if (temp.trim().equalsIgnoreCase("null")) {
            return "";
        } else {
            return temp.trim();
        }
    }

    /**
     * @param ipString为传入的一个IP地址
     * @return 返回一个12位数字字串
     * @将一个IP地址格式化成一个12位数字字串
     */
    public static String formatIPString(String ipString) {
        if (StringHelper.nullOrBlank(ipString)) {
            return "000.000.000.000";
        }
        int index = 0;
        while (ipString.startsWith(".")) {
            ipString = ipString.substring(1);
        }
        while (ipString.endsWith(".")) {
            ipString = ipString.substring(0, ipString.length() - 1);
        }
        String result = "";
        while (ipString.length() > 0) {
            index = ipString.indexOf(".");
            if (index >= 0) {
                String temp = ipString.substring(0, index);
                if (temp.length() > 0) {
                    temp = String.valueOf(1000 + Integer.parseInt(temp));
                } else {
                    temp = "000";
                }
                result += temp.substring(temp.length() - 3) + ".";
                ipString = ipString.substring(index + 1);
            } else {
                String temp = "";
                if (ipString.length() > 0) {
                    temp = String.valueOf(1000 + Integer.parseInt(ipString));
                } else {
                    temp = "000";
                }
                result += temp.substring(temp.length() - 3);
                ipString = "";
            }
        }
        if (result.length() > 15) {
            return result.substring(result.length() - 15);
        } else if (result.length() == 15) {
            return result;
        } else {
            return result + "000.000.000.000".substring(result.length());
        }
    }

    public static String nullToEmpty(char[] temp) {
        if (temp == null) {
            return "";
        } else {
            return String.valueOf(temp);
        }
    }

    /**
     * 校验当前字符串是否安全（主要校验是否包含的数据库操作关键字）
     */
    public static String getSafeableString(String string) {
        if (StringHelper.nullOrBlank(string)) {
            return "";
        }
        return StringHelper.replace(string, StringHelper.DY, "").trim();
    }

    /**
     * 获取全角字串
     */
    public static String getSBCCase(String string) {
        String result = StringHelper.nullToEmpty(string);
        if (nullOrBlank(result)) {
            return result;
        }
        for (String key : SBC_TO_DBC_TranMap.keySet()) {
            result = replace(result, key, SBC_TO_DBC_TranMap.get(key));
        }
        return result;
    }

    /**
     * 反转排序规则
     */
    public static String getReverseOrderStr(String orderStr) {
        String result = orderStr.toLowerCase();
        result = result.replaceAll(" asc ", " &desc& ");
        result = result.replaceAll(" asc,", " &desc&,");
        if (result.endsWith(" asc")) {
            result = result.substring(0, result.length() - 4) + " &desc&";
        }
        result = result.replaceAll(" desc ", " &asc& ");
        result = result.replaceAll(" desc,", " &asc&,");
        if (result.endsWith(" desc")) {
            result = result.substring(0, result.length() - 4) + " &asc&";
        }
        result = result.replaceAll("&asc&", "asc");
        result = result.replaceAll("&desc&", "desc");
        return result;
    }

    /**
     * 获取半角字串
     */
    public static String getDBCCase(String string) {
        String result = StringHelper.nullToEmpty(string);
        if (nullOrBlank(result)) {
            return result;
        }
        for (String key : SBC_TO_DBC_TranMap.keySet()) {
            result = replace(result, SBC_TO_DBC_TranMap.get(key), key);
        }
        return result;
    }

    /**
     * 获取like查询组装语句
     */
    public static String getLikeConditionString(String condition, String column) {
        condition = StringHelper.getSafeableString(condition);
        condition = StringHelper.replace(condition, "，", ",");
        if (condition.length() <= 0) {
            return "'%%'";
        }
        String result = "";
        boolean more = false;
        String[] strings = condition.split(",");
        for (String string : strings) {
            string = "'%" + string + "%'";
            string = replace(string, " ", "%");
            string = replace(string, "　", "%");
            while (string.indexOf("%%") >= 0) {
                string = replace(string, "%%", "%");
            }
            if (!more) {
                result += (column + " like " + string);
            } else {
                result += (" or " + column + " like " + string);
            }
            more = true;
        }
        if (more) {
            result = "(" + result + ")";
        }
        return result;
    }

    /**
     * 获取like查询组装语句
     */
    public static String getLikeConditionString(String string) {
        string = StringHelper.getSafeableString(string);
        if (string.length() <= 0) {
            return "'%%'";
        }
        string = "'%" + string + "%'";
        string = replace(string, " ", "%");
        string = replace(string, "　", "%");
        while (string.indexOf("%%") >= 0) {
            string = replace(string, "%%", "%");
        }
        return string;
    }

    /**
     * 将对象为空或不为空都转为字符串
     * 
     * @param obj
     * @return
     */
    public static String getString(Object obj) {

        if (obj == null)
            return "";
        else
            return obj.toString();
    }

    public static String getLengthString(int num, String str) {
        if (str == null)
            return "";
        else if (str.length() < num)
            return str;
        else
            return str.substring(0, num);

    }

    public static void main(String[] args) {
        String str = "WAD", str1 = "WAD_2016";
        System.out.println(str1.indexOf(str));
        String ip1 = "20.001.1220.22";
        String ip2 = "20.001.1220";
        String ip3 = "20.001.1220.22.3566";
        String ip4 = "...3..20.001.1220.22.35660....";
        String ip5 = "....22..3.";
        System.out.println(ip1 + "  格式化后值:  " + formatIPString(ip1));
        System.out.println(ip2 + "  格式化后值:  " + formatIPString(ip2));
        System.out.println(ip3 + "  格式化后值:  " + formatIPString(ip3));
        System.out.println(ip4 + "  格式化后值:  " + formatIPString(ip4));
        System.out.println(ip5 + "  格式化后值:  " + formatIPString(ip5));
        Object obj = null;
        System.out.println(getString(obj));
        System.out.println(12 / 10);
        System.out.println("date:" + getTimeBetweenString("2008-05-08", "2008-25-20", false));
    }

    /**
     * @param timestart为传入的开始时间
     *            （格式：yyyy-mm-dd），showTime是否有hhMMss等字段
     * @return
     */
    public static String getStartTime(String timestart, boolean showTime) {
        if (nullOrBlank(timestart)) {
            return "to_timestamp('1000-01-01 00:00:00','yyyy-mm-dd hh24:Mi:ss')";
        } else {
            return "to_timestamp('" + getSafeableString(timestart) + (showTime ? "" : " 00:00:00") + "','yyyy-mm-dd hh24:Mi:ss')";
        }
    }

    /**
     * @param timeend为传入的结束时间
     *            （格式：yyyy-mm-dd），showTime是否有hhMMss等字段
     * @return
     */
    public static String getEndTime(String timeend, boolean showTime) {
        if (nullOrBlank(timeend)) {
            return "to_timestamp('3000-12-31 23:59:59','yyyy-mm-dd hh24:Mi:ss')";
        } else {
            return "to_timestamp('" + getSafeableString(timeend) + (showTime ? "" : " 23:59:59") + "','yyyy-mm-dd hh24:Mi:ss')";
        }
    }

    /**
     * @param timestart为起始时间
     *            ，timeend为结束时间（格式：yyyy-mm-dd），showTime是否有hhMMss等字段
     * @return 一个时间范围查询条件
     */
    public static String getTimeBetweenString(String timestart, String timeend, boolean showTime) {
        return " between " + getStartTime(timestart, showTime) + " and " + getEndTime(timeend, showTime);
    }

    /**
     * 把object[]转换成字符串，并用pix变量赋值作为分隔符
     * 
     * @param obj
     * @param pix
     * @return
     */
    public static String getStringByObjArray(Object[] obj, String pix) {
        String str = "";
        String result = "";
        for (int i = 0; i < obj.length; i++) {
            str = str + obj[i] + pix;
        }
        if (!"".equals(str)) {
            result = str.substring(0, str.lastIndexOf(pix));
        }

        return result;
    }

}
