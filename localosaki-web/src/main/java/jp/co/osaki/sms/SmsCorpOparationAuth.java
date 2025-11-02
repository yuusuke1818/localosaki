package jp.co.osaki.sms;

import java.util.List;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;

/**
 *
 * 操作権限チェック
 *
 */
public final class SmsCorpOparationAuth extends OsolConstants {

    /**
     * 現在操作中企業での操作権限状況
     *
     * @param functionType 操作機能種別
     * @param loginUserCorpType         ログインユーザーの企業種別
     * @param loginUserPersonType       ログインユーザーの担当者種別
     * @param operationCorpSelected     ログインユーザーが現在操作している担当企業の権限種別
     * @param operationCorpCorpType     操作企業の企業種別   ※ログインユーザーの企業種別
     * @param operationCorpAuthorityCd  操作企業の担当者権限
     * @return 操作機能権限
     */
    public static SmsConstants.OPARATION_FUNCTION_RESULT getCorpOparationAuth(
            SmsConstants.OPARATION_FUNCTION_TYPE functionType,
            String loginUserCorpType,
            String loginUserPersonType,
            String operationCorpAuthorityType,
            String operationCorpCorpType,
            List<CorpPersonAuthResultSet> operationCorpAuthorityCd) {

        boolean smsAuth = false;
        boolean ocrAuth = false;

        // ログインユーザーの企業種別:大崎電気
        if (loginUserCorpType.equals(OsolConstants.CORP_TYPE.OSAKI.getVal())) {
            switch (functionType) {
                case SMS_SERVER_SETTING:    // SMSサーバー設定
                    // 大崎権限：管理者の場合のみ表示
                    if (OsolConstants.PERSON_TYPE.ADMIN.getVal().equals(loginUserPersonType)) {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                    } else {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    }
                default:
                    // 大崎権限ユーザーは全ての機能許可
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
            }

        }

        // メンテナンス権限ユーザーは特定処理を行わない（ログインできないので必要ないが念のため）
        if (loginUserCorpType.equals(OsolConstants.CORP_TYPE.MAINTENANCE.getVal())) {
            return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
        }

        // パートナー権限ユーザー
        if (loginUserCorpType.equals(OsolConstants.CORP_TYPE.PARTNER.getVal())) {
            // 企業担当
            if (OsolConstants.AUTHORITY_TYPE.CORP.getVal().equals(operationCorpAuthorityType)) {
                switch (functionType) {
                    case SMS_SERVER_SETTING:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    case DATA_COLLECT_DEVICE:
                    case DAY_LOAD:
                    case INSP_DATA:
                    case MANAGEMENT:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                    case SETTING_BULK:  // 設定一括収集のみ許可しない
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    default:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                }
            }
            // 建物・テナント担当
            if (OsolConstants.AUTHORITY_TYPE.BUILDING.getVal().equals(operationCorpAuthorityType)) {
                String authCd;
                switch (functionType) {
                    case SMS_SERVER_SETTING:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    case DATA_COLLECT_DEVICE:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                    case DATA_VIEW:     // データ表示はユーザー権限にSMSがあるかのみ
                        for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                            // SMS
                            if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                                }
                            }
                        }
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    case DAY_LOAD:     // 日報データ系
                        for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                            // SMS
                            if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    smsAuth = true;
                                }
                            }else if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    ocrAuth = true;
                                }
                            }
                        }
                        if(ocrAuth) {
                            return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                        }else if(smsAuth) {
                            return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                        }else {
                            return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                        }
                    case INSP_DATA:     // 検針データ
                        for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                            // SMS
                            if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                                }
                            }
                        }
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    case MANAGEMENT:
                        authCd = OsolConstants.USER_AUTHORITY.BUILDING.getVal();
                        break;
                    case METER:
                    case METER_TENANT:
                    case MANUAL_INSPECTION:
                        for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                            // SMS
                            if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                                }
                            }
                        }
                    case INSPECTION_METER_BEF:
                        authCd = OsolConstants.USER_AUTHORITY.BUILDING.getVal();
                        break;
                    case NONE:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    default:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                }

                boolean userAuthBuilding = false;
                boolean userAuthOcr = false;

                for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                    // 建物・設備管理
                    if (authCd.equals(auth.getOperationAuthorityCd())) {
                        if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                            userAuthBuilding = true;
                        }
                    }
                    //OCR
                    if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(auth.getOperationAuthorityCd())) {
                        if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                            userAuthOcr = true;
                        }
                    }
                }

                // 建物・設備管理が利用可で表示
                if (userAuthBuilding) {
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                } else {
                    //OCR権限あれば利用可
                    if (userAuthOcr) {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                    } else {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    }
                }
            }
        }

        // 契約企業権限ユーザー
        if (loginUserCorpType.equals(OsolConstants.CORP_TYPE.CONTRACT.getVal())) {
            // 管理者権限
            if (loginUserPersonType.equals(OsolConstants.PERSON_TYPE.ADMIN.getVal())) {
                switch (functionType) {
                case SMS_SERVER_SETTING:
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                case DATA_COLLECT_DEVICE:
                case DAY_LOAD:
                case INSP_DATA:
                case MANAGEMENT:
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                case SETTING_BULK:  // 設定一括収集のみ許可しない
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                default:
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                }
            }
            // 一般ユーザー
            if (loginUserPersonType.equals(OsolConstants.PERSON_TYPE.PERSON.getVal())) {
                String authCd;
                switch (functionType) {
                case SMS_SERVER_SETTING:
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                case DATA_COLLECT_DEVICE:
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                case DATA_VIEW:     // データ表示はユーザー権限にSMSがあるかのみ
                    for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                        // SMS
                        if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                            if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                            }
                        }
                    }
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                case DAY_LOAD:     // 日報データ系
                    for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                        // SMS
                        if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                            if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                smsAuth = true;
                            }
                        }else if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(auth.getOperationAuthorityCd())) {
                            if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                ocrAuth = true;
                            }
                        }
                    }
                    if(ocrAuth) {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    }else if(smsAuth) {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                    }else {
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    }
                case INSP_DATA:     // 検針データ
                    for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                        // SMS
                        if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(auth.getOperationAuthorityCd())) {
                            if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                            }
                        }
                    }
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    case MANAGEMENT:
                        authCd = OsolConstants.USER_AUTHORITY.BUILDING.getVal();
                        break;
                    case METER:
                    case METER_TENANT:
                    case MANUAL_INSPECTION:
                        for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                            // SMS
                            if (OsolConstants.USER_AUTHORITY.OCR.getVal().equals(auth.getOperationAuthorityCd())) {
                                if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                                }
                            }
                        }
                    case INSPECTION_METER_BEF:
                        authCd = OsolConstants.USER_AUTHORITY.BUILDING.getVal();
                        break;
                    case NONE:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                    default:
                        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                }

                boolean userAuthBuilding = false;

                for (CorpPersonAuthResultSet auth : operationCorpAuthorityCd) {
                    // 建物・設備管理
                    if (authCd.equals(auth.getOperationAuthorityCd())) {
                        if(auth.getOperationAuthorityFlg() == OsolConstants.FLG_ON){
                            userAuthBuilding = true;
                        }
                    }
                }

                // 建物・設備管理が利用可で表示
                if (userAuthBuilding) {
                    return SmsConstants.OPARATION_FUNCTION_RESULT.ENABLE;
                } else {
                    return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
                }
            }
        }
        // 例外
        return SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE;
    }
}
