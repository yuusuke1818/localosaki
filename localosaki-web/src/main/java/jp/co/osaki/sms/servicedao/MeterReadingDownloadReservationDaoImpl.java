package jp.co.osaki.sms.servicedao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jp.co.osaki.osol.entity.TMeterReadingDownloadReservationInfo;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class MeterReadingDownloadReservationDaoImpl implements BaseServiceDao<TMeterReadingDownloadReservationInfo> {

	@Override
	public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
		return 0;
	}

	@Override
	public List<TMeterReadingDownloadReservationInfo> getResultList(TMeterReadingDownloadReservationInfo target,
			EntityManager em) {
		return null;
	}

	@Override
	public List<TMeterReadingDownloadReservationInfo> getResultList(Map<String, List<Object>> parameterMap,
			EntityManager em) {
		// TODO 検索を作る
		List<Object> searchCorpIdList = parameterMap.get("searchCorpId");
		String corpId = searchCorpIdList.get(0).toString();

		List<Object> searchPersonIdList = parameterMap.get("searchPersonId");
		String personId = searchPersonIdList.get(0).toString();

		List<Object> searchProcessResultList = parameterMap.get("searchResult");

		StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" * ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" corp_id = :corpId ");
		builder.append(" AND ");
		builder.append(" person_id = :personId ");
		builder.append(" AND ");
		builder.append(" reservation_date >= :limitDate ");

		if(parameterMap.containsKey("searchResult")) {
			builder.append(" AND ");
			builder.append("  process_result = :incomplete ");
		}

		builder.append("ORDER BY");
		builder.append(" reservation_id DESC ");
		builder.append("LIMIT");
		builder.append(" :dispNum ");

		Query query = em.createNativeQuery(builder.toString());

		query.setParameter("corpId", corpId);
		query.setParameter("personId", personId);

		if(parameterMap.containsKey("searchResult")) {
			query.setParameter("incomplete", searchProcessResultList.get(0).toString());
		}


		// 3か月より前のデータは取得しない
		Timestamp now = new Timestamp((new Date()).getTime());
		Timestamp limitDate =  Timestamp.valueOf(now.toLocalDateTime().minusMonths(3));
//		Timestamp limitDate =  Timestamp.valueOf(now.toLocalDateTime().minusWeeks(1)); //テスト用
		query.setParameter("limitDate", limitDate);

		// 表示件数 最大10件
		int num = 10;
		query.setParameter("dispNum", num);

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

//	@Override
//	public Integer getIncompletedResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
//
//		List<Object> searchCorpIdList = parameterMap.get("searchCorpId");
//		String corpId = searchCorpIdList.get(0).toString();
//
//		List<Object> searchPersonIdList = parameterMap.get("searchPersonId");
//		String personId = searchPersonIdList.get(0).toString();
//
//		StringBuilder builder = new StringBuilder();
//		builder.append("SELECT");
//		builder.append(" * ");
//		builder.append("FROM");
//		builder.append(" t_meter_reading_download_reservation_info ");
//		builder.append("WHERE");
//		builder.append(" corp_id = :corpId ");
//		builder.append(" AND ");
//		builder.append(" person_id = :personId ");
//		builder.append(" AND ");
//		builder.append(" process_status = :status1 ");
//		builder.append(" AND ");
//		builder.append(" process_status = :status2 ");
//
//		Query query = em.createNativeQuery(builder.toString());
//
//		query.setParameter("corpId", corpId);
//		query.setParameter("personId", personId);
//		query.setParameter("status1", "処理待ち");
//		query.setParameter("status2", "処理中");
//
//		@SuppressWarnings("unchecked")
//		List<Object[]> resultRecords = query.getResultList();
//
//		return resultRecords.size();
//	}


	@Override
	public List<TMeterReadingDownloadReservationInfo> getResultList(
			List<TMeterReadingDownloadReservationInfo> entityList, EntityManager em) {
		return null;
	}

	@Override
	public List<TMeterReadingDownloadReservationInfo> getResultList(EntityManager em) {
		return null;
	}

	@Override
	public TMeterReadingDownloadReservationInfo find(TMeterReadingDownloadReservationInfo target, EntityManager em) {

		Long id = target.getReservationId();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT");
		builder.append(" output_file_path, ");
		builder.append(" output_file_name ");
		builder.append("FROM");
		builder.append(" t_meter_reading_download_reservation_info ");
		builder.append("WHERE");
		builder.append(" reservation_id = :rsvId");

		Query query = em.createNativeQuery(builder.toString());

		query.setParameter("rsvId", id);

		@SuppressWarnings("unchecked")
		List<Object[]> resultRecords = query.getResultList();

		List<TMeterReadingDownloadReservationInfo> records = new ArrayList<TMeterReadingDownloadReservationInfo>();
		for (Object[] obj : resultRecords) {
			TMeterReadingDownloadReservationInfo record = new TMeterReadingDownloadReservationInfo();
			record.setOutputFilePath(obj[0].toString());
			record.setOutputFileName(obj[1].toString());
			records.add(record);
		}

		return records.get(0);
	}

	@Override
	public void persist(TMeterReadingDownloadReservationInfo target, EntityManager em) {
		em.persist(target);
	}

	@Override
	public TMeterReadingDownloadReservationInfo merge(TMeterReadingDownloadReservationInfo target, EntityManager em) {
		return em.merge(target);
	}

	@Override
	public void remove(TMeterReadingDownloadReservationInfo target, EntityManager em) {
	}
}
