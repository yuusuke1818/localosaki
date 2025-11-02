package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 確定前検針データ一覧ページング処理用クラス
 *
 * @author d-komatsubara
 */
@Named(value = "smsCollectSettingMeterreadingInspectionMeterBefPaging")
@Dependent
public class InspectionMeterBefPaging extends BasePaging<InspectionMeterBef> implements Serializable {

    private static final long serialVersionUID = 2340473259950182727L;

    public List<InspectionMeterBef> getResultList() {
        return super.getResultList();
    }

}
