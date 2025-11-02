package jp.co.osaki.sms.bean.sms.server.setting.handy;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.server.setting.handy.ListSmsHandyDeviceDetailResultData;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 装置一覧ページング処理用クラス
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingHandyInfoPagingList")
@Dependent
public class InfoPagingList extends BasePaging<ListSmsHandyDeviceDetailResultData> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = -4001843286575117499L;

}
