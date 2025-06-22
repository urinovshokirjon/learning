package learning.center.uz.service;

import learning.center.uz.dto.attach.AttachDTO;
import learning.center.uz.entity.AttachEntity;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.repository.AttachRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${server.url}")
    private String serverUrl;
    @Value("${attach.upload.folder}")
    private String folderName;


    public AttachDTO save(MultipartFile file) {
        try {

            String pathFolder = getYmDString(); // 2022/04/23
            File folder = new File(folderName + pathFolder);
            if (!folder.exists()) { // uploads/2022/04/23
                folder.mkdirs();
            }
            String extension = getExtension(file.getOriginalFilename());
            String key = UUID.randomUUID().toString() + "." + extension;

            byte[] bytes = file.getBytes();

            Path path = Paths.get(folderName + pathFolder + "/" + key);
            Files.write(path, bytes);
            AttachEntity entity = new AttachEntity();
            entity.setId(key);
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setPath(pathFolder);

            attachRepository.save(entity);
            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String save2(MultipartFile file) {
        try {

            String pathFolder = getYmDString(); // 2022/04/23
            File folder = new File(folderName + pathFolder);
            if (!folder.exists()) { // uploads/2022/04/23
                folder.mkdirs();
            }
            String extension = getExtension(file.getOriginalFilename());
            String key = UUID.randomUUID().toString() + "." + extension;

            byte[] bytes = file.getBytes();

            Path path = Paths.get(folderName + pathFolder + "/" + key);
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setId(key);
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setPath(pathFolder);
            attachRepository.save(entity);
            return key;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadImage(String attachId) {
        AttachEntity entity = get(attachId);
        byte[] data;
        try {
            Path file = Paths.get(folderName + entity.getPath() + "/" + attachId);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            log.warn("get attach by id {}", attachId);
            e.printStackTrace();
        }
        return new byte[0];
    }

    public ResponseEntity download(String attachId) {
        try {
            String id = attachId.substring(0, attachId.lastIndexOf("."));
            AttachEntity entity = get(id);
            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("could not read the file");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Boolean delete(String id) {
        Optional<AttachEntity> optional = attachRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadException("delete was not found");
        }
        AttachEntity entity = optional.get();
        File file = new File(String.valueOf(Path.of("uploads/" + entity.getPath() + "/" + entity.getId() + "." + entity.getExtension())));
        file.delete();
        attachRepository.delete(entity);
        return true;
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setExtension(entity.getExtension());
        return dto;
    }

    public AttachDTO getURL(String id) {
        AttachDTO dto = new AttachDTO();
        AttachEntity entity = get(id);
        dto.setUrl(serverUrl + "/attach/any/" + entity.getId() + "." + entity.getExtension());
        return dto;
    }

    public String getOnlyUrl(String id) {
        return id == null ? null : serverUrl + "/attach/img/" + id;
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() ->
                new AppBadException("File not found"));
    }


}
