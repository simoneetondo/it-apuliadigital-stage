package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.agent.tool.Tool;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDate;


@Transactional
@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking>
{

    @Tool("Cancel a booking")
    @Transactional
    public void cancelBooking(long bookingId, String customerFirstName, String customerLastName) {
        var booking = getBookingDetails(bookingId, customerFirstName, customerLastName);
        // too late to cancel
        if (booking.dateFrom.minusDays(11).isBefore(LocalDate.now())) {
            throw new Exceptions.BookingCannotBeCancelledException(bookingId, "booking from date is 11 days before today");
        }
        // too short to cancel
        if (booking.dateTo.minusDays(4).isBefore(booking.dateFrom)) {
            throw new Exceptions.BookingCannotBeCancelledException(bookingId, "booking period is less than four days");
        }
        delete(booking);
    }
}
