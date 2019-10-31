package no.kristiania.dominigeiger

import io.micrometer.core.instrument.MeterRegistry
import no.kristiania.dominigeiger.db.DeviceMeasurementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class AverageDeviceMeasurementGauge(
        @Autowired private val deviceMeasurementRepository: DeviceMeasurementRepository,
        @Autowired private val meterRegistry: MeterRegistry
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        meterRegistry.gauge("Average measurements", deviceMeasurementRepository) {
            val value = deviceMeasurementRepository.averageSievert()
            value
        }
    }
}