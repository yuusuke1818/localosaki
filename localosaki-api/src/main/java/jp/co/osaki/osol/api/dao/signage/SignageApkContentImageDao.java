package jp.co.osaki.osol.api.dao.signage;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkContentImageParameter;
import jp.co.osaki.osol.api.result.signage.SignageApkContentImageResult;

/**
 * アプリからサイネージコンテンツの画像を取得する時に利用 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class SignageApkContentImageDao extends OsolApiDao<SignageApkContentImageParameter> {

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SignageApkContentImageResult query(SignageApkContentImageParameter parameter) throws Exception {
        return null;
    }

}
