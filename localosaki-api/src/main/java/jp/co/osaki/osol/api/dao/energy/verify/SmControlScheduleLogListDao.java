package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleLogListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御スケジュール履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlScheduleLogListDao extends OsolApiDao<SmControlScheduleLogListParameter> {

    private final SmControlScheduleLogListServiceDaoImpl smControlScheduleLogListServiceDaoImpl;

    public SmControlScheduleLogListDao() {
        smControlScheduleLogListServiceDaoImpl = new SmControlScheduleLogListServiceDaoImpl();
    }

    @Override
    public SmControlScheduleLogListResult query(SmControlScheduleLogListParameter parameter) throws Exception {
        SmControlScheduleLogListDetailResultData param = DemandVerifyUtility.getSmControlScheduleLogListParam(
                parameter.getSmControlScheduleLogId(), parameter.getSmId(), parameter.getSettingUpdateDateTimeFrom(),
                parameter.getSettingUpdateDateTimeTo(), null);
        return new SmControlScheduleLogListResult(getResultList(smControlScheduleLogListServiceDaoImpl, param));
    }

}
