package jp.co.osaki.osol.api.utility.energy.setting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.energy.setting.ProductControlLoadFlgSeparateListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadFlgSeparateListFlgResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;

/**
 * 製品制御負荷 制御フラグ別リスト Utilityクラス
 * @author ya-ishida
 *
 */
public class ProductControlLoadFlgSeparateUtility {

    /**
     * 取得した製品制御負荷を制御フラグ別に分割する
     * @param productControlLoadList
     * @return
     */
    public static ProductControlLoadFlgSeparateListResult setProductControlLoadFlgSeparateList(List<ProductControlLoadListDetailResultData> productControlLoadList) {

        ProductControlLoadFlgSeparateListResult result = new ProductControlLoadFlgSeparateListResult();
        ProductControlLoadFlgSeparateListFlgResultData manualControlFlgData = new ProductControlLoadFlgSeparateListFlgResultData();
        ProductControlLoadFlgSeparateListFlgResultData demandControlFlgData = new ProductControlLoadFlgSeparateListFlgResultData();
        ProductControlLoadFlgSeparateListFlgResultData eventControlFlgData = new ProductControlLoadFlgSeparateListFlgResultData();
        ProductControlLoadFlgSeparateListFlgResultData scheduleControlFlgData = new ProductControlLoadFlgSeparateListFlgResultData();
        List<ProductControlLoadListDetailResultData> manualControlList = new ArrayList<>();
        List<ProductControlLoadListDetailResultData> demandControlList = new ArrayList<>();
        List<ProductControlLoadListDetailResultData> eventControlList = new ArrayList<>();
        List<ProductControlLoadListDetailResultData> scheduleControlList = new ArrayList<>();

        if(productControlLoadList == null || productControlLoadList.isEmpty()) {
            //製品制御負荷情報が取得できない場合は処理を終了
            return result;
        } else {
            //制御負荷順にソート
            productControlLoadList = productControlLoadList.stream()
                    .sorted(Comparator.comparing(ProductControlLoadListDetailResultData::getControlLoad,Comparator.naturalOrder()))
                    .collect(Collectors.toList());
        }

        for(ProductControlLoadListDetailResultData productControlLoad : productControlLoadList) {
            //各制御負荷の値を見て、有効なデータのみデータを設定する
            //手動負荷制御フラグ
            if(productControlLoad.getManualLoadControlFlg().equals(OsolConstants.FLG_ON)) {
                manualControlList.add(productControlLoad);
            }
            //デマンド制御フラグ
            if(productControlLoad.getDemandControlFlg().equals(OsolConstants.FLG_ON)) {
                demandControlList.add(productControlLoad);
            }
            //イベント制御フラグ
            if(productControlLoad.getEventControlFlg().equals(OsolConstants.FLG_ON)) {
                eventControlList.add(productControlLoad);
            }
            //スケジュール制御フラグ
            if(productControlLoad.getScheduleControlFlg().equals(OsolConstants.FLG_ON)) {
                scheduleControlList.add(productControlLoad);
            }
        }

        //各データの件数をセットし、各データに詰める
        manualControlFlgData.setControlLoadCount(manualControlList.size());
        manualControlFlgData.setProductControlLoadList(manualControlList);
        demandControlFlgData.setControlLoadCount(demandControlList.size());
        demandControlFlgData.setProductControlLoadList(demandControlList);
        eventControlFlgData.setControlLoadCount(eventControlList.size());
        eventControlFlgData.setProductControlLoadList(eventControlList);
        scheduleControlFlgData.setControlLoadCount(scheduleControlList.size());
        scheduleControlFlgData.setProductControlLoadList(scheduleControlList);

        //詰めたデータをresultにセット
        result.setManualControlFlgData(manualControlFlgData);
        result.setDemandControlFlgData(demandControlFlgData);
        result.setEventControlFlgData(eventControlFlgData);
        result.setScheduleControlFlgData(scheduleControlFlgData);

        return result;
    }

}
