import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.JvmDependencyFromClassLoader
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object Main {
    val scriptProvider = ScriptProvider()

    val dep = JvmDependencyFromClassLoader { Main::class.java.classLoader }

    val host = BasicJvmScriptingHost()

    @JvmStatic
    fun main(args: Array<String>) {
        val parentScript = SourceCodeImp()

        parentScript.text = """
class SomeClass {
    fun some() = 123

    override fun toString() = "Some"
}

val someInst = SomeClass()

val compConf = mapOf(
"inst" to SomeClass::class
)

val evalConf = mapOf(
"inst" to someInst
)

println("parent script executes child script")

val childScript = "println(123)"

host.eval(childScript, compConf, evalConf)
"""

        val compileConf = ScriptCompilationConfiguration {
            dependencies(dep)

            providedProperties.put(
                mapOf(
                    "host" to KotlinType(ScriptProvider::class)
                )
            )
        }

        val evalConf = ScriptEvaluationConfiguration {
            enableScriptsInstancesSharing()

            providedProperties.put(
                mapOf(
                    "host" to scriptProvider
                )
            )
        }

        val result = host.eval(parentScript, compileConf, evalConf)

        println("Parent script result:")
        result.reports.forEach { println(it.render()) }
    }
}
