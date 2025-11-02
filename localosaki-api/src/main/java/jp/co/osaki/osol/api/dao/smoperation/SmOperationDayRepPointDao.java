/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.smoperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationDayRepPointParameter;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.result.smoperation.SmOperationDayRepPointResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.smoperation.SmOperationPointDayRepResultData;
import jp.co.osaki.osol.api.resultdata.smoperation.SmOperationPointPointDataResultData;
import jp.co.osaki.osol.api.resultdata.smoperation.SmOperationPointPointResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonDemandDayReportPointListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmPointListServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御DB閲覧 日報データ取得 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class SmOperationDayRepPointDao extends OsolApiDao<SmOperationDayRepPointParameter> {

    private final DemandBuildingSmPointListServiceDaoImpl demandBuildingSmPointListServiceDaoImpl;
    private final CommonDemandDayReportPointListServiceDaoImpl commonDemandDayReportPointListServiceDaoImpl;

    public SmOperationDayRepPointDao() {
        demandBuildingSmPointListServiceDaoImpl = new DemandBuildingSmPointListServiceDaoImpl();
        commonDemandDayReportPointListServiceDaoImpl = new CommonDemandDayReportPointListServiceDaoImpl();
    }

    @Override
    public SmOperationDayRepPointResult query(SmOperationDayRepPointParameter parameter) throws Exception {
        String corpId = parameter.getOperationCorpId();
        Long buildingId = parameter.getBuildingId();
        Long smId = parameter.getSmId();
        Date measurementDateFrom = parameter.getMeasurementDateFrom();
        Date measurementDateTo = parameter.getMeasurementDateTo();

        //計測年月日ToがNULLの場合、計測年月日Fromと同じ値を入れる
        if(measurementDateTo == null) {
            measurementDateTo = measurementDateFrom;
        }

        SmOperationDayRepPointResult result = new SmOperationDayRepPointResult();
        result.setCorpId(corpId);
        result.setBuildingId(buildingId);
        result.setSmId(smId);

        //企業、建物、機器のポイント一覧を取得する
        DemandBuildingSmPointListDetailResultData pointParam = new DemandBuildingSmPointListDetailResultData();
        pointParam.setCorpId(corpId);
        pointParam.setBuildingId(buildingId);
        pointParam.setSmId(smId);

        List<DemandBuildingSmPointListDetailResultData> pointResultList = getResultList(demandBuildingSmPointListServiceDaoImpl,pointParam);

        //データ取得に失敗またはデータが存在しない場合は処理を終了する
        if(pointResultList == null || pointResultList.size() == 0) {
            return result;
        }

        //ポイント番号 受電・001～256ソート
        pointResultList.sort(
                (DemandBuildingSmPointListDetailResultData c1, DemandBuildingSmPointListDetailResultData c2) -> {
                    String p1 = "SRC".equals(c1.getPointNo()) ? "000" : c1.getPointNo();
                    String p2 = "SRC".equals(c2.getPointNo()) ? "000" : c2.getPointNo();
                    return p1.compareTo(p2);
                });

        //ポイント分のポイント情報取得結果を作成
        List<SmOperationPointPointResultData> pointDetailList = new ArrayList<>();

        //ポイント番号とポイント名称を取得
        for(DemandBuildingSmPointListDetailResultData point:pointResultList) {

            SmOperationPointPointResultData pointDetail = new SmOperationPointPointResultData();
            pointDetail.setPointNo(point.getPointNo());
            pointDetail.setPointName(point.getPointName());

            pointDetailList.add(pointDetail);
        }
        result.setPointDetailList(pointDetailList);

        Date currentDate = null;
        List<SmOperationPointDayRepResultData> dayRepList = new ArrayList<>();

        do {

            if (currentDate == null) {
                currentDate = measurementDateFrom;
            } else {
                //1日進める
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

            SmOperationPointDayRepResultData dayRep = new SmOperationPointDayRepResultData();
            dayRep.setMeasurementDate(currentDate);

            List<SmOperationPointPointDataResultData> pointDataList = new ArrayList<>();
            for(DemandBuildingSmPointListDetailResultData point:pointResultList) {
                SmOperationPointPointDataResultData pointData = new SmOperationPointPointDataResultData();
                pointData.setPointNo(point.getPointNo());

                CommonDemandDayReportPointListResult pointDataParam = new CommonDemandDayReportPointListResult();
                pointDataParam.setCorpId(corpId);
                pointDataParam.setBuildingId(buildingId);
                pointDataParam.setSmId(smId);
                pointDataParam.setMeasurementDateFrom(currentDate);
                pointDataParam.setMeasurementDateTo(currentDate);
                pointDataParam.setPointNoFrom(point.getPointNo());
                pointDataParam.setJigenNoFrom(BigDecimal.ONE);
                pointDataParam.setJigenNoTo(BigDecimal.valueOf(48));

                List<CommonDemandDayReportPointListResult> dayPointDataList = getResultList(commonDemandDayReportPointListServiceDaoImpl,pointDataParam);
                if(dayPointDataList == null || dayPointDataList.size() == 0) {
                }else {
                    setPointValue(dayPointDataList,pointData);
                }

                pointDataList.add(pointData);
            }
            dayRep.setPointDataList(pointDataList);

            dayRepList.add(dayRep);

        } while(!currentDate.equals(measurementDateTo));
        result.setMeasurementDateList(dayRepList);

        return result;
    }

    /**
     * デマンド日報ポイントのポイント値をセットする
     * @param dayPointDataList
     * @param pointData
     */
    private void setPointValue(List<CommonDemandDayReportPointListResult> dayPointDataList, SmOperationPointPointDataResultData pointData) {

        for(CommonDemandDayReportPointListResult point:dayPointDataList) {
            if(point.getJigenNo().compareTo(BigDecimal.ONE) == 0) {
                pointData.setPointVal1(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(2)) == 0) {
                pointData.setPointVal2(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(3)) == 0) {
                pointData.setPointVal3(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(4)) == 0) {
                pointData.setPointVal4(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(5)) == 0) {
                pointData.setPointVal5(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(6)) == 0) {
                pointData.setPointVal6(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(7)) == 0) {
                pointData.setPointVal7(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(8)) == 0) {
                pointData.setPointVal8(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(9)) == 0) {
                pointData.setPointVal9(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.TEN) == 0) {
                pointData.setPointVal10(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(11)) == 0) {
                pointData.setPointVal11(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(12)) == 0) {
                pointData.setPointVal12(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(13)) == 0) {
                pointData.setPointVal13(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(14)) == 0) {
                pointData.setPointVal14(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(15)) == 0) {
                pointData.setPointVal15(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(16)) == 0) {
                pointData.setPointVal16(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(17)) == 0) {
                pointData.setPointVal17(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(18)) == 0) {
                pointData.setPointVal18(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(19)) == 0) {
                pointData.setPointVal19(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(20)) == 0) {
                pointData.setPointVal20(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(21)) == 0) {
                pointData.setPointVal21(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(22)) == 0) {
                pointData.setPointVal22(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(23)) == 0) {
                pointData.setPointVal23(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(24)) == 0) {
                pointData.setPointVal24(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(25)) == 0) {
                pointData.setPointVal25(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(26)) == 0) {
                pointData.setPointVal26(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(27)) == 0) {
                pointData.setPointVal27(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(28)) == 0) {
                pointData.setPointVal28(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(29)) == 0) {
                pointData.setPointVal29(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(30)) == 0) {
                pointData.setPointVal30(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(31)) == 0) {
                pointData.setPointVal31(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(32)) == 0) {
                pointData.setPointVal32(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(33)) == 0) {
                pointData.setPointVal33(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(34)) == 0) {
                pointData.setPointVal34(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(35)) == 0) {
                pointData.setPointVal35(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(36)) == 0) {
                pointData.setPointVal36(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(37)) == 0) {
                pointData.setPointVal37(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(38)) == 0) {
                pointData.setPointVal38(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(39)) == 0) {
                pointData.setPointVal39(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(40)) == 0) {
                pointData.setPointVal40(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(41)) == 0) {
                pointData.setPointVal41(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(42)) == 0) {
                pointData.setPointVal42(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(43)) == 0) {
                pointData.setPointVal43(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(44)) == 0) {
                pointData.setPointVal44(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(45)) == 0) {
                pointData.setPointVal45(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(46)) == 0) {
                pointData.setPointVal46(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(47)) == 0) {
                pointData.setPointVal47(point.getPointVal());
            }else if(point.getJigenNo().compareTo(BigDecimal.valueOf(48)) == 0) {
                pointData.setPointVal48(point.getPointVal());
            }
        }
    }
}
