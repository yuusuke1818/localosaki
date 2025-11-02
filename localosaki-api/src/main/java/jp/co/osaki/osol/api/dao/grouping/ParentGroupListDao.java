/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.grouping;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.grouping.ParentGroupListParameter;
import jp.co.osaki.osol.api.result.grouping.ParentGroupListResult;
import jp.co.osaki.osol.api.resultdata.grouping.ParentGroupListDetailResultData;
import jp.co.osaki.osol.api.servicedao.grouping.ParentGroupListServiceDaoImpl;

/**
 * 親グループ一覧取得処理 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class ParentGroupListDao extends OsolApiDao<ParentGroupListParameter> {

    private final ParentGroupListServiceDaoImpl parentGroupListServiceDaoImpl;

    public ParentGroupListDao() {
        parentGroupListServiceDaoImpl = new ParentGroupListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public ParentGroupListResult query(ParentGroupListParameter parameter) throws Exception {
        ParentGroupListResult result = new ParentGroupListResult();

        ParentGroupListDetailResultData param = new ParentGroupListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        List<ParentGroupListDetailResultData> resultList = getResultList(parentGroupListServiceDaoImpl, param);

        result.setDetailList(resultList);

        return result;
    }

}
