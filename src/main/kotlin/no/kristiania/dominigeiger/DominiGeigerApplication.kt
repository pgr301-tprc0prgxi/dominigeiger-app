package no.kristiania.dominigeiger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableJpaRepositories("no.kristiania.dominigeiger")
@EntityScan("no.kristiania.dominigeiger")
@EnableSwagger2
class DominiGeigerApplication

fun main(args: Array<String>) {
    runApplication<DominiGeigerApplication>(*args)
}
