import kotlin.reflect.KClass
import kotlin.script.experimental.api.*

class ScriptProvider {
    val sourceCode = SourceCodeImp()

    fun eval(code: String, compilation: Map<String, KClass<*>>, runtime: Map<String, Any>) {
        sourceCode.text = code

        val compilationConfiguration = ScriptCompilationConfiguration {
            dependencies(Main.dep)

            if (compilation.isNotEmpty()) {
                val map = HashMap<String, KotlinType>()
                compilation.forEach { (key, obj) ->
                    map[key] = KotlinType(obj::class)
                }

                providedProperties.put(map)
            }
        }

        val evaluationConfiguration = ScriptEvaluationConfiguration {
            enableScriptsInstancesSharing()

            if (runtime.isNotEmpty()) {
                providedProperties.put(runtime)
            }
        }

        val result2 = Main.host.eval(sourceCode, compilationConfiguration, evaluationConfiguration)

        println("Child script result:")
        result2.reports.forEach { println(it.render()) }
    }
}