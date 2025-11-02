package jp.co.osaki.osol.api.dao.smoperation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationLoadControlResultParameter;
import jp.co.osaki.osol.api.result.servicedao.LoadControlResult;
import jp.co.osaki.osol.api.result.smoperation.SmOperationLoadControlResult;
import jp.co.osaki.osol.api.resultdata.smoperation.SmOperationLoadControlResultData;
import jp.co.osaki.osol.api.servicedao.smoperation.LoadControlResultServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御DB閲覧 負荷制御実績 Daoクラス
 *
 * @author t_hirata
 *
 */
@Stateless
public class SmOperationLoadControlResultDao extends OsolApiDao<SmOperationLoadControlResultParameter> {

    private final LoadControlResultServiceDaoImpl loadControlResultServiceDaoImpl;



    public SmOperationLoadControlResultDao() {
        loadControlResultServiceDaoImpl = new LoadControlResultServiceDaoImpl();
    }



    @Override
    public SmOperationLoadControlResult query(SmOperationLoadControlResultParameter parameter) throws Exception {

        // 機器ID
        Long smId = parameter.getSmId();
        // 対象年月
        String targetYm = parameter.getTargetYm();

        // DB取得
        LoadControlResult param = new LoadControlResult();
        param.setSmId(smId);
        param.setTargetYm(targetYm);
        param.setControlTarget(parameter.getControlTarget());
        List<LoadControlResult> resultList = getResultList(loadControlResultServiceDaoImpl, param);

        if (resultList != null && resultList.size() > 0) {

            // 返却値
            SmOperationLoadControlResult ret = new SmOperationLoadControlResult();

            // ソート
            resultList.sort((LoadControlResult r1, LoadControlResult r2) -> (r1.getLoadControlDate().compareTo(r2.getLoadControlDate())));

            String ctrlYmd = "";
            SmOperationLoadControlResultData data = null;
            for (LoadControlResult res : resultList) {
                // 記録日
                String _ctrlYmd = DateUtility.changeDateFormat(res.getLoadControlDate(), DateUtility.DATE_FORMAT_YYYYMMDD);
                if (!ctrlYmd.equals(_ctrlYmd)) {
                    // 日付が変わった場合
                    if (data != null) {
                        ret.addLoadControlResultList(data);
                    }
                    data = new SmOperationLoadControlResultData();
                    data.setControlYmd(_ctrlYmd);
                    data.setLoadValueList(new ArrayList<>());
                    ctrlYmd = _ctrlYmd;
                }
                List<int[]> loadValueList = data.getLoadValueList();
                if(res.getDailyTotalMinute() != null) {
                    loadValueList.add(new int[]{res.getControlLoad().intValue() ,res.getDailyTotalMinute().intValue()});
                }
            }
            if (data != null) {
                ret.addLoadControlResultList(data);
            }
            return ret;
        }

        return null;
    }

}
