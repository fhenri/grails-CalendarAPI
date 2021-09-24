package org.demo.calendar

import java.time.LocalDateTime
import java.time.ZonedDateTime;

class Event {

    // not persisted object - used only for json rendering
    static mapWith = "none"

    Long id
    String name
    ZonedDateTime startTime
    ZonedDateTime endTime

    static constraints = {
        name required: true
        startTime nullable: true
        endTime nullable: true
    }

    static mapping = {
        id generator:'assigned'
    }


}