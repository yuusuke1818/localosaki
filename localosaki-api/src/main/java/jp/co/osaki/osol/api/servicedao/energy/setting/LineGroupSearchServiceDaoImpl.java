/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.entity.MLineGroup;
import jp.co.osaki.osol.entity.MLineGroupPK_;
import jp.co.osaki.osol.entity.MLineGroup_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統グループ取得ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class LineGroupSearchServiceDaoImpl implements BaseServiceDao<LineGroupSearchDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineGroupSearchDetailResultData> getResultList(LineGroupSearchDetailResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        String lineGroupType = t.getLineGroupType();
        Long buildingId = t.getBuildingId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LineGroupSearchDetailResultData> query = builder
                .createQuery(LineGroupSearchDetailResultData.class);

        Root<MLineGroup> root = query.from(MLineGroup.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MLineGroup_.id).get(MLineGroupPK_.corpId), corpId));
        //系統グループ区分
        if (!CheckUtility.isNullOrEmpty(lineGroupType)) {
            whereList.add(builder.equal(root.get(MLineGroup_.lineGroupType), lineGroupType));
        }
        //建物ID
        if (CheckUtility.isNullOrEmpty(lineGroupType)) {
            if (buildingId != null) {
                whereList.add(builder.or(
                        builder.and(
                                builder.equal(root.get(MLineGroup_.lineGroupType),
                                        ApiCodeValueConstants.LINE_GROUP_TYPE.CORP_STANDARD.getVal()),
                                builder.isNull(root.get(MLineGroup_.buildingId))),
                        builder.and(
                                builder.equal(root.get(MLineGroup_.lineGroupType),
                                        ApiCodeValueConstants.LINE_GROUP_TYPE.MEMBER_SETTING.getVal()),
                                builder.equal(root.get(MLineGroup_.buildingId), buildingId))));
            }
        } else if (ApiCodeValueConstants.LINE_GROUP_TYPE.CORP_STANDARD.getVal().equals(lineGroupType)) {
            whereList.add(builder.isNull(root.get(MLineGroup_.buildingId)));
        } else if (ApiCodeValueConstants.LINE_GROUP_TYPE.MEMBER_SETTING.getVal().equals(lineGroupType)) {
            if (buildingId != null) {
                whereList.add(builder.equal(root.get(MLineGroup_.buildingId), buildingId));
            }
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MLineGroup_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(LineGroupSearchDetailResultData.class,
                root.get(MLineGroup_.id).get(MLineGroupPK_.corpId),
                root.get(MLineGroup_.id).get(MLineGroupPK_.lineGroupId),
                root.get(MLineGroup_.lineGroupName),
                root.get(MLineGroup_.lineGroupType),
                root.get(MLineGroup_.initialViewFlg),
                root.get(MLineGroup_.buildingId),
                root.get(MLineGroup_.delFlg),
                root.get(MLineGroup_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<LineGroupSearchDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineGroupSearchDetailResultData> getResultList(List<LineGroupSearchDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineGroupSearchDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineGroupSearchDetailResultData find(LineGroupSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(LineGroupSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineGroupSearchDetailResultData merge(LineGroupSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(LineGroupSearchDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
