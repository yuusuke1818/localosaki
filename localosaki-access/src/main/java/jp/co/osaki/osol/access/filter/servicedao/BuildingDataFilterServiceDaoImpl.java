package jp.co.osaki.osol.access.filter.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.access.filter.datafilter.BuildingContractAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingContractPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingMaintenanceGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingOsakiAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingOsakiPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.BuildingPartnerGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.GetFilterDataInterface;
import jp.co.osaki.osol.access.filter.datafilter.PersonTypeGetFilterData;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.BuildingDataFilterResultSet;
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * 建物データフィルターserviceDaoクラス
 *
 * @author take_suzuki
 */
public class BuildingDataFilterServiceDaoImpl implements BaseServiceDao<BuildingDataFilterResultSet> {

    @Deprecated
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public List<BuildingDataFilterResultSet> getResultList(BuildingDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * 建物データフィルター取得
     *
     * @param parameterMap 検索条件 CORP_ID:企業ID PERSON_ID:担当者ID
     * @param em エンティティマネージャ
     * @return 建物データフィルター用 取得結果リスト
     */
    @Override
    public List<BuildingDataFilterResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        List<BuildingDataFilterResultSet> ListBuilding = new ArrayList<>();
        //必須チェック
        if (parameterMap.get(PersonDataParam.LOGIN_CORP_ID) == null || parameterMap.get(PersonDataParam.LOGIN_PERSON_ID) == null) {
            return ListBuilding;
        }
        PersonDataParam param = new PersonDataParam(
                parameterMap.get(PersonDataParam.LOGIN_CORP_ID).get(0).toString(),
                parameterMap.get(PersonDataParam.LOGIN_PERSON_ID).get(0).toString());
        PersonTypeGetFilterData ptgf = new PersonTypeGetFilterData();
        Map<String, PersonTypeResultSet> personMap = ptgf.getFilterData(em, param);
        PersonTypeResultSet typeResultSet = null;
        if (!personMap.isEmpty()) {
            typeResultSet = personMap.entrySet().iterator().next().getValue();
        }
        if (typeResultSet != null) {
            GetFilterDataInterface<BuildingDataFilterResultSet, PersonDataParam> gl = null;
            //企業種別、担当者種別の判定
            switch (typeResultSet.getCorpType().concat(typeResultSet.getPersonType())) {
                //大崎電気/管理者
                case "00":
                    gl = new BuildingOsakiAdminGetFilterData();
                    break;
                //大崎電気/担当者
                case "01":
                    gl = new BuildingOsakiPersonGetFilterData();
                    break;
                //パートナー/管理者/担当者
                case "10":
                case "11":
                    gl = new BuildingPartnerGetFilterData();
                    break;
                //メンテナンス/管理者・担当者
                case "20":
                case "21":
                    gl = new BuildingMaintenanceGetFilterData();
                    break;
                //契約企業/管理者
                case "30":
                    gl = new BuildingContractAdminGetFilterData();
                    break;
                //契約企業/担当者
                case "31":
                    gl = new BuildingContractPersonGetFilterData();
                    break;
                default:
                    break;
            }
            if (gl != null) {
                ListBuilding = new ArrayList<>(gl.getFilterData(em, param).values());
            }
        }
        return ListBuilding;
    }

    @Deprecated
    @Override
    public List<BuildingDataFilterResultSet> getResultList(List<BuildingDataFilterResultSet> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public List<BuildingDataFilterResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public BuildingDataFilterResultSet find(BuildingDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public void persist(BuildingDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public BuildingDataFilterResultSet merge(BuildingDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public void remove(BuildingDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
