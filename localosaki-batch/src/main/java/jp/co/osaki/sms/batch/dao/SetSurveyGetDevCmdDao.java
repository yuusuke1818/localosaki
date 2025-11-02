package jp.co.osaki.sms.batch.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK_;
import jp.co.osaki.osol.entity.MAutoInsp_;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter_;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTime;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTime_;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.osol.entity.TInspectionMeterPK_;
import jp.co.osaki.osol.entity.TInspectionMeter_;
import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.osol.entity.TMeterData_;
import jp.co.osaki.osol.entity.TWorkHst;
import jp.co.osaki.osol.entity.TWorkHstPK_;
import jp.co.osaki.osol.entity.TWorkHst_;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.AutoInspDayHourResultSet;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialDeviceListResultSet;

/**
 * SMS 日報収集データセット（定期処理） Daoクラス
 *
 * @author tominaga
 *
 */
public class SetSurveyGetDevCmdDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public SetSurveyGetDevCmdDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * コマンドテーブル削除
     *
     * @param Timestamp recDate
     */
    public void delCommand(Timestamp recDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TCommand> delete = cb.createCriteriaDelete(TCommand.class);

        Root<TCommand> root = delete.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:srvEnt
        conditionList.add(cb.or(cb.isNull(root.get(TCommand_.srvEnt)), cb.notEqual(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())));
        // 条件2:recDate
        conditionList.add(cb.lessThan(root.get(TCommand_.id).get(TCommandPK_.recDate), recDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * コマンドテーブルメーター指定用削除
     *
     * @param Timestamp recDate
     */
    public void delCommandMeter(Timestamp recDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TCommandLoadSurveyMeter> delete = cb.createCriteriaDelete(TCommandLoadSurveyMeter.class);

        Root<TCommandLoadSurveyMeter> root = delete.from(TCommandLoadSurveyMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:srvEnt
        conditionList.add(cb.or(cb.isNull(root.get(TCommandLoadSurveyMeter_.srvEnt)), cb.notEqual(root.get(TCommandLoadSurveyMeter_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())));
        // 条件2:recDate
        conditionList.add(cb.lessThan(root.get(TCommandLoadSurveyMeter_.recDate), recDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * コマンドテーブル時間指定用削除
     *
     * @param Timestamp recDate
     */
    public void delCommandTime(Timestamp recDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TCommandLoadSurveyTime> delete = cb.createCriteriaDelete(TCommandLoadSurveyTime.class);

        Root<TCommandLoadSurveyTime> root = delete.from(TCommandLoadSurveyTime.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:srvEnt
        conditionList.add(cb.or(cb.isNull(root.get(TCommandLoadSurveyTime_.srvEnt)), cb.notEqual(root.get(TCommandLoadSurveyTime_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())));
        // 条件2:recDate
        conditionList.add(cb.lessThan(root.get(TCommandLoadSurveyTime_.recDate), recDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * 処理予約テーブル削除
     *
     * @param Timestamp recDate
     */
    public void delWrkHst(Timestamp recDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TWorkHst> delete = cb.createCriteriaDelete(TWorkHst.class);

        Root<TWorkHst> root = delete.from(TWorkHst.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:srvEnt
        conditionList.add(cb.or(cb.isNull(root.get(TWorkHst_.srvEnt)), cb.notEqual(root.get(TWorkHst_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())));
        // 条件2:recDate
        conditionList.add(cb.lessThan(root.get(TWorkHst_.id).get(TWorkHstPK_.recDate), recDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * ロードサーベイ日データ削除
     *
     * @param String delDate
     */
    public void delDayLoadSurvey(String delDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TDayLoadSurvey> delete = cb.createCriteriaDelete(TDayLoadSurvey.class);

        Root<TDayLoadSurvey> root = delete.from(TDayLoadSurvey.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getDate
        conditionList.add(cb.lessThan(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), delDate + "0000"));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * ロードサーベイ日データ（逆方向）削除
     *
     * @param String delDate
     */
    public void delRDayLoadSurvey(String delDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TDayLoadSurveyRev> delete = cb.createCriteriaDelete(TDayLoadSurveyRev.class);

        Root<TDayLoadSurveyRev> root = delete.from(TDayLoadSurveyRev.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getDate
        conditionList.add(cb.lessThan(root.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate), delDate + "0000"));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * 検針結果データ削除
     *
     * @param String delDate
     */
    public void delInspMeter(String delDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TInspectionMeter> delete = cb.createCriteriaDelete(TInspectionMeter.class);

        Root<TInspectionMeter> root = delete.from(TInspectionMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getDate
        String year = delDate.substring(0, 4);
        conditionList.add(cb.lessThan(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspYear), year));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * メータ現在値データ削除
     *
     * @param String delDate
     */
    public void delCurrentMeterData(Timestamp delDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<TMeterData> delete = cb.createCriteriaDelete(TMeterData.class);

        Root<TMeterData> root = delete.from(TMeterData.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getDate
        conditionList.add(cb.lessThan(root.get(TMeterData_.measureDate), delDate));

        delete.where(
                cb.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * MUDM2リスト取得
     *
     * @param
     * @retrun List<String> devIdリスト(MUDM2)
     */
    public List<PrevSurveyRetrialDeviceListResultSet> listDevPrm() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PrevSurveyRetrialDeviceListResultSet> query = cb.createQuery(PrevSurveyRetrialDeviceListResultSet.class);
        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:getDate
        conditionList.add(cb.equal(root.get(MDevPrm_.devKind), SmsBatchConstants.DEV_KIND.MUDM2.getVal()));
        conditionList.add(cb.equal(root.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(PrevSurveyRetrialDeviceListResultSet.class,
                root.get(MDevPrm_.devId),
                root.get(MDevPrm_.revFlg)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<PrevSurveyRetrialDeviceListResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }

    /**
     * コマンド設定
     *
     * @param String devId 装置ID
     * @param String command コマンド
     * @param String tag タグ
     * @param String svDate バッチ実施時間
     * @retrun
     */
    public void setCommand(String devId, String command, String tag, Timestamp svDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = cb.createQuery(TCommand.class);
        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();
        // タグ
        if (tag.equals("")) {
            conditionList.add(cb.isNull(root.get(TCommand_.tag)));
        } else {
            conditionList.add(cb.equal(root.get(TCommand_.tag), tag));
        }
        // 装置ID
        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        // コマンド
        conditionList.add(cb.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));

        // 指定コマンドが存在するかチェック
        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})));
        this.entityManager.clear();

        List<TCommand> ret = this.entityManager.createQuery(query).getResultList();

        if (ret.size() == 0) {
            // 存在しない場合 Insert
            TCommand entity = new TCommand();
            TCommandPK pk = new TCommandPK();
            pk.setDevId(devId);
            pk.setCommand(command);
            pk.setRecDate(svDate);

            entity.setId(pk);
            entity.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());
            entity.setRecMan("setCommand");
            entity.setTag(tag);

            entity.setCreateDate(svDate);
            entity.setCreateUserId(0L);
            entity.setUpdateDate(svDate);
            entity.setUpdateUserId(0L);

            entityManager.persist(entity);
            entityManager.flush();
            entityManager.clear();
        } else {
            // 存在して処理終了フラグが正常終了中の場合 update
            if (ret.get(0).getSrvEnt() == null || ret.get(0).getSrvEnt().equals("")) {
                CriteriaUpdate<TCommand> update = cb.createCriteriaUpdate(TCommand.class);
                root = update.from(TCommand.class);
                update.set(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal())
                        .set(root.get(TCommand_.id).get(TCommandPK_.recDate), svDate)
                        .set(root.get(TCommand_.recMan), "setCommand")
                        .set(root.get(TCommand_.updateDate), svDate)
                        .set(root.get(TCommand_.updateUserId), 0L)
                        .where(cb.and(conditionList.toArray(new Predicate[] {})));
                entityManager.createQuery(update).executeUpdate();
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * 自動検針設定リスト取得
     *
     * @param String devId
     * @retrun List<AutoInspDayHourResultSet> 自動検針設定リスト
     */
    public List<AutoInspDayHourResultSet> listAutoInspDayHour(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutoInspDayHourResultSet> query = cb.createQuery(AutoInspDayHourResultSet.class);
        Root<MAutoInsp> root = query.from(MAutoInsp.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(MAutoInsp_.id).get(MAutoInspPK_.devId), devId));

        query.select(cb.construct(AutoInspDayHourResultSet.class,
                root.get(MAutoInsp_.day),
                root.get(MAutoInsp_.hour)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<AutoInspDayHourResultSet> ret = this.entityManager.createQuery(query).getResultList();

        return ret;

    }
    /**
     * 自動検針結果の存在確認
     *
     * @param String devId
     * @param String year
     * @param String month
     * @retrun Boolean true:あり、false:なし
     */

    public Boolean isLatestAutoInspMeter(String devId,String year,String month) {
        Boolean ret = true;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeter> query = cb.createQuery(TInspectionMeter.class);
        Root<TInspectionMeter> root = query.from(TInspectionMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:devId
        conditionList.add(cb.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.devId), devId));
        // 条件2:inspYear
        conditionList.add(cb.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspYear), year));
        // 条件3:inspMonth
        String tmpMonth = String.valueOf(Integer.parseInt(month));
        conditionList.add(cb.equal(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspMonth), tmpMonth));
        // 条件4:inspType
        conditionList.add(cb.equal(root.get(TInspectionMeter_.inspType), SmsBatchConstants.INSP_KIND.AUTO.getVal()));


        query.select(root)
                .where(cb.and(conditionList.toArray(new Predicate[] {})))
                .orderBy(
                        cb.desc(root.get(TInspectionMeter_.id).get(TInspectionMeterPK_.inspMonthNo)));
        List<TInspectionMeter> retlist = this.entityManager.createQuery(query).setMaxResults(1).getResultList();
        this.entityManager.clear();

        if(retlist.size()==0) {
            ret = false;
        }
        return ret;
    }
}
