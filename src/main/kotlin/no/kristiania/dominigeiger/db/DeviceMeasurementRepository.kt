package no.kristiania.dominigeiger.db

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceMeasurementRepository: CrudRepository<DeviceMeasurement, UUID> {
    @Query("SELECT avg(sievert) from DeviceMeasurement")
    fun averageSievert(): Double
}