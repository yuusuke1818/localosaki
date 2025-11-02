package jp.co.osaki.osol.api.dao.energy.verify;

import java.util.Comparator;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.VerifySettingDownLoadTemplateInfoParameter;
import jp.co.osaki.osol.api.result.energy.verify.VerifySettingDownLoadTemplateInfoResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.utility.common.SortUtility;

/**
 * 検証ダウンロードテンプレート情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class VerifySettingDownLoadTemplateInfoDao extends OsolApiDao<VerifySettingDownLoadTemplateInfoParameter> {

    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    public VerifySettingDownLoadTemplateInfoDao() {
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public VerifySettingDownLoadTemplateInfoResult query(VerifySettingDownLoadTemplateInfoParameter parameter)
            throws Exception {
        VerifySettingDownLoadTemplateInfoResult result = new VerifySettingDownLoadTemplateInfoResult();
        //系統情報を取得する（デマンド用）
        LineListDetailResultData lineParam = new LineListDetailResultData();
        lineParam.setCorpId(parameter.getOperationCorpId());
        lineParam.setLineGroupId(parameter.getLineGroupId());
        lineParam.setDmValidFlg(OsolConstants.FLG_ON);

        //系統情報（デマンド用）
        result.setLineDemandList(SortUtility.sortLineListByLineNo(getResultList(lineListServiceDaoImpl, lineParam),
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));

        //系統情報を取得する（イベント用）
        lineParam = new LineListDetailResultData();
        lineParam.setCorpId(parameter.getOperationCorpId());
        lineParam.setLineGroupId(parameter.getLineGroupId());
        lineParam.setEventValidFlg(OsolConstants.FLG_ON);

        //系統情報（イベント用）
        result.setLineEventList(SortUtility.sortLineListByLineNo(getResultList(lineListServiceDaoImpl, lineParam),
                ApiCodeValueConstants.SORT_ORDER.ASC.getVal()));

        //製品制御負荷情報を取得
        ProductControlLoadListDetailResultData productParam = new ProductControlLoadListDetailResultData();
        productParam.setProductCd(parameter.getProductCd());

        //製品制御負荷情報
        result.setProductControlLoadList(
                getResultList(productControlLoadListServiceDaoImpl, productParam).stream().
                //制御負荷でソート
                        sorted(Comparator.comparing(rs -> rs.getControlLoad(), Comparator.naturalOrder()))
                        .collect(Collectors.toList()));

        return result;
    }

}
