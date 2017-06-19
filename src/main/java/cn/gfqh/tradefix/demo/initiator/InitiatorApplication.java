package cn.gfqh.tradefix.demo.initiator;

import cn.gfqh.tradefix.common.AccountConstant;
import cn.gfqh.tradefix.common.LoggerWapper;
import cn.gfqh.tradefix.common.MessageCreateFactory;
import cn.gfqh.tradefix.core.ApplicationAdapter;

import cn.gfqh.tradefix.core.EventType;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MarketDepth;
import quickfix.field.NoRelatedSym;
import quickfix.field.Password;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.field.UserRequestID;
import quickfix.field.UserRequestType;
import quickfix.field.Username;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.UserRequest;

import java.util.UUID;

/**
 * Created by Alvin on 2017/6/19.
 */
public class InitiatorApplication extends ApplicationAdapter {
    @Override
    public void processOnCreate(SessionID sessionID) {
        LoggerWapper.traceCallStart();
        LoggerWapper.debug("processOnCreate(sessionID) : " + "[" + sessionID + "]");
        LoggerWapper.traceCallEnd();
    }

    @Override
    public void processOnLogon(SessionID sessionID) {
        LoggerWapper.traceCallStart();
        LoggerWapper.debug("processOnLogon(sessionID) : " + "[" + sessionID + "]");

        UserRequest userRequest = MessageCreateFactory.createMessage(UserRequest.class);
        userRequest.set(new Username("MyFIX"));
        userRequest.set(new Password("123456"));
        userRequest.set(new UserRequestType(UserRequestType.LOGONUSER));
        userRequest.set(new UserRequestID(UUID.randomUUID().toString()));

        sendMessage(userRequest, sessionID);

        LoggerWapper.traceCallEnd();
    }

    @Override
    public void processUserResponse(Message message, SessionID sessionID, EventType eventType) throws FieldNotFound {
        LoggerWapper.traceCallStart();
        LoggerWapper.debug("processUserResponse(message,sessionID,eventType) : " + "["+message + " , " + sessionID + " , " + eventType + "]");

        if(EventType.FROM_APP.equals(eventType)) {

            if(AccountConstant.QUICKFIX_USER1.equals(sessionID.getSenderCompID())) {
                String[] productIDS = new String[]{"USD/JPY","USD/CHF","EUR/JPY"};

                for(String productId : productIDS) {
                    MarketDataRequest marketDataRequest = new MarketDataRequest();
                    marketDataRequest.set(new MDReqID(UUID.randomUUID().toString()));
                    marketDataRequest.set(new SubscriptionRequestType(
                            SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));
                    marketDataRequest.set(new MarketDepth(1));

                    MarketDataRequest.NoMDEntryTypes noMDEntryTypes = new MarketDataRequest.NoMDEntryTypes();
                    noMDEntryTypes.set(new MDEntryType(MDEntryType.BID));
                    marketDataRequest.addGroup(noMDEntryTypes);
                    noMDEntryTypes.set(new MDEntryType(MDEntryType.OFFER));
                    marketDataRequest.addGroup(noMDEntryTypes);

                    MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
                    noRelatedSym.set(new Symbol(productId));
                    marketDataRequest.addGroup(noRelatedSym);
                    marketDataRequest.set(new NoRelatedSym(1));

                    sendMessage(marketDataRequest, sessionID);
                }
            }

            if(AccountConstant.QUICKFIX_USER2.equals(sessionID.getSenderCompID())) {
                String[] productIDS = new String[]{"XAU/USD","XAG/USD","OIL/USD"};

                for(String productId : productIDS) {
                    MarketDataRequest marketDataRequest = new MarketDataRequest();
                    marketDataRequest.set(new MDReqID(UUID.randomUUID().toString()));
                    marketDataRequest.set(
                            new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));
                    marketDataRequest.set(new MarketDepth(1));

                    MarketDataRequest.NoMDEntryTypes noMDEntryTypes = new MarketDataRequest.NoMDEntryTypes();
                    noMDEntryTypes.set(new MDEntryType(MDEntryType.BID));
                    marketDataRequest.addGroup(noMDEntryTypes);
                    noMDEntryTypes.set(new MDEntryType(MDEntryType.OFFER));
                    marketDataRequest.addGroup(noMDEntryTypes);

                    MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
                    noRelatedSym.set(new Symbol(productId));
                    marketDataRequest.addGroup(noRelatedSym);
                    marketDataRequest.set(new NoRelatedSym(1));

                    sendMessage(marketDataRequest, sessionID);
                }
            }
        }
        LoggerWapper.traceCallEnd();
    }
    @Override
    public void processMarketDataSnapshotFullRefresh(Message message, SessionID sessionID,
                                                     EventType eventType) throws FieldNotFound {
        super.processMarketDataSnapshotFullRefresh(message, sessionID, eventType);
        LoggerWapper.debug("---->>>> " + message);
        RateReceiver.getQueue().offer(message);
    }
}
