package ch.hearc.shaketodo.model

import java.time.LocalDateTime
import java.util.*

class FactoryToDo {
    companion object {
        public fun createToDo(
            name: String,
            duedate: String,
            notes: String,
            imageLocation: String,
            priority: Int,
            completed: Boolean
        )
                : ToDo {
            var createdate = LocalDateTime.now();
            return ToDo(
                0,
                name,
                createdate.toString(),
                duedate,
                notes,
                imageLocation,
                priority,
                completed
            )
        }
    }
}