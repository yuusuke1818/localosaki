package jp.co.osaki.osol.access.filter.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.DevPrmDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.DevPrmDataFilterServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
*
* 装置データフィルターDaoクラス
*
* @author nishida.t
*/

@Stateless
public class DevPrmDataFilterDao extends AbstractDataFilterDao<DevPrmDataFilterResultSet, BuildingPersonDevDataParam> {

    /**
     * 装置情報 データフィルターServiceDao
     */
    private final DevPrmDataFilterServiceDaoImpl DevPrmDataFilterServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public DevPrmDataFilterDao() {
        DevPrmDataFilterServiceDaoImpl = new DevPrmDataFilterServiceDaoImpl();
    }

    @Override
    public <T> List<T> applyDataFilter(List<T> inputList, BuildingPersonDevDataParam param) {

        List<T> outputList = new ArrayList<>();
        //パラメータ無しの場合はデータなしを返す
        if (param == null) {
            return outputList;
        }
        if (param.getCorpId() == null || param.getCorpId().length() <= 0) {
            return outputList;
        }
        if (param.getBuildingId() == null) {
            return outputList;
        }
        if (param.getLoginCorpId() == null || param.getLoginCorpId().length() <= 0) {
            return outputList;
        }
        if (param.getLoginPersonId() == null || param.getLoginPersonId().length() <= 0) {
            return outputList;
        }

        /**
         * 装置情報フィルター
         */
        List<DevPrmDataFilterResultSet> filterList = this.getDataFilter(param);

        Method mGetDevId;
        String devId;

        for (Iterator<T> it = inputList.iterator(); it.hasNext();) {
            try {
                T b = it.next();

                mGetDevId = b.getClass().getMethod("getDevId");
                mGetDevId.setAccessible(true);
                devId = mGetDevId.invoke(b).toString();

                for (DevPrmDataFilterResultSet filter : filterList) {
                    if (filter.getDevId().equals(devId)) {
                        outputList.add(b);
                        break;
                    }
                }

            } catch (NoSuchMethodException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            } catch (SecurityException | IllegalArgumentException | ReflectiveOperationException ex) {
                errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            }
        }
        return outputList;
    }

    @Override
    protected List<DevPrmDataFilterResultSet> getDataFilter(BuildingPersonDevDataParam param) {

        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(param.getCorpId());
        keyList.put(BuildingPersonDevDataParam.CORP_ID, corpIdList);

        // 建物ID 必須
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(param.getBuildingId());
        keyList.put(BuildingPersonDevDataParam.BUILDING_ID, buildingIdList);

        // ログイン担当者企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(param.getLoginCorpId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_CORP_ID, loginCorpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(param.getLoginPersonId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_PERSON_ID, personIdList);

        List<DevPrmDataFilterResultSet> list = getResultList(DevPrmDataFilterServiceDaoImpl, keyList);
        return list;
    }

}
