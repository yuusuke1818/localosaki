package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 建物装置設定 建物検索ページング処理用クラス
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingBuildingDeviceInfoPagingList")
@Dependent
public class InfoPagingList extends BasePaging<BuildingInfo> implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = -7874186639432650377L;

}
