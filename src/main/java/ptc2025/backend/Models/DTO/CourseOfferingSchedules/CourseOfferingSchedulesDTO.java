package ptc2025.backend.Models.DTO.CourseOfferingSchedules;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseOfferingSchedulesDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String courseOfferingID;
    @NotBlank
    private String weekday;
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;
    @NotBlank
    private String classroom;

    private String courseoffering;
}
