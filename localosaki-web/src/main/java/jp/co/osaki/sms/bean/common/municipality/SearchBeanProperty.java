package jp.co.osaki.sms.bean.common.municipality;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

@Named(value = "commonSearchMunicipalityProperty")
@Dependent

public class SearchBeanProperty extends BaseSearchBeanProperty<Condition> implements Serializable {

    private static final long serialVersionUID = 5008014904965522049L;

}
