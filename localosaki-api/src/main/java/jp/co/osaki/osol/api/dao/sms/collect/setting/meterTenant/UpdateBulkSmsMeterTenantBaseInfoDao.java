package jp.co.osaki.osol.api.dao.sms.collect.setting.meterTenant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolConstants.ID_SEQUENCE_NAME;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoResult;
import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPrefectureServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MTenantSmsServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants.SMS_ID_SEQUENCE_NAME;

@Stateless
public class UpdateBulkSmsMeterTenantBaseInfoDao extends OsolApiDao<UpdateBulkSmsMeterTenantBaseInfoParameter> {

    // 建物
    private final TBuildingApiServiceDaoImpl tBuildingApiServiceDaoImpl;

    // 企業
    private final MCorpApiServiceDaoImpl mCorpApiServiceDaoImpl;

    // 都道府県マスタ
    private final MPrefectureServiceDaoImpl mPrefectureServiceDaoImpl;

    //  テナントユーザー
    private final MTenantSmsServiceDaoImpl mTenantSmsServiceDaoImpl;

    public UpdateBulkSmsMeterTenantBaseInfoDao () {
        tBuildingApiServiceDaoImpl = new TBuildingApiServiceDaoImpl();
        mCorpApiServiceDaoImpl = new MCorpApiServiceDaoImpl();
        mPrefectureServiceDaoImpl = new MPrefectureServiceDaoImpl();
        mTenantSmsServiceDaoImpl = new MTenantSmsServiceDaoImpl();
    }

    @Override
    public UpdateBulkSmsMeterTenantBaseInfoResult query(UpdateBulkSmsMeterTenantBaseInfoParameter parameter) throws Exception {

        UpdateBulkSmsMeterTenantBaseInfoResult result = new UpdateBulkSmsMeterTenantBaseInfoResult();

        List<UpdateBulkSmsMeterTenantBaseInfoRequestSet> reqParamList = parameter.getRequest().getRequestSetList();

        // 担当者情報
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();

        if (reqParamList != null) {
            Map<Long,Long> buildingIdMap = new HashMap<>();
            for (UpdateBulkSmsMeterTenantBaseInfoRequestSet reqParam : reqParamList) {
                if (reqParam == null) {
                    throw new Exception();
                }

                // 企業ID（テナント企業ID）
                String corpId = reqParam.getCorpId();

                // 所属企業ID
                String divisionCorpId = reqParam.getDivisionCorpId();

                // 所属建物ID
                Long divisionBuildingId = reqParam.getDivisionBuildingId();

                // 現在日時
                Timestamp svDate = getServerDateTime();

                // 新規
                if (!reqParam.isUpdateProcessFlg()) {

                    Long newBuildingId = createBuildingId();
                    buildingIdMap.put(reqParam.getTenantId(), newBuildingId);

                    TBuildingPK tBuidlingPK = new TBuildingPK();
                    TBuilding tBuidling = new TBuilding();
                    tBuidlingPK.setCorpId(divisionCorpId);
                    tBuidlingPK.setBuildingId(newBuildingId);
                    tBuidling.setId(tBuidlingPK);

                    //テナント番号
                    //指定が無い場合は親建物番号_ユーザーコード
                    tBuidling.setBuildingNo(reqParam.getBuildingNo());

                    //指定が無い場合は親建物の建物名を登録
                    if (reqParam.getBuildingName() != null ) {
                        tBuidling.setBuildingName(reqParam.getBuildingName());
                    }

                    tBuidling.setBuildingNameKana(reqParam.getBuildingNameKana());
                    tBuidling.setBuildingTansyukuName(reqParam.getBuildingTansyukuName());

                    // 自治体
                    tBuidling.setMMunicipality(null);

                    // 都道府県
                    if (reqParam.getPrefectureCd() != null) {
                        MPrefecture mPrefecture = new MPrefecture();
                        mPrefecture.setPrefectureCd(reqParam.getPrefectureCd());
                        tBuidling.setMPrefecture(find(mPrefectureServiceDaoImpl, mPrefecture));
                    } else {
                        tBuidling.setMPrefecture(null);
                    }
                    tBuidling.setZipCd(reqParam.getZipCd());
                    tBuidling.setAddress(reqParam.getAddress());
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

                    tBuidling.setEstimateUse(ApiGenericTypeConstants.ESTIMATE_USE.NOT_USE.getVal());
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
                    // 建物ID（テナント建物ID）
                    Long buildingId;
                    if (reqParam.getBuildingId() != null) {
                        buildingId = reqParam.getBuildingId();
                    } else if (buildingIdMap.containsKey(reqParam.getTenantId())) {
                        buildingId = buildingIdMap.get(reqParam.getTenantId());
                    } else {
                        return null;
                    }
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

                    if (reqParam.getBuildingNo() != null) {
                        tBuidlingEntity.setBuildingNo(reqParam.getBuildingNo());
                    }

                    if (reqParam.getBuildingName() != null) {
                        tBuidlingEntity.setBuildingName(reqParam.getBuildingName());
                    }

                    tBuidlingEntity.setBuildingNameKana(reqParam.getBuildingNameKana());
                    tBuidlingEntity.setBuildingTansyukuName(reqParam.getBuildingTansyukuName());

                    // 都道府県
                    if (reqParam.getPrefectureCd() != null) {
                        MPrefecture mPrefecture = new MPrefecture();
                        mPrefecture.setPrefectureCd(reqParam.getPrefectureCd());
                        tBuidlingEntity.setMPrefecture(find(mPrefectureServiceDaoImpl, mPrefecture));
                    }
                    tBuidlingEntity.setZipCd(reqParam.getZipCd());
                    if (reqParam.getAddress() != null) {
                        tBuidlingEntity.setAddress(reqParam.getAddress());
                    }
                    tBuidlingEntity.setAddressBuilding(reqParam.getAddressBuilding());
                    tBuidlingEntity.setTelNo(reqParam.getTelNo());
                    tBuidlingEntity.setFaxNo(reqParam.getFaxNo());
                    tBuidlingEntity.setBiko(reqParam.getBiko());

                    if (reqParam.getTotalStartYm() != null) {
                        tBuidlingEntity.setTotalStartYm(DateUtility.conversionDate(reqParam.getTotalStartYm(),
                                DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                    } else {
                     // 集計終了年月が未入力だった場合のみnull更新
                        tBuidlingEntity.setTotalStartYm(null);
                    }
                    if (reqParam.getTotalEndYm() != null) {
                        tBuidlingEntity.setTotalEndYm(DateUtility.conversionDate(reqParam.getTotalEndYm(),
                                DateUtility.DATE_FORMAT_YYYYMM_SLASH));
                    } else {
                        // 集計終了年月が未入力だった場合のみnull更新
                        tBuidlingEntity.setTotalEndYm(null);
                    }

                    // 更新 固定項目
                    tBuidlingEntity.setUpdateDate(svDate);
                    tBuidlingEntity.setUpdateUserId(userId);

                    // 公開フラグ
                    if (Objects.equals(reqParam.getPublicFlg(), OsolConstants.FLG_ON)) {
                        tBuidlingEntity.setPublicFlg(OsolConstants.FLG_ON);
                    } else {
                        tBuidlingEntity.setPublicFlg(OsolConstants.FLG_OFF);
                    }

                    // 建物更新
                    merge(tBuildingApiServiceDaoImpl, tBuidlingEntity);

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
                }
            }
        }
        return result;
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
