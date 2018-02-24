package energister.grails.sample

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"rates")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
