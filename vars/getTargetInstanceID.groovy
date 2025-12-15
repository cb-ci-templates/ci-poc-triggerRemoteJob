import com.cloudbees.opscenter.context.remote.RemoteDirectory
import com.cloudbees.opscenter.context.remote.RemotePath

/*  Resolves a Controller name and jpb path to an Controller instanceID
controllerName=name of the target controller (for example "my-target-controller"),
jobPath=job_path/job_name jobname path (from controller root, f.e "my-foldername/my-jobname")
*/
@NonCPS
def call(String controllerName,String jobPath){
    //Note: Does not work if you put the controller name only; the full path to the target job is required. `//` is important as prefix
    String controllerPath = "${controllerName}/${jobPath}"
    RemotePath resolvedPath = RemoteDirectory.walk(controllerPath)
    if (resolvedPath != null) {
        println "Instance ID for '${controllerPath}': ${resolvedPath.getInstanceId()}"
    } else {
        println "Could not resolve path: ${controllerPath}"
    }
    return "jenkins://${resolvedPath.getInstanceId()}/${jobPath}"
}





