/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.ListSmsDevRelationResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsDevRelationDetailResultData;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.ListSmsDevRelationServiceDaoImpl;

/**
 * 建物、装置関連 取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class ListSmsDevRelationDao extends OsolApiDao<ListSmsDevRelationParameter> {

    private final ListSmsDevRelationServiceDaoImpl listSmsDevRelationServiceDaoImpl;

    public ListSmsDevRelationDao() {
        listSmsDevRelationServiceDaoImpl = new ListSmsDevRelationServiceDaoImpl();
    }

    @Override
    public ListSmsDevRelationResult query(ListSmsDevRelationParameter parameter) throws Exception {
        ListSmsDevRelationDetailResultData listSmsDevRelationDetailResultData = new ListSmsDevRelationDetailResultData();
        listSmsDevRelationDetailResultData.setCorpId(parameter.getCorpId());
        listSmsDevRelationDetailResultData.setBuildingId(parameter.getBuildingId());
        listSmsDevRelationDetailResultData.setDevId(parameter.getDevId());
        List<ListSmsDevRelationDetailResultData> result =
                getResultList(listSmsDevRelationServiceDaoImpl, listSmsDevRelationDetailResultData);

        return new ListSmsDevRelationResult(result);
    }

}
