package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ejb.EJB;	// 2024-02-01
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.GetSmsDeviceParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.UpdateSmsRelationParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.buildingdevice.UpdateSmsRelationRequest;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.GetSmsDeviceResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.ListSmsDevRelationResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.UpdateSmsRelationResponse;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.GetSmsDeviceResult;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.UpdateSmsRelationResult;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsDevRelationDetailResultData;
import jp.co.osaki.osol.entity.MMeter;	// 2024-02-01
import jp.co.osaki.osol.entity.MMeterPK;	// 2024-02-01
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConfigs;					//2025-03-10
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;	// 2024-02-01

/**
 * 建物装置設定 装置設定画面
 *
 * @author yoneda_y
 */
@Named(value = "smsServerSettingBuildingDeviceEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 8187126132011039566L;

    //smsConfigsの追加　2025-03-10
    private static final SmsConfigs smsConfigs = new SmsConfigs();

    //当クラスパッケージ名
    private String packageName = this.getClass().getPackage().getName();

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private EditBeanProperty editBeanProperty;

    // エラーメッセージ
    private List<String> invalidComponent;

    @EJB
    private MMeterDao mMeterDao;

    @Override
    public String init() {
        conversationStart();

        return null;
    }

    public String init(EditBeanProperty editBeanProperty) {
        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceEditBean():START"));

        // 初期処理
        init();

        invalidComponent = new ArrayList<>();

        this.editBeanProperty = editBeanProperty;

        String corpId = this.editBeanProperty.getBuildingInfo().getCorpId();
        Long buildingId = this.editBeanProperty.getBuildingInfo().getBuildingId();
        String devId = null;
        List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpId, buildingId, devId);
        covertDisplay(relationList);

        this.editBeanProperty.setAddDevIdFlg(true);

        eventLogger.debug(packageName.concat("smsServerSettingBuildingDeviceEditBean():END"));

        return "buildingdeviceEdit";
    }

    /**
     * 画面表示用に置き換え
     *
     * @param result
     */
    private void covertDisplay(List<ListSmsDevRelationDetailResultData> orgList) {

        List<String> devIdList;

        if (Objects.isNull(orgList)) {
            devIdList = new ArrayList<>();
        } else {
            devIdList = orgList.stream().map(s -> s.getDevId()).collect(Collectors.toList());
        }

        editBeanProperty.setOrgDevIdList(new ArrayList<String>(devIdList));
        editBeanProperty.setDevIdList(devIdList);
    }

    /**
     * 装置情報 登録・更新
     *
     * @return
     * @throws Exception
     */
    @Logged
    public String regist() throws Exception {

        // 追加、削除リスト作成
        createDevIdParam();

        // バリデーションチェック
        if (!validateInputParam()) {
            return STR_EMPTY;
        }

        // 追加行を追加リストへ追加
        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getDevId())) {
            if (!editBeanProperty.getOrgDevIdList().contains(editBeanProperty.getDevId())) {
                editBeanProperty.getAddDevIdList().add(editBeanProperty.getDevId());
            } else if (editBeanProperty.getDeleteDevIdList().contains(editBeanProperty.getDevId())) {
                editBeanProperty.getDeleteDevIdList().remove(editBeanProperty.getDevId());
            }
            editBeanProperty.setDevId("");
        }

        // 登録・更新処理
        UpdateSmsRelation();

        // 登録・更新後データ取得
        String corpId = this.editBeanProperty.getBuildingInfo().getCorpId();
        Long buildingId = this.editBeanProperty.getBuildingInfo().getBuildingId();
        String devId = null;
        List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpId, buildingId, devId);
        covertDisplay(relationList);

        if (!hasError()) {
            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        }

        return STR_EMPTY;
    }

    /**
     * 装置ID 追加リスト、削除リスト作成
     */
    private void createDevIdParam() {
        List<Object> addDevIdList = new ArrayList<>();
        List<Object> deleteDevIdList = new ArrayList<>();

        for (String devId : editBeanProperty.getDevIdList()) {
            if (!editBeanProperty.getOrgDevIdList().contains(devId)) {
                addDevIdList.add(devId);
				// 2024-01-31
                String corpIdTmp = smsConfigs.getConfig(SmsConstants.LTE_TMP_CORP_ID);
                Long buildingIdTmp = SmsConstants.LTE_TMP_BUILDING_ID;

                List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpIdTmp, buildingIdTmp, devId);
                // APIエラーの際はバリデーションエラーを表示しない
                if (!hasError()) {
	                if (!CollectionUtils.isEmpty(relationList)) {
	                    // 仮登録されているdevIdを削除
						List<Object> devIdList = new ArrayList<>();
						devIdList.add(devId);

				        UpdateSmsRelationResponse response = new UpdateSmsRelationResponse();
				        UpdateSmsRelationParameter parameter = new UpdateSmsRelationParameter();
				        UpdateSmsRelationRequest request = new UpdateSmsRelationRequest();

				        request.setCorpId(smsConfigs.getConfig(SmsConstants.LTE_TMP_CORP_ID));
				        request.setBuildingId(SmsConstants.LTE_TMP_BUILDING_ID);
				        request.setAddDevIdList(null);
				        request.setDeleteDevIdList(devIdList);

				        //リクエストパラメータセット
				        parameter.setBean("UpdateSmsRelationBean");
				        parameter.setRequest(request);

				        // APIアクセス
				        SmsApiGateway gateway = new SmsApiGateway();
				        response = (UpdateSmsRelationResponse) gateway.osolApiPost(
				                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
				                SmsApiGateway.PATH.JSON,
				                parameter,
				                response);
	                }
				}
				else {
                    invalidComponent.clear();
                }
				// ここまで
            }
        }

        for (String devId : editBeanProperty.getOrgDevIdList()) {
            if (!editBeanProperty.getDevIdList().contains(devId)) {
                deleteDevIdList.add(devId);
            }
        }

        editBeanProperty.setAddDevIdList(addDevIdList);
        editBeanProperty.setDeleteDevIdList(deleteDevIdList);


        // 2024-02-01
        for (Object devId : addDevIdList) {
        	if (devId.toString().startsWith(DEVICE_KIND.LTE.getVal())) {
		        MMeterPK retMMeterPK1 = new MMeterPK();
		        MMeter retMMeter1 = new MMeter();
		        retMMeterPK1.setDevId(devId.toString());
		        retMMeterPK1.setMeterMngId((long)1);
		        retMMeter1.setId(retMMeterPK1);
		        retMMeter1.setCommandFlg(SmsConstants.MMETER_COMMAND_FLG.DELETE.getVal());
		        retMMeter1.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
		        if (!mMeterDao.isNull(retMMeter1)) {
		        	retMMeter1 = mMeterDao.find(retMMeter1);
	            	retMMeter1.setCommandFlg(null);
	            	retMMeter1.setSrvEnt(null);
	            	mMeterDao.merge(retMMeter1);
		        }
		        MMeterPK retMMeterPK2 = new MMeterPK();
		        MMeter retMMeter2 = new MMeter();
		        retMMeterPK2.setDevId(devId.toString());
		        retMMeterPK2.setMeterMngId((long)2);
		        retMMeter2.setId(retMMeterPK2);
		        retMMeter2.setCommandFlg(SmsConstants.MMETER_COMMAND_FLG.DELETE.getVal());
		        retMMeter2.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
		        if (!mMeterDao.isNull(retMMeter2)) {
		        	retMMeter2 = mMeterDao.find(retMMeter2);
	            	retMMeter2.setCommandFlg(null);
	            	retMMeter2.setSrvEnt(null);
	            	mMeterDao.merge(retMMeter2);
		        }
	        }
        }
        // ここまで
    }

    /**
     * バリデーションチェック
     *
     * @return true:OK false:NG
     */
    private boolean validateInputParam() {
        boolean errorFlg = false;
        invalidComponent = new ArrayList<>();
        List<String> validateErrorMessageList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(editBeanProperty.getDevIdList())) {
            for (int i = 0; i < editBeanProperty.getDevIdList().size(); i++) {
                if (CheckUtility.isNullOrEmpty(editBeanProperty.getDevIdList().get(i))) {
                    // NULLチェック
                    eventLogger.debug(
                            EditBean.class.getPackage().getName()
                                    .concat(":: validateInputParam() error[dev_id_" + i + "]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessageFormat(
                                    "smsServerSettingBuildingDeviceEditBean.error.notInputDevId",
                                    new String[] { String.valueOf((i + 1)) }));
                    invalidComponent.add("smsServerSettingBuildingDeviceEditBean:dev_id_" + i);
                    errorFlg = true;
                } else if (!checkDevId(editBeanProperty.getDevIdList().get(i))) {
                    // 文字数、書式チェック
                    eventLogger.debug(
                            EditBean.class.getPackage().getName()
                                    .concat(":: validateInputParam() error[dev_id_" + i + "]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessageFormat(
                                    "smsServerSettingBuildingDeviceEditBean.error.notFormatDevId",
                                    new String[] { String.valueOf((i + 1)) }));
                    invalidComponent.add("smsServerSettingBuildingDeviceEditBean:dev_id_" + i);
                    errorFlg = true;
                } else {
                    // 存在チェック
                    GetSmsDeviceResult result = findDevice(editBeanProperty.getDevIdList().get(i));
                    // APIエラーの際はバリデーションエラーを表示しない
                    if (hasError()) {
                        invalidComponent.clear();
                        errorFlg = true;
                        return !errorFlg;
                    }

                    if (Objects.isNull(result)) {
                        eventLogger.debug(
                                EditBean.class.getPackage().getName()
                                        .concat(":: validateInputParam() error[dev_id_" + i + "]"));
                        validateErrorMessageList.add(
                                beanMessages.getMessageFormat(
                                        "smsServerSettingBuildingDeviceEditBean.error.notExistDevId",
                                        new String[] { String.valueOf((i + 1)) }));
                        invalidComponent.add("smsServerSettingBuildingDeviceEditBean:dev_id_" + i);
                        errorFlg = true;
                    } else if (editBeanProperty.getAddDevIdList()
                            .contains(editBeanProperty.getDevIdList().get(i))) {
                        // 重複チェック
                        String corpId = null;
                        Long buildingId = null;
                        String devId = editBeanProperty.getDevIdList().get(i);
                        List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpId, buildingId,
                                devId);
                        // APIエラーの際はバリデーションエラーを表示しない
                        if (hasError()) {
                            invalidComponent.clear();
                            errorFlg = true;
                            return !errorFlg;
                        }

                        if (!editBeanProperty.getOrgDevIdList().contains(devId)
                                && !CollectionUtils.isEmpty(relationList)) {
                            // 他の建物に登録されている
                            eventLogger.debug(
                                    EditBean.class.getPackage().getName()
                                            .concat(":: validateInputParam() error[dev_id_" + i + "]"));
                            validateErrorMessageList.add(
                                    beanMessages.getMessageFormat(
                                            "smsServerSettingBuildingDeviceEditBean.error.otherBuildingDevId",
                                            new String[] { String.valueOf((i + 1)), devId }));
                            invalidComponent.add("smsServerSettingBuildingDeviceEditBean:dev_id_" + i);
                            errorFlg = true;
                        }
                    }
                }
            }
            List<String> dupChecklist = new ArrayList<>(editBeanProperty.getDevIdList());
            dupChecklist.removeAll(Collections.singleton(null));
            if (!CollectionUtils.isEmpty(dupChecklist)) {
                boolean duplicationError = dupChecklist.stream()
                        .collect(Collectors.groupingBy(x -> x, Collectors.counting())).values().stream()
                        .filter(e -> e > 1)
                        .collect(Collectors.counting()) > 0;
                if (duplicationError) {
                    validateErrorMessageList.add(
                            beanMessages.getMessage(
                                    "smsServerSettingBuildingDeviceEditBean.error.inputDuplicationDevId"));
                    errorFlg = true;
                }
            }
        }

        if (!CheckUtility.isNullOrEmpty(editBeanProperty.getDevId())) {
            if (!checkDevId(editBeanProperty.getDevId())) {
                // 文字数、書式チェック
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[addDevId]"));
                validateErrorMessageList.add(
                        beanMessages.getMessageFormat("smsServerSettingBuildingDeviceEditBean.error.notFormatDevId",
                                new String[] { "追加" }));
                invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                errorFlg = true;
            } else {
                // 存在チェック
                GetSmsDeviceResult result = findDevice(editBeanProperty.getDevId());
                // APIエラーの際はバリデーションエラーを表示しない
                if (hasError()) {
                    invalidComponent.clear();
                    errorFlg = true;
                    return !errorFlg;
                }

                if (Objects.isNull(result)) {
                    eventLogger.debug(
                            EditBean.class.getPackage().getName().concat(":: validateInputParam() error[addDevId]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessageFormat("smsServerSettingBuildingDeviceEditBean.error.notExistDevId",
                                    new String[] { "追加" }));
                    invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                    errorFlg = true;
                } else {
                    // 重複チェック
                    String corpId = null;
                    Long buildingId = null;
                    String devId = editBeanProperty.getDevId();
                    List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpId, buildingId, devId);
                    // APIエラーの際はバリデーションエラーを表示しない
                    if (hasError()) {
                        invalidComponent.clear();
                        errorFlg = true;
                        return !errorFlg;
                    }

                    if (editBeanProperty.getDevIdList().contains(devId)) {
                        // 入力されているリストに同じ装置IDがある
                        eventLogger.debug(
                                EditBean.class.getPackage().getName()
                                        .concat(":: validateInputParam() error[addDevId]"));
                        validateErrorMessageList.add(
                                beanMessages.getMessageFormat(
                                        "smsServerSettingBuildingDeviceEditBean.error.duplicationDevId",
                                        new String[] { "追加", editBeanProperty.getDevId() }));
                        invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                        errorFlg = true;
                    } else if (!editBeanProperty.getOrgDevIdList().contains(devId)
                            && !CollectionUtils.isEmpty(relationList)) {
                        // 他の建物に登録されている
                        eventLogger.debug(
                                EditBean.class.getPackage().getName()
                                        .concat(":: validateInputParam() error[addDevId]"));
                        validateErrorMessageList.add(
                                beanMessages.getMessageFormat(
                                        "smsServerSettingBuildingDeviceEditBean.error.otherBuildingDevId",
                                        new String[] { "追加", editBeanProperty.getDevId() }));
                        invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                        errorFlg = true;
                    }
                }
            }
        }

        // APIエラーがなければバリデーションエラーを追加する
        for (String msg : validateErrorMessageList) {
            addErrorMessage(msg);
        }

        return !errorFlg;

    }

    /**
     * 装置情報 取得API
     *
     * @param devId
     * @return
     */
    private GetSmsDeviceResult findDevice(String devId) {

        // 装置取得
        GetSmsDeviceResponse response = new GetSmsDeviceResponse();
        GetSmsDeviceParameter parameter = new GetSmsDeviceParameter();

        //リクエストパラメータセット
        parameter.setBean("GetSmsDeviceBean");
        parameter.setDevId(devId);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (GetSmsDeviceResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        GetSmsDeviceResult result = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            result = response.getResult();
        }

        return result;
    }

    /**
     * 建物、装置関連 取得API
     *
     * @param corpId
     * @param buildingId
     * @param devId
     * @return
     */
    private List<ListSmsDevRelationDetailResultData> listDevRelation(String corpId, Long buildingId, String devId) {

        // 建物、装置関連 取得
        ListSmsDevRelationResponse response = new ListSmsDevRelationResponse();
        ListSmsDevRelationParameter parameter = new ListSmsDevRelationParameter();

        //リクエストパラメータセット
        parameter.setBean("ListSmsDevRelationBean");
        parameter.setCorpId(corpId);
        parameter.setBuildingId(buildingId);
        parameter.setDevId(devId);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsDevRelationResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        List<ListSmsDevRelationDetailResultData> list = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            list = response.getResult().getDetailList();
        }

        return list;
    }

    /**
     * 建物、装置関連 登録・更新API
     *
     * @return
     */
    private UpdateSmsRelationResult UpdateSmsRelation() {

        // 装置取得
        UpdateSmsRelationResponse response = new UpdateSmsRelationResponse();
        UpdateSmsRelationParameter parameter = new UpdateSmsRelationParameter();
        UpdateSmsRelationRequest request = new UpdateSmsRelationRequest();

        request.setCorpId(editBeanProperty.getBuildingInfo().getCorpId());
        request.setBuildingId(editBeanProperty.getBuildingInfo().getBuildingId());
        request.setAddDevIdList(editBeanProperty.getAddDevIdList());
        request.setDeleteDevIdList(editBeanProperty.getDeleteDevIdList());

        //リクエストパラメータセット
        parameter.setBean("UpdateSmsRelationBean");
        parameter.setRequest(request);

        // APIアクセス
        SmsApiGateway gateway = new SmsApiGateway();
        response = (UpdateSmsRelationResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        UpdateSmsRelationResult result = null;

        if (Objects.isNull(response)) {
            addErrorMessage(
                    beanMessages.getMessage("api.response.null"));
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
        } else if (Objects.nonNull(response.getResult())) {
            result = response.getResult();
        }

        return result;
    }

    /**
     * 装置ID 追加
     */
    @Logged
    public void addDevId() {
        invalidComponent = new ArrayList<>();
        List<String> validateErrorMessageList = new ArrayList<>();
        editBeanProperty.setAddDevIdFlg(false);

        if (CheckUtility.isNullOrEmpty(editBeanProperty.getDevId())) {
            // NULLチェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[addDevId]"));
            validateErrorMessageList.add(
                    beanMessages.getMessageFormat("smsServerSettingBuildingDeviceEditBean.error.notInputDevId",
                            new String[] { "追加" }));
            invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
        } else if (!checkDevId(editBeanProperty.getDevId())) {
            // 文字数、書式チェック
            eventLogger.debug(
                    EditBean.class.getPackage().getName().concat(":: validateInputParam() error[addDevId]"));
            validateErrorMessageList.add(
                    beanMessages.getMessageFormat("smsServerSettingBuildingDeviceEditBean.error.notFormatDevId",
                            new String[] { "追加" }));
            invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
        } else {
            // 存在チェック
            GetSmsDeviceResult result = findDevice(editBeanProperty.getDevId());
            // APIエラーの際はバリデーションエラーを表示しない
            if (hasError()) {
                invalidComponent.clear();
                return;
            }

            if (Objects.isNull(result)) {
                eventLogger.debug(
                        EditBean.class.getPackage().getName().concat(":: validateInputParam() error[addDevId]"));
                validateErrorMessageList.add(
                        beanMessages.getMessageFormat("smsServerSettingBuildingDeviceEditBean.error.notExistDevId",
                                new String[] { "追加" }));
                invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
            } else {
                // 重複チェック
                String corpId = null;
                Long buildingId = null;
                String devId = editBeanProperty.getDevId();
                List<ListSmsDevRelationDetailResultData> relationList = listDevRelation(corpId, buildingId, devId);
                // APIエラーの際はバリデーションエラーを表示しない
                if (hasError()) {
                    invalidComponent.clear();
                    return;
                }

                if (editBeanProperty.getDevIdList().contains(devId)) {
                    // 入力されているリストに同じ装置IDがある
                    eventLogger.debug(
                            EditBean.class.getPackage().getName()
                                    .concat(":: validateInputParam() error[addDevId]"));
                    validateErrorMessageList.add(
                            beanMessages.getMessageFormat(
                                    "smsServerSettingBuildingDeviceEditBean.error.duplicationDevId",
                                    new String[] { "追加", editBeanProperty.getDevId() }));
                    invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                } else if (!editBeanProperty.getOrgDevIdList().contains(devId)
                        && !CollectionUtils.isEmpty(relationList)) {
                    // 他の建物に登録されている
                    // 2024-01-31
                    boolean isTempEntry = false;
                    for (ListSmsDevRelationDetailResultData checker : relationList) {
                        if (checker.getCorpId().equals(smsConfigs.getConfig(SmsConstants.LTE_TMP_CORP_ID)) && checker.getBuildingId()==SmsConstants.LTE_TMP_BUILDING_ID) {
                            isTempEntry = true;
                        }
                    }
                    if (!isTempEntry) {
                    // ここまで
                        eventLogger.debug(
                                EditBean.class.getPackage().getName()
                                        .concat(":: validateInputParam() error[addDevId]"));
                        validateErrorMessageList.add(
                                beanMessages.getMessageFormat(
                                        "smsServerSettingBuildingDeviceEditBean.error.otherBuildingDevId",
                                        new String[] { "追加", editBeanProperty.getDevId() }));
                        invalidComponent.add("smsServerSettingBuildingDeviceEditBean:addDevId");
                    }   // 2024-01-31
                }
            }
        }

        // APIエラーがなければバリデーションエラーを追加する
        if (!CollectionUtils.isEmpty(validateErrorMessageList)) {
            for (String msg : validateErrorMessageList) {
                addErrorMessage(msg);
            }
            return;
        }

        //編集行に追加
        editBeanProperty.getDevIdList().add(editBeanProperty.getDevId());

        //追加行を初期化
        editBeanProperty.setDevId(STR_EMPTY);
        editBeanProperty.setAddDevIdFlg(true);
    }

    /**
     * 装置ID 削除
     */
    @Logged
    public void deleteDevId(int index) {
        editBeanProperty.getDevIdList().remove(index);
    }

    /**
     * 装置ID 詳細項目表示フラグ
     */
    public boolean dispDetailDevIdFlg() {
        return !CollectionUtils.isEmpty(editBeanProperty.getDevIdList());
    }

    /**
     * 装置ID 追加項目表示フラグ
     */
    public boolean dispAddDevIdFlg() {
        return editBeanProperty.getDevIdList().size() < SmsConstants.MAX_DEV_COUNT;
    }

    /**
     * 装置 装置IDのチェック
     *
     * @param devId
     * @return true:OK false:NG
     */
    private boolean checkDevId(String devId) {
        String regrexStr = "^[A-Za-z0-9]{1,8}$";
        Pattern p = Pattern.compile(regrexStr);
        Matcher m = p.matcher(devId);
        return m.matches();
    }

    /**
     * エラーの有無チェック
     * @return
     */
    public boolean hasError() {
        List<FacesMessage> messageList = getMessageList();
        if (messageList == null || messageList.isEmpty()) {
            return false;
        }
        for (FacesMessage message : messageList) {
            if (message.getSeverity().equals(FacesMessage.SEVERITY_FATAL)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_ERROR)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_WARN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * デザイン指定
     */
    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    /**
     * 確認ダイアログ(登録)表示メッセージの取得
     *
     * @return
     */
    public String getDialogMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    /**
     * 画面用データ
     *
     * @return
     */
    public EditBeanProperty getEditBeanProperty() {
        return editBeanProperty;
    }

}
