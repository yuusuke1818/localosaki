package jp.co.osaki.osol.access.function.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.CorpPersonAuthDao;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.access.function.dao.MCorpFunctionUseDao;
import jp.co.osaki.osol.access.function.model.LoginModel;
import jp.co.osaki.osol.access.function.model.OperationModel;
import jp.co.osaki.osol.access.function.model.RoleGroupModel;
import jp.co.osaki.osol.access.function.model.RoleModel;
import jp.co.osaki.osol.access.function.parser.LoginListParser;
import jp.co.osaki.osol.access.function.parser.OperationListParser;
import jp.co.osaki.osol.access.function.parser.RoleGroupListParser;
import jp.co.osaki.osol.access.function.parser.RoleListParser;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * OSOLアクセス制御情報クラス
 *
 * @author take_suzuki
 */
@Named(value = "osolAccessBean")
@ApplicationScoped
public class OsolAccessBean implements Serializable {

    private static final long serialVersionUID = -5833961240739385973L;

    /**
     * 機能コード　定数
     */
    public static enum FUNCTION_CD {
        NONE("NONE"),                 //利用可否チェック無し
        ENERGY_INPUT("100"),         //エネルギー入力
        ENERGY_USAGE_STATUS("101"), //エネルギー使用状況
        ANALYSIS("110"),             //集計・分析
        REPORT("111"),                //行政報告
        FACILITY("201"),             //設備情報
        MAINTENANCE("202"),          //メンテナンス依頼
        EQUIP_CONTROL("300"),        //機器制御
        PLAN_FULFILLMENT("400"),     //計画履行
        SIGNAGE("500"),               //サイネージ
        SMS("600"),                   //SMS
        REVISED_LAW("700"),          //改正省エネ法
    	SMS_BULK_DOWNLOAD("800");   //検針一括ダウンロード

        private final String val;

        private FUNCTION_CD(final String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    @Inject
    private OsolConfigs osolConfigs;

    @EJB
    private CorpPersonAuthDao corpPersonAuthDataFilterDao;

    /**
     * 企業機能利用マスタDao
     */
    @EJB
    private MCorpFunctionUseDao mCorpFunctionUseDao;

    /**
     * イベントロガー
     */
    public static final Logger eventLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.EVENT.getVal());

    /**
     * ログイン情報リスト
     */
    private LinkedHashMap<String, LoginModel> loginList = new LinkedHashMap<>();

    /**
     * 操作企業リスト
     */
    private LinkedHashMap<String, OperationModel> operationList = new LinkedHashMap<>();

    /**
     * ロールグループリスト
     */
    private LinkedHashMap<String, RoleGroupModel> roleGroupList = new LinkedHashMap<>();

    /**
     * ロールリスト
     */
    private LinkedHashMap<String, RoleModel> roleList = new LinkedHashMap<>();

    /**
     * アプリケーション起動時処理
     */
    @PostConstruct
    public void init() {

        String propFilePath = BaseConstants.STR_EMPTY;
        String propFilePathSetting = osolConfigs.getConfig(OsolConstants.OSOL_ACCESS_DIR_LIST);
        String[] propFilePathList = propFilePathSetting.split(";", 0);
        for (int x = 0 ; x < propFilePathList.length ; x++){
            if (propFilePathList[x].length() > 0){
                File propFile = new File(propFilePathList[x]);
                if (propFile.isDirectory() && propFile.exists()){
                    propFilePath = propFilePathList[x];
                    break;
                }
            }
        }
        eventLogger.debug(this.getClass().getName() + " : " + propFilePathSetting.concat(" => ").concat(propFilePath));

        File loginListFile = new File(propFilePath.concat(File.separator).concat(LoginListParser.XML_FILE));
        LoginListParser loginListParser = new LoginListParser();
        this.loginList = loginListParser.parse(loginListFile);

        File operationListFile = new File(propFilePath.concat(File.separator).concat(OperationListParser.XML_FILE));
        OperationListParser operationParser = new OperationListParser();
        this.operationList = operationParser.parse(operationListFile);

        File roleListFile = new File(propFilePath.concat(File.separator).concat(RoleListParser.XML_FILE));
        RoleListParser roleListParser = new RoleListParser();
        this.roleList = roleListParser.parse(roleListFile);

        File roleGroupListFile = new File(propFilePath.concat(File.separator).concat(RoleGroupListParser.XML_FILE));
        RoleGroupListParser roleGroupListParser = new RoleGroupListParser();
        this.roleGroupList = roleGroupListParser.parse(roleGroupListFile);
    }

    /**
     * ログイン取得
     *
     * @param key キー
     * @return ログイン
     */
    private LoginModel getLogin(String key) {

        if (this.loginList != null && key != null) {
            if (!loginList.isEmpty()) {
                return this.loginList.get(key);
            }
        }
        return null;
    }

    /**
     * 操作企業取得
     *
     * @param key キー
     * @return 操作企業
     */
    private OperationModel getOperation(String key) {

        if (this.operationList != null && key != null) {
            if (!operationList.isEmpty()) {
                return this.operationList.get(key);
            }
        }
        return null;
    }

    /**
     * ロールグループ取得
     *
     * @param key キー
     * @return ロールグループ
     */
    private RoleGroupModel getRoleGroup(String key) {

        if (this.roleGroupList != null && key != null) {
            if (!roleGroupList.isEmpty()) {
                return this.roleGroupList.get(key);
            }
        }
        return null;
    }

    /**
     * ロール取得
     *
     * @param key キー
     * @return ロール
     */
    private RoleModel getRole(String key) {

        if (this.roleList != null && key != null) {
            if (!roleList.isEmpty()) {
                return this.roleList.get(key);
            }
        }
        return null;
    }

    /**
     *
     * アクセスチェック
     *
     * @param functionCd 機能コード
     * @param roleGroupKey ロールグループキー
     * @param loginCorpId ログインしている企業ID
     * @param loginPersonId ログインしている担当者ID
     * @param operationCorpId 操作している企業ID
     * @return アクセスチェック結果
     */
    public AccessResultSet getAccessEnable(
            OsolAccessBean.FUNCTION_CD functionCd,
            String roleGroupKey,
            String loginCorpId,
            String loginPersonId,
            String operationCorpId) {

        //チェック結果(入力情報, 出力情報)
        AccessResultSet accessResultSet = new AccessResultSet();
        //入力情報
        AccessResultSet.Input input = new AccessResultSet().new Input();
        //出力情報
        AccessResultSet.Output output = new AccessResultSet().new Output();

        //機能コード
        if (functionCd == null) {
            input.setFunctionCd(OsolAccessBean.FUNCTION_CD.NONE);
        } else {
            input.setFunctionCd(functionCd);
        }
        //ロールグループキー
        input.setRoleGroupKey(roleGroupKey);
        //ログイン企業ID
        input.setLoginCorpId(loginCorpId);
        //ログイン担当者ID
        input.setLoginPersonId(loginPersonId);
        //操作企業ID
        input.setOperationCorpId(operationCorpId);

        //必須チェック
        if ((input.getRoleGroupKey() == null || input.getRoleGroupKey().isEmpty())
                || (input.getLoginCorpId() == null || input.getLoginCorpId().isEmpty())
                || (input.getLoginPersonId() == null || input.getLoginPersonId().isEmpty())
                || (input.getOperationCorpId() == null || input.getOperationCorpId().isEmpty())) {
            accessResultSet.setInput(input);
            accessResultSet.setOutput(output);
            resultLog(accessResultSet);
            return accessResultSet;
        }

        //企業種別、担当者種別、権限種別、担当者権限を取得
        List<CorpPersonAuthResultSet> dataList = corpPersonAuthDataFilterDao.getCorpPersonAuth(new CorpPersonAuthParam(input.getLoginCorpId(), input.getLoginPersonId(), input.getOperationCorpId()));
        if (dataList == null || dataList.size() <= 0) {
            accessResultSet.setInput(input);
            accessResultSet.setOutput(output);
            resultLog(accessResultSet);
            return accessResultSet;
        }
        //企業種別、担当者種別、権限種別を設定
        input.setLoginCorpType(dataList.get(0).getLoginCorpType());
        input.setLoginPersonType(dataList.get(0).getLoginPersonType());
        input.setOperationCorpType(dataList.get(0).getOperationCorpType());
        input.setOperationAuthorityType(dataList.get(0).getOperationAuthorityType());
        //担当者権限を取得
        for (CorpPersonAuthResultSet data : dataList) {
            if (data.getOperationAuthorityFlg() == 1) {
                input.getOperationAuthorityCdList().add(data.getOperationAuthorityCd());
            }
        }
        accessResultSet.setInput(input);

        //機能利用チェック
        boolean functionCdEnable = false;
        //大崎電気 or メンテナンスは機能利用チェック無し
        if (input.getLoginCorpType().equals("0") || input.getLoginCorpType().equals("2")) {
            functionCdEnable = true;
            //機能指定なしの場合は機能利用チェック無し
        } else if (input.getFunctionCd().equals(OsolAccessBean.FUNCTION_CD.NONE)) {
            functionCdEnable = true;
            //機能指定ありの場合、機能利用チェック
        } else if ((input.getFunctionCd() != null && !input.getFunctionCd().equals(OsolAccessBean.FUNCTION_CD.NONE))
                && (input.getLoginCorpId() != null && !input.getLoginCorpId().isEmpty())) {
            MCorpFunctionUse mCorpFunctionUse = mCorpFunctionUseDao.find(input.getFunctionCd().getVal(), input.getLoginCorpId());
            if (mCorpFunctionUse != null) {
                functionCdEnable = mCorpFunctionUse.getUseFlg() == 1;
            }
        }

        //機能利用 組み合わせチェック
        if(!functionMatchCheck(input)){
            functionCdEnable = false;   // 組み合わせNG
        }

        output.setFunctionCd(input.getFunctionCd());
        output.setLoginCorpId(input.getLoginCorpId());
        output.setLoginCorpType(input.getLoginCorpType());
        output.setFunctionEnable(functionCdEnable);

        //ロールグループ存在チェック
        RoleGroupModel roleGroup = this.getRoleGroup(input.getRoleGroupKey());
        output.setRoleGroupKey(input.getRoleGroupKey());
        if (roleGroup == null) {
            accessResultSet.setOutput(output);
            resultLog(accessResultSet);
            return accessResultSet;
        }
        List<Boolean> roleOrCheckList = new ArrayList<>();
        AccessResultSet.RoleResult roleResult;
        for (String roleKey : roleGroup.getRoleList()) {
            roleResult = new AccessResultSet().new RoleResult();
            //Role
            roleResult.setRoleKey(roleKey);
            RoleModel role = this.getRole(roleKey);
            if (role == null) {
                roleResult.setRoleEnable(false);
                output.getRoleResultList().put(roleKey, roleResult);
                roleOrCheckList.add(Boolean.FALSE);
            } else {
                // LoginModel
                boolean loginCorpTypeCheck = false;
                boolean loginPersonTypeCheck = false;
                LoginModel login;
                if ((!role.getLogin().isEmpty()) && (this.getLogin(role.getLogin()) != null)) {
                    login = this.getLogin(role.getLogin());
                    if (input.getLoginCorpType() == null || input.getLoginCorpType().isEmpty()) {
                        //loginCorpTypeの指定がない場合はFalse
                        loginCorpTypeCheck = false;
                    } else if (login.getCorpType().isEmpty()) {
                        //CorpTypeの指定がない場合はtrue
                        loginCorpTypeCheck = true;
                    } else if (login.getCorpType().equals(input.getLoginCorpType())) {
                        loginCorpTypeCheck = true;
                    }
                    if (input.getLoginPersonType() == null || input.getLoginPersonType().isEmpty()) {
                        //loginPersonTypeの指定がない場合はfalse
                        loginPersonTypeCheck = false;
                    } else if (login.getPersonType().isEmpty()) {
                        //PersonTypeの指定がない場合はtrue
                        loginPersonTypeCheck = true;
                    } else if (login.getPersonType().equals(input.getLoginPersonType())) {
                        loginPersonTypeCheck = true;
                    }
                } else {
                    login = new LoginModel();
                    //loginの指定が場合はTrue
                    loginCorpTypeCheck = true;
                    loginPersonTypeCheck = true;
                }
                roleResult.setLogin(login);
                roleResult.setLoginCorpTypeEnable(loginCorpTypeCheck);
                roleResult.setLoginPersonTypeEnable(loginPersonTypeCheck);

                // OperationModel
                boolean operationCorpTypeCheck = false;
                boolean operationAuthorityTypeCheck = false;
                boolean operationAuthorityCdCheck = false;
                OperationModel operation;
                if ((!role.getOperation().isEmpty()) && (this.getOperation(role.getOperation()) != null)) {
                    operation = this.getOperation(role.getOperation());
                    if (input.getOperationCorpType() == null || input.getOperationCorpType().isEmpty()) {
                        //operationCorpTypeの指定がない場合はtrue
                        operationCorpTypeCheck = true;
                    } else if (operation.getCorpType().isEmpty()) {
                        //CorpTypeの指定がない場合はtrue
                        operationCorpTypeCheck = true;
                    } else if (operation.getCorpType().equals(input.getOperationCorpType())) {
                        operationCorpTypeCheck = true;
                    }
                    if (input.getOperationAuthorityType() == null || input.getOperationAuthorityType().isEmpty()) {
                        //operationAuthorityTypeの指定がない場合はtrue
                        operationAuthorityTypeCheck = true;
                    } else if (operation.getAuthorityType().isEmpty()) {
                        //AuthorityTypeの指定がない場合はtrue
                        operationAuthorityTypeCheck = true;
                    } else if (operation.getAuthorityType().equals(input.getOperationAuthorityType())) {
                        operationAuthorityTypeCheck = true;
                    }
                    if (input.getOperationAuthorityCdList() == null || input.getOperationAuthorityCdList().size() <= 0) {
                        //operationAuthorityCdListの指定がない場合はtrue
                        operationAuthorityCdCheck = true;
                    } else if (operation.getAuthorityCd().isEmpty()) {
                        //AuthorityCdの指定がない場合はtrue
                        operationAuthorityCdCheck = true;
                    } else {
                        for (String operationAuthorityCd : input.getOperationAuthorityCdList()) {
                            if (operation.getAuthorityCd().equals(operationAuthorityCd)) {
                                operationAuthorityCdCheck = true;
                                break;
                            }
                        }
                    }
                } else {
                    operation = new OperationModel();
                    //operationの指定が場合はTrue
                    operationCorpTypeCheck = true;
                    operationAuthorityTypeCheck = true;
                    operationAuthorityCdCheck = true;
                }
                roleResult.setOperation(operation);
                roleResult.setOperationCorpTypeEnable(operationCorpTypeCheck);
                roleResult.setOperationAuthorityTypeEnable(operationAuthorityTypeCheck);
                roleResult.setOperationAuthorityCdEnable(operationAuthorityCdCheck);

                Boolean roleEnableFlg = (Boolean) (loginCorpTypeCheck && loginPersonTypeCheck && operationCorpTypeCheck && operationAuthorityTypeCheck && operationAuthorityCdCheck);
                roleResult.setRoleEnable(roleEnableFlg);
                output.getRoleResultList().put(roleKey, roleResult);
                roleOrCheckList.add(roleEnableFlg);
            }
        }
        //いずれか(OR)のロールにTrueがであればTrue
        output.setRoleGroupEnable(roleOrCheckList.contains(Boolean.TRUE));
        accessResultSet.setOutput(output);
        resultLog(accessResultSet);

        return accessResultSet;
    }

    /**
     *
     * 機能利用組み合わせチェック
     *
     * @param boolean 組み合わせ結果 false:組み合わせNG
     */
    private boolean functionMatchCheck(AccessResultSet.Input input) {

        // 設備が利用不可であればメンテナンスは利用不可
        if(input.getFunctionCd().equals(OsolAccessBean.FUNCTION_CD.MAINTENANCE)){
            MCorpFunctionUse mCorpFunctionUse = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.FACILITY.getVal(), input.getLoginCorpId());
            if (mCorpFunctionUse != null && mCorpFunctionUse.getUseFlg() != 1) {
                return false;
            }
        }
        // エネルギー入力、計画履行、EMSが利用不可であれば集計・分析は利用不可
        if(input.getFunctionCd().equals(OsolAccessBean.FUNCTION_CD.ANALYSIS)){
            MCorpFunctionUse mCorpFunctionUse1 = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.ENERGY_INPUT.getVal(), input.getLoginCorpId());
            MCorpFunctionUse mCorpFunctionUse2 = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.PLAN_FULFILLMENT.getVal(), input.getLoginCorpId());
            MCorpFunctionUse mCorpFunctionUse3 = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.ENERGY_USAGE_STATUS.getVal(), input.getLoginCorpId());
            if (mCorpFunctionUse1 != null && mCorpFunctionUse2 != null && mCorpFunctionUse3 != null) {
                if( mCorpFunctionUse1.getUseFlg() != 1 && mCorpFunctionUse2.getUseFlg() != 1 && mCorpFunctionUse3.getUseFlg() != 1) {
                    return false;
                }
            }
        }
        // 設備、メンテナンスどちらか利用不可であれば行政報告は利用不可
        if(input.getFunctionCd().equals(OsolAccessBean.FUNCTION_CD.REPORT)){
            MCorpFunctionUse mCorpFunctionUse1 = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.FACILITY.getVal(), input.getLoginCorpId());
            MCorpFunctionUse mCorpFunctionUse2 = mCorpFunctionUseDao.find(OsolAccessBean.FUNCTION_CD.MAINTENANCE.getVal(), input.getLoginCorpId());
            if (mCorpFunctionUse1 != null && mCorpFunctionUse2 != null) {
                if( mCorpFunctionUse1.getUseFlg() != 1 || mCorpFunctionUse2.getUseFlg() != 1 ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * アクセス結果ログ出力
     *
     * @param accessResultSet アクセスチェック結果
     */
    private void resultLog(AccessResultSet accessResultSet) {

        String lsp = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        sb.append(lsp);
        sb.append(lsp);
        sb.append("*** Input *** ").append(lsp);
        sb.append("FunctionCd : ").append(accessResultSet.getInput().getFunctionCd().getVal()).append(lsp);
        sb.append("RoleGroup : ").append(accessResultSet.getInput().getRoleGroupKey()).append(lsp);
        sb.append("Login.CorpId : ").append(accessResultSet.getInput().getLoginCorpId()).append(lsp);
        sb.append("Login.CorpType : ").append(accessResultSet.getInput().getLoginCorpType()).append(lsp);
        sb.append("Login.PersonId : ").append(accessResultSet.getInput().getLoginPersonId()).append(lsp);
        sb.append("Login.PersonType : ").append(accessResultSet.getInput().getLoginPersonType()).append(lsp);
        sb.append("Operation.CorpId : ").append(accessResultSet.getInput().getOperationCorpId()).append(lsp);
        sb.append("Operation.CorpType : ").append(accessResultSet.getInput().getOperationCorpType()).append(lsp);
        sb.append("Operation.AuthorityType : ").append(accessResultSet.getInput().getOperationAuthorityType()).append(lsp);
        sb.append("Operation.AuthorityCd : ");
        for (String a : accessResultSet.getInput().getOperationAuthorityCdList()) {
            sb.append(a).append(" ");
        }
        sb.append(lsp);
        sb.append(lsp);
        sb.append("*** Outout *** ").append(lsp);
        sb.append("FunctionUse => ").append(accessResultSet.getOutput().isFunctionEnable()).append(lsp);
        sb.append("  FunctionCd : ").append(accessResultSet.getOutput().getFunctionCd().getVal()).append(lsp);
        sb.append("  Login.CorpId : ").append(accessResultSet.getOutput().getLoginCorpId()).append(lsp);
        sb.append("  Login.CorpType : ").append(accessResultSet.getOutput().getLoginCorpType()).append(lsp);
        sb.append("RoleGroup : ").append(accessResultSet.getOutput().getRoleGroupKey()).append(" => ").append(accessResultSet.getOutput().isRoleGroupEnable()).append(lsp);
        for (Map.Entry<String, AccessResultSet.RoleResult> b : accessResultSet.getOutput().getRoleResultList().entrySet()) {
            sb.append("  Role : ").append(b.getValue().getRoleKey()).append(" => ").append(b.getValue().isRoleEnable()).append(lsp);
            if (!b.getValue().getLogin().getCorpType().isEmpty()) {
                sb.append("    Login.CorpType : ").append(b.getValue().getLogin().getCorpType()).append(" => ").append(b.getValue().isLoginCorpTypeEnable()).append(lsp);
            }
            if (!b.getValue().getLogin().getPersonType().isEmpty()) {
                sb.append("    Login.PersonType : ").append(b.getValue().getLogin().getPersonType()).append(" => ").append(b.getValue().isLoginPersonTypeEnable()).append(lsp);
            }
            if (!b.getValue().getOperation().getCorpType().isEmpty()) {
                sb.append("    Operation.CorpType : ").append(b.getValue().getOperation().getCorpType()).append(" => ").append(b.getValue().isOperationCorpTypeEnable()).append(lsp);
            }
            if (!b.getValue().getOperation().getAuthorityType().isEmpty()) {
                sb.append("    Operation.AuthorityType : ").append(b.getValue().getOperation().getAuthorityType()).append(" => ").append(b.getValue().isOperationAuthorityTypeEnable()).append(lsp);
            }
            if (!b.getValue().getOperation().getAuthorityCd().isEmpty()) {
                sb.append("    Operation.AuthorityCd : ").append(b.getValue().getOperation().getAuthorityCd()).append(" => ").append(b.getValue().isOperationAuthorityCdEnable()).append(lsp);
            }
        }
        sb.append(lsp);
        eventLogger.debug(this.getClass().getName().concat(".getAccessEnable : ").concat(sb.toString()));
    }

}
