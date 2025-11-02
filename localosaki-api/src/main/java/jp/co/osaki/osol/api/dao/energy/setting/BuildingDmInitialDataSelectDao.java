package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingDmInitialDataSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.BuildingDmInitialDataSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDmInitialDataSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingDmInitialDataSelectMonthDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepLineServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDmYearRepServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.setting.BuildingDmInitialDataUtility;
import jp.co.osaki.osol.entity.MBuildingDm;
import jp.co.osaki.osol.entity.MBuildingDmPK;
import jp.co.osaki.osol.entity.MBuildingSmPoint;
import jp.co.osaki.osol.entity.MBuildingSmPointPK;
import jp.co.osaki.osol.entity.MSmPoint;
import jp.co.osaki.osol.entity.TDmYearRep;
import jp.co.osaki.osol.entity.TDmYearRepLine;
import jp.co.osaki.osol.entity.TDmYearRepLinePK;
import jp.co.osaki.osol.entity.TDmYearRepPK;
import jp.co.osaki.osol.entity.TDmYearRepPoint;
import jp.co.osaki.osol.entity.TDmYearRepPointPK;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 建物導入前データ取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class BuildingDmInitialDataSelectDao extends OsolApiDao<BuildingDmInitialDataSelectParameter> {

    //TODO 参照のみのServiceDaoImplをEntityServiceDaoから取得しないようにする
    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final TDmYearRepLineServiceDaoImpl dmYearRepLineServiceDaoImpl;
    private final MBuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;
    private final TDmYearRepPointServiceDaoImpl dmYearRepPointServiceDaoImpl;
    private final MBuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final TDmYearRepServiceDaoImpl dmYearRepServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public BuildingDmInitialDataSelectDao() {
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        dmYearRepLineServiceDaoImpl = new TDmYearRepLineServiceDaoImpl();
        buildingSmPointServiceDaoImpl = new MBuildingSmPointServiceDaoImpl();
        dmYearRepPointServiceDaoImpl = new TDmYearRepPointServiceDaoImpl();
        buildingDmServiceDaoImpl = new MBuildingDmServiceDaoImpl();
        dmYearRepServiceDaoImpl = new TDmYearRepServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
    * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
    */
    @Override
    public BuildingDmInitialDataSelectResult query(BuildingDmInitialDataSelectParameter parameter) throws Exception {
        BuildingDmInitialDataSelectResult result = new BuildingDmInitialDataSelectResult();

        //選択企業IDが設定されている場合は、企業はそちらが優先
        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        //排他建物情報を取得する
        CommonBuildingExclusionResult exBuildingParam = new CommonBuildingExclusionResult();
        exBuildingParam.setCorpId(parameter.getOperationCorpId());
        exBuildingParam.setBuildingId(parameter.getBuildingId());
        List<CommonBuildingExclusionResult> exBuildingList = getResultList(commonBuildingExclusionServiceDaoImpl,
                exBuildingParam);

        //フィルタ処理を行う
        exBuildingList = buildingDataFilterDao.applyDataFilter(exBuildingList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exBuildingList == null || exBuildingList.size() != 1) {
            return new BuildingDmInitialDataSelectResult();
        }

        List<BuildingDmInitialDataSelectDetailResultData> detailList = new ArrayList<>();

        // 系統
        BuildingDmInitialDataSelectDetailResultData line = getLineAll(parameter.getOperationCorpId(),
                parameter.getBuildingId());
        if (line != null)
            detailList.add(line);

        // アナログ
        List<BuildingDmInitialDataSelectDetailResultData> analog = getAnalogList(parameter.getOperationCorpId(),
                parameter.getBuildingId());
        if (analog != null)
            detailList.addAll(analog);

        // 外気温
        BuildingDmInitialDataSelectDetailResultData out = getOutTemperature(parameter.getOperationCorpId(),
                parameter.getBuildingId());
        if (out != null)
            detailList.add(out);

        result.setCorpId(parameter.getOperationCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setDetailList(detailList);
        return result;
    }

    /**
     * 系統
     * @param corpId
     * @param buildingId
     * @return
     */
    private BuildingDmInitialDataSelectDetailResultData getLineAll(String corpId, Long buildingId) throws Exception {

        BuildingDmInitialDataSelectDetailResultData _ret = null;

        // 系統グループ
        LineGroupSearchDetailResultData pLineGrp = new LineGroupSearchDetailResultData();
        pLineGrp.setCorpId(corpId);
        pLineGrp.setLineGroupType(ApiCodeValueConstants.LINE_GROUP_TYPE.CORP_STANDARD.getVal()); // 企業
        List<LineGroupSearchDetailResultData> retLineGrp = getResultList(lineGroupSearchServiceDaoImpl, pLineGrp);
        if (retLineGrp != null && retLineGrp.size() > 0) {
            // 系統
            LineGroupSearchDetailResultData rsLineGrp = retLineGrp.get(0);
            LineListDetailResultData pLine = new LineListDetailResultData();
            pLine.setCorpId(corpId);
            pLine.setLineGroupId(rsLineGrp.getLineGroupId());
            pLine.setLineNo(ApiGenericTypeConstants.LINE_TARGET.ALL.getVal()); // 全体
            List<LineListDetailResultData> retLine = getResultList(lineListServiceDaoImpl, pLine);
            if (retLine != null && retLine.size() > 0) {
                LineListDetailResultData rsLine = retLine.get(0);
                // 全体行
                _ret = new BuildingDmInitialDataSelectDetailResultData();
                _ret.setLineGroupId(rsLine.getLineGroupId());
                _ret.setLineNo(rsLine.getLineNo());
                _ret.setKind(BuildingDmInitialDataUtility.KIND_LINE);
                _ret.setName(rsLine.getLineName());
                BuildingDmInitialDataSelectMonthDetailResultData _setting = new BuildingDmInitialDataSelectMonthDetailResultData();
                _setting.setColumn(BuildingDmInitialDataUtility.COL_USED);
                _setting.setUnit(ApiSimpleConstants.UNIT_USE_POWER);
                // デマンド年報系統
                TDmYearRepLinePK _id = new TDmYearRepLinePK();
                _id.setCorpId(corpId);
                _id.setBuildingId(buildingId);
                _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                _id.setLineGroupId(rsLine.getLineGroupId());
                _id.setLineNo(rsLine.getLineNo());
                TDmYearRepLine pDmYearRepLine = new TDmYearRepLine();
                pDmYearRepLine.setId(_id);
                List<TDmYearRepLine> retDmYearRepLine = getResultList(dmYearRepLineServiceDaoImpl, pDmYearRepLine);
                if (retDmYearRepLine != null) {
                    for (TDmYearRepLine dmYearRepLine : retDmYearRepLine) {
                        switch (dmYearRepLine.getId().getMonthNo().intValue()) {
                        case 1:
                            _setting.setJan(dmYearRepLine.getLineValueKwh());
                            break;
                        case 2:
                            _setting.setFeb(dmYearRepLine.getLineValueKwh());
                            break;
                        case 3:
                            _setting.setMar(dmYearRepLine.getLineValueKwh());
                            break;
                        case 4:
                            _setting.setApr(dmYearRepLine.getLineValueKwh());
                            break;
                        case 5:
                            _setting.setMay(dmYearRepLine.getLineValueKwh());
                            break;
                        case 6:
                            _setting.setJun(dmYearRepLine.getLineValueKwh());
                            break;
                        case 7:
                            _setting.setJul(dmYearRepLine.getLineValueKwh());
                            break;
                        case 8:
                            _setting.setAug(dmYearRepLine.getLineValueKwh());
                            break;
                        case 9:
                            _setting.setSep(dmYearRepLine.getLineValueKwh());
                            break;
                        case 10:
                            _setting.setOct(dmYearRepLine.getLineValueKwh());
                            break;
                        case 11:
                            _setting.setNov(dmYearRepLine.getLineValueKwh());
                            break;
                        case 12:
                            _setting.setDec(dmYearRepLine.getLineValueKwh());
                            break;
                        }
                    }
                }
                _ret.addSetting(_setting);
            }
        }
        return _ret;
    }

    /**
     * アナログ
     * @param corpId
     * @param buildingId
     * @return
     */
    private List<BuildingDmInitialDataSelectDetailResultData> getAnalogList(String corpId, Long buildingId)
            throws Exception {

        List<BuildingDmInitialDataSelectDetailResultData> _ret = null;

        // 建物機器ポイント
        MBuildingSmPointPK id = new MBuildingSmPointPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        MBuildingSmPoint pBuildingSmPoint = new MBuildingSmPoint();
        pBuildingSmPoint.setId(id);
        MSmPoint smPoint = new MSmPoint();
        smPoint.setPointType(ApiGenericTypeConstants.POINT_TYPE.ANALOG.getVal()); // アナログ
        pBuildingSmPoint.setMSmPoint(smPoint);
        pBuildingSmPoint.setPointSumFlg(ApiCodeValueConstants.POINT_SUM_FLG.FLG_ON.getVal());
        List<MBuildingSmPoint> retBuildingSmPoint = getResultList(buildingSmPointServiceDaoImpl, pBuildingSmPoint);
        if (retBuildingSmPoint != null) {
            _ret = new ArrayList<>();
            for (MBuildingSmPoint buildingSmPoint : retBuildingSmPoint) {
                BuildingDmInitialDataSelectDetailResultData _rs = new BuildingDmInitialDataSelectDetailResultData();
                _rs.setKind(BuildingDmInitialDataUtility.KIND_ANALOG);
                _rs.setSmId(buildingSmPoint.getId().getSmId());
                _rs.setPointNo(buildingSmPoint.getId().getPointNo());
                _rs.setName(buildingSmPoint.getPointName());
                // 平均値
                BuildingDmInitialDataSelectMonthDetailResultData _ave = new BuildingDmInitialDataSelectMonthDetailResultData();
                _ave.setColumn(BuildingDmInitialDataUtility.COL_AVERAGE);
                _ave.setUnit(buildingSmPoint.getPointUnit());
                // 最大値
                BuildingDmInitialDataSelectMonthDetailResultData _max = new BuildingDmInitialDataSelectMonthDetailResultData();
                _max.setColumn(BuildingDmInitialDataUtility.COL_MAX);
                _max.setUnit(buildingSmPoint.getPointUnit());
                // デマンド年報ポイント
                TDmYearRepPointPK _id = new TDmYearRepPointPK();
                _id.setCorpId(corpId);
                _id.setBuildingId(buildingId);
                _id.setSmId(buildingSmPoint.getId().getSmId());
                _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
                _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
                _id.setPointNo(buildingSmPoint.getId().getPointNo());
                TDmYearRepPoint pDmYearRepPoint = new TDmYearRepPoint();
                pDmYearRepPoint.setId(_id);
                List<TDmYearRepPoint> retDmYearRepPoint = getResultList(dmYearRepPointServiceDaoImpl, pDmYearRepPoint);
                if (retDmYearRepPoint != null) {
                    for (TDmYearRepPoint dmYearRepPoint : retDmYearRepPoint) {
                        switch (dmYearRepPoint.getId().getMonthNo().intValue()) {
                        case 1:
                            _ave.setJan(dmYearRepPoint.getPointAvg());
                            _max.setJan(dmYearRepPoint.getPointMax());
                            break;
                        case 2:
                            _ave.setFeb(dmYearRepPoint.getPointAvg());
                            _max.setFeb(dmYearRepPoint.getPointMax());
                            break;
                        case 3:
                            _ave.setMar(dmYearRepPoint.getPointAvg());
                            _max.setMar(dmYearRepPoint.getPointMax());
                            break;
                        case 4:
                            _ave.setApr(dmYearRepPoint.getPointAvg());
                            _max.setApr(dmYearRepPoint.getPointMax());
                            break;
                        case 5:
                            _ave.setMay(dmYearRepPoint.getPointAvg());
                            _max.setMay(dmYearRepPoint.getPointMax());
                            break;
                        case 6:
                            _ave.setJun(dmYearRepPoint.getPointAvg());
                            _max.setJun(dmYearRepPoint.getPointMax());
                            break;
                        case 7:
                            _ave.setJul(dmYearRepPoint.getPointAvg());
                            _max.setJul(dmYearRepPoint.getPointMax());
                            break;
                        case 8:
                            _ave.setAug(dmYearRepPoint.getPointAvg());
                            _max.setAug(dmYearRepPoint.getPointMax());
                            break;
                        case 9:
                            _ave.setSep(dmYearRepPoint.getPointAvg());
                            _max.setSep(dmYearRepPoint.getPointMax());
                            break;
                        case 10:
                            _ave.setOct(dmYearRepPoint.getPointAvg());
                            _max.setOct(dmYearRepPoint.getPointMax());
                            break;
                        case 11:
                            _ave.setNov(dmYearRepPoint.getPointAvg());
                            _max.setNov(dmYearRepPoint.getPointMax());
                            break;
                        case 12:
                            _ave.setDec(dmYearRepPoint.getPointAvg());
                            _max.setDec(dmYearRepPoint.getPointMax());
                            break;
                        }
                    }
                }
                _rs.addSetting(_ave);
                _rs.addSetting(_max);
                _ret.add(_rs);
            }
        }
        return _ret;
    }

    /**
     * 外気温
     * @param corpId
     * @param buildingId
     * @return
     */
    private BuildingDmInitialDataSelectDetailResultData getOutTemperature(String corpId, Long buildingId)
            throws Exception {

        BuildingDmInitialDataSelectDetailResultData _ret = null;

        // 外気温表示
        if (!isOutAirTempDisp(corpId, buildingId))
            return null;

        // デマンド年報
        TDmYearRepPK _id = new TDmYearRepPK();
        _id.setCorpId(corpId);
        _id.setBuildingId(buildingId);
        _id.setYearNo(BuildingDmInitialDataUtility.YEAR_NO_ZERO);
        _id.setSummaryUnit(BuildingDmInitialDataUtility.SUMMARY_UNIT_CORP);
        TDmYearRep param = new TDmYearRep();
        param.setId(_id);
        List<TDmYearRep> retDmYearRep = getResultList(dmYearRepServiceDaoImpl, param);
        if (retDmYearRep != null) {
            _ret = new BuildingDmInitialDataSelectDetailResultData();
            _ret.setKind(BuildingDmInitialDataUtility.KIND_ANALOG);
            _ret.setName(ApiSimpleConstants.OUT_TEMPERATURE);
            _ret.setOutTemp(true);
            // 平均値
            BuildingDmInitialDataSelectMonthDetailResultData _ave = new BuildingDmInitialDataSelectMonthDetailResultData();
            _ave.setColumn(BuildingDmInitialDataUtility.COL_AVERAGE);
            _ave.setUnit(BuildingDmInitialDataUtility.UNIT_TEMP);
            // 最大値
            BuildingDmInitialDataSelectMonthDetailResultData _max = new BuildingDmInitialDataSelectMonthDetailResultData();
            _max.setColumn(BuildingDmInitialDataUtility.COL_MAX);
            _max.setUnit(BuildingDmInitialDataUtility.UNIT_TEMP);

            for (TDmYearRep dmYearRep : retDmYearRep) {
                switch (dmYearRep.getId().getMonthNo().intValue()) {
                case 1:
                    _ave.setJan(dmYearRep.getOutAirTempAvg());
                    _max.setJan(dmYearRep.getOutAirTempMax());
                    break;
                case 2:
                    _ave.setFeb(dmYearRep.getOutAirTempAvg());
                    _max.setFeb(dmYearRep.getOutAirTempMax());
                    break;
                case 3:
                    _ave.setMar(dmYearRep.getOutAirTempAvg());
                    _max.setMar(dmYearRep.getOutAirTempMax());
                    break;
                case 4:
                    _ave.setApr(dmYearRep.getOutAirTempAvg());
                    _max.setApr(dmYearRep.getOutAirTempMax());
                    break;
                case 5:
                    _ave.setMay(dmYearRep.getOutAirTempAvg());
                    _max.setMay(dmYearRep.getOutAirTempMax());
                    break;
                case 6:
                    _ave.setJun(dmYearRep.getOutAirTempAvg());
                    _max.setJun(dmYearRep.getOutAirTempMax());
                    break;
                case 7:
                    _ave.setJul(dmYearRep.getOutAirTempAvg());
                    _max.setJul(dmYearRep.getOutAirTempMax());
                    break;
                case 8:
                    _ave.setAug(dmYearRep.getOutAirTempAvg());
                    _max.setAug(dmYearRep.getOutAirTempMax());
                    break;
                case 9:
                    _ave.setSep(dmYearRep.getOutAirTempAvg());
                    _max.setSep(dmYearRep.getOutAirTempMax());
                    break;
                case 10:
                    _ave.setOct(dmYearRep.getOutAirTempAvg());
                    _max.setOct(dmYearRep.getOutAirTempMax());
                    break;
                case 11:
                    _ave.setNov(dmYearRep.getOutAirTempAvg());
                    _max.setNov(dmYearRep.getOutAirTempMax());
                    break;
                case 12:
                    _ave.setDec(dmYearRep.getOutAirTempAvg());
                    _max.setDec(dmYearRep.getOutAirTempMax());
                    break;
                }
            }
            _ret.addSetting(_ave);
            _ret.addSetting(_max);
        }

        return _ret;
    }

    /**
     * 外気温表示
     * @param corpId
     * @param buildingId
     * @return
     */
    private boolean isOutAirTempDisp(String corpId, Long buildingId) throws Exception {
        // 建物デマンド
        MBuildingDmPK id = new MBuildingDmPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        MBuildingDm pBuildingDm = new MBuildingDm();
        pBuildingDm.setId(id);
        MBuildingDm retBuildingDm = find(buildingDmServiceDaoImpl, pBuildingDm);
        if (retBuildingDm == null)
            return false;
        // 外気温表示フラグ
        return retBuildingDm.getOutAirTempDispFlg() == 1;
    }

}
