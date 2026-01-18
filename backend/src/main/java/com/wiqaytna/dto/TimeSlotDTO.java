package com.wiqaytna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Data Transfer Object for TimeSlot
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    private Long id;
    private Long doctorId;
    private Integer dayOfWeek;
    private String dayName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isAvailable;
}
