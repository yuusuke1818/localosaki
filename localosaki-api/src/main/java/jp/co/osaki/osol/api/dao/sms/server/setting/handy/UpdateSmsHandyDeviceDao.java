/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.handy;

import java.sql.Timestamp;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.handy.UpdateSmsHandyDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.handy.UpdateSmsHandyDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsConstants;

/**
 * 装置情報 追加、更新 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class UpdateSmsHandyDeviceDao extends OsolApiDao<UpdateSmsHandyDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;

    public UpdateSmsHandyDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
    }

    @Override
    public UpdateSmsHandyDeviceResult query(UpdateSmsHandyDeviceParameter parameter) throws Exception {

        //ユーザー識別IDを取得
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();
        String personId = mPerson.getId().getPersonId();

        //DBサーバ時刻取得
        Timestamp serverDateTime = getServerDateTime();

        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        UpdateSmsHandyDeviceResult result = null;

        if (Objects.isNull(findMDevPrm)) {
            // 追加
            mDevPrm.setDevKind(SmsConstants.DEVICE_PRM_KIND.MUDM2.getVal());
            mDevPrm.setRecDate(serverDateTime);
            mDevPrm.setRecMan(personId);
            mDevPrm.setDevPw(parameter.getDevPw());
            mDevPrm.setName(parameter.getName());
            mDevPrm.setDelFlg(0);
            mDevPrm.setVersion(0);
            mDevPrm.setCreateUserId(userId);
            mDevPrm.setCreateDate(serverDateTime);
            mDevPrm.setUpdateUserId(userId);
            mDevPrm.setUpdateDate(serverDateTime);
            persist(mDevPrmServiceDaoImpl, mDevPrm);
        } else {
            // 更新
            findMDevPrm.setRecDate(serverDateTime);
            findMDevPrm.setRecMan(personId);
            findMDevPrm.setDevPw(parameter.getDevPw());
            findMDevPrm.setName(parameter.getName());
            findMDevPrm.setDelFlg(parameter.getDelFlg());
            findMDevPrm.setVersion(parameter.getVersion());
            findMDevPrm.setUpdateUserId(userId);
            findMDevPrm.setUpdateDate(serverDateTime);
            MDevPrm res = merge(mDevPrmServiceDaoImpl, findMDevPrm);
            result = new UpdateSmsHandyDeviceResult(res);
        }

        return result;
    }
}
