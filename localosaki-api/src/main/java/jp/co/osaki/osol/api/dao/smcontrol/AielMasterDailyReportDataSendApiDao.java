package jp.co.osaki.osol.api.dao.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterDailyReportDataSendApiParameter;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDemandListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.SmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.weather.AmedasWeatherListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.BuildingDemandListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.weather.AmedasWeatherObservatoryNoListServiceDaoImpl;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * [AielMaster]日報・アメダスデータ送信 Dao クラス
 * アメダスデータ送信も同時に行う
 * @author nishida.t
 *
 */
@Stateless
public class AielMasterDailyReportDataSendApiDao extends BaseSmControlDao {

    //デマンド日報ポイント設定
    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;

    //機器ポイント
    private final SmPointListServiceDaoImpl smPointListServiceDaoImpl;

    //建物デマンド
    private final BuildingDemandListServiceDaoImpl buildingDmServiceDaoImpl;

    //アメダス気象データ
    private final AmedasWeatherObservatoryNoListServiceDaoImpl amedasWeatherObservatoryNoListServiceDaoImpl;

    //建物機器
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;


    public AielMasterDailyReportDataSendApiDao() {
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
        smPointListServiceDaoImpl = new SmPointListServiceDaoImpl();
        buildingDmServiceDaoImpl = new BuildingDemandListServiceDaoImpl();
        amedasWeatherObservatoryNoListServiceDaoImpl = new AmedasWeatherObservatoryNoListServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
    }

    //デマンド日報ポイント 取得
    public List<CommonDemandDayReportPointListResult> getDemandDailyReportPointList(FvpCtrlMngResponse<BaseParam> res, AielMasterDailyReportDataSendApiParameter param) throws Exception {

//        // DBサーバ時刻取得
//        Timestamp nowTimestamp = super.getServerDateTime();
//        Long id = super.createId(OsolConstants.ID_SEQUENCE_NAME.SM_CONTROL_SCHEDULE_LOG_ID.getVal());

        List<CommonDemandDayReportPointListResult> registList = new ArrayList<>();

        if (param == null) {
            return registList;
        }

        CommonDemandDayReportPointListResult paramRes = new CommonDemandDayReportPointListResult();

        paramRes.setCorpId(param.getOperationCorpId());
        paramRes.setBuildingId(param.getBuildingId());
        paramRes.setSmId(param.getCollectDataSmId());

        //From, To の日付は同じ日を入れて１日分取得
        paramRes.setMeasurementDateFrom(DateUtility.conversionDate(param.getMeasurementDateFrom(), DateUtility.DATE_FORMAT_YYYYMMDD));
        paramRes.setMeasurementDateTo(DateUtility.conversionDate(param.getMeasurementDateFrom(), DateUtility.DATE_FORMAT_YYYYMMDD));
        paramRes.setJigenNoFrom(new BigDecimal("1"));
        paramRes.setJigenNoTo(new BigDecimal("48"));
//        paramRes.setPointNoFrom(null);
//        paramRes.setPointNoTo(null);

        registList = getResultList(commonDemandDayReportPointListServiceDaoImpl, paramRes);
        return registList;
    }

    //機器ポイント 取得
    public List<SmPointListDetailResultData> getSmPointList(FvpCtrlMngResponse<BaseParam> res, AielMasterDailyReportDataSendApiParameter param) throws Exception {

        List<SmPointListDetailResultData> registList = new ArrayList<>();

        if (param == null) {
            return registList;
        }

        SmPointListDetailResultData paramRes = new SmPointListDetailResultData();

        paramRes.setSmId(param.getCollectDataSmId());

        registList = getResultList(smPointListServiceDaoImpl, paramRes);

        return registList;

    }

    //建物デマンド
    public List<BuildingDemandListDetailResultData> getBuildingDemandList(FvpCtrlMngResponse<BaseParam> res, AielMasterDailyReportDataSendApiParameter param) {

        List<BuildingDemandListDetailResultData> registList = new ArrayList<>();

        if (param == null) {
            return registList;
        }

        BuildingDemandListDetailResultData paramRes = new BuildingDemandListDetailResultData();

        paramRes.setCorpId(param.getOperationCorpId());
        paramRes.setBuildingId(param.getBuildingId());

        registList = getResultList(buildingDmServiceDaoImpl, paramRes);

        return registList;
    }

    //アメダス気象データ
    public List<AmedasWeatherListDetailResultData> getAmedasWeatherObservatoryNoList(FvpCtrlMngResponse<BaseParam> res, AielMasterDailyReportDataSendApiParameter param) throws Exception {

        List<AmedasWeatherListDetailResultData> registList = new ArrayList<>();

        if (param == null) {
            return registList;
        }

        // 当日の1:00～翌日の0:00までが取得範囲
        Date observationDateFrom = DateUtility.plusHour(DateUtility.conversionDate(param.getMeasurementDateFrom(), DateUtility.DATE_FORMAT_YYYYMMDD), 1);
        Date observationDateTo = DateUtility.plusDay(DateUtility.conversionDate(param.getMeasurementDateFrom(), DateUtility.DATE_FORMAT_YYYYMMDD), 1);

        AmedasWeatherListDetailResultData paramRes = new AmedasWeatherListDetailResultData();

        paramRes.setAmedasObservatoryNo(param.getAmedasObservatoryNo());
        paramRes.setObservationDateFrom(observationDateFrom);
        paramRes.setObservationDateTo(observationDateTo);

        registList = getResultList(amedasWeatherObservatoryNoListServiceDaoImpl, paramRes);

        return registList;
    }

    // 建物機器リスト取得
    public List<DemandBuildingSmListDetailResultData> getBuildingSmList(AielMasterDailyReportDataSendApiParameter param){
        //建物機器からデータを取得する
        DemandBuildingSmListDetailResultData buildingSmParam = new DemandBuildingSmListDetailResultData();
        buildingSmParam.setCorpId(param.getOperationCorpId());
        buildingSmParam.setBuildingId(param.getBuildingId());
        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                demandBuildingSmListServiceDaoImpl, buildingSmParam);

        return buildingSmList;
    }
}
