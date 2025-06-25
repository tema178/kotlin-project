package org.tema.app.spring.config

import CorSettings
import IStatusAppSettings
import StatusProcessor

data class StatusAppSettings(
    override val corSettings: CorSettings,
    override val processor: StatusProcessor,
): IStatusAppSettings