package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSettingCollectionParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSettingCollectionResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.CommandResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.CommandServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.DevRelationServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.TCommand;

/**
 * 操作状態一覧 取得（設定一括収集画面） Daoクラス
 * 参照コード
 *  SettingAllCollect.js
 *  SettingCollectCheck.php  検索: getSettingCheck または getSettingCheckMeter
 * @author kobayashi.sho
 */
@Stateless
public class ListSettingCollectionDao extends OsolApiDao<ListSettingCollectionParameter> {

    // 建物、装置関連テーブル ServiceDao
    private final DevRelationServiceDaoImpl devRelationServiceDaoImpl;

    // コマンド送信 ServiceDao
    private final CommandServiceDaoImpl commandServiceDaoImpl;

    public ListSettingCollectionDao() {
        devRelationServiceDaoImpl = new DevRelationServiceDaoImpl();
        commandServiceDaoImpl = new CommandServiceDaoImpl();
    }

    @Override
    public ListSettingCollectionResult query(ListSettingCollectionParameter parameter) throws Exception {
        // 装置IDを取得
        List<String> devIdList = getDevId(parameter.getCorpId(), parameter.getBuildingId());
        if (devIdList == null) {
            return null; // データエラー終了
        }

        // 一括収集の状態取得
        List<CommandResultData> resultList = getSettingCheck(devIdList);

        return new ListSettingCollectionResult(devIdList, resultList);
    }

    /**
     * 接続先の装置IDを取得
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 装置ID
     */
    private List<String> getDevId(String corpId, Long buildingId) {

        MDevRelation target = new MDevRelation();
        target.setId(new MDevRelationPK());
        target.getId().setCorpId(corpId);
        target.getId().setBuildingId(buildingId);

        List<MDevRelation> entity = getResultList(devRelationServiceDaoImpl, target);

        if (entity == null || entity.isEmpty()) {
            return null;
        }

        return entity.stream().map(row -> row.getId().getDevId()).collect(Collectors.toList());
    }

    /**
     * 一括収集の状態取得.
     * @param devIdList 装置ID
     * @return 状態確認
     */
    private List<CommandResultData> getSettingCheck(List<String> devIdList) {
        // 検索条件セット
        Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
        targetMap.put(CommandServiceDaoImpl.DEV_ID,
                devIdList.stream()
                        .map(devId -> (Object) devId)
                        .collect(Collectors.toList()));

        // 検索
        List<TCommand> entityList = getResultList(commandServiceDaoImpl, targetMap);
        if (entityList == null || entityList.isEmpty()) {
            return null; // 該当データなし
        }

        return entityList.stream().map(row -> new CommandResultData(row)).collect(Collectors.toList());
    }

}
