package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;

public class SmStub implements Runnable {
    /*
     * 3000番ポート宛のUDPメッセージを待機し、
     * メッセージを受信すると、ログ出力した後、
     * ヘッダー以外を指定ファイルから返却値、およびレスポンスコードを取得し、
     * 返却電文を作成後、ヘッダーと固定長データを分割して送信する。
     */

    private boolean isRun = false;

    // class名保持
    private String API_ID = "";
    // テスト番号取得
    private String METHOD_NAME = "";
    // アドレス記憶
    private SocketAddress SOCKET_ADDRESS;

    // 製品コード
    private String productCd = "";

    // 受信ポート
    private int recPort;

    // ログファイル情報
    private SmControlTestUtil LOG_UTIL;

    //
    private DatagramChannel channel;

    //3000番ポート利用時
    public SmStub(String apiId) {
        this.API_ID = apiId;
        this.isRun = true;
        this.recPort = 3000;
    }

    //3000番ポート以外利用時
    public SmStub(String apiId, int recPort) {
        this.API_ID = apiId;
        this.isRun = true;
        this.recPort = recPort;
    }

    public void initCase() {
        this.METHOD_NAME = "";
        this.SOCKET_ADDRESS = null;
    }

    public void stop() {
        isRun =  false;
    }

    public void setMethodName(String methodName) {
        this.METHOD_NAME = methodName;
    }
    public void setUtil(SmControlTestUtil util) {
        this.LOG_UTIL = util;
    }

    public void setProductCd(String productCd) {
        this.productCd = productCd;
    }

    @Override
    public void run() {
        try {
            channel = DatagramChannel.open();

            // 指定したポート宛のUDPメッセージを受け取るようにする
            channel.socket().bind(new InetSocketAddress(recPort));
            while(isRun) {

                // メッセージの受信を待機する
                String seq = receive(channel);

                executeConnect(channel, seq);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 受信
     * @throws IOException
     * @throws SmControlException
     */
    private void executeConnect(DatagramChannel channel, String seq) throws IOException, SmControlException {

        ///////////////////////////////////////////
        // 応答電文作成
        ///////////////////////////////////////////
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        int responseCd = 0;
        String fixedData = "";
        try {
            //取得処理
            String homePath = System.getProperty("user.dir");
            String smResponseDir = homePath + SmConstants.TEST_DATA_DIRECTORY
                    + this.API_ID + "\\" + this.METHOD_NAME;

            // stubプロパティファイル名作成
            String stubFile = this.METHOD_NAME + SmConstants.SM_RESPONSE_FILE + SmConstants.PROPERTY_EXTENSION;

            // プロパティファイル取得では値を取得する順番がランダムとなるため、
            // FileReaderより1行ずつ取得
            BufferedReader in = new BufferedReader (new FileReader(smResponseDir+"\\"+stubFile));

            // レスポンスコード返却
            responseCd = Integer.parseInt(in.readLine().split("=")[1]);

            String line;
            while((line = in.readLine()) != null) {
                // 各値を取得
                String[] field = line.split("=");

                // 固定長作成
                fixedData = fixedData + field[1];
            }
            in.close();

            System.out.println("fixedData = "+fixedData);
        } catch (Exception e) {
            this.LOG_UTIL.writeLogFile(this.METHOD_NAME + "の応答電文設定情報が読み込めません。");
            this.LOG_UTIL.writeLogFile(e.getMessage());
        }
        // 返却値送信
        send(channel, this.SOCKET_ADDRESS, seq, fixedData, responseCd );
    }

    /**
     * 受信
     * @throws IOException
     * @throws SmControlException
     */
    private String receive(DatagramChannel channel) throws IOException, SmControlException {
        // リトライ・タイムアウト用 Selector
        channel.configureBlocking(false);
        Selector sel = Selector.open();
        channel.register(sel, SelectionKey.OP_READ);
        while (sel.select(SmConstants.SM_TIMEOUT) > 0) {
            for (Iterator<SelectionKey> iterator  = sel.selectedKeys().iterator(); iterator.hasNext();) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                List<String> receiveList = new ArrayList<>();

                if (selectionKey.isReadable()) {
                    String receive = doReceive(selectionKey);
                    // sendログ用にリスト格納
                    receiveList.add(receive);
                    String seq = receive.substring(0, 2);

                    // ログ出力
                    this.LOG_UTIL.writeLogFile("[SM_RESV] : "+receive);
                    // 先頭9文字はヘッダ
                    if(receive.length() < SmConstants.SM_HEAD_LENGTH) {
                        throw new SmControlException(OsolApiResultCode.API_ERROR_SMCONTROL_BADRESPONSE,"API_ERROR_SMCONTROL_BADRESPONSE");
                    }

                    // ヘッダの全受信回数 と 現送信回数 の一致で受信終了
                    if (getTotalPacketNumFromResponseData(receive) == getCurrentPacketNumFromResponseData(receive)) {
                        // 送信受信
                        System.out.println("this.API_ID" + this.API_ID);
                        System.out.println("this.METHOD_NAME" + this.METHOD_NAME);
                        this.LOG_UTIL.smSendLog(this.API_ID, this.METHOD_NAME, receiveList);
                        return seq;
                    }
                }
            }
        }
        // タイムアウト
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
    private String doReceive(SelectionKey selectionKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocate(1400);
        buf.clear();

        // メッセージ受信時のアドレスを保存
        this.SOCKET_ADDRESS = datagramChannel.receive(buf);

        buf.flip();
        byte[] data = new byte[buf.limit()];
        buf.get(data);
        return new String(data);
    }


    /**
     * 送信
     * @throws IOException
     */
    private void send(DatagramChannel channel, SocketAddress address, String seqense, String fixedString, int responseCd) throws IOException {

        int seq = Integer.parseInt(seqense);

        // 1400バイトで分割
        List<String> fixedStringList = new ArrayList<>();
        Matcher m = Pattern.compile(".{1,1400}").matcher(fixedString);
        while(m.find()) {
            fixedStringList.add(m.group());
        }

        // 分割送信
        int size = fixedStringList.size();		// 通常テスト用
//		int size = 1000;		// 機器制御マネージャ_test_016
        int count = 1;
        List<String> sendList = new ArrayList<>();

        for(String body : fixedStringList) {

            String head = null;
            if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
                // ヘッダ シーケンス番号:2Byte 全送信回数:2Byte 現送信回数:2Byte 全送信長:6Byte 異常フラグ1Byte
                head = String.format("%02d%02d%02d%06d%d", seq, size, count++, fixedString.length(), responseCd);
            } else {
                // ヘッダ シーケンス番号:2Byte 全送信回数:1Byte 現送信回数:1Byte 全送信長:4Byte 異常フラグ1Byte
                head = String.format("%02d%d%d%04d%d", seq, size, count++, fixedString.length(), responseCd);
            }

            String sendString = head + body;
            sendList.add(sendString);
            // ログ出力
            this.LOG_UTIL.writeLogFile("[SM_SEND] : "+sendString);

            // 送信
            ByteBuffer buf = ByteBuffer.allocate(sendString.length());
            buf.clear();
            buf.put(sendString.getBytes());
            buf.flip();
            channel.send(buf, address);
        }

        // 受信ログOnly出力
        System.out.println("this.API_ID" + this.API_ID);
        System.out.println("this.METHOD_NAME" + this.METHOD_NAME);
        this.LOG_UTIL.smRecvLog(this.API_ID, this.METHOD_NAME, sendList);
    }

    /**
     * 応答電文から総パケット数(タグ1)を取得
     *
     * @param receiveData
     * @return
     */
    private int getTotalPacketNumFromResponseData(String receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            ret = Integer.parseInt(receiveData.substring(2, 4));
        } else {
            ret = Integer.parseInt(receiveData.substring(2, 3));
        }

        return ret;
    }

    /**
     * 応答電文から対象パケット数(タグ2)を取得
     *
     * @param receiveData
     * @return
     */
    private int getCurrentPacketNumFromResponseData(String receiveData) {
        int ret = 0;

        if (SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(this.productCd)) {
            ret = Integer.parseInt(receiveData.substring(4, 6));
        } else {
            ret = Integer.parseInt(receiveData.substring(3, 4));
        }

        return ret;
    }

}