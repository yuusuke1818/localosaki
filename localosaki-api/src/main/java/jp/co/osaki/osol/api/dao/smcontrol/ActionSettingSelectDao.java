package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.ActionSettingSelectParameter;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200071Param;

/**
 * 動作モード(取得) Dao クラス
 * @author nakao.h
 *
 */
@Stateless
public class ActionSettingSelectDao extends BaseSmControlDao {

    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;

    public ActionSettingSelectDao() {
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
    }

    /**
     * 機器テーブルから取得する
     * @param param
     * @param res
     * @throws Exception
     */
    public void updateSmPrm(ActionSettingSelectParameter param, FvpCtrlMngResponse<A200071Param> res) throws Exception {

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
