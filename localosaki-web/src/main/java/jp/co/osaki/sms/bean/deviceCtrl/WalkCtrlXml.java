package jp.co.osaki.sms.bean.deviceCtrl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
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

import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import jp.co.osaki.osol.api.response.osolapi.OsolApiErrorMessageListResponse;
import jp.co.osaki.osol.api.response.osolapi.OsolApiErrorMessageResponse;
import jp.co.osaki.osol.api.result.osolapi.OsolApiErrorMessageListResult;
import jp.co.osaki.osol.api.result.osolapi.OsolApiErrorMessageResult;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.osol.entity.TInspectionMeterPK;
import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.dao.TInspectionMeterDao;
import jp.co.osaki.sms.dao.TMeterDataDao;
import jp.co.osaki.sms.deviceCtrl.dao.MAlertMailDao;
import jp.co.osaki.sms.deviceCtrl.dao.MAutoInspDao;
import jp.co.osaki.sms.deviceCtrl.dao.MBuildingSmsDao;
import jp.co.osaki.sms.deviceCtrl.dao.MConcentratorDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevPrmDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevRelationDao;
import jp.co.osaki.sms.deviceCtrl.dao.MDevRelationJoinTBuildingDao;
import jp.co.osaki.sms.deviceCtrl.dao.MManualInspDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterLoadlimitDao;
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
import jp.co.osaki.sms.deviceCtrl.response.CreateDeviceXmlResponse;
import jp.co.osaki.sms.deviceCtrl.response.ErrorResponse;
import jp.co.osaki.sms.deviceCtrl.response.MMeterResponse;
import jp.co.osaki.sms.deviceCtrl.resultset.MDevRelationJoinTBuildingResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.MMeterResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.TRfMeterInfoResultSet;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiResponse;
import jp.skygroup.enl.webap.base.api.BaseApiServlet;

@WebServlet(name = "WalkCtrlXml", urlPatterns = {"/walkCtrl","/walk/bin/*" }, description = "XML形式で受け取る", displayName = "OSOL API XML Servlet")
public class WalkCtrlXml extends BaseApiServlet {

    private static final long serialVersionUID = -6336507619797445302L;

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

    //ハンディ端末コマンド
    final String[] walkCmd = {"get-rfmeterinfo","set-rfmeterinfo"};
    final String[] tCmd = {"get-dlsdata","get-dmvdata","get-rdmvdata","get-ainspdata","get-minspdata","get-rdlsdata","get-meterctrl","get-meterinfo","get-meterval","get-spitconf","set-interval","meter-list","repeater-list","concent-list","manual-inspect"};
    final String[] exceptionCmd = {"send-rfmeterinfo"};

    /**t_commandテーブルにしか登録されないコマンド*/
    final String[] tCmdOnly = {"get-meterctrl","get-meterinfo","get-meterval","get-spitconf","set-interval","meter-list","repeater-list","concent-list","manual-inspect"};
    final String[] tCmdBulk = {"get-meterctrl","get-meterinfo","get-spitconf"};


    final String[] cmdConcentrator = {"add-concent","del-concent","init-concent"};
    final String[] cmdRepeater = {"add-repeater","del-repeater"};
    final String[] cmdMeter = {"add-meter","del-meter","chg-meter", "set-breaker", "set-spitconf"};
    final String[] cmdLoadlimit = {"set-loadlimit","get-meterctrl"};


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


    /**
     * smControlログ
     */
    private static final Logger smCtrlLogger = Logger.getLogger(DeviceCtrlConstants.SmControl);
    /**
     * エラーログ
     */
    private static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());


    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {

        Map<String,String> nodeMap = new HashMap<>();
        String bodyString = "";

        //通信仕様を見てエラーコードを設定
        String[] errorCode = new String[1];
        errorCode[0] = "";

        /** データが存在しない・認証エラー
         * trueの場合はエラー発生 **/
        boolean[] errorFlg = new boolean[1];
        errorFlg[0] = false;

        /** 異常処理用のやつ*/
        String dataErr = "";

        /** 認証用 */
        String dateDev = "";

        /** 機器通信終了 */
        Boolean endFlg = false;

        nodeMap.clear();

        //ヘッダー情報出力
        headerOut(request);

        //セッションIDを付与
        HttpSession session = request.getSession();
        String id = session.getId();
        Cookie cookie = new Cookie("JSESSID",id);
        cookie.setPath("/");
        response.addCookie(cookie);

        try {
            request.setCharacterEncoding("EUC-JP");
            //httpリクエストのbody部分をString型で取りだす
            bodyString = request.getReader().lines().collect(Collectors.joining("\r\n"));
            bodyString = bodyString.replace("\0", "");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try(StringReader reader = new StringReader(bodyString)){
            DocumentBuilderFactory responseFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder responseDocBuilder = responseFactory.newDocumentBuilder();
            Document responseDocument = responseDocBuilder.newDocument();

            smCtrlLogger.info("INPUT:\n".concat(bodyString));

            String codeName = "";
            int beginIndex;
            int endIndex;


            beginIndex = bodyString.indexOf(DeviceCtrlConstants.authS) + DeviceCtrlConstants.authS.length();
            endIndex = bodyString.indexOf(DeviceCtrlConstants.coron);
            String devId = bodyString.substring(beginIndex, endIndex);

            beginIndex = bodyString.indexOf(DeviceCtrlConstants.coron) + DeviceCtrlConstants.coron.length();
            endIndex = bodyString.indexOf(DeviceCtrlConstants.authE);
            String devPw = bodyString.substring(beginIndex, endIndex);

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
//            }

            //返信用のcommandの子要素, テキストを追加
            Element command = responseDocument.createElement(DeviceCtrlConstants.command);

            dateDev = nodeMap.get(DeviceCtrlConstants.date);
            dataErr = nodeMap.get(DeviceCtrlConstants.data);

            //機器のID:PWチェック
            devAuth(devId, devPw, errorFlg);

            //xmlの日付チェック
            dateAuth(dateDev, errorFlg);

            if(codeName.equals(DeviceCtrlConstants.none)) {
                endFlg = true;
            }

            //装置から来たxmlがエラーを調べる
            if(codeName.equals(DeviceCtrlConstants.error)) {
                //エラー 01,02,03の場合
                if(dataErr.equals(DeviceCtrlConstants.syntaxErr)) {
                    errorLogger.error("errorCode : " + dataErr);
                    errorLogger.error("devId : " + devId);
                    endFlg = true;
                }


            }

            smCtrlLogger.info("code : " + codeName + " devId : " + devId);

            if(!errorFlg[0] && !endFlg) {
                if(codeName != null && !codeName.equals("")) {
                    if(!codeName.equals(DeviceCtrlConstants.error)) {
                        //装置idとpwが一致している　且　機器制御コマンドが記載されている　且　時刻が基準日より後の場合
                        commandCheck(devId, codeName, errorFlg, nodeMap, bodyString, errorCode);
                        if(Arrays.asList(exceptionCmd).contains(codeName)) {
                            //特殊ケースの処理
                            command = exceptionCommandCheck(devId, codeName, responseDocument, command, errorCode);
                        }else if(!errorFlg[0]) {
                            command = whatXml(command, responseDocument);
                        }
                    }
                }else{
                    //walkはt_commandからt_work_hstへ移すものが無いので何もしない
                    command = whatXml(command, responseDocument);
                }
            }




            if(errorFlg[0]) {
                if(CheckUtility.isNullOrEmpty(errorCode[0])) {
                    errorCode[0] = DeviceCtrlConstants.boundaryValueErr;
                }
                ErrorResponse errorResponse = new ErrorResponse();
                command = errorResponse.responseTxt(errorCode[0], responseDocument, command);
            }else if(endFlg) {
                CreateDeviceXmlResponse endResponse = new CreateDeviceXmlResponse();
                command = endResponse.endResponseTxt(responseDocument, command);
            }

            contents.appendChild(command);
            response.setContentType("text/plain;charset=EUC-JP");
            //responseのbodyに書き込む
            try(PrintWriter outBody = response.getWriter();) {
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

    public void commandCheck(String devId, String codeName, boolean[] errorFlg, Map<String,String> nodeMap, String bodyString, String[] errorCode) {
        MDevRelation mDevRelation = new MDevRelation();
        MDevRelationPK mDevRelationPK = new MDevRelationPK();
        String requestDate = "";
        NodeList nodes;

        /** DB用 */
        List<TRfMeterInfoResultSet> rfMeterInfoList = new ArrayList<>();

        /** xml用 */
        List<TRfMeterInfoResultSet> xmlRfMeterInfoList = new ArrayList<>();
        switch(codeName) {
        case DeviceCtrlConstants.getRfmeterinfo:


            requestDate = nodeMap.get(DeviceCtrlConstants.date);
            requestDate = requestDate.substring(0,8);


            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.meterData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                TRfMeterInfoResultSet retTRfMeterInfoResultSet = new TRfMeterInfoResultSet();
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);

                if(CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterMngId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.meterType)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.tenantId))
                        || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.wirelessId)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.latestInspVal)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.multi)) || CheckUtility.isNullOrEmpty(nodeMap.get(DeviceCtrlConstants.latestInspDate))) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.commandErr;
                    return;
                }

                retTRfMeterInfoResultSet.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                retTRfMeterInfoResultSet.setMeterType(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterType)));
                retTRfMeterInfoResultSet.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));
                retTRfMeterInfoResultSet.setTenantId(nodeMap.get(DeviceCtrlConstants.tenantId));
                retTRfMeterInfoResultSet.setBuildingName(Objects.isNull(nodeMap.get(DeviceCtrlConstants.name)) ? "" : nodeMap.get(DeviceCtrlConstants.name));
                retTRfMeterInfoResultSet.setAddress1(Objects.isNull(nodeMap.get(DeviceCtrlConstants.address1)) ? "" : nodeMap.get(DeviceCtrlConstants.address1));
                retTRfMeterInfoResultSet.setAddress2(Objects.isNull(nodeMap.get(DeviceCtrlConstants.address2)) ? "" : nodeMap.get(DeviceCtrlConstants.address2));
                retTRfMeterInfoResultSet.setWirelessId(nodeMap.get(DeviceCtrlConstants.wirelessId));

                retTRfMeterInfoResultSet.setHop1Id(Objects.isNull(nodeMap.get(DeviceCtrlConstants.hop1Id)) ? "" : nodeMap.get(DeviceCtrlConstants.hop1Id));
                retTRfMeterInfoResultSet.setHop2Id(Objects.isNull(nodeMap.get(DeviceCtrlConstants.hop2Id)) ? "" : nodeMap.get(DeviceCtrlConstants.hop2Id));
                retTRfMeterInfoResultSet.setHop3Id(Objects.isNull(nodeMap.get(DeviceCtrlConstants.hop3Id)) ? "" : nodeMap.get(DeviceCtrlConstants.hop3Id));
                retTRfMeterInfoResultSet.setPollingId(Objects.isNull(nodeMap.get(DeviceCtrlConstants.pollingId)) ? "" : nodeMap.get(DeviceCtrlConstants.pollingId));
                retTRfMeterInfoResultSet.setLatestInspVal(nodeMap.get(DeviceCtrlConstants.latestInspVal));
                retTRfMeterInfoResultSet.setAlarm(Objects.isNull(nodeMap.get(DeviceCtrlConstants.alarm)) ? "" : nodeMap.get(DeviceCtrlConstants.alarm));
                retTRfMeterInfoResultSet.setPrevInspVal(Objects.isNull(nodeMap.get(DeviceCtrlConstants.prevInspVal)) ? "" : nodeMap.get(DeviceCtrlConstants.prevInspVal));


                try {
                    retTRfMeterInfoResultSet.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                    retTRfMeterInfoResultSet.setLatestInspDateStr(nodeMap.get(DeviceCtrlConstants.latestInspDate));
                    retTRfMeterInfoResultSet.setPrevInspDateStr(Objects.isNull(nodeMap.get(DeviceCtrlConstants.prevInspDate)) ? "" : nodeMap.get(DeviceCtrlConstants.prevInspDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //該当メーターがm_build_dev_meter_relationテーブルに登録されてるかどうかのチェック(tenant_id)
                MMeter mMeter = new MMeter();
                MMeterPK mMeterPK = new MMeterPK();
                mMeterPK.setDevId(devId);
                mMeterPK.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                mMeter.setId(mMeterPK);
                mMeter.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));


                TBuilding tBuilding = new TBuilding();
                tBuilding = searchTenantBuilding(mMeter);

                if(mMeterDao.isNull(mMeter) || searchTenant(mTenantSmsDao,tBuilding).equals(null)) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.noData;
                    return;
                }

                rfMeterInfoList.add(retTRfMeterInfoResultSet);
            }

            mDevRelationPK.setDevId(devId);
            mDevRelation.setId(mDevRelationPK);
            if(mDevRelationDao.isNull(mDevRelation)) {
                errorFlg[0] = true;
                errorCode[0] = DeviceCtrlConstants.syntaxErr;
                return;
            }

            // 出力ファイルの作成
            try(FileWriter f = new FileWriter(DeviceCtrlConstants.fPathCsv + "rfmeter_info_" + devId + "_" + requestDate + ".csv", false);
                PrintWriter p = new PrintWriter(new BufferedWriter(f))) {

                for(int i=0;i<rfMeterInfoList.size();i++) {
                    p.print(devId + "," + rfMeterInfoList.get(i).getMeterMngId() + "," + rfMeterInfoList.get(i).getMeterType() + "," + rfMeterInfoList.get(i).getMeterId() + "," + rfMeterInfoList.get(i).getTenantId() +
                            "," + rfMeterInfoList.get(i).getBuildingName() + "," + rfMeterInfoList.get(i).getAddress1() + "," + rfMeterInfoList.get(i).getAddress2() + "," + rfMeterInfoList.get(i).getWirelessId() +
                            "," + rfMeterInfoList.get(i).getHop1Id() + "," + rfMeterInfoList.get(i).getHop2Id() + "," + rfMeterInfoList.get(i).getHop3Id() + "," + rfMeterInfoList.get(i).getPollingId() +
                            "," + rfMeterInfoList.get(i).getMulti() + "," + rfMeterInfoList.get(i).getLatestInspVal() + "," + rfMeterInfoList.get(i).getLatestInspDateStr() + "," + rfMeterInfoList.get(i).getAlarm() +
                            "," + rfMeterInfoList.get(i).getPrevInspVal() + "," + rfMeterInfoList.get(i).getPrevInspDateStr() + "\n");
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            break;
        case DeviceCtrlConstants.setRfmeterinfo:
            MMeterResultSet sendMMeterRfResultSet = new MMeterResultSet();

            //装置が結びついている建物のIDと建物名を取得
            //装置はテナントで登録できないので、この処理で問題ない
            MDevRelationJoinTBuildingResultSet sendRfMDevRelationJoinTBuildingResultSet = new MDevRelationJoinTBuildingResultSet();
            sendRfMDevRelationJoinTBuildingResultSet.setDevId(devId);
            sendRfMDevRelationJoinTBuildingResultSet = mMeterJoinTBuildingRelationDao.find(sendRfMDevRelationJoinTBuildingResultSet);

            sendMMeterRfResultSet.setDevId(devId);
            rfMeterInfoList.addAll(chkSetRfInfo(sendMMeterRfResultSet));
            //ここまでが今のDB最新データ取得

            if(errorFlg[0]) {
                errorCode[0] = DeviceCtrlConstants.noData;
            }

            //xmlの内容を詰めて行く
            nodes = getXmlList(DeviceCtrlConstants.meterData, bodyString);

            for (int i = 0; i < nodes.getLength(); i++) {
                nodeMap.clear();
                Node ret = nodes.item(i);
                allNodeSearchToMap(ret, nodeMap);
                TRfMeterInfoResultSet tRfMeterInfoResultSet = new TRfMeterInfoResultSet();

                tRfMeterInfoResultSet.setMeterMngId(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterMngId)));
                tRfMeterInfoResultSet.setMeterType(Long.parseLong(nodeMap.get(DeviceCtrlConstants.meterType)));
                tRfMeterInfoResultSet.setMeterId(nodeMap.get(DeviceCtrlConstants.meterId));
                tRfMeterInfoResultSet.setTenantId(nodeMap.get(DeviceCtrlConstants.tenantId));
                tRfMeterInfoResultSet.setBuildingName(nodeMap.get(DeviceCtrlConstants.name));
                tRfMeterInfoResultSet.setWirelessId(nodeMap.get(DeviceCtrlConstants.wirelessId));

                if(nodeMap.get(DeviceCtrlConstants.address1) == null) {
                    tRfMeterInfoResultSet.setAddress1("");
                }else {
                    tRfMeterInfoResultSet.setAddress1(nodeMap.get(DeviceCtrlConstants.address1));
                }

                if(nodeMap.get(DeviceCtrlConstants.address2) == null) {
                    tRfMeterInfoResultSet.setAddress2("");
                }else {
                    tRfMeterInfoResultSet.setAddress2(nodeMap.get(DeviceCtrlConstants.address2));
                }

                if(nodeMap.get(DeviceCtrlConstants.hop1Id) == null) {
                    tRfMeterInfoResultSet.setHop1Id("");
                }else {
                    tRfMeterInfoResultSet.setHop1Id(nodeMap.get(DeviceCtrlConstants.hop1Id));
                }

                if(nodeMap.get(DeviceCtrlConstants.hop2Id) == null) {
                    tRfMeterInfoResultSet.setHop2Id("");
                }else {
                    tRfMeterInfoResultSet.setHop2Id(nodeMap.get(DeviceCtrlConstants.hop2Id));
                }

                if(nodeMap.get(DeviceCtrlConstants.hop3Id) == null) {
                    tRfMeterInfoResultSet.setHop3Id("");
                }else {
                    tRfMeterInfoResultSet.setHop3Id(nodeMap.get(DeviceCtrlConstants.hop3Id));
                }

                if(nodeMap.get(DeviceCtrlConstants.pollingId) == null) {
                    tRfMeterInfoResultSet.setPollingId("");
                }else {
                    tRfMeterInfoResultSet.setPollingId(nodeMap.get(DeviceCtrlConstants.pollingId));
                }

                try {
                    tRfMeterInfoResultSet.setMulti(StringUtility.toBigDecimal(nodeMap.get(DeviceCtrlConstants.multi)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                xmlRfMeterInfoList.add(tRfMeterInfoResultSet);
            }

            for(int i=0;i<xmlRfMeterInfoList.size();i++) {
                if(!xmlRfMeterInfoList.get(i).getMeterMngId().equals(rfMeterInfoList.get(i).getMeterMngId()) ||
                        !xmlRfMeterInfoList.get(i).getMeterType().equals(rfMeterInfoList.get(i).getMeterType()) ||
                        !xmlRfMeterInfoList.get(i).getMeterId().equals(rfMeterInfoList.get(i).getMeterId()) ||
                        !xmlRfMeterInfoList.get(i).getTenantId().equals(rfMeterInfoList.get(i).getTenantId()) ||
                        !xmlRfMeterInfoList.get(i).getBuildingName().equals(rfMeterInfoList.get(i).getBuildingName()) ||
                        !xmlRfMeterInfoList.get(i).getAddress1().equals(rfMeterInfoList.get(i).getAddress1()) ||
                        !xmlRfMeterInfoList.get(i).getAddress2().equals(rfMeterInfoList.get(i).getAddress2()) ||
                        !xmlRfMeterInfoList.get(i).getWirelessId().equals(rfMeterInfoList.get(i).getWirelessId()) ||
                        !xmlRfMeterInfoList.get(i).getHop1Id().equals(rfMeterInfoList.get(i).getHop1Id()) ||
                        !xmlRfMeterInfoList.get(i).getHop2Id().equals(rfMeterInfoList.get(i).getHop2Id()) ||
                        !xmlRfMeterInfoList.get(i).getHop3Id().equals(rfMeterInfoList.get(i).getHop3Id()) ||
                        !xmlRfMeterInfoList.get(i).getPollingId().equals(rfMeterInfoList.get(i).getPollingId())) {
                    errorFlg[0] = true;
                    errorCode[0] = DeviceCtrlConstants.syntaxErr;

                    if(!xmlRfMeterInfoList.get(i).getMeterMngId().equals(rfMeterInfoList.get(i).getMeterMngId())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: meterMngId");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getMeterMngId());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getMeterMngId());
                    }

                    if(!xmlRfMeterInfoList.get(i).getMeterType().equals(rfMeterInfoList.get(i).getMeterType())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: meterType");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getMeterType());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getMeterType());
                    }

                    if(!xmlRfMeterInfoList.get(i).getMeterId().equals(rfMeterInfoList.get(i).getMeterId())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: meterId");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getMeterId());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getMeterId());
                    }

                    if(!xmlRfMeterInfoList.get(i).getTenantId().equals(rfMeterInfoList.get(i).getTenantId())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: tenantId");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getTenantId());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getTenantId());
                    }

                    if(!xmlRfMeterInfoList.get(i).getBuildingName().equals(rfMeterInfoList.get(i).getBuildingName())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: buildingName");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getBuildingName());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getBuildingName());
                    }

                    if(!xmlRfMeterInfoList.get(i).getAddress1().equals(rfMeterInfoList.get(i).getAddress1())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: Address1");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getAddress1());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getAddress1());
                    }

                    if(!xmlRfMeterInfoList.get(i).getAddress2().equals(rfMeterInfoList.get(i).getAddress2())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: Address2");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getAddress2());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getAddress2());
                    }

                    if(!xmlRfMeterInfoList.get(i).getWirelessId().equals(rfMeterInfoList.get(i).getWirelessId())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: WirelessId");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getWirelessId());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getWirelessId());
                    }

                    if(!xmlRfMeterInfoList.get(i).getHop1Id().equals(rfMeterInfoList.get(i).getHop1Id())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: hop1Id");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getHop1Id());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getHop1Id());
                    }

                    if(!xmlRfMeterInfoList.get(i).getHop2Id().equals(rfMeterInfoList.get(i).getHop2Id())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: hop2Id");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getHop2Id());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getHop2Id());
                    }

                    if(!xmlRfMeterInfoList.get(i).getHop3Id().equals(rfMeterInfoList.get(i).getHop3Id())) {
                        errorLogger.error("error command : " + codeName);
                        errorLogger.error("not equal data: hop1Id3");
                        errorLogger.error("xml : " + xmlRfMeterInfoList.get(i).getHop3Id());
                        errorLogger.error("db : " + rfMeterInfoList.get(i).getHop3Id());
                    }



                    return;
                }
            }

            break;

        default:
            break;
        }

    }

    public  List<TRfMeterInfoResultSet> chkSetRfInfo(MMeterResultSet mMeterResultSet) {
        MMeter entity = new MMeter();
        List<MMeter> list = new ArrayList<>();
        MMeterPK id = new MMeterPK();
        id.setDevId(mMeterResultSet.getDevId());
        entity.setId(id);
        //削除されていないものを取得する
        entity.setDelFlg(DeviceCtrlConstants.zero);

        TInspectionMeter entityT = new TInspectionMeter();
        List<TInspectionMeter> listT = new ArrayList<>();
        TInspectionMeterPK idT = new TInspectionMeterPK();
        idT.setDevId(mMeterResultSet.getDevId());
        entityT.setId(idT);

        List<TRfMeterInfoResultSet> listTRf = new ArrayList<>();

        list.addAll(mMeterDao.getMMeterList(entity));
        listT.addAll(tInspectionMeterDao.getTInspectionMeterList(entityT));
        //ループを限界まで減らす
        int j=0;
        for(int i=0;i<list.size();i++) {
            TBuilding retTBuilding = new TBuilding();
            retTBuilding = searchTenant(list.get(i), tBuildingDao, tBuildDevMeterRelationDao);

            MTenantSm mTenantSm = new MTenantSm();
            mTenantSm = searchTenant(mTenantSmsDao, retTBuilding);

            TRfMeterInfoResultSet tRfMeterInfoResultSet = new TRfMeterInfoResultSet();

            tRfMeterInfoResultSet.setMeterMngId(list.get(i).getId().getMeterMngId());
            tRfMeterInfoResultSet.setMeterType(list.get(i).getMeterType());
            tRfMeterInfoResultSet.setMeterId(list.get(i).getMeterId());
            tRfMeterInfoResultSet.setTenantId(mTenantSm.getTenantId().toString());
            tRfMeterInfoResultSet.setBuildingName(retTBuilding.getBuildingName());
            if(retTBuilding.getAddress() == null) {
                tRfMeterInfoResultSet.setAddress1("");
            }else {
                tRfMeterInfoResultSet.setAddress1(retTBuilding.getAddress());
            }

            if(mTenantSm.getAddress2() == null) {
                tRfMeterInfoResultSet.setAddress2("");
            }else {
                tRfMeterInfoResultSet.setAddress2(mTenantSm.getAddress2());
            }

            tRfMeterInfoResultSet.setWirelessId(list.get(i).getWirelessId());

            if(list.get(i).getHop1Id() == null) {
                tRfMeterInfoResultSet.setHop1Id("");
            }else {
                tRfMeterInfoResultSet.setHop1Id(list.get(i).getHop1Id());
            }

            if(list.get(i).getHop2Id() == null) {
                tRfMeterInfoResultSet.setHop2Id("");
            }else {
                tRfMeterInfoResultSet.setHop2Id(list.get(i).getHop2Id());
            }

            if(list.get(i).getHop3Id() == null) {
                tRfMeterInfoResultSet.setHop3Id("");
            }else {
                tRfMeterInfoResultSet.setHop3Id(list.get(i).getHop3Id());
            }

            if(list.get(i).getPollingId() == null) {
                tRfMeterInfoResultSet.setPollingId("");
            }else {
                tRfMeterInfoResultSet.setPollingId(list.get(i).getPollingId());
            }

            tRfMeterInfoResultSet.setMulti(list.get(i).getMulti());

            //2重ループだと遅いが一旦これで...一度見た部分は観ないようにjの値を使いまわす。
            for(;j<listT.size();j++) {
                //一度setしたらもう見ない(取得時にソートしてるので最新値は一番最初に来るようにしてる。)
                if(tRfMeterInfoResultSet.getPrevInspVal() != null) {
                    break;
                }
                if(tRfMeterInfoResultSet.getMeterMngId().equals(listT.get(j).getId().getMeterMngId())) {
                    tRfMeterInfoResultSet.setPrevInspDate(listT.get(j).getPrevInspDate());
                    tRfMeterInfoResultSet.setPrevInspVal(listT.get(j).getPrevInspVal());
                }
            }

            listTRf.add(tRfMeterInfoResultSet);

        }

        return listTRf;

    }


    /**
     * 特殊ケースの処理
     * @param devId
     * @param codeName
     * @param responseDocument
     * @param command
     * @return
     */
    public Element exceptionCommandCheck(String devId, String codeName, Document responseDocument, Element command, String[] errorCode) {

        switch(codeName) {
        //noneが来た場合機器通信終了
        case DeviceCtrlConstants.none:
            return endXml(command, responseDocument);
            //無線メーター情報要求(send)
        case DeviceCtrlConstants.sendRfmeterinfo:
            MMeterResponse sendMeterRfResponse = new MMeterResponse();
            MMeterResultSet sendMMeterRfResultSet = new MMeterResultSet();

            MDevRelationJoinTBuildingResultSet sendRfMDevRelationJoinTBuildingResultSet = new MDevRelationJoinTBuildingResultSet();
            sendRfMDevRelationJoinTBuildingResultSet.setDevId(devId);
            sendRfMDevRelationJoinTBuildingResultSet = mMeterJoinTBuildingRelationDao.find(sendRfMDevRelationJoinTBuildingResultSet);

            sendMMeterRfResultSet.setDevId(devId);
            return sendMeterRfResponse.sendMeterRfInfo(mMeterDao, tInspectionMeterDao, responseDocument, command, sendMMeterRfResultSet, tBuildingDao, tBuildDevMeterRelationDao, mTenantSmsDao);

        default:
            break;
        }

        //xmlの内容だけで範囲超えた場合
        ErrorResponse errorResponse = new ErrorResponse();
        return errorResponse.responseTxt(errorCode[0], responseDocument, command);
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
        try(StringWriter stringWriter = new StringWriter();) {
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
     * @return
     */
    public void devAuth(String devId,String devPw, boolean[] errorFlg) {
        MDevPrm mDevPrm = new MDevPrm ();
        mDevPrm.setDevId(devId);

        if(mDevPrmDao.isNull(mDevPrm)) {
            errorFlg[0] = true;
            return ;
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
     * メーターがひもづいている
     * テナントの検索
     * @param mMeter
     * @return
     */
    public TBuilding searchTenant(MMeter mMeter) {
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
                    return tBuilding;
                }

            }
        }

        //テナントがなければnullで返却
        return null;
    }


    /**
     *
     * @param mMeter
     * @return TBuilding
     */
    public TBuilding searchTenantBuilding(MMeter mMeter) {
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
                    return tBuilding;
                }

            }
        }

        //テナントがなければnullで返却
        //テナントが存在しなければ登録できないはずなのでnullで返すことは無いが
        return null;
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


    /**
     * メーターがひもづいている
     * テナントの検索(m_tenant_smsから)
     * @param mMeter
     * @return MTenantSm
     */
    public MTenantSm searchTenant(MTenantSmsDao mTenantSmsDao,  TBuilding tBuilding) {

        MTenantSm mTenantSm = new MTenantSm();
        MTenantSmPK mTenantSmPK = new MTenantSmPK();

        mTenantSmPK.setBuildingId(tBuilding.getId().getBuildingId());
        mTenantSmPK.setCorpId(tBuilding.getId().getCorpId());
        mTenantSm.setId(mTenantSmPK);

        if(!mTenantSmsDao.isNull(mTenantSm)) {
            return mTenantSmsDao.find(mTenantSm);
        }

        //テナントがなければnullで返却
        return null;
    }

    /**
     * メーターがひもづいている
     * テナントの検索(TBuildingから)
     * @param mMeter
     * @return
     */
    public TBuilding searchTenant(MMeter mMeter,  TBuildingDao tBuildingDao, TBuildDevMeterRelationDao tBuildDevMeterRelationDao) {
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
                    return tBuilding;
                }

            }
        }

        //テナントがなければnullで返却
        return null;
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


}

