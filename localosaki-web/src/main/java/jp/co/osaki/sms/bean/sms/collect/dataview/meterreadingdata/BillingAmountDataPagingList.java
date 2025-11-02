package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDataByTenant;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 請求金額データリストページング処理用クラス
 *
 */
@Named(value = "billingAmountDataPagingList")
@Dependent
public class BillingAmountDataPagingList extends BasePaging<BillingAmountDataByTenant> implements Serializable {

    private static final long serialVersionUID = 8484860248178263468L;

    public List<BillingAmountDataByTenant> getAllList() {
        return this.getResultList();
    }


}
