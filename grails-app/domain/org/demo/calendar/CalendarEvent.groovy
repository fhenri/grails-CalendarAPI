package org.demo.calendar

import java.time.LocalDateTime

import com.fasterxml.jackson.annotation.JsonFormat
import grails.rest.*

@Resource(uri='/calendar/event')
class CalendarEvent {

    String name

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime = LocalDateTime.now()

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime

    static constraints = {
        name      required: true
        startTime nullable: true
        endTime   nullable: true
    }

    // XXX this could/should be moved in a service to handle the fact endTime could be null, less than start time and all condition so returning a new exception for those cases. for now if endTime is not provided we will assume it is startTime + 1 hour and we dont handle case when endTime is not greater than startTime
    def beforeValidate() {
        endTime   = !endTime ? ((startTime == null) ? LocalDateTime.now().minusHours(-1) : startTime.minusHours(-1)) : endTime
    }

}
