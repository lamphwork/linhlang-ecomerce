package linhlang.commons.storage;

import linhlang.commons.model.ApiResponse;
import linhlang.commons.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class UploadEndpoint {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<String>>> upload(
            @RequestParam String folder,
            @RequestPart MultipartFile[] files) {
        return ResponseUtils.success(storageService.uploadFile(folder, files));
    }
}
