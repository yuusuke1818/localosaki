/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpDemandSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.CorpDemandSelectServiceDaoImpl;

/**
 * 企業デマンド取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class CorpDemandSelectDao extends OsolApiDao<CorpDemandSelectParameter> {

    private final CorpDemandSelectServiceDaoImpl corpDemandSelectServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public CorpDemandSelectDao() {
        corpDemandSelectServiceDaoImpl = new CorpDemandSelectServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public CorpDemandSelectResult query(CorpDemandSelectParameter parameter) throws Exception {
        CorpDemandSelectResult result = new CorpDemandSelectResult();
        CorpDemandSelectResult param = new CorpDemandSelectResult();
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();

        // 排他企業情報を取得する
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        // フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new CorpDemandSelectResult();
        }

        // 企業デマンド情報を取得する
        param.setCorpId(parameter.getOperationCorpId());
        List<CorpDemandSelectResult> list = getResultList(corpDemandSelectServiceDaoImpl, param);

        // 結果を設定する
        result.setCorpId(exList.get(0).getCorpId());
        result.setCorpDemandVersion(exList.get(0).getVersion());

        if (list != null && list.size() == 1) {
            result.setSumDate(list.get(0).getSumDate());
            result.setWeekClosingDayOfWeek(list.get(0).getWeekClosingDayOfWeek());
            result.setWeekStartDay(list.get(0).getWeekStartDay());
            result.setCorpDemandVersion(list.get(0).getCorpDemandVersion());
        }

        return result;
    }

}
