package jp.co.osaki.sms.bean.deviceCtrl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.MeterDataFilterDao;	// 2023-12-21
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.InsertSmsMeterParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.UpdateSmsMeterParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.InsertSmsMeterRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meter.UpdateSmsMeterRequest;
import jp.co.osaki.osol.api.response.apikeyissue.OsolApiKeyCreateResponse;
import jp.co.osaki.osol.api.response.osolapi.OsolApiErrorMessageListResponse;
import jp.co.osaki.osol.api.response.osolapi.OsolApiErrorMessageResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.InsertSmsMeterResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.UpdateSmsMeterResponse;
import jp.co.osaki.osol.api.result.osolapi.OsolApiErrorMessageListResult;
import jp.co.osaki.osol.api.result.osolapi.OsolApiErrorMessageResult;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.entity.MAlertMail;
import jp.co.osaki.osol.entity.MAlertMailPK;
import jp.co.osaki.osol.entity.MAlertMailSetting;	// 2023-12-20 追加
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;
import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK;
import jp.co.osaki.osol.entity.MDevFwVersion;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MManualInsp;
import jp.co.osaki.osol.entity.MManualInspPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MMeterLoadlimitPK;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.MRepeaterPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeterPK;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTime;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTimePK;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK;
import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.osol.entity.TInspectionMeterPK;
import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.osol.web.csv.sms.converter.DeviceCtrlCsvConverter;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConfigs;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;	// 2023-12-27
import jp.co.osaki.sms.bean.sms.collect.setting.device.SendLteAutoRegistMail;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterManagementConstants;
import jp.co.osaki.sms.dao.MAlertMailSettingDao;	// 2023-12-21 追加
import jp.co.osaki.sms.dao.TInspectionMeterDao;
import jp.co.osaki.sms.dao.TMeterDataDao;
import jp.co.osaki.sms.deviceCtrl.dao.MAlertMailDao;
import jp.co.osaki.sms.deviceCtrl.dao.MAutoInspDao;
import jp.co.osaki.sms.deviceCtrl.dao.MBuildingSmsDao;
import jp.co.osaki.sms.deviceCtrl.dao.MConcentratorDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevFwVersionDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevPrmDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevRelationDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevRelationJoinTBuildingDao;
import jp.co.osaki.sms.deviceCtrl.dao.MManualInspDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterLoadlimitDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterTypeDao;
import jp.co.osaki.sms.deviceCtrl.dao.MPauseMailDao;
import jp.co.osaki.sms.deviceCtrl.dao.MRepeaterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MTenantSmsDao;
import jp.co.osaki.sms.deviceCtrl.dao.TBuildDevMeterRelationDao;
import jp.co.osaki.sms.deviceCtrl.dao.TBuildingDao;
import jp.co.osaki.sms.deviceCtrl.dao.TCommandDao;
import jp.co.osaki.sms.deviceCtrl.dao.TCommandLoadSurveyMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.TCommandLoadSurveyTimeDao;
import jp.co.osaki.sms.deviceCtrl.dao.TDayLoadSurveyDao;
import jp.co.osaki.sms.deviceCtrl.dao.TDayLoadSurveyRevDao;
import jp.co.osaki.sms.deviceCtrl.dao.TWorkHstDao;
import jp.co.osaki.sms.deviceCtrl.dao.TWorkHstForCsvDao;
import jp.co.osaki.sms.deviceCtrl.response.CreateDeviceXmlResponse;
import jp.co.osaki.sms.deviceCtrl.response.ErrorResponse;
import jp.co.osaki.sms.deviceCtrl.resultset.MDevRelationJoinTBuildingResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.SendConcentratorErrResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.SendMeterErrResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.SendTermErrResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.TDayLoadSurveyResultSet;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;
import jp.skygroup.enl.webap.base.api.BaseApiServlet;

@WebServlet(name = "DeviceCtrlXml", urlPatterns = {"/deviceCtrl", "/sms_ver3_honban_deploy/bin/*" }, description = "XML形式で受け取る", displayName = "OSOL API XML Servlet")
public class DeviceCtrlXml extends BaseApiServlet {


    private static final long serialVersionUID = -455249511254702159L;

    /**
     * コンフィグ設定値取得クラス.
     */
    private static final SmsConfigs smsConfigs = new SmsConfigs();



    //mudm2コマンド
    final String[] mudm2Cmd = {"add-repeater",  "del-repeater", "add-concent", "del-concent",  "init-concent",  "concent-list",  "repeater-list",
            "add-meter","del-meter","chg-meter",  "set-spitconf", "meter-list", "get-meterinfo", "get-spitconf", "get-meterval","set-breaker",
            "set-loadlimit", "get-meterctrl", "get-mvalfile", "get-dlsfile","get-mlsfile","get-dlsdata", "get-rdlsdata","get-demdata", "send-metererr",
            "send-termerr", "send-concenterr", "get-autoinsp", "set-autoinsp","get-demconf","set-demconf", "send-demalarm","get-dmvdata", "get-rdmvdata",
            "get-demalarm",  "manual-inspect", "get-ainspdata", "get-minspdata", "get-ainspfile",  "get-rainspfile", "get-minspfile",
            "get-rminspfile","get-demfile"};

    //mudm3コマンド
    final String[] mudm3Cmd = {"get-dlsmeter", "get-dlsperiod", "get-rdlsmeter", "get-rdlsperiod","get-dmvmeter", "get-dmvperiod", "get-rdmvmeter",  "get-rdmvperiod","set-interval"};

    //t_command_load_survey_meter
    final String[] tCmdMeter = {"get-dlsmeter","get-dmvmeter","get-rdlsmeter","get-rdmvmeter"};
    //t_command_load_survey_time
    final String[] tCmdTime = {"get-dlsperiod","get-dmvperiod","get-rdlsperiod","get-rdmvperiod"};
    //日報系コマンド
    final String[] tCmdDay = {"get-dlsdata","get-dmvdata","get-rdlsdata","get-rdmvdata"};

    //list系コマンド
    final String[] tCmdList = {"meter-list","repeater-list","concent-list"};

    //ハンディ端末コマンド
    final String[] walkCmd = {"get-rfmeterinfo","set-rfmeterinfo"};

    //メール送信
//    final String[] mailCmd = {"send-metererr","send-concenterr","send-termerr"};
    final String[] mailCmd = {"send-metererr","send-concenterr","send-termerr","send-meteradd"};	// 2023-12-25

    final String[] tCmd = {"get-dlsdata","get-dmvdata","get-rdmvdata","get-ainspdata","get-minspdata","get-rdlsdata","get-meterctrl","get-meterinfo","get-meterval","get-spitconf","set-interval","meter-list","repeater-list","concent-list","manual-inspect"};

    /** 一括設定コマンド*/
    final String[] tCmdBulk = {"get-meterctrl","get-meterinfo","get-spitconf"};

    final String[] cmdConcentrator = {"add-concent","del-concent","init-concent"};
    final String[] cmdRepeater = {"add-repeater","del-repeater"};
    final String[] cmdMeter = {"add-meter","del-meter","chg-meter", "set-breaker", "set-spitconf"};
    final String[] cmdLoadlimit = {"set-loadlimit"};
    final String[] month = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    @EJB
    private MConcentratorDao mConcentratorDao;
    @EJB
    private MDevRelationDao mDevRelationDao;
    @EJB
    private MMeterDao mMeterDao;
    @EJB
    private MRepeaterDao mRepeaterDao;
    @EJB
    private TMeterDataDao tMeterDataDao;
    @EJB
    private MMeterLoadlimitDao mMeterLoadlimitDao;
    @EJB
    private TDayLoadSurveyDao tDayLoadSurveyDao;
    @EJB
    private TInspectionMeterDao tInspectionMeterDao;
    @EJB
    private MAlertMailDao mAlertMailDao;
    @EJB
    private TBuildingDao tBuildingDao;
    @EJB
    private MDevPrmDao mDevPrmDao;
    @EJB
    private MBuildingSmsDao mBuildingSmsDao;
    @EJB
    private TDayLoadSurveyRevDao tDayLoadSurveyRevDao;
    @EJB
    private MDevRelationJoinTBuildingDao mMeterJoinTBuildingRelationDao;
    @EJB
    private MTenantSmsDao mTenantSmsDao;
    @EJB
    private TWorkHstDao tWorkHstDao;
    @EJB
    private MManualInspDao mManualInspDao;
    @EJB
    private TBuildDevMeterRelationDao tBuildDevMeterRelationDao;
    @EJB
    private TCommandDao tCommandDao;
    @EJB
    private TCommandLoadSurveyMeterDao tCommandLoadSurveyMeterDao;
    @EJB
    private TCommandLoadSurveyTimeDao tCommandLoadSurveyTimeDao;
    @EJB
    private MAutoInspDao mAutoInspDao;
    @EJB
    private TWorkHstForCsvDao tWorkHstForCsvDao;
    @EJB
    private MDevRelationJoinTBuildingDao mDevJoinTBuildingRelationDao;
    @EJB
    private MMeterTypeDao mMeterTypeDao;
    @EJB
    private MPauseMailDao mPauseMailDao;
    @EJB
    private MAlertMailSettingDao mAlertMailSettingDao;	// 2023-12-21
    @EJB
    private MDevFwVersionDao mDevFwVersionDao;

    /**
     * メーターフィルター
    **/
    @EJB
    private MeterDataFilterDao meterDataFilterDao;

    @Inject
    private OsolConfigs osolConfigs;

    /**
     * smControlログ
     */
    private static final Logger smCtrlLogger = Logger.getLogger(DeviceCtrlConstants.SmControl);

    private static final String lteRegistMail = "lte_term_auto_regist";	// 2023-12-20

    /**
     * エラーログ
     */
    private static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());


    /**
     * リクエストログ
     */
    protected static Logger requestLogger = Logger.getLogger(LOGGER_NAME.REQUEST.getVal());

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> nodeMap = new HashMap<>();
        String bodyString = "";
        boolean[] errorFlg = new boolean[1];
        errorFlg[0] = false;
        Boolean endFlg = false;
        Boolean busyFlg = false;

        String[] errorCode = new String[1];
        errorCode[0] = "";

        nodeMap.clear();
        String devId = "";
        String devPw = "";
        /** 異常処理用のやつ*/
        String dataErr = "";

        /** 認証用 */
        String dateDev = "";

        //ヘッダー情報出力
        headerOut(request);

        //セッションIDを付与
        HttpSession session = request.getSession();
        //タイムアウト時間設定
        session.setMaxInactiveInterval(1800000);
        String id = session.getId();
        Cookie cookie = new Cookie("JSESSID",id);
        cookie.setPath("/");
        response.addCookie(cookie);

        //httpリクエストのbody部分をString型で取りだす
        try {
            bodyString = request.getReader().lines().collect(Collectors.joining("\r\n"));
            bodyString = bodyString.replace("\0", "");
            smCtrlLogger.info("INPUT:\n".concat(bodyString));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(StringReader reader = new StringReader(bodyString)){
            DocumentBuilderFactory responseFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder responseDocBuilder = responseFactory.newDocumentBuilder();
            Document responseDocument = responseDocBuilder.newDocument();


            // CSV用処理
            DeviceCtrlForCSVMethod devMethod = new DeviceCtrlForCSVMethod(tWorkHstForCsvDao, mDevPrmDao);


            String codeName = "";
            int beginIndex;
            int endIndex;

            if(bodyString.contains(DeviceCtrlConstants.authS)) {
                beginIndex = bodyString.indexOf(DeviceCtrlConstants.authS) + DeviceCtrlConstants.authS.length();
                endIndex = bodyString.indexOf(DeviceCtrlConstants.coron);
                devId = bodyString.substring(beginIndex, endIndex);

                beginIndex = bodyString.indexOf(DeviceCtrlConstants.coron) + DeviceCtrlConstants.coron.length();
                endIndex = bodyString.indexOf(DeviceCtrlConstants.authE);
                devPw = bodyString.substring(beginIndex, endIndex);
            }

            //codeタグが存在すれば抜きだす
            if(bodyString.contains(DeviceCtrlConstants.codeS)) {
                beginIndex = bodyString.indexOf(DeviceCtrlConstants.codeS) + DeviceCtrlConstants.codeS.length();
                endIndex = bodyString.indexOf(DeviceCtrlConstants.codeE);
                codeName = bodyString.substring(beginIndex, endIndex);
            }

            //読みだしたStringをxmlに変換
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(reader));

            // XMLドキュメントのルート要素を取得 => root
            Node root = doc.getDocumentElement();

            //nodeの内容をMapに詰めて行く(仮)
            allNodeSearchToMap(root, nodeMap);

            //返信用XMLドキュメントのコンテンツ要素作成
            Element contents = null;
            contents = responseXmlCreate(contents, responseDocument, nodeMap);

            //返信用のcommandの子要素, テキストを追加
            Element command = responseDocument.createElement(DeviceCtrlConstants.command);

            dateDev = nodeMap.get(DeviceCtrlConstants.date);
            dataErr = nodeMap.get(DeviceCtrlConstants.data);

            //機器のID:PWチェック.
            devAuth(devId, devPw, errorFlg);

            //xmlの日付チェック
            dateAuth(dateDev, errorFlg);

            //装置から来たxmlがエラーを調べる
            if(codeName.equals(DeviceCtrlConstants.error)) {

                //エラー 10, 20の場合
                if(dataErr.equals(DeviceCtrlConstants.ftpErr) || dataErr.equals(DeviceCtrlConstants.devErr)) {
                    endFlg=true;
                    errorLogger.error("errorCode : " + dataErr);
                    errorLogger.error("devId : " + devId);
                    TWorkHst tWrkHst = new TWorkHst();
                    TWorkHstPK tWrkHstPK = new TWorkHstPK();
                    tWrkHstPK.setDevId(devId);
                    tWrkHst.setId(tWrkHstPK);
                    tWrkHst.setSrvEnt(DeviceCtrlConstants.two.toString());
                    tWrkHst = tWorkHstDao.find(tWrkHst);
                    error20(tWrkHst, nodeMap);
                }

                //エラー 01,02,03の場合
                if(dataErr.equals(DeviceCtrlConstants.commandErr) || dataErr.equals(DeviceCtrlConstants.syntaxErr) || dataErr.equals(DeviceCtrlConstants.boundaryValueErr)) {
                    errorLogger.error("errorCode : " + dataErr);
                    errorLogger.error("devId : " + devId);

                    List<TWorkHst> tWrkHstList = new ArrayList<>();
                    TWorkHst tWrkHst = new TWorkHst();
                    TWorkHstPK tWrkHstPK = new TWorkHstPK();
                    tWrkHstPK.setDevId(devId);
                    tWrkHst.setId(tWrkHstPK);
                    tWrkHst.setSrvEnt(DeviceCtrlConstants.two.toString());

                    //存在するかチェック
                    if(!tWorkHstDao.isNull(tWrkHst)) {
                        //srv_ent = 2のコマンド全部取得する
                        tWrkHstList.addAll(tWorkHstDao.getTWorkHstList(tWrkHst));
                        for(TWorkHst retTWrkHst : tWrkHstList) {

                            //t_commandに対象のコマンド持ったレコードがあるか探しに行く
                            if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command)) {
                                TCommand tCommand = new TCommand();
                                TCommandPK tCommandPK = new TCommandPK();
                                tCommandPK.setDevId(devId);
                                tCommandPK.setCommand(retTWrkHst.getId().getCommand());
                                tCommand.setId(tCommandPK);
                                tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandDao.isNull(tCommand)) {
                                    if(!Arrays.asList(tCmdBulk).contains(tCommand.getId().getCommand())) {
                                        tCommand = tCommandDao.find(tCommand);
                                        tCommandToNine(tCommand);
                                    }else {
                                        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
                                        tCommand = tCommandDao.getTCommandList(tCommand).get(0);
                                        tCommandToNine(tCommand);
                                        String link = tCommand.getLinking();
                                        String errCodeName = tCommand.getId().getCommand();
                                        if(tCommandBulkSize(devId, errCodeName, link) == 0) {
                                            tCommandBulkToNull(devId, errCodeName, link);
                                        }
                                    }
                                    //t_work_hst srv_entを空文字にする
                                    tWrkHstToNull(retTWrkHst);
                                }
                            }else if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command_load_survey_meter)) {
                                List<TCommandLoadSurveyMeter> tCmdMeterList = new ArrayList<>();
                                TCommandLoadSurveyMeter tCmdMeter = new TCommandLoadSurveyMeter();
                                TCommandLoadSurveyMeterPK tCmdMeterPK = new TCommandLoadSurveyMeterPK();
                                tCmdMeterPK.setDevId(devId);
                                tCmdMeterPK.setCommand(retTWrkHst.getId().getCommand());
                                tCmdMeter.setId(tCmdMeterPK);
                                tCmdMeter.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandLoadSurveyMeterDao.isNull(tCmdMeter)) {
                                    //該当コマンドを全部積める
                                    tCmdMeterList.addAll(tCommandLoadSurveyMeterDao.getResultList(tCmdMeter));

                                    //全てエラーに上書きする
                                    for(TCommandLoadSurveyMeter retTCmdMeter : tCmdMeterList) {
                                        //t_command_load_survey_meterテーブル srv_entを9にする
                                        tCommandMeterToNine(retTCmdMeter);
                                    }
                                    //t_work_hst srv_entを空文字にする
                                    tWrkHstToNull(retTWrkHst);
                                }

                            }else if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command_load_survey_time)) {
                                List<TCommandLoadSurveyTime> tCmdTimeList = new ArrayList<>();
                                TCommandLoadSurveyTime tCmdTime = new TCommandLoadSurveyTime();
                                TCommandLoadSurveyTimePK tCmdTimePK = new TCommandLoadSurveyTimePK();
                                tCmdTimePK.setDevId(devId);
                                tCmdTimePK.setCommand(retTWrkHst.getId().getCommand());
                                tCmdTime.setId(tCmdTimePK);
                                tCmdTime.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandLoadSurveyTimeDao.isNull(tCmdTime)) {
                                    //該当コマンドを全部積める
                                    tCmdTimeList.addAll(tCommandLoadSurveyTimeDao.getResultList(tCmdTime));

                                    //全てエラーに上書きする
                                    for(TCommandLoadSurveyTime retTCmdTime : tCmdTimeList) {
                                        //t_command_load_survey_meterテーブル srv_entを9にする
                                        tCommandTimeToNine(retTCmdTime);
                                    }
                                    //t_work_hst srv_entを空文字にする
                                    tWrkHstToNull(retTWrkHst);
                                }

                            }else {
                                error20(retTWrkHst, nodeMap);
                            }

                        }
                    }
                }


                //エラー 00,04の場合
                //違いはmergeするものが「9」ではなく「""」という点
                if(dataErr.equals(DeviceCtrlConstants.busy) || dataErr.equals(DeviceCtrlConstants.noData)) {
                    errorLogger.error("errorCode : " + dataErr);
                    errorLogger.error("devId : " + devId);

                    List<TWorkHst> tWrkHstList = new ArrayList<>();
                    TWorkHst tWrkHst = new TWorkHst();
                    TWorkHstPK tWrkHstPK = new TWorkHstPK();
                    tWrkHstPK.setDevId(devId);
                    tWrkHst.setId(tWrkHstPK);
                    tWrkHst.setSrvEnt(DeviceCtrlConstants.two.toString());

                    //存在するかチェック
                    if(!tWorkHstDao.isNull(tWrkHst)) {
                        //srv_ent = 2のコマンド全部取得する
                        tWrkHstList.addAll(tWorkHstDao.getTWorkHstList(tWrkHst));

                        for(TWorkHst retTWrkHst : tWrkHstList) {
                            //t_commandに対象のコマンド持ったレコードがあるか探しに行く
                            if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command)) {
                                TCommand tCmd = new TCommand();
                                TCommandPK tCmdPK = new TCommandPK();
                                tCmdPK.setDevId(devId);
                                tCmdPK.setCommand(retTWrkHst.getId().getCommand());
                                tCmd.setId(tCmdPK);
                                tCmd.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandDao.isNull(tCmd)) {
                                    if(!Arrays.asList(tCmdBulk).contains(tCmd.getId().getCommand())) {
                                        tCmd = tCommandDao.find(tCmd);
                                        tWrkHstToNull(retTWrkHst);
                                        tCommandToNull(tCmd);
                                    }else {
                                        tCmd.setSrvEnt(DeviceCtrlConstants.one.toString());
                                        tCmd = tCommandDao.getTCommandList(tCmd).get(0);
                                        tWrkHstToNull(retTWrkHst);
                                        tCommandToNull(tCmd);
                                        String link = tCmd.getLinking();
                                        String errCodeName = tCmd.getId().getCommand();
                                        if(tCommandBulkSize(devId, errCodeName, link) == 0) {
                                            tCommandBulkToNull(devId, errCodeName, link);
                                        }
                                    }

                                }

                            }else if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command_load_survey_meter)) {
                                List<TCommandLoadSurveyMeter> tCmdMeterList = new ArrayList<>();
                                TCommandLoadSurveyMeter tCmdMeter = new TCommandLoadSurveyMeter();
                                TCommandLoadSurveyMeterPK tCmdMeterPK = new TCommandLoadSurveyMeterPK();
                                tCmdMeterPK.setDevId(devId);
                                tCmdMeterPK.setCommand(retTWrkHst.getId().getCommand());
                                tCmdMeter.setId(tCmdMeterPK);
                                tCmdMeter.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandLoadSurveyMeterDao.isNull(tCmdMeter)) {
                                    //該当コマンドを全部積める
                                    tCmdMeterList.addAll(tCommandLoadSurveyMeterDao.getResultList(tCmdMeter));

                                    //全てエラーに上書きする
                                    for(TCommandLoadSurveyMeter retTCmdMeter : tCmdMeterList) {
                                        //t_command_load_survey_meterテーブル srv_entを空文字にする
                                        tCommandMeterToNull(retTCmdMeter);

                                        //t_work_hst srv_entを空文字にする
                                        tWrkHstToNull(retTWrkHst);
                                    }
                                }

                            }else if(retTWrkHst.getTableName().equals(DeviceCtrlConstants.t_command_load_survey_time)) {
                                List<TCommandLoadSurveyTime> tCmdTimeList = new ArrayList<>();
                                TCommandLoadSurveyTime tCmdTime = new TCommandLoadSurveyTime();
                                TCommandLoadSurveyTimePK tCmdTimePK = new TCommandLoadSurveyTimePK();
                                tCmdTimePK.setDevId(devId);
                                tCmdTimePK.setCommand(retTWrkHst.getId().getCommand());
                                tCmdTime.setId(tCmdTimePK);
                                tCmdTime.setSrvEnt(DeviceCtrlConstants.two.toString());
                                //t_commandに存在するかチェック
                                if(!tCommandLoadSurveyTimeDao.isNull(tCmdTime)) {
                                    //該当コマンドを全部積める
                                    tCmdTimeList.addAll(tCommandLoadSurveyTimeDao.getResultList(tCmdTime));

                                    //全てエラーに上書きする
                                    for(TCommandLoadSurveyTime retTCmdTime : tCmdTimeList) {
                                        //t_command_load_survey_meterテーブル srv_entを空文字にする
                                        tCommandTimeToNull(retTCmdTime);

                                        //t_work_hst srv_entを空文字にする
                                        tWrkHstToNull(retTWrkHst);
                                    }
                                }

                            }else {
                                error20(retTWrkHst, nodeMap);
                            }
                        }
                        endFlg = true;
                    }

                    tWrkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                    List<TWorkHst> tWrkHstOneList = new ArrayList<>();
                    //存在するかチェック
                    if(!tWorkHstDao.isNull(tWrkHst)) {
                        tWrkHstOneList.addAll(tWorkHstDao.getTWorkHstList(tWrkHst));
                        for(TWorkHst retTWrkHst : tWrkHstOneList) {
                            //t_work_hst srv_entを空文字にする
                            tWrkHstToNull(retTWrkHst);
                        }
                    }


                    if(dataErr.equals(DeviceCtrlConstants.err21)) {
                        endFlg = true;
                    }
                }
            }

            smCtrlLogger.info("code : " + codeName + " devId : " + devId);
            if(codeName.equals(DeviceCtrlConstants.none)) {
                smCtrlLogger.info("code : none endFlg = true");
                endFlg = true;
            }

            if(codeName.equals(DeviceCtrlConstants.noneWithVersion)) {
            	// <data>部から装置IDとバージョン情報を抽出し、DBに入れる処理
            	reportFwVersion(devId, nodeMap);
            	smCtrlLogger.info("code : none-with-version endFlg = true");
            	endFlg = true;
            }

            if(codeName.equals(DeviceCtrlConstants.busyCode)) {
                busyFlg = true;
            }

            if(busyFlg) {
                busyUpdate(devId);
            }



            if(!errorFlg[0] && !busyFlg && !endFlg) {
                if(!CheckUtility.isNullOrEmpty(codeName)) {
                    if(!codeName.equals(DeviceCtrlConstants.error)) {
                        TWorkHst retTWrkHst = new TWorkHst();
                        retTWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

                        //mail送信コマンド || tableNameがあれば通常処理
                        if(Arrays.asList(mailCmd).contains(codeName) || !CheckUtility.isNullOrEmpty(retTWrkHst.getTableName())) {
                            //装置idとpwが一致している　且　機器制御コマンドが記載されている　且　時刻が基準日より後の場合
                            commandCheck(devId, codeName, errorFlg, nodeMap, bodyString, errorCode);

                            // CSV用処理
                            // 応答電文から通知CSVデータを作成
                            devMethod.createCsvNotify(devId, doc);

                        }else {
                            //tableName != null && > 0 &&
                            //filePath != null && > 0    が存在すればcsv処理
                            if(!CheckUtility.isNullOrEmpty(retTWrkHst.getFilePath())) {
                                // CSV用処理
                                // 応答電文から応答CSVデータを作成
                                devMethod.createCsvResponse(devId, doc, retTWrkHst);
                            }
                        }
                    }
                }else{
                    //装置idとpwが一致している　且　機器制御コマンドが記載されていない　且　時刻が基準日より後の場合

                    //前回の通信が異常終了 && t_work_hstがきれいにリセットされないケースを考慮(デプロイ中に処理落ちしたケース考慮)
                    TWorkHstPK tWorkHstPK = new TWorkHstPK();
                    TWorkHst tWorkHst = new TWorkHst();
                    List<TWorkHst> tWorkList = new ArrayList<>();
                    tWorkHstPK.setDevId(devId);
                    tWorkHst.setId(tWorkHstPK);
                    //srv_ent=1のとき
                    tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                    if(!tWorkHstDao.isNull(tWorkHst)) {
                        tWorkList.addAll(tWorkHstDao.getTWorkHstList(tWorkHst));
                        for(TWorkHst ret : tWorkList) {
                            if(ret.getFilePath() == null) {
                                tWrkHstToNull(ret);
                            }
                        }
                    }

                    //srv_ent=2のとき
                    tWorkList.clear();
                    tWorkHst.setSrvEnt(DeviceCtrlConstants.two.toString());
                    if(!tWorkHstDao.isNull(tWorkHst)) {
                        tWorkList.addAll(tWorkHstDao.getTWorkHstList(tWorkHst));
                        for(TWorkHst ret : tWorkList) {
                            tWrkHstToNull(ret);
                        }
                    }

                    TCommandPK tCommandPK = new TCommandPK();
                    TCommand tCommand = new TCommand();
                    List<TCommand> tCommandList = new ArrayList<>();
                    tCommandPK.setDevId(devId);
                    tCommand.setId(tCommandPK);
                    //srv_ent=2のとき
                    tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
                    if(!tCommandDao.isNull(tCommand)) {
                        tCommandList.addAll(tCommandDao.getTCommandList(tCommand));
                        for(TCommand ret : tCommandList) {
                            if(!Arrays.asList(tCmdBulk).contains(ret.getId().getCommand())) {
                                tCommandToNull(ret);
                            }

                        }
                    }

                    TCommandLoadSurveyMeterPK tCommandMeterPK = new TCommandLoadSurveyMeterPK();
                    TCommandLoadSurveyMeter tCommandMeter = new TCommandLoadSurveyMeter();
                    List<TCommandLoadSurveyMeter> tCommandLoadSurveyMeterList = new ArrayList<>();
                    tCommandMeterPK.setDevId(devId);
                    tCommandMeter.setId(tCommandMeterPK);
                    //srv_ent=2のとき
                    tCommandMeter.setSrvEnt(DeviceCtrlConstants.two.toString());
                    if(!tCommandLoadSurveyMeterDao.isNull(tCommandMeter)) {
                        tCommandLoadSurveyMeterList.addAll(tCommandLoadSurveyMeterDao.getResultList(tCommandMeter));
                        for(TCommandLoadSurveyMeter ret : tCommandLoadSurveyMeterList) {
                                tCommandMeterToNull(ret);
                        }
                    }

                    TCommandLoadSurveyTimePK tCommandTimePK = new TCommandLoadSurveyTimePK();
                    TCommandLoadSurveyTime tCommandTime = new TCommandLoadSurveyTime();
                    List<TCommandLoadSurveyTime> tCommandLoadSurveyTimeList = new ArrayList<>();
                    tCommandTimePK.setDevId(devId);
                    tCommandTime.setId(tCommandTimePK);
                    //srv_ent=2のとき
                    tCommandTime.setSrvEnt(DeviceCtrlConstants.two.toString());
                    if(!tCommandLoadSurveyTimeDao.isNull(tCommandTime)) {
                        tCommandLoadSurveyTimeList.addAll(tCommandLoadSurveyTimeDao.getResultList(tCommandTime));
                        for(TCommandLoadSurveyTime ret : tCommandLoadSurveyTimeList) {
                                tCommandTimeToNull(ret);
                        }
                    }


                    //一括設定の展開(初回通信の時のみやる)
                    bulkCollectionToDeploy(devId);

                    //初回通信のときだけtWorkHstに登録する処理
                    tWorkHstPersistFirstConnection(devId);
                }
            }


            if(!errorFlg[0] && !endFlg){
                // CSV用処理
                // setフォルダにある要求CSVから処理予約テーブルに積む
                devMethod.setRequestFromCSV(devId);


                TWorkHst tWrkHst = new TWorkHst();
                TWorkHstPK tWrkHstPK = new TWorkHstPK();
                tWrkHstPK.setDevId(devId);
                tWrkHst.setId(tWrkHstPK);

                //処理待ちのものを探す
                tWrkHst.setSrvEnt(DeviceCtrlConstants.one.toString());

                if(tWorkHstDao.isNull(tWrkHst)) {
                    command = whatXml(command, responseDocument);
                }else {
                    TWorkHst retTWrkHst = new TWorkHst();
                    //先頭の処理するレコード(rec_dateが昔なもの)
                    retTWrkHst = tWorkHstDao.getTWorkHstList(tWrkHst).get(0);
                    String retCmd = retTWrkHst.getId().getCommand();

                    if(retTWrkHst.getFilePath() == null) {
                        //コマンドが t_commandテーブルに登録されるものならば
                        if(Arrays.asList(tCmd).contains(retCmd)) {
                            TCommand tCommand = new TCommand();
                            TCommandPK tCommandPK = new TCommandPK();
                            tCommandPK.setDevId(devId);
                            tCommandPK.setCommand(retCmd);
                            tCommand.setId(tCommandPK);
                            tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
                            if(!tCommandDao.isNull(tCommand)) {
                                TCommand retTCommand = new TCommand();
                                retTCommand = tCommandDao.getTCommandList(tCommand).get(0);
                                command = commandCheckXmlTCommand(responseDocument, command, retTCommand);

                                //一括設定のコマンド以外は 1→2
                                if(!Arrays.asList(tCmdBulk).contains(retCmd)) {
                                    //t_commandを処理中に更新(2)
                                    tCommandToTwo(retTCommand);
                                }
                                //t_work_hstは一括設定でも1→2に変更
                                //t_work_hstを処理中に更新(2)
                                tWrkHstOneToTwo(retTWrkHst);

                            }
                        }else if(Arrays.asList(tCmdMeter).contains(retCmd)) {
                            TCommandLoadSurveyMeter cmdMeter = new TCommandLoadSurveyMeter();
                            TCommandLoadSurveyMeterPK cmdMeterPK = new TCommandLoadSurveyMeterPK();
                            cmdMeterPK.setDevId(devId);
                            cmdMeter.setId(cmdMeterPK);

                            //処理待ちのものを探す
                            cmdMeter.setSrvEnt(DeviceCtrlConstants.one.toString());

                            if(!tCommandLoadSurveyMeterDao.isNull(cmdMeter)) {
                                TCommandLoadSurveyMeter retTCommandLoadSurveyMeter = new TCommandLoadSurveyMeter();
                                //先頭の処理するレコード(rec_dateが昔なもの)
                                retTCommandLoadSurveyMeter = tCommandLoadSurveyMeterDao.getResultList(cmdMeter).get(0);
                                command = commandCheckXmlTCommandMeter(responseDocument, command, retTCommandLoadSurveyMeter);

                                //t_work_hstを処理中に更新(2)
                                tWrkHstOneToTwo(retTWrkHst);

                                //t_command_load_survey_meterを処理中に更新(2)
                                tCommandMeterToTwo(retTCommandLoadSurveyMeter);
                            }

                        }else if(Arrays.asList(tCmdTime).contains(retCmd)) {
                            TCommandLoadSurveyTime cmdTime = new TCommandLoadSurveyTime();
                            TCommandLoadSurveyTimePK cmdTimePK = new TCommandLoadSurveyTimePK();
                            cmdTimePK.setDevId(devId);
                            cmdTime.setId(cmdTimePK);

                            //処理待ちのものを探す
                            cmdTime.setSrvEnt(DeviceCtrlConstants.one.toString());

                            if(!tCommandLoadSurveyTimeDao.isNull(cmdTime)) {
                                TCommandLoadSurveyTime retTCommandLoadSurveyTime = new TCommandLoadSurveyTime();
                                //先頭の処理するレコード(rec_dateが昔なもの)
                                retTCommandLoadSurveyTime = tCommandLoadSurveyTimeDao.getResultList(cmdTime).get(0);
                                command = commandCheckXmlTCommandTime(responseDocument, command, retTCommandLoadSurveyTime);

                                //t_work_hstを処理中に更新(2)
                                tWrkHstOneToTwo(retTWrkHst);

                                //t_command_load_survey_timeを処理中に更新(2)
                                tCommandTimeToTwo(retTCommandLoadSurveyTime);
                            }
                        }else {
                            //t_work_hstにしか登録されないもの
                            command = commandCheckXmlTWorkHst(responseDocument, command, retTWrkHst, errorFlg, errorCode);
                            tWrkHstOneToTwo(retTWrkHst);

                        }
                    }else {
                        //CSVのとき
                        TCommandPK retTCommandPK = new TCommandPK();
                        TCommand retTCommand = new TCommand();
                        String filePath = retTWrkHst.getFilePath();

                        //返却用レスポンス
                        CreateDeviceXmlResponse createDeviceXmlResponse = new CreateDeviceXmlResponse();

                        // ファイル内容取得
                        DeviceCtrlCsvConverter csv = new DeviceCtrlCsvConverter();
                        List<String> lineList = csv.listCsvColumn(filePath);
                        String[] column = lineList.get(0).split(",");
                        if(Arrays.asList(tCmd).contains(retCmd) || Arrays.asList(tCmdBulk).contains(retCmd)) {
                            if(!Arrays.asList(tCmdList).contains(retCmd)) {

                                String tag = column[1];
                                if(Arrays.asList(tCmdDay).contains(retCmd)) {
                                    tag = tag.replace("/", "");
                                }
                                retTCommand.setTag(tag);
                            }
                            retTCommandPK.setDevId(devId);
                            retTCommandPK.setCommand(retCmd);

                            retTCommand.setId(retTCommandPK);
                            command = commandCheckXmlTCommand(responseDocument, command, retTCommand);
                        }else if(Arrays.asList(cmdMeter).contains(retCmd)) {
                            MMeterPK retMeterPK = new MMeterPK();
                            MMeter retMeter = new MMeter();

                            switch(retCmd) {
                            case DeviceCtrlConstants.addMeter:
                                retMeterPK.setDevId(devId);
                                retMeterPK.setMeterMngId(Long.parseLong(column[1]));
                                retMeter.setId(retMeterPK);
                                retMeter.setMeterId(column[2]);
                                command = createDeviceXmlResponse.addMeterResponseTxt(responseDocument, command, retMeter, column[3]);
                                break;
                            case DeviceCtrlConstants.delMeter:
                                retMeterPK.setDevId(devId);
                                retMeterPK.setMeterMngId(Long.parseLong(column[1]));
                                retMeter.setId(retMeterPK);
                                command = createDeviceXmlResponse.delMeterResponseTxt(responseDocument, command, retMeter);
                                break;
                            case DeviceCtrlConstants.chgMeter:
                                retMeterPK.setDevId(devId);
                                retMeterPK.setMeterMngId(Long.parseLong(column[1]));
                                retMeter.setId(retMeterPK);
                                retMeter.setMeterId(column[3]);
                                retMeter.setMeterIdOld(column[2]);
                                command = createDeviceXmlResponse.chgMeterResponseTxt(responseDocument, command, retMeter);
                                break;
                            case DeviceCtrlConstants.setBreaker:
                                retMeterPK.setDevId(devId);
                                retMeterPK.setMeterMngId(Long.parseLong(column[1]));
                                retMeter.setId(retMeterPK);
                                retMeter.setOpenMode(column[2]);
                                command = createDeviceXmlResponse.setBreakerResponseTxt(responseDocument, command, retMeter);
                                break;
                            default:
                                break;
                            }

                        }else if(Arrays.asList(cmdConcentrator).contains(retCmd)) {
                            MConcentratorPK mConcentPK = new MConcentratorPK();
                            MConcentrator mConcent = new MConcentrator();
                            switch(retCmd) {
                            case DeviceCtrlConstants.addConcent:
                                mConcentPK.setDevId(devId);
                                mConcentPK.setConcentId(Long.parseLong(column[2]));
                                mConcent.setId(mConcentPK);
                                mConcent.setIpAddr(column[3]);
                                command =  createDeviceXmlResponse.addConcentResponseTxt(responseDocument, command, mConcent);
                                break;
                            case DeviceCtrlConstants.delConcent:
                                mConcentPK.setDevId(devId);
                                mConcentPK.setConcentId(Long.parseLong(column[2]));
                                mConcent.setId(mConcentPK);
                                command =  createDeviceXmlResponse.delConcentResponseTxt(responseDocument, command, mConcent);
                                break;
                            case DeviceCtrlConstants.initConcent:
                                mConcentPK.setDevId(devId);
                                mConcentPK.setConcentId(Long.parseLong(column[2]));
                                mConcent.setId(mConcentPK);
                                command =  createDeviceXmlResponse.initConcentResponseTxt(responseDocument, command, mConcent);
                                break;
                            default:
                                break;
                            }

                        }else if(Arrays.asList(cmdLoadlimit).contains(retCmd)) {
                            MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
                            MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
                            switch(retCmd) {
                            case DeviceCtrlConstants.setLoadlimit:
                                mMeterLoadlimitPK.setDevId(devId);
                                mMeterLoadlimitPK.setMeterMngId(Long.parseLong(column[1]));
                                mMeterLoadlimit.setId(mMeterLoadlimitPK);
                                mMeterLoadlimit.setLoadlimitMode(column[3]);
                                mMeterLoadlimit.setLoadCurrent(column[4]);
                                mMeterLoadlimit.setAutoInjection(column[5]);
                                mMeterLoadlimit.setBreakerActCount(column[6]);
                                mMeterLoadlimit.setCountClear(column[7]);
                                command = createDeviceXmlResponse.setLoadLimitResponseTxt(responseDocument, command, mMeterLoadlimit);
                                break;
                            default:
                                break;

                            }
                        }

                        tWrkHstOneToTwo(retTWrkHst);

                    }
                }
            }

            if(errorFlg[0]) {
                // CSV用処理
                // エラー発生時は_DMSTATE.csvを作成
                devMethod.createCsvState(devId);

                if(CheckUtility.isNullOrEmpty(errorCode[0])) {
                    errorCode[0] = DeviceCtrlConstants.boundaryValueErr;
                }

                if(errorCode[0].equals(DeviceCtrlConstants.syntaxErr)) {
                    //処理実行中にエラーが出た場合、また初期通信に戻るため
                    //t_work_hstのsrv_ent=1をsrv_ent=nullへ
                    TWorkHstPK tWorkHstPK = new TWorkHstPK();
                    TWorkHst tWorkHst = new TWorkHst();
                    List<TWorkHst> tWorkList = new ArrayList<>();
                    tWorkHstPK.setDevId(devId);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                    if(!tWorkHstDao.isNull(tWorkHst)) {
                        tWorkList.addAll(tWorkHstDao.getTWorkHstList(tWorkHst));
                        for(TWorkHst ret : tWorkList) {
                            tWrkHstToNull(ret);
                        }
                    }
                }

                ErrorResponse errorResponse = new ErrorResponse();
                command = errorResponse.responseTxt(errorCode[0], responseDocument, command);
            }else if(endFlg) {
                CreateDeviceXmlResponse endResponse = new CreateDeviceXmlResponse();
                command = endResponse.endResponseTxt(responseDocument, command);
            }


            contents.appendChild(command);
            response.setContentType("text/plain;charset=UTF-8");
            //responseのbodyに書き込む
            try (PrintWriter outBody = response.getWriter()){
                String responseRet = domToString(responseDocument);
                responseRet = responseRet.replace(DeviceCtrlConstants.xmlHeader, "");
                outBody.print(responseRet);

                smCtrlLogger.info("OUTPUT:\n".concat(responseRet));
            } catch(Exception e) {
                e.printStackTrace();
            }

        }catch(Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
        return;

    }

    public void bulkCollectionToDeploy(String devId) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();
        String linking = timestampToString(tCommandDao.getSvDate());

        tCommandPK.setDevId(devId);
        tCommand.setId(tCommandPK);
        tCommand.setSrvEnt(DeviceCtrlConstants.five.toString());

        if(!tCommandDao.isNull(tCommand)) {
            List<TCommand> tCommandList = new ArrayList<>();
            tCommandList.addAll(tCommandDao.getTCommandList(tCommand));
            for(TCommand retTCommand : tCommandList) {
                MMeter mMeter = new MMeter();
                MMeterPK mMeterPK = new MMeterPK();
                mMeterPK.setDevId(devId);
                mMeter.setId(mMeterPK);
                mMeter.setDelFlg(DeviceCtrlConstants.zero);
                List<MMeter> mMeterList = new ArrayList<>();

                MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
                MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
                mMeterLoadlimitPK.setDevId(devId);
                mMeterLoadlimit.setId(mMeterLoadlimitPK);


                switch(retTCommand.getId().getCommand()) {
                case DeviceCtrlConstants.getMeterinfo:
                    if(!mMeterDao.isNull(mMeter)) {
                        mMeterList.addAll(mMeterDao.getMMeterList(mMeter));
                        for(MMeter retMeter : mMeterList) {
                            //meterIdの先頭文字取得してAなら処理
                            if(retMeter.getMeterId().substring(0,1).equals(DeviceCtrlConstants.A)) {

                                TCommand retCommand = new TCommand();
                                TCommandPK retCommandPK = new TCommandPK();
                                retCommandPK.setDevId(devId);
                                retCommandPK.setCommand(DeviceCtrlConstants.getMeterinfo);
                                retCommandPK.setRecDate(mMeterDao.getSvDate());
                                retCommand.setId(retCommandPK);
                                retCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
                                retCommand.setLinking(linking);
                                retCommand.setTag(retMeter.getId().getMeterMngId().toString());
                                retCommand.setRecMan(DeviceCtrlConstants.firstConnection);
                                retCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
                                retCommand.setCreateDate(tInspectionMeterDao.getSvDate());
                                retCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
                                retCommand.setUpdateDate(tInspectionMeterDao.getSvDate());
                                tCommandDao.persist(retCommand);
                            }
                        }

                    }else {
                        errorLogger.error("登録されているメーターが存在しない");
                        tCommandToNine(retTCommand);
                    }
                    break;
                case DeviceCtrlConstants.getSpitconf:
                    if(!mMeterDao.isNull(mMeter)) {
                        mMeterList.addAll(mMeterDao.getMMeterList(mMeter));
                        for(MMeter retMeter : mMeterList) {
                            //meterIdの先頭文字取得してPなら処理
                            if(retMeter.getMeterId().substring(0,1).equals(DeviceCtrlConstants.P)) {

                                TCommand retCommand = new TCommand();
                                TCommandPK retCommandPK = new TCommandPK();
                                retCommandPK.setDevId(devId);
                                retCommandPK.setCommand(DeviceCtrlConstants.getSpitconf);
                                retCommandPK.setRecDate(mMeterDao.getSvDate());
                                retCommand.setId(retCommandPK);
                                retCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
                                retCommand.setLinking(linking);
                                retCommand.setTag(retMeter.getId().getMeterMngId().toString());
                                retCommand.setRecMan(DeviceCtrlConstants.firstConnection);
                                retCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
                                retCommand.setCreateDate(tInspectionMeterDao.getSvDate());
                                retCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
                                retCommand.setUpdateDate(tInspectionMeterDao.getSvDate());
                                tCommandDao.persist(retCommand);
                            }
                        }
                    }else {
                        errorLogger.error("登録されているメーターが存在しない");
                        tCommandToNine(retTCommand);
                    }
                    break;
                case DeviceCtrlConstants.getMeterctrl:
                    if(!mMeterDao.isNull(mMeter)) {
                        mMeterList.addAll(mMeterDao.getMMeterList(mMeter));
                        for(MMeter retMeter : mMeterList) {
                            //meterIdの先頭文字取得してAなら処理
                            if(retMeter.getMeterId().substring(0,1).equals(DeviceCtrlConstants.A)) {

                                TCommand retCommand = new TCommand();
                                TCommandPK retCommandPK = new TCommandPK();
                                retCommandPK.setDevId(devId);
                                retCommandPK.setCommand(DeviceCtrlConstants.getMeterctrl);
                                retCommandPK.setRecDate(mMeterDao.getSvDate());
                                retCommand.setId(retCommandPK);
                                retCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
                                retCommand.setLinking(linking);
                                retCommand.setTag(retMeter.getId().getMeterMngId().toString());
                                retCommand.setRecMan(DeviceCtrlConstants.firstConnection);
                                retCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
                                retCommand.setCreateDate(tInspectionMeterDao.getSvDate());
                                retCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
                                retCommand.setUpdateDate(tInspectionMeterDao.getSvDate());
                                tCommandDao.persist(retCommand);
                            }
                        }
                    }else {
                        errorLogger.error("登録されているメーターが存在しない");
                        tCommandToNine(retTCommand);
                    }
                    break;

                default:
                    break;

                }
                retTCommand.setLinking(linking);
                tCommandToTwo(retTCommand);
            }
        }

    }

    public void tWorkHstPersistFirstConnection(String devId) {
        MConcentrator mConcentrator = new MConcentrator();
        MConcentratorPK mConcentratorPK = new MConcentratorPK();
        mConcentratorPK.setDevId(devId);
        mConcentrator.setId(mConcentratorPK);
        mConcentrator.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!mConcentratorDao.isNull(mConcentrator)) {
            List<MConcentrator> mConcentratorList = new ArrayList<>();
            mConcentratorList.addAll(mConcentratorDao.getMConcentrotorList(mConcentrator));
            for(MConcentrator retConcentrator : mConcentratorList) {
                if(retConcentrator.getCommandFlg() ==null) {
                    continue;
                }
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(mConcentratorDao.getSvDate());
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.m_concentrator);

                switch(retConcentrator.getCommandFlg()) {
                case "1":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.addConcent);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "2":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.delConcent);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "3":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.initConcent);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                default:
                    break;
                }
            }
        }


        MMeter mMeter = new MMeter();
        MMeterPK mMeterPK = new MMeterPK();
        mMeterPK.setDevId(devId);
        mMeter.setId(mMeterPK);
        mMeter.setSrvEnt(DeviceCtrlConstants.one.toString());
        if(!mMeterDao.isNull(mMeter)) {
            List<MMeter> mMeterList = new ArrayList<>();
            mMeterList.addAll(mMeterDao.getMMeterList(mMeter));
            for(MMeter retMeter : mMeterList) {
                if(retMeter.getCommandFlg() ==null) {
                    continue;
                }
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(mMeterDao.getSvDate());
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.m_meter);

                switch(retMeter.getCommandFlg()) {
                case "1":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.addMeter);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "2":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.delMeter);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "3":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.chgMeter);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "4":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.setBreaker);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "5":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.setSpitconf);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                default:
                    break;
                }
            }
        }

        MRepeater mRepeater = new MRepeater();
        MRepeaterPK mRepeaterPK = new MRepeaterPK();
        mRepeaterPK.setDevId(devId);
        mRepeater.setId(mRepeaterPK);
        mRepeater.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!mRepeaterDao.isNull(mRepeater)) {
            List<MRepeater> mRepeaterList = new ArrayList<>();
            mRepeaterList.addAll(mRepeaterDao.getMRepeaterList(mRepeater));
            for(MRepeater retRepeater : mRepeaterList) {
                if(retRepeater.getCommandFlg() ==null) {
                    continue;
                }
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(mRepeaterDao.getSvDate());
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tInspectionMeterDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.m_repeater);

                switch(retRepeater.getCommandFlg()) {
                case "1":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.addRepeater);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                case "2":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.delRepeater);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                default:
                    break;
                }
            }
        }

        MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
        MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
        mMeterLoadlimitPK.setDevId(devId);
        mMeterLoadlimit.setId(mMeterLoadlimitPK);
        mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!mMeterLoadlimitDao.isNull(mMeterLoadlimit)) {
            List<MMeterLoadlimit> mMeterLoadlimitList = new ArrayList<>();
            mMeterLoadlimitList.addAll(mMeterLoadlimitDao.getMMeterLoadlimitList(mMeterLoadlimit));
            for(MMeterLoadlimit retMeterLoadlimit : mMeterLoadlimitList) {
                if(retMeterLoadlimit.getCommandFlg() ==null) {
                    continue;
                }
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(mMeterLoadlimitDao.getSvDate());
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setWriteDate(mMeterLoadlimitDao.getSvDate());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(mMeterLoadlimitDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(mMeterLoadlimitDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.m_meter_loadlimit);

                switch(retMeterLoadlimit.getCommandFlg()) {
                case "1":
                    tWorkHstPK.setCommand(DeviceCtrlConstants.setLoadlimit);
                    tWorkHst.setId(tWorkHstPK);
                    tWorkHstDao.persist(tWorkHst);
                    break;
                default:
                    break;
                }
            }
        }

        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();
        tCommandPK.setDevId(devId);
        tCommand.setId(tCommandPK);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!tCommandDao.isNull(tCommand)) {
            List<TCommand> tCommandList = new ArrayList<>();
            tCommandList.addAll(tCommandDao.getTCommandList(tCommand));
            for(TCommand retTCommand : tCommandList) {
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setCommand(retTCommand.getId().getCommand());
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(tCommandDao.getSvDate());
                tWorkHst.setId(tWorkHstPK);
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tCommandDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tCommandDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.t_command);
                tWorkHstDao.persist(tWorkHst);
            }
        }

        TCommandLoadSurveyMeter tCommandLoadSurveyMeter = new TCommandLoadSurveyMeter();
        TCommandLoadSurveyMeterPK tCommandLoadSurveyMeterPK = new TCommandLoadSurveyMeterPK();
        tCommandLoadSurveyMeterPK.setDevId(devId);
        tCommandLoadSurveyMeter.setId(tCommandLoadSurveyMeterPK);
        tCommandLoadSurveyMeter.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!tCommandLoadSurveyMeterDao.isNull(tCommandLoadSurveyMeter)) {
            List<TCommandLoadSurveyMeter> tCommandLoadSurveyMeterList = new ArrayList<>();
            tCommandLoadSurveyMeterList.addAll(tCommandLoadSurveyMeterDao.getResultList(tCommandLoadSurveyMeter));
            for(TCommandLoadSurveyMeter retCommandLoadSurveyMeter : tCommandLoadSurveyMeterList) {
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setCommand(retCommandLoadSurveyMeter.getId().getCommand());
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(tCommandDao.getSvDate());
                tWorkHst.setId(tWorkHstPK);
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tCommandDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tCommandDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.t_command_load_survey_meter);
                tWorkHstDao.persist(tWorkHst);
            }
        }


        TCommandLoadSurveyTime tCommandLoadSurveyTime = new TCommandLoadSurveyTime();
        TCommandLoadSurveyTimePK tCommandLoadSurveyTimePK = new TCommandLoadSurveyTimePK();
        tCommandLoadSurveyTimePK.setDevId(devId);
        tCommandLoadSurveyTime.setId(tCommandLoadSurveyTimePK);
        tCommandLoadSurveyTime.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!tCommandLoadSurveyTimeDao.isNull(tCommandLoadSurveyTime)) {
            List<TCommandLoadSurveyTime> tCommandLoadSurveyTimeList = new ArrayList<>();
            tCommandLoadSurveyTimeList.addAll(tCommandLoadSurveyTimeDao.getResultList(tCommandLoadSurveyTime));
            for(TCommandLoadSurveyTime retCommandLoadSurveyTime : tCommandLoadSurveyTimeList) {
                TWorkHst tWorkHst = new TWorkHst();
                TWorkHstPK tWorkHstPK = new TWorkHstPK();
                tWorkHstPK.setCommand(retCommandLoadSurveyTime.getId().getCommand());
                tWorkHstPK.setDevId(devId);
                tWorkHstPK.setRecDate(tCommandDao.getSvDate());
                tWorkHst.setId(tWorkHstPK);
                tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                tWorkHst.setRecMan(DeviceCtrlConstants.firstConnection);
                tWorkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                tWorkHst.setCreateDate(tCommandDao.getSvDate());
                tWorkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                tWorkHst.setUpdateDate(tCommandDao.getSvDate());
                tWorkHst.setTableName(DeviceCtrlConstants.t_command_load_survey_time);
                tWorkHstDao.persist(tWorkHst);
            }
        }



    }

    public Element commandCheckXmlTWorkHst(Document responseDocument, Element command, TWorkHst tWorkHst, boolean[] errorFlg, String[] errorCode) {
        String devId = tWorkHst.getId().getDevId();

        MConcentrator mConcentrator = new MConcentrator();
        MConcentratorPK mConcentratorPK = new MConcentratorPK();
        mConcentratorPK.setDevId(devId);
        mConcentrator.setId(mConcentratorPK);
        mConcentrator.setSrvEnt(DeviceCtrlConstants.one.toString());


        MMeter mMeter = new MMeter();
        MMeterPK mMeterPK = new MMeterPK();
        mMeterPK.setDevId(devId);
        mMeter.setId(mMeterPK);
        mMeter.setSrvEnt(DeviceCtrlConstants.one.toString());

        MRepeater mRepeater = new MRepeater();
        MRepeaterPK mRepeaterPK = new MRepeaterPK();
        mRepeaterPK.setDevId(devId);
        mRepeater.setId(mRepeaterPK);
        mRepeater.setSrvEnt(DeviceCtrlConstants.one.toString());

        MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
        MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
        mMeterLoadlimitPK.setDevId(devId);
        mMeterLoadlimit.setId(mMeterLoadlimitPK);
        mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.one.toString());

        CreateDeviceXmlResponse createDeviceXmlResponse = new CreateDeviceXmlResponse();
        String commandTag = tWorkHst.getId().getCommand();

        switch(commandTag) {
        case DeviceCtrlConstants.addConcent:
            mConcentrator.setCommandFlg(DeviceCtrlConstants.one.toString());
            if(mConcentratorDao.isNull(mConcentrator)) {
                errorFlg[0] = true;
            }else{
                mConcentrator = mConcentratorDao.getMConcentrotorList(mConcentrator).get(0);
                createDeviceXmlResponse.addConcentResponseTxt(responseDocument, command, mConcentrator);
            }
            break;
        case DeviceCtrlConstants.addMeter:
            mMeter.setCommandFlg(DeviceCtrlConstants.one.toString());
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
            }else{
                mMeter = mMeterDao.getMMeterList(mMeter).get(0);
                String buildingNo = searchTenantId(mMeter);
                createDeviceXmlResponse.addMeterResponseTxt(responseDocument, command, mMeter, buildingNo);
            }
            break;
        case DeviceCtrlConstants.addRepeater:
            mRepeater.setCommandFlg(DeviceCtrlConstants.one.toString());
            if(mRepeaterDao.isNull(mRepeater)) {
                errorFlg[0] = true;
            }else{
                mRepeater = mRepeaterDao.getMRepeaterList(mRepeater).get(0);
                createDeviceXmlResponse.addRepeaterResponseTxt(responseDocument, command, mRepeater);
            }
            break;
        case DeviceCtrlConstants.chgMeter:
            mMeter.setCommandFlg(DeviceCtrlConstants.three.toString());
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
            }else{
                mMeter = mMeterDao.getMMeterList(mMeter).get(0);
                createDeviceXmlResponse.chgMeterResponseTxt(responseDocument, command, mMeter);
            }
            break;
        case DeviceCtrlConstants.delConcent:
            mConcentrator.setCommandFlg(DeviceCtrlConstants.two.toString());
            if(mConcentratorDao.isNull(mConcentrator)) {
                errorFlg[0] = true;
            }else{
                mConcentrator = mConcentratorDao.getMConcentrotorList(mConcentrator).get(0);
                createDeviceXmlResponse.delConcentResponseTxt(responseDocument, command, mConcentrator);
            }
            break;
        case DeviceCtrlConstants.delMeter:
            mMeter.setCommandFlg(DeviceCtrlConstants.two.toString());
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
            }else{
                mMeter = mMeterDao.getMMeterList(mMeter).get(0);
                createDeviceXmlResponse.delMeterResponseTxt(responseDocument, command, mMeter);
            }
            break;
        case DeviceCtrlConstants.delRepeater:
            mRepeater.setCommandFlg(DeviceCtrlConstants.two.toString());
            if(mRepeaterDao.isNull(mRepeater)) {
                errorFlg[0] = true;
            }else{
                mRepeater = mRepeaterDao.getMRepeaterList(mRepeater).get(0);
                createDeviceXmlResponse.delRepeaterResponseTxt(responseDocument, command, mRepeater);
            }
            break;
        case DeviceCtrlConstants.initConcent:
            mConcentrator.setCommandFlg(DeviceCtrlConstants.three.toString());
            if(mConcentratorDao.isNull(mConcentrator)) {
                errorFlg[0] = true;
            }else{
                mConcentrator = mConcentratorDao.getMConcentrotorList(mConcentrator).get(0);
                createDeviceXmlResponse.initConcentResponseTxt(responseDocument, command, mConcentrator);
            }
            break;
        case DeviceCtrlConstants.setBreaker:
            mMeter.setCommandFlg(DeviceCtrlConstants.four.toString());
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
            }else{
                mMeter = mMeterDao.getMMeterList(mMeter).get(0);
                createDeviceXmlResponse.setBreakerResponseTxt(responseDocument, command, mMeter);
            }
            break;
        case DeviceCtrlConstants.setLoadlimit:
            mMeterLoadlimit.setCommandFlg(DeviceCtrlConstants.one.toString());
            if(mMeterLoadlimitDao.isNull(mMeterLoadlimit)) {
                errorFlg[0] = true;
            }else{
                mMeterLoadlimit = mMeterLoadlimitDao.getMMeterLoadlimitList(mMeterLoadlimit).get(0);
                createDeviceXmlResponse.setLoadLimitResponseTxt(responseDocument, command, mMeterLoadlimit);
            }
            break;
        case DeviceCtrlConstants.setSpitconf:
            mMeter.setCommandFlg(DeviceCtrlConstants.five.toString());
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
            }else{
                mMeter = mMeterDao.getMMeterList(mMeter).get(0);
                String buildingNo = searchTenantId(mMeter);
                createDeviceXmlResponse.setSpitResponseTxt(responseDocument, command, mMeter, buildingNo);
            }
            break;
        default:
            break;
        }

        if(errorFlg[0]) {
            errorCode[0] = DeviceCtrlConstants.noData;
            ErrorResponse errorResponse = new ErrorResponse();
            command = errorResponse.responseTxt(errorCode[0], responseDocument, command);
            tWrkHstToNine(tWorkHst);
        }

        return command;
    }

    public Element commandCheckXmlTCommand(Document responseDocument, Element command, TCommand tCommand) {
        CreateDeviceXmlResponse createDeviceXmlResponse = new CreateDeviceXmlResponse();
        //コマンド名
        String codeName = tCommand.getId().getCommand();

        switch(codeName) {
        case DeviceCtrlConstants.getAinspdata:
            return createDeviceXmlResponse.ainspResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getDlsdata:
            return createDeviceXmlResponse.dlsDataResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getDmvdata:
            return createDeviceXmlResponse.dmvDataResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getMinspdata:
            return createDeviceXmlResponse.minspResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdlsdata:
            return createDeviceXmlResponse.rDlsDataResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdmvdata:
            return createDeviceXmlResponse.rDmvDataResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getMeterctrl:
            return createDeviceXmlResponse.meterCtrlResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getMeterinfo:
            return createDeviceXmlResponse.meterInfoResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getMeterval:
            return createDeviceXmlResponse.meterValResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getSpitconf:
            return createDeviceXmlResponse.getSpitConfResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.setInterval:
            return createDeviceXmlResponse.intervalResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.meterList:
            return createDeviceXmlResponse.meterListResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.repeaterList:
            return createDeviceXmlResponse.repeaterListResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.concentList:
            return createDeviceXmlResponse.concentListResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.manualInspect:
            List<MManualInsp> mManualInspList = new ArrayList<>();
            MManualInsp mManualInsp = new MManualInsp();
            MManualInspPK mManualInspPK = new MManualInspPK();
            mManualInspPK.setDevId(tCommand.getId().getDevId());
            mManualInsp.setId(mManualInspPK);
            mManualInsp.setSrvEnt(DeviceCtrlConstants.one.toString());
            if(mManualInspDao.isNull(mManualInsp)) {
                tCommandToNine(tCommandDao.find(tCommand));
            }
            mManualInspList.addAll(mManualInspDao.getManualInspResultList(mManualInsp));
            return createDeviceXmlResponse.manualInspResponseTxt(responseDocument, command, mManualInspList, tCommand);
        default :
            break;
        }

        return command;
    }

    public Element commandCheckXmlTCommandMeter(Document responseDocument, Element command, TCommandLoadSurveyMeter tCommand) {
        CreateDeviceXmlResponse createDeviceXmlResponse = new CreateDeviceXmlResponse();
        String commandTag = tCommand.getId().getCommand();
        switch(commandTag) {
        case DeviceCtrlConstants.getDlsmeter:
            return createDeviceXmlResponse.dlsMeterResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdlsmeter:
            return createDeviceXmlResponse.rDlsMeterResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getDmvmeter:
            return createDeviceXmlResponse.dmvMeterResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdmvmeter:
            return createDeviceXmlResponse.rDmvMeterResponseTxt(responseDocument, command, tCommand);
        default:
            break;
        }
        return command;
    }

    public Element commandCheckXmlTCommandTime(Document responseDocument, Element command, TCommandLoadSurveyTime tCommand) {
        CreateDeviceXmlResponse createDeviceXmlResponse = new CreateDeviceXmlResponse();
        String commandTag = tCommand.getId().getCommand();
        switch(commandTag) {
        case DeviceCtrlConstants.getDlsperiod:
            return createDeviceXmlResponse.dlsPeriodResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdlsperiod:
            return createDeviceXmlResponse.rDlsPeriodResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getDmvperiod:
            return createDeviceXmlResponse.dmvPeriodResponseTxt(responseDocument, command, tCommand);
        case DeviceCtrlConstants.getRdmvperiod:
            return createDeviceXmlResponse.rDmvPeriodResponseTxt(responseDocument, command, tCommand);
        default:
            break;
        }
        return command;
    }

    public void commandCheck(String devId, String codeName, boolean[] errorFlg, Map<String,String> nodeMap, String bodyString, String[] errorCode) {
        smCtrlLogger.info("code : " + codeName + " devId : " + devId + "の処理を開始");

        List<List<String>> csvList = new ArrayList<>();
        String saveFilename = "";
        NodeList nodes;
        String inspMonth = "";
        String inspDate = "";
        String inspYear = "";
        errorFlg[0] = false;
        Long maxInspMonthNo = (long) 1;
        String meterMngIdXml = "";
        String requestDate = "";

        MConcentrator mConcentrator = new MConcentrator();
        MConcentratorPK mConcentratorPK = new MConcentratorPK();

        MMeter mMeter = new MMeter();
        MMeterPK mMeterPK = new MMeterPK();

        MRepeater mRepeater = new MRepeater();
        MRepeaterPK mRepeaterPK = new MRepeaterPK();

        TInspectionMeter tInspectionMeter = new TInspectionMeter();
        TInspectionMeterPK tInspectionMeterPK = new TInspectionMeterPK();

        TDayLoadSurvey tDayLoadSurvey = new TDayLoadSurvey();
        TDayLoadSurveyPK tDayLoadSurveyPK = new TDayLoadSurveyPK();

        TDayLoadSurveyRev tDayLoadSurveyRev = new TDayLoadSurveyRev();
        TDayLoadSurveyRevPK tDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();

        MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
        MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();

        MManualInsp mManualInsp = new MManualInsp();
        MManualInspPK mManualInspPK = new MManualInspPK();

        MMeterType mMeterType = new MMeterType();
        MMeterTypePK mMeterTypePK = new MMeterTypePK();

        Map<Long, String> meterTypeMap = new HashMap<>();
        List<MMeterType> meterTypeList = new ArrayList<>();

        MDevPrm mDevPrm = new MDevPrm();

        TWorkHst tWrkHst = new TWorkHst();
        TCommand tCommand = new TCommand();
        TCommandLoadSurveyMeter tCommandLoadSurveyMeter = new TCommandLoadSurveyMeter();
        TCommandLoadSurveyTime tCommandLoadSurveyTime = new TCommandLoadSurveyTime();

        String link = "";

        /** xmlのList **/
        List<TInspectionMeter> tInspectionMeterList = new ArrayList<>();

        /** xmlのList **/
        List<TDayLoadSurveyResultSet> tDayLoadSurveyResultSetList = new ArrayList<>();

        /** xmlのList **/
        List<TDayLoadSurvey> tDayLoadSurveyMeterList = new ArrayList<>();

        /** xmlのList **/
        List<TDayLoadSurveyRev> tDayLoadSurveyRevMeterList = new ArrayList<>();

        /** xmlのList **/
        List<MManualInsp> mManualInspList = new ArrayList<>();

        /** xmlのList **/
        List<MMeter> mMeterList = new ArrayList<>();

        /** xmlのList **/
        List<MRepeater> mRepeaterList = new ArrayList<>();

        /** dbのList **/
        List<TInspectionMeter> dbTInspectionMeterList = new ArrayList<>();

        /** dbのList **/
        List<MManualInsp> dbMManualInspList = new ArrayList<>();

        /**
         *  API サーバーのエンドポイント
         */
        String smsApiServerEndPoint = smsConfigs.getConfig(SmsConstants.OSOL_API_SERVER_ENDPOINT);

        /**
         *  API実行 企業ID
         */
        String smsApiLoginCorpId = smsConfigs.getConfig(SmsConstants.OSOL_API_LOGIN_CORP_ID);

        /**
         *  API実行 担当者
         */
        String smsApiLoginPersonId = smsConfigs.getConfig(SmsConstants.OSOL_API_LOGIN_PERSON_ID);

        /**
         *  API実行 操作企業
         */
        String smsApiOperationCorpId = smsConfigs.getConfig(SmsConstants.OSOL_API_OPERATION_CORP_ID);

        /**
         *  API実行 APIキー
         */
        String apiKey = null;

        /**
         *  API実行 ハッシュ値作成時刻
         */
        Long setMillisec = mMeterDao.getSvDate().getTime();

        /**
         *  API実行 ハッシュ値
         */
        String authHash = OsolApiAuthUtil.createAuthHash(smsApiLoginCorpId, smsApiLoginPersonId, String.valueOf(setMillisec));

        /**
         * authHashキーを生成
         */
        if (authHash == null) {
            errorFlg[0] = true;
            return;
        }

        //APIキー取得
        OsolApiKeyCreateResponse keyCreateResponse = new OsolApiKeyCreateResponse();
        OsolApiKeyCreateParameter keyCreateParameter = new OsolApiKeyCreateParameter();
        SmsDeviceGateway gateway = new SmsDeviceGateway();
        keyCreateParameter.setBean("OsolApiKeyCreateBean");
        keyCreateParameter.setLoginPersonId(smsApiLoginPersonId);
        keyCreateParameter.setLoginCorpId(smsApiLoginCorpId);
        keyCreateParameter.setOperationCorpId(smsApiOperationCorpId);
        keyCreateParameter.setMillisec(setMillisec);
        keyCreateParameter.setAuthHash(authHash);
        keyCreateResponse = (OsolApiKeyCreateResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON, keyCreateParameter, keyCreateResponse);
        apiKey = keyCreateResponse.getResult().getApiKey();

        switch(codeName) {
        case DeviceCtrlConstants.addConcent:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);



            mConcentratorPK.setConcentId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)));
            mConcentratorPK.setDevId(devId);
            mConcentrator.setId(mConcentratorPK);

            //登録フラグが立っているもの
            mConcentrator.setCommandFlg(DeviceCtrlConstants.one.toString());

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.concentId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ipAddr))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mConcentrator = mConcentratorDao.find(mConcentrator);
                mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mConcentratorDao.merge(mConcentrator);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }


            //対象レコードが存在するかどうか
            if(!mConcentratorDao.isNull(mConcentrator)) {
                mConcentrator = mConcentratorDao.find(mConcentrator);

                //バリデーションチェック
                if(concentValChk(codeName, mConcentrator,nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mConcentratorDao.merge(mConcentrator);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }


                mConcentrator.setCommandFlg(null);
                mConcentrator.setSrvEnt(null);
                mConcentrator.setIpAddr(nodeMap.get(DeviceCtrlConstants.ipAddr));
                mConcentrator.setRecMan(DeviceCtrlConstants.concentratorRecMan);
                mConcentrator.setRecDate(mConcentratorDao.getSvDate());
                mConcentrator.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mConcentrator.setUpdateDate(mConcentratorDao.getSvDate());
                mConcentratorDao.merge(mConcentrator);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.addMeter:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);


            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterPK.setDevId(devId);
            mMeter.setId(mMeterPK);

            //登録フラグが立っているもの
            mMeter.setCommandFlg(DeviceCtrlConstants.one.toString());
            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.tenantId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter = mMeterDao.find(mMeter);
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                return;
            }


            //対象レコードが存在するかどうか
            if(!mMeterDao.isNull(mMeter)) {
                mMeter = mMeterDao.find(mMeter);

                //バリデーションチェック
                if(meterValChk(codeName, mMeter, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterDao.merge(mMeter);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }


                mMeter = mMeterDao.find(mMeter);
                //フラグ落とす
                mMeter.setCommandFlg(null);
                mMeter.setSrvEnt(null);
                mMeterDao.merge(mMeter);

                tWrkHstToNull(tWorkHstDao.find(tWrkHst));

            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }
            break;

        case DeviceCtrlConstants.addRepeater:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mRepeaterPK.setRepeaterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.repeaterMngId)));
            mRepeaterPK.setDevId(devId);
            mRepeater.setId(mRepeaterPK);

            //登録フラグが立っているもの
            mRepeater.setCommandFlg(DeviceCtrlConstants.one.toString());

            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.repeaterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.repeaterId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mRepeater = mRepeaterDao.find(mRepeater);
                mRepeater.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mRepeaterDao.merge(mRepeater);
                return;
            }


            //対象レコードが存在するかどうか
            if(!mRepeaterDao.isNull(mRepeater)) {
                //レコードが存在すれば更新
                mRepeater = mRepeaterDao.find(mRepeater);

                //バリデーションチェック
                if(repeaterValChk(codeName, mRepeater, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mRepeater.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mRepeaterDao.merge(mRepeater);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                mRepeater.setCommandFlg(null);
                mRepeater.setSrvEnt(null);
                mRepeater.setRepeaterId(nodeMap.get(DeviceCtrlConstants.repeaterId));
                mRepeater.setRecMan(DeviceCtrlConstants.meterRecMan);
                mRepeater.setRecDate(mRepeaterDao.getSvDate());
                mRepeater.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mRepeater.setUpdateDate(mRepeaterDao.getSvDate());
                mRepeaterDao.merge(mRepeater);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.chgMeter:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter = mMeterDao.find(mMeter);
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }


            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterPK.setDevId(devId);
            mMeter.setId(mMeterPK);

            //登録フラグが立っているもの
            mMeter.setCommandFlg(DeviceCtrlConstants.three.toString());

            if(!mMeterDao.isNull(mMeter)) {
                mMeter = mMeterDao.find(mMeter);

                //バリデーションチェック
                if(meterValChk(codeName, mMeter, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterDao.merge(mMeter);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                mMeter.setCommandFlg(null);
                mMeter.setSrvEnt(null);

                mMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                mMeter.setRecDate(mMeterDao.getSvDate());
                mMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mMeter.setUpdateDate(mMeterDao.getSvDate());
                mMeterDao.merge(mMeter);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.concentList:
            //返答次第で処理変更
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            nodes = getXmlList(DeviceCtrlConstants.concentData, bodyString);

            //xmlの情報を格納するList
            List<MConcentrator> mConcentratorList = new ArrayList<>();

            //xmlの内容をListに詰めて行く
            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.concentId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ipAddr))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }

                MConcentratorPK retMConcentratorPK = new MConcentratorPK();
                MConcentrator retMConcentrator = new MConcentrator();


                retMConcentratorPK.setConcentId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)));
                retMConcentratorPK.setDevId(devId);
                retMConcentrator.setId(retMConcentratorPK);

                try {
                    retMConcentrator.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                retMConcentrator.setIpAddr(nodeMap.get(DeviceCtrlConstants.ipAddr));
                retMConcentrator.setRecMan(DeviceCtrlConstants.meterRecMan);
                retMConcentrator.setRecDate(mMeterDao.getSvDate());
                retMConcentrator.setUpdateUserId(DeviceCtrlConstants.update_userId);
                retMConcentrator.setUpdateDate(mMeterDao.getSvDate());
                retMConcentrator.setCreateDate(mMeterDao.getSvDate());
                retMConcentrator.setCreateUserId(DeviceCtrlConstants.create_userId);
                mConcentratorList.add(retMConcentrator);
            }


            for(MConcentrator ret : mConcentratorList) {
                //対象レコードを取りだす
                MConcentrator retMConcentrator = new MConcentrator();
                MConcentratorPK retMConcentratorPK = new MConcentratorPK();
                retMConcentratorPK.setDevId(devId);
                retMConcentratorPK.setConcentId(ret.getId().getConcentId());
                retMConcentrator.setId(retMConcentratorPK);
                if(mConcentratorDao.isNull(retMConcentrator)) {
                    mConcentratorDao.persist(ret);
                }else {
                    retMConcentrator = mConcentratorDao.find(ret);
                    retMConcentrator.setIpAddr(nodeMap.get(DeviceCtrlConstants.ipAddr));
                    retMConcentrator.setRecMan(DeviceCtrlConstants.meterRecMan);
                    retMConcentrator.setRecDate(mMeterDao.getSvDate());
                    retMConcentrator.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    retMConcentrator.setUpdateDate(mMeterDao.getSvDate());
                    mConcentratorDao.merge(retMConcentrator);
                }
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;

        case DeviceCtrlConstants.delConcent:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mConcentratorPK.setConcentId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)));
            mConcentratorPK.setDevId(devId);
            mConcentrator.setId(mConcentratorPK);

            //登録フラグが立っているもの
            mConcentrator.setCommandFlg(DeviceCtrlConstants.two.toString());

            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.concentId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mConcentrator = mConcentratorDao.find(mConcentrator);
                mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mConcentratorDao.merge(mConcentrator);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }


            //対象レコードが存在するかどうか
            if(!mConcentratorDao.isNull(mConcentrator)) {
                mConcentrator = mConcentratorDao.find(mConcentrator);

                //バリデーションチェック
                if(concentValChk(codeName, mConcentrator, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mConcentratorDao.merge(mConcentrator);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }


                //MConcentratorは物削
                mConcentratorDao.remove(mConcentrator);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.delMeter:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterPK.setDevId(devId);
            mMeter.setId(mMeterPK);

            //フラグが立っているもの
            mMeter.setCommandFlg(DeviceCtrlConstants.two.toString());

            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter = mMeterDao.find(mMeter);
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }


            if(!mMeterDao.isNull(mMeter)) {
                mMeter = mMeterDao.find(mMeter);

                //バリデーションチェック
                if(meterValChk(codeName, mMeter, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterDao.merge(mMeter);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                mMeter.setCommandFlg(null);
                mMeter.setSrvEnt(null);
                mMeter.setDelFlg(DeviceCtrlConstants.one);
                mMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                mMeter.setRecDate(mMeterDao.getSvDate());
                mMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mMeter.setUpdateDate(mMeterDao.getSvDate());

                //MMeterは論削
                mMeterDao.merge(mMeter);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.delRepeater:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mRepeaterPK.setRepeaterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.repeaterMngId)));
            mRepeaterPK.setDevId(devId);
            mRepeater.setId(mRepeaterPK);

            //フラグが立っているもの
            mRepeater.setCommandFlg(DeviceCtrlConstants.two.toString());


            //xmlチェック
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.repeaterMngId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mRepeater = mRepeaterDao.find(mRepeater);
                mRepeater.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mRepeaterDao.merge(mRepeater);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            //対象レコードが存在するかどうか
            if(!mRepeaterDao.isNull(mRepeater)) {
                mRepeater = mRepeaterDao.find(mRepeater);

                //バリデーションチェック
                if(repeaterValChk(codeName, mRepeater, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mRepeater.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mRepeaterDao.merge(mRepeater);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                //MRepeaterは物削
                mRepeaterDao.remove(mRepeater);
            }else {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.getAinspdata:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            inspYear = requestDate.substring(0,4);
            inspMonth = requestDate.substring(4,6);
            if(inspMonth.substring(0,1).equals(DeviceCtrlConstants.zero.toString())) {
                inspMonth = inspMonth.substring(1);
            }
            tInspectionMeterPK.setDevId(devId);
            tInspectionMeterPK.setInspYear(inspYear);
            tInspectionMeterPK.setInspMonth(inspMonth);
            tInspectionMeter.setId(tInspectionMeterPK);
            //DBのデータを格納
            dbTInspectionMeterList.addAll(tInspectionMeterDao.getTInspectionMeterList(tInspectionMeter));

            //xmlの内容をListに詰めて行く
            if(dbTInspectionMeterList.size() != DeviceCtrlConstants.zero) {
                //DBのinspMonthNoの最大値を格納
                for(int i = 0;i<dbTInspectionMeterList.size();i++) {
                    maxInspMonthNo = Math.max(maxInspMonthNo, dbTInspectionMeterList.get(i).getId().getInspMonthNo());
                }
                //DBのinspMonthNoの最大値+1を格納するのでプラスする
                maxInspMonthNo = maxInspMonthNo +1;
            }
            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.inspData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterType)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspVal1)) ||
                        CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspVal2)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.used1)) ||
                        CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.used2)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspDate))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;

                }
                TInspectionMeterPK retTInspectionMeterPK = new TInspectionMeterPK();
                TInspectionMeter retTInspectionMeter = new TInspectionMeter();

                retTInspectionMeterPK.setInspMonthNo(maxInspMonthNo);
                retTInspectionMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTInspectionMeterPK.setDevId(devId);
                retTInspectionMeterPK.setInspYear(inspYear);
                retTInspectionMeterPK.setInspMonth(inspMonth);
                retTInspectionMeter.setId(retTInspectionMeterPK);
                retTInspectionMeter.setInspType(DeviceCtrlConstants.a);
                retTInspectionMeter.setLatestInspVal(nodeMap.get(DeviceCtrlConstants.inspVal1).trim());
                retTInspectionMeter.setPrevInspVal(nodeMap.get(DeviceCtrlConstants.inspVal2).trim());
                try {
                    retTInspectionMeter.setMultipleRate(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    retTInspectionMeter.setLatestUseVal(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.used1)));
                    retTInspectionMeter.setPrevUseVal(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.used2)));

                    if(nodeMap.get(DeviceCtrlConstants.ratio).equals(DeviceCtrlConstants.emptyRatio) || nodeMap.get(DeviceCtrlConstants.ratio) == null) {
                        retTInspectionMeter.setUsePerRate(StringUtility.toBigDecimal(DeviceCtrlConstants.empty));
                    }else {
                        retTInspectionMeter.setUsePerRate(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ratio)));
                    }
                    retTInspectionMeter.setLatestInspDate(strToTimeStampHHMMSS((nodeMap.get(DeviceCtrlConstants.inspDate))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                retTInspectionMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                retTInspectionMeter.setRecDate(tInspectionMeterDao.getSvDate());
                retTInspectionMeter.setCreateUserId(DeviceCtrlConstants.create_userId);
                retTInspectionMeter.setCreateDate(tInspectionMeterDao.getSvDate());
                retTInspectionMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                retTInspectionMeter.setUpdateDate(tInspectionMeterDao.getSvDate());
                tInspectionMeterList.add(retTInspectionMeter);
            }

            for(int i=0;i<tInspectionMeterList.size();i++) {
                tInspectionMeterDao.persist(tInspectionMeterList.get(i));
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;

        case DeviceCtrlConstants.getDlsdata:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.requestDate))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }

            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);


            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                List<String> dataList = new ArrayList<>();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);


                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }

                dataList.add(devId);
                dataList.add(nodeMap.get(DeviceCtrlConstants.meterMngId));
                dataList.add(nodeMap.get(DeviceCtrlConstants.date));
                dataList.add(Objects.isNull(nodeMap.get(DeviceCtrlConstants.lsKwh)) ? "" : nodeMap.get(DeviceCtrlConstants.lsKwh));
                csvList.add(dataList);
            }
            if(csvList.size() != 0) {
                saveFilename = "load_survey_dls_" + devId + "_" + requestDate + ".csv";
                csvPrint(DeviceCtrlConstants.fPathCsv, saveFilename, "Windows-31J", csvList);
            }

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;

        case DeviceCtrlConstants.getDlsmeter:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyMeter = tCommandMeterTwoToEight(devId, codeName, errorFlg);

            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandMeterToNine(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
                    return;
                }

                retTDayLoadSurveyPK.setDevId(devId);
                retTDayLoadSurveyPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.lsKwh))) {
                        retTDayLoadSurvey.setKwh30(null);
                    }else {
                        retTDayLoadSurvey.setKwh30(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.lsKwh)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyMeterList.add(retTDayLoadSurvey);
            }

            //DBの情報を見るための設定
            tDayLoadSurveyPK.setDevId(devId);
            tDayLoadSurveyPK.setGetDate(requestDate);
            tDayLoadSurveyPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurvey.setId(tDayLoadSurveyPK);

            if(tDayLoadSurveyDao.isNull(tDayLoadSurvey)) {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyDao.persist(tDayLoadSurveyMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                    TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();

                    retTDayLoadSurveyPK.setDevId(devId);
                    retTDayLoadSurveyPK.setGetDate(tDayLoadSurveyMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyPK.setMeterMngId(tDayLoadSurveyMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                    if(tDayLoadSurveyDao.isNull(retTDayLoadSurvey)) {
                        retTDayLoadSurvey.setKwh30(tDayLoadSurveyMeterList.get(i).getKwh30());
                        retTDayLoadSurvey.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.persist(retTDayLoadSurvey);
                    }else {
                        retTDayLoadSurvey = tDayLoadSurveyDao.find(retTDayLoadSurvey);
                        retTDayLoadSurvey.setKwh30(tDayLoadSurveyMeterList.get(i).getKwh30());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.merge(retTDayLoadSurvey);
                    }
                }
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandMeterToNull(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
            break;

        case DeviceCtrlConstants.getDlsperiod:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyTime = tCommandTimeTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandTimeToNine(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
                    return;
                }

                retTDayLoadSurveyPK.setDevId(devId);
                retTDayLoadSurveyPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.lsKwh))) {
                        retTDayLoadSurvey.setKwh30(null);
                    }else {
                        retTDayLoadSurvey.setKwh30(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.lsKwh)));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyMeterList.add(retTDayLoadSurvey);
            }

            //DBの情報を見るための設定
            tDayLoadSurveyPK.setDevId(devId);
            tDayLoadSurveyPK.setGetDate(requestDate);
            tDayLoadSurveyPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurvey.setId(tDayLoadSurveyPK);

            if(tDayLoadSurveyDao.isNull(tDayLoadSurvey)) {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyDao.persist(tDayLoadSurveyMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                    TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();

                    retTDayLoadSurveyPK.setDevId(devId);
                    retTDayLoadSurveyPK.setGetDate(tDayLoadSurveyMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyPK.setMeterMngId(tDayLoadSurveyMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                    if(tDayLoadSurveyDao.isNull(retTDayLoadSurvey)) {
                        retTDayLoadSurvey.setKwh30(tDayLoadSurveyMeterList.get(i).getKwh30());
                        retTDayLoadSurvey.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.persist(retTDayLoadSurvey);
                    }else {
                        retTDayLoadSurvey = tDayLoadSurveyDao.find(retTDayLoadSurvey);
                        retTDayLoadSurvey.setKwh30(tDayLoadSurveyMeterList.get(i).getKwh30());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.merge(retTDayLoadSurvey);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandTimeToNull(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
            break;

        case DeviceCtrlConstants.getDmvdata:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.requestDate))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata,bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                List<String> dataList = new ArrayList<>();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }

                dataList.add(devId);
                dataList.add(nodeMap.get(DeviceCtrlConstants.meterMngId));
                dataList.add(nodeMap.get(DeviceCtrlConstants.date));
                dataList.add(Objects.isNull(nodeMap.get(DeviceCtrlConstants.kwh)) ? "" : nodeMap.get(DeviceCtrlConstants.kwh));
                csvList.add(dataList);
            }
            if(csvList.size() != 0) {
                saveFilename = "load_survey_dmv_" + devId + "_" + requestDate + ".csv";
                csvPrint(DeviceCtrlConstants.fPathCsv, saveFilename, "Windows-31J", csvList);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;
        case DeviceCtrlConstants.getDmvmeter:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyMeter = tCommandMeterTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata,bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandMeterToNine(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
                    return;
                }

                retTDayLoadSurveyPK.setDevId(devId);
                retTDayLoadSurveyPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.kwh))) {
                        retTDayLoadSurvey.setDmvKwh(null);
                    }else {
                        retTDayLoadSurvey.setDmvKwh(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.kwh)));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyMeterList.add(retTDayLoadSurvey);
            }

            //DBの情報を見るための設定
            tDayLoadSurveyPK.setDevId(devId);
            tDayLoadSurveyPK.setGetDate(requestDate);
            tDayLoadSurveyPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurvey.setId(tDayLoadSurveyPK);

            if(tDayLoadSurveyDao.isNull(tDayLoadSurvey)) {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyDao.persist(tDayLoadSurveyMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                    TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();

                    retTDayLoadSurveyPK.setDevId(devId);
                    retTDayLoadSurveyPK.setGetDate(tDayLoadSurveyMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyPK.setMeterMngId(tDayLoadSurveyMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                    if(tDayLoadSurveyDao.isNull(retTDayLoadSurvey)) {
                        retTDayLoadSurvey.setDmvKwh(tDayLoadSurveyMeterList.get(i).getDmvKwh());
                        retTDayLoadSurvey.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.persist(retTDayLoadSurvey);
                    }else {
                        retTDayLoadSurvey = tDayLoadSurveyDao.find(retTDayLoadSurvey);
                        retTDayLoadSurvey.setDmvKwh(tDayLoadSurveyMeterList.get(i).getDmvKwh());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.merge(retTDayLoadSurvey);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandMeterToNull(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
            break;
        case DeviceCtrlConstants.getDmvperiod:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyTime = tCommandTimeTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata,bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandTimeToNine(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
                    return;
                }

                retTDayLoadSurveyPK.setDevId(devId);
                retTDayLoadSurveyPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.kwh))) {
                        retTDayLoadSurvey.setDmvKwh(null);
                    }else {
                        retTDayLoadSurvey.setDmvKwh(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.kwh)));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyMeterList.add(retTDayLoadSurvey);
            }

            //DBの情報を見るための設定
            tDayLoadSurveyPK.setDevId(devId);
            tDayLoadSurveyPK.setGetDate(requestDate);
            tDayLoadSurveyPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurvey.setId(tDayLoadSurveyPK);

            if(tDayLoadSurveyDao.isNull(tDayLoadSurvey)) {
                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyDao.persist(tDayLoadSurveyMeterList.get(i));
                }
            }else {

                for(int i=0;i<tDayLoadSurveyMeterList.size();i++) {
                    TDayLoadSurveyPK retTDayLoadSurveyPK = new TDayLoadSurveyPK();
                    TDayLoadSurvey retTDayLoadSurvey = new TDayLoadSurvey();

                    retTDayLoadSurveyPK.setDevId(devId);
                    retTDayLoadSurveyPK.setGetDate(tDayLoadSurveyMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyPK.setMeterMngId(tDayLoadSurveyMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurvey.setId(retTDayLoadSurveyPK);
                    if(tDayLoadSurveyDao.isNull(retTDayLoadSurvey)) {
                        retTDayLoadSurvey.setDmvKwh(tDayLoadSurveyMeterList.get(i).getDmvKwh());
                        retTDayLoadSurvey.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.persist(retTDayLoadSurvey);
                    }else {
                        retTDayLoadSurvey = tDayLoadSurveyDao.find(retTDayLoadSurvey);
                        retTDayLoadSurvey.setDmvKwh(tDayLoadSurveyMeterList.get(i).getDmvKwh());
                        retTDayLoadSurvey.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurvey.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurvey.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyDao.merge(retTDayLoadSurvey);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandTimeToNull(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
            break;
        case DeviceCtrlConstants.getMeterctrl:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandOnetoEight(devId, codeName, errorFlg);
            link = tCommand.getLinking();

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.mode)) ||  CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.loadCurrent)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.autoInjection)) ||
                    CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.breakerActCount)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.countClear)) || nodeMap.get(DeviceCtrlConstants.mode).trim().length() == 0 || nodeMap.get(DeviceCtrlConstants.loadCurrent).trim().length() == 0 ||
                    nodeMap.get(DeviceCtrlConstants.autoInjection).trim().length() == 0 || nodeMap.get(DeviceCtrlConstants.breakerActCount).trim().length() == 0 || nodeMap.get(DeviceCtrlConstants.countClear).trim().length() == 0) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }


            mMeterLoadlimitPK.setDevId(devId);
            mMeterLoadlimitPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterLoadlimit.setId(mMeterLoadlimitPK);
            if(mMeterLoadlimitDao.isNull(mMeterLoadlimit)){
                mMeterPK.setDevId(devId);
                mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                mMeter.setId(mMeterPK);
                //m_meterに存在するか
                //存在しなければ登録できないので、登録するようにする。
                if(mMeterDao.isNull(mMeter)) {
                    errorFlg[0] = true;
                }
                mMeterLoadlimit.setLoadlimitMode(nodeMap.get(DeviceCtrlConstants.mode));
                mMeterLoadlimit.setLoadCurrent(nodeMap.get(DeviceCtrlConstants.loadCurrent));
                mMeterLoadlimit.setAutoInjection(nodeMap.get(DeviceCtrlConstants.autoInjection));
                mMeterLoadlimit.setBreakerActCount(nodeMap.get(DeviceCtrlConstants.breakerActCount));
                mMeterLoadlimit.setCountClear(nodeMap.get(DeviceCtrlConstants.countClear));
                mMeterLoadlimit.setCreateDate(mMeterDao.getSvDate());
                mMeterLoadlimit.setUpdateDate(mMeterDao.getSvDate());
                mMeterLoadlimit.setRecDate(mMeterDao.getSvDate());
                mMeterLoadlimit.setCreateUserId(DeviceCtrlConstants.create_userId);
                mMeterLoadlimit.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mMeterLoadlimit.setRecMan(DeviceCtrlConstants.meterRecMan);
                mMeterLoadlimitDao.persist(mMeterLoadlimit);
            }else {
                mMeterLoadlimit = mMeterLoadlimitDao.find(mMeterLoadlimit);
                mMeterLoadlimit.setLoadlimitMode(nodeMap.get(DeviceCtrlConstants.mode));
                mMeterLoadlimit.setLoadCurrent(nodeMap.get(DeviceCtrlConstants.loadCurrent));
                mMeterLoadlimit.setAutoInjection(nodeMap.get(DeviceCtrlConstants.autoInjection));
                mMeterLoadlimit.setBreakerActCount(nodeMap.get(DeviceCtrlConstants.breakerActCount));
                mMeterLoadlimit.setCountClear(nodeMap.get(DeviceCtrlConstants.countClear));
                mMeterLoadlimit.setUpdateDate(mMeterDao.getSvDate());
                mMeterLoadlimit.setRecDate(mMeterDao.getSvDate());
                mMeterLoadlimit.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mMeterLoadlimit.setRecMan(DeviceCtrlConstants.meterRecMan);
                mMeterLoadlimitDao.merge(mMeterLoadlimit);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));

            if(tCommandBulkSize(devId, codeName, link) == 0) {
                tCommandBulkToNull(devId, codeName, link);
            }

            break;

        case DeviceCtrlConstants.getMeterinfo:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandOnetoEight(devId, codeName, errorFlg);

            link = tCommand.getLinking();
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.concentId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.tenantId))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }


            mMeterPK.setDevId(devId);
            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeter.setId(mMeterPK);
            if(mMeterDao.isNull(mMeter)) {
                //登録処理
                InsertSmsMeterResponse smartResponse = new InsertSmsMeterResponse();
                InsertSmsMeterParameter smartParameter = new InsertSmsMeterParameter();

                smartParameter.setBean("InsertSmsMeterBean");
                InsertSmsMeterRequest request = new InsertSmsMeterRequest();

                request.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                request.setDevId(devId);


                try {
                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))) {
                        request.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                    }
                    request.setMeterType(Long.parseLong(DeviceCtrlConstants.one.toString()));
                    final String tenantIdStr = nodeMap.get(DeviceCtrlConstants.tenantId);
                    if (tenantIdStr != null) {
                        request.setTenantId(Long.parseLong(tenantIdStr));
                    }
                    request.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    request.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                smartParameter.setResult(request);
                // スマメ
                smartParameter.setMeterKind(MeterManagementConstants.METER_TYPE_SMART);
                smartParameter.setFromDeviceCtrl(true);
                smartParameter.setLoginCorpId(smsApiLoginCorpId);
                smartParameter.setOperationCorpId(smsApiOperationCorpId);
                smartParameter.setLoginPersonId(smsApiLoginPersonId);
                smartParameter.setAuthHash(authHash);
                smartParameter.setApiKey(apiKey);
                smartResponse = (InsertSmsMeterResponse) gateway.osolApiPost(
                        osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                        SmsApiGateway.PATH.JSON,
                        smartParameter,
                        smartResponse);
                if(!smartResponse.getResultCode().equals(DeviceCtrlConstants.apiSuccess)) {
                    errorFlg[0] = true;
                    tCommandBulkToNine(devId, codeName, link, errorFlg);
                    tCommandToNine(tCommandDao.find(tCommand));
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    return;
                }else {
                    tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    tCommandToNull(tCommandDao.find(tCommand));
                }

            }else {

                mMeter = mMeterDao.find(mMeter);
                mMeterLoadlimitPK.setDevId(devId);
                mMeterLoadlimitPK.setMeterMngId(mMeter.getId().getMeterMngId());
                mMeterLoadlimit.setId(mMeterLoadlimitPK);
                mMeterLoadlimit = mMeterLoadlimitDao.find(mMeterLoadlimit);

                //tenant情報などの変更がなければ値の更新だけを行う
                String tenantId = searchTenantId(mMeter);
                if(!nodeMap.get(DeviceCtrlConstants.tenantId).equals(tenantId)) {

                    //更新処理
                    UpdateSmsMeterResponse smartResponse = new UpdateSmsMeterResponse();
                    UpdateSmsMeterParameter smartParameter = new UpdateSmsMeterParameter();

                    smartParameter.setBean("UpdateSmsMeterBean");
                    UpdateSmsMeterRequest request = new UpdateSmsMeterRequest();

                    request.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                    request.setDevId(devId);
                    try {
                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))) {
                            request.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                        }
                        request.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));
                        request.setMeterType(mMeter.getMeterType());
                        request.setOpenMode(mMeter.getOpenMode());
                        request.setMemo(mMeter.getMemo());
                        request.setExamEndYm(mMeter.getExamEndYm());
                        request.setExamNotice(mMeter.getExamNotice());
                        request.setDispYearFlg(mMeter.getDispYearFlg());

                        request.setLoadlimitMode(mMeterLoadlimit.getLoadlimitMode());
                        request.setLoadCurrent(mMeterLoadlimit.getLoadCurrent());
                        request.setAutoInjection(mMeterLoadlimit.getAutoInjection());
                        request.setBreakerActCount(mMeterLoadlimit.getBreakerActCount());
                        request.setCountClear(mMeterLoadlimit.getCountClear());

                        request.setTempLoadCurrent(mMeterLoadlimit.getTempLoadCurrent());
                        request.setTempAutoInjection(mMeterLoadlimit.getTempAutoInjection());
                        request.setTempBreakerActCount(mMeterLoadlimit.getTempBreakerActCount());
                        request.setTempCountClear(mMeterLoadlimit.getTempCountClear());



                        request.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                        final String tenantIdStr = nodeMap.get(DeviceCtrlConstants.tenantId);
                        if (tenantIdStr != null) {
                        	if (!devId.startsWith(DEVICE_KIND.LTE.getVal())) {	// 2024-01-12
	                            request.setTenantId(Long.parseLong(tenantIdStr));
                        	// 2024-01-12
                        	}else {
                        		request.setTenantId(Long.parseLong(tenantId));	// LTE端末の場合はtenantIdを更新しない
                        	}	// ここまで
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    smartParameter.setResult(request);
                    // スマメ
                    smartParameter.setMeterKind(MeterManagementConstants.METER_TYPE_SMART);
                    smartParameter.setFromDeviceCtrl(true);
                    smartParameter.setUpdatePattern(SmsConstants.METER_UPDATE_PATTERN.ALL.getVal());
                    smartParameter.setLoginCorpId(smsApiLoginCorpId);
                    smartParameter.setOperationCorpId(smsApiOperationCorpId);
                    smartParameter.setLoginPersonId(smsApiLoginPersonId);
                    smartParameter.setAuthHash(authHash);
                    smartParameter.setApiKey(apiKey);
                    smartResponse = (UpdateSmsMeterResponse) gateway.osolApiPost(
                            osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                            SmsApiGateway.PATH.JSON,
                            smartParameter,
                            smartResponse);
                    if(!smartResponse.getResultCode().equals(DeviceCtrlConstants.apiSuccess)) {
                        tCommandBulkToNine(devId, codeName, link, errorFlg);
                        tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                        tCommandToNine(tCommandDao.find(tCommand));
                    }else {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                        tCommandToNull(tCommandDao.find(tCommand));
                    }
                }else {
                    try {
                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))) {
                            mMeter.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                        }

                        mMeter.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mMeterDao.merge(mMeter);

                    tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    tCommandToNull(tCommandDao.find(tCommand));
                }
            }
            if(tCommandBulkSize(devId, codeName, link) == 0) {
                tCommandBulkToNull(devId, codeName, link);
            }


            break;
        case DeviceCtrlConstants.getMeterval:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            requestDate = nodeMap.get(DeviceCtrlConstants.date);
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.measureDate)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.currentKwh1)) ||
                    CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.currentKwh2)) ||  CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.momentaryPwr)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.voltage12)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.voltage13)) ||
                    CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.voltage23)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ampere1)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ampere2)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ampere3))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return ;
            }

            try(FileWriter f = new FileWriter(DeviceCtrlConstants.fPathCsv + "meter_data_" + devId + "_" + requestDate + ".csv", false);
                    PrintWriter p = new PrintWriter(new BufferedWriter(f));) {

                // 出力ファイルの作成
                p.print(devId + "," + nodeMap.get(DeviceCtrlConstants.meterMngId) + "," + nodeMap.get(DeviceCtrlConstants.meterId) + "," + nodeMap.get(DeviceCtrlConstants.measureDate)
                + "," + nodeMap.get(DeviceCtrlConstants.currentKwh1) + "," + nodeMap.get(DeviceCtrlConstants.currentKwh2) + "," + nodeMap.get(DeviceCtrlConstants.momentaryPwr) + "," + nodeMap.get(DeviceCtrlConstants.voltage12)
                + "," + nodeMap.get(DeviceCtrlConstants.voltage13) + "," + nodeMap.get(DeviceCtrlConstants.voltage23) + "," + nodeMap.get(DeviceCtrlConstants.ampere1) + "," + nodeMap.get(DeviceCtrlConstants.ampere2) + "," + nodeMap.get(DeviceCtrlConstants.ampere3)
                + "," + nodeMap.get(DeviceCtrlConstants.circuitBreaker).trim() + "," + nodeMap.get(DeviceCtrlConstants.powerFactor) + "\n");
            }catch(Exception e) {
                e.printStackTrace();

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;
        case DeviceCtrlConstants.getMinspdata:

            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);

            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            inspYear = requestDate.substring(0,4);
            inspMonth = requestDate.substring(4,6);
            if(inspMonth.substring(0,1).equals(DeviceCtrlConstants.zero.toString())) {
                inspMonth = inspMonth.substring(1);
            }
            tInspectionMeterPK.setDevId(devId);
            tInspectionMeterPK.setInspYear(inspYear);
            tInspectionMeterPK.setInspMonth(inspMonth);
            tInspectionMeter.setId(tInspectionMeterPK);
            //DBのデータを格納
            dbTInspectionMeterList.addAll(tInspectionMeterDao.getTInspectionMeterList(tInspectionMeter));

            //xmlの内容をListに詰めて行く
            if(dbTInspectionMeterList.size() != DeviceCtrlConstants.zero) {
                //DBのinspMonthNoの最大値を格納
                for(int i = 0;i<dbTInspectionMeterList.size();i++) {
                    maxInspMonthNo = Math.max(maxInspMonthNo, dbTInspectionMeterList.get(i).getId().getInspMonthNo());
                }
                //DBのinspMonthNoの最大値+1を格納するのでプラスする
                maxInspMonthNo = maxInspMonthNo +1;
            }
            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.inspData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterType)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspVal1)) ||
                        CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspVal2)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.used1)) ||
                        CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.used2)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.inspDate))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;

                }
                TInspectionMeterPK retTInspectionMeterPK = new TInspectionMeterPK();
                TInspectionMeter retTInspectionMeter = new TInspectionMeter();

                retTInspectionMeterPK.setInspMonthNo(maxInspMonthNo);
                retTInspectionMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTInspectionMeterPK.setDevId(devId);
                retTInspectionMeterPK.setInspYear(inspYear);
                retTInspectionMeterPK.setInspMonth(inspMonth);
                retTInspectionMeter.setId(retTInspectionMeterPK);
                retTInspectionMeter.setInspType(DeviceCtrlConstants.m);
                retTInspectionMeter.setLatestInspVal(nodeMap.get(DeviceCtrlConstants.inspVal1).trim());
                retTInspectionMeter.setPrevInspVal(nodeMap.get(DeviceCtrlConstants.inspVal2).trim());
                try {
                    retTInspectionMeter.setMultipleRate(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    retTInspectionMeter.setLatestUseVal(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.used1)));
                    retTInspectionMeter.setPrevUseVal(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.used2)));

                    if(nodeMap.get(DeviceCtrlConstants.ratio).equals(DeviceCtrlConstants.emptyRatio) || nodeMap.get(DeviceCtrlConstants.ratio) == null) {
                        retTInspectionMeter.setUsePerRate(StringUtility.toBigDecimal(DeviceCtrlConstants.empty));
                    }else {
                        retTInspectionMeter.setUsePerRate(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ratio)));
                    }
                    retTInspectionMeter.setLatestInspDate(strToTimeStampHHMMSS((nodeMap.get(DeviceCtrlConstants.inspDate))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                retTInspectionMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                retTInspectionMeter.setRecDate(tInspectionMeterDao.getSvDate());
                retTInspectionMeter.setCreateUserId(DeviceCtrlConstants.create_userId);
                retTInspectionMeter.setCreateDate(tInspectionMeterDao.getSvDate());
                retTInspectionMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                retTInspectionMeter.setUpdateDate(tInspectionMeterDao.getSvDate());
                tInspectionMeterList.add(retTInspectionMeter);
            }

            for(int i=0;i<tInspectionMeterList.size();i++) {
                tInspectionMeterDao.persist(tInspectionMeterList.get(i));
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;
        case DeviceCtrlConstants.getRdlsdata:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);
            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.requestDate))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);


            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                List<String> dataList = new ArrayList<>();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }
                dataList.add(devId);
                dataList.add(nodeMap.get(DeviceCtrlConstants.meterMngId));
                dataList.add(nodeMap.get(DeviceCtrlConstants.date));
                dataList.add(Objects.isNull(nodeMap.get(DeviceCtrlConstants.lsKwh)) ? "" : nodeMap.get(DeviceCtrlConstants.lsKwh));
                csvList.add(dataList);
            }
            if(csvList.size() != 0) {
                saveFilename = "load_survey_rdls_" + devId + "_" + requestDate + ".csv";
                csvPrint(DeviceCtrlConstants.fPathCsv, saveFilename, "Windows-31J", csvList);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;
        case DeviceCtrlConstants.getRdlsmeter:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyMeter = tCommandMeterTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //DBの情報を見るための設定
            tDayLoadSurveyRevPK.setDevId(devId);
            tDayLoadSurveyRevPK.setGetDate(requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);



                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandMeterToNine(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
                    return;
                }

                retTDayLoadSurveyRevPK.setDevId(devId);
                retTDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyRevPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.lsKwh))) {
                        retTDayLoadSurveyRev.setKwh30(null);
                    }else {
                        retTDayLoadSurveyRev.setKwh30(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.lsKwh)));
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyRevMeterList.add(retTDayLoadSurveyRev);
            }

            tDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurveyRev.setId(tDayLoadSurveyRevPK);


            if(tDayLoadSurveyRevDao.isNull(tDayLoadSurveyRev)) {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyRevMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyRevMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyRevMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyRevDao.persist(tDayLoadSurveyRevMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                    TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();

                    retTDayLoadSurveyRevPK.setDevId(devId);
                    retTDayLoadSurveyRevPK.setGetDate(tDayLoadSurveyRevMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyRevPK.setMeterMngId(tDayLoadSurveyRevMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                    if(tDayLoadSurveyRevDao.isNull(retTDayLoadSurveyRev)) {
                        retTDayLoadSurveyRev.setKwh30(tDayLoadSurveyRevMeterList.get(i).getKwh30());
                        retTDayLoadSurveyRev.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.persist(retTDayLoadSurveyRev);
                    }else {
                        retTDayLoadSurveyRev = tDayLoadSurveyRevDao.find(retTDayLoadSurveyRev);
                        retTDayLoadSurveyRev.setKwh30(tDayLoadSurveyRevMeterList.get(i).getKwh30());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.merge(retTDayLoadSurveyRev);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandMeterToNull(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
            break;
        case DeviceCtrlConstants.getRdlsperiod:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyTime = tCommandTimeTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //DBの情報を見るための設定
            tDayLoadSurveyRevPK.setDevId(devId);
            tDayLoadSurveyRevPK.setGetDate(requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmLsdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);



                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandTimeToNine(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
                    return;
                }

                retTDayLoadSurveyRevPK.setDevId(devId);
                retTDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyRevPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.lsKwh))) {
                        retTDayLoadSurveyRev.setKwh30(null);
                    }else {
                        retTDayLoadSurveyRev.setKwh30(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.lsKwh)));
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyRevMeterList.add(retTDayLoadSurveyRev);
            }

            tDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurveyRev.setId(tDayLoadSurveyRevPK);


            if(tDayLoadSurveyRevDao.isNull(tDayLoadSurveyRev)) {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyRevMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyRevMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyRevMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyRevDao.persist(tDayLoadSurveyRevMeterList.get(i));
                }
            }else {

                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                    TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();

                    retTDayLoadSurveyRevPK.setDevId(devId);
                    retTDayLoadSurveyRevPK.setGetDate(tDayLoadSurveyRevMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyRevPK.setMeterMngId(tDayLoadSurveyRevMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                    if(tDayLoadSurveyRevDao.isNull(retTDayLoadSurveyRev)) {
                        retTDayLoadSurveyRev.setKwh30(tDayLoadSurveyRevMeterList.get(i).getKwh30());
                        retTDayLoadSurveyRev.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.persist(retTDayLoadSurveyRev);
                    }else {
                        retTDayLoadSurveyRev = tDayLoadSurveyRevDao.find(retTDayLoadSurveyRev);
                        retTDayLoadSurveyRev.setKwh30(tDayLoadSurveyRevMeterList.get(i).getKwh30());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.merge(retTDayLoadSurveyRev);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandTimeToNull(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
            break;
        case DeviceCtrlConstants.getRdmvdata:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.requestDate))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);


            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata,bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                List<String> dataList = new ArrayList<>();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }

                dataList.add(devId);
                dataList.add(nodeMap.get(DeviceCtrlConstants.meterMngId));
                dataList.add(nodeMap.get(DeviceCtrlConstants.date));
                dataList.add(Objects.isNull(nodeMap.get(DeviceCtrlConstants.kwh)) ? "" : nodeMap.get(DeviceCtrlConstants.kwh));
                csvList.add(dataList);
            }
            if(csvList.size() != 0) {
                saveFilename = "load_survey_rdmv_" + devId + "_" + requestDate + ".csv";
                csvPrint(DeviceCtrlConstants.fPathCsv, saveFilename, "Windows-31J", csvList);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;
        case DeviceCtrlConstants.getRdmvmeter:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyMeter = tCommandMeterTwoToEight(devId, codeName, errorFlg);
            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);

            //DBの情報を見るための設定
            tDayLoadSurveyRevPK.setDevId(devId);
            tDayLoadSurveyRevPK.setGetDate(requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata,bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);



                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandMeterToNine(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
                    return;
                }

                retTDayLoadSurveyRevPK.setDevId(devId);
                retTDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyRevPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.kwh))) {
                        retTDayLoadSurveyRev.setDmvKwh(null);
                    }else {
                        retTDayLoadSurveyRev.setDmvKwh(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.kwh)));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyRevMeterList.add(retTDayLoadSurveyRev);
            }

            tDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurveyRev.setId(tDayLoadSurveyRevPK);


            if(tDayLoadSurveyRevDao.isNull(tDayLoadSurveyRev)) {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyRevMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyRevMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyRevMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyRevDao.persist(tDayLoadSurveyRevMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                    TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();

                    retTDayLoadSurveyRevPK.setDevId(devId);
                    retTDayLoadSurveyRevPK.setGetDate(tDayLoadSurveyRevMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyRevPK.setMeterMngId(tDayLoadSurveyRevMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                    if(tDayLoadSurveyRevDao.isNull(retTDayLoadSurveyRev)) {
                        retTDayLoadSurveyRev.setDmvKwh(tDayLoadSurveyRevMeterList.get(i).getDmvKwh());
                        retTDayLoadSurveyRev.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.persist(retTDayLoadSurveyRev);
                    }else {
                        retTDayLoadSurveyRev = tDayLoadSurveyRevDao.find(retTDayLoadSurveyRev);
                        retTDayLoadSurveyRev.setDmvKwh(tDayLoadSurveyRevMeterList.get(i).getDmvKwh());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.merge(retTDayLoadSurveyRev);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandMeterToNull(tCommandLoadSurveyMeterDao.find(tCommandLoadSurveyMeter));
            break;
        case DeviceCtrlConstants.getRdmvperiod:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommandLoadSurveyTime = tCommandTimeTwoToEight(devId, codeName, errorFlg);

            requestDate = nodeMap.get(DeviceCtrlConstants.requestDate);
            //DBの情報を見るための設定
            tDayLoadSurveyRevPK.setDevId(devId);
            tDayLoadSurveyRevPK.setGetDate(requestDate);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.whmdata, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);



                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.date))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandTimeToNine(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
                    return;
                }

                retTDayLoadSurveyRevPK.setDevId(devId);
                retTDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTDayLoadSurveyRevPK.setGetDate(nodeMap.get(DeviceCtrlConstants.date));
                retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                try {
                    if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.kwh))) {
                        retTDayLoadSurveyRev.setDmvKwh(null);
                    }else {
                        retTDayLoadSurveyRev.setDmvKwh(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.kwh)));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                meterMngIdXml = nodeMap.get(DeviceCtrlConstants.meterMngId);
                tDayLoadSurveyRevMeterList.add(retTDayLoadSurveyRev);
            }

            tDayLoadSurveyRevPK.setMeterMngId(Long.parseLong(meterMngIdXml));
            tDayLoadSurveyRev.setId(tDayLoadSurveyRevPK);


            if(tDayLoadSurveyRevDao.isNull(tDayLoadSurveyRev)) {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    //xmlのデータをそのまま書き込む
                    tDayLoadSurveyRevMeterList.get(i).setCreateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setUpdateDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setRecDate(tDayLoadSurveyDao.getSvDate());
                    tDayLoadSurveyRevMeterList.get(i).setCreateUserId(DeviceCtrlConstants.create_userId);
                    tDayLoadSurveyRevMeterList.get(i).setUpdateUserId(DeviceCtrlConstants.update_userId);
                    tDayLoadSurveyRevMeterList.get(i).setRecMan(DeviceCtrlConstants.meterRecMan);
                    tDayLoadSurveyRevDao.persist(tDayLoadSurveyRevMeterList.get(i));
                }
            }else {
                for(int i=0;i<tDayLoadSurveyRevMeterList.size();i++) {
                    TDayLoadSurveyRevPK retTDayLoadSurveyRevPK = new TDayLoadSurveyRevPK();
                    TDayLoadSurveyRev retTDayLoadSurveyRev = new TDayLoadSurveyRev();

                    retTDayLoadSurveyRevPK.setDevId(devId);
                    retTDayLoadSurveyRevPK.setGetDate(tDayLoadSurveyRevMeterList.get(i).getId().getGetDate());
                    retTDayLoadSurveyRevPK.setMeterMngId(tDayLoadSurveyRevMeterList.get(i).getId().getMeterMngId());
                    retTDayLoadSurveyRev.setId(retTDayLoadSurveyRevPK);
                    if(tDayLoadSurveyRevDao.isNull(retTDayLoadSurveyRev)) {
                        retTDayLoadSurveyRev.setDmvKwh(tDayLoadSurveyRevMeterList.get(i).getDmvKwh());
                        retTDayLoadSurveyRev.setCreateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setCreateUserId(DeviceCtrlConstants.create_userId);
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.persist(retTDayLoadSurveyRev);
                    }else {
                        retTDayLoadSurveyRev = tDayLoadSurveyRevDao.find(retTDayLoadSurveyRev);
                        retTDayLoadSurveyRev.setDmvKwh(tDayLoadSurveyRevMeterList.get(i).getDmvKwh());
                        retTDayLoadSurveyRev.setUpdateDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setRecDate(tDayLoadSurveyDao.getSvDate());
                        retTDayLoadSurveyRev.setUpdateUserId(DeviceCtrlConstants.update_userId);
                        retTDayLoadSurveyRev.setRecMan(DeviceCtrlConstants.meterRecMan);
                        tDayLoadSurveyRevDao.merge(retTDayLoadSurveyRev);
                    }
                }

            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandTimeToNull(tCommandLoadSurveyTimeDao.find(tCommandLoadSurveyTime));
            break;
        case DeviceCtrlConstants.getSpitconf:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandOnetoEight(devId, codeName, errorFlg);
            link = tCommand.getLinking();

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }


            mMeterPK.setDevId(devId);
            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeter.setId(mMeterPK);
            if(mMeterDao.isNull(mMeter)) {
                //登録処理
                InsertSmsMeterResponse pulseResponse = new InsertSmsMeterResponse();
                InsertSmsMeterParameter pulseParameter = new InsertSmsMeterParameter();

                pulseParameter.setBean("InsertSmsMeterBean");
                InsertSmsMeterRequest request = new InsertSmsMeterRequest();

                request.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                request.setDevId(devId);
                request.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));

                try {
                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))){
                        request.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                    }
                    request.setMeterType(Long.parseLong(DeviceCtrlConstants.one.toString()));
                    if (!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.tenantId))) {
                        request.setTenantId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.tenantId)));
                    }

                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi))){
                        request.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    }

                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseWeight).trim())){
                        request.setPulseWeight(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.pulseWeight)).scaleByPowerOfTen(2));
                    }

                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.currentData).trim())){
                        request.setCurrentData(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.currentData)));
                    }

                    if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseType))){
                        request.setPulseType(nodeMap.get(DeviceCtrlConstants.pulseType));
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

                pulseParameter.setResult(request);
                // パルス
                pulseParameter.setMeterKind(MeterManagementConstants.METER_TYPE_PULSE);
                pulseParameter.setFromDeviceCtrl(true);
                pulseParameter.setLoginCorpId(smsApiLoginCorpId);
                pulseParameter.setOperationCorpId(smsApiOperationCorpId);
                pulseParameter.setLoginPersonId(smsApiLoginPersonId);
                pulseParameter.setAuthHash(authHash);
                pulseParameter.setApiKey(apiKey);
                pulseResponse = (InsertSmsMeterResponse) gateway.osolApiPost(
                        osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                        SmsApiGateway.PATH.JSON,
                        pulseParameter,
                        pulseResponse);
                if(!pulseResponse.getResultCode().equals(DeviceCtrlConstants.apiSuccess)) {
                    tCommandBulkToNine(devId, codeName, link, errorFlg);
                    tCommandToNine(tCommandDao.find(tCommand));
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                }else {
                    tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    tCommandToNull(tCommandDao.find(tCommand));
                }


            }else {
                //更新処理
                mMeter = mMeterDao.find(mMeter);

                //更新する内容が特になければフラグを落とすだけ。
                //画面側で更新されるはずなので本来必要ない処理ではあるが一応入れている
                String buildingNo = searchTenantId(mMeter);
                if(!nodeMap.get(DeviceCtrlConstants.tenantId).equals(buildingNo)) {

                    UpdateSmsMeterResponse pulseResponse = new UpdateSmsMeterResponse();
                    UpdateSmsMeterParameter pulseParameter = new UpdateSmsMeterParameter();

                    pulseParameter.setBean("UpdateSmsMeterBean");
                    UpdateSmsMeterRequest request = new UpdateSmsMeterRequest();

                    request.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                    request.setDevId(devId);


                    try {
                        request.setMeterType(mMeter.getMeterType());
                        request.setOpenMode(mMeter.getOpenMode());
                        request.setMemo(mMeter.getMemo());
                        request.setExamEndYm(mMeter.getExamEndYm());
                        request.setExamNotice(mMeter.getExamNotice());
                        request.setDispYearFlg(mMeter.getDispYearFlg());
                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))){
                            request.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                        }
                        request.setMeterType(Long.parseLong(DeviceCtrlConstants.one.toString()));
                        if (!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.tenantId))) {
                            request.setTenantId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.tenantId)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi))){
                            request.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseWeight).trim())){
                            request.setPulseWeight(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.pulseWeight)).scaleByPowerOfTen(2));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.currentData).trim())){
                            request.setCurrentData(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.currentData)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseType))){
                            request.setPulseType(nodeMap.get(DeviceCtrlConstants.pulseType));
                        }
                        request.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));

                        request.setPulseWeightChg(mMeter.getPulseWeightChg());
                        request.setPulseTypeChg(mMeter.getPulseTypeChg());
                        request.setCurrentDataChg(mMeter.getCurrentDataChg());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    pulseParameter.setResult(request);
                    // パルス
                    pulseParameter.setMeterKind(MeterManagementConstants.METER_TYPE_PULSE);
                    pulseParameter.setFromDeviceCtrl(true);
                    pulseParameter.setUpdatePattern(SmsConstants.METER_UPDATE_PATTERN.ALL.getVal());
                    pulseParameter.setLoginCorpId(smsApiLoginCorpId);
                    pulseParameter.setOperationCorpId(smsApiOperationCorpId);
                    pulseParameter.setLoginPersonId(smsApiLoginPersonId);
                    pulseParameter.setAuthHash(authHash);
                    pulseParameter.setApiKey(apiKey);
                    pulseResponse = (UpdateSmsMeterResponse) gateway.osolApiPost(
                            osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                            SmsApiGateway.PATH.JSON,
                            pulseParameter,
                            pulseResponse);
                    if(!pulseResponse.getResultCode().equals(DeviceCtrlConstants.apiSuccess)) {
                        tCommandBulkToNine(devId, codeName, link, errorFlg);
                        tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                        tCommandToNine(tCommandDao.find(tCommand));
                    }else {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                        tCommandToNull(tCommandDao.find(tCommand));
                    }
                }else {
                    try {
                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))){
                            mMeter.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi))){
                            mMeter.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseWeight).trim())){
                            mMeter.setPulseWeight(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.pulseWeight)).scaleByPowerOfTen(2));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.currentData).trim())){
                            mMeter.setCurrentData(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.currentData)));
                        }

                        if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.pulseType))){
                            mMeter.setPulseType(nodeMap.get(DeviceCtrlConstants.pulseType));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mMeterDao.merge(mMeter);
                    tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    tCommandToNull(tCommandDao.find(tCommand));
                }
            }
            if(tCommandBulkSize(devId, codeName, link) == 0) {
                tCommandBulkToNull(devId, codeName, link);
            }


            break;
        case DeviceCtrlConstants.initConcent:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mConcentratorPK.setDevId(devId);
            mConcentratorPK.setConcentId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)));
            mConcentrator.setId(mConcentratorPK);
            mConcentrator.setCommandFlg(DeviceCtrlConstants.three.toString());

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.concentId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.dm2Id))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mConcentrator = mConcentratorDao.find(mConcentrator);
                mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mConcentratorDao.merge(mConcentrator);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            if(mConcentratorDao.isNull(mConcentrator)) {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            mConcentrator = mConcentratorDao.find(mConcentrator);
            //バリデーションチェック
            if(concentValChk(codeName, mConcentrator, nodeMap)) {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mConcentratorDao.merge(mConcentrator);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return ;
            }
            mConcentrator.setSrvEnt(null);
            mConcentrator.setCommandFlg(null);
            mConcentrator.setUpdateDate(mConcentratorDao.getSvDate());
            mConcentrator.setRecDate(mConcentratorDao.getSvDate());
            mConcentrator.setUpdateUserId(DeviceCtrlConstants.update_userId);
            mConcentrator.setRecMan(DeviceCtrlConstants.meterRecMan);
            mConcentratorDao.merge(mConcentrator);

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.manualInspect:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.inspPoint, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                MManualInspPK retMManualInspPK = new MManualInspPK();
                MManualInsp retMManualInsp = new MManualInsp();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    return;
                }

                retMManualInspPK.setDevId(devId);
                retMManualInspPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retMManualInsp.setId(retMManualInspPK);
                retMManualInsp.setSrvEnt(null);
                retMManualInsp.setCommandFlg(null);
                mManualInspList.add(retMManualInsp);
            }

            mManualInspPK.setDevId(devId);
            mManualInsp.setId(mManualInspPK);
            mManualInsp.setCommandFlg(DeviceCtrlConstants.one.toString());
            if(mManualInspDao.isNull(mManualInsp)) {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }
            dbMManualInspList.addAll(mManualInspDao.getManualInspResultList(mManualInsp));

            for(int i=0;i<mManualInspList.size();i++) {
                if(!mManualInspList.get(i).getId().getMeterMngId().equals(dbMManualInspList.get(i).getId().getMeterMngId())) {
                    errorFlg[0] = true;
                    return;
                }
                mManualInsp = dbMManualInspList.get(i);
                mManualInsp.setCommandFlg(null);
                mManualInsp.setSrvEnt(null);
                mManualInsp.setRecDate(mConcentratorDao.getSvDate());
                mManualInsp.setRecMan(DeviceCtrlConstants.setManualInspRecMan);
                mManualInspDao.merge(mManualInsp);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;

        case DeviceCtrlConstants.meterList:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            // 2024-01-11 LTE端末接続メーター自動登録機能対応
            boolean isGetMeterinfo = false;
            try {
            	String tag = tCommand.getTag();
            	if(tag != null) {
            		if(tag.equals(DeviceCtrlConstants.getMeterinfo)) {
            			isGetMeterinfo = true;
            		}
            	}
            } catch(NullPointerException e) {
            	e.printStackTrace();
            }

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.meterData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                MMeterPK retMMeterPK = new MMeterPK();
                MMeter retMMeter = new MMeter();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId))) {
                    continue;	// xmlにメーター管理番号やメーターIDがない場合は、そのリストだけを破棄することでエラー終了させない。
                }
                retMMeterPK.setDevId(devId);
                retMMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retMMeter.setId(retMMeterPK);
                retMMeter.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));

                if(!CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.ifType))){
                    try {
                        retMMeter.setIfType(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.ifType)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        retMMeter.setIfType(BigDecimal.valueOf(0));
                    }
                } else {
                    retMMeter.setIfType(BigDecimal.valueOf(0));
                }
                mMeterList.add(retMMeter);
            }
            for(int i=0;i<mMeterList.size();i++) {
                MMeterPK retMMeterPK = new MMeterPK();
                MMeter retMMeter = new MMeter();
                retMMeterPK.setDevId(devId);
                retMMeterPK.setMeterMngId(mMeterList.get(i).getId().getMeterMngId());
                retMMeter.setId(retMMeterPK);
                retMMeter.setDelFlg(DeviceCtrlConstants.zero);
                if(mMeterDao.isNull(retMMeter)) {
                    //登録処理
                    InsertSmsMeterResponse meterResponse = new InsertSmsMeterResponse();
                    InsertSmsMeterParameter meterParameter = new InsertSmsMeterParameter();

                    meterParameter.setBean("InsertSmsMeterBean");
                    InsertSmsMeterRequest request = new InsertSmsMeterRequest();

                    request.setMeterMngId(mMeterList.get(i).getId().getMeterMngId());
                    request.setDevId(devId);
                    request.setIfType(mMeterList.get(i).getIfType());
                    request.setMeterId(mMeterList.get(i).getMeterId());
                    request.setMeterType(Long.parseLong(DeviceCtrlConstants.one.toString()));

                    meterParameter.setResult(request);
                    if(mMeterList.get(i).getMeterId().substring(0,1).equals(DeviceCtrlConstants.A)) {
                        // スマメ
                        meterParameter.setMeterKind(MeterManagementConstants.METER_TYPE_SMART);
                    }else {
                        meterParameter.setMeterKind(MeterManagementConstants.METER_TYPE_PULSE);
                    }
                    meterParameter.setFromDeviceCtrl(true);
                    meterParameter.setLoginCorpId(smsApiLoginCorpId);
                    meterParameter.setOperationCorpId(smsApiOperationCorpId);
                    meterParameter.setLoginPersonId(smsApiLoginPersonId);
                    meterParameter.setAuthHash(authHash);
                    meterParameter.setApiKey(apiKey);
                    meterResponse = (InsertSmsMeterResponse) gateway.osolApiPost(
                            osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                            SmsApiGateway.PATH.JSON,
                            meterParameter,
                            meterResponse);

                    if(!meterResponse.getResultCode().equals(DeviceCtrlConstants.apiSuccess)) {
                        errorFlg[0] = true;
                        tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                        tCommandToNine(tCommandDao.find(tCommand));
                        errorCode[0] = DeviceCtrlConstants.syntaxErr;
                        return;
                    }
                    if (isGetMeterinfo && devId.startsWith(DEVICE_KIND.LTE.getVal())) {		// 2024-02-06
                    	sendLteMeterAddMail(devId, mMeterList.get(i).getId().getMeterMngId(), mMeterList.get(i).getMeterId());	// 2024-02-06
                    }

                }else {
                    retMMeter = mMeterDao.find(retMMeter);
                    String orgMeterId = retMMeter.getMeterId();	// 2024-02-06
                    retMMeter.setMeterId(mMeterList.get(i).getMeterId());
                    retMMeter.setIfType(mMeterList.get(i).getIfType());
                    retMMeter.setUpdateDate(mMeterDao.getSvDate());
                    retMMeter.setRecDate(mMeterDao.getSvDate());
                    retMMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    retMMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                    mMeterDao.merge(retMMeter);
                    if (isGetMeterinfo && !mMeterList.get(i).getMeterId().equals(orgMeterId)) {		// 2024-02-06
                    	sendLteMeterChangeMail(devId, mMeterList.get(i).getId().getMeterMngId(), mMeterList.get(i).getMeterId(), orgMeterId);
                    }	// 2024-02-06 ここまで
                }
                if(isGetMeterinfo) {	// 2024-01-10
                	addMeterInfoCommand(devId, mMeterList.get(i).getId().getMeterMngId());	// 2024-01-10
                }
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));

            if(isGetMeterinfo) {	// 2023-12-28
            	tWorkHstPersistFirstConnection(devId);	// 2024-01-11
            }
            break;

        case DeviceCtrlConstants.repeaterList:

            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.repeaterData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                MRepeaterPK retMRepeaterPK = new MRepeaterPK();
                MRepeater retMRepeater = new MRepeater();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                //xmlのチェック
                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.repeaterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.repeaterId))) {
                    errorFlg[0] = true;
                    errorLogNull(devId,codeName);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    tCommandToNine(tCommandDao.find(tCommand));
                    errorLogger.error("xmlが不正です");
                    return;
                }
                retMRepeaterPK.setDevId(devId);
                retMRepeaterPK.setRepeaterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.repeaterMngId)));
                retMRepeater.setId(retMRepeaterPK);
                retMRepeater.setRepeaterId(nodeMap.get(DeviceCtrlConstants.repeaterId));

                mRepeaterList.add(retMRepeater);
            }
            for(int i=0;i<mRepeaterList.size();i++) {
                MRepeaterPK retMRepeaterPK = new MRepeaterPK();
                MRepeater retMRepeater = new MRepeater();
                retMRepeaterPK.setDevId(devId);
                retMRepeaterPK.setRepeaterMngId(mRepeaterList.get(i).getId().getRepeaterMngId());
                retMRepeater.setId(retMRepeaterPK);
                if(mRepeaterDao.isNull(retMRepeater)) {
                    retMRepeater.setRepeaterId(mRepeaterList.get(i).getRepeaterId());
                    retMRepeater.setUpdateDate(mRepeaterDao.getSvDate());
                    retMRepeater.setRecDate(mRepeaterDao.getSvDate());
                    retMRepeater.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    retMRepeater.setRecMan(DeviceCtrlConstants.meterRecMan);
                    retMRepeater.setCreateUserId(DeviceCtrlConstants.create_userId);
                    retMRepeater.setCreateDate(mRepeaterDao.getSvDate());
                    mRepeaterDao.persist(retMRepeater);
                }else {
                    retMRepeater = mRepeaterDao.find(retMRepeater);
                    retMRepeater.setRepeaterId(mRepeaterList.get(i).getRepeaterId());
                    retMRepeater.setUpdateDate(mRepeaterDao.getSvDate());
                    retMRepeater.setRecDate(mRepeaterDao.getSvDate());
                    retMRepeater.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    retMRepeater.setRecMan(DeviceCtrlConstants.meterRecMan);
                    mRepeaterDao.merge(retMRepeater);
                }
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            tCommandToNull(tCommandDao.find(tCommand));
            break;

        case DeviceCtrlConstants.sendConcenterr:

            MPauseMail pauseMailConcent = new MPauseMail();
            pauseMailConcent.setDevId(devId);
            if(!mPauseMailDao.isNull(pauseMailConcent)) {
                pauseMailConcent = mPauseMailDao.find(pauseMailConcent);
                if(pauseMailConcent.getConcentErr() != null && pauseMailConcent.getConcentErr().equals("1")) {
                    return;
                }
            }

            MDevRelation mDevRelationConcentErr = new MDevRelation();
            MDevRelationPK MDevRelationConcentErrPK = new MDevRelationPK();
            MDevRelationConcentErrPK.setDevId(devId);
            mDevRelationConcentErr.setId(MDevRelationConcentErrPK);

            MAlertMail mAlertMailConcentErr = new MAlertMail();
            MAlertMailPK mAlertMailConcentErrPK = new MAlertMailPK();
            mAlertMailConcentErrPK.setDevId(devId);
            mAlertMailConcentErr.setId(mAlertMailConcentErrPK);
            mAlertMailConcentErr.setAlertConcentErr(DeviceCtrlConstants.one.toString());
            List<MAlertMail> mAlertMailConcentErrList = new ArrayList<>();

            TBuilding tBuildingConcentErr = new TBuilding();
            TBuildingPK tBuildingConcentErrPK = new TBuildingPK();

            MBuildingSms mBuildingSmsConcentErr = new MBuildingSms();
            MBuildingSmsPK mBuildingSmsConcentErrPK = new MBuildingSmsPK();

            SendAlertMail sendAlertMailConcentErr = new SendAlertMail();

            MDevPrm mDevPrmConcentErr = new MDevPrm();
            mDevPrmConcentErr.setDevId(devId);
            if(mDevRelationDao.isNull(mDevRelationConcentErr)) {
                errorFlg[0] = true;
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                errorLogger.error("建物と装置が結びついていません");
                return;
            }
            mDevRelationConcentErr = mDevRelationDao.find(mDevRelationConcentErr);
            mAlertMailConcentErrList.addAll(mAlertMailDao.getMAlertMailList(mAlertMailConcentErr));

            tBuildingConcentErrPK.setCorpId(mDevRelationConcentErr.getId().getCorpId());
            tBuildingConcentErrPK.setBuildingId(mDevRelationConcentErr.getId().getBuildingId());
            tBuildingConcentErr.setId(tBuildingConcentErrPK);
            tBuildingConcentErr = tBuildingDao.find(tBuildingConcentErr);

            mBuildingSmsConcentErrPK.setCorpId(mDevRelationConcentErr.getId().getCorpId());
            mBuildingSmsConcentErrPK.setBuildingId(mDevRelationConcentErr.getId().getBuildingId());
            mBuildingSmsConcentErr.setId(mBuildingSmsConcentErrPK);
            mBuildingSmsConcentErr = mBuildingSmsDao.find(mBuildingSmsConcentErr);

            mDevPrmConcentErr = mDevPrmDao.find(mDevPrmConcentErr);

            List<SendConcentratorErrResultSet> sendConcentErrList = new ArrayList<>();

            //エラーが継続して出ているメーター
            List<SendConcentratorErrResultSet> concentContErrList = new ArrayList<>();
            List<MConcentrator> concentDBContErrList = new ArrayList<>();

            //DBの sta =0 || sta =null && xmlのsta = 1のときにメールの文面に含めないための対応
            List<String> concentIdStateList = new ArrayList<>();

            //メールアドレス詰めて行く
            List<String> toAddMailConcentErrList = new ArrayList<>();
            for(MAlertMail mail : mAlertMailConcentErrList) {
                if((mail.getDisabledFlg() == null || !mail.getDisabledFlg().equals(DeviceCtrlConstants.one.toString())) && (mail.getAlertConcentErr() != null && mail.getAlertConcentErr().equals(DeviceCtrlConstants.one.toString()))) {
                    toAddMailConcentErrList.add(mail.getEmail());
                }
            }


            sendAlertMailConcentErr.addMail(toAddMailConcentErrList, mBuildingSmsConcentErr);

            if(toAddMailConcentErrList.size() == DeviceCtrlConstants.zero) {
                errorFlg[0] = true;
                errorLogger.error("送信先情報が存在しません");
                return;
            }


            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.concentError, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);
                SendConcentratorErrResultSet sendConcentErr = new SendConcentratorErrResultSet();

                if(nodeMap.get(DeviceCtrlConstants.concentId) == null || nodeMap.get(DeviceCtrlConstants.concentState) == null) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return;
                }
                String retNo = String.format("%02d", Integer.parseInt(nodeMap.get(DeviceCtrlConstants.concentId)));
                sendConcentErr.setConcentId(retNo);
                sendConcentErr.setConcentState(nodeMap.get(DeviceCtrlConstants.concentState));

                //DBと状態が異なるかのチェック
                MConcentrator retConcentratorMail = new MConcentrator();
                MConcentratorPK retConcentratorMailPK = new MConcentratorPK();
                retConcentratorMailPK.setDevId(devId);
                retConcentratorMailPK.setConcentId(Long.parseLong(sendConcentErr.getConcentId()));
                retConcentratorMail.setId(retConcentratorMailPK);
                if(!mConcentratorDao.isNull(retConcentratorMail)) {
                    retConcentratorMail = mConcentratorDao.find(retConcentratorMail);

                    if((retConcentratorMail.getConcentSta() == null || retConcentratorMail.getConcentSta().toString().equals(DeviceCtrlConstants.zero.toString())) && nodeMap.get(DeviceCtrlConstants.concentState).equals(DeviceCtrlConstants.one.toString())) {
                        concentIdStateList.add(retConcentratorMail.getId().getConcentId().toString());
                    }

                    //nullだったら比べずに処理へ(差異が必ずあるため)
                    //nullじゃなければ比較して差異があれば処理
                    if(retConcentratorMail.getConcentSta() == null || !retConcentratorMail.getConcentSta().toString().equals(sendConcentErr.getConcentState())) {
                        try {
                            retConcentratorMail.setConcentSta(StringUtility.toBigDecimal(sendConcentErr.getConcentState()));
                            mConcentratorDao.merge(retConcentratorMail);
                            sendConcentErrList.add(sendConcentErr);

                        } catch (ParseException e) {
                            errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        }

                    }
                }
            }

            MConcentrator retMConcentratorMail = new MConcentrator();
            MConcentratorPK retMConcentratorMailPK = new MConcentratorPK();
            retMConcentratorMailPK.setDevId(devId);
            retMConcentratorMail.setId(retMConcentratorMailPK);
            try {
                retMConcentratorMail.setConcentSta(StringUtility.toBigDecimal(DeviceCtrlConstants.one.toString()));
            } catch (ParseException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
            concentDBContErrList.addAll(mConcentratorDao.getMConcentrotorList(retMConcentratorMail));
            for(MConcentrator retMConcentrator : concentDBContErrList) {
                if(concentIdStateList.contains(retMConcentrator.getId().getConcentId().toString())) {
                    continue;
                }
                SendConcentratorErrResultSet sendMeterErrResultSet = new SendConcentratorErrResultSet();
                String retNo = String.format("%02d", retMConcentrator.getId().getConcentId());
                sendMeterErrResultSet.setConcentId(retNo);
                concentContErrList.add(sendMeterErrResultSet);
            }


            //エラーで送信するべきコンセントレーターが無い場合はreturn
            if(sendConcentErrList.size() == 0) {
                requestLogger.info("異常状態の変更がありません");
                return;
            }

            //メール送信
            sendAlertMailConcentErr.alertMailConcentErr(toAddMailConcentErrList, tBuildingConcentErr, sendConcentErrList, mDevPrmConcentErr, concentContErrList);
            break;
            //メーター異常情報送信
        case DeviceCtrlConstants.sendMetererr:

            MPauseMail pauseMailMeter = new MPauseMail();
            pauseMailMeter.setDevId(devId);
            if(!mPauseMailDao.isNull(pauseMailMeter)) {
                pauseMailMeter = mPauseMailDao.find(pauseMailMeter);
                if(pauseMailMeter.getMeterErr() != null && pauseMailMeter.getMeterErr().equals("1")) {
                    return;
                }
            }

            MDevRelation mDevRelationMeterErr = new MDevRelation();
            MDevRelationPK MDevRelationMeterErrPK = new MDevRelationPK();
            MDevRelationMeterErrPK.setDevId(devId);
            mDevRelationMeterErr.setId(MDevRelationMeterErrPK);

            MAlertMail mAlertMailMeterErr = new MAlertMail();
            MAlertMailPK mAlertMailMeterErrPK = new MAlertMailPK();
            mAlertMailMeterErrPK.setDevId(devId);
            mAlertMailMeterErr.setId(mAlertMailMeterErrPK);
            List<MAlertMail> mAlertMailMeterErrList = new ArrayList<>();

            TBuilding tBuildingMeterErr = new TBuilding();
            TBuildingPK tBuildingMeterErrPK = new TBuildingPK();

            MBuildingSms mBuildingSmsMeterErr = new MBuildingSms();
            MBuildingSmsPK mBuildingSmsMeterErrPK = new MBuildingSmsPK();

            SendAlertMail sendAlertMailMeterErr = new SendAlertMail();

            MDevPrm mDevPrmMeterErr = new MDevPrm();
            mDevPrmMeterErr.setDevId(devId);

            mDevRelationMeterErr = mDevRelationDao.find(mDevRelationMeterErr);
            mAlertMailMeterErrList.addAll(mAlertMailDao.getMAlertMailList(mAlertMailMeterErr));

            tBuildingMeterErrPK.setCorpId(mDevRelationMeterErr.getId().getCorpId());
            tBuildingMeterErrPK.setBuildingId(mDevRelationMeterErr.getId().getBuildingId());
            tBuildingMeterErr.setId(tBuildingMeterErrPK);
            tBuildingMeterErr = tBuildingDao.find(tBuildingMeterErr);

            mBuildingSmsMeterErrPK.setCorpId(mDevRelationMeterErr.getId().getCorpId());
            mBuildingSmsMeterErrPK.setBuildingId(mDevRelationMeterErr.getId().getBuildingId());
            mBuildingSmsMeterErr.setId(mBuildingSmsMeterErrPK);
            mBuildingSmsMeterErr = mBuildingSmsDao.find(mBuildingSmsMeterErr);

            mDevPrmMeterErr = mDevPrmDao.find(mDevPrmMeterErr);

            //xmlの内容を詰めて行く
            List<SendMeterErrResultSet> sendMeterErrList = new ArrayList<>();

            //エラーが継続して出ているメーター
            List<SendMeterErrResultSet> meterContErrList = new ArrayList<>();
            List<MMeter> meterDBContErrList = new ArrayList<>();

            //DBの sta =0 || sta =null && xmlのsta = 1のときにメールの文面に含めないための対応
            List<String> meterMngIdStateList = new ArrayList<>();

            //メールアドレス詰めて行く
            List<String> toAddMailList = new ArrayList<>();

            for(MAlertMail mail : mAlertMailMeterErrList) {
                if((mail.getDisabledFlg() == null || !mail.getDisabledFlg().equals(DeviceCtrlConstants.one.toString())) && (mail.getAlertMeterErr() != null && mail.getAlertMeterErr().equals(DeviceCtrlConstants.one.toString()))) {
                    toAddMailList.add(mail.getEmail());
                }
            }
            sendAlertMailMeterErr.addMail(toAddMailList, mBuildingSmsMeterErr);

            if(toAddMailList.size() == DeviceCtrlConstants.zero) {
                errorFlg[0] = true;
                errorLogger.error("送信先情報が存在しません");
                return;
            }

            //建物IDを抜き出す(建物のみ テナントではない)
            MDevRelationJoinTBuildingResultSet retMJTBMeterErr = new MDevRelationJoinTBuildingResultSet();
            retMJTBMeterErr.setDevId(devId);
            retMJTBMeterErr = mDevJoinTBuildingRelationDao.find(retMJTBMeterErr);

            //メータータイプ全部抜き出し
            mMeterTypePK.setBuildingId(retMJTBMeterErr.getBuildingId());
            mMeterType.setId(mMeterTypePK);
            meterTypeList.addAll(mMeterTypeDao.getResultList(mMeterType));
            for(MMeterType ret : meterTypeList) {
                meterTypeMap.put(ret.getId().getMeterType(), ret.getMeterTypeName());
            }

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.meterError, bodyString);
            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);
                SendMeterErrResultSet sendMeterErr = new SendMeterErrResultSet();

                if(nodeMap.get(DeviceCtrlConstants.meterMngId) == null || nodeMap.get(DeviceCtrlConstants.meterState) == null) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return;
                }
                String retNo = String.format("%03d", Integer.parseInt(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                sendMeterErr.setMeterMngId(retNo);
                sendMeterErr.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));
                sendMeterErr.setMeterState(nodeMap.get(DeviceCtrlConstants.meterState));


                //DBと状態が異なるかのチェック
                MMeter mMeterErrMail = new MMeter();
                MMeterPK mMeterErrMailPK = new MMeterPK();
                mMeterErrMailPK.setDevId(devId);
                mMeterErrMailPK.setMeterMngId(Long.parseLong(sendMeterErr.getMeterMngId()));
                mMeterErrMail.setId(mMeterErrMailPK);
                mMeterErrMail.setDelFlg(DeviceCtrlConstants.zero);
                if(!mMeterDao.isNull(mMeterErrMail)) {
                    mMeterErrMail = mMeterDao.find(mMeterErrMail);

                    //停止フラグ経ってれば次のメーターへ
                    if(mMeterErrMail.getAlertPauseFlg() != null && mMeterErrMail.getAlertPauseFlg().toString().equals(DeviceCtrlConstants.one.toString())) {

                        //開始と終了がnullであれば次のメーターへ
                        if(mMeterErrMail.getAlertPauseStart() == null && mMeterErrMail.getAlertPauseEnd() == null) {
                            continue;
                        }

                        //期間内であれば項目に含めず次のメーターへ
                        if(!sendMailPeriod(mMeterErrMail.getAlertPauseStart(), mMeterErrMail.getAlertPauseEnd())) {
                            continue;
                        }
                    }

                    if((mMeterErrMail.getMeterSta() == null || mMeterErrMail.getMeterSta().toString().equals(DeviceCtrlConstants.zero.toString())) && nodeMap.get(DeviceCtrlConstants.meterState).equals(DeviceCtrlConstants.one.toString())) {
                        meterMngIdStateList.add(mMeterErrMail.getId().getMeterMngId().toString());
                    }

                    //nullだったら比べずに処理へ(差異が必ずあるため)
                    //nullじゃなければ比較して差異があれば処理
                    if(mMeterErrMail.getMeterSta() == null || !mMeterErrMail.getMeterSta().toString().equals(sendMeterErr.getMeterState())) {
                        try {
                            mMeterErrMail.setMeterSta(StringUtility.toBigDecimal(sendMeterErr.getMeterState()));
                            mMeterDao.merge(mMeterErrMail);
                            sendMeterErr.setTenantName(searchTenantName(mMeterErrMail));
                            sendMeterErr.setInspDate(searchLatestInspDateAfter(mMeterErrMail));
                            sendMeterErr.setMeterType(meterTypeMap.get(mMeterErrMail.getMeterType()));
                            sendMeterErrList.add(sendMeterErr);

                        } catch (ParseException e) {
                            errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        }
                    }
                }
            }

            MMeter retMMeterMail = new MMeter();
            MMeterPK retMMeterMailPK = new MMeterPK();
            retMMeterMailPK.setDevId(devId);
            retMMeterMail.setId(retMMeterMailPK);
            try {
                retMMeterMail.setMeterSta(StringUtility.toBigDecimal(DeviceCtrlConstants.one.toString()));
            } catch (ParseException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
            retMMeterMail.setDelFlg(DeviceCtrlConstants.zero);
            meterDBContErrList.addAll(mMeterDao.getMMeterList(retMMeterMail));
            for(MMeter retMMeter : meterDBContErrList) {
                if(meterMngIdStateList.contains(retMMeter.getId().getMeterMngId().toString())) {
                    continue;
                }

                //停止フラグ経ってれば次のメーターへ
                if(retMMeter.getAlertPauseFlg() != null && retMMeter.getAlertPauseFlg().toString().equals(DeviceCtrlConstants.one.toString())) {

                    //開始と終了がnullであれば次のメーターへ
                    if(retMMeter.getAlertPauseStart() == null && retMMeter.getAlertPauseEnd() == null) {
                        continue;
                    }

                    //期間内であれば項目に含めず次のメーターへ
                    if(!sendMailPeriod(retMMeter.getAlertPauseStart(), retMMeter.getAlertPauseEnd())) {
                        continue;
                    }

                }


                SendMeterErrResultSet sendMeterErrResultSet = new SendMeterErrResultSet();
                String retNo = String.format("%03d", retMMeter.getId().getMeterMngId());
                sendMeterErrResultSet.setMeterMngId(retNo);
                sendMeterErrResultSet.setMeterId(retMMeter.getMeterId());
                sendMeterErrResultSet.setMeterState(retMMeter.getMeterSta().toString());
                sendMeterErrResultSet.setTenantName(searchTenantName(retMMeter));
                sendMeterErrResultSet.setInspDate(searchLatestInspDateAfter(retMMeter));
                sendMeterErrResultSet.setMeterType(meterTypeMap.get(retMMeter.getMeterType()));
                meterContErrList.add(sendMeterErrResultSet);

            }

            if(sendMeterErrList.size() == 0) {
                requestLogger.info("異常状態の変更がありません");
                return;
            }
            sendAlertMailMeterErr.alertMailMeterErr(toAddMailList, tBuildingMeterErr, sendMeterErrList, mDevPrmMeterErr, meterContErrList);

            break;
        case DeviceCtrlConstants.sendTermerr:

            MPauseMail pauseMailTerm = new MPauseMail();
            pauseMailTerm.setDevId(devId);
            if(!mPauseMailDao.isNull(pauseMailTerm)) {
                pauseMailTerm = mPauseMailDao.find(pauseMailTerm);
                if(pauseMailTerm.getTermErr() != null && pauseMailTerm.getTermErr().equals("1")) {
                    return;
                }
            }

            MDevRelation mDevRelationTermErr = new MDevRelation();
            MDevRelationPK MDevRelationTermErrPK = new MDevRelationPK();
            MDevRelationTermErrPK.setDevId(devId);
            mDevRelationTermErr.setId(MDevRelationTermErrPK);

            MAlertMail mAlertMailTermErr = new MAlertMail();
            MAlertMailPK mAlertMailTermErrPK = new MAlertMailPK();
            mAlertMailTermErrPK.setDevId(devId);
            mAlertMailTermErr.setId(mAlertMailTermErrPK);
            List<MAlertMail> mAlertMailTermErrList = new ArrayList<>();

            TBuilding tBuildingTermErr = new TBuilding();
            TBuildingPK tBuildingTermErrPK = new TBuildingPK();

            MBuildingSms mBuildingSmsTermErr = new MBuildingSms();
            MBuildingSmsPK mBuildingSmsTermErrPK = new MBuildingSmsPK();

            SendAlertMail sendAlertMailTermErr = new SendAlertMail();

            MDevPrm mDevPrmTermErr = new MDevPrm();
            mDevPrmTermErr.setDevId(devId);
            mDevRelationTermErr = mDevRelationDao.find(mDevRelationTermErr);
            mAlertMailTermErrList.addAll(mAlertMailDao.getMAlertMailList(mAlertMailTermErr));

            tBuildingTermErrPK.setCorpId(mDevRelationTermErr.getId().getCorpId());
            tBuildingTermErrPK.setBuildingId(mDevRelationTermErr.getId().getBuildingId());
            tBuildingTermErr.setId(tBuildingTermErrPK);
            tBuildingTermErr = tBuildingDao.find(tBuildingTermErr);

            mBuildingSmsTermErrPK.setCorpId(mDevRelationTermErr.getId().getCorpId());
            mBuildingSmsTermErrPK.setBuildingId(mDevRelationTermErr.getId().getBuildingId());
            mBuildingSmsTermErr.setId(mBuildingSmsTermErrPK);
            mBuildingSmsTermErr = mBuildingSmsDao.find(mBuildingSmsTermErr);

            mDevPrmTermErr = mDevPrmDao.find(mDevPrmTermErr);

            List<SendTermErrResultSet> sendTermErrList = new ArrayList<>();

            //エラーが継続して出ているメーター
            List<SendTermErrResultSet> termContErrList = new ArrayList<>();
            List<MMeter> termDBContErrList = new ArrayList<>();

            //DBの sta =0 || sta =null && xmlのsta = 1のときにメールの文面に含めないための対応
            List<String> meterMngIdTermStateList = new ArrayList<>();

            //メールアドレス詰めて行く
            List<String> toAddMailTermErrList = new ArrayList<>();

            for(MAlertMail mail : mAlertMailTermErrList) {
                if((mail.getDisabledFlg() == null || !mail.getDisabledFlg().equals(DeviceCtrlConstants.one.toString())) &&
                        (mail.getAlertTermErr() != null && mail.getAlertTermErr().equals(DeviceCtrlConstants.one.toString()))) {
                    toAddMailTermErrList.add(mail.getEmail());
                }
            }
            sendAlertMailTermErr.addMail(toAddMailTermErrList, mBuildingSmsTermErr);

            if(toAddMailTermErrList.size() == DeviceCtrlConstants.zero) {
                errorFlg[0] = true;
                errorLogger.error("送信先情報が存在しません");
                return;
            }

            //建物IDを抜き出す(建物のみ テナントではない)
            MDevRelationJoinTBuildingResultSet retMJTBTermErr = new MDevRelationJoinTBuildingResultSet();
            retMJTBTermErr.setDevId(devId);
            retMJTBTermErr = mDevJoinTBuildingRelationDao.find(retMJTBTermErr);

            //メータータイプ全部抜き出し
            mMeterTypePK.setBuildingId(retMJTBTermErr.getBuildingId());
            mMeterType.setId(mMeterTypePK);
            meterTypeList.addAll(mMeterTypeDao.getResultList(mMeterType));
            for(MMeterType ret : meterTypeList) {
                meterTypeMap.put(ret.getId().getMeterType(), ret.getMeterTypeName());
            }

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.termError, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);
                SendTermErrResultSet sendTermErr = new SendTermErrResultSet();

                if(nodeMap.get(DeviceCtrlConstants.meterMngId) == null || nodeMap.get(DeviceCtrlConstants.termState) == null) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return;
                }
                String retNo = String.format("%03d", Integer.parseInt(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                sendTermErr.setMeterMngId(retNo);
                sendTermErr.setTermState(nodeMap.get(DeviceCtrlConstants.termState));


                //DBと状態が異なるかのチェック
                MMeter mMeterTermErrMail = new MMeter();
                MMeterPK mMeterTermErrMailPK = new MMeterPK();
                mMeterTermErrMailPK.setDevId(devId);
                mMeterTermErrMailPK.setMeterMngId(Long.parseLong(sendTermErr.getMeterMngId()));
                mMeterTermErrMail.setId(mMeterTermErrMailPK);
                mMeterTermErrMail.setDelFlg(DeviceCtrlConstants.zero);
                if(!mMeterDao.isNull(mMeterTermErrMail)) {
                    mMeterTermErrMail = mMeterDao.find(mMeterTermErrMail);

                    //停止フラグ経ってれば次のメーターへ
                    if(mMeterTermErrMail.getAlertPauseFlg() != null && mMeterTermErrMail.getAlertPauseFlg().toString().equals(DeviceCtrlConstants.one.toString())) {

                        //開始と終了がnullであれば次のメーターへ
                        if(mMeterTermErrMail.getAlertPauseStart() == null && mMeterTermErrMail.getAlertPauseEnd() == null) {
                            continue;
                        }

                        //期間内であれば項目に含めず次のメーターへ
                        if(!sendMailPeriod(mMeterTermErrMail.getAlertPauseStart(), mMeterTermErrMail.getAlertPauseEnd())) {
                            continue;
                        }
                    }

                    if((mMeterTermErrMail.getTermSta() == null || mMeterTermErrMail.getTermSta().toString().equals(DeviceCtrlConstants.zero.toString())) && nodeMap.get(DeviceCtrlConstants.termState).equals(DeviceCtrlConstants.one.toString())) {
                        meterMngIdTermStateList.add(mMeterTermErrMail.getId().getMeterMngId().toString());
                    }

                    //nullだったら比べずに処理へ(差異が必ずあるため)
                    //nullじゃなければ比較して差異があれば処理
                    if(mMeterTermErrMail.getTermSta() == null || !mMeterTermErrMail.getTermSta().toString().equals(sendTermErr.getTermState())) {
                        try {
                            mMeterTermErrMail.setTermSta(StringUtility.toBigDecimal(sendTermErr.getTermState()));
                            mMeterDao.merge(mMeterTermErrMail);
                            sendTermErr.setTenantName(searchTenantName(mMeterTermErrMail));
                            sendTermErr.setInspDate(searchLatestInspDateAfter(mMeterTermErrMail));
                            sendTermErr.setMeterType(meterTypeMap.get(mMeterTermErrMail.getMeterType()));
                            sendTermErrList.add(sendTermErr);

                        } catch (ParseException e) {
                            errorLogger.error(BaseUtility.getStackTraceMessage(e));
                        }
                    }
                }
            }

            MMeter retTermMail = new MMeter();
            MMeterPK retTermMailPK = new MMeterPK();
            retTermMailPK.setDevId(devId);
            retTermMail.setId(retTermMailPK);
            try {
                retTermMail.setTermSta(StringUtility.toBigDecimal(DeviceCtrlConstants.one.toString()));
            } catch (ParseException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
            retTermMail.setDelFlg(DeviceCtrlConstants.zero);
            termDBContErrList.addAll(mMeterDao.getMMeterList(retTermMail));
            for(MMeter retMMeter : termDBContErrList) {
                if(meterMngIdTermStateList.contains(retMMeter.getId().getMeterMngId().toString())) {
                    continue;
                }

                //停止フラグ経ってれば次のメーターへ
                if(retMMeter.getAlertPauseFlg() != null && retMMeter.getAlertPauseFlg().toString().equals(DeviceCtrlConstants.one.toString())) {

                    //開始と終了がnullであれば次のメーターへ
                    if(retMMeter.getAlertPauseStart() == null && retMMeter.getAlertPauseEnd() == null) {
                        continue;
                    }

                    //期間内であれば項目に含めず次のメーターへ
                    if(!sendMailPeriod(retMMeter.getAlertPauseStart(), retMMeter.getAlertPauseEnd())) {
                        continue;
                    }
                }

                SendTermErrResultSet sendTermErrResultSet = new SendTermErrResultSet();
                String retNo = String.format("%03d", retMMeter.getId().getMeterMngId());
                sendTermErrResultSet.setMeterMngId(retNo);
                sendTermErrResultSet.setTermState(retMMeter.getMeterSta().toString());
                sendTermErrResultSet.setTenantName(searchTenantName(retMMeter));
                sendTermErrResultSet.setInspDate(searchLatestInspDateAfter(retMMeter));
                sendTermErrResultSet.setMeterType(meterTypeMap.get(retMMeter.getMeterType()));

                termContErrList.add(sendTermErrResultSet);

            }

            if(sendTermErrList.size() == 0) {
                requestLogger.info("異常状態の変更がありません");
                return;
            }

            //メール送信
            sendAlertMailTermErr.alertMailTermErr(toAddMailTermErrList, tBuildingTermErr, sendTermErrList, mDevPrmTermErr, termContErrList);
            break;
        case DeviceCtrlConstants.setBreaker:

            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterPK.setDevId(devId);
            mMeter.setId(mMeterPK);

            //開閉フラグが立っているもの
            mMeter.setCommandFlg(DeviceCtrlConstants.four.toString());

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.mode))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter = mMeterDao.find(mMeter);
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }


            //対象レコードが存在するかどうか
            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }else {
                mMeter = mMeterDao.find(mMeter);

                //バリデーションチェック
                if(meterValChk(codeName, mMeter, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterDao.merge(mMeter);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                mMeter.setCommandFlg(null);
                mMeter.setSrvEnt(null);
                mMeter.setOpenMode(nodeMap.get(DeviceCtrlConstants.mode));
                mMeter.setRecMan(DeviceCtrlConstants.meterRecMan);
                mMeter.setRecDate(mMeterDao.getSvDate());
                mMeter.setUpdateUserId(DeviceCtrlConstants.update_userId);
                mMeter.setUpdateDate(mMeterDao.getSvDate());
                mMeterDao.merge(mMeter);
            }
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;
        case DeviceCtrlConstants.setInterval:
            tWrkHst = tWrkHstTwoToEight(devId, codeName, errorFlg);
            tCommand = tCommandTwoToEight(devId, codeName, errorFlg);

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.interval))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                tCommandToNine(tCommandDao.find(tCommand));
                return;
            }
            mDevPrm.setDevId(devId);

            if(mDevPrmDao.isNull(mDevPrm)) {
                errorFlg[0] = true;
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                return;
            }else {
                mDevPrm = mDevPrmDao.find(mDevPrm);
                try {
                    mDevPrm.setCommInterval(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.interval)));
                    mDevPrm.setUpdateDate(mDevPrmDao.getSvDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDevPrmDao.merge(mDevPrm);
            }
            tCommandToNull(tCommandDao.find(tCommand));
            tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            break;

        case DeviceCtrlConstants.setLoadlimit:

            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mMeterLoadlimitPK.setDevId(devId);
            mMeterLoadlimitPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeterLoadlimit.setId(mMeterLoadlimitPK);
            mMeterLoadlimit.setCommandFlg(DeviceCtrlConstants.one.toString());

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.mode)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.loadCurrent)) ||
                    CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.autoInjection)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.breakerActCount)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.countClear))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                mMeterLoadlimit = mMeterLoadlimitDao.find(mMeterLoadlimit);
                mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterLoadlimitDao.merge(mMeterLoadlimit);
                return;
            }

            if(mMeterLoadlimitDao.isNull(mMeterLoadlimit)) {
                errorFlg[0] = true;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;

            }else {
                boolean loadlimitFlg = false;
                mMeterLoadlimit = mMeterLoadlimitDao.find(mMeterLoadlimit);

                //バリデーションチェック
                if(loadlimitValChk(codeName, mMeterLoadlimit, nodeMap)) {
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterLoadlimitDao.merge(mMeterLoadlimit);
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;
                    return ;
                }

                if(nodeMap.get(DeviceCtrlConstants.mode).trim().length() > 0) {
                    if(nodeMap.get(DeviceCtrlConstants.mode).equals(DeviceCtrlConstants.A) || nodeMap.get(DeviceCtrlConstants.mode).equals(DeviceCtrlConstants.R)) {

                        if(!mMeterLoadlimit.getTempLoadCurrent().equals(nodeMap.get(DeviceCtrlConstants.loadCurrent))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getTempAutoInjection().equals(nodeMap.get(DeviceCtrlConstants.autoInjection))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getTempBreakerActCount().equals(nodeMap.get(DeviceCtrlConstants.breakerActCount))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getTempCountClear().equals(nodeMap.get(DeviceCtrlConstants.countClear))){
                            loadlimitFlg = true;
                        }

                    }else {

                        if(!mMeterLoadlimit.getLoadCurrent().equals(nodeMap.get(DeviceCtrlConstants.loadCurrent))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getAutoInjection().equals(nodeMap.get(DeviceCtrlConstants.autoInjection))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getBreakerActCount().equals(nodeMap.get(DeviceCtrlConstants.breakerActCount))){
                            loadlimitFlg = true;
                        }

                        if(!mMeterLoadlimit.getCountClear().equals(nodeMap.get(DeviceCtrlConstants.countClear))){
                            loadlimitFlg = true;
                        }

                    }
                }

                if(loadlimitFlg) {
                    mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterLoadlimit.setUpdateDate(mMeterLoadlimitDao.getSvDate());
                    mMeterLoadlimit.setRecDate(mMeterLoadlimitDao.getSvDate());
                    mMeterLoadlimit.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    mMeterLoadlimit.setRecMan(DeviceCtrlConstants.meterRecMan);
                    mMeterLoadlimitDao.merge(mMeterLoadlimit);
                    errorFlg[0] = true;
                    tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;

                }else {
                    mMeterLoadlimit.setCommandFlg(null);
                    mMeterLoadlimit.setSrvEnt(null);
                    mMeterLoadlimit.setUpdateDate(mMeterLoadlimitDao.getSvDate());
                    mMeterLoadlimit.setRecDate(mMeterLoadlimitDao.getSvDate());
                    mMeterLoadlimit.setUpdateUserId(DeviceCtrlConstants.update_userId);
                    mMeterLoadlimit.setRecMan(DeviceCtrlConstants.meterRecMan);
                    mMeterLoadlimitDao.merge(mMeterLoadlimit);
                    tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                }

            }


            break;
        case DeviceCtrlConstants.setSpitconf:
            tWrkHst = getTWrkHstTwo(devId, codeName, errorFlg);

            mMeterPK.setDevId(devId);
            mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
            mMeter.setId(mMeterPK);
            mMeter.setCommandFlg(DeviceCtrlConstants.five.toString());

            if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi))) {
                errorFlg[0] = true;
                errorLogNull(devId,codeName);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter = mMeterDao.find(mMeter);
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                return;
            }


            if(mMeterDao.isNull(mMeter)) {
                errorFlg[0] = true;
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            mMeter = mMeterDao.find(mMeter);

            //バリデーションチェック
            if(meterValChk(codeName, mMeter, nodeMap)) {
                errorFlg[0] = true;
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                tWrkHstToNine(tWorkHstDao.find(tWrkHst));
                mMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                mMeterDao.merge(mMeter);
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return ;
            }


            mMeter.setCommandFlg(null);
            mMeter.setSrvEnt(null);

            try {
                mMeter.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                mMeter.setPulseWeight(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.pulseWeight).trim()));
                mMeter.setCurrentData(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.currentData).trim()));
                mMeter.setPulseType(nodeMap.get(DeviceCtrlConstants.pulseType).trim());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mMeterDao.merge(mMeter);

            tWrkHstToNull(tWorkHstDao.find(tWrkHst));


            break;
        case DeviceCtrlConstants.sendMeteradd:	// 2023-12-25
        	addMeterlistCommand(devId);				// 2023-12-25
        	tWorkHstPersistFirstConnection(devId);	// 2024-01-11
        	break;
        default :
            break;
        }

        return ;
    }


    /**
     * LTE端末からのFWバージョン情報付きnoneからバージョン情報を抽出しDBに登録する
     * @param devId
     * @param nodeMap
     */
    public void reportFwVersion(String devId, Map<String,String> nodeMap) {
        try {
            // nodeMapからキー"ver"の値を抽出
        	String tagValue = nodeMap.get("ver");
            // <ver>の値が空、または値が4文字以外の場合はエラーログを出力して終了
            if (tagValue.isEmpty()) {
                errorLogger.error("FW ver is empty for devId: " + devId);
                return;
            }
        	if (tagValue.length() != 4) {
        		errorLogger.error("FW ver length error for devId: " + devId + " value: " + tagValue + ". The value must be exactly 4 characters.");
        		return;
        	}

            Integer reportedVersion;
            // tagValueをIntegerに変換。変換できない場合はエラーログを出力して終了
            try {
                reportedVersion = Integer.valueOf(tagValue);
            } catch (NumberFormatException e) {
                errorLogger.error("FW ver is not a valid number for devId: " + devId + " value: " + tagValue);
                return;
            }
            smCtrlLogger.info("Reported FWversion from " + devId + " is " + reportedVersion);

            // dev_id = devIdのレコードを取得
            MDevFwVersion searchList = new MDevFwVersion();
            searchList.setDevId(devId);
            MDevFwVersion resultRecord = mDevFwVersionDao.getRecord(searchList);

            // レコードがなかった場合、レコード追加
            if (resultRecord == null) {
                smCtrlLogger.info("The record does not exist yet.");
                MDevFwVersion insertRecord = new MDevFwVersion();
                insertRecord.setDevId(devId);
                insertRecord.setCurrentFwVer(reportedVersion);
                insertRecord.setUpdateFlg(false);

                mDevFwVersionDao.insertRecord(insertRecord);
                smCtrlLogger.info("New record for " + devId + " has been inserted into m_dev_fw_version");

                return;
            }

            // レコードがあった場合
            Integer currentVersion = resultRecord.getCurrentFwVer();
            smCtrlLogger.info("The value of current_fw_ver in m_dev_fw_version is " + currentVersion);

            // DBのcurrent_fw_verと報告されたバージョンが違う値だったら
            if (!currentVersion.equals(reportedVersion)) {
                smCtrlLogger.info("The values are different.");

                // 取得したレコードを更新用に再利用
                Timestamp prevUpdateDate = resultRecord.getLatestUpdateDate();    // 取得レコードのlast_update_dateをprev_update_dateとして定義
                resultRecord.setCurrentFwVer(reportedVersion);
                resultRecord.setLatestUpdateDate(mDevFwVersionDao.getSvDate());
                resultRecord.setPrevUpdateDate(prevUpdateDate);

                mDevFwVersionDao.updateRecord(resultRecord);
                smCtrlLogger.info("It has been updated.");

                return;
            } else {
                smCtrlLogger.info("The values are the same. No update was performed.");
                return;
            }
        }catch(Exception e) {
            errorLogger.error("FW ver cannot be retrieved. The import process of " + devId + " will be skipped. : " + e);
            return;
        }
    }


    /**
     * LTE端末からのsend-meteradd受信に対するmeter-listコマンド登録
     * @param devId
    **/
    public void addMeterlistCommand(String devId) {	// 2023-12-25 -> 2024-02-06修正
    	Date date = new Date();
    	Timestamp serverDateTime = new Timestamp(date.getTime());

    	TCommand retCommand = new TCommand();
    	TCommandPK retCommandPK = new TCommandPK();
    	retCommandPK.setDevId(devId);
    	retCommandPK.setCommand(DeviceCtrlConstants.meterList);
    	retCommand.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
    	retCommand.setId(retCommandPK);
    	if (!tCommandDao.isNull(retCommand)) {
    		retCommand.setTag(DeviceCtrlConstants.getMeterinfo);
    		tCommandDao.merge(retCommand);
    	}
    	else {
    		TCommand tCommand = new TCommand();
    		TCommandPK tCommandPK = new TCommandPK();
    		tCommandPK.setDevId(devId);
	    	tCommandPK.setCommand(DeviceCtrlConstants.meterList);
	    	tCommandPK.setRecDate(serverDateTime);
	    	tCommand.setId(tCommandPK);
	    	tCommand.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
	    	tCommand.setTag(DeviceCtrlConstants.getMeterinfo);
	    	tCommand.setRecMan(DeviceCtrlConstants.lteAutoRegist);
	    	tCommand.setVersion(0);
	    	tCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
	    	tCommand.setCreateDate(serverDateTime);
	    	tCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
	    	tCommand.setUpdateDate(serverDateTime);
	    	tCommandDao.persist(tCommand);
    	}
    }

    /**
     * LTE端末からのmeter-list受信に対するget-meterinfoコマンド登録
     * @param devId
     * @param mMeterMngId
    **/
    public void addMeterInfoCommand(String devId, Long mMeterMngId) {		// 2024-01-10
    	Date date = new Date();
    	Timestamp serverDateTime = new Timestamp(date.getTime());

    	TCommand tCommand = new TCommand();
    	TCommandPK tCommandPK = new TCommandPK();
    	tCommandPK.setDevId(devId);
    	tCommandPK.setCommand(DeviceCtrlConstants.getMeterinfo);
    	tCommandPK.setRecDate(serverDateTime);
    	tCommand.setId(tCommandPK);
    	tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
    	tCommand.setTag(mMeterMngId.toString());
    	tCommand.setRecMan(DeviceCtrlConstants.lteAutoRegist);
    	tCommand.setVersion(0);
    	tCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
    	tCommand.setCreateDate(serverDateTime);
    	tCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
    	tCommand.setUpdateDate(serverDateTime);
    	tCommandDao.persist(tCommand);
    }

    /**
     * コンセントレーターのバリデ
     * trueの場合はxmlとDBに差異がありエラーとする
     * @param codeName
     * @param mConcentrator
     * @return
     */
    public  boolean concentValChk(String codeName, MConcentrator mConcentrator, Map<String,String> nodeMap) {

        switch(codeName) {
        case DeviceCtrlConstants.addConcent:
            if(!mConcentrator.getIpAddr().equals(nodeMap.get(DeviceCtrlConstants.ipAddr)) || !mConcentrator.getId().getConcentId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mConcentrator.getId().getDevId());
                errorLogger.error("concentId : " + mConcentrator.getId().getConcentId());
                return true;
            }
            break;
        case DeviceCtrlConstants.delConcent:
            if(!mConcentrator.getId().getConcentId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)))){
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mConcentrator.getId().getDevId());
                errorLogger.error("concentId : " + mConcentrator.getId().getConcentId());
                return true;
            }
            break;
        case DeviceCtrlConstants.initConcent:
            if(!mConcentrator.getId().getConcentId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.concentId)))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mConcentrator.getId().getDevId());
                errorLogger.error("concentId : " + mConcentrator.getId().getConcentId());
                return true;
            }
            break;

        default:
            return false;
        }
        return false;
    }

    /**
     * メーターのバリデ
     * trueの場合はxmlとDBに差異がありエラーとする
     * @param codeName
     * @param mMeter
     * @return
     */
    public  boolean meterValChk(String codeName, MMeter mMeter, Map<String,String> nodeMap) {

        String buildingNo = "";

        switch(codeName) {
        case DeviceCtrlConstants.addMeter:
            buildingNo = searchTenantId(mMeter);
            if(!mMeter.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId))) || !mMeter.getMeterId().equals(nodeMap.get(DeviceCtrlConstants.meterId)) ||
                    !buildingNo.equals(nodeMap.get(DeviceCtrlConstants.tenantId))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeter.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                return true;
            }
            break;
        case DeviceCtrlConstants.delMeter:
            if(!mMeter.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)))){
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeter.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                return true;
            }
            break;
        case DeviceCtrlConstants.chgMeter:
            if(mMeter.getIfType() != null  && !CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.newIfType))) {
                BigDecimal chgIfType = new BigDecimal(nodeMap.get(DeviceCtrlConstants.newIfType));
                if(!mMeter.getIfType().equals(chgIfType)) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeter.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                    return true;
                }
            }

            if(!CheckUtility.isNullOrEmpty(mMeter.getMeterIdOld()) && !CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.oldMeterId))) {
                if(!mMeter.getMeterIdOld().equals(nodeMap.get(DeviceCtrlConstants.oldMeterId))) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeter.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                    return true;
                }
            }
            if(!mMeter.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)))) {

                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeter.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                return true;
            }
            break;
        case DeviceCtrlConstants.setBreaker:
            if(!mMeter.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId))) || !mMeter.getOpenMode().equals(nodeMap.get(DeviceCtrlConstants.mode))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeter.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                return true;
            }
            break;
        case DeviceCtrlConstants.setSpitconf:
            buildingNo = searchTenantId(mMeter);
            BigDecimal spitMulti = new BigDecimal(nodeMap.get(DeviceCtrlConstants.multi));


            if(!mMeter.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId))) || !mMeter.getMeterId().equals(nodeMap.get(DeviceCtrlConstants.meterId)) ||
                    !buildingNo.equals(nodeMap.get(DeviceCtrlConstants.tenantId)) || !mMeter.getMulti().equals(spitMulti)) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeter.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                return true;
            }

            if(mMeter.getPulseWeight() != null && mMeter.getPulseWeight().toString().length() != 0) {
                BigDecimal spitPulseWeight = new BigDecimal(nodeMap.get(DeviceCtrlConstants.pulseWeight).trim());
                if(!mMeter.getPulseWeight().equals(spitPulseWeight)) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeter.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                    return true;
                }
            }

            if(mMeter.getPulseType() != null && mMeter.getPulseType().toString().length() != 0) {
                if(!mMeter.getPulseType().equals(nodeMap.get(DeviceCtrlConstants.pulseType))) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeter.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                    return true;
                }
            }

            if(mMeter.getCurrentData() != null && mMeter.getCurrentData().toString().length() != 0) {

                BigDecimal spitCurrentData = new BigDecimal(nodeMap.get(DeviceCtrlConstants.currentData).trim());
                if(!mMeter.getCurrentData().equals(spitCurrentData)) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeter.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeter.getId().getMeterMngId());
                    return true;
                }
            }
            break;
        default:
            return false;
        }
        return false;
    }


    /**
     * 中継装置のバリデ
     * trueの場合はxmlとDBに差異がありエラーとする
     * @param codeName
     * @param mConcentrator
     * @return
     */
    public  boolean repeaterValChk(String codeName, MRepeater mRepeater, Map<String,String> nodeMap) {

        switch(codeName) {
        case DeviceCtrlConstants.addRepeater:
            if(!mRepeater.getId().getRepeaterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.repeaterMngId))) || !mRepeater.getRepeaterId().equals(nodeMap.get(DeviceCtrlConstants.repeaterId))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mRepeater.getId().getDevId());
                errorLogger.error("repeaterMngId : " + mRepeater.getId().getRepeaterMngId());
                return true;
            }
            break;
        case DeviceCtrlConstants.delRepeater:
            if(!mRepeater.getId().getRepeaterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.repeaterMngId)))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mRepeater.getId().getDevId());
                errorLogger.error("repeaterMngId : " + mRepeater.getId().getRepeaterMngId());
                return true;
            }
            break;

        default:
            return false;
        }
        return false;
    }

    /**
     * 負荷設定のバリデ
     * trueの場合はxmlとDBに差異がありエラーとする
     * @param codeName
     * @param mConcentrator
     * @return
     */
    public  boolean loadlimitValChk(String codeName, MMeterLoadlimit mMeterLoadlimit, Map<String,String> nodeMap) {

        switch(codeName) {
        case DeviceCtrlConstants.setLoadlimit:
            if(!mMeterLoadlimit.getId().getMeterMngId().equals(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId))) || !mMeterLoadlimit.getLoadlimitMode().equals(nodeMap.get(DeviceCtrlConstants.mode))) {
                errorLogger.error("バリデーションエラー : " + codeName);
                errorLogger.error("devId : " + mMeterLoadlimit.getId().getDevId());
                errorLogger.error("meterMngId : " + mMeterLoadlimit.getId().getMeterMngId());
                return true;
            }
            //
            if(mMeterLoadlimit.getLoadlimitMode().equals(DeviceCtrlConstants.A) || mMeterLoadlimit.getLoadlimitMode().equals(DeviceCtrlConstants.R)) {

                if(!nodeMap.get(DeviceCtrlConstants.loadCurrent).trim().equals(mMeterLoadlimit.getTempLoadCurrent()) || !nodeMap.get(DeviceCtrlConstants.autoInjection).trim().equals(mMeterLoadlimit.getTempAutoInjection()) ||
                        !nodeMap.get(DeviceCtrlConstants.breakerActCount).trim().equals(mMeterLoadlimit.getTempBreakerActCount()) || !nodeMap.get(DeviceCtrlConstants.countClear).equals(mMeterLoadlimit.getTempCountClear()) ) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeterLoadlimit.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeterLoadlimit.getId().getMeterMngId());
                    return true;
                }

            }else {
                if(!nodeMap.get(DeviceCtrlConstants.loadCurrent).trim().equals(mMeterLoadlimit.getLoadCurrent()) || !nodeMap.get(DeviceCtrlConstants.autoInjection).trim().equals(mMeterLoadlimit.getAutoInjection()) ||
                        !nodeMap.get(DeviceCtrlConstants.breakerActCount).trim().equals(mMeterLoadlimit.getBreakerActCount()) || !nodeMap.get(DeviceCtrlConstants.countClear).trim().equals(mMeterLoadlimit.getCountClear())) {
                    errorLogger.error("バリデーションエラー : " + codeName);
                    errorLogger.error("devId : " + mMeterLoadlimit.getId().getDevId());
                    errorLogger.error("meterMngId : " + mMeterLoadlimit.getId().getMeterMngId());
                    return true;
                }

            }
            break;

        default:
            return false;
        }
        return false;
    }




    /**
     * 秒まで変換
     * String → TimeStamp
     * @param str
     * @return
     * @throws ParseException
     */
    public Timestamp strToTimeStampHHMMSS(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        date = sdf.parse(str);
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }

    /**
     * 分までの変換
     * String → TimeStamp
     * @param str
     * @return
     * @throws ParseException
     */
    public Timestamp strToTimeStampHHMM(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date();
        date = sdf.parse(str);
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }

    /**
     * 秒まで変換
     * Timestamp → String
     * @param ts
     * @return
     */
    public String timestampToString(Timestamp ts) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTs = sdf.format(ts);
        return dateTs;
    }






    /**
     * Documentを文字列にして返す
     * @param responseDocument
     * @return
     */
    public String domToString(Document responseDocument) {
        // DOMオブジェクトを文字列として出力
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try(StringWriter stringWriter = new StringWriter()) {
            transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(responseDocument), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (TransformerException te) {
            errorLogger.error(BaseUtility.getStackTraceMessage(te));
            return "";
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
            return "";
        }

    }

    /**
     * mapに要素を入れていく
     * @param n
     * @param str
     */
    public  void allNodeSearchToMap(Node n, Map<String,String> nodeMap){
        // Node n の最初の子Nodeを取得 => ch
        Node ch = n.getFirstChild();

        // chがnull（子Nodeが取得できないか、NextSiblingがない）でない間ループ
        while(ch!=null){
            if(ch.getNodeName() != null || !ch.getNodeName().equals("#text")) {
                nodeMap.put(ch.getNodeName(), ch.getTextContent());
            }
            // 自メソッドを再帰的に呼び出す
            allNodeSearchToMap(ch, nodeMap);
            // Node ch の弟Nodeを取得 => ch
            ch = ch.getNextSibling();
        }
    }

    /**
     * 共通処理
     * 装置の認証
     * @param devId
     * @param devPw
     * @param errorFlg TODO
     * @return
     */
    public void devAuth(String devId,String devPw, boolean[] errorFlg) {

        String SmsLTETmpCorpId = smsConfigs.getConfig(SmsConstants.LTE_TMP_CORP_ID);	         //LTE端末仮登録建物		20250310追加

        MDevPrm mDevPrm = new MDevPrm ();
        mDevPrm.setDevId(devId);

        if(mDevPrmDao.isNull(mDevPrm)) {
            errorFlg[0] = true;
//            return ;
			if(!devId.startsWith(DEVICE_KIND.LTE.getVal())) {
				return ;	// 装置IDが未登録でLTE端末ではない。
			}else {
				Charset charset = StandardCharsets.UTF_8;
				byte[]encodeStr = Base64.getEncoder().encode(devId.getBytes(charset));
				String encodePw = new String(encodeStr, charset);

				if(!encodePw.equals(devPw)) {
					errorLogger.error("LTE端末のPWが不一致です\n xmlのPW : " +  devPw + "\n dbのPW(64変換後) : " + encodePw);
				}else {
		            errorLogger.error("未登録のLTE端末からの接続要求あり");

// 以下は「osol-api\src\main\java\jp\co\osaki\osol\api\dao\sms\server\sertting\collect\UpdateSmsCollectDeviceDao.java」を参照した。
					//リクエストパラメータセット
		            BigDecimal commInterval = new BigDecimal(SmsConstants.LTE_SET_INTERVAL);
					Date date = new Date();
					Timestamp serverDateTime = new Timestamp(date.getTime());
					MDevPrm mDevPrmEnt = new MDevPrm();
					mDevPrmEnt.setDevKind(SmsConstants.DEVICE_PRM_KIND.MUDM2.getVal());		// 装置タイプ：1（MU-DMx）
					mDevPrmEnt.setRecDate(serverDateTime);
					mDevPrmEnt.setRecMan(DeviceCtrlConstants.lteAutoRegist);	// 登録者（とりあえずLTE端末からの接続なので）
					mDevPrmEnt.setDevId(devId);
					mDevPrmEnt.setDevPw(devId);							// パスワードはdevIdと同じ
					mDevPrmEnt.setDevSta(SmsConstants.DEV_STA.NORMAL.getVal());	// 装置異常：0（正常復帰）
					mDevPrmEnt.setName(null);
					mDevPrmEnt.setMemo(null);
					mDevPrmEnt.setIpAddr(null);
					mDevPrmEnt.setHomeDirectory(null);
					mDevPrmEnt.setExamNoticeMonth(SmsConstants.LTE_SET_EXAM_NOTICE_MONTH);	// 検満通知月数：18
					mDevPrmEnt.setAlertDisableFlg("0");					// 警報メール対象：0（警報メール対象）
					mDevPrmEnt.setRevFlg("0");							// 逆調対応機能：0（未使用）
					mDevPrmEnt.setCommInterval(commInterval);			// 装置→サーバ　通信周期：30分
					mDevPrmEnt.setDelFlg(0);							// 削除フラグ：0（未削除）
					mDevPrmEnt.setVersion(0);							// バージョン：0
					mDevPrmEnt.setCreateUserId(DeviceCtrlConstants.create_userId);	// 作成ユーザー識別ID：0
					mDevPrmEnt.setCreateDate(serverDateTime);
					mDevPrmEnt.setUpdateUserId(DeviceCtrlConstants.update_userId);	// 更新ユーザー識別ID：0
					mDevPrmEnt.setUpdateDate(serverDateTime);
// 以下で装置追加ができるのだろうと考えたが…
//					persist(mDevPrmServiceDaoImpl, mDevPrmEnt);
					mDevPrmDao.persist(mDevPrmEnt); // FIXME ここ追加しました。

					// 2024-01-19
					MDevRelation mDevRelation = new MDevRelation();
					MDevRelationPK mDevRelationPK = new MDevRelationPK();
					mDevRelationPK.setCorpId(SmsLTETmpCorpId);
					mDevRelationPK.setBuildingId(SmsConstants.LTE_TMP_BUILDING_ID);
					mDevRelationPK.setDevId(devId);
					mDevRelation.setId(mDevRelationPK);
					mDevRelation.setVersion(0);
					mDevRelation.setCreateUserId(DeviceCtrlConstants.create_userId);
					mDevRelation.setCreateDate(serverDateTime);
					mDevRelation.setUpdateUserId(DeviceCtrlConstants.update_userId);
					mDevRelation.setUpdateDate(serverDateTime);
					mDevRelationDao.persist(mDevRelation);

					errorFlg[0] = false;

					addCommIntervalCommand(devId, commInterval.toString());	// 2023-12-22
					sendLteAutoRegistMail(devId);	// 2023-12-20
				}
				return ;
			}
        }
        mDevPrm = mDevPrmDao.find(mDevPrm);
        Charset charset = StandardCharsets.UTF_8;
        //DBのPWをbase64変換する
        byte[]encodeStr = Base64.getEncoder().encode((mDevPrm.getDevPw().getBytes(charset)));
        String encodePw = new String(encodeStr, charset);

        if(!encodePw.equals(devPw)) {
            errorFlg[0] = true;
            errorLogger.error("装置PWが不一致です\n xmlのPW : " +  devPw + "\n dbのPW(64変換後) : " + encodePw);
        }

        return ;
    }

    /**
     * LTE端末に対する通信周期送信コマンド登録
     * @param devId
     * @param commInterval
    **/
    public void addCommIntervalCommand(String devId, String commInterval) {
    	Date date = new Date();
    	Timestamp serverDateTime = new Timestamp(date.getTime());

    	TCommand tCommand = new TCommand();
    	TCommandPK tCommandPK = new TCommandPK();
    	tCommandPK.setDevId(devId);
    	tCommandPK.setCommand(SmsConstants.CMD_KIND.SET_INTERVAL.getVal());
    	tCommandPK.setRecDate(serverDateTime);
    	tCommand.setId(tCommandPK);
    	tCommand.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
    	tCommand.setTag(commInterval);
    	tCommand.setRecMan(DeviceCtrlConstants.lteAutoRegist);
    	tCommand.setVersion(0);
    	tCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
    	tCommand.setCreateDate(serverDateTime);
    	tCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
    	tCommand.setUpdateDate(serverDateTime);
    	tCommandDao.persist(tCommand);
    }

    /**
     * mail送信
     * 未登録のLTE端末から接続要求があったときのみ
     * @param devId
    **/
    public void sendLteAutoRegistMail(String devId) {
        List<String> toAddresses = new ArrayList<>();

        MAlertMailSetting entity = new MAlertMailSetting();
        entity.setAlertCd(lteRegistMail);
        MAlertMailSetting mailSendAddress = mAlertMailSettingDao.find(entity);

        toAddresses = addAddress(toAddresses, mailSendAddress.getMail1());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail2());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail3());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail4());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail5());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail6());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail7());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail8());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail9());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail10());

        String svDate = getSeverDate();
        String dayOfWeek = getDayOfWeek(svDate);
        SendLteAutoRegistMail mail = new SendLteAutoRegistMail();
        mail.lteAutoRegist(toAddresses, devId, svDate, dayOfWeek);
    }

    /**
     * mail送信
     * LTE端末からsend-addmeterコマンドがあり、新規メーターが追加されたとき
     * @param devId
     * @param meterMngId
     * @param meterId
    **/
    public void sendLteMeterAddMail(String devId, Long meterMngId, String meterId) {
        List<String> toAddresses = new ArrayList<>();

        MAlertMailSetting entity = new MAlertMailSetting();
        entity.setAlertCd(lteRegistMail);
        MAlertMailSetting mailSendAddress = mAlertMailSettingDao.find(entity);

        toAddresses = addAddress(toAddresses, mailSendAddress.getMail1());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail2());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail3());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail4());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail5());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail6());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail7());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail8());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail9());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail10());

        String svDate = getSeverDate();
        String dayOfWeek = getDayOfWeek(svDate);
        SendLteAutoRegistMail mail = new SendLteAutoRegistMail();
        mail.lteMeterAdd(toAddresses, devId, meterMngId.toString(), meterId, svDate, dayOfWeek);
    }

    /**
     * mail送信
     * LTE端末からsend-addmeterコマンドがあり、メーター交換されたとき
     * @param devId
     * @param meterMngId
     * @param newMeterId
     * @param oldMeterId
    **/
    public void sendLteMeterChangeMail(String devId, Long meterMngId, String newMeterId, String oldMeterId) {
        List<String> toAddresses = new ArrayList<>();

        MAlertMailSetting entity = new MAlertMailSetting();
        entity.setAlertCd(lteRegistMail);
        MAlertMailSetting mailSendAddress = mAlertMailSettingDao.find(entity);

        toAddresses = addAddress(toAddresses, mailSendAddress.getMail1());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail2());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail3());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail4());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail5());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail6());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail7());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail8());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail9());
        toAddresses = addAddress(toAddresses, mailSendAddress.getMail10());

        String svDate = getSeverDate();
        String dayOfWeek = getDayOfWeek(svDate);
        SendLteAutoRegistMail mail = new SendLteAutoRegistMail();
        mail.lteMeterChange(toAddresses, devId, meterMngId.toString(), newMeterId, oldMeterId, svDate, dayOfWeek);
    }

    private String getSeverDate() {
    	String svDate = "";

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	svDate = sdf.format(meterDataFilterDao.getSvDate());
    	return svDate;
    }

    /**
     * null or 空文字をチェックしてリストにadd
     *
     * @param List<String> addressList
     * @param String address
     * @return アドレスリスト
    **/
    private List<String> addAddress(List<String> addressList, String address) {
    	if (!CheckUtility.isNullOrEmpty(address)) {
    		addressList.add(address);
    	}
    	return addressList;
    }

    /**
     * 曜日取得
     * @param svDate
     * @return　svDateの曜日
    **/
    private String getDayOfWeek(String svDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = dateFormat.parse(svDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:     // Calendar.SUNDAY:1
                //日曜日
                return "（日）";
            case Calendar.MONDAY:     // Calendar.MONDAY:2
                //月曜日
                return "（月）";
            case Calendar.TUESDAY:    // Calendar.TUESDAY:3
                //火曜日
                return "（火）";
            case Calendar.WEDNESDAY:  // Calendar.WEDNESDAY:4
                //水曜日
                return "（水）";
            case Calendar.THURSDAY:   // Calendar.THURSDAY:5
                //木曜日
                return "（木）";
            case Calendar.FRIDAY:     // Calendar.FRIDAY:6
                //金曜日
                return "（金）";
            case Calendar.SATURDAY:   // Calendar.SATURDAY:7
                //土曜日
                return "（土）";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 返却用xmlの共通部分作成
     * @param contents
     * @param responseDocument
     * @return
     */
    public Element responseXmlCreate(Element contents, Document responseDocument, Map<String,String> nodeMap) {
        contents = responseDocument.createElement(DeviceCtrlConstants.contents);
        responseDocument.appendChild(contents);

        //返信用のauthの子要素, テキストを追加
        Element auth = responseDocument.createElement(DeviceCtrlConstants.auth);
        contents.appendChild(auth);
        Text authText = responseDocument.createTextNode(nodeMap.get(DeviceCtrlConstants.auth));
        auth.appendChild(authText);

        //返信用のdateの子要素, テキストを追加
        Element date = responseDocument.createElement(DeviceCtrlConstants.date);
        contents.appendChild(date);
        Date responseTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Text dateText = responseDocument.createTextNode(format.format(responseTime));
        date.appendChild(dateText);

        return contents;
    }

    /**
     * 処理予約テーブルの個数
     * @param devId
     * @return
     */
    public int tWrkHstSize(String devId) {
        TWorkHst entity = new TWorkHst();
        TWorkHstPK id = new TWorkHstPK();
        id.setDevId(devId);
        entity.setId(id);

        List<TWorkHst> list = new ArrayList<>();
        list.addAll(tWorkHstDao.getTWorkHstList(entity));

        return list.size();
    }

    /**
     * 処理予約テーブルのレコード削除
     * @param devId
     * @param codeName
     */
    public void tWrkHstDelete(String devId, String codeName) {
        TWorkHst entity = new TWorkHst();
        TWorkHstPK id = new TWorkHstPK();
        id.setDevId(devId);
        id.setCommand(codeName);
        entity.setId(id);
        entity.setSrvEnt(DeviceCtrlConstants.eight.toString());
        entity = tWorkHstDao.find(entity);
        tWorkHstDao.remove(entity);
        return;
    }

    /**
     * 処理予約テーブルのレコード削除
     * @param entity
     */
    public void tWrkHstDelete(TWorkHst entity) {
        tWorkHstDao.remove(entity);
        return;
    }



    /**
     * what返信
     * @param command
     * @param responseDocument
     * @return
     */
    public Element whatXml(Element command, Document responseDocument) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.what);
        code.appendChild(codeText);

        return command;
    }

    /**
     * end返信
     * @param command
     * @param responseDocument
     * @return
     */
    public Element endXml(Element command, Document responseDocument) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.end);
        code.appendChild(codeText);

        return command;
    }


    /**
     * 処理待ちフラグ
     * 1 → 2にする。
     * @param errorFlg
     * @param entity
     */
    public TWorkHst tWrkHstOneToTwo(String devId, String codeName, boolean[] errorFlg) {
        TWorkHst tWorkHst = new TWorkHst();
        TWorkHstPK tWorkHstPK = new TWorkHstPK();

        tWorkHstPK.setDevId(devId);
        tWorkHstPK.setCommand(codeName);
        tWorkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
        tWorkHst.setId(tWorkHstPK);
        if(!tWorkHstDao.isNull(tWorkHst)) {
            //先頭取得
            tWorkHst = tWorkHstDao.getTWorkHstList(tWorkHst).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }

        tWorkHst.setSrvEnt(DeviceCtrlConstants.two.toString());
        tWorkHstDao.merge(tWorkHst);
        return tWorkHst;
    }

    /**
     * 処理待ちフラグ
     * 1 → 2にする。
     * @param entity
     */
    public void tWrkHstOneToTwo(TWorkHst entity) {
        entity.setSrvEnt(DeviceCtrlConstants.two.toString());
        entity.setUpdateDate(tWorkHstDao.getSvDate());
        tWorkHstDao.merge(entity);
    }

    /**
     * 処理待ちフラグ
     * 9にする。
     * @param entity
     */
    public void tWrkHstToNine(TWorkHst entity) {
        entity.setSrvEnt(DeviceCtrlConstants.nine.toString());
        entity.setUpdateDate(tWorkHstDao.getSvDate());
        tWorkHstDao.merge(entity);
    }

    /**
     * 処理待ちフラグ
     * srv_entをnull
     * 装置から送られてきたときにエラーだった場合に使用
     * @param entity
     */
    public void tWrkHstToNull(TWorkHst entity) {
        entity.setSrvEnt(null);
        entity.setUpdateDate(tWorkHstDao.getSvDate());
        entity.setWriteDate(tWorkHstDao.getSvDate());
        tWorkHstDao.merge(entity);
    }

    /**
     *  処理待ちフラグ
     *  2 → 8にする
     * @param errorFlg TODO
     * @param entity
     */
    public TWorkHst tWrkHstTwoToEight(String devId, String codeName, boolean[] errorFlg) {
        TWorkHst tWorkHst = new TWorkHst();
        TWorkHstPK tWorkHstPK = new TWorkHstPK();

        tWorkHstPK.setDevId(devId);
        tWorkHstPK.setCommand(codeName);
        tWorkHst.setSrvEnt(DeviceCtrlConstants.two.toString());
        tWorkHst.setId(tWorkHstPK);
        if(!tWorkHstDao.isNull(tWorkHst)) {
            //先頭取得
            tWorkHst = tWorkHstDao.getTWorkHstList(tWorkHst).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tWorkHst.setWriteDate(tWorkHstDao.getSvDate());
        tWorkHst.setSrvEnt(DeviceCtrlConstants.eight.toString());
        return tWorkHstDao.merge(tWorkHst);

    }

    /**
     *  処理待ちフラグ
     *  先頭を取得する
     * @param entity
     */
    public TWorkHst getTWrkHst(String devId, String codeName, boolean[] errorFlg) {
        TWorkHst tWorkHst = new TWorkHst();
        TWorkHstPK tWorkHstPK = new TWorkHstPK();

        tWorkHstPK.setDevId(devId);
        tWorkHstPK.setCommand(codeName);
        tWorkHst.setId(tWorkHstPK);
        if(!tWorkHstDao.isNull(tWorkHst)) {
            //先頭取得
            tWorkHst = tWorkHstDao.getTWorkHstList(tWorkHst).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        return tWorkHst;

    }

    /**
     *  処理待ちフラグ
     *  先頭を取得する(srv_ent=2)
     * @param entity
     */
    public TWorkHst getTWrkHstTwo(String devId, String codeName, boolean[] errorFlg) {
        TWorkHst tWorkHst = new TWorkHst();
        TWorkHstPK tWorkHstPK = new TWorkHstPK();

        tWorkHstPK.setDevId(devId);
        tWorkHstPK.setCommand(codeName);
        tWorkHst.setId(tWorkHstPK);
        tWorkHst.setSrvEnt(DeviceCtrlConstants.two.toString());

        if(!tWorkHstDao.isNull(tWorkHst)) {
            //先頭取得
            tWorkHst = tWorkHstDao.getTWorkHstList(tWorkHst).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        return tWorkHst;

    }

    /**
     *  処理待ちフラグ
     *  2 → 9にする
     * @param entity
     */
    public TWorkHst tWrkHstTwoToNine(String devId, String codeName, boolean[] errorFlg) {
        TWorkHst tWorkHst = new TWorkHst();
        TWorkHstPK tWorkHstPK = new TWorkHstPK();

        tWorkHstPK.setDevId(devId);
        tWorkHstPK.setCommand(codeName);
        tWorkHst.setSrvEnt(DeviceCtrlConstants.two.toString());
        tWorkHst.setId(tWorkHstPK);
        if(!tWorkHstDao.isNull(tWorkHst)) {
            //先頭取得
            tWorkHst = tWorkHstDao.getTWorkHstList(tWorkHst).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tWorkHst.setUpdateDate(tWorkHstDao.getSvDate());
        tWorkHst.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tWorkHstDao.merge(tWorkHst);
        return tWorkHst;
    }

    /**
     * DBから処理内容を消去
     * @param entity
     */
    public void delTWrkHst(TWorkHst entity) {
        tWorkHstDao.remove(entity);
    }

    /**
     * 処理待ちフラグ
     * 2にする。
     * @param entity
     */
    public void tCommandToTwo(TCommand entity) {
        entity.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandDao.merge(entity);
    }

    public TCommand tCommandOneToTwo(String devId, String codeName, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandDao.merge(tCommand);
        return tCommand;
    }

    /**
     * 処理待ちフラグ
     * 9にする。
     * @param entity
     */
    public void tCommandToNine(TCommand entity) {
        entity.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tCommandDao.merge(entity);
    }

    /**
     * 処理待ちフラグ
     * srv_entをnullにする
     * @param entity
     */
    public void tCommandToNull(TCommand entity) {
        entity.setSrvEnt(null);
        entity.setUpdateDate(tCommandDao.getSvDate());
        tCommandDao.merge(entity);
    }


    /**
     * 一括処理の親をエラーにする
     * @param devId
     * @param codeName
     * @param link
     */
    public void tCommandBulkToNine(String devId, String codeName, String link, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setLinking(link);
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return ;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tCommandDao.merge(tCommand);
    }

    /**
     * 一括処理の親をnullにする
     * @param devId
     * @param codeName
     * @param link
     */
    public void tCommandBulkToNull(String devId, String codeName, String link) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setLinking(link);
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
            tCommand.setSrvEnt(null);
            tCommandDao.merge(tCommand);
        }

    }

    /**
     * 一括処理の残件
     */
    public int tCommandBulkSize(String devId, String codeName, String link) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        tCommand.setLinking(link);
        tCommand.setId(tCommandPK);

        return tCommandDao.getTCommandList(tCommand).size();
    }

    /**
     * 処理待ちフラグ
     * 2 → 8にする。
     * @param entity
     */
    public TCommand tCommandTwoToEight(String devId, String codeName, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.eight.toString());
        tCommandDao.merge(tCommand);
        return tCommand;
    }

    /**
     * 一括設定の親を取得
     * @param devId
     * @param codeName
     * @return
     */
    public TCommand getTCommandTwo(String devId, String codeName, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        return tCommandDao.getTCommandList(tCommand).get(0);
    }

    /**
     * t_command
     * 処理待ちフラグ
     * 1 → 8にする。
     * @param entity
     */
    public TCommand tCommandOnetoEight(String devId, String codeName, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.eight.toString());
        tCommandDao.merge(tCommand);
        return tCommand;
    }


    /**
     * 処理待ちフラグ
     * 2 → 9にする。
     * @param entity
     */
    public TCommand tCommandTwoToNine(String devId, String codeName, boolean[] errorFlg) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandDao.isNull(tCommand)) {
            tCommand = tCommandDao.getTCommandList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tCommandDao.merge(tCommand);
        return tCommand;
    }

    /**
     * DBから処理内容を消去
     * @param entity
     */
    public void delTCommand(TCommand entity) {
        tCommandDao.remove(entity);
    }

    /**
     * 処理待ちフラグ
     * 2 → 8にする。
     * @param entity
     */
    public TCommandLoadSurveyMeter tCommandMeterTwoToEight(String devId, String codeName, boolean[] errorFlg) {
        TCommandLoadSurveyMeter tCommand = new TCommandLoadSurveyMeter();
        TCommandLoadSurveyMeterPK tCommandPK = new TCommandLoadSurveyMeterPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandLoadSurveyMeterDao.isNull(tCommand)) {
            tCommand = tCommandLoadSurveyMeterDao.getResultList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.eight.toString());
        tCommandLoadSurveyMeterDao.merge(tCommand);
        return tCommand;
    }

    /**
     * 1 → 2にする。
     * @param devId
     * @param codeName
     * @return
     */
    public TCommandLoadSurveyMeter tCommandMeterOneToTwo(String devId, String codeName, boolean[] errorFlg) {
        TCommandLoadSurveyMeter tCommand = new TCommandLoadSurveyMeter();
        TCommandLoadSurveyMeterPK tCommandPK = new TCommandLoadSurveyMeterPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandLoadSurveyMeterDao.isNull(tCommand)) {
            tCommand = tCommandLoadSurveyMeterDao.getResultList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandLoadSurveyMeterDao.merge(tCommand);
        return tCommand;
    }

    /**
     * t_command_load_survey_meterの
     * srv_entを2に変更
     * @param entity
     */
    public void tCommandMeterToTwo(TCommandLoadSurveyMeter entity) {
        entity.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandLoadSurveyMeterDao.merge(entity);
    }
    /**
     * 処理待ちフラグ
     * 9にする。
     * @param entity
     */
    public void tCommandMeterToNine(TCommandLoadSurveyMeter entity) {
        entity.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tCommandLoadSurveyMeterDao.merge(entity);
    }

    /**
     * 処理待ちフラグ
     * srv_entをnullにする
     * @param entity
     */
    public void tCommandMeterToNull(TCommandLoadSurveyMeter entity) {
        entity.setSrvEnt(null);
        tCommandLoadSurveyMeterDao.merge(entity);
    }


    /**
     * DBから処理内容を消去
     * @param entity
     */
    public void delTCommandMeter(TCommandLoadSurveyMeter entity) {
        tCommandLoadSurveyMeterDao.remove(entity);
    }


    /**
     * 処理待ちフラグ
     * 2 → 8にする。
     * @param entity
     */
    public TCommandLoadSurveyTime tCommandTimeTwoToEight(String devId, String codeName, boolean[] errorFlg) {
        TCommandLoadSurveyTime tCommand = new TCommandLoadSurveyTime();
        TCommandLoadSurveyTimePK tCommandPK = new TCommandLoadSurveyTimePK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandLoadSurveyTimeDao.isNull(tCommand)) {
            tCommand = tCommandLoadSurveyTimeDao.getResultList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.eight.toString());
        tCommandLoadSurveyTimeDao.merge(tCommand);
        return tCommand;
    }

    /**
     * 1 → 2にする。
     * @param devId
     * @param codeName
     * @return
     */
    public TCommandLoadSurveyTime tCommandTimeOneToTwo(String devId, String codeName, boolean[] errorFlg) {
        TCommandLoadSurveyTime tCommand = new TCommandLoadSurveyTime();
        TCommandLoadSurveyTimePK tCommandPK = new TCommandLoadSurveyTimePK();

        tCommandPK.setDevId(devId);
        tCommandPK.setCommand(codeName);
        tCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        tCommand.setId(tCommandPK);
        if(!tCommandLoadSurveyTimeDao.isNull(tCommand)) {
            tCommand = tCommandLoadSurveyTimeDao.getResultList(tCommand).get(0);
        }else {
            errorFlg[0] = true;
            return null;
        }
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandLoadSurveyTimeDao.merge(tCommand);
        return tCommand;
    }

    /**
     * t_command_load_survey_timeの
     * srv_entを2に変更する
     * @param entity
     */
    public void tCommandTimeToTwo(TCommandLoadSurveyTime entity) {
        entity.setSrvEnt(DeviceCtrlConstants.two.toString());
        tCommandLoadSurveyTimeDao.merge(entity);
    }


    /**
     * 処理待ちフラグ
     * 9にする。
     * @param entity
     */
    public void tCommandTimeToNine(TCommandLoadSurveyTime entity) {
        entity.setSrvEnt(DeviceCtrlConstants.nine.toString());
        tCommandLoadSurveyTimeDao.merge(entity);
    }

    /**
     * 処理待ちフラグ
     * srv_entをnullにする
     * @param entity
     */
    public void tCommandTimeToNull(TCommandLoadSurveyTime entity) {
        entity.setSrvEnt(null);
        tCommandLoadSurveyTimeDao.merge(entity);
    }


    /**
     * DBから処理内容を消去
     * @param entity
     */
    public void delTCommandTime(TCommandLoadSurveyTime entity) {
        tCommandLoadSurveyTimeDao.remove(entity);
    }


    /**
     * xmlをListにしないといけないとき
     * @param str
     * @return
     */
    public NodeList getXmlList(String str, String bodyString) {
        try(StringReader reader = new StringReader(bodyString)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(reader));
            Element elementList = doc.getDocumentElement();
            return elementList.getElementsByTagName(str);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日付のチェック
     * 20100401000000以前の場合はエラーとする
     * @param dateDev
     */
    public void dateAuth(String dateDev, boolean[] errorFlg) {
        String dateMin = "20100401000000";
        if(dateDev.compareTo(dateMin) < 0) {
            errorFlg[0] = true;
            errorLogger.error("日付がエラーになっています\n xmlの日付 : " + dateDev );
            return ;
        }
        return ;
    }

    /**
     * エラー時のその他対応(error20_setting.phpに相当)
     * @param tWrkHst
     * @param nodeMap TODO
     */
    public void error20(TWorkHst tWrkHst, Map<String, String> nodeMap) {

        if(tWrkHst.getFilePath() != null) {

            //srv_ent 9に変更
            tWrkHstToNine(tWorkHstDao.find(tWrkHst));
            File file = new File(tWrkHst.getFilePath());

            //csvファイルの削除
            //削除以降のcsv返送は坂内さん預かりとのこと(error20_setting.phpに相当)
            if(file.exists()) {
                if(!file.delete()) {
                    errorLogger.error("ファイル削除に失敗しました");
                }
            }else {
                errorLogger.error("ファイルが見つかりません");
            }
        }

        if(tWrkHst.getTableName() != null) {
            String command = tWrkHst.getId().getCommand();
            String tableName = tWrkHst.getTableName();
            String devId = tWrkHst.getId().getDevId();

            MConcentrator mConcentrator = new MConcentrator();
            MConcentratorPK mConcentratorPK = new MConcentratorPK();
            List<MConcentrator> mConcentratorList = new ArrayList<>();

            MRepeater mRepeater = new MRepeater();
            MRepeaterPK mRepeaterPK = new MRepeaterPK();
            List<MRepeater> mRepeaterList = new ArrayList<>();

            MMeter mMeter = new MMeter();
            MMeterPK mMeterPK = new MMeterPK();
            List<MMeter> mMeterList = new ArrayList<>();

            MMeterLoadlimit mMeterLoadlimit = new MMeterLoadlimit();
            MMeterLoadlimitPK mMeterLoadlimitPK = new MMeterLoadlimitPK();
            List<MMeterLoadlimit> mMeterLoadlimitList = new ArrayList<>();

            switch(tableName) {
            case DeviceCtrlConstants.m_concentrator:
                mConcentratorPK.setDevId(devId);
                mConcentrator.setId(mConcentratorPK);
                mConcentrator.setSrvEnt(DeviceCtrlConstants.one.toString());

                if(!mConcentratorDao.isNull(mConcentrator)) {
                    mConcentratorList.addAll(mConcentratorDao.getMConcentrotorList(mConcentrator));
                    MConcentrator retMConcentrator = new MConcentrator();
                    retMConcentrator = mConcentratorList.get(0);

                    retMConcentrator.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mConcentratorDao.merge(retMConcentrator);

                    if(Arrays.asList(cmdConcentrator).contains(command)) {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    }

                }
                break;

            case DeviceCtrlConstants.m_repeater:
                mRepeaterPK.setDevId(devId);
                mRepeater.setId(mRepeaterPK);
                mRepeater.setSrvEnt(DeviceCtrlConstants.one.toString());
                if(!mRepeaterDao.isNull(mRepeater)) {
                    mRepeaterList.addAll(mRepeaterDao.getMRepeaterList(mRepeater));
                    MRepeater retMRepeater = new MRepeater();
                    retMRepeater = mRepeaterList.get(0);
                    retMRepeater.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mRepeaterDao.merge(retMRepeater);

                    if(Arrays.asList(cmdRepeater).contains(command)) {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    }
                }
                break;

            case DeviceCtrlConstants.m_meter:
                mMeterPK.setDevId(devId);
                mMeter.setId(mMeterPK);
                mMeter.setSrvEnt(DeviceCtrlConstants.one.toString());
                if(!mMeterDao.isNull(mMeter)) {
                    mMeterList.addAll(mMeterDao.getMMeterList(mMeter));
                    MMeter retMeter = new MMeter();
                    retMeter = mMeterList.get(0);

                    retMeter.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterDao.merge(retMeter);

                    if(Arrays.asList(cmdMeter).contains(command)) {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    }
                }
                break;

            case DeviceCtrlConstants.m_meter_loadlimit:
                mMeterLoadlimitPK.setDevId(devId);
                mMeterLoadlimit.setId(mMeterLoadlimitPK);
                mMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.one.toString());
                if(!mMeterLoadlimitDao.isNull(mMeterLoadlimit)) {
                    mMeterLoadlimitList.addAll(mMeterLoadlimitDao.getMMeterLoadlimitList(mMeterLoadlimit));
                    MMeterLoadlimit retMeterLoadlimit = new MMeterLoadlimit();
                    retMeterLoadlimit = mMeterLoadlimitList.get(0);

                    retMeterLoadlimit.setSrvEnt(DeviceCtrlConstants.nine.toString());
                    mMeterLoadlimitDao.merge(retMeterLoadlimit);

                    if(Arrays.asList(cmdLoadlimit).contains(command)) {
                        tWrkHstToNull(tWorkHstDao.find(tWrkHst));
                    }
                }
                break;


        // 2023-10-25 追加
            case DeviceCtrlConstants.t_command:
            	if (command.equals(DeviceCtrlConstants.getMeterinfo)) {
            		Date date = new Date();
            		Timestamp serverDateTime = new Timestamp(date.getTime());

            		TCommand tCommand = new TCommand();
            		TCommandPK tCommandPK = new TCommandPK();
            		tCommandPK.setDevId(devId);
            		tCommandPK.setCommand(DeviceCtrlConstants.getMeterinfo);
            		tCommand.setId(tCommandPK);
            		tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());

            		// 展開 子ノード
            		TCommand tCommandBulk = new TCommand();
            		TCommandPK tCommandBulkPK = new TCommandPK();
            		tCommandBulkPK.setDevId(devId);
            		tCommandBulk.setId(tCommandBulkPK);
            		tCommandBulk.setSrvEnt(DeviceCtrlConstants.one.toString());

            		// t_commandに存在するかチェック
            		if (!tCommandDao.isNull(tCommand)) {
            			List<TCommand> retCmdList = new ArrayList<>();
            			retCmdList.addAll(tCommandDao.getTCommandList(tCommand));

            			// srv_ent = 2のものが2つ存在した場合はtComd[]出ない方を処理していく
            			if (retCmdList.size() > 1) {
            				for (TCommand ret : retCmdList) {
            					if (!Arrays.asList(tCmdBulk).contains(ret.getId().getCommand())) {
            						tCommand = ret;
            					}
            				}
            				if (tCommand.getId().getCommand() == null) {
            					tCommand = retCmdList.get(0);
            				}
            			}
            			else {
            				tCommand = tCommandDao.getTCommandList(tCommand).get(0);
            			}
            			// 以下コマンドはsrv_ent = 2で送らないのでチェック
            			// リストの先頭は必ず一番古いものなのでそれを取得
            			if (Arrays.asList(tCmdBulk).contains(tCommand.getId().getCommand())) {
            				// 2番目以降を再登録（先頭は送信したコマンドのため飛ばす。）
            				// countUpはしない
            				tCommand = tCommandDao.getTCommandList(tCommandBulk).get(0);
            			}

            			TCommand retCommand = new TCommand();
            			TCommandPK retCommandPK = new TCommandPK();

            			retCommandPK.setDevId(devId);
            			retCommandPK.setCommand(DeviceCtrlConstants.getMeterinfo);
            			retCommandPK.setRecDate(serverDateTime);	// rec_dateを更新する
            			retCommand.setId(retCommandPK);
            			retCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
            			retCommand.setRecMan(tCommand.getRecMan());
            			retCommand.setTag(tCommand.getTag());
            			retCommand.setLinking(tCommand.getLinking());
            			retCommand.setVersion(0);
            			retCommand.setCreateUserId(tCommand.getCreateUserId());
            			retCommand.setCreateDate(serverDateTime);	// create_dateも更新する
            			retCommand.setUpdateUserId(tCommand.getUpdateUserId());
            			retCommand.setUpdateDate(serverDateTime);	// uodate_dateも更新する

            			tCommandDao.persist(retCommand);	// rec_dateを更新したレコードを保存

            			tCommandDao.remove(tCommand);		// error20が返されたレコードを削除
            		}
            		// srv_ent nullに変更
            		tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            	}
            	break;
    	// ここまで


            default :
                break;
            }

        }

    }

    /**
     * コマンドチェック時nullチェックにかかったもの
     * @param devId
     * @param codeName
     */
    public void errorLogNull(String devId, String codeName) {
        errorLogger.error("xmlが不正です(null値が存在します。)");
        errorLogger.error("devId : " + devId);
        errorLogger.error("コマンド名 : " + codeName);
    }

    /**
     * busyが送られてきたときの対応
     * recdateを更新して、srv_ent = 1に変更する
     * t_commandのみを更新する
     * mudm2_flag_retry.php 参考
     * @param devId
     */
    public void busyUpdate(String devId) {

        //5回のリトライでtrueにする
        boolean retryFlg = false;

        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();
        tCommandPK.setDevId(devId);
        tCommand.setId(tCommandPK);
        tCommand.setSrvEnt(DeviceCtrlConstants.two.toString());

        //展開 子ノード
        TCommand tCommandBulk = new TCommand();
        TCommandPK tCommandBulkPK = new TCommandPK();
        tCommandBulkPK.setDevId(devId);
        tCommandBulk.setId(tCommandBulkPK);
        tCommandBulk.setSrvEnt(DeviceCtrlConstants.one.toString());

        if(!tCommandDao.isNull(tCommand)) {
            List<TCommand> retCmdList = new ArrayList<>();
            retCmdList.addAll(tCommandDao.getTCommandList(tCommand));

            //srv_ent = 2のものが2つ存在した場合はtCmd[]出ない方を処理していく
            if(retCmdList.size() > 1) {
                for(TCommand ret : retCmdList) {
                    if(!Arrays.asList(tCmdBulk).contains(ret.getId().getCommand())) {
                        tCommand = ret;
                    }
                }
                if(tCommand.getId().getCommand() == null) {
                    tCommand = retCmdList.get(0);
                }

            }else {
                tCommand = tCommandDao.getTCommandList(tCommand).get(0);
            }

            //以下コマンドはsrv_ent = 2で送らないのでチェック
            //リストの先頭は必ず一番古いものなのでそれを取得
            if(Arrays.asList(tCmdBulk).contains(tCommand.getId().getCommand())) {

                //2番目以降を再登録(先頭は送信したコマンドのため飛ばす。)
                //countUpはしない
                tCommand = tCommandDao.getTCommandList(tCommandBulk).get(0);
            }
            retryFlg = retryCount(tCommand);
        }


        TWorkHst tWrkHst = new TWorkHst();
        TWorkHstPK tWrkHstPK = new TWorkHstPK();
        tWrkHstPK.setDevId(devId);
        tWrkHst.setId(tWrkHstPK);
        tWrkHst.setSrvEnt(DeviceCtrlConstants.two.toString());

        if(!tWorkHstDao.isNull(tWrkHst)) {
            tWrkHst = tWorkHstDao.find(tWrkHst);

            if(retryFlg) {
                tWrkHstToNull(tWorkHstDao.find(tWrkHst));
            }else {
                //再登録
                //recdateがユニークなので再登録するしかない
                TWorkHst retryWrkHst = new TWorkHst();
                TWorkHstPK retryWrkHstPK = new TWorkHstPK();
                retryWrkHstPK.setDevId(devId);
                retryWrkHstPK.setCommand(tWrkHst.getId().getCommand());
                retryWrkHstPK.setRecDate(tWorkHstDao.getSvDate());
                retryWrkHst.setId(retryWrkHstPK);
                retryWrkHst.setUpdateDate(tWorkHstDao.getSvDate());
                retryWrkHst.setRecMan(tWrkHst.getRecMan());
                retryWrkHst.setTableName(tWrkHst.getTableName());
                retryWrkHst.setFilePath(tWrkHst.getFilePath());
                retryWrkHst.setSrvEnt(DeviceCtrlConstants.one.toString());
                retryWrkHst.setCreateUserId(DeviceCtrlConstants.create_userId);
                retryWrkHst.setUpdateUserId(DeviceCtrlConstants.update_userId);
                retryWrkHst.setCreateDate(tWorkHstDao.getSvDate());
                tWorkHstDao.persist(retryWrkHst);

                //登録終わったから削除
                tWorkHstDao.remove(tWrkHst);

            }

        }

    }

    public boolean retryCount(TCommand tCommand) {

        TCommand retCommand = new TCommand();
        TCommandPK retCommandPK = new TCommandPK();

        retCommandPK.setDevId(tCommand.getId().getDevId());
        retCommandPK.setCommand(tCommand.getId().getCommand());
        retCommandPK.setRecDate(tCommandDao.getSvDate());
        retCommand.setId(retCommandPK);
        retCommand.setTag(tCommand.getTag());
        retCommand.setCreateUserId(DeviceCtrlConstants.create_userId);
        retCommand.setUpdateUserId(DeviceCtrlConstants.update_userId);
        retCommand.setRecMan(tCommand.getRecMan());
        retCommand.setSrvEnt(DeviceCtrlConstants.one.toString());
        retCommand.setCreateDate(tCommandDao.getSvDate());
        retCommand.setUpdateDate(tCommandDao.getSvDate());


        BigDecimal one = BigDecimal.valueOf(1);
        BigDecimal five = BigDecimal.valueOf(5);

        if(tCommand.getRetryCount() != null) {
            //リトライ許容は5回まで
            if(tCommand.getRetryCount().compareTo(five) == DeviceCtrlConstants.zero) {
                tCommandToNine(tCommand);
                return true;
            }else {
                //リトライカウント +1
                retCommand.setRetryCount(tCommand.getRetryCount().add(one));;
                tCommandDao.persist(retCommand);
                tCommandDao.remove(tCommand);
            }
        }else {
            retCommand.setRetryCount(one);
            tCommandDao.persist(retCommand);
            tCommandDao.remove(tCommand);
        }
        return false;
    }


    /**
     * tenantIdの検索
     * @param mMeter
     * @return
     */
    public String searchTenantId(MMeter mMeter) {
        List<TBuildDevMeterRelation> list = new ArrayList<>();
        TBuildDevMeterRelation tBuildDevMeterRelation = new TBuildDevMeterRelation();
        TBuildDevMeterRelationPK tBuildDevMeterRelationPK = new TBuildDevMeterRelationPK();

        tBuildDevMeterRelationPK.setDevId(mMeter.getId().getDevId());
        tBuildDevMeterRelationPK.setMeterMngId(mMeter.getId().getMeterMngId());
        tBuildDevMeterRelation.setId(tBuildDevMeterRelationPK);

        if(!tBuildDevMeterRelationDao.isNull(tBuildDevMeterRelation)) {
            list.addAll(tBuildDevMeterRelationDao.getResultList(tBuildDevMeterRelation));
            for(TBuildDevMeterRelation retTBuildDevMeterRelation : list) {
                TBuilding tBuilding = new TBuilding();
                TBuildingPK tBuildingPK = new TBuildingPK();
                tBuildingPK.setCorpId(retTBuildDevMeterRelation.getId().getCorpId());
                tBuildingPK.setBuildingId(retTBuildDevMeterRelation.getId().getBuildingId());
                tBuilding.setId(tBuildingPK);
                tBuilding = tBuildingDao.find(tBuilding);

                //テナントで登録されている者
                if(tBuilding.getBuildingType().equals(DeviceCtrlConstants.one.toString())) {
                    MTenantSm mTenantSm = new MTenantSm();
                    MTenantSmPK mTenantSmPK = new MTenantSmPK();

                    mTenantSmPK.setBuildingId(tBuilding.getId().getBuildingId());
                    mTenantSmPK.setCorpId(tBuilding.getId().getCorpId());
                    mTenantSm.setId(mTenantSmPK);

                    mTenantSm = mTenantSmsDao.find(mTenantSm);


                    return mTenantSm.getTenantId().toString();
                }

            }
        }

        //テナントがなければ"0"で返却
        return "0";
    }


    /**
     * tenantNameの検索(メール送信)
     * @param mMeter
     * @return
     */
    public String searchTenantName(MMeter mMeter) {
        List<TBuildDevMeterRelation> list = new ArrayList<>();
        TBuildDevMeterRelation tBuildDevMeterRelation = new TBuildDevMeterRelation();
        TBuildDevMeterRelationPK tBuildDevMeterRelationPK = new TBuildDevMeterRelationPK();

        tBuildDevMeterRelationPK.setDevId(mMeter.getId().getDevId());
        tBuildDevMeterRelationPK.setMeterMngId(mMeter.getId().getMeterMngId());
        tBuildDevMeterRelation.setId(tBuildDevMeterRelationPK);

        if(!tBuildDevMeterRelationDao.isNull(tBuildDevMeterRelation)) {
            list.addAll(tBuildDevMeterRelationDao.getResultList(tBuildDevMeterRelation));
            for(TBuildDevMeterRelation retTBuildDevMeterRelation : list) {
                TBuilding tBuilding = new TBuilding();
                TBuildingPK tBuildingPK = new TBuildingPK();
                tBuildingPK.setCorpId(retTBuildDevMeterRelation.getId().getCorpId());
                tBuildingPK.setBuildingId(retTBuildDevMeterRelation.getId().getBuildingId());
                tBuilding.setId(tBuildingPK);
                tBuilding = tBuildingDao.find(tBuilding);

                //テナントで登録されている者
                if(tBuilding.getBuildingType().equals(DeviceCtrlConstants.one.toString())) {
                    return tBuilding.getBuildingName();
                }

            }
        }

        //テナントがなければ空文字で返却
        return "-";
    }

    /**
     * ヘッダー情報を出力
     * @param request
     */
    public void headerOut(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        // 全リクエストヘッダ名を取得
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {

            // ヘッダ名と値を取得
            String headerName = (String)headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            sb.append(headerName);
            sb.append("=");
            sb.append(headerValue);
            sb.append("\n");
        }

        smCtrlLogger.info(sb);
    }



    @Override
    protected BaseApiResponse createErrorMessageResponse(String resultCode, String errorMessage) {
        // TODO 自動生成されたメソッド・スタブ
        OsolApiErrorMessageResponse osolApiErrorMessageResponse = new OsolApiErrorMessageResponse();
        osolApiErrorMessageResponse.setResultCode(resultCode);

        OsolApiErrorMessageResult osolApiErrorMessageResult = new OsolApiErrorMessageResult();
        osolApiErrorMessageResult.setErrorMessage(errorMessage);

        osolApiErrorMessageResponse.setResult(osolApiErrorMessageResult);

        return osolApiErrorMessageResponse;
    }

    @Override
    protected BaseApiResponse createErrorMessageListResponse(String resultCode, List<String> errorMessageList) {

        OsolApiErrorMessageListResponse osolApiErrorMessageListResponse = new OsolApiErrorMessageListResponse();
        osolApiErrorMessageListResponse.setResultCode(resultCode);

        OsolApiErrorMessageListResult osolApiErrorMessageListResult = new OsolApiErrorMessageListResult();
        osolApiErrorMessageListResult.setErrorMessageList(errorMessageList);

        osolApiErrorMessageListResponse.setResult(osolApiErrorMessageListResult);

        return osolApiErrorMessageListResponse;
    }

    @Override
    protected void settingResponse(HttpServletResponse response, BaseApiResponse apiResponse) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().println("test");
        } catch (IOException e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
    }

    public boolean csvPrint(String csvFileDir, String csvFileName, String charsetName, List<List<String>> csvDataList) {

        File dir = new File(csvFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File csvFile = new File(csvFileDir.concat(File.separator).concat(csvFileName));
        try(OutputStream csvOut = new FileOutputStream(csvFile, false);
            OutputStreamWriter outSw = new OutputStreamWriter(csvOut, Charset.forName(charsetName));
            CSVPrinter printer = new CSVPrinter(outSw, CSVFormat.EXCEL)) {
            for (Iterator<List<String>> list = csvDataList.iterator(); list.hasNext();) {
                printer.printRecord(list.next());
            }
            return true;
        } catch (Exception e) {
            errorLogger.error(BaseUtility.getStackTraceMessage(e));
        }
        return false;
    }

    /**
     * アラートメールの検針日時検索メソッド(検針過去日検索)
     * @param mMeter
     * @return
     */
    public String searchLatestInspDateBefore(MMeter mMeter) {
        //返却する日時
        String returnStr = "";

        Calendar nowDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH");
        String nowYear = Integer.valueOf(nowDate.get(Calendar.YEAR)).toString();
        String nowMonth = Integer.valueOf(nowDate.get(Calendar.MONTH) + DeviceCtrlConstants.one).toString();
        String nowDay = Integer.valueOf(nowDate.get(Calendar.DAY_OF_MONTH)).toString();;
        String nowHour = Integer.valueOf(nowDate.get(Calendar.HOUR_OF_DAY)).toString();

        MAutoInsp mAutoInsp = new MAutoInsp();
        MAutoInspPK mAutoInspPK = new MAutoInspPK();

        List<Calendar> dbDateList = new ArrayList<>();
        String dbDateSet = "";

        mAutoInspPK.setMeterType(mMeter.getMeterType());
        mAutoInspPK.setDevId(mMeter.getId().getDevId());
        mAutoInsp.setId(mAutoInspPK);
        if(!mAutoInspDao.isNull(mAutoInsp)) {
            mAutoInsp = mAutoInspDao.find(mAutoInsp);
            for(int i=0;i<DeviceCtrlConstants.twelve;i++) {
                //今年をまずは入れておく
                String dbYear = nowYear;
                String dbDay = mAutoInsp.getDay();
                String dbHour = mAutoInsp.getHour().toString();
                String dbMonth ="";

                //左から1が立っているか見て行く
                if(mAutoInsp.getMonth().substring(i,i+1).equals(DeviceCtrlConstants.one.toString())) {
                    dbMonth = month[i];
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MMddHH");
                    String retM = String.format("%02d", Integer.valueOf(nowMonth));
                    String retD = String.format("%02d", Integer.valueOf(nowDay));
                    String retH = String.format("%02d", Integer.valueOf(nowHour));

                    String strDateN = retM + retD + retH;

                    retM = String.format("%02d", Integer.valueOf(dbMonth));
                    retD = String.format("%02d", Integer.valueOf(dbDay));
                    retH = String.format("%02d", Integer.valueOf(dbHour));

                    String strDateDB = retM + retD + retH;

                    try {
                        Date dateN = sdf2.parse(strDateN);
                        Date dateD = sdf2.parse(strDateDB);
                        if(dateN.before(dateD)) {
                            Integer retInt = Integer.valueOf(dbYear) -1;
                            dbYear = retInt.toString();
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    dbDateSet = dbYear + "/" + dbMonth + "/" + dbDay + " " + dbHour;

                    try {
                        Date retDate = sdf.parse(dbDateSet);
                        Calendar retCal = Calendar.getInstance();
                        retCal.setTime(retDate);

                        dbDateList.add(retCal);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            return "-";
        }

        if(dbDateList.size() == 0) {
            return DeviceCtrlConstants.alertDateMessage;
        }

        //小さい順に並び替え
        Collections.sort(dbDateList);

        //返却する日時セット
        returnStr = sdf.format(dbDateList.get(dbDateList.size() - 1).getTime()) + "時";

        return returnStr;
    }

    /**
     * アラートメールの検針日時検索メソッド(検針過去日検索)
     * @param mMeter
     * @return
     */
    public String searchLatestInspDateAfter(MMeter mMeter) {
        //返却する日時
        String returnStr = "";

        Calendar nowDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH");
        String nowYear = Integer.valueOf(nowDate.get(Calendar.YEAR)).toString();
        String nowMonth = Integer.valueOf(nowDate.get(Calendar.MONTH) + DeviceCtrlConstants.one).toString();
        String nowDay = Integer.valueOf(nowDate.get(Calendar.DAY_OF_MONTH)).toString();;
        String nowHour = Integer.valueOf(nowDate.get(Calendar.HOUR_OF_DAY)).toString();

        MAutoInsp mAutoInsp = new MAutoInsp();
        MAutoInspPK mAutoInspPK = new MAutoInspPK();

        List<Calendar> dbDateList = new ArrayList<>();
        String dbDateSet = "";

        mAutoInspPK.setMeterType(mMeter.getMeterType());
        mAutoInspPK.setDevId(mMeter.getId().getDevId());
        mAutoInsp.setId(mAutoInspPK);
        if(!mAutoInspDao.isNull(mAutoInsp)) {
            mAutoInsp = mAutoInspDao.find(mAutoInsp);
            for(int i=0;i<DeviceCtrlConstants.twelve;i++) {
                //今年をまずは入れておく
                String dbYear = nowYear;
                String dbDay = mAutoInsp.getDay();
                String dbHour = mAutoInsp.getHour().toString();
                String dbMonth ="";

                //左から1が立っているか見て行く
                if(mAutoInsp.getMonth().substring(i,i+1).equals(DeviceCtrlConstants.one.toString())) {
                    dbMonth = month[i];
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MMddHH");
                    String retM = String.format("%02d", Integer.valueOf(nowMonth));
                    String retD = String.format("%02d", Integer.valueOf(nowDay));
                    String retH = String.format("%02d", Integer.valueOf(nowHour));

                    String strDateN = retM + retD + retH;

                    retM = String.format("%02d", Integer.valueOf(dbMonth));
                    retD = String.format("%02d", Integer.valueOf(dbDay));
                    retH = String.format("%02d", Integer.valueOf(dbHour));

                    String strDateDB = retM + retD + retH;

                    try {
                        Date dateN = sdf2.parse(strDateN);
                        Date dateD = sdf2.parse(strDateDB);
                        if(dateN.after(dateD)) {
                            Integer retInt = Integer.valueOf(dbYear) +1;
                            dbYear = retInt.toString();
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    dbDateSet = dbYear + "/" + dbMonth + "/" + dbDay + " " + dbHour;

                    try {
                        Date retDate = sdf.parse(dbDateSet);
                        Calendar retCal = Calendar.getInstance();
                        retCal.setTime(retDate);

                        dbDateList.add(retCal);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            return "-";
        }

        if(dbDateList.size() == 0) {
            return DeviceCtrlConstants.alertDateMessage;
        }

        //小さい順に並び替え
        Collections.sort(dbDateList);

        //返却する日時セット
        returnStr = sdf.format(dbDateList.get(0).getTime()) + "時";

        return returnStr;
    }

    /**
     * mailのアラート停止期間の判別（開始日）
     * false : 項目に含めない
     * true  : 項目に含める
     * @param dbDateStart
     * @return
     */
    public boolean sendMailPeriod(String dbDateStart, String dbDateEnd) {

        Date date = new Date(); // 今日の日付
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strToday = dateFormat.format(date);

        Integer start = dbDateStart.compareTo(strToday);
        Integer end = null;

        if(dbDateEnd != null) {
            end = strToday.compareTo(dbDateEnd);
        }

        if(dbDateStart != null) {
            //期間開始<=当日, 期間終了 == null
            //期間開始<=当日<=期間終了
            if((start <= 0 && end == null) || (start <= 0 && end <= 0)) {
                return false;
            }
        }


        return true;
    }

}

