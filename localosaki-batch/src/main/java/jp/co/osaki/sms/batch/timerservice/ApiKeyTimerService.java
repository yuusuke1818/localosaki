package jp.co.osaki.sms.batch.timerservice;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyCreateParameter;
import jp.co.osaki.osol.api.result.apikeyissue.ApiKeyCreateResult;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.MPersonDao;
import jp.co.osaki.sms.batch.dao.TApiKeyDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * LTE-M APIキーTimerServiceクラス
 *
 * @author y.nakamura
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class ApiKeyTimerService extends SmsBatchTimerService implements Serializable {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = 3134604249692165955L;

    /**
     * バッチプロセスコード
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.ISSUE_API_KEY.getVal();

    /**
     * APIキーDao.
     */
    private TApiKeyDao tApiKeyDao;

    /**
     * 担当者Dao.
     */
    private MPersonDao mPersonDao;

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    // API存在状況フラグ
    private boolean apikeyExistFlg;

    //APIキー
    private String apiKeyString;

    //有効期限
    private String expirationDate;

    // リフレッシュキー
    private String refreshKeyString;

    //発行ボタン表示内容
    private String buttonTitle;

    /**
     * APIキーテーブルのVERSION
     */
    private Integer version;

    @Inject
    private OsolConfigs osolConfigs;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {

        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.tApiKeyDao = new TApiKeyDao(this.entityManager);
        this.mPersonDao = new MPersonDao(this.entityManager);

        TBatchStartupSetting tBatchStartupSetting = this.tApiKeyDao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(), SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);

    }

    /*
     * (非 Javadoc)
     *
     * @see jp.skygroup.enl.webap.base.batch.BaseBatchScheduleTimer#execute()
     */
    @TransactionTimeout(unit = TimeUnit.HOURS, value = 2)
    @Timeout
    @Override
    protected void execute() {

        try {
            // バッチ処理開始ログ出力
            batchLogger.info(this.batchName.concat(" Start"));

            // バッチ処理日時を取得する
            serverDateTime = tApiKeyDao.getServerDateTime();

            // --- ▽▽▽ APIキー発行処理(ここから) ▽▽▽ ---

            // プロパティファイルから企業IDと担当者IDを取得
            String corpId = smsBatchConfigs.getConfig("LTEM_CORP_ID");
            String personId = smsBatchConfigs.getConfig("LTEM_PERSON_ID");
            // 既存のAPIキー取得
            loadApiKey(corpId, personId);

            // APIキー発行
            ApiKeyCreateResult result = issueApiKey(corpId, personId, serverDateTime);

            // 発行結果をマスクしてログに残す
            if (result != null) {
                String masked = (result.getApiKey() != null && result.getApiKey().length() > 8)
                        ? result.getApiKey().substring(0, 4) + "..." + result.getApiKey().substring(result.getApiKey().length() - 4)
                        : String.valueOf(result.getApiKey());
                batchLogger.info(String.format("APIキー発行成功 corpId=%s personId=%s issueDate=%s apiKey=%s",
                        corpId, personId, result.getIssueDateTime(), masked));
            }

            // --- △△△ APIキー発行処理(ここまで) △△△ ---

            batchLogger.info(this.batchName.concat(" End"));

        } catch (Exception ex) {
            ex.printStackTrace();
            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                entityManager.clear();
            }

            // Teams通知
            sendErrorNotification(this.batchName, ex);
        }
    }

    /**
     * APIキーを取得する
     *
     * @param corpId 対象の企業ID
     * @param personId 対象の担当者ID
     */
    public void loadApiKey(String corpId, String personId) {
        TApiKey apiKey = tApiKeyDao.find(corpId, personId);

        if (apiKey != null) {
            apiKeyString = apiKey.getApiKey();
            setExpirationDate(DateUtility.changeDateFormat(apiKey.getExpirationDate(),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
            setRefreshKeyString(apiKey.getRefreshKey());
            apikeyExistFlg = true;
            setButtonTitle("再発行");
            this.setVersion(apiKey.getVersion());
        } else {
            apiKeyString = "";
            setExpirationDate("");
            setRefreshKeyString("");
            apikeyExistFlg = false;
            setButtonTitle("発行");
            this.setVersion(null);
        }
    }

    /**
     * APIキーを発行する
     *
     * @param corpId 対象の企業ID
     * @param personId 対象の担当者ID
     * @param serverDateTime バッチ処理日時
     * @return 発行結果
     */
    public ApiKeyCreateResult issueApiKey(String corpId, String personId, Timestamp serverDateTime) {

        try {
            // トランザクション処理開始
            entityManager.getTransaction().begin();

            // APIキー発行
            ApiKeyCreateParameter parameter = new ApiKeyCreateParameter();
            parameter.setVersion(this.getVersion());
            parameter.setLoginCorpId(corpId);
            parameter.setLoginPersonId(personId);
            parameter.setOperationCorpId(corpId);

            ApiKeyCreateResult result = new ApiKeyCreateResult();

            // 有効期限日時を取得する
            Timestamp limitDate = new Timestamp(DateUtility.plusMinute(
                            new Date(serverDateTime.getTime()), OsolConstants.API_PUBLIC_KEY_LIFE_TIME).getTime());

            // APIキー発行対象ユーザーIDを取得
            Long userId = getMPerson(corpId, personId).getUserId();

            TApiKey tApiKey = tApiKeyDao.find(corpId, personId);

            // 楽観ロック
            if (tApiKey != null) {
                if (parameter.getVersion() == null || !tApiKey.getVersion().equals(parameter.getVersion())) {
                    throw new OptimisticLockException();
                }
            } else {
                if (parameter.getVersion() != null) {
                    throw new OptimisticLockException();
                }
            }

            // APIキーを発行する
            String apiKey = OsolApiAuthUtil.createPublicApiKey(userId, personId,
                    corpId, parameter.getOperationCorpId(), serverDateTime);

            String refreshKey = OsolApiAuthUtil.createRefreshKey(userId, personId,
                    corpId, parameter.getOperationCorpId(), serverDateTime);

            if (CheckUtility.isNullOrEmpty(apiKey) || CheckUtility.isNullOrEmpty(refreshKey)) {
                throw new IllegalStateException("APIキー生成に失敗した");
            } else {
                result.setApiKey(apiKey);
                result.setIssueDateTime(serverDateTime);
                result.setRefreshKey(refreshKey);
            }

            // DB登録/更新
            if (tApiKey == null) {

                TApiKey insertParam = new TApiKey();
                TApiKeyPK pkInsertParam = new TApiKeyPK();

                pkInsertParam.setCorpId(corpId);
                pkInsertParam.setPersonId(personId);
                insertParam.setId(pkInsertParam);
                insertParam.setApiKey(apiKey);
                insertParam.setIssuedDate(serverDateTime);
                insertParam.setRefreshKey(refreshKey);
                insertParam.setExpirationDate(limitDate);
                insertParam.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
                insertParam.setDelFlg(OsolConstants.FLG_OFF);
                insertParam.setVersion(0);
                insertParam.setCreateDate(serverDateTime);
                insertParam.setCreateUserId(userId);
                insertParam.setUpdateDate(serverDateTime);
                insertParam.setUpdateUserId(userId);

                tApiKeyDao.register(insertParam);

            } else {

                tApiKey.setApiKey(apiKey);
                tApiKey.setIssuedDate(serverDateTime);
                tApiKey.setExpirationDate(limitDate);
                tApiKey.setRefreshKey(refreshKey);
                tApiKey.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
                tApiKey.setDelFlg(OsolConstants.FLG_OFF);
                tApiKey.setUpdateDate(serverDateTime);
                tApiKey.setUpdateUserId(userId);

                tApiKeyDao.merge(tApiKey);
            }

            // コミット処理
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.clear(); // キャッシュに古い値が残らないようにクリア実施

            // 画面表示用の再読込（内部状態更新）
            loadApiKey(corpId, personId);

            // 呼び出し側に渡す結果オブジェクトをセット
            result.setApiKey(apiKey);
            result.setIssueDateTime(serverDateTime);
            result.setRefreshKey(refreshKey);

            return result;

        } catch (Exception ex) {

            // ログ出力
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                entityManager.clear();
            }
            // 呼び出し側でハンドリングしやすいよう再送出
            throw (ex instanceof RuntimeException) ? (RuntimeException) ex : new RuntimeException(ex);
        }
    }

    /**
     * 異常終了時のTeams通知。
     *
     * @param batchName バッチ名
     * @param ex 発生した例外
     */
    private void sendErrorNotification(String batchName, Exception ex) {
        sendNotification(createErrorMessage(batchName, ex));
    }

    /**
     * 異常終了時メッセージを生成。
     *
     * @param batchName バッチ名
     * @param ex 発生した例外
     * @return メッセージ本文
     */
    private String createErrorMessage(String batchName, Exception ex) {

        batchLogger.info(this.batchName.concat(" createErrorMessage called."));

        TemplateEngine templateEngine = getTemplateEngine();

        Map<String, Object> variables = new HashMap<String, Object>();
        // 実行環境
        variables.put("environment", smsBatchConfigs.getConfig("LTEM_API_KEY_EXECUTION_ENVIRONMENT"));
        // バッチ名
        variables.put("batchName", batchName);
        // 実行日時
        variables.put("implementationDate",  DateUtility.changeDateFormat(new Timestamp((new Date()).getTime()), DateUtility.DATE_FORMAT_YYYYMMDDHHmm_SLASH));
        // エラー内容
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        String fullStackTrace = sw.toString();
        // 最初の10行だけ取得、残りは省略
        String[] lines = fullStackTrace.split("\n");
        StringBuilder sb = new StringBuilder();
        int limit = Math.min(lines.length, 10);
        for (int i = 0; i < limit; i++) {
            sb.append(lines[i]).append("\n");
        }
        // 省略がある場合、末尾に残りの行数を追加
        if (lines.length > 10) {
            sb.append("\n...（他 ").append(lines.length - 10).append(" 行）");
        }
        variables.put("errorContent", sb.toString());

        Context con = new Context();
        con.setVariables(variables);

        String message = templateEngine.process(
                smsBatchConfigs.getConfig("LTEM_API_KEY_ERROR_NOTIFICATION_MESSAGE"),
                con);

        batchLogger.info(this.batchName.concat("異常終了時メッセージ:" + message));

        return message;
    }

    /**
     * Teamsに通知。
     *
     * @param message 送信メッセージ
     */
    private void sendNotification(String message) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault();) {

            // HTTP通信設定
            RequestConfig config = RequestConfig.custom()
                    .setSocketTimeout(60000)
                    .setConnectTimeout(60000)
                    .build();

            // HTTP POSTリクエスト生成
            HttpPost httpPost = new HttpPost(smsBatchConfigs.getConfig("LTEM_API_KEY_NOTIFICATION_URL"));
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setConfig(config);
            httpPost.setEntity(new ByteArrayEntity((message).getBytes()));

            // 通信実行
            HttpResponse response = httpClient.execute(httpPost);

            // HTTPステータスを取得
            int returnHttpStatus = response.getStatusLine().getStatusCode();

            batchLogger.info(this.batchName.concat("Teams通知のHTTPステータス：" + returnHttpStatus));

            // 返却メッセージを取得
            String returnMessage = EntityUtils.toString(response.getEntity());

            if (returnHttpStatus != 200) {
                throw new RuntimeException("returnHttpStatus is " + returnHttpStatus);
            }
            // HTTP通信としては成功し200が返るが、teams側で400が発生する事がある。
            else {

                // 以下の単語を含んでいた場合はエラーとする。
                if (this.containsAny(returnMessage, "failed", "error", "400")) {

                    batchLogger.error(returnMessage);
                    errorLogger.error(returnMessage);

                    throw new RuntimeException("Contains words that refer to failure");
                }
            }

            batchLogger.info("Teamsへのメッセージ送信に成功しました。");

        } catch (Throwable t) {
            batchLogger.error("Teamsへのメッセージ送信に失敗しました。", t);
            errorLogger.error("Teamsへのメッセージ送信に失敗しました。", t);
        }
    }

    /**
     * 対象文字列に指定文字列(複数可)が含まれるか判定する。
     *
     * @param target 対象文字列
     * @param words 指定文字列
     * @return true：何れかの指定文字列が含まれる、false：何れも含まれない
     */
    public boolean containsAny(String target, String... words) {
        return StringUtils.containsAny(target, words);
    }

    /**
     * テキストテンプレート取得
     *
     * @return テキストテンプレート
     */
    private TemplateEngine getTemplateEngine() {

        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setPrefix("notification/template/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver);

        return templateEngine;
    }

    /**
     * 担当者取得メソッド
     *
     * @param corpId 対象の企業ID
     * @param personId 対象の担当者ID
     * @return MPerson
     */
    protected MPerson getMPerson(String corpId, String personId) {

        return mPersonDao.find(corpId, personId);
    }

    public boolean isApikeyExistFlg() {
        return apikeyExistFlg;
    }

    public void setApikeyExistFlg(boolean apikeyExistFlg) {
        this.apikeyExistFlg = apikeyExistFlg;
    }

    public String getApiKeyString() {
        return apiKeyString;
    }

    public void setApiKeyString(String apiKeyString) {
        this.apiKeyString = apiKeyString;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRefreshKeyString() {
        return refreshKeyString;
    }

    public void setRefreshKeyString(String refreshKeyString) {
        this.refreshKeyString = refreshKeyString;
    }

}

