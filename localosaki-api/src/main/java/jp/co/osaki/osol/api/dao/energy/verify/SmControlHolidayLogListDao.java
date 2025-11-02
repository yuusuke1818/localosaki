package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlHolidayLogListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlHolidayLogListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlHolidayLogListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.verify.DemandVerifyUtility;

/**
 * 機器制御祝日設定履歴取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlHolidayLogListDao extends OsolApiDao<SmControlHolidayLogListParameter> {

    private final SmControlHolidayLogListServiceDaoImpl smControlHolidayLogListServiceDaoImpl;

    public SmControlHolidayLogListDao() {
        smControlHolidayLogListServiceDaoImpl = new SmControlHolidayLogListServiceDaoImpl();
    }

    @Override
    public SmControlHolidayLogListResult query(SmControlHolidayLogListParameter parameter) throws Exception {
        SmControlHolidayLogListDetailResultData param = DemandVerifyUtility.getSmControlHolidayLogListParam(
                parameter.getSmControlHolidayLogId(), parameter.getSmId(), parameter.getSettingUpdateDateTimeFrom(),
                parameter.getSettingUpdateDateTimeTo(), null);
        return new SmControlHolidayLogListResult(getResultList(smControlHolidayLogListServiceDaoImpl, param));
    }

}
