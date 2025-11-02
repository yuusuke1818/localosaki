package jp.co.osaki.osol.api.dao.energy.verify;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlLoadVerifyListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmControlLoadVerifyListResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlLoadVerifyListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmControlLoadVerifyListServiceDaoImpl;

/**
 * 機器制御負荷検証取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmControlLoadVerifyListDao extends OsolApiDao<SmControlLoadVerifyListParameter> {

    private final SmControlLoadVerifyListServiceDaoImpl smControlLoadVerifyListServiceDaoImpl;

    public SmControlLoadVerifyListDao() {
        smControlLoadVerifyListServiceDaoImpl = new SmControlLoadVerifyListServiceDaoImpl();
    }

    @Override
    public SmControlLoadVerifyListResult query(SmControlLoadVerifyListParameter parameter) throws Exception {
        SmControlLoadVerifyListDetailResultData param = new SmControlLoadVerifyListDetailResultData();
        param.setSmId(parameter.getSmId());

        return new SmControlLoadVerifyListResult(getResultList(smControlLoadVerifyListServiceDaoImpl, param));
    }

}
