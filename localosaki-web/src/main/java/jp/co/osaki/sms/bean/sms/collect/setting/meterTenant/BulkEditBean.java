package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.ListSmsMeterTenantBelongTenantsResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterTenant.UpdateBulkSmsMeterTenantBaseInfoResponse;
import jp.co.osaki.osol.api.resultdata.sms.meterTenant.SearchTenantSmsResultData;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.web.csv.sms.converter.MeterTenantManagementCsvConverter;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsFileUpload;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.sms.collect.TopBean;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MeterTenantDao;

@Named("smsCollectSettingMeterTenantBulkEditBean")
@ConversationScoped
public class BulkEditBean extends SmsConversationBean implements Serializable {

    // シリアライズID
    private static final long serialVersionUID = 532971684871526260L;

    //infoメッセージリスト
    private List<String> infoMessages = new ArrayList<>();

    //errorメッセージリスト
    private List<String> errorMessages = new ArrayList<>();

    //所属企業ID
    private String divisionCorpId;

    //所属企業名
    private String divisionCorpName;

    //所属建物ID
    private Long divisionBuildingId;

    //所属建物No
    private String divisionBuildingNo;

    //所属建物名
    private String divisionBuildingName;

    //ダウンロードファイル名
    private String saveFileName;

    //ダウンロードファイルパス
    private String downloadFilePath;

    //アップロードファイル
    private Part uploadFile;

    //アップロードファイルパス
    private String uploadFilePath;

    //アップロードファイル名
    private String uploadFileName;

    //アップロードCSV コメント行数
    private final int CSV_COMMENT_ROW = 5;

    //登録済建物リスト
    private List<SearchTenantSmsResultData> existBuildingList;

    //ユーザーコードリスト
    private List<Long> userCodeList;

    // 都道府県マップ
    private Map<String, String> prefectureMap;

    // 電気料金メニュー
    private Map<String, String> priceMenuMap;

    //
    List<List<BulkErrorInfo>> errorList = new ArrayList<>();

    @Inject
    private SmsMessages beanMessages;

    @Inject
    private SmsFileDownload fileDownloader;

    @Inject
    private SmsFileUpload fileUploader;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TopBean topBean;

    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private OsolAccessBean osolAccessBean;

    // メーターテナント検索クラス
    @EJB
    private MeterTenantDao meterTenantDao;

    @Override
    public String init() {
        conversationStart();

        this.uploadFileName = null;
        this.uploadFile = null;
        this.errorList.clear();

        this.divisionCorpId = topBean.getTopBeanProperty().getListInfo().getCorpId();
        this.divisionCorpName = topBean.getTopBeanProperty().getListInfo().getCorpName();

        this.divisionBuildingId = Long.valueOf(topBean.getTopBeanProperty().getListInfo().getBuildingId());
        this.divisionBuildingName = topBean.getTopBeanProperty().getListInfo().getBuildingName();
        this.divisionBuildingNo = topBean.getTopBeanProperty().getListInfo().getBuildingNo();


        return "meterTenantBulkEdit";
    }

    /**
     * ダウンロードボタン押下時の処理<br>
     * CSVファイルの準備
     */
    @Logged
    public void download() {
        String fileName = "MeterTenantTemplate.csv";
        saveFileName = fileName;
        // 出力データ取得
        List<List<String>> templateList = createCsvTemplate();
        String outputDir = getCsvOutputDir();
        downloadFilePath = Paths.get(outputDir, fileName).toString();
        MeterTenantManagementCsvConverter csvConverter = new MeterTenantManagementCsvConverter();
        csvConverter.csvPrint(outputDir, fileName, templateList);

    }

    /**
     * データテンプレートの中身を作る
     * @return データテンプレートとしてCSV出力するファイルの中身
     */
    private List<List<String>> createCsvTemplate() {
        List<List<String>> retList = new ArrayList<>();
        retList.add(
                new ArrayList<>(Arrays.asList("# メーターテナントデータは、以下の順番になります。")));
        retList.add(
        		new ArrayList<>(Arrays.asList("# ユーザーコード(入力必須)","テナント番号","テナント名","テナント名かな","テナント短縮名称","郵便番号","都道府県","住所","住所(建物名)","電話番号","FAX番号","備考","集計開始年月","集計終了年月","公開可否","固定費1名称","固定費1金額","固定費2名称","固定費2金額","固定費3名称","固定費3金額","固定費4名称","固定費4金額","電気料金メニュー(入力必須)","按分率1","按分率2","按分率3","按分率4","按分率5","按分率6","按分率7","按分率8","按分率9","按分率10")));
        retList.add(
        		new ArrayList<>(Arrays.asList("# 1-999999(半角数字)","最大50文字","最大50文字","最大100文字 ひらがなのみ","最大5文字","半角数字3桁-半角数字4桁","都・道・府・県まで記載","最大50文字","最大50文字","最大50文字","最大50文字","最大500文字","YYYY/MM","YYYY/MM、集計年月の開始日 < 終了日","0:未公開、1:公開(契約企業権限の場合は設定有無に関わらず反映されません。)","最大20文字","1-999999(半角数字)","最大20文字","1-999999(半角数字)","最大20文字","1-999999(半角数字)","最大20文字","1-999999(半角数字)","0:--、1:従量電灯A、2:従量電灯B","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)","0-100(半角数字)")));
        retList.add(
        		new ArrayList<>(Arrays.asList("# (例) 10001","tenant_no01","テナントナンバー01","てなんとなんばー","テナント1","123-4567","東京都","××市△△町1-1-1","□□ビル 3F","311111111","411111111","備考です。","2020/01","2020/12","0","固定費1","100","固定費2","200","固定費3","300","固定費4","400","1","10","20","30","40","50","60","70","80","90","100")));
        retList.add(
                new ArrayList<>(Arrays.asList("# この行の下にメーターデータを追加してください。")));

        return retList;
    }

    /**
     * 実ダウンロード処理
     *
     * @return
     */
    public String downloadStart() {
        fileDownloader.fileDownload(downloadFilePath, saveFileName);
        return STR_EMPTY;
    }

    // アップロード処理詳細ここから
    /**
     * ファイルアップロード処理
     *
     * @return outcome
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InvalidFormatException
     */
    @Logged
    public String execFileUpload() throws IOException, FileNotFoundException, InvalidFormatException {
        // バリデートチェック(falseの場合処理終了)
        if (!validateUploadFile(uploadFile)) {
            uploadFileName = null;
            uploadFile = null;
            return "";
        }

        //ファイルのアップロード
        uploadFilePath = fileUploader.tenantBulkTempFileUpload(uploadFile, getLoginUserId());

        //アップロードしたファイルの内容を取得
        MeterTenantManagementCsvConverter csvConverter = new MeterTenantManagementCsvConverter();
        List<List<String>> uploadFileDataList;
        try {
            uploadFileDataList = csvConverter.csvParse(uploadFilePath, "MS932");
        } catch (IOException e) {
            uploadFileName = null;
            uploadFile = null;
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.templateNotExist"));
            return "";
        }

        //アップロードしたファイルの件数確認
        if (uploadFileDataList.size() < CSV_COMMENT_ROW) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.csvFormatWrong"));
            return "";
        }

        List<List<String>> uploadTargetDataList = uploadFileDataList.subList(CSV_COMMENT_ROW,uploadFileDataList.size());
        if (uploadTargetDataList.isEmpty()) {
            addErrorMessage(
                    beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.recodeNotFound"));
            return "";
        }

        //ユーザーコードリスト取得処理
        ListSmsMeterTenantBelongTenantsParameter parameter = new ListSmsMeterTenantBelongTenantsParameter();
        parameter.setBean("ListSmsMeterTenantBelongTenants");
        parameter.setCorpId(this.divisionCorpId);
        parameter.setBuildingId(this.divisionBuildingId);

        ListSmsMeterTenantBelongTenantsResponse response = new ListSmsMeterTenantBelongTenantsResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        response = (ListSmsMeterTenantBelongTenantsResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);
        if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            uploadFileName = null;
            return "";
        }

        //ユーザーコードリスト、テナント番号リスト作成
        List<String> tenantNoList = new ArrayList<>();
        if (response.getResult() != null && response.getResult().getResultDataList() != null) {
            this.existBuildingList = response.getResult().getResultDataList();
            this.userCodeList = this.existBuildingList.stream()
                    .map(x -> x.getTenantId())
                    .collect(Collectors.toList());

            tenantNoList = this.existBuildingList.stream()
                    .map(x -> x.getBuildingNo())
                    .collect(Collectors.toList());
        }

        //都道府県コード取得
        prefectureMap = toolsPullDownList.getPrefecture(false, null);
        //電気料金メニュー取得
        priceMenuMap = toolsPullDownList.getPriceMenu(true, "-");
        priceMenuMap.replace("-", "0");

        //アップロードしたファイルを順に処理する
        this.errorList = new ArrayList<>();
        for (List<String> record : uploadTargetDataList) {
            boolean editFlg = checkEditMode(record);
            //ユーザーコードが無い場合は新規、ある場合は更新でバリデーション実行
            List<BulkErrorInfo> errorValueList = createValidationErrorList(record, editFlg, tenantNoList);

            //バリデーションにエラーがある場合、登録処理を実行
            if (errorValueList.stream().anyMatch(x -> x.getError().isPresent())) {
                //エラーリストに登録
                this.errorList.add(errorValueList);
            }
        }

        //エラーが無い場合、登録処理をする
        if (errorList.isEmpty()) {
            //パラメーター生成
            UpdateBulkSmsMeterTenantBaseInfoParameter updateParameter = createParameter(uploadTargetDataList);

            UpdateBulkSmsMeterTenantBaseInfoResponse updateResponse = new UpdateBulkSmsMeterTenantBaseInfoResponse();
            gateway = new SmsApiGateway();
            updateResponse = (UpdateBulkSmsMeterTenantBaseInfoResponse) gateway.osolApiPost(
                    osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                    SmsApiGateway.PATH.JSON,
                    updateParameter,
                    updateResponse);
            if (!OsolApiResultCode.API_OK.equals(updateResponse.getResultCode())) {
                addErrorMessage(
                        beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(updateResponse.getResultCode())));
                uploadFileName = null;
                return "";
            }
            addMessage(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.uploadSuccess"));
        } else {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.uploadFaild"));
        }

        fileUploader.tenantBulkTempDirDelete(getLoginUserId());
        uploadFileName = null;
        return "";
    }

    /**
     * 新規/更新チェック
     *
     * @param record 対象レコード
     * @return
     */
    private boolean checkEditMode(List<String> record) {
        //変更フラグ
        boolean editFlg = false;
        if (record.size() < BulkCsvIndex.USER_CODE.getIndex() + 1) {
            return editFlg;
        }
        //ユーザーコード有無確認
        if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.USER_CODE.getIndex()))
                && NumberUtils.isNumber(record.get(BulkCsvIndex.USER_CODE.getIndex()))) {
            //ユーザーコードがリストにあるか確認
            if (this.userCodeList.contains(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex())))) {
                editFlg = true;
            } else {
                //新規登録の場合、ユーザーコードリストに追加する
                this.userCodeList.add(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex())));
            }
        }
        return editFlg;
    }

    /**
     * アップロード時のバリデーション
     *
     * @param uploadFile
     * @return
     */
    private boolean validateUploadFile(Part uploadFile) {

        if (uploadFile == null || CheckUtility.isNullOrEmpty(uploadFileName)) {
            addErrorMessage(beanMessages.getMessage("energyInputYearSearchBean.error.notInputFile"));
            return false;
        }

        String extension = fileUploader.getFileName(uploadFile).substring(
                fileUploader.getFileName(uploadFile).lastIndexOf(".") + 1, fileUploader.getFileName(uploadFile).length());
        if (CheckUtility.isNullOrEmpty(extension) || !extension.equals("csv")) {
            addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.notInputFileType"));
            return false;
        }
        return true;
    }

    /**
     * 登録時のバリデーション
     *
     * @param uploadFile
     * @return
     */
    private List<BulkErrorInfo> createValidationErrorList(List<String> valueList, boolean updateFlg, List<String> tenantNoList) {
        List<BulkErrorInfo> errorValueList = new ArrayList<>();

        //CSVの項目数が違う場合は一行全てエラーとする
        if (valueList.size() != BulkCsvIndex.values().length) {
            String errorMsg = beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.csvFormatWrong");
            for (int i = 0; i < BulkCsvIndex.values().length; i++) {
                //値がある場合は入力、無い場合は空文字を入れる
                if (i < valueList.size()) {
                    errorValueList.add(new BulkErrorInfo(errorMsg, valueList.get(i)));
                } else {
                    errorValueList.add(new BulkErrorInfo(errorMsg, ""));
                }
            }
            return errorValueList;
        }

        //ユーザーコードバリデーション
        errorValueList.add(checkUserCode(valueList.get(BulkCsvIndex.USER_CODE.getIndex())));

        //テナント番号バリデーション
        String altTenantNo = divisionBuildingNo + "_" + valueList.get(BulkCsvIndex.USER_CODE.getIndex());
        errorValueList.add(checkTenantNo(valueList.get(BulkCsvIndex.NO.getIndex()), updateFlg, tenantNoList, altTenantNo));

        //テナント名
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.NAME.getIndex()), "テナント名", 50));

        //テナント名かな
        errorValueList.add(checkTenantKana(valueList.get(BulkCsvIndex.KANA.getIndex())));

        //テナント短縮名
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.CONTRACTION.getIndex()), "テナント短縮名称", 5));

        //郵便番号
        errorValueList.add(checkZipCode(valueList.get(BulkCsvIndex.ZIP_CODE.getIndex())));

        //都道府県
        errorValueList.add(checkPrefecture(valueList.get(BulkCsvIndex.PREFECTURE.getIndex())));

        //住所
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.ADDRESS.getIndex()), "住所", 50));

        //住所(建物名)
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.ADDR_BLDG.getIndex()), "住所(建物名)", 50));

        //電話番号
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.TEL_NUM.getIndex()), "電話番号", 50));

        //FAX番号
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.FAX_NUM.getIndex()), "FAX番号", 50));

        //備考
        errorValueList.add(checkBiko(valueList.get(BulkCsvIndex.BIKO.getIndex())));

        //集計開始年月,集計終了年月
        errorValueList.addAll(checkTotalYm(valueList.get(BulkCsvIndex.START_YM.getIndex()), valueList.get(BulkCsvIndex.END_YM.getIndex())));

        //公開可否
        errorValueList.add(checkPublicFlg(valueList.get(BulkCsvIndex.PUBLIC_FLG.getIndex())));

        //固定費1名称
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.FIXED1_NAME.getIndex()), "固定費1名称", 20));

        //固定費1金額
        errorValueList.add(checkFixed(valueList.get(BulkCsvIndex.FIXED1_PRICE.getIndex()), "固定費1金額"));

        //固定費2名称
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.FIXED2_NAME.getIndex()), "固定費2名称", 20));

        //固定費2金額
        errorValueList.add(checkFixed(valueList.get(BulkCsvIndex.FIXED2_PRICE.getIndex()), "固定費2金額"));

        //固定費3名称
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.FIXED3_NAME.getIndex()), "固定費3名称", 20));

        //固定費3金額
        errorValueList.add(checkFixed(valueList.get(BulkCsvIndex.FIXED3_PRICE.getIndex()), "固定費3金額"));

        //固定費4名称
        errorValueList.add(checkStringLength(valueList.get(BulkCsvIndex.FIXED4_NAME.getIndex()), "固定費4名称", 20));

        //固定費4金額
        errorValueList.add(checkFixed(valueList.get(BulkCsvIndex.FIXED4_PRICE.getIndex()), "固定費4金額"));

        //電気料金メニュー
        errorValueList.add(checkPriceMenuNo(valueList.get(BulkCsvIndex.PRICE_MENU.getIndex())));

        //按分率1
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE1.getIndex()), "按分率1"));

        //按分率2
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE2.getIndex()), "按分率2"));

        //按分率3
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE3.getIndex()), "按分率3"));

        //按分率4
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE4.getIndex()), "按分率4"));

        //按分率5
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE5.getIndex()), "按分率5"));

        //按分率6
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE6.getIndex()), "按分率6"));

        //按分率7
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE7.getIndex()), "按分率7"));

        //按分率8
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE8.getIndex()), "按分率8"));

        //按分率9
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE9.getIndex()), "按分率9"));

        //按分率10
        errorValueList.add(checkDivRate(valueList.get(BulkCsvIndex.DIV_RATE10.getIndex()), "按分率10"));

        return errorValueList;
    }

    /**
     * ユーザーコードチェック処理
     * @param userCode ユーザーコード
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkUserCode(String userCode) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), userCode == null ? "　" : userCode);
        if (CheckUtility.isNullOrEmpty(userCode)) {
            errorInfo.setStringError(
                    "ユーザーコード" + beanMessages.getMessage("osol.error.required"));
            return errorInfo;

        }

        //ユーザーコード 範囲チェック
        if (!CheckUtility.checkLongRange(userCode, 1L, 999999L)) {
            errorInfo.setStringError(
                    beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                            new String[] { "ユーザーコード", "1", "999999" }));
            return errorInfo;
        }

        return errorInfo;
    }

    /**
     * テナント番号チェック処理
     * @param tenantNo テナント番号
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkTenantNo(String tenantNo, boolean updateFlg, List<String> tenantNoList, String altTenantNo) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), tenantNo == null ? "" : tenantNo);
        //テナント番号 更新の場合、入力されていればエラー
        if (updateFlg) {
            if (!CheckUtility.isNullOrEmpty(tenantNo)) {
                errorInfo.setStringError(
                        beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.tenantNoInput"));
                return errorInfo;

            }
        } else {
            //新規で未入力の場合は親建物番号_ユーザーコード
            if (CheckUtility.isNullOrEmpty(tenantNo)) {
                tenantNo = altTenantNo;
            }

            //文字数チェック
            if (tenantNo.length() > 50) {
                errorInfo.setStringError(
                        beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.StringLengthTooLong",
                                new String[] {"テナント番号", "50"}));
                return errorInfo;
            }

            //新規の場合、重複があればエラー
            if (tenantNoList.contains(tenantNo)) {
                errorInfo.setStringError(
                        beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.registered",
                                new String[] {"テナント番号"})
                        );
                return errorInfo;
            }

            //新規の場合、tenantNoListに追加する
            tenantNoList.add(tenantNo);
        }

        return errorInfo;
    }

    /**
     * テナント名かなチェック処理
     * @param tenantKana テナント名かな
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkTenantKana(String tenantKana) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), tenantKana == null ? "" : tenantKana);
        if (!CheckUtility.isNullOrEmpty(tenantKana)) {
            //文字数チェック
            if (tenantKana.length() > 100) {
                errorInfo.setStringError(
                        beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.StringLengthTooLong",
                                new String[] {"テナント名かな", "100"}));
                return errorInfo;
            }

            if(!CheckUtility.checkFurigana(tenantKana)) {
                errorInfo.setStringError(beanMessages.getMessage("osol.error.furiganaValidation"));
                return errorInfo;
            }
        }

        return errorInfo;
    }

    /**
     * 郵便番号チェック処理
     * @param zipCode 郵便番号
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkZipCode(String zipCode) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), zipCode == null ? "" : zipCode);
        if (!CheckUtility.isNullOrEmpty(zipCode)
                && !CheckUtility.checkZipCode(zipCode)) {
            errorInfo.setStringError(beanMessages.getMessage("osol.error.zipValueValidation"));
        }
        return errorInfo;
    }

    /**
     * 都道府県チェック処理
     * @param prefecture 都道府県
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkPrefecture(String prefecture) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), prefecture == null ? "" : prefecture);

        if (!CheckUtility.isNullOrEmpty(prefecture)
                && !prefectureMap.containsKey(prefecture)) {
            errorInfo.setStringError(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.wrongPrefectureCd"));
        }
        return errorInfo;
    }

    /**
     * 備考チェック処理
     * @param biko 備考
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkBiko(String biko) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), biko == null ? "" : biko);

        if (!CheckUtility.isNullOrEmpty(biko)
                && biko.length() > 500) {
            errorInfo.setStringError(beanMessages.getMessage("buildingInfoEditBean.error.bikoValidation"));
        }
        return errorInfo;
    }

    /**
     * 集計開始/終了年月チェック処理
     * @param startYm 集計開始年月
     * @param endYm 集計終了年月
     * @return errorInfoList エラー情報リスト
     */
    private List<BulkErrorInfo> checkTotalYm(String startYm, String endYm) {
        BulkErrorInfo startErrorInfo = new BulkErrorInfo(Optional.empty(), startYm == null ? "" : startYm);
        BulkErrorInfo endErrorInfo = new BulkErrorInfo(Optional.empty(), endYm == null ? "" : endYm);
        List<BulkErrorInfo> errorInfoList = new ArrayList<>();
        errorInfoList.add(startErrorInfo);
        errorInfoList.add(endErrorInfo);

        boolean errFlg = false;
         // 集計開始年月の日付チェック
        if (!CheckUtility.isNullOrEmpty(startYm)
                && !DateUtility.checkRegDateYmSlash(startYm)) {
            startErrorInfo.setStringError(beanMessages.getMessage("buildingInfoEditBean.error.totalStartYmValidation"));
            errFlg = true;
        }

        // 集計終了年月の日付チェック
        if (!CheckUtility.isNullOrEmpty(endYm)
                && !DateUtility.checkRegDateYmSlash(endYm)) {
            endErrorInfo.setStringError(beanMessages.getMessage("buildingInfoEditBean.error.totalEndYmValidation"));
            errFlg = true;
        }

        if (errFlg) {
            return errorInfoList;
        }
        if (!CheckUtility.isNullOrEmpty(startYm) && !CheckUtility.isNullOrEmpty(endYm)) {
            // 両項目入力時は集計開始年月 <= 集計終了年月であること
            Date strStartDate = DateUtility.conversionDate(startYm,
                    DateUtility.DATE_FORMAT_YYYYMM_SLASH);
            Date strEndDate = DateUtility.conversionDate(endYm,
                    DateUtility.DATE_FORMAT_YYYYMM_SLASH);
            eventLogger.debug(EditBean.class.getPackage().getName()
                    .concat(" strStartDate:" + strStartDate.toString() + " / strEndDate:" + strEndDate.toString()));

            int diff = strEndDate.compareTo(strStartDate);
            eventLogger.debug(EditBean.class.getPackage().getName().concat(" diff(" + diff + ")"));
            if (diff < 0) {
                startErrorInfo.setStringError(beanMessages.getMessage("buildingInfoEditBean.error.illegalRangeTotalEndYm"));
                endErrorInfo.setStringError(beanMessages.getMessage("buildingInfoEditBean.error.illegalRangeTotalEndYm"));
            }
        }
        return errorInfoList;
    }

    /**
     * 公開可否チェック処理
     * @param  公開可否
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkPublicFlg(String publicFlg) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), publicFlg == null ? "" : publicFlg);
        //0:公開 1:未公開
        List<String> optList = new ArrayList<String>(Arrays.asList("0", "1"));
        //公開可否値チェック
        if (!CheckUtility.isNullOrEmpty(publicFlg) && !optList.contains(publicFlg)) {
            errorInfo.setStringError(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.wrongOptionNo"));
        }

        return errorInfo;
    }

    /**
     * 固定費チェック処理
     * @param fixed 固定費
     * @param name 項目名
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkFixed(String fixed, String name) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), fixed == null ? "" : fixed);

        //固定値数値チェック
        if (!CheckUtility.isNullOrEmpty(fixed)) {
            //固定値 範囲チェック
            if (!CheckUtility.checkLongRange(fixed, 1L, 999999L)) {
                errorInfo.setStringError(
                        beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                                new String[] { name, "1", "999999" }));
                return errorInfo;
            }
        }
        return errorInfo;
    }

    /**
     * 電気料金メニューチェック処理
     * @param priceMenuNo 電気料金メニュー
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkPriceMenuNo(String priceMenuNo) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), priceMenuNo == null ? "" : priceMenuNo);

        //電気料金メニューチェック
        if (CheckUtility.isNullOrEmpty(priceMenuNo)) {
            errorInfo.setStringError("電気料金メニュー" + beanMessages.getMessage("osol.error.required"));
            return errorInfo;
        }

        if (!priceMenuMap.containsValue(priceMenuNo)) {
            errorInfo.setStringError(beanMessages.getMessage("smsCollectSettingMeterTenantEditBean.error.wrongOptionNo"));
            return errorInfo;
        }
        return errorInfo;
    }

    /**
     * 按分率チェック処理
     * @param divRate 按分率
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkDivRate(String divRate, String name) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), divRate == null ? "" : divRate);
        //按分率 範囲チェック
        if (!CheckUtility.isNullOrEmpty(divRate) && !CheckUtility.checkIntegerRange(divRate, 1, 100)) {
            errorInfo.setStringError(
                    beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.numericRange",
                            new String[] { name, "1", "100" }));
            return errorInfo;
        }

        return errorInfo;
    }

    /**
     * 文字数チェック処理
     * @param target 対象文字列
     * @param name 対象項目名
     * @param length 最大文字数
     * @return errorInfo エラー情報
     */
    private BulkErrorInfo checkStringLength(String target, String name, int length) {
        BulkErrorInfo errorInfo = new BulkErrorInfo(Optional.empty(), target == null ? "" : target);
        //文字数チェック
        if (!CheckUtility.isNullOrEmpty(target)
                && target.length() > length) {
            errorInfo.setStringError(
                    beanMessages.getMessageFormat("smsCollectSettingMeterTenantEditBean.error.StringLengthTooLong",
                            new String[] {name, String.valueOf(length)}));
            return errorInfo;
        }
        return errorInfo;
    }

    private UpdateBulkSmsMeterTenantBaseInfoParameter createParameter (List<List<String>> reqData) {
        //所属元建物情報取得
        TBuilding divisionBuilding = meterTenantDao.getBuilding(this.divisionCorpId, this.divisionBuildingId);

        //ユーザーコード一覧を作成
        this.userCodeList = this.existBuildingList.stream()
                .map(x -> x.getTenantId())
                .collect(Collectors.toList());
        UpdateBulkSmsMeterTenantBaseInfoRequest request = new UpdateBulkSmsMeterTenantBaseInfoRequest();
        for (List<String> record: reqData) {
            Optional<SearchTenantSmsResultData> resExistBuilding = this.existBuildingList.stream()
                    .filter(x -> x.getTenantId().equals(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex()))))
                    .findFirst();
            UpdateBulkSmsMeterTenantBaseInfoRequestSet requestSet = new UpdateBulkSmsMeterTenantBaseInfoRequestSet();
            boolean editFlg = userCodeList.contains(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex())));

            //編集フラグ
            requestSet.setUpdateProcessFlg(editFlg);
            if (!editFlg ) {
                userCodeList.add(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex())));
            }
            //企業ID
            requestSet.setCorpId(this.divisionCorpId);

            //建物ID
            if (editFlg && resExistBuilding.isPresent()) {
                requestSet.setBuildingId(resExistBuilding.get().getBuildingId());
            }

            //所属企業ID
            requestSet.setDivisionCorpId(this.divisionCorpId);

            //所属建物ID
            requestSet.setDivisionBuildingId(this.divisionBuildingId);

            //ユーザーコード
            requestSet.setTenantId(Long.valueOf(record.get(BulkCsvIndex.USER_CODE.getIndex())));

            //テナント番号
            //新規で入力が無い場合は所属建物番号_ユーザーコード
            if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.NO.getIndex()))) {
                if (!editFlg) {
                    requestSet.setBuildingNo(divisionBuilding.getBuildingNo() + "_" + record.get(BulkCsvIndex.USER_CODE.getIndex()));
                }
            } else {
                requestSet.setBuildingNo(record.get(BulkCsvIndex.NO.getIndex()));
            }

            //テナント名
            //新規で入力が無い場合は所属建物の建物名
            if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.NAME.getIndex()))) {
                if (!editFlg) {
                    requestSet.setBuildingName(divisionBuilding.getBuildingName());
                }
            } else {
                requestSet.setBuildingName(record.get(BulkCsvIndex.NAME.getIndex()));
            }

            //テナント名かな
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.KANA.getIndex()))) {
                requestSet.setBuildingNameKana(record.get(BulkCsvIndex.KANA.getIndex()));
            }

            //テナント短縮名称
            //新規で入力が無い場合はnull
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.CONTRACTION.getIndex()))) {
                requestSet.setBuildingTansyukuName(record.get(BulkCsvIndex.CONTRACTION.getIndex()));
            }

            //郵便番号
            //新規で入力が無い場合は所属建物の郵便番号
            if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.ZIP_CODE.getIndex()))) {
                if (!editFlg) {
                    requestSet.setZipCd(divisionBuilding.getZipCd());
                }
            } else {
                requestSet.setZipCd(record.get(BulkCsvIndex.ZIP_CODE.getIndex()));
            }

            //都道府県
            //新規で入力が無い場合は所属建物の都道府県
            if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.PREFECTURE.getIndex()))) {
                if (!editFlg) {
                    requestSet.setPrefectureCd(divisionBuilding.getMPrefecture().getPrefectureCd());
                }
            } else {
                String prefCode = prefectureMap.get(record.get(BulkCsvIndex.PREFECTURE.getIndex()));
                requestSet.setPrefectureCd(prefCode);
            }

            //住所
            //新規で入力が無い場合は所属建物の住所
            if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.ADDRESS.getIndex()))) {
                if (!editFlg) {
                    requestSet.setAddress(divisionBuilding.getAddress());
                }
            } else {
                requestSet.setAddress(record.get(BulkCsvIndex.ADDRESS.getIndex()));
            }

            //住所(建物名)
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.ADDR_BLDG.getIndex()))) {
                requestSet.setAddressBuilding(record.get(BulkCsvIndex.ADDR_BLDG.getIndex()));
            }

            //電話番号
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.TEL_NUM.getIndex()))) {
                requestSet.setTelNo(record.get(BulkCsvIndex.TEL_NUM.getIndex()));
            }

            //FAX番号
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FAX_NUM.getIndex()))) {
                requestSet.setFaxNo(record.get(BulkCsvIndex.FAX_NUM.getIndex()));
            }

            //備考
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.BIKO.getIndex()))) {
                requestSet.setBiko(record.get(BulkCsvIndex.BIKO.getIndex()));
            }

            //集計開始年月
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.START_YM.getIndex()))) {
                requestSet.setTotalStartYm(record.get(BulkCsvIndex.START_YM.getIndex()));
            }

            //集計終了年月
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.END_YM.getIndex()))) {
                requestSet.setTotalEndYm(record.get(BulkCsvIndex.END_YM.getIndex()));
            }

            //公開可否
            //公開可否が設定できる場合は設定する
            if (getPublicSettingVisble()) {
              //新規で入力が無い場合は公開
                if (CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.PUBLIC_FLG.getIndex()))) {
                    if (!editFlg) {
                        requestSet.setPublicFlg(1);
                    }
                } else {
                    requestSet.setPublicFlg(Integer.valueOf(record.get(BulkCsvIndex.PUBLIC_FLG.getIndex())));
                }
            } else {
                if (editFlg && resExistBuilding.isPresent()) {
                    //公開可否が設定できない場合は既存値を登録する
                    requestSet.setPublicFlg(resExistBuilding.get().getPublicFlg());
                } else {
                    //今回新規登録分はデフォルト値を設定する
                    requestSet.setPublicFlg(1);
                }
            }

            //固定費1名称
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED1_NAME.getIndex()))) {
                requestSet.setFixedName1(record.get(BulkCsvIndex.FIXED1_NAME.getIndex()));
            }

            //固定費1金額
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED1_PRICE.getIndex()))) {
                requestSet.setFixedPrice1(new BigDecimal(record.get(BulkCsvIndex.FIXED1_PRICE.getIndex())));
            }

            //固定費2名称
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED2_NAME.getIndex()))) {
                requestSet.setFixedName2(record.get(BulkCsvIndex.FIXED2_NAME.getIndex()));
            }

            //固定費2金額
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED2_PRICE.getIndex()))) {
                requestSet.setFixedPrice2(new BigDecimal(record.get(BulkCsvIndex.FIXED2_PRICE.getIndex())));
            }

            //固定費3名称
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED3_NAME.getIndex()))) {
                requestSet.setFixedName3(record.get(BulkCsvIndex.FIXED3_NAME.getIndex()));
            }

            //固定費3金額
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED3_PRICE.getIndex()))) {
                requestSet.setFixedPrice3(new BigDecimal(record.get(BulkCsvIndex.FIXED3_PRICE.getIndex())));
            }

            //固定費4名称
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED4_NAME.getIndex()))) {
                requestSet.setFixedName4(record.get(BulkCsvIndex.FIXED4_NAME.getIndex()));
            }

            //固定費4金額
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.FIXED4_PRICE.getIndex()))) {
                requestSet.setFixedPrice4(new BigDecimal(record.get(BulkCsvIndex.FIXED4_PRICE.getIndex())));
            }

            //電気料金メニュー
            requestSet.setPriceMenuNo(new BigDecimal(record.get(BulkCsvIndex.PRICE_MENU.getIndex())));

            //契約容量
            requestSet.setContractCapacity(new BigDecimal(1));

            //按分率1
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE1.getIndex()))) {
                requestSet.setDivRate1(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE1.getIndex())));
            }

            //按分率2
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE2.getIndex()))) {
                requestSet.setDivRate2(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE2.getIndex())));
            }

            //按分率3
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE3.getIndex()))) {
                requestSet.setDivRate3(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE3.getIndex())));
            }

            //按分率4
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE4.getIndex()))) {
                requestSet.setDivRate4(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE4.getIndex())));
            }

            //按分率5
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE5.getIndex()))) {
                requestSet.setDivRate5(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE5.getIndex())));
            }

            //按分率6
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE6.getIndex()))) {
                requestSet.setDivRate6(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE6.getIndex())));
            }

            //按分率7
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE7.getIndex()))) {
                requestSet.setDivRate7(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE7.getIndex())));
            }

            //按分率8
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE8.getIndex()))) {
                requestSet.setDivRate8(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE8.getIndex())));
            }

            //按分率9
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE9.getIndex()))) {
                requestSet.setDivRate9(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE9.getIndex())));
            }

            //按分率10
            if (!CheckUtility.isNullOrEmpty(record.get(BulkCsvIndex.DIV_RATE10.getIndex()))) {
                requestSet.setDivRate10(new BigDecimal(record.get(BulkCsvIndex.DIV_RATE10.getIndex())));
            }

            request.getRequestSetList().add(requestSet);

        }

        UpdateBulkSmsMeterTenantBaseInfoParameter param = new UpdateBulkSmsMeterTenantBaseInfoParameter();
        param.setBean("UpdateBulkSmsMeterTenantBaseInfoBean");
        param.setRequest(request);

        return param;

    }

    /**
     * 公開設定表示可否
     *
     * @return true:表示
     */
    public boolean getPublicSettingVisble() {
        AccessResultSet result = osolAccessBean.getAccessEnable(
                OsolAccessBean.FUNCTION_CD.NONE,
                "OsakiOrPartnerCorp",
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginOperationCorpId());
        return result.getOutput().isRoleGroupEnable() && result.getOutput().isFunctionEnable();
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(List<String> infoMessages) {
        this.infoMessages = infoMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getDivisionCorpId() {
        return divisionCorpId;
    }

    public void setDivisionCorpId(String divisionCorpId) {
        this.divisionCorpId = divisionCorpId;
    }

    public String getDivisionCorpName() {
        return divisionCorpName;
    }

    public void setDivisionCorpName(String divisionCorpName) {
        this.divisionCorpName = divisionCorpName;
    }

    public Long getDivisionBuildingId() {
        return divisionBuildingId;
    }

    public void setDivisionBuildingId(Long divisionBuildingId) {
        this.divisionBuildingId = divisionBuildingId;
    }

    public String getDivisionBuildingNo() {
        return divisionBuildingNo;
    }

    public void setDivisionBuildingNo(String divisionBuildingNo) {
        this.divisionBuildingNo = divisionBuildingNo;
    }

    public String getDivisionBuildingName() {
        return divisionBuildingName;
    }

    public void setDivisionBuildingName(String divisionBuildingName) {
        this.divisionBuildingName = divisionBuildingName;
    }

    public Part getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(Part uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public List<List<BulkErrorInfo>> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<List<BulkErrorInfo>> errorList) {
        this.errorList = errorList;
    }
}
