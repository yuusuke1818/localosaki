package jp.co.osaki.sms.bean.common.municipality;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

@Named(value = "commonMunicipalitySelectPaging")
@Dependent

public class MunicipalitySelectPaging extends BasePaging<MunicipalityInfo> implements Serializable {

    private static final long serialVersionUID = 7091863886247022701L;

}
