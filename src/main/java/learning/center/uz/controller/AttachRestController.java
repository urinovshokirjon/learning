package learning.center.uz.controller;

import jakarta.annotation.Resource;
import learning.center.uz.dto.attach.AttachDTO;
import learning.center.uz.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/rest/attach")
public class AttachRestController {

    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(attachService.save(file));
    }

    @PostMapping("/upload2")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getById(@PathVariable("fileName") String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                log.info("file is not {}", fileName);
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }


    @GetMapping("/download/{fineName}")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        log.info("file is not found {}", fileName);
        return attachService.download(fileName);
    }


    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        log.info("file not found {}", id);
        return ResponseEntity.ok(attachService.delete(id));
    }

    @GetMapping("/getUrl/{id}")
    public ResponseEntity<?> getUrl(@PathVariable("id") String id) {
        log.info("file not found {}", id);
        return ResponseEntity.ok(attachService.getURL(id));
    }

}
