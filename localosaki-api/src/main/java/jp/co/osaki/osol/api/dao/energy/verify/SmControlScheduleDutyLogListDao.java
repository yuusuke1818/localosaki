package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleDutyLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleDutyLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleDutyLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlScheduleDutyServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御スケジュール時間帯履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlScheduleDutyLogListDao extends OsolApiDao<SmControlScheduleDutyLogListParameter> {

    private final SmControlScheduleDutyServiceDaoImpl smControlScheduleDutyServiceDaoImpl;

    public SmControlScheduleDutyLogListDao() {
        smControlScheduleDutyServiceDaoImpl = new SmControlScheduleDutyServiceDaoImpl();
    }

    @Override
    public SmControlScheduleDutyLogListResult query(SmControlScheduleDutyLogListParameter parameter) throws Exception {
        SmControlScheduleDutyLogListDetailResultData param = DemandVerifyUtility.getSmControlScheduleDutyLogListParam(
                parameter.getSmControlScheduleLogId(), parameter.getSmId(), parameter.getPatternNoFrom(),
                parameter.getPatternNoTo());
        return new SmControlScheduleDutyLogListResult(getResultList(smControlScheduleDutyServiceDaoImpl, param));
    }

}
