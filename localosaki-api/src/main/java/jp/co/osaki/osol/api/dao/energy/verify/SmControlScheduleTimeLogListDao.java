package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleTimeLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleTimeLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleTimeLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleTimeLogListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御スケジュール時間帯履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlScheduleTimeLogListDao extends OsolApiDao<SmControlScheduleTimeLogListParameter> {

    private final SmControlScheduleTimeLogListServiceDaoImpl smControlScheduleTimeLogListServiceDaoImpl;

    public SmControlScheduleTimeLogListDao() {
        smControlScheduleTimeLogListServiceDaoImpl = new SmControlScheduleTimeLogListServiceDaoImpl();
    }

    @Override
    public SmControlScheduleTimeLogListResult query(SmControlScheduleTimeLogListParameter parameter)
            throws Exception {
        SmControlScheduleTimeLogListDetailResultData param = DemandVerifyUtility
                .getSmControlScheduleTimeLogListParam(parameter.getSmControlScheduleLogId(), parameter.getSmId(),
                        parameter.getPatternNoFrom(), parameter.getPatternNoTo(), parameter.getTimeSlotNoFrom(),
                        parameter.getTimeSlotNoTo());
        return new SmControlScheduleTimeLogListResult(
                getResultList(smControlScheduleTimeLogListServiceDaoImpl, param));
    }

}
