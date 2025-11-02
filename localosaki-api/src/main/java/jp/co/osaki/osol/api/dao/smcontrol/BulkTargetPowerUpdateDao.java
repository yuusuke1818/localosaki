package jp.co.osaki.osol.api.dao.smcontrol;


import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;


/**
 *
 * 複数建物・テナント一括 目標電力(設定) Dao クラス.
 *
 * @author Takemura
 *
 */
@Stateless
public class BulkTargetPowerUpdateDao extends BaseSmControlDao{

	// ServiceDAO呼出
	private final BuildingDmServiceDaoImpl buildingDmServiceDaoImpl;
	private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
	private final BuildingServiceDaoImpl buildingServiceDaoImpl;

	public BulkTargetPowerUpdateDao() {
		buildingDmServiceDaoImpl = new BuildingDmServiceDaoImpl();
		buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
		buildingServiceDaoImpl = new BuildingServiceDaoImpl();
	}

	// 1トランザクションで複数テーブルに更新をかけるため、
	// DAO内に処理を記載
	public void updateBuildingDM(BuildingDmResult param) throws Exception{

		// DBサーバ時刻取得
		Timestamp updateDate = super.getServerDateTime();

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

		return ;
	}
}
