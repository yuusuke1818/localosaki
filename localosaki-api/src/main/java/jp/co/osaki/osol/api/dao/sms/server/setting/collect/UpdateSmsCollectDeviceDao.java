/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.collect;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.collect.UpdateSmsCollectDeviceParameter;
import jp.co.osaki.osol.api.result.sms.server.setting.collect.UpdateSmsCollectDeviceResult;
import jp.co.osaki.osol.api.servicedao.entity.MDevPrmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TCommandServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.sms.SmsConstants;

/**
 * 装置情報 追加、更新 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class UpdateSmsCollectDeviceDao extends OsolApiDao<UpdateSmsCollectDeviceParameter> {

    private final MDevPrmServiceDaoImpl mDevPrmServiceDaoImpl;
    private final TCommandServiceDaoImpl tCommandServiceDaoImpl;

    public UpdateSmsCollectDeviceDao() {
        mDevPrmServiceDaoImpl = new MDevPrmServiceDaoImpl();
        tCommandServiceDaoImpl = new TCommandServiceDaoImpl();
    }

    @Override
    public UpdateSmsCollectDeviceResult query(UpdateSmsCollectDeviceParameter parameter) throws Exception {

        //ユーザー識別IDを取得
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();
        String personId = mPerson.getId().getPersonId();

        //DBサーバ時刻取得
        Timestamp serverDateTime = getServerDateTime();
        Timestamp serverDateTime2 = getServerDateTime();

        // 装置情報
        MDevPrm mDevPrm = new MDevPrm();
        mDevPrm.setDevId(parameter.getDevId());
        MDevPrm findMDevPrm = find(mDevPrmServiceDaoImpl, mDevPrm);

        UpdateSmsCollectDeviceResult result = null;

        if (Objects.isNull(findMDevPrm)) {


			if (Objects.isNull(mPerson)) {
				personId = "LTE-Term Auto Regist";
				userId = (long)0;
			}



            // 追加
            mDevPrm.setDevKind(SmsConstants.DEVICE_PRM_KIND.MUDM2.getVal());
            mDevPrm.setRecDate(serverDateTime);
            mDevPrm.setRecMan(personId);
            mDevPrm.setDevPw(parameter.getDevPw());
            mDevPrm.setDevSta(new BigDecimal(0));
            mDevPrm.setName(parameter.getName());
            mDevPrm.setMemo(parameter.getMemo());
            mDevPrm.setIpAddr(parameter.getIpAddr());
            mDevPrm.setHomeDirectory(parameter.getHomeDirectory());
            mDevPrm.setExamNoticeMonth(parameter.getExamNoticeMonth());
            mDevPrm.setAlertDisableFlg(parameter.getAlertDisableFlg());
            mDevPrm.setRevFlg(parameter.getRevFlg());
            mDevPrm.setCommInterval(parameter.getCommInterval());
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
            findMDevPrm.setMemo(parameter.getMemo());
            findMDevPrm.setIpAddr(parameter.getIpAddr());
            findMDevPrm.setHomeDirectory(parameter.getHomeDirectory());
            findMDevPrm.setExamNoticeMonth(parameter.getExamNoticeMonth());
            findMDevPrm.setAlertDisableFlg(parameter.getAlertDisableFlg());
            findMDevPrm.setRevFlg(parameter.getRevFlg());
            findMDevPrm.setCommInterval(parameter.getCommInterval());
            findMDevPrm.setDelFlg(parameter.getDelFlg());
            findMDevPrm.setVersion(parameter.getVersion());
            findMDevPrm.setUpdateUserId(userId);
            findMDevPrm.setUpdateDate(serverDateTime);
            MDevPrm res = merge(mDevPrmServiceDaoImpl, findMDevPrm);
            result = new UpdateSmsCollectDeviceResult(res);
        }

        // 装置情報
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPk = new TCommandPK();
        tCommandPk.setDevId(parameter.getDevId());
        tCommandPk.setCommand(SmsConstants.CMD_KIND.SET_INTERVAL.getVal());
        tCommand.setId(tCommandPk);

        List<TCommand> tCommandList = getResultList(tCommandServiceDaoImpl, tCommand);

        // NULL or 空 or 処理フラグが処理待ち、電文送信中状態ではない場合は、コマンド送信を追加
        if (CollectionUtils.isEmpty(tCommandList) ||
                !(Objects.equals(tCommandList.get(0).getSrvEnt(), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())
                        || Objects.equals(tCommandList.get(0).getSrvEnt(),
                                SmsConstants.TCOMMAND_SRV_ENT.SENDING.getVal()))) {
            tCommand = new TCommand();
            tCommandPk = new TCommandPK();
            tCommandPk.setDevId(parameter.getDevId());
            tCommandPk.setCommand(SmsConstants.CMD_KIND.SET_INTERVAL.getVal());
            tCommandPk.setRecDate(serverDateTime);
            tCommand.setId(tCommandPk);
            tCommand.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
            tCommand.setTag(parameter.getCommInterval().toString());
            tCommand.setRecMan(personId);
            tCommand.setVersion(0);
            tCommand.setCreateUserId(userId);
            tCommand.setCreateDate(serverDateTime);
            tCommand.setUpdateUserId(userId);
            tCommand.setUpdateDate(serverDateTime);
            persist(tCommandServiceDaoImpl, tCommand);
        }

        return result;
    }
}
