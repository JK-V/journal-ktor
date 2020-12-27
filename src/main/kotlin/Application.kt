package com.jkv.journal

import com.jkv.journal.model.BlogEntry
import com.jkv.journal.model.blogEntries
import freemarker.cache.*
import freemarker.core.HTMLOutputFormat
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.receiveParameters
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(FreeMarker){
        //FreeMarker templates will be located in the templates directory inside our application resources
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        //helps convert control characters provided by the user to their corresponding HTML entities.
        outputFormat = HTMLOutputFormat.INSTANCE
    }
    routing {

        get("/"){
            call.respond(FreeMarkerContent("index.ftl", mapOf("entries" to blogEntries), ""))
        }

        post("/submit") {
            val params = call.receiveParameters()
            val headline = params["headline"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val body = params["body"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val newEntry = BlogEntry(headline, body)
            blogEntries.add(0, newEntry)
            call.respondHtml {
                body {
                    h1 {
                        +"Thanks for submitting your entry!"
                    }
                    p {
                        +"We've submitted your new entry titled "
                        b {
                            +newEntry.headline
                        }
                    }
                    p {
                        +"You have submitted a total of ${blogEntries.count()} articles!"
                    }
                    a("/") {
                        +"Go back"
                    }
                }
            }
        }

        static("/static"){
//            This instructs Ktor that everything under the URL /static should
//            be served using the files directory inside resources.
            resources("static")
        }
    }
}
