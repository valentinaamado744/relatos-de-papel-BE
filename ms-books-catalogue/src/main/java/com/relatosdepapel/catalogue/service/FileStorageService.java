package com.relatosdepapel.catalogue.service;

import com.relatosdepapel.catalogue.config.FileStorageProperties;
import com.relatosdepapel.catalogue.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("No se pudo crear el directorio donde se almacenarán los archivos.", ex);
        }
    }

    /**
     * Almacena un archivo y retorna el nombre del archivo almacenado
     */
    public String storeFile(MultipartFile file) {
        // Normalizar nombre del archivo
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Verificar si el archivo contiene caracteres inválidos
            if (originalFileName.contains("..")) {
                throw new FileStorageException("El nombre del archivo contiene una secuencia de ruta inválida " + originalFileName);
            }

            // Generar un nombre único para el archivo
            String fileExtension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copiar archivo al directorio de destino
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo almacenar el archivo " + originalFileName + ". Por favor intente de nuevo.", ex);
        }
    }

    /**
     * Genera la ruta relativa del archivo
     */
    public String getFileUrl(String fileName) {
        return "/uploads/" + fileName;
    }

    /**
     * Elimina un archivo del sistema
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("No se pudo eliminar el archivo " + fileName, ex);
        }
    }
}
