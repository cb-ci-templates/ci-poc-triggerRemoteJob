pipeline {
    agent {
        label 'built-in'
    }
    options { skipDefaultCheckout() } // Disable the broken auto-checkout
    parameters {
        string defaultValue: 'paramValue1', description: 'paramKey1=paramValue1', name: 'paramKey1', trim: true
    }
    stages {
        stage('Hello Branch2') {
            steps {
                echo "Hello Child Parameters: paramKey1=${params.paramKey1}"
                //sleep 1000
                echo "Git Branch: ${env.GIT_BRANCH}"
                
                // If you need the commit hash
                echo "Git Commit: ${env.GIT_COMMIT}"
                sh "ls -l"
                sh "cat .triggerTest"
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
            }
        }
    }
}
