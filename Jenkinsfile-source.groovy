env.SHARED_LIB_TAG="main"
library identifier: "ci-shared-library@${env.SHARED_LIB_TAG}", retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-poc-triggerRemoteJob.git'])

pipeline {
    agent none
    parameters {
        string defaultValue: 'test', name: 'test'
    }
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
                // Trigger parameterized job
                triggerRemoteJob parameterFactories: [[$class: 'SimpleString', name: 'paramKey1', value: 'paramtValueFromparent']], remotePathMissing: stopAsFailure(), remotePathUrl: getTargetInstanceID("controllers/my-target-controller","folder/my-target-job")

                // Trigger normal job, without parameters
                // triggerRemoteJob remotePathMissing: stopAsFailure(), remotePathUrl: getTargetInstanceID("my-target-controller","my-target-job")

                // Trigger normal job "my-target-job" (organized in a folder "folder")  on a controller "my-target-controller" which organized in a Operations center folder "controllers"
                // triggerRemoteJob remotePathMissing: stopAsFailure(), remotePathUrl: getTargetInstanceID("controllers/my-target-controller","folder/my-target-job")

            }
        }
    }
}
