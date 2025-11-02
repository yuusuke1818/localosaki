package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingResultDate;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 請求データリストページング処理用クラス
 *
 */
@Named(value = "selectBillingDataPagingList")
@Dependent
public class SelectBillingDataPagingList extends BasePaging<ListSmsSelectBillingResultDate> implements Serializable {

    private static final long serialVersionUID = 3605626657566598830L;

    public List<ListSmsSelectBillingResultDate> getAllList() {
        return super.getResultList();
    }


}