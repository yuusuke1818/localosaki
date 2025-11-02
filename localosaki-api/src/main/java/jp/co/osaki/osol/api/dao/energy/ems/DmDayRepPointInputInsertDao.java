/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.ems;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DmDayRepPointInputInsertParameter;
import jp.co.osaki.osol.api.request.energy.ems.DmDayRepPointInputInsertRequestSet;
import jp.co.osaki.osol.api.result.energy.ems.DmDayRepPointInputInsertResult;
import jp.co.osaki.osol.api.servicedao.energy.ems.DmDayRepPointInputDaoImpl;
import jp.co.osaki.osol.entity.TDmDayRepPointInput;
import jp.co.osaki.osol.entity.TDmDayRepPointInputPK;

/**
 * デマンド日報ポイント入力 Daoクラス
 *
 * @author y-maruta
 */
@Stateless
public class DmDayRepPointInputInsertDao extends OsolApiDao<DmDayRepPointInputInsertParameter> {

    private final DmDayRepPointInputDaoImpl dmDayRepPointInputDaoImpl;

    public DmDayRepPointInputInsertDao() {
        dmDayRepPointInputDaoImpl = new DmDayRepPointInputDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public DmDayRepPointInputInsertResult query(DmDayRepPointInputInsertParameter parameter) throws Exception {

      //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

      //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        List<DmDayRepPointInputInsertRequestSet> inputPointList;
        inputPointList = parameter.getDmDayRepPointInputInsertRequest().getInputPointList();

        for(DmDayRepPointInputInsertRequestSet pointData:inputPointList) {
            TDmDayRepPointInput param = new TDmDayRepPointInput();
            TDmDayRepPointInputPK idParam = new TDmDayRepPointInputPK();
            idParam.setCorpId(parameter.getOperationCorpId());
            idParam.setBuildingId(pointData.getBuildingId());
            idParam.setSmId(pointData.getSmId());
            idParam.setMeasurementDate(pointData.getMeasurementDate());
            idParam.setJigenNo(pointData.getJigenNo());
            idParam.setPointNo(pointData.getPointNo());
            idParam.setDmDayRepPointInputId(createId(OsolConstants.ID_SEQUENCE_NAME.DM_DAY_REP_POINT_INPUT_ID.getVal()));

            param.setId(idParam);
            param.setPointVal(pointData.getPointVal());
            param.setVersion(0);
            param.setCreateDate(serverDateTime);
            param.setCreateUserId(loginUserId);
            param.setUpdateDate(serverDateTime);
            param.setUpdateUserId(loginUserId);
            persist(dmDayRepPointInputDaoImpl,param);
        }

        return new DmDayRepPointInputInsertResult();
    }

}

