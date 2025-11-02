package jp.co.osaki.osol.api.dao.corp;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.corp.CorpInfoSelectParameter;
import jp.co.osaki.osol.api.result.corp.CorpInfoSelectResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;

/**
 * 企業情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class CorpInfoSelectDao extends OsolApiDao<CorpInfoSelectParameter> {

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    public CorpInfoSelectDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    @Override
    public CorpInfoSelectResult query(CorpInfoSelectParameter parameter) throws Exception {
        CorpInfoSelectResult result = new CorpInfoSelectResult();

        CommonCorpExclusionResult param = new CommonCorpExclusionResult();
        param.setCorpId(parameter.getOperationCorpId());

        List<CommonCorpExclusionResult> resultList = getResultList(commonCorpExclusionServiceDaoImpl, param);
        if (resultList == null || resultList.size() != 1) {
            return new CorpInfoSelectResult();
        } else {
            result.setCorpId(resultList.get(0).getCorpId());
            result.setVersion(resultList.get(0).getVersion());
        }

        return result;
    }

}
