package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * スケジュール(設定) Eα Param クラス
 *
 * @autho t_hayama
 *
 */
public class A200056Param extends BaseParam {

	/**
	 * 計測ポイント情報
	 */
	@NotNull
	@Pattern(regexp="[0-9]")
	private String measurePointInfo;

	/**
	 * 計測ポイント情報リスト
	 */
	@NotNull
	private List<Map<String, String>> measurePointInfoList;

	/**
	 * プロット用ポイント No.1
	 */
	private String plotPoint1;

	/**
	 * プロット用ポイント No.2
	 */
	private String plotPoint2;


	@AssertTrue
	public boolean isValidateMeasurePointInfoList() {
		return measurePointInfoList != null && validateList(measurePointInfoList, new HashMap<String, String>() {
			{
				put("addressNo",         "[0-9]{1,2}");                  // アドレス番号
				put("portNo",            "[0-9]{1,2}");                  // ポート番号
				put("pointType",         "[0-9]");                       // ポイント種別
				put("pulseWeight",       "[0-9]{1,6}");                  // パルス重み
				put("blockingDirection", "[0-9]");                       // 遮断状態方向
				put("sensorType",        "[0-9]{1,2}");                  // センサタイプ
				put("headScale",         "[\\s\\S0-9]{5}");              // ヘッドスケール
				put("bottomScale",       "[\\s\\S0-9]{5}");              // ボトムスケール
			}
		});
	}


	public String getMeasurePointInfo() {
		return measurePointInfo;
	}

	public void setMeasurePointInfo(String measurePointInfo) {
		this.measurePointInfo = measurePointInfo;
	}

	public List<Map<String, String>> getMeasurePointInfoList() {
		return measurePointInfoList;
	}

	public void setMeasurePointInfoList(List<Map<String, String>> measurePointInfoList) {
		this.measurePointInfoList = measurePointInfoList;
	}

	public String getPlotPoint1() {
		return plotPoint1;
	}

	public void setPlotPoint1(String plotPoint1) {
		this.plotPoint1 = plotPoint1;
	}

	public String getPlotPoint2() {
		return plotPoint2;
	}

	public void setPlotPoint2(String plotPoint2) {
		this.plotPoint2 = plotPoint2;
	}


	@Override
	public boolean partDataComparison(Object oldData) {
		// 一括機器制御系：比較処理
		// スケジュール管理指定比較
		return true;
	}

}
