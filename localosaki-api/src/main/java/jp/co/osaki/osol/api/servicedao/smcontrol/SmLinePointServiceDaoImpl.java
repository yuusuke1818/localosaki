package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.smcontrol.SmLinePointResult;
import jp.co.osaki.osol.entity.MSmLinePoint;
import jp.co.osaki.osol.entity.MSmLinePointPK;
import jp.co.osaki.osol.entity.MSmLinePointPK_;
import jp.co.osaki.osol.entity.MSmLinePoint_;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class SmLinePointServiceDaoImpl implements BaseServiceDao<SmLinePointResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<MSmLinePoint> update = builder.createCriteriaUpdate(MSmLinePoint.class);

        Root<MSmLinePoint> root = update.from(MSmLinePoint.class);

        // versionの更新を手動
        Path<Integer> versionPath = root.get("version");
        Expression<Integer> incrementVersion = builder.sum((Integer) 1, versionPath);

        // 更新件数
        int count = 0;

        List<Object> paramList = parameterMap.get(SmControlConstants.UNITUPDATE_SM_LINE_POINT);
        if(paramList != null) {
            for (Object data : paramList) {
                SmLinePointResult target = (SmLinePointResult)data;

                // 条件に一致するレコードを一括更新
                update
                    .set(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), target.getSmId())
                    .set(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), target.getCorpId())
                    .set(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), target.getBuildingId())
                    .set(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo), target.getPointNo())
                    .set(root.get(MSmLinePoint_.pointCalcType), target.getPointCalcType())
                    .set(root.get(MSmLinePoint_.updateUserId), target.getUpdateUserId())
                    .set(root.get(MSmLinePoint_.updateDate), target.getUpdateDate())
                    .set(versionPath, incrementVersion)
                    .where(
                        builder.and(
                            builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), target.getCorpId()),
                            builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), target.getBuildingId()),
                            builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), target.getSmId()),
                            builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo), target.getPointNo())
                        )
                    );
                count += em.createQuery(update).executeUpdate();
            }
        }

        // 更新件数を返却
        return count;

    }

    @Override
    public List<SmLinePointResult> getResultList(SmLinePointResult target, EntityManager em) {


        // SQL文構築用
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmLinePointResult> query = builder.createQuery(SmLinePointResult.class);
        Root<MSmLinePoint> root = query.from(MSmLinePoint.class);
        List<Predicate> whereList = new ArrayList<>();

        // where 機器ID,企業ID,建物ID,ポイトNo
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId), target.getSmId() ));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId), target.getCorpId() ));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId), target.getBuildingId() ));
        whereList.add(builder.equal(root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo), target.getPointNo() ));

        // sql文構築
        query = query.select(builder.construct(SmLinePointResult.class,
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.corpId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineGroupId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.lineNo),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.buildingId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.smId),
                root.get(MSmLinePoint_.id).get(MSmLinePointPK_.pointNo))).
                where(builder.and(whereList.toArray(new Predicate[]{})));


        em.createQuery(query).getResultList();
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmLinePointResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointResult> getResultList(List<SmLinePointResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLinePointResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointResult find(SmLinePointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmLinePointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLinePointResult merge(SmLinePointResult target, EntityManager em) {
        // PK用のエンティティ
        MSmLinePointPK entityPk = new MSmLinePointPK();

        // 要企業TBLのServiceDAO作成+Resultsetの設定
        entityPk.setCorpId(target.getCorpId());
        entityPk.setLineGroupId(target.getLineGroupId());
        entityPk.setLineNo(target.getLineNo());
        entityPk.setBuildingId(target.getBuildingId());
        entityPk.setSmId(target.getSmId());
        entityPk.setPointNo(target.getPointNo());

        // 更新対象テーブルのエンティティ
        MSmLinePoint entity = new MSmLinePoint();

        // 更新対象レコード
        entity = em.find(MSmLinePoint.class, entityPk);

        // 更新対象カラム設定
        entity.setPointCalcType(target.getPointCalcType());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(target.getUpdateDate());

        // 更新処理
        em.merge(entity);

        return null;
    }

    @Override
    public void remove(SmLinePointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}