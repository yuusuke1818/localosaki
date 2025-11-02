package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;
import jp.co.osaki.osol.api.servicedao.entity.TAielMasterAreaSettingNameServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.AreaNameListServiceDaoImpl;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingName;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingNamePK;
import jp.co.osaki.osol.mng.param.A210006Param;

/**
 *
 * AielMasterエリア設定(設定) Dao クラス.
 *
 * @author s_sunada
 *
 */
@Stateless
public class AielMasterAreaConfigUpdateDao extends BaseSmControlDao {

    private final TAielMasterAreaSettingNameServiceDaoImpl tAielMasterAreaSettingNameServiceDaoImpl;
    private final AreaNameListServiceDaoImpl areaNameListServiceDaoImpl;

    public AielMasterAreaConfigUpdateDao() {
        tAielMasterAreaSettingNameServiceDaoImpl = new TAielMasterAreaSettingNameServiceDaoImpl();
        areaNameListServiceDaoImpl = new AreaNameListServiceDaoImpl();
    }

    /**
     * エリア情報を更新する
     * @param param
     * @param loginUserId
     * @return
     */
    public List<AreaNameExtractResultData> updateAreaNameData(A210006Param param, Long loginUserId) {

        List<TAielMasterAreaSettingName> updateList = new ArrayList<>();
        Timestamp serverDateTime = getServerDateTime();

        //エリア情報の排他チェックを行う
        for (AreaNameExtractResultData AreaName : param.getAreaNameList()) {
            TAielMasterAreaSettingName updateParam = new TAielMasterAreaSettingName();
            TAielMasterAreaSettingNamePK pkUpdateParam = new TAielMasterAreaSettingNamePK();
            pkUpdateParam.setSmId(AreaName.getSmId());
            pkUpdateParam.setAreaNo(AreaName.getAreaNo());
            updateParam.setId(pkUpdateParam);
            TAielMasterAreaSettingName updateResult = find(tAielMasterAreaSettingNameServiceDaoImpl, updateParam);
            if (AreaName.getVersion() == null) {
                //新規のため、データがある場合、排他エラーとする
                if (updateResult != null) {
                    throw new OptimisticLockException();
                } else {
                    //新規登録情報をセットする
                    TAielMasterAreaSettingName insertParam = new TAielMasterAreaSettingName();
                    TAielMasterAreaSettingNamePK pkInsertParam = new TAielMasterAreaSettingNamePK();
                    pkInsertParam.setSmId(AreaName.getSmId());
                    pkInsertParam.setAreaNo(AreaName.getAreaNo());
                    insertParam.setId(pkInsertParam);
                    insertParam.setAreaName(AreaName.getAreaName());
                    insertParam.setSensorName1(AreaName.getSensorName1());
                    insertParam.setSensorName2(AreaName.getSensorName2());
                    insertParam.setSensorName3(AreaName.getSensorName3());
                    insertParam.setSensorName4(AreaName.getSensorName4());
                    insertParam.setCreateDate(serverDateTime);
                    insertParam.setCreateUserId(loginUserId);
                    insertParam.setUpdateDate(serverDateTime);
                    insertParam.setUpdateUserId(loginUserId);
                    updateList.add(insertParam);
                }
            } else {
                //更新のため、データがない場合またはVersionが異なる場合、排他エラーとする
                if (updateResult == null) {
                    throw new OptimisticLockException();
                } else if (!updateResult.getVersion().equals(AreaName.getVersion())) {
                    throw new OptimisticLockException();
                } else {
                    //更新情報をセットする
                    updateResult.setAreaName(AreaName.getAreaName());
                    updateResult.setSensorName1(AreaName.getSensorName1());
                    updateResult.setSensorName2(AreaName.getSensorName2());
                    updateResult.setSensorName3(AreaName.getSensorName3());
                    updateResult.setSensorName4(AreaName.getSensorName4());
                    updateResult.setUpdateDate(serverDateTime);
                    updateResult.setUpdateUserId(loginUserId);
                    updateList.add(updateResult);
                }
            }
        }



        //エリア情報の登録/更新を行う
        for (TAielMasterAreaSettingName updateInfo : updateList) {
            if (updateInfo.getVersion() == null) {
                persist(tAielMasterAreaSettingNameServiceDaoImpl, updateInfo);
            } else {
                merge(tAielMasterAreaSettingNameServiceDaoImpl, updateInfo);
            }
        }

        //最新のエリア情報の取得を行う
        AreaNameExtractResultData selectParam = new AreaNameExtractResultData();
        selectParam.setSmId(param.getAreaNameList().get(0).getSmId());

        List<AreaNameExtractResultData> resultList = getResultList(areaNameListServiceDaoImpl, selectParam);
        if (resultList == null) {
            return new ArrayList<>();
        } else {
            return resultList;
        }
    }

}
