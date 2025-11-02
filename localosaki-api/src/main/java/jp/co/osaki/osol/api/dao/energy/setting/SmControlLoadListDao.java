/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmControlLoadListParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmControlLoadListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmControlLoadListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmControlLoadListServiceDaoImpl;

/**
 * 機器制御負荷情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class SmControlLoadListDao extends OsolApiDao<SmControlLoadListParameter> {

    private final SmControlLoadListServiceDaoImpl smControlLoadListServiceDaoImpl;

    public SmControlLoadListDao() {
        smControlLoadListServiceDaoImpl = new SmControlLoadListServiceDaoImpl();
    }

    @Override
    public SmControlLoadListResult query(SmControlLoadListParameter parameter) throws Exception {
        SmControlLoadListDetailResultData param = new SmControlLoadListDetailResultData();
        param.setSmId(parameter.getSmId());
        param.setControlLoadFrom(parameter.getControlLoadFrom());
        param.setControlLoadTo(parameter.getControlLoadTo());

        return new SmControlLoadListResult(getResultList(smControlLoadListServiceDaoImpl, param));
    }

}
