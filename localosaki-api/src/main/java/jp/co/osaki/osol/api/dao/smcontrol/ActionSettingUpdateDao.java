package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.ActionSettingUpdateParameter;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200072Param;

/**
 * 動作モード(設定) Dao クラス
 * @author nakao.h
 *
 */
@Stateless
public class ActionSettingUpdateDao extends BaseSmControlDao {

    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;

    public ActionSettingUpdateDao() {
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
    }


    /**
     * 機器テーブルを更新する
     * @param param
     * @param res
     * @throws Exception
     */
    public void updateSmPrm(ActionSettingUpdateParameter param, FvpCtrlMngResponse<A200072Param> res, A200072Param resParam) throws Exception {

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

      //ログインユーザーIDを取得
        Long loginUserId = getMPerson(param.getLoginCorpId(), param.getLoginPersonId()).getUserId();

        MSmPrm parameter = new MSmPrm();

        parameter.setSmId(res.getSmId());

        MSmPrm data = find(mSmPrmServiceDaoImpl, parameter);
        if(data == null) {
            return;
        }

        data.setUpdateUserId(loginUserId);
        data.setUpdateDate(updateDate);
        merge(mSmPrmServiceDaoImpl, data);
    }
}
