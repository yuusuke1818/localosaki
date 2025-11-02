package jp.co.osaki.sms.deviceCtrl.response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;

public class ErrorResponse {

    public Element responseTxt(String errorCode, Document responseDocument, Element command) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.error);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);
        Text errorText = responseDocument.createTextNode(errorCode);
        data.appendChild(errorText);

        return command;

    }
}
