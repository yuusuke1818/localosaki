/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.signage;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentIdParameter;
import jp.co.osaki.osol.api.result.signage.SignageContentIdResult;

/**
 * サイネージID取得 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class SignageContentIdDao extends OsolApiDao<SignageContentIdParameter> {

    /* (非 Javadoc)
    * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
    */
    @Override
    public SignageContentIdResult query(SignageContentIdParameter parameter) throws Exception {
        SignageContentIdResult result = new SignageContentIdResult();
        result.setSignageContentId(createId(OsolConstants.ID_SEQUENCE_NAME.SIGNAGE_CONTENTS_ID.getVal()));
        return result;
    }

}
