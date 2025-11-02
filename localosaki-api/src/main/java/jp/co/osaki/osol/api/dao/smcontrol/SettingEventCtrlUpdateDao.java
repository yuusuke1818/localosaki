package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;
import jp.co.osaki.osol.api.servicedao.entity.MSmAirSettingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.LinkSettingListServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmAirSetting;
import jp.co.osaki.osol.entity.MSmAirSettingPK;
import jp.co.osaki.osol.mng.param.A200021Param;

/**
 *
 * イベント制御設定(設定) Dao クラス.
 *
 * @author t_sakamoto
 *
 */
@Stateless
public class SettingEventCtrlUpdateDao extends BaseSmControlDao {

    private final LinkSettingListServiceDaoImpl linkSettingListServiceDaoImpl;
    private final MSmAirSettingServiceDaoImpl mSmAirSettingServiceDaoImpl;

    public SettingEventCtrlUpdateDao() {
        linkSettingListServiceDaoImpl = new LinkSettingListServiceDaoImpl();
        mSmAirSettingServiceDaoImpl = new MSmAirSettingServiceDaoImpl();
    }

    /**
     * 機器空調設定情報を更新する
     * @param param
     * @param loginUserId
     * @return
     */
    public List<LinkSettingExtractResultData> updateLinkSettingData(A200021Param param, Long loginUserId, Long smId) {

        List<MSmAirSetting> updateList = new ArrayList<>();
        Timestamp serverDateTime = getServerDateTime();

        //機器空調設定情報の排他チェックを行う
        for (LinkSettingExtractResultData linkSetting : param.getLinkSettingList()) {
            MSmAirSetting updateParam = new MSmAirSetting();
            MSmAirSettingPK pkUpdateParam = new MSmAirSettingPK();
            pkUpdateParam.setSmId(smId);
            pkUpdateParam.setOutputPortNo(linkSetting.getControlLoad());
            updateParam.setId(pkUpdateParam);
            MSmAirSetting updateResult = find(mSmAirSettingServiceDaoImpl, updateParam);
            if (linkSetting.getVersion() == null) {
                //新規のため、データがある場合、排他エラーとする
                if (updateResult != null) {
                    throw new OptimisticLockException();
                } else {
                    //新規登録情報をセットする
                    MSmAirSetting insertParam = new MSmAirSetting();
                    MSmAirSettingPK pkInsertParam = new MSmAirSettingPK();
                    pkInsertParam.setSmId(smId);
                    pkInsertParam.setOutputPortNo(linkSetting.getControlLoad());
                    insertParam.setId(pkInsertParam);
                    insertParam.setLinkOutputPortNo(linkSetting.getLinkOutputPortNo());
                    insertParam.setCoolingDifferential(linkSetting.getCoolingDifferential());
                    insertParam.setHeatingDifferential(linkSetting.getHeatingDifferential());
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
                } else if (!updateResult.getVersion().equals(linkSetting.getVersion())) {
                    throw new OptimisticLockException();
                } else {
                    //更新情報をセットする
                    updateResult.setLinkOutputPortNo(linkSetting.getLinkOutputPortNo());
                    updateResult.setCoolingDifferential(linkSetting.getCoolingDifferential());
                    updateResult.setHeatingDifferential(linkSetting.getHeatingDifferential());
                    updateResult.setUpdateDate(serverDateTime);
                    updateResult.setUpdateUserId(loginUserId);
                    updateList.add(updateResult);
                }
            }
        }

        //機器空調設定情報の登録/更新を行う
        for (MSmAirSetting updateInfo : updateList) {
            if (updateInfo.getVersion() == null) {
                persist(mSmAirSettingServiceDaoImpl, updateInfo);
            } else {
                merge(mSmAirSettingServiceDaoImpl, updateInfo);
            }
        }

        //最新の機器空調設定情報の取得を行う
        LinkSettingExtractResultData selectParam = new LinkSettingExtractResultData();
        selectParam.setSmId(smId);

        List<LinkSettingExtractResultData> resultList = getResultList(linkSettingListServiceDaoImpl, selectParam);
        if (resultList == null) {
            return new ArrayList<>();
        } else {
            return resultList;
        }
    }

}
