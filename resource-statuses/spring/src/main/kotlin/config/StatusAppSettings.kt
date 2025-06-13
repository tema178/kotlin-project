package io.tema.app.spring.config

import io.tema.app.spring.util.StatusProcessor

data class StatusAppSettings(
//    override val corSettings: MkplCorSettings,
    override val processor: StatusProcessor,
): IStatusAppSettings