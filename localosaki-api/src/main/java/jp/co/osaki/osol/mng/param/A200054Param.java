package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * ユニット(設定) Eα Param クラス
 *
 * @autho t_hayama
 *
 */
public class A200054Param extends BaseParam {

    /**
     * アドレスX
     */
    @NotNull
    @Size(max=99,min=99)
    private List<Map<String, String>> addressList;


    @AssertTrue
    public boolean isValidateAddressList() {
        return addressList != null && validateList(addressList, new HashMap<String, String>() {
            {
                put("terminalKind", "[0-9]{1,2}");    // 端末種別一覧
            }
        });
    }


    public List<Map<String, String>> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Map<String, String>> addressList) {
        this.addressList = addressList;
    }

}
