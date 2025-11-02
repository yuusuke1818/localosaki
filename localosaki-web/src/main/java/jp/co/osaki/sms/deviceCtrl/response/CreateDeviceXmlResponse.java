package jp.co.osaki.sms.deviceCtrl.response;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MManualInsp;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterLoadlimit;
import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandLoadSurveyMeter;
import jp.co.osaki.osol.entity.TCommandLoadSurveyTime;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;

public class CreateDeviceXmlResponse {

    public Element ainspResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getAinspdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);


        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element minspResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getMinspdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);


        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element dlsDataResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDlsdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element dlsMeterResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyMeter tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDlsmeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element dlsPeriodResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyTime tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDlsperiod);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        //返信用の子要素, テキストを追加
        Element startTime = responseDocument.createElement(DeviceCtrlConstants.startTime);
        data.appendChild(startTime);
        Text startTimeText = responseDocument.createTextNode(tCommand.getId().getStartTime());
        startTime.appendChild(startTimeText);

        //返信用の子要素, テキストを追加
        Element endTime = responseDocument.createElement(DeviceCtrlConstants.endTime);
        data.appendChild(endTime);
        Text endTimeText = responseDocument.createTextNode(tCommand.getId().getEndTime());
        endTime.appendChild(endTimeText);

        return command;
    }

    public Element dmvDataResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDmvdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);


        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element dmvMeterResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyMeter tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDmvmeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element dmvPeriodResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyTime tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getDmvperiod);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        //返信用の子要素, テキストを追加
        Element startTime = responseDocument.createElement(DeviceCtrlConstants.startTime);
        data.appendChild(startTime);
        Text startTimeText = responseDocument.createTextNode(tCommand.getId().getStartTime());
        startTime.appendChild(startTimeText);

        //返信用の子要素, テキストを追加
        Element endTime = responseDocument.createElement(DeviceCtrlConstants.endTime);
        data.appendChild(endTime);
        Text endTimeText = responseDocument.createTextNode(tCommand.getId().getEndTime());
        endTime.appendChild(endTimeText);


        return command;
    }

    public Element meterCtrlResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getMeterctrl);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getTag());
        meterMngId.appendChild(meterMngIdText);

        return command;
    }

    public Element meterInfoResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getMeterinfo);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getTag());
        meterMngId.appendChild(meterMngIdText);

        return command;
    }

    public Element meterValResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getMeterval);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getTag());
        meterMngId.appendChild(meterMngIdText);

        return command;
    }

    public Element rDlsDataResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdlsdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);


        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element rDlsMeterResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyMeter tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdlsmeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element rDlsPeriodResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyTime tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdlsperiod);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        //返信用の子要素, テキストを追加
        Element startTime = responseDocument.createElement(DeviceCtrlConstants.startTime);
        data.appendChild(startTime);
        Text startTimeText = responseDocument.createTextNode(tCommand.getId().getStartTime());
        startTime.appendChild(startTimeText);

        //返信用の子要素, テキストを追加
        Element endTime = responseDocument.createElement(DeviceCtrlConstants.endTime);
        data.appendChild(endTime);
        Text endTimeText = responseDocument.createTextNode(tCommand.getId().getEndTime());
        endTime.appendChild(endTimeText);

        return command;
    }

    public Element rDmvDataResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdmvdata);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);


        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getTag());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element rDmvMeterResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyMeter tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdmvmeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        return command;
    }

    public Element rDmvPeriodResponseTxt(Document responseDocument, Element command, TCommandLoadSurveyTime tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getRdmvperiod);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element requestDate = responseDocument.createElement(DeviceCtrlConstants.requestDate);
        data.appendChild(requestDate);
        Text requestDateText = responseDocument.createTextNode(tCommand.getId().getRequestDate());
        requestDate.appendChild(requestDateText);

        //返信用の子要素, テキストを追加
        Element startTime = responseDocument.createElement(DeviceCtrlConstants.startTime);
        data.appendChild(startTime);
        Text startTimeText = responseDocument.createTextNode(tCommand.getId().getStartTime());
        startTime.appendChild(startTimeText);

        //返信用の子要素, テキストを追加
        Element endTime = responseDocument.createElement(DeviceCtrlConstants.endTime);
        data.appendChild(endTime);
        Text endTimeText = responseDocument.createTextNode(tCommand.getId().getEndTime());
        endTime.appendChild(endTimeText);

        return command;
    }

    public Element getSpitConfResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.getSpitconf);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(tCommand.getTag());
        meterMngId.appendChild(meterMngIdText);

        return command;
    }

    public Element meterListResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.meterList);
        code.appendChild(codeText);

        return command;
    }

    public Element repeaterListResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.repeaterList);
        code.appendChild(codeText);

        return command;
    }

    public Element concentListResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.concentList);
        code.appendChild(codeText);

        return command;
    }

    public Element intervalResponseTxt(Document responseDocument, Element command, TCommand tCommand) {

        //返信用の子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.setInterval);
        code.appendChild(codeText);

        //返信用の子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element interval = responseDocument.createElement(DeviceCtrlConstants.interval);
        data.appendChild(interval);
        Text intervalText = responseDocument.createTextNode(tCommand.getTag());
        interval.appendChild(intervalText);

        return command;
    }

    public Element addConcentResponseTxt(Document responseDocument, Element command, MConcentrator mConcentrator) {

        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.addConcent);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のdm2_idの子要素, テキストを追加
        Element dm2Id = responseDocument.createElement(DeviceCtrlConstants.dm2Id);
        data.appendChild(dm2Id);
        Text dm2IdText = responseDocument.createTextNode(DeviceCtrlConstants.zero.toString());
        dm2Id.appendChild(dm2IdText);

        //返信用のconcent_idの子要素, テキストを追加
        Element concentId = responseDocument.createElement(DeviceCtrlConstants.concentId);
        data.appendChild(concentId);
        Text concentIdText = responseDocument.createTextNode(mConcentrator.getId().getConcentId().toString());
        concentId.appendChild(concentIdText);

        //返信用のip_addrの子要素, テキストを追加
        Element ipAddr = responseDocument.createElement(DeviceCtrlConstants.ipAddr);
        data.appendChild(ipAddr);
        Text ipAddrText = responseDocument.createTextNode(mConcentrator.getIpAddr());
        ipAddr.appendChild(ipAddrText);

        return command;
    }

    public Element initConcentResponseTxt(Document responseDocument, Element command, MConcentrator mConcentrator) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.initConcent);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のdm2_idの子要素, テキストを追加
        Element dm2Id = responseDocument.createElement(DeviceCtrlConstants.dm2Id);
        data.appendChild(dm2Id);
        Text dm2IdText = responseDocument.createTextNode(DeviceCtrlConstants.zero.toString());
        dm2Id.appendChild(dm2IdText);

        //返信用のconcent_idの子要素, テキストを追加
        Element concentId = responseDocument.createElement(DeviceCtrlConstants.concentId);
        data.appendChild(concentId);
        Text concentIdText = responseDocument.createTextNode(mConcentrator.getId().getConcentId().toString());
        concentId.appendChild(concentIdText);

        return command;
    }

    public Element delConcentResponseTxt(Document responseDocument, Element command, MConcentrator mConcentrator) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.delConcent);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のdm2_idの子要素, テキストを追加
        Element dm2Id = responseDocument.createElement(DeviceCtrlConstants.dm2Id);
        data.appendChild(dm2Id);
        Text dm2IdText = responseDocument.createTextNode(DeviceCtrlConstants.zero.toString());
        dm2Id.appendChild(dm2IdText);

        //返信用のconcent_idの子要素, テキストを追加
        Element concentId = responseDocument.createElement(DeviceCtrlConstants.concentId);
        data.appendChild(concentId);
        Text concentIdText = responseDocument.createTextNode(mConcentrator.getId().getConcentId().toString());
        concentId.appendChild(concentIdText);

        return command;
    }

    public Element addMeterResponseTxt(Document responseDocument, Element command, MMeter mMeter, String buildingNo) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.addMeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のdm2_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeter.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用のconcent_idの子要素, テキストを追加
        Element meterId = responseDocument.createElement(DeviceCtrlConstants.meterId);
        data.appendChild(meterId);
        Text meterIdText = responseDocument.createTextNode(mMeter.getMeterId());
        meterId.appendChild(meterIdText);

        //返信用のconcent_idの子要素, テキストを追加
        Element tenantId = responseDocument.createElement(DeviceCtrlConstants.tenantId);
        data.appendChild(tenantId);
        Text tenantIdText = responseDocument.createTextNode(buildingNo);
        tenantId.appendChild(tenantIdText);

        return command;
    }

    public Element chgMeterResponseTxt(Document responseDocument, Element command, MMeter mMeter) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.chgMeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のmeter_mng_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeter.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element oldMeterId = responseDocument.createElement(DeviceCtrlConstants.oldMeterId);
        data.appendChild(oldMeterId);
        Text oldMeterIdText = responseDocument.createTextNode(Objects.isNull(mMeter.getMeterIdOld()) ? "" : mMeter.getMeterIdOld());
        oldMeterId.appendChild(oldMeterIdText);

        //返信用のnew_meter_idの子要素, テキストを追加
        Element newMeterId = responseDocument.createElement(DeviceCtrlConstants.newMeterId);
        data.appendChild(newMeterId);
        Text newMeterIdText = responseDocument.createTextNode(mMeter.getMeterId());
        newMeterId.appendChild(newMeterIdText);

        //返信用のnew_if_typeの子要素, テキストを追加
        Element newIfType = responseDocument.createElement(DeviceCtrlConstants.newIfType);
        data.appendChild(newIfType);
        Text newIfTypeText = responseDocument.createTextNode(Objects.isNull(mMeter.getIfType()) ? "" : mMeter.getIfType().toString());
        newIfType.appendChild(newIfTypeText);

        return command;
    }

    public Element delMeterResponseTxt(Document responseDocument, Element command, MMeter mMeter) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.delMeter);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のdm2_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeter.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        return command;
    }

    public Element setSpitResponseTxt(Document responseDocument, Element command, MMeter mMeter, String buildingNo) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.setSpitconf);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のmeter_mng_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeter.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element meterId = responseDocument.createElement(DeviceCtrlConstants.meterId);
        data.appendChild(meterId);
        Text meterIdText = responseDocument.createTextNode(mMeter.getMeterId());
        meterId.appendChild(meterIdText);

        //返信用の子要素, テキストを追加
        Element tenantId = responseDocument.createElement(DeviceCtrlConstants.tenantId);
        data.appendChild(tenantId);
        Text tenantIdText = responseDocument.createTextNode(buildingNo);
        tenantId.appendChild(tenantIdText);

        //返信用の子要素, テキストを追加
        Element multi = responseDocument.createElement(DeviceCtrlConstants.multi);
        data.appendChild(multi);
        Text multiText = responseDocument.createTextNode(mMeter.getMulti().toString());
        multi.appendChild(multiText);

        //返信用の子要素, テキストを追加
        Element pulseWeight = responseDocument.createElement(DeviceCtrlConstants.pulseWeight);
        data.appendChild(pulseWeight);
        //5桁で返す
        DecimalFormat pulseWeightFormat = new DecimalFormat("#");
        pulseWeightFormat.setMinimumIntegerDigits(5);
        pulseWeightFormat.setMaximumIntegerDigits(5);
        Text pulseWeightText = null;
        if(mMeter.getPulseWeight() == null || mMeter.getPulseWeight().toString().length() == 0) {
            pulseWeightText = responseDocument.createTextNode(DeviceCtrlConstants.spFiveConstant);
        }else {
            pulseWeightText = responseDocument.createTextNode(pulseWeightFormat.format(mMeter.getPulseWeight()));
        }

        pulseWeight.appendChild(pulseWeightText);

        //返信用の子要素, テキストを追加
        Element pulseType = responseDocument.createElement(DeviceCtrlConstants.pulseType);
        data.appendChild(pulseType);
        Text pulseTypeText = null;
        if(mMeter.getPulseType() == null  || mMeter.getPulseType().toString().length() == 0) {
            pulseTypeText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
        }else {
            pulseTypeText = responseDocument.createTextNode(mMeter.getPulseType().toString());
        }
        pulseType.appendChild(pulseTypeText);

        Element currentData = responseDocument.createElement(DeviceCtrlConstants.currentData);
        data.appendChild(currentData);
        //8桁で返す
        DecimalFormat currentDataFormat = new DecimalFormat("#");
        currentDataFormat.setMinimumIntegerDigits(8);
        currentDataFormat.setMaximumIntegerDigits(8);
        //返信用の子要素, テキストを追加
        Text currentDataText = null;
        if(mMeter.getCurrentData() == null  || mMeter.getCurrentData().toString().length() == 0) {
            currentDataText = responseDocument.createTextNode(DeviceCtrlConstants.spEightConstant);
        }else {
            currentDataText = responseDocument.createTextNode(currentDataFormat.format(mMeter.getCurrentData()));
        }

        currentData.appendChild(currentDataText);

        return command;
    }

    public Element setBreakerResponseTxt(Document responseDocument, Element command, MMeter mMeter) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.setBreaker);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のmeter_mng_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeter.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);


        //返信用の子要素, テキストを追加
        Element mode = responseDocument.createElement(DeviceCtrlConstants.mode);
        data.appendChild(mode);
        if(mMeter.getOpenMode() == null) {
            Text modeText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
            mode.appendChild(modeText);
        }else {
            Text modeText = responseDocument.createTextNode(mMeter.getOpenMode());
            mode.appendChild(modeText);
        }
        return command;
    }

    public Element setLoadLimitResponseTxt(Document responseDocument, Element command, MMeterLoadlimit mMeterLoadlimit) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.setLoadlimit);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用のmeter_mng_idの子要素, テキストを追加
        Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
        data.appendChild(meterMngId);
        Text meterMngIdText = responseDocument.createTextNode(mMeterLoadlimit.getId().getMeterMngId().toString());
        meterMngId.appendChild(meterMngIdText);

        //返信用の子要素, テキストを追加
        Element mode = responseDocument.createElement(DeviceCtrlConstants.mode);
        data.appendChild(mode);
        if(mMeterLoadlimit.getLoadlimitMode() == null) {
            Text modeText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
            mode.appendChild(modeText);
        }else {
            Text modeText = responseDocument.createTextNode(mMeterLoadlimit.getLoadlimitMode());
            mode.appendChild(modeText);
        }

        if(mMeterLoadlimit.getLoadlimitMode().equals(DeviceCtrlConstants.A) || mMeterLoadlimit.getLoadlimitMode().equals(DeviceCtrlConstants.R)) {
            //返信用の子要素, テキストを追加
            Element loadCurrent = responseDocument.createElement(DeviceCtrlConstants.loadCurrent);
            data.appendChild(loadCurrent);

            if(mMeterLoadlimit.getTempLoadCurrent() == null) {
                Text loadCurrentText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
                loadCurrent.appendChild(loadCurrentText);
            }else {
                Text loadCurrentText = responseDocument.createTextNode(mMeterLoadlimit.getTempLoadCurrent());
                loadCurrent.appendChild(loadCurrentText);
            }


          //返信用の子要素, テキストを追加
            Element autoInjection = responseDocument.createElement(DeviceCtrlConstants.autoInjection);
            data.appendChild(autoInjection);

            if(mMeterLoadlimit.getTempAutoInjection() == null) {
                Text autoInjectionText = responseDocument.createTextNode(DeviceCtrlConstants.spThreeConstant);
                autoInjection.appendChild(autoInjectionText);
            }else {
                Text autoInjectionText = responseDocument.createTextNode(mMeterLoadlimit.getTempAutoInjection());
                autoInjection.appendChild(autoInjectionText);
            }


            //返信用の子要素, テキストを追加
              Element breakerActCount = responseDocument.createElement(DeviceCtrlConstants.breakerActCount);
              data.appendChild(breakerActCount);

              if(mMeterLoadlimit.getTempBreakerActCount() == null) {
                  Text breakerActCountText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
                  breakerActCount.appendChild(breakerActCountText);
              }else {
                  Text breakerActCountText = responseDocument.createTextNode(mMeterLoadlimit.getTempBreakerActCount());
                  breakerActCount.appendChild(breakerActCountText);
              }


              //返信用の子要素, テキストを追加
              Element countClear = responseDocument.createElement(DeviceCtrlConstants.countClear);
              data.appendChild(countClear);
              if(mMeterLoadlimit.getTempCountClear() == null) {
                  Text countClearText = responseDocument.createTextNode(DeviceCtrlConstants.spTwoConstant);
                  countClear.appendChild(countClearText);
              }else {
                  Text countClearText = responseDocument.createTextNode(mMeterLoadlimit.getTempCountClear());
                  countClear.appendChild(countClearText);
              }
        }else {
            //返信用の子要素, テキストを追加
            Element loadCurrent = responseDocument.createElement(DeviceCtrlConstants.loadCurrent);
            data.appendChild(loadCurrent);

            if(mMeterLoadlimit.getLoadCurrent() == null) {
                Text loadCurrentText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
                loadCurrent.appendChild(loadCurrentText);
            }else {
                Text loadCurrentText = responseDocument.createTextNode(mMeterLoadlimit.getLoadCurrent());
                loadCurrent.appendChild(loadCurrentText);
            }


          //返信用の子要素, テキストを追加
            Element autoInjection = responseDocument.createElement(DeviceCtrlConstants.autoInjection);
            data.appendChild(autoInjection);

            if(mMeterLoadlimit.getAutoInjection() == null) {
                Text autoInjectionText = responseDocument.createTextNode(DeviceCtrlConstants.spThreeConstant);
                autoInjection.appendChild(autoInjectionText);
            }else {
                Text autoInjectionText = responseDocument.createTextNode(mMeterLoadlimit.getAutoInjection());
                autoInjection.appendChild(autoInjectionText);
            }


            //返信用の子要素, テキストを追加
              Element breakerActCount = responseDocument.createElement(DeviceCtrlConstants.breakerActCount);
              data.appendChild(breakerActCount);

              if(mMeterLoadlimit.getBreakerActCount() == null) {
                  Text breakerActCountText = responseDocument.createTextNode(DeviceCtrlConstants.spOneConstant);
                  breakerActCount.appendChild(breakerActCountText);
              }else {
                  Text breakerActCountText = responseDocument.createTextNode(mMeterLoadlimit.getBreakerActCount());
                  breakerActCount.appendChild(breakerActCountText);
              }


              //返信用の子要素, テキストを追加
              Element countClear = responseDocument.createElement(DeviceCtrlConstants.countClear);
              data.appendChild(countClear);
              if(mMeterLoadlimit.getCountClear() == null) {
                  Text countClearText = responseDocument.createTextNode(DeviceCtrlConstants.spTwoConstant);
                  countClear.appendChild(countClearText);
              }else {
                  Text countClearText = responseDocument.createTextNode(mMeterLoadlimit.getCountClear());
                  countClear.appendChild(countClearText);
              }
        }


        return command;
    }

    public Element addRepeaterResponseTxt(Document responseDocument, Element command, MRepeater mRepeater) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.addRepeater);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element dm2Id = responseDocument.createElement(DeviceCtrlConstants.dm2Id);
        data.appendChild(dm2Id);
        Text dm2IdText = responseDocument.createTextNode(DeviceCtrlConstants.zero.toString());
        dm2Id.appendChild(dm2IdText);

        //返信用の子要素, テキストを追加
        Element repeaterMngId = responseDocument.createElement(DeviceCtrlConstants.repeaterMngId);
        data.appendChild(repeaterMngId);
        Text repeaterMngIdText = responseDocument.createTextNode(mRepeater.getId().getRepeaterMngId().toString());
        repeaterMngId.appendChild(repeaterMngIdText);

        //返信用の子要素, テキストを追加
        Element repeaterId = responseDocument.createElement(DeviceCtrlConstants.repeaterId);
        data.appendChild(repeaterId);
        Text repeaterIdText = responseDocument.createTextNode(mRepeater.getRepeaterId());
        repeaterId.appendChild(repeaterIdText);
        return command;
    }

    public Element delRepeaterResponseTxt(Document responseDocument, Element command, MRepeater mRepeater) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.delRepeater);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        //返信用の子要素, テキストを追加
        Element dm2Id = responseDocument.createElement(DeviceCtrlConstants.dm2Id);
        data.appendChild(dm2Id);
        Text dm2IdText = responseDocument.createTextNode(DeviceCtrlConstants.zero.toString());
        dm2Id.appendChild(dm2IdText);

        //返信用の子要素, テキストを追加
        Element repeaterMngId = responseDocument.createElement(DeviceCtrlConstants.repeaterMngId);
        data.appendChild(repeaterMngId);
        Text repeaterMngIdText = responseDocument.createTextNode(mRepeater.getId().getRepeaterMngId().toString());
        repeaterMngId.appendChild(repeaterMngIdText);

        return command;
    }

    public Element manualInspResponseTxt(Document responseDocument, Element command, List<MManualInsp> mManualInspList, TCommand tCommand) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.manualInspect);
        code.appendChild(codeText);

        //返信用のdateの子要素, テキストを追加
        Element data = responseDocument.createElement(DeviceCtrlConstants.data);
        command.appendChild(data);

        Element inspDate = responseDocument.createElement(DeviceCtrlConstants.inspDate);
        data.appendChild(inspDate);
        Text inspDateText = responseDocument.createTextNode(tCommand.getTag());
        inspDate.appendChild(inspDateText);

        Element inspPoint = responseDocument.createElement(DeviceCtrlConstants.inspPoint);
        data.appendChild(inspPoint);

        for(MManualInsp ret : mManualInspList) {
            //返信用のdm2_idの子要素, テキストを追加
            Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
            inspPoint.appendChild(meterMngId);
            Text meterMngIdText = responseDocument.createTextNode(ret.getId().getMeterMngId().toString());
            meterMngId.appendChild(meterMngIdText);

        }

        return command;
    }

    public Element endResponseTxt(Document responseDocument, Element command) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.end);
        code.appendChild(codeText);

        return command;
    }

    public Element retryResponseTxt(Document responseDocument, Element command) {
        //返信用のdateの子要素, テキストを追加
        Element code = responseDocument.createElement(DeviceCtrlConstants.code);
        command.appendChild(code);
        Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.end);
        code.appendChild(codeText);

        return command;
    }

}
