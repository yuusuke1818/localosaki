package jp.co.osaki.osol.api.dao.energy.setting;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.Co2CoefficientSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.Co2CoefficientSelectResult;
import jp.co.osaki.osol.api.servicedao.entity.MCoefficientHistoryManageServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TAvailableEnergyServiceDaoImpl;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage;
import jp.co.osaki.osol.entity.MCoefficientHistoryManagePK;
import jp.co.osaki.osol.entity.TAvailableEnergy;
import jp.co.osaki.osol.entity.TAvailableEnergyPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * CO2排出係数取得 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class Co2CoefficientSelectDao extends OsolApiDao<Co2CoefficientSelectParameter> {

    //TODO EntityServiceDaoクラスを使わないようにする
    private final TAvailableEnergyServiceDaoImpl availableEnergyServiceDaoImpl;
    private final MCoefficientHistoryManageServiceDaoImpl coefficientHistoryManageServiceDaoImpl;

    public Co2CoefficientSelectDao() {
        availableEnergyServiceDaoImpl = new TAvailableEnergyServiceDaoImpl();
        coefficientHistoryManageServiceDaoImpl = new MCoefficientHistoryManageServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public Co2CoefficientSelectResult query(Co2CoefficientSelectParameter parameter) throws Exception {
        Co2CoefficientSelectResult result = new Co2CoefficientSelectResult();

        if (!CheckUtility.isNullOrEmpty(parameter.getSelectedCorpId())) {
            //選択企業が設定されている場合はそちらを優先
            parameter.setOperationCorpId(parameter.getSelectedCorpId());
        }

        Timestamp timestamp = getServerDateTime();
        //基準日は取得した月の1日
        Date standardDate = DateUtility.changeDateMonthFirst(timestamp);

        // エネルギー種類
        String engTypeCd = ApiCodeValueConstants.ENG_TYPE_CD.ELECTRICAL.getVal();

        // 使用エネルギー
        TAvailableEnergy availableEnergy = getAvailableEnergy(parameter.getOperationCorpId(), parameter.getBuildingId(),
                engTypeCd, parameter.getEngId(), parameter.getContractId(), standardDate);

        // 係数履歴管理
        Long engId = null;
        String dayAndNightType = null;
        if (availableEnergy != null) {
            engId = availableEnergy.getId().getEngId();
            dayAndNightType = availableEnergy.getDayAndNightType();
        }

        String ym = DateUtility.changeDateFormat(standardDate, DateUtility.DATE_FORMAT_YYYYMM);
        MCoefficientHistoryManage chm = getMCoefficientHistoryManage(engTypeCd, engId, dayAndNightType, ym);

        if (chm != null) {
            result.setValue(chm.getStdCo2Coefficient());
        } else {
            result.setValue(null);
        }

        return result;
    }

    /**
     * 使用エネルギー
     * エネルギー利用開始年月～終了年月の範囲内を最優先するが、
     * 昼夜区分が欲しいだけなので、範囲内がなければ範囲外の値を返す。
     *
     * @param corpId
     * @param buildingId
     * @param engTypeCd
     * @param engId
     * @param contractId
     * @param stdDate
     * @return
     */
    private TAvailableEnergy getAvailableEnergy(String corpId, Long buildingId, String engTypeCd, Long engId,
            Long contractId, Date stdDate) throws Exception {

        TAvailableEnergy ret = null;

        TAvailableEnergy param = new TAvailableEnergy();
        TAvailableEnergyPK id = new TAvailableEnergyPK();
        id.setCorpId(corpId);
        id.setBuildingId(buildingId);
        id.setEngTypeCd(engTypeCd);
        id.setEngId(engId);
        id.setContractId(contractId);
        param.setId(id);
        List<TAvailableEnergy> list = getResultList(availableEnergyServiceDaoImpl, param);
        if (list != null) {
            for (TAvailableEnergy eng : list) {
                // 利用開始日
                if (eng.getEnergyStartYm() != null && eng.getEnergyStartYm().compareTo(stdDate) > 0) {
                    if (ret == null)
                        ret = eng;
                    continue;
                }
                // 利用終了日
                if (eng.getEnergyEndYm() != null && eng.getEnergyEndYm().compareTo(stdDate) < 0) {
                    if (ret == null)
                        ret = eng;
                    continue;
                }
                // 範囲内
                return eng;
            }
        }
        return ret;
    }

    /**
     * 係数履歴管理
     *
     * @param engTypeCd
     * @param engId
     * @param dayAndNightType
     * @param sYm
     * @return
     */
    private MCoefficientHistoryManage getMCoefficientHistoryManage(String engTypeCd, Long engId, String dayAndNightType,
            String sYm) throws Exception {

        MCoefficientHistoryManage param = new MCoefficientHistoryManage();
        MCoefficientHistoryManagePK id = new MCoefficientHistoryManagePK();
        id.setEngTypeCd(engTypeCd);
        id.setEngId(engId);
        id.setDayAndNightType(dayAndNightType);
        param.setId(id);

        List<MCoefficientHistoryManage> list = getResultList(coefficientHistoryManageServiceDaoImpl, param);
        if (list != null) {
            for (MCoefficientHistoryManage entity : list) {
                // 開始年月
                if (entity.getStartYm() != null && entity.getStartYm().compareTo(sYm) > 0) {
                    continue;
                }
                // 終了年月
                if (entity.getStartYm() != null && entity.getStartYm().compareTo(sYm) > 0) {
                    continue;
                }
                // 範囲内
                return entity;
            }
        }
        return null;
    }

}
