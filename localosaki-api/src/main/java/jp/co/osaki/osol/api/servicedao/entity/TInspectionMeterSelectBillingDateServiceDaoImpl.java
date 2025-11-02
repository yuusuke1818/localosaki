package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataParameter;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class TInspectionMeterSelectBillingDateServiceDaoImpl implements BaseServiceDao<TInspectionMeterSvr> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(TInspectionMeterSvr target, EntityManager em) {
        return null;
    }

    // 【生成したいSQL】
    //SELECT
    //    TIMS.INSP_YEAR
    //    , TIMS.INSP_MONTH
    //    , TIMS.DEV_ID
    //    , TIMS.METER_MNG_ID
    //    , TIMS.LATEST_INSP_DATE
    //    , TIMS.INSP_MONTH_NO
    //    , TIMS.INSP_TYPE
    //    , TIMS.END_FLG
    //FROM
    //    INNER JOIN T_INSPECTION_METER_SVR TIMS
    //WHERE
    //    TIMS.LATEST_INSP_DATE BETWEEN '開始年' AND '終了年'
    //ORDER BY
    //    INSP_MONTH_NO
    //    , INSP_TYPE

    @Override
    public List<TInspectionMeterSvr> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        //Mapからリクエストを取得する
        List<Object> parameterList = parameterMap.get("ListSmsSelectBillingDataParameter");
        ListSmsSelectBillingDataParameter parameter = (ListSmsSelectBillingDataParameter) parameterList.get(0);

        String fromYear = parameter.getFromYear();//開始年
        String toYear = parameter.getToYear();//終了年

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

        // 取得元テーブル
        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        //検索条件
        List<Predicate> whereList = new ArrayList<>();

        // 取得年
        whereList.add(builder.between(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), fromYear, toYear));
        query = query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<TInspectionMeterSvr> getResultList(List<TInspectionMeterSvr> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<TInspectionMeterSvr> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public TInspectionMeterSvr find(TInspectionMeterSvr target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(TInspectionMeterSvr target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public TInspectionMeterSvr merge(TInspectionMeterSvr target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(TInspectionMeterSvr target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
