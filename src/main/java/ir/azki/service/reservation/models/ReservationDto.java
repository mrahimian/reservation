package ir.azki.service.reservation.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;        // reservation ID
    private Long slotId;    // associated slot
    private Long userId;    // user who reserved
    private LocalDateTime reservedAt; // timestamp of reservation
}
