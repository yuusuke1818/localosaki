package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * (AieLink用メーター用)メーター管理番号取得.
 * 「OCR検針」→「AieLink」へ変更
 * ※取得できない場合は自動採番.
 * @author kobayashi.sho
 */
public class MeterMngIdServiceDaoImpl implements BaseServiceDao<MeterMngIdResultData> {

    @Override
    public MeterMngIdResultData find(MeterMngIdResultData target, EntityManager em) {

        MeterMngIdResultData meterMngIdDto = getMMeterByMeterId(target.getDevId(), target.getMeterId(), true, em); // 論理削除を含まない
        if (meterMngIdDto == null) {
            meterMngIdDto = getMMeterByMeterId(target.getDevId(), target.getMeterId(), false, em); // 論理削除を含む
        }
        if (meterMngIdDto == null) {
            // 取得できなかったため、自動採番する
            meterMngIdDto = getMaxMeterMngId(target.getDevId(), em);
        }

        return meterMngIdDto;
    }

    /**
     * メーター管理番号取得.
     * SQL : select meter_mng_id from m_meter where dev_id = 'xxx' and meter_id = 'xxx' and (del_flg is null or del_flg != '1') order by meter_mng_id desc limit 1
     * @param devId 装置ID
     * @param meterId メーターID
     * @param excludesLogicalDel 論理削除除外フラグ  true:論理削除を除外する  true:論理削除を除外しない
     * @param em EntityManager
     * @return メーター管理番号  null:該当なし
     */
    private MeterMngIdResultData getMMeterByMeterId(String devId, String meterId, boolean excludesLogicalDel, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MeterMngIdResultData> query = builder.createQuery(MeterMngIdResultData.class);

        Root<MMeter> root = query.from(MMeter.class);

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 条件1: 装置IDが引数と一致
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

        // 条件2: 計器IDが引数と一致
        whereList.add(builder.equal(root.get(MMeter_.meterId), meterId));

        if (excludesLogicalDel) {
            // 条件3: 削除フラグがONでない
            whereList.add(builder.or(builder.notEqual(root.get(MMeter_.delFlg), OsolConstants.FLG_ON),
                    builder.isNull(root.get(MMeter_.delFlg))));
        }

        query = query.select(builder.construct(MeterMngIdResultData.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.desc(root.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        List<MeterMngIdResultData> meterList = em.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();

        if (meterList.isEmpty()) {
            return null;
        }

        return meterList.get(0);
    }

    /**
     * メーター管理番号 採番 (最大値 + 1).
     * SQL : select max(meter_mng_id) from m_meter where dev_id = 'xxx'
     * @param devId 装置ID
     * @param em EntityManager
     * @return メーター管理番号  1～
     */
    private MeterMngIdResultData getMaxMeterMngId(String devId, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MeterMngIdResultData> query = builder.createQuery(MeterMngIdResultData.class);

        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> whereList = new ArrayList<>();

        // 装置ID
        whereList.add(builder.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

        query = query.select(builder.construct(MeterMngIdResultData.class,
                builder.max(root.get(MMeter_.id).get(MMeterPK_.meterMngId))))
                .where(whereList.toArray(new Predicate[]{}));

        List<MeterMngIdResultData> meterList = em.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();

        if (meterList.isEmpty() || meterList.get(0).getMeterMngId() == null) {
            return new MeterMngIdResultData(1L);
        }

        MeterMngIdResultData meterMngIdDto = meterList.get(0);
        meterMngIdDto.setMeterMngId(meterMngIdDto.getMeterMngId() + 1L);
        return meterMngIdDto;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public List<MeterMngIdResultData> getResultList(MeterMngIdResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public List<MeterMngIdResultData> getResultList(List<MeterMngIdResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public List<MeterMngIdResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public void persist(MeterMngIdResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public MeterMngIdResultData merge(MeterMngIdResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public void remove(MeterMngIdResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }

    @Override
    public List<MeterMngIdResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // 何もしない
    }
}
