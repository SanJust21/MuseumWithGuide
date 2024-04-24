package com.example.MuseumTicketing.Service.Slot;

import com.example.MuseumTicketing.Model.*;
import com.example.MuseumTicketing.Repo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
public class CalendarEventService {
    @Autowired
    private ShowTimeRepo showTimeRepo;
    @Autowired
    private CalendarRepo calendarRepo;
    @Autowired
    private PublicDetailsRepo publicDetailsRepo;
    @Autowired
    private InstitutionDetailsRepo institutionDetailsRepo;
    @Autowired
    private ForeignerDetailsRepo foreignerDetailsRepo;
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private ShowTimeService showTimeService;


    public ResponseEntity<List<CalendarEvent>> createCalendar(LocalDate date) {

        List<CalendarEvent> excal = calendarRepo.findByStartDate(date);

        if (!excal.isEmpty()) {

            LocalDate startDate = excal.get(0).getStartDate();
            List<CalendarEvent> calendarEventsAll = new ArrayList<>();
            for (CalendarEvent event : excal) {
                if (event.getStartDate().equals(startDate)) {

                    List<Booking> expiredBookings = bookingRepo.findByExpireTimeBefore(LocalDateTime.now());

                    for (Booking expiredBooking : expiredBookings) {
                        Integer bId = expiredBooking.getBookingId();

                        if ("institution".equals(expiredBooking.getCategory())) {

                            InstitutionDetails institutionDetails = institutionDetailsRepo.findByBookingId(bId);
                            Integer expId = expiredBooking.getBookingId();
                            Integer instiutionId = institutionDetails.getBookingId();

                            if (expId.equals(instiutionId)) {
                                String paymentId = institutionDetails.getPaymentid();
                                if (paymentId == null) {
                                    LocalDate visitDate = institutionDetails.getVisitDate();
                                    LocalTime slotName = institutionDetails.getSlotName();

                                    event = calendarRepo.findFirstByStartDateAndStartTime(visitDate, slotName);

                                    if (event != null && expiredBooking.getTickets() != null) {
                                        Integer capacity = event.getCapacity();
                                        Integer ticket = expiredBooking.getTickets();
                                        event.setCapacity(capacity + ticket);
                                        calendarRepo.save(event);
                                        bookingRepo.delete(expiredBooking);

                                        log.info("institution Data : "+institutionDetails);
                                        institutionDetailsRepo.delete(institutionDetails);
                                    }
                                } else {
                                    log.info("payment id is present");
                                }
                            } else {
                                Booking booking = bookingRepo.findByBookingId(bId);
                                log.info("instituId : "+institutionDetails.getId());
                                bookingRepo.delete(booking);
                            }
                        } else if ("public".equals(expiredBooking.getCategory())) {

                            PublicDetails publicDetails = publicDetailsRepo.findByBookingId(bId);
                            Integer expId = expiredBooking.getBookingId();
                            Integer publicId = publicDetails.getBookingId();

                            if (expId.equals(publicId)) {
                                String paymentId = publicDetails.getPaymentid();
                                if (paymentId == null) {
                                    LocalDate visitDate = publicDetails.getVisitDate();
                                    LocalTime slotName = publicDetails.getSlotName();

                                    event = calendarRepo.findFirstByStartDateAndStartTime(visitDate, slotName);

                                    if (event != null && expiredBooking.getTickets() != null) {
                                        Integer capacity = event.getCapacity();
                                        Integer ticket = expiredBooking.getTickets();
                                        event.setCapacity(capacity + ticket);
                                        calendarRepo.save(event);
                                        bookingRepo.delete(expiredBooking);

                                        log.info("public Data : "+publicDetails);
                                        publicDetailsRepo.delete(publicDetails);
                                    }
                                } else {
                                    log.info("payment id is present");
                                }
                            }else {
                                Booking booking = bookingRepo.findByBookingId(bId);
                                bookingRepo.delete(booking);
                            }
                        } else {

                            ForeignerDetails foreignerDetails = foreignerDetailsRepo.findByBookingId(bId);
                            Integer expId = expiredBooking.getBookingId();
                            Integer foreignerId = foreignerDetails.getBookingId();
                            if (expId.equals(foreignerId)) {
                                String paymentId = foreignerDetails.getPaymentid();
                                if (paymentId == null) {
                                    LocalDate visitDate = foreignerDetails.getVisitDate();
                                    LocalTime slotName = foreignerDetails.getSlotName();

                                    event = calendarRepo.findFirstByStartDateAndStartTime(visitDate, slotName);

                                    if (event != null && expiredBooking.getTickets() != null) {
                                        Integer capacity = event.getCapacity();
                                        Integer ticket = expiredBooking.getTickets();
                                        event.setCapacity(capacity + ticket);
                                        calendarRepo.save(event);
                                        bookingRepo.delete(expiredBooking);

                                        log.info("foreigner Data : "+foreignerDetails);
                                        foreignerDetailsRepo.delete(foreignerDetails);
                                    }
                                } else {
                                    log.info("payment id is present");
                                }
                            } else {
                                Booking booking = bookingRepo.findByBookingId(bId);
                                bookingRepo.delete(booking);
                            }
                        }
                    }
                    calendarEventsAll.add(event);
                }
            }

            return ResponseEntity.ok(calendarEventsAll);

        } else {
            List<CalendarEvent> savedEvent1 = new ArrayList<>();

            ShowTime showTime = showTimeRepo.findById(102).orElseThrow();
            ShowTime showTime1 = showTimeRepo.findById(103).orElseThrow();
            ShowTime showTime2 = showTimeRepo.findById(104).orElseThrow();
            ShowTime showTime3 = showTimeRepo.findById(105).orElseThrow();
            ShowTime showTime4 = showTimeRepo.findById(106).orElseThrow();
            ShowTime showTime5 = showTimeRepo.findById(107).orElseThrow();

            CalendarEvent calendarEvent1 = new CalendarEvent();
            calendarEvent1.setStartDate(date);
            LocalDate endT1 = calendarEvent1.getStartDate().plusDays(30);
            calendarEvent1.setEndDate(endT1);
            calendarEvent1.setStartTime(showTime.getStartTime());
            calendarEvent1.setEndTime(showTime.getEndTime());
            calendarEvent1.setCapacity(showTime.getCapacity());
            calendarEvent1.setStatus(showTime.getStatus());
            calendarEvent1.setTotalCapacity(showTime.getTotalCapacity());
            CalendarEvent saveEnt1 = calendarRepo.save(calendarEvent1);
            savedEvent1.add(saveEnt1);

            CalendarEvent calendarEvent2 = new CalendarEvent();
            calendarEvent2.setStartDate(date);
            LocalDate end2 = calendarEvent2.getStartDate().plusDays(30);
            calendarEvent2.setEndDate(end2);
            calendarEvent2.setStartTime(showTime1.getStartTime());
            calendarEvent2.setEndTime(showTime1.getEndTime());
            calendarEvent2.setCapacity(showTime1.getCapacity());
            calendarEvent2.setStatus(showTime1.getStatus());
            calendarEvent2.setTotalCapacity(showTime1.getTotalCapacity());
            CalendarEvent saveEv2 = calendarRepo.save(calendarEvent2);
            savedEvent1.add(saveEv2);

            CalendarEvent calendarEvent3 = new CalendarEvent();
            calendarEvent3.setStartDate(date);
            calendarEvent3.setEndDate(calendarEvent3.getStartDate().plusDays(30));
            calendarEvent3.setStartTime(showTime2.getStartTime());
            calendarEvent3.setEndTime(showTime2.getEndTime());
            calendarEvent3.setCapacity(showTime2.getCapacity());
            calendarEvent3.setStatus(showTime2.getStatus());
            calendarEvent3.setTotalCapacity(showTime2.getTotalCapacity());
            CalendarEvent saveEv3 = calendarRepo.save(calendarEvent3);
            savedEvent1.add(saveEv3);

            CalendarEvent calendarEvent4 = new CalendarEvent();
            calendarEvent4.setStartDate(date);
            calendarEvent4.setEndDate(calendarEvent4.getStartDate().plusDays(30));
            calendarEvent4.setStartTime(showTime3.getStartTime());
            calendarEvent4.setEndTime(showTime3.getEndTime());
            calendarEvent4.setCapacity(showTime3.getCapacity());
            calendarEvent4.setStatus(showTime3.getStatus());
            calendarEvent4.setTotalCapacity(showTime3.getTotalCapacity());
            CalendarEvent saveEv4 = calendarRepo.save(calendarEvent4);
            savedEvent1.add(saveEv4);

            CalendarEvent calendarEvent5 = new CalendarEvent();
            calendarEvent5.setStartDate(date);
            calendarEvent5.setEndDate(calendarEvent5.getStartDate().plusDays(30));
            calendarEvent5.setStartTime(showTime4.getStartTime());
            calendarEvent5.setEndTime(showTime4.getEndTime());
            calendarEvent5.setCapacity(showTime4.getCapacity());
            calendarEvent5.setStatus(showTime4.getStatus());
            calendarEvent5.setTotalCapacity(showTime4.getTotalCapacity());
            CalendarEvent saveEv5 = calendarRepo.save(calendarEvent5);
            savedEvent1.add(saveEv5);

            CalendarEvent calendarEvent6 = new CalendarEvent();
            calendarEvent6.setStartDate(date);
            calendarEvent6.setEndDate(calendarEvent6.getStartDate().plusDays(30));
            calendarEvent6.setStartTime(showTime5.getStartTime());
            calendarEvent6.setEndTime(showTime5.getEndTime());
            calendarEvent6.setCapacity(showTime5.getCapacity());
            calendarEvent6.setStatus(showTime5.getStatus());
            calendarEvent6.setTotalCapacity(showTime5.getTotalCapacity());
            CalendarEvent saveEv6 = calendarRepo.save(calendarEvent6);
            savedEvent1.add(saveEv6);

            return ResponseEntity.ok(savedEvent1);
        }


    }
}

