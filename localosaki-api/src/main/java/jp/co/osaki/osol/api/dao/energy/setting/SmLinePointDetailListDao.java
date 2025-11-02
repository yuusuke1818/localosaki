package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointDetailListParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointDetailListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmLinePointListResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmLinePointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;

/**
 * 機器系統ポイント情報取得 Daoクラス
 * @author y-maruta
 */
@Stateless
public class SmLinePointDetailListDao extends OsolApiDao<SmLinePointDetailListParameter> {

    //TODO EntityServiceDaoクラスを使わない
    private final SmLinePointListServiceDaoImpl smLinePointDetailListServiceDaoImpl;
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;

    public SmLinePointDetailListDao() {
        smLinePointDetailListServiceDaoImpl = new SmLinePointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public SmLinePointDetailListResult query(SmLinePointDetailListParameter parameter) throws Exception {
        SmLinePointDetailListResult result = new SmLinePointDetailListResult();

        SmLinePointListResultData smLinePointparam = new SmLinePointListResultData();
        smLinePointparam.setCorpId(parameter.getOperationCorpId());
        smLinePointparam.setLineGroupId(parameter.getLineGroupId());
        smLinePointparam.setLineNo(parameter.getLineNo());
        smLinePointparam.setBuildingId(parameter.getBuildingId());
        smLinePointparam.setSmId(parameter.getSmId());
        smLinePointparam.setPointNo(parameter.getPointNo());
        List<SmLinePointListResultData> smLinePointList = getResultList(smLinePointDetailListServiceDaoImpl, smLinePointparam);

        result.setSmLinePointListResultList(smLinePointList);

        List<SmPointListDetailResultData> smPointListResult = new ArrayList<>();
        for(SmLinePointListResultData smLinePoint:smLinePointList) {
            SmPointListDetailResultData smPointParam = new SmPointListDetailResultData();
            smPointParam.setSmId(smLinePoint.getSmId());
            smPointParam.setPointNo(smLinePoint.getPointNo());
            List<SmPointListDetailResultData> smPointList = getResultList(smPointListServiceDaoImpl, smPointParam);
            smPointListResult.addAll(smPointList);
        }

        result.setSmPointListDetailResultData(smPointListResult);

        return result;
    }

}
