package zti_project.Controller.Websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import zti_project.Aspect.UseDatabase;
import zti_project.Model.Database.UserDatabase;
import zti_project.Model.Models.authorization.AuthToken;
import zti_project.Utils.TokenGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocketendpoint")
public class WebsocketProductResource {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    private static final Map<Session, AuthToken> authTokenMap = new HashMap<>();

    public Boolean isTokenValid(AuthToken authToken)
    {
        UserDatabase userDatabase = new UserDatabase();
        TokenGenerator tokenGenerator = new TokenGenerator();
        return tokenGenerator.isAuthTokenValid(userDatabase, authToken);
    }
    @OnOpen
    @UseDatabase
    public void onOpen(Session session) throws IOException
    {
        String authTokenString = session.getQueryString().split("=")[1];
        authTokenString = authTokenString.replace("%20", " ");
        AuthToken authToken = new AuthToken(authTokenString);
        if(isTokenValid(authToken))
        {
            sessions.add(session);
            authTokenMap.put(session, authToken);
        }
        else
        {
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        AuthToken authToken = authTokenMap.get(session);
        if (authToken != null)
        {
            authTokenMap.remove(session);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {
    }

    @OnError
    public void onError(Throwable e)
    {
        e.printStackTrace();
    }
    public static void notifyProductChange(String username)
    {
        String notification = "ProductChange";
        for (Session session : sessions)
        {
            if(authTokenMap.get(session).getUsername() == username)
                continue;
            try
            {
                session.getBasicRemote().sendText(notification);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void notifyStoredProductChange(String username)
    {
        String notification = "StoredProductChange";
        for (Session session : sessions)
        {
            if(authTokenMap.get(session).getUsername() == username)
                continue;
            try
            {
                session.getBasicRemote().sendText(notification);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void notifyOrderProductChange(String username)
    {
        String notification = "OrderProductChange";
        for (Session session : sessions)
        {
            try
            {
                if(authTokenMap.get(session).getUsername() == username)
                    continue;
                session.getBasicRemote().sendText(notification);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void notifyDeliverProductChange(String username)
    {
        String notification = "DeliveryProductChange";
        for (Session session : sessions)
        {
            try
            {
                if(authTokenMap.get(session).getUsername() == username)
                    continue;
                session.getBasicRemote().sendText(notification);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}