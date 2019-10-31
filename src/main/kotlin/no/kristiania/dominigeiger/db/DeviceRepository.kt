package no.kristiania.dominigeiger.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository: CrudRepository<Device, UUID>