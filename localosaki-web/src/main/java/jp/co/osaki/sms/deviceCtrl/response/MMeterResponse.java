package jp.co.osaki.sms.deviceCtrl.response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSmPK;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TInspectionMeter;
import jp.co.osaki.osol.entity.TInspectionMeterPK;
import jp.co.osaki.sms.bean.deviceCtrl.DeviceCtrlConstants;
import jp.co.osaki.sms.dao.TInspectionMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MTenantSmsDao;
import jp.co.osaki.sms.deviceCtrl.dao.TBuildDevMeterRelationDao;
import jp.co.osaki.sms.deviceCtrl.dao.TBuildingDao;
import jp.co.osaki.sms.deviceCtrl.resultset.MMeterResultSet;
import jp.co.osaki.sms.deviceCtrl.resultset.TRfMeterInfoResultSet;
import jp.skygroup.enl.webap.base.BaseConstants;

public class MMeterResponse {
    /**
     * エラーログ
     */
    private static Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    public Element sendMeterRfInfo(MMeterDao mMeterDao, TInspectionMeterDao tInspectionMeterDao, Document responseDocument, Element command, MMeterResultSet mMeterResultSet, TBuildingDao tBuildingDao, TBuildDevMeterRelationDao tBuildDevMeterRelationDao, MTenantSmsDao mTenantSmsDao) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        MMeter entity = new MMeter();
        List<MMeter> list = new ArrayList<>();
        MMeterPK id = new MMeterPK();
        id.setDevId(mMeterResultSet.getDevId());
        entity.setId(id);
        //削除されていないものを取得する
        entity.setDelFlg(DeviceCtrlConstants.zero);

        TInspectionMeter entityT = new TInspectionMeter();
        List<TInspectionMeter> listT = new ArrayList<>();
        TInspectionMeterPK idT = new TInspectionMeterPK();
        idT.setDevId(mMeterResultSet.getDevId());
        entityT.setId(idT);

        List<TRfMeterInfoResultSet> listTRf = new ArrayList<>();

        try {
            list.addAll(mMeterDao.getMMeterList(entity));
            listT.addAll(tInspectionMeterDao.getTInspectionMeterList(entityT));
            //ループを限界まで減らす
            int j=0;
            for(int i=0;i<list.size();i++) {
                TBuilding retTBuilding = new TBuilding();
                retTBuilding = searchTenant(list.get(i), tBuildingDao, tBuildDevMeterRelationDao);

                MTenantSm mTenantSm = new MTenantSm();
                mTenantSm = searchTenant(mTenantSmsDao, retTBuilding);

                TRfMeterInfoResultSet tRfMeterInfoResultSet = new TRfMeterInfoResultSet();

                tRfMeterInfoResultSet.setMeterMngId(list.get(i).getId().getMeterMngId());
                tRfMeterInfoResultSet.setMeterType(list.get(i).getMeterType());
                tRfMeterInfoResultSet.setMeterId(list.get(i).getMeterId());
                tRfMeterInfoResultSet.setTenantId(mTenantSm.getTenantId().toString());
                tRfMeterInfoResultSet.setBuildingName(retTBuilding.getBuildingName());
                if(retTBuilding.getAddress() == null) {
                    tRfMeterInfoResultSet.setAddress1("");
                }else {
                    tRfMeterInfoResultSet.setAddress1(retTBuilding.getAddress());
                }

                if(mTenantSm.getAddress2() == null) {
                    tRfMeterInfoResultSet.setAddress2("");
                }else {
                    tRfMeterInfoResultSet.setAddress2(mTenantSm.getAddress2());
                }

                tRfMeterInfoResultSet.setWirelessId(list.get(i).getWirelessId());

                if(list.get(i).getHop1Id() == null) {
                    tRfMeterInfoResultSet.setHop1Id("");
                }else {
                    tRfMeterInfoResultSet.setHop1Id(list.get(i).getHop1Id());
                }

                if(list.get(i).getHop2Id() == null) {
                    tRfMeterInfoResultSet.setHop2Id("");
                }else {
                    tRfMeterInfoResultSet.setHop2Id(list.get(i).getHop2Id());
                }

                if(list.get(i).getHop3Id() == null) {
                    tRfMeterInfoResultSet.setHop3Id("");
                }else {
                    tRfMeterInfoResultSet.setHop3Id(list.get(i).getHop3Id());
                }

                if(list.get(i).getPollingId() == null) {
                    tRfMeterInfoResultSet.setPollingId("");
                }else {
                    tRfMeterInfoResultSet.setPollingId(list.get(i).getPollingId());
                }

                tRfMeterInfoResultSet.setMulti(list.get(i).getMulti());

                //2重ループだと遅いが一旦これで...一度見た部分は観ないようにjの値を使いまわす。
                for(;j<listT.size();j++) {
                    //一度setしたらもう見ない(取得時にソートしてるので最新値は一番最初に来るようにしてる。)
                    if(tRfMeterInfoResultSet.getPrevInspVal() != null) {
                        break;
                    }
                    if(tRfMeterInfoResultSet.getMeterMngId().equals(listT.get(j).getId().getMeterMngId())) {
                        tRfMeterInfoResultSet.setPrevInspDate(listT.get(j).getPrevInspDate());
                        tRfMeterInfoResultSet.setPrevInspVal(listT.get(j).getPrevInspVal());
                    }


                }

                listTRf.add(tRfMeterInfoResultSet);

            }
            //返信用のdateの子要素, テキストを追加
            Element code = responseDocument.createElement(DeviceCtrlConstants.code);
            command.appendChild(code);
            Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.setRfmeterinfo);
            code.appendChild(codeText);

            //返信用のdateの子要素, テキストを追加
            Element data = responseDocument.createElement(DeviceCtrlConstants.data);
            command.appendChild(data);

            Element dataCount = responseDocument.createElement(DeviceCtrlConstants.dataCount);
            data.appendChild(dataCount);
            Text dataCountText = responseDocument.createTextNode(String.valueOf(listTRf.size()));
            dataCount.appendChild(dataCountText);

            for(TRfMeterInfoResultSet tRfMeterInfoResultSet : listTRf) {

                Element meterData = responseDocument.createElement(DeviceCtrlConstants.meterData);
                data.appendChild(meterData);

                //返信用のmeter_mng_idの子要素, テキストを追加
                Element meterMngId = responseDocument.createElement(DeviceCtrlConstants.meterMngId);
                meterData.appendChild(meterMngId);
                Text meterMngIdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getMeterMngId().toString());
                meterMngId.appendChild(meterMngIdText);

                Element meterType = responseDocument.createElement(DeviceCtrlConstants.meterType);
                meterData.appendChild(meterType);
                Text meterTypeText = responseDocument.createTextNode(tRfMeterInfoResultSet.getMeterType().toString());
                meterType.appendChild(meterTypeText);

                Element meterId = responseDocument.createElement(DeviceCtrlConstants.meterId);
                meterData.appendChild(meterId);
                Text meterIdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getMeterId());
                meterId.appendChild(meterIdText);

                Element tenantId = responseDocument.createElement(DeviceCtrlConstants.tenantId);
                meterData.appendChild(tenantId);
                Text tenantIdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getTenantId().toString());
                tenantId.appendChild(tenantIdText);

                Element name = responseDocument.createElement(DeviceCtrlConstants.name);
                meterData.appendChild(name);
                Text nameText = responseDocument.createTextNode(tRfMeterInfoResultSet.getBuildingName());
                name.appendChild(nameText);

                Element address1 = responseDocument.createElement(DeviceCtrlConstants.address1);
                meterData.appendChild(address1);
                Text address1Text = responseDocument.createTextNode(tRfMeterInfoResultSet.getAddress1());
                address1.appendChild(address1Text);

                Element address2 = responseDocument.createElement(DeviceCtrlConstants.address2);
                meterData.appendChild(address2);
                Text address2Text = responseDocument.createTextNode(tRfMeterInfoResultSet.getAddress2());
                address2.appendChild(address2Text);

                Element wirelessId = responseDocument.createElement(DeviceCtrlConstants.wirelessId);
                meterData.appendChild(wirelessId);
                Text wirelessIdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getWirelessId());
                wirelessId.appendChild(wirelessIdText);

                Element hop1Id = responseDocument.createElement(DeviceCtrlConstants.hop1Id);
                meterData.appendChild(hop1Id);
                Text hop1IdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getHop1Id());
                hop1Id.appendChild(hop1IdText);

                Element hop2Id = responseDocument.createElement(DeviceCtrlConstants.hop2Id);
                meterData.appendChild(hop2Id);
                Text hop2IdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getHop2Id());
                hop2Id.appendChild(hop2IdText);

                Element hop3Id = responseDocument.createElement(DeviceCtrlConstants.hop3Id);
                meterData.appendChild(hop3Id);
                Text hop3IdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getHop3Id());
                hop3Id.appendChild(hop3IdText);

                Element pollindId = responseDocument.createElement(DeviceCtrlConstants.pollingId);
                meterData.appendChild(pollindId);
                Text pollindIdText = responseDocument.createTextNode(tRfMeterInfoResultSet.getPollingId());
                pollindId.appendChild(pollindIdText);

                Element multi = responseDocument.createElement(DeviceCtrlConstants.multi);
                meterData.appendChild(multi);
                Text multiText = responseDocument.createTextNode(tRfMeterInfoResultSet.getMulti().toString());
                multi.appendChild(multiText);

                if(tRfMeterInfoResultSet.getPrevInspVal() == null) {
                    Element prevInspVal = responseDocument.createElement(DeviceCtrlConstants.prevInspVal);
                    meterData.appendChild(prevInspVal);
                }else {
                    Element prevInspVal = responseDocument.createElement(DeviceCtrlConstants.prevInspVal);
                    meterData.appendChild(prevInspVal);
                    Text prevInspValText = responseDocument.createTextNode(tRfMeterInfoResultSet.getPrevInspVal());
                    prevInspVal.appendChild(prevInspValText);
                }

                if(tRfMeterInfoResultSet.getPrevInspDate() == null) {
                    Element PrevInspDate = responseDocument.createElement(DeviceCtrlConstants.prevInspDate);
                    meterData.appendChild(PrevInspDate);
                }else {
                    Element PrevInspDate = responseDocument.createElement(DeviceCtrlConstants.prevInspDate);
                    meterData.appendChild(PrevInspDate);
                    Date prevDate = new Date(tRfMeterInfoResultSet.getPrevInspDate().getTime());
                    Text PrevInspDateText = responseDocument.createTextNode(sdFormat.format(prevDate));
                    PrevInspDate.appendChild(PrevInspDateText);
                }
            }

        }catch(Exception e) {
            Element code = responseDocument.createElement(DeviceCtrlConstants.code);
            command.appendChild(code);
            Text codeText = responseDocument.createTextNode(DeviceCtrlConstants.what);
            code.appendChild(codeText);

            return command;
        }

        return command;

    }

    /**
     * メーターがひもづいている
     * テナントの検索(m_tenant_smsから)
     * @param mMeter
     * @return
     */
    public MTenantSm searchTenant(MTenantSmsDao mTenantSmsDao,  TBuilding tBuilding) {

        MTenantSm mTenantSm = new MTenantSm();
        MTenantSmPK mTenantSmPK = new MTenantSmPK();

        mTenantSmPK.setBuildingId(tBuilding.getId().getBuildingId());
        mTenantSmPK.setCorpId(tBuilding.getId().getCorpId());
        mTenantSm.setId(mTenantSmPK);

        if(!mTenantSmsDao.isNull(mTenantSm)) {
            return mTenantSmsDao.find(mTenantSm);
        }

        //テナントがなければnullで返却
        return null;
    }

    /**
     * メーターがひもづいている
     * テナントの検索(TBuildingから)
     * @param mMeter
     * @return
     */
    public TBuilding searchTenant(MMeter mMeter,  TBuildingDao tBuildingDao, TBuildDevMeterRelationDao tBuildDevMeterRelationDao) {
        List<TBuildDevMeterRelation> list = new ArrayList<>();
        TBuildDevMeterRelation tBuildDevMeterRelation = new TBuildDevMeterRelation();
        TBuildDevMeterRelationPK tBuildDevMeterRelationPK = new TBuildDevMeterRelationPK();

        tBuildDevMeterRelationPK.setDevId(mMeter.getId().getDevId());
        tBuildDevMeterRelationPK.setMeterMngId(mMeter.getId().getMeterMngId());
        tBuildDevMeterRelation.setId(tBuildDevMeterRelationPK);

        if(!tBuildDevMeterRelationDao.isNull(tBuildDevMeterRelation)) {
            list.addAll(tBuildDevMeterRelationDao.getResultList(tBuildDevMeterRelation));
            for(TBuildDevMeterRelation retTBuildDevMeterRelation : list) {
                TBuilding tBuilding = new TBuilding();
                TBuildingPK tBuildingPK = new TBuildingPK();
                tBuildingPK.setCorpId(retTBuildDevMeterRelation.getId().getCorpId());
                tBuildingPK.setBuildingId(retTBuildDevMeterRelation.getId().getBuildingId());
                tBuilding.setId(tBuildingPK);
                tBuilding = tBuildingDao.find(tBuilding);

                //テナントで登録されている者
                if(tBuilding.getBuildingType().equals(DeviceCtrlConstants.one.toString())) {
                    return tBuilding;
                }

            }
        }

        //テナントがなければnullで返却
        return null;
    }

}
