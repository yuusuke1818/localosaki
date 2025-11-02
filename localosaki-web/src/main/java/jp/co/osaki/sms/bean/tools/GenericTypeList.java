package jp.co.osaki.sms.bean.tools;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.entity.MGenericType;
import jp.co.osaki.sms.dao.MGenericTypeDao;

/**
 *
 * 汎用区分マスタ
 *
 * @author take_suzuki
 */
@Named(value = "genericTypeListBean")
@ApplicationScoped
public class GenericTypeList extends OsolConstants implements Serializable {

    private static final long serialVersionUID = -2726463145582395876L;

//    @Inject
//    private OsolConfigs osolConfigs;
//
//    // メッセージクラス
//    @Inject
//    private OsolMessages beanMessages;

    /**
     * 汎用区分マスタDao
     */
    @EJB
    private MGenericTypeDao mGenericTypeDao;

    private LinkedHashMap<String, String> markCode;                     //001
    private LinkedHashMap<String, String> targetCode;                   //002
    private LinkedHashMap<String, String> loginPermitStatus;            //003
    private LinkedHashMap<String, String> corpType;                     //004
    //005～007は欠番
    private LinkedHashMap<String, String> authorityType;                //008
    private LinkedHashMap<String, String> personType;                   //009
    private LinkedHashMap<String, String> nyukyoTypeCd;                 //010
    private LinkedHashMap<String, String> engManageFactoryType;         //011
    private LinkedHashMap<String, String> ownerShipCode;                //012
    private LinkedHashMap<String, String> repeatInterval;               //013
    private LinkedHashMap<String, String> requestStatus;                //014
    private LinkedHashMap<String, String> requestType;                  //015
    private LinkedHashMap<String, String> contactUs;                    //016
    private LinkedHashMap<String, String> dayAndNightType;              //017
    private LinkedHashMap<String, String> engSupplyType;                //018
    private LinkedHashMap<String, String> processStatus;                //019
    private LinkedHashMap<String, String> processResult;                //020
    private LinkedHashMap<String, String> buildingSituation;            //021
    private LinkedHashMap<String, String> facilityState;                //022
    private LinkedHashMap<String, String> fileType;                     //023
    private LinkedHashMap<String, String> refrigerantType;              //024
    private LinkedHashMap<String, String> unitDividePowerRoot;          //025
    private LinkedHashMap<String, String> estimateUse;                  //026
    private LinkedHashMap<String, String> useExtension;                 //027
    private LinkedHashMap<String, String> planFulfillmentTarget;        //028
    private LinkedHashMap<String, String> planFulfillmentDateType;      //029
    private LinkedHashMap<String, String> maintenanceUseSetting;        //030
    private LinkedHashMap<String, String> lineTarget;                   //031
    private LinkedHashMap<String, String> lineGroupType;                //032
    private LinkedHashMap<String, String> graphElementType;             //033
    private LinkedHashMap<String, String> pointCalcType;                //034
    private LinkedHashMap<String, String> pointType;                    //035
    private LinkedHashMap<String, String> signageContentsType;          //036
    private LinkedHashMap<String, String> weatherTimeSlot;              //037
    private LinkedHashMap<String, String> weatherState;                 //038
    private LinkedHashMap<String, String> summaryUnit;                  //039
    private LinkedHashMap<String, String> batchRunCycle;                //040
    private LinkedHashMap<String, String> buildingType;                 //041
    private LinkedHashMap<String, String> airVerifyType;                //042
    private LinkedHashMap<String, String> temperatureSensorConnect;     //043
    private LinkedHashMap<String, String> temperatureControlCondition;  //044
    private LinkedHashMap<String, String> devCtrlCommonEnabled;         //045
    private LinkedHashMap<String, String> devCtrlCommonPermission;      //046
    private LinkedHashMap<String, String> coolWarmSwitchPort;           //047
    private LinkedHashMap<String, String> coolWarmTermKind;             //048
    private LinkedHashMap<String, String> temperatureSensorKind;        //049
    private LinkedHashMap<String, String> devCtrlCommonOnOff;           //050
    private LinkedHashMap<String, String> dutyCtrlTimes;                //051
    private LinkedHashMap<String, String> timelyMethod;                 //052
    private LinkedHashMap<String, String> timeSynchroMethod;            //053
    private LinkedHashMap<String, String> stopPowerTime;                //054
    private LinkedHashMap<String, String> cutMethodFv2;                 //055
    private LinkedHashMap<String, String> cutMethodFvpD;                //056
    private LinkedHashMap<String, String> cutMethodFvpaD;               //057
    private LinkedHashMap<String, String> cutMethodFvpaG2;              //058
    private LinkedHashMap<String, String> cutSettingMethod;             //059
    private LinkedHashMap<String, String> targetValueChange;            //060
    private LinkedHashMap<String, String> handControlStatus;            //061
    private LinkedHashMap<String, String> loadControlStatus;            //062
    private LinkedHashMap<String, String> warnControlStatus;            //063
    private LinkedHashMap<String, String> outputTermControlInfo;        //064
    private LinkedHashMap<String, String> termKindFv2;                  //065
    private LinkedHashMap<String, String> termKindFvpaD;                //066
    private LinkedHashMap<String, String> termKindFvpaG2;               //067
    private LinkedHashMap<String, String> pointAddress;                 //068
    private LinkedHashMap<String, String> pointKind;                    //069
    private LinkedHashMap<String, String> sensorKind;                   //070
    private LinkedHashMap<String, String> transmissionMode;             //071
    private LinkedHashMap<String, String> transmissionSpeed;            //072
    private LinkedHashMap<String, String> outputFunctionSelect;         //073
    private LinkedHashMap<String, String> contorolCondition;            //074
    private LinkedHashMap<String, String> eventCondition;               //075
    private LinkedHashMap<String, String> cutStatusDirection;           //076
    private LinkedHashMap<String, String> demandOperation;              //077
    private LinkedHashMap<String, String> demandWarnFlg;                //078
    private LinkedHashMap<String, String> errorFlg;                     //079
    private LinkedHashMap<String, String> coolWarmSwitch;               //080
    private LinkedHashMap<String, String> measurePointStatus;           //081
    private LinkedHashMap<String, String> batteryWarning;               //082
    private LinkedHashMap<String, String> antennaStrength;              //083
    private LinkedHashMap<String, String> productsIdentificationInfo;   //084
    private LinkedHashMap<String, String> timeSynchroStatus;            //085
    private LinkedHashMap<String, String> palseInputMonitor;            //086
    private LinkedHashMap<String, String> occurrenceRestoreFlg;         //087
    private LinkedHashMap<String, String> marginExcess;                 //088
    private LinkedHashMap<String, String> loadOnOff;                    //089
    private LinkedHashMap<String, String> equipmentErrorInfo;           //090
    private LinkedHashMap<String, String> destinationKbn;               //091
    private LinkedHashMap<String, String> demandAlertLv;                //092
    private LinkedHashMap<String, String> termKindFvpD;                 //093
    private LinkedHashMap<String, String> targetValueChangeEa;          //094
    private LinkedHashMap<String, String> targetValueChangeEa2;         //095
    private LinkedHashMap<String, String> termKindEa;                   //096
    private LinkedHashMap<String, String> termKindEa2;                  //097
    private LinkedHashMap<String, String> pointKindEa;                  //098
    private LinkedHashMap<String, String> contorolConditionEa;          //099
    private LinkedHashMap<String, String> eventConditionEa;             //100
    private LinkedHashMap<String, String> productsIdentificationInfoEa; //101
    private LinkedHashMap<String, String> productsIdentificationInfoEa2;//102
    private LinkedHashMap<String, String> rs485AbnormalStatusEa;        //103
    private LinkedHashMap<String, String> equipmentErrorInfoEa;         //104
    private LinkedHashMap<String, String> equipmentErrorInfo2Ea2;       //105
    private LinkedHashMap<String, String> eventControlEa;               //106
    private LinkedHashMap<String, String> eventTargetSelectEa;          //107
    private LinkedHashMap<String, String> illuminationControlChangeEa;  //108
    private LinkedHashMap<String, String> alarmStatusEa;                //109
    private LinkedHashMap<String, String> eventFactorEa;                //110
    private LinkedHashMap<String, String> illuminationModeEa;           //111
    private LinkedHashMap<String, String> scheduleInterLockingEa;       //112
    private LinkedHashMap<String, String> cutStatusDirectionEa;         //113
    private LinkedHashMap<String, String> weather;                      //114
    private LinkedHashMap<String, String> windDirection;                //115
    private LinkedHashMap<String, String> cloudCover;                   //116
    private LinkedHashMap<String, String> sensorConnectEquipmentEa2;    //117
    private LinkedHashMap<String, String> sensorKindEa2;                //118
    private LinkedHashMap<String, String> aielMasterActionModeEa2;      //119
    private LinkedHashMap<String, String> powerSavingPossibleEa2;       //120
    private LinkedHashMap<String, String> updateKbn;                    //137
    private LinkedHashMap<String, String> oshiraseDeliveryCd;           //138
    private LinkedHashMap<String, String> smsLoadLimit;                 //300
    private LinkedHashMap<String, String> smsLoadCurrent;               //301
    private LinkedHashMap<String, String> smsOpenMode;                  //302
    private LinkedHashMap<String, String> smsDispYear;                  //303
    private LinkedHashMap<String, String> smsGengo;                     //304
    private LinkedHashMap<String, String> smsExamNotic;                 //305
    private LinkedHashMap<String, String> smsDispDirect;                //306
    private LinkedHashMap<String, String> smsDispType;                  //307
    private LinkedHashMap<String, String> smsSaleTaxDeal;               //308
    private LinkedHashMap<String, String> smsDecimalFraction;           //309
    private LinkedHashMap<String, String> smsYearCloseMonth;            //310
    private LinkedHashMap<String, String> smsUnitCo2Coefficient;        //311
    private LinkedHashMap<String, String> smsElectricMenu;              //312
    private LinkedHashMap<String, String> smsMeterKind;                 //314
    private LinkedHashMap<String, String> smsDevStatus;                 //315
    private LinkedHashMap<String, String> smsDevSta;                    //316
    private LinkedHashMap<String, String> smsConcentSta;                //317
    private LinkedHashMap<String, String> smsMeterSta;                  //318
    private LinkedHashMap<String, String> smsTermSta;                   //319
    private LinkedHashMap<String, String> smsPulseType;                 //322
    private LinkedHashMap<String, String> smsInspType;                  //325
    private LinkedHashMap<String, String> smsHandyType;                 //326
    private LinkedHashMap<String, String> smsDispTimeUnit;              //327
    private LinkedHashMap<String, String> smsMeterPresSitu;             //328
    private LinkedHashMap<String, String> monthExtract;                 //329

    /**
     * APIから指定のグループコードの汎用区分マスタを取得し、Mapに詰める
     * @param groupCode
     * @return
     */
    private void initGroupMap(ApiGenericTypeConstants.GROUP_CODE groupCode,LinkedHashMap<String, String> groupMap){

        groupMap.clear();

        for (MGenericType genericType : mGenericTypeDao.getResultList(groupCode.getVal())) {
            groupMap.put(genericType.getKbnName(), genericType.getId().getKbnCode());
        }

//        GenericTypeListResponse listResponse = new GenericTypeListResponse();
//        GenericTypeListParameter listParameter = new GenericTypeListParameter();
//        OsolApiGateway listGateway = new OsolApiGateway();
//        listParameter.setBean("GenericTypeListBean");
//        listParameter.setGroupCode(groupCode.getVal());
//        listResponse = (GenericTypeListResponse) listGateway.osolApiPost(
//                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
//                OsolApiGateway.PATH.JSON, listParameter, listResponse);
//
//        if (!OsolApiResultCode.API_OK.equals(listResponse.getResultCode())) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(listResponse.getResultCode())),
//                    null));
//            return;
//        }
//
//        if (listResponse.getResult() == null || listResponse.getResult().getDetailList() == null
//                || listResponse.getResult().getDetailList().isEmpty()) {
//            return;
//        } else {
//
//            for(GenericTypeListDetailResultData genericType : listResponse.getResult().getDetailList()) {
//                groupMap.put(genericType.getKbnName(), genericType.getKbnCode());
//            }
//            return;
//        }
    }

    private String getMapKey(LinkedHashMap<String, String> map, String value) {
        if (map != null && value != null) {
            if (!map.isEmpty()) {
                // コードから名前を取得
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(value)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return STR_EMPTY;
    }

    /**
     * コンストラクタ
     *
     * 各種リストに汎用区分を設定する。
     */
    @PostConstruct
    public void init() {

        //各区分用のMapを初期化しておく
        markCode = new LinkedHashMap<>();
        targetCode = new LinkedHashMap<>();
        loginPermitStatus = new LinkedHashMap<>();
        corpType = new LinkedHashMap<>();
        authorityType = new LinkedHashMap<>();
        personType = new LinkedHashMap<>();
        nyukyoTypeCd = new LinkedHashMap<>();
        engManageFactoryType = new LinkedHashMap<>();
        ownerShipCode = new LinkedHashMap<>();
        repeatInterval = new LinkedHashMap<>();
        requestStatus = new LinkedHashMap<>();
        requestType = new LinkedHashMap<>();
        contactUs = new LinkedHashMap<>();
        dayAndNightType = new LinkedHashMap<>();
        engSupplyType = new LinkedHashMap<>();
        processStatus = new LinkedHashMap<>();
        processResult = new LinkedHashMap<>();
        buildingSituation = new LinkedHashMap<>();
        facilityState = new LinkedHashMap<>();
        fileType = new LinkedHashMap<>();
        refrigerantType = new LinkedHashMap<>();
        unitDividePowerRoot = new LinkedHashMap<>();
        estimateUse = new LinkedHashMap<>();
        useExtension = new LinkedHashMap<>();
        planFulfillmentTarget = new LinkedHashMap<>();
        planFulfillmentDateType = new LinkedHashMap<>();
        maintenanceUseSetting = new LinkedHashMap<>();
        lineTarget = new LinkedHashMap<>();
        lineGroupType = new LinkedHashMap<>();
        graphElementType = new LinkedHashMap<>();
        pointCalcType = new LinkedHashMap<>();
        pointType = new LinkedHashMap<>();
        signageContentsType = new LinkedHashMap<>();
        weatherTimeSlot = new LinkedHashMap<>();
        weatherState = new LinkedHashMap<>();
        summaryUnit = new LinkedHashMap<>();
        batchRunCycle = new LinkedHashMap<>();
        buildingType = new LinkedHashMap<>();
        airVerifyType = new LinkedHashMap<>();
        temperatureSensorConnect = new LinkedHashMap<>();
        temperatureControlCondition = new LinkedHashMap<>();
        devCtrlCommonEnabled = new LinkedHashMap<>();
        devCtrlCommonPermission = new LinkedHashMap<>();
        coolWarmSwitchPort = new LinkedHashMap<>();
        coolWarmTermKind = new LinkedHashMap<>();
        temperatureSensorKind = new LinkedHashMap<>();
        devCtrlCommonOnOff = new LinkedHashMap<>();
        dutyCtrlTimes = new LinkedHashMap<>();
        timelyMethod = new LinkedHashMap<>();
        timeSynchroMethod = new LinkedHashMap<>();
        stopPowerTime = new LinkedHashMap<>();
        cutMethodFv2 = new LinkedHashMap<>();
        cutMethodFvpD = new LinkedHashMap<>();
        cutMethodFvpaD = new LinkedHashMap<>();
        cutMethodFvpaG2 = new LinkedHashMap<>();
        cutSettingMethod = new LinkedHashMap<>();
        targetValueChange = new LinkedHashMap<>();
        handControlStatus = new LinkedHashMap<>();
        loadControlStatus = new LinkedHashMap<>();
        warnControlStatus = new LinkedHashMap<>();
        outputTermControlInfo = new LinkedHashMap<>();
        termKindFv2 = new LinkedHashMap<>();
        termKindFvpaD = new LinkedHashMap<>();
        termKindFvpaG2 = new LinkedHashMap<>();
        pointAddress = new LinkedHashMap<>();
        pointKind = new LinkedHashMap<>();
        sensorKind = new LinkedHashMap<>();
        transmissionMode = new LinkedHashMap<>();
        transmissionSpeed = new LinkedHashMap<>();
        outputFunctionSelect = new LinkedHashMap<>();
        contorolCondition = new LinkedHashMap<>();
        eventCondition = new LinkedHashMap<>();
        cutStatusDirection = new LinkedHashMap<>();
        demandOperation = new LinkedHashMap<>();
        demandWarnFlg = new LinkedHashMap<>();
        errorFlg = new LinkedHashMap<>();
        coolWarmSwitch = new LinkedHashMap<>();
        measurePointStatus = new LinkedHashMap<>();
        batteryWarning = new LinkedHashMap<>();
        antennaStrength = new LinkedHashMap<>();
        productsIdentificationInfo = new LinkedHashMap<>();
        timeSynchroStatus = new LinkedHashMap<>();
        palseInputMonitor = new LinkedHashMap<>();
        occurrenceRestoreFlg = new LinkedHashMap<>();
        marginExcess = new LinkedHashMap<>();
        loadOnOff = new LinkedHashMap<>();
        equipmentErrorInfo = new LinkedHashMap<>();
        destinationKbn = new LinkedHashMap<>();
        demandAlertLv = new LinkedHashMap<>();
        termKindFvpD = new LinkedHashMap<>();
        targetValueChangeEa = new LinkedHashMap<>();
        targetValueChangeEa2 = new LinkedHashMap<>();
        termKindEa = new LinkedHashMap<>();
        termKindEa2 = new LinkedHashMap<>();
        pointKindEa = new LinkedHashMap<>();
        contorolConditionEa = new LinkedHashMap<>();
        eventConditionEa = new LinkedHashMap<>();
        productsIdentificationInfoEa = new LinkedHashMap<>();
        productsIdentificationInfoEa2 = new LinkedHashMap<>();
        rs485AbnormalStatusEa = new LinkedHashMap<>();
        equipmentErrorInfoEa = new LinkedHashMap<>();
        equipmentErrorInfo2Ea2 = new LinkedHashMap<>();
        eventControlEa = new LinkedHashMap<>();
        eventTargetSelectEa = new LinkedHashMap<>();
        illuminationControlChangeEa = new LinkedHashMap<>();
        alarmStatusEa = new LinkedHashMap<>();
        eventFactorEa = new LinkedHashMap<>();
        illuminationModeEa = new LinkedHashMap<>();
        scheduleInterLockingEa = new LinkedHashMap<>();
        cutStatusDirectionEa = new LinkedHashMap<>();
        weather = new LinkedHashMap<>();
        windDirection = new LinkedHashMap<>();
        cloudCover = new LinkedHashMap<>();
        sensorConnectEquipmentEa2 = new LinkedHashMap<>();
        sensorKindEa2 = new LinkedHashMap<>();
        aielMasterActionModeEa2 = new LinkedHashMap<>();
        powerSavingPossibleEa2 = new LinkedHashMap<>();
        updateKbn = new LinkedHashMap<>();
        oshiraseDeliveryCd = new LinkedHashMap<>();
        smsSaleTaxDeal = new LinkedHashMap<>();
        smsDecimalFraction = new LinkedHashMap<>();
        smsYearCloseMonth = new LinkedHashMap<>();
        smsUnitCo2Coefficient = new LinkedHashMap<>();
        smsElectricMenu = new LinkedHashMap<>();
        smsLoadLimit = new LinkedHashMap<>();
        smsLoadCurrent = new LinkedHashMap<>();
        smsOpenMode = new LinkedHashMap<>();
        smsDispYear = new LinkedHashMap<>();
        smsGengo = new LinkedHashMap<>();
        smsExamNotic = new LinkedHashMap<>();
        smsDispDirect = new LinkedHashMap<>();
        smsDispType = new LinkedHashMap<>();
        smsMeterKind = new LinkedHashMap<>();
        smsDevStatus = new LinkedHashMap<>();
        smsDevSta = new LinkedHashMap<>();
        smsConcentSta = new LinkedHashMap<>();
        smsMeterSta = new LinkedHashMap<>();
        smsTermSta = new LinkedHashMap<>();
        smsPulseType = new LinkedHashMap<>();
        smsInspType = new LinkedHashMap<>();
        smsHandyType = new LinkedHashMap<>();
        smsDispTimeUnit = new LinkedHashMap<>();
        smsMeterPresSitu = new LinkedHashMap<>();
        monthExtract = new LinkedHashMap<>();
    }

    /**
     * Mapが空の場合、指定のグループコードからマップを取得する。空でない場合は、元のMapを返却する
     * @param groupCode
     * @param linkedHashMap
     * @return
     */
    private LinkedHashMap<String, String> getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE groupCode,LinkedHashMap<String, String> linkedHashMap){

        if(linkedHashMap.isEmpty()) {
            initGroupMap(groupCode,linkedHashMap);
        }

        return linkedHashMap;
    }

    /**
     * @return マークコード :001
     */
    public LinkedHashMap<String, String> getMarkCode() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MARK_CODE, markCode);
    }

    /**
     * @param markCode マークコード:001
     */
    public void setMarkCode(LinkedHashMap<String, String> markCode) {
        this.markCode = markCode;
    }

    /**
     * @return ターゲットコード:002
     */
    public LinkedHashMap<String, String> getTargetCode() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TARGET_CODE, targetCode);
    }

    /**
     * @param targetCode ターゲットコード:002
     */
    public void setTargetCode(LinkedHashMap<String, String> targetCode) {
        this.targetCode = targetCode;
    }

    /**
     * @return ログイン許可ステータス:003
     */
    public LinkedHashMap<String, String> getLoginPermitStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.LOGIN_PERMIT_INFO, loginPermitStatus);
    }

    /**
     * @param loginPermitStatus ログイン許可ステータス:003
     */
    public void setLoginPermitStatus(LinkedHashMap<String, String> loginPermitStatus) {
        this.loginPermitStatus = loginPermitStatus;
    }

    /**
     * @return 企業種別:004
     */
    public LinkedHashMap<String, String> getCorpType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.CORP_TYPE, corpType);
    }

    /**
     * @param corpType 企業種別:004
     */
    public void setCorpType(LinkedHashMap<String, String> corpType) {
        this.corpType = corpType;
    }

    /**
     *
     * 企業種別名称取得:004
     *
     * @param value 区分コード
     * @return 企業種別 名称
     */
    public String getCorpTypeByName(String value) {
        return getMapKey(getCorpType(), value);
    }

    /**
     * @return 権限種別:008
     */
    public LinkedHashMap<String, String> getAuthorityType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AUTHORITY_TYPE, authorityType);
    }

    /**
     * @param authorityType 権限種別:008
     */
    public void setAuthorityType(LinkedHashMap<String, String> authorityType) {
        this.authorityType = authorityType;
    }

    /**
     * @return 担当種別:009
     */
    public LinkedHashMap<String, String> getPersonType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PERSON_TYPE, personType);
    }

    /**
     * @param personType 担当種別:009
     */
    public void setPersonType(LinkedHashMap<String, String> personType) {
        this.personType = personType;
    }

    /**
     * @return 入居形態コード:010
     */
    public LinkedHashMap<String, String> getNyukyoTypeCd() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.NYUKYO_TYPE, nyukyoTypeCd);
    }

    /**
     * @param nyukyoTypeCd 入居形態コード:010
     */
    public void setNyukyoTypeCd(LinkedHashMap<String, String> nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    /**
     * 入居形態名称取得:010
     *
     * @param value 区分コード
     * @return 入居形態 名称
     */
    public String getNyukyoTypeCdByName(String value) {
        return getMapKey(getNyukyoTypeCd(), value);
    }

    /**
     * @return エネルギー管理指定工場種別:011
     */
    public LinkedHashMap<String, String> getEngManageFactoryType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ENG_MANAGE_FACTORY_TYPE, engManageFactoryType);
    }

    /**
     * @param engManageFactoryType エネルギー管理指定工場種別:011
     */
    public void setEngManageFactoryType(LinkedHashMap<String, String> engManageFactoryType) {
        this.engManageFactoryType = engManageFactoryType;
    }

    /**
     * @return 所有形態コード:012
     */
    public LinkedHashMap<String, String> getOwnerShipCode() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.OWNER_SHIP_CODE, ownerShipCode);
    }

    /**
     * @param ownerShipCode 所有形態コード:012
     */
    public void setOwnerShipCode(LinkedHashMap<String, String> ownerShipCode) {
        this.ownerShipCode = ownerShipCode;
    }

    /**
     * @return 繰り返し間隔:013
     */
    public LinkedHashMap<String, String> getRepeatInterval() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.REPEAT_INTERVAL, repeatInterval);
    }

    /**
     * @param repeatInterval 繰り返し間隔:013
     */
    public void setRepeatInterval(LinkedHashMap<String, String> repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    /**
     * 繰り返し間隔名称取得:013
     *
     * @param value 区分コード
     * @return 繰り返し間隔 名称
     */
    public String getRepeatIntervalByName(String value) {
        return getMapKey(getRepeatInterval(), value);
    }

    /**
     * @return 依頼ステータス:014
     */
    public LinkedHashMap<String, String> getRequestStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.REQUEST_STATUS, requestStatus);
    }

    /**
     * @param requestStatus 依頼ステータス:014
     */
    public void setRequestStatus(LinkedHashMap<String, String> requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * 依頼ステータス名称取得:014
     *
     * @param value 区分コード
     * @return 依頼ステータス 名称
     */
    public String getRequestStatusByName(String value) {
        return getMapKey(getRequestStatus(), value);
    }

    /**
     * @return 依頼種別:015
     */
    public LinkedHashMap<String, String> getRequestType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.REQUEST_TYPE, requestType);
    }

    /**
     * @param requestType 依頼種別:015
     */
    public void setRequestType(LinkedHashMap<String, String> requestType) {
        this.requestType = requestType;
    }

    /**
     * 依頼種別 名称取得:015
     *
     * @param value 区分コード
     * @return 依頼種別 名称
     */
    public String getRequestTypeByName(String value) {
        return getMapKey(getRequestType(), value);
    }

    /**
     * @return 連絡方法:016
     */
    public LinkedHashMap<String, String> getContactUs() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.CONTACT_US, contactUs);
    }

    /**
     * @param contactUs 連絡方法:016
     */
    public void setContactUs(LinkedHashMap<String, String> contactUs) {
        this.contactUs = contactUs;
    }

    /**
     * @return 昼夜区分:017
     */
    public LinkedHashMap<String, String> getDayAndNightType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DAY_AND_NIGNT_TYPE, dayAndNightType);
    }

    /**
     * @param dayAndNightType 昼夜区分:017
     */
    public void setDayAndNightType(LinkedHashMap<String, String> dayAndNightType) {
        this.dayAndNightType = dayAndNightType;
    }

    /**
     * 昼夜区分 名称取得:017
     *
     * @param value 区分コード
     * @return 昼夜区分 名称
     */
    public String getDayAndNightTypeByName(String value) {
        return getMapKey(getDayAndNightType(), value);
    }

    /**
     * @return 供給区分:018
     */
    public LinkedHashMap<String, String> getEngSupplyType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ENG_SUPPLY_TYPE, engSupplyType);
    }

    /**
     * @param engSupplyType 供給区分:018
     */
    public void setEngSupplyType(LinkedHashMap<String, String> engSupplyType) {
        this.engSupplyType = engSupplyType;
    }

    /**
     *
     * 供給区分名称取得:018
     *
     * @param value 区分コード
     * @return 供給区分 名称
     */
    public String getEngSupplyTypeByName(String value) {
        return getMapKey(getEngSupplyType(), value);
    }

    /**
     * @return 処理ステータス:019
     */
    public LinkedHashMap<String, String> getProcessStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PROCESS_STATUS, processStatus);
    }

    /**
     * @param processStatus 処理ステータス:019
     */
    public void setProcessStatus(LinkedHashMap<String, String> processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * @return 処理結果:020
     */
    public LinkedHashMap<String, String> getProcessResult() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PROCESS_RESULT, processResult);
    }

    /**
     * @param processResult 処理結果:020
     */
    public void setProcessResult(LinkedHashMap<String, String> processResult) {
        this.processResult = processResult;
    }

    /**
     * @return 建物状況:021
     */
    public LinkedHashMap<String, String> getBuildingSituation() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.BUILDING_SITUATION, buildingSituation);
    }

    /**
     * @param buildingSituation 建物状況:021
     */
    public void setBuildingSituation(LinkedHashMap<String, String> buildingSituation) {
        this.buildingSituation = buildingSituation;
    }

    /**
     *
     * 建物状況名称取得:021
     *
     * @param value 区分コード
     * @return 建物状況 名称
     */
    public String getBuildingSituationByName(String value) {
        return getMapKey(getBuildingSituation(), value);
    }

    /**
     * @return 設備状態:022
     */
    public LinkedHashMap<String, String> getFacilityState() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.FACILITY_STATE, facilityState);
    }

    /**
     * @param facilityState 設備状態:022
     */
    public void setFacilityState(LinkedHashMap<String, String> facilityState) {
        this.facilityState = facilityState;
    }

    /**
     *
     * 設備状態名称取得:022
     *
     * @param value 区分コード
     * @return 設備状態 名称
     */
    public String getFacilityStateByName(String value) {
        return getMapKey(getFacilityState(), value);
    }

    /**
     * @return ファイル種別:023
     */
    public LinkedHashMap<String, String> getFileType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.FILE_TYPE, fileType);
    }

    /**
     * @param fileType ファイル種別:023
     */
    public void setFileType(LinkedHashMap<String, String> fileType) {
        this.fileType = fileType;
    }

    /**
     * @return 冷媒種別:024
     */
    public LinkedHashMap<String, String> getRefrigerantType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.REFRIGERANT_TYPE, refrigerantType);
    }

    /**
     * @param refrigerantType 冷媒種別:024
     */
    public void setRefrigerantType(LinkedHashMap<String, String> refrigerantType) {
        this.refrigerantType = refrigerantType;
    }

    /**
     * @return 原単位累乗根:025
     */
    public LinkedHashMap<String, String> getUnitDividePowerRoot() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.UNIT_DIVIDE_POWER_ROOT, unitDividePowerRoot);
    }

    /**
     * @param unitDividePowerRoot 原単位累乗根:025
     */
    public void setUnitDividePowerRoot(LinkedHashMap<String, String> unitDividePowerRoot) {
        this.unitDividePowerRoot = unitDividePowerRoot;
    }

    /**
     *
     * 原単位累乗根名称取得:025
     *
     * @param value 区分コード
     * @return 原単位累乗根 名称
     */
    public String getUnitDividePowerRootByName(String value) {
        return getMapKey(getUnitDividePowerRoot(), value);
    }

    /**
     * @return 推計使用:026
     */
    public LinkedHashMap<String, String> getEstimateUse() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ESTIMATE_USE, estimateUse);
    }

    /**
     * @param estimateUse 推計使用:026
     */
    public void setEstimateUse(LinkedHashMap<String, String> estimateUse) {
        this.estimateUse = estimateUse;
    }

    /**
     *
     * 推計使用 名称取得:026
     *
     * @param value 区分コード
     * @return 推計使用 名称
     */
    public String getEstimateUseByName(String value) {
        return getMapKey(getEstimateUse(), value);
    }

    /**
     * @return useExtension 使用可能拡張子:027
     */
    public LinkedHashMap<String, String> getUseExtension() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.USE_EXTENSION, useExtension);
    }

    /**
     * @param value 区分コード:027
     * @return 使用可能拡張子
     */
    public String getUseExtension(String value) {
        return getMapKey(getUseExtension(), value);
    }

    /**
     * @param useExtension 使用可能拡張子:027
     */
    public void setUseExtension(LinkedHashMap<String, String> useExtension) {
        this.useExtension = useExtension;
    }

    /**
     *
     * @return planFulfillmentTarget 計画履行対象:028
     */
    public LinkedHashMap<String, String> getPlanFulfillmentTarget() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PLAN_FULFILLMENT_TARGET, planFulfillmentTarget);
    }

    /**
     *
     * @param planFulfillmentTarget 計画履行対象:028
     */
    public void setPlanFulfillmentTarget(LinkedHashMap<String, String> planFulfillmentTarget) {
        this.planFulfillmentTarget = planFulfillmentTarget;
    }

    /**
     *
     * 計画履行対象 名称取得:028
     *
     * @param value 区分コード
     * @return 計画履行対象 名称
     */
    public String getPlanFulfillmentTargetByName(String value) {
        return getMapKey(getPlanFulfillmentTarget(), value);
    }

    /**
     *
     * @return planFulfillmentDateType 計画履行日種別:029
     */
    public LinkedHashMap<String, String> getPlanFulfillmentDateType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PLAN_FULFILLMENT_DATE_TYPE, planFulfillmentDateType);
    }

    /**
     *
     * @param planFulfillmentDateType 計画履行日種別:029
     */
    public void setPlanFulfillmentDateType(LinkedHashMap<String, String> planFulfillmentDateType) {
        this.planFulfillmentDateType = planFulfillmentDateType;
    }

    /**
     *
     * @return メンテナンス利用設定:030
     */
    public LinkedHashMap<String, String> getMaintenanceUseSetting() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MAINTENANCE_USE_SETTING, maintenanceUseSetting);
    }

    /**
     *
     * @param maintenanceUseSetting メンテナンス利用設定:030
     */
    public void setMaintenanceUseSetting(LinkedHashMap<String, String> maintenanceUseSetting) {
        this.maintenanceUseSetting = maintenanceUseSetting;
    }

    /**
     *
     * @return 計測目的:031
     */
    public LinkedHashMap<String, String> getLineTarget() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.LINE_TARGET, lineTarget);
    }

    /**
     *
     * @param lineTarget 計測目的:031
     */
    public void setLineTarget(LinkedHashMap<String, String> lineTarget) {
        this.lineTarget = lineTarget;
    }

    /**
     *
     * @return 系統グループ区分:032
     */
    public LinkedHashMap<String, String> getLineGroupType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.LINE_GROUP_TYPE, lineGroupType);
    }

    /**
     *
     * @param lineGroupType　系統グループ区分:032
     */
    public void setLineGroupType(LinkedHashMap<String, String> lineGroupType) {
        this.lineGroupType = lineGroupType;
    }

    /**
     *
     * @return グラフ要素種別:033
     */
    public LinkedHashMap<String, String> getGraphElementType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.GRAPH_ELEMENT_TYPE, graphElementType);
    }

    /**
     *
     * @param graphElementType グラフ要素種別:033
     */
    public void setGraphElementType(LinkedHashMap<String, String> graphElementType) {
        this.graphElementType = graphElementType;
    }

    /**
     *
     * @return ポイント算出区分:034
     */
    public LinkedHashMap<String, String> getPointCalcType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POINT_CALC_TYPE, pointCalcType);
    }

    /**
     *
     * @param pointCalcType ポイント算出区分:034
     */
    public void setPointCalcType(LinkedHashMap<String, String> pointCalcType) {
        this.pointCalcType = pointCalcType;
    }

    /**
     *
     * @return ポイント種別:035
     */
    public LinkedHashMap<String, String> getPointType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POINT_TYPE, pointType);
    }

    /**
     *
     * @param pointType ポイント種別:035
     */
    public void setPointType(LinkedHashMap<String, String> pointType) {
        this.pointType = pointType;
    }

    /**
     *
     * @return サイネージコンテンツ種別:036
     */
    public LinkedHashMap<String, String> getSignageContentsType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SIGNAGE_CONTENTS_TYPE, signageContentsType);
    }

    /**
     *
     * @param signageContentsType サイネージコンテンツ種別:036
     */
    public void setSignageContentsType(LinkedHashMap<String, String> signageContentsType) {
        this.signageContentsType = signageContentsType;
    }

    /**
     *
     * @return サイネージコンテンツ名:036
     */
    public String getSignageContentsNameByCode(String code) {
        return getMapKey(getSignageContentsType(), code);
    }

    /**
     *
     * @return 天気時間帯:037
     */
    public LinkedHashMap<String, String> getWeatherTimeSlot() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.WEATHER_TIME_SLOT, weatherTimeSlot);
    }

    /**
     *
     * @param weatherTimeSlot 天気時間帯:037
     */
    public void setWeatherTimeSlot(LinkedHashMap<String, String> weatherTimeSlot) {
        this.weatherTimeSlot = weatherTimeSlot;
    }

    /**
     *
     * @return 天気状態:038
     */
    public LinkedHashMap<String, String> getWeatherState() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.WEATHER_STATE, weatherState);
    }

    /**
     *
     * @param weatherState 天気状態:038
     */
    public void setWeatherState(LinkedHashMap<String, String> weatherState) {
        this.weatherState = weatherState;
    }

    /**
     *
     * @return 集計区分（集計単位）:039
     */
    public LinkedHashMap<String, String> getSummaryUnit() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SUMMARY_UNIT, summaryUnit);
    }

    /**
     *
     * @param summaryUnit 集計区分（集計単位）:039
     */
    public void setSummaryUnit(LinkedHashMap<String, String> summaryUnit) {
        this.summaryUnit = summaryUnit;
    }

    /**
     *
     * @return バッチ実行周期:040
     */
    public LinkedHashMap<String, String> getBatchRunCycle() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.BATCH_RUN_CYCLE, batchRunCycle);
    }

    /**
     *
     * @param batchRunCycle バッチ実行周期:040
     */
    public void setBatchRunCycle(LinkedHashMap<String, String> batchRunCycle) {
        this.batchRunCycle = batchRunCycle;
    }

    /**
     * @return 建物種別:041
     */
    public LinkedHashMap<String, String> getBuildingType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.BUILDING_TYPE, buildingType);
    }

    /**
     * @return 建物種別名称:041
     */
    public String getBuildingTypeByName(String value) {
        return getMapKey(getBuildingType(), value);
    }

    /**
     * @param buildingType 建物種別:041
     */
    public void setBuildingType(LinkedHashMap<String, String> buildingType) {
        this.buildingType = buildingType;
    }


    /**
     *
     * @return 空調検証種別：042
     */
    public LinkedHashMap<String, String> getAirVerifyType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AIR_VERIFY_TYPE, airVerifyType);
    }

    /**
     *
     * @param airVerifyType 空調検証種別:042
     */
    public void setAirVerifyType(LinkedHashMap<String, String> airVerifyType) {
        this.airVerifyType = airVerifyType;
    }

    /**
     * 空調検証種別名称取得：042
     * @param value
     * @return
     */
    public String getAirVerifyTypeByName(String value) {
        return getMapKey(getAirVerifyType(), value);
    }

    /**
     * 温度センサ接続:043
     * @return
     */
    public LinkedHashMap<String, String> getTemperatureSensorConnect() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TEMPERATURE_SENSOR_CONNECT_043, temperatureSensorConnect);
    }

    /**
     * 温度センサ接続:043
     * @return
     */
    public String getTemperatureSensorConnectByName(String value) {
        return getMapKey(getTemperatureSensorConnect(), value);
    }

    /**
     * 温度センサ接続:043
     * @param temperatureSensorConnect
     */
    public void setTemperatureSensorConnect(LinkedHashMap<String, String> temperatureSensorConnect) {
        this.temperatureSensorConnect = temperatureSensorConnect;
    }

    /**
     * 温湿度制御条件:044
     * @return
     */

    public LinkedHashMap<String, String> getTemperatureControlCondition() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TEMP_HUMID_CONTROL_CONDITION_044, temperatureControlCondition);
    }

    /**
     * 温湿度制御条件:044
     * @return
     */
    public String getTemperatureControlConditionByName(String value) {
        return getMapKey(getTemperatureControlCondition(), value);
    }

    /**
     * 温湿度制御条件:044
     * @param temperatureControlCondition
     */

    public void setTemperatureControlCondition(LinkedHashMap<String, String> temperatureControlCondition) {
        this.temperatureControlCondition = temperatureControlCondition;
    }

    /**
     * 機器制御共通・有効無効:045
     * @return
     */

    public LinkedHashMap<String, String> getDevCtrlCommonEnabled() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SM_CONTROL_COMMON_VALID, devCtrlCommonEnabled);
    }

    /**
     * 機器制御共通・有効無効:045
     * @return
     */
    public String getDevCtrlCommonEnabledByName(String value) {
        return getMapKey(getDevCtrlCommonEnabled(), value);
    }

    /**
     * 機器制御共通・有効無効:045
     * @param devCtrlCommonEnabled
     */

    public void setDevCtrlCommonEnabled(LinkedHashMap<String, String> devCtrlCommonEnabled) {
        this.devCtrlCommonEnabled = devCtrlCommonEnabled;
    }

    /**
     * 機器制御共通・許可:046
     * @return
     */

    public LinkedHashMap<String, String> getDevCtrlCommonPermission() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SM_CONTROL_COMMON_PERMISSION, devCtrlCommonPermission);
    }

    /**
     * 機器制御共通・許可:046
     * @return
     */
    public String getDevCtrlCommonPermissionByName(String value) {
        return getMapKey(getDevCtrlCommonPermission(), value);
    }

    /**
     * 機器制御共通・許可:046
     * @param devCtrlCommonPermission
     */

    public void setDevCtrlCommonPermission(LinkedHashMap<String, String> devCtrlCommonPermission) {
        this.devCtrlCommonPermission = devCtrlCommonPermission;
    }

    /**
     * 冷暖房切替SW選択ポート:047
     * @return
     */

    public LinkedHashMap<String, String> getCoolWarmSwitchPort() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AIR_SWITCH_PORT_047, coolWarmSwitchPort);
    }

    /**
     * 冷暖房切替SW選択ポート:047
     * @return
     */
    public String getCoolWarmSwitchPortByName(String value) {
        return getMapKey(getCoolWarmSwitchPort(), value);
    }

    /**
     * 冷暖房切替SW選択ポート:047
     * @param coolWarmSwitchPort
     */

    public void setCoolWarmSwitchPort(LinkedHashMap<String, String> coolWarmSwitchPort) {
        this.coolWarmSwitchPort = coolWarmSwitchPort;
    }

    /**
     * 冷暖房切替用端末アドレス・端末種別:048
     * @return
     */

    public LinkedHashMap<String, String> getCoolWarmTermKind() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AIR_SWITCH_TERMINAL_ADDRESS_TYPE_048, coolWarmTermKind);
    }

    /**
     * 冷暖房切替用端末アドレス・端末種別:048
     * @return
     */
    public String getCoolWarmTermKindByName(String value) {
        return getMapKey(getCoolWarmTermKind(), value);
    }

    /**
     * 冷暖房切替用端末アドレス・端末種別:048
     * @param coolWarmTermKind
     */

    public void setCoolWarmTermKind(LinkedHashMap<String, String> coolWarmTermKind) {
        this.coolWarmTermKind = coolWarmTermKind;
    }

    /**
     * 温湿度センサ・種別:049
     * @return
     */

    public LinkedHashMap<String, String> getTemperatureSensorKind() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TEMP_HUMID_SENSOR_TYPE_049, temperatureSensorKind);
    }

    /**
     * 温湿度センサ・種別:049
     * @return
     */
    public String getTemperatureSensorKindByName(String value) {
        return getMapKey(getTemperatureSensorKind(), value);
    }

    /**
     * 温湿度センサ・種別:049
     * @param temperatureSensorKind
     */

    public void setTemperatureSensorKind(LinkedHashMap<String, String> temperatureSensorKind) {
        this.temperatureSensorKind = temperatureSensorKind;
    }

    /**
     * 機器制御共通・有り無し:050
     * @return
     */

    public LinkedHashMap<String, String> getDevCtrlCommonOnOff() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SM_CONTROL_COMMON_EXIST, devCtrlCommonOnOff);
    }

    /**
     * 機器制御共通・有り無し:050
     * @return
     */
    public String getDevCtrlCommonOnOffByName(String value) {
        return getMapKey(getDevCtrlCommonOnOff(), value);
    }

    /**
     * 機器制御共通・有り無し:050
     * @param devCtrlCommonOnOff
     */

    public void setDevCtrlCommonOnOff(LinkedHashMap<String, String> devCtrlCommonOnOff) {
        this.devCtrlCommonOnOff = devCtrlCommonOnOff;
    }

    /**
     * 間欠制御時間帯:051
     * @return
     */

    public LinkedHashMap<String, String> getDutyCtrlTimes() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DUTY_TIME_AREA_051, dutyCtrlTimes);
    }

    /**
     * 間欠制御時間帯:051
     * @return
     */
    public String getDutyCtrlTimesByName(String value) {
        return getMapKey(getDutyCtrlTimes(), value);
    }

    /**
     * 間欠制御時間帯:051
     * @param dutyCtrlTimes
     */

    public void setDutyCtrlTimes(LinkedHashMap<String, String> dutyCtrlTimes) {
        this.dutyCtrlTimes = dutyCtrlTimes;
    }

    /**
     * 時限方式:052
     * @return
     */

    public LinkedHashMap<String, String> getTimelyMethod() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TIME_LIMIT_METHOD_052, timelyMethod);
    }

    /**
     * 時限方式:052
     * @return
     */
    public String getTimelyMethodByName(String value) {
        return getMapKey(getTimelyMethod(), value);
    }

    /**
     * 時限方式:052
     * @param timelyMethod
     */

    public void setTimelyMethod(LinkedHashMap<String, String> timelyMethod) {
        this.timelyMethod = timelyMethod;
    }

    /**
     * 時計同期方式:053
     * @return
     */

    public LinkedHashMap<String, String> getTimeSynchroMethod() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TIME_SYNC_METHOD_053, timeSynchroMethod);
    }

    /**
     * 時計同期方式:053
     * @return
     */
    public String getTimeSynchroMethodByName(String value) {
        return getMapKey(getTimeSynchroMethod(), value);
    }

    /**
     * 時計同期方式:053
     * @param timeSynchroMethod
     */

    public void setTimeSynchroMethod(LinkedHashMap<String, String> timeSynchroMethod) {
        this.timeSynchroMethod = timeSynchroMethod;
    }

    /**
     * 停電時時限:054
     * @return
     */

    public LinkedHashMap<String, String> getStopPowerTime() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.BLACKOUT_TIME_LIMIT_054, stopPowerTime);
    }

    /**
     * 停電時時限:054
     * @return
     */
    public String getStopPowerTimeByName(String value) {
        return getMapKey(getStopPowerTime(), value);
    }

    /**
     * 停電時時限:054
     * @param stopPowerTime
     */

    public void setStopPowerTime(LinkedHashMap<String, String> stopPowerTime) {
        this.stopPowerTime = stopPowerTime;
    }

    /**
     * 遮断方式【FV2】:055
     * @return
     */

    public LinkedHashMap<String, String> getCutMethodFv2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_METHOD_055, cutMethodFv2);
    }

    /**
     * 遮断方式【FV2】:055
     * @return
     */
    public String getCutMethodFv2ByName(String value) {
        return getMapKey(getCutMethodFv2(), value);
    }

    /**
     * 遮断方式【FV2】:055
     * @param cutMethodFv2
     */

    public void setCutMethodFv2(LinkedHashMap<String, String> cutMethodFv2) {
        this.cutMethodFv2 = cutMethodFv2;
    }

    /**
     * 遮断方式【FVP(D)】:056
     * @return
     */

    public LinkedHashMap<String, String> getCutMethodFvpD() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_METHOD_056, cutMethodFvpD);
    }

    /**
     * 遮断方式【FVP(D)】:056
     * @return
     */
    public String getCutMethodFvpDByName(String value) {
        return getMapKey(getCutMethodFvpD(), value);
    }

    /**
     * 遮断方式【FVP(D)】:056
     * @param cutMethodFvpD
     */

    public void setCutMethodFvpD(LinkedHashMap<String, String> cutMethodFvpD) {
        this.cutMethodFvpD = cutMethodFvpD;
    }

    /**
     * 遮断方式【FVPα(D)】:057
     * @return
     */

    public LinkedHashMap<String, String> getCutMethodFvpaD() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_METHOD_057, cutMethodFvpaD);
    }

    /**
     * 遮断方式【FVPα(D)】:057
     * @return
     */
    public String getCutMethodFvpaDByName(String value) {
        return getMapKey(getCutMethodFvpaD(), value);
    }

    /**
     * 遮断方式【FVPα(D)】:057
     * @param cutMethodFvpaD
     */

    public void setCutMethodFvpaD(LinkedHashMap<String, String> cutMethodFvpaD) {
        this.cutMethodFvpaD = cutMethodFvpaD;
    }

    /**
     * 遮断方式【FVPα(G2)】:058
     * @return
     */

    public LinkedHashMap<String, String> getCutMethodFvpaG2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_METHOD_058, cutMethodFvpaG2);
    }

    /**
     * 遮断方式【FVPα(G2)】:058
     * @return
     */
    public String getCutMethodFvpaG2ByName(String value) {
        return getMapKey(getCutMethodFvpaG2(), value);
    }

    /**
     * 遮断方式【FVPα(G2)】:058
     * @param cutMethodFvpaG2
     */

    public void setCutMethodFvpaG2(LinkedHashMap<String, String> cutMethodFvpaG2) {
        this.cutMethodFvpaG2 = cutMethodFvpaG2;
    }

    /**
     * 遮断設定・方法:059
     * @return
     */

    public LinkedHashMap<String, String> getCutSettingMethod() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_SETTING_METHOD_059, cutSettingMethod);
    }

    /**
     * 遮断設定・方法:059
     * @return
     */
    public String getCutSettingMethodByName(String value) {
        return getMapKey(getCutSettingMethod(), value);
    }

    /**
     * 遮断設定・方法:059
     * @param cutSettingMethod
     */

    public void setCutSettingMethod(LinkedHashMap<String, String> cutSettingMethod) {
        this.cutSettingMethod = cutSettingMethod;
    }

    /**
     * 目標値切替（目標方式）:060
     * @return
     */

    public LinkedHashMap<String, String> getTargetValueChange() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TARGET_VALUE_CHANGE_060, targetValueChange);
    }

    /**
     * 目標値切替（目標方式）:060
     * @return
     */
    public String getTargetValueChangeByName(String value) {
        return getMapKey(getTargetValueChange(), value);
    }

    /**
     * 目標値切替（目標方式）:060
     * @param targetValueChange
     */

    public void setTargetValueChange(LinkedHashMap<String, String> targetValueChange) {
        this.targetValueChange = targetValueChange;
    }

    /**
     * 手動制御状態:061
     * @return
     */

    public LinkedHashMap<String, String> getHandControlStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MANUAL_CONTROL_STATUS_061, handControlStatus);
    }

    /**
     * 手動制御状態:061
     * @return
     */
    public String getHandControlStatusByName(String value) {
        return getMapKey(getHandControlStatus(), value);
    }

    /**
     * 手動制御状態:061
     * @param handControlStatus
     */

    public void setHandControlStatus(LinkedHashMap<String, String> handControlStatus) {
        this.handControlStatus = handControlStatus;
    }

    /**
     * 負荷の手動制御状態:062
     * @return
     */

    public LinkedHashMap<String, String> getLoadControlStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.LOAD_MANUAL_CONTROL_STATUS_062, loadControlStatus);
    }

    /**
     * 負荷の手動制御状態:062
     * @return
     */
    public String getLoadControlStatusByName(String value) {
        return getMapKey(getLoadControlStatus(), value);
    }

    /**
     * 負荷の手動制御状態:062
     * @param loadControlStatus
     */

    public void setLoadControlStatus(LinkedHashMap<String, String> loadControlStatus) {
        this.loadControlStatus = loadControlStatus;
    }

    /**
     * 警報の手動制御状態:063
     * @return
     */

    public LinkedHashMap<String, String> getWarnControlStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ALARM_MANUAL_CONTROL_STATUS_063, warnControlStatus);
    }

    /**
     * 警報の手動制御状態:063
     * @return
     */
    public String getWarnControlStatusByName(String value) {
        return getMapKey(getWarnControlStatus(), value);
    }

    /**
     * 警報の手動制御状態:063
     * @param warnControlStatus
     */

    public void setWarnControlStatus(LinkedHashMap<String, String> warnControlStatus) {
        this.warnControlStatus = warnControlStatus;
    }

    /**
     * 出力端末手動制御情報:064
     * @return
     */

    public LinkedHashMap<String, String> getOutputTermControlInfo() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.OUTPUT_TERMINAL_MANUAL_CONTROL_064, outputTermControlInfo);
    }

    /**
     * 出力端末手動制御情報:064
     * @return
     */
    public String getOutputTermControlInfoByName(String value) {
        return getMapKey(getOutputTermControlInfo(), value);
    }

    /**
     * 出力端末手動制御情報:064
     * @param outputTermControlInfo
     */

    public void setOutputTermControlInfo(LinkedHashMap<String, String> outputTermControlInfo) {
        this.outputTermControlInfo = outputTermControlInfo;
    }

    /**
     * 端末種別一覧【FV2】:065
     * @return
     */

    public LinkedHashMap<String, String> getTermKindFv2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_065, termKindFv2);
    }

    /**
     * 端末種別一覧【FV2】:065
     * @return
     */
    public String getTermKindFv2ByName(String value) {
        return getMapKey(getTermKindFv2(), value);
    }

    /**
     * 端末種別一覧【FV2】:065
     * @param termKindFv2
     */

    public void setTermKindFv2(LinkedHashMap<String, String> termKindFv2) {
        this.termKindFv2 = termKindFv2;
    }

    /**
     * 端末種別一覧【FVPα(D)】:066
     * @return
     */

    public LinkedHashMap<String, String> getTermKindFvpaD() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_066, termKindFvpaD);
    }

    /**
     * 端末種別一覧【FVPα(D)】:066
     * @return
     */
    public String getTermKindFvpaDByName(String value) {
        return getMapKey(getTermKindFvpaD(), value);
    }

    /**
     * 端末種別一覧【FVPα(D)】:066
     * @param termKindFvpaD
     */

    public void setTermKindFvpaD(LinkedHashMap<String, String> termKindFvpaD) {
        this.termKindFvpaD = termKindFvpaD;
    }

    /**
     * 端末種別一覧【FVPα(G2)】:067
     * @return
     */

    public LinkedHashMap<String, String> getTermKindFvpaG2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_067, termKindFvpaG2);
    }

    /**
     * 端末種別一覧【FVPα(G2)】:067
     * @return
     */
    public String getTermKindFvpaG2ByName(String value) {
        return getMapKey(getTermKindFvpaG2(), value);
    }

    /**
     * 端末種別一覧【FVPα(G2)】:067
     * @param termKindFvpaG2
     */

    public void setTermKindFvpaG2(LinkedHashMap<String, String> termKindFvpaG2) {
        this.termKindFvpaG2 = termKindFvpaG2;
    }

    /**
     * ポイントアドレス一覧:068
     * @return
     */

    public LinkedHashMap<String, String> getPointAddress() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POINT_ADDRESS_LIST_068, pointAddress);
    }

    /**
     * ポイントアドレス一覧:068
     * @return
     */
    public String getPointAddressByName(String value) {
        return getMapKey(getPointAddress(), value);
    }

    /**
     * ポイントアドレス一覧:068
     * @param pointAddress
     */

    public void setPointAddress(LinkedHashMap<String, String> pointAddress) {
        this.pointAddress = pointAddress;
    }

    /**
     * ポイント種別:069
     * @return
     */

    public LinkedHashMap<String, String> getPointKind() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POINT_TYPE_069, pointKind);
    }

    /**
     * ポイント種別:069
     * @return
     */
    public String getPointKindByName(String value) {
        return getMapKey(getPointKind(), value);
    }

    /**
     * ポイント種別:069
     * @param pointKind
     */

    public void setPointKind(LinkedHashMap<String, String> pointKind) {
        this.pointKind = pointKind;
    }

    /**
     * センサー種別:070
     * @return
     */

    public LinkedHashMap<String, String> getSensorKind() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SENSOR_TYPE_070, sensorKind);
    }

    /**
     * センサー種別:070
     * @return
     */
    public String getSensorKindByName(String value) {
        return getMapKey(getSensorKind(), value);
    }

    /**
     * センサー種別:070
     * @param sensorKind
     */

    public void setSensorKind(LinkedHashMap<String, String> sensorKind) {
        this.sensorKind = sensorKind;
    }

    /**
     * 伝送モード:071
     * @return
     */

    public LinkedHashMap<String, String> getTransmissionMode() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TRANSMISSION_MODE_071, transmissionMode);
    }

    /**
     * 伝送モード:071
     * @return
     */
    public String getTransmissionModeByName(String value) {
        return getMapKey(getTransmissionMode(), value);
    }

    /**
     * 伝送モード:071
     * @param transmissionMode
     */

    public void setTransmissionMode(LinkedHashMap<String, String> transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    /**
     * 伝送速度:072
     * @return
     */

    public LinkedHashMap<String, String> getTransmissionSpeed() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TRANSMISSION_SPEED_072, transmissionSpeed);
    }

    /**
     * 伝送速度:072
     * @return
     */
    public String getTransmissionSpeedByName(String value) {
        return getMapKey(getTransmissionSpeed(), value);
    }

    /**
     * 伝送速度:072
     * @param transmissionSpeed
     */

    public void setTransmissionSpeed(LinkedHashMap<String, String> transmissionSpeed) {
        this.transmissionSpeed = transmissionSpeed;
    }

    /**
     * 出力機能選択:073
     * @return
     */

    public LinkedHashMap<String, String> getOutputFunctionSelect() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.OUTPUT_FUNCTION_SELECT_073, outputFunctionSelect);
    }

    /**
     * 出力機能選択:073
     * @return
     */
    public String getOutputFunctionSelectByName(String value) {
        return getMapKey(getOutputFunctionSelect(), value);
    }

    /**
     * 出力機能選択:073
     * @param outputFunctionSelect
     */

    public void setOutputFunctionSelect(LinkedHashMap<String, String> outputFunctionSelect) {
        this.outputFunctionSelect = outputFunctionSelect;
    }

    /**
     * 制御条件:074
     * @return
     */

    public LinkedHashMap<String, String> getContorolCondition() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.CONTROL_CONDITION_074, contorolCondition);
    }

    /**
     * 制御条件:074
     * @return
     */
    public String getContorolConditionByName(String value) {
        return getMapKey(getContorolCondition(), value);
    }

    /**
     * 制御条件:074
     * @param contorolCondition
     */

    public void setContorolCondition(LinkedHashMap<String, String> contorolCondition) {
        this.contorolCondition = contorolCondition;
    }

    /**
     * イベント条件:075
     * @return
     */

    public LinkedHashMap<String, String> getEventCondition() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.EVENT_CONDITION_075, eventCondition);
    }

    /**
     * イベント条件:075
     * @return
     */
    public String getEventConditionByName(String value) {
        return getMapKey(getEventCondition(), value);
    }

    /**
     * イベント条件:075
     * @param eventCondition
     */

    public void setEventCondition(LinkedHashMap<String, String> eventCondition) {
        this.eventCondition = eventCondition;
    }

    /**
     * 遮断状態方向:076
     * @return
     */

    public LinkedHashMap<String, String> getCutStatusDirection() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_STATUS_DIRECTION_076, cutStatusDirection);
    }

    /**
     * 遮断状態方向:076
     * @return
     */
    public String getCutStatusDirectionByName(String value) {
        return getMapKey(getCutStatusDirection(), value);
    }

    /**
     * 遮断状態方向:076
     * @param cutStatusDirection
     */

    public void setCutStatusDirection(LinkedHashMap<String, String> cutStatusDirection) {
        this.cutStatusDirection = cutStatusDirection;
    }

    /**
     * デマンド運転中:077
     * @return
     */

    public LinkedHashMap<String, String> getDemandOperation() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DEMAND_DRIVING_077, demandOperation);
    }

    /**
     * デマンド運転中:077
     * @return
     */
    public String getDemandOperationByName(String value) {
        return getMapKey(getDemandOperation(), value);
    }

    /**
     * デマンド運転中:077
     * @param demandOperation
     */

    public void setDemandOperation(LinkedHashMap<String, String> demandOperation) {
        this.demandOperation = demandOperation;
    }

    /**
     * デマンド警報フラグ:078
     * @return
     */

    public LinkedHashMap<String, String> getDemandWarnFlg() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DEMAND_ALARM_FLG_078, demandWarnFlg);
    }

    /**
     * デマンド警報フラグ:078
     * @return
     */
    public String getDemandWarnFlgByName(String value) {
        return getMapKey(getDemandWarnFlg(), value);
    }

    /**
     * デマンド警報フラグ:078
     * @param demandWarnFlg
     */

    public void setDemandWarnFlg(LinkedHashMap<String, String> demandWarnFlg) {
        this.demandWarnFlg = demandWarnFlg;
    }

    /**
     * 異常フラグ:079
     * @return
     */

    public LinkedHashMap<String, String> getErrorFlg() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ABNORMAL_FLG_079, errorFlg);
    }

    /**
     * 異常フラグ:079
     * @return
     */
    public String getErrorFlgByName(String value) {
        return getMapKey(getErrorFlg(), value);
    }

    /**
     * 異常フラグ:079
     * @param errorFlg
     */

    public void setErrorFlg(LinkedHashMap<String, String> errorFlg) {
        this.errorFlg = errorFlg;
    }

    /**
     * 冷暖切替:080
     * @return
     */

    public LinkedHashMap<String, String> getCoolWarmSwitch() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AIR_CHANGING_080, coolWarmSwitch);
    }

    /**
     * 冷暖切替:080
     * @return
     */
    public String getCoolWarmSwitchByName(String value) {
        return getMapKey(getCoolWarmSwitch(), value);
    }

    /**
     * 冷暖切替:080
     * @param coolWarmSwitch
     */

    public void setCoolWarmSwitch(LinkedHashMap<String, String> coolWarmSwitch) {
        this.coolWarmSwitch = coolWarmSwitch;
    }

    /**
     * 計測ポイント状態:081
     * @return
     */

    public LinkedHashMap<String, String> getMeasurePointStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MEASUREMENT_POINT_STATUS_081, measurePointStatus);
    }

    /**
     * 計測ポイント状態:081
     * @return
     */
    public String getMeasurePointStatusByName(String value) {
        return getMapKey(getMeasurePointStatus(), value);
    }

    /**
     * 計測ポイント状態:081
     * @param measurePointStatus
     */
    public void setMeasurePointStatus(LinkedHashMap<String, String> measurePointStatus) {
        this.measurePointStatus = measurePointStatus;
    }

    /**
     * 電池警報:082
     * @return
     */

    public LinkedHashMap<String, String> getBatteryWarning() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.BATTERY_ALARM_082, batteryWarning);
    }

    /**
     * 電池警報:082
     * @return
     */
    public String getBatteryWarningByName(String value) {
        return getMapKey(getBatteryWarning(), value);
    }

    /**
     * 電池警報:082
     * @param batteryWarning
     */

    public void setBatteryWarning(LinkedHashMap<String, String> batteryWarning) {
        this.batteryWarning = batteryWarning;
    }

    /**
     * アンテナ強度:083
     * @return
     */

    public LinkedHashMap<String, String> getAntennaStrength() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ANTENNA_STRENGTH_083, antennaStrength);
    }

    /**
     * アンテナ強度:083
     * @return
     */
    public String getAntennaStrengthByName(String value) {
        return getMapKey(getAntennaStrength(), value);
    }

    /**
     * アンテナ強度:083
     * @param antennaStrength
     */

    public void setAntennaStrength(LinkedHashMap<String, String> antennaStrength) {
        this.antennaStrength = antennaStrength;
    }

    /**
     * 製品識別情報:084
     * @return
     */

    public LinkedHashMap<String, String> getProductsIdentificationInfo() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PRODUCT_IDENTIFICATION_INFO_084, productsIdentificationInfo);
    }

    /**
     * 製品識別情報:084
     */
    public String getProductsIdentificationInfoByName(String value) {
        return getMapKey(getProductsIdentificationInfo(), value);
    }

    /**
     * 製品識別情報:084
     * @param productsIdentificationInfo
     */

    public void setProductsIdentificationInfo(LinkedHashMap<String, String> productsIdentificationInfo) {
        this.productsIdentificationInfo = productsIdentificationInfo;
    }

    /**
     * 時計同期状態:085
     * @return timeSynchroStatus
     */
    public LinkedHashMap<String, String> getTimeSynchroStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TIME_SYNC_METHOD_085, timeSynchroStatus);
    }

    /**
     * 時計同期状態:085
     * @return
     */
    public String getTimeSynchroStatusByName(String value) {
        return getMapKey(getTimeSynchroStatus(), value);
    }

    /**
     * 時計同期状態:085
     * @param timeSynchroStatus セットする timeSynchroStatus
     */
    public void setTimeSynchroStatus(LinkedHashMap<String, String> timeSynchroStatus) {
        this.timeSynchroStatus = timeSynchroStatus;
    }

    /**
     * パルス入力モニタ:086
     * @return
     */

    public LinkedHashMap<String, String> getPalseInputMonitor() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PULSE_INPUT_MONITOR_086, palseInputMonitor);
    }

    /**
     * パルス入力モニタ:086
     * @return
     */
    public String getPalseInputMonitorByName(String value) {
        return getMapKey(getPalseInputMonitor(), value);
    }

    /**
     * パルス入力モニタ:086
     * @param palseInputMonitor
     */

    public void setPalseInputMonitor(LinkedHashMap<String, String> palseInputMonitor) {
        this.palseInputMonitor = palseInputMonitor;
    }

    /**
     * 発生/復旧フラグ:087
     * @return
     */

    public LinkedHashMap<String, String> getOccurrenceRestoreFlg() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.OCCURRENCE_FLG_087, occurrenceRestoreFlg);
    }

    /**
     * 発生/復旧フラグ:087
     * @return
     */
    public String getOccurrenceRestoreFlgByName(String value) {
        return getMapKey(getOccurrenceRestoreFlg(), value);
    }

    /**
     * 発生/復旧フラグ:087
     * @param occurrenceRestoreFlg
     */

    public void setOccurrenceRestoreFlg(LinkedHashMap<String, String> occurrenceRestoreFlg) {
        this.occurrenceRestoreFlg = occurrenceRestoreFlg;
    }

    /**
     * 余裕／超過:088
     * @return
     */

    public LinkedHashMap<String, String> getMarginExcess() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MARGIN_FLG_088, marginExcess);
    }

    /**
     * 余裕／超過:088
     * @return
     */
    public String getMarginExcessByName(String value) {
        return getMapKey(getMarginExcess(), value);
    }

    /**
     * 余裕／超過:088
     * @param marginExcess
     */

    public void setMarginExcess(LinkedHashMap<String, String> marginExcess) {
        this.marginExcess = marginExcess;
    }

    /**
     * 負荷:089
     * @return
     */

    public LinkedHashMap<String, String> getLoadOnOff() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.LOAD_CONTROL_089, loadOnOff);
    }

    /**
     * 負荷:089
     * @return
     */
    public String getLoadOnOffByName(String value) {
        return getMapKey(getLoadOnOff(), value);
    }

    /**
     * 負荷:089
     * @param loadOnOff
     */

    public void setLoadOnOff(LinkedHashMap<String, String> loadOnOff) {
        this.loadOnOff = loadOnOff;
    }

    /**
     * 装置エラー情報:090
     * @return
     */

    public LinkedHashMap<String, String> getEquipmentErrorInfo() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ERROR_INFO_090, equipmentErrorInfo);
    }

    /**
     * 装置エラー情報:090
     * @return
     */
    public String getEquipmentErrorInfoByName(String value) {
        return getMapKey(getEquipmentErrorInfo(), value);
    }

    /**
     * 装置エラー情報:090
     * @param equipmentErrorInfo
     */

    public void setEquipmentErrorInfo(LinkedHashMap<String, String> equipmentErrorInfo) {
        this.equipmentErrorInfo = equipmentErrorInfo;
    }

    /**
     * 宛先区分:091
     * @return destinationKbn
     */
    public LinkedHashMap<String, String> getDestinationKbn() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DESTINATION_TYPE, destinationKbn);
    }

    /**
     * 宛先区分:091
     * @param destinationKbn セットする destinationKbn
     */
    public void setDestinationKbn(LinkedHashMap<String, String> destinationKbn) {
        this.destinationKbn = destinationKbn;
    }

    /**
     * デマンド警報発生レベル:092
     *
     * @return
     */
    public LinkedHashMap<String, String> getDemandAlertLv() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.DEMAND_ALARM_OCCURRENCE_LEVEL_092, demandAlertLv);
    }

    /**
     * デマンド警報発生レベル:092
     *
     * @return
     */
    public String getDemandAlertLvByName(String value) {
        return getMapKey(getDemandAlertLv(), value);
    }

    /**
     * デマンド警報発生レベル:092
     *
     * @param demandAlertLv
     */
    public void setDemandAlertLv(LinkedHashMap<String, String> demandAlertLv) {
        this.demandAlertLv = demandAlertLv;
    }

    /**
     * 端末種別一覧【FVP(D)】:093
     * @return
     */

    public LinkedHashMap<String, String> getTermKindFvpD() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_093, termKindFvpD);
    }

    /**
     * 端末種別一覧【FVP(D)】:093
     * @return
     */
    public String getTermKindFvpDByName(String value) {
        return getMapKey(getTermKindFvpD(), value);
    }

    /**
     * 端末種別一覧【FVP(D)】:093
     * @param termKindFvpD
     */

    public void setTermKindFvpD(LinkedHashMap<String, String> termKindFvpD) {
        this.termKindFvpD = termKindFvpD;
    }

    /**
     * @return 目標値切替（目標方式）【Eα】:094
     */
    public LinkedHashMap<String, String> getTargetValueChangeEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TARGET_VALUE_CHANGE_094, targetValueChangeEa);
    }

    /**
     * @param 目標値切替（目標方式）【Eα】:094
     */
    public void setTargetValueChangeEa(LinkedHashMap<String, String> targetValueChangeEa) {
        this.targetValueChangeEa = targetValueChangeEa;
    }

    /**
     * 目標値切替（目標方式）【Eα】:094
     * @return
     */
    public String getTargetValueChangeEaByName(String value) {
        return getMapKey(getTargetValueChangeEa(), value);
    }

    /**
     * @return 095:目標値切替（目標方式）【Eα2】:095
     */
    public LinkedHashMap<String, String> getTargetValueChangeEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TARGET_VALUE_CHANGE_095, targetValueChangeEa2);
    }

    /**
     * @param 095:目標値切替（目標方式）【Eα2】:095
     */
    public void setTargetValueChangeEa2(LinkedHashMap<String, String> targetValueChangeEa2) {
        this.targetValueChangeEa2 = targetValueChangeEa2;
    }

    /**
     * 目標値切替（目標方式）【Eα2】:095
     * @return
     */
    public String getTargetValueChangeEa2ByName(String value) {
        return getMapKey(getTargetValueChangeEa2(), value);
    }

    /**
     * @return 端末種別一覧【Eα】:096
     */
    public LinkedHashMap<String, String> getTermKindEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_096, termKindEa);
    }

    /**
     * @param 端末種別一覧【Eα】:096
     */
    public void setTermKindEa(LinkedHashMap<String, String> termKindEa) {
        this.termKindEa = termKindEa;
    }

    /**
     * 端末種別一覧【Eα】:096
     * @return
     */
    public String getTermKindEaByName(String value) {
        return getMapKey(getTermKindEa(), value);
    }

    /**
     * @return 端末種別一覧【Eα2】:097
     */
    public LinkedHashMap<String, String> getTermKindEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.TERMINAL_TYPE_LIST_097, termKindEa2);
    }

    /**
     * @param 端末種別一覧【Eα2】:097
     */
    public void setTermKindEa2(LinkedHashMap<String, String> termKindEa2) {
        this.termKindEa2 = termKindEa2;
    }

    /**
     * 端末種別一覧【Eα2】:097
     * @return
     */
    public String getTermKindEa2ByName(String value) {
        return getMapKey(getTermKindEa2(), value);
    }

    /**
     * @return ポイント種別【Eα】【Eα2】:098
     */
    public LinkedHashMap<String, String> getPointKindEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POINT_TYPE_098, pointKindEa);
    }

    /**
     * @param ポイント種別【Eα】【Eα2】:098
     */
    public void setPointKindEa(LinkedHashMap<String, String> pointKindEa) {
        this.pointKindEa = pointKindEa;
    }

    /**
     * ポイント種別【Eα】【Eα2】:098
     * @return
     */
    public String getPointKindEaByName(String value) {
        return getMapKey(getPointKindEa(), value);
    }

    /**
     * @return 制御条件【Eα】【Eα2】:099
     */
    public LinkedHashMap<String, String> getContorolConditionEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.CONTROL_CONDITION_099, contorolConditionEa);
    }

    /**
     * @param 制御条件【Eα】【Eα2】:099
     */
    public void setContorolConditionEa(LinkedHashMap<String, String> contorolConditionEa) {
        this.contorolConditionEa = contorolConditionEa;
    }

    /**
     * 制御条件【Eα】【Eα2】:099
     * @return
     */
    public String getContorolConditionEaByName(String value) {
        return getMapKey(getContorolConditionEa(), value);
    }

    /**
     * @return イベント条件【Eα】【Eα2】:100
     */
    public LinkedHashMap<String, String> getEventConditionEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.EVENT_CONDITION_100, eventConditionEa);
    }

    /**
     * @param イベント条件【Eα】【Eα2】:100
     */
    public void setEventConditionEa(LinkedHashMap<String, String> eventConditionEa) {
        this.eventConditionEa = eventConditionEa;
    }

    /**
     * イベント条件【Eα】【Eα2】:100
     * @return
     */
    public String getEventConditionEaByName(String value) {
        return getMapKey(getEventConditionEa(), value);
    }

    /**
     * @return 製品識別情報【Eα】:101
     */
    public LinkedHashMap<String, String> getProductsIdentificationInfoEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PRODUCT_IDENTIFICATION_INFO_101, productsIdentificationInfoEa);
    }

    /**
     * @param 製品識別情報【Eα】:101
     */
    public void setProductsIdentificationInfoEa(LinkedHashMap<String, String> productsIdentificationInfoEa) {
        this.productsIdentificationInfoEa = productsIdentificationInfoEa;
    }

    /**
     * 製品識別情報【Eα】:101
     * @return
     */
    public String getProductsIdentificationInfoEaByName(String value) {
        return getMapKey(getProductsIdentificationInfoEa(), value);
    }

    /**
     * @return 製品識別情報【Eα2】:102
     */
    public LinkedHashMap<String, String> getProductsIdentificationInfoEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.PRODUCT_IDENTIFICATION_INFO_102, productsIdentificationInfoEa2);
    }

    /**
     * @param 製品識別情報【Eα2】:102
     */
    public void setProductsIdentificationInfoEa2(LinkedHashMap<String, String> productsIdentificationInfoEa2) {
        this.productsIdentificationInfoEa2 = productsIdentificationInfoEa2;
    }

    /**
     * 製品識別情報【Eα2】:102
     * @return
     */
    public String getProductsIdentificationInfoEa2ByName(String value) {
        return getMapKey(getProductsIdentificationInfoEa2(), value);
    }

    /**
     * @return RS485異常状態【Eα】【Eα2】:103
     */
    public LinkedHashMap<String, String> getRs485AbnormalStatusEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.RS485_ABNORMAL_STATUS_103, rs485AbnormalStatusEa);
    }

    /**
     * @param RS485異常状態【Eα】【Eα2】:103
     */
    public void setRs485AbnormalStatusEa(LinkedHashMap<String, String> rs485AbnormalStatusEa) {
        this.rs485AbnormalStatusEa = rs485AbnormalStatusEa;
    }

    /**
     * RS485異常状態【Eα】【Eα2】:103
     * @return
     */
    public String getRs485AbnormalStatusEaByName(String value) {
        return getMapKey(getRs485AbnormalStatusEa(), value);
    }

    /**
     * @return エラー情報【Eα】【Eα2】:104
     */
    public LinkedHashMap<String, String> getEquipmentErrorInfoEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ERROR_INFO_104, equipmentErrorInfoEa);
    }

    /**
     * @param エラー情報【Eα】【Eα2】:104
     */
    public void setEquipmentErrorInfoEa(LinkedHashMap<String, String> equipmentErrorInfoEa) {
        this.equipmentErrorInfoEa = equipmentErrorInfoEa;
    }

    /**
     * エラー情報【Eα】【Eα2】:104
     * @return
     */
    public String getEquipmentErrorInfoEaByName(String value) {
        return getMapKey(getEquipmentErrorInfoEa(), value);
    }

    /**
     * @return エラー情報2【Eα2】:105
     */
    public LinkedHashMap<String, String> getEquipmentErrorInfo2Ea2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ERROR_INFO2_105, equipmentErrorInfo2Ea2);
    }

    /**
     * @param エラー情報2【Eα2】:105
     */
    public void setEquipmentErrorInfo2Ea2(LinkedHashMap<String, String> equipmentErrorInfo2Ea2) {
        this.equipmentErrorInfo2Ea2 = equipmentErrorInfo2Ea2;
    }

    /**
     * エラー情報2【Eα2】:105
     * @return
     */
    public String getEquipmentErrorInfo2Ea2ByName(String value) {
        return getMapKey(getEquipmentErrorInfo2Ea2(), value);
    }

    /**
     * @return イベント制御【Eα】【Eα2】:106
     */
    public LinkedHashMap<String, String> getEventControlEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.EVENT_CONTROL_106, eventControlEa);
    }

    /**
     * @param イベント制御【Eα】【Eα2】:106
     */
    public void setEventControlEa(LinkedHashMap<String, String> eventControlEa) {
        this.eventControlEa = eventControlEa;
    }

    /**
     * イベント制御【Eα】【Eα2】:106
     * @return
     */
    public String getEventControlEaByName(String value) {
        return getMapKey(getEventControlEa(), value);
    }

    /**
     * @return イベント対象指定【Eα】【Eα2】:107
     */
    public LinkedHashMap<String, String> getEventTargetSelectEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.EVENT_TARGET_SELECT_107, eventTargetSelectEa);
    }

    /**
     * @param イベント対象指定【Eα】【Eα2】:107
     */
    public void setEventTargetSelectEa(LinkedHashMap<String, String> eventTargetSelectEa) {
        this.eventTargetSelectEa = eventTargetSelectEa;
    }

    /**
     * イベント対象指定【Eα】【Eα2】:107
     * @return
     */
    public String getEventTargetSelectEaByName(String value) {
        return getMapKey(getEventTargetSelectEa(), value);
    }

    /**
     * @return 照明制御切替【Eα】【Eα2】:108
     */
    public LinkedHashMap<String, String> getIlluminationControlChangeEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ILLUMINATION_CONTROL_CHANGE_108, illuminationControlChangeEa);
    }

    /**
     * @param 照明制御切替【Eα】【Eα2】:108
     */
    public void setIlluminationControlChangeEa(LinkedHashMap<String, String> illuminationControlChangeEa) {
        this.illuminationControlChangeEa = illuminationControlChangeEa;
    }

    /**
     * 照明制御切替【Eα】【Eα2】:108
     * @return
     */
    public String getIlluminationControlChangeEaByName(String value) {
        return getMapKey(getIlluminationControlChangeEa(), value);
    }

    /**
     * @return 警報状態【Eα】【Eα2】:109
     */
    public LinkedHashMap<String, String> getAlarmStatusEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ALARM_STATUS_109, alarmStatusEa);
    }

    /**
     * @param 警報状態【Eα】【Eα2】:109
     */
    public void setAlarmStatusEa(LinkedHashMap<String, String> alarmStatusEa) {
        this.alarmStatusEa = alarmStatusEa;
    }

    /**
     * 警報状態【Eα】【Eα2】:109
     * @return
     */
    public String getAlarmStatusEaByName(String value) {
        return getMapKey(getIlluminationControlChangeEa(), value);
    }

    /**
     * @return イベント要因【Eα】【Eα2】:110
     */
    public LinkedHashMap<String, String> getEventFactorEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.EVENT_FACTOR_110, eventFactorEa);
    }

    /**
     * @param イベント要因【Eα】【Eα2】:110
     */
    public void setEventFactorEa(LinkedHashMap<String, String> eventFactorEa) {
        this.eventFactorEa = eventFactorEa;
    }

    /**
     * イベント要因【Eα】【Eα2】:110
     * @return
     */
    public String getEventFactorEaByName(String value) {
        return getMapKey(getEventFactorEa(), value);
    }

    /**
     * @return 照明モード【Eα】【Eα2】:111
     */
    public LinkedHashMap<String, String> getIlluminationModeEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.ILLUMINATION_MODE_111, illuminationModeEa);
    }

    /**
     * @param 照明モード【Eα】【Eα2】:111
     */
    public void setIlluminationModeEa(LinkedHashMap<String, String> illuminationModeEa) {
        this.illuminationModeEa = illuminationModeEa;
    }

    /**
     * 照明モード【Eα】【Eα2】:111
     * @return
     */
    public String getIlluminationModeEaByName(String value) {
        return getMapKey(getIlluminationModeEa(), value);
    }

    /**
     * @return スケジュール連動【Eα】【Eα2】:112
     */
    public LinkedHashMap<String, String> getScheduleInterLockingEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SCHEDULE_INTERLOCKING_112, scheduleInterLockingEa);
    }

    /**
     * @param スケジュール連動【Eα】【Eα2】:112
     */
    public void setScheduleInterLockingEa(LinkedHashMap<String, String> scheduleInterLockingEa) {
        this.scheduleInterLockingEa = scheduleInterLockingEa;
    }

    /**
     * スケジュール連動【Eα】【Eα2】:112
     * @return
     */
    public String getScheduleInterLockingEaByName(String value) {
        return getMapKey(getScheduleInterLockingEa(), value);
    }

    /**
     * @return 遮断状態方向【Eα】【Eα2】:113
     */
    public LinkedHashMap<String, String> getCutStatusDirectionEa() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SHUTOFF_STATUS_DIRECTION_113, cutStatusDirectionEa);
    }

    /**
     * @param 遮断状態方向【Eα】【Eα2】:113
     */
    public void setCutStatusDirectionEa(LinkedHashMap<String, String> cutStatusDirectionEa) {
        this.cutStatusDirectionEa = cutStatusDirectionEa;
    }

    /**
     * 遮断状態方向【Eα】【Eα2】:113
     * @return
     */
    public String getCutStatusDirectionEaByName(String value) {
        return getMapKey(getCutStatusDirectionEa(), value);
    }

    /**
     * @return 天気:114
     */
    public LinkedHashMap<String, String> getWeather() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.WEATHER_114, weather);
    }

    /**
     * @param 天気:114
     */
    public void setWeather(LinkedHashMap<String, String> weather) {
        this.weather = weather;
    }

    /**
     * 天気:114
     * @return
     */
    public String getWeatherByName(String value) {
        return getMapKey(getWeather(), value);
    }

    /**
     * @return 風向:115
     */
    public LinkedHashMap<String, String> getWindDirection() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.WIND_DIRECTION_115, windDirection);
    }

    /**
     * @param 風向:115
     */
    public void setWindDirection(LinkedHashMap<String, String> windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * 風向:115
     * @return
     */
    public String getWindDirectionByName(String value) {
        return getMapKey(getWindDirection(), value);
    }

    /**
     * @return 雲量:116
     */
    public LinkedHashMap<String, String> getCloudCover() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.CLOUD_COVER_116, cloudCover);
    }

    /**
     * @param 雲量:116
     */
    public void setCloudCover(LinkedHashMap<String, String> cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * 雲量:116
     * @return
     */
    public String getCloudCoverByName(String value) {
        return getMapKey(getCloudCover(), value);
    }

    /**
     * @return センサ接続装置【Eα2】:117
     */
    public LinkedHashMap<String, String> getSensorConnectEquipmentEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SENSOR_CONNECT_EQUIPMENT_117, sensorConnectEquipmentEa2);
    }

    /**
     * @param センサ接続装置【Eα2】:117
     */
    public void setSensorConnectEquipmentEa2(LinkedHashMap<String, String> sensorConnectEquipmentEa2) {
        this.sensorConnectEquipmentEa2 = sensorConnectEquipmentEa2;
    }

    /**
     * センサ接続装置【Eα2】:117
     * @return
     */
    public String getSensorConnectEquipmentEa2ByName(String value) {
        return getMapKey(getSensorConnectEquipmentEa2(), value);
    }

    /**
     * @return センサ種類【Eα2】:118
     */
    public LinkedHashMap<String, String> getSensorKindEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SENSOR_KIND_118, sensorKindEa2);
    }

    /**
     * @param センサ種類【Eα2】:118
     */
    public void setSensorKindEa2(LinkedHashMap<String, String> sensorKindEa2) {
        this.sensorKindEa2 = sensorKindEa2;
    }

    /**
     * センサ種類【Eα2】:118
     * @return
     */
    public String getSensorKindEa2ByName(String value) {
        return getMapKey(getSensorKindEa2(), value);
    }

    /**
     * @return Aiel Master動作モード【Eα2】:119
     */
    public LinkedHashMap<String, String> getAielMasterActionModeEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.AIEL_MASTER_ACTION_MODE_119, aielMasterActionModeEa2);
    }

    /**
     * @param Aiel Master動作モード【Eα2】:119
     */
    public void setAielMasterActionModeEa2(LinkedHashMap<String, String> aielMasterActionModeEa2) {
        this.aielMasterActionModeEa2 = aielMasterActionModeEa2;
    }

    /**
     * Aiel Master動作モード【Eα2】:119
     * @return
     */
    public String getAielMasterActionModeEa2ByName(String value) {
        return getMapKey(getAielMasterActionModeEa2(), value);
    }

    /**
     * @return 節電可能計測指定【Eα2】:120
     */
    public LinkedHashMap<String, String> getPowerSavingPossibleEa2() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.POWER_SAVING_POSSIBLE_120, powerSavingPossibleEa2);
    }

    /**
     * @param 節電可能計測指定【Eα2】:120
     */
    public void setPowerSavingPossibleEa2(LinkedHashMap<String, String> powerSavingPossibleEa2) {
        this.powerSavingPossibleEa2 = powerSavingPossibleEa2;
    }

    /**
     * 節電可能計測指定【Eα2】:120
     * @return
     */
    public String getPowerSavingPossibleEa2ByName(String value) {
        return getMapKey(getPowerSavingPossibleEa2(), value);
    }

    /**
     * @return 更新区分 :137
     */
    public LinkedHashMap<String, String> getUpdateKbn() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.UPDATE_KBN_137, updateKbn);
    }

    /**
     * @param 更新区分 :137
     */
    public void setUpdateKbn(LinkedHashMap<String, String> updateKbn) {
        this.updateKbn = updateKbn;
    }

    /**
     * 更新区分 :137
     * @return
     */
    public String getUpdateKbn(String value) {
        return getMapKey(getUpdateKbn(), value);
    }

    /**
     * 配信先コード：138
     * @return
     */
    public LinkedHashMap<String, String> getOshiraseDeliveryCd() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.OSHIRASE_DELIVERY_CD_138, oshiraseDeliveryCd);
    }

    /**
     * @param 配信先コード：138
     */
    public void setOshiraseDeliveryCd(LinkedHashMap<String, String> oshiraseDeliveryCd) {
        this.oshiraseDeliveryCd = oshiraseDeliveryCd;
    }

    /**
     * 配信先コード：138
     * @return
     */
    public String getOshiraseDeliveryCdByName(String value) {
        return getMapKey(getOshiraseDeliveryCd(), value);
    }

    /**
     * 料金メニュー :312
     * @return
     */
    public LinkedHashMap<String, String> getSmsElectricMenu() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_ELECTRIC_MENU, smsElectricMenu);
    }

    /**
     * 料金メニュー :312
     * @return
     */
    public void setSmsElectricMenu(LinkedHashMap<String, String> smsElectricMenu) {
        this.smsElectricMenu = smsElectricMenu;
    }

    /**
     * 料金メニュー :312
     * @return
     */
    public String getSmsElectricMenuByName(String value) {
        return getMapKey(getSmsElectricMenu(), value);
    }

    /**
     * @return smsLoadLimit
     */
    public LinkedHashMap<String, String> getSmsLoadLimit() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_LOAD_LIMIT_300, smsLoadLimit);
    }

    /**
     * @param smsLoadLimit セットする smsLoadLimit
     */
    public void setSmsLoadLimit(LinkedHashMap<String, String> smsLoadLimit) {
        this.smsLoadLimit = smsLoadLimit;
    }

    /**
     * smsLoadLimit
     * @return
     */
    public String getSmsLoadLimit(String value) {
        return getMapKey(getSmsLoadLimit(), value);
    }

    /**
     * @return smsLoadCurrent
     */
    public LinkedHashMap<String, String> getSmsLoadCurrent() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_LOAD_CURRENT_301, smsLoadCurrent);
    }

    /**
     * @param smsLoadCurrent セットする smsLoadCurrent
     */
    public void setSmsLoadCurrent(LinkedHashMap<String, String> smsLoadCurrent) {
        this.smsLoadCurrent = smsLoadCurrent;
    }

    /**
     * @return smsOpenMode
     */
    public LinkedHashMap<String, String> getSmsOpenMode() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_OPEN_MODE_302, smsOpenMode);
    }

    /**
     * @param smsOpenMode セットする smsOpenMode
     */
    public void setSmsOpenMode(LinkedHashMap<String, String> smsOpenMode) {
        this.smsOpenMode = smsOpenMode;
    }

    /**
     * smsOpenMode
     * @return
     */
    public String getSmsOpenMode(String value) {
        return getMapKey(getSmsOpenMode(), value);
    }

    /**
     * @return smsDispYear
     */
    public LinkedHashMap<String, String> getSmsDispYear() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DISP_YEAR_303, smsDispYear);
    }

    /**
     * @param smsDispYear セットする smsDispYear
     */
    public void setSmsDispYear(LinkedHashMap<String, String> smsDispYear) {
        this.smsDispYear = smsDispYear;
    }

    /**
     * smsDispYear
     * @return
     */
    public String getSmsDispYear(String value) {
        return getMapKey(getSmsDispYear(), value);
    }

    /**
     * @return smsGengo
     */
    public LinkedHashMap<String, String> getSmsGengo() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_GENGO_304, smsGengo);
    }

    /**
     * @param smsGengo セットする smsGengo
     */
    public void setSmsGengo(LinkedHashMap<String, String> smsGengo) {
        this.smsGengo = smsGengo;
    }

    /**
     * @return smsExamNotic
     */
    public LinkedHashMap<String, String> getSmsExamNotic() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_EXAM_NOTIC_305, smsExamNotic);
    }

    /**
     * @param smsExamNotic セットする smsExamNotic
     */
    public void setSmsExamNotic(LinkedHashMap<String, String> smsExamNotic) {
        this.smsExamNotic = smsExamNotic;
    }

    /**
     * smsExamNotic
     * @return
     */
    public String getSmsExamNotic(String value) {
        return getMapKey(getSmsExamNotic(), value);
    }

    /**
     * @param 表示方向:306
     */
    public LinkedHashMap<String, String> getSmsDispDirect() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DISP_DIRECT_306, smsDispDirect);
    }

    /**
     * @return 表示方向:306
     */
    public void setSmsDispDirect(LinkedHashMap<String, String> smsDispDirect) {
        this.smsDispDirect = smsDispDirect;
    }

    /**
     * @param 表示種別:307
     */
    public LinkedHashMap<String, String> getSmsDispType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DISP_TYPE_307, smsDispType);
    }

    /**
     * @return 表示種別:307
     */
    public void setSmsDispType(LinkedHashMap<String, String> smsDispType) {
        this.smsDispType = smsDispType;
    }

    /**
     * @param 消費税扱い:308
     */
    public LinkedHashMap<String, String> getSmsSaleTaxDeal() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_SALE_TAX_DEAL_308, smsSaleTaxDeal);
    }

    /**
     * @return 消費税扱い:308
     */
    public void setSmsSaleTaxDeal(LinkedHashMap<String, String> smsSaleTaxDeal) {
        this.smsSaleTaxDeal = smsSaleTaxDeal;
    }

    /**
     * @param 小数部端数処理:309
     */
    public LinkedHashMap<String, String> getSmsDecimalFraction() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DECIMAL_FRACTION_309, smsDecimalFraction);
    }

    /**
     * @return 小数部端数処理:309
     */
    public void setSmsDecimalFraction(LinkedHashMap<String, String> smsDecimalFraction) {
        this.smsDecimalFraction = smsDecimalFraction;
    }

    /**
     * @param 年報締め月:310
     */
    public LinkedHashMap<String, String> getSmsYearCloseMonth() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_YEAR_CLOSE_MONTH_310, smsYearCloseMonth);
    }

    /**
     * @return 年報締め月:310
     */
    public void setSmsYearCloseMonth(LinkedHashMap<String, String> smsYearCloseMonth) {
        this.smsYearCloseMonth = smsYearCloseMonth;
    }

    /**
     * @param CO2排出係数単位:311
     */
    public LinkedHashMap<String, String> getSmsUnitCo2Coefficient() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_UNIT_CO2_COEFFICIENT_311, smsUnitCo2Coefficient);
    }

    /**
     * @return CO2排出係数単位:311
     */
    public void setSmsUnitCo2Coefficient(LinkedHashMap<String, String> smsUnitCo2Coefficient) {
        this.smsUnitCo2Coefficient = smsUnitCo2Coefficient;
    }

    /**
     * @param メーター種別:314
     */
    public LinkedHashMap<String, String> getSmsMeterKind() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_METER_KIND_314, smsMeterKind);
    }

    /**
     * @return メーター種別:314
     */
    public void setSmsMeterKind(LinkedHashMap<String, String> smsMeterKind) {
        this.smsMeterKind = smsMeterKind;
    }

    /**
     * @return 装置 稼働状況:315
     */
    public LinkedHashMap<String, String> getSmsDevStatus() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DEV_STATUS_315, smsDevStatus);
    }

    /**
     * @param ハンディ端末 稼働状況:315
     */
    public void setSmsDevStatus(LinkedHashMap<String, String> smsDevStatus) {
        this.smsDevStatus = smsDevStatus;
    }

    /**
     * ハンディ端末 稼働状況:315
     * @return
     */
    public String getSmsDevStatusByName(String value) {
        return getMapKey(getSmsDevStatus(), value);
    }

    /**
     * @param 装置異常:316
     */
    public LinkedHashMap<String, String> getSmsDevSta() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DEV_STA_316, smsDevSta);
    }

    /**
     * @return 装置異常:316
     */
    public void setSmsDevSta(LinkedHashMap<String, String> smsDevSta) {
        this.smsDevSta = smsDevSta;
    }

    /**
     * @param コンセントレーター異常:317
     */
    public LinkedHashMap<String, String> getSmsConcentSta() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_CONCENT_STA_317, smsConcentSta);
    }

    /**
     * @return コンセントレーター異常:317
     */
    public void setSmsConcentSta(LinkedHashMap<String, String> smsConcentSta) {
        this.smsConcentSta = smsConcentSta;
    }

    /**
     * @param メータ状態:318
     */
    public LinkedHashMap<String, String> getSmsMeterSta() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_METER_STA_318, smsMeterSta);
    }

    /**
     * @return メータ状態:318
     */
    public void setSmsMeterSta(LinkedHashMap<String, String> smsMeterSta) {
        this.smsMeterSta = smsMeterSta;
    }

    /**
     * @param 通信端末異常:319
     */
    public LinkedHashMap<String, String> getSmsTermSta() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_TERM_STA_319, smsTermSta);
    }

    /**
     * @return 通信端末異常:319
     */
    public void setSmsTermSta(LinkedHashMap<String, String> smsTermSta) {
        this.smsTermSta = smsTermSta;
    }

    /**
     * @return smsPulseType
     */
    public LinkedHashMap<String, String> getSmsPulseType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_PULSE_TYPE_322, smsPulseType);
    }

    /**
     * @param smsPulseType セットする smsPulseType
     */
    public void setSmsPulseType(LinkedHashMap<String, String> smsPulseType) {
        this.smsPulseType = smsPulseType;
    }

    /**
     * smsPulseType
     * @return
     */
    public String getSmsPulseType(String value) {
        return getMapKey(getSmsPulseType(), value);
    }

    /**
     * @return 検針種別:325
     */
    public LinkedHashMap<String, String> getSmsInspType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_INSP_TYPE_325, smsInspType);
    }

    /**
     * @param 検針種別:325
     */
    public void setSmsInspType(LinkedHashMap<String, String> smsInspType) {
        this.smsInspType = smsInspType;
    }

    /**
     * ハンディ種別 :326
     * @return
     */
    public String getSmsHandyTypeByName(String value) {
        return getMapKey(getSmsHandyType(), value);
    }

    /**
     * @return ハンディ種別:326
     */
    public LinkedHashMap<String, String> getSmsHandyType() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_HANDY_TYPE_326, smsHandyType);
    }

    /**
     * @param ハンディ種別:326
     */
    public void setSmsHandyType(LinkedHashMap<String, String> smsHandyType) {
        this.smsHandyType = smsHandyType;
    }

    /**
     * @return 表示時間単位:327
     */
    public LinkedHashMap<String, String> getSmsDispTimeUnit() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_DISP_TIME_UNIT_327, smsDispTimeUnit);
    }

    /**
     * @param 表示時間単位:327
     */
    public void setSmsDispTimeUnit(LinkedHashMap<String, String> smsDispTimeUnit) {
        this.smsInspType = smsDispTimeUnit;
    }

    /**
     * @return メーター状況:328
     */
    public LinkedHashMap<String, String> getSmsMeterPresSitu() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.SMS_PRES_SITU_328, smsMeterPresSitu);
    }

    /**
     * @param メーター状況:328
     */
    public void setSmsMeterPresSitu(LinkedHashMap<String, String> smsMeterPresSitu) {
        this.smsMeterPresSitu = smsMeterPresSitu;
    }

    /**
     * @return 抽出範囲:329
     */
    public LinkedHashMap<String, String> getMonthExtract() {
        return getLinkedHashMap(ApiGenericTypeConstants.GROUP_CODE.MONTH_EXTRACT_329, monthExtract);
    }

    /**
     * @param 抽出範囲:329
     */
    public void setMonthExtract(LinkedHashMap<String, String> monthExtract) {
        this.monthExtract = monthExtract;
    }

}
