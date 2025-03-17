package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;


public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        if(message.getMessage_text()==null || message.getMessage_text().length() > 255 || message.getMessage_text().trim().isEmpty()){
            return null;
        }
        else if(!messageDAO.accountExists(message.getPosted_by())){
            return null;
        }
        else{
            return messageDAO.insertMessage(message);
        }
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int messageID){
        return messageDAO.getMessageByID(messageID);
    }

    public Message getDeletedMessage(int messageID){
        Message deletedMessage = messageDAO.getMessageByID(messageID);
        messageDAO.deleteMessage(messageID);
        return deletedMessage;
    }

    public Message patchMessage(String messageText, int messageID) throws Exception{
        if(messageDAO.getMessageByID(messageID)!=null && messageText != null && messageText.length() < 256 && !messageText.trim().isEmpty()){
            messageDAO.patchMessage(messageText, messageID);
            Message updatedMessage = messageDAO.getMessageByID(messageID);
            return updatedMessage;
        }
        else{
            return null;
        }
    }
    
    public List<Message> getAllMessagesByUser(int accountID){
        return messageDAO.getAllMessagesByUser(accountID);
    }
}
