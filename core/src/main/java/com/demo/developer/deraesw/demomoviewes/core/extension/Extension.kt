package com.demo.developer.deraesw.demomoviewes.core.extension

import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkFailed
import com.demo.developer.deraesw.demomoviewes.core.data.model.NetworkResults

inline fun NetworkResults.whenFailed(action: (item: NetworkFailed) -> Unit) {
    this.takeIf { it is NetworkFailed }?.also {
        action(it as NetworkFailed)
    }
}
