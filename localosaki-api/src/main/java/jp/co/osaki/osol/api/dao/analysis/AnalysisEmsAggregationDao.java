package jp.co.osaki.osol.api.dao.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsReservationInfoResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsReservationInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiPersonServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

/**
 * 集計・分析 EMS実績 予約情報集計処理 Daoクラス
 * @author nishida.t
 *
 */
@Stateless
public class AnalysisEmsAggregationDao extends BaseApiDao {

    private final AnalysisEmsReservationInfoServiceDaoImpl analysisEmsReservationInfoServiceDaoImpl;

    private final OsolApiPersonServiceDaoImpl osolApiPersonServiceDaoImpl;

    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;

    public AnalysisEmsAggregationDao() {
        analysisEmsReservationInfoServiceDaoImpl = new AnalysisEmsReservationInfoServiceDaoImpl();
        osolApiPersonServiceDaoImpl = new OsolApiPersonServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();

    }

    /**
     * 集計分析予約情報を取得（集計ID）
     * @param aggregateId
     * @return
     */
    public List<AnalysisEmsReservationInfoResultData> getAnalysisEmsReservationInfoResultList(Long aggregateId) {
        Map<String, List<Object>> parameterMap = new HashMap<String, List<Object>>();
        parameterMap.put("aggregateId", new ArrayList<>(Arrays.asList(aggregateId)));
        List<AnalysisEmsReservationInfoResultData> resultList = getResultList(analysisEmsReservationInfoServiceDaoImpl, parameterMap);
        return resultList;
    }

    /**
     * 担当者情報取得
     * @param corpId
     * @param personId
     * @return
     */
    public MPerson getMPerson(String corpId, String personId) {
        MPerson mPerson = new MPerson();
        MPersonPK mPersonPk = new MPersonPK();
        mPersonPk.setCorpId(corpId);
        mPersonPk.setPersonId(personId);
        mPerson.setId(mPersonPk);
        return find(osolApiPersonServiceDaoImpl, mPerson);
    }

    /**
     * 企業情報取得
     * @param corpId
     * @return
     */
    public MCorp getMCorp(String corpId) {
        MCorp mCorp = new MCorp();
        mCorp.setCorpId(corpId);
        MCorp result = find(mCorpApiServiceDaoImpl, mCorp);
        return result;
    }

    /**
     * 集計分析予約情報を１件取得
     * @param param
     * @return
     */
    public AnalysisEmsReservationInfoResultData find(AnalysisEmsReservationInfoResultData target) {
        AnalysisEmsReservationInfoResultData result = find(analysisEmsReservationInfoServiceDaoImpl, target);
        return result;
    }

    /**
     * 集計分析予約情報を更新
     * @param target
     * @return
     */
    public AnalysisEmsReservationInfoResultData merge(AnalysisEmsReservationInfoResultData target) {
        // 更新実行
        AnalysisEmsReservationInfoResultData result = merge(analysisEmsReservationInfoServiceDaoImpl, target);
        return result;
    }

}
