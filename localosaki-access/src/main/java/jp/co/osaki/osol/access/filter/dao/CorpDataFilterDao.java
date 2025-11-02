package jp.co.osaki.osol.access.filter.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.CorpDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.CorpDataFilterServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 企業データフィルターDaoクラス
 *
 * @author take_suzuki
 */
@Stateless
public class CorpDataFilterDao extends AbstractDataFilterDao<CorpDataFilterResultSet, PersonDataParam> {

    /**
     * 企業データフィルターServiceDao
     */
    private final CorpDataFilterServiceDaoImpl corpDataFilterServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public CorpDataFilterDao() {
        corpDataFilterServiceDaoImpl = new CorpDataFilterServiceDaoImpl();
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
        Method mGetId;
        Method mGetCorpId;
        String sCorpId;
        boolean mGetIdFlg = false;
        List<CorpDataFilterResultSet> filterList = this.getDataFilter(personDataParam);
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
                        sCorpId = String.valueOf(mGetCorpId.invoke(id));
                        for (CorpDataFilterResultSet filter : filterList) {
                            if (filter.getCorpId().equals(sCorpId)) {
                                outputList.add(b);
                                break;
                            }
                        }
                    }
                } else {
                    mGetCorpId = b.getClass().getMethod("getCorpId");
                    mGetCorpId.setAccessible(true);
                    sCorpId = String.valueOf(mGetCorpId.invoke(b));
                    for (CorpDataFilterResultSet filter : filterList) {
                        if (filter.getCorpId().equals(sCorpId)) {
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
    protected List<CorpDataFilterResultSet> getDataFilter(PersonDataParam param) {

        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(param.getLoginCorpId());
        keyList.put(PersonDataParam.LOGIN_CORP_ID, corpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(param.getLoginPersonId());
        keyList.put(PersonDataParam.LOGIN_PERSON_ID, personIdList);

        List<CorpDataFilterResultSet> list = getResultList(corpDataFilterServiceDaoImpl, keyList);
        return list;
    }

}
