package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.*;

import java.io.IOException;
import java.security.Principal;
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

    public MailboxController(CategoryDAO categoryDAO, UserDAO userDAO,Message_CategoryDAO message_categoryDAO, MessageDAO messageDAO){
        this.categoryDAO=categoryDAO;
        this.userDAO=userDAO;
        this.messageDAO=messageDAO;
        this.message_categoryDAO=message_categoryDAO;
    }

    @PostMapping("/mailbox")
    public String getMailbox(Model model){
        int userid = 2;
        User user = userDAO.getUserById(userid);
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }

    @GetMapping("/mailbox/{categoryId}")
    public String getCategoryPage(@PathVariable int categoryId, Model model){
        int userid = 2;
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
        int userid=2;
        User user = userDAO.getUserById(userid);
        Message message = messageDAO.getMessageById(messageId);
        User sender = userDAO.getUserById(message.getSender().getId());
        User receiver = userDAO.getUserById(message.getReceiver().getId());
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("sender",sender);
        model.addAttribute("receiver", receiver);

        return "message";
    }

    @GetMapping("/mailbox/new-message")
    public String getNewMessagePage(Model model){
        int userid=2;
        User sender = userDAO.getUserById(userid);
        Message message = new Message();

        model.addAttribute("message", message);
        model.addAttribute("sender", sender);

        return "new-message";
    }

    @PostMapping("/mailbox/new-message")
    public String sendNewMessage(HttpServletRequest request){
        int userid=2;
        User sender = userDAO.getUserById(userid);
        String receiverUsername = request.getParameter("receiver");
        System.out.println("receiver name: " + receiverUsername);
        String subject = request.getParameter("subject");
        System.out.println("subject name: " + subject);
        String text = request.getParameter("text");
        List<MultipartFile> attachmentFiles = ((MultipartHttpServletRequest) request).getFiles("attachments");
        System.out.println("attachment: " + attachmentFiles.getFirst().getOriginalFilename());
        List<Attachment> attachments = processAttachments(attachmentFiles);
        System.out.println("true attachment: " + attachments.getFirst().getName());


        User receiver = userDAO.getUserByUserame(receiverUsername);
        System.out.println("id: " + receiver.getId());

        messageDAO.sendMessage(sender, receiver, subject, text, attachments);

        return "redirect:/mailbox";
    }

    private List<Attachment> processAttachments(List<MultipartFile> attachments) {
        return attachments.stream()
                .map(this::convertToAttachment) // Assuming Attachment::new is the correct constructor
                .collect(Collectors.toList());
    }

    private Attachment convertToAttachment(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setType(file.getContentType());
        attachment.setSize((int) file.getSize());

        // You may want to set the Message association if needed
        // attachment.setMessage(yourMessageObject);

        // Save the file content to the Attachment if necessary
        try {
            byte [] content = convertMultipartFileToByteArray(file);
            attachment.setContent(content);
        } catch (IOException e) {
            // Handle exception appropriately
            e.printStackTrace();
        }

        return attachment;
    }

    public byte[] convertMultipartFileToByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    @GetMapping("/mailbox")
    public String getMailboxPage(Model model){
        int userid = 2;
        User user = userDAO.getUserById(userid);
        List<Category> categories = categoryDAO.getAllCategories();
        model.addAttribute("user",user);
        model.addAttribute("categories", categories);

        return "mailbox";
    }
}
