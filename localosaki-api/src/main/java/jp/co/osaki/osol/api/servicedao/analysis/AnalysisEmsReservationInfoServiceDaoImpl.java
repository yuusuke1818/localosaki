package jp.co.osaki.osol.api.servicedao.analysis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsReservationInfoResultData;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.entity.TAggregateReservationInfo;
import jp.co.osaki.osol.entity.TAggregateReservationInfoPK;
import jp.co.osaki.osol.entity.TAggregateReservationInfoPK_;
import jp.co.osaki.osol.entity.TAggregateReservationInfo_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 集計分析予約情報
 * @author nishida.t
 *
 */
public class AnalysisEmsReservationInfoServiceDaoImpl implements BaseServiceDao<AnalysisEmsReservationInfoResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsReservationInfoResultData> getResultList(AnalysisEmsReservationInfoResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        String personCorpId = target.getPersonCorpId();
        String personId = target.getPersonId();
        Long aggregateId = target.getAggregateId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsReservationInfoResultData> query = builder.createQuery(AnalysisEmsReservationInfoResultData.class);

        Root<TAggregateReservationInfo> root = query.from(TAggregateReservationInfo.class);
        Join<TAggregateReservationInfo, MPerson> joinMPerson = root.join(TAggregateReservationInfo_.MPerson, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // 企業ID（操作企業ID）
        if (corpId != null) {
            whereList.add(builder.equal(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.corpId), corpId));
        }
        // 担当者企業ID
        if (personCorpId != null) {
            whereList.add(builder.equal(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personCorpId), personCorpId));
        }

        // 担当者ID
        if (personId != null) {
            whereList.add(builder.equal(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personId), personId));
        }

        // 集計ID
        if (aggregateId != null) {
            whereList.add(builder.equal(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId), aggregateId));
        }

        // 未削除
        whereList.add(builder.equal(root.get(TAggregateReservationInfo_.delFlg), OsolConstants.FLG_OFF));


        query = query.select(builder.construct(AnalysisEmsReservationInfoResultData.class,
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.corpId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personCorpId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId),
                root.get(TAggregateReservationInfo_.reservationDate),
                root.get(TAggregateReservationInfo_.aggregateCondition),
                root.get(TAggregateReservationInfo_.aggregateProcessStatus),
                root.get(TAggregateReservationInfo_.aggregateProcessResult),
                root.get(TAggregateReservationInfo_.startDate),
                root.get(TAggregateReservationInfo_.endDate),
                root.get(TAggregateReservationInfo_.outputFilePath),
                root.get(TAggregateReservationInfo_.outputFileName),
                joinMPerson.get(MPerson_.personName))).
                where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.desc(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId))
                        );

        return em.createQuery(query).getResultList();
    }

    /**
     * 集計分析EMS DL予約集計実行APIでの集計IDのみでの検索用
     */
    @Override
    public List<AnalysisEmsReservationInfoResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        Long aggregateId = null;
        List<Object> aggregateIdList = parameterMap.get("aggregateId");
        if (aggregateIdList != null && !aggregateIdList.isEmpty()) {
            aggregateId = (Long) aggregateIdList.get(0);
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsReservationInfoResultData> query = builder.createQuery(AnalysisEmsReservationInfoResultData.class);

        Root<TAggregateReservationInfo> root = query.from(TAggregateReservationInfo.class);

        List<Predicate> whereList = new ArrayList<>();

        // 集計ID
        if (aggregateId != null) {
            whereList.add(builder.equal(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId), aggregateId));
        }

        // 処理結果（未処理）
        whereList.add(builder.equal(root.get(TAggregateReservationInfo_.aggregateProcessResult), ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.WAIT.getVal()));
        // 未削除
        whereList.add(builder.equal(root.get(TAggregateReservationInfo_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AnalysisEmsReservationInfoResultData.class,
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.corpId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personCorpId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.personId),
                root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId),
                root.get(TAggregateReservationInfo_.reservationDate),
                root.get(TAggregateReservationInfo_.aggregateCondition),
                root.get(TAggregateReservationInfo_.aggregateProcessStatus),
                root.get(TAggregateReservationInfo_.aggregateProcessResult),
                root.get(TAggregateReservationInfo_.startDate),
                root.get(TAggregateReservationInfo_.endDate),
                root.get(TAggregateReservationInfo_.outputFilePath),
                root.get(TAggregateReservationInfo_.outputFileName),
                root.get(TAggregateReservationInfo_.delFlg),
                root.get(TAggregateReservationInfo_.version),
                root.get(TAggregateReservationInfo_.createUserId),
                root.get(TAggregateReservationInfo_.createDate),
                root.get(TAggregateReservationInfo_.updateUserId),
                root.get(TAggregateReservationInfo_.updateDate))).
                where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(root.get(TAggregateReservationInfo_.id).get(TAggregateReservationInfoPK_.aggregateId))
                        );

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsReservationInfoResultData> getResultList(
            List<AnalysisEmsReservationInfoResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsReservationInfoResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsReservationInfoResultData find(AnalysisEmsReservationInfoResultData target, EntityManager em) {
        // 主キー用エンティティ
        TAggregateReservationInfoPK entityPk = new TAggregateReservationInfoPK();
        entityPk.setCorpId(target.getCorpId());
        entityPk.setPersonCorpId(target.getPersonCorpId());
        entityPk.setPersonId(target.getPersonId());
        entityPk.setAggregateId(target.getAggregateId());

        // 検索用Entity
        TAggregateReservationInfo entity = em.find(TAggregateReservationInfo.class, entityPk);

        // ヒットしなければnullを返却
        if(entity == null){
            return null;
        }

        // 取得したレコード情報をResultクラスにセットする
        target.setReservationDate(entity.getReservationDate());
        target.setAggregateCondition(entity.getAggregateCondition());
        target.setAggregateProcessStatus(entity.getAggregateProcessStatus());
        target.setAggregateProcessResult(entity.getAggregateProcessResult());
        target.setAggregateStartDate(entity.getStartDate());
        target.setAggregateEndDate(entity.getEndDate());
        target.setOutputFilePath(entity.getOutputFilePath());
        target.setOutputFileName(entity.getOutputFileName());
        target.setVersion(entity.getVersion());
        target.setCreateUserId(entity.getCreateUserId());
        target.setUpdateUserId(entity.getUpdateUserId());
        target.setCreateDate(new Timestamp(entity.getCreateDate().getTime()));
        target.setUpdateDate(new Timestamp(entity.getUpdateDate().getTime()));
        return target;
    }

    @Override
    public void persist(AnalysisEmsReservationInfoResultData target, EntityManager em) {
        TAggregateReservationInfoPK entityPk = new TAggregateReservationInfoPK();
        TAggregateReservationInfo entity = new TAggregateReservationInfo();

        entityPk.setCorpId(target.getCorpId());
        entityPk.setPersonCorpId(target.getPersonCorpId());
        entityPk.setPersonId(target.getPersonId());
        entityPk.setAggregateId(target.getAggregateId());

        entity.setId(entityPk);
        entity.setReservationDate(new Timestamp(target.getReservationDate().getTime()));
        entity.setAggregateCondition(target.getAggregateCondition());
        entity.setAggregateProcessStatus(target.getAggregateProcessStatus());
        entity.setAggregateProcessResult(target.getAggregateProcessResult());
        entity.setDelFlg(target.getDelFlg());
        entity.setVersion(target.getVersion());
        entity.setCreateUserId(target.getCreateUserId());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setCreateDate(new Timestamp(target.getCreateDate().getTime()));
        entity.setUpdateDate(new Timestamp(target.getUpdateDate().getTime()));

        em.persist(entity);
    }

    @Override
    public AnalysisEmsReservationInfoResultData merge(AnalysisEmsReservationInfoResultData target, EntityManager em) {
        TAggregateReservationInfoPK entityPk = new TAggregateReservationInfoPK();
        entityPk.setCorpId(target.getCorpId());
        entityPk.setPersonCorpId(target.getPersonCorpId());
        entityPk.setPersonId(target.getPersonId());
        entityPk.setAggregateId(target.getAggregateId());

        TAggregateReservationInfo entity = new TAggregateReservationInfo();
        entity = em.find(TAggregateReservationInfo.class, entityPk);    // emのfindメソッドで設定した主キーのレコードを取得

        if(entity == null) {
            return null;
        }
        entity.setReservationDate(new Timestamp(target.getReservationDate().getTime()));
        entity.setAggregateCondition(target.getAggregateCondition());
        entity.setAggregateProcessStatus(target.getAggregateProcessStatus());
        entity.setAggregateProcessResult(target.getAggregateProcessResult());

        if (target.getAggregateStartDate() != null) {
            entity.setStartDate(new Timestamp(target.getAggregateStartDate().getTime()));
        }

        if (target.getAggregateEndDate() != null) {
            entity.setEndDate(new Timestamp(target.getAggregateEndDate().getTime()));
        }

        entity.setOutputFilePath(target.getOutputFilePath());
        entity.setOutputFileName(target.getOutputFileName());
        entity.setVersion(target.getVersion());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(new Timestamp(target.getUpdateDate().getTime()));

        return target;
    }

    @Override
    public void remove(AnalysisEmsReservationInfoResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
