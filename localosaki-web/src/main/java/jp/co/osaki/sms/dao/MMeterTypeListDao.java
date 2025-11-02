package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MMeterTypeServiceDaoImpl;

/**
 * メーター種別設定取得Daoクラス
 *
 * @author yonezawa.a
 */
@Stateless
public class MMeterTypeListDao extends SmsDao {

    private final MMeterTypeServiceDaoImpl mMeterTypeServiceDaoImpl;

    public MMeterTypeListDao() {
        mMeterTypeServiceDaoImpl = new MMeterTypeServiceDaoImpl();
    }

    /**
     * メーター種別設定取得Daoクラス
     *
     * @return
     */
    public List<MMeterType> getMeterTypeList(String corpId, Long buildingId) {
        Map<String, List<Object>> param = new HashMap<>();
        List<Object> corpIdList = new ArrayList<>();
        List<Object> buildingIdList = new ArrayList<>();
        corpIdList.add(corpId);
        buildingIdList.add(buildingId);
        param.put("corpId", corpIdList);
        param.put("buildingId", buildingIdList);

        return getResultList(mMeterTypeServiceDaoImpl, param);
    }
}
