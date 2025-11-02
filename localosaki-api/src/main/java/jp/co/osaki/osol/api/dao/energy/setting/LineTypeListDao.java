package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.energy.setting.LineTypeListParameter;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineTypeListDetailResult;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineTypeListServiceDaoImpl;

/**
 * 系統種別取得処理 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class LineTypeListDao extends OsolApiDao<LineTypeListParameter> {

    private final LineTypeListServiceDaoImpl lineTypeListServiceDaoImpl;

    public LineTypeListDao() {
        this.lineTypeListServiceDaoImpl = new LineTypeListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public List<LineTypeListDetailResult> query(LineTypeListParameter parameter) throws Exception {
        LineTypeListDetailResult param = new LineTypeListDetailResult();
        return getResultList(lineTypeListServiceDaoImpl, param);
    }

}
