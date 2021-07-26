package com.formssihk.sample.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
//4.6 changes
import org.hibernate.annotations.Type
import javax.persistence.ElementCollection


/**
 * The family of schemas for IOUState.
 */
object IOUSchema

/**
 * An IOUState schema.
 */
object IOUSchemaV1 : MappedSchema(
        schemaFamily = IOUSchema.javaClass,
        version = 1,
        mappedTypes = listOf(PersistentIOU::class.java)) {

    override val migrationResource: String?
        get() = "iou.changelog-master";

    @Entity
    @Table(name = "iou_states")
    class PersistentIOU(
            @Column(name = "creator")
            var creator: String,

            @Column(name = "value")
            var value: Int,

            // @ElementCollection
            // var listOfString: List<String>,

            @Column(name = "linear_id")
            @Type(type = "uuid-char")
            var linearId: UUID
    ) : PersistentState() {
        // Default constructor required by hibernate.
        constructor(): this("", 0, UUID.randomUUID())
    }
}