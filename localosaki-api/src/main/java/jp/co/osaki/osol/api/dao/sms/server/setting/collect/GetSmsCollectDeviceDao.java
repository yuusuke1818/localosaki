/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.collect;

import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.GetSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.GetSmsCollectDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;

/**
 * 装置情報取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class GetSmsCollectDeviceDao extends OsolApiDao<GetSmsCollectDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;

    public GetSmsCollectDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
    }

    @Override
    public GetSmsCollectDeviceResult query(GetSmsCollectDeviceParameter parameter) throws Exception {
        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        GetSmsCollectDeviceResult result = null;
        if (Objects.nonNull(findMDevPrm)) {
            result = new GetSmsCollectDeviceResult(findMDevPrm);
        }
        return result;
    }

}
