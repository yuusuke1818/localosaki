package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 担当者情報
 *
 * @author take_suzuki
 */
public class MPersonServiceDaoImpl implements BaseServiceDao<MPerson> {

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
    public MPerson find(MPerson target, EntityManager em) {

        // 担当者情報を1件取得
//        MPerson reseltObject = em.find(MPerson.class, target.getId());
//        MUiScreen mu = reseltObject.getMUiScreen();
//        reseltObject.setMUiScreen(mu);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MPerson> p = cb.createQuery(MPerson.class);
        Root<MPerson> person = p.from(MPerson.class);
        person.join(MPerson_.MUiScreen, JoinType.LEFT);

        String corpId = target.getId().getCorpId();
        String personId = target.getId().getPersonId();
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(person.get(MPerson_.id).get(MPersonPK_.corpId), corpId));
        whereList.add(cb.equal(person.get(MPerson_.id).get(MPersonPK_.personId), personId));

        p = p.select(person).where(cb.and(whereList.toArray(new Predicate[]{})));
        // 検索実行
        List<MPerson> list = em.createQuery(p).getResultList();
        if (list == null || list.isEmpty() || list.size() != 1) {
            return null;
        }
        return list.get(0);

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
    public List<MPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
    	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MPerson> getResultList(List<MPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(EntityManager em) {
        TypedQuery<MPerson> query = em.createNamedQuery("MPerson.findAll", MPerson.class);
        return query.getResultList();
    }

}
