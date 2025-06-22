package learning.center.uz.controller;

import learning.center.uz.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/attach")
public class AttachController {

    @Autowired
    private AttachService attachService;

    @GetMapping("/goToUploadPage")
    public String goToUploadPage() {
        return "temp/attach-upload";
    }

    @PostMapping("/upload")
    public String create(@RequestBody @RequestParam("file") MultipartFile file) {
        attachService.save2(file);
        return "redirect:/attach/goToUploadPage";
    }

    @GetMapping(value = "/img/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
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
}
