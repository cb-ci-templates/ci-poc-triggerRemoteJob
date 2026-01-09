env.SHARED_LIB_TAG="main"
library identifier: "ci-shared-library@${env.SHARED_LIB_TAG}", retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/cb-ci-templates/ci-poc-triggerRemoteJob.git'])

pipeline {
    agent {
        label 'built-in'
    }
    parameters {
        string defaultValue: 'test', name: 'test'
    }
    stages {
        stage('Hello') {
            steps {
                echo "Hello world"
                // This usually contains "origin/branch-name" or just "branch-name"
                echo "Git Branch: ${env.GIT_BRANCH}"
                
                // If you need the commit hash
                echo "Git Commit: ${env.GIT_COMMIT}"
                script {
                    // Get branch name, stripping 'origin/' if present
                    def branch = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                    
                    // Sometimes Jenkins checks out a specific commit (detached HEAD)
                    // If so, you might want the commit hash instead
                    if (branch == "HEAD") {
                        branch = sh(script: "git rev-parse HEAD", returnStdout: true).trim()
                    }
                    
                    echo "Actual Checked out ref: ${branch}"
                }
                    
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
