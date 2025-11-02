package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.DevPrmDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.MDevPrmListResultSet;
import jp.co.osaki.sms.servicedao.MDevPrmListServiceDaoImpl;

/**
 * 装置情報Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class MDevPrmListDao extends SmsDao {

    private final MDevPrmListServiceDaoImpl daoImpl;

    /**
     * 装置フィルター
     */
    @EJB
    private DevPrmDataFilterDao devPrmDataFilterDao;

    public MDevPrmListDao() {
        daoImpl = new MDevPrmListServiceDaoImpl();
    }

    /**
     * 装置情報検索.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 装置情報List
     */
    public List<MDevPrm> getDevPrmList(String corpId, Long buildingId) {
        return getDevPrmList(corpId, buildingId, false);
    }

    /**
     * 装置情報検索.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報List
     */
    public List<MDevPrm> getDevPrmList(String corpId, Long buildingId, boolean isTenant) {
        MDevPrmListResultSet dbParam = new MDevPrmListResultSet();
        dbParam.setCorpId(corpId);
        dbParam.setBuildingId(buildingId);
        dbParam.setTenant(isTenant);

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(MDevPrmListServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));

        List<MDevPrm> resultList = getResultList(daoImpl, parameterMap);

        // ログイン担当者情報取得
        MPerson person = (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());

        // 担当者が権限のある装置に絞り込む
        resultList = devPrmDataFilterDao.applyDataFilter(resultList, new BuildingPersonDevDataParam(
                corpId, buildingId, person.getId().getCorpId(), person.getId().getPersonId()));

        return resultList;
    }

    /**
     * 装置情報取得.
     *
     * @param devId 装置ID
     * @return 装置情報
     */
    public MDevPrm getDevPrm(String devId) {
        MDevPrm dbParam = new MDevPrm();
        dbParam.setDevId(devId);

        return find(daoImpl, dbParam);
    }
}
