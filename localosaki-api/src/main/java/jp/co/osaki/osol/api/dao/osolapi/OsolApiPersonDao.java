package jp.co.osaki.osol.api.dao.osolapi;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.OsolApiLoginParameter;
import jp.co.osaki.osol.entity.MPerson;

/**
 *
 * OSOL API 担当者取得Dao
 *
 * @author take_suzuki
 *
 */
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class OsolApiPersonDao extends OsolApiDao<OsolApiLoginParameter> {

    /**
     * MPerson取得メソッド
     *
     * @param parameter OsolApiParameter
     * @return UserId
     */
    @Override
    public MPerson query(OsolApiLoginParameter parameter) throws Exception {

        return getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
    }
}
