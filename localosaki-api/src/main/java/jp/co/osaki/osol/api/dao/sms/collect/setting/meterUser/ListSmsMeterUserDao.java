package jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.ListSmsMeterUserParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterUser.ListSmsMeterUserConditionRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.ListSmsMeterUserResult;
import jp.co.osaki.osol.api.resultdata.sms.meterUser.SearchMeterUserResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterUser.MeterUserSearchServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

@Stateless
public class ListSmsMeterUserDao extends OsolApiDao<ListSmsMeterUserParameter> {

    // メーターユーザー（担当者）
    private final MeterUserSearchServiceDaoImpl meterUserSearchServiceDaoImpl;

    public ListSmsMeterUserDao() {
        meterUserSearchServiceDaoImpl = new MeterUserSearchServiceDaoImpl();
    }

    @Override
    public ListSmsMeterUserResult query(ListSmsMeterUserParameter parameter) throws Exception {

        ListSmsMeterUserResult result = new ListSmsMeterUserResult();
        ListSmsMeterUserConditionRequestSet  condition = parameter.getRequest().getCondition();

        List<Object> corpIdList = new ArrayList<>();
        if (parameter.getRequest().getCorpId() != null) {
            corpIdList.add(parameter.getRequest().getCorpId());
        }

        List<Object> personIdOrNameList = new ArrayList<>();
        if (condition.getPersonIdOrName() != null) {
            personIdOrNameList.add(condition.getPersonIdOrName());
        }

        List<Object> personKanaList = new ArrayList<>();
        if (condition.getPersonKana() != null) {
            personKanaList.add(condition.getPersonKana());
        }

        List<Object> deptNameList = new ArrayList<>();
        if (condition.getDeptName() != null) {
            deptNameList.add(condition.getDeptName());
        }

        List<Object> positionNameList = new ArrayList<>();
        if (condition.getPositionName() != null) {
            positionNameList.add(condition.getPositionName());
        }

        String nowDate = DateUtility.changeDateFormat(getServerDateTime(), DateUtility.DATE_FORMAT_YYYYMMDD);
        Date now = DateUtility.conversionDate(nowDate, DateUtility.DATE_FORMAT_YYYYMMDD);
        List<Object> svDateList = new ArrayList<>();
        svDateList.add(nowDate);

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put("personIdOrName", personIdOrNameList);
        parameterMap.put("personKana", personKanaList);
        parameterMap.put("deptName", deptNameList);
        parameterMap.put("positionName", positionNameList);
        parameterMap.put("corpId", corpIdList);
        parameterMap.put("svDate", svDateList);

        List<SearchMeterUserResultData> searchResultList = getResultList(meterUserSearchServiceDaoImpl, parameterMap);
        result.setSearchResultList(searchResultList);

        // 検索結果が０件
        if (searchResultList.isEmpty()) {
            return result;
        }

        // アカウント状態リストが０件
        if (condition.getAccountStatusList().isEmpty()) {
            return result;
        }

        List<SearchMeterUserResultData> deleteTarget = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            // 選択されていない条件のみ結果から削除（選択された条件の結果のみ返すため）
            for (SearchMeterUserResultData target : searchResultList) {

                if (!condition.getAccountStatusList().contains(String.valueOf(i))) {
                    switch (i) {
                        case 1:
                            // 正常が選択されていない場合
                            if (target.getPassMissCount() < OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT
                                    && target.getAccountStopFlg().equals(OsolConstants.FLG_OFF)
                                    && target.getLastLoginDate() != null
                                    && target.getTempPassExpirationDate() == null) {

                                deleteTarget.add(target);
                            }
                            break;
                        case 2:
                            // 未ログインが選択されていない場合
                            if (target.getAccountStopFlg().equals(OsolConstants.FLG_OFF)
                                    && target.getPassMissCount() < OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT
                                    && (target.getTempPassExpirationDate() != null && now.compareTo(target.getTempPassExpirationDate()) <= 0)) {

                                deleteTarget.add(target);
                            }
                            break;

                        case 3:
                            // ロック(期限切れ)が選択されていない場合
                            if ((target.getTempPassExpirationDate() != null && now.compareTo(target.getTempPassExpirationDate()) > 0)
                                    && target.getAccountStopFlg().equals(OsolConstants.FLG_OFF)
                                    && target.getPassMissCount() < OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {

                                deleteTarget.add(target);
                            }
                            break;

                        case 4:
                            // ロック(入力ミス)が選択されていない場合
                            if (target.getAccountStopFlg().equals(OsolConstants.FLG_OFF)
                                    && target.getPassMissCount() >= OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {

                                deleteTarget.add(target);
                            }
                            break;

                        case 5:
                            // アカウント停止が選択されていない場合
                            if (target.getAccountStopFlg().equals(OsolConstants.FLG_ON)) {

                                deleteTarget.add(target);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // アカウント状態リストにないものを削除
        for (SearchMeterUserResultData del : deleteTarget) {
            searchResultList.remove(del);
        }

        return result;
    }
}
