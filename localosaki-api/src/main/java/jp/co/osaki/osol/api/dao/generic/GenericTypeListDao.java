/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.generic;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.generic.GenericTypeListParameter;
import jp.co.osaki.osol.api.result.generic.GenericTypeListResult;
import jp.co.osaki.osol.api.resultdata.generic.GenericTypeListDetailResultData;
import jp.co.osaki.osol.api.servicedao.generic.GenericTypeListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;

/**
 * 汎用区分マスタ取得 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class GenericTypeListDao extends OsolApiDao<GenericTypeListParameter> {

    @Inject
    private GenericTypeUtility genericTypeUtility;

    private final GenericTypeListServiceDaoImpl genericTypeServiceDaoImpl;

    public GenericTypeListDao() {
        genericTypeServiceDaoImpl = new GenericTypeListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public GenericTypeListResult query(GenericTypeListParameter parameter) throws Exception {
        GenericTypeListResult result = new GenericTypeListResult();
        List<GenericTypeListDetailResultData> list = genericTypeUtility.getKbnList(parameter.getGroupCode());
        result.setDetailList(list);
        return result;
    }

    public List<GenericTypeListDetailResultData> getGenericTypeList(ApiGenericTypeConstants.GROUP_CODE groupCode) {
        GenericTypeListDetailResultData resultSet = new GenericTypeListDetailResultData();
        resultSet.setGroupCode(groupCode.getVal());
        return getResultList(genericTypeServiceDaoImpl, resultSet);
    }

}
