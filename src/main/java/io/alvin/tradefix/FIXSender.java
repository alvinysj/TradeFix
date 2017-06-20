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

/**
 * Created by Alvin on 2017/6/20.
 */
public class FIXSender implements Application {

    @Override
    public void onCreate(SessionID sessionID) {

    }

    @Override
    public void onLogon(SessionID sessionID) {

    }

    @Override
    public void onLogout(SessionID sessionID) {

    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, RejectLogon {

    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        System.out.println("Sender toApp: " + message.toString());
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, UnsupportedMessageType {

    }
}
