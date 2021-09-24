package org.demo.calendar

import grails.gorm.transactions.Transactional
import org.dmfs.rfc5545.DateTime
import org.dmfs.rfc5545.Duration
import org.dmfs.rfc5545.recur.RecurrenceRule
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Transactional
class CalendarService {

    ZoneId localZone = ZoneId.of("Europe/Paris")

    int maxRecurrences = 100
    DateTimeFormatter shortDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd\'T\'HHmmss\'Z\'");

    // ---- controller bindings ----
    List<CalendarEvent> search(String name, Integer max) {
        def query = CalendarEvent.where {
            name ==~ ~/$name/
        }
        query.list(max: Math.min(max ?: 10, 100))
    }

    List<Event> findByDate(String startDate, String endDate) {
        ZonedDateTime myStartTime = ZonedDateTime.of(LocalDate.parse(startDate).atStartOfDay(), localZone)
        ZonedDateTime myEndTime   = ZonedDateTime.of(LocalDate.parse(endDate).atStartOfDay(), localZone)
        List<Event> event = findEventsBetweenDate(myStartTime, myEndTime)
        List<Event> recurrentEvent = findRecurrentEventsBetweenDate(myStartTime.toLocalDate(), myEndTime.toLocalDate())
        // convert to set to remove duplicate
        Set<CalendarEvent> set = new LinkedHashSet<Event>(event);
        set.addAll(recurrentEvent);
        new ArrayList<Event>(set);
    }

    List<Event> findAfterDate(String startDate) {
        ZonedDateTime myStartTime = ZonedDateTime.of(LocalDate.parse(startDate).atStartOfDay(), localZone)
        List<Event> event = findEventsAfterDate(myStartTime)
        List<Event> recurrentEvent = findRecurringEventsAfterDate(myStartTime.toLocalDate(), maxRecurrences)
        // convert to set to remove duplicate
        Set<CalendarEvent> set = new LinkedHashSet<Event>(event);
        set.addAll(recurrentEvent);
        new ArrayList<Event>(set);
    }


    // --- util function to work on dates and the recurrence pattern ---
    List<Event> findEventsBetweenDate(ZonedDateTime startDate, ZonedDateTime endDate) {
        def list = []
        CalendarEvent.findAll(
                "from CalendarEvent as event " +
                "where startDateTime >= :start_time and endDateTime <= :end_time",
                [start_time: startDate, end_time: endDate],
                [sort:"startRecurrence", order:"asc"]).each {
            list.add (new Event(name: it.name, startTime: it.startTime, endTime: it.endTime))
        }
        list
    }

    List<Event> findEventsAfterDate(ZonedDateTime startDate) {
        def list = []
        CalendarEvent.findAll(
                "from CalendarEvent as event " +
                        "where startDateTime >= :start_time",
                [start_time: startDate],
                [sort:"startRecurrence", order:"asc"]).each {
            list.add (new Event(name: it.name, startTime: it.startTime, endTime: it.endTime))
        }
        list
    }

    List<Event> findRecurringEventsAfterDate(LocalDate startDate, Integer max) {
        List<Event> returnList = []
        getAllRecurringEventAvailableAfterDate(LocalDate.now()).each {
            RecurrenceRule rule = new RecurrenceRule(it.rrule)
            DateTime startOccurence = new DateTime(startDate.year, startDate.monthValue, startDate.dayOfMonth)
            RecurrenceRuleIterator ruleIterator = rule.iterator(startOccurence)
            while (ruleIterator.hasNext() && (!rule.isInfinite() || max-- > 0)) {
                DateTime nextInstance = ruleIterator.nextDateTime();
                LocalDate nextOccurenceDate = LocalDate.parse(nextInstance.toString(), shortDateFormatter)
                // we have the next occurence date, we need to setup time accordingly
                LocalDateTime startTime = nextOccurenceDate.atStartOfDay().plusHours(it.hour).plusMinutes(it.minute)
                returnList.add(new Event(
                        name: it.name,
                        startTime: startTime,
                        endTime: startTime.plusMinutes(it.duration)
                ))
            }
        }
        return returnList
    }

    List<Event> findRecurrentEventsBetweenDate(LocalDate startDate, LocalDate endDate) {
        List<Event> returnList = []
        getAllRecurringEventAvailableAfterDate(LocalDate.now()).each {
            RecurrenceRule rule = new RecurrenceRule(it.rrule)
            DateTime startOccurence = new DateTime(startDate.year, startDate.monthValue, startDate.dayOfMonth)
            RecurrenceRuleIterator ruleIterator = rule.iterator(startOccurence)
            while (ruleIterator.hasNext()) {
                DateTime nextInstance = ruleIterator.nextDateTime()
                LocalDate nextOccurenceDate = LocalDate.parse(nextInstance.toString(), shortDateFormatter)
                if (!nextOccurenceDate.isAfter(endDate)) {
                    // we have the next occurence date, we need to setup time accordingly
                    LocalDateTime startTime = nextOccurenceDate.atStartOfDay().plusHours(it.hour).plusMinutes(it.minute)
                    returnList.add(new Event(
                            name: it.name,
                            startTime: startTime,
                            endTime: startTime.plusMinutes(it.duration)
                    ))
                } else {
                    break
                }
            }
        }
        return returnList
    }

    List<CalendarEvent> getAllRecurringEvent() {
        CalendarEvent.findAll(sort:"startRecurrence", order:"asc") {
            isRecurring == true
        }
    }

    // we need to pull all event with no end recurrence defined as we can have some defined to start later in future
    // which would have occurences after this date
    // all recurring events with an end_recurrence_date define before this date can be exclude
    List<CalendarEvent> getAllRecurringEventAvailableAfterDate(LocalDate date) {
        CalendarEvent.findAll(
                "from CalendarEvent as event " +
                "where isRecurring = true and (endRecurrence is null or endRecurrence >= :query_date)", [query_date: date],
                [sort:"startRecurrence", order:"asc"])
    }
}