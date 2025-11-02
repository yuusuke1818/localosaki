/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * @author n-takada
 */
public class Utility {

    /**
     * パラメーターセパレーター
     */
    private static final String PARAM_SEPARATOR = "&";

    /**
     * パラメーターイコール
     */
    private static final String PARAM_EQUAL = "=";

    /**
     * リクエストパラメーターを一つの文字列に変更する
     *
     * @param request
     * @return
     */
    public static String createStringParameter(HttpServletRequest request) {
        String retString = "";
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramKey = paramNames.nextElement();
            String paramValue = request.getParameter(paramKey);
            if (!CheckUtility.isNullOrEmpty(paramKey)) {
                if (!"".equals(retString)) {
                    retString += PARAM_SEPARATOR;
                }
                retString += paramKey;
                retString += PARAM_EQUAL;
                if (paramValue == null) {
                    paramValue = "";
                }
                retString += paramValue;
            }
        }
        return retString;
    }

    /**
     * 一つの文字列だったパラメーターをマップに変更する
     *
     * @param request
     * @return
     */
    public static Map<String, String> changeParamToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramKey = request.getParameterNames();

        while (paramKey.hasMoreElements()) {
            String key = paramKey.nextElement();
            if (CheckUtility.isNullOrEmpty(key)) {
                continue;
            }
            String value = request.getParameter(key);
            map.put(key, value);

        }
        return map;
    }

    /**
     * 引数で渡されたListがnullまたは、空ではないとき、最初の要素を返す。<br>
     * 引数で渡されたListがnullまたは、空である場合、nullを返す。<br>
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> T getListFirstItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 文字列から.を削除してlong型に変換する
     *
     * @param number
     * @return
     */
    public static long changeStringToLong(String number) {
        if (number.contains(".")) {
            number = number.substring(0, number.indexOf("."));
        }
        return new Long(number);
    }
}
