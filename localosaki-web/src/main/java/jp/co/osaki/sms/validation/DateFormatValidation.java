package jp.co.osaki.sms.validation;

import java.util.Date;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import jp.co.osaki.osol.utility.DateUtility;

/**
 * 日付形式バリデーション
 *
 * @author s-takino
 */
public class DateFormatValidation implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // 空だったら何もしない
        if (value == null) {
            return;
        }

        ValidatorException validatorException = new ValidatorException(new FacesMessage());
        final Map<String, Object> map = component.getAttributes();
        String format = map.get("format").toString();
        String valueStr = value.toString();

        // フォーマット指定なしは想定していない。エラーとする
        if (format == null) {
            throw validatorException;
        }

        String pattern;

        switch (format) {
            case DateUtility.DATE_FORMAT_YYYYMM_SLASH:
                pattern = "^\\d{4}/\\d{2}$";
                break;
            case DateUtility.DATE_FORMAT_YYYYMMDD_SLASH:
                pattern = "^\\d{4}/\\d{2}/\\d{2}$";
                break;
            default:
                // format の指定が間違っている
                throw validatorException;
        }

        // 形式チェック（全角や桁数をチェック）
        if (!valueStr.matches(pattern)) {
            throw validatorException;
        }

        // 日付形式に変換可能かチェック（13月などのありえない日付をチェック）
        Date date = DateUtility.conversionDate(valueStr, format);

        if (date == null) {
            throw validatorException;
        }

    }

}
