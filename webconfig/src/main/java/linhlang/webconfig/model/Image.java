package linhlang.webconfig.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class Image {

    private String id;

    @NotBlank
    private String url;

    private Integer orderIndex;
}
