package cn.gfqh.tradefix.demo.acceptor;

import cn.gfqh.tradefix.common.LoggerWapper;
import quickfix.Message;

/**
 * Created by Alvin on 2017/6/19.
 */
public class RateSender implements Runnable {

    private RateGenerater rateGenerater = null;
    private SendEvent sendEvent = null;

    public RateSender(RateGenerater rateGenerater , SendEvent sendEvent) {
        this.rateGenerater = rateGenerater;
        this.sendEvent = sendEvent;
    }

    public static interface SendEvent {
        public void onSend(Message message);
    }

    public void run() {
        while(true) {
            try {

                Message message = rateGenerater.generate();
                sendEvent.onSend(message);

                Thread.sleep(1000);
            } catch(Exception ex) {
                ex.printStackTrace();
                LoggerWapper.error(ex);
            }
        }
    }
}
