package jp.co.osaki.sms.servicedao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.entity.MOshiraseDelivery;
import jp.co.osaki.osol.entity.MOshiraseDeliveryPK_;
import jp.co.osaki.osol.entity.MOshiraseDelivery_;
import jp.co.osaki.osol.entity.TOshirase;
import jp.co.osaki.osol.entity.TOshirasePK_;
import jp.co.osaki.osol.entity.TOshirase_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * お知らせ/トピックス情報
 *
 * @author h-shiba
 */
public class TOshiraseServiceDaoImpl implements BaseServiceDao<TOshirase> {

    /* 企業ID（パラメータキー） */
    public final String CORP_ID_KEY = "CORP_ID";

    /** 配信先コード（パラメータキー） */
    public final String DELIVERY_CD_KEY = "DELIVERY_CD";

    @Override
    public List<TOshirase> getResultList(TOshirase target, EntityManager em) {

        TypedQuery<TOshirase> query = em.createNamedQuery("TOshirase.findMenuOshiraseList", TOshirase.class);
        query.setParameter("corpId", target.getId().getCorpId());
        List<TOshirase> oshiraseList = query.getResultList();
        return oshiraseList;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TOshirase> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        String corpId = null;
        List<Object> deliveryCdList = null;
        List<Object> corpIdList = parameterMap.get(CORP_ID_KEY);

        if (corpIdList != null && !corpIdList.isEmpty()) {
            corpId = corpIdList.get(0).toString();
        }

        if (parameterMap.get(DELIVERY_CD_KEY) != null) {
            deliveryCdList = parameterMap.get(DELIVERY_CD_KEY);
        }

        Date nowDate = new Timestamp((new Date()).getTime());

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TOshirase> query = builder.createQuery(TOshirase.class);

        Root<TOshirase> root = query.from(TOshirase.class);
        Join<TOshirase, MOshiraseDelivery> joinMOshiraseDelivery = root.join(TOshirase_.MOshiraseDeliveries, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // 対象コード: 1（全企業） または （対象コード: 2（個別指定）かつ 企業ID）
        whereList.add(
                builder.or(
                        builder.equal(root.get(TOshirase_.targetCode), ApiGenericTypeConstants.TARGET_CODE.ALL.getVal()),
                        builder.and(
                                builder.equal(root.get(TOshirase_.targetCode), ApiGenericTypeConstants.TARGET_CODE.INDIVIDUAL.getVal()),
                                builder.equal(root.get(TOshirase_.id).get(TOshirasePK_.corpId), corpId))
                        )
                );

        // 掲載開始日 <= 取得日
        whereList.add(builder.lessThanOrEqualTo(root.get(TOshirase_.publishedStartDay), nowDate));

        // 掲載終了日がNULL または （掲載終了日 >= 取得日）
        whereList.add(
                builder.or(
                        builder.isNull(root.get(TOshirase_.publishedEndDay)),
                        builder.greaterThanOrEqualTo(root.get(TOshirase_.publishedEndDay), nowDate)
                        )
                );

        // お知らせ配信先（全サービス or SMS）
        whereList.add(joinMOshiraseDelivery.get(MOshiraseDelivery_.id).get(MOshiraseDeliveryPK_.deliveryCd).in(deliveryCdList));

        // 配信フラグがON
        whereList.add(builder.equal(joinMOshiraseDelivery.get(MOshiraseDelivery_.deliveryUseFlg), OsolConstants.FLG_ON));

        whereList.add(builder.equal(root.get(TOshirase_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .groupBy(
                        root.get(TOshirase_.id).get(TOshirasePK_.corpId),
                        root.get(TOshirase_.id).get(TOshirasePK_.oshiraseId))
                .orderBy(builder.desc(root.get(TOshirase_.publishedStartDay)));

        List<TOshirase> oshiraseList = em.createQuery(query).getResultList();
        return oshiraseList;
    }

    @Override
    public List<TOshirase> getResultList(List<TOshirase> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TOshirase> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TOshirase find(TOshirase target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TOshirase target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TOshirase merge(TOshirase target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TOshirase target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
