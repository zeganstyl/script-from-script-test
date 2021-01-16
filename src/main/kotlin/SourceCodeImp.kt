import kotlin.script.experimental.api.SourceCode

class SourceCodeImp: SourceCode {
    override val locationId: String?
        get() = null
    override val name: String?
        get() = null
    override var text: String = ""
}
