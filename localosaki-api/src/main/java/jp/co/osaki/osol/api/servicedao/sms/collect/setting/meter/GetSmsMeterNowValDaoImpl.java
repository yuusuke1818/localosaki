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

import com.amazonaws.util.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.sms.collect.setting.meter.GetSmsMeterNowValResult;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.osol.entity.TMeterDataPK_;
import jp.co.osaki.osol.entity.TMeterData_;
import jp.co.osaki.sms.SmsConstants.CMD_KIND;
import jp.co.osaki.sms.SmsConstants.METER_KIND;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーター管理 最新の検針値取得 ServiceDaoクラス
 * @author kimura.m
 */
public class GetSmsMeterNowValDaoImpl implements BaseServiceDao<GetSmsMeterNowValResult> {
    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GetSmsMeterNowValResult> getResultList(GetSmsMeterNowValResult target, EntityManager em) {
        List<GetSmsMeterNowValResult> result = new ArrayList<GetSmsMeterNowValResult>();
        final String devId = target.getDevId();
        final Long meterMngId = target.getMeterMngId();
        // メーター種類判別
        final MMeterPK meterParamPK = new MMeterPK();
        meterParamPK.setDevId(target.getDevId());
        meterParamPK.setMeterMngId(target.getMeterMngId());

        final MMeter exMeter = em.find(MMeter.class, meterParamPK);

        if (exMeter.getDelFlg().equals(OsolConstants.FLG_ON)) {
            result.add(new GetSmsMeterNowValResult());
        } else {
            final String meterId = exMeter.getMeterId();

            // スマートメーター
            if (meterId.startsWith(METER_KIND.SMART.getVal()) || meterId.startsWith(METER_KIND.PULSE.getVal())) {
                result = getMeterNowVal(devId, meterMngId, meterId, em);
                if(CollectionUtils.isNullOrEmpty(result)) {
                    result = new ArrayList<GetSmsMeterNowValResult>();
                    result.add(new GetSmsMeterNowValResult());
                }
                result.get(0).setDemandingFlg(getMeterNowvalCommandState(devId, meterMngId, em));
            } else {
                result = new ArrayList<GetSmsMeterNowValResult>();
                result.add(new GetSmsMeterNowValResult());
            }
        }
        return result;
    }

    @Override
    public List<GetSmsMeterNowValResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GetSmsMeterNowValResult> getResultList(List<GetSmsMeterNowValResult> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GetSmsMeterNowValResult> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GetSmsMeterNowValResult find(GetSmsMeterNowValResult target, EntityManager em) {
        // // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(GetSmsMeterNowValResult target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GetSmsMeterNowValResult merge(GetSmsMeterNowValResult target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(GetSmsMeterNowValResult target, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 現在値取得要求コマンドの状態を返す
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param em エンティティマネージャー
     * @return 当該メーターに対するメーター現在値要求の件数
     */
    private String getMeterNowvalCommandState(final String devId, final Long meterMngId, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = cb.createQuery(TCommand.class);
        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();

        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        conditionList.add(cb.equal(root.get(TCommand_.tag), meterMngId.toString()));
        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.command), CMD_KIND.GET_METERVAL.getVal()));

        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(cb.desc(root.get(TCommand_.updateDate)));
        final List<TCommand> resultList = em.createQuery(query).getResultList();
        String result = null;
        if (resultList.size() > 0) {
            result = resultList.get(0).getSrvEnt();
        }
        return result;
    }

    /**
     * メーター現在値を取得する。
     * @param devId 装置ID
     * @param meterMngId メーター管理番号
     * @param meterId 計器ID
     * @param em エンティティマネージャー
     * @return 現在値のresultクラス
     */
    private List<GetSmsMeterNowValResult> getMeterNowVal(final String devId, final Long meterMngId,
            final String meterId, EntityManager em) {
        // クエリ生成
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GetSmsMeterNowValResult> query = cb.createQuery(GetSmsMeterNowValResult.class);
        Root<MMeter> mRoot = query.from(MMeter.class);

        // メーター現在値テーブル…リレーションがないのでJOINできない
        Root<TMeterData> dRoot = query.from(TMeterData.class);

        // 条件指定
        List<Predicate> conditionList = new ArrayList<>();

        // 条件1: 装置IDが引数に一致
        conditionList.add(cb.equal(mRoot.get(MMeter_.id).get(MMeterPK_.devId), devId));

        // 条件2: メーター管理番号が引数に一致
        conditionList.add(cb.equal(mRoot.get(MMeter_.id).get(MMeterPK_.meterMngId), meterMngId));

        // 条件3: メーター現在値テーブルとの結合 装置ID
        conditionList.add(cb.equal(mRoot.get(MMeter_.id).get(MMeterPK_.devId),
                dRoot.get(TMeterData_.id).get(TMeterDataPK_.devId)));

        // 条件4: メーター現在値テーブルとの結合 メーター管理番号
        conditionList.add(cb.equal(mRoot.get(MMeter_.id).get(MMeterPK_.meterMngId),
                dRoot.get(TMeterData_.id).get(TMeterDataPK_.meterMngId)));

        // メーター種類ごとに取得カラムが異なる
        if (meterId.startsWith(METER_KIND.SMART.getVal())) {
            query.select(cb.construct(GetSmsMeterNowValResult.class, mRoot.get(MMeter_.id).get(MMeterPK_.meterMngId),
                    mRoot.get(MMeter_.id).get(MMeterPK_.devId), mRoot.get(MMeter_.meterId), mRoot.get(MMeter_.ifType),
                    dRoot.get(TMeterData_.ampere1), dRoot.get(TMeterData_.ampere3),
                    dRoot.get(TMeterData_.circuitBreaker), dRoot.get(TMeterData_.currentKwh1),
                    dRoot.get(TMeterData_.currentKwh2), dRoot.get(TMeterData_.measureDate),
                    dRoot.get(TMeterData_.momentaryPwr), dRoot.get(TMeterData_.voltage12),
                    dRoot.get(TMeterData_.voltage23)))
                    .where(cb.and(conditionList.toArray(new Predicate[] {})));
        } else if (meterId.startsWith(METER_KIND.PULSE.getVal())) {
            query.select(cb.construct(GetSmsMeterNowValResult.class, mRoot.get(MMeter_.id).get(MMeterPK_.meterMngId),
                    mRoot.get(MMeter_.id).get(MMeterPK_.devId), mRoot.get(MMeter_.meterId), mRoot.get(MMeter_.multi),
                    mRoot.get(MMeter_.pulseType), mRoot.get(MMeter_.pulseWeight), dRoot.get(TMeterData_.currentKwh1),
                    dRoot.get(TMeterData_.measureDate)))
                    .where(cb.and(conditionList.toArray(new Predicate[] {})));
        } else {
            // 基本的にはあり得ないが念のため
            List<GetSmsMeterNowValResult> result = new ArrayList<GetSmsMeterNowValResult>();
            result.add(new GetSmsMeterNowValResult());
            return result;
        }

        return em.createQuery(query).getResultList();
    }

}
