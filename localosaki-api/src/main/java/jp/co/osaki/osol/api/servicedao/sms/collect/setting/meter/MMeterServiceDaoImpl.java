package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターテーブル ServiceDaoクラス
 * @author sagi_h
 *
 */
public class MMeterServiceDaoImpl implements BaseServiceDao<MMeter> {

    // SQL1 : update m_meter set hop1_id = null where dev_id = '装置ID' and hop1_id = '削除される無線ID';
    // SQL2 : update m_meter set hop2_id = null where dev_id = '装置ID' and hop2_id = '削除される無線ID';
    // SQL3 : update m_meter set hop3_id = null where dev_id = '装置ID' and hop3_id = '削除される無線ID';
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 無線メーター削除時のリレー無線ID抹消処理
        // クエリ生成
        List<Predicate> conditionList = new ArrayList<>();
        final String devId = (String) parameterMap.get("devId").get(0);
        /** 削除される無線ID */
        final String wirelessId = (String) parameterMap.get("wirelessId").get(0);

        for (int i = 0; i < 3; i++) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaUpdate<MMeter> query = cb.createCriteriaUpdate(MMeter.class);
            Root<MMeter> root = query.from(MMeter.class);

            // リレー無線1～3の条件を設定
            SingularAttribute<MMeter, String> hopIdAttribute;
            switch (i) {
            case 0:
                hopIdAttribute = MMeter_.hop1Id;
                break;
            case 1:
                hopIdAttribute = MMeter_.hop2Id;
                break;
            case 2:
                hopIdAttribute = MMeter_.hop3Id;
                break;
            default:
                // 通らないが、エラー対策の都合リレー無線1を入れる
                hopIdAttribute = MMeter_.hop1Id;
                break;
            }

            // 条件1: 装置IDが引数と一致
            conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

            // 条件2: リレー無線IDが削除対象と一致
            conditionList.add(cb.equal(root.get(hopIdAttribute), wirelessId));

            // 当該リレー無線IDをNULLに変更
            query.set(root.get(hopIdAttribute), (String) null).where(cb.and(conditionList.toArray(new Predicate[] {})));

            em.createQuery(query).executeUpdate();
            conditionList.clear();
        }

        return 0;
    }

    // 検索
    // select * from m_meter
    //  where dev_id = '装置ID' and meter_id = '計器ID' and wireless_id = '無線ID'(ハンディのみ)
    //  and (del_flg != '1' or del_flg is null)
    @Override
    public List<MMeter> getResultList(MMeter target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = builder.createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 条件1: 装置IDが引数と一致
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), target.getId().getDevId()));

        // 条件2: 計器IDが引数と一致
        if (target.getMeterId() != null) {
            whereList.add(builder.equal(root.get(MMeter_.meterId), target.getMeterId()));
        }

        // 条件3: ハンディのみ: 無線IDが引数と一致
        if (target.getWirelessId() != null) {
            whereList.add(builder.equal(root.get(MMeter_.wirelessId), target.getWirelessId()));
        }

        // 条件4: 削除フラグがONでない
        whereList.add(builder.or(builder.notEqual(root.get(MMeter_.delFlg), OsolConstants.FLG_ON),
                builder.isNull(root.get(MMeter_.delFlg))));

        query.where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MMeter> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(List<MMeter> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MMeter> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // 検索(条件：主キー)
    @Override
    public MMeter find(MMeter target, EntityManager em) {
        return em.find(MMeter.class, target.getId());
    }

    @Override
    public void persist(MMeter target, EntityManager em) {
        em.persist(target);
        return;
    }

    // 更新
    @Override
    public MMeter merge(MMeter target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(MMeter target, EntityManager em) {
        em.remove(target);
    }
}
