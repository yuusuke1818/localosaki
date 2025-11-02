package jp.co.osaki.osol.api.dao.smcontrol;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.SmCntrolServiceDaoImpl;
import jp.co.osaki.osol.mng.SmControlException;

/**
 *
 * 機器制御 API Dao 親 クラス.
 *
 * @author yasu_shimizu
 *
 */
public class BaseSmControlDao extends OsolApiDao<BaseSmControlApiParameter> implements SmCntrolDao {

	private final SmCntrolServiceDaoImpl smCntrolServiceDaoImpl;

	public BaseSmControlDao() {
		smCntrolServiceDaoImpl = new SmCntrolServiceDaoImpl();
	}

	/**
	 * 機器情報取得
	 *
	 */
	@Override
	public SmPrmResultData findSmPrm(Long smId) throws SmControlException {
		SmPrmResultData smPrm = find(smCntrolServiceDaoImpl,new SmPrmResultData(smId));
		if(smPrm==null) {
			daoLogger.error(this.getClass().getPackage().getName().concat(" smId="+ smId));
			throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
		}
		return  smPrm;
	}

	/**
	 * OsolApiDao 実装(未使用)
	 *
	 */
	@Override
	public Object query(BaseSmControlApiParameter parameter) throws Exception {
		return null;
	}

}
