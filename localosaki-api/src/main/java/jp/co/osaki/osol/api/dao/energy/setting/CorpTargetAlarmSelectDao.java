package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpTargetAlarmSelectDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpTargetAlarmSelectServiceDaoImpl;

/**
 * 企業目標超過警報取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class CorpTargetAlarmSelectDao extends OsolApiDao<CorpTargetAlarmSelectParameter> {

    private final CorpTargetAlarmSelectServiceDaoImpl corpTargetAlarmSelectServiceDaoImpl;

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public CorpTargetAlarmSelectDao() {
        corpTargetAlarmSelectServiceDaoImpl = new CorpTargetAlarmSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public CorpTargetAlarmSelectResult query(CorpTargetAlarmSelectParameter parameter) throws Exception {
        CorpTargetAlarmSelectResult result = new CorpTargetAlarmSelectResult();
        CorpTargetAlarmSelectDetailResultData detailResult = new CorpTargetAlarmSelectDetailResultData();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new CorpTargetAlarmSelectResult();
        }

        //企業目標超過警報情報を取得する
        CorpTargetAlarmSelectDetailResultData detailParam = new CorpTargetAlarmSelectDetailResultData();
        detailParam.setCorpId(parameter.getOperationCorpId());
        List<CorpTargetAlarmSelectDetailResultData> detailList = getResultList(corpTargetAlarmSelectServiceDaoImpl,
                detailParam);

        if (detailList == null || detailList.size() != 1) {
            detailResult = new CorpTargetAlarmSelectDetailResultData();
        } else {
            detailResult = detailList.get(0);
            detailResult.setDetectTime(convTimeToDate(detailResult.getDetectTime()));
        }

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetail(detailResult);

        return result;
    }

    private Date convTimeToDate (Date date) {
        if(date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

}
