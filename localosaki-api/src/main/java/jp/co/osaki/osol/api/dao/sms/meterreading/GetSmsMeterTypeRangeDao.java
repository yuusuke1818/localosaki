package jp.co.osaki.osol.api.dao.sms.meterreading;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsMeterTypeRangeParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.GetSmsMeterTypeRangeResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterRangeResultData;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.MeterTypeResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterRangeServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.MeterTypeServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterRange;
import jp.co.osaki.osol.entity.MMeterRangePK;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK;

/**
 * メータ種別設定情報 + メータ種別従量値情報 検索 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class GetSmsMeterTypeRangeDao extends OsolApiDao<GetSmsMeterTypeRangeParameter> {

    // 種別設定情報 取得・登録 処理
    private final MeterTypeServiceDaoImpl meterTypeServiceDaoImpl;

    // メータ種別従量値情報 取得・登録 処理
    private final MeterRangeServiceDaoImpl meterRangeServiceDaoImpl;

    public GetSmsMeterTypeRangeDao() {
        meterTypeServiceDaoImpl = new MeterTypeServiceDaoImpl();
        meterRangeServiceDaoImpl = new MeterRangeServiceDaoImpl();
    }

    @Override
    public GetSmsMeterTypeRangeResult query(GetSmsMeterTypeRangeParameter parameter) throws Exception {

        // 種別設定 検索条件設定
        MMeterTypePK targetPk = new MMeterTypePK();
        targetPk.setMeterType(parameter.getMeterType());      // メーター種別
        targetPk.setCorpId(parameter.getCorpId());            // 企業ID
        targetPk.setBuildingId(parameter.getBuildingId());    // 建物ID
        targetPk.setMenuNo(parameter.getMenuNo());            // メニュー番号

        MMeterType target = new MMeterType();
        target.setId(targetPk);

        // 種別設定を取得
        MMeterType entity = find(meterTypeServiceDaoImpl, target);
        if (entity == null) {
            return new GetSmsMeterTypeRangeResult(null, null);
        }


        // 種別従量値 検索条件設定
        MMeterRangePK targetPkMeterRange = new MMeterRangePK();
        targetPkMeterRange.setMeterType(parameter.getMeterType());    // メーター種別
        targetPkMeterRange.setCorpId(parameter.getCorpId());          // 企業ID
        targetPkMeterRange.setBuildingId(parameter.getBuildingId());  // 建物ID
        targetPkMeterRange.setMenuNo(parameter.getMenuNo());          // メニュー番号

        MMeterRange targetMeterRange = new MMeterRange();
        targetMeterRange.setId(targetPkMeterRange);

        // 種別従量値を取得
        List<MMeterRange> entityMeterRangeList = getResultList(meterRangeServiceDaoImpl, targetMeterRange);
        List<MeterRangeResultData> meterRangeResultDataList = null;
        if (entityMeterRangeList != null && !entityMeterRangeList.isEmpty()) {
            meterRangeResultDataList = new ArrayList<MeterRangeResultData>();
            for (MMeterRange entityMeterRange : entityMeterRangeList) {
                meterRangeResultDataList.add(new MeterRangeResultData(entityMeterRange));
            }
        }

        return new GetSmsMeterTypeRangeResult(new MeterTypeResultData(entity), meterRangeResultDataList);
    }

}
