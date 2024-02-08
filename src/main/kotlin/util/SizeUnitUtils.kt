package util

object SizeUnitUtils {

    private const val BYTES_IN_KB = 1024.0
    private const val BYTES_IN_MB = 1024.0 * 1024.0
    private const val BYTES_IN_GB = 1024.0 * 1024.0 * 1024.0

    enum class SizeUnit(val shortUnit: String) {
        BYTE("B"),
        KILOBYTE("KB"),
        MEGABYTE("MB"),
        GIGABYTE("GB");
    }

    data class Size(
        val size: Double,
        val unit: SizeUnit,
    ) {
        fun displayString(precision: Int = 2): String = "${String.format("%.2f", size)} ${unit.shortUnit}"
    }

    fun bytesToKB(bytes: Double): Double {
        return bytes / BYTES_IN_KB
    }

    fun bytesToMB(bytes: Double): Double {
        return bytes / BYTES_IN_MB
    }

    fun bytesToGB(bytes: Double): Double {
        return bytes / BYTES_IN_GB
    }

    fun bytesToHumanReadable(bytes: Double): Size {
        return when {
            bytes > BYTES_IN_GB -> Size(bytesToGB(bytes), SizeUnit.GIGABYTE)
            bytes > BYTES_IN_MB -> Size(bytesToMB(bytes), SizeUnit.MEGABYTE)
            bytes > BYTES_IN_KB -> Size(bytesToKB(bytes), SizeUnit.KILOBYTE)
            else -> Size(bytes, SizeUnit.BYTE)
        }
    }

}