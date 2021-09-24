package org.demo.calendar

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

//@Integration
class CalendarControllerSpec extends Specification implements ControllerUnitTest<CalendarController>, DataTest {

    void setup() {
        CalendarEvent.saveAll(
                new CalendarEvent(name: 'meeting 1'),
                new CalendarEvent(name: 'meeting 2', startDateTime: LocalDateTime.of(2021, Month.SEPTEMBER, 20, 11, 45, 00)),
                new CalendarEvent(name: 'weekly'),
                new CalendarEvent(name: 'monthly')
        )
    }

    def cleanup() {
    }

    void testIndex() {
        when:
        controller.index()

        then:
        response.json.size() == 3
    }

    void 'test the search action finds results'() {
        given:
        when: 'A query is executed that finds results'
        controller.search('meeting', 10)

        then: 'The response is correct'
        response.json.size() == 2
        response.json[0].name == 'meeting 1'
    }
}