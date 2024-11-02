import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service
class GitCommitCheckerPlugin ( project : Project ){
    init {
        println("Git commit checker plugin started...")
    }
}