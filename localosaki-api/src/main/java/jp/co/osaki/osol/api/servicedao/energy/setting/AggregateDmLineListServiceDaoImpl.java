package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.AggregateDmLineListDetailResultData;
import jp.co.osaki.osol.entity.MAggregateDm;
import jp.co.osaki.osol.entity.MAggregateDmLine;
import jp.co.osaki.osol.entity.MAggregateDmLinePK_;
import jp.co.osaki.osol.entity.MAggregateDmLine_;
import jp.co.osaki.osol.entity.MAggregateDmPK_;
import jp.co.osaki.osol.entity.MAggregateDm_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 集計デマンド系統情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class AggregateDmLineListServiceDaoImpl implements BaseServiceDao<AggregateDmLineListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmLineListDetailResultData> getResultList(AggregateDmLineListDetailResultData target,
            EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        Long lineGroupId = target.getLineGroupId();
        String lineNo = target.getLineNo();
        String aggregateDmCorpId = target.getAggregateDmCorpId();
        Long aggregateDmBuildingId = target.getAggregateDmBuildingId();
        Long aggregateDmId = target.getAggregateDmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AggregateDmLineListDetailResultData> query = builder
                .createQuery(AggregateDmLineListDetailResultData.class);

        Root<MAggregateDmLine> root = query.from(MAggregateDmLine.class);
        Join<MAggregateDmLine, MAggregateDm> joinAggregateDm = root.join(MAggregateDmLine_.MAggregateDm);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.corpId), corpId));

        //建物ID
        if (buildingId != null) {
            whereList
                    .add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.buildingId), buildingId));
        }
        //系統グループID
        if (lineGroupId != null) {
            whereList.add(
                    builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineGroupId), lineGroupId));
        }
        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineNo), lineNo));
        }

        //集計デマンド企業ID
        if (!CheckUtility.isNullOrEmpty(aggregateDmCorpId)) {
            whereList.add(builder.equal(joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.corpId),
                    aggregateDmCorpId));
        }

        //集計デマンド建物ID
        if (aggregateDmBuildingId != null) {
            whereList.add(builder.equal(joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.buildingId),
                    aggregateDmBuildingId));
        }

        //集計デマンドID
        if (aggregateDmId != null) {
            whereList.add(builder.equal(joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.aggregateDmId),
                    aggregateDmId));
        }

        //削除フラグ（集計デマンド）
        whereList.add(builder.equal(joinAggregateDm.get(MAggregateDm_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AggregateDmLineListDetailResultData.class,
                root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.corpId),
                root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.buildingId),
                root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineGroupId),
                root.get(MAggregateDmLine_.id).get(MAggregateDmLinePK_.lineNo),
                joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.corpId),
                joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.buildingId),
                joinAggregateDm.get(MAggregateDm_.id).get(MAggregateDmPK_.aggregateDmId),
                root.get(MAggregateDmLine_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AggregateDmLineListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmLineListDetailResultData> getResultList(List<AggregateDmLineListDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AggregateDmLineListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AggregateDmLineListDetailResultData find(AggregateDmLineListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AggregateDmLineListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AggregateDmLineListDetailResultData merge(AggregateDmLineListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AggregateDmLineListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
