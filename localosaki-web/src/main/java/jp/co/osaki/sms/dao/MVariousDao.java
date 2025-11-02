package jp.co.osaki.sms.dao;

import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.MVariousPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MVariousServiceDaoImpl;

/**
 * 各種設定情報Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class MVariousDao extends SmsDao {
    /** 規定値 年報取得開始月. */
    private static final int YEAR_START_MONTH_DEFAULT_VALUE = 4;

    private final MVariousServiceDaoImpl daoImpl;

    public MVariousDao() {
        daoImpl = new MVariousServiceDaoImpl();
    }

    /**
     * 各種設定情報取得.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 各種設定情報
     */
    public MVarious getVariousInfo(String corpId, Long buildingId) {
        MVariousPK targetPk = new MVariousPK();
        targetPk.setCorpId(corpId);
        targetPk.setBuildingId(buildingId);
        MVarious target = new MVarious();
        target.setId(targetPk); // 更新条件

        return find(daoImpl, target);
    }

    /**
     * 基準日付直近の年報年度開始年月日取得.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param baseDate 基準日付
     * @return 基準日付直近の年報年度開始年月日
     */
    public Date getYearStartDate(String corpId, Long buildingId, Date baseDate) {
        // 年報年度開始月
        int yearStartMonth = getYearStartMonth(corpId, buildingId);

        // 基準日付の月初日
        Date baseDateFirstDay = DateUtility.changeDateMonthFirst(baseDate);

        String baseYear = DateUtility.changeDateFormat(baseDate, DateUtility.DATE_FORMAT_YYYY);
        Date yearStartDate = DateUtility.conversionDate(baseYear + String.format("%02d", yearStartMonth) + "01",
                DateUtility.DATE_FORMAT_YYYYMMDD);

        if (baseDateFirstDay.compareTo(yearStartDate) < 0) {
            // 対象月が年報取得開始月より前の場合は前年度
            yearStartDate = DateUtility.plusYear(yearStartDate, -1);
        }

        return yearStartDate;
    }

    /**
     * 年報年度開始月取得.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 年報年度開始月
     */
    public int getYearStartMonth(String corpId, Long buildingId) {
        int yearStartMonth = YEAR_START_MONTH_DEFAULT_VALUE;

        MVarious variousInfo = getVariousInfo(corpId, buildingId);
        if (variousInfo == null) {
            return yearStartMonth;
        }

        BigDecimal yearCloseMonthBd = variousInfo.getYearCloseMonth();
        if (yearCloseMonthBd == null) {
            return yearStartMonth;
        }

        // 年報締め月
        int yearCloseMonth = yearCloseMonthBd.intValue();

        // 年報取得開始月 = 年報締め月の翌月
        if (yearCloseMonth == 12) {
            yearStartMonth = 1;
        } else {
            yearStartMonth = yearCloseMonth + 1;
        }

        return yearStartMonth;
    }
}
