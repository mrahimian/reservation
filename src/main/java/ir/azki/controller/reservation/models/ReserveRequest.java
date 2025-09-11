package ir.azki.controller.reservation.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReserveRequest {
    @NotNull(message = "slotId is required")
    private Long slotId;
}

