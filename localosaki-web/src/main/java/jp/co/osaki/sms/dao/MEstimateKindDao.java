package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MEstimateKind;
import jp.co.osaki.osol.entity.MEstimateKindPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MEstimateKindServiceDaoImpl;

/**
 *
 * @author h-shiba
 */
@Stateless
public class MEstimateKindDao extends SmsDao {

    private final MEstimateKindServiceDaoImpl impl;

    public MEstimateKindDao() {
        this.impl = new MEstimateKindServiceDaoImpl();
    }

    /**
     * 推計種別情報取得
     *
     * @param estimate_id
     * @return
     */
    public MEstimateKind find(Long estimate_id) {

        MEstimateKind mek = new MEstimateKind();
        MEstimateKindPK mekpk = new MEstimateKindPK();
        mekpk.setEstimateId(estimate_id);
        mek.setId(mekpk);
        MEstimateKind result = find(impl, mek);
        return result;

    }

    /**
     * 推計種別情報リスト取得
     *
     * @param corpId
     * @return
     */
    public List<MEstimateKind> getResultList(String corpId) {
        MEstimateKind mek = new MEstimateKind();
        MEstimateKindPK mekId = new MEstimateKindPK();
        mekId.setCorpId(corpId);
        mek.setId(mekId);
        return getResultList(impl, mek);
    }
}
