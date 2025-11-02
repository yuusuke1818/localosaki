package jp.co.osaki.sms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MPersonSearchServiceDaoImpl;
import jp.co.osaki.sms.servicedao.MeterUserSearchServiceDaoImpl;

/**
 * メーターユーザーDaoクラス
 *
 * @author nishida.t
 */
@Stateless
public class SearchMPersonDao extends SmsDao {

    private final MeterUserSearchServiceDaoImpl meterUserSearchServiceDaoImpl;
    private final MPersonSearchServiceDaoImpl mPersonImpl;

    public SearchMPersonDao() {
        this.meterUserSearchServiceDaoImpl = new MeterUserSearchServiceDaoImpl();
        this.mPersonImpl = new MPersonSearchServiceDaoImpl();
    }

    /**
     * 担当者情報取得
     *
     * @param corpId 企業ID
     * @param personId 担当者ID
     * @return 担当者情報Bean
     */
    public MPerson find(String corpId, String personId) {

        MPerson mPerson = new MPerson();
        MPersonPK mPersonPk = new MPersonPK();
        mPersonPk.setCorpId(corpId);
        mPersonPk.setPersonId(personId);
        mPerson.setId(mPersonPk);
        return find(meterUserSearchServiceDaoImpl, mPerson);
    }

    /**
    *
    *  担当者検索
    *
    * @param targetPersonIdOrName  担当者ID or 担当者名
    * @param targetPersonKana      担当者カナ
    * @param targetDeptName        部署名
    * @param targetPositionName    役職名
    * @param targetAccountStatus   アカウント状態
    * @param corpIdList            企業ID
    * @param svDateList            登録日時
    * @return  検索結果
    */
   public List<MPerson> getSearchResultList(
           List<Object> targetPersonIdOrName,
           List<Object> targetPersonKana,
           List<Object> targetDeptName,
           List<Object> targetPositionName,
           List<Object> targetAccountStatus,
           List<Object> corpIdList,
           List<Object> svDateList) {

       Map<String, List<Object>> parameterMap = new HashMap<>();

       parameterMap.put("personIdOrName", targetPersonIdOrName);
       parameterMap.put("personKana", targetPersonKana);
       parameterMap.put("deptName", targetDeptName);
       parameterMap.put("positionName", targetPositionName);
       parameterMap.put("accountStatus", targetAccountStatus);
       parameterMap.put("corpId", corpIdList);
       parameterMap.put("svDate", svDateList);

       return getResultList(mPersonImpl, parameterMap);
   }
}
