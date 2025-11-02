package jp.co.osaki.osol.api.dao.energy.verify;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmLineControlLoadVerifyListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmLineControlLoadVerifyListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonBuildingExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.result.servicedao.CommonSmExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmLineControlLoadVerifyListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonBuildingExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.common.CommonSmExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.verify.SmLineControlLoadVerifyListServiceDaoImpl;

/**
 * 機器系統制御負荷検証取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class SmLineControlLoadVerifyListDao extends OsolApiDao<SmLineControlLoadVerifyListParameter> {

    private final SmLineControlLoadVerifyListServiceDaoImpl smLineControlLoadVerifyListServiceDaoImpl;
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    private final CommonBuildingExclusionServiceDaoImpl commonBuildingExclusionServiceDaoImpl;
    private final CommonSmExclusionServiceDaoImpl commonSmExclusionServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public SmLineControlLoadVerifyListDao() {
        smLineControlLoadVerifyListServiceDaoImpl = new SmLineControlLoadVerifyListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        commonBuildingExclusionServiceDaoImpl = new CommonBuildingExclusionServiceDaoImpl();
        commonSmExclusionServiceDaoImpl = new CommonSmExclusionServiceDaoImpl();
    }

    @Override
    public SmLineControlLoadVerifyListResult query(SmLineControlLoadVerifyListParameter parameter) throws Exception {
        SmLineControlLoadVerifyListResult result = new SmLineControlLoadVerifyListResult();

        //排他企業情報を取得する
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new SmLineControlLoadVerifyListResult();
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
            return new SmLineControlLoadVerifyListResult();
        }

        //排他機器情報を取得する
        CommonSmExclusionResult exSmParam = new CommonSmExclusionResult();
        exSmParam.setSmId(parameter.getSmId());
        List<CommonSmExclusionResult> exSmList = getResultList(commonSmExclusionServiceDaoImpl, exSmParam);
        if (exSmList == null || exSmList.size() != 1) {
            return new SmLineControlLoadVerifyListResult();
        }

        SmLineControlLoadVerifyListDetailResultData param = new SmLineControlLoadVerifyListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setBuildingId(parameter.getBuildingId());
        param.setSmId(parameter.getSmId());
        List<SmLineControlLoadVerifyListDetailResultData> resultList = getResultList(
                smLineControlLoadVerifyListServiceDaoImpl, param);
        result.setCorpId(exCorpList.get(0).getCorpId());
        result.setBuildingId(exBuildingList.get(0).getBuildingId());
        result.setSmId(exSmList.get(0).getSmId());
        result.setCorpVersion(exCorpList.get(0).getVersion());
        result.setBuildingVersion(exBuildingList.get(0).getVersion());
        result.setSmVersion(exSmList.get(0).getVersion());
        result.setDetailList(resultList);
        return result;
    }

}
