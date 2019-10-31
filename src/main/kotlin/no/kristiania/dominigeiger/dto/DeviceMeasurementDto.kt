package no.kristiania.dominigeiger.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

class DeviceMeasurementDto(
        @ApiModelProperty("The longitude of the reading")
        @get:NotNull
        var longitude: Float,

        @ApiModelProperty("The latitude of the reading")
        @get:NotNull
        var latitude: Float,

        @ApiModelProperty("The reading measured in sievert")
        @get:NotNull
        var sievert: Int
)