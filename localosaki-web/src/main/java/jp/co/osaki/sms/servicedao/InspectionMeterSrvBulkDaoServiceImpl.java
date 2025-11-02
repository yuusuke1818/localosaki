package jp.co.osaki.sms.servicedao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.resultset.InspectionMeterSrvBulkResultSet;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class InspectionMeterSrvBulkDaoServiceImpl implements BaseServiceDao<InspectionMeterSrvBulkResultSet> {

	protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

	@Override
	public List<InspectionMeterSrvBulkResultSet> getResultList( //
			Map<String, List<Object>> parameterMap, //
			EntityManager em) {
		return search(parameterMap, em);
	}

	private List<InspectionMeterSrvBulkResultSet> search(Map<String, List<Object>> parameterMap, EntityManager em) {

		eventLogger.info("****************************** search called.");
		eventLogger.info("****************************** param size:" + parameterMap.size());
		for (Entry<String, List<Object>> param : parameterMap.entrySet()) {
			eventLogger.info("****************************** param key:" + param.getKey());
			eventLogger.info("****************************** param value:" + param.getValue());
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
				builder.append("  building_or_tenant.building_id IN (SELECT building_id FROM t_building WHERE public_flg = '1' AND t_building.building_del_date is null AND t_building.del_flg = '0') ");





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
		List<Object> personAuths = (List<Object>)parameterMap.get("personAuths").get(0);

		// 企業参照範囲の指定
		if (OsolConstants.CORP_TYPE.OSAKI.getVal().equals(loginCorpType)) {

			eventLogger.info("****************************** 大崎企業");

			// 担当者
			if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType)) {

				eventLogger.info("****************************** 担当者権限");

				// 担当する企業、担当する建物が参照範囲
				builder.append("AND ");
				builder.append(" tbdmr.corp_id IN (SELECT corp_id FROM m_corp_person WHERE person_id = '" + loginPersonId + "' AND del_flg = '0' GROUP BY corp_id) ");
				}
		}
		// パートナー企業
		else if (OsolConstants.CORP_TYPE.PARTNER.getVal().equals(loginCorpType)) {

			eventLogger.info("------------------------------ パートナー企業");

			boolean isSmsAuth = false;
			boolean isOcrAuth = false;

			for (Object auth : personAuths) {

				eventLogger.debug("------------------------------ personAuth:" + String.valueOf(auth));

				// SMS権限保持
				if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(String.valueOf(auth))) {
					isSmsAuth = true;
				}

				// OCR権限保持
				if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(String.valueOf(auth))) {
					isOcrAuth = true;
				}
			}

			eventLogger.info("------------------------------ 管理者？:" + OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(loginPersonType));
			eventLogger.info("------------------------------ 担当者権限かつSMS権限保有？:" + (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType) && isSmsAuth));
			eventLogger.info("------------------------------ SMS権限保有？:" + isSmsAuth);
			eventLogger.info("------------------------------ OCR権限保有？:" + isOcrAuth);

			// 管理者権限
			if (OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(loginPersonType)) {

				eventLogger.info("-------------------------- 管理者なので参照企業で絞り込み");

				builder.append("AND ");
				builder.append(" tbdmr.corp_id IN (SELECT corp_id FROM m_corp_person WHERE person_corp_id = '" + loginCorpId + "' AND del_flg = '0' GROUP BY corp_id) ");
			}
			// 担当者権限かつSMS権限保有
			else if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType) && isSmsAuth) {

				eventLogger.info("-------------------------- 担当者なので参照企業、参照建物で絞り込み");

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

					eventLogger.info("-------------------------- さらにOCR権限保有につきOCから始まる機器IDで絞り込み");

					builder.append("AND ");
					builder.append(" tims.dev_id LIKE 'OC%' ");
				}
			}
		}
		// 契約企業
		else if (OsolConstants.CORP_TYPE.CONTRACT.getVal().equals(loginCorpType)) {

			eventLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 契約企業");

			builder.append("AND ");
			builder.append(" tbdmr.corp_id = :contractCorpId ");

			// 担当者
			if (OsolConstants.PERSON_TYPE.PERSON.getVal().equals(loginPersonType)) {

				eventLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 担当者権限");

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
		builder.append(" building_or_tenant.building_no ");

		Query query = em.createNativeQuery(builder.toString());

		eventLogger.info("****************************** 検索SQL:" + builder.toString());

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

		eventLogger.debug("検索開始:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		eventLogger.debug("検索終了:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

		eventLogger.debug("検索結果件数:" + resultRecords.size());

		List<InspectionMeterSrvBulkResultSet> records = resultRecords.stream() //
			.map(e -> new InspectionMeterSrvBulkResultSet(e)) //
			.collect(Collectors.toList());

		return 	records;
	}

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<InspectionMeterSrvBulkResultSet> getResultList(InspectionMeterSrvBulkResultSet target,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<InspectionMeterSrvBulkResultSet> getResultList(List<InspectionMeterSrvBulkResultSet> entityList,
			EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<InspectionMeterSrvBulkResultSet> getResultList(EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public InspectionMeterSrvBulkResultSet find(InspectionMeterSrvBulkResultSet target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void persist(InspectionMeterSrvBulkResultSet target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public InspectionMeterSrvBulkResultSet merge(InspectionMeterSrvBulkResultSet target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void remove(InspectionMeterSrvBulkResultSet target, EntityManager em) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
