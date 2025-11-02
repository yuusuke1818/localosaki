package jp.co.osaki.sms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MPersonServiceDaoImpl;

/**
 * 担当者Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class MPersonDao extends SmsDao {

    private final MPersonServiceDaoImpl mpersonImpl;

    public MPersonDao() {
        this.mpersonImpl = new MPersonServiceDaoImpl();
    }

    /**
     * 担当者情報取得
     *
     * @param corp_id 企業ID(第一キー)
     * @param personId 担当者ID(第二キー)
     * @return 担当者情報Bean
     */
    public MPerson find(String corp_id, String personId) {

        // 担当者情報キーBeanにキーをセットする
        MPerson mp = new MPerson();
        MPersonPK mpk = new MPersonPK();
        mpk.setCorpId(corp_id);
        mpk.setPersonId(personId);
        mp.setId(mpk);
        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        MPerson result = find(mpersonImpl, mp);

        return result;

    }

    /**
     * <p>
     * 担当者情報 新規登録
     * </p>
     *
     * @param targetBean 担当者情報Bean
     */
    public void register(MPerson targetBean) {

        persist(mpersonImpl, targetBean);

    }

    /**
     * <p>
     * 担当者情報 更新処理
     * </p>
     *
     * @param target 担当者情報
     * @return 更新結果()
     */
    public MPerson merge(MPerson target) {

        MPerson result = merge(mpersonImpl, target);

        return result;
    }

    /**
     *
     * 担当者情報 検索
     *
     * @return 担当者List
     */
    public List<MPerson> searchPersonAll() {

        return getResultList(mpersonImpl);
    }

    /**
     *
     * 担当者情報 検索
     * <p>
     * 担当者情報の検索を行う<br>
     * 部分検索になる<br>
     * </p>
     * TODO ほか画面で担当者検索がある場合は条件を追加すればいいかも
     *
     * @param target
     * @return 担当者情報BeanList
     */
    public List<MPerson> searchPerson(MPerson target) {

        // 担当者情報キーBeanにキーをセットする
        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        List<MPerson> result;
        result = getResultList(mpersonImpl, target);

        return result;
    }

    // TODO 仮対応中
    public List<MPerson> getSearchResultList(
            List<Object> targetPersonId,
            List<Object> targetPersonKana,
            List<Object> targetPersonName,
            List<Object> targetDeptName,
            List<Object> targetPositionName,
            List<Object> targetAccountStatus) {

        Map<String, List<Object>> parameterMap = new HashMap<>();

        parameterMap.put("personId", targetPersonId);
        parameterMap.put("personKana", targetPersonKana);
        parameterMap.put("personName", targetPersonName);
        parameterMap.put("deptName", targetDeptName);
        parameterMap.put("positionName", targetPositionName);
        parameterMap.put("accountStatus", targetAccountStatus);

        return getResultList(mpersonImpl, parameterMap);
    }

    /**
     *
     * @param corpId
     * @param personId
     * @param mCorpPersonAuthList
     * @param mCorpPersonList
     * @param tBuildingPersonList
     */
    public void setLoginUserInfo(String corpId, String personId,
            List<MCorpPersonAuth> mCorpPersonAuthList,
            List<MCorpPerson> mCorpPersonList,
            List<TBuildingPerson> tBuildingPersonList){

        MPerson mPerson = new MPerson();
        MPersonPK id = new MPersonPK();
        id.setCorpId(corpId);
        id.setPersonId(personId);
        mPerson.setId(id);
        mPerson = this.find(mpersonImpl, mPerson);

        mCorpPersonAuthList.clear();
        mCorpPersonList.clear();
        tBuildingPersonList.clear();

        for (MCorpPerson mCorpPerson : mPerson.getMCorpPersons()){
            if(Objects.equals(mCorpPerson.getDelFlg(), OsolConstants.FLG_ON)){
                continue;
            }
            mCorpPersonList.add(mCorpPerson);
            for (MCorpPersonAuth mCorpPersonAuth : mCorpPerson.getMCorpPersonAuths()){
                if(Objects.equals(mCorpPersonAuth.getDelFlg(), OsolConstants.FLG_ON)){
                    continue;
                }
                mCorpPersonAuthList.add(mCorpPersonAuth);
            }
        }
        for (TBuildingPerson tBuildingPerson : mPerson.getTBuildingPersons()){
            if(Objects.equals(tBuildingPerson.getDelFlg(), OsolConstants.FLG_ON)){
                continue;
            }
            tBuildingPersonList.add(tBuildingPerson);
        }

    }

}
