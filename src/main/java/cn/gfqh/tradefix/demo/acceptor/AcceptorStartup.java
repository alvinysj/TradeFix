package cn.gfqh.tradefix.demo.acceptor;

import cn.gfqh.tradefix.common.LoggerWapper;
import quickfix.ConfigError;

/**
 * Created by Alvin on 2017/6/19.
 */
public class AcceptorStartup {
    public static void main(String[] args) throws ConfigError {
        LoggerWapper.traceCallStart();

        AcceptorLauncher.getInstance().launch();

        LoggerWapper.traceCallEnd();
    }
}
