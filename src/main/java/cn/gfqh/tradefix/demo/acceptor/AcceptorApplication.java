package cn.gfqh.tradefix.demo.acceptor;

import cn.gfqh.tradefix.common.LoggerWapper;
import cn.gfqh.tradefix.common.MessageCreateFactory;
import cn.gfqh.tradefix.core.ApplicationAdapter;

import cn.gfqh.tradefix.core.EventType;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MDReqID;
import quickfix.field.MsgType;
import quickfix.field.NoRelatedSym;
import quickfix.field.Symbol;
import quickfix.field.UserRequestID;
import quickfix.field.UserStatus;
import quickfix.field.Username;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.UserRequest;

import java.util.List;

/**
 * Created by Alvin on 2017/6/19.
 */
public class AcceptorApplication extends ApplicationAdapter {
    @Override
    public void processUserRequest(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound {
        LoggerWapper.traceCallStart();
        LoggerWapper.debug("processUserRequest(message,sessionID,eventType) : " + "["+message + " , " + sessionID + " , " + eventType + "]");

        if(EventType.FROM_APP.equals(eventType)) {

            UserRequest userRequest = MessageCreateFactory.cast(message, UserRequest.class);
            String userRequestID = userRequest.getString(UserRequestID.FIELD);
            String username = userRequest.getString(Username.FIELD);

            Message userResponse = MessageCreateFactory.createMessage(sessionID,MsgType.USER_RESPONSE);

            userResponse.setString(UserRequestID.FIELD, userRequestID);
            userResponse.setString(Username.FIELD, username);
            userResponse.setInt(UserStatus.FIELD, 1);

            sendMessage(userResponse, sessionID);
        }

        LoggerWapper.traceCallEnd();
    }

    @Override
    public void processMarketDataRequest(Message message, final SessionID sessionID, EventType eventType) throws FieldNotFound {
        LoggerWapper.traceCallStart();
        LoggerWapper.debug("processMarketDataRequest(message,sessionID,eventType) : " + "["+message + " , " + sessionID + " , " + eventType + "]");

        if(EventType.FROM_APP.equals(eventType)) {
            MarketDataRequest marketDataRequest = MessageCreateFactory.cast(message, MarketDataRequest.class);

            MDReqID mdReqID = marketDataRequest.get(new MDReqID());
            Symbol symbol = null;
            List<Group> NoRelatedSymList = marketDataRequest.getGroups(NoRelatedSym.FIELD);
            if(!NoRelatedSymList.isEmpty()) {
                Group group = NoRelatedSymList.get(0);
                symbol = new Symbol(group.getString(Symbol.FIELD));
            }

            RateGenerater rateGenerater = new RateGenerater(mdReqID, symbol);
            RateSender rateSender = new RateSender(rateGenerater, new RateSender.SendEvent() {

                public void onSend(Message message) {
                    sendMessage(message,sessionID);
                }
            });
            new Thread(rateSender).start();
        }

        LoggerWapper.traceCallEnd();
    }
}
