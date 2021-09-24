package org.demo.calendar

class UrlMappings {

    static mappings = {

        group "/calendar", {
            "/search" (action: "search", controller:"calendar", method: "GET")
            "/findByDate" (action: "findByDate", controller:"calendar", method: "GET")
            // add just a plural to return all calendar events
            "/events" (action: "index", controller:"calendar", method: "GET")
        }

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
