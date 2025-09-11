package ir.azki.controller.reservation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.azki.controller.BaseController;
import ir.azki.controller.reservation.models.ReserveRequest;
import ir.azki.controller.reservation.models.ReserveResponse;
import ir.azki.service.reservation.ReservationApplicationService;
import ir.azki.service.reservation.models.ReservationDto;
import ir.azki.service.reservation.models.SlotDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reservation API", description = "APIs for managing reservations and slots")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController extends BaseController {

    private final ReservationApplicationService reservationApplicationService;

    /**
     * List available slots (paginated)
     */
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/slots")
    public ResponseEntity<List<SlotDto>> getAvailableSlots(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        var slots = reservationApplicationService.getAvailableSlots(page, size);
        return success(slots);
    }

    /**
     * Reserve a slot
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/reservations")
    public ResponseEntity<ReserveResponse> reserveSlot(@Valid @RequestBody ReserveRequest request) {
        var reservationDto = reservationApplicationService.reserveSlot(request.getSlotId(), getCurrentUserId());
        return success(new ReserveResponse(reservationDto.getId(), reservationDto.getReservedAt()));
    }

    /**
     * Cancel a reservation
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationApplicationService.cancelReservation(reservationId, getCurrentUserId());
        return success();
    }

    /**
     * Helper to extract userId from Authentication (JWT)
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }

}
