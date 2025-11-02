package jp.co.osaki.osol.api.dao.smcontrol;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.smcontrol.AielMasterAreaConfigSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.AreaNameListServiceDaoImpl;

/**
 *
 * AielMasterエリア設定(取得) Dao クラス.
 *
 * @author s_sunada
 *
 */
@Stateless
public class AielMasterAreaConfigSelectDao extends BaseSmControlDao {

    private final AreaNameListServiceDaoImpl areaNameListServiceDaoImpl;

    public AielMasterAreaConfigSelectDao() {
        areaNameListServiceDaoImpl = new AreaNameListServiceDaoImpl();
    }

    /**
     * エリア名称とセンサ名称を取得する
     * @param SmId
     * @return
     */
    public AielMasterAreaConfigSelectDetailResultData getData(Long smId) {

        AielMasterAreaConfigSelectDetailResultData result = new AielMasterAreaConfigSelectDetailResultData();

        //エリア名称,センサ名称を取得する
        AreaNameExtractResultData areaNameParam = new AreaNameExtractResultData();
        areaNameParam.setSmId(smId);

        List<AreaNameExtractResultData> areaNameList = getResultList(areaNameListServiceDaoImpl, areaNameParam);
        result.setAreaNameList(areaNameList);


        return result;
    }
}
