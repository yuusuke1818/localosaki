package jp.skygroup.enl.webap.base;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.servlet.http.HttpSession;
import org.jboss.logging.Logger;

public class BaseExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    private static final Logger errorLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.ERROR.getVal());

    public BaseExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void handle() {

        BaseMessages baseMessages = new BaseMessages();
        baseMessages.initMessages();

        for (Iterator<ExceptionQueuedEvent> it
                = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext() == true;) {

            ExceptionQueuedEventContext eventContext = it.next().getContext();
            FacesContext facesContext = eventContext.getContext();

            // 1. ハンドリング対象のアプリケーション例外を取得
            Throwable ex = eventContext.getException();     //wrapされた一番外のException
            Throwable th = ex;
            Throwable rc = ex.getCause();
            while (rc != null) {
                String stackTrace = null;
                //JPA関連の例外の場合は、画面にエラーメッセージを表示する。
                if (!facesContext.getApplication().getProjectStage().equals(ProjectStage.Production)) {
                    stackTrace = BaseUtility.getStackTraceMessage(rc);
                }
                if (rc instanceof EntityExistsException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("EntityExistsException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof EntityNotFoundException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("EntityNotFoundException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof LockTimeoutException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("LockTimeoutException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof NonUniqueResultException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("NonUniqueResultException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof NoResultException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("NoResultException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof OptimisticLockException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("OptimisticLockException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof PessimisticLockException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("PessimisticLockException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof QueryTimeoutException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("QueryTimeoutException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof RollbackException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("RollbackException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof TransactionRequiredException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("TransactionRequiredException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof org.hibernate.exception.JDBCConnectionException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("org.hibernate.exception.JDBCConnectionException.Message"), stackTrace));
                    th = null;
                    break;
                }
                if (rc instanceof PersistenceException) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("PersistenceException.Message"), stackTrace));
                    th = null;
                    break;
                }
                th = rc;
                rc = th.getCause();
            }

            if (th != null && th instanceof Exception) {
                //想定外の例外。おそらく操作継続することが困難なのでエラー画面にリダイレクトする。
                errorLogger.fatal(BaseUtility.getStackTraceMessage(th));
                //セッション破棄
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                String redirectPage = null;
                if (th instanceof ViewExpiredException) {//セッションタイムアウト
                    redirectPage = facesContext.getExternalContext().getInitParameter("SESSION_TIMEOUT_ERROR_PAGE");
                } else {//以外のエラー
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, baseMessages.getMessage("Exception.Message"), BaseUtility.getStackTraceMessage(th)));
                    redirectPage = facesContext.getExternalContext().getInitParameter("ERROR_PAGE");
                }

                // 2. リダイレクトしてもFacesMessageが消えないように設定
                facesContext.getExternalContext().getFlash().setKeepMessages(true);

                try {
                    // エラー画面に画面遷移させる
                    String contextPath = facesContext.getExternalContext().getRequestContextPath();
                    facesContext.getExternalContext().redirect(contextPath.concat(redirectPage));
                } catch (IOException e) {
                    //リダイレクト失敗
                    errorLogger.fatal(BaseUtility.getStackTraceMessage(e));
                } finally {
                }
            }
            // 3. 未ハンドリングキューから削除する
            it.remove();
        }
        wrapped.handle();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }
}
