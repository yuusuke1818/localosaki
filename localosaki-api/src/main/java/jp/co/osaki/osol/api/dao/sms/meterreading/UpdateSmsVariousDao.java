package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.UpdateSmsVariousParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.UpdateSmsVariousResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.VariousResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.VariousServiceDaoImpl;
import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.MVariousPK;

/**
 * 各種設定情報登録 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class UpdateSmsVariousDao extends OsolApiDao<UpdateSmsVariousParameter> {

    private final VariousServiceDaoImpl variousServiceDaoImpl;

    public UpdateSmsVariousDao() {
        variousServiceDaoImpl = new VariousServiceDaoImpl();
    }

    @Override
    public UpdateSmsVariousResult query(UpdateSmsVariousParameter parameter) throws Exception {
        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        // 更新条件をセット
        MVariousPK targetPk = new MVariousPK();
        targetPk.setCorpId(parameter.getCorpId()); // 企業ID
        targetPk.setBuildingId(parameter.getBuildingId()); // 建物ID

        // 更新データをセット
        MVarious target = new MVarious();
        target.setId(targetPk); // 更新条件
        target.setRecDate(serverDateTime); // REC_DATE
        target.setRecMan(parameter.getLoginPersonId()); // REC_MAN
        target.setSaleTaxRate(parameter.getSaleTaxRate()); // 消費税率
        target.setSaleTaxDeal(parameter.getSaleTaxDeal()); // 消費税扱い
        target.setDecimalFraction(parameter.getDecimalFraction()); // 小数部端数処理
        target.setYearCloseMonth(parameter.getYearCloseMonth()); // 年報締め月
        target.setVersion(parameter.getVersion()); // 排他制御用カラム
        target.setCreateUserId(loginUserId); // 作成ユーザー識別ID ※新規登録時のみ使用
        target.setCreateDate(serverDateTime); // 作成日時 ※新規登録時のみ使用
        target.setUpdateUserId(loginUserId); // 更新ユーザー識別ID
        target.setUpdateDate(serverDateTime); // 更新日時

        // 新規登録 または 更新
        MVarious entity = merge(variousServiceDaoImpl, target);
        if (entity == null) {
            //排他エラー
            throw new OptimisticLockException();
        }

        return new UpdateSmsVariousResult(new VariousResultData(entity));
    }

}
