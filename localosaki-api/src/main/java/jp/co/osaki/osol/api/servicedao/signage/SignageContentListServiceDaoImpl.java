/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.signage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.signage.SignageApkFileListDetailResultData;
import jp.co.osaki.osol.entity.TSignageContent;
import jp.co.osaki.osol.entity.TSignageContentPK_;
import jp.co.osaki.osol.entity.TSignageContent_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class SignageContentListServiceDaoImpl implements BaseServiceDao<SignageApkFileListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SignageApkFileListDetailResultData> getResultList(SignageApkFileListDetailResultData t, EntityManager em) {

        String corpId = t.getCorpId();
        long buildingId = t.getBuildingId();
        String signageContentsType = t.getSignageContentsType();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SignageApkFileListDetailResultData> query = cb.createQuery(SignageApkFileListDetailResultData.class);
        Root<TSignageContent> signageContentRoot = query.from(TSignageContent.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(signageContentRoot.get(TSignageContent_.delFlg), 0));
        whereList.add(cb.equal(signageContentRoot.get(TSignageContent_.id).get(TSignageContentPK_.corpId), corpId));
        whereList.add(cb.equal(signageContentRoot.get(TSignageContent_.id).get(TSignageContentPK_.buildingId), buildingId));
        if (signageContentsType != null && !"".equals(signageContentsType)) {
            whereList.add(cb.equal(signageContentRoot.get(TSignageContent_.signageContentsType), signageContentsType));
        }
        query = query.select(cb.construct(SignageApkFileListDetailResultData.class,
                signageContentRoot.get(TSignageContent_.id).get(TSignageContentPK_.corpId),
                signageContentRoot.get(TSignageContent_.id).get(TSignageContentPK_.buildingId),
                signageContentRoot.get(TSignageContent_.id).get(TSignageContentPK_.signageContentsId),
                signageContentRoot.get(TSignageContent_.signageContentsType),
                signageContentRoot.get(TSignageContent_.displayStartTime),
                signageContentRoot.get(TSignageContent_.displayEndTime),
                signageContentRoot.get(TSignageContent_.displayOrder),
                signageContentRoot.get(TSignageContent_.specifySunday),
                signageContentRoot.get(TSignageContent_.specifyMonday),
                signageContentRoot.get(TSignageContent_.specifyTuesday),
                signageContentRoot.get(TSignageContent_.specifyWednesday),
                signageContentRoot.get(TSignageContent_.specifyThursday),
                signageContentRoot.get(TSignageContent_.specifyFriday),
                signageContentRoot.get(TSignageContent_.specifySaturday),
                signageContentRoot.get(TSignageContent_.title),
                signageContentRoot.get(TSignageContent_.message),
                signageContentRoot.get(TSignageContent_.fontSize),
                signageContentRoot.get(TSignageContent_.fontColorCode),
                signageContentRoot.get(TSignageContent_.imageFilePath),
                signageContentRoot.get(TSignageContent_.imageFileName),
                signageContentRoot.get(TSignageContent_.delFlg),
                signageContentRoot.get(TSignageContent_.version),
                signageContentRoot.get(TSignageContent_.createUserId),
                signageContentRoot.get(TSignageContent_.createDate),
                signageContentRoot.get(TSignageContent_.updateUserId),
                signageContentRoot.get(TSignageContent_.updateDate)))
                .where(cb.and(whereList.toArray(new Predicate[]{})))
                .orderBy(cb.asc(signageContentRoot.get(TSignageContent_.displayOrder)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SignageApkFileListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SignageApkFileListDetailResultData> getResultList(List<SignageApkFileListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SignageApkFileListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SignageApkFileListDetailResultData find(SignageApkFileListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SignageApkFileListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SignageApkFileListDetailResultData merge(SignageApkFileListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SignageApkFileListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
