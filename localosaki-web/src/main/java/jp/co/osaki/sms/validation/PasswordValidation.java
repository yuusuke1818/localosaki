package jp.co.osaki.sms.validation;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * パスワード用バリデーション
 *
 * @author s-takino
 */
public class PasswordValidation implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // 空だったら何もしない
        if (value == null) {
            return;
        }

        ValidatorException validatorException = new ValidatorException(new FacesMessage());
        final Map<String, Object> map = component.getAttributes();
        Integer minDigits = Integer.parseInt(map.get("minDigits").toString());
        Integer maxDigits = Integer.parseInt(map.get("maxDigits").toString());
        String valueStr = value.toString();

        if (false == passwordCheck(valueStr, minDigits, maxDigits)) {
            throw validatorException;
        }
    }

    public static boolean passwordCheck(String password, Integer minLength, Integer maxLength)
    {
        if (null == password) {
            return false;
        }

        // 文字数チェック
        if (password.length() < minLength || maxLength < password.length()) {
            return false;
        }

        // アルファベット、数字、記号以外の文字が含まれている
        if (!Pattern.compile("^[\\u0020-\\u007E]+$").matcher(password).find()) {
            return false;
        }

        int kindCount = 0;

        // 数値が含まれている
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            kindCount++;
        }

        // 英字が含まれている
        if (Pattern.compile("[A-Za-z]").matcher(password).find()) {
            kindCount++;
        }

        // 記号が含まれている
        if (Pattern.compile("[\\u0020-\\/:-@\\[-`{-~]").matcher(password).find()) {
            kindCount++;
        }

        // 2種類未満
        if (kindCount < 2) {
            return false;
        }

        return true;
    }
}