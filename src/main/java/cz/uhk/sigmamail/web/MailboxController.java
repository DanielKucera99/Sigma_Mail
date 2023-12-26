package cz.uhk.sigmamail.web;

import cz.uhk.sigmamail.user.CustomUserDetails;
import cz.uhk.sigmamail.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class MailboxController {

    private final CategoryDAO categoryDAO;
    private final UserDAO userDAO;
    private final Message_CategoryDAO message_categoryDAO;
    private final MessageDAO messageDAO;
    private final AttachmentDAO attachmentDAO;

    public MailboxController(CategoryDAO categoryDAO, UserDAO userDAO,Message_CategoryDAO message_categoryDAO, MessageDAO messageDAO, AttachmentDAO attachmentDAO){
        this.categoryDAO=categoryDAO;
        this.userDAO=userDAO;
        this.messageDAO=messageDAO;
        this.message_categoryDAO=message_categoryDAO;
        this.attachmentDAO=attachmentDAO;
    }

    public User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user = new User();
        if(principal instanceof CustomUserDetails){
            CustomUserDetails loggedUser = (CustomUserDetails) principal;
            user = userDAO.getUserByUserame(loggedUser.getUsername());
        }
        return user;
    }

    @PostMapping("/mailbox")
    public String getMailbox(Model model){
        User user = getLoggedUser();
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }

    @GetMapping("/mailbox/{categoryId}")
    public String getCategoryPage(@PathVariable int categoryId, Model model){
        User user = getLoggedUser();
        List<Category> categories = categoryDAO.getAllCategories();
        Category category = categoryDAO.getCategoryById(categoryId);
        List<Message> messages;

        if(categoryId==2) {
            messages = messageDAO.getAllMessagesByReceiverInCategory(user,category);

        } else if (categoryId==4) {
            messages = messageDAO.getAllMessagesInTrash(user);
        }
        else {
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
        User user = getLoggedUser();
        Category category = categoryDAO.getCategoryById(categoryId);
        Message message = messageDAO.getMessageById(messageId);
        User sender = new User();
        User receiver = new User();
        if (message.getSender() != null) {
            sender = userDAO.getUserById(message.getSender().getId());
        } else {
            sender.setUsername("deleted user");
        }
        if(message.getReceiver() != null) {
            receiver = userDAO.getUserById(message.getReceiver().getId());
        } else {
            receiver.setUsername("deleted user");
        }
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("sender", sender);
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
        User user = getLoggedUser();
        User sender = getLoggedUser();
        Message message = new Message();

        model.addAttribute("user",user);
        model.addAttribute("message", message);
        model.addAttribute("sender", sender);

        return "new-message";
    }

    @PostMapping("/mailbox/new-message/send")
    public String sendNewMessage(HttpServletRequest request, Model model){

        User sender = getLoggedUser();

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        if(text.length() > 65535){
            int errorValue = 1;
            Message message = new Message();
            model.addAttribute("errorValue", errorValue);
            model.addAttribute("user", sender);
            model.addAttribute("sender", sender);
            model.addAttribute("message", message);
            return "new-message";
        }
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
    public String saveNewMessage(HttpServletRequest request, Model model){
        User sender = getLoggedUser();

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        if(text.length() > 65535){
            int errorValue = 1;
            Message message = new Message();
            model.addAttribute("errorValue", errorValue);
            model.addAttribute("user", sender);
            model.addAttribute("sender", sender);
            model.addAttribute("message", message);
            return "new-message";
        }
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
        User user = getLoggedUser();
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }

    @PostMapping("/moveToTrash/{categoryId}/{messageId}")
    public String moveMessageToTrash(@PathVariable int categoryId, @PathVariable int messageId) {
        User user = getLoggedUser();
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(categoryId);
        Message_Category currentMessage_Category = message_categoryDAO.getDataByMessageAndCategory(message, category);
        message_categoryDAO.deleteMessage_Category(currentMessage_Category);
        messageDAO.associateMessageWithCategory(message, "Trash", user);

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
    public String sendConceptMessage(@PathVariable int messageId, HttpServletRequest request, Model model){
        User sender = getLoggedUser();
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(3);
        Message_Category message_category = message_categoryDAO.getDataByMessageAndCategory(message,category);
        message_categoryDAO.deleteMessage_Category(message_category);
        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        if(text.length() > 65535){
            int errorValue = 1;
            model.addAttribute("errorValue", errorValue);
            model.addAttribute("user", sender);
            model.addAttribute("sender", sender);
            model.addAttribute("message", message);
            model.addAttribute("category", category);
            return "concept-message";
        }
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

        if(attachments.isEmpty())
        {
            messageDAO.sendMessage(sender, receiver, subject, text, null);
        } else {
            messageDAO.sendMessage(sender, receiver, subject, text, attachments);
        }

        return "redirect:/mailbox";
    }

    @PostMapping("/mailbox/{messageId}/concept-message/save")
    public String saveConceptMessage(@PathVariable int messageId, HttpServletRequest request, Model model){
        User sender = getLoggedUser();
        Message message = messageDAO.getMessageById(messageId);
        Category category = categoryDAO.getCategoryById(3);

        String receiverUsername = request.getParameter("receiver");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");
        if(text.length() > 65535){
            int errorValue = 1;
            model.addAttribute("errorValue", errorValue);
            model.addAttribute("user", sender);
            model.addAttribute("sender", sender);
            model.addAttribute("message", message);
            model.addAttribute("category", category);
            return "concept-message";
        }
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

        User user = getLoggedUser();
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
