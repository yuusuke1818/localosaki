package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.resultset.MeterDataFilterResultSet;
import jp.co.osaki.osol.access.filter.servicedao.MeterDataFilterServiceDaoImpl;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsInspectionMeterBefResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.InspectionMeterBefResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.BuildDevMeterRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.InspectionMeterBefServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK;

/**
 * 確定前検針データ一覧 取得 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class ListSmsInspectionMeterBefDao extends OsolApiDao<ListSmsInspectionMeterBefParameter> {

    // 確定前検針データ一覧 取得
    private final InspectionMeterBefServiceDaoImpl inspectionMeterBefServiceDaoImpl;

    // 建物、メーター関連テーブル 取得
    private final BuildDevMeterRelationServiceDaoImpl buildDevMeterRelationServiceDaoImpl;

    // 確定前検針データ一覧 取得
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メータ登録用 取得
    private final MeterServiceDaoImpl meterServiceDaoImpl;

    // メーターフィルター
    private final MeterDataFilterServiceDaoImpl meterDataFilterServiceDaoImpl;

    public ListSmsInspectionMeterBefDao() {
        inspectionMeterBefServiceDaoImpl = new InspectionMeterBefServiceDaoImpl();
        buildDevMeterRelationServiceDaoImpl = new BuildDevMeterRelationServiceDaoImpl();
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterServiceDaoImpl = new MeterServiceDaoImpl();
        meterDataFilterServiceDaoImpl = new MeterDataFilterServiceDaoImpl();
    }

    @Override
    public ListSmsInspectionMeterBefResult query(ListSmsInspectionMeterBefParameter parameter) throws Exception {

        TInspectionMeterBefPK targetPk = new TInspectionMeterBefPK();
        targetPk.setDevId(parameter.getDevId()); // 装置ID
        TInspectionMeterBef target = new TInspectionMeterBef();
        target.setId(targetPk); // 条件

        // 確定前検針データ一覧 取得
        List<TInspectionMeterBef> entityList = getResultList(inspectionMeterBefServiceDaoImpl, target);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }

        // メーター種別名称 取得 して、メーター種別名称MAP に格納する。
        Map<Long, String> meterTypeMap = getMeterTypeName(parameter.getCorpId(), parameter.getBuildingId());

        // 検索結果リスト＜確定前検針データ＞初期化 ※List<InspectionMeterBefResultData> ← List<TInspectionMeterBef> + Map<TBuilding> + getBuilding(～)
        List<InspectionMeterBefResultData> resultList = new ArrayList<InspectionMeterBefResultData>();

        for (int rowNo = 0; rowNo < entityList.size(); rowNo++) { // ソート順 : 装置ID, メーター管理番号, データ取得日時
            TInspectionMeterBef row = entityList.get(rowNo); // 1行分のデータ

            // 最新データのみの場合、最新以外のデータをスキップする
            if (parameter.getIsLastData()) {
                // 次の行のデータがあるか？
                if (rowNo + 1 < entityList.size()) {
                    // 次の行のデータがある
                    // 次の行のデータと 装置ID、メーター管理番号 が一致 が一致する場合はスキップする(最新データではない)
                    TInspectionMeterBef nextRow = entityList.get(rowNo + 1); // 次の行のデータ
                    if (row.getId().getDevId().equals(nextRow.getId().getDevId()) && row.getId().getMeterMngId().equals(nextRow.getId().getMeterMngId())) {
                        continue;
                    }
                }
            }

            resultList.add(new InspectionMeterBefResultData(
                    true, // true:確定前データ
                    row.getId().getDevId(),
                    row.getId().getMeterMngId(),
                    null, // ユーザーコード (別途下記処理でセット)
                    null, // テナント番号 (別途下記処理でセット)
                    null, // テナント名 (別途下記処理でセット)
                    meterTypeMap.get(row.getMeterType()),
                    row.getLatestInspVal(),
                    row.getMulti(),
                    row.getId().getLatestInspDate(),
                    row.getMeterType(),
                    row.getRecMan(),
                    row.getRecDate(),
                    row.getVersion()
            ));
        }

        // 検針漏れ管理番号 一覧 検索
        Map<String, List<Object>> targetMap = new HashMap<String, List<Object>>();
        targetMap.put(MeterServiceDaoImpl.DEV_ID,
                Arrays.asList(parameter.getDevId()));       // 装置ID
        targetMap.put(MeterServiceDaoImpl.EXCLUSION_METER_MNG_ID,
                resultList.stream()
                    .map(row -> row.getMeterMngId())
                    .distinct()
                    .collect(Collectors.toList())); // 取得したメーター管理番号一覧(検索除外条件)
        List<MMeter> exclusionMeterList = getResultList(meterServiceDaoImpl, targetMap);

        // (取得した)検針漏れ管理番号 を 検索結果リスト＜確定前検針データ＞ に追加
        for (MMeter exclusionMeter : exclusionMeterList) {
            resultList.add(new InspectionMeterBefResultData(
                    false, // false:検針漏れデータ
                    exclusionMeter.getId().getDevId(),
                    exclusionMeter.getId().getMeterMngId(),
                    null, // ユーザーコード (別途下記処理でセット)
                    null, // テナント番号 (別途下記処理でセット)
                    null, // テナント名 (別途下記処理でセット)
                    meterTypeMap.get(exclusionMeter.getMeterType()),
                    null, // 検針値
                    exclusionMeter.getMulti(),
                    null, // 検針日時
                    exclusionMeter.getMeterType(),
                    null,
                    null,
                    null
            ));
        }

        // テナント情報(ユーザーコード、テナント番号、テナント名) セット
        for (InspectionMeterBefResultData row : resultList) {
            // テナント情報 取得
            TBuildDevMeterRelation buildDevMeterRelation = getBuildDevMeterRelation(parameter.getCorpId(), row.getDevId(), row.getMeterMngId());
            if (buildDevMeterRelation != null && buildDevMeterRelation.getTBuilding() != null) {
                TBuilding building = buildDevMeterRelation.getTBuilding();
                List<MTenantSm> tenantSmsList = building.getMTenantSms(); // ※リストになっているが、主キーで検索しているので1件しか取得できない
                if (tenantSmsList != null && !tenantSmsList.isEmpty()) {
                    row.setUserCode(tenantSmsList.get(0).getTenantId()); // ユーザーコード
                }
                row.setTenantNo(building.getBuildingNo());      // テナント番号
                row.setTenantName(building.getBuildingName());  // テナント名
            }
        }

        // メータ登録用(M_METER) が存在しないものを除外する

        Map<String, List<Object>> meterTargetMap = new HashMap<String, List<Object>>();
        meterTargetMap.put(MeterServiceDaoImpl.DEV_ID,
                Arrays.asList(parameter.getDevId()));       // 装置ID
        meterTargetMap.put(MeterServiceDaoImpl.METER_MNG_ID,
                resultList.stream()
                    .map(row -> row.getMeterMngId())
                    .distinct()
                    .collect(Collectors.toList())); // 取得したメーター管理番号一覧(検索除外条件)
        List<MMeter> meterList = getResultList(meterServiceDaoImpl, meterTargetMap);

        resultList.removeAll(
            resultList.stream()
                .filter(x -> meterList.stream().noneMatch(y -> y.getId().getDevId().equals(x.getDevId()) && y.getId().getMeterMngId().equals(x.getMeterMngId())))
                .collect(Collectors.toList()));

        // --- SMS権限対応 ここから ---

        // このフィルタで m_meter に登録されていない or del_flg=1 のレコードも対象外になる

        // メーターフィルター
        Map<String, List<Object>> keyList = new HashMap<>();
        // 企業ID 必須
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getCorpId());
        keyList.put(BuildingPersonDevDataParam.CORP_ID, corpIdList);

        // 建物ID 必須
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(parameter.getBuildingId());
        keyList.put(BuildingPersonDevDataParam.BUILDING_ID, buildingIdList);

        // ログイン担当者企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(parameter.getLoginCorpId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_CORP_ID, loginCorpIdList);

        // 担当者ID 必須
        List<Object> personIdList = new ArrayList<>();
        personIdList.add(parameter.getLoginPersonId());
        keyList.put(BuildingPersonDevDataParam.LOGIN_PERSON_ID, personIdList);

        List<MeterDataFilterResultSet> filterList = getResultList(meterDataFilterServiceDaoImpl, keyList);

        // フィルターリストと重複しているデータ以外を削除
        resultList.removeAll(
                resultList.stream()
                .filter(x -> filterList.stream().noneMatch(y -> y.getDevId().equals(x.getDevId()) && y.getMeterMngId().equals(x.getMeterMngId())))
                .collect(Collectors.toList()));

        // --- SMS権限対応 ここまで ---

        return new ListSmsInspectionMeterBefResult(resultList);
    }

    /**
     * テナント情報 取得
     * @param corpId 企業ID
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @return TBuildDevMeterRelation  null:該当データなし
     */
    private TBuildDevMeterRelation getBuildDevMeterRelation(String corpId, String devId, Long meterMngId) {
        TBuildDevMeterRelationPK targetPk = new TBuildDevMeterRelationPK();
        targetPk.setCorpId(corpId); // 企業ID
        targetPk.setDevId(devId); // 装置ID
        targetPk.setMeterMngId(meterMngId); // メーター管理番号
        TBuildDevMeterRelation target = new TBuildDevMeterRelation();
        target.setId(targetPk); // 条件

        TBuildDevMeterRelation entity = find(buildDevMeterRelationServiceDaoImpl, target);

        return entity;
    }

    /**
     * メーター種別名称 取得
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return Map<METER_TYPE, METER_TYPE_NAME>
     */
    private Map<Long, String> getMeterTypeName(String corpId, Long buildingId) {
        Map<Long, String> map = new HashMap<Long, String>();

        MMeterTypePK targetPk = new MMeterTypePK();
        targetPk.setCorpId(corpId); // 企業ID
        targetPk.setBuildingId(buildingId); // 建物ID
        MMeterType target = new MMeterType();
        target.setId(targetPk); // 条件

        // (補足)
        // メーター種別設定 の 主キーは「メーター種別, 企業ID, 建物ID, メニュー番号」のため、
        // 条件「企業ID, 建物ID」で検索しても、メーター種別(METER_TYPE) でユニークにならない。
        // ただし、必要な項目はメーター種別名称(METER_TYPE_NAME)のため、重複していても同じ名称が取得できる

        List<MMeterType> entityList = getResultList(meterTypeServiceDaoImpl, target);
        if (entityList != null) {
            entityList.stream().forEach(row -> {
                map.put(row.getId().getMeterType(), row.getMeterTypeName());
            });
        }

        return map;
    }
}
