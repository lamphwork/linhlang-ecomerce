package linhlang.webconfig.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SaveStickyRequest {

    @NotBlank
    @Length(max = 300)
    private String title;

    @NotBlank
    @Length(max = 1024)
    private String content;

    private Boolean visible;
}
