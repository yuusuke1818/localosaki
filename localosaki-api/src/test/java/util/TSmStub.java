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

public class TSmStub implements Runnable {
	/*
	 * T系コマンド用スタブ
	 */

	private boolean isRun = false;

	// class名保持
	private String API_ID = "";
	// テスト番号取得
	private String METHOD_NAME = "";
	// アドレス記憶
	private SocketAddress SOCKET_ADDRESS;

	// 受信ポート
	private int recPort;

	// ログファイル情報
	private SmControlTestUtil LOG_UTIL;

	//
	private DatagramChannel channel;

	//3000番ポート利用時
	public TSmStub(String apiId) {
		this.API_ID = apiId;
		this.isRun = true;
		this.recPort = 3000;
	}

	//3000番ポート以外利用時
	public TSmStub(String apiId, int recPort) {
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
		channel.socket().close();
	}

	public void setMethodName(String methodName) {
		this.METHOD_NAME = methodName;
	}
	public void setUtil(SmControlTestUtil util) {
		this.LOG_UTIL = util;
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

					return seq;
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
		int size = fixedStringList.size();
		int count = 1;
		List<String> sendList = new ArrayList<>();

		for(String body : fixedStringList) {

			// ヘッダ シーケンス番号
			String head = seqense;

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

}