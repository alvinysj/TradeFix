package cn.gfqh.tradefix.demo.initiator;

import cn.gfqh.tradefix.common.LoggerWapper;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import java.io.InputStream;

/**
 * Created by Alvin on 2017/6/19.
 */
public class InitiatorLauncher {

    private final static InitiatorLauncher launcher = new InitiatorLauncher();
    private final static String CONF_FILE_PATH = "/resources/initiator.conf";

    private InitiatorLauncher() {
        LoggerWapper.traceCallStart();
        LoggerWapper.traceCallEnd();
    }

    public static InitiatorLauncher getInstance() {
        LoggerWapper.traceCallStart();
        LoggerWapper.traceCallEnd();
        return launcher;
    }

    public void launch() {
        LoggerWapper.traceCallStart();
        try {

            //
            RateReceiver rateReceiver = new RateReceiver();
            Thread receiverThread = new Thread(rateReceiver);
            receiverThread.start();

            //
            InputStream in = this.getClass().getResourceAsStream(CONF_FILE_PATH);
            SessionSettings settings = new SessionSettings(in);
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            LogFactory logFactory = new SLF4JLogFactory(settings);
            MessageFactory messageFactory = new DefaultMessageFactory();

            //
            Initiator initiator = new SocketInitiator(new InitiatorApplication(), messageStoreFactory, settings, logFactory, messageFactory);
            initiator.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LoggerWapper.traceCallEnd();
    }
}
