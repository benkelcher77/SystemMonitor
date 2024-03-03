package util

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

object ShellCommandUtils {

    // See https://stackoverflow.com/a/41495542. Note that this function does *not* invoke a shell, so no pipes or
    // redirection.
    fun String.runAsProcess(workingDir: File = File("/")): String? {
        try {
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.SECONDS)

            val stdout = proc.inputStream.bufferedReader().readText()
            return stdout
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}