package cn.gfqh.tradefix.demo.initiator;

import cn.gfqh.tradefix.common.LoggerWapper;

/**
 * Created by Alvin on 2017/6/19.
 */
public class InitiatorStartup {
    public static void main(String[] args) {
        LoggerWapper.traceCallStart();
        InitiatorLauncher.getInstance().launch();
        LoggerWapper.traceCallEnd();
    }
}
