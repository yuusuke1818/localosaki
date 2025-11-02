package jp.co.osaki.osol.api.dao.osolapi;

import java.sql.Timestamp;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.OsolApiParameter;

/**
 *
 * OSOL API DBサーバー時刻取得Dao クラス.
 *
 * @author take_suzuki
 *
 */
@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class OsolApiServerDateTimeDao extends OsolApiDao<OsolApiParameter> {

    public OsolApiServerDateTimeDao() {
    }

    /**
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public Timestamp query(OsolApiParameter parameter) {
        return this.getServerDateTime();
    }
}
