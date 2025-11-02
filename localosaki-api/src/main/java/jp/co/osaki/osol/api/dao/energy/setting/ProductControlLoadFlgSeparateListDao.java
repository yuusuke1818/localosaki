package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.ProductControlLoadFlgSeparateListParameter;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.ProductControlLoadFlgSeparateUtility;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 製品制御負荷 制御フラグ別リスト取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class ProductControlLoadFlgSeparateListDao extends OsolApiDao<ProductControlLoadFlgSeparateListParameter> {

    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;
    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;

    public ProductControlLoadFlgSeparateListDao() {
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public ProductControlLoadFlgSeparateListResult query(ProductControlLoadFlgSeparateListParameter parameter) throws Exception {

        ProductControlLoadFlgSeparateListResult result = new ProductControlLoadFlgSeparateListResult();


        if(CheckUtility.isNullOrEmpty(parameter.getProductCd())) {
            //製品コードが設定されていない場合は機器から製品情報を取得する
            SmSelectResult smParam = new SmSelectResult();
            smParam.setSmId(parameter.getSmId());
            List<SmSelectResult> smResultList = getResultList(smSelectResultServiceDaoImpl, smParam);
            if(smResultList == null || smResultList.size() != 1) {
                //機器情報が取得できない場合は処理を終了
                return result;
            } else {
                parameter.setProductCd(smResultList.get(0).getProductCd());
            }
        }

        //製品制御負荷情報を取得する
        ProductControlLoadListDetailResultData productControlLoadParam = new ProductControlLoadListDetailResultData();
        productControlLoadParam.setProductCd(parameter.getProductCd());
        List<ProductControlLoadListDetailResultData> productControlLoadList = getResultList(productControlLoadListServiceDaoImpl, productControlLoadParam);

        return ProductControlLoadFlgSeparateUtility.setProductControlLoadFlgSeparateList(productControlLoadList);
    }

}
