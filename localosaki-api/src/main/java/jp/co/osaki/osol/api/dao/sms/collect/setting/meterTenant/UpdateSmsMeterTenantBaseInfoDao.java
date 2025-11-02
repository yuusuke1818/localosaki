package jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.UpdateSmsMeterTenantBaseInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.BuildDevMeterResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterGroupNameResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterRangeTenantPriceInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTenantSmsInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterTypeResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.RangeUnitPriceResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.SearchTenantSmsResultData;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MEstimateKindServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MMeterRangeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MMunicipalityServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPrefectureServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPriceMenuLightaServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPriceMenuLightbServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantPriceInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingEstimateKindServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.BuildDevMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterGroupNameServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterRangeUnitPriceServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterTenantSmsInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.SearchTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant.UnitPriceServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MEstimateKind;
import jp.co.osaki.osol.entity.MEstimateKindPK;
import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK;
import jp.co.osaki.osol.entity.MMunicipality;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPriceMenuLighta;
import jp.co.osaki.osol.entity.MPriceMenuLightaPK;
import jp.co.osaki.osol.entity.MPriceMenuLightb;
import jp.co.osaki.osol.entity.MPriceMenuLightbPK;
import jp.co.osaki.osol.entity.MTenantPriceInfo;
import jp.co.osaki.osol.entity.MTenantPriceInfoPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.MUnitPrice;
import jp.co.osaki.osol.entity.MUnitPricePK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingEstimateKind;
import jp.co.osaki.osol.entity.TBuildingEstimateKindPK;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants.SMS_ID_SEQUENCE_NAME;

@Stateless
public class UpdateSmsMeterTenantBaseInfoDao extends OsolApiDao<UpdateSmsMeterTenantBaseInfoParameter> {

    // 所属建物に属するテナント
    private final SearchTenantSmsServiceDaoImpl searchTenantSmsServiceDaoImpl;

    // 建物
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;

    // 企業
    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;

    // 自治体マスタ
    private final MMunicipalityServiceDaoImpl mMunicipalityServiceDaoImpl;

    // 都道府県マスタ
    private final MPrefectureServiceDaoImpl mPrefectureServiceDaoImpl;

    // 推計種別マスタ
    private final MEstimateKindServiceDaoImpl mEstimateKindServiceDaoImpl;

    // 建物推計種別
    private final TBuildingEstimateKindServiceDaoImpl tBuildingEstimateKindServiceDaoImpl;

    // テナント料金情報
    private final MTenantPriceInfoServiceDaoImpl mTenantPriceInfoServiceDaoImpl;

    //  テナントユーザー
    private final MTenantSmsServiceDaoImpl mTenantSmsServiceDaoImpl;

    // 料金メニュー（従量電灯A）
    private final MPriceMenuLightaServiceDaoImpl mPriceMenuLightaServiceDaoImpl;

    // 料金メニュー（従量電灯B）
    private final MPriceMenuLightbServiceDaoImpl mPriceMenuLightbServiceDaoImpl;

    // 料金単価情報
    private final UnitPriceServiceDaoImpl unitPriceServiceDaoImpl;

    // 料金単価情報（メーター種別従量値）
    private final MeterRangeUnitPriceServiceDaoImpl meterRangeUnitPriceServiceDaoImpl;

    // テナントユーザー
    private final MeterTenantSmsInfoServiceDaoImpl meterTenantSmsInfoServiceDaoImpl;

    // メーター種別設定
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メーターグループ名称設定
    private final MeterGroupNameServiceDaoImpl meterGroupNameServiceDaoImpl;

    // 登録計器（メーター）
    private final BuildDevMeterServiceDaoImpl buildingDevMeterServiceDaoImpl;

    // メーター種別従量値情報
    private final MMeterRangeServiceDaoImpl mMeterRangeServiceDaoImpl;

    public UpdateSmsMeterTenantBaseInfoDao () {
        searchTenantSmsServiceDaoImpl = new SearchTenantSmsServiceDaoImpl();
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        mMunicipalityServiceDaoImpl = new MMunicipalityServiceDaoImpl();
        mPrefectureServiceDaoImpl = new MPrefectureServiceDaoImpl();
        mEstimateKindServiceDaoImpl = new MEstimateKindServiceDaoImpl();
        tBuildingEstimateKindServiceDaoImpl = new TBuildingEstimateKindServiceDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
        mTenantPriceInfoServiceDaoImpl = new MTenantPriceInfoServiceDaoImpl();
        mPriceMenuLightaServiceDaoImpl = new MPriceMenuLightaServiceDaoImpl();
        mPriceMenuLightbServiceDaoImpl = new MPriceMenuLightbServiceDaoImpl();
        unitPriceServiceDaoImpl = new UnitPriceServiceDaoImpl();
        meterRangeUnitPriceServiceDaoImpl = new MeterRangeUnitPriceServiceDaoImpl();
        meterTenantSmsInfoServiceDaoImpl = new MeterTenantSmsInfoServiceDaoImpl();
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterGroupNameServiceDaoImpl = new MeterGroupNameServiceDaoImpl();
        buildingDevMeterServiceDaoImpl = new BuildDevMeterServiceDaoImpl();
        mMeterRangeServiceDaoImpl = new MMeterRangeServiceDaoImpl();
    }

    @Override
    public UpdateSmsMeterTenantBaseInfoResult query(UpdateSmsMeterTenantBaseInfoParameter parameter) throws Exception {

        UpdateSmsMeterTenantBaseInfoResult result = new UpdateSmsMeterTenantBaseInfoResult();

        UpdateSmsMeterTenantBaseInfoRequest reqParam = parameter.getRequestSet();
        if (reqParam == null) {
            throw new Exception();
        }

        // 企業ID（テナント企業ID）
        String corpId = reqParam.getCorpId();

        // 建物ID（テナント建物ID）
        Long buildingId = reqParam.getBuildingId();

        // 所属企業ID
        String divisionCorpId = reqParam.getDivisionCorpId();

        // 所属建物ID
        Long divisionBuildingId = reqParam.getDivisionBuildingId();

        // 現在日時
        Timestamp svDate = getServerDateTime();

        // 担当者情報
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();

        // 新規
        if (!reqParam.isUpdateProcessFlg()) {
            Long newBuildingId = createBuildingId();

            TBuildingPK tBuidlingPK = new TBuildingPK();
            TBuilding tBuidling = new TBuilding();
            tBuidlingPK.setCorpId(divisionCorpId);
            tBuidlingPK.setBuildingId(newBuildingId);
            tBuidling.setId(tBuidlingPK);

            tBuidling.setBuildingNo(reqParam.getBuildingNo());
            tBuidling.setBuildingName(reqParam.getBuildingName());
            tBuidling.setBuildingNameKana(reqParam.getBuildingNameKana());
            tBuidling.setBuildingTansyukuName(reqParam.getBuildingTansyukuName());

            // 自治体
            if (reqParam.getMunicipalityCd() != null) {
                MMunicipality mMunicipality = new MMunicipality();
                mMunicipality.setMunicipalityCd(reqParam.getMunicipalityCd());
                tBuidling.setMMunicipality(find(mMunicipalityServiceDaoImpl, mMunicipality));
            } else {
                tBuidling.setMMunicipality(null);
            }

            // 都道府県
            if (reqParam.getPrefectureCd() != null) {
                MPrefecture mPrefecture = new MPrefecture();
                mPrefecture.setPrefectureCd(reqParam.getPrefectureCd());
                tBuidling.setMPrefecture(find(mPrefectureServiceDaoImpl, mPrefecture));
            } else {
                tBuidling.setMPrefecture(null);
            }
            tBuidling.setZipCd(reqParam.getZipCd());
            tBuidling.setAddress(reqParam.getMunicipalityName());
            tBuidling.setAddressBuilding(reqParam.getAddressBuilding());
            tBuidling.setTelNo(reqParam.getTelNo());
            tBuidling.setFaxNo(reqParam.getFaxNo());
            tBuidling.setBiko(reqParam.getBiko());
            tBuidling.setNyukyoTypeCd(ApiGenericTypeConstants.NYUKYO_TYPE.NONE.getVal());

            if (reqParam.getTotalStartYm() != null) {
                tBuidling.setTotalStartYm(DateUtility.conversionDate(reqParam.getTotalStartYm(),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            }
            if (reqParam.getTotalEndYm() != null) {
                tBuidling.setTotalEndYm(DateUtility.conversionDate(reqParam.getTotalEndYm(),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            } else if (reqParam.getTotalEndYm() == null) {
                // 集計終了年月が未入力だった場合のみnull更新
                tBuidling.setTotalEndYm(null);
            }

            tBuidling.setEstimateUse(reqParam.getEstimateUse());
            tBuidling.setDivisionCorpId(divisionCorpId);
            tBuidling.setDivisionBuildingId(divisionBuildingId);

            // 新規登録 固定項目
            tBuidling.setFreonDischargeOffice(OsolConstants.FLG_OFF);
            tBuidling.setVersion(0);
            tBuidling.setCreateDate(svDate);
            tBuidling.setCreateUserId(userId);
            tBuidling.setUpdateDate(svDate);
            tBuidling.setUpdateUserId(userId);
            tBuidling.setDelFlg(OsolConstants.FLG_OFF);
            tBuidling.setBuildingType(OsolConstants.BUILDING_TYPE.TENANT.getVal());
            // 公開フラグ
            if (Objects.equals(reqParam.getPublicFlg(), OsolConstants.FLG_ON)) {
                tBuidling.setPublicFlg(OsolConstants.FLG_ON);
            } else {
                tBuidling.setPublicFlg(OsolConstants.FLG_OFF);
            }
            // 建物更新（登録）
            persist(tBuildingApiServiceDaoImpl, tBuidling);

            // 登録後テナントの企業ID、建物IDを設定（最新値取得用）
            corpId = tBuidling.getId().getCorpId();
            buildingId = tBuidling.getId().getBuildingId();

            // 推計が選択されていれば-1以外が入るはず
            if (reqParam.getEstimateId() != null
                    && !"-1".equals(reqParam.getEstimateId().toString())) {
                MEstimateKindPK mEstimateKindPK = new MEstimateKindPK();
                MEstimateKind mEstimateKind = new MEstimateKind();
                mEstimateKindPK.setEstimateId(reqParam.getEstimateId());
                mEstimateKind.setId(mEstimateKindPK);
                MEstimateKind mEstimateKindEntity = find(mEstimateKindServiceDaoImpl, mEstimateKind);

                // 推計が選択されていて、該当IDがマスタに登録されていたら建物推計種別TBLに登録
                if (mEstimateKindEntity != null) {
                    TBuildingEstimateKindPK tBuildingEstimateKindPK = new TBuildingEstimateKindPK();
                    TBuildingEstimateKind tBuildingEstimateKind = new TBuildingEstimateKind();
                    tBuildingEstimateKindPK.setCorpId(divisionCorpId);
                    tBuildingEstimateKindPK.setBuildingId(newBuildingId);
                    tBuildingEstimateKindPK.setEstimateId(mEstimateKindEntity.getId().getEstimateId());
                    tBuildingEstimateKindPK.setEstimateCorpId(mEstimateKindEntity.getId().getCorpId());
                    tBuildingEstimateKind.setId(tBuildingEstimateKindPK);
                    tBuildingEstimateKind.setCreateDate(svDate);
                    tBuildingEstimateKind.setCreateUserId(userId);
                    tBuildingEstimateKind.setUpdateDate(svDate);
                    tBuildingEstimateKind.setUpdateUserId(userId);
                    tBuildingEstimateKind.setDelFlg(OsolConstants.FLG_OFF);
                    persist(tBuildingEstimateKindServiceDaoImpl, tBuildingEstimateKind);
                }
            }


            // 企業情報を更新
            MCorp mCorp = new MCorp();
            mCorp.setCorpId(divisionCorpId);
            MCorp mCorpEntity = find(mCorpApiServiceDaoImpl, mCorp);
            mCorpEntity.setUpdateDate(svDate);
            mCorpEntity.setUpdateUserId(userId);
            merge(mCorpApiServiceDaoImpl, mCorpEntity);

            MTenantSmPK mTenantSmPK = new MTenantSmPK();
            MTenantSm mTenantSm = new MTenantSm();
            mTenantSmPK.setCorpId(divisionCorpId);
            mTenantSmPK.setBuildingId(newBuildingId);
            mTenantSm.setId(mTenantSmPK);
            mTenantSm.setTenantId(reqParam.getTenantId());
            mTenantSm.setRecMan(String.valueOf(userId));
            mTenantSm.setRecDate(svDate);
            mTenantSm.setFixed1Name(reqParam.getFixedName1());
            mTenantSm.setFixed1Price(reqParam.getFixedPrice1());
            mTenantSm.setFixed2Name(reqParam.getFixedName2());
            mTenantSm.setFixed2Price(reqParam.getFixedPrice2());
            mTenantSm.setFixed3Name(reqParam.getFixedName3());
            mTenantSm.setFixed3Price(reqParam.getFixedPrice3());
            mTenantSm.setFixed4Name(reqParam.getFixedName4());
            mTenantSm.setFixed4Price(reqParam.getFixedPrice4());
            mTenantSm.setPriceMenuNo(reqParam.getPriceMenuNo());
            mTenantSm.setContractCapacity(BigDecimal.valueOf(1));
            mTenantSm.setDivRate1(reqParam.getDivRate1());
            mTenantSm.setDivRate2(reqParam.getDivRate2());
            mTenantSm.setDivRate3(reqParam.getDivRate3());
            mTenantSm.setDivRate4(reqParam.getDivRate4());
            mTenantSm.setDivRate5(reqParam.getDivRate5());
            mTenantSm.setDivRate6(reqParam.getDivRate6());
            mTenantSm.setDivRate7(reqParam.getDivRate7());
            mTenantSm.setDivRate8(reqParam.getDivRate8());
            mTenantSm.setDivRate9(reqParam.getDivRate9());
            mTenantSm.setDivRate10(reqParam.getDivRate10());
            mTenantSm.setDelFlg(OsolConstants.FLG_OFF);
            mTenantSm.setVersion(0);
            mTenantSm.setCreateDate(svDate);
            mTenantSm.setCreateUserId(userId);
            mTenantSm.setUpdateDate(svDate);
            mTenantSm.setUpdateUserId(userId);
            // テナントユーザー情報（メーターユーザー）更新（登録）
            persist(mTenantSmsServiceDaoImpl, mTenantSm);

        }

        // 更新
        else {
            // テナントが削除されたか判別用
            boolean tenantDelFlg = false;

            TBuildingPK tBuidlingPK = new TBuildingPK();
            TBuilding tBuidling = new TBuilding();
            tBuidlingPK.setCorpId(corpId);
            tBuidlingPK.setBuildingId(buildingId);
            tBuidling.setId(tBuidlingPK);

            TBuilding tBuidlingEntity = find(tBuildingApiServiceDaoImpl, tBuidling);

            // 更新レコードなし
            if (tBuidlingEntity == null) {
                return null;
            }

            tBuidlingEntity.setBuildingNo(reqParam.getBuildingNo());
            tBuidlingEntity.setBuildingName(reqParam.getBuildingName());
            tBuidlingEntity.setBuildingNameKana(reqParam.getBuildingNameKana());
            tBuidlingEntity.setBuildingTansyukuName(reqParam.getBuildingTansyukuName());

            // 自治体
            if (reqParam.getMunicipalityCd() != null) {
                MMunicipality mMunicipality = new MMunicipality();
                mMunicipality.setMunicipalityCd(reqParam.getMunicipalityCd());
                tBuidlingEntity.setMMunicipality(find(mMunicipalityServiceDaoImpl, mMunicipality));
            } else {
                tBuidlingEntity.setMMunicipality(null);
            }

            // 都道府県
            if (reqParam.getPrefectureCd() != null) {
                MPrefecture mPrefecture = new MPrefecture();
                mPrefecture.setPrefectureCd(reqParam.getPrefectureCd());
                tBuidlingEntity.setMPrefecture(find(mPrefectureServiceDaoImpl, mPrefecture));
            } else {
                tBuidlingEntity.setMPrefecture(null);
            }
            tBuidlingEntity.setZipCd(reqParam.getZipCd());
            tBuidlingEntity.setAddress(reqParam.getMunicipalityName());
            tBuidlingEntity.setAddressBuilding(reqParam.getAddressBuilding());
            tBuidlingEntity.setTelNo(reqParam.getTelNo());
            tBuidlingEntity.setFaxNo(reqParam.getFaxNo());
            tBuidlingEntity.setBiko(reqParam.getBiko());
            tBuidlingEntity.setNyukyoTypeCd(ApiGenericTypeConstants.NYUKYO_TYPE.NONE.getVal());

            if (reqParam.getTotalStartYm() != null) {
                tBuidlingEntity.setTotalStartYm(DateUtility.conversionDate(reqParam.getTotalStartYm(),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            }
            if (reqParam.getTotalEndYm() != null) {
                tBuidlingEntity.setTotalEndYm(DateUtility.conversionDate(reqParam.getTotalEndYm(),
                        DateUtility.DATE_FORMAT_YYYYMM_SLASH));
            } else if (reqParam.getTotalEndYm() == null) {
                // 集計終了年月が未入力だった場合のみnull更新
                tBuidlingEntity.setTotalEndYm(null);
            }

            tBuidlingEntity.setEstimateUse(reqParam.getEstimateUse());
//            tBuidlingEntity.setDivisionCorpId(divisionCorpId);
//            tBuidlingEntity.setDivisionBuildingId(divisionBuildingId);

            // 更新 固定項目
            tBuidlingEntity.setUpdateDate(svDate);
            tBuidlingEntity.setUpdateUserId(userId);

            if (Objects.equals(reqParam.getDelFlg(), OsolConstants.FLG_ON)) {
                // 削除時も削除フラグは立てない。削除日等をセット
                tBuidlingEntity.setDelFlg(OsolConstants.FLG_OFF);
                tBuidlingEntity.setBuildingDelDate(svDate);
                tBuidlingEntity.setBuildingDelPersonCorpId(parameter.getLoginCorpId());
                tBuidlingEntity.setBuildingDelPersonId(parameter.getLoginPersonId());
                tenantDelFlg = true;
            } else {
                tBuidlingEntity.setDelFlg(OsolConstants.FLG_OFF);
                tBuidlingEntity.setBuildingDelDate(null);
                tBuidlingEntity.setBuildingDelPersonCorpId(null);
                tBuidlingEntity.setBuildingDelPersonId(null);
            }

            // 公開フラグ
            if (Objects.equals(reqParam.getPublicFlg(), OsolConstants.FLG_ON)) {
                tBuidlingEntity.setPublicFlg(OsolConstants.FLG_ON);
            } else {
                tBuidlingEntity.setPublicFlg(OsolConstants.FLG_OFF);
            }

            // 建物更新
            merge(tBuildingApiServiceDaoImpl, tBuidlingEntity);


            // 推計が選択されていれば-1以外が入るはず
            if (reqParam.getEstimateId() != null
                    && !"-1".equals(reqParam.getEstimateId().toString())) {
                MEstimateKindPK mEstimateKindPK = new MEstimateKindPK();
                MEstimateKind mEstimateKind = new MEstimateKind();
                mEstimateKindPK.setEstimateId(reqParam.getEstimateId());
                mEstimateKind.setId(mEstimateKindPK);
                MEstimateKind mEstimateKindEntity = find(mEstimateKindServiceDaoImpl, mEstimateKind);

                // 推計が選択されていて、該当IDがマスタに登録されていたら建物推計種別TBLを更新
                if (mEstimateKindEntity != null) {
                    TBuildingEstimateKindPK delTBuildingEstimateKindPK = new TBuildingEstimateKindPK();
                    TBuildingEstimateKind delTBuildingEstimateKind = new TBuildingEstimateKind();

                    // 既に登録されていたら削除してから新規登録
                    delTBuildingEstimateKindPK.setCorpId(corpId);
                    delTBuildingEstimateKindPK.setBuildingId(buildingId);
                    delTBuildingEstimateKind.setId(delTBuildingEstimateKindPK);
                    List<TBuildingEstimateKind> delList = getResultList(tBuildingEstimateKindServiceDaoImpl, delTBuildingEstimateKind);
                    if (delList != null) {
                        for (TBuildingEstimateKind delEntity : delList) {
                            remove(tBuildingEstimateKindServiceDaoImpl, delEntity);
                        }
                    }

                    TBuildingEstimateKindPK newTBuildingEstimateKindPK = new TBuildingEstimateKindPK();
                    TBuildingEstimateKind newBuildingEstimateKind = new TBuildingEstimateKind();
                    newTBuildingEstimateKindPK.setCorpId(corpId);
                    newTBuildingEstimateKindPK.setBuildingId(buildingId);
                    newTBuildingEstimateKindPK.setEstimateCorpId(mEstimateKindEntity.getId().getCorpId());
                    newTBuildingEstimateKindPK.setEstimateId(mEstimateKindEntity.getId().getEstimateId());
                    newBuildingEstimateKind.setId(newTBuildingEstimateKindPK);
                    newBuildingEstimateKind.setCreateDate(svDate);
                    newBuildingEstimateKind.setCreateUserId(userId);
                    newBuildingEstimateKind.setUpdateDate(svDate);
                    newBuildingEstimateKind.setUpdateUserId(userId);
                    newBuildingEstimateKind.setVersion(0);
                    newBuildingEstimateKind.setDelFlg(OsolConstants.FLG_OFF);
                    persist(tBuildingEstimateKindServiceDaoImpl, newBuildingEstimateKind);

                }
            }

            // 企業情報を更新
            MCorp mCorp = new MCorp();
            mCorp.setCorpId(divisionCorpId);
            MCorp mCorpEntity = find(mCorpApiServiceDaoImpl, mCorp);
            mCorpEntity.setUpdateDate(svDate);
            mCorpEntity.setUpdateUserId(userId);


            // テナントユーザー情報更新
            MTenantSmPK mTenantSmPK = new MTenantSmPK();
            MTenantSm mTenantSm = new MTenantSm();
            mTenantSmPK.setCorpId(corpId);
            mTenantSmPK.setBuildingId(buildingId);
            mTenantSm.setId(mTenantSmPK);
            MTenantSm mTenantSmEntity = find(mTenantSmsServiceDaoImpl, mTenantSm);

            if (mTenantSmEntity != null) {
                mTenantSm.setRecMan(String.valueOf(userId));
                mTenantSm.setRecDate(svDate);
                mTenantSmEntity.setFixed1Name(reqParam.getFixedName1());
                mTenantSmEntity.setFixed1Price(reqParam.getFixedPrice1());
                mTenantSmEntity.setFixed2Name(reqParam.getFixedName2());
                mTenantSmEntity.setFixed2Price(reqParam.getFixedPrice2());
                mTenantSmEntity.setFixed3Name(reqParam.getFixedName3());
                mTenantSmEntity.setFixed3Price(reqParam.getFixedPrice3());
                mTenantSmEntity.setFixed4Name(reqParam.getFixedName4());
                mTenantSmEntity.setFixed4Price(reqParam.getFixedPrice4());
                mTenantSmEntity.setPriceMenuNo(reqParam.getPriceMenuNo());
                mTenantSmEntity.setContractCapacity(reqParam.getContractCapacity());
                mTenantSmEntity.setDivRate1(reqParam.getDivRate1());
                mTenantSmEntity.setDivRate2(reqParam.getDivRate2());
                mTenantSmEntity.setDivRate3(reqParam.getDivRate3());
                mTenantSmEntity.setDivRate4(reqParam.getDivRate4());
                mTenantSmEntity.setDivRate5(reqParam.getDivRate5());
                mTenantSmEntity.setDivRate6(reqParam.getDivRate6());
                mTenantSmEntity.setDivRate7(reqParam.getDivRate7());
                mTenantSmEntity.setDivRate8(reqParam.getDivRate8());
                mTenantSmEntity.setDivRate9(reqParam.getDivRate9());
                mTenantSmEntity.setDivRate10(reqParam.getDivRate10());

                if (tenantDelFlg) {
                    mTenantSmEntity.setDelFlg(OsolConstants.FLG_ON);
                } else {
                    mTenantSmEntity.setDelFlg(OsolConstants.FLG_OFF);
                }
                mTenantSmEntity.setCreateDate(svDate);
                mTenantSmEntity.setCreateUserId(userId);
                mTenantSmEntity.setUpdateDate(svDate);
                mTenantSmEntity.setUpdateUserId(userId);
                // テナントユーザー情報（メーターユーザー）更新
                merge(mTenantSmsServiceDaoImpl, mTenantSmEntity);
            }
            else {
                mTenantSm.setRecMan(String.valueOf(userId));
                mTenantSm.setRecDate(svDate);
                mTenantSm.setFixed1Name(reqParam.getFixedName1());
                mTenantSm.setFixed1Price(reqParam.getFixedPrice1());
                mTenantSm.setFixed2Name(reqParam.getFixedName2());
                mTenantSm.setFixed2Price(reqParam.getFixedPrice2());
                mTenantSm.setFixed3Name(reqParam.getFixedName3());
                mTenantSm.setFixed3Price(reqParam.getFixedPrice3());
                mTenantSm.setFixed4Name(reqParam.getFixedName4());
                mTenantSm.setFixed4Price(reqParam.getFixedPrice4());
                mTenantSm.setPriceMenuNo(reqParam.getPriceMenuNo());
                mTenantSm.setContractCapacity(reqParam.getContractCapacity());
                mTenantSm.setDivRate1(reqParam.getDivRate1());
                mTenantSm.setDivRate2(reqParam.getDivRate2());
                mTenantSm.setDivRate3(reqParam.getDivRate3());
                mTenantSm.setDivRate4(reqParam.getDivRate4());
                mTenantSm.setDivRate5(reqParam.getDivRate5());
                mTenantSm.setDivRate6(reqParam.getDivRate6());
                mTenantSm.setDivRate7(reqParam.getDivRate7());
                mTenantSm.setDivRate8(reqParam.getDivRate8());
                mTenantSm.setDivRate9(reqParam.getDivRate9());
                mTenantSm.setDivRate10(reqParam.getDivRate10());

                if (tenantDelFlg) {
                    mTenantSm.setDelFlg(OsolConstants.FLG_ON);
                } else {
                    mTenantSm.setDelFlg(OsolConstants.FLG_OFF);
                }
                mTenantSm.setVersion(0);
                mTenantSm.setCreateDate(svDate);
                mTenantSm.setCreateUserId(userId);
                mTenantSm.setUpdateDate(svDate);
                mTenantSm.setUpdateUserId(userId);
                // テナントユーザー情報（メーターユーザー）登録
                persist(mTenantSmsServiceDaoImpl, mTenantSmEntity);
            }

            // メーター種別詳細
            if (reqParam.getMeterTypeDetailList() != null) {
                for (UpdateSmsMeterTenantBaseInfoRequestSet meterTypeDetail : reqParam.getMeterTypeDetailList()) {

                    // 所属企業、所属建物が同じ他テナントも更新（SMS利用テナントに限る）
                    List <SearchTenantSmsResultData> tenantSmsList = new ArrayList<>();

                    // テナント一括
                    if (meterTypeDetail.isBulkFlg()) {
                        SearchTenantSmsResultData searchTenantSmsResultData = new SearchTenantSmsResultData();
                        searchTenantSmsResultData.setDivisionCorpId(divisionCorpId);
                        searchTenantSmsResultData.setDivisionBuildingId(divisionBuildingId);
                        tenantSmsList = getResultList(searchTenantSmsServiceDaoImpl, searchTenantSmsResultData);
                    }
                    else {
                        SearchTenantSmsResultData searchTenantSmsResultData = new SearchTenantSmsResultData();
                        searchTenantSmsResultData.setCorpId(corpId);
                        searchTenantSmsResultData.setBuildingId(buildingId);
                        SearchTenantSmsResultData tenantSms = find(searchTenantSmsServiceDaoImpl, searchTenantSmsResultData);
                        tenantSmsList.add(tenantSms);
                    }

                    // 更新対象テナント数
                    for (SearchTenantSmsResultData tenantSms : tenantSmsList) {
                        MTenantPriceInfoPK mTenantPriceInfoPK = new MTenantPriceInfoPK();
                        MTenantPriceInfo mTenantPriceInfo = new MTenantPriceInfo();
                        mTenantPriceInfoPK.setMeterType(meterTypeDetail.getMeterType());
                        mTenantPriceInfoPK.setPowerPlanId(meterTypeDetail.getMenuNo());
                        mTenantPriceInfoPK.setCorpId(tenantSms.getCorpId());
                        mTenantPriceInfoPK.setBuildingId(tenantSms.getBuildingId());
                        mTenantPriceInfo.setId(mTenantPriceInfoPK);
                        MTenantPriceInfo mTenantPriceInfoEntity = find(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfo);
                        // 料金プランID
                        long pricePlanId;

                        // 削除フラグがあり、自テナントならば削除
                        if (mTenantPriceInfoEntity != null && tenantDelFlg && tenantSms.getCorpId().equals(corpId) && tenantSms.getBuildingId().equals(buildingId)) {
                            pricePlanId = mTenantPriceInfoEntity.getId().getPricePlanId().longValue();
                            remove(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfoEntity);
                        }
                        // 更新
                        else if (mTenantPriceInfoEntity != null) {
                            mTenantPriceInfoEntity.setRecMan(String.valueOf(userId));
                            mTenantPriceInfoEntity.setRecDate(svDate);
                            mTenantPriceInfoEntity.setUpdateUserId(userId);
                            mTenantPriceInfoEntity.setUpdateDate(svDate);
                            mTenantPriceInfoEntity.setDiscountRate(meterTypeDetail.getDiscountRate());

                            // 料金メニュー（その他）
                            if (meterTypeDetail.getMenuNo().longValue() == 0L) {
                                mTenantPriceInfoEntity.setBasicPrice(meterTypeDetail.getBasicPrice());
                            }
                            else {
                                mTenantPriceInfoEntity.setBasicPrice(null);
                            }
                            mTenantPriceInfoEntity  = merge(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfoEntity);
                            pricePlanId = mTenantPriceInfoEntity.getId().getPricePlanId().longValue();
                        }
                        // 新規
                        else {
                            pricePlanId = createPricePlanId().longValue();
                            mTenantPriceInfoPK.setPricePlanId(pricePlanId);
                            mTenantPriceInfo.setId(mTenantPriceInfoPK);
                            mTenantPriceInfo.setRecMan(String.valueOf(userId));
                            mTenantPriceInfo.setRecDate(svDate);
                            mTenantPriceInfo.setCreateUserId(userId);
                            mTenantPriceInfo.setCreateDate(svDate);
                            mTenantPriceInfo.setUpdateUserId(userId);
                            mTenantPriceInfo.setUpdateDate(svDate);
                            mTenantPriceInfo.setVersion(0);

                            // 料金メニュー（その他）
                            if (meterTypeDetail.getMenuNo().longValue() == 0L) {
                                mTenantPriceInfo.setBasicPrice(meterTypeDetail.getBasicPrice());
                            }
                            else {
                                mTenantPriceInfo.setBasicPrice(null);
                            }
                            mTenantPriceInfo.setDiscountRate(meterTypeDetail.getDiscountRate());
                            persist(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfo);
                        }

                        // 料金メニュー（従量電灯A）
                        if (meterTypeDetail.getMenuNo().longValue() == 1L
                                && meterTypeDetail.getPriceMenuLightaData() != null) {
                            MPriceMenuLightaPK mPriceMenuLightaPK = new MPriceMenuLightaPK();
                            MPriceMenuLighta mPriceMenuLighta = new MPriceMenuLighta();
                            mPriceMenuLightaPK.setCorpId(tenantSms.getCorpId());
                            mPriceMenuLightaPK.setBuildingId(tenantSms.getBuildingId());
                            mPriceMenuLighta.setId(mPriceMenuLightaPK);
                            MPriceMenuLighta mPriceMenuLightaEntity = find(mPriceMenuLightaServiceDaoImpl, mPriceMenuLighta);

                            // レコードが存在して、削除フラグがあり、自テナントならば削除
                            if (mPriceMenuLightaEntity != null && tenantDelFlg && tenantSms.getCorpId().equals(corpId) && tenantSms.getBuildingId().equals(buildingId)) {
                                remove(mPriceMenuLightaServiceDaoImpl, mPriceMenuLightaEntity);
                            }
                            // 更新
                            else if (mPriceMenuLightaEntity != null) {
                                mPriceMenuLightaEntity.setLowestPrice(meterTypeDetail.getPriceMenuLightaData().getLowestPrice());
                                mPriceMenuLightaEntity.setFuelAdjustPrice(meterTypeDetail.getPriceMenuLightaData().getFuelAdjustPrice());
                                mPriceMenuLightaEntity.setAdjustPriceOver15(meterTypeDetail.getPriceMenuLightaData().getAdjustPriceOver15());
                                mPriceMenuLightaEntity.setRenewEnerPrice(meterTypeDetail.getPriceMenuLightaData().getRenewEnerPrice());
                                mPriceMenuLightaEntity.setRenewPriceOver15(meterTypeDetail.getPriceMenuLightaData().getRenewPriceOver15());
                                mPriceMenuLightaEntity.setRecMan(String.valueOf(userId));
                                mPriceMenuLightaEntity.setRecDate(svDate);
                                mPriceMenuLightaEntity.setUpdateUserId(userId);
                                mPriceMenuLightaEntity.setUpdateDate(svDate);
                                merge(mPriceMenuLightaServiceDaoImpl, mPriceMenuLightaEntity);
                            }
                            // 新規
                            else {
                                mPriceMenuLighta.setLowestPrice(meterTypeDetail.getPriceMenuLightaData().getLowestPrice());
                                mPriceMenuLighta.setFuelAdjustPrice(meterTypeDetail.getPriceMenuLightaData().getFuelAdjustPrice());
                                mPriceMenuLighta.setAdjustPriceOver15(meterTypeDetail.getPriceMenuLightaData().getAdjustPriceOver15());
                                mPriceMenuLighta.setRenewEnerPrice(meterTypeDetail.getPriceMenuLightaData().getRenewEnerPrice());
                                mPriceMenuLighta.setRenewPriceOver15(meterTypeDetail.getPriceMenuLightaData().getRenewPriceOver15());
                                mPriceMenuLighta.setRecMan(String.valueOf(userId));
                                mPriceMenuLighta.setRecDate(svDate);
                                mPriceMenuLighta.setCreateUserId(userId);
                                mPriceMenuLighta.setCreateDate(svDate);
                                mPriceMenuLighta.setUpdateUserId(userId);
                                mPriceMenuLighta.setUpdateDate(svDate);
                                mPriceMenuLighta.setVersion(0);
                                persist(mPriceMenuLightaServiceDaoImpl, mPriceMenuLighta);
                            }
                        }

                        // 料金メニュー（従量電灯B）
                        if (meterTypeDetail.getMenuNo().longValue() == 2L
                                && meterTypeDetail.getPriceMenuLightbData() != null) {
                            MPriceMenuLightbPK mPriceMenuLightbPK = new MPriceMenuLightbPK();
                            MPriceMenuLightb mPriceMenuLightb = new MPriceMenuLightb();
                            mPriceMenuLightbPK.setCorpId(tenantSms.getCorpId());
                            mPriceMenuLightbPK.setBuildingId(tenantSms.getBuildingId());
                            mPriceMenuLightb.setId(mPriceMenuLightbPK);
                            MPriceMenuLightb mPriceMenuLightbEntity = find(mPriceMenuLightbServiceDaoImpl, mPriceMenuLightb);

                            // 削除フラグがあり、自テナントならば削除
                            if (mPriceMenuLightbEntity != null && tenantDelFlg && tenantSms.getCorpId().equals(corpId) && tenantSms.getBuildingId().equals(buildingId)) {
                                remove(mPriceMenuLightbServiceDaoImpl, mPriceMenuLightbEntity);
                            }
                            // 更新
                            else if (mPriceMenuLightbEntity != null) {
                                mPriceMenuLightbEntity.setBasicPrice(meterTypeDetail.getPriceMenuLightbData().getBasicPrice());
                                mPriceMenuLightbEntity.setFuelAdjustPrice(meterTypeDetail.getPriceMenuLightbData().getFuelAdjustPrice());
                                mPriceMenuLightbEntity.setRenewEnerPrice(meterTypeDetail.getPriceMenuLightbData().getRenewEnerPrice());
                                mPriceMenuLightbEntity.setRecMan(String.valueOf(userId));
                                mPriceMenuLightbEntity.setRecDate(svDate);
                                mPriceMenuLightbEntity.setUpdateUserId(userId);
                                mPriceMenuLightbEntity.setUpdateDate(svDate);
                                merge(mPriceMenuLightbServiceDaoImpl, mPriceMenuLightbEntity);
                            }
                            // 新規
                            else {
                                mPriceMenuLightb.setBasicPrice(meterTypeDetail.getPriceMenuLightbData().getBasicPrice());
                                mPriceMenuLightb.setFuelAdjustPrice(meterTypeDetail.getPriceMenuLightbData().getFuelAdjustPrice());
                                mPriceMenuLightb.setRenewEnerPrice(meterTypeDetail.getPriceMenuLightbData().getRenewEnerPrice());
                                mPriceMenuLightb.setRecMan(String.valueOf(userId));
                                mPriceMenuLightb.setRecDate(svDate);
                                mPriceMenuLightb.setCreateUserId(userId);
                                mPriceMenuLightb.setCreateDate(svDate);
                                mPriceMenuLightb.setUpdateUserId(userId);
                                mPriceMenuLightb.setUpdateDate(svDate);
                                mPriceMenuLightb.setVersion(0);
                                persist(mPriceMenuLightbServiceDaoImpl, mPriceMenuLightb);
                            }
                        }

                        // 削除フラグがあり、自テナントならば削除
                        if (tenantDelFlg && tenantSms.getCorpId().equals(corpId) && tenantSms.getBuildingId().equals(buildingId)) {
                            Map<String, List<Object>> paramMap = new HashMap<>();

                            List<Object> pricePlanIdList = new ArrayList<>();
                            pricePlanIdList.add(pricePlanId);
                            paramMap.put("pricePlanId", pricePlanIdList);
                            // 一括削除実行
                            executeUpdate(unitPriceServiceDaoImpl, paramMap);
                        }
                        // 料金単価情報を更新
                        else if (meterTypeDetail.getRangeUnitPriceDataList() != null) {
                            for (RangeUnitPriceResultData rangeUnitPriceResultData : meterTypeDetail.getRangeUnitPriceDataList()) {
                                MUnitPricePK mUnitPricePK = new MUnitPricePK();
                                MUnitPrice mUnitPrice = new MUnitPrice();
                                mUnitPricePK.setPricePlanId(pricePlanId);
                                mUnitPricePK.setLimitUsageVal(rangeUnitPriceResultData.getRangeValue());
                                mUnitPrice.setId(mUnitPricePK);
                                MUnitPrice mUnitPriceEntity = find(unitPriceServiceDaoImpl, mUnitPrice);

                                if (mUnitPriceEntity != null) {
                                    mUnitPriceEntity.setUnitPrice(rangeUnitPriceResultData.getUnitPrice());
                                    mUnitPriceEntity.setRecMan(String.valueOf(userId));
                                    mUnitPriceEntity.setRecDate(svDate);
                                    mUnitPriceEntity.setUpdateUserId(userId);
                                    mUnitPriceEntity.setUpdateDate(svDate);
                                    merge(unitPriceServiceDaoImpl, mUnitPriceEntity);
                                }
                                else {
                                    mUnitPrice.setUnitPrice(rangeUnitPriceResultData.getUnitPrice());
                                    mUnitPrice.setRecMan(String.valueOf(userId));
                                    mUnitPrice.setRecDate(svDate);
                                    mUnitPrice.setCreateUserId(userId);
                                    mUnitPrice.setCreateDate(svDate);
                                    mUnitPrice.setUpdateUserId(userId);
                                    mUnitPrice.setUpdateDate(svDate);
                                    mUnitPrice.setVersion(0);
                                    persist(unitPriceServiceDaoImpl, mUnitPrice);
                                }
                            }
                            // メーター種別従量値情報にあるデータ以外は削除
                            Map<String, List<Object>> paramMap =
                                    createParameterMap(divisionCorpId, divisionBuildingId,
                                            meterTypeDetail.getMeterType(), meterTypeDetail.getMenuNo(), pricePlanId);
                            // 一括削除実行
                            executeUpdate(meterRangeUnitPriceServiceDaoImpl, paramMap);
                        }
                    }
                }
            }
        }

        // 最新の建物情報取得
        TBuildingPK tBuildingPK = new TBuildingPK();
        TBuilding tBuilding = new TBuilding();
        tBuildingPK.setCorpId(corpId);
        tBuildingPK.setBuildingId(buildingId);
        tBuilding.setId(tBuildingPK);

        TBuilding tenantInfo = find(tBuildingApiServiceDaoImpl, tBuilding);
        // 建物情報を設定
        result.setCorpId(tenantInfo.getId().getCorpId());
        result.setBuildingId(tenantInfo.getId().getBuildingId());
        result.setBuildingNo(tenantInfo.getBuildingNo());
        result.setBuildingName(tenantInfo.getBuildingName());
        result.setBuildingTansyukuName(tenantInfo.getBuildingTansyukuName());
        result.setBuildingNameKana(tenantInfo.getBuildingNameKana());
        result.setZipCd(tenantInfo.getZipCd());
        result.setPrefectureCd(tenantInfo.getMPrefecture().getPrefectureCd());

        if (tenantInfo.getMMunicipality() != null) {
            result.setMunicipalityCd(tenantInfo.getMMunicipality().getMunicipalityCd());
        }
        result.setMunicipalityName(tenantInfo.getAddress());
        result.setAddressBuilding(tenantInfo.getAddressBuilding());
        result.setTelNo(tenantInfo.getTelNo());
        result.setFaxNo(tenantInfo.getFaxNo());
        result.setBiko(tenantInfo.getBiko());
        result.setBuildingDelDate(tenantInfo.getBuildingDelDate());
        result.setPublicFlg(tenantInfo.getPublicFlg());
        result.setDelFlg(tenantInfo.getDelFlg());
        result.setVersion(tenantInfo.getVersion());
        // 利用機能を設定
        result.setTotalStartYm(tenantInfo.getTotalStartYm());
        result.setTotalEndYm(tenantInfo.getTotalEndYm());
        result.setEstimateUse(tenantInfo.getEstimateUse());

        // 推計種別取得
        if (tenantInfo.getTBuildingEstimateKinds() != null && !tenantInfo.getTBuildingEstimateKinds().isEmpty()
                && tenantInfo.getTBuildingEstimateKinds().get(0) != null) {
            result.setEstimateId(tenantInfo.getTBuildingEstimateKinds().get(0).getId().getEstimateId());
        }

        // メーター種別関連
        MeterTypeResultData meterTypeAllResultData = new MeterTypeResultData();
        meterTypeAllResultData.setCorpId(divisionCorpId);
        meterTypeAllResultData.setBuildingId(divisionBuildingId);
        List<MeterTypeResultData> meterTypeAllList = getResultList(meterTypeServiceDaoImpl, meterTypeAllResultData);
        List<MeterRangeTenantPriceInfoResultData> MeterRangeTenantPriceInfoList = new ArrayList<>();

        for (MeterTypeResultData target : meterTypeAllList) {
            // 従量値範囲をリストで取得
            MMeterRangePK mMeterRangePK = new MMeterRangePK();
            MMeterRange mMeterRange = new MMeterRange();
            mMeterRangePK.setCorpId(target.getCorpId());
            mMeterRangePK.setBuildingId(target.getBuildingId());
            mMeterRangePK.setMeterType(target.getMeterType());
            mMeterRangePK.setMenuNo(target.getMenuNo());
            mMeterRange.setId(mMeterRangePK);
            List<MMeterRange> mMeterRangeList = getResultList(mMeterRangeServiceDaoImpl, mMeterRange);

            // 従量料金単価リストを作成
            // 従量値範囲をリストが空の場合nullを設定
            List<RangeUnitPriceResultData> rangeUnitPriceResultDataList = (mMeterRangeList.isEmpty() ? null
                    : new ArrayList<>());

            for (MMeterRange entity : mMeterRangeList) {
                RangeUnitPriceResultData rangeUnitPriceResultData = new RangeUnitPriceResultData();
                rangeUnitPriceResultData.setRangeValue(entity.getId().getRangeValue());
                rangeUnitPriceResultDataList.add(rangeUnitPriceResultData);
            }

            MeterRangeTenantPriceInfoResultData meterRangeTenantPriceInfo = new MeterRangeTenantPriceInfoResultData();
            meterRangeTenantPriceInfo.setMeterType(target.getMeterType());
            meterRangeTenantPriceInfo.setMenuNo(target.getMenuNo());
            meterRangeTenantPriceInfo.setMeterTypeName(target.getMeterTypeName());
            meterRangeTenantPriceInfo.setUnitUsageBased(target.getUnitUsageBased());
            meterRangeTenantPriceInfo.setRangeUnitPriceResultDataList(rangeUnitPriceResultDataList);
            MeterRangeTenantPriceInfoList.add(meterRangeTenantPriceInfo);
        }

        // メーターテナント情報、料金メニュー（従量電灯A）、電気料金メニュー（従量電灯B）
        MeterTenantSmsInfoResultData meterTenantSmsInfoResultData = new MeterTenantSmsInfoResultData();
        meterTenantSmsInfoResultData.setCorpId(corpId);
        meterTenantSmsInfoResultData.setBuildingId(buildingId);
        MeterTenantSmsInfoResultData meterTenantSmsInfoEntity = find(meterTenantSmsInfoServiceDaoImpl, meterTenantSmsInfoResultData);

        // Entityが存在しない場合、テナントまだ登録されていない
        if (meterTenantSmsInfoEntity != null) {
            result.setTenantId(meterTenantSmsInfoEntity.getTenantId());
            result.setFixedName1(meterTenantSmsInfoEntity.getFixedName1());
            result.setFixedPrice1(meterTenantSmsInfoEntity.getFixedPrice1());
            result.setFixedName2(meterTenantSmsInfoEntity.getFixedName2());
            result.setFixedPrice2(meterTenantSmsInfoEntity.getFixedPrice2());
            result.setFixedName3(meterTenantSmsInfoEntity.getFixedName3());
            result.setFixedPrice3(meterTenantSmsInfoEntity.getFixedPrice3());
            result.setFixedName4(meterTenantSmsInfoEntity.getFixedName4());
            result.setFixedPrice4(meterTenantSmsInfoEntity.getFixedPrice4());
            result.setPriceMenuNo(meterTenantSmsInfoEntity.getPriceMenuNo());
            result.setContractCapacity(meterTenantSmsInfoEntity.getContractCapacity());
            result.setDivRate1(meterTenantSmsInfoEntity.getDivRate1());
            result.setDivRate2(meterTenantSmsInfoEntity.getDivRate2());
            result.setDivRate3(meterTenantSmsInfoEntity.getDivRate3());
            result.setDivRate4(meterTenantSmsInfoEntity.getDivRate4());
            result.setDivRate5(meterTenantSmsInfoEntity.getDivRate5());
            result.setDivRate6(meterTenantSmsInfoEntity.getDivRate6());
            result.setDivRate7(meterTenantSmsInfoEntity.getDivRate7());
            result.setDivRate8(meterTenantSmsInfoEntity.getDivRate8());
            result.setDivRate9(meterTenantSmsInfoEntity.getDivRate9());
            result.setDivRate10(meterTenantSmsInfoEntity.getDivRate10());

            // テナント料金情報取得
            for (MeterRangeTenantPriceInfoResultData target : MeterRangeTenantPriceInfoList) {

                // 料金メニュー（従量電灯A）設定
                if (target.getMeterType().longValue() == 1L
                        && target.getMenuNo().longValue() == Long.valueOf(ApiGenericTypeConstants.SMS_ELECTRIC_MENU_312.LIGHT_A.getVal())) {
                    target.setPriceMenuLightaResultData(meterTenantSmsInfoEntity.getPriceMenuLightaResultData());
                }

                // 料金メニュー（従量電灯B）設定
                if (target.getMeterType().longValue() == 1L
                        && target.getMenuNo().longValue() == Long.valueOf(ApiGenericTypeConstants.SMS_ELECTRIC_MENU_312.LIGHT_B.getVal())) {
                    target.setPriceMenuLightbResultData(meterTenantSmsInfoEntity.getPriceMenuLightbResultData());
                }

                MTenantPriceInfoPK mTenantPriceInfoPK = new MTenantPriceInfoPK();
                MTenantPriceInfo mTenantPriceInfo = new MTenantPriceInfo();
                mTenantPriceInfoPK.setMeterType(target.getMeterType());
                mTenantPriceInfoPK.setCorpId(tenantInfo.getId().getCorpId());
                mTenantPriceInfoPK.setBuildingId(tenantInfo.getId().getBuildingId());
                mTenantPriceInfoPK.setPowerPlanId(target.getMenuNo());
                mTenantPriceInfo.setId(mTenantPriceInfoPK);
                MTenantPriceInfo mTenantPriceInfoEntity = find(mTenantPriceInfoServiceDaoImpl, mTenantPriceInfo);

                // テナント料金情報が存在する
                if (mTenantPriceInfoEntity != null) {
                    target.setPricePlanId(mTenantPriceInfoEntity.getId().getPricePlanId());

                    // 料金メニュー（従量電灯A）以外は設定
                    if (mTenantPriceInfoEntity.getId().getMeterType().longValue() != 1L
                            && mTenantPriceInfoEntity.getId().getPowerPlanId() != 1L) {
                        target.setBasicPrice(mTenantPriceInfoEntity.getBasicPrice());
                    }

                    target.setDiscountRate(mTenantPriceInfoEntity.getDiscountRate());

                    // 料金単価情報から取得
                    // メーター従量値がなければ料金単価情報は必要ない
                    if (target.getRangeUnitPriceResultDataList() != null && target.getPricePlanId() != null) {
                        MUnitPricePK mUnitPricePK = new MUnitPricePK();
                        MUnitPrice mUnitPrice = new MUnitPrice();
                        mUnitPricePK.setPricePlanId(target.getPricePlanId());
                        mUnitPrice.setId(mUnitPricePK);
                        List<MUnitPrice> mUnitPriceList = getResultList(meterRangeUnitPriceServiceDaoImpl, mUnitPrice);

                        if (!mUnitPriceList.isEmpty()) {
                            // 従量範囲リストの従量値に一致する料金単価情報が存在
                            for (RangeUnitPriceResultData rangeUnitPriceResultData : target.getRangeUnitPriceResultDataList()) {
                                for (MUnitPrice mUnitPriceEntity : mUnitPriceList) {
                                    // 料金単価設定
                                    if (rangeUnitPriceResultData.getRangeValue().longValue() == mUnitPriceEntity.getId().getLimitUsageVal().longValue()) {
                                        rangeUnitPriceResultData.setUnitPrice(mUnitPriceEntity.getUnitPrice());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // 登録計器（メーター）ID取得
        BuildDevMeterResultData buildDevMeterResultData = new BuildDevMeterResultData();
        buildDevMeterResultData.setCorpId(tenantInfo.getId().getCorpId());
        buildDevMeterResultData.setBuildingId(tenantInfo.getId().getBuildingId());
        List<BuildDevMeterResultData> buildingDevMeterList = getResultList(buildingDevMeterServiceDaoImpl,
                buildDevMeterResultData);

        // 登録計器（メーター）ID設定
        result.setBuildDevMeterList(buildingDevMeterList);

        // メーターグループ名称設定取得
        MeterGroupNameResultData meterGroupNameResultData = new MeterGroupNameResultData();
        meterGroupNameResultData.setCorpId(divisionCorpId);
        meterGroupNameResultData.setBuildingId(divisionBuildingId);
        List<MeterGroupNameResultData> meterGroupNameList = getResultList(meterGroupNameServiceDaoImpl,
                meterGroupNameResultData);
        result.setMeterGroupNameList(meterGroupNameList);
        result.setMeterTypeList(MeterRangeTenantPriceInfoList);

        return result;
    }

    private Map<String, List<Object>> createParameterMap(String corpId, Long building, Long meterType, Long menuNo, Long pricePlanId) {
        Map<String, List<Object>> paramMap = new HashMap<>();

        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(corpId);
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);

        List<Object> buildingList = new ArrayList<>();
        buildingList.add(building);
        paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingList);


        List<Object> meterTypeList = new ArrayList<>();
        meterTypeList.add(meterType);
        paramMap.put("meterType", meterTypeList);

        List<Object> menuNoList = new ArrayList<>();
        menuNoList.add(menuNo);
        paramMap.put("menuNo", menuNoList);

        List<Object> pricePlanIdList = new ArrayList<>();
        pricePlanIdList.add(pricePlanId);
        paramMap.put("pricePlanId", pricePlanIdList);

        return paramMap;
    }

    /**
    *
    * 建物ID採番
    *
    * @return 新規採番された建物ID
    */
   public Long createBuildingId() {

       return super.createId(ID_SEQUENCE_NAME.BUILDING_ID.getVal());

   }

   /**
   *
   * 料金プランID採番
   *
   * @return 新規採番された料金プランID
   */
  public Long createPricePlanId() {

      return super.createId(SMS_ID_SEQUENCE_NAME.PRICE_PLAN_ID.getVal());

  }

}
