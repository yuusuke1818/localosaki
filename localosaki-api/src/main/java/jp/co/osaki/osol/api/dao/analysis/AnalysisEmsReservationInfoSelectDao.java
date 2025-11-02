package jp.co.osaki.osol.api.dao.analysis;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsReservationInfoSelectParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsReservationInfoSelectResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsReservationInfoResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsReservationInfoServiceDaoImpl;

/**
 * 集計・分析 EMS実績 予約情報取得(DL予約) Daoクラス
 * @author nishida.t
 *
 */
@Stateless
public class AnalysisEmsReservationInfoSelectDao extends OsolApiDao<AnalysisEmsReservationInfoSelectParameter> {

    private final AnalysisEmsReservationInfoServiceDaoImpl analysisEmsReservationInfoServiceDaoImpl;

    public AnalysisEmsReservationInfoSelectDao() {
        analysisEmsReservationInfoServiceDaoImpl = new AnalysisEmsReservationInfoServiceDaoImpl();
    }

    @Override
    public AnalysisEmsReservationInfoSelectResult query(AnalysisEmsReservationInfoSelectParameter parameter) throws Exception {
        AnalysisEmsReservationInfoSelectResult result = new AnalysisEmsReservationInfoSelectResult();

        // 集計分析予約情報を取得
        AnalysisEmsReservationInfoResultData serachData = new AnalysisEmsReservationInfoResultData();
        serachData.setCorpId(parameter.getOperationCorpId());
        List<AnalysisEmsReservationInfoResultData> resultList = getResultList(analysisEmsReservationInfoServiceDaoImpl, serachData);
        result.setAggregateReservationInfoList(resultList);

        return result;
    }
}
