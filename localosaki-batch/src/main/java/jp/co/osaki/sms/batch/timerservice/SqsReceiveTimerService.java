package jp.co.osaki.sms.batch.timerservice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.gson.Gson;

import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.TBatchStartupSetting;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TInspectionMeterBefPK;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.batch.SmsBatchConfigs;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchTimerService;
import jp.co.osaki.sms.batch.dao.SqsReceiveDao;
import jp.co.osaki.sms.batch.resultset.SqsReceiveMeterResultSet;
import jp.co.osaki.sms.batch.resultset.SqsReceiveResultSet;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * SMS SQS取得(813) TimerServiceクラス
 *
 * @author hayashi_tak
 *
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class SqsReceiveTimerService extends SmsBatchTimerService implements Serializable  {

    /**
     * implements Serializable.
     */
    private static final long serialVersionUID = -8042240428918032974L;

    /**
     * システム日時
     */
    private Timestamp serverDateTime;

    /**
     * SQS取得
     */
    private static final String BATCH_PROCESS_CD = OsolBatchConstants.BATCH_PROCESS_CD.SQS_RECEIVE.getVal();

    /**
     * DAO
     */
    private SqsReceiveDao dao;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsBatchConfigs smsBatchConfigs = new SmsBatchConfigs();

    /**
     * 起動処理
     */
    @PostConstruct
    protected void start() {
        // entityManagerの生成
        this.entityManager = this.entityManagerFactory.createEntityManager();
        // Daoのインスタンス生成
        this.dao = new SqsReceiveDao(this.entityManager);
        TBatchStartupSetting tBatchStartupSetting = this.dao.getTBatchStartupSetting(BATCH_PROCESS_CD);
        if (tBatchStartupSetting != null) {
            // バッチ名取得
            this.batchName = tBatchStartupSetting.getBatchProcessName();
            // バッチ起動スケジュール取得
            this.scheduleExpression = scheduleExpressionByCronTab(tBatchStartupSetting.getScheduleCronSpring(),
                    SCHEDULE_CRON_TYPE);
        }

        // 起動処理
        super.start(this.batchName);
    }




    @Override
    @Timeout
    protected void execute() {
        try {
            batchLogger.info(this.batchName.concat(" Start"));
            // サーバ時刻を取得
            serverDateTime = dao.getServerDateTime();
            AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(SmsBatchConstants.SQS_REGION).build();
            String queueUrl = smsBatchConfigs.getConfig(SmsBatchConstants.VAL_SQS_SERVERNAME);
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl).withMaxNumberOfMessages(10);
            List<Message> messageList = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for(int i=0;i<messageList.size();i++) {
                batchLogger.info("取得したキュー : \n" + messageList.get(i));
                Gson gson = new Gson();
                SqsReceiveResultSet sqsReceiveResultSet = new SqsReceiveResultSet();
                sqsReceiveResultSet = gson.fromJson(messageList.get(i).getBody(), SqsReceiveResultSet.class);
                if(hexConvertBool(sqsReceiveResultSet.getAlarm_code())) {
                    String meterId = sqsReceiveResultSet.getManage_number();
                    SqsReceiveMeterResultSet sqsReceiveMeterResultSet = new SqsReceiveMeterResultSet();
                    Long tenantId = null;
                    sqsReceiveMeterResultSet = dao.getMeterInfo(meterId);
                    if(sqsReceiveMeterResultSet == null) {
                        batchLogger.info("対象のmeterIdが見つかりません。meterId : " + meterId);
                        entityManager.clear();
                        continue;
                    }
                    List<TBuildDevMeterRelation> meterRelationList = new ArrayList<>();
                    meterRelationList.addAll(dao.getBuildMeterRelation(sqsReceiveMeterResultSet.getDevId(), sqsReceiveMeterResultSet.getMeterMngId()));

                    for(int j=0;j<meterRelationList.size();j++) {
                        if(dao.getTenantId(meterRelationList.get(j).getId().getCorpId(), meterRelationList.get(j).getId().getBuildingId()) != null) {
                            tenantId = dao.getTenantId(meterRelationList.get(j).getId().getCorpId(), meterRelationList.get(j).getId().getBuildingId());
                        }
                    }

                    TInspectionMeterBefPK tInspectionMeterBefPK = new TInspectionMeterBefPK();
                    TInspectionMeterBef tInspectionMeterBef = new TInspectionMeterBef();
                    try {
                        tInspectionMeterBefPK.setDevId(sqsReceiveMeterResultSet.getDevId());
                        tInspectionMeterBefPK.setMeterMngId(sqsReceiveMeterResultSet.getMeterMngId());
                        tInspectionMeterBefPK.setLatestInspDate(convertTimeStamp(sqsReceiveResultSet.getMeter_reading_time()));
                        tInspectionMeterBef.setId(tInspectionMeterBefPK);
                        if(dao.getBoolInspectionMeterBef(tInspectionMeterBef)) {
                            tInspectionMeterBef = dao.find(tInspectionMeterBef);
                        }
                        tInspectionMeterBef.setRecDate(serverDateTime);
                        tInspectionMeterBef.setRecMan("TgfuncSqs");
                        tInspectionMeterBef.setBuildingId(tenantId);
                        tInspectionMeterBef.setMeterType(sqsReceiveMeterResultSet.getMeterType());
                        tInspectionMeterBef.setMulti(sqsReceiveMeterResultSet.getMulti());
                        tInspectionMeterBef.setLatestInspVal(StringUtility.toBigDecimal(sqsReceiveResultSet.getMeter_value()).setScale(1, BigDecimal.ROUND_DOWN));
                        tInspectionMeterBef.setCreateUserId(Long.valueOf(0));
                        tInspectionMeterBef.setCreateDate(serverDateTime);
                        tInspectionMeterBef.setUpdateUserId(Long.valueOf(0));
                        tInspectionMeterBef.setUpdateDate(serverDateTime);
                    }catch(Exception e) {
                        errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        entityManager.clear();
                        continue;
                    }
                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }

                    //t_inspection_bef登録
                    if (entityManager.getTransaction().isActive()) {
                        dao.createInspectionBef(tInspectionMeterBef);
                    }

                    if (entityManager.getTransaction().getRollbackOnly()) {
                        batchLogger.info(this.batchName.concat(" rollback>> start"));
                        entityManager.getTransaction().rollback();
                        batchLogger.info(this.batchName.concat(" rollback>> end"));
                    } else {
                        batchLogger.info(this.batchName.concat(" commit>> start"));
                        entityManager.getTransaction().commit();
                        batchLogger.info(this.batchName.concat(" commit>> end"));
                    }
                    sqs.deleteMessage(queueUrl, messageList.get(i).getReceiptHandle());
                }
                entityManager.clear();
            }
            entityManager.clear();
            batchLogger.info(this.batchName.concat(" End"));
        }catch(Exception e) {
            batchLogger.error(this.batchName.concat(" Error"));
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            entityManager.flush();
            entityManager.clear();
        }
    }

    public Date convertTimeStamp(String time) {
        //【-】を【/】に変換する
        String strTime = time.replace("-", "/");
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(strTime);
        } catch (ParseException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
        return date;
    }

    /**
     * bit変換
     * 問題なければtrueを返却
     * @param alertCode
     * @return
     */
    public boolean hexConvertBool(String alertCode) {
        Integer num;
        try {
            //文字列を数値に変換
            num = Integer.parseInt(alertCode);
            String byteRet = Integer.toBinaryString(num);
            String byteStr = String.format("%8s", byteRet).replace(" ", "0");
            char[] byteChar = new char[byteStr.length()];
            for(int i = 0; i < byteStr.length(); i++){
                byteChar[i] = byteStr.charAt(i);
                if(byteChar[i]=='1' && (i == 3 || i == 5 || i == 6)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

}
