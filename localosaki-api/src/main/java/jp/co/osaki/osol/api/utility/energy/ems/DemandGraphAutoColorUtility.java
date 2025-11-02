/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * グラフ自動配色 Utilityクラス
 *
 * @author ya-ishida
 */
public class DemandGraphAutoColorUtility {

    /**
     * 濃度（％）
     */
    private static final int DARK_RATE = 90;
    /**
     * テーマカラー
     */
    private static final String[] UNIVERSAL_COLORS = {
        "#ff2800", // 赤
        "#faf500", // 黄色
        "#35a16b", // 緑
        "#0041ff", // 青
        "#66ccff", // 空色
        "#ff99a0", // ピンク
        "#ff9900", // オレンジ
        "#9a0079", // 紫
        "#663300" // 茶
    };

    /**
     * カラーコードに付与する接頭辞
     */
    private static final String COLOR_PREFIX = "#";

    /**
     * グラフ自動配色を複数取得する
     *
     * @param count 取得件数
     * @return
     */
    public static List<String> getGraphAutoColorList(Integer count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Integer deepLevel = new BigDecimal(i).divide(new BigDecimal(UNIVERSAL_COLORS.length), 0, BigDecimal.ROUND_DOWN).intValue();
            Integer universalColorIndex = i % UNIVERSAL_COLORS.length;
            list.add(getGraphColor(UNIVERSAL_COLORS[universalColorIndex], deepLevel));
        }
        return list;
    }

    /**
     * グラフ自動配色を1つ取得する
     *
     * @param index 自動配色のインデックス番号
     * @return
     */
    public static String getGraphAutoColor(Integer index) {
        Integer deepLevel = new BigDecimal(index).divide(new BigDecimal(UNIVERSAL_COLORS.length), 0, BigDecimal.ROUND_DOWN).intValue();
        Integer universalColorIndex = index % UNIVERSAL_COLORS.length;
        return getGraphColor(UNIVERSAL_COLORS[universalColorIndex], deepLevel);
    }

    /**
     * 自動配色の色を調整する
     *
     * @param colorCodeBefore 処理前カラーコード
     * @param deepLevel 濃度レベル
     * @return
     */
    private static String getGraphColor(String colorCodeBefore, Integer deepLevel) {

        if (colorCodeBefore.length() != 7) {
            return null;
        }

        //赤、緑、青にそれぞれ分解する
        String red = colorCodeBefore.substring(1, 3);
        String green = colorCodeBefore.substring(3, 5);
        String blue = colorCodeBefore.substring(5, 7);
        return COLOR_PREFIX.concat(deepenColor(red, deepLevel)).concat(deepenColor(green, deepLevel)).concat(deepenColor(blue, deepLevel));
    }

    /**
     * 指定の濃度レベルまで色を変更する
     *
     * @param colorCode
     * @param deepLevel
     * @return
     */
    private static String deepenColor(String colorCode, Integer deepLevel) {

        //カラーコードをIntegerに変換
        Integer colorVal = Integer.decode("0x".concat(colorCode));
        BigDecimal colorDecimal = new BigDecimal(colorVal);
        BigDecimal rate = new BigDecimal(DARK_RATE).divide(new BigDecimal(100), 1, BigDecimal.ROUND_CEILING);
        for (int i = 0; i < deepLevel; i++) {
            colorDecimal = colorDecimal.multiply(rate);
        }

        return String.format("%02x", colorDecimal.intValue());
    }

}
