package jp.co.osaki.osol.access.filter.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
*
* メーター（SMS）データフィルターDaoクラス
* 担当者が権限のあるメーター（指定した建物装置に紐づくメーターから絞り込み）
*
* @author nishida.t
*/
@Stateless
public class MeterDataFilterDao extends AbstractDataFilterDao<MeterDataFilterResultSet, BuildingPersonDevDataParam> {

    /**
     * メーター（SMS）データフィルターServiceDao
     */
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public MeterDataFilterDao() {
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
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
        /*
        if (param.getDevId() == null || param.getDevId().length() <= 0) {
            return outputList;
        }
        */

        /**
         * データフィルター
        * パラメータ.装置IDを未入力で権限のある建物に紐づく装置のメーター取得 <br />
        * パラメータ.装置IDを入力で権限のある建物に紐づく指定した装置のメーター取得
         */
        List<MeterDataFilterResultSet> filterList = this.getDataFilter(param);

        Method mGetMeterMngId;
        Method mGetDevId;
        Long meterMngId;
        String devId;

        for (Iterator<T> it = inputList.iterator(); it.hasNext();) {
            try {
                T b = it.next();

                if (param.getDevId() != null && param.getDevId().length() >= 1) {
                    // 装置に紐づくメーターでフィルター
                    // メーター管理番号
                    mGetMeterMngId = b.getClass().getMethod("getMeterMngId");
                    mGetMeterMngId.setAccessible(true);
                    meterMngId = Long.parseLong(mGetMeterMngId.invoke(b).toString());
//                    meterMngId = Long.parseLong((b).toString());

                    for (MeterDataFilterResultSet filter : filterList) {
                        if (filter.getMeterMngId().equals(meterMngId) && filter.getDevId().equals(param.getDevId())) {
                            outputList.add(b);
                            break;
                        }
                    }

                } else {
                    // メーター管理番号
                    mGetMeterMngId = b.getClass().getMethod("getMeterMngId");
                    mGetMeterMngId.setAccessible(true);
                    meterMngId = Long.parseLong(mGetMeterMngId.invoke(b).toString());

                    // 装置ID
                    mGetDevId = b.getClass().getMethod("getDevId");
                    mGetDevId.setAccessible(true);
                    devId = String.valueOf(mGetDevId.invoke(b));

                    for (MeterDataFilterResultSet filter : filterList) {
                        if (filter.getMeterMngId().equals(meterMngId) && filter.getDevId().equals(devId)) {
                            outputList.add(b);
                            break;
                        }
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
    protected List<MeterDataFilterResultSet> getDataFilter(BuildingPersonDevDataParam param) {
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

        // 入力されていればセット
        if (param.getDevId() != null && param.getDevId().length() >= 1) {
            // 装置ID
            List<Object> devIdList = new ArrayList<>();
            devIdList.add(param.getDevId());
            keyList.put(BuildingPersonDevDataParam.DEV_ID, devIdList);
        }

        List<MeterDataFilterResultSet> list = getResultList(meterDataFilterServiceDaoImpl, keyList);
        return list;
    }

}
