package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DayReportSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DayReportSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.DayReportSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200039Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * 日報(取得) Bean クラス
 *
 * @autho da_yamano
 *
 */

@Named(value = SmControlConstants.DAY_REPORT_SELECT)
@RequestScoped
public class DayReportSelectBean
extends AbstractApiBean<DayReportSelectResult, DayReportSelectParameter> {


	@EJB
	private DayReportSelectDao dao;

	@Override
	protected SmCntrolDao getSmCntrolDao() {
		return dao;
	}

	// 計測ポイント
	private String mesurePointStr;

	// 計測ポイントリスト
	private List<String> mesurePointList;

	// コマンドパラメータ
	private String commandParam;

	// 機器情報
	private SmPrmResultData smPrm;

	// コマンド判別
	private String judgeCommand;

	@Override
	protected <T extends BaseParam> T initParam(DayReportSelectParameter parameter) {
		A200039Param param = new A200039Param();

		if (param != null) {

			// コマンドパラメータを取得
			param.setCommandParameter(parameter.getCommandParameter());
			param.setUpdateDBflg(parameter.isUpdateDBflg());

			// コマンドパラメータをクラス内に保持
			this.commandParam =parameter.getCommandParameter();

			// 計測ポイントに値が存在すれば処理を行う
			if(parameter.getMeasurePointList() == null || parameter.getMeasurePointList().isEmpty()) {
				@SuppressWarnings("unchecked")
				T ret = (T) param;
				return ret;
			} else {
				// 計測ポイントをクラス内に保持
				this.mesurePointStr =parameter.getMeasurePointList();

				// リストの型に成形したString型の計測ポイント
				String rePointList = mesurePointStr.replace("]", "").replace("[", "");

				// リスト化したポイントリスト
				List<String> mesurePointList = Arrays.asList(rePointList.split(","));
				this.mesurePointList = mesurePointList;

				// ポイントリストの0番目をString型に変換
				String castMPoint = String.valueOf(mesurePointList.get(0));

				// ポイントリスト0番目の値を格納
				param.setMeasurePointList(castMPoint);
			}
		}

		@SuppressWarnings("unchecked")
		T ret = (T) param;

		return ret;
	}

	// 機種依存チェック(FVP2、FVP(D)、FVPa(D)、FVPa(G2)以外はエラー)
	@Override
	protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

		// 機器情報をクラス内に保持
		this.smPrm=smPrm;

		// paramを取得
		A200039Param parameter = (A200039Param) param;

		// 対応機器チェック
		if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
				&& !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
			StackTraceElement st = Thread.currentThread().getStackTrace()[1];
			super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}

		// FVPa(G2)用処理
		if("12".equals(smPrm.getProductCd())) {
			if(parameter.getMeasurePointList()==null || parameter.getMeasurePointList().isEmpty()) {

				// 計測ポイントリストに値が存在しなければコマンドにHを格納
				param.setCommand(SmControlConstants.COMMAND_INITIALS_H);

				// HD0000を格納
				this.judgeCommand = SmControlConstants.COMMAND_HD;

			} else {

				// 計測ポイントリストに値が存在すればコマンドにDを格納
				param.setCommand(SmControlConstants.COMMAND_INITIALS_D);

				//D00000を格納
				this.judgeCommand = SmControlConstants.COMMAND_D0;
			}



		}else if("00".equals(smPrm.getProductCd()) || "03".equals(smPrm.getProductCd())
				|| "06".equals(smPrm.getProductCd())){//FVP2、FVP(D)、FVPa(D)用処理

			// DB0000を格納
			this.judgeCommand = SmControlConstants.COMMAND_DB;
		}

		return true;
	}

	// オブジェクトに値を格納する処理
	@Override
	protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

		// param取得
		@SuppressWarnings("unchecked")
		FvpCtrlMngResponse<A200039Param> res = (FvpCtrlMngResponse<A200039Param>) response;

		if(judgeCommand.equals(SmControlConstants.COMMAND_D0)) {

			// addCalendarDayReportメソッド呼び出し
			addCalendarDayReport(res);

		}else if(judgeCommand.equals(SmControlConstants.COMMAND_DB)){

			// addDemandDayReportメソッド呼び出し
			addDemandDayReport(res);

		}else if(judgeCommand.equals(SmControlConstants.COMMAND_HD)){

			// addDemandMeasureDataメソッド呼び出し
			addDemandMeasureData(res);;
		}

		// 親の引数を上書き
		response = res;
	}

	//ポイントリストD0000用にポイントリストに追加する処理とカレンダ日報に全値を格納する処理
	private void addCalendarDayReport(FvpCtrlMngResponse<A200039Param> res) throws Exception {

		//ポイントリストD0000用にポイントリストに追加する処理

		//ポイントリスト生成
		List<Map<String, List<Map<String,String>>>> pointList = new ArrayList<>();

		//param生成
		A200039Param param = (A200039Param)res.getParam();

		Map<String, List<Map<String,String>>> map  = new HashMap<>();

		//時限リストを追加
		map.put("timeList", param.getTimeList());

		//ポイントリストに時限リストを追加
		pointList.add(map);

		//計測ポイントリスト2番目から処理を開始する
		for (int i = 1; i < this.mesurePointList.size(); i++) {

			//新規parameter生成
			A200039Param parameter = new A200039Param();

			//機器通信を行うためコマンドを格納
			parameter.setCommand(SmControlConstants.COMMAND_INITIALS_D);

			//機器通信を行うためコマンドパラメータを格納
			parameter.setCommandParameter(this.commandParam);

			//計測ポイントリストのi番目の数値をString型に変換
			String castMPoint = String.valueOf(this.mesurePointList.get(i));

			//計測ポイントリストに数値を格納
			parameter.setMeasurePointList(castMPoint);

			//機器通信リクエスト生成
			FvpCtrlMngRequest<BaseParam> req =new FvpCtrlMngRequest<>(smPrm,super.apiParameter.getLoginCorpId(),
					super.apiParameter.getLoginPersonId(),super.loginUserId);
			req.setParam(parameter);

			//機器通信呼び出し
			FvpCtrlMngResponse<BaseParam> baseRes = fvpCtrlMngClient.excute(req);
			A200039Param paramF = (A200039Param)baseRes.getParam();

			Map<String, List<Map<String,String>>> mapF  = new HashMap<>();

			//時限リストを追加
			mapF.put("timeList", paramF.getTimeList());

			//ポイントリストに時限リストを追加
			pointList.add(mapF);

		}//計測ポイントリスト分処理を行う

		//ポイントリストD0000用にポイントリストを追加
		param.setPointListD0(pointList);

		//既存の時限リストは不要の為、空にする
		param.setTimeList(null);


		// カレンダ日報に全値を格納する処理

		Map<String, Object> calendarDayReport = new HashMap<String, Object>();

		// mapに全値を追加
		calendarDayReport.put(SmControlConstants.DAY_REPORT_COMMAND, res.getParam().getCommand());
		calendarDayReport.put(SmControlConstants.MEASURE_DAY_TIME, param.getMeasureDayTime());
		calendarDayReport.put(SmControlConstants.AGGREGATE_HOUR, param.getAggregateHour());
		calendarDayReport.put(SmControlConstants.POINT_LIST, pointList);

		// 空のparamを生成
		A200039Param parameter = new A200039Param();

		// 値を格納
		parameter.setAddress(res.getParam().getAddress());
		parameter.setProductCd(res.getParam().getProductCd());
		parameter.setCalendarDayReport(calendarDayReport);

		// レスポンスに格納
		res.setParam(parameter);
	}

	// デマンド日報に全値を格納する処理
	private void addDemandDayReport(FvpCtrlMngResponse<A200039Param> res) throws SmControlException {

		// param生成
		A200039Param param = (A200039Param)res.getParam();

		Map<String, Object> demandDatReport = new HashMap<String, Object>();

		// mapに全値を追加
		demandDatReport.put(SmControlConstants.DAY_REPORT_COMMAND, res.getParam().getCommand());
		demandDatReport.put(SmControlConstants.MEASURE_DAY_TIME, param.getMeasureDayTime());
		demandDatReport.put(SmControlConstants.AGGREGATE_HOUR, param.getAggregateHour());
		demandDatReport.put(SmControlConstants.MEASURE_POINT_NUM, param.getMeasurePointNum());
		demandDatReport.put(SmControlConstants.POINT_LIST, param.getPointList());
		demandDatReport.put(SmControlConstants.DAY_MAX_DEMAND, param.getDayMaxDemand());
		demandDatReport.put(SmControlConstants.DAY_MAX_DEMAND_TIME, param.getDayMaxDemandTime());
		demandDatReport.put(SmControlConstants.MONTH_MAX_DEMAND, param.getMonthMaxDemand());
		demandDatReport.put(SmControlConstants.MONTH_MAX_DEMANDTIME, param.getMonthMaxDemandTime());
		demandDatReport.put(SmControlConstants.YEAR_MAX_DEMAND, param.getYearMaxDemand());
		demandDatReport.put(SmControlConstants.YEAR_MAX_DEMAND_TIME, param.getYearMaxDemandTime());

		// 空のparamを生成
		A200039Param parameter = new A200039Param();

		// 値を格納
		parameter.setAddress(res.getParam().getAddress());
		parameter.setProductCd(res.getParam().getProductCd());
		parameter.setDemandDayReport(demandDatReport);

		// レスポンスに格納
		res.setParam(parameter);
	}

	//30分計測データに全値を格納する処理
	private void addDemandMeasureData(FvpCtrlMngResponse<A200039Param> res) throws SmControlException {

		// param生成
		A200039Param param = (A200039Param)res.getParam();

		Map<String, Object> demandMeasureData = new HashMap<String, Object>();

		// mapに全値を追加
		demandMeasureData.put(SmControlConstants.DAY_REPORT_COMMAND, res.getParam().getCommand());
		demandMeasureData.put(SmControlConstants.MEASURE_DAY_TIME, param.getMeasureDayTime());
		demandMeasureData.put(SmControlConstants.TIME_NUM_LIST, param.getTimeNumList());

		// 空のparamを生成
		A200039Param parameter = new A200039Param();

		// 値を格納
		parameter.setAddress(res.getParam().getAddress());
		parameter.setProductCd(res.getParam().getProductCd());
		parameter.setDemandMeasureData(demandMeasureData);

		// レスポンスに格納
		res.setParam(parameter);
	}

}