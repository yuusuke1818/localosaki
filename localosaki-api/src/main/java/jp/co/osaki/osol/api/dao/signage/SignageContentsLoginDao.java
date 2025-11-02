/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.signage;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.servicedao.entity.MCorpApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPersonApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.skygroup.enl.webap.base.BaseDao;

/**
 *
 * @author n-takada
 */
@Stateless
public class SignageContentsLoginDao extends BaseDao {

    private final MCorpApiServiceDaoImpl corpImpl;

    private final MPersonApiServiceDaoImpl personImpl;

    private final TBuildingApiServiceDaoImpl buildingImpl;

    public SignageContentsLoginDao() {
        corpImpl = new MCorpApiServiceDaoImpl();
        personImpl = new MPersonApiServiceDaoImpl();
        buildingImpl = new TBuildingApiServiceDaoImpl();
    }

    /**
     *
     * @param corpId
     * @return
     */
    public MCorp findMCorp(String corpId) {
        MCorp entity = new MCorp();
        entity.setCorpId(corpId);
        return find(corpImpl, entity);
    }

    /**
     *
     * @param corpId
     * @param personId
     * @return
     */
    public MPerson findMPerson(String corpId, String personId) {
        MPerson entity = new MPerson();
        MPersonPK id = new MPersonPK();
        id.setCorpId(corpId);
        id.setPersonId(personId);
        entity.setId(id);
        return find(personImpl, entity);
    }
    
    /**
     *
     * @param person
     * @return
     */
    public MPerson mergeMPerson(MPerson person) {
        return merge(personImpl, person);
    }

    /**
     *
     * @param corpId
     * @param buildingNo
     * @return
     */
    public TBuilding findBuilding(String corpId, String buildingNo) {
        TBuilding entity = new TBuilding();
        TBuildingPK id = new TBuildingPK();
        id.setCorpId(corpId);
        entity.setId(id);
        entity.setBuildingNo(buildingNo);
        return find(buildingImpl, entity);
    }
}
