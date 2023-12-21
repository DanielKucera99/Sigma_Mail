package cz.uhk.sigmamail;

import cz.uhk.sigmamail.model.AttachmentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Controller
public class AttachmentController {

    private AttachmentDAO attachmentDAO;

    public AttachmentController(AttachmentDAO attachmentDAO){
        this.attachmentDAO=attachmentDAO;
    }

    @GetMapping("/download/{attachmentId}")
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable int attachmentId){
        byte[] fileContent = attachmentDAO.getAttachmentById(attachmentId).getContent();
        InputStream inputStream = new ByteArrayInputStream(fileContent);
        InputStreamResource resource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachmentDAO.getAttachmentById(attachmentId).getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
