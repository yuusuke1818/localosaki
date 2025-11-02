package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 気象予報データ送信 Paramクラス
 * @author ya-ishida
 *
 */
public class A210007Param extends BaseParam{

    /**
     * 送信日付
     */
    @Pattern(regexp="[0-9]{10}")
    private String sendDate;

    /**
     * 予報時間リスト
     */
    @NotNull
    @Size(max=117,min=1)
    private List<Map<String, String>> forecastList;

    /**
     * 予報時間リスト個別項目のバリデーション
     * @return
     */
    @AssertTrue
    @SuppressWarnings("serial")
    public boolean isValidationForecastList() {
        return forecastList != null && validateList(forecastList, new HashMap<String, String>() {{
            put("mdh", "[0-9]{6}");                     // 月日時
            put("weather", "[0-9]{2}");                 // 天気
            put("barometricPressure", "[0-9]{1,6}");    // 気圧
            put("rainAmount", "[0-9]{1,4}");            // 降水量
            put("windDirection", "[0-9]{2}");           // 風向き
            put("windSpeed", "[0-9]{1,3}");             // 風速
            put("temperature", "[+-]?[0-9]{3}");         // 外気温
            put("humidity", "[0-9]{1,3}");              // 湿度
            put("cloudAmount", "[0-9]");                // 雲量
        }});
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public List<Map<String, String>> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<Map<String, String>> forecastList) {
        this.forecastList = forecastList;
    }

}
