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

import jp.co.osaki.osol.api.result.smcontrol.DmYearRepPointResult;
import jp.co.osaki.osol.entity.TDmYearRepPoint;
import jp.co.osaki.osol.entity.TDmYearRepPointPK;
import jp.co.osaki.osol.entity.TDmYearRepPointPK_;
import jp.co.osaki.osol.entity.TDmYearRepPoint_;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class DmYearRepPointServiceDaoImpl implements BaseServiceDao<DmYearRepPointResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<TDmYearRepPoint> update = builder.createCriteriaUpdate(TDmYearRepPoint.class);

        Root<TDmYearRepPoint> root = update.from(TDmYearRepPoint.class);

        // versionの更新を手動
        Path<Integer> versionPath = root.get("version");
        Expression<Integer> incrementVersion = builder.sum((Integer) 1, versionPath);

        // 更新件数
        int count = 0;

        List<Object> paramList = parameterMap.get(SmControlConstants.UNITUPDATE_DM_YEAR_REP_POINT);
        if(paramList != null) {
            for (Object data : paramList) {
                DmYearRepPointResult target = (DmYearRepPointResult)data;

                // 条件に一致するレコードを一括更新
                update
                    .set(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId), target.getSmId())
                    .set(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId), target.getCorpId())
                    .set(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId), target.getBuildingId())
                    .set(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), target.getPointNo())
                    .set(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), target.getYearNo())
                    .set(root.get(TDmYearRepPoint_.pointAvg), target.getPointAvg())
                    .set(root.get(TDmYearRepPoint_.pointMax), target.getPointMax())
                    .set(root.get(TDmYearRepPoint_.updateUserId), target.getUpdateUserId())
                    .set(root.get(TDmYearRepPoint_.updateDate), target.getUpdateDate())
                    .set(versionPath, incrementVersion)
                    .where(
                        builder.and(
                            builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId), target.getCorpId()),
                            builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId), target.getBuildingId()),
                            builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId), target.getSmId()),
                            builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), target.getPointNo()),
                            builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo), target.getYearNo())
                        )
                    );
                count += em.createQuery(update).executeUpdate();
            }
        }

        // 更新件数を返却
        return count;

    }

    @Override
    public List<DmYearRepPointResult> getResultList(DmYearRepPointResult target, EntityManager em) {

        // SQL文構築用
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DmYearRepPointResult> query = builder.createQuery(DmYearRepPointResult.class);
        Root<TDmYearRepPoint> root = query.from(TDmYearRepPoint.class);
        List<Predicate> whereList = new ArrayList<>();

        // where
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId), target.getCorpId() ));
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId), target.getBuildingId() ));
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId), target.getSmId() ));
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo), target.getPointNo() ));
        whereList.add(builder.equal(root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo),target.getYearNo()));

        // sql文構築
        query = query.select(builder.construct(DmYearRepPointResult.class,
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.corpId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.buildingId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.smId),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.yearNo),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.monthNo),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.summaryUnit),
                root.get(TDmYearRepPoint_.id).get(TDmYearRepPointPK_.pointNo))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DmYearRepPointResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DmYearRepPointResult> getResultList(List<DmYearRepPointResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DmYearRepPointResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DmYearRepPointResult find(DmYearRepPointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DmYearRepPointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DmYearRepPointResult merge(DmYearRepPointResult target, EntityManager em) {
        // 更新対象テーブルのPK用エンティティ
        TDmYearRepPointPK entityPk = new TDmYearRepPointPK();


        // 要企業TBLのServiceDAO作成+Resultsetの設定
        entityPk.setCorpId(target.getCorpId());
        entityPk.setBuildingId(target.getBuildingId());
        entityPk.setSmId(target.getSmId());
        entityPk.setYearNo(target.getYearNo());
        entityPk.setMonthNo(target.getMonthNo());
        entityPk.setSummaryUnit(target.getSummaryUnit());
        entityPk.setPointNo(target.getPointNo());


        // 更新対象エンティティ
        TDmYearRepPoint entity = new TDmYearRepPoint();


        // 更新対象レコード
        entity = em.find(TDmYearRepPoint.class, entityPk);

        // 更新対象カラム設定
        entity.setPointAvg(target.getPointAvg());
        entity.setPointMax(target.getPointMax());
        entity.setPointMin(target.getPointMin());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(target.getUpdateDate());

        // 更新処理
        em.merge(entity);

        return null;
    }

    @Override
    public void remove(DmYearRepPointResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




}
