/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.verify;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoDemandBuildingSearchParameter;
import jp.co.osaki.osol.api.result.energy.verify.KensyoDemandBuildingSearchResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.KensyoDemandBuildingSearchDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.verify.KensyoDemandBuildingSearchServiceDaoImpl;

/**
 * 検証画面用建物検索 Daoクラス
 *
 * @author n-takada
 */
/**
 * @author ya-ishida
 *
 */
@Stateless
public class KensyoDemandBuildingSearchDao extends OsolApiDao<KensyoDemandBuildingSearchParameter> {

    private final KensyoDemandBuildingSearchServiceDaoImpl kensyoDemandBuildingSearchServiceDaoImpl;

    public KensyoDemandBuildingSearchDao() {
        kensyoDemandBuildingSearchServiceDaoImpl = new KensyoDemandBuildingSearchServiceDaoImpl();
    }

    @Override
    public KensyoDemandBuildingSearchResult query(KensyoDemandBuildingSearchParameter parameter) throws Exception {
        KensyoDemandBuildingSearchDetailResultData searchResultSet = new KensyoDemandBuildingSearchDetailResultData();
        searchResultSet.setCorpId(parameter.getLoginCorpId());
        searchResultSet.setBuildingNo(parameter.getBuildingNo());
        List<KensyoDemandBuildingSearchDetailResultData> list = getResultList(kensyoDemandBuildingSearchServiceDaoImpl,
                searchResultSet);

        return new KensyoDemandBuildingSearchResult(list);
    }

}
