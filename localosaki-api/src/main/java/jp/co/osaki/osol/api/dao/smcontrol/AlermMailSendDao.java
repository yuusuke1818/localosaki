package jp.co.osaki.osol.api.dao.smcontrol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AlermMailSendParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmPointResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingDmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmPointServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmCntrolServiceDaoImpl;


/**
 *
 * 警報メール送信 Dao クラス.
 *
 * @author yasu_shimizu
 *
 */
@Stateless
public class AlermMailSendDao extends OsolApiDao<AlermMailSendParameter> {

	private final SmCntrolServiceDaoImpl smCntrolServiceDaoImpl;

	private final BuildingSmPointServiceDaoImpl buildingSmPointServiceDaoImpl;

	private final BuildingDmServiceDaoImpl buildingDmServiceDaoImpl;

	public AlermMailSendDao() {
		smCntrolServiceDaoImpl = new SmCntrolServiceDaoImpl();
		buildingSmPointServiceDaoImpl = new BuildingSmPointServiceDaoImpl();
		buildingDmServiceDaoImpl = new BuildingDmServiceDaoImpl();
	}

	/**
	 * 機器情報取得
	 *
	 */
	@Override
	public Object query(AlermMailSendParameter parameter) throws Exception {
		SmPrmResultData target = new SmPrmResultData();
		target.setIpAddress(parameter.getIpAddress());
		target.setSmAddress(parameter.getAlermMsg().substring(6, 10));
		List<SmPrmResultData> resultList = getResultList(smCntrolServiceDaoImpl,target);
		if(resultList.size()==1) {
			return find(smCntrolServiceDaoImpl,resultList.iterator().next());
		}
		return null;
	}

	/**
	 * ポイント名取得
	 *
	 */
	public Map<String, String> getPointNameMap(SmPrmResultData smPrm){
		BuildingSmPointResult target = new BuildingSmPointResult();
		target.setSmId(smPrm.getSmId());
		List<BuildingSmPointResult> list = getResultList(buildingSmPointServiceDaoImpl, target);
		Map<String,String> map = new HashMap<>();
		for(BuildingSmPointResult point:list) {
			// key:ポイントナンバー value:ポイント名
			map.put(point.getPointNo(),point.getPointName());
		}
		return map;
	}

	/**
	 * 警報用 メール情報取得
	 *
	 * @param smPrm
	 * @return
	 */
	public BuildingDmResult getAlertMaiInfo(SmPrmResultData smPrm){
		BuildingDmResult target = new BuildingDmResult();
		target.setSmId(smPrm.getSmId());
		List<BuildingDmResult> list = getResultList(buildingDmServiceDaoImpl ,target);
		Iterator<BuildingDmResult> itr = list.iterator();
		return itr.hasNext()? itr.next() : null;
	}

}
