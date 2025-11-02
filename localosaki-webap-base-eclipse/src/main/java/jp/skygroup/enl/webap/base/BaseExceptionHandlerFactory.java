package jp.skygroup.enl.webap.base;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class BaseExceptionHandlerFactory extends ExceptionHandlerFactory {
  private final ExceptionHandlerFactory parent;

  public BaseExceptionHandlerFactory(ExceptionHandlerFactory parent) {
    this.parent = parent;
  }
	
  @Override
  public ExceptionHandler getExceptionHandler() {
    ExceptionHandler handler = new BaseExceptionHandler(parent.getExceptionHandler());
    return handler;
  }    
}
