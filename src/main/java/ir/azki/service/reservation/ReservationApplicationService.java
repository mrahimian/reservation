package ir.azki.service.reservation;

import com.github.benmanes.caffeine.cache.Cache;
import ir.azki.data.reservation.ReservationRepository;
import ir.azki.service.reservation.models.ReservationDto;
import ir.azki.service.reservation.models.SlotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationApplicationService {

    private final ReservationRepository reservationRepository;
    private final Cache<String, List<SlotDto>> slotsCache;

    public List<SlotDto> getAvailableSlots(int page, int size) {
        var cacheKey = "page:" + page + ":size:" + size;

        var slots = slotsCache.getIfPresent(cacheKey);
        if (slots != null) {
            return slots;
        }

        slots = reservationRepository.findAvailableSlots(page, size);
        slotsCache.put(cacheKey, slots);
        return slots;
    }

    public ReservationDto reserveSlot(Long slotId, Long userId) {
        //First cache invalidation to prevent probable inconsistency
        var reservation = reservationRepository.reserveSlot(slotId, userId);
        slotsCache.asMap().keySet().forEach(slotsCache::invalidate);
        return reservation;
    }

    public void cancelReservation(Long reservationId, Long userId) {
        reservationRepository.cancelReservation(reservationId, userId);
        slotsCache.asMap().keySet().forEach(slotsCache::invalidate);
    }
}
