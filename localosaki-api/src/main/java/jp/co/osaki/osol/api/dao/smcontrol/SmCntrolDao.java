package jp.co.osaki.osol.api.dao.smcontrol;

import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;

public interface SmCntrolDao {
	public SmPrmResultData findSmPrm(Long smId) throws SmControlException;
}
