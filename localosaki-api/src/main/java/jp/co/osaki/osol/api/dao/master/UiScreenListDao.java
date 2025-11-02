package jp.co.osaki.osol.api.dao.master;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.master.UiScreenListParameter;
import jp.co.osaki.osol.api.result.master.UiScreenListResult;
import jp.co.osaki.osol.api.resultdata.master.UiScreenListDetailResultData;
import jp.co.osaki.osol.api.servicedao.master.UiScreenListServiceDaoImpl;

/**
 * UI画面マスタ Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class UiScreenListDao extends OsolApiDao<UiScreenListParameter> {

    private final UiScreenListServiceDaoImpl impl;

    public UiScreenListDao() {
        impl = new UiScreenListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public UiScreenListResult query(UiScreenListParameter parameter) throws Exception {
        UiScreenListResult result = new UiScreenListResult();
        result.setDetailList(getResultList(impl, new UiScreenListDetailResultData()));
        return result;
    }

}
