package jp.co.osaki.sms.validation;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * 入力範囲指定の数値バリデーション
 *
 * @author s-takino
 */
public class RangeNumericValidation implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        // 空だったら何もしない
        if (value == null) {
            return;
        }

        ValidatorException validatorException = new ValidatorException(new FacesMessage());
        final Map<String, Object> map = component.getAttributes();
        Integer minValue = Integer.parseInt(map.get("minValue").toString());
        Integer maxValue = Integer.parseInt(map.get("maxValue").toString());
        String valueStr = value.toString();

        // 数字形式チェックの正規表現
        String numericFormat = "\\d+";

        if (!valueStr.matches(numericFormat)) {
            throw validatorException;
        }

        Integer intValue = Integer.valueOf(valueStr);

        // 数値が範囲外
        if (intValue < minValue || maxValue < intValue) {
            throw validatorException;
        }

    }

}
