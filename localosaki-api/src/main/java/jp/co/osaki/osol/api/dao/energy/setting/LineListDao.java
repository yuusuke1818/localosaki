/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.LineListParameter;
import jp.co.osaki.osol.api.result.energy.setting.LineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 系統取得処理 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class LineListDao extends OsolApiDao<LineListParameter> {

    private final LineListServiceDaoImpl listLineServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public LineListDao() {
        listLineServiceDaoImpl = new LineListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    @Override
    public LineListResult query(LineListParameter parameter) throws Exception {
        LineListResult result = new LineListResult();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new LineListResult();
        }

        LineListDetailResultData param = new LineListDetailResultData();

        param.setCorpId(parameter.getOperationCorpId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setLineNo(parameter.getLineNo());
        param.setLineEnableFlg(parameter.getLineEnableFlg());

        result.setDetailList(getResultList(listLineServiceDaoImpl, param));

        return result;
    }

}
