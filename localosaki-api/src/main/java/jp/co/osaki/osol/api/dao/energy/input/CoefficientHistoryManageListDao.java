/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.input;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.input.CoefficientHistoryManageListParameter;
import jp.co.osaki.osol.api.result.energy.input.CoefficientHistoryManageListResult;
import jp.co.osaki.osol.api.resultdata.energy.input.CoefficientHistoryManageListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.input.CoefficientHistoryManageListServiceDaoImpl;

/**
 * 係数履歴管理マスタ取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class CoefficientHistoryManageListDao extends OsolApiDao<CoefficientHistoryManageListParameter> {

    private final CoefficientHistoryManageListServiceDaoImpl coefficientHistoryManageListServiceDaoImpl;

    public CoefficientHistoryManageListDao() {
        coefficientHistoryManageListServiceDaoImpl = new CoefficientHistoryManageListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public CoefficientHistoryManageListResult query(CoefficientHistoryManageListParameter parameter) throws Exception {
        return new CoefficientHistoryManageListResult(getCoefficientHistoryManage(parameter));
    }

    /**
     * 係数履歴管理マスタデータを取得する
     *
     * @param parameter
     * @return
     */
    private List<CoefficientHistoryManageListDetailResultData> getCoefficientHistoryManage(
            CoefficientHistoryManageListParameter parameter) {

        CoefficientHistoryManageListDetailResultData param = new CoefficientHistoryManageListDetailResultData();
        param.setEngTypeCd(parameter.getEngTypeCd());
        param.setEngId(parameter.getEngId());
        param.setDayAndNightType(parameter.getDayAndNightType());
        param.setCalYmFrom(parameter.getCalYmFrom());
        param.setCalYmTo(parameter.getCalYmTo());

        return getResultList(coefficientHistoryManageListServiceDaoImpl, param);
    }

}
