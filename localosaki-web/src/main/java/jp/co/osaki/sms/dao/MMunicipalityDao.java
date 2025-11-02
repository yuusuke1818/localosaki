package jp.co.osaki.sms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MMunicipalityServiceDaoImp;

/**
 *
 * @author h-shiba
 */
@Stateless
public class MMunicipalityDao extends SmsDao {

    private final MMunicipalityServiceDaoImp impl;

    public MMunicipalityDao() {
        impl = new MMunicipalityServiceDaoImp();
    }

    public List<MMunicipality> getResultList(
            List<Object> targetPrefectureList,
            List<Object> targetMunicipalityNameList,
            List<Object> targetMunicipalityCodeList) {

        Map<String, List<Object>> parameterMap = new HashMap<>();

        parameterMap.put("prefectureCd", targetPrefectureList);
        parameterMap.put("municipalityName", targetMunicipalityNameList);
        parameterMap.put("municipalityCd", targetMunicipalityCodeList);

        //parameterMap.put("orderBy", orderByList);
        return getResultList(impl, parameterMap);
    }

    public MMunicipality find(String municipalityCd) {
        MMunicipality mmdb = new MMunicipality();
        mmdb.setMunicipalityCd(municipalityCd);

        return find(impl, mmdb);
    }

}
