package io.alvin.tradefix;

import java.util.Scanner;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

/**
 * Created by Alvin on 2017/6/20.
 */
public class ReceiverApp {
    public static void main(String[] args) throws ConfigError {

        SessionSettings settings = new SessionSettings("receiver.cfg");
        Application myApp = new FIXReceiver();
        FileStoreFactory fileStoreFactory = new FileStoreFactory(settings);
        ScreenLogFactory screenLogFactory = new ScreenLogFactory(settings);
        DefaultMessageFactory msgFactory = new DefaultMessageFactory();
        SocketAcceptor acceptor = new SocketAcceptor(myApp, fileStoreFactory,
                settings, screenLogFactory, msgFactory);

        acceptor.start();

        Scanner reader = new Scanner(System.in);
        System.out.println("press <enter> to quit");

        //get user input for a
        reader.nextLine();

        acceptor.stop();
    }

}
