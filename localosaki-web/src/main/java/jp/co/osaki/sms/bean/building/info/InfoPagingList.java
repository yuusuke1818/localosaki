package jp.co.osaki.sms.bean.building.info;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 建物一覧ページング処理用クラス
 *
 * @author d-komatsubara
 */
@Named(value = "buildingInfoPagingList")
@Dependent
public class InfoPagingList extends BasePaging<ListInfo> implements Serializable {

    private static final long serialVersionUID = -955104059723397517L;

}
