/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.utility.common.Utility;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メータ登録用 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class MeterServiceDaoImpl implements BaseServiceDao<MMeter> {

    /** 検索条件: 装置ID. */
    public static final String DEV_ID = "devId";

    /** 検索条件: (確定する)メーター管理番号一覧(非必須). */
    public static final String EXCLUSION_METER_MNG_ID = "exclusionMeterMngId";

    /** 検索条件: メーター管理番号一覧(非必須). */
    public static final String METER_MNG_ID = "meterMngId";

    /** フラグOFF. */
    public final static int FLG_OFF = 0;

    // メーター種別 検索
    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MMeter find(MMeter target, EntityManager em) {
        MMeter entity = em.find(MMeter.class, target.getId());

        if (entity == null || (entity.getDelFlg() != null && entity.getDelFlg() != FLG_OFF)) {
            return null;
        }

        return entity;
    }

    // 確定漏れ管理番号一覧 検索
    // 複数件数取得DBアクセス処理
    @Override
    public List<MMeter> getResultList(Map<String, List<Object>> map, EntityManager em) {

        String devId = Utility.getListFirstItem(map.get(DEV_ID)).toString(); // 装置ID(必須)
        List<Long> exclusionMngIdList = (map.get(EXCLUSION_METER_MNG_ID) == null ? null
                : map.get(EXCLUSION_METER_MNG_ID).stream().map(meterMngId -> (Long) meterMngId).collect(Collectors.toList())); // 確定するメーター管理番号リスト(非必須)
        List<Long> mngIdList = (map.get(METER_MNG_ID) == null ? null
                : map.get(METER_MNG_ID).stream().map(meterMngId -> (Long) meterMngId).collect(Collectors.toList())); // メーター管理番号リスト(非必須)

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MMeter> query = builder.createQuery(MMeter.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> whereList = new ArrayList<>();
        // 装置ID (必須)
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));
        // 除外するメーター管理番号リスト (非必須)
        if (exclusionMngIdList != null) {
            whereList.add(builder.not(root.get(MMeter_.id).get(MMeterPK_.meterMngId).in(exclusionMngIdList)));
        }
        // メーター管理番号リスト (非必須)
        if (mngIdList != null) {
            whereList.add(root.get(MMeter_.id).get(MMeterPK_.meterMngId).in(mngIdList));
        }
        // 削除フラグ
        whereList.add(builder.equal(root.get(MMeter_.delFlg), FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        List<MMeter> resultList = em.createQuery(query).getResultList();

        return resultList;
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MMeter> getResultList(MMeter target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 更新処理
    @Override
    public MMeter merge(MMeter target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeter> getResultList(List<MMeter> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MMeter> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(MMeter t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
