package jp.co.osaki.osol.api.servicedao.osolapi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.osolapi.OsolApiUseResultListDetailResultData;
import jp.co.osaki.osol.entity.TApiUseResult;
import jp.co.osaki.osol.entity.TApiUseResultPK;
import jp.co.osaki.osol.entity.TApiUseResultPK_;
import jp.co.osaki.osol.entity.TApiUseResult_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class OsolApiUseResultServiceDaoImpl implements BaseServiceDao<OsolApiUseResultListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<OsolApiUseResultListDetailResultData> getResultList(OsolApiUseResultListDetailResultData target, EntityManager em) {
        String corpId = target.getCorpId();
        String apiKind = target.getApiKind();
        Date useDateFrom = target.getUseDateFrom();
        Date useDateTo = target.getUseDateTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<OsolApiUseResultListDetailResultData> query = builder.createQuery(OsolApiUseResultListDetailResultData.class);

        Root<TApiUseResult> root = query.from(TApiUseResult.class);

        List<Predicate> whereList = new ArrayList<>();

        // 企業ID
        if (corpId != null) {
            whereList.add(builder.equal(root.get(TApiUseResult_.id).get(TApiUseResultPK_.corpId), target.getCorpId()));
        }
        // API分類
        if (apiKind != null) {
            whereList.add(builder.equal(root.get(TApiUseResult_.id).get(TApiUseResultPK_.apiKind), target.getApiKind()));
        }

        // 利用日時From
        if (useDateFrom != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TApiUseResult_.id).get(TApiUseResultPK_.useDate), useDateFrom));
        }
        // 利用日時To
        if (useDateTo != null) {
            whereList.add(builder.lessThanOrEqualTo(root.get(TApiUseResult_.id).get(TApiUseResultPK_.useDate), useDateTo));
        }

        query = query.select(builder.construct(OsolApiUseResultListDetailResultData.class,
                root.get(TApiUseResult_.id).get(TApiUseResultPK_.corpId),
                root.get(TApiUseResult_.id).get(TApiUseResultPK_.useDate),
                root.get(TApiUseResult_.id).get(TApiUseResultPK_.apiKind),
                root.get(TApiUseResult_.apiCount),
                root.get(TApiUseResult_.version))).
                where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<OsolApiUseResultListDetailResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<OsolApiUseResultListDetailResultData> getResultList(List<OsolApiUseResultListDetailResultData> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<OsolApiUseResultListDetailResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public OsolApiUseResultListDetailResultData find(OsolApiUseResultListDetailResultData target, EntityManager em) {
        // 主キー用エンティティ
        TApiUseResultPK entityPK = new TApiUseResultPK();
        entityPK.setCorpId(target.getCorpId());
        entityPK.setUseDate(target.getUseDate());
        entityPK.setApiKind(target.getApiKind());

        // 検索用Entity
        TApiUseResult entity = em.find(TApiUseResult.class, entityPK);

        // ヒットしなければnullを返却
        if(entity == null){
            return null;
        }

        // 取得したレコード情報をResultクラスにセットする
        target.setApiCount(entity.getApiCount());
        target.setVersion(entity.getVersion());
        return target;
    }

    @Override
    public void persist(OsolApiUseResultListDetailResultData target, EntityManager em) {
        TApiUseResultPK entityPk = new TApiUseResultPK();
        TApiUseResult entity = new TApiUseResult();

        entityPk.setCorpId(target.getCorpId());
        entityPk.setUseDate(target.getUseDate());
        entityPk.setApiKind(target.getApiKind());
        entity.setId(entityPk);
        entity.setApiCount(target.getApiCount());
        entity.setVersion(target.getVersion());
        entity.setCreateUserId(target.getCreateUserId());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setCreateDate(new Timestamp(target.getCreateDate().getTime()));
        entity.setUpdateDate(new Timestamp(target.getUpdateDate().getTime()));

        em.persist(entity);
    }

    @Override
    public OsolApiUseResultListDetailResultData merge(OsolApiUseResultListDetailResultData target, EntityManager em) {
        TApiUseResultPK entityPk = new TApiUseResultPK();

        // 探したい主キーをentityPkに設定
        entityPk.setCorpId(target.getCorpId());
        entityPk.setUseDate(target.getUseDate());
        entityPk.setApiKind(target.getApiKind());

        TApiUseResult entity = new TApiUseResult();
        entity = em.find(TApiUseResult.class, entityPk);    // emのfindメソッドで設定した主キーのレコードを取得

        if(entity == null) {
            return null;
        }

        entity.setApiCount(target.getApiCount());
        entity.setVersion(target.getVersion());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(new Timestamp(target.getUpdateDate().getTime()));

        return null;
    }

    @Override
    public void remove(OsolApiUseResultListDetailResultData target, EntityManager em) {

    }
}
