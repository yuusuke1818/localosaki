package jp.co.osaki.sms.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class NotNullValidation implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        ValidatorException validatorException = new ValidatorException(new FacesMessage());
        if (value == null) {
            //入力済みの必須Inputを敢えて削除してsubmitし、バリデーションにひっかかった際に
            //valueがnullだとなぜか入力値が復活してしまい「○○を入力してください」のエラー表示にもかかわらず値が入っていて
            //ユーザーを困惑させる元となるので空文字を入れておくことで復活を阻止する
            if(component instanceof HtmlInputText){
                HtmlInputText inp = (HtmlInputText)component;
                inp.setValue("");
//                ValueExpression ve = inp.getValueExpression("value");
//                if(ve != null){
//                    ELContext elc = context.getELContext();
//                    ve.setValue(elc, "");
//                }
            }
            throw validatorException;
        }
        String valueStr = value.toString();
        if(valueStr.length() == 0){
            throw validatorException;
        }
    }

}
