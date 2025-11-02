package jp.co.osaki.osol.api.dao.smcontrol;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.servicedao.entity.MSmControlLoadServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoadPK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200049Param;
import jp.co.osaki.osol.utility.StringUtility;

/**
 *
 * デマンド(取得) Eα Dao クラス.
 *
 * @author t_hayama
 *
 */
@Stateless
public class DemandSelectEaDao extends BaseSmControlDao {

    // ServiceDAO呼出
    private final BuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
    private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
    private final BuildingServiceDaoImpl buildingServiceDaoImpl;
    private final MSmControlLoadServiceDaoImpl mSmControlLoadServiceDaoImpl;

    public DemandSelectEaDao() {
        buildingDmServiceDaoImpl = new BuildingDmServiceDaoImpl();
        buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
        buildingServiceDaoImpl = new BuildingServiceDaoImpl();
        mSmControlLoadServiceDaoImpl = new MSmControlLoadServiceDaoImpl();
    }

    // 1トランザクションで複数テーブルに更新をかけるため、
    // DAO内に処理を記載
    public void updateBuildingDM(BuildingDmResult param, FvpCtrlMngResponse<A200049Param> res) throws Exception {

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

        //機器制御負荷を登録する
        updateSmControlLoad(res, param.getUpdateUserId(), updateDate);

        // 建物機器Result
        BuildingSmResult paramPk = new BuildingSmResult();
        paramPk.setSmId(param.getSmId());
        // 建物機器検索
        List<BuildingSmResult> paramPkList;
        paramPkList = getResultList(buildingSmServiceDaoImpl, paramPk);

        for (BuildingSmResult paramPkelm : paramPkList) {
            param.setCorpId(paramPkelm.getCorpId());
            param.setBuildingId(paramPkelm.getBuildingId());
            param.setUpdateDate(updateDate);

            // 建物デマンドTBL_更新処理
            if (merge(buildingDmServiceDaoImpl, param) == null) {
                // 更新がない場合、後続のDB更新は実施しない
                continue;
            }

            // 建物デマンドに更新があった場合、設定対象の建物の情報を更新する
            BuildingResult buildingParam = new BuildingResult();

            // 値をセット
            buildingParam.setCorpId(paramPkelm.getCorpId());
            buildingParam.setBuildingId(paramPkelm.getBuildingId());
            buildingParam.setUpdateUserId(param.getUpdateUserId());
            buildingParam.setUpdateDate(updateDate);

            // 建物TBL_更新処理
            merge(buildingServiceDaoImpl, buildingParam);

        }

        return;
    }

    /**
     * 機器制御負荷を登録する
     * @param res
     * @param loginUserId
     * @param updateDate
     */
    private void updateSmControlLoad(FvpCtrlMngResponse<A200049Param> res, Long loginUserId, Timestamp updateDate) {
        //最小負荷遮断時間リスト
        List<String> minLoadBlockTimeList = res.getParam().get_minLoadBlockTimeList();
        //遮断方法リスト
        List<String> loadBlockMethodList = res.getParam().get_loadBlockMethodList();
        //遮断順位リスト
        List<String> loadBlockRankList = res.getParam().get_loadBlockRankList();
        //負荷遮断容量リスト
        List<String> loadBlockCapacityList = res.getParam().get_loadBlockCapacityList();
        //機器ID
        Long smId = res.getSmId();
        //インデックス
        int controlLoadIndex = 0;
        for (String minLoadBlockTime : minLoadBlockTimeList) {
            //最小負荷遮断順位（遮断方法+遮断順位）
            String shutOffRank = null;
            //遮断方法
            String loadBlockMethod = loadBlockMethodList.get(controlLoadIndex);
            //遮断順位
            String loadBlockRank = loadBlockRankList.get(controlLoadIndex);
            //負荷遮断容量
            String shutOffCapacity = loadBlockCapacityList.get(controlLoadIndex);

            //各項目、数値変換できるかを確認する。数値以外の場合は、Nullを設定
            if (!StringUtility.isNumeric(minLoadBlockTime)) {
                minLoadBlockTime = null;
            }
            if (!StringUtility.isNumeric(loadBlockMethod)) {
                loadBlockMethod = null;
            }
            if (!StringUtility.isNumeric(loadBlockRank)) {
                loadBlockRank = null;
            }
            if (!StringUtility.isNumeric(shutOffCapacity)) {
                shutOffCapacity = null;
            }

            //最小負荷遮断順位を設定
            //TODO 遮断順位の桁数がDBで定義されているものより1桁多いため、1桁削ってDBに登録 (システムで使用されていないため、問題なし)
            if (loadBlockMethod != null && loadBlockRank != null) {
                shutOffRank = loadBlockMethod.concat(loadBlockRank.substring(1));
            }

            //インデックスをカウントアップ
            controlLoadIndex++;
            MSmControlLoad param = new MSmControlLoad();
            MSmControlLoadPK pkParam = new MSmControlLoadPK();
            pkParam.setSmId(smId);
            pkParam.setControlLoad(BigDecimal.valueOf(controlLoadIndex));
            param.setId(pkParam);
            MSmControlLoad updateData = find(mSmControlLoadServiceDaoImpl, param);
            if (updateData == null) {
                //対象データがない場合
                updateData = new MSmControlLoad();
                updateData.setId(pkParam);
                updateData.setControlLoadShutOffTime(minLoadBlockTime);
                updateData.setControlLoadShutOffRank(shutOffRank);
                updateData.setControlLoadShutOffCapacity(shutOffCapacity);
                updateData.setDelFlg(OsolConstants.FLG_OFF);
                updateData.setCreateUserId(loginUserId);
                updateData.setCreateDate(updateDate);
                updateData.setUpdateUserId(loginUserId);
                updateData.setUpdateDate(updateDate);
                persist(mSmControlLoadServiceDaoImpl, updateData);
            } else {
                //対象データがある場合
                updateData.setControlLoadShutOffTime(minLoadBlockTime);
                updateData.setControlLoadShutOffRank(shutOffRank);
                updateData.setControlLoadShutOffCapacity(shutOffCapacity);
                updateData.setDelFlg(OsolConstants.FLG_OFF);
                updateData.setUpdateUserId(loginUserId);
                updateData.setUpdateDate(updateDate);
                merge(mSmControlLoadServiceDaoImpl, updateData);
            }
        }
    }
}
