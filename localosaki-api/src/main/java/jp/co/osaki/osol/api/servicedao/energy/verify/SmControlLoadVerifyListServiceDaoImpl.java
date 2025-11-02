package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlLoadVerifyListDetailResultData;
import jp.co.osaki.osol.entity.MSmControlLoadVerify;
import jp.co.osaki.osol.entity.MSmControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmControlLoadVerify_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御負荷検証取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlLoadVerifyListServiceDaoImpl implements BaseServiceDao<SmControlLoadVerifyListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadVerifyListDetailResultData> getResultList(SmControlLoadVerifyListDetailResultData target,
            EntityManager em) {
        Long smId = target.getSmId();
        BigDecimal controlLoad = target.getControlLoad();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlLoadVerifyListDetailResultData> query = builder
                .createQuery(SmControlLoadVerifyListDetailResultData.class);

        Root<MSmControlLoadVerify> root = query.from(MSmControlLoadVerify.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.smId), smId));
        }
        //制御負荷
        if (controlLoad != null) {
            whereList.add(builder.equal(root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.controlLoad),
                    controlLoad));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MSmControlLoadVerify_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmControlLoadVerifyListDetailResultData.class,
                root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.smId),
                root.get(MSmControlLoadVerify_.id).get(MSmControlLoadVerifyPK_.controlLoad),
                root.get(MSmControlLoadVerify_.controlLoadRunningHours),
                root.get(MSmControlLoadVerify_.delFlg),
                root.get(MSmControlLoadVerify_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlLoadVerifyListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadVerifyListDetailResultData> getResultList(
            List<SmControlLoadVerifyListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlLoadVerifyListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlLoadVerifyListDetailResultData find(SmControlLoadVerifyListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlLoadVerifyListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlLoadVerifyListDetailResultData merge(SmControlLoadVerifyListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlLoadVerifyListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
