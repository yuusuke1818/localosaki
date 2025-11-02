package jp.co.osaki.osol.api.bean.smcontrol;

import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.dao.smcontrol.AielMasterDiscomfortIndexStandardSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterDiscomfortIndexStandardSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterDiscomfortIndexStandardSelectResult;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210014Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

@Named(value = SmControlConstants.AIELMASTER_DISCOMFORT_INDEX_STANDARD_SELECT)
@RequestScoped
public class AielMasterDiscomfortIndexStandardSelectBean
        extends AbstractApiBean<AielMasterDiscomfortIndexStandardSelectResult, AielMasterDiscomfortIndexStandardSelectParameter> {

    @EJB
    private AielMasterDiscomfortIndexStandardSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(AielMasterDiscomfortIndexStandardSelectParameter parameter) {
        A210014Param param = new A210014Param();
        String settingtServerDateTimeString = parameter.getDateTime();//日付

        //パラメータに日付がセットされていなかった場合、サーバの時刻をセット
        if (CheckUtility.isNullOrEmpty(settingtServerDateTimeString)) {
            settingtServerDateTimeString = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(dao.getServerDateTime());
        }
        param.setDateTime(settingtServerDateTimeString);

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }


}
