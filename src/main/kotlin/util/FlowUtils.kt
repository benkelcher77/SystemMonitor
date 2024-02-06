package util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

object FlowUtils {

    fun <T> Flow<T>.accumulate(size: Int): Flow<List<T>> {
        val list = mutableListOf<T>()
        return transform { value ->
            list.add(value)
            if (list.size > size) list.removeFirst()
            return@transform emit(list)
        }
    }

}