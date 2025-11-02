/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.ocr;

import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.ocr.GetSmsOcrDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.ocr.GetSmsOcrDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;

/**
 * 装置情報取得（AieLinkのみ） Daoクラス
 * 「OCR検針」→「AieLink」へ変更
 * @author iwasaki_y
 */
@Stateless
public class GetSmsOcrDeviceDao extends OsolApiDao<GetSmsOcrDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;

    public GetSmsOcrDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
    }

    @Override
    public GetSmsOcrDeviceResult query(GetSmsOcrDeviceParameter parameter) throws Exception {
        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        GetSmsOcrDeviceResult result = null;
        if (Objects.nonNull(findMDevPrm)) {
            result = new GetSmsOcrDeviceResult(findMDevPrm);
        }
        return result;
    }

}
