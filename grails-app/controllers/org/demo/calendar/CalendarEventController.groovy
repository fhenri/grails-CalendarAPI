package org.demo.calendar

import grails.rest.*
import grails.converters.*
import java.time.LocalDateTime

class CalendarEventController extends RestfulController {
    static responseFormats = ['json', 'xml']
    CalendarEventController() {
        super(CalendarEvent)
    }

    // XXX would need to create a service for calendar event and move some of those methods below (handle cases again when start and end dates are not valid)
    def search(String name, Integer max) { 
        log.error "search {$name}"
        if (name) {
            def query = CalendarEvent.where { 
                name ==~ ~/$name/
            }
            respond query.list(max: Math.min( max ?: 10, 100)) 
        }
        else {
            respond([]) 
        }
    }

}
