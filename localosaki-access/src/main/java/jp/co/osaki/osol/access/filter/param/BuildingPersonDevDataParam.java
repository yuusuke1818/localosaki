package jp.co.osaki.osol.access.filter.param;

/**
 * 建物、担当者、装置パラメータ クラス
 * @author nishida.t
 *
 */
public class BuildingPersonDevDataParam {

    /**
     * 企業ID
     */
    public static final String CORP_ID = "corpId";

    private String corpId;

    /**
     * 建物ID
     */
    public static final String BUILDING_ID = "buildingId";

    private Long buildingId;

    /**
     * ログイン担当者企業ID
     */
    public static final String LOGIN_CORP_ID = "loginCorpId";

    private String loginCorpId;

    /**
     * 担当者ID
     */
    public static final String LOGIN_PERSON_ID = "loginPersonId";

    private String loginPersonId;

    /**
     * 装置ID
     * 装置に紐づくメーターのみ取得する場合
     */
    public static final String DEV_ID = "devId";

    private String devId;

    /**
    *
    * コンストラクタ
    *
    * @param corpId 企業ID
    * @param buildingId 建物ID
    * @param loginCorpId ログイン企業ID
    * @param loginPersonId ログイン担当者ID
    * @param devId 装置ID
    */
   public BuildingPersonDevDataParam(String corpId, Long buildingId, String loginCorpId, String loginPersonId) {
       this.corpId = corpId;
       this.buildingId = buildingId;
       this.loginCorpId = loginCorpId;
       this.loginPersonId = loginPersonId;
   }

    /**
    *
    * コンストラクタ
    *
    * @param corpId 企業ID
    * @param buildingId 建物ID
    * @param loginCorpId ログイン企業ID
    * @param loginPersonId ログイン担当者ID
    * @param devId 装置ID
    */
   public BuildingPersonDevDataParam(String corpId, Long buildingId, String loginCorpId, String loginPersonId, String devId) {
       this.corpId = corpId;
       this.buildingId = buildingId;
       this.loginCorpId = loginCorpId;
       this.loginPersonId = loginPersonId;
       this.devId = devId;
   }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getLoginCorpId() {
        return loginCorpId;
    }

    public void setLoginCorpId(String loginCorpId) {
        this.loginCorpId = loginCorpId;
    }

    public String getLoginPersonId() {
        return loginPersonId;
    }

    public void setLoginPersonId(String loginPersonId) {
        this.loginPersonId = loginPersonId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

}
