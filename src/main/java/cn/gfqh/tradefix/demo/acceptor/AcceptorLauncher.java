package cn.gfqh.tradefix.demo.acceptor;

import cn.gfqh.tradefix.common.LoggerWapper;
import quickfix.Acceptor;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SLF4JLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

import java.io.InputStream;

/**
 * Created by Alvin on 2017/6/19.
 */
public class AcceptorLauncher {
    private final static AcceptorLauncher launcher = new AcceptorLauncher();
    private final static String CONF_FILE_PATH = "/resources/acceptor.conf";

    private AcceptorLauncher() {
        LoggerWapper.traceCallStart();

        LoggerWapper.traceCallEnd();
    }

    public static AcceptorLauncher getInstance() {
        LoggerWapper.traceCallStart();


        LoggerWapper.traceCallEnd();
        return launcher;
    }

    public void launch() throws ConfigError {
        LoggerWapper.traceCallStart();

        InputStream in = this.getClass().getResourceAsStream(CONF_FILE_PATH);
        SessionSettings settings = new SessionSettings(in);
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new SLF4JLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        Acceptor acceptor = new SocketAcceptor(new AcceptorApplication(), messageStoreFactory,
                settings, logFactory, messageFactory);
        acceptor.start();

        LoggerWapper.traceCallEnd();
    }
}
