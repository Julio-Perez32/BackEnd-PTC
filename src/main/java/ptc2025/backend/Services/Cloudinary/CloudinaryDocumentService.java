package ptc2025.backend.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryDocumentService {

    private static final long MAX_DOC_SIZE = 2000 * 1024 * 1024; // 2GB
    private static final String[] ALLOWED_DOCS = {".pdf", ".doc", ".docx"};

    private final Cloudinary cloudinary;

    public CloudinaryDocumentService(@Qualifier("cloudinaryDocs") Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Subir documento simple
    public String uploadDocument(MultipartFile file) throws IOException {
        validateDocument(file);

        File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
        file.transferTo(tempFile);

        Map<?, ?> uploadResult = cloudinary.uploader()
                .upload(tempFile, ObjectUtils.asMap(
                        "resource_type", "raw"
                ));

        tempFile.delete();

        return (String) uploadResult.get("secure_url");
    }

    // Subir documento a folder con nombre único
    public String UploadDocument(MultipartFile file, String folder) throws IOException {
        validateDocument(file);

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        String uniqueFileName = "doc_" + UUID.randomUUID() + extension;

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,
                "public_id", uniqueFileName,
                "use_filename", false,
                "unique_filename", false,
                "overwrite", false,
                "resource_type", "raw"
        );

        File tempFile = File.createTempFile("upload_", originalFileName);
        file.transferTo(tempFile);

        Map<?, ?> uploadResult = cloudinary.uploader().upload(tempFile, options);

        tempFile.delete();

        return (String) uploadResult.get("secure_url");
    }

    // Validación de documento
    private void validateDocument(MultipartFile file) {
        if (file.isEmpty())
            throw new IllegalArgumentException("El documento no puede estar vacío");

        if (file.getSize() > MAX_DOC_SIZE)
            throw new IllegalArgumentException("El tamaño del archivo no puede exceder los 2GB");

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null)
            throw new IllegalArgumentException("Nombre de documento no válido");

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_DOCS).contains(extension))
            throw new IllegalArgumentException("Solo se permiten documentos .pdf, .doc, .docx");

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.equals("application/msword") &&
                        !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new IllegalArgumentException("El archivo debe ser un documento válido");
        }
    }
}
