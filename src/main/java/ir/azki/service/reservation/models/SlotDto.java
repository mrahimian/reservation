package ir.azki.service.reservation.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean reserved;
}
