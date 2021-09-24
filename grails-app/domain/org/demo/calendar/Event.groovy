package org.demo.calendar

import java.time.LocalDateTime;

class Event {

    // not persisted object - used only for json rendering
    static mapWith = "none"

    Long id
    String name
    LocalDateTime startTime
    LocalDateTime endTime

    static constraints = {
        name required: true
        startTime nullable: true
        endTime nullable: true
    }

    static mapping = {
        id generator:'assigned'
    }


}