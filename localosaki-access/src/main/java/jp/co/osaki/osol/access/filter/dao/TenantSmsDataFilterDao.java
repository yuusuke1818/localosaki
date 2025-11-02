package jp.co.osaki.osol.access.filter.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.TenantSmsDataFilterServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * SMSテナント データフィルターDaoクラス
 *
 * @author nishida.t
 */
@Stateless
public class TenantSmsDataFilterDao extends AbstractDataFilterDao<BuildingDataFilterResultSet, BuildingPersonDevDataParam> {

    /**
     * SMSテナント データフィルターServiceDao
     */
    private final TenantSmsDataFilterServiceDaoImpl tenantSmsDataFilterServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public TenantSmsDataFilterDao() {
        tenantSmsDataFilterServiceDaoImpl = new TenantSmsDataFilterServiceDaoImpl();
    }

    @Override
    public <T> List<T> applyDataFilter(List<T> inputList, BuildingPersonDevDataParam param) {

        List<T> outputList = new ArrayList<>();
        //パラメータ無しの場合はデータなしを返す
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
        //データフィルター
        List<BuildingDataFilterResultSet> filterList = this.getDataFilter(param);
        Method mGetId;
        Method mGetCorpId;
        Method mGetBuildingId;
        String sCorpId;
        Long lBuildingId;
        boolean mGetIdFlg = false;
        for (Iterator<T> it = inputList.iterator(); it.hasNext();) {
            try {
                T b = it.next();
                if (!mGetIdFlg){
                    for (Method me : b.getClass().getMethods()){
                        if (me.getName().equals("getId")){
                            mGetIdFlg = true;
                            break;
                        }
                    }
                }
                if (mGetIdFlg){
                    mGetId = b.getClass().getMethod("getId");
                    if (mGetId != null) {
                        mGetId.setAccessible(true);
                        Object id = mGetId.invoke(b);
                        mGetCorpId = id.getClass().getMethod("getCorpId");
                        mGetCorpId.setAccessible(true);
                        mGetBuildingId = id.getClass().getMethod("getBuildingId");
                        mGetBuildingId.setAccessible(true);
                        sCorpId = String.valueOf(mGetCorpId.invoke(id));
                        lBuildingId = Long.parseLong(mGetBuildingId.invoke(id).toString());
                        for (BuildingDataFilterResultSet filter : filterList) {
                            if (filter.getCorpId().equals(sCorpId) && filter.getBuildingId().equals(lBuildingId)) {
                                outputList.add(b);
                                break;
                            }
                        }
                    }
                } else {
                    mGetCorpId = b.getClass().getMethod("getCorpId");
                    mGetCorpId.setAccessible(true);
                    mGetBuildingId = b.getClass().getMethod("getBuildingId");
                    mGetBuildingId.setAccessible(true);
                    sCorpId = String.valueOf(mGetCorpId.invoke(b));
                    lBuildingId = Long.parseLong(mGetBuildingId.invoke(b).toString());
                    for (BuildingDataFilterResultSet filter : filterList) {
                        if (filter.getCorpId().equals(sCorpId) && filter.getBuildingId().equals(lBuildingId)) {
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
    protected List<BuildingDataFilterResultSet> getDataFilter(BuildingPersonDevDataParam param) {

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

        List<BuildingDataFilterResultSet> list = getResultList(tenantSmsDataFilterServiceDaoImpl, keyList);
        return list;
    }

}
