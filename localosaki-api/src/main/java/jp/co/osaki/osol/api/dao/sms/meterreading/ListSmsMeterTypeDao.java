package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsMeterTypeParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsMeterTypeResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterTypeResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.AutoInspServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.DevRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterRangeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;

/**
 * 種別設定情報検索 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class ListSmsMeterTypeDao extends OsolApiDao<ListSmsMeterTypeParameter> {

    // 種別設定情報 取得・登録 処理
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メータ種別従量値情報 取得・登録 処理
    private final MeterRangeServiceDaoImpl meterRangeServiceDaoImpl;

    // 自動検針月 日時設定 取得 処理
    private final AutoInspServiceDaoImpl autoInspServiceDaoImpl;

    // 建物、装置関連テーブル 取得 処理
    private final DevRelationServiceDaoImpl devRelationServiceDaoImpl;

    public ListSmsMeterTypeDao() {
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterRangeServiceDaoImpl = new MeterRangeServiceDaoImpl();
        autoInspServiceDaoImpl = new AutoInspServiceDaoImpl();
        devRelationServiceDaoImpl = new DevRelationServiceDaoImpl();
    }

    @Override
    public ListSmsMeterTypeResult query(ListSmsMeterTypeParameter parameter) throws Exception {

        // 種別設定検索条件設定
        MMeterTypePK targetPk = new MMeterTypePK();
        targetPk.setMeterType(null); // メータ種別
        targetPk.setCorpId(parameter.getCorpId()); // 企業ID
        targetPk.setBuildingId(parameter.getBuildingId()); // 建物ID
        targetPk.setMenuNo(null); // メニュー番号

        MMeterType target = new MMeterType();
        target.setId(targetPk);

        // 種別設定検索を行う
        List<MMeterType> entityList = getResultList(meterTypeServiceDaoImpl, target);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }

        // 建物、装置関連テーブル(M_DEV_RELATION) から 装置ID を取得
        MDevRelation devRelationTarget = new MDevRelation();
        devRelationTarget.setId(new MDevRelationPK());
        devRelationTarget.getId().setCorpId(parameter.getCorpId()); // 企業ID
        devRelationTarget.getId().setBuildingId(parameter.getBuildingId()); // 建物ID
        List<MDevRelation> devRelationList = getResultList(devRelationServiceDaoImpl, devRelationTarget);

        // 自動検針月 日時設定(M_AUTO_INSP) から 自動検針日・自動検針時 を取得
        Map<Long, String> autoInspDayMap = new HashMap<Long, String>();
        Map<Long, String> autoInspHourMap = new HashMap<Long, String>();
        if (!devRelationList.isEmpty()) {
            // 検索条件セット
            Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
            targetMap.put(AutoInspServiceDaoImpl.DEV_ID,
                    devRelationList.stream()
                            .map(row -> (Object) row.getId().getDevId())
                            .distinct()
                            .collect(Collectors.toList()));

            // 検索
            List<MAutoInsp> autoInspList = getResultList(autoInspServiceDaoImpl, targetMap);
            autoInspList.stream().forEach(row -> {
                autoInspDayMap.put(row.getId().getMeterType(), row.getDay());
                autoInspHourMap.put(row.getId().getMeterType(), row.getHour().toString());
            });
        }

        // メータ種別従量値索条件設定
        MMeterRangePK targetPkMeterRange = new MMeterRangePK();
        targetPkMeterRange.setMeterType(null); // メーター種別
        targetPkMeterRange.setCorpId(parameter.getCorpId()); // 企業ID
        targetPkMeterRange.setBuildingId(parameter.getBuildingId()); // 建物ID
        targetPkMeterRange.setMenuNo(null); // メニュー番号

        MMeterRange targetMeterRange = new MMeterRange();
        targetMeterRange.setId(targetPkMeterRange);

        // メータ種別従量値情報から従量値 取得
        List<MMeterRange> entityMeterRangeList = getResultList(meterRangeServiceDaoImpl, targetMeterRange);

        // 型変換など ※List<MMeterType> + List<MMeterRange>の従量値 → List<MeterTypeResultData>
        List<MeterTypeResultData> resultList = new ArrayList<MeterTypeResultData>();
        entityList.stream().forEach(mmt -> {
            MMeterTypePK mmtId = mmt.getId();
            resultList.add(new MeterTypeResultData(
                    mmt,
                    autoInspDayMap.get(mmt.getId().getMeterType()), // 自動検針日時（日）（0：日の指定なし  1～31：日の指定）
                    autoInspHourMap.get(mmt.getId().getMeterType()), // 自動検針日時（時）（0～23：時の指定）
                    // 従量値(","区切り) 整形
                    entityMeterRangeList.stream().filter(mmr -> {
                        MMeterRangePK mmrId = mmr.getId();
                        return mmtId.getMeterType().equals(mmrId.getMeterType())
                                && mmtId.getCorpId().equals(mmrId.getCorpId())
                                && mmtId.getBuildingId().equals(mmrId.getBuildingId())
                                && mmtId.getMenuNo() == mmrId.getMenuNo();
                    })
                            .map(mmr -> mmr.getId().getRangeValue().toString())
                            .collect(Collectors.joining(","))

            ));
        });

        return new ListSmsMeterTypeResult(resultList);
    }

}
