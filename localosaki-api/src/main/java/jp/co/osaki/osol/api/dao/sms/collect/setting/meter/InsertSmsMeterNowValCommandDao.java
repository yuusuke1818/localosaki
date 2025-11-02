package jp.co.osaki.osol.api.dao.sms.collect.setting.meter;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterNowValCommandParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.InsertSmsMeterNowValCommandResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter.TCommandServiceDaoImpl;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.sms.SmsConstants.CMD_KIND;
import jp.co.osaki.sms.SmsConstants.TCOMMAND_SRV_ENT;

/**
 * メーター現在値要求 DAOクラス
 * @author sagi_h
 *
 */
@Stateless
public class InsertSmsMeterNowValCommandDao extends OsolApiDao<InsertSmsMeterNowValCommandParameter> {

    private final TCommandServiceDaoImpl tCommandServiceDaoImpl;

    public InsertSmsMeterNowValCommandDao() {
        tCommandServiceDaoImpl = new TCommandServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public InsertSmsMeterNowValCommandResult query(InsertSmsMeterNowValCommandParameter parameter) throws Exception {
        /** 既存のコマンド */
        TCommand exCommand;

        /** コマンドパラメーター */
        TCommand commandParam = new TCommand();

        /** コマンドパラメーター主キー */
        TCommandPK commandParamPK = new TCommandPK();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        // メーター情報のNULLチェック
        if (parameter.getDevId() == null || parameter.getMeterMngId() == null) {
            return new InsertSmsMeterNowValCommandResult();
        }

        commandParamPK.setDevId(parameter.getDevId());
        commandParamPK.setCommand(CMD_KIND.GET_METERVAL.getVal());
        commandParam.setId(commandParamPK);
        commandParam.setTag(parameter.getMeterMngId());

        // 既存のコマンドがないか確認
        exCommand = find(tCommandServiceDaoImpl, commandParam);

        // 既存のコマンドがあれば削除
        if(exCommand != null) {
            remove(tCommandServiceDaoImpl, exCommand);
        }

        commandParam.getId().setRecDate(serverDateTime);

        commandParam.setSrvEnt(TCOMMAND_SRV_ENT.WAIT.getVal());

        commandParam.setRecMan(parameter.getLoginPersonId());
        commandParam.setCreateUserId(loginUserId);
        commandParam.setCreateDate(serverDateTime);
        commandParam.setUpdateUserId(loginUserId);
        commandParam.setUpdateDate(serverDateTime);

        persist(tCommandServiceDaoImpl, commandParam);

        return getNewCommandInfo(parameter, serverDateTime);
    }

    /**
     * 登録・更新後のエンティティを取得する
     * @param result APIのパラメーター
     * @param serverDateTime recDateの日時
     * @return 更新後のエンティティ
     */
    private InsertSmsMeterNowValCommandResult getNewCommandInfo(InsertSmsMeterNowValCommandParameter result, Timestamp serverDateTime) {
        TCommand commandParam = new TCommand();
        TCommandPK commandParamPK = new TCommandPK();
        commandParamPK.setDevId(result.getDevId());
        commandParamPK.setCommand(CMD_KIND.GET_METERVAL.getVal());
        commandParamPK.setRecDate(serverDateTime);
        commandParam.setId(commandParamPK);
        TCommand newCommand = find(tCommandServiceDaoImpl, commandParam);

        InsertSmsMeterNowValCommandResult newResult = new InsertSmsMeterNowValCommandResult(newCommand.getId().getDevId(), newCommand.getId().getCommand(), newCommand.getId().getRecDate(), newCommand.getCreateDate(),
                newCommand.getCreateUserId(), newCommand.getLinking(), newCommand.getRecMan(), newCommand.getRetryCount(), newCommand.getSrvEnt(), newCommand.getTag(),
                newCommand.getUpdateDate(), newCommand.getUpdateUserId(), newCommand.getVersion());

        return newResult;
    }
}
