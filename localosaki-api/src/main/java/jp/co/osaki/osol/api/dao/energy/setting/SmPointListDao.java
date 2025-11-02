/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmPointListParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmPointListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;

/**
 * 機器ポイント情報取得 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class SmPointListDao extends OsolApiDao<SmPointListParameter> {

    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;

    public SmPointListDao() {
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SmPointListResult query(SmPointListParameter parameter) throws Exception {

        SmPointListDetailResultData param = new SmPointListDetailResultData();
        param.setSmId(parameter.getSmId());
        param.setPointNo(parameter.getPointNo());
        return new SmPointListResult(getResultList(smPointListServiceDaoImpl, param));
    }

}
