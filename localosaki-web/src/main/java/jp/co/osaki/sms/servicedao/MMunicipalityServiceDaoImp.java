/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MMunicipality;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * @author h-shiba
 */
public class MMunicipalityServiceDaoImp implements BaseServiceDao<MMunicipality> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMunicipality> getResultList(MMunicipality target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMunicipality> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MMunicipality> criteria = cb.createQuery(MMunicipality.class);
        Root<MMunicipality> municipality = criteria.from(MMunicipality.class);
        List<Predicate> MMunicipalityList = new ArrayList<Predicate>();

        // 都道府県
        List<Object> targetPrefectureList = parameterMap.get("prefectureCd");
        if (targetPrefectureList != null && !targetPrefectureList.isEmpty()) {
            MMunicipalityList.add(municipality.get("MPrefecture").<String> get("prefectureCd").in(targetPrefectureList));
        }
        // 自治体コード
        List<Object> targetMunicipalityCdList = parameterMap.get("municipalityCd");
        if (targetMunicipalityCdList != null && !targetMunicipalityCdList.isEmpty()) {
            for (Object s : targetMunicipalityCdList) {
                MMunicipalityList.add(cb.like(municipality.<String>get("municipalityCd"), BaseUtility.addSqlWildcard((String) s)));
            }
        }
        // 自治体名
        List<Object> targetMunicipalityNameList = parameterMap.get("municipalityName");
        if (targetMunicipalityNameList != null && !targetMunicipalityNameList.isEmpty()) {
            for (Object s : targetMunicipalityNameList) {
                MMunicipalityList.add(cb.like(municipality.<String>get("municipalityName"), BaseUtility.addSqlWildcard((String) s)));
            }
        }

        criteria = criteria.select(municipality).where(cb.and(MMunicipalityList.toArray(new Predicate[]{}))).orderBy(cb.asc(municipality.get("municipalityCd")));
        List<MMunicipality> resultMMunicipality = em.createQuery(criteria).getResultList();
        return resultMMunicipality;
    }

    @Override
    public List<MMunicipality> getResultList(List<MMunicipality> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMunicipality find(MMunicipality target, EntityManager em) {
        MMunicipality reselt = em.find(MMunicipality.class, target.getMunicipalityCd());
        return reselt;
    }

    @Override
    public void persist(MMunicipality target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MMunicipality merge(MMunicipality target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MMunicipality target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMunicipality> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
