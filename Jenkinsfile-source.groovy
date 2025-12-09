//env.SHARED_LIB_TAG should be injected from (Folder) environment properties
env.SHARED_LIB_TAG="main"
library identifier: "ci-shared-library@${env.SHARED_LIB_TAG}", retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-poc-triggerRemoteJob.git'])


pipeline {
    agent none
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                //triggerRemoteJob mode: trackProgressAwaitResult(allowAbort: false, scheduledTimeout: [timeoutStr: ''], startedTimeout: [timeoutStr: ''], timeout: [timeoutStr: '1d'], whenFailure: stopAsFailure(), whenScheduledTimeout: continueAsIs(), whenStartedTimeout: continueAsIs(), whenTimeout: continueAsFailure(), whenUnstable: continueAsUnstable()), remotePathMissing: continueAsIs(), remotePathUrl: 'jenkins://b52f0c5313985cbde80b7602b63bcc51/test-triggers/child'
                //triggerRemoteJob parameterFactories: [[$class: 'SimpleString', name: 'paramKey1', value: 'paramtValueFromparent']], remotePathMissing: stopAsFailure(), remotePathUrl: "jenkins://${instanceID}/test-triggers/child"
                triggerRemoteJob remotePathMissing: stopAsFailure(), remotePathUrl: getTargetInstanceID("target","child")                //build 'child'
            }
        }
    }
}


