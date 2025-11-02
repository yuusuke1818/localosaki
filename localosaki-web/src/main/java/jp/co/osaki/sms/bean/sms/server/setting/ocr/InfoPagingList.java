package jp.co.osaki.sms.bean.sms.server.setting.ocr;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.osol.api.resultdata.sms.server.setting.ocr.ListSmsOcrDeviceDetailResultData;
import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 装置一覧ページング処理用クラス
 *
 * @author iwasaki_y
 */
@Named(value = "smsServerSettingOcrInfoPagingList")
@Dependent
public class InfoPagingList extends BasePaging<ListSmsOcrDeviceDetailResultData> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 4740774255511096118L;

}
