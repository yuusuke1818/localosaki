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
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター管理 メーター種別一覧取得 ServiceDaoクラス
 * @author kimura.m
 */
public class ListSmsMeterMeterTypeDaoImple implements BaseServiceDao<ListSmsMeterResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    // 【生成したいSQL】
//    SELECT DISTINCT
//        METER_TYPE                                      -- メーター種別
//        , METER_TYPE_NAME                               -- メーター種別名
//      FROM
//        M_METER_TYPE                                    -- メーター種別TBL
//      WHERE
//        CORP_ID = 'osakitest01' /* corpId */
//        AND BUILDING_ID = 1001 /* buildingId */

    @Override
    public List<ListSmsMeterResultData> getResultList(ListSmsMeterResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterResultData> query = builder.createQuery(ListSmsMeterResultData.class);

        // ■from句の整理
        // メーター種別TBL
        Root<MMeterType> root = query.from(MMeterType.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        // CORP_ID = 'osakitest01' /* corpId */ and BUILDING_ID = 1001 /* buildingId */
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.corpId), target.getCorpId()));
        whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.buildingId), target.getBuildingId()));

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsMeterResultData.class,
                root.get(MMeterType_.id).get(MMeterTypePK_.meterType),
                root.get(MMeterType_.meterTypeName))).
                distinct(true).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MMeterType_.id).get(MMeterTypePK_.meterType)));

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
