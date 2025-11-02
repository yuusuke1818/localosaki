package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import jp.skygroup.enl.webap.base.BasePaging;

@Dependent
public class ReservationStatusDataPagingList extends BasePaging<ReservationStatusData> implements Serializable {

    private static final long serialVersionUID = -955104098723397789L;

}
