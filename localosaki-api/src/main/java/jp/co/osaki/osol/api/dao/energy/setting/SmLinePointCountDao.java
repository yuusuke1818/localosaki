package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointCountParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointCountResult;
import jp.co.osaki.osol.api.servicedao.entity.MSmLinePointServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 機器系統ポイント件数取得 Daoクラス
 * @author t_hirata
 */
@Stateless
public class SmLinePointCountDao extends OsolApiDao<SmLinePointCountParameter> {

    //TODO EntityServiceDaoクラスを使わない
    private final MSmLinePointServiceDaoImpl smLinePointServiceDaoImpl;

    public SmLinePointCountDao() {
        smLinePointServiceDaoImpl = new MSmLinePointServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public SmLinePointCountResult query(SmLinePointCountParameter parameter) throws Exception {
        SmLinePointCountResult result = new SmLinePointCountResult();

        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        MSmLinePoint param = new MSmLinePoint();
        MSmLinePointPK pkParam = new MSmLinePointPK();
        pkParam.setCorpId(parameter.getOperationCorpId());
        pkParam.setLineGroupId(parameter.getLineGroupId());
        pkParam.setBuildingId(parameter.getBuildingId());
        param.setId(pkParam);
        List<MSmLinePoint> pointList = getResultList(smLinePointServiceDaoImpl, param);
        if (pointList == null || pointList.isEmpty()) {
            result.setCount(0);
        } else {
            result.setCount(pointList.size());
        }

        return result;
    }

}
