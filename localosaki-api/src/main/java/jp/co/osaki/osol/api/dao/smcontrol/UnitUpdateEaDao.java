package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmPointResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.result.smcontrol.DmYearRepPointResult;
import jp.co.osaki.osol.api.result.smcontrol.GraphElementResult;
import jp.co.osaki.osol.api.result.smcontrol.SmLinePointResult;
import jp.co.osaki.osol.api.result.smcontrol.SmPointResult;
import jp.co.osaki.osol.api.result.smcontrol.SmPrmResult;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.DmYearRepPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.GraphElementServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmLinePointLineGroupIdServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmLinePointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmPrmServiceDaoImpl;
import jp.co.osaki.osol.mng.constants.SmControlConstants;

/**
 *
 *  ユニット(設定) Eα Dao クラス
 *
 * @author t_hayama
 *
 */
@Stateless
public class UnitUpdateEaDao extends BaseSmControlDao {
    // ServiceDAO
    private final SmPointServiceDaoImpl smPointServiceDaoImpl;
    private final BuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;
    private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
    private final SmLinePointServiceDaoImpl smLinePointServiceDaoImpl;
    private final GraphElementServiceDaoImpl graphElementServiceDaoImpl;
    private final DmYearRepPointServiceDaoImpl dmYearRepPointServiceDaoImpl;
    private final SmPrmServiceDaoImpl smPrmServiceDaoImpl;
    private final BuildingServiceDaoImpl buildingServiceDaoImpl;
    private final SmLinePointLineGroupIdServiceDaoImpl smLinePointLineGroupIdServiceDaoImpl;


    public UnitUpdateEaDao() {
        smPointServiceDaoImpl = new SmPointServiceDaoImpl();
        buildingSmPointServiceDaoImpl = new BuildingSmPointServiceDaoImpl();
        buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
        smLinePointServiceDaoImpl= new SmLinePointServiceDaoImpl();
        graphElementServiceDaoImpl = new GraphElementServiceDaoImpl();
        dmYearRepPointServiceDaoImpl = new DmYearRepPointServiceDaoImpl();
        smPrmServiceDaoImpl = new SmPrmServiceDaoImpl();
        buildingServiceDaoImpl = new BuildingServiceDaoImpl();
        smLinePointLineGroupIdServiceDaoImpl = new SmLinePointLineGroupIdServiceDaoImpl();
    }


    // 1トランザクションで複数テーブルに更新をかけるため、
    // DAO内に処理を記載
    public void UpdateSmPoint(List<SmPointResult> paramList) throws Exception {

        // 対象のリストがnul、または空の場合更新せずに終了
        if (paramList == null || paramList.isEmpty()) {
            return;
        }

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();
        // 更新ユーザーID
        Long updateUserId = paramList.get(0).getUpdateUserId();
        // 対象機器ID
        Long smId = paramList.get(0).getSmId();

        // 変更のあった機器ポイントを抽出
        List<SmPointResult> targetList = new ArrayList<>();
        for (SmPointResult param : paramList) {
            param.setUpdateDate(updateDate);
            // 機器ポイントテーブル更新
            if (merge(smPointServiceDaoImpl, param) != null) {
                targetList.add(param);
            }
        }

        // 機器ポイントに変更が１件もない場合、処理終了
        if (targetList == null || targetList.isEmpty()) {
            return;
        }

        // 機器に紐づく建物・テナントを取得
        // 建物機器テーブルResult
        BuildingSmResult buildingSmResultPrm = new BuildingSmResult();

        // 建物機器テーブル主キー検索用
        buildingSmResultPrm.setSmId(smId);

        // 建物機器テーブルの主キーList取得
        List<BuildingSmResult> buidingSmList = getResultList(buildingSmServiceDaoImpl, buildingSmResultPrm);

        // 機器に紐づく建物・テナント数
        for (BuildingSmResult buildingSm : buidingSmList) {
            // 変更のあったポイント数
            for (SmPointResult target : targetList) {

                //建物機器ポイントResult
                BuildingSmPointResult buildingSmPointResultPrm = new BuildingSmPointResult();

                // 更新対象レコード検索用
                buildingSmPointResultPrm.setSmId(buildingSm.getSmId());
                buildingSmPointResultPrm.setCorpId(buildingSm.getCorpId());
                buildingSmPointResultPrm.setBuildingId(buildingSm.getBuildingId());
                buildingSmPointResultPrm.setPointNo(target.getPointNo());

                // 建物機器ポイントの更新値を設定
                buildingSmPointResultPrm.setUpdateUserId(updateUserId);
                buildingSmPointResultPrm.setUpdateDate(updateDate);
                buildingSmPointResultPrm.setPointSumFlg(SmControlConstants.POINT_FLG);

                // 建物機器ポイント更新
                merge(buildingSmPointServiceDaoImpl, buildingSmPointResultPrm);

                // 機器系統ポイントテーブル内 企業ID、建物ID、機器ID、ポイント番号が一致するレコードを更新する。
                SmLinePointResult smLinePointResultprm = new SmLinePointResult();

                // 更新対象レコード検索用
                smLinePointResultprm.setCorpId(buildingSm.getCorpId());
                smLinePointResultprm.setBuildingId(buildingSm.getBuildingId());
                smLinePointResultprm.setSmId(buildingSm.getSmId());
                smLinePointResultprm.setPointNo(target.getPointNo());

                // 機器系統ポイントの更新値を設定
                smLinePointResultprm.setUpdateUserId(updateUserId);
                smLinePointResultprm.setUpdateDate(updateDate);
                smLinePointResultprm.setPointCalcType(SmControlConstants.POINT_TYPE);

                // 更新用データ作成
                List<Object> smLinePointList = new ArrayList<>();
                smLinePointList.add(smLinePointResultprm);
                Map<String, List<Object>> smLinePointListParam = new HashMap<>();
                smLinePointListParam.put(SmControlConstants.UNITUPDATE_SM_LINE_POINT, smLinePointList);

                // 一括更新（戻り値：更新件数）
                executeUpdate(smLinePointServiceDaoImpl, smLinePointListParam);

                // グラフ要素設定テーブル内 企業ID、建物ID、機器ID、ポイント番号が一致するレコードを更新する。
                SmLinePointResult smLinePointLineGroupIdResultprm = new SmLinePointResult();
                smLinePointLineGroupIdResultprm.setCorpId(buildingSm.getCorpId());
                smLinePointLineGroupIdResultprm.setBuildingId(buildingSm.getBuildingId());
                smLinePointLineGroupIdResultprm.setSmId(buildingSm.getSmId());
                smLinePointLineGroupIdResultprm.setPointNo(target.getPointNo());

                // グラフ要素設定の更新に必要な系統グループIDを取得
                List<SmLinePointResult> linePointList = getResultList(smLinePointLineGroupIdServiceDaoImpl, smLinePointLineGroupIdResultprm);

                // 系統グループID数
                for (SmLinePointResult smLinePoint : linePointList) {
                    // グラフ要素設定Result
                    GraphElementResult graphElementTarget = new GraphElementResult();

                    // 更新対象レコード検索用
                    graphElementTarget.setCorpId(buildingSm.getCorpId());
                    graphElementTarget.setBuildingId(buildingSm.getBuildingId());
                    graphElementTarget.setLineGroupId(smLinePoint.getLineGroupId());

                    // グラフ要素設定の更新値を設定
                    graphElementTarget.setDelFlg(SmControlConstants.DEL_FLG_ON);
                    graphElementTarget.setUpdateUserId(updateUserId);
                    graphElementTarget.setUpdateDate(updateDate);

                    // 更新用データ作成
                    List<Object> graphElementList = new ArrayList<>();
                    graphElementList.add(graphElementTarget);
                    Map<String, List<Object>> graphElementListParam = new HashMap<>();
                    graphElementListParam.put(SmControlConstants.UNITUPDATE_GRAPH_ELEMENT, graphElementList);

                    // 一括更新（戻り値：更新件数）
                    executeUpdate(graphElementServiceDaoImpl, graphElementListParam);
                }

                // デマンド年報ポイントResult
                DmYearRepPointResult dmYearRepPointResultPrm = new DmYearRepPointResult();

                // 更新対象レコード検索用
                dmYearRepPointResultPrm.setCorpId(buildingSm.getCorpId());
                dmYearRepPointResultPrm.setBuildingId(buildingSm.getBuildingId());
                dmYearRepPointResultPrm.setSmId(buildingSm.getSmId());
                dmYearRepPointResultPrm.setPointNo(target.getPointNo());

                // 更新対象レコード検索用（追加条件）
                dmYearRepPointResultPrm.setYearNo(SmControlConstants.YEAR_NO);

                // デマンド年報ポイントの更新値を設定
                dmYearRepPointResultPrm.setPointAvg(SmControlConstants.POINT_AVG);
                dmYearRepPointResultPrm.setPointMax(SmControlConstants.POINT_MAX);
                dmYearRepPointResultPrm.setUpdateUserId(updateUserId);
                dmYearRepPointResultPrm.setUpdateDate(updateDate);

                List<Object> dmYearRepPointListTemp = new ArrayList<>();
                dmYearRepPointListTemp.add(dmYearRepPointResultPrm);
                Map<String, List<Object>> dmYearRepPointListParam = new HashMap<>();
                dmYearRepPointListParam.put(SmControlConstants.UNITUPDATE_DM_YEAR_REP_POINT, dmYearRepPointListTemp);

                // 一括更新（戻り値：更新件数）
                executeUpdate(dmYearRepPointServiceDaoImpl, dmYearRepPointListParam);

            }

            // 機器テーブルResult
            SmPrmResult SmPrmResultPrm = new SmPrmResult();

            // 機器テーブル主キー検索用
            SmPrmResultPrm.setSmId(buildingSm.getSmId());

            // 機器テーブルの更新値を設定
            SmPrmResultPrm.setUpdateUserId(updateUserId);
            SmPrmResultPrm.setUpdateDate(updateDate);
            // 機器テーブル更新
            merge(smPrmServiceDaoImpl, SmPrmResultPrm);

            //建物テーブルResult
            BuildingResult buildingResultPrm = new BuildingResult();

            // 建物テーブル主キー検索用
            buildingResultPrm.setCorpId(buildingSm.getCorpId());
            buildingResultPrm.setBuildingId(buildingSm.getBuildingId());

            // 建物テーブルの更新値を設定
            buildingResultPrm.setUpdateUserId(updateUserId);
            buildingResultPrm.setUpdateDate(updateDate);

            // 建物テーブル更新
            merge(buildingServiceDaoImpl,buildingResultPrm);
        }
    }
}
