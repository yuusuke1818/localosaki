package jp.co.osaki.sms.bean.sms.server.setting.collect;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.server.setting.collect.ListSmsCollectDeviceDetailResultData;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 装置一覧ページング処理用クラス
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingCollectInfoPagingList")
@Dependent
public class InfoPagingList extends BasePaging<ListSmsCollectDeviceDetailResultData> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = -2933354379975934842L;

}
