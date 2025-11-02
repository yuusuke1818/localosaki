package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointLineDetailListParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointLineDetailListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmLinePointLineDetailListResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmLinePointLineDetailListServiceDaoImpl;

/**
 * 機器系統ポイント取得 Daoクラス
 * @author y-maruta
 */
@Stateless
public class SmLinePointLineDetailListDao extends OsolApiDao<SmLinePointLineDetailListParameter> {

    //TODO EntityServiceDaoクラスを使わない
    private final SmLinePointLineDetailListServiceDaoImpl SmLinePointLineDetailListServiceDaoImpl;

    public SmLinePointLineDetailListDao() {
        SmLinePointLineDetailListServiceDaoImpl = new SmLinePointLineDetailListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public SmLinePointLineDetailListResult query(SmLinePointLineDetailListParameter parameter) throws Exception {
        SmLinePointLineDetailListResult result = new SmLinePointLineDetailListResult();

        SmLinePointLineDetailListResultData smLinePointparam = new SmLinePointLineDetailListResultData();
        smLinePointparam.setCorpId(parameter.getOperationCorpId());
        smLinePointparam.setLineGroupId(parameter.getLineGroupId());
        smLinePointparam.setLineNo(parameter.getLineNo());
        smLinePointparam.setBuildingId(parameter.getBuildingId());
        smLinePointparam.setSmId(parameter.getSmId());
        smLinePointparam.setPointNo(parameter.getPointNo());
        smLinePointparam.setPointCalcType(parameter.getPointCalcType());
        smLinePointparam.setLineEnableFlg(parameter.getLineEnableFlg());
        smLinePointparam.setInputEnableFlg(parameter.getInputEnableFlg());
        List<SmLinePointLineDetailListResultData> SmLinePointLineDetailList = getResultList(SmLinePointLineDetailListServiceDaoImpl, smLinePointparam);

        result.setSmLinePointLineDetailListResultList(SmLinePointLineDetailList);

        return result;
    }

}
