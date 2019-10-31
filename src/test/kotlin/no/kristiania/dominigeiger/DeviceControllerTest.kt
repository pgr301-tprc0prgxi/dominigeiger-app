package no.kristiania.dominigeiger

import io.micrometer.core.instrument.MeterRegistry
import no.kristiania.dominigeiger.db.Device
import no.kristiania.dominigeiger.db.DeviceMeasurement
import no.kristiania.dominigeiger.db.DeviceMeasurementRepository
import no.kristiania.dominigeiger.db.DeviceRepository
import no.kristiania.dominigeiger.dto.DeviceMeasurementDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import javax.transaction.Transaction
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DeviceControllerTest() {
    @Autowired private lateinit var deviceController: DeviceController
    @Autowired private lateinit var deviceRepository: DeviceRepository
    @Autowired private lateinit var deviceMeasurementRepository: DeviceMeasurementRepository
    @Autowired private lateinit var meterRegistry: MeterRegistry

    @Test
    fun list() {
        assertThat(deviceController.list().body).isNotNull.isEmpty()

        deviceRepository.save(Device())

        assertThat(deviceController.list().body)
                .isNotNull.hasSize(1)
    }

    @Test
    fun create() {
        assertThat(deviceRepository.count()).isEqualTo(0)

        val counter = meterRegistry.counter("counter.metersCreated")

        assertThat(counter.count()).isEqualTo(0.0)
        deviceController.create()
        assertThat(counter.count()).isEqualTo(1.0)

        assertThat(deviceRepository.count()).isEqualTo(1)
    }

    @Test
    fun createMeasurements() {
        var device = deviceRepository.save(Device())
        assertThat(device.measurements).isEmpty()

        val counter = meterRegistry.counter("counter.meterNotFound")

        assertThat(counter.count()).isEqualTo(0.0)
        deviceController.createMeasurements(device.id!!, DeviceMeasurementDto(1.0f, 1.2f, 4))
        assertThat(counter.count()).isEqualTo(0.0)
        device = deviceRepository.findByIdOrNull(device.id!!)!!

        assertThat(device)
                .hasFieldOrPropertyWithValue("id", device.id)
        assertThat(device.measurements)
                .hasSize(1)
    }

    @Test
    fun getMeasurements() {
        val device = deviceRepository.save(Device())
        assertThat(device.measurements).isEmpty()
        val counter = meterRegistry.counter("counter.meterNotFound")

        assertThat(counter.count()).isEqualTo(0.0)
        assertThat(deviceController.getMeasurements(device.id!!).body).isEmpty()
        assertThat(counter.count()).isEqualTo(0.0)

        deviceMeasurementRepository.save(DeviceMeasurement(1.0f, 1.2f, 4, device))

        assertThat(deviceController.getMeasurements(device.id!!).body).hasSize(1)
    }
}