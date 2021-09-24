package org.demo.calendar

import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings> {

    void setup() {
        mockController(CalendarController)
    }

    void "test calendar url mappings"() {

        expect: "calls to /calendar api to succeed"
        verifyUrlMapping("/calendar/events", controller: 'event', action: 'index', method: 'GET')
        verifyUrlMapping("/calendar/search", controller: 'event', action: 'search', method: 'GET') {
            name="meeting"
        }

        when: "calling /calendar urls"
        assertUrlMapping("/calendar/events", controller: 'event', action: 'index', method: 'GET')
        assertUrlMapping("/calendar/search", controller: 'event', action: 'search', method: 'GET') {
            name="meeting"
        }

        then: "no exception is thrown"
        noExceptionThrown()

    }

}