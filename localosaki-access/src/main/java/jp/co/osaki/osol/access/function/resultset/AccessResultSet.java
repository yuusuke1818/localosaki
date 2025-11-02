package jp.co.osaki.osol.access.function.resultset;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean.FUNCTION_CD;
import jp.co.osaki.osol.access.function.model.LoginModel;
import jp.co.osaki.osol.access.function.model.OperationModel;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * アクセスチェック結果クラス
 * 
 * @author take_suzuki
 */
public class AccessResultSet extends BaseResultSet {

    /**
     * 入力情報
     */
    private Input input;

    /**
     * 出力情報
     */
    private Output output;

    /**
     * コンストラクタ
     */
    public AccessResultSet() {
        this.input = new Input();
        this.output = new Output();
    }

    /**
     * 入力情報取得
     * 
     * @return 入力情報
     */
    public Input getInput() {
        return input;
    }

    /**
     * 入力情報設定
     * 
     * @param input 入力情報
     */
    public void setInput(Input input) {
        this.input = input;
    }

    /**
     *
     * 出力情報取得
     * 
     * @return 出力情報
     */
    public Output getOutput() {
        return output;
    }

    /**
     *
     * 出力情報設定
     * 
     * @param output 出力情報
     */
    public void setOutput(Output output) {
        this.output = output;
    }

    /**
     * 入力情報インナークラス
     */
    public class Input {

        /**
         *  機能コード
         */
        private OsolAccessBean.FUNCTION_CD functionCd;

        /**
         *  ロールグループキー
         */
        private String roleGroupKey;

        /**
         *  ログイン企業ID
         */
        private String loginCorpId;

        /**
         *  ログイン企業種別
         */
        private String loginCorpType;

        /**
         *  ログイン担当者ID
         */
        private String loginPersonId;

        /**
         *  ログイン担当者種別
         */
        private String loginPersonType;

        /**
         *  操作企業ID
         */
        private String operationCorpId;

        /**
         *  操作企業種別
         */
        private String operationCorpType;

        /**
         *  操作企業担当者権限種別
         */
        private String operationAuthorityType;

        /**
         *  操作企業担当者権限コードリスト
         */
        private List<String> operationAuthorityCdList;

        public Input(){
            this.functionCd = OsolAccessBean.FUNCTION_CD.NONE;
            this.roleGroupKey = BaseConstants.STR_EMPTY;
            this.loginCorpId = BaseConstants.STR_EMPTY;
            this.loginCorpType = BaseConstants.STR_EMPTY;
            this.loginPersonId = BaseConstants.STR_EMPTY;
            this.loginPersonType = BaseConstants.STR_EMPTY;
            this.operationCorpId = BaseConstants.STR_EMPTY;
            this.operationCorpType = BaseConstants.STR_EMPTY;
            this.operationAuthorityType = BaseConstants.STR_EMPTY;
            this.operationAuthorityCdList = new ArrayList<>();
        }

        /**
         *  機能コード取得
         * @return 機能コード
         */
        public FUNCTION_CD getFunctionCd() {
            return functionCd;
        }

        /**
         *  機能コード設定
         * @param functionCd 機能コード
         */
        public void setFunctionCd(FUNCTION_CD functionCd) {
            this.functionCd = functionCd;
        }
        
        /**
         *  機能コード取得
         * @return 機能コード
         */
        public String getRoleGroupKey() {
            return roleGroupKey;
        }

        /**
         *  ロールグループ取得
         * @param roleGroupKey ロールグループ
         */
        public void setRoleGroupKey(String roleGroupKey) {
            this.roleGroupKey = roleGroupKey;
        }

        /**
         *  ロールグループ取得
         * @return ロールグループ
         */
        public String getLoginCorpId() {
            return loginCorpId;
        }

        /**
         *
         *  ログイン企業ID 取得
         * 
         * @param loginCorpId ログイン企業ID
         */
        public void setLoginCorpId(String loginCorpId) {
            this.loginCorpId = loginCorpId;
        }

        /**
         *
         *  ログイン企業種別 取得
         * 
         * @return ログイン企業ID
         */
        public String getLoginCorpType() {
            return loginCorpType;
        }

        /**
         *
         *  ログイン企業種別 設定
         * @param loginCorpType ログイン担当者ID
         */
        public void setLoginCorpType(String loginCorpType) {
            this.loginCorpType = loginCorpType;
        }

        /**
         *
         *  ログイン担当者ID 設定
         * @return ログイン担当者ID
         */
        public String getLoginPersonId() {
            return loginPersonId;
        }

        /**
         *
         *  ログイン担当者ID 設定
         * @param loginPersonId ログイン担当者ID
         */
        public void setLoginPersonId(String loginPersonId) {
            this.loginPersonId = loginPersonId;
        }

        /**
         *
         *  ログイン担当者種別 設定
         * @return ログイン担当者種別
         */
        public String getLoginPersonType() {
            return loginPersonType;
        }

        /**
         *
         *  ログイン担当者種別 設定
         * @param loginPersonType ログイン担当者種別
         */
        public void setLoginPersonType(String loginPersonType) {
            this.loginPersonType = loginPersonType;
        }

        /**
         *
         * 操作企業ID 取得
         * 
         * @return 操作企業ID
         */
        public String getOperationCorpId() {
            return operationCorpId;
        }

        /**
         *
         * 操作企業ID 設定
         * 
         * @param operationCorpId 操作企業ID
         */
        public void setOperationCorpId(String operationCorpId) {
            this.operationCorpId = operationCorpId;
        }

        /**
         *
         * 操作企業種別 取得
         * 
         * @return 操作企業種別
         */
        public String getOperationCorpType() {
            return operationCorpType;
        }

        /**
         *
         * 操作企業種別 設定
         * 
         * @param operationCorpType 操作企業種別
         */
        public void setOperationCorpType(String operationCorpType) {
            this.operationCorpType = operationCorpType;
        }

        /**
         *
         * 操作企業担当者権限種別 取得
         * 
         * @return 操作企業担当者権限種別
         */
        public String getOperationAuthorityType() {
            return operationAuthorityType;
        }

        /**
         *
         * 操作企業担当者権限種別 設定
         * 
         * @param operationAuthorityType 操作企業担当者権限種別
         */
        public void setOperationAuthorityType(String operationAuthorityType) {
            this.operationAuthorityType = operationAuthorityType;
        }

        /**
         *
         * 操作企業担当者権限コードリスト 取得
         * 
         * @return 操作企業担当者権限コードリスト
         */
        public List<String> getOperationAuthorityCdList() {
            return operationAuthorityCdList;
        }

        /**
         *
         * 操作企業担当者権限コードリスト 設定
         * 
         * @param operationAuthorityCdList 操作企業担当者権限コードリスト
         */
        public void setOperationAuthorityCdList(List<String> operationAuthorityCdList) {
            this.operationAuthorityCdList = operationAuthorityCdList;
        }
    }

    /**
     * 出力情報インナークラス
     */
    public class Output {

        private OsolAccessBean.FUNCTION_CD functionCd;

        private String loginCorpId;

        private String loginCorpType;

        private boolean functionEnable;
        
        private String roleGroupKey;

        private boolean roleGroupEnable;

        private LinkedHashMap<String, RoleResult> roleResultList;

        public Output() {
            this.functionCd = OsolAccessBean.FUNCTION_CD.NONE;
            this.loginCorpId = BaseConstants.STR_EMPTY;
            this.loginCorpType = BaseConstants.STR_EMPTY;
            this.functionEnable = false;
            this.roleGroupKey = BaseConstants.STR_EMPTY;
            this.roleGroupEnable = false;
            this.roleResultList = new LinkedHashMap<>();
        }

        public OsolAccessBean.FUNCTION_CD getFunctionCd() {
            return functionCd;
        }

        public void setFunctionCd(OsolAccessBean.FUNCTION_CD functionCd) {
            this.functionCd = functionCd;
        }

        public String getLoginCorpId() {
            return loginCorpId;
        }

        public void setLoginCorpId(String loginCorpId) {
            this.loginCorpId = loginCorpId;
        }

        public String getLoginCorpType() {
            return loginCorpType;
        }

        public void setLoginCorpType(String loginCorpType) {
            this.loginCorpType = loginCorpType;
        }

        public boolean isFunctionEnable() {
            return functionEnable;
        }

        public void setFunctionEnable(boolean functionEnable) {
            this.functionEnable = functionEnable;
        }

        public boolean isRoleGroupEnable() {
            return roleGroupEnable;
        }

        public void setRoleGroupEnable(boolean roleGroupEnable) {
            this.roleGroupEnable = roleGroupEnable;
        }

        public String getRoleGroupKey() {
            return roleGroupKey;
        }

        public void setRoleGroupKey(String roleGroupKey) {
            this.roleGroupKey = roleGroupKey;
        }

        public LinkedHashMap<String, RoleResult> getRoleResultList() {
            return roleResultList;
        }

        public void setRoleResultList(LinkedHashMap<String, RoleResult> roleResultList) {
            this.roleResultList = roleResultList;
        }

    }

    /**
     * 出力情報のロールチェック結果インナークラス
     */
    public class RoleResult {

        private String roleKey;

        private boolean roleEnable;

        private LoginModel login;

        private boolean loginCorpTypeEnable;

        private boolean loginPersonTypeEnable;

        private OperationModel operation;

        private boolean operationCorpTypeEnable;

        private boolean operationAuthorityTypeEnable;

        private boolean operationAuthorityCdEnable;

        public RoleResult(){
            this.roleKey = BaseConstants.STR_EMPTY;
            this.roleEnable = false;
            this.login = new LoginModel();
            this.loginCorpTypeEnable = false;
            this.loginPersonTypeEnable = false;
            this.operation = new OperationModel();
            this.operationCorpTypeEnable = false;
            this.operationAuthorityTypeEnable = false;
            this.operationAuthorityCdEnable = false;
        }

        public String getRoleKey() {
            return roleKey;
        }

        public void setRoleKey(String roleKey) {
            this.roleKey = roleKey;
        }

        public boolean isRoleEnable() {
            return roleEnable;
        }

        public void setRoleEnable(boolean roleEnable) {
            this.roleEnable = roleEnable;
        }

        public LoginModel getLogin() {
            return login;
        }

        public void setLogin(LoginModel login) {
            this.login = login;
        }

        public boolean isLoginCorpTypeEnable() {
            return loginCorpTypeEnable;
        }

        public void setLoginCorpTypeEnable(boolean loginCorpTypeEnable) {
            this.loginCorpTypeEnable = loginCorpTypeEnable;
        }

        public boolean isLoginPersonTypeEnable() {
            return loginPersonTypeEnable;
        }

        public void setLoginPersonTypeEnable(boolean loginPersonTypeEnable) {
            this.loginPersonTypeEnable = loginPersonTypeEnable;
        }

        public OperationModel getOperation() {
            return operation;
        }

        public void setOperation(OperationModel operation) {
            this.operation = operation;
        }

        public boolean isOperationCorpTypeEnable() {
            return operationCorpTypeEnable;
        }

        public void setOperationCorpTypeEnable(boolean operationCorpTypeEnable) {
            this.operationCorpTypeEnable = operationCorpTypeEnable;
        }

        public boolean isOperationAuthorityTypeEnable() {
            return operationAuthorityTypeEnable;
        }

        public void setOperationAuthorityTypeEnable(boolean operationAuthorityTypeEnable) {
            this.operationAuthorityTypeEnable = operationAuthorityTypeEnable;
        }

        public boolean isOperationAuthorityCdEnable() {
            return operationAuthorityCdEnable;
        }

        public void setOperationAuthorityCdEnable(boolean operationAuthorityCdEnable) {
            this.operationAuthorityCdEnable = operationAuthorityCdEnable;
        }

    }
}
