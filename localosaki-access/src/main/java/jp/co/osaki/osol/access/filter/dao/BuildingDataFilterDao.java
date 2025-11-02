package jp.co.osaki.osol.access.filter.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.BuildingDataFilterServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 建物データフィルターDaoクラス
 *
 * @author take_suzuki
 */
@Stateless
public class BuildingDataFilterDao extends AbstractDataFilterDao<BuildingDataFilterResultSet, PersonDataParam> {

    /**
     * 建物データフィルターServiceDao
     */
    private final BuildingDataFilterServiceDaoImpl buildingDataFilterServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public BuildingDataFilterDao() {
        buildingDataFilterServiceDaoImpl = new BuildingDataFilterServiceDaoImpl();
    }

    @Override
    public <T> List<T> applyDataFilter(List<T> inputList, PersonDataParam personDataParam) {

        List<T> outputList = new ArrayList<>();
        //パラメータ無しの場合はデータなしを返す
        if (personDataParam == null) {
            return outputList;
        }
        if (personDataParam.getLoginCorpId() == null || personDataParam.getLoginCorpId().length() <= 0) {
            return outputList;
        }
        if (personDataParam.getLoginPersonId() == null || personDataParam.getLoginPersonId().length() <= 0) {
            return outputList;
        }
        //データフィルター
        List<BuildingDataFilterResultSet> filterList = this.getDataFilter(personDataParam);
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
    protected List<BuildingDataFilterResultSet> getDataFilter(PersonDataParam param) {

        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(param.getLoginCorpId());
        keyList.put(PersonDataParam.LOGIN_CORP_ID, corpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(param.getLoginPersonId());
        keyList.put(PersonDataParam.LOGIN_PERSON_ID, personIdList);

        List<BuildingDataFilterResultSet> list = getResultList(buildingDataFilterServiceDaoImpl, keyList);
        return list;
    }

}
