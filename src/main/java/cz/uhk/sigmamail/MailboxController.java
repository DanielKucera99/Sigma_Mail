package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.*;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class MailboxController {

    private final CategoryDAO categoryDAO;
    private final UserDAO userDAO;
    private Message_CategoryDAO message_categoryDAO;
    private MessageDAO messageDAO;
    AttachmentDAO attachmentDAO;


    public MailboxController(CategoryDAO categoryDAO, UserDAO userDAO,Message_CategoryDAO message_categoryDAO, MessageDAO messageDAO, AttachmentDAO attachmentDAO){
        this.categoryDAO=categoryDAO;
        this.userDAO=userDAO;
        this.messageDAO=messageDAO;
        this.message_categoryDAO=message_categoryDAO;
        this.attachmentDAO=attachmentDAO;
    }

    @PostMapping("/mailbox")
    public String getMailbox(Model model){
        int userid = 1;
        User user = userDAO.getUserById(userid);
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }

    @GetMapping("/mailbox/{categoryId}")
    public String getCategoryPage(@PathVariable int categoryId, Model model){
        int userid = 1;
        User user = userDAO.getUserById(userid);
        List<Category> categories = categoryDAO.getAllCategories();
        Category category = categoryDAO.getCategoryById(categoryId);
        List<Message> messages;

        if(categoryId==2) {
            messages = messageDAO.getAllMessagesByReceiverInCategory(user,category);
        } else {
            messages = messageDAO.getAllMessagesBySenderInCategory(user, category);
        }
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);
        model.addAttribute("user",user);
        model.addAttribute("messages",messages);

        return "category";
    }

    @GetMapping("/mailbox/{categoryId}/{messageId}")
    public String getMessagePage(@PathVariable int categoryId,@PathVariable int messageId,Model model){
        int userid=1;
        User user = userDAO.getUserById(userid);
        Category category = categoryDAO.getCategoryById(categoryId);
        Message message = messageDAO.getMessageById(messageId);
        User sender = userDAO.getUserById(message.getSender().getId());
        User receiver = userDAO.getUserById(message.getReceiver().getId());
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("sender",sender);
        model.addAttribute("receiver", receiver);
        model.addAttribute("category", category);
        if (categoryId == 3){
            return "concept-message";
        } else {
            return "message";
        }
    }

    @GetMapping("/mailbox/new-message")
    public String getNewMessagePage(Model model){
        int userid=1;
        User user = userDAO.getUserById(userid);
        User sender = userDAO.getUserById(userid);
        Message message = new Message();

        model.addAttribute("user",user);
        model.addAttribute("message", message);
        model.addAttribute("sender", sender);

        return "new-message";
    }

    @PostMapping("/mailbox/new-message/send")
    public String sendNewMessage(HttpServletRequest request){
        int userid=1;
        User sender = userDAO.getUserById(userid);

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        List<MultipartFile> attachmentFiles = ((MultipartHttpServletRequest) request).getFiles("attachments");
        List<Attachment> attachments = processAttachments(attachmentFiles);

        User receiver = userDAO.getUserByUserame(receiverUsername);

        if(attachments.getFirst().getSize() == 0)
        {
            messageDAO.sendMessage(sender, receiver, subject, text, null);
        } else {
            messageDAO.sendMessage(sender, receiver, subject, text, attachments);
        }
        return "redirect:/mailbox";
    }
    @PostMapping("/mailbox/new-message/save")
    public String saveNewMessage(HttpServletRequest request){
        int userid=1;
        User sender = userDAO.getUserById(userid);

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        List<MultipartFile> attachmentFiles = ((MultipartHttpServletRequest) request).getFiles("attachments");
        List<Attachment> attachments = processAttachments(attachmentFiles);

        User receiver = userDAO.getUserByUserame(receiverUsername);

        if(attachments.getFirst().getSize() == 0)
        {
            messageDAO.saveMessage(sender, receiver, subject, text, null);
        } else {
            messageDAO.saveMessage(sender, receiver, subject, text, attachments);
        }
        return "redirect:/mailbox";
    }

    private List<Attachment> processAttachments(List<MultipartFile> attachments) {
        return attachments.stream()
                .map(this::convertToAttachment)
                .collect(Collectors.toList());
    }

    private Attachment convertToAttachment(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setType(file.getContentType());
        attachment.setSize((int) file.getSize());

        try {
            byte [] content = convertMultipartFileToByteArray(file);
            attachment.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public byte[] convertMultipartFileToByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    @GetMapping("/mailbox")
    public String getMailboxPage(Model model){
        int userid = 1;
        User user = userDAO.getUserById(userid);
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }

    @PostMapping("/moveToTrash/{categoryId}/{messageId}")
    public String moveMessageToTrash(@PathVariable int categoryId,@PathVariable int messageId){
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(categoryId);
        Message_Category currentMessage_Category = message_categoryDAO.getDataByMessageAndCategory(message,category);
        message_categoryDAO.deleteMessage_Category(currentMessage_Category);
        messageDAO.associateMessageWithCategory(message,"Trash");

        return "redirect:/mailbox";
    }

    @PostMapping("/delete/{categoryId}/{messageId}")
    public String deleteMessage(@PathVariable int categoryId,@PathVariable int messageId){
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(categoryId);
        Message_Category message_category = message_categoryDAO.getDataByMessageAndCategory(message,category);
        message_categoryDAO.deleteMessage_Category(message_category);

        return "redirect:/mailbox";
    }

    @PostMapping("/mailbox/{messageId}/concept-message/send")
    public String sendConceptMessage(@PathVariable int messageId, HttpServletRequest request){
        int userid=1;
        User sender = userDAO.getUserById(userid);
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(3);
        Message_Category message_category = message_categoryDAO.getDataByMessageAndCategory(message,category);
        message_categoryDAO.deleteMessage_Category(message_category);
        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        List<MultipartFile> attachmentFiles = ((MultipartHttpServletRequest) request).getFiles("attachments");
        List<Attachment> attachments = processAttachments(attachmentFiles);
        List<Attachment> messageAttachments = message.getAttachments();
        attachments.addAll(messageAttachments);

        for(int i = 0;i < attachments.size();i++)
        {
            if (attachments.get(i).getSize() == 0)
            {
                attachments.remove(i);
            }
        }
        User receiver = userDAO.getUserByUserame(receiverUsername);

        if(attachments.getFirst().getSize() == 0)
        {
            messageDAO.sendMessage(sender, receiver, subject, text, null);
        } else {
            messageDAO.sendMessage(sender, receiver, subject, text, attachments);
        }

        return "redirect:/mailbox";
    }

    @PostMapping("/mailbox/{messageId}/concept-message/save")
    public String saveConceptMessage(@PathVariable int messageId, HttpServletRequest request){
        int userid=1;
        User sender = userDAO.getUserById(userid);
        Message message = messageDAO.getMessageById(messageId);

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        List<MultipartFile> attachmentFiles = ((MultipartHttpServletRequest) request).getFiles("attachments");
        List<Attachment> attachments = processAttachments(attachmentFiles);
        User receiver = userDAO.getUserByUserame(receiverUsername);

        List<Attachment> attachmentsToRemove = new ArrayList<>(message.getAttachments());
        for(Attachment attachment:attachmentsToRemove)
        {
            Attachment newAttachment = new Attachment();
            newAttachment.setType(attachment.getType());
            newAttachment.setSize(attachment.getSize());
            newAttachment.setName(attachment.getName());
            newAttachment.setContent(attachment.getContent());
            attachments.add(newAttachment);
        }

        for(int i = 0;i < attachments.size();i++)
        {
            if (attachments.get(i).getSize() == 0)
            {
                attachments.remove(i);
            }
        }
        message.getAttachments().clear();
        attachmentDAO.deleteAttachments(attachmentsToRemove);
        messageDAO.deleteMessage(message);
        if(attachments.isEmpty())
        {
            messageDAO.saveMessage(sender, receiver, subject, text, null);
        } else {
            messageDAO.saveMessage(sender, receiver, subject, text, attachments);
        }

        return "redirect:/mailbox";
    }
    @PostMapping("/mailbox/{messageId}/concept-message/{index}/delete")
    public String deleteAttachment(@PathVariable int messageId, @PathVariable int index, Model model) {
        int userid = 1;
        User user = userDAO.getUserById(userid);
        Message message = messageDAO.getMessageById(messageId);
        Attachment attachment = message.getAttachments().get(index);
        message.getAttachments().remove(index);
        attachmentDAO.deleteAttachment(attachment);

        User sender = message.getSender();
        User receiver = message.getReceiver();
        Category category = categoryDAO.getCategoryById(3);

        model.addAttribute("user",user);
        model.addAttribute("sender",sender);
        model.addAttribute("receiver", receiver);
        model.addAttribute("message", message);
        model.addAttribute("category",category);

        return "concept-message";

    }

}
