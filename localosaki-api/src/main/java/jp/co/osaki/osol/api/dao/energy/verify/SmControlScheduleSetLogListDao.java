package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleSetLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleSetLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleSetLogListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御スケジュール設定履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlScheduleSetLogListDao extends OsolApiDao<SmControlScheduleSetLogListParameter> {

    private final SmControlScheduleSetLogListServiceDaoImpl smControlScheduleSetLogListServiceDaoImpl;

    public SmControlScheduleSetLogListDao() {
        smControlScheduleSetLogListServiceDaoImpl = new SmControlScheduleSetLogListServiceDaoImpl();
    }

    @Override
    public SmControlScheduleSetLogListResult query(SmControlScheduleSetLogListParameter parameter) throws Exception {
        SmControlScheduleSetLogListDetailResultData param = DemandVerifyUtility.getSmControlScheduleSetLogListParam(
                parameter.getSmControlScheduleLogId(), parameter.getSmId(), parameter.getControlLoadFrom(),
                parameter.getControlLoadTo(), parameter.getTargetMonthFrom(), parameter.getTargetMonthTo());
        return new SmControlScheduleSetLogListResult(getResultList(smControlScheduleSetLogListServiceDaoImpl, param));
    }

}
