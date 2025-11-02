package jp.co.osaki.sms.bean.sms.collect.setting.meterGroup;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;

public class MeterGroupInfo {
    private Boolean checkBox;
    private GetSmsMeterGroupResultData meterGroup;

    public Boolean getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }
    public GetSmsMeterGroupResultData getMeterGroup() {
        return meterGroup;
    }
    public void setMeterGroup(GetSmsMeterGroupResultData meterGroup) {
        this.meterGroup = meterGroup;
    }
}
