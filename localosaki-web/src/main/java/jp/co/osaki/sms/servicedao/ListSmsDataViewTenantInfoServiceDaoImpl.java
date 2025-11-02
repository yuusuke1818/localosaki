package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.ListSmsDataViewResultData;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.utility.CriteriaUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * ロードサーベイ テナント情報取得 ServiceDaoクラス
 *
 * @author ozaki.y
 */
public class ListSmsDataViewTenantInfoServiceDaoImpl implements BaseServiceDao<ListSmsDataViewResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(ListSmsDataViewResultData target, EntityManager em) {
        return selectTenantInfoDataList(em, target.getCorpId(), target.getBuildingId(), target.isTenant(),
                target.getDevId(), target.getMeterMngIdList());
    }

    /**
     * テナント情報を取得.
     *
     * @param em EntityManager
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @return 各メーター情報List
     */
    private List<ListSmsDataViewResultData> selectTenantInfoDataList(EntityManager em, String corpId,
            Long buildingId, boolean isTenant, String devId, Collection<Long> meterMngIdClct) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsDataViewResultData> query = builder.createQuery(ListSmsDataViewResultData.class);

        Root<TBuilding> tBuilding = query.from(TBuilding.class);

        Join<TBuilding, TBuildDevMeterRelation> tBuildDevMeterRelation = tBuilding
                .join(TBuilding_.TBuildDevMeterRelations);

        Path<String> devIdPath = tBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                .get(TBuildDevMeterRelationPK_.devId);
        Path<Long> meterMngIdPath = tBuildDevMeterRelation.get(TBuildDevMeterRelation_.id)
                .get(TBuildDevMeterRelationPK_.meterMngId);

        Collection<Predicate> whereClct = new ArrayList<>();
        whereClct.add(builder.equal(tBuilding.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

        if (isTenant) {
            // テナントの場合
            whereClct.add(builder.equal(tBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId), corpId));
            whereClct.add(builder.equal(tBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId), buildingId));

        } else {
            // 建物の場合
            whereClct.add(builder.equal(tBuilding.get(TBuilding_.divisionCorpId), corpId));
            whereClct.add(builder.equal(tBuilding.get(TBuilding_.divisionBuildingId), buildingId));
        }

        whereClct.add(builder.equal(devIdPath, devId));

        if (CollectionUtils.isNotEmpty(meterMngIdClct)) {
            whereClct.addAll(CriteriaUtility.createInCollection(meterMngIdPath, meterMngIdClct));
        }

        query.select(builder.construct(ListSmsDataViewResultData.class,
                tBuilding.get(TBuilding_.id).get(TBuildingPK_.corpId),
                tBuilding.get(TBuilding_.id).get(TBuildingPK_.buildingId),
                devIdPath,
                tBuilding.get(TBuilding_.buildingName),
                meterMngIdPath))
                .where(whereClct.toArray(new Predicate[0]))
                .orderBy(builder.asc(devIdPath), builder.asc(meterMngIdPath));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(List<ListSmsDataViewResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsDataViewResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData find(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsDataViewResultData merge(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsDataViewResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
