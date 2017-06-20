package io.alvin.tradefix;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.MessageCracker;
import quickfix.fix42.NewOrderSingle;

/**
 * Created by Alvin on 2017/6/20.
 */
public class FIXReceiver extends MessageCracker implements Application {

    @Override
    public void onMessage(NewOrderSingle order, SessionID sessionID) {
        System.out.println("Receiver onMessage..  " + order);
    }

    @Override
    public void onCreate(SessionID sessionID) {
        System.out.println("Receiver onCreate.. " + sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        System.out.println("Receiver onLogon.." + sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        System.out.println("Receiver onLogout.." + sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, RejectLogon {
        System.out.println(String.format("Receiver fromAdmin [Message]=%s, [SessionID]=%s", message, sessionID));
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {

    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, UnsupportedMessageType {
        System.out.println(String.format("Receiver fromApp [Message]=%s, [SessionID]=%s", message, sessionID));
        crack(message, sessionID);
    }
}
