/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.verify;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgKensyoSmInfoParameter;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgKensyoSmInfoResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgKensyoSmInfoDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.DemandOrgKensyoSmInfoServiceDaoImpl;

/**
 * エネルギー検証 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class DemandOrgKensyoSmInfoDao extends OsolApiDao<DemandOrgKensyoSmInfoParameter> {

    private final DemandOrgKensyoSmInfoServiceDaoImpl demandOrgKensyoSmInfoServiceDaoImpl;;

    public DemandOrgKensyoSmInfoDao() {
        demandOrgKensyoSmInfoServiceDaoImpl = new DemandOrgKensyoSmInfoServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DemandOrgKensyoSmInfoResult query(DemandOrgKensyoSmInfoParameter parameter) throws Exception {
        DemandOrgKensyoSmInfoResult result = new DemandOrgKensyoSmInfoResult();
        DemandOrgKensyoSmInfoDetailResultData param = new DemandOrgKensyoSmInfoDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setBuildingId(parameter.getBuildingId());
        List<DemandOrgKensyoSmInfoDetailResultData> list = getResultList(demandOrgKensyoSmInfoServiceDaoImpl, param);
        result.setDetailList(list);
        return result;
    }

}
