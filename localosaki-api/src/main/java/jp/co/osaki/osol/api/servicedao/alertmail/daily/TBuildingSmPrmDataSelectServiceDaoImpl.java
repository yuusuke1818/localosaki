package jp.co.osaki.osol.api.servicedao.alertmail.daily;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TBuildingSmPrmCheckListResultData;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK_;
import jp.co.osaki.osol.entity.MBuildingSm_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.entity.MSmPrmEx;
import jp.co.osaki.osol.entity.MSmPrmEx_;
import jp.co.osaki.osol.entity.MSmPrm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物機器データ取得 ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class TBuildingSmPrmDataSelectServiceDaoImpl implements BaseServiceDao<TBuildingSmPrmCheckListResultData> {

    /**
     * チェック対象となる建物機器データ取得
     *
     * @param parameterMap パラメータマップ
     * @param em エンティティ
     * @return 建物・テナントリスト
     */
    @Override
    public List<TBuildingSmPrmCheckListResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {

        List<Object> targetDateList = parameterMap.get("targetDate");

        if (targetDateList == null || targetDateList.size() != 1) {
            return new ArrayList<TBuildingSmPrmCheckListResultData>();
        }

        Date targetDay = DateUtility.conversionDate(
                DateUtility.changeDateFormat((Date)targetDateList.get(0), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH),
                DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TBuildingSmPrmCheckListResultData> query = builder
                .createQuery(TBuildingSmPrmCheckListResultData.class);

        // 取得元テーブル
        Root<MCorp> rootMCorp = query.from(MCorp.class);

        // 結合テーブル
        Join<MCorp, TBuilding> joinTBuilding = rootMCorp.join(MCorp_.TBuildings, JoinType.INNER);
        Join<TBuilding, MBuildingSm> joinMBuildingSm = joinTBuilding.join(TBuilding_.MBuildingSm, JoinType.INNER);
        Join<MBuildingSm, MSmPrm> joinMSmPrm = joinMBuildingSm.join(MBuildingSm_.MSmPrm, JoinType.INNER);
        Join<MSmPrm, MSmPrmEx> joinMSmPrmEx = joinMSmPrm.join(MSmPrm_.MSmPrmEx, JoinType.LEFT);

        // 結合条件（ON句）
        // ※建物が稼働停止していても機器が稼働している場合があるので建物の期間は見ない（機器の期間は見る）
        joinTBuilding.on(
                builder.equal(rootMCorp.get(MCorp_.corpId), joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId)),
                builder.equal(joinTBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        joinMBuildingSm.on(
                builder.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                        joinMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.corpId)),
                builder.equal(joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                        joinMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.buildingId)));

        joinMSmPrm.on(
                builder.equal(joinMBuildingSm.get(MBuildingSm_.id).get(MBuildingSmPK_.smId), joinMSmPrm.get(MSmPrm_.smId)),
                builder.equal(joinMSmPrm.get(MSmPrm_.delFlg), OsolConstants.FLG_OFF),
                builder.and(
                        builder.or(
                                builder.isNull(joinMSmPrm.get(MSmPrm_.startDate)),
                                builder.lessThanOrEqualTo(joinMSmPrm.get(MSmPrm_.startDate), targetDay))),
                builder.and(
                        builder.or(
                                builder.isNull(joinMSmPrm.get(MSmPrm_.endDate)),
                                builder.greaterThanOrEqualTo(joinMSmPrm.get(MSmPrm_.endDate), targetDay))));

        joinMSmPrmEx.on(
                builder.equal(joinMSmPrm.get(MSmPrm_.smId), joinMSmPrmEx.get(MSmPrmEx_.smId)),
                builder.equal(joinMSmPrmEx.get(MSmPrmEx_.delFlg), OsolConstants.FLG_OFF));

        // SQL発行
        query = query.select(builder.construct(TBuildingSmPrmCheckListResultData.class,
                rootMCorp.get(MCorp_.corpName),
                rootMCorp.get(MCorp_.corpId),
                joinTBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinTBuilding.get(TBuilding_.buildingNo),
                joinTBuilding.get(TBuilding_.buildingName),
                joinMSmPrm.get(MSmPrm_.smId),
                joinMSmPrm.get(MSmPrm_.smAddress),
                joinMSmPrm.get(MSmPrm_.ipAddress),
                joinMSmPrmEx.get(MSmPrmEx_.nothingDataNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.kwCalcNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.tempHumidSensorNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.maxLimitValNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.dmTargetNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.lineValMinusNotCheckFlg),
                joinMSmPrmEx.get(MSmPrmEx_.waterOilLeakNotCheckFlg)))
                .orderBy(builder.asc(rootMCorp.get(MCorp_.corpId)), builder.asc(joinTBuilding.get(TBuilding_.buildingNo)),
                        builder.asc(joinMSmPrm.get(MSmPrm_.smAddress)));

        // 取得結果を返却
        return em.createQuery(query).getResultList();

    }

    @Override
    public List<TBuildingSmPrmCheckListResultData> getResultList(TBuildingSmPrmCheckListResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TBuildingSmPrmCheckListResultData target, EntityManager em) {
    }

    @Override
    public void remove(TBuildingSmPrmCheckListResultData target, EntityManager em) {
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingSmPrmCheckListResultData> getResultList(List<TBuildingSmPrmCheckListResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildingSmPrmCheckListResultData find(TBuildingSmPrmCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TBuildingSmPrmCheckListResultData merge(TBuildingSmPrmCheckListResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TBuildingSmPrmCheckListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
