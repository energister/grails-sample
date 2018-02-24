package energister.grails.sample

class BootStrap {

    def init = { servletContext ->
        JavaTimeCategory.load()
    }
    def destroy = {
    }
}
