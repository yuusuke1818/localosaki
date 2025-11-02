package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 担当者情報(ユーザー一覧用)
 *
 */
public class MPersonSearchServiceDaoImpl implements BaseServiceDao<MPerson> {

    @Override
    public List<MPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        TypedQuery<MPerson> query = em.createNamedQuery("MPerson.searchPersonInfo", MPerson.class);

        // 企業ID
        if (parameterMap.get("corpId").isEmpty()) {
            query.setParameter("corpId", null);
        } else {
            query.setParameter("corpId", parameterMap.get("corpId").get(0).toString());

        }

        // 担当者名(ユーザー名)
        if (parameterMap.get("personIdOrName").isEmpty()) {
            query.setParameter("personId", null);
            query.setParameter("personName", null);
        } else {
            query.setParameter("personId", BaseUtility.addSqlWildcard(parameterMap.get("personIdOrName").get(0).toString()));
            query.setParameter("personName", BaseUtility.addSqlWildcard(parameterMap.get("personIdOrName").get(0).toString()));
        }

        // ふりがな
        if (parameterMap.get("personKana").isEmpty()) {
            query.setParameter("personKana", null);
        } else {
            query.setParameter("personKana", BaseUtility.addSqlWildcard(parameterMap.get("personKana").get(0).toString()));
        }

        // 部署名
        if (parameterMap.get("deptName").isEmpty()) {
            query.setParameter("deptName", null);
        } else {
            query.setParameter("deptName", BaseUtility.addSqlWildcard(parameterMap.get("deptName").get(0).toString()));
        }

        // 役職名
        if (parameterMap.get("positionName").isEmpty()) {
            query.setParameter("positionName", null);
        } else {
            query.setParameter("positionName", BaseUtility.addSqlWildcard(parameterMap.get("positionName").get(0).toString()));
        }
// accountStatus
//1="正常"
//2="未ログイン"
//3="ロック(期限切れ)"
//5="アカウント停止中"
//4="ロック（入力ミス）"
//        if (parameterMap.get("accountStatus").isEmpty()) {
//            // 関連するフラグをnull
////            query.setParameter("statusUse", null);
//            query.setParameter("statusLockInputFailure", null);
//            query.setParameter("statusStopAccount", null);
//            query.setParameter("nowDate", null);
//
//            query.setParameter("statusNormalFlg", null);
//            query.setParameter("statusNotLoginFlg", null);
//            query.setParameter("statusLockExpirationFlg", null);
//            query.setParameter("statusLockInputFailureFlg", null);
//            query.setParameter("statusStopAccountFlg", 1);
//
//        } else {
//            List<Object> accountStatusList = parameterMap.get("accountStatus");
//
////            query.setParameter("statusUse", 1);
//            if (accountStatusList.contains(OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getVal())) {
//                // 正常が選択されている場合
//                query.setParameter("statusNormalFlg", 1);
//            } else {
//                query.setParameter("statusNormalFlg", null);
//            }
//
//            if (accountStatusList.contains(OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getVal())) {
//                // 未ログインが選択されている場合
//                query.setParameter("statusNotLoginFlg", 1);
//            } else {
//                query.setParameter("statusNotLoginFlg", null);
//            }
//
//            if (accountStatusList.contains(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getVal())) {
//                // ロック(期限切れ)が選択されている場合
//                query.setParameter("statusLockExpirationFlg", 1);
//
//            } else {
//                query.setParameter("statusLockExpirationFlg", null);
//            }
//
//            if (accountStatusList.contains(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getVal())) {
//                // ロック（入力ミス）が選択されている場合
//                query.setParameter("statusLockInputFailureFlg", OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT);
//                query.setParameter("statusLockInputFailure", OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT);
//            } else {
//                query.setParameter("statusLockInputFailureFlg", null);
//                query.setParameter("statusLockInputFailure", OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT);
//            }
//
//            if (accountStatusList.contains(OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getVal())) {
//                // アカウント停止中が選択されている場合
//                query.setParameter("statusStopAccountFlg", 1);
//                query.setParameter("statusStopAccount", 1);
//
//            } else {
//                query.setParameter("statusStopAccountFlg", null);
//                query.setParameter("statusStopAccount", null);
//            }
//        query.setParameter("nowDate", DateUtility.conversionDate(parameterMap.get("svDate").get(0).toString(), DateUtility.DATE_FORMAT_YYYYMMDD));
// }
        List<MPerson> resultList = query.getResultList();
        return resultList;

    }

    /**
     * 担当者情報リスト取得
     *
     * @param target
     * @param em
     * @return 担当者情報List
     */
    @Override
    public List<MPerson> getResultList(MPerson target, EntityManager em) {

        // 担当者情報を取得してみる
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MPerson> p = cb.createQuery(MPerson.class);
        Root<MPerson> person = p.from(MPerson.class);

        List<Predicate> MpersonList = new ArrayList<Predicate>();

        // 企業IDは必ず必要ログインしている企業以外はみえてはいけない？
        // TODO 管理者やパートナー企業の場合はここが複数になるのでは？
        MpersonList.add(cb.equal(person.get("id").<String>get("corpId"), target.getId().getCorpId()));
        // 条件
        if (!CheckUtility.isNullOrEmpty(target.getPersonKana())) {
            // 担当者名ふりがな
            MpersonList.add(cb.like(person.<String>get("personKana"), "%" + target.getPersonKana() + "%"));
        }

        if (!CheckUtility.isNullOrEmpty(target.getPersonName())) {
            // 担当者名
            MpersonList.add(cb.like(person.<String>get("personName"), "%" + target.getPersonName() + "%"));
        }

        if (!CheckUtility.isNullOrEmpty(target.getId().getPersonId())) {
            // 担当者ID
            MpersonList.add(cb.like(person.get("id").<String>get("personId"), "%" + target.getId().getPersonId() + "%"));
        }

        if (!CheckUtility.isNullOrEmpty(target.getDeptName())) {
            // 部署名
            MpersonList.add(cb.like(person.<String>get("deptName"), "%" + target.getDeptName() + "%"));
        }

        if (!CheckUtility.isNullOrEmpty(target.getPositionName())) {
            // 役職名
            MpersonList.add(cb.like(person.<String>get("positionName"), "%" + target.getPositionName() + "%"));
        }

        MpersonList.add(cb.lessThanOrEqualTo(person.<String>get("positionName"), "%" + target.getPositionName() + "%"));
        MpersonList.add(cb.equal(person.<Date>get("positionName"), "%" + target.getPositionName() + "%"));

        MpersonList.add(cb.equal(person.get("delFlg"), 0));

        // 検索条件を組み込む
        // 「person.get("corpId")」の取得不良のため、orderByを回避する
        // p = p.select(person).where(cb.and(MpersonList.toArray(new Predicate[]{}))).orderBy(cb.desc(person.get("person_id")));
        p = p.select(person).where(cb.and(MpersonList.toArray(new Predicate[]{})));

        // 検索実行
        List<MPerson> resultPersonList = em.createQuery(p).getResultList();
        // TODO 戻り値
        return resultPersonList;
    }

    /**
     * 担当者情報取得処理
     *
     * @param target 担当者キー情報Bean
     * @param em エンティティマネージャ
     * @return 担当者情報(Mperson)
     */
    @Override
    public MPerson
            find(MPerson target, EntityManager em) {

        // 担当者情報を1件取得
        MPerson reseltObject = em.find(MPerson.class, target.getId());

        return reseltObject;
    }

    @Override
    public void persist(MPerson target, EntityManager em) {

        em.persist(target);

    }

    @Override
    public MPerson merge(MPerson target, EntityManager em) {

        MPerson reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(MPerson target, EntityManager em) {

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(List<MPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
