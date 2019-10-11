
import quickfix.Application;
import quickfix.SessionID;
import quickfix.Message;
import quickfix.Acceptor;
import quickfix.SocketAcceptor;
import quickfix.SessionSettings;
import quickfix.FileStoreFactory;
import quickfix.MessageStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.ScreenLogFactory;
import quickfix.DefaultMessageFactory;
import quickfix.ConfigError;
import quickfix.SessionNotFound;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.DoNotSend;
import quickfix.UnsupportedMessageType;

import java.io.FileNotFoundException;

public class Main
{

    public static class ServerApplication implements Application
    {
        @Override
        public void onCreate(SessionID sessionID)
        {
        }

        @Override
        public void onLogon(SessionID sessionID)
        {
        }

        @Override
        public void onLogout(SessionID sessionID)
        {
        }

        @Override
        public void toAdmin(Message message, SessionID sessionID)
        {
        }

        @Override
        public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon
        {
        }

        @Override
        public void toApp(Message message, SessionID sessionID) throws DoNotSend
        {
        }

        @Override
        public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType
        {
            System.out.println("FromApp: " + message);
        }
    }

    public static void main(String[] args) throws ConfigError, FileNotFoundException, InterruptedException, SessionNotFound
    {
        SessionSettings settings = new SessionSettings("/Users/rossir/workspace/java/fix_pre/acceptor/src/main/resources/config.cfg");

        Application application = new ServerApplication();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory( true, true, true);
        MessageFactory messageFactory = new DefaultMessageFactory();

        Acceptor initiator = new SocketAcceptor(application, messageStoreFactory, settings, logFactory, messageFactory);
        initiator.start();

        while (true)
        {
            Thread.sleep(1000);
        }
    }

}
