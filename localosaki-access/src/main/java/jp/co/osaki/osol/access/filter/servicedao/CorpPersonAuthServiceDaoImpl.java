package jp.co.osaki.osol.access.filter.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.access.filter.datafilter.CorpAuthContractCorpGetResultList;
import jp.co.osaki.osol.access.filter.datafilter.CorpAuthMaintenanceCorpGetResultList;
import jp.co.osaki.osol.access.filter.datafilter.CorpAuthOsakiAdminGetResultList;
import jp.co.osaki.osol.access.filter.datafilter.CorpAuthOsakiPersonGetResultList;
import jp.co.osaki.osol.access.filter.datafilter.CorpAuthPartnerGetResultList;
import jp.co.osaki.osol.access.filter.datafilter.GetFilterDataInterface;
import jp.co.osaki.osol.access.filter.datafilter.PersonTypeGetFilterData;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * 操作企業の担当者権限serviceDaoクラス
 *
 * @author take_suzuki
 */
public class CorpPersonAuthServiceDaoImpl implements BaseServiceDao<CorpPersonAuthResultSet> {

    @Deprecated
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public List<CorpPersonAuthResultSet> getResultList(CorpPersonAuthResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * 該当する担当者の企業種別/担当者種別の判定データ取得
     *
     * @param parameterMap 検索条件 CORP_ID:企業ID PERSON_ID:担当者ID
     * OPERATION_CORP_ID:操作企業ID
     * @param em エンティティマネージャ
     * @return 担当企業実行権限データ用 取得結果リスト
     */
    @Override
    public List<CorpPersonAuthResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        List<CorpPersonAuthResultSet> ListCorpAuth = new ArrayList<>();
        //必須チェック
        if (parameterMap.get(CorpPersonAuthParam.LOGIN_CORP_ID) == null || parameterMap.get(CorpPersonAuthParam.LOGIN_PERSON_ID) == null || parameterMap.get(CorpPersonAuthParam.OPERATION_CORP_ID) == null) {
            return ListCorpAuth;
        }
        PersonDataParam param = new PersonDataParam(
                parameterMap.get(PersonDataParam.LOGIN_CORP_ID).get(0).toString(),
                parameterMap.get(PersonDataParam.LOGIN_PERSON_ID).get(0).toString());

        CorpPersonAuthParam param2 = new CorpPersonAuthParam(
                parameterMap.get(CorpPersonAuthParam.LOGIN_CORP_ID).get(0).toString(),
                parameterMap.get(CorpPersonAuthParam.LOGIN_PERSON_ID).get(0).toString(),
                parameterMap.get(CorpPersonAuthParam.OPERATION_CORP_ID).get(0).toString());

        PersonTypeGetFilterData ptgf = new PersonTypeGetFilterData();
        Map<String, PersonTypeResultSet> personMap = ptgf.getFilterData(em, param);
        PersonTypeResultSet typeResultSet = null;
        if (!personMap.isEmpty()) {
            typeResultSet = personMap.entrySet().iterator().next().getValue();
        }
        GetFilterDataInterface<CorpPersonAuthResultSet, CorpPersonAuthParam> qb = null;
        if (typeResultSet != null) {
            //企業種別、担当者種別の判定
            switch (typeResultSet.getCorpType().concat(typeResultSet.getPersonType())) {
                //大崎電気/管理者
                case "00":
                    qb = new CorpAuthOsakiAdminGetResultList();
                    break;
                //大崎電気/担当者
                case "01":
                    qb = new CorpAuthOsakiPersonGetResultList();
                    break;
                //パートナー/管理者・担当者
                case "10":
                case "11":
                    qb = new CorpAuthPartnerGetResultList();
                    break;
                //メンテナンス/管理者・担当者
                case "20":
                case "21":
                    qb = new CorpAuthMaintenanceCorpGetResultList();
                    break;
                //契約企業/管理者・担当者
                case "30":
                case "31":
                    qb = new CorpAuthContractCorpGetResultList();
                    break;
                default:
                    break;
            }
        }
        if (qb != null) {
            ListCorpAuth = new ArrayList<>(qb.getFilterData(em, param2).values());
        }
        return ListCorpAuth;
    }

    @Deprecated
    @Override
    public List<CorpPersonAuthResultSet> getResultList(List<CorpPersonAuthResultSet> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public List<CorpPersonAuthResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public CorpPersonAuthResultSet find(CorpPersonAuthResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public void persist(CorpPersonAuthResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public CorpPersonAuthResultSet merge(CorpPersonAuthResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    @Override
    public void remove(CorpPersonAuthResultSet t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
