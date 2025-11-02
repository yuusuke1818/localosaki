package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.TenantSmsDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.UserCdListResultSet;
import jp.co.osaki.sms.servicedao.MeterTenantSmsInfoListDaoImpl;
import jp.co.osaki.sms.servicedao.UserCdListSearchServiceDaoImpl;

/**
 * ユーザーコード取得Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class UserCdListSearchDao extends SmsDao {

    private final UserCdListSearchServiceDaoImpl userCdListSearchServiceDaoImpl;

    // メーターテナント検索
    private final MeterTenantSmsInfoListDaoImpl meterTenantSmsInfoListDaoImpl;

    // SMSテナントフィルター
    @EJB
    private TenantSmsDataFilterDao tenantSmsDataFilterDao;

    public UserCdListSearchDao() {
        userCdListSearchServiceDaoImpl = new UserCdListSearchServiceDaoImpl();
        meterTenantSmsInfoListDaoImpl = new MeterTenantSmsInfoListDaoImpl();
    }

    /**
     * ユーザーコード取得Daoクラス
     * @param corpId
     * @param buildingId
     * @param loginCorpId
     * @param loginPersonId
     * @return
     */
    public List<UserCdListResultSet> getUserCdList(String corpId, String buildingId, String loginCorpId,
            String loginPersonId) {
        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put("divisionCorpId", Arrays.asList(corpId));
        parameterMap.put(OsolConstants.SEARCH_CONDITION_TENANT_BELONG_TO_BUILDING, Arrays.asList(buildingId));
        parameterMap.put(OsolConstants.SEARCH_CONDITION_BUILDING_TENANT,
                Arrays.asList(OsolConstants.BUILDING_TYPE.TENANT.getVal()));

        // テナント情報を取得する
        List<TBuilding> tenantList = getResultList(meterTenantSmsInfoListDaoImpl, parameterMap);

        // 権限のあるテナントに絞り込み、ユーザーコード情報のリストを返却
        List<UserCdListResultSet> resultList = convertList(tenantSmsDataFilterDao.applyDataFilter(tenantList,
                new BuildingPersonDevDataParam(corpId, Long.valueOf(buildingId), loginCorpId, loginPersonId)));

        // ユーザーコードを昇順にソートする
        Collections.sort(resultList, new Comparator<UserCdListResultSet>() {
            @Override
            public int compare(UserCdListResultSet o1, UserCdListResultSet o2) {
                return Long.valueOf(o1.getTenantId()).compareTo(Long.valueOf(o2.getTenantId())) < 0 ? -1 : 1;
            }
        });

        return resultList;
    }

    /**
     * テナントリストをユーザーコード情報のリストに詰め替える
     *
     * @param orgList
     * @return
     */
    private List<UserCdListResultSet> convertList(List<TBuilding> orgList) {

        List<UserCdListResultSet> userCdList = new ArrayList<>();

        for (TBuilding tBuilding : orgList) {
            if (CollectionUtils.isNotEmpty(tBuilding.getMTenantSms())) {
                UserCdListResultSet userCdListResultSet = new UserCdListResultSet();
                userCdListResultSet.setTenantId(tBuilding.getMTenantSms().get(0).getTenantId());
                userCdListResultSet.setBuildingName(tBuilding.getBuildingName());
                userCdList.add(userCdListResultSet);
            }
        }
        return userCdList;
    }
}
