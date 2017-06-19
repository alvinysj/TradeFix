package cn.gfqh.tradefix.common;

import quickfix.FixVersions;
import quickfix.Message;
import quickfix.SessionID;

/**
 * Created by Alvin on 2017/6/19.
 */
public class MessageCreateFactory {

    private static final quickfix.fix40.MessageFactory MESSAGE_FACTORY_VER_40 = new quickfix.fix40.MessageFactory();
    private static final quickfix.fix41.MessageFactory MESSAGE_FACTORY_VER_41 = new quickfix.fix41.MessageFactory();
    private static final quickfix.fix42.MessageFactory MESSAGE_FACTORY_VER_42 = new quickfix.fix42.MessageFactory();
    private static final quickfix.fix43.MessageFactory MESSAGE_FACTORY_VER_43 = new quickfix.fix43.MessageFactory();
    private static final quickfix.fix44.MessageFactory MESSAGE_FACTORY_VER_44 = new quickfix.fix44.MessageFactory();
    private static final quickfix.fix50.MessageFactory MESSAGE_FACTORY_VER_50 = new quickfix.fix50.MessageFactory();

    public static <T> T createMessage(Class<T> type) {
        Object obj = null;
        try {
            obj = type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type.cast(obj);
    }

    public static <T> T cast(Object obj, Class<T> type) {
        return type.cast(obj);
    }

    public static Message createMessage(SessionID sessionID, String msgType) {
        return buildMessage(sessionID.getBeginString(), msgType);
    }

    public static Message createMessage(String fixVersions, String msgType) {
        return buildMessage(fixVersions, msgType);
    }

    private static Message buildMessage(String fixVersions, String msgType) {
        Message message = null;
        if (FixVersions.BEGINSTRING_FIX40.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_40.create(FixVersions.BEGINSTRING_FIX40, msgType);
        } else if (FixVersions.BEGINSTRING_FIX41.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_41.create(FixVersions.BEGINSTRING_FIX41, msgType);
        } else if (FixVersions.BEGINSTRING_FIX42.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_42.create(FixVersions.BEGINSTRING_FIX42, msgType);
        } else if (FixVersions.BEGINSTRING_FIX43.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_43.create(FixVersions.BEGINSTRING_FIX43, msgType);
        } else if (FixVersions.BEGINSTRING_FIX44.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_44.create(FixVersions.BEGINSTRING_FIX44, msgType);
        } else if (FixVersions.FIX50.equals(fixVersions)) {
            message = MESSAGE_FACTORY_VER_50.create(FixVersions.FIX50, msgType);
        }
        return message;
    }
}
