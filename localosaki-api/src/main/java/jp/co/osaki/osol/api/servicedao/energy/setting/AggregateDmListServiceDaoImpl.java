package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmListDetailResultData;
import jp.co.osaki.osol.entity.MAggregateDm;
import jp.co.osaki.osol.entity.MAggregateDmPK_;
import jp.co.osaki.osol.entity.MAggregateDm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 集計デマンド情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class AggregateDmListServiceDaoImpl implements BaseServiceDao<AggregateDmListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmListDetailResultData> getResultList(AggregateDmListDetailResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        Long aggregateDmId = target.getAggregateDmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AggregateDmListDetailResultData> query = builder
                .createQuery(AggregateDmListDetailResultData.class);

        Root<MAggregateDm> root = query.from(MAggregateDm.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MAggregateDm_.id).get(MAggregateDmPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(root.get(MAggregateDm_.id).get(MAggregateDmPK_.buildingId), buildingId));
        }
        //集計デマンドID
        if (aggregateDmId != null) {
            whereList.add(builder.equal(root.get(MAggregateDm_.id).get(MAggregateDmPK_.aggregateDmId), aggregateDmId));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MAggregateDm_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AggregateDmListDetailResultData.class,
                root.get(MAggregateDm_.id).get(MAggregateDmPK_.corpId),
                root.get(MAggregateDm_.id).get(MAggregateDmPK_.buildingId),
                root.get(MAggregateDm_.id).get(MAggregateDmPK_.aggregateDmId),
                root.get(MAggregateDm_.sumDate),
                root.get(MAggregateDm_.aggregateDmName),
                root.get(MAggregateDm_.delFlg),
                root.get(MAggregateDm_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AggregateDmListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmListDetailResultData> getResultList(List<AggregateDmListDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AggregateDmListDetailResultData find(AggregateDmListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AggregateDmListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AggregateDmListDetailResultData merge(AggregateDmListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AggregateDmListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
