/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター管理 メーター情報のメーター管理ID一覧取（条件：装置IDのみ） ServiceDaoクラス
 * @author kimura.m
 */
public class ListSmsMeterMngIdByDevIdDaoImple implements BaseServiceDao<ListSmsMeterResultData> {

    /** 削除フラグ */
    public final static int DEL_FLG_OFF = 0;

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    // 【生成したいSQL】
//    SELECT
//        METER_MNG_ID
//      FROM
//        M_METER
//      WHERE
//        DEV_ID = 'M388819Y'
//        AND DEL_FLG = '0'
    @Override
    public List<ListSmsMeterResultData> getResultList(ListSmsMeterResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterResultData> query = builder.createQuery(ListSmsMeterResultData.class);

        // ■from句の整理
        // [MuDM2]メータ登録用TBL
        Root<MMeter> root = query.from(MMeter.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        // DEV_ID = 'M388819Y' AND DEL_FLG = '0'
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), target.getDevId()));
        whereList.add(builder.equal(root.get(MMeter_.delFlg), DEL_FLG_OFF));

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsMeterResultData.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(List<ListSmsMeterResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsMeterResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public ListSmsMeterResultData find(ListSmsMeterResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(ListSmsMeterResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public ListSmsMeterResultData merge(ListSmsMeterResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(ListSmsMeterResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
