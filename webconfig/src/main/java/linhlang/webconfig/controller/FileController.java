package linhlang.webconfig.controller;


import linhlang.commons.model.ApiResponse;
import linhlang.commons.utils.ResponseUtils;
import linhlang.webconfig.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/menu/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Secured("ROLE_ADMIN")
    @PostMapping(value = "/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<String>> uploadImages(@RequestPart MultipartFile files) throws IOException {
        return ResponseUtils.success(fileService.upload(files));
    }
}
