# Cross-Controller Pipeline Triggering Flow
    
![flow.png](flow.png)

## Shared Library Functions & Steps
* **`triggerRemoteJob`**: A pipeline step used to trigger a job on a remote controller. It requires a `remotePathUrl` and can optionally take parameters via `parameterFactories`.
* **`getTargetInstanceID` (Shared Library `call` function)**: A function that takes a `controllerName` and `jobPath` as input. It uses `RemoteDirectory.walk` to resolve the path's instance ID and returns the fully constructed `jenkins://` URL needed to trigger the remote job.

## Pipelines
* **[Source Pipeline](Jenkinsfile-source.groovy)**: The initiating pipeline script that loads a shared library and executes the `triggerRemoteJob` step to start a job on another controller.
* **[Target Pipeline](Jenkinsfile-target.groovy)**: The target pipeline script that defines parameters (e.g., `paramKey1`) and executes steps when triggered by an upstream process.
* **[Target Pipeline (raw/for testing)](Jenkinsfile-source-raw.groovy)**: Pipeline for raw testing without shared library (requires script approval, because it's not behind a shared library) 

## Key Concepts & Parameters
* **Remote URL Structure**: The specific URL format required by `triggerRemoteJob`, constructed as `jenkins://${resolvedPath.getInstanceId()}/${jobPath}`.
* **`parameterFactories`**: A list used within the triggering pipeline to define and pass parameters (names and values) to the remote job.
* **Pipeline Parameters**: Variables defined at the beginning of a pipeline (e.g., within a `parameters {}` block) that allow the pipeline to accept input values, such as strings defined with a default value and description.

# Setup

## Prerequisites
CJOC authentication mapping must be configured as **Trusted Controllers** with a **SYSTEM ↔ SYSTEM** mapping.  
See the [CloudBees documentation - trigger-jobs-across-controllers](https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/client-and-managed-controllers/trigger-jobs-across-controllers):  
**Note**: The controllers must be re-provisioned after authentication mappings have been updated or changed on the Operations Center. 
Documentation says "reconnect," but it must be re-provisioning [CloudBees documentation - change_the_authentication_mapping_strategy](https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping#_change_the_authentication_mapping_strategy)

## Controllers

1. **Source Controller**
    - The controller name is arbitrary.

2. **Target Controller**
    - Controller name: `my-target-controller`
    - This name must match the values defined in `Jenkinsfile-source.groovy`:
      ```groovy
         triggerRemoteJob remotePathMissing: stopAsFailure(), 
                remotePathUrl: getTargetInstanceID("my-target-controller","my-target-job")
      ```
## Source Pipeline

On the **Source Controller**, create a pipeline with the following configuration:

- **Job name**: `my-source-job`
- **Pipeline definition**: *Pipeline from SCM* (this repo)
- **Jenkinsfile**: `Jenkinsfile-source.groovy`


## Target Pipeline

On the **Target Controller**, create a pipeline with the following configuration:

- **Job name**: `my-target-job`
- **Pipeline definition**: *Pipeline from SCM* (this repo)
- **Jenkinsfile**: `Jenkinsfile-target.groovy`

  
# Links
* https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/client-and-managed-controllers/trigger-jobs-across-controllers
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping#_change_the_authentication_mapping_strategy
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/cloud-admin-guide/trigger-restrictions
