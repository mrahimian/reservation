package ir.azki.data.reservation;

import ir.azki.error.exceptions.BaseException;
import ir.azki.error.exceptions.NotFoundException;
import ir.azki.service.reservation.models.ReservationDto;
import ir.azki.service.reservation.models.SlotDto;
import ir.azki.tables.daos.AvailableSlotsDao;
import ir.azki.tables.daos.ReservationsDao;
import ir.azki.tables.pojos.ReservationsEntity;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static ir.azki.Tables.AVAILABLE_SLOTS;
import static ir.azki.Tables.RESERVATIONS;
import static ir.azki.error.ApplicationError.*;

@Repository
public class ReservationRepository {

    private final DSLContext dsl;
    private final ReservationsDao reservationsDao;
    private final AvailableSlotsDao availableSlotsDao;

    public ReservationRepository(DSLContext dsl, ReservationsDao reservationsDao, AvailableSlotsDao availableSlotsDao) {
        this.dsl = dsl;
        this.reservationsDao = reservationsDao;
        this.availableSlotsDao = availableSlotsDao;
    }

    /**
     * List available slots (not reserved) with pagination
     */
    public List<SlotDto> findAvailableSlots(int page, int size) {
        int offset = page * size;
        return dsl.selectFrom(AVAILABLE_SLOTS)
                .where(AVAILABLE_SLOTS.IS_RESERVED.eq((byte) 0)) // convert boolean to byte
                .orderBy(AVAILABLE_SLOTS.START_TIME)
                .offset(offset)
                .limit(size)
                .fetch(record -> new SlotDto(
                        record.get(AVAILABLE_SLOTS.ID),
                        record.get(AVAILABLE_SLOTS.START_TIME),
                        record.get(AVAILABLE_SLOTS.END_TIME),
                        record.get(AVAILABLE_SLOTS.IS_RESERVED) != 0 // convert byte → boolean
                ));
    }


    /**
     * Reserve a slot for a user
     */
    @Transactional
    public ReservationDto reserveSlot(Long slotId, Long userId) {
        // Atomic update: only succeeds if slot is not reserved
        int updatedRows = dsl.update(AVAILABLE_SLOTS)
                .set(AVAILABLE_SLOTS.IS_RESERVED, (byte) 1)
                .where(AVAILABLE_SLOTS.ID.eq(slotId))
                .and(AVAILABLE_SLOTS.IS_RESERVED.eq((byte) 0))
                .execute();

        if (updatedRows == 0) {
            // Slot already reserved
            throw new NotFoundException("Slot already reserved or does not exists. id: " + slotId, SLOT_NOT_AVAILABLE);
        }

        // Insert reservation
        var reserve = new ReservationsEntity()
                .setSlotId(slotId)
                .setUserId(userId)
                .setReservedAt(LocalDateTime.now());

        reservationsDao.insert(reserve);
        return new ReservationDto(reserve.getId(), reserve.getSlotId(), reserve.getUserId(), reserve.getReservedAt());
    }

    /**
     * Cancel a reservation
     */
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        var reservation = dsl.selectFrom(RESERVATIONS)
                .where(RESERVATIONS.ID.eq(reservationId)
                        .and(RESERVATIONS.USER_ID.eq(userId)))
                .fetchOne();

        if (reservation == null) {
            throw new NotFoundException("Reserve not found. id: " + reservationId, RESOURCE_NOT_FOUND);
        }

        int affectedRows = dsl.deleteFrom(RESERVATIONS)
                .where(RESERVATIONS.ID.eq(reservationId))
                .execute();

        if (affectedRows == 0) {
            throw new BaseException("Reserve already deleted. id:" + reservationId, SERVER_ERROR);
        }

        affectedRows = dsl.update(AVAILABLE_SLOTS)
                .set(AVAILABLE_SLOTS.IS_RESERVED, (byte) 0)
                .where(AVAILABLE_SLOTS.ID.eq(reservation.getSlotId()))
                .and(AVAILABLE_SLOTS.IS_RESERVED.eq((byte) 1))
                .execute();

        if (affectedRows == 0) {
            throw new BaseException("Slot conflict. id:" + reservation.getSlotId(), SERVER_ERROR);
        }
    }


}
