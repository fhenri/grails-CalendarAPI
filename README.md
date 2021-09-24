# grails-CalendarAPI
This is a code challenge app (keeping basic) to demo use of calendar recurring events


## Design
As we are dealing with recurrent events, I designed 2 domain classes:
* [CalendarEvent](grails-app/domain/org/demo/calendar/CalendarEvent.groovy) is a persisted domain which handles the necessary properties of a meeting. The recurrent pattern is based on `RRULE` pattern and is parsed using the [lib-recur](https://github.com/dmfs/lib-recur)
* [Event](grails-app/domain/org/demo/calendar/Event.groovy) domain is non persisted domain which will return all generated occurences of a recurrent meeting

## Rest-API

The following end-points have been defined

```
Dynamic Mappings
 |    *     | ERROR: 404                      | View:   /notFound            |
 |    *     | ERROR: 500                      | View:   /error               |

Controller: application
 |    *     | /                               | Action: index                |

Controller: calendar
 |   GET    | /calendar/search                | Action: search               |
 |   GET    | /calendar/findByDate            | Action: findByDate           |
 |   GET    | /calendar/events                | Action: index                |

Controller: calendarEvent
 |   GET    | /calendar/event/create          | Action: create               |
 |   GET    | /calendar/event/${id}/edit      | Action: edit                 |
 |   POST   | /calendar/event                 | Action: save                 |
 |   GET    | /calendar/event                 | Action: index                |
 |  DELETE  | /calendar/event/${id}           | Action: delete               |
 |  PATCH   | /calendar/event/${id}           | Action: patch                |
 |   PUT    | /calendar/event/${id}           | Action: update               |
 |   GET    | /calendar/event/${id}           | Action: show                 |
 ```

The [BootStrap](grails-app/init/org/demo/calendar/BootStrap.groovy) class defines some objects and some examples of using the endpoints. 

* `/calendar/events` will just return all events (as CalendarEvent) from the db
* `/calendar/search` will return all events (as CalendarEvent) matching a query name pattern
* `/calendar/findByDate` (most interesting end point) will retrieve all events (as generated Event) after a given date (given a max of 100 entries) or all events between 2 given dates. 

## Running Test

`grails test-app` will just return errors, I dont know for what reasons but grails 4 has changes on the Spec test and my tests are not working. Even the dummy ones on UrlMappings is not working.
the test on controllers are not working neither, I can overcome the current error by adding a @transactional annotation (which would make the test more as integration test) but anyway I get another error on hibernate session.
I spent a bit of time on this and moved to something else.

## ToDo

There are many things that could be improved. 

One would be to use json views so we could put more logic in the view (date conversion, parameters output) and potentially align the output of both `Event` and `CalendarEvent` class.

I am using the java.time API and specifically the ZonedDateTime to manage with TimeZone, it cannot be deserialize using jackson only, I am using transient field to run the conversion from a string to the zonedDateTime (registering and using a custom JSonDeserializer did not work !) There are also lot of conversion and date manipulation (with the org-dmfs DateTime object) which could be moved to Util method.
