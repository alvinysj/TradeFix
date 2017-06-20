package io.alvin.tradefix;

import java.util.Date;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;

/**
 * Created by Alvin on 2017/6/20.
 */
public class SenderApp {
    public static void main(String[] args) throws ConfigError, InterruptedException, SessionNotFound {

        SessionSettings settings = new SessionSettings("sender.cfg");
        Application myApp = new FIXSender();
        FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(settings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        SocketInitiator initiator = new SocketInitiator(myApp, fileStoreFactory, settings,
                screenLogFactory, msgFactory);

        initiator.start();

        Thread.sleep(3000);

        // matching values from sender.cfg
        SessionID sessionID = new SessionID("FIX.4.2", "CLIENT1", "FixServer");
        NewOrderSingle order = new NewOrderSingle(new ClOrdID("DLF"),
                new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE), new Symbol("BHP"), new Side(Side.BUY),
                new TransactTime(new Date()), new OrdType(OrdType.LIMIT));
        order.set(new OrderQty(45.00));
        order.set(new Price(25.40));
        Session.sendToTarget(order, sessionID);

        Thread.sleep(60000);

        initiator.stop();
    }
}
