package com.relatosdepapel.catalogue.controller;

import com.relatosdepapel.catalogue.entity.Book;
import com.relatosdepapel.catalogue.repository.BookRepository;
import com.relatosdepapel.catalogue.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Subir imagen de portada para un libro
     * POST /api/files/upload/book/{bookId}
     */
    @PostMapping("/upload/book/{bookId}")
    public ResponseEntity<?> uploadBookCover(@PathVariable Long bookId,
                                             @RequestParam("file") MultipartFile file) {
        try {
            // Verificar que el libro existe
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Libro no encontrado con id: " + bookId));
            }

            Book book = bookOptional.get();

            // Si el libro ya tiene una imagen, eliminarla
            if (book.getCoverImageUrl() != null) {
                try {
                    String oldFileName = book.getCoverImageUrl().substring(book.getCoverImageUrl().lastIndexOf('/') + 1);
                    fileStorageService.deleteFile(oldFileName);
                } catch (Exception e) {
                    // Log error pero continuar con la subida de la nueva imagen
                    System.err.println("Error al eliminar imagen anterior: " + e.getMessage());
                }
            }

            // Guardar el nuevo archivo
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = fileStorageService.getFileUrl(fileName);

            // Actualizar el libro con la nueva URL de imagen
            book.setCoverImageUrl(fileUrl);
            bookRepository.save(book);

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileUrl", fileUrl);
            response.put("bookId", bookId);
            response.put("message", "Imagen de portada subida exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo subir el archivo: " + e.getMessage()));
        }
    }

    /**
     * Subir imagen sin asociarla a un libro (para uso posterior)
     * POST /api/files/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = fileStorageService.getFileUrl(fileName);

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("fileUrl", fileUrl);
            response.put("message", "Archivo subido exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo subir el archivo: " + e.getMessage()));
        }
    }

    /**
     * Eliminar imagen de portada de un libro
     * DELETE /api/files/book/{bookId}/cover
     */
    @DeleteMapping("/book/{bookId}/cover")
    public ResponseEntity<?> deleteBookCover(@PathVariable Long bookId) {
        try {
            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Libro no encontrado con id: " + bookId));
            }

            Book book = bookOptional.get();
            
            if (book.getCoverImageUrl() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "El libro no tiene imagen de portada"));
            }

            // Extraer nombre del archivo de la URL
            String fileName = book.getCoverImageUrl().substring(book.getCoverImageUrl().lastIndexOf('/') + 1);
            fileStorageService.deleteFile(fileName);

            // Actualizar el libro
            book.setCoverImageUrl(null);
            bookRepository.save(book);

            return ResponseEntity.ok(Map.of("message", "Imagen de portada eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo eliminar el archivo: " + e.getMessage()));
        }
    }
}
