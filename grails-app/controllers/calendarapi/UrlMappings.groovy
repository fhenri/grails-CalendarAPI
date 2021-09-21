package calendarapi

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        '/calendar' (resources: 'CalendarEvent') {
            collection {
                '/search' (controller: "CalendarEvent", action: "search", method: "GET")
                // add just a plural to return all calendar events
                '/events' (controller: "CalendarEvent", action: "index", method: "GET")
            }
        }

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
