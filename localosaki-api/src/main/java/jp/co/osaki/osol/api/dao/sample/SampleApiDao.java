package jp.co.osaki.osol.api.dao.sample;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sample.SampleApiParameter;
import jp.co.osaki.osol.api.resultdata.sample.SampleApiResultData;
import jp.co.osaki.osol.api.servicedao.sample.SampleApiServiceDaoImpl;

/**
 *
 * Sample Api Daoクラス.
 *
 * @author take_suzuki
 *
 */
@Stateless
public class SampleApiDao extends OsolApiDao<SampleApiParameter> {

    private final SampleApiServiceDaoImpl sampleApiServiceDaoImpl;

    public SampleApiDao() {
        sampleApiServiceDaoImpl = new SampleApiServiceDaoImpl();
    }

    /**
     * (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public List<SampleApiResultData> query(SampleApiParameter parameter) throws Exception {

        SampleApiResultData sampleApiResultData = new SampleApiResultData();
        sampleApiResultData.setGroupCode(parameter.getGroupCode());
        return getResultList(sampleApiServiceDaoImpl, sampleApiResultData);
    }

}
