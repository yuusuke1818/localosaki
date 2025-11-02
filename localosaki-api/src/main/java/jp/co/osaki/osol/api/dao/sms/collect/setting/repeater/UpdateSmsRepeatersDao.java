package jp.co.osaki.osol.api.dao.sms.collect.setting.repeater;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.repeater.UpdateSmsRepeaterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.repeater.UpdateSmsRepeaterRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.repeater.ListSmsRepeatersResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.repeater.MRepeaterServiceDaoImpl;
import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.MRepeaterPK;

/**
 * コンセントレータ管理 コンセントレータ一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class UpdateSmsRepeatersDao extends OsolApiDao<UpdateSmsRepeaterParameter> {

    private final MRepeaterServiceDaoImpl mRepeaterServiceDaoImpl;

    public UpdateSmsRepeatersDao() {
        mRepeaterServiceDaoImpl = new MRepeaterServiceDaoImpl();
    }

    @Override
    public ListSmsRepeatersResult query(UpdateSmsRepeaterParameter parameter) throws Exception {

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        String devId = parameter.getUpdateSmsRepeaterRequest().getDevId();
        List<UpdateSmsRepeaterRequestSet> requestList = parameter.getUpdateSmsRepeaterRequest().getRepeaterList();
        List<ListSmsRepeatersResultData> resultRepeatersList = new ArrayList<>();
        if (requestList != null && requestList.size() > 0) {
            for (UpdateSmsRepeaterRequestSet requestSet : requestList) {
                // クエリ生成時に必要なパラメータを設定
                MRepeater target = new MRepeater();
                MRepeaterPK targetPk = new MRepeaterPK();
                targetPk.setDevId(devId);
                targetPk.setRepeaterMngId(requestSet.getRepeaterMngId());
                target.setId(targetPk);

                MRepeater entity = find(mRepeaterServiceDaoImpl, target);
                if (entity == null) {
                    //追加
                    target.setRecDate(serverDateTime);
                    target.setRecMan(parameter.getLoginPersonId());
                    target.setCommandFlg(requestSet.getCommandFlg());
                    target.setSrvEnt(requestSet.getSrvEnt());
                    target.setRepeaterId(requestSet.getRepeaterId());
                    target.setMemo(requestSet.getMemo());
                    target.setVersion(1);
                    target.setCreateUserId(loginUserId);
                    target.setCreateDate(serverDateTime);
                    target.setUpdateUserId(loginUserId);
                    target.setUpdateDate(serverDateTime);

                    // 新規登録
                    persist(mRepeaterServiceDaoImpl, target);

                    resultRepeatersList.add(setResultData(find(mRepeaterServiceDaoImpl, target)));
                } else {
                    //更新
                    //排他チェック
                    if (entity.getVersion().equals(requestSet.getVersion())) {
                        entity.setRecDate(serverDateTime);
                        entity.setRecMan(parameter.getLoginPersonId());
                        entity.setCommandFlg(requestSet.getCommandFlg());
                        entity.setSrvEnt(requestSet.getSrvEnt());
                        entity.setRepeaterId(requestSet.getRepeaterId());
                        entity.setMemo(requestSet.getMemo());
                        entity.setVersion(entity.getVersion() + 1);
                        entity.setUpdateUserId(loginUserId);
                        entity.setUpdateDate(serverDateTime);

                        merge(mRepeaterServiceDaoImpl, entity);

                        resultRepeatersList.add(setResultData(find(mRepeaterServiceDaoImpl, entity)));
                    } else {
                        throw new OptimisticLockException();
                    }

                }
            }
        }

        ListSmsRepeatersResult result = new ListSmsRepeatersResult();
        result.setRepeaterList(resultRepeatersList);
        return result;
    }

    private ListSmsRepeatersResultData setResultData (MRepeater entity) {
        ListSmsRepeatersResultData resultData = new ListSmsRepeatersResultData();
        resultData.setDevId(entity.getId().getDevId());
        resultData.setRepeaterMngId(entity.getId().getRepeaterMngId());
        resultData.setRecDate(entity.getRecDate());
        resultData.setRecMan(entity.getRecMan());
        resultData.setCommandFlg(entity.getCommandFlg());
        resultData.setSrvEnt(entity.getSrvEnt());
        resultData.setRepeaterId(entity.getRepeaterId());
        resultData.setMemo(entity.getMemo());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
