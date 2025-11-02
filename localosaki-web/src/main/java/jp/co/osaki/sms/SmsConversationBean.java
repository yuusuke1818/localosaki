package jp.co.osaki.sms;

import javax.enterprise.context.Conversation;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

public abstract class SmsConversationBean extends SmsBean {

    @Inject
    Conversation conv;

    protected void conversationStart(){
        //init()で呼ぶこと
        if(conv.isTransient()){
            HttpSession session = (HttpSession) this.getWrapped().getSession(false);
            int intervalTime = session.getMaxInactiveInterval();    //秒
            conv.setTimeout(intervalTime*1000);     //ミリ秒

            conv.begin();
            eventLogger.info(this.toString().concat(" conversationStart cid=".concat(conv.getId())));
        }
    }

    protected void conversationEnd(){
        if(!conv.isTransient()){
            eventLogger.info(this.toString().concat(" conversationEnd cid=".concat(conv.getId())));
            conv.end();
        }
    }

    public void receive(@Observes SmsConversationEvent ev){
        conversationEnd();
    }
}
