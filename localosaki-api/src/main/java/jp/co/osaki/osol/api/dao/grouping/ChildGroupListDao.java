/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.grouping;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.grouping.ChildGroupListParameter;
import jp.co.osaki.osol.api.result.grouping.ChildGroupListResult;
import jp.co.osaki.osol.api.resultdata.grouping.ChildGroupListDetailResultData;
import jp.co.osaki.osol.api.servicedao.grouping.ChildGroupListServiceDaoImpl;

/**
 * 子グループ一覧取得処理 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class ChildGroupListDao extends OsolApiDao<ChildGroupListParameter> {

    private final ChildGroupListServiceDaoImpl childGroupListServiceDaoImpl;

    public ChildGroupListDao() {
        childGroupListServiceDaoImpl = new ChildGroupListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public ChildGroupListResult query(ChildGroupListParameter parameter) throws Exception {
        ChildGroupListResult result = new ChildGroupListResult();

        ChildGroupListDetailResultData param = new ChildGroupListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setParentGroupId(parameter.getParentGroupId());
        List<ChildGroupListDetailResultData> resultList = getResultList(childGroupListServiceDaoImpl, param);

        result.setDetailList(resultList);

        return result;
    }

}
