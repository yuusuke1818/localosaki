package jp.co.osaki.osol.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.dao.osolapi.OsolApiPersonDao;
import jp.co.osaki.osol.entity.MPerson;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;
import jp.skygroup.enl.webap.base.api.BaseApiParameter;

/**
 *
 * OSOL API Bean 共通クラス.
 *
 * @author take_suzuki
 *
 */
public abstract class OsolApiBean<P extends BaseApiParameter> {

    /**
     * エラー用ログ
     */
    protected static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    @EJB
    private OsolApiPersonDao osolApiPersonDao;

    /**
     * validator
     */
    protected static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 担当者取得メソッド
     *
     * @param parameter OsolApiParameter
     * @return
     * @return UserId
     */
    protected MPerson getPerson(OsolApiParameter osolApiParameter) {

        try {
            MPerson mPerson = osolApiPersonDao.query(osolApiParameter);
            return mPerson;
        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return null;
        }
    }

    /**
     * OsolApiParameterのコピー
     *
     * @param parameter OsolApiParameter
     * @return
     * @return UserId
     */
    protected void copyOsolApiParameter(OsolApiParameter fromApiParameter, OsolApiParameter toApiParameter) {

        toApiParameter.setBean(fromApiParameter.getBean());
        toApiParameter.setLoginCorpId(fromApiParameter.getLoginCorpId());
        toApiParameter.setLoginPersonId(fromApiParameter.getLoginPersonId());
        toApiParameter.setAuthHash(fromApiParameter.getAuthHash());
        toApiParameter.setMillisec(fromApiParameter.getMillisec());
        toApiParameter.setOperationCorpId(fromApiParameter.getOperationCorpId());
        toApiParameter.setApiKey(fromApiParameter.getApiKey());
        toApiParameter.setOemName(fromApiParameter.getOemName());
        toApiParameter.setInqury(fromApiParameter.getInqury());
        toApiParameter.setUserGuide(fromApiParameter.getUserGuide());
        toApiParameter.setUserGuidePdf(fromApiParameter.getUserGuidePdf());
        toApiParameter.setTermsOfService(fromApiParameter.getTermsOfService());
        toApiParameter.setTermsOfServicePdf(fromApiParameter.getTermsOfServicePdf());
        toApiParameter.setLogo(fromApiParameter.getLogo());
        toApiParameter.setPrivacy(fromApiParameter.getPrivacy());
        toApiParameter.setFavicon(fromApiParameter.getFavicon());
        toApiParameter.setCopyright(fromApiParameter.getCopyright());
    }

    /**
     * APIパラメータクラスのvalidate.
     *
     * @param parameter APIパラメータクラスのインスタンス
     * @return validateの結果
     */
    protected List<String> validate(P parameter){

        List<String> errorMessageList = new ArrayList<>();
        for (Iterator<ConstraintViolation<P>> iterator = validator.validate(parameter).iterator(); iterator
                .hasNext();) {
            ConstraintViolation<P> ite = iterator.next();
            errorMessageList.add(
            ite.getLeafBean().getClass().getSimpleName()
            .concat(".")
            .concat(ite.getPropertyPath().toString())
            .concat(" = ")
            .concat(StringUtils.defaultString((String)ite.getInvalidValue())
            .concat(" [")
            .concat(ite.getMessage())
            .concat("]")));
        }
        return errorMessageList;
    }

}
