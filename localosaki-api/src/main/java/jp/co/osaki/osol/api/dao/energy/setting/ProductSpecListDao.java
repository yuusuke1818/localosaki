package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductSpecListParameter;
import jp.co.osaki.osol.api.result.energy.setting.ProductSpecListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductSpecListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductSpecListServiceDaoImpl;

/**
 * 製品仕様情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class ProductSpecListDao extends OsolApiDao<ProductSpecListParameter> {

    private final ProductSpecListServiceDaoImpl productSpecListServiceDaoImpl;

    public ProductSpecListDao() {
        productSpecListServiceDaoImpl = new ProductSpecListServiceDaoImpl();
    }

    @Override
    public ProductSpecListResult query(ProductSpecListParameter parameter) throws Exception {

        ProductSpecListDetailResultData param = new ProductSpecListDetailResultData();
        param.setProductCd(parameter.getProductCd());
        param.setProductName(parameter.getProductName());

        List<ProductSpecListDetailResultData> resultList = getResultList(productSpecListServiceDaoImpl, param);

        return new ProductSpecListResult(resultList);
    }

}
