package jp.co.osaki.sms.batch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr;
import jp.co.osaki.osol.entity.TInspectionMeterSvrPK_;
import jp.co.osaki.osol.entity.TInspectionMeterSvr_;
import jp.co.osaki.osol.entity.TMeterReadingDownloadReservationInfo;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.batch.SmsBatchDao;
import jp.co.osaki.sms.batch.dto.InspectionMeterSrvBulkResultSet;

public class MeterReadingDownloadReservationDao extends SmsBatchDao {

	public MeterReadingDownloadReservationDao(EntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * 処理対象の検針ダウンロード予約情報を取得
	 */
	public List<TMeterReadingDownloadReservationInfo> getMeterReadingDownloadReservationInfoList() {

		String targetResult = "未完了";

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" * ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" process_result = :processResult");

		Query query = entityManager.createNativeQuery(builder.toString());

		query.setParameter("processResult", targetResult);

		entityManager.clear();
		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		List<TMeterReadingDownloadReservationInfo> records = new ArrayList<TMeterReadingDownloadReservationInfo>();

		for (Object[] obj : resultRecords) {
			TMeterReadingDownloadReservationInfo record = new TMeterReadingDownloadReservationInfo();
			record.setReservationId(Long.parseLong(String.valueOf(obj[0])));
			record.setCorpId((String)obj[1]);
			record.setPersonId((String)obj[2]);
			record.setReservationDate((Timestamp)obj[3]);
			record.setSearchCondition((String)obj[4]);
			record.setProcessStatus((String)obj[5]);
			record.setProcessResult((String)obj[6]);
			record.setStartDate((Timestamp)obj[7]);
			record.setEndDate((Timestamp)obj[8]);
			record.setOutputFilePath((String)obj[9]);
			record.setOutputFileName((String)obj[10]);
			record.setDelFlg((Integer)obj[11]);
			record.setVersion((Integer)obj[12]);
			record.setCreateUserId(Long.parseLong(String.valueOf(obj[13])));
			record.setCreateDate((Timestamp)obj[14]);
			record.setUpdateUserId(Long.parseLong(String.valueOf(obj[15])));
			record.setUpdateDate((Timestamp)obj[16]);

			records.add(record);
		}

		return records;
	}

	/**
	 * 検針ダウンロード予約情報テーブルを更新。
	 */

    public void updateMeterReadingDownloadReservationInfo(TMeterReadingDownloadReservationInfo reservationInfo) {

        entityManager.merge(reservationInfo);
        entityManager.flush();
        entityManager.clear();
    }



    /**
     * DB更新：処理開始時
     * @param reservationId
     * @param startDate
     * @param processStatus
     */
    public void updateDB(Long reservationId, Timestamp startDate, String processStatus) {

    	StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" * ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" reservation_id = :reservationId");

		Query query = entityManager.createNativeQuery(builder.toString());

		query.setParameter("reservationId", BigInteger.valueOf(reservationId));

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		TMeterReadingDownloadReservationInfo record = putInEntityClass(resultRecords).get(0);

		record.setProcessStatus(processStatus);
		record.setStartDate(startDate);

        entityManager.merge(record);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * DB更新：処理成功時
     * @param reservationId
     * @param endDate
     * @param filePath
     * @param fileName
     * @param processStatus
     * @param processResult
     */
    public void updateDB(Long reservationId, Timestamp endDate, String filePath, String fileName, String processStatus, String processResult) {


    	StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" * ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" reservation_id = :reservationId");

		Query query = entityManager.createNativeQuery(builder.toString());

		query.setParameter("reservationId", BigInteger.valueOf(reservationId));

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		TMeterReadingDownloadReservationInfo record = putInEntityClass(resultRecords).get(0);

		record.setEndDate(endDate);
		record.setOutputFilePath(filePath);
		record.setOutputFileName(fileName);
		record.setProcessStatus(processStatus);
		record.setProcessResult(processResult);
		batchLogger.debug("DB登録前"+ record);

        entityManager.merge(record);
        entityManager.flush();
        entityManager.clear();
    }

    /**
     *DB更新：処理失敗時
     * @param reservationId
     * @param endDate
     * @param processStatus
     * @param processResult
     */
    public void updateDB(Long reservationId, Timestamp endDate, String processStatus, String processResult) {
    	StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" * ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" reservation_id = :reservationId");

		Query query = entityManager.createNativeQuery(builder.toString());

		query.setParameter("reservationId", BigInteger.valueOf(reservationId));

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		TMeterReadingDownloadReservationInfo record = putInEntityClass(resultRecords).get(0);

		record.setEndDate(endDate);
		record.setProcessStatus(processStatus);
		record.setProcessResult(processResult);

        entityManager.merge(record);
        entityManager.flush();
        entityManager.clear();

    }




	/**
	 * ファイルに出力するベース内容を取得。
	 */
	public List<InspectionMeterSrvBulkResultSet> meterReadingDataSearch(Map<String, List<Object>> parameterMap) {

		batchLogger.info("****************************** search called.");
		batchLogger.info("****************************** param size:" + parameterMap.size());
		for (Entry<String, List<Object>> param : parameterMap.entrySet()) {
			batchLogger.info("****************************** param key:" + param.getKey());
			batchLogger.info("****************************** param value:" + param.getValue());
		}

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");
		builder.append(" tbdmr.corp_id AS corp_id, ");                                    // 0  企業ID
		builder.append(" mc.corp_name AS corp_name, ");                                   // 1  企業名
		builder.append(" building_or_tenant.building_id AS building_id, ");               // 2  建物ID
		builder.append(" building_or_tenant.building_no AS building_no, ");               // 3  建物番号
		builder.append(" building_or_tenant.building_name AS building_name, ");           // 4  建物名
		builder.append(" building_or_tenant.building_address AS building_address, ");     // 5  建物住所
		builder.append(" building_or_tenant.prefecture_cd AS prefecture_cd, ");           // 6  都道府県コード
		builder.append(" mp.prefecture_name AS prefecture_name, ");                       // 7  都道府県名
		builder.append(" building_or_tenant.tenant_id AS tenant_id, ");                   // 8  テナントID
		builder.append(" building_or_tenant.tenant_building_no AS tenant_building_no, "); // 9  テナント建物番号
		builder.append(" building_or_tenant.tenant_name AS tenant_name, ");               // 10 テナント名
		builder.append(" mts.tenant_id AS user_cd, ");                                    // 11 ユーザーコード
		builder.append(" building_or_tenant.building_type AS building_type, ");           // 12 建物種別
		builder.append(" tims.dev_id AS dev_id, ");                                       // 13 機器ID
		builder.append(" mdp.name AS dev_name, ");                                        // 14 機器名
		builder.append(" tims.meter_mng_id AS meter_mng_id, ");                           // 15 メーター管理番号
		builder.append(" mm.meter_type AS meter_type, ");                                 // 16 メーター種別
		builder.append(" mmt.meter_type_name AS meter_type_name, ");                      // 17 メーター種別名
		builder.append(" tims.insp_year AS insp_year, ");                                 // 18 検針年
		builder.append(" tims.insp_month AS insp_month, ");                               // 19 検針月
		builder.append(" tims.insp_month_no AS insp_month_no, ");                         // 20 月検針連番
		builder.append(" tims.insp_type AS insp_type, ");                                 // 21 検針種別
		builder.append(" tims.latest_insp_val AS latest_insp_val, ");                     // 22 最新検針値
		builder.append(" tims.prev_insp_val AS prev_insp_val, ");                         // 23 前回検針値
		builder.append(" tims.multiple_rate AS multiple_rate, ");                         // 24 乗率
		builder.append(" tims.latest_use_val AS latest_use_val, ");                       // 25 今回使用量
		builder.append(" tims.prev_use_val AS prev_use_val, ");                           // 26 前回使用量
		builder.append(" tims.use_per_rate AS use_per_rate, ");                           // 27 使用量率
		builder.append(" tims.latest_insp_date AS latest_insp_date, ");                   // 28 最新検針日時
		builder.append(" tims.prev_insp_date AS prev_insp_date, ");                       // 29 前回検針日時
		builder.append(" tims.end_flg AS end_flg ");                                      // 30 処理終了フラグ
		builder.append("FROM ( ");
		builder.append(" SELECT ");
		builder.append("  this_insp.dev_id, ");
		builder.append("  this_insp.meter_mng_id, ");
		builder.append("  this_insp.insp_year, ");
		builder.append("  this_insp.insp_month, ");
		builder.append("  this_insp.insp_month_no, ");
		builder.append("  this_insp.insp_type, ");
		builder.append("  this_insp.latest_insp_val, ");
		builder.append("  this_insp.prev_insp_val, ");
		builder.append("  this_insp.multiple_rate, ");
		builder.append("  this_insp.latest_use_val, ");
		builder.append("  this_insp.prev_use_val, ");
		builder.append("  this_insp.use_per_rate, ");
		builder.append("  this_insp.latest_insp_date, ");
		builder.append("  this_insp.prev_insp_date, ");
		builder.append("  this_insp.end_flg ");
		builder.append(" FROM ");
		builder.append("  t_inspection_meter_svr this_insp ");
		builder.append(" WHERE ");
		builder.append("  ( this_insp.latest_insp_date >= :startDate AND this_insp.latest_insp_date <= :endDate )"); // 期間指定
		builder.append(" ) AS tims ");
		builder.append(" LEFT JOIN t_build_dev_meter_relation tbdmr ");
		builder.append("  ON tims.dev_id = tbdmr.dev_id ");
		builder.append("  AND tims.meter_mng_id = tbdmr.meter_mng_id ");
		builder.append(" INNER JOIN ( ");
		builder.append("  SELECT ");
		builder.append("   tbdmr.dev_id, ");
		builder.append("   tbdmr.meter_mng_id, ");
		builder.append("   tbdmr.corp_id, ");
		builder.append("   building_or_tenant.prefecture_cd AS prefecture_cd, ");
		builder.append("   building_or_tenant.building_id AS building_or_tenant_id, ");
		builder.append("   building_or_tenant.building_type AS building_type, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '0' THEN building_or_tenant.building_id ");
		builder.append("        WHEN building_or_tenant.building_type = '1' THEN building.building_id ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS building_id, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '0' THEN building_or_tenant.building_no ");
		builder.append("        WHEN building_or_tenant.building_type = '1' THEN building.building_no ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS building_no, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '0' THEN null ");
		builder.append("        WHEN building_or_tenant.building_type = '1' THEN building_or_tenant.building_no ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS tenant_building_no, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '0' THEN building_or_tenant.building_name ");
		builder.append("        WHEN building_or_tenant.building_type = '1' THEN building.building_name ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS building_name, ");
		builder.append("   building.address as building_address, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '1' THEN building_or_tenant.building_id ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS tenant_id, ");
		builder.append("   CASE WHEN building_or_tenant.building_type = '1' THEN building_or_tenant.building_name ");
		builder.append("        ELSE NULL ");
		builder.append("   END AS tenant_name ");
		builder.append("  FROM ");
		builder.append("   t_build_dev_meter_relation tbdmr ");
		builder.append("   INNER JOIN t_building building_or_tenant ");
		builder.append("    ON tbdmr.corp_id = building_or_tenant.corp_id ");
		builder.append("    AND tbdmr.building_id = building_or_tenant.building_id ");
		builder.append("   LEFT JOIN t_building building ");
		builder.append("    ON building_or_tenant.division_building_id = building.building_id ");
		builder.append("  WHERE ");
		builder.append("   (tbdmr.dev_id, tbdmr.meter_mng_id, tbdmr.corp_id, tbdmr.building_id ) NOT IN ( ");
		builder.append("    SELECT ");
		builder.append("     tbdmr.dev_id, ");
		builder.append("     tbdmr.meter_mng_id, ");
		builder.append("     tbdmr.corp_id, ");
		builder.append("     tb.division_building_id ");
		builder.append("    FROM ");
		builder.append("     t_build_dev_meter_relation tbdmr ");
		builder.append("     INNER JOIN t_building tb ");
		builder.append("      ON tb.corp_id = tbdmr.corp_id ");
		builder.append("      AND tb.building_id = tbdmr.building_id ");
		builder.append("    WHERE ");
		builder.append("     tb.building_type = '1' ");
		builder.append("   ) ");
		builder.append(" ) AS building_or_tenant ");
		builder.append("  ON tbdmr.corp_id = building_or_tenant.corp_id ");
		builder.append("  AND tbdmr.building_id = building_or_tenant.building_or_tenant_id ");
		builder.append("  AND tims.dev_id = building_or_tenant.dev_id ");
		builder.append("  AND tims.meter_mng_id = building_or_tenant.meter_mng_id ");
		builder.append(" LEFT JOIN m_corp mc ");
		builder.append("  ON tbdmr.corp_id = mc.corp_id ");
		builder.append(" LEFT JOIN m_prefecture mp ");
		builder.append("  ON building_or_tenant.prefecture_cd = mp.prefecture_cd ");
		builder.append(" LEFT JOIN m_tenant_sms mts ");
		builder.append("  ON mts.corp_id = tbdmr.corp_id ");
		builder.append("  AND mts.building_id = building_or_tenant.tenant_id ");
		builder.append(" LEFT JOIN m_dev_prm mdp ");
		builder.append("  ON mdp.dev_id = tims.dev_id ");
		builder.append(" INNER JOIN m_meter mm ");
		builder.append("  ON mm.dev_id = tims.dev_id ");
		builder.append("  AND mm.meter_mng_id = tims.meter_mng_id ");
		builder.append(" LEFT JOIN m_meter_type mmt ");
		builder.append("  ON mm.meter_type = mmt.meter_type ");
		builder.append("  AND tbdmr.corp_id = mmt.corp_id ");
		builder.append("  AND building_or_tenant.building_id = mmt.building_id ");
		builder.append("  AND mmt.menu_no IN (0, 1) ");
		builder.append("WHERE ");

		// 検針種別の指定が無い場合
		if (parameterMap.get("inspTypes").isEmpty()) {
			builder.append(" tims.insp_type IS NULL ");
		}
		// 検針種別の指定がある場合
		else {

			builder.append(" tims.insp_type IN (");
			builder.append("'" + (String)parameterMap.get("inspTypes").get(0) + "'");

			if (parameterMap.get("inspTypes").size() > 1) {

				for (int i = 1; i < parameterMap.get("inspTypes").size(); i++) {
					builder.append(", ");
					builder.append("'" + (String)parameterMap.get("inspTypes").get(i) + "'");
				}
			}

			builder.append(" ) ");
		}

		// 建物の公開フラグが１かつ建物削除日がNULLかつ削除フラグが０
		builder.append(" AND ");
		builder.append("  building_or_tenant.building_id IN (SELECT building_id FROM t_building WHERE public_flg = '1' AND  t_building.building_del_date is null AND t_building.del_flg = '0' ) ");


		// 以降、権限による参照範囲の絞り込み

		// 企業ID
		String loginCorpId = String.valueOf(parameterMap.get("loginCorpId").get(0));
		// 担当者ID
		String loginPersonId = String.valueOf(parameterMap.get("loginPersonId").get(0));
        // 企業種別
        // 0:大崎、1:パートナー企業、3:契約企業
		String loginCorpType = String.valueOf(parameterMap.get("loginCorpType").get(0));
        // 担当者種別
        // 0:管理者、1:担当者
		String loginPersonType = String.valueOf(parameterMap.get("loginPersonType").get(0));
        // 担当者権限
        // SMS権限:012、OCR権限:013
		List<Object> personAuths = parameterMap.get("personAuths");

		// 企業参照範囲の指定
		if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(loginCorpType)) {

			batchLogger.info("****************************** 大崎企業");

			// 担当者
			if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType)) {

				batchLogger.info("****************************** 担当者権限");

				// 担当する企業、担当する建物が参照範囲
				builder.append("AND ");
				builder.append(" tbdmr.corp_id IN (SELECT corp_id FROM m_corp_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY corp_id) ");
			}
		}
		// パートナー企業
		else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(loginCorpType)) {

			batchLogger.info("------------------------------ パートナー企業");

			boolean isSmsAuth = false;
			boolean isOcrAuth = false;

			for (Object auth : personAuths) {

				batchLogger.debug("------------------------------ personAuth:" + String.valueOf(auth));

				// SMS権限保持
				if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(String.valueOf(auth))) {
					isSmsAuth = true;
				}

				// OCR権限保持
				if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(String.valueOf(auth))) {
					isOcrAuth = true;
				}
			}

			batchLogger.info("------------------------------ 管理者？:" + OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(loginPersonType));
			batchLogger.info("------------------------------ 担当者権限かつSMS権限保有？:" + (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType) && isSmsAuth));
			batchLogger.info("------------------------------ SMS権限保有？:" + isSmsAuth);
			batchLogger.info("------------------------------ OCR権限保有？:" + isOcrAuth);

			// 管理者権限
			if (OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(loginPersonType)) {

				batchLogger.info("-------------------------- 管理者なので参照企業で絞り込み");

				builder.append("AND ");
				builder.append(" tbdmr.corp_id IN (SELECT corp_id FROM m_corp_person WHERE person_corp_id = '" + loginCorpId + "' AND del_flg = '0' GROUP BY corp_id) ");
			}
			// 担当者権限かつSMS権限保有
			// 企業担当も建物担当も全部こっち！！！！
			else if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType) && isSmsAuth) {

				batchLogger.info("-------------------------- 担当者なので参照企業、参照建物で絞り込み");

				// 担当する企業、担当する建物が参照範囲
				builder.append("AND ");
				builder.append(" ( ");
				builder.append("  tbdmr.corp_id IN (SELECT corp_id FROM m_corp_person WHERE person_id = '" + loginPersonId + "' AND authority_type = '0' AND del_flg = '0' GROUP BY corp_id) ");
				builder.append("  OR ");
				builder.append("  ( ");
				builder.append("   building_or_tenant.building_or_tenant_id IN (SELECT building_id FROM t_building_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY building_id) ");
				builder.append("   OR ");
				builder.append("   building_or_tenant.building_id IN (SELECT building_id FROM t_building_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY building_id) ");
				builder.append("  ) ");
				builder.append(" ) ");

				// さらにOCR権限保有
				if (isOcrAuth) {

					batchLogger.info("-------------------------- さらにOCR権限保有につきOCから始まる機器IDで絞り込み");

					builder.append("AND ");
					builder.append(" tims.dev_id LIKE 'OC%' ");
				}
			}
		}
		// 契約企業
		else if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(loginCorpType)) {

			batchLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 契約企業");

			builder.append("AND ");
			builder.append(" tbdmr.corp_id = :contractCorpId ");

			// 担当者
			if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType)) {

				batchLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 担当者権限");

				// 自企業の担当する建物が参照範囲
				builder.append("AND ");
				builder.append(" ( ");
				builder.append("  building_or_tenant.building_or_tenant_id IN (SELECT building_id FROM t_building_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY building_id) ");
				builder.append(" OR ");
				builder.append("  building_or_tenant.building_id IN (SELECT building_id FROM t_building_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY building_id) ");
				builder.append(" ) ");
			}
		}

		// 企業、建物が指定されている場合
		if (parameterMap.get("corpParam") != null || parameterMap.get("buildingParam") != null) {

			builder.append("AND ");

			// 建物指定
			if (parameterMap.get("buildingParam") != null) {

				builder.append("( ");

				builder.append(" ( building_or_tenant.building_no LIKE :buildingParam0 OR building_or_tenant.building_name LIKE :buildingParam0 ) ");

				for (int i = 1; i < parameterMap.get("buildingParam").size(); i++) {

					builder.append("OR ");
					builder.append(" ( building_or_tenant.building_no LIKE :buildingParam" + i + " OR building_or_tenant.building_name LIKE :buildingParam"+ i +" ) ");
				}

				builder.append(") ");

				if (parameterMap.get("corpParam") != null) {
					builder.append("AND ");
				}
			}

			// 企業指定
			if (parameterMap.get("corpParam") != null) {
				builder.append(" ( tbdmr.corp_id LIKE :corpParam OR mc.corp_name LIKE :corpParam ) ");
			}
		}

		builder.append("ORDER BY ");
		builder.append(" cast(insp_year as integer), ");
		builder.append(" cast(insp_month_no as integer), ");
		builder.append(" insp_month_no, ");
		builder.append(" insp_type, ");
		builder.append(" UPPER(tbdmr.corp_id), ");
		builder.append(" building_or_tenant.building_no, ");
		builder.append(" mdp.name, ");
		builder.append(" tims.meter_mng_id ");

		Query query = entityManager.createNativeQuery(builder.toString());

		batchLogger.info("****************************** 検索SQL:" + builder.toString());

		query.setParameter("startDate", DateUtility.conversionDate((String)parameterMap.get("startDate").get(0)  + " 00:00:00", DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH), TemporalType.TIMESTAMP);
		query.setParameter("endDate", DateUtility.conversionDate((String)parameterMap.get("endDate").get(0)  + " 23:59:59", DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH), TemporalType.TIMESTAMP);

		if (parameterMap.get("corpParam") != null) {
			query.setParameter("corpParam", "%" + String.valueOf(parameterMap.get("corpParam").get(0))  + "%");
		}

		if (parameterMap.get("buildingParam") != null) {

			for (int i = 0; i < parameterMap.get("buildingParam").size(); i++) {
				query.setParameter("buildingParam" + i, "%" + String.valueOf(parameterMap.get("buildingParam").get(i)) + "%");
			}
		}

		if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(loginCorpType)) {
			query.setParameter("contractCorpId", loginCorpId);
		}

		batchLogger.debug("検索開始:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		batchLogger.debug("検索終了:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

		batchLogger.debug("検索結果件数:" + resultRecords.size());

		List<InspectionMeterSrvBulkResultSet> records = resultRecords.stream() //
			.map(e -> new InspectionMeterSrvBulkResultSet(e)) //
			.collect(Collectors.toList());

		return 	records;
	}

	/**
	 * 最大デマンド値を取得。
	 */
	public TDayLoadSurvey getMaxDemand(Map<String, List<Object>> parameterMap) {

		String devId = String.valueOf(parameterMap.get("devId").get(0));
		Long meterMngId = Long.parseLong(String.valueOf(parameterMap.get("meterMngId").get(0)));
		String fromGetDate = String.valueOf(parameterMap.get("fromGetDate").get(0));
		String toGetDate = String.valueOf(parameterMap.get("toGetDate").get(0));

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);

        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), devId));
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), meterMngId));
        whereList.add(builder.greaterThanOrEqualTo(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), fromGetDate)); // from
        whereList.add(builder.lessThanOrEqualTo(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate), toGetDate)); // to

        query = query //
        		.select( //
        			builder.max(root.get(TDayLoadSurvey_.kwh30))) //
        		.where(builder.and(whereList.toArray(new Predicate[] {})));

        BigDecimal maxKWh30 = entityManager.createQuery(query).getSingleResult();

        if (maxKWh30 != null) {

            TDayLoadSurveyPK id = new TDayLoadSurveyPK();
            id.setDevId(devId);
            id.setMeterMngId(meterMngId);

            TDayLoadSurvey tDayLoadSurvey = new TDayLoadSurvey();
            tDayLoadSurvey.setId(id);
            tDayLoadSurvey.setKwh30(maxKWh30);

            return tDayLoadSurvey;
        }

        return null;
	}

	/**
	 * 前年の検針データを取得。
	 */
	public TInspectionMeterSvr getPrevYearMeterReading(Map<String, List<Object>> parameterMap) {

		String devId = String.valueOf(parameterMap.get("devId").get(0));
		Long meterMngId = Long.parseLong(String.valueOf(parameterMap.get("meterMngId").get(0)));
		String inspYear = String.valueOf(parameterMap.get("inspYear").get(0));
		String inspMonth = String.valueOf(parameterMap.get("inspMonth").get(0));
		String inspType = String.valueOf(parameterMap.get("inspType").get(0));

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TInspectionMeterSvr> query = builder.createQuery(TInspectionMeterSvr.class);

        Root<TInspectionMeterSvr> root = query.from(TInspectionMeterSvr.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.devId), devId));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.meterMngId), meterMngId));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspYear), inspYear));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.id).get(TInspectionMeterSvrPK_.inspMonth), inspMonth));
        whereList.add(builder.equal(root.get(TInspectionMeterSvr_.inspType), inspType));
        whereList.add(root.get(TInspectionMeterSvr_.inspType).in(Arrays.asList("a", "r")));

        query = query //
        		.select(root) //
        		.where(builder.and(whereList.toArray(new Predicate[] {})));

        List<TInspectionMeterSvr> tInspectionMeterSvrs = entityManager.createQuery(query).getResultList();

    	if (tInspectionMeterSvrs != null && !tInspectionMeterSvrs.isEmpty()) {

    		if (tInspectionMeterSvrs.size() == 1) {
    			return tInspectionMeterSvrs.get(0);
    		}
    	}

        return null;
	}


	/**
	 * DBから取得したObjectをEntityにセットする
	 */
	public List<TMeterReadingDownloadReservationInfo> putInEntityClass (List<Object[]> resultRecords) {

		List<TMeterReadingDownloadReservationInfo> records = new ArrayList<TMeterReadingDownloadReservationInfo>();

		for (Object[] obj : resultRecords) {
			TMeterReadingDownloadReservationInfo record = new TMeterReadingDownloadReservationInfo();
			record.setReservationId(Long.parseLong(String.valueOf(obj[0])));
			record.setCorpId((String)obj[1]);
			record.setPersonId((String)obj[2]);
			record.setReservationDate((Timestamp)obj[3]);
			record.setSearchCondition((String)obj[4]);
			record.setProcessStatus((String)obj[5]);
			record.setProcessResult((String)obj[6]);
			record.setStartDate((Timestamp)obj[7]);
			record.setEndDate((Timestamp)obj[8]);
			record.setOutputFilePath((String)obj[9]);
			record.setOutputFileName((String)obj[10]);
			record.setDelFlg((Integer)obj[11]);
			record.setVersion((Integer)obj[12]);
			record.setCreateUserId(Long.parseLong(String.valueOf(obj[13])));
			record.setCreateDate((Timestamp)obj[14]);
			record.setUpdateUserId(Long.parseLong(String.valueOf(obj[15])));
			record.setUpdateDate((Timestamp)obj[16]);

			records.add(record);
		}

		return records;
	}





}
