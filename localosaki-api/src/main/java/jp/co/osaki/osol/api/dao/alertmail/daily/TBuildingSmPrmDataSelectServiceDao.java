package jp.co.osaki.osol.api.dao.alertmail.daily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TBuildingSmPrmDataSelectParameter;
import jp.co.osaki.osol.api.result.alertmail.daily.TBuildingSmPrmDataSelectResult;
import jp.co.osaki.osol.api.resultdata.alertmail.daily.TBuildingSmPrmCheckListResultData;
import jp.co.osaki.osol.api.servicedao.alertmail.daily.TBuildingSmPrmDataSelectServiceDaoImpl;

/**
 * 建物機器データ取得 Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class TBuildingSmPrmDataSelectServiceDao extends OsolApiDao<TBuildingSmPrmDataSelectParameter> {

    /**
     * Daoコントロール
     */
    private final TBuildingSmPrmDataSelectServiceDaoImpl tBuildingSmPrmDataSelectServiceDaoImpl;

    public TBuildingSmPrmDataSelectServiceDao() {
        tBuildingSmPrmDataSelectServiceDaoImpl = new TBuildingSmPrmDataSelectServiceDaoImpl();
    }

    @Override
    public TBuildingSmPrmDataSelectResult query(TBuildingSmPrmDataSelectParameter parameter) throws Exception {
        TBuildingSmPrmDataSelectResult result = new TBuildingSmPrmDataSelectResult();

        Map<String, List<Object>> parameterMap = new HashMap<>();

        List<Object> targetDateList = new ArrayList<>();
        // 対象日は前日
        targetDateList.add(parameter.getNowDate());

        parameterMap.put("targetDate", targetDateList);

        // 対象データ取得
        List<TBuildingSmPrmCheckListResultData> resultList = getResultList(tBuildingSmPrmDataSelectServiceDaoImpl,
                parameterMap);
        result.setTBuildingSmPrmCheckListResultDataList(resultList);

        return result;
    }

}