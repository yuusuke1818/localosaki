/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice;

import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.GetSmsDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.GetSmsDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;

/**
 * 装置情報取得 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class GetSmsDeviceDao extends OsolApiDao<GetSmsDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;

    public GetSmsDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
    }

    @Override
    public GetSmsDeviceResult query(GetSmsDeviceParameter parameter) throws Exception {
        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        GetSmsDeviceResult result = null;
        if (Objects.nonNull(findMDevPrm)) {
            result = new GetSmsDeviceResult(findMDevPrm);
        }
        return result;
    }

}
