package ir.azki.controller.reservation.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class ReserveResponse {
    @JsonProperty("reserve_id")
    private Long reserveId; // reservation ID
    @JsonProperty("reserved_at")
    private LocalDateTime reservedAt; // timestamp of reservation
}

