package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenerationTime;


/**
 * The persistent class for the m_person database table.
 *
 */
@Entity
@Table(name = "m_person")
@NamedQueries({
    //
    // tempPassExpirationDate との比較について
    //  2016-10-20 との比較で、NamedQueryの場合 2016-10-20 >= 2016-10-20 14:20:30 が成立する体で結果が返ってくる
    // 期限が10/20なのであれば、その日中は期限内なので結果としては問題ないが、文法としては違和感がある
    // ExpirationDateは 2016-10-20 23:59:59 のイメージで処理は考えないといけない
    //
    @NamedQuery(name = "MPerson.findAll", query = "SELECT m FROM MPerson m"),
    @NamedQuery(name = "MPerson.findbyUserId", query = "SELECT m FROM MPerson m WHERE m.userId =:userId"),
    @NamedQuery(name = "MPerson.searchPersonInfo", query = "SELECT m FROM MPerson m "
            + "WHERE m.id.corpId =:corpId "
            + "AND ((:personId IS NULL or m.id.personId LIKE :personId) OR (:personName IS NULL or m.personName LIKE :personName)) "
            + "AND (:personKana IS NULL or m.personKana LIKE :personKana) "
            + "AND (:deptName IS NULL or m.deptName LIKE :deptName) "
            + "AND (:positionName IS NULL or m.positionName LIKE :positionName) "
            //            + "AND (:statusStopAccountFlg IS NULL OR (m.accountStopFlg =:statusStopAccount)) "
            //            + "AND (:statusLockInputFailureFlg IS NULL OR (m.passMissCount >= :statusLockInputFailure)) "
            //            + "AND (:statusLockExpirationFlg IS NULL OR (m.tempPassExpirationDate < :nowDate))"
            //            + "AND (:statusNotLoginFlg IS NULL OR ((m.tempPassExpirationDate >= :nowDate) AND (m.lastLoginDate IS NULL)))"
            //            + "AND (:statusNormalFlg IS NULL OR ((m.accountStopFlg = 0) AND (m.passMissCount <= :statusLockInputFailure) AND (m.tempPassExpirationDate IS NULL) AND (m.lastLoginDate IS NOT NULL))) "
            + "AND m.delFlg = 0 "
            + "ORDER BY m.id.personId ASC "
    ),
    @NamedQuery(name = "MPerson.findBuildingPerson",    //建物担当設定済みユーザー
            query = "SELECT p FROM MPerson p INNER JOIN p.TBuildingPersons bp "
            + " WHERE bp.id.corpId = :corpId"
            + " AND bp.id.buildingId = :buildingId"
            + " AND bp.delFlg <>1"
            + " AND (bp.id.personCorpId = :corpId"  //対象企業(+パートナー)　（で担当設定されているユーザー）
                + " OR (:additionalCorpId IS NOT NULL AND bp.id.personCorpId = :additionalCorpId) )"
            + " AND p.delFlg <>1"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonAll", //建物担当設定済みユーザー（大崎権限での閲覧）
            query = "SELECT p FROM MPerson p INNER JOIN p.TBuildingPersons bp INNER JOIN p.MCorp c "
            + " WHERE bp.id.corpId = :corpId"
            + " AND bp.id.buildingId = :buildingId"
            + " AND bp.delFlg <>1"
            + " AND c.corpType <> '0'"    //大崎権限ユーザー以外　（で担当設定されてる全員）
            + " AND p.delFlg <>1"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonCandidateTargetCorp",   //①建物担当設定候補ユーザー：対象企業
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorpPersons cp "
            + " WHERE (p.id.corpId =:corpId)"
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
            + " AND cp.id.corpId = :corpId"     //対象企業の建物担当
            + " AND cp.authorityType = '1'"
            + " AND cp.delFlg = 0"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonCandidatePartnerAll",   //②建物担当設定候補ユーザー：操作ユーザー企業(パートナー)全員
            query = "SELECT p FROM MPerson p"
            + " WHERE (p.id.corpId =:additionalCorpId)"
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonCandidatePartnerAdmin",   //③建物担当設定候補ユーザー：操作ユーザー企業(パートナー)企業担当
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorpPersons cp "
            + " WHERE (p.id.corpId =:additionalCorpId)"
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
            + " AND cp.id.corpId = :corpId"     //対象企業の企業担当
            + " AND cp.authorityType = '0'"
            + " AND cp.delFlg = 0"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonCandidatePartnerMainteAll",    //②建物担当設定候補ユーザー：全パートナー、メンテの全員
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorp c "
            + " WHERE (c.corpType = '1' OR c.corpType = '2')"    //パートナーかメンテ
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
    ),
    @NamedQuery(name = "MPerson.findBuildingPersonCandidatePartnerAdminAll",    //③建物担当設定候補ユーザー：全パートナーで既に企業担当
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorpPersons cp INNER JOIN p.MCorp c "
            + " WHERE c.corpType = '1'"    //パートナー
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND cp.id.corpId = :corpId"
            + " AND cp.authorityType = '0'" //その企業の企業担当
            + " AND cp.delFlg = 0"
            + " AND p.delFlg = 0"
    ),
    @NamedQuery(name = "MPerson.findCorpPerson",    //企業担当設定済みユーザー
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorpPersons cp "
            + " WHERE cp.id.corpId = :corpId"
            + " AND cp.delFlg <>1"
            + " AND cp.authorityType = '0'"
            + " AND cp.id.personCorpId = :additionalCorpId"  //操作してるパートナー企業　（で企業担当設定されているユーザー）
            + " AND p.delFlg <>1"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findCorpPersonAll", //企業担当設定済みユーザー（大崎権限での閲覧）
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorpPersons cp INNER JOIN p.MCorp c"
            + " WHERE cp.id.corpId = :corpId"
            + " AND cp.delFlg <>1"
            + " AND cp.authorityType = '0'"
            + " AND c.corpType <> '3'"  //一般企業以外
            + " AND p.delFlg <>1"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findCorpPersonCandidate",   //企業担当設定候補ユーザー
            query = "SELECT p FROM MPerson p "
            + " WHERE p.id.corpId =:additionalCorpId"     //操作してるパートナー企業
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findCorpPersonCandidateAll",    //企業担当設定候補ユーザー（大崎権限で編集）
            query = "SELECT p FROM MPerson p INNER JOIN p.MCorp c "
            + " WHERE (c.corpType = '1'"    //パートナーか
                + " OR (c.corpType = '0' AND p.personType = '1') )"    //大崎の非管理者    （担当設定される可能性のある全員）
            + " AND (:personId IS NULL OR p.id.personId LIKE :personId)"
            + " AND (:personName IS NULL OR p.personName LIKE :personName)"
            + " AND (:personIdOrName IS NULL OR p.id.personId LIKE :personIdOrName OR p.personName LIKE :personIdOrName)"
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
            + " ORDER BY p.id.corpId ASC, p.id.personId ASC"
    ),
    @NamedQuery(name = "MPerson.findExcel",
            query = "SELECT p FROM MPerson p "
            + " WHERE p.id.corpId =:corpId"
            + " AND ((:personId IS NULL or p.id.personId LIKE :personId) OR (:personName IS NULL or p.personName LIKE :personName)) "
            + " AND (:personKana IS NULL OR p.personKana LIKE :personKana)"
            + " AND (:deptName IS NULL OR p.deptName LIKE :deptName)"
            + " AND (:positionName IS NULL OR p.positionName LIKE :positionName)"
            + " AND (:statusUse IS NULL "
            + "       OR (:statusStopAccountUse IS NOT NULL AND p.accountStopFlg = :statusStopAccount)"     //停止してない
            + "       OR (:statusLockInputFailureUse IS NOT NULL AND p.passMissCount >= :statusLockInputFailure"   //ミスロック
            + "          AND NOT p.accountStopFlg = :statusStopAccount)"
            + "       OR (:statusLockExpirationUse IS NOT NULL AND p.tempPassExpirationDate < :nowDate"     //期限切れ
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNotLoginUse IS NOT NULL AND p.tempPassExpirationDate >= :nowDate"          //未ログイン
            + "          AND NOT p.accountStopFlg = :statusStopAccount AND NOT p.passMissCount >= :statusLockInputFailure)"
            + "       OR (:statusNormalUse IS NOT NULL AND ("
            + "              NOT p.accountStopFlg = :statusStopAccount"
            + "          AND NOT p.passMissCount >= :statusLockInputFailure"
            + "          AND p.tempPassExpirationDate IS NULL)"
            + "       )"
            + " )"
            + " AND p.delFlg = 0"
            + " ORDER BY p.id.personId ASC"),})

    public class MPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MPersonPK id;

	@Column(name="account_stop_date")
	private Timestamp accountStopDate;

	@Column(name="account_stop_flg", nullable=false)
	private Integer accountStopFlg;

	@Column(name="auth_last_update_date", nullable=false)
	private Timestamp authLastUpdateDate;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="dept_name", length=100)
	private String deptName;

	@Column(name="fax_no", length=100)
	private String faxNo;

	@Column(name="last_login_date")
	private Timestamp lastLoginDate;

	@Column(name="last_oshirase_check_time")
	private Timestamp lastOshiraseCheckTime;

	@Column(name="mail_address", length=100)
	private String mailAddress;

	@Column(name="pass_miss_count", nullable=false)
	private Integer passMissCount;

	@Column(nullable=false, length=100)
	private String password;

	@Column(name="person_kana", length=200)
	private String personKana;

	@Column(name="person_name", nullable=false, length=100)
	private String personName;

	@Column(name="person_type", nullable=false, length=6)
	private String personType;

	@Column(name="position_name", length=100)
	private String positionName;

	@Column(name="tel_no", length=100)
	private String telNo;

	@Temporal(TemporalType.DATE)
	@Column(name="temp_pass_expiration_date")
	private Date tempPassExpirationDate;

	@Column(name="temp_password", length=100)
	private String tempPassword;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_pass_date", nullable=false)
	private Timestamp updatePassDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    @Column(name = "user_id", nullable = false)
	private Long userId;

	@Version
	@Column(nullable=false)
	private Integer version;

	//bi-directional many-to-one association to MCorp
	@OneToMany(mappedBy="MPerson")
	private List<MCorp> MCorps;

	//bi-directional many-to-one association to MCorpPerson
	@OneToMany(mappedBy="MPerson")
	private List<MCorpPerson> MCorpPersons;

	//bi-directional many-to-one association to MCorp
	@ManyToOne
	@JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
	private MCorp MCorp;

	//bi-directional many-to-one association to TAvailableEnergyBulkInput
	@OneToMany(mappedBy="MPerson")
	private List<TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs;

	//bi-directional many-to-one association to TBuildingPerson
	@OneToMany(mappedBy="MPerson")
	private List<TBuildingPerson> TBuildingPersons;

	//bi-directional many-to-one association to TMaintenanceRequest
	@OneToMany(mappedBy="MPerson1")
	private List<TMaintenanceRequest> TMaintenanceRequests1;

	//bi-directional many-to-one association to TMaintenanceRequest
	@OneToMany(mappedBy="MPerson2")
	private List<TMaintenanceRequest> TMaintenanceRequests2;

	//bi-directional many-to-one association to TFacilityBulkInput
	@OneToMany(mappedBy="MPerson")
	private List<TFacilityBulkInput> TFacilityBulkInputs;

	//bi-directional many-to-one association to MUiScreen
	@ManyToOne
	@JoinColumn(name="login_ui_screen_id")
	private MUiScreen MUiScreen;

	//bi-directional one-to-many association to TApiKey
	@OneToMany(mappedBy="MPerson")
	private List<TApiKey> TApiKeys;

	//bi-directional many-to-one association to MClaimantInfo
	@OneToMany(mappedBy="MPerson")
	private List<MClaimantInfo> MClaimantInfos;

    //bi-directional many-to-one association to TAggregateReservationInfo
	@OneToMany(mappedBy="MPerson")
	private List<TAggregateReservationInfo> TAggregateReservationInfos;

	//bi-directional many-to-one association to TMeterReadingDownloadReservationInfo
    @OneToMany(mappedBy="MPerson", cascade={CascadeType.ALL})
    private List<TMeterReadingDownloadReservationInfo> TMeterReadingDownloadReservationInfos;

    public MPerson() {
	}

	public MPersonPK getId() {
		return this.id;
	}

	public void setId(MPersonPK id) {
		this.id = id;
	}

	public Timestamp getAccountStopDate() {
		return this.accountStopDate;
	}

	public void setAccountStopDate(Timestamp accountStopDate) {
		this.accountStopDate = accountStopDate;
	}

	public Integer getAccountStopFlg() {
		return this.accountStopFlg;
	}

	public void setAccountStopFlg(Integer accountStopFlg) {
		this.accountStopFlg = accountStopFlg;
	}

	public Timestamp getAuthLastUpdateDate() {
		return this.authLastUpdateDate;
	}

	public void setAuthLastUpdateDate(Timestamp authLastUpdateDate) {
		this.authLastUpdateDate = authLastUpdateDate;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Integer getDelFlg() {
		return this.delFlg;
	}

	public void setDelFlg(Integer delFlg) {
		this.delFlg = delFlg;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getFaxNo() {
		return this.faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public Timestamp getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Timestamp getLastOshiraseCheckTime() {
		return this.lastOshiraseCheckTime;
	}

	public void setLastOshiraseCheckTime(Timestamp lastOshiraseCheckTime) {
		this.lastOshiraseCheckTime = lastOshiraseCheckTime;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public Integer getPassMissCount() {
		return this.passMissCount;
	}

	public void setPassMissCount(Integer passMissCount) {
		this.passMissCount = passMissCount;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersonKana() {
		return this.personKana;
	}

	public void setPersonKana(String personKana) {
		this.personKana = personKana;
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonType() {
		return this.personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getPositionName() {
		return this.positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getTelNo() {
		return this.telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Date getTempPassExpirationDate() {
		return this.tempPassExpirationDate;
	}

	public void setTempPassExpirationDate(Date tempPassExpirationDate) {
		this.tempPassExpirationDate = tempPassExpirationDate;
	}

	public String getTempPassword() {
		return this.tempPassword;
	}

	public void setTempPassword(String tempPassword) {
		this.tempPassword = tempPassword;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Timestamp getUpdatePassDate() {
		return this.updatePassDate;
	}

	public void setUpdatePassDate(Timestamp updatePassDate) {
		this.updatePassDate = updatePassDate;
	}

	public Long getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<MCorp> getMCorps() {
		return this.MCorps;
	}

	public void setMCorps(List<MCorp> MCorps) {
		this.MCorps = MCorps;
	}

	public MCorp addMCorp(MCorp MCorp) {
		getMCorps().add(MCorp);
		MCorp.setMPerson(this);

		return MCorp;
	}

	public MCorp removeMCorp(MCorp MCorp) {
		getMCorps().remove(MCorp);
		MCorp.setMPerson(null);

		return MCorp;
	}

	public List<MCorpPerson> getMCorpPersons() {
		return this.MCorpPersons;
	}

	public void setMCorpPersons(List<MCorpPerson> MCorpPersons) {
		this.MCorpPersons = MCorpPersons;
	}

	public MCorpPerson addMCorpPerson(MCorpPerson MCorpPerson) {
		getMCorpPersons().add(MCorpPerson);
		MCorpPerson.setMPerson(this);

		return MCorpPerson;
	}

	public MCorpPerson removeMCorpPerson(MCorpPerson MCorpPerson) {
		getMCorpPersons().remove(MCorpPerson);
		MCorpPerson.setMPerson(null);

		return MCorpPerson;
	}

	public MCorp getMCorp() {
		return this.MCorp;
	}

	public void setMCorp(MCorp MCorp) {
		this.MCorp = MCorp;
	}

	public List<TAvailableEnergyBulkInput> getTAvailableEnergyBulkInputs() {
		return this.TAvailableEnergyBulkInputs;
	}

	public void setTAvailableEnergyBulkInputs(List<TAvailableEnergyBulkInput> TAvailableEnergyBulkInputs) {
		this.TAvailableEnergyBulkInputs = TAvailableEnergyBulkInputs;
	}

	public TAvailableEnergyBulkInput addTAvailableEnergyBulkInput(TAvailableEnergyBulkInput TAvailableEnergyBulkInput) {
		getTAvailableEnergyBulkInputs().add(TAvailableEnergyBulkInput);
		TAvailableEnergyBulkInput.setMPerson(this);

		return TAvailableEnergyBulkInput;
	}

	public TAvailableEnergyBulkInput removeTAvailableEnergyBulkInput(TAvailableEnergyBulkInput TAvailableEnergyBulkInput) {
		getTAvailableEnergyBulkInputs().remove(TAvailableEnergyBulkInput);
		TAvailableEnergyBulkInput.setMPerson(null);

		return TAvailableEnergyBulkInput;
	}

	public List<TBuildingPerson> getTBuildingPersons() {
		return this.TBuildingPersons;
	}

	public void setTBuildingPersons(List<TBuildingPerson> TBuildingPersons) {
		this.TBuildingPersons = TBuildingPersons;
	}

	public TBuildingPerson addTBuildingPerson(TBuildingPerson TBuildingPerson) {
		getTBuildingPersons().add(TBuildingPerson);
		TBuildingPerson.setMPerson(this);

		return TBuildingPerson;
	}

	public TBuildingPerson removeTBuildingPerson(TBuildingPerson TBuildingPerson) {
		getTBuildingPersons().remove(TBuildingPerson);
		TBuildingPerson.setMPerson(null);

		return TBuildingPerson;
	}

	public List<TMaintenanceRequest> getTMaintenanceRequests1() {
		return this.TMaintenanceRequests1;
	}

	public void setTMaintenanceRequests1(List<TMaintenanceRequest> TMaintenanceRequests1) {
		this.TMaintenanceRequests1 = TMaintenanceRequests1;
	}

	public TMaintenanceRequest addTMaintenanceRequests1(TMaintenanceRequest TMaintenanceRequests1) {
		getTMaintenanceRequests1().add(TMaintenanceRequests1);
		TMaintenanceRequests1.setMPerson1(this);

		return TMaintenanceRequests1;
	}

	public TMaintenanceRequest removeTMaintenanceRequests1(TMaintenanceRequest TMaintenanceRequests1) {
		getTMaintenanceRequests1().remove(TMaintenanceRequests1);
		TMaintenanceRequests1.setMPerson1(null);

		return TMaintenanceRequests1;
	}

	public List<TMaintenanceRequest> getTMaintenanceRequests2() {
		return this.TMaintenanceRequests2;
	}

	public void setTMaintenanceRequests2(List<TMaintenanceRequest> TMaintenanceRequests2) {
		this.TMaintenanceRequests2 = TMaintenanceRequests2;
	}

	public TMaintenanceRequest addTMaintenanceRequests2(TMaintenanceRequest TMaintenanceRequests2) {
		getTMaintenanceRequests2().add(TMaintenanceRequests2);
		TMaintenanceRequests2.setMPerson2(this);

		return TMaintenanceRequests2;
	}

	public TMaintenanceRequest removeTMaintenanceRequests2(TMaintenanceRequest TMaintenanceRequests2) {
		getTMaintenanceRequests2().remove(TMaintenanceRequests2);
		TMaintenanceRequests2.setMPerson2(null);

		return TMaintenanceRequests2;
	}

        public List<TFacilityBulkInput> getTFacilityBulkInputs() {
		return this.TFacilityBulkInputs;
	}

	public void setTFacilityBulkInputs(List<TFacilityBulkInput> TFacilityBulkInputs) {
		this.TFacilityBulkInputs = TFacilityBulkInputs;
	}

	public TFacilityBulkInput addTFacilityBulkInput(TFacilityBulkInput TFacilityBulkInput) {
		getTFacilityBulkInputs().add(TFacilityBulkInput);
		TFacilityBulkInput.setMPerson(this);

		return TFacilityBulkInput;
	}

	public TFacilityBulkInput removeTFacilityBulkInput(TFacilityBulkInput TFacilityBulkInput) {
		getTFacilityBulkInputs().remove(TFacilityBulkInput);
		TFacilityBulkInput.setMPerson(null);

		return TFacilityBulkInput;
	}
        public MUiScreen getMUiScreen() {
		return this.MUiScreen;
	}

	public void setMUiScreen(MUiScreen MUiScreen) {
		this.MUiScreen = MUiScreen;
	}

        public List<TApiKey> getTApiKeys() {
		return this.TApiKeys;
	}

	public void setTApiKeys(List<TApiKey> TApiKeys) {
		this.TApiKeys = TApiKeys;
	}

	public List<MClaimantInfo> getMClaimantInfos() {
		return this.MClaimantInfos;
	}

	public void setMClaimantInfos(List<MClaimantInfo> MClaimantInfos) {
		this.MClaimantInfos = MClaimantInfos;
	}

	public MClaimantInfo addMClaimantInfo(MClaimantInfo MClaimantInfo) {
		getMClaimantInfos().add(MClaimantInfo);
		MClaimantInfo.setMPerson(this);

		return MClaimantInfo;
	}

	public MClaimantInfo removeMClaimantInfo(MClaimantInfo MClaimantInfo) {
		getMClaimantInfos().remove(MClaimantInfo);
		MClaimantInfo.setMPerson(null);

		return MClaimantInfo;
	}

	public List<TAggregateReservationInfo> getTAggregateReservationInfos() {
		return this.TAggregateReservationInfos;
	}

	public void setTAggregateReservationInfos(List<TAggregateReservationInfo> TAggregateReservationInfos) {
		this.TAggregateReservationInfos = TAggregateReservationInfos;
	}

	public TAggregateReservationInfo addTAggregateReservationInfo(TAggregateReservationInfo TAggregateReservationInfo) {
		getTAggregateReservationInfos().add(TAggregateReservationInfo);
		TAggregateReservationInfo.setMPerson(this);

		return TAggregateReservationInfo;
	}

	public TAggregateReservationInfo removeTAggregateReservationInfo(TAggregateReservationInfo TAggregateReservationInfo) {
		getTAggregateReservationInfos().remove(TAggregateReservationInfo);
		TAggregateReservationInfo.setMPerson(null);

		return TAggregateReservationInfo;
	}
    public List<TMeterReadingDownloadReservationInfo> getTMeterReadingDownloadReservationInfos() {
        return this.TMeterReadingDownloadReservationInfos;
    }

    public void setTMeterReadingDownloadReservationInfos(List<TMeterReadingDownloadReservationInfo> TMeterReadingDownloadReservationInfos) {
        this.TMeterReadingDownloadReservationInfos = TMeterReadingDownloadReservationInfos;
    }

    public TMeterReadingDownloadReservationInfo addTMeterReadingDownloadReservationInfo(TMeterReadingDownloadReservationInfo TMeterReadingDownloadReservationInfo) {
        getTMeterReadingDownloadReservationInfos().add(TMeterReadingDownloadReservationInfo);
        TMeterReadingDownloadReservationInfo.setMPerson(this);

        return TMeterReadingDownloadReservationInfo;
    }

    public TMeterReadingDownloadReservationInfo removeTMeterReadingDownloadReservationInfo(TMeterReadingDownloadReservationInfo TMeterReadingDownloadReservationInfo) {
        getTMeterReadingDownloadReservationInfos().remove(TMeterReadingDownloadReservationInfo);
        TMeterReadingDownloadReservationInfo.setMPerson(null);

        return TMeterReadingDownloadReservationInfo;
    }

}