package com.timeworx.modules.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/8 8:17 PM
 */
@Data
public class AddOrUpdateDto {

    private Long eventId;

    @NotBlank(message = "theme empty")
    @Size(max = 60, message = "theme length limit")
    private String theme;

    @Size(max = 256, message = "desc length limit")
    private String desc = "";

    @Size(max = 128, message = "photoUrl length limit")
    private String photoUrl = "";

    @NotNull(message = "startTime empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "startTime has past")
    private Date startTime;

    @NotNull(message = "endTime empty")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "endTime has past")
    private Date endTime;

    @NotNull(message = "eventType empty")
    private Integer eventType;

    @Size(max = 256, message = "eventLink length limit")
    private String eventLink = "";

    @Digits(integer = 10, fraction = 2, message = "price incorrect")
    private Double price = 0.00;

    // 默认 1
    private Integer limit = 1;
}
