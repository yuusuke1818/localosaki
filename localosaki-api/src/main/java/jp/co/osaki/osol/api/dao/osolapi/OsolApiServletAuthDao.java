package jp.co.osaki.osol.api.dao.osolapi;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiBuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiBuildingSmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiCorpFunctionUseServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiCorpPersonAuthServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSm;
import jp.co.osaki.osol.entity.MBuildingSmPK;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MCorpFunctionUsePK;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MCorpPersonAuthPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

/**
 * 権限
 *
 * */
@Stateless
public class OsolApiServletAuthDao extends BaseApiDao {

    private final OsolApiCorpPersonAuthServiceDaoImpl osolApiCorpPersonAuthServiceDaoImpl;
    private final OsolApiBuildingServiceDaoImpl osolApiBuildingServiceDaoImpl;
    private final OsolApiBuildingSmServiceDaoImpl osolApiBuildingSmServiceDaoImpl;
    private final OsolApiCorpFunctionUseServiceDaoImpl osolApiCorpFunctionUseServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public OsolApiServletAuthDao() {
        osolApiCorpPersonAuthServiceDaoImpl = new OsolApiCorpPersonAuthServiceDaoImpl();
        osolApiBuildingServiceDaoImpl = new OsolApiBuildingServiceDaoImpl();
        osolApiBuildingSmServiceDaoImpl = new OsolApiBuildingSmServiceDaoImpl();
        osolApiCorpFunctionUseServiceDaoImpl = new OsolApiCorpFunctionUseServiceDaoImpl();
    }

    /**
     * 企業担当者権限取得
     *
     * @param corpId
     * @param personCorpId
     * @param personId
     * @param authorityCd
     * @return
     */
    private MCorpPersonAuth getCorpPersonAuth(String corpId, String personCorpId, String personId, String authorityCd) {
        MCorpPersonAuth mCorpPersonAuth = new MCorpPersonAuth();
        MCorpPersonAuthPK id = new MCorpPersonAuthPK();
        id.setCorpId(corpId);
        id.setPersonCorpId(personCorpId);
        id.setPersonId(personId);
        id.setAuthorityCd(authorityCd);
        mCorpPersonAuth.setId(id);
        return this.find(osolApiCorpPersonAuthServiceDaoImpl, mCorpPersonAuth);
    }
    /**
     * 企業担当者権限
     *
     * @param corpId
     * @param personCorpId
     * @param personId
     * @param authorityCd
     * @return 権限が有効の場合、trueを返します
     */
    public boolean isCorpPersonAuth(String corpId, String personCorpId, String personId, String authorityCd) {
        MCorpPersonAuth ret = getCorpPersonAuth(corpId, personCorpId, personId, authorityCd);
        if (ret != null) {
            if (ret.getAuthorityFlg() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 建物取得
     *
     * @param corpId
     * @param buildingNo
     * @return
     */
    private TBuilding getBuilding(String corpId, String buildingNo) {
        TBuilding target = new TBuilding();
        TBuildingPK id = new TBuildingPK();
        id.setCorpId(corpId);
        target.setBuildingNo(buildingNo);
        target.setId(id);
        return this.find(osolApiBuildingServiceDaoImpl, target);
    }
    /**
     * 建物ID取得
     *
     * @param corpId
     * @param buildingNo
     * @return
     */
    public Long getBuildingId(String corpId, String buildingNo) {
        TBuilding ret = getBuilding(corpId, buildingNo);
        if (ret != null && ret.getId() != null) {
            return ret.getId().getBuildingId();
        }
        return null;
    }

    /**
     * 建物機器
     *
     * @param corpId
     * @param smId
     * @return
     */
    private List<MBuildingSm> getBuildingSmList(String corpId, long smId) {
        MBuildingSm target = new MBuildingSm();
        MBuildingSmPK id = new MBuildingSmPK();
        id.setCorpId(corpId);
        id.setSmId(smId);
        target.setId(id);
        return this.getResultList(osolApiBuildingSmServiceDaoImpl, target);

    }
    /**
     * 建物IDリスト取得
     *
     * @param corpId
     * @param smId
     * @return
     */
    public List<String> getBuildingIdList(String corpId, long smId) {
        List<String> list = null;
        List<MBuildingSm> ret = getBuildingSmList(corpId, smId);
        if (ret != null && ret.size() > 0) {
            list = new ArrayList<>();
            for (MBuildingSm item: ret) {
                list.add(String.valueOf(item.getId().getBuildingId()));
            }
        }
        return list;
    }
    /**
    *
    * 企業機能利用マスタ検索
    *
    * @param functionCd 機能コード
    * @param corpId 企業ID
    * @return 企業機能利用マスタエンティティ
    */
   public MCorpFunctionUse getCorpFunctionUse(String functionCd, String corpId) {

       MCorpFunctionUsePK mCorpFunctionUsePK = new MCorpFunctionUsePK();
       mCorpFunctionUsePK.setCorpId(corpId);
       mCorpFunctionUsePK.setFunctionCd(functionCd);
       MCorpFunctionUse mCorpFunctionUse = new MCorpFunctionUse();
       mCorpFunctionUse.setId(mCorpFunctionUsePK);
       return find(osolApiCorpFunctionUseServiceDaoImpl, mCorpFunctionUse);
   }

}
