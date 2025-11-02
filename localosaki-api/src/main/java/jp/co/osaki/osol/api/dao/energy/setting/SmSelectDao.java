/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmSelectParameter;
import jp.co.osaki.osol.api.result.energy.setting.SmSelectResult;
import jp.co.osaki.osol.api.servicedao.energy.setting.SmSelectResultServiceDaoImpl;

/**
 * 機器情報取得処理 Daoクラス
 *
 * @author ya-ishida
 */
@Stateless
public class SmSelectDao extends OsolApiDao<SmSelectParameter> {

    private final SmSelectResultServiceDaoImpl smSelectResultServiceDaoImpl;

    public SmSelectDao() {
        smSelectResultServiceDaoImpl = new SmSelectResultServiceDaoImpl();
    }

    @Override
    public SmSelectResult query(SmSelectParameter parameter) throws Exception {

        SmSelectResult param = new SmSelectResult();
        param.setSmId(parameter.getSmId());
        List<SmSelectResult> list = getResultList(smSelectResultServiceDaoImpl, param);
        if (list == null || list.size() != 1) {
            return new SmSelectResult();
        }
        return list.get(0);
    }

}
