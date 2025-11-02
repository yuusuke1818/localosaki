package jp.co.osaki.osol.access.filter.servicedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.access.filter.datafilter.CorpContractAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpContractPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpMaintenanceCorpGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpMyCorpGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpOsakiAdminGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpOsakiPersonGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.CorpPartnerGetFilterData;
import jp.co.osaki.osol.access.filter.datafilter.GetFilterDataInterface;
import jp.co.osaki.osol.access.filter.datafilter.PersonTypeGetFilterData;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.CorpDataFilterResultSet;
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * 操作企業データフィルターserviceDaoクラス
 *
 * @author take_suzuki
 */
public class CorpDataFilterServiceDaoImpl implements BaseServiceDao<CorpDataFilterResultSet> {

    @Override
    @Deprecated
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public List<CorpDataFilterResultSet> getResultList(CorpDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * 企業データフィルター取得
     *
     * @param parameterMap 検索条件 CORP_ID:企業ID PERSON_ID:担当者ID
     * @param em エンティティマネージャ
     * @return 企業データフィルター用 取得結果
     */
    @Override
    public List<CorpDataFilterResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        List<CorpDataFilterResultSet> ListCorp = new ArrayList<>();
        //必須チェック
        if (parameterMap.get(PersonDataParam.LOGIN_CORP_ID) == null || parameterMap.get(PersonDataParam.LOGIN_PERSON_ID) == null) {
            return ListCorp;
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
        GetFilterDataInterface<CorpDataFilterResultSet, PersonDataParam> gl = null;
        if (typeResultSet != null) {
            //企業種別、担当者種別の判定
            switch (typeResultSet.getCorpType().concat(typeResultSet.getPersonType())) {
                //大崎電気/管理者
                case "00":
                    gl = new CorpOsakiAdminGetFilterData();
                    break;
                //大崎電気/担当者
                case "01":
                    gl = new CorpOsakiPersonGetFilterData();
                    break;
                //パートナー/管理者・担当者
                case "10":
                case "11":
                    gl = new CorpPartnerGetFilterData();
                    break;
                //メンテナンス/管理者・担当者
                case "20":
                case "21":
                    gl = new CorpMaintenanceCorpGetFilterData();
                    break;
                //契約企業/管理者・担当者
                case "30":
                    gl = new CorpContractAdminGetFilterData();
                    break;
                case "31":
                    gl = new CorpContractPersonGetFilterData();
                    break;
                default:
                    break;
            }
        }
        Map<String, CorpDataFilterResultSet> map = new HashMap<>();
        if (gl != null) {
            map = gl.getFilterData(em, param);
        }
        //所属企業を追加
        gl = new CorpMyCorpGetFilterData();
        for (CorpDataFilterResultSet m : gl.getFilterData(em, param).values()) {
            map.put(m.getCorpId(), m);
        }
        ListCorp = new ArrayList<>(map.values());
        return ListCorp;
    }

    @Override
    @Deprecated
    public List<CorpDataFilterResultSet> getResultList(List<CorpDataFilterResultSet> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public List<CorpDataFilterResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public CorpDataFilterResultSet find(CorpDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public void persist(CorpDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public CorpDataFilterResultSet merge(CorpDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Deprecated
    public void remove(CorpDataFilterResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
