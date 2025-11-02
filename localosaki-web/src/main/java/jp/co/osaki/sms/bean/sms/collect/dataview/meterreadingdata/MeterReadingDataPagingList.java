package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResultData;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 検針データリストページング処理用クラス
 *
 */
@Named(value = "meterReadingDataPagingList")
@Dependent
public class MeterReadingDataPagingList extends BasePaging<ListSmsMeterReadingDataResultData> implements Serializable {

    private static final long serialVersionUID = 9222294012358014921L;

    public List<ListSmsMeterReadingDataResultData> getAllList() {
        return super.getResultList();
    }


}
