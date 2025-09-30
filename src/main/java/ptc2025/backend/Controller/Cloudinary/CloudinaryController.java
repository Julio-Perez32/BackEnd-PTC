package ptc2025.backend.Controller.Cloudinary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ptc2025.backend.Services.Cloudinary.CloudinaryDocumentService;
import ptc2025.backend.Services.Cloudinary.CloudinaryImageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/Files")
@CrossOrigin
public class CloudinaryController {

    @Autowired
    private final CloudinaryImageService service;

    @Autowired
    private final CloudinaryDocumentService documentService;

    public CloudinaryController(CloudinaryImageService service, CloudinaryDocumentService documentService){this.service = service;
        this.documentService = documentService;
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file){
        try{
            String imageUrl = service.uploadImage(file);
            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida exitosamente",
                    "url", imageUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    @PostMapping("/upload-to-image-folder")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file, @RequestParam String folder){
        try{
            String imageUrl = service.UploadImage(file,folder);
            return ResponseEntity.ok(Map.of(
                    "message", "Imagen en folder subida exitosamente",
                    "url", imageUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    @PostMapping("/uploadDocument")
    public ResponseEntity<?> uploadDocument(@RequestParam("document")MultipartFile file){
        try{
            String docUrl = documentService.uploadDocument(file);
            return ResponseEntity.ok(Map.of(
                    "message", "Documento subido exitosamente",
                    "url", docUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir el documento");
        }
    }

    @PostMapping("/upload-to-document-folder")
    public ResponseEntity<?> uploadDocument(@RequestParam("document")MultipartFile file, @RequestParam String folder){
        try {
            String docUrl = documentService.UploadDocument(file,folder);
            return ResponseEntity.ok(Map.of(
                    "message", "Documento en folder subido exitosamente",
                    "url", docUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir el documento");
        }
    }
}
