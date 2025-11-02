package jp.co.osaki.sms.batch.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.batch.OsolBatchConstants;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemand;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyMaxDemandRevPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK;
import jp.co.osaki.osol.entity.TInspectionMeterBef;
import jp.co.osaki.osol.entity.TMeterData;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.resultset.ImportCsvConcentratorInfoResultSet;
import jp.co.osaki.sms.batch.resultset.RFMeterInfoResultSetMMeter;
import jp.co.osaki.sms.batch.resultset.RFMeterInfoResultSetRelations;

/**
 * SMS CSV取込 Daoクラス
 *
 * @author yonezawa.a
 *
 */
public class ImportFromCsvDao extends SmsBatchDao {

    // 最大デマンドを2倍にする
    private final BigDecimal calcMaxdemand = new BigDecimal("2");

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public ImportFromCsvDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * ロードサーベイ日データ登録更新
     *
     * @param tDayLoadSurvey
     * @param serverDateTime
     * @param dataType
     */
    public void createTDayLoadSurvey(TDayLoadSurvey tDayLoadSurvey, Timestamp serverDateTime, String dataType) {

        TDayLoadSurvey tdls = new TDayLoadSurvey();

        // DB取得
        TDayLoadSurvey entity = entityManager.find(TDayLoadSurvey.class, tDayLoadSurvey.getId());

        // 登録更新処理
        if (entity == null) {
            // 登録
            tdls.setId(tDayLoadSurvey.getId());
            tdls.setRecDate(serverDateTime);
            tdls.setRecMan("TGFUNC_LOAD_SURVEY");
            if (OsolBatchConstants.TARGET_KWH30.equals(dataType)) {
                tdls.setKwh30(tDayLoadSurvey.getKwh30());
                tdls.setDmvKwh(null);
            } else if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)) {
                tdls.setKwh30(null);
                tdls.setDmvKwh(tDayLoadSurvey.getDmvKwh());
            }
            tdls.setVersion(0);
            tdls.setCreateUserId(Long.valueOf(0));
            tdls.setCreateDate(serverDateTime);
            tdls.setUpdateUserId(Long.valueOf(0));
            tdls.setUpdateDate(serverDateTime);
            entityManager.persist(tdls);
            entityManager.flush();
            entityManager.clear();
        } else {
            // 更新対象が順方向かつ30分使用電力量がnullの場合のみ更新
            if (OsolBatchConstants.TARGET_KWH30.equals(dataType) && entity.getKwh30() == null) {
                entity.setKwh30(tDayLoadSurvey.getKwh30());
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }

            // 更新対象が逆方法かつ指針値データがnullの場合のみ更新
            if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType) && entity.getDmvKwh() == null) {
                entity.setDmvKwh(tDayLoadSurvey.getDmvKwh());
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * 正方向の登録
     * @param tDayLoadSurvey
     * @param serverDateTime
     * @param dataType
     */
    public void createMaxDemand(TDayLoadSurvey tDayLoadSurvey, Timestamp serverDateTime, String dataType) {
        TDayLoadSurveyMaxDemand tdlsmd = new TDayLoadSurveyMaxDemand();
        TDayLoadSurveyMaxDemandPK tdlsmdPk = new TDayLoadSurveyMaxDemandPK();
        tdlsmdPk.setDevId(tDayLoadSurvey.getId().getDevId());
        tdlsmdPk.setMeterMngId(tDayLoadSurvey.getId().getMeterMngId());
        tdlsmdPk.setGetSurveyDate(calDay(tDayLoadSurvey.getId().getGetDate()));
        TDayLoadSurveyMaxDemand entity = entityManager.find(TDayLoadSurveyMaxDemand.class, tdlsmdPk);
        if(entity == null) {
            tdlsmd.setId(tdlsmdPk);
            tdlsmd.setMaxDemand(tDayLoadSurvey.getKwh30() == null ? null : tDayLoadSurvey.getKwh30().multiply(calcMaxdemand));
            tdlsmd.setRecDate(serverDateTime);
            tdlsmd.setRecMan("TGFUNC_LOAD_SURVEY");
            tdlsmd.setVersion(0);
            tdlsmd.setCreateUserId(Long.valueOf(0));
            tdlsmd.setCreateDate(serverDateTime);
            tdlsmd.setUpdateUserId(Long.valueOf(0));
            tdlsmd.setUpdateDate(serverDateTime);
            entityManager.persist(tdlsmd);
            entityManager.flush();
            entityManager.clear();
        }else {
            if (entity.getMaxDemand() == null || (tDayLoadSurvey.getKwh30() != null && entity.getMaxDemand().compareTo(tDayLoadSurvey.getKwh30().multiply(calcMaxdemand)) < 0)) {
                entity.setMaxDemand(tDayLoadSurvey.getKwh30() == null ? null : tDayLoadSurvey.getKwh30().multiply(calcMaxdemand));
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * 逆方向の登録
     * @param tDayLoadSurveyRev
     * @param serverDateTime
     * @param dataType
     */
    public void createMaxDemandRev(TDayLoadSurveyRev tDayLoadSurveyRev, Timestamp serverDateTime, String dataType) {
        TDayLoadSurveyMaxDemandRev tdlsmd = new TDayLoadSurveyMaxDemandRev();
        TDayLoadSurveyMaxDemandRevPK tdlsmdPk = new TDayLoadSurveyMaxDemandRevPK();
        tdlsmdPk.setDevId(tDayLoadSurveyRev.getId().getDevId());
        tdlsmdPk.setMeterMngId(tDayLoadSurveyRev.getId().getMeterMngId());
        tdlsmdPk.setGetSurveyDate(calDay(tDayLoadSurveyRev.getId().getGetDate()));
        TDayLoadSurveyMaxDemandRev entity = entityManager.find(TDayLoadSurveyMaxDemandRev.class, tdlsmdPk);
        if(entity == null) {
            tdlsmd.setId(tdlsmdPk);
            tdlsmd.setMaxDemand(tDayLoadSurveyRev.getKwh30() == null ? null : tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand));
            tdlsmd.setRecDate(serverDateTime);
            tdlsmd.setRecMan("TGFUNC_LOAD_SURVEY");
            tdlsmd.setVersion(0);
            tdlsmd.setCreateUserId(Long.valueOf(0));
            tdlsmd.setCreateDate(serverDateTime);
            tdlsmd.setUpdateUserId(Long.valueOf(0));
            tdlsmd.setUpdateDate(serverDateTime);
            entityManager.persist(tdlsmd);
            entityManager.flush();
            entityManager.clear();
        }else {
            if (entity.getMaxDemand() == null || (tDayLoadSurveyRev.getKwh30() != null && entity.getMaxDemand().compareTo(tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand)) < 0)) {
                entity.setMaxDemand(tDayLoadSurveyRev.getKwh30() == null ? null : tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand));
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * 正方向の登録
     * @param kwh30
     */
    public void createMaxDemandForConcentrator(TDayLoadSurvey tDayLoadSurvey) {
        Timestamp serverDateTime = tDayLoadSurvey.getUpdateDate();

        TDayLoadSurveyMaxDemand tdlsmd = new TDayLoadSurveyMaxDemand();
        TDayLoadSurveyMaxDemandPK tdlsmdPk = new TDayLoadSurveyMaxDemandPK();
        tdlsmdPk.setDevId(tDayLoadSurvey.getId().getDevId());
        tdlsmdPk.setMeterMngId(tDayLoadSurvey.getId().getMeterMngId());
        tdlsmdPk.setGetSurveyDate(calDay(tDayLoadSurvey.getId().getGetDate()));
        TDayLoadSurveyMaxDemand entity = entityManager.find(TDayLoadSurveyMaxDemand.class, tdlsmdPk);
        if(entity == null) {
            tdlsmd.setId(tdlsmdPk);
            tdlsmd.setMaxDemand(tDayLoadSurvey.getKwh30() == null ? null : tDayLoadSurvey.getKwh30().multiply(calcMaxdemand));
            tdlsmd.setRecDate(serverDateTime);
            tdlsmd.setRecMan("TGFUNC_LOAD_SURVEY");
            tdlsmd.setVersion(0);
            tdlsmd.setCreateUserId(Long.valueOf(0));
            tdlsmd.setCreateDate(serverDateTime);
            tdlsmd.setUpdateUserId(Long.valueOf(0));
            tdlsmd.setUpdateDate(serverDateTime);
            entityManager.persist(tdlsmd);
            entityManager.flush();
            entityManager.clear();
        }else {
            if (entity.getMaxDemand() == null || (tDayLoadSurvey.getKwh30() != null && entity.getMaxDemand().compareTo(tDayLoadSurvey.getKwh30().multiply(calcMaxdemand)) < 0)) {
                entity.setMaxDemand(tDayLoadSurvey.getKwh30() == null ? null : tDayLoadSurvey.getKwh30().multiply(calcMaxdemand));
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * 逆方向の登録
     * @param kwh30
     */
    public void createMaxDemandRevForConcentrator(TDayLoadSurveyRev tDayLoadSurveyRev) {
        Timestamp serverDateTime = tDayLoadSurveyRev.getUpdateDate();

        TDayLoadSurveyMaxDemandRev tdlsmd = new TDayLoadSurveyMaxDemandRev();
        TDayLoadSurveyMaxDemandRevPK tdlsmdPk = new TDayLoadSurveyMaxDemandRevPK();
        tdlsmdPk.setDevId(tDayLoadSurveyRev.getId().getDevId());
        tdlsmdPk.setMeterMngId(tDayLoadSurveyRev.getId().getMeterMngId());
        tdlsmdPk.setGetSurveyDate(calDay(tDayLoadSurveyRev.getId().getGetDate()));
        TDayLoadSurveyMaxDemandRev entity = entityManager.find(TDayLoadSurveyMaxDemandRev.class, tdlsmdPk);
        if(entity == null) {
            tdlsmd.setId(tdlsmdPk);
            tdlsmd.setMaxDemand(tDayLoadSurveyRev.getKwh30() == null ? null : tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand));
            tdlsmd.setRecDate(serverDateTime);
            tdlsmd.setRecMan("TGFUNC_LOAD_SURVEY");
            tdlsmd.setVersion(0);
            tdlsmd.setCreateUserId(Long.valueOf(0));
            tdlsmd.setCreateDate(serverDateTime);
            tdlsmd.setUpdateUserId(Long.valueOf(0));
            tdlsmd.setUpdateDate(serverDateTime);
            entityManager.persist(tdlsmd);
            entityManager.flush();
            entityManager.clear();
        }else {
            if (entity.getMaxDemand() == null || (tDayLoadSurveyRev.getKwh30() != null && entity.getMaxDemand().compareTo(tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand)) < 0)) {
                entity.setMaxDemand(tDayLoadSurveyRev.getKwh30() == null ? null : tDayLoadSurveyRev.getKwh30().multiply(calcMaxdemand));
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     *
     * @param date
     * @return
     */
    private String calDay(String date) {
        //HHmmだけ抜き取る
        String hour = date.substring(8);
        if(hour.equals("0000")) {
            return DateUtility.plusDay(date.substring(0, 8), -1);
        }
        return date.substring(0, 8);
    }

    /**
     * ロードサーベイ日データのコンセントレータ対応
     *
     * @param tDayLoadSurvey
     * @param serverDateTime
     */
    public void updateTDayLoadSurveyForConsentrator(TDayLoadSurvey tDayLoadSurvey, String dataType,
            Timestamp serverDateTime, BigDecimal multi) {

        // DB取得
        TDayLoadSurvey entity = entityManager.find(TDayLoadSurvey.class, tDayLoadSurvey.getId());

        if (entity == null) {
            // 取れなかったらおかしい
        } else {
            if (entity.getKwh30() == null) {
                DecimalFormat dFmt = new DecimalFormat("#.#");
                BigDecimal updKwh30 = new BigDecimal("0");

                // 取得したGET＿DATEの30分前を取得
                String targetGetDate = get30minBeforGetDate(entity.getId().getGetDate());

                // エンティティ情報クリア
                entityManager.clear();

                // 30分前の指針値を取得
                TDayLoadSurveyPK oldPK = new TDayLoadSurveyPK();
                oldPK.setDevId(tDayLoadSurvey.getId().getDevId());
                oldPK.setGetDate(targetGetDate);
                oldPK.setMeterMngId(tDayLoadSurvey.getId().getMeterMngId());
                final TDayLoadSurvey oldRecord = entityManager.find(TDayLoadSurvey.class, oldPK);

                // 小数点以下の最小値
                dFmt.setMinimumFractionDigits(1);
                // 小数点以下の最大値
                dFmt.setMaximumFractionDigits(1);

                if (oldRecord == null) {
                    // 前回指針値がない場合は、nullを登録する
                    updKwh30 = null;
                } else {
                    // コンセントレータの30分使用量をセット
                    updKwh30 = getUpdKwh30(dFmt, oldRecord.getDmvKwh(), tDayLoadSurvey.getDmvKwh()).multiply(multi);
                }

                // エンティティ情報クリア
                entityManager.clear();

                // 更新
                entity.setKwh30(updKwh30);
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();

                createMaxDemandForConcentrator(entity);

            }
        }
    }

    /**
     * 取得日からを30分前の取得日を取得
     *
     * @param getDate
     * @return
     */
    private String get30minBeforGetDate(final String getDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
        Calendar prevGetDateCal = Calendar.getInstance();
        try {
            prevGetDateCal.setTime(sdf.parse(getDate));
        } catch (ParseException e) {
            return null;
        }
        prevGetDateCal.add(Calendar.MINUTE, -30);
        final String targetGetDate = sdf.format(prevGetDateCal.getTime());

        return targetGetDate;
    }

    /**
     * 30分使用電力量の算出（コンセントレータ対応）
     *
     * @param dFmt
     * @param oldDmvKwh
     * @param newDmvKwh
     * @return 更新値
     */
    private BigDecimal getUpdKwh30(DecimalFormat dFmt, BigDecimal oldDmvKwh, BigDecimal newDmvKwh) {

        // フォーマット：12345.1
        BigDecimal updKwh30 = oldDmvKwh == null ? new BigDecimal("0") : oldDmvKwh;
        updKwh30 = new BigDecimal(dFmt.format(updKwh30));
        newDmvKwh = new BigDecimal(dFmt.format(newDmvKwh));
        // 全体の桁数から、小数点以下の桁数を引く
        final int integerDigits = updKwh30.precision() - updKwh30.scale();

        if (updKwh30.compareTo(newDmvKwh) == 1) {
            // 1回転
            updKwh30 = newDmvKwh.add(new BigDecimal(Math.pow(10, integerDigits))).subtract(updKwh30);
        } else {
            updKwh30 = newDmvKwh.subtract(updKwh30);
        }
        return updKwh30;
    }

    /**
     * ロードサーベイ日データ（逆方向）登録更新
     *
     * @param tDayLoadSurveyRev
     * @param serverDateTime
     * @param dataType
     */
    public void createTDayLoadSurveyRev(TDayLoadSurveyRev tDayLoadSurveyRev, Timestamp serverDateTime,
            String dataType) {

        TDayLoadSurveyRev tdlsr = new TDayLoadSurveyRev();

        // DB取得
        TDayLoadSurveyRev entity = entityManager.find(TDayLoadSurveyRev.class, tDayLoadSurveyRev.getId());

        // 登録更新処理
        if (entity == null) {
            // 登録
            tdlsr.setId(tDayLoadSurveyRev.getId());
            tdlsr.setRecDate(serverDateTime);
            tdlsr.setRecMan("TGFUNC_LOAD_SURVEY_R");
            if (OsolBatchConstants.TARGET_KWH30.equals(dataType)) {
                tdlsr.setKwh30(tDayLoadSurveyRev.getKwh30());
                tdlsr.setDmvKwh(null);
            } else if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType)) {
                tdlsr.setKwh30(null);
                tdlsr.setDmvKwh(tDayLoadSurveyRev.getDmvKwh());
            }
            tdlsr.setVersion(0);
            tdlsr.setCreateUserId(Long.valueOf(0));
            tdlsr.setCreateDate(serverDateTime);
            tdlsr.setUpdateUserId(Long.valueOf(0));
            tdlsr.setUpdateDate(serverDateTime);
            entityManager.persist(tdlsr);
            entityManager.flush();
            entityManager.clear();
        } else {
            // 更新対象が順方向かつ30分使用電力量がnullの場合のみ更新
            if (OsolBatchConstants.TARGET_KWH30.equals(dataType) && entity.getKwh30() == null) {
                entity.setKwh30(tDayLoadSurveyRev.getKwh30());
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }

            // 更新対象が逆方法かつ指針値データがnullの場合のみ更新
            if (OsolBatchConstants.TARGET_DMVKWH.equals(dataType) && entity.getDmvKwh() == null) {
                entity.setDmvKwh(tDayLoadSurveyRev.getDmvKwh());
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    /**
     * ロードサーベイ日データ（逆方向）のコンセントレータ対応
     *
     * @param tDayLoadSurveyRev
     * @param dataType
     * @param serverDateTime
     */
    public void updateTDayLoadSurveyRevForConsentrator(TDayLoadSurveyRev tDayLoadSurveyRev, String dataType,
            Timestamp serverDateTime, BigDecimal multi) {

        // DB取得
        TDayLoadSurveyRev entity = entityManager.find(TDayLoadSurveyRev.class, tDayLoadSurveyRev.getId());

        // 更新処理
        if (entity == null) {
            // 取れなかったらおかしい
        } else {
            if (entity.getKwh30() == null) {
                DecimalFormat dFmt = new DecimalFormat("#.#");
                BigDecimal updKwh30 = new BigDecimal("0");

                // 取得したGET＿DATEの30分前を取得
                String targetGetDate = get30minBeforGetDate(entity.getId().getGetDate());

                // エンティティ情報クリア
                entityManager.clear();

                // 30分前の指針値を取得
                TDayLoadSurveyRevPK oldPK = new TDayLoadSurveyRevPK();
                oldPK.setDevId(tDayLoadSurveyRev.getId().getDevId());
                oldPK.setGetDate(targetGetDate);
                oldPK.setMeterMngId(tDayLoadSurveyRev.getId().getMeterMngId());
                final TDayLoadSurveyRev oldRecord = entityManager.find(TDayLoadSurveyRev.class, oldPK);

                // 小数点以下の最小値
                dFmt.setMinimumFractionDigits(1);
                // 小数点以下の最大値
                dFmt.setMaximumFractionDigits(1);

                if (oldRecord == null) {
                    // 前回指針値がない場合は、nullを登録する
                    updKwh30 = null;
                } else {
                    // コンセントレータの30分使用量をセット
                    updKwh30 = getUpdKwh30(dFmt, oldRecord.getDmvKwh(), tDayLoadSurveyRev.getDmvKwh()).multiply(multi);
                }

                // エンティティ情報クリア
                entityManager.clear();

                // 更新
                entity.setKwh30(updKwh30);
                entity.setUpdateDate(serverDateTime);
                entityManager.merge(entity);
                entityManager.flush();
                entityManager.clear();

                createMaxDemandRevForConcentrator(entity);
            }
        }
    }

    /**
     * メーター現在値データ登録更新
     *
     * @param tMeterData
     * @param serverDateTime
     */
    public void createTMeterData(TMeterData tMeterData, Timestamp serverDateTime) {

        TMeterData tmd = new TMeterData();

        // DB取得
        TMeterData entity = entityManager.find(TMeterData.class, tMeterData.getId());

        // 登録更新処理
        if (entity == null) {
            // 登録
            tmd.setId(tMeterData.getId());
            tmd.setMeterId(tMeterData.getMeterId());
            tmd.setRecDate(serverDateTime);
            tmd.setRecMan("TGFUNC_METER_DATA");
            tmd.setMeasureDate(tMeterData.getMeasureDate());
            tmd.setCurrentKwh1(tMeterData.getCurrentKwh1());
            tmd.setCurrentKwh2(tMeterData.getCurrentKwh2());
            tmd.setMomentaryPwr(tMeterData.getMomentaryPwr());
            tmd.setVoltage12(tMeterData.getVoltage12());
            tmd.setVoltage13(tMeterData.getVoltage13());
            tmd.setVoltage23(tMeterData.getVoltage23());
            tmd.setAmpere1(tMeterData.getAmpere1());
            tmd.setAmpere2(tMeterData.getAmpere2());
            tmd.setAmpere3(tMeterData.getAmpere3());
            tmd.setCircuitBreaker(tMeterData.getCircuitBreaker());
            tmd.setPowerFactor(tMeterData.getPowerFactor());
            tmd.setCreateUserId(Long.valueOf(0));
            tmd.setCreateDate(serverDateTime);
            tmd.setUpdateUserId(Long.valueOf(0));
            tmd.setUpdateDate(serverDateTime);
            entityManager.persist(tmd);
            entityManager.flush();
            entityManager.clear();
        } else {
            // 更新
            entity.setMeterId(tMeterData.getMeterId());
            entity.setRecDate(serverDateTime);
            entity.setRecMan("TGFUNC_METER_DATA");
            entity.setMeasureDate(tMeterData.getMeasureDate());
            entity.setCurrentKwh1(tMeterData.getCurrentKwh1());
            entity.setCurrentKwh2(tMeterData.getCurrentKwh2());
            entity.setMomentaryPwr(tMeterData.getMomentaryPwr());
            entity.setVoltage12(tMeterData.getVoltage12());
            entity.setVoltage13(tMeterData.getVoltage13());
            entity.setVoltage23(tMeterData.getVoltage23());
            entity.setAmpere1(tMeterData.getAmpere1());
            entity.setAmpere2(tMeterData.getAmpere2());
            entity.setAmpere3(tMeterData.getAmpere3());
            entity.setCircuitBreaker(tMeterData.getCircuitBreaker());
            entity.setPowerFactor(tMeterData.getPowerFactor());
            entity.setUpdateDate(serverDateTime);
            entityManager.merge(entity);
        }
    }

    /**
     * メーターに関連するテーブル登録更新
     *
     * @param mMeter
     * @param tenantId
     * @param tBuilding
     * @param mDevRelation
     * @param tBuildDevMeterRelation
     * @param tInspectionMeterBef
     * @param mTenantSms
     * @param serverDateTime
     * @return エラー文字列
     */
    public String createrRMeterInfo(MMeter mMeter, String tenantId, TBuilding tBuilding, MDevRelation mDevRelation,
            TBuildDevMeterRelation tBuildDevMeterRelation, TInspectionMeterBef tInspectionMeterBef,
            MTenantSm mTenantSms, Timestamp serverDateTime) {

        // テーブル登録フラグ
        boolean insFlg = false;

        // メーター登録用DB取得
        List<RFMeterInfoResultSetMMeter> mmList = getMMeterList(mMeter);

        // 建物、装置関連DB取得
        List<RFMeterInfoResultSetRelations> relationsList = getRelation(mMeter.getId().getDevId());

        // 建物テナントDB取得
        Long tbCnt = getBuildingTenantCnt(tenantId, relationsList);

        // メーター、建物テナントが0件の場合
        if ((mmList == null || mmList.size() == 0) && tbCnt == 0) {

            // 建物、装置関連DBリスト分ループ
            for (RFMeterInfoResultSetRelations target : relationsList) {
                // 親建物情報を取得
                TBuildingPK parentBuildingPk = new TBuildingPK();
                parentBuildingPk.setCorpId(target.getCorpId());
                parentBuildingPk.setBuildingId(target.getBuildingId());
                TBuilding parentBuildingInfo = entityManager.find(TBuilding.class, parentBuildingPk);

                // 建物テーブル
                TBuildingPK tbPk = new TBuildingPK();
                tbPk.setCorpId(target.getCorpId());
                // シーケンス採番
                tbPk.setBuildingId(createId(OsolConstants.ID_SEQUENCE_NAME.BUILDING_ID.getVal()));
                tBuilding.setId(tbPk);
                // "親建物ID_ユーザーコード"を建物NOにセット
                tBuilding.setBuildingNo(String.valueOf(target.getBuildingId() + "_" + tenantId));
                // CSVのテナントコードを所属建物IDにセット
                tBuilding.setDivisionBuildingId(Long.valueOf(tenantId));
                tBuilding.setDelFlg(OsolConstants.FLG_OFF);
                tBuilding.setCreateUserId(Long.valueOf(0));
                tBuilding.setCreateDate(serverDateTime);
                tBuilding.setUpdateUserId(Long.valueOf(0));
                tBuilding.setUpdateDate(serverDateTime);

                // T_BUILDING登録時のnull制約項目をセット
                MPrefecture mp = new MPrefecture();
                mp.setPrefectureCd(parentBuildingInfo.getMPrefecture().getPrefectureCd());
                mp.setPrefectureName(parentBuildingInfo.getMPrefecture().getPrefectureName());
                mp.setVersion(0);
                mp.setCreateUserId(0L);
                mp.setCreateDate(serverDateTime);
                mp.setUpdateUserId(0L);
                mp.setUpdateDate(serverDateTime);
                tBuilding.setMPrefecture(mp);
                tBuilding.setNyukyoTypeCd(parentBuildingInfo.getNyukyoTypeCd());
                // 0（使用しない）で登録
                tBuilding.setEstimateUse("0");
                tBuilding.setFreonDischargeOffice(parentBuildingInfo.getFreonDischargeOffice());
                tBuilding.setPublicFlg(parentBuildingInfo.getPublicFlg());
                // テナントとして登録
                tBuilding.setBuildingType("1");
                insertDB(tBuilding);

                // テナントユーザー情報
                MTenantSmPK mTenantSmsPk = new MTenantSmPK();
                mTenantSmsPk.setCorpId(target.getCorpId());
                // 建物テーブルに登録したBUILDING_IDを建物IDにセット
                mTenantSmsPk.setBuildingId(tbPk.getBuildingId());
                mTenantSms.setId(mTenantSmsPk);

                // 存在チェック
                MTenantSm mtsEntity = entityManager.find(MTenantSm.class, mTenantSms.getId());

                // 登録処理
                if (mtsEntity == null) {
                    mTenantSms.setTenantId(Long.valueOf(tenantId));
                    mTenantSms.setRecDate(serverDateTime);
                    mTenantSms.setRecMan("TgfuncRfmeterInfo");
                    mTenantSms.setDelFlg(OsolConstants.FLG_OFF);
                    mTenantSms.setCreateUserId(Long.valueOf(0));
                    mTenantSms.setCreateDate(serverDateTime);
                    mTenantSms.setUpdateUserId(Long.valueOf(0));
                    mTenantSms.setUpdateDate(serverDateTime);
                    insertDB(mTenantSms);
                }

                // メーター登録用
                mMeter.setRecDate(serverDateTime);
                mMeter.setRecMan("TgfuncRfmeterInfo");
                mMeter.setDelFlg(OsolConstants.FLG_OFF);
                mMeter.setCreateUserId(Long.valueOf(0));
                mMeter.setCreateDate(serverDateTime);
                mMeter.setUpdateUserId(Long.valueOf(0));
                mMeter.setUpdateDate(serverDateTime);
                insertDB(mMeter);

                // 建物、メーター関連
                TBuildDevMeterRelationPK tbdmrPk = new TBuildDevMeterRelationPK();
                tbdmrPk.setCorpId(target.getCorpId());
                // 新規採番した建物番号をセット
                tbdmrPk.setBuildingId(tBuilding.getId().getBuildingId());
                tbdmrPk.setDevId(tBuildDevMeterRelation.getId().getDevId());
                tbdmrPk.setMeterMngId(tBuildDevMeterRelation.getId().getMeterMngId());
                tBuildDevMeterRelation.setId(tbdmrPk);
                tBuildDevMeterRelation.setVersion(0);
                tBuildDevMeterRelation.setCreateUserId(Long.valueOf(0));
                tBuildDevMeterRelation.setCreateDate(serverDateTime);
                tBuildDevMeterRelation.setUpdateUserId(Long.valueOf(0));
                tBuildDevMeterRelation.setUpdateDate(serverDateTime);
                insertDB(tBuildDevMeterRelation);

                insFlg = true;
            }

            if (!insFlg) {
                // ※テナント（建物、装置関連DB）情報が取得できなかった場合でもメーター、検針データは登録する
                // 建物、メーター関連テーブルへの登録は不要
                // 装置マスタ（メーター登録用の外部キー）
                MDevPrm mdp = new MDevPrm();
                mdp.setDevId(mMeter.getId().getDevId());

                MDevPrm entity = entityManager.find(MDevPrm.class, mdp.getDevId());

                // 装置IDが登録されていない場合は登録
                if (entity == null) {
                    mdp.setDevKind("1");
                    mdp.setRecDate(serverDateTime);
                    mdp.setRecMan("TgfuncRfmeterInfo");
                    mdp.setDelFlg(OsolConstants.FLG_OFF);
                    mdp.setCreateUserId(Long.valueOf(0));
                    mdp.setCreateDate(serverDateTime);
                    mdp.setUpdateUserId(Long.valueOf(0));
                    mdp.setUpdateDate(serverDateTime);
                    insertDB(mdp);
                }

                // メーター登録用
                mMeter.setRecDate(serverDateTime);
                mMeter.setRecMan("TgfuncRfmeterInfo");
                mMeter.setDelFlg(OsolConstants.FLG_OFF);
                mMeter.setCreateUserId(Long.valueOf(0));
                mMeter.setCreateDate(serverDateTime);
                mMeter.setUpdateUserId(Long.valueOf(0));
                mMeter.setUpdateDate(serverDateTime);
                insertDB(mMeter);
            }

            // t_inspection_meter_bef
            // 無線種別による判定
            if ("1".equals(mMeter.getWirelessType()) || "3".equals(mMeter.getWirelessType())) {
                tInspectionMeterBef.setMeterType(2L);
            } else {
                tInspectionMeterBef.setMeterType(1L);
            }
        } else if (mmList != null && mmList.size() == 1 && tbCnt == 1) {
            // メータ登録用DBの件数と建物テナントDBの件数が同じ（1件）
            // t_inspection_meter_bef
            // 既存メーター登録用DBのメーター種別をセット
            tInspectionMeterBef.setMeterType(mmList.get(0).getMeterType());
        } else {
            // メータ登録用DBの件数と建物テナントDBの件数が同じ出ない場合はエラー文字列を返却
            return String.valueOf("【メーター】devId：" + mMeter.getId().getDevId() + ", meterMngId："
                    + mMeter.getId().getMeterMngId() + ", 【テナント】tbCnt：" + tbCnt);
        }

        // 確定前検針データ
        tInspectionMeterBef.setRecDate(serverDateTime);
        tInspectionMeterBef.setRecMan("TgfuncRfmeterInfo");
        tInspectionMeterBef.setCreateUserId(Long.valueOf(0));
        tInspectionMeterBef.setCreateDate(serverDateTime);
        tInspectionMeterBef.setUpdateUserId(Long.valueOf(0));
        tInspectionMeterBef.setUpdateDate(serverDateTime);
        insertDB(tInspectionMeterBef);

        if (!insFlg) {
            // メーター、検針データのみ登録時はログに出力しておく
            return String.valueOf("メーター、検針データのみ登録 " + "【メーター】devId：" + mMeter.getId().getDevId() + ", meterMngId："
                    + mMeter.getId().getMeterMngId());
        }

        return "";
    }

    /**
     * メータ登録用テーブル取得
     *
     * @param mMeter
     * @return メータ登録用リスト
     */
    private List<RFMeterInfoResultSetMMeter> getMMeterList(MMeter mMeter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RFMeterInfoResultSetMMeter> query = cb.createQuery(RFMeterInfoResultSetMMeter.class);
        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), mMeter.getId().getDevId()));
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.meterMngId), mMeter.getId().getMeterMngId()));

        query.select(cb.construct(RFMeterInfoResultSetMMeter.class, root.get(MMeter_.id).get(MMeterPK_.devId),
                root.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(MMeter_.meterType),
                root.get(MMeter_.wirelessType))).where(cb.and(conditionList.toArray(new Predicate[] {})));

        return this.entityManager.createQuery(query).getResultList();
    }

    public List<ImportCsvConcentratorInfoResultSet> getMMeterList(String devId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImportCsvConcentratorInfoResultSet> query = cb.createQuery(ImportCsvConcentratorInfoResultSet.class);
        Root<MMeter> root = query.from(MMeter.class);

        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(cb.equal(root.get(MMeter_.id).get(MMeterPK_.devId), devId));

        query.select(cb.construct(ImportCsvConcentratorInfoResultSet.class,
                root.get(MMeter_.id).get(MMeterPK_.meterMngId), root.get(MMeter_.multi))).where(cb.and(conditionList.toArray(new Predicate[] {})));

        return this.entityManager.createQuery(query).getResultList();
    }

    /**
     * 建物、装置、メーター関連情報取得
     * @param devId
     * @return 関連リスト
     */
    private List<RFMeterInfoResultSetRelations> getRelation(String devId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RFMeterInfoResultSetRelations> query = cb
                .createQuery(RFMeterInfoResultSetRelations.class);
        Root<MDevRelation> root = query.from(MDevRelation.class);
        Join<MDevRelation, MDevPrm> joinMDevPrm = root.join(MDevRelation_.MDevPrm, JoinType.INNER);

        // 結合条件
        joinMDevPrm.on(cb.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId),
                joinMDevPrm.get(MDevPrm_.devId)));

        // 取得条件
        List<Predicate> conditionList = new ArrayList<>();
        conditionList.add(cb.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.devId), devId));
        conditionList.add(cb.equal(joinMDevPrm.get(MDevPrm_.delFlg), OsolConstants.FLG_OFF));

        query.select(cb.construct(RFMeterInfoResultSetRelations.class,
                root.get(MDevRelation_.id).get(MDevRelationPK_.corpId),
                root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId)))
                .where(cb.and(conditionList.toArray(new Predicate[] {})));

        return this.entityManager.createQuery(query).getResultList();
    }

    /**
     * 建物テーブル件数取得
     *
     * @param tenantId
     * @param devId
     * @param mdrList
     * @return 建物件数テーブル
     */
    private Long getBuildingTenantCnt(String tenantId, List<RFMeterInfoResultSetRelations> mdrList) {

        Long cnt = 0L;

        // 取得した中間テーブル分ループ
        for (RFMeterInfoResultSetRelations target : mdrList) {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<TBuilding> root = query.from(TBuilding.class);
            Join<TBuilding, MTenantSm> joinMTenantSm = root.join(TBuilding_.MTenantSms, JoinType.INNER);

            // CSVのテナントIDと一致
            joinMTenantSm.on(cb.equal(joinMTenantSm.get(MTenantSm_.tenantId), Long.valueOf(tenantId)));

            List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(cb.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), target.getCorpId()));
            //所属建物で絞る
            conditionList.add(cb.equal(root.get(TBuilding_.divisionBuildingId), target.getBuildingId()));
            conditionList.add(cb.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));

            query.select(cb.construct(Long.class,
                    cb.count(root.get(TBuilding_.id).get(TBuildingPK_.corpId))))
                    .where(cb.and(conditionList.toArray(new Predicate[] {})));

            cnt += this.entityManager.createQuery(query).getSingleResult();
        }

        // ※建物に紐づくテナントが複数件存在した（cntが1でない）場合はデータ不整合でエラーになる
        return cnt;
    }

    /**
     * 登録処理
     *
     * @param entity
     */
    private void insertDB(Object entity) {

        entityManager.persist(entity);
        entityManager.flush();
        entityManager.clear();
    }

}
