package no.kristiania.dominigeiger.db

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity()
data class Device(
        @JsonManagedReference
        @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
        var measurements: List<DeviceMeasurement> = emptyList(),

        @Id
        @GeneratedValue
        var id: UUID? = null,

        @Column(updatable = false)
        @CreatedDate
        var createdAt: Long? = null
)