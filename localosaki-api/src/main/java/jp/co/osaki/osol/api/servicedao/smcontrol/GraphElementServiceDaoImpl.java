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

import jp.co.osaki.osol.api.result.smcontrol.GraphElementResult;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK;
import jp.co.osaki.osol.entity.MGraphElementPK_;
import jp.co.osaki.osol.entity.MGraphElement_;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GraphElementServiceDaoImpl implements BaseServiceDao<GraphElementResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<MGraphElement> update = builder.createCriteriaUpdate(MGraphElement.class);

        Root<MGraphElement> root = update.from(MGraphElement.class);

        // versionの更新を手動
        Path<Integer> versionPath = root.get("version");
        Expression<Integer> incrementVersion = builder.sum((Integer) 1, versionPath);

        // 更新件数
        int count = 0;

        List<Object> paramList = parameterMap.get(SmControlConstants.UNITUPDATE_GRAPH_ELEMENT);
        if(paramList != null) {
            for (Object data : paramList) {
                GraphElementResult target = (GraphElementResult)data;

                // 条件に一致するレコードを一括更新
                update
                    .set(root.get(MGraphElement_.id).get(MGraphElementPK_.corpId), target.getCorpId())
                    .set(root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId), target.getBuildingId())
                    .set(root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId), target.getLineGroupId())
                    .set(root.get(MGraphElement_.delFlg), target.getDelFlg())
                    .set(root.get(MGraphElement_.updateUserId), target.getUpdateUserId())
                    .set(root.get(MGraphElement_.updateDate), target.getUpdateDate())
                    .set(versionPath, incrementVersion)
                    .where(
                        builder.and(
                            builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.corpId), target.getCorpId()),
                            builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId), target.getBuildingId()),
                            builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId), target.getLineGroupId()),
                            builder.notEqual(root.get(MGraphElement_.updateDate), target.getUpdateDate())
                        )
                    );
                count += em.createQuery(update).executeUpdate();
            }
        }

        // 更新件数を返却
        return count;

    }

    @Override
    public List<GraphElementResult> getResultList(GraphElementResult target, EntityManager em) {

        // SQL文構築用
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GraphElementResult> query = builder.createQuery(GraphElementResult.class);
        Root<MGraphElement> root = query.from(MGraphElement.class);
        List<Predicate> whereList = new ArrayList<>();

        // where
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.corpId), target.getCorpId() ));
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId), target.getBuildingId() ));
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId), target.getLineGroupId() ));

        // sql文構築
        query = query.select(builder.construct(GraphElementResult.class,
                root.get(MGraphElement_.id).get(MGraphElementPK_.corpId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.graphId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.graphElementId))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }


    @Override
    public List<GraphElementResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GraphElementResult> getResultList(List<GraphElementResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GraphElementResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GraphElementResult find(GraphElementResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(GraphElementResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GraphElementResult merge(GraphElementResult target, EntityManager em) {
        // 更新対象テーブルのPK用エンティティ
        MGraphElementPK entityPk = new MGraphElementPK();


        // 要企業TBLのServiceDAO作成+Resultsetの設定
        entityPk.setCorpId(target.getCorpId());
        entityPk.setBuildingId(target.getBuildingId());
        entityPk.setLineGroupId(target.getLineGroupId());
        entityPk.setGraphId(target.getGraphId());
        entityPk.setGraphElementId(target.getGraphElementId());

        // 更新対象エンティティ
        MGraphElement entity = new MGraphElement();

        // 更新対象レコード
        entity = em.find(MGraphElement.class, entityPk);

        // 更新対象カラム設定
        entity.setDelFlg(target.getDelFlg());
        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(target.getUpdateDate());

        // 更新処理
        em.merge(entity);

        return null;
    }


    @Override
    public void remove(GraphElementResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
