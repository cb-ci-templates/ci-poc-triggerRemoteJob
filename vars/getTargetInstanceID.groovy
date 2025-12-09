import com.cloudbees.opscenter.context.remote.RemoteDirectory
import com.cloudbees.opscenter.context.remote.RemotePath

def call(String controllerName,String jobPath){

    /* The controller name or path to resolve.  "target"= controller_name, "child" = job_path/job_name
     Note: Does not work if you put the controller name only; the full path to the target job is required. `//` is important as prefix
    */
    String controllerPath = "${controllerNameAndJobPath}"
    RemotePath resolvedPath = RemoteDirectory.walk(controllerNameAndJobPath)
    if (resolvedPath != null) {
        println "Instance ID for '${controllerNameAndJobPath}': ${resolvedPath.getInstanceId()}"
    } else {
        println "Could not resolve path: ${controllerNameAndJobPath}"
    }
    return "jenkins://${resolvedPath.getInstanceId()}/${jobPath}"
}





