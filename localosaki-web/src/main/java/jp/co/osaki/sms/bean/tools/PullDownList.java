package jp.co.osaki.sms.bean.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.MeterDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MEstimateKind;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.dao.BuildingDevMeterListDao;
import jp.co.osaki.sms.dao.MChildGroupDao;
import jp.co.osaki.sms.dao.MDevPrmCollectListDao;
import jp.co.osaki.sms.dao.MDevPrmHandyListDao;
import jp.co.osaki.sms.dao.MDevPrmListDao;
import jp.co.osaki.sms.dao.MDevPrmOcrListDao;
import jp.co.osaki.sms.dao.MEstimateKindDao;
import jp.co.osaki.sms.dao.MMeterTypeListDao;
import jp.co.osaki.sms.dao.MParentGroupDao;
import jp.co.osaki.sms.dao.MPrefectureDao;
import jp.co.osaki.sms.dao.OcrDevPrmFilterDao;
import jp.co.osaki.sms.dao.UserCdListSearchDao;
import jp.co.osaki.sms.resultset.BuildingDevMeterResultSet;
import jp.co.osaki.sms.resultset.UserCdListResultSet;
import jp.skygroup.enl.webap.base.BaseSession;

/**
 *
 * プルダウン情報取得
 *
 * 各画面で利用するセレクトボックスの値を取得する
 *
 * @author d-komatsubara
 */
@Named(value = "pullDownListBean")
@ApplicationScoped
public class PullDownList extends BaseSession implements Serializable {

    private static final long serialVersionUID = 8440797877088700209L;

    /**
     * 都道府県マスタ
     */
    @EJB
    MPrefectureDao mPrefectureDao;

    /**
     * 親グループマスタ
     */
    @EJB
    MParentGroupDao mParentGroupDao;

    /**
     * 子グループマスタ
     */
    @EJB
    MChildGroupDao mChildGroupDao;

    /**
     * 装置情報.
     */
    @EJB
    MDevPrmListDao mDevPrmListDao;

    /**
     * OCR権限に基づく装置情報
     */
    @EJB
    OcrDevPrmFilterDao ocrDevPrmDataFilterDao;

    /**
     * 装置情報（ハンディ端末以外の装置を対象）
     */
    @EJB
    MDevPrmCollectListDao mDevPrmCollectListDao;

    /**
     * 装置情報（ハンディ端末のみ）
     */
    @EJB
    MDevPrmHandyListDao mDevPrmHandyListDao;

    /**
     * 装置情報（AieLink）
     * 「OCR検針」→「AieLink」へ変更
     */
    @EJB
    MDevPrmOcrListDao mDevPrmOcrListDao;

    /**
     * 建物・装置・メーター.
     */
    @EJB
    BuildingDevMeterListDao buildingDevMeterListDao;

    /**
     * 推計種別マスタ
     */
    @EJB
    MEstimateKindDao mEstimateKindDao;

    /**
     * 汎用区分
     */
    @Inject
    private GenericTypeList genericTypeList;

    /**
     * メーター種別設定
     */
    @EJB
    MMeterTypeListDao mMeterTypeListDao;

    /**
     * 建物、テナント情報
     */
    @EJB
    UserCdListSearchDao userCdListSearchDao;

    /**
     * メーターフィルター
     */
    @EJB
    private MeterDataFilterDao meterDataFilterDao;

    public PullDownList() {
    }

    /**
     * 都道府県情報
     *
     * @return 都道府県情報Map
     */
    public LinkedHashMap<String, String> getPrefecture() {
        return getPrefecture(true, null);
    }

    /**
     * 都道府県情報
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 都道府県情報Map
     */
    public LinkedHashMap<String, String> getPrefecture(boolean defaultBoxFlg, String dispMessage) {

        MPrefecture mPrefecture = new MPrefecture();
        List<MPrefecture> resultList = mPrefectureDao.getResultList(mPrefecture);

        LinkedHashMap<String, String> prefectureMap = setDefaultValue(defaultBoxFlg, dispMessage);

        // 検索結果をMap情報にセットする(画面表示内容：コード値)
        for (MPrefecture prefectureInfo : resultList) {
            prefectureMap.put(prefectureInfo.getPrefectureName(), prefectureInfo.getPrefectureCd());
        }

        return prefectureMap;

    }

    /**
     * 親グループマスタ情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @return 親グループマスタ情報Map
     */
    public LinkedHashMap<String, String> getParentGroup(boolean defaultBoxFlg, String dispMessage, String corpId) {

        List<MParentGroup> resultList = mParentGroupDao.getCategoryList(corpId);
        LinkedHashMap<String, String> parentGroupMap = setDefaultValue(defaultBoxFlg, dispMessage);

        for (MParentGroup mParentGroup : resultList) {
            parentGroupMap.put(mParentGroup.getParentGroupName(),
                    Long.toString(mParentGroup.getId().getParentGroupId()));
        }

        return parentGroupMap;

    }

    /**
     * 子グループマスタ情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @param parentGroupId 親グループID
     * @return 子グループマスタ情報Map
     */
    public LinkedHashMap<String, String> getChildGroup(boolean defaultBoxFlg, String dispMessage, String corpId,
            long parentGroupId) {

        List<MChildGroup> resultList = mChildGroupDao.getGroupList(corpId, parentGroupId);
        LinkedHashMap<String, String> childGroupMap = setDefaultValue(defaultBoxFlg, dispMessage);

        for (MChildGroup mChildGroup : resultList) {
            childGroupMap.put(mChildGroup.getChildGroupName(), Long.toString(mChildGroup.getId().getChildGroupId()));
        }

        return childGroupMap;
    }

    /**
     * 入居形態情報取得
     *
     * @return 入居形態情報Map
     */
    public LinkedHashMap<String, String> getTenantsForm() {
        return getTenantsForm(true, null);
    }

    /**
     * 入居形態情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 入居形態情報Map
     */
    public LinkedHashMap<String, String> getTenantsForm(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> tenantsFormMap = setDefaultValue(defaultBoxFlg, dispMessage);

        tenantsFormMap.putAll(genericTypeList.getNyukyoTypeCd());
        return tenantsFormMap;
    }

    /**
     * 建物状況情報取得
     *
     * @return 建物状況情報Map
     */
    public LinkedHashMap<String, String> getBuildingSituation() {
        return getBuildingSituation(true, null);
    }

    /**
     * 建物状況情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 建物状況情報Map
     */
    public LinkedHashMap<String, String> getBuildingSituation(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> buildingSituationMap = setDefaultValue(defaultBoxFlg, dispMessage);

        buildingSituationMap.putAll(genericTypeList.getBuildingSituation());

        return buildingSituationMap;
    }

    /**
     * 装置情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 装置情報Map
     */
    public Map<String, String> getDevPrm(String corpId, Long buildingId) {
        return getDevPrm(corpId, buildingId, false);
    }

    /**
     * 装置情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報Map
     */
    public Map<String, String> getDevPrm(String corpId, Long buildingId, boolean isTenant) {
        return getDevPrm(false, null, corpId, buildingId, isTenant, "装置%d", "(%d)");
    }

    /**
     * OCR装置以外の情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報Map
     */
    public Map<String, String> getNotOcrDevPrm(String corpId, Long buildingId, CorpPersonAuthParam corpPersonAuthParam) {
        return getNotOcrDevPrm(corpId, buildingId, corpPersonAuthParam, false);
    }

    /**
     * OCR装置情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報Map
     */
    public Map<String, String> getOcrDevPrm(String corpId, Long buildingId, CorpPersonAuthParam corpPersonAuthParam) {
        return getOcrDevPrm(corpId, buildingId, corpPersonAuthParam, false);
    }

    /**
     * OCR装置情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報Map
     */
    public Map<String, String> getOcrDevPrm(String corpId, Long buildingId, CorpPersonAuthParam corpPersonAuthParam, boolean isTenant) {
        return getOcrDevPrm(false, null, corpId, buildingId, corpPersonAuthParam, isTenant, "装置%d", "(%d)");
    }

    /**
     * OCR装置以外の情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報Map
     */
    public Map<String, String> getNotOcrDevPrm(String corpId, Long buildingId, CorpPersonAuthParam corpPersonAuthParam, boolean isTenant) {
        return getNotOcrDevPrm(false, null, corpId, buildingId, corpPersonAuthParam, isTenant, "装置%d", "(%d)");
    }


    /**
     * 装置情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param defaultDevName 既定装置名
     * @param sameDevNameSuffix 同一装置名接尾辞
     * @return 装置情報Map
     */
    public Map<String, String> getDevPrm(boolean defaultBoxFlg, String dispMessage, String corpId,
            Long buildingId, boolean isTenant, String defaultDevName, String sameDevNameSuffix) {

        return createDevPrmPullDownMap(defaultBoxFlg, dispMessage,
                mDevPrmListDao.getDevPrmList(corpId, buildingId, isTenant), defaultDevName, sameDevNameSuffix);
    }

    /**
     * OCR装置以外の情報取得
     * OCR権限によって取得する装置を変更する
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param defaultDevName 既定装置名
     * @param sameDevNameSuffix 同一装置名接尾辞
     * @return 装置情報Map
     */
    public Map<String, String> getNotOcrDevPrm(boolean defaultBoxFlg, String dispMessage, String corpId,
            Long buildingId, CorpPersonAuthParam corpPersonAuthParam, boolean isTenant, String defaultDevName, String sameDevNameSuffix) {
        return createDevPrmPullDownMap(defaultBoxFlg, dispMessage,
                ocrDevPrmDataFilterDao.getNotOcrDevPrmList(corpId, buildingId, corpPersonAuthParam, isTenant), defaultDevName, sameDevNameSuffix);
    }

    /**
     * OCR装置情報取得
     * OCR権限によって取得する装置を変更する
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param defaultDevName 既定装置名
     * @param sameDevNameSuffix 同一装置名接尾辞
     * @return 装置情報Map
     */
    public Map<String, String> getOcrDevPrm(boolean defaultBoxFlg, String dispMessage, String corpId,
            Long buildingId, CorpPersonAuthParam corpPersonAuthParam, boolean isTenant, String defaultDevName, String sameDevNameSuffix) {
        return createDevPrmPullDownMap(defaultBoxFlg, dispMessage,
                ocrDevPrmDataFilterDao.getDevPrmList(corpId, buildingId, corpPersonAuthParam, isTenant), defaultDevName, sameDevNameSuffix);
    }

    /**
     * 装置情報プルダウンMap生成.
     *
     * @param devPrmList 装置情報List
     * @return 装置情報プルダウンMap
     */
    public Map<String, String> createDevPrmPullDownMap(List<MDevPrm> devPrmList) {
        return createDevPrmPullDownMap(false, null, devPrmList, "装置%d", "(%d)");
    }

    /**
     * 装置情報プルダウンMap生成.
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param devPrmList 装置情報List
     * @return 装置情報プルダウンMap
     */
    public Map<String, String> createDevPrmPullDownMap(boolean defaultBoxFlg, String dispMessage,
            List<MDevPrm> devPrmList) {

        return createDevPrmPullDownMap(defaultBoxFlg, dispMessage, devPrmList, "装置%d", "(%d)");
    }

    /**
     * 装置情報プルダウンMap生成.
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param devPrmList 装置情報List
     * @param defaultDevName 既定装置名
     * @param sameDevNameSuffix 同一装置名接尾辞
     * @return 装置情報プルダウンMap
     */
    public Map<String, String> createDevPrmPullDownMap(boolean defaultBoxFlg, String dispMessage,
            List<MDevPrm> devPrmList, String defaultDevName, String sameDevNameSuffix) {

        Map<String, String> pullDownMap = setDefaultValue(defaultBoxFlg, dispMessage);

        Map<String, Integer> devNameMap = new HashMap<>();

        int noNameCount = 0;
        for (MDevPrm devPrm : devPrmList) {
            String devId = devPrm.getDevId();
            String devName = devPrm.getName();
            String devNameSurffix = "";
            if (CheckUtility.isNullOrEmpty(devName)) {
                // 装置名未設定時
                devName = String.format(defaultDevName, ++noNameCount);
            }

            int sameDevNameCount = 1;
            if (devNameMap.containsKey(devName)) {
                // 同一装置名称が重複している場合

                sameDevNameCount = devNameMap.get(devName);
                if (pullDownMap.containsKey(devName)) {
                    // 当該装置名称がプルダウンMapに追加済の場合
                    String sameDevNameId = pullDownMap.get(devName);
                    pullDownMap.remove(devName);

                    pullDownMap.put(devName + String.format(sameDevNameSuffix, sameDevNameCount), sameDevNameId);
                }

                devNameSurffix = String.format(sameDevNameSuffix, ++sameDevNameCount);
            }

            pullDownMap.put(devName + devNameSurffix, devId);
            devNameMap.put(devName, sameDevNameCount);
        }

        return pullDownMap;
    }

    /**
     * メーター管理番号情報取得(日報ダウンロード用)
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @return メーター管理番号情報Map
     */
    public Map<String, Long> getMeterMngId(String corpId, Long buildingId, boolean isTenant, String devId) {
        return getMeterMngId(corpId, buildingId, isTenant, devId, null, null, null, false);
    }

    /**
     * メーター管理番号情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param tenantName テナント名
     * @param isReportMode 日報・月報・年報フラグ
     * @return メーター管理番号情報Map
     */
    public Map<String, Long> getMeterMngId(String corpId, Long buildingId, boolean isTenant, String devId,
            String meterKind, String tenantName, boolean isReportMode) {
        return getMeterMngId(corpId, buildingId, isTenant, devId, meterKind, null, tenantName, isReportMode);
    }

    /**
     * メーター管理番号情報取得
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param meterMngId メーター管理番号
     * @param tenantName テナント名
     * @param isReportMode 日報・月報・年報フラグ
     * @return メーター管理番号情報Map
     */
    public Map<String, Long> getMeterMngId(String corpId, Long buildingId, boolean isTenant, String devId,
            String meterKind, Long meterMngId, String tenantName, boolean isReportMode) {
        return getMeterMngId(false, null, corpId, buildingId, isTenant, devId, meterKind, meterMngId, tenantName, isReportMode);
    }

    /**
     * メーター管理番号情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @param devId 装置ID
     * @param meterKind メーター種類
     * @param meterMngId メーター管理番号
     * @param tenantName テナント名
     * @param isReportMode 日報・月報・年報フラグ
     * @return メーター管理番号情報Map
     */
    public Map<String, Long> getMeterMngId(boolean defaultBoxFlg, String dispMessage, String corpId, Long buildingId,
            boolean isTenant, String devId, String meterKind, Long meterMngId, String tenantName, boolean isReportMode) {

        List<BuildingDevMeterResultSet> meterMngIdList = buildingDevMeterListDao
                .getBuildingDevMeterList(corpId, buildingId, isTenant, devId, meterKind, meterMngId, tenantName);

        // ログイン担当者情報取得
        MPerson person = (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());

        // 担当者が権限のあるメーターに絞り込む
        if(devId.equals("0")) {
            meterMngIdList = filterDevId(meterMngIdList, mDevPrmListDao.getDevPrmList(corpId, buildingId, isTenant));
        }
        meterMngIdList = meterDataFilterDao.applyDataFilter(meterMngIdList, new BuildingPersonDevDataParam(
                corpId, buildingId, person.getId().getCorpId(), person.getId().getPersonId()));

        return createMeterMngIdPullDownMap(defaultBoxFlg, dispMessage, meterMngIdList, isReportMode);
    }

    /**
     * メーター管理番号情報プルダウンMap生成.
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param meterMngIdList メーター管理番号情報List
     * @param isReportMode 日報・月報・年報画面フラグ
     * @return メーター管理番号情報プルダウンMap
     */
    public Map<String, Long> createMeterMngIdPullDownMap(boolean defaultBoxFlg, String dispMessage,
            List<BuildingDevMeterResultSet> meterMngIdList, boolean isReportMode) {

        Map<String, Long> pullDownMap = new LinkedHashMap<>();

        Map<String, String> defaultPullDownMap = setDefaultValue(defaultBoxFlg, dispMessage);
        Set<Entry<String, String>> defaultPullDownMapEntrySet = defaultPullDownMap.entrySet();
        for (Entry<String, String> defaultPullDownMapEntry : defaultPullDownMapEntrySet) {
            pullDownMap.put(defaultPullDownMapEntry.getKey(), Long.parseLong(defaultPullDownMapEntry.getValue()));
        }

        // メータ管理番号
        for (BuildingDevMeterResultSet buildingDevMeter : meterMngIdList) {
            String meterMngIdDisp = buildingDevMeter.getMeterMngIdDisp();
            if(isReportMode) {
                // 日報・月報・年報データ
                pullDownMap.put(buildingDevMeter.getDevId() + "_" +  meterMngIdDisp, Long.parseLong(meterMngIdDisp));
            } else {
                // データ比較グラフ
                pullDownMap.put(meterMngIdDisp, Long.parseLong(meterMngIdDisp));
            }
        }

        return pullDownMap;
    }

    /**
     * 対象メーター管理番号Listを生成.
     *
     * @param allMeterMngIdMap メーター管理番号全件Map
     * @param pageNo ページ番号
     * @param pageDataCount ページ表示件数
     * @return 対象メーター管理番号List
     */
    public List<Long> createTargetMeterMngIdList(Map<String, Long> allMeterMngIdMap, Integer pageNo,
            Integer pageDataCount) {
        List<Long> meterMngIdList = new ArrayList<>();

        if (allMeterMngIdMap == null || allMeterMngIdMap.isEmpty()) {
            return meterMngIdList;
        }
        if (pageNo == null || pageDataCount == null) {
            return meterMngIdList;
        }

        Collection<Long> meterMngIdValues = allMeterMngIdMap.values();
        for (Long meterMngId : meterMngIdValues) {
            meterMngIdList.add(meterMngId);
        }
        // ページ制御
        int fromIndex = (pageNo - 1) * pageDataCount;
        int meterMngIdListSize = meterMngIdList.size();
        if (meterMngIdListSize >= fromIndex) {
            int toIndex = fromIndex + pageDataCount;
            if (toIndex > meterMngIdListSize) {
                toIndex = meterMngIdListSize;
            }
            meterMngIdList = meterMngIdList.subList(fromIndex, toIndex);
        }

        return meterMngIdList;
    }

    /**
     * 装置毎の対象メーター管理番号ListMapを生成.
     *
     * @param allMeterMngIdMap メーター管理番号全件Map
     * @param pageNo ページ番号
     * @param pageDataCount ページ表示件数
     * @return 対象メーター管理番号List
     */
    public Map<String, List<Long>> createDevIdMeterMngIdListMap(Map<String, Long> allMeterMngIdMap, Integer pageNo,
            Integer pageDataCount) {
        Map<String, List<Long>> meterMngDevIdMeterMngIdListMap = new HashMap<>();

        if (allMeterMngIdMap == null || allMeterMngIdMap.isEmpty()) {
            return meterMngDevIdMeterMngIdListMap;
        }
        // pageNoがnullの場合はcsv, pdf出力
        if (pageDataCount == null) {
            return meterMngDevIdMeterMngIdListMap;
        }

        List<Long> meterMngIdList = new ArrayList<>();

        // ページ制御
        int fromIndex = 0;
        int toIndex = 0;
        int meterMngIdListSize = allMeterMngIdMap.values().size();
        if(pageNo != null) {
            fromIndex = (pageNo - 1) * pageDataCount;
            toIndex =fromIndex + pageDataCount;
            if (meterMngIdListSize >= fromIndex) {
                if (toIndex > meterMngIdListSize) {
                    toIndex = meterMngIdListSize;
                }
            }
        } else {
            toIndex = meterMngIdListSize;
        }

        ArrayList<String> keys = new ArrayList<>(allMeterMngIdMap.keySet());
        ArrayList<Long> values = new ArrayList<>(allMeterMngIdMap.values());
        for(int index = fromIndex; index < toIndex; index++) {
            Integer truncateIndex1 = keys.get(index).indexOf("_");
            String devId1 = keys.get(index).substring(0, truncateIndex1);
            // 装置1台でメーターも1台しか紐づいてない場合
            if(index == fromIndex && index == toIndex - 1) {
                meterMngIdList.add(values.get(index));
                meterMngDevIdMeterMngIdListMap.put(devId1, new ArrayList<Long>(meterMngIdList));
                continue;
            }else if(index == fromIndex) {
             // 1周目, 装置名が変わった時は必ずaddする
                meterMngIdList.add(values.get(index));
                continue;
            }
            Integer truncateIndex2 = keys.get(index - 1).indexOf("_");
            String devId2 = keys.get(index - 1).substring(0, truncateIndex2);
            // 装置Idが前の値と異なればmapにputする
            if(devId1.equals(devId2)) {
                meterMngIdList.add(values.get(index));
            } else {
                meterMngDevIdMeterMngIdListMap.put(devId2, new ArrayList<Long>(meterMngIdList));
                meterMngIdList.clear();
                meterMngIdList.add(values.get(index));
            }
            // 最終行の場合は追加
            if(index == toIndex - 1) {
                meterMngDevIdMeterMngIdListMap.put(devId1, new ArrayList<Long>(meterMngIdList));
            }
        }

        return meterMngDevIdMeterMngIdListMap;
    }

    /**
     * メーター表示切替(ページ番号)プルダウンを生成.
     *
     * @param allMeterMngIdMap メーター管理番号全件Map
     * @param pageDataCount ページ表示件数
     * @param isReportMode 日報・月報・年報画面フラグ
     * @param devId 装置ID
     * @return 対象メーター管理番号List
     */
    public Map<String, Integer> createMngMeterIdPageMap(Map<String, Long> allMeterMngIdMap, Integer pageDataCount,
        boolean isReportMode, String devId) {
        Map<String, Integer> meterMngIdPageMap = new LinkedHashMap<>();

        Set<String> meterMngIdSet = allMeterMngIdMap.keySet();

        int index = 0;
        if(isReportMode) {
            if (devId.equals("0")) {
                // メータ台数(日報・月報・年報) 全装置表示
                int size = meterMngIdSet.size();
                for(;;) {
                    if(index==0) {
                        meterMngIdPageMap.put(String.format("%03d", index + 1), ((index / pageDataCount) + 1));
                        index++;
                        continue;
                    }
                    size = size - 100;
                    if(size < 0) {
                        break;
                    }
                    meterMngIdPageMap.put(String.format("%03d", index * 100 + 1), ((index * 100 / pageDataCount) + 1));
                    index++;
                }
            } else {
                //ページ先頭のメータ管理番号(日報・月報・年報) 全装置表示適用なし
                for (String meterMngId : meterMngIdSet) {
                    if ((index % pageDataCount) == 0) {
                        //管理番号(後ろ3桁)のみを抽出し設定
                        meterMngIdPageMap.put(meterMngId.substring(meterMngId.length() - 3), ((index / pageDataCount) + 1));
                    }
                    index++;
                }

            }
        } else {
            // メータ管理番号(データ比較グラフ)
            for (String meterMngId : meterMngIdSet) {
                if ((index % pageDataCount) == 0) {
                    meterMngIdPageMap.put(meterMngId, ((index / pageDataCount) + 1));
                }
                index++;
            }
        }

        return meterMngIdPageMap;
    }

    /**
     * 表示方向情報取得
     *
     * @param devId 装置ID
     * @param devIdMap 装置情報Map
     * @return 表示方向情報Map
     */
    public Map<String, String> getDispDirect(String devId, Map<String, String> devIdMap) {
        return getDispDirect(false, null, devId, devIdMap);
    }

    /**
     * 表示方向情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param selectedDevId 選択された装置ID
     * @param devIdMap 装置情報Map
     * @return 表示方向情報Map
     */
    public Map<String, String> getDispDirect(boolean defaultBoxFlg, String dispMessage, String selectedDevId, Map<String, String> devIdMap) {
        String revFlg = null;

        // 接続先で「全て」を選択しているとき、建物に紐づく装置に1台でも逆方向有効が存在する場合、表示方向で「逆方向」を選択できるようにする。
        if ("0".equals(selectedDevId)) {
            // 「全て」の場合
            for (String devId : devIdMap.values()) {
                if (!"0".equals(devId)) {
                    MDevPrm devPrm = mDevPrmListDao.getDevPrm(devId);
                    if (devPrm != null) {
                        revFlg = devPrm.getRevFlg();
                    }
                    if (SmsConstants.REV_FLG.ON.getVal().equals(revFlg)) {
                        break;
                    }
                }
            }
        } else {
            MDevPrm devPrm = mDevPrmListDao.getDevPrm(selectedDevId);
            if (devPrm != null) {
                revFlg = devPrm.getRevFlg();
            }
        }

        Map<String, String> pulldownMap = setDefaultValue(defaultBoxFlg, dispMessage);
        pulldownMap.putAll(genericTypeList.getSmsDispDirect());
        if (!SmsConstants.REV_FLG.ON.getVal().equals(revFlg)) {
            // 逆潮対応機能がON以外の場合
            pulldownMap.remove(MapUtility.searchValueOfKeyName(pulldownMap, SmsConstants.DISP_DIRECT.REVERSE.getVal()));
        }

        return pulldownMap;
    }

    /**
     * 表示種別情報取得
     *
     * @return 表示種別情報Map
     */
    public Map<String, String> getDispType() {
        return getDispType(false, null);
    }

    /**
     * 表示種別情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 表示種別情報Map
     */
    public Map<String, String> getDispType(boolean defaultBoxFlg, String dispMessage) {
        Map<String, String> pulldownMap = setDefaultValue(defaultBoxFlg, dispMessage);
        pulldownMap.putAll(genericTypeList.getSmsDispType());

        return pulldownMap;
    }

    /**
     * メーター種類情報取得
     *
     * @return メーター種類情報Map
     */
    public Map<String, String> getMeterKind() {
        return getMeterKind(false, null);
    }

    /**
     * メーター種類情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return メーター種類情報Map
     */
    public Map<String, String> getMeterKind(boolean defaultBoxFlg, String dispMessage) {
        Map<String, String> pulldownMap = setDefaultValue(defaultBoxFlg, dispMessage);
        pulldownMap.putAll(genericTypeList.getSmsMeterKind());

        return pulldownMap;
    }

    /**
     * セレクトボックスデフォルト値設定
     *
     * @param defaultBoxFlg　未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage デフォルト値表示メッセージ
     * @return 表示対象MAP
     */
    private LinkedHashMap<String, String> setDefaultValue(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> targetMap = new LinkedHashMap<>();
        // ボックス初期選択の状態が指定ありなら表示メッセージ
        if (defaultBoxFlg) {
            if (dispMessage == null) {
                // 指定文字に"-1"を設定
                targetMap.put(OsolConstants.SELECT_BOX_DEFAULT.UNSPECFILED.getVal(),
                        OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE);
            } else {
                // 指定文字に"-1"を設定
                targetMap.put(dispMessage, OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE);
            }

        }

        return targetMap;
    }

    /**
     * 推計機能(推計フラグ)情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 推計機能(推計フラグ)情報取得Map
     */
    public LinkedHashMap<String, String> getEstimateUse(boolean defaultBoxFlg, String dispMessage) {
        LinkedHashMap<String, String> estimateUseMap = setDefaultValue(defaultBoxFlg, dispMessage);

        estimateUseMap.putAll(genericTypeList.getEstimateUse());

        return estimateUseMap;
    }

    /**
     * 推計種別情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 推計種別情報Map
     */
    public LinkedHashMap<String, String> getEstimateKind(boolean defaultBoxFlg, String dispMessage, String corpId) {

        LinkedHashMap<String, String> estimateKindMap = setDefaultValue(defaultBoxFlg, dispMessage);
        for (MEstimateKind entity : mEstimateKindDao.getResultList(corpId)) {
            estimateKindMap.put(entity.getEstimateName(), entity.getId().getEstimateId().toString());
        }

        return estimateKindMap;
    }

    /**
     * 消費税扱い情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 消費税扱い情報Map
     */
    public LinkedHashMap<String, String> getSaleTaxDeal(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> saleTaxDealMap = setDefaultValue(defaultBoxFlg, dispMessage);

        saleTaxDealMap.putAll(genericTypeList.getSmsSaleTaxDeal());

        return saleTaxDealMap;
    }

    /**
     * 小数部端数処理情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 小数部端数処理情報Map
     */
    public LinkedHashMap<String, String> getDecimalFraction(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> decimalFractionMap = setDefaultValue(defaultBoxFlg, dispMessage);

        decimalFractionMap.putAll(genericTypeList.getSmsDecimalFraction());

        return decimalFractionMap;
    }

    /**
     * 年報締め月情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 年報締め月情報Map
     */
    public LinkedHashMap<String, String> getYearCloseMonth(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> yearCloseMonthMap = setDefaultValue(defaultBoxFlg, dispMessage);

        yearCloseMonthMap.putAll(genericTypeList.getSmsYearCloseMonth());

        return yearCloseMonthMap;
    }

    /**
     * CO2排出係数単位情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return CO2排出係数単位情報Map
     */
    public LinkedHashMap<String, String> getUnitCo2Coefficient(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> unitCo2CoefficientMap = setDefaultValue(defaultBoxFlg, dispMessage);

        unitCo2CoefficientMap.putAll(genericTypeList.getSmsUnitCo2Coefficient());

        return unitCo2CoefficientMap;
    }

    /**
     * 料金メニュー情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 推計種別情報Map
     */
    public LinkedHashMap<String, String> getPriceMenu(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> priceMenuMap = setDefaultValue(defaultBoxFlg, dispMessage);

        priceMenuMap.putAll(genericTypeList.getSmsElectricMenu());

        return priceMenuMap;
    }

    /**
     * 装置ID情報取得
     *
     * @return 装置状況情報Map
     */
    public LinkedHashMap<String, String> getCollectDeviceId() {
        return getCollectDeviceId(true, null);
    }

    /**
     * 装置ID情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 装置状況情報Map
     */
    public LinkedHashMap<String, String> getCollectDeviceId(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> collectDeviceIdMap = setDefaultValue(defaultBoxFlg, dispMessage);

        List<MDevPrm> devPrmCollectList = mDevPrmCollectListDao.getDevPrmCollectList();

        Map<String, String> map = devPrmCollectList.stream()
                .collect(Collectors.toMap(MDevPrm::getDevId, MDevPrm::getDevId));

        collectDeviceIdMap.putAll(map);

        return collectDeviceIdMap;
    }

    /**
     * ハンディ端末ID情報取得
     *
     * @return ハンディ端末状況情報Map
     */
    public LinkedHashMap<String, String> getHandyDeviceId() {
        return getHandyDeviceId(true, null);
    }

    /**
     * ハンディ端末ID情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return ハンディ端末状況情報Map
     */
    public LinkedHashMap<String, String> getHandyDeviceId(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> handyDeviceIdMap = setDefaultValue(defaultBoxFlg, dispMessage);

        List<MDevPrm> devPrmHandyList = mDevPrmHandyListDao.getDevPrmHandyList();

        Map<String, String> map = devPrmHandyList.stream()
                .collect(Collectors.toMap(MDevPrm::getDevId, MDevPrm::getDevId));

        handyDeviceIdMap.putAll(map);

        return handyDeviceIdMap;
    }

    /**
     * AieLinkID情報取得
     * 「OCR検針」→「AieLink」へ変更
     *
     * @return AieLink状況情報Map
     */
    public LinkedHashMap<String, String> getOcrDeviceId() {
        return getOcrDeviceId(true, null);
    }

    /**
     * AieLinkID情報取得
     * 「OCR検針」→「AieLink」へ変更
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return AieLink状況情報Map
     */
    public LinkedHashMap<String, String> getOcrDeviceId(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> ocrDeviceIdMap = setDefaultValue(defaultBoxFlg, dispMessage);

        List<MDevPrm> devPrmOcrList = mDevPrmOcrListDao.getDevPrmOcrList();

        Map<String, String> map = devPrmOcrList.stream()
                .collect(Collectors.toMap(MDevPrm::getDevId, MDevPrm::getDevId));

        ocrDeviceIdMap.putAll(map);

        return ocrDeviceIdMap;
    }

    /**
     * 装置状況情報取得
     *
     * @return 装置状況情報Map
     */
    public LinkedHashMap<String, String> getDevStatus() {
        return getDevStatus(true, null);
    }

    /**
     * 装置状況情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 装置状況情報Map
     */
    public LinkedHashMap<String, String> getDevStatus(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> devStatusMap = setDefaultValue(defaultBoxFlg, dispMessage);

        devStatusMap.putAll(genericTypeList.getSmsDevStatus());

        return devStatusMap;
    }

    /**
     * 表示時間単位情報取得
     *
     * @return 表示時間単位情報Map
     */
    public Map<String, String> getDispTimeUnit() {
        return getDispTimeUnit(false, null);
    }

    /**
     * 表示時間単位情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 表示時間単位情報Map
     */
    public Map<String, String> getDispTimeUnit(boolean defaultBoxFlg, String dispMessage) {
        Map<String, String> dispTimeUnitMap = setDefaultValue(defaultBoxFlg, dispMessage);
        dispTimeUnitMap.putAll(genericTypeList.getSmsDispTimeUnit());

        return dispTimeUnitMap;
    }

    /**
     * 検針種別情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 検針種別情報Map
     */
    public LinkedHashMap<String, String> getInspType(boolean defaultBoxFlg, String dispMessage) {

        LinkedHashMap<String, String> inspTypeMap = setDefaultValue(defaultBoxFlg, dispMessage);

        inspTypeMap.putAll(genericTypeList.getSmsInspType());

        return inspTypeMap;
    }

    /**
     * 表示年情報Map取得
     *
     * @return 表示年情報Map
     */
    public Map<String, String> getDispYearMap() {
        return getDispYearMap(false, null, getDispYearList());
    }

    /**
     * 表示年情報Map取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param dispYearList 表示年情報List
     * @return 表示年情報Map
     */
    public Map<String, String> getDispYearMap(boolean defaultBoxFlg, String dispMessage, List<String> dispYearList) {
        Map<String, String> pulldownMap = setDefaultValue(defaultBoxFlg, dispMessage);

        for (String dispYear : dispYearList) {
            pulldownMap.put(dispYear, dispYear);
        }

        return pulldownMap;
    }

    /**
     * 表示年情報List取得
     *
     * @return 表示年情報List
     */
    public List<String> getDispYearList() {
        int latestYear = Integer.parseInt(DateUtility.changeDateFormat(
                DateUtility.changeDateMonthFirst(mDevPrmListDao.getSvDate()), DateUtility.DATE_FORMAT_YYYY));

        return getDispYearList(latestYear);
    }

    /**
     * 表示年情報List取得
     *
     * @param latestYear 最新年
     * @return 表示年情報List
     */
    public List<String> getDispYearList(int latestYear) {
        List<String> dispYearList = new ArrayList<>();

        int targetYear = latestYear;
        while (targetYear >= SmsConstants.OLDEST_DISP_YEAR) {
            dispYearList.add(String.valueOf(targetYear));

            targetYear--;
        }

        return dispYearList;
    }

    /**
     * ユーザー名称取得
     *
     * @param corpId 企業ID
     * @return ユーザー名称マップ
     */
    public LinkedHashMap<String, String> getUserCd(String corpId, String buildingId) {
        return getUserCd(true, null, corpId, buildingId);
    }

    /**
     * ユーザー名称取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @param corpId 企業ID
     * @return ユーザー名称マップ
     */
    public LinkedHashMap<String, String> getUserCd(boolean defaultBoxFlg, String dispMessage, String corpId, String buildingId) {

        LinkedHashMap<String, String> userCdMap = setDefaultValue(defaultBoxFlg, dispMessage);

        // ログイン担当者情報取得
        MPerson person = (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());

        // フィルタリング後のユーザーコード取得
        List<UserCdListResultSet> userCdList = userCdListSearchDao.getUserCdList(
                corpId, buildingId, person.getId().getCorpId(), person.getId().getPersonId());

        for (UserCdListResultSet target : userCdList) {
            userCdMap.put(String.valueOf(target.getTenantId()) + "（" + target.getBuildingName() + "）",
                    String.valueOf(target.getTenantId()));
        }

        return userCdMap;
    }

    /**
    * 種別名称取得
    *
    * @param corpId 企業ID
    * @param buildingId 建物ID
    * @return 種別名称マップ
    */
    public LinkedHashMap<String, String> getMeterTypeName(String corpId, Long buildingId) {
        return getMeterTypeName(true, null, corpId, buildingId);
    }

    /**
    * 種別名称取得
    *
    * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
    * @param dispMessage 未選択領域表示メッセージ
    * @param corpId 企業ID
    * @param buildingId 建物ID
    * @return 種別名称マップ
    */
    public LinkedHashMap<String, String> getMeterTypeName(boolean defaultBoxFlg, String dispMessage, String corpId,
            Long buildingId) {

        LinkedHashMap<String, String> meterTypeNameMap = setDefaultValue(defaultBoxFlg, dispMessage);

        // メーター種別設定テーブル取得
        List<MMeterType> mMeterTypeList = mMeterTypeListDao.getMeterTypeList(corpId, buildingId);

        for (MMeterType target : mMeterTypeList) {
            meterTypeNameMap.put(target.getMeterTypeName(), target.getMeterTypeName());
        }

        return meterTypeNameMap;
    }

    /**
    * 抽出範囲取得
    *
    * @param corpId 企業ID
    * @param buildingId 建物ID
    * @return 種別名称マップ
    */
    public Map<String, String> getMonthExtract() {
        return getMonthExtract(false, null);
    }

    /**
     * 表示種別情報取得
     *
     * @param defaultBoxFlg 未選択領域FLG TRUE:未選択あり FALSE:未選択なし(必須選択)
     * @param dispMessage 未選択領域表示メッセージ
     * @return 表示種別情報Map
     */
    public Map<String, String> getMonthExtract(boolean defaultBoxFlg, String dispMessage) {
        Map<String, String> pulldownMap = setDefaultValue(defaultBoxFlg, dispMessage);
        pulldownMap.putAll(genericTypeList.getMonthExtract());

        return pulldownMap;
    }

    /**
    * 装置idでフィルターをかけたメータリストを取得
    *
    * @param targetList フィルター前のリスト
    * @param devIdMap 対象の装置id
    * @return フィルターをかけたリスト
    */
    private List<BuildingDevMeterResultSet> filterDevId(List<BuildingDevMeterResultSet> targetList, List<MDevPrm> devIdList){
        List<BuildingDevMeterResultSet> filteredList = new ArrayList<>();
        for(MDevPrm targetDevId : devIdList) {
            targetDevId.getDevId();
            List<BuildingDevMeterResultSet> test = targetList.stream().filter(r -> r.getDevId().equals(targetDevId.getDevId())).collect(Collectors.toList());
            filteredList.addAll(filteredList.size(), test);
        }
        return filteredList;
    }

}
