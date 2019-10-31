package no.kristiania.dominigeiger.db

import com.fasterxml.jackson.annotation.JsonBackReference
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity
data class DeviceMeasurement(
        var longitude: Float? = null,
        var latitude: Float? = null,
        var sievert: Int? = null,

        @JsonBackReference
        @ManyToOne()
        @JoinColumn(name="device_id", nullable=false)
        var device: Device? = null,

        @Id
        @GeneratedValue
        var id: UUID? = null,

        @Column(updatable = false)
        @CreatedDate
        var createdAt: Long? = null
)