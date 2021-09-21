package calendarapi

import org.demo.calendar.CalendarEvent

import java.time.LocalDateTime
import java.time.Month;

class BootStrap {

    def init = { servletContext ->


/*
curl -i -H "Accept: application/json" localhost:8080/calendar/event
curl -i -H "Accept: application/json" localhost:8080/calendar/event/1

curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event"}' localhost:8080/calendar/event

curl -i -X POST -H "Content-Type: application/json" -d '{"name":"new meeting event", "startTime":"2021-09-25T09:30:00"}' localhost:8080/calendar/event

curl -i -H "Accept: application/json" localhost:8080/calendar/events
curl -i -H "Accept: application/json" localhost:8080/calendar/search?name=Jan

curl -i -H "Accept: application/json" "localhost:8080/calendar/search?name=event&max=1"
*/
    new CalendarEvent(
        name:"dummy test event"
        ).save()

    new CalendarEvent(
        name : "Jan meeting",
        startTime : LocalDateTime.of(2021, Month.SEPTEMBER, 20, 11, 45, 00)
        ).save()

    new CalendarEvent(
        name : "Test 2 hours meeting",
        startTime : LocalDateTime.of(2021, Month.SEPTEMBER, 22, 11, 45, 00),
        endTime : LocalDateTime.of(2021, Month.SEPTEMBER, 22, 13, 55, 00)
        ).save()

    }

    def destroy = {
    }
}
