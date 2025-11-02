package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK_;
import jp.co.osaki.osol.entity.MAlertMail_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class listAlertMailServiceDaoImpl implements BaseServiceDao<MAlertMail>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 【生成したいSQL】
//  SELECT
//      email
//    FROM
//      m_alert_mail
//    WHERE
//      dev_id = '' /* $1 */
//      AND alert_manual_insp = '1'
//      AND disabled_flg <> '1'
//    ORDER BY
//      alert_id
    @Override
    public List<MAlertMail> getResultList(MAlertMail target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MAlertMail> query = builder.createQuery(MAlertMail.class);

        // ■from句の整理
        // 建物
        Root<MAlertMail> root = query.from(MAlertMail.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MAlertMail_.id).get(MAlertMailPK_.devId), target.getId().getDevId()));
        whereList.add(builder.equal(root.get(MAlertMail_.alertManualInsp), "1"));
        whereList.add(builder.notEqual(root.get(MAlertMail_.disabledFlg), "1"));

        // ■SELECT句の整理
        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        // ■ORDER BY句の整理
        query.orderBy(
                builder.asc(
                        root.get(MAlertMail_.id).get(MAlertMailPK_.alertId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MAlertMail> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MAlertMail> getResultList(List<MAlertMail> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MAlertMail> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MAlertMail find(MAlertMail target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(MAlertMail target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MAlertMail merge(MAlertMail target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(MAlertMail target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
