package com.github.gibbrich.airmee.core.repository

import androidx.lifecycle.MutableLiveData
import com.github.gibbrich.airmee.core.model.Range

interface ApartmentParametersRepository {
    val bedsNumber: MutableLiveData<Int>
    val range: MutableLiveData<Range?>
}

class ApartmentParametersDataRepository(
    override val bedsNumber: MutableLiveData<Int>,
    override val range: MutableLiveData<Range?>
) : ApartmentParametersRepository