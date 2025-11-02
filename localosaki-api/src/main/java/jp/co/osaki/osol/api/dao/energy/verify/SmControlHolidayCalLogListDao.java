package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlHolidayCalLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlHolidayCalLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayCalLogListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御祝日設定カレンダ履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlHolidayCalLogListDao extends OsolApiDao<SmControlHolidayCalLogListParameter> {

    private final SmControlHolidayCalLogListServiceDaoImpl smControlHolidayCalLogListServiceDaoImpl;

    public SmControlHolidayCalLogListDao() {
        smControlHolidayCalLogListServiceDaoImpl = new SmControlHolidayCalLogListServiceDaoImpl();
    }

    @Override
    public SmControlHolidayCalLogListResult query(SmControlHolidayCalLogListParameter parameter) throws Exception {
        SmControlHolidayCalLogListDetailResultData param = DemandVerifyUtility.getSmControlHolidayCalLogListParam(
                parameter.getSmControlHolidayLogId(), parameter.getSmId(), parameter.getHolidayMmDdFrom(),
                parameter.getHolidayMmDdTo());
        return new SmControlHolidayCalLogListResult(getResultList(smControlHolidayCalLogListServiceDaoImpl, param));
    }

}
