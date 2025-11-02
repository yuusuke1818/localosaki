package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

/**
 * ユーザ一覧ページング処理用クラス
 *
 * @author nishida.t
 */
@Named("smsCollectSettingMeterUserInfoListPaging")
@Dependent
public class InfoListPaging extends BasePaging<PersonInfo> implements Serializable {

    private static final long serialVersionUID = 6380589336769302100L;

}
