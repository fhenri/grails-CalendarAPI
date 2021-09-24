package org.demo.calendar

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule

import java.time.ZoneId
import java.time.ZonedDateTime;

class BootStrap {

    def init = { servletContext ->


/*
curl -i -H "Accept: application/json" localhost:8080/calendar/event
curl -i -H "Accept: application/json" localhost:8080/calendar/event/1
curl -i -H "Accept: application/json" localhost:8080/calendar/events


curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event", "startDate":"2018-04-01T16:24:11.252+05:30[Asia/Calcutta]"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event", "startDate":"2021-09-23T16:30:00.000+02:00[Europe/Paris]"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event", "startDate":"2021-09-23T16:30:00.000+02:00[Europe/Paris]", "endDate":"2021-09-23T18:30:00.000+02:00[Europe/Paris]"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"yearly rrule test", "startDate":"2021-09-01T22:25:15.000+02:00[Europe/Paris]", "rrule":"FREQ=YEARLY;BYMONTH=4;BYDAY=SU;BYSETPOS=3"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"one year monthly upwork meeting", "startDate":"2020-01-01T22:25:15.000+02:00[Europe/Paris]", "rrule":"FREQ=MONTHLY;BYSETPOS=1;BYDAY=MO;INTERVAL=1;UNTIL=20221231T230000Z"}' localhost:8080/calendar/event
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"past year meeting", "startDate":"2020-09-23T22:25:15.000+02:00[Europe/Paris]", "rrule":"FREQ=MONTHLY;BYSETPOS=1;BYDAY=MO;INTERVAL=1;UNTIL=20201231T230000Z"}' localhost:8080/calendar/event

curl -i -H "Accept: application/json" "http://localhost:8080/calendar/search?name=meeting"
curl -i -H "Accept: application/json" "localhost:8080/calendar/search?name=meeting&max=1"

curl -i -H "Accept: application/json" "localhost:8080/calendar/findByDate?startDate=2021-09-01&endDate=2021-12-31"
curl -i -H "Accept: application/json" "localhost:8080/calendar/findByDate?startDate=2021-09-01T00:00:00&endDate=2021-12-31T23:00:00"
*/
        ZoneId localZone = ZoneId.of("Europe/Paris")
        new CalendarEvent(
            name:"dummy test event"
            ).save()

        new CalendarEvent(
            name : "Jan meeting",
            startDateTime: ZonedDateTime.of(2021, 9, 20, 11, 45, 00, 00, localZone)
            ).save()

        new CalendarEvent(
            name : "Test 2 hours meeting",
            startDateTime: ZonedDateTime.of(2021, 9, 22, 11, 45, 00, 00, localZone),
            endDateTime: ZonedDateTime.of(2021, 9, 22, 13, 55, 00, 00, localZone)
            ).save()

        new CalendarEvent(
            name : "Recurring weekly sport",
            startDateTime: ZonedDateTime.of(2021, 9, 1, 7, 30, 00,00, localZone),
            endDateTime: ZonedDateTime.of(2021, 9, 1, 8, 30, 00, 00, localZone),
            rrule : "FREQ=WEEKLY;BYDAY=WE;INTERVAL=1"
        ).save()
    }

    def destroy = {
    }
}
