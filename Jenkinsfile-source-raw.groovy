/**
 * For raw testing only, requires script aprovals.
 * In production, wrap ths behind sharedLib step. see Jenkinsfile-source.groovy
 *
 */

import com.cloudbees.opscenter.context.remote.RemoteDirectory
import com.cloudbees.opscenter.context.remote.RemotePath
import hudson.security.ACL
import jenkins.model.Jenkins

// @NonCPS is required here to prevent serialization issues with ACLs and internal objects
@NonCPS
String resolveInstanceId(String controllerPath) {
    String instanceId = null
    // Elevate privileges to SYSTEM to ensure visibility of the remote path
    //ACL.impersonate(ACL.SYSTEM, {
    RemotePath resolvedPath = RemoteDirectory.walk(controllerPath)
    if (resolvedPath != null) {
        instanceId = resolvedPath.getInstanceId()
    }
    //})

    return instanceId
}

node {
    stage('Resolve ID') {
        String controllerName="my-target-controller"
        String jobName="my-target-job"
        String controllerPath = "///${controllerName}/${jobName}"
        String id = resolveInstanceId(controllerPath)

        if (id) {
            println "Instance ID for '${controllerPath}': ${id}"
            triggerRemoteJob remotePathMissing: stopAsFailure(), remotePathUrl: "jenkins://${id}/${jobName}"

        } else {
            error "Could not resolve path: ${controllerPath} (Check path correctness)"
        }
    }
}