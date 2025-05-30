package linhlang.webconfig.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveBannerRequest {

    @NotBlank
    private String imageUrl;

    @Min(0)
    private Integer orderIndex;
}
