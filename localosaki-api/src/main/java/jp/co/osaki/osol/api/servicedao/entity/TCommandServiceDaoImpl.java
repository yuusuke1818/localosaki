/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * コマンド送信テーブル EntityServiceDaoクラス
 *
 * @author yoneda_y
 */
public class TCommandServiceDaoImpl implements BaseServiceDao<TCommand> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TCommand> getResultList(TCommand t, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = builder.createQuery(TCommand.class);

        Root<TCommand> root = query.from(TCommand.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        if (Objects.nonNull(t.getId().getDevId())) {
            whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), t.getId().getDevId()));
        }
        if (Objects.nonNull(t.getId().getCommand())) {
            whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), t.getId().getCommand()));
        }
        if (Objects.nonNull(t.getId().getRecDate())) {
            whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.recDate), t.getId().getRecDate()));
        }

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.desc(root.get(TCommand_.id).get(TCommandPK_.recDate)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<TCommand> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TCommand> getResultList(List<TCommand> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TCommand> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TCommand find(TCommand t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TCommand t, EntityManager em) {
        em.persist(t);
    }

    @Override
    public TCommand merge(TCommand t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TCommand t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
