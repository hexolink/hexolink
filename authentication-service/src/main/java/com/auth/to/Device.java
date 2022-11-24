package com.auth.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Device implements Serializable {

    @NotBlank
    @JsonProperty("device_factory_identifier")
    private String deviceFactoryIdentifier;
    @JsonProperty("device_brand")
    private String deviceBrand;
    @JsonProperty("device_model")
    private String deviceModel;
    @JsonProperty("system")
    private String system;
    @JsonProperty("system_version")
    private String systemVersion;
    @JsonProperty("fCMToken")
    private String fCMToken;
}
