package jp.co.osaki.osol.api.dao.osolapi;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiIpAddrCheckFilterDaoImpl;
import jp.co.osaki.osol.entity.MLoginIpAddr;
import jp.co.osaki.osol.entity.MLoginIpAddrPK;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

@Stateless
public class OsolApiIpAddrCheckFilterDao extends BaseApiDao {

    private final OsolApiIpAddrCheckFilterDaoImpl osolApiIpAddrCheckFilterDaoImpl;

    public OsolApiIpAddrCheckFilterDao() {
        osolApiIpAddrCheckFilterDaoImpl = new OsolApiIpAddrCheckFilterDaoImpl();
    }
    /**
     * ログイン許可IPアドレス取得
     *
     * @param corp_id            企業ID
     * @param ipAddress          IPアドレス
     * @param loginPermitStatus  ログイン許可ステータス
     * @return ログイン許可IPアドレスリスト
     */
    public List<MLoginIpAddr> getLoginIpAddrList(String corp_id, String ipAddress, String loginPermitStatus, String loginPermitTarget) {

        // 担当者情報キーBeanにキーをセットする
        MLoginIpAddr mipa = new MLoginIpAddr();
        MLoginIpAddrPK mipapk = new MLoginIpAddrPK();
        mipapk.setCorpId(corp_id);
        mipa.setId(mipapk);
        mipa.setIpAddress(ipAddress);
        mipa.setLoginPermitStatus(loginPermitStatus);
        mipa.setLoginPermitTarget(loginPermitTarget);
        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        List<MLoginIpAddr> result = getResultList(osolApiIpAddrCheckFilterDaoImpl,mipa);

        return result;

    }

}
