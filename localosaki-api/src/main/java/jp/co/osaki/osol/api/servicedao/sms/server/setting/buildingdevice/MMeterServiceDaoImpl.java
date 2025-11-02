/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice;

import java.sql.Timestamp;
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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConstants.MMETER_COMMAND_FLG;
import jp.co.osaki.sms.SmsConstants.MMETER_SRV_ENT;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターテーブル ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class MMeterServiceDaoImpl implements BaseServiceDao<MMeter> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<MMeter> update = builder.createCriteriaUpdate(MMeter.class);

        Root<MMeter> root = update.from(MMeter.class);

        Path<Integer> versionPath = root.get("version");
        Expression<Integer> incrementVersion = builder.sum((Integer) 1, versionPath);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(root.get(MMeter_.id).get(MMeterPK_.devId).in(map.get(SmsConstants.RERATION_KEY_NAME.DEV_ID.getVal())));
        whereList.add(builder.notLike(root.get(MMeter_.id).get(MMeterPK_.devId), DEVICE_KIND.HANDY.getVal() + "%"));
        whereList.add(builder.equal(root.get(MMeter_.delFlg), OsolConstants.FLG_OFF));

        update
        .set(root.get(MMeter_.commandFlg), MMETER_COMMAND_FLG.DELETE.getVal())
        .set(root.get(MMeter_.srvEnt), MMETER_SRV_ENT.WAIT.getVal())
        .set(root.get(MMeter_.updateUserId), (Long)map.get("updateUserId").get(0))
        .set(root.get(MMeter_.updateDate), (Timestamp)map.get("updateDate").get(0))
        .set(versionPath, incrementVersion)
        .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(update).executeUpdate();

    }

    @Override
    public List<MMeter> getResultList(MMeter t, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = builder
                .createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> whereList = new ArrayList<>();

        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), t.getId().getDevId()));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeter> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeter> getResultList(List<MMeter> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeter> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeter find(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMeter merge(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
