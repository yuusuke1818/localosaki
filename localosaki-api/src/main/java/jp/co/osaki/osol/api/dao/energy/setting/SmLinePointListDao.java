package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointListParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmLinePointListResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmLinePointListServiceDaoImpl;

/**
 * 機器系統ポイント取得 Daoクラス
 * @author y-maruta
 */
@Stateless
public class SmLinePointListDao extends OsolApiDao<SmLinePointListParameter> {

    //TODO EntityServiceDaoクラスを使わない
    private final SmLinePointListServiceDaoImpl smLinePointListServiceDaoImpl;

    public SmLinePointListDao() {
        smLinePointListServiceDaoImpl = new SmLinePointListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public SmLinePointListResult query(SmLinePointListParameter parameter) throws Exception {
        SmLinePointListResult result = new SmLinePointListResult();

        SmLinePointListResultData smLinePointparam = new SmLinePointListResultData();
        smLinePointparam.setCorpId(parameter.getOperationCorpId());
        smLinePointparam.setLineGroupId(parameter.getLineGroupId());
        smLinePointparam.setLineNo(parameter.getLineNo());
        smLinePointparam.setBuildingId(parameter.getBuildingId());
        smLinePointparam.setSmId(parameter.getSmId());
        smLinePointparam.setPointNo(parameter.getPointNo());
        List<SmLinePointListResultData> smLinePointList = getResultList(smLinePointListServiceDaoImpl, smLinePointparam);

        result.setSmLinePointListResultList(smLinePointList);

        return result;
    }

}
