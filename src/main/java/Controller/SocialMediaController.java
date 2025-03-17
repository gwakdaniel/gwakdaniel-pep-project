package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and r
 * @throws JsonProcessingException 
 * @throws JsonMappingException esponse.
     */
    private void postAccountHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
        }else{
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedAccount = accountService.loginAccount(account);
        if (loggedAccount!=null) {
            context.json(mapper.writeValueAsString(loggedAccount));
        }else{
            context.status(401);
        }
    }
    private void postMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            context.json(mapper.writeValueAsString(addedMessage));
        }else{
            context.status(400);
        }
    }
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }
    private void getMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message queriedMessage = messageService.getMessageByID(id);
        if(queriedMessage!=null){
            context.json(queriedMessage).status(200);
        }
        else{
            context.status(200);
        }
    }
    private void deleteMessageHandler(Context context) throws JsonMappingException, JsonProcessingException{
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.getDeletedMessage(id);
        if(deletedMessage!=null){
            context.json(deletedMessage).status(200);
        }
        else{
            context.status(200);
        }
        
    }
    private void patchMessageHandler(Context context) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message patchedMessage = messageService.patchMessage(message.getMessage_text(), id);
        if(patchedMessage!=null){
            context.json(patchedMessage).status(200);
        }
        else{
            context.status(400);
        }
    }
    private void getAllMessagesByUserHandler(Context context) {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(id);
        context.json(messages);
    }

}
