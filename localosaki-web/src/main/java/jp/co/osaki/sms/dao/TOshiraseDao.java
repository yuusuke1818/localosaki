package jp.co.osaki.sms.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.entity.TOshirase;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.TOshiraseServiceDaoImpl;

/**
 *
 * @author h-shiba
 */
@Stateless
public class TOshiraseDao extends SmsDao {

    private final TOshiraseServiceDaoImpl impl;

    public TOshiraseDao() {
        impl = new TOshiraseServiceDaoImpl();
    }
    /**
     *
     * お知らせID採番
     *
     * @return 新規採番されたお知らせID
     */
    public Long createOshiraseId() {
        return super.createId(OsolConstants.ID_SEQUENCE_NAME.OSHIRASE_ID.getVal());
    }
    /**
     * メニューに表示するお知らせの取得
     * @param corp_id
     * @return
     */
    public List<TOshirase> getMenuDispOshiraseList(String corp_id) {
        // 企業ID
        List<Object> corpIdList = Arrays.asList(corp_id);

        // 配信先コード（全サービスまたは、SMS）
        List<Object> deliveryCdList = Arrays.asList(
                ApiGenericTypeConstants.OSHIRASE_DELIVERY_CD_138.ALL.getVal(),
                ApiGenericTypeConstants.OSHIRASE_DELIVERY_CD_138.SMS.getVal());

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(impl.CORP_ID_KEY, corpIdList);
        parameterMap.put(impl.DELIVERY_CD_KEY, deliveryCdList);
        List<TOshirase> tOshiraseList = getResultList(impl, parameterMap);

        return tOshiraseList;
    }
}
