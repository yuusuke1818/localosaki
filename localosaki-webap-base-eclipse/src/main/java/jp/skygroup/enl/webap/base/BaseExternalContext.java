package jp.skygroup.enl.webap.base;

import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextWrapper;
import javax.faces.context.FacesContext;

/**
 * 
 * BaseExternalContextクラス
 * 
 * BaseクラスにFacesContextへのアクセスを提供するクラス
 *
 * @author n-takada
 */
public abstract class BaseExternalContext extends ExternalContextWrapper {

    @Override
    public ExternalContext getWrapped() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

}
