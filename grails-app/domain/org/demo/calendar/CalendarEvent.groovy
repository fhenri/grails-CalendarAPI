package org.demo.calendar

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import java.time.*

import grails.rest.*
import org.dmfs.rfc5545.recur.RecurrenceRule

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Resource(uri='/calendar/event')
class CalendarEvent {

    String name

    static transients = ['startDate', 'endDate']
    String startDate
    String endDate

    ZonedDateTime startDateTime
    LocalTime startTime

    ZonedDateTime endDateTime
    LocalTime endTime

    int hour
    int minute
    long duration // in minutes

    String rrule
    boolean isRecurring = false

    LocalDate startRecurrence
    LocalDate endRecurrence

    static constraints = {
        name required: true
        startDateTime nullable: true
        endDateTime nullable: true
        startTime nullable: true
        endTime nullable: true
        rrule nullable: true
        startRecurrence nullable: true
        endRecurrence nullable: true
    }

    def beforeValidate() {
        startDateTime   = startDate ? ZonedDateTime.parse(startDate) : ZonedDateTime.now()
        // XXX this could/should be moved in a service to handle the fact endTime could be null, less than start time and all condition so returning a new exception for those cases. for now if endTime is not provided we will assume it is startTime + 1 hour and we dont handle case when endTime is not greater than startTime
        endDateTime     = !endDate ? ((startDateTime == null) ? ZonedDateTime.now().minusHours(-1) : startDateTime.minusHours(-1)) : ZonedDateTime.parse(endDate)
        startTime       = startDateTime.toLocalTime()
        endTime         = endDateTime.toLocalTime()
        hour            = startTime.hour
        minute          = startTime.minute
        duration        = ChronoUnit.MINUTES.between(startDateTime, endDateTime);

        if (rrule) {
            RecurrenceRule rule = new RecurrenceRule(rrule)
            isRecurring = true
            startRecurrence = startDateTime.toLocalDate()
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd\'T\'HHmmss\'Z\'");
            endRecurrence = rule.getUntil() ? LocalDate.parse(rule.getUntil().toString(), formatter) : null
        }
    }

}
