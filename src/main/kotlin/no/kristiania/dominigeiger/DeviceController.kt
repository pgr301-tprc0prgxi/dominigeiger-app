package no.kristiania.dominigeiger

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.LongTaskTimer
import io.micrometer.core.instrument.MeterRegistry
import no.kristiania.dominigeiger.db.Device
import no.kristiania.dominigeiger.db.DeviceMeasurement
import no.kristiania.dominigeiger.db.DeviceMeasurementRepository
import no.kristiania.dominigeiger.db.DeviceRepository
import no.kristiania.dominigeiger.dto.DeviceMeasurementDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit

@RestController
class DeviceController(
        @Autowired private val deviceRepository: DeviceRepository,
        @Autowired private val deviceMeasurementRepository: DeviceMeasurementRepository,
        @Autowired private val meterRegistry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(DeviceController::class.java)

    private val creationCounter = Counter.builder("counter.metersCreated").description("Meters created").register(meterRegistry)
    private val notFoundCounter = Counter.builder("counter.meterNotFound").description("Meter not found").register(meterRegistry)
    private val sievertDistributionSummary = DistributionSummary.builder("distibution.sievert")
            .description("Sievert Distribution")
            .baseUnit("sievert")
            .publishPercentiles(0.3, 0.5, 0.95)
            .register(meterRegistry)

    @GetMapping("/devices")
    @Timed("List devices", longTask = true)
    fun list(): ResponseEntity<List<Device>> {
        val devices = deviceRepository.findAll()

        val ms = (Math.random() * 200).toLong()
        // Fake some long running task
        TimeUnit.MILLISECONDS.sleep(ms)
        logger.debug("Long running task ran for {} ms", ms)

        logger.info("List devices returned {} device(s)", devices.count())
        return ResponseEntity.status(200).body(devices.toList())
    }

    @PostMapping("/devices")
    @Timed
    fun create(): ResponseEntity<Device> {
        logger.debug("Created devices before create: {}", creationCounter.count())
        val device = deviceRepository.save(Device())
        logger.info("Created new device with id: {}", device.id)

        creationCounter.increment();
        logger.debug("Created devices after create: {}", creationCounter.count())

        return ResponseEntity.ok(device)
    }

    @PostMapping("/devices/{deviceId}/measurements")
    @Timed
    fun createMeasurements(@PathVariable("deviceId") deviceId: UUID, @RequestBody rawMeasurement: DeviceMeasurementDto): ResponseEntity<Device> {
        logger.debug("Searching for device by id: {}", deviceId)
        val device = deviceRepository.findByIdOrNull(deviceId)
        if (device == null) {
            logger.error("Could not find device by id: {}", deviceId)
            notFoundCounter.increment()
            return ResponseEntity.status(404).body(null)
        }
        logger.info("Device by id: {} had {} measurements before new", device.id, device.measurements.count())

        deviceMeasurementRepository.save(DeviceMeasurement(
                rawMeasurement.longitude,
                rawMeasurement.latitude,
                rawMeasurement.sievert,
                device
        ))
        sievertDistributionSummary.record(rawMeasurement.sievert.toDouble());
        logger.info("Registered reading of {} sievert at lat: {}, lng: {}", rawMeasurement.sievert, rawMeasurement.latitude, rawMeasurement.longitude)

        return ResponseEntity.ok(deviceRepository.findByIdOrNull(deviceId)!!)
    }

    @GetMapping("/devices/{deviceId}/measurements")
    @Timed
    fun getMeasurements(@PathVariable("deviceId") deviceId: UUID): ResponseEntity<List<DeviceMeasurement>> {
        logger.debug("Searching for device by id: {}", deviceId)
        val device = deviceRepository.findByIdOrNull(deviceId)
        if (device == null) {
            logger.error("Could not find device by id: {}", deviceId)
            notFoundCounter.increment();
            return ResponseEntity.status(404).body(null)
        }
        return ResponseEntity.ok(device.measurements.toList())
    }

}