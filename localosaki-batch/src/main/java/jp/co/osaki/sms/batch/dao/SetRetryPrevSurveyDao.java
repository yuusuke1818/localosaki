package jp.co.osaki.sms.batch.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.batch.SmsBatchConstants;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialCommandCountResultSet;
import jp.co.osaki.sms.batch.resultset.PrevSurveyRetrialDeviceListResultSet;

/**
 * 日報データ前日分 再収集予約処理実行 DAOクラス
 * @author sagi_h
 *
 */
public class SetRetryPrevSurveyDao extends SmsBatchDao {

    /**
     * コンストラクタ
     * @param entityManager
     */
    public SetRetryPrevSurveyDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 再収集予約対象の装置リストを取得する。
     * @return 装置リスト
     */
    public List<PrevSurveyRetrialDeviceListResultSet> getDeviceListForSurveyRetrial() {
        // 装置情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<PrevSurveyRetrialDeviceListResultSet> query = builder
                .createQuery(PrevSurveyRetrialDeviceListResultSet.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1:装置IDがMHで始まらない
        conditionList.add(builder.notLike(root.get(MDevPrm_.devId), DEVICE_KIND.HANDY.getVal() + "%"));
        // 条件2:装置IDがOCで始まらない
        conditionList.add(builder.notLike(root.get(MDevPrm_.devId), "OC%"));
        // 条件3:削除フラグがONでない
        conditionList.add(builder.notEqual(root.get(MDevPrm_.delFlg), OsolConstants.FLG_ON));

        query.select(builder.construct(PrevSurveyRetrialDeviceListResultSet.class, root.get(MDevPrm_.devId),
                root.get(MDevPrm_.revFlg)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));

        this.entityManager.clear();
        List<PrevSurveyRetrialDeviceListResultSet> tMDevPrmList = this.entityManager.createQuery(query).getResultList();

        return tMDevPrmList;
    }

    /**
     * 指定した条件に合致する、処理待ちまたは電文送信中のコマンドの件数を取得する。
     * @param devId 装置ID
     * @param command コマンド文字列
     * @param tag タグ(対象日付)
     * @return 合致するコマンドの件数
     */
    public PrevSurveyRetrialCommandCountResultSet countCommandsForPrevSurvey(String devId, String command, String tag) {
        // コマンド情報を検索
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<PrevSurveyRetrialCommandCountResultSet> query = builder
                .createQuery(PrevSurveyRetrialCommandCountResultSet.class);
        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> conditionList = new ArrayList<>();
        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        // 条件2: コマンド文字列が引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        // 条件3: タグ(対象日時)が引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.tag), tag));
        // 条件4: 処理フラグが処理待ちまたは電文送信中
        conditionList
                .add(builder.or(builder.equal(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal()),
                        builder.equal(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.SENDING.getVal())));

        // 論理削除対象でないため、削除フラグ判定はなし

        query.select(builder.construct(PrevSurveyRetrialCommandCountResultSet.class, builder.count(root)))
                .where(builder.and(conditionList.toArray(new Predicate[] {})));
        this.entityManager.clear();

        PrevSurveyRetrialCommandCountResultSet result = entityManager.createQuery(query).getSingleResult();

        return result;
    }

    /**
     * 指定された条件に合致するコマンドを削除する。
     * @param devId
     * @param command
     * @param tag
     */
    public void deleteCommandForPrevSurveyRetrial(String devId, String command, String tag) {
        // コマンド情報を検索
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<TCommand> delete = builder.createCriteriaDelete(TCommand.class);

        Root<TCommand> root = delete.from(TCommand.class);
        List<Predicate> conditionList = new ArrayList<>();

        // 条件1: 装置IDが引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), devId));
        // 条件2: コマンド文字列が引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), command));
        // 条件3: タグが引数に一致
        conditionList.add(builder.equal(root.get(TCommand_.tag), tag));
        // 条件4: 処理フラグが処理待ちでも電文送信中でもない
        conditionList
                .add(builder.or(builder.isNull(root.get(TCommand_.srvEnt)), builder.and(
                        builder.notEqual(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal()),
                        builder.notEqual(root.get(TCommand_.srvEnt), SmsConstants.TCOMMAND_SRV_ENT.SENDING.getVal()))));

        // 論理削除対象でないため、削除フラグ判定はなし

        delete.where(
                builder.and(conditionList.toArray(new Predicate[] {})));

        entityManager.createQuery(delete).executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 与えられた値でコマンド追加する。
     * @param devId 装置ID
     * @param command コマンド文字列
     * @param tag タグ(対象日付)
     * @param recDate 現在時刻(REC_DATE、作成・更新日時用)
     */
    public void createCommandForPrevSurveyRetrial(String devId, String command, String tag, Timestamp recDate) {
        TCommand tCommand = new TCommand();
        TCommandPK tCommandPK = new TCommandPK();

        tCommandPK.setDevId(devId);
        tCommandPK.setRecDate(recDate);
        tCommandPK.setCommand(command);
        tCommand.setId(tCommandPK);
        tCommand.setTag(tag);
        tCommand.setSrvEnt(SmsConstants.TCOMMAND_SRV_ENT.WAIT.getVal());

        tCommand.setVersion(Integer.valueOf(0));
        tCommand.setRecMan(SmsBatchConstants.REC_MAN.SET_RETRY_PREV_SURVEY.getVal());
        tCommand.setCreateUserId(Long.valueOf(0));
        tCommand.setCreateDate(recDate);
        tCommand.setUpdateUserId(Long.valueOf(0));
        tCommand.setUpdateDate(recDate);

        entityManager.persist(tCommand);
        entityManager.flush();
        entityManager.clear();
    }
}
