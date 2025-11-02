/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductControlLoadListParameter;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;

/**
 * 製品制御負荷情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class ProductControlLoadListDao extends OsolApiDao<ProductControlLoadListParameter> {

    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    public ProductControlLoadListDao() {
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public ProductControlLoadListResult query(ProductControlLoadListParameter parameter) throws Exception {
        ProductControlLoadListDetailResultData param = new ProductControlLoadListDetailResultData();
        param.setProductCd(parameter.getProductCd());
        param.setControlLoadFrom(parameter.getControlLoadFrom());
        param.setControlLoadTo(parameter.getControlLoadTo());
        return new ProductControlLoadListResult(getResultList(productControlLoadListServiceDaoImpl, param));
    }

}
