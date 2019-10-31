package no.kristiania.dominigeiger

import io.micrometer.core.instrument.MeterRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class DominiGeigerApplicationTests {

    @Autowired
    var registry: MeterRegistry? = null

    @Test
    fun contextLoads() {
        registry?.counter("Test")
                ?.increment()
    }
}
