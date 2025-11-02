package jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.UpdateSmsConcentratorParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.concentrator.UpdateSmsConcentratorRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.ListSmsConcentratorsResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.concentrator.MConcentratorServiceDaoImpl;
import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK;

/**
 * コンセントレータ管理 コンセントレータ一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class UpdateSmsConcentratorsDao extends OsolApiDao<UpdateSmsConcentratorParameter> {

    private final MConcentratorServiceDaoImpl mConcentratorDaoImpl;

    public UpdateSmsConcentratorsDao() {
        mConcentratorDaoImpl = new MConcentratorServiceDaoImpl();
    }

    @Override
    public ListSmsConcentratorsResult query(UpdateSmsConcentratorParameter parameter) throws Exception {

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        String devId = parameter.getUpdateSmsConcentratorRequest().getDevId();
        List<UpdateSmsConcentratorRequestSet> requestList = parameter.getUpdateSmsConcentratorRequest().getConcentratorList();
        List<ListSmsConcentratorsResultData> resultConcentratorsList = new ArrayList<>();
        if (requestList != null && requestList.size() > 0) {
            for (UpdateSmsConcentratorRequestSet requestSet : requestList) {
                // クエリ生成時に必要なパラメータを設定
                MConcentrator target = new MConcentrator();
                MConcentratorPK targetPk = new MConcentratorPK();
                targetPk.setDevId(devId);
                targetPk.setConcentId(requestSet.getConcentId());
                target.setId(targetPk);

                MConcentrator entity = find(mConcentratorDaoImpl, target);
                if (entity == null) {
                    //追加
                    target.setRecDate(serverDateTime);
                    target.setRecMan(parameter.getLoginPersonId());
                    target.setCommandFlg(requestSet.getCommandFlg());
                    target.setSrvEnt(requestSet.getSrvEnt());
                    target.setIpAddr(requestSet.getIpAddr());
                    target.setConcentSta(BigDecimal.ZERO);
                    target.setName(requestSet.getName());
                    target.setMemo(requestSet.getMemo());
                    target.setVersion(1);
                    target.setCreateUserId(loginUserId);
                    target.setCreateDate(serverDateTime);
                    target.setUpdateUserId(loginUserId);
                    target.setUpdateDate(serverDateTime);

                    // 新規登録
                    persist(mConcentratorDaoImpl, target);

                    resultConcentratorsList.add(setResultData(find(mConcentratorDaoImpl, target)));
                } else {
                    //更新
                    //排他チェック
                    if (entity.getVersion().equals(requestSet.getVersion())) {
                        entity.setRecDate(serverDateTime);
                        entity.setRecMan(parameter.getLoginPersonId());
                        entity.setCommandFlg(requestSet.getCommandFlg());
                        entity.setSrvEnt(requestSet.getSrvEnt());
                        entity.setIpAddr(requestSet.getIpAddr());
                        entity.setConcentSta(requestSet.getConcentSta());
                        entity.setName(requestSet.getName());
                        entity.setMemo(requestSet.getMemo());
                        entity.setVersion(entity.getVersion() + 1);
                        entity.setUpdateUserId(loginUserId);
                        entity.setUpdateDate(serverDateTime);

                        merge(mConcentratorDaoImpl, entity);

                        resultConcentratorsList.add(setResultData(find(mConcentratorDaoImpl, entity)));
                    } else {
                        throw new OptimisticLockException();
                    }

                }
            }
        }

        ListSmsConcentratorsResult result = new ListSmsConcentratorsResult();
        result.setConcentratorList(resultConcentratorsList);
        return result;
    }

    private ListSmsConcentratorsResultData setResultData (MConcentrator entity) {
        ListSmsConcentratorsResultData resultData = new ListSmsConcentratorsResultData();
        resultData.setDevId(entity.getId().getDevId());
        resultData.setConcentId(entity.getId().getConcentId());
        resultData.setRecDate(entity.getRecDate());
        resultData.setRecMan(entity.getRecMan());
        resultData.setCommandFlg(entity.getCommandFlg());
        resultData.setSrvEnt(entity.getSrvEnt());
        resultData.setIpAddr(entity.getIpAddr());
        resultData.setConcentSta(entity.getConcentSta());
        resultData.setName(entity.getName());
        resultData.setMemo(entity.getMemo());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
