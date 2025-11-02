package jp.co.osaki.osol.mng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.utility.smcontrol.AielMasterLogFileUtility;
import jp.co.osaki.osol.api.utility.smcontrol.FileUploadUtility;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.constants.SmControlConstants.SMCONTROL_LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

public class FvpCtrlMng {

    /**
     * エラー用ログ
     */
    private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * 機器通信用ログ
     */
    private static Logger smControlLogger = Logger.getLogger(SMCONTROL_LOGGER_NAME.SMCONTROL.getVal());

    /**
     * Aiel Masterコマンド
     */
    private static final List<String> LIST_CMD_OF_AIEL_MASTER = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add("AM000");    // 需要電力予測データ

            add("DM10X");    // 瞬時データ
            add("DM20X");    // NAC補正値
            add("DM30X");    // 手動制御テーブル
            add("DM40X");    // AI演算結果ログ
            add("DM50X");    // 動作ログ

            add("VM000");    // エリア設定情報
            add("VM001");    // スケジュール設定情報
            add("VM002");    // 店舗設定情報
            add("VM003");    // 動作モード設定情報
            add("VM007");    // 不快指数基準値設定情報

            add("SM000");    // 気象データ送信
            add("SM001");    // アメダスデータ送信
            add("SM002");    // 日報データ送信

            add("XM000");    // エリア設定
            add("XM001");    // スケジュール設定
            add("XM002");    // 店舗設定
            add("XM003");    // 動作モード設定
            add("XM007");    // 不快指数基準値設定
        }
    });

    /**
     * データ圧縮コマンドマップ
     */
    private static final Map<String, List<String>> COMPRESS_DATA_CMD_MAP;
    static {
        Map<String, List<String>> map = new HashMap<>();

        // FV2
        map.put(SmControlConstants.PRODUCT_CD_FV2,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // No target command.
                    }
                })
                );
        // FVP(D)
        map.put(SmControlConstants.PRODUCT_CD_FVP_D,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // No target command.
                    }
                })
                );
        // FVPα(D)
        map.put(SmControlConstants.PRODUCT_CD_FVP_ALPHA_D,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // No target command.
                    }
                })
                );
        // FVPα(G2)
        map.put(SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // No target command.
                    }
                })
                );
        // Eα
        map.put(SmControlConstants.PRODUCT_CD_E_ALPHA,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // No target command.
                    }
                })
                );
        // Eα2
        map.put(SmControlConstants.PRODUCT_CD_E_ALPHA_2,
                Collections.unmodifiableList(new ArrayList<String>() {
                    {
                        // Eα2
                        add("X0048");  add("X0049");  add("X0002");  add("X0001");  add("X0051");  add("X0052");
                        add("X0045");  add("X0057");  add("X0050");
                        // Aiel Master
                        add("XM000");  add("XM001");  add("XM002");  add("SM000");  add("SM001");  add("SM002");
                    }
                })
                );

        COMPRESS_DATA_CMD_MAP = Collections.unmodifiableMap(map);
    }


    /**
     * ソケット通信 リトライ回数
     */
    private int retryCount;

    /**
     * ソケット通信 タイムアウト
     */
    private int timeout;

    /**
     * ソケット通信 コープID
     */
    private String corpId;

    /**
     * ソケット通信 パーソンID
     */
    private String personId;

    /**
     * ソケット通信 機器ID
     */
    private long smId;

    /**
     * ソケット通信 IPアドレス
     */
    private String ip;

    /**
     * ソケット通信 ポート
     */
    private int port;

    /**
     * 機器からの応答コード
     */
    private byte smResponseCode;

    /**
     * 製品コード
     */
    private String productCd;

    /**
     * ファイルアップロード Utiltiy
     */
    private FileUploadUtility fileUploadUtility;

    /**
     * 出力ファイル
     */
    private AielMasterLogFileUtility outputFile;

    /**
     * コンストラクタ
     *
     * @param retryCount
     * @param timeout
     * @param corpId
     * @param personId
     * @param smId
     */
    public FvpCtrlMng(int retryCount,int timeout, String corpId, String personId, Long smId, String productCd, FileUploadUtility fileUploadUtility, AielMasterLogFileUtility outputFile) {
        this.retryCount = retryCount;
        this.timeout = timeout * 1000;
        this.corpId = corpId;
        this.personId = personId;
        this.smId = smId;
        this.productCd = productCd;
        this.setSmResponseCode(SmControlConstants.SM_COMM_RESPONSE_ERROR);
        this.fileUploadUtility = fileUploadUtility;
        this.outputFile = outputFile;
    }

    /**
     * UDP送受信処理
     *
     * @param ip
     * @param port
     * @param fixedString
     * @param compressFlg
     * @return 受信文字列
     * @throws SmControlException
     */
    public String sendUDP(String ip, int port, String fixedString, int ctrlOption) throws SmControlException {
        int tempPort = port;
        int opt = ctrlOption & SmControlConstants.FVP_CTRL_MNG_OPT_BIT_MASK;

        //機器通信用ログ出力用にIPアドレスをフィールドで保持
        if (LIST_CMD_OF_AIEL_MASTER.contains(getCommandData(fixedString))) {
            tempPort = SmControlConstants.SM_CONTROL_UDP_PORT_AIEL_MASTER;
        }
        this.ip = ip;
        this.port = tempPort;

        // 送信先
        InetSocketAddress address =  new InetSocketAddress(ip, this.port);

        SmControlException exception = null;

        String receiveString = "";
        int cnt = 0;
        // 文字列を受信できるか、リトライ回数を超えるまで繰り返し送受信
        do{
            try (
                    DatagramChannel channel = DatagramChannel.open();
                    Selector sel = Selector.open();
                ) {
                channel.socket().bind(null);

                // 送信処理
                send(channel, address, fixedString);

                // 受信処理
                receiveString = receive(channel, sel, opt);
            } catch (SmControlException e) {
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                exception = e;
            } catch (Exception e) {
                // SmControlException系の例外以外の場合はリトライせずにthrow
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                errorLogger.errorf("[%s][%s](%s) Exception=%s",st.getClassName(),st.getMethodName(),st.getLineNumber(),e);
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                exception = new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN, "API_ERROR_UNKNOWN", e);
            } finally {
                cnt++;
            }
        }while(receiveString.isEmpty() && cnt< this.retryCount);

        // 最後の通信で発生した例外をthrow
        if(receiveString.isEmpty() && exception!=null) {
            throw exception;
        }

        return receiveString;
    }

    /**
     * 機器からの応答コード取得
     *
     * @return smResponseCode
     */
    public byte getSmResponseCode() {
        return smResponseCode;
    }

    /**
     * 機器からの応答コード設定
     *
     * @param smResponseCode
     */
    public void setSmResponseCode(byte smResponseCode) {
        this.smResponseCode = smResponseCode;
    }

    /**
     * 送信
     *
     * @param channel
     * @param address
     * @param fixedString
     * @throws Exception
     */
    private void send(DatagramChannel channel, InetSocketAddress address, String fixedString) throws Exception {

        // 送信元ポートの下2桁からシーケンス番号作成
        int sendPort = ((InetSocketAddress)channel.getLocalAddress()).getPort();
        int seq = sendPort % 100;
        String productCd = fixedString.substring(0, SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH);
        List<String> compressDataCmdList = COMPRESS_DATA_CMD_MAP.get(productCd);

        byte[] tempFixedByte;
        if ((compressDataCmdList != null) && compressDataCmdList.contains(getCommandData(fixedString))) {
            // データ圧縮が必要な場合、データ部を圧縮を実施
            byte[] head = fixedString.substring(0, getTelegramDataHeadLen(fixedString)).getBytes();
            byte[] data = fixedString.substring((getTelegramDataHeadLen(fixedString) + SmControlConstants.SM_COMM_COMPRESSDATA_LENGTH)).getBytes();
            byte[] compressData = compressDataToGzip(data);

            //圧縮されたデータを16進数の文字列でログ出力する
            String output = bin2hex(compressData);
            smControlLogger.infof("%s %s %s %s %s [%s]", SmControlConstants.SM_COMM_LOG_TAG_COMP, corpId, personId, smId, ip,  output);

            byte[] compressDataSize = String.format("%06d", compressData.length).getBytes();
            tempFixedByte = ArrayUtils.addAll(ArrayUtils.addAll(head, compressDataSize), compressData);
        } else {
            tempFixedByte = fixedString.getBytes();
        }

        byte[] fixedByte = Arrays.copyOf(tempFixedByte, tempFixedByte.length);
        ByteBuffer buf = ByteBuffer.allocate(getTelegramHeadLen() + SmControlConstants.SM_COMM_DATA_LENGTH_MAX);
        int count = 1;
        int size = fixedByte.length / SmControlConstants.SM_COMM_DATA_LENGTH_MAX;
        int remainder = fixedByte.length % SmControlConstants.SM_COMM_DATA_LENGTH_MAX;

        // データが最大長より長い場合もしくは、最大長より短い場合
        if(remainder != 0) {
            size += 1;
        }

        for (int offset = 0 ; offset < fixedByte.length ; offset+=SmControlConstants.SM_COMM_DATA_LENGTH_MAX) {

            // ヘッダ生成
            // シーケンス番号:2Byte
            String head = String.format("%02d", seq);
            if(address.getPort() != SmControlConstants.SM_CONTROL_UDP_PORT_T) {
                head += createMessageHeader(productCd, size, count, fixedByte.length);
                count++;
            }
            buf.clear();
            buf.put(head.getBytes());

            // fixedByteからoffsetを引き、最大データ長を超えている場合処理に入る
            if(fixedByte.length-offset > SmControlConstants.SM_COMM_DATA_LENGTH_MAX) {
                buf.put(fixedByte, offset, SmControlConstants.SM_COMM_DATA_LENGTH_MAX);
            }else{
                // 最大データ長以下なら
                buf.put(fixedByte, offset, fixedByte.length-offset);
            }
            buf.flip();

            // ログ出力用バイト配列
            ByteBuffer bufLog = buf.asReadOnlyBuffer();
            byte[] fixed = new byte[bufLog.remaining()];
            bufLog.get(fixed);

            //送信ログ
            smControlLogger.infof("%s %s %s %s %s [%s]", SmControlConstants.SM_COMM_LOG_TAG_SEND, corpId, personId, smId, ip, new String(fixed));
            channel.send(buf, address);
        }
    }

    /**
     * 受信
     *
     * @param channel
     * @param sel
     * @param ctrlOption
     * @return
     * @throws Exception
     */
    private String receive(DatagramChannel channel, Selector sel, int ctrlOption) throws Exception {

        // 受信文字列（ヘッダ除く）
        StringBuilder result =new StringBuilder();
        List<byte[]> recDataList = new ArrayList<>();

        // リトライ・タイムアウト用 Selector
        channel.configureBlocking(false);
        channel.register(sel, SelectionKey.OP_READ);

        while (sel.select(this.timeout) > 0) {
            for (Iterator<SelectionKey> iterator = sel.selectedKeys().iterator(); iterator.hasNext();) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isReadable()) {

                    // 受信
                    byte[] receive = doReceive(selectionKey);

                    // ヘッダ長、レスポンスコード位置
                    int headLength = getTelegramHeadLen();
                    int indexResponseCode = getResponseCodeIndex();

                    // 応答コードを保持
                    if (receive.length >= indexResponseCode) {
                        this.setSmResponseCode(receive[indexResponseCode]);
                    }

                    // 受信フラグを確認
                    if (!(isResponseStatus(receive, indexResponseCode))) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        errorLogger.errorf("[%s][%s](%s) code=%s recive=%s", st.getClassName(), st.getMethodName(),st.getLineNumber(), receive[indexResponseCode], new String(receive));
                        throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE,"API_ERROR_SMCONTROL_BADRESPONSE");
                    }

                    // ヘッダ除き受信文字列に追加
                    result.append(new String(receive, headLength, receive.length - headLength));

                    // T系コマンド以外は受信データ長の確認
                    if (this.port != SmControlConstants.SM_CONTROL_UDP_PORT_T) {
                        // データ部を保持
                        recDataList.add(Arrays.copyOfRange(receive, headLength, receive.length));

                        // 総パケット数 と パケット番号 が 一致で受信終了
                        int totalPacketNum = getTotalPacketNumFromResponseData(receive);
                        int currentPacketNum = getCurrentPacketNumFromResponseData(receive);
                        if (totalPacketNum == currentPacketNum) {
                            String ret;

                            // 受信したデータを統合
                            byte[] receiveData = null;
                            for (byte[] data : recDataList) {
                                receiveData = ArrayUtils.addAll(receiveData, data);
                            }

                            // ヘッダに含まれているデータバイト数とデータ長が不一致の場合
                            if (getDataByteNumFromResponseData(receive) != receiveData.length) {
                                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                                errorLogger.errorf("[%s][%s](%s) recive=%s reciveAll=%s", st.getClassName(), st.getMethodName(), st.getLineNumber(), new String(receive), new String(receiveData));
                                throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE, "API_ERROR_SMCONTROL_BADRESPONSE");
                            } else {
                                int compressSize = getCompressDataSize(receiveData);
                                if ((this.smResponseCode == SmControlConstants.SM_COMM_RESPONSE_SUCCESS) && (compressSize > 0)) {
                                    if ((ctrlOption & SmControlConstants.FVP_CTRL_MNG_OPT_OUTPUT_TEMP_FILE) != 0) {
                                        // 圧縮データをファイル出力
                                        String outputFilePath = uploadAielMasterLog(receiveData, compressSize);
                                        if (outputFilePath == null) {
                                            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                                            errorLogger.errorf("[%s][%s](%s) Compress data output failure ...", st.getClassName(), st.getMethodName(), st.getLineNumber());
                                            throw new SmControlException(OsolApiResultCode.API_ERROR_UNKNOWN, "API_ERROR_UNKNOWN");
                                        }
                                        ret = new String(receiveData);
                                    } else {
                                        // ファイル出力しない場合は、解凍を実施
                                        String dataHead = new String(getDataHeaderFromResponseData(receiveData));
                                        byte[] temp = getCompressDataFromResponseData(receiveData);

                                        // 解凍
                                        String decompressData = null;
                                        if (temp != null) {

                                            //圧縮データを16進数の文字列に変換する
                                            String output = bin2hex(temp);
                                            smControlLogger.infof("%s %s %s %s %s [%s]", SmControlConstants.SM_COMM_LOG_TAG_COMP, corpId, personId, smId, ip,  output);
                                            decompressData = decompressDataFromGzip(temp);
                                        }
                                        ret = dataHead + decompressData;
                                    }
                                } else {
                                    ret = new String(receiveData);
                                }
                            }
                            return ret;
                        }
                    } else {
                        return result.toString();
                    }
                }
            }
        }

        // タイムアウトログ
        smControlLogger.infof("%s %s %s %s %s [TimeOut]", SmControlConstants.SM_COMM_LOG_TAG_RECV, corpId, personId, smId, ip);
        throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_SOCTIMEOUT,"API_ERROR_SMCONTROL_SOCTIMEOUT");
    }

    /**
     * 受信処理（単一）
     *
     * @param selectionKey
     * @return 受信文字列
     * @throws IOException
     * @throws SmControlException
     */
    private byte[] doReceive(SelectionKey selectionKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(getTelegramHeadLen() + SmControlConstants.SM_COMM_DATA_LENGTH_MAX);
        buf.clear();
        datagramChannel.receive(buf);
        buf.flip();
        byte[] data = new byte[buf.limit()];
        buf.get(data);
        // 受信ログ
        smControlLogger.infof("%s %s %s %s %s [%s]", SmControlConstants.SM_COMM_LOG_TAG_RECV, corpId, personId, smId, ip,  new String(data));
        return data;
    }

    /**
     * GZIP形式のデータに圧縮
     *
     * @param data
     * @return
     * @throws Exception
     */
    private byte[] compressDataToGzip(byte[] data) throws Exception {
        byte[] compressData;

        // 圧縮
        ByteArrayOutputStream compressBaos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip =  new GZIPOutputStream(compressBaos)) {
            gzip.write(data);
        }
        compressData = compressBaos.toByteArray();

        return compressData;
    }

    /**
     * GZIP形式のデータを解凍
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String decompressDataFromGzip(byte[] data) throws Exception {
        StringBuilder decompressData = new StringBuilder();

        try (GZIPInputStream gzip =  new GZIPInputStream(new ByteArrayInputStream(data))) {
            int size = 0;
            byte[] byteDatas = new byte[1024];
            while ((size = gzip.read(byteDatas)) > 0) {
                decompressData.append(new String(byteDatas, 0, size));
            }
        }

        return decompressData.toString();
    }

    /**
     * 電文ヘッダー生成 (シーケンス番号以降)
     *
     * @param productCd
     * @param tag1
     * @param tag2
     * @param dataLen
     * @return
     */
    private String createMessageHeader(String productCd, int tag1, int tag2, int dataLen) {
        String ret = null;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            // 全送信回数:2Byte 現送信回数:2Byte 全送信長:6Byte 異常フラグ1Byte
            ret = String.format("%02d%02d%06d0", tag1, tag2, dataLen);
        } else {
            // 全送信回数:1Byte 現送信回数:1Byte 全送信長:4Byte 異常フラグ1Byte
            ret = String.format("%d%d%04d0", tag1, tag2, dataLen);
        }

        return ret;
    }

    /**
     * 電文のヘッダー長を取得
     *
     * @return
     */
    private int getTelegramHeadLen() {
        int ret = 0;

        if (this.port != SmControlConstants.SM_CONTROL_UDP_PORT_T) {
            if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
                ret = SmControlConstants.SM_COMM_HEAD_LENGTH_PTN2;
            } else {
                ret = SmControlConstants.SM_COMM_HEAD_LENGTH;
            }
        } else {
            ret = SmControlConstants.SM_COMM_SEQ_LENGTH;
        }

        return ret;
    }

    /**
     * コマンドデータを取得
     *
     * @param fixedString
     * @return
     */
    private String getCommandData(String fixedString) {
        String ret;
        int beginIndex = SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH + SmControlConstants.SM_COMM_ADDRESS_LENGTH;
        int endIndex = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH;

        if (fixedString.length() > beginIndex) {
            ret = fixedString.substring(beginIndex, endIndex);
        } else {
            ret = fixedString;
        }

        return ret;
    }

    /**
     * 電文のデータヘッダー長を取得
     *
     * @return
     */
    private int getTelegramDataHeadLen(String fixedString) {
        int ret = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            // コマンド部を取得
            String command = getCommandData(fixedString);
            // データヘッダー長を設定
            if (LIST_CMD_OF_AIEL_MASTER.contains(command)) {
                ret = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_INC_DATE;
            }
        }

        return ret;
    }

    /**
     * 応答電文の応答コードインデックスを取得
     *
     * @return
     */
    private int getResponseCodeIndex() {
        int ret = 0;

        if (this.port != SmControlConstants.SM_CONTROL_UDP_PORT_T) {
            if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
                ret = SmControlConstants.SM_COMM_DATAPOINT_RESPONSECODE_PTN2;
            } else {
                ret = SmControlConstants.SM_COMM_DATAPOINT_RESPONSECODE;
            }
        } else {
            ret = SmControlConstants.SM_COMM_DATAPOINT_RESPONSECODE_T;
        }

        return ret;
    }

    /**
     * 応答電文から総パケット数(タグ1)を取得
     *
     * @param receiveData
     * @return
     */
    private int getTotalPacketNumFromResponseData(byte[] receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            String strPacketNum = new String(Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATAPOINT_TAG1_PTN2, SmControlConstants.SM_COMM_DATAPOINT_TAG2_PTN2));
            if (strPacketNum.matches("^-?\\d+$")) {
                ret = Integer.parseInt(strPacketNum);
            }
        } else {
            ret = receiveData[SmControlConstants.SM_COMM_DATAPOINT_TAG1];
        }

        return ret;
    }

    /**
     * 応答電文から対象パケット数(タグ2)を取得
     *
     * @param receiveData
     * @return
     */
    private int getCurrentPacketNumFromResponseData(byte[] receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            String strPacketNum = new String(Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATAPOINT_TAG2_PTN2, SmControlConstants.SM_COMM_DATAPOINT_DATABYTE_LENGTH_PTN2));
            if (strPacketNum.matches("^-?\\d+$")) {
                ret = Integer.parseInt(strPacketNum);
            }
        } else {
            ret = receiveData[SmControlConstants.SM_COMM_DATAPOINT_TAG2];
        }

        return ret;
    }

    /**
     * 応答電文からデータバイト数を取得
     *
     * @param receiveData
     * @return
     */
    private int getDataByteNumFromResponseData(byte[] receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            String strPacketNum = new String(Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATAPOINT_DATABYTE_LENGTH_PTN2, SmControlConstants.SM_COMM_DATAPOINT_RESPONSECODE_PTN2));
            if (strPacketNum.matches("^-?\\d+$")) {
                ret = Integer.parseInt(strPacketNum);
            }
        } else {
            String strDataByteNum = new String(Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATAPOINT_DATABYTE_LENGTH, SmControlConstants.SM_COMM_DATAPOINT_RESPONSECODE));
            if (strDataByteNum.matches("^-?\\d+$")) {
                ret = Integer.parseInt(strDataByteNum);
            }
        }

        return ret;
    }

    /**
     * 応答電文の応答ステータスを判定
     *
     * @param receiveData
     * @param indexResponseCode
     * @return true:正常応答 false:異常応答 (非正常)
     */
    private boolean isResponseStatus(byte[] receiveData, int indexResponseCode) {
        boolean ret = true;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(this.productCd) || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            if (receiveData.length < indexResponseCode
                    || (this.smResponseCode != SmControlConstants.SM_COMM_RESPONSE_SUCCESS && this.smResponseCode != SmControlConstants.SM_COMM_RESPONSE_NONE)) {
                ret = false;
            }
        } else {
            if (receiveData.length < indexResponseCode
                    || this.smResponseCode != SmControlConstants.SM_COMM_RESPONSE_SUCCESS) {
                ret = false;
            }
        }

        return ret;
    }

    /**
     * 応答電文から圧縮データ長を取得
     *   応答電文に圧縮データ長が含まれない場合は"0"を返却
     *
     * @param receiveData
     * @return
     */
    private int getCompressDataSize(byte[] receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            // 応答電文からコマンド部を取得
            String command = new String(Arrays.copyOfRange(receiveData,
                    SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH + SmControlConstants.SM_COMM_ADDRESS_LENGTH,
                    SmControlConstants.SM_COMM_DATA_HEAD_LENGTH));

            // 応答電文から圧縮データ長部を取得
            int compressDataSizeFromPos = 0;
            int compressDataSizeToPos = 0;
            if (LIST_CMD_OF_AIEL_MASTER.contains(command)) {
                compressDataSizeFromPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_INC_DATE;
                compressDataSizeToPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS_INC_DATE;
            } else {
                compressDataSizeFromPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH;
                compressDataSizeToPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS;
            }
            String compressDataSize = new String(Arrays.copyOfRange(receiveData, compressDataSizeFromPos, compressDataSizeToPos));

            // 圧縮データ長部のデータが数値変換可能であれば、数値に変換
            if (compressDataSize.matches("^-?\\d+$")) {
                ret = Integer.parseInt(compressDataSize);
            }
        }

        return ret;
    }

    /**
     * 応答電文からデータヘッダーを取得
     *
     * @param receiveData
     * @return
     */
    private byte[] getDataHeaderFromResponseData(byte[] receiveData) {
        byte[] ret = null;
        int dataHeaderFromPos = 0;
        int dataHeaderToPos = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            // 応答電文からコマンド部を取得
            String command = new String(Arrays.copyOfRange(receiveData,
                    SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH + SmControlConstants.SM_COMM_ADDRESS_LENGTH,
                    SmControlConstants.SM_COMM_DATA_HEAD_LENGTH));

            // 応答電文からデータヘッダー部を取得
            if (LIST_CMD_OF_AIEL_MASTER.contains(command)) {
                dataHeaderToPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS_INC_DATE;
            } else {
                dataHeaderToPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS;
            }
        } else {
            dataHeaderToPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH;
        }

        ret = Arrays.copyOfRange(receiveData, dataHeaderFromPos, dataHeaderToPos);

        return ret;
    }

    /**
     * 応答電文から圧縮データを取得
     *
     * @param receiveData
     * @return
     */
    private byte[] getCompressDataFromResponseData(byte[] receiveData) {
        byte[] ret = null;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            // 応答電文からコマンド部を取得
            String command = new String(Arrays.copyOfRange(receiveData,
                    SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH + SmControlConstants.SM_COMM_ADDRESS_LENGTH,
                    SmControlConstants.SM_COMM_DATA_HEAD_LENGTH));

            // 応答電文から圧縮データ部を取得
            int compressDataFromPos = 0;
            int compressDataToPos = receiveData.length;
            if (LIST_CMD_OF_AIEL_MASTER.contains(command)) {
                compressDataFromPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS_INC_DATE;
            } else {
                compressDataFromPos = SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS;
            }
            ret = Arrays.copyOfRange(receiveData, compressDataFromPos, compressDataToPos);
        }

        return ret;
    }

    /**
     * 応答電文から日付情報を取得
     *
     * @param receiveData
     * @return
     */
    private String getDateInfoFromResponseData(byte[] receiveData) {
        String ret = null;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            // 応答電文からコマンド部を取得
            String command = new String(Arrays.copyOfRange(receiveData,
                    SmControlConstants.SM_COMM_PRODUCT_CD_LENGTH + SmControlConstants.SM_COMM_ADDRESS_LENGTH,
                    SmControlConstants.SM_COMM_DATA_HEAD_LENGTH));

            // Aiel Masterコマンドであれば、応答電文から日付情報を取得
            if (LIST_CMD_OF_AIEL_MASTER.contains(command)) {
                ret = new String(Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATA_HEAD_LENGTH, SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_INC_DATE));
            }
        }

        return ret;
    }

    private String uploadAielMasterLog(byte[] receiveData, int compressSize) {
        String ret = null;
        String resDate = null;
        String outputDir = null;
        String outputFileName = null;
        byte[] compressData;

        if ((fileUploadUtility == null) || (outputFile == null)) {
            return ret;
        }

        // 応答日付を取得
        resDate = getDateInfoFromResponseData(receiveData);
        outputFile.setResDayTime(resDate);

        // ファイル出力先を生成
        outputDir = fileUploadUtility.getAielMasterLogUploadDir(outputFile);
        outputFileName = fileUploadUtility.getAielMasterLogFileName(outputFile);

        // 応答電文から圧縮データを取得
        compressData = Arrays.copyOfRange(receiveData, SmControlConstants.SM_COMM_DATA_HEAD_LENGTH_COMPRESS_INC_DATE, receiveData.length);

        //圧縮データを16進数の文字列に変換する
        String output = bin2hex(compressData);
        smControlLogger.infof("%s %s %s %s %s [%s]", SmControlConstants.SM_COMM_LOG_TAG_COMP, corpId, personId, smId, ip,  output);

        // 一時領域へアップロード
        ret = fileUploadUtility.uploadFile(outputDir, outputFileName, compressData, compressSize);

        return ret;
    }

    //byte配列から16進数の文字列変換処理
    public static String bin2hex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (byte b : data) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

}