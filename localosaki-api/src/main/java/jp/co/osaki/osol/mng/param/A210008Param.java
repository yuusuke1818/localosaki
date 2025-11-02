package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * アメダスデータ送信 Paramクラス
 * @author ya-ishida
 *
 */
public class A210008Param extends BaseParam {

    /**
     * 送信日付
     */
    @Pattern(regexp="[0-9]{10}")
    private String sendDate;

    /**
     * アメダスデータリスト
     */
    @NotNull
    @Size(max=24,min=1)
    private List<Map<String, String>> amedasList;

    /**
     * アメダスリスト個別項目のバリデーション
     * @return
     */
    @AssertTrue
    @SuppressWarnings("serial")
    public boolean isValidationAmedasList() {
        return amedasList != null && (validateList(amedasList, new HashMap<String, String>() {{
            put("temperature", "[+-]?[0-9]{3}|[\\s]{4}");         // 外気温
            put("humidity", "[\\s]{1,3}");              // 湿度
        }}) || validateList(amedasList, new HashMap<String, String>() {{
            put("temperature", "[\\s]{4}");             // 外気温
            put("humidity", "[\\s]{1,3}");              // 湿度
        }}));
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public List<Map<String, String>> getAmedasList() {
        return amedasList;
    }

    public void setAmedasList(List<Map<String, String>> amedasList) {
        this.amedasList = amedasList;
    }

}
