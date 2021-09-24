package org.demo.calendar

import grails.rest.*

class CalendarController extends RestfulController {

    static responseFormats = ['json', 'xml']

    CalendarController() {
        super(CalendarEvent)
    }

    CalendarService calendarService

    def search(String name, Integer max) {
        if (name) {
            respond calendarService.search(name, max)
        } else {
            respond([]) 
        }
    }

    def findByDate(String startDate, String endDate) {
        if (startDate) {
            endDate ? respond(calendarService.findByDate(startDate, endDate)) : respond(calendarService.findAfterDate(startDate))
        } else {
            respond([])
        }
    }
}
