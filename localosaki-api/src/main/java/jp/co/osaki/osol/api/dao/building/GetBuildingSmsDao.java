/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.building;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.building.GetBuildingSmsParameter;
import jp.co.osaki.osol.api.result.building.GetBuildingSmsResult;
import jp.co.osaki.osol.api.servicedao.entity.MBuildingSmsServiceDaoImpl;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;

/**
 * SMS建物情報 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class GetBuildingSmsDao extends OsolApiDao<GetBuildingSmsParameter> {

    private final MBuildingSmsServiceDaoImpl mBuildingSmsServiceDaoImpl;

    public GetBuildingSmsDao() {
        mBuildingSmsServiceDaoImpl = new MBuildingSmsServiceDaoImpl();
    }

    @Override
    public GetBuildingSmsResult query(GetBuildingSmsParameter parameter) throws Exception {
        MBuildingSmsPK targetPk = new MBuildingSmsPK();
        targetPk.setCorpId(parameter.getCorpId());
        targetPk.setBuildingId(parameter.getBuildingId());
        MBuildingSms target = new MBuildingSms();
        target.setId(targetPk);
        MBuildingSms result = find(mBuildingSmsServiceDaoImpl, target);
        if (result == null) {
            return null;
        }

        return new GetBuildingSmsResult(result);
    }
}
