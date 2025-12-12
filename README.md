# Cross-Controller Pipeline Triggering Flow

![flow.png](flow.png)

## Pipelines
* **[Parent Pipeline](Jenkinsfile-source.groovy)**: The initiating pipeline script that loads a shared library and executes the `triggerRemoteJob` step to start a job on another controller.
* **[Child Pipeline](Jenkinsfile-target.groovy)**: The target pipeline script that defines parameters (e.g., `paramKey1`) and executes steps when triggered by an upstream process.

## Shared Library Functions & Steps
* **`triggerRemoteJob`**: A pipeline step used to trigger a job on a remote controller. It requires a `remotePathUrl` and can optionally take parameters via `parameterFactories`.
* **`getTargetInstanceID` (Shared Library `call` function)**: A function that takes a `controllerName` and `jobPath` as input. It uses `RemoteDirectory.walk` to resolve the path's instance ID and returns the fully constructed `jenkins://` URL needed to trigger the remote job.
* **`resolveInstanceId`**: A helper function annotated with `@NonCPS` designed to elevate privileges to `ACL.SYSTEM` to ensure visibility when resolving a remote controller path into an instance ID.

## Key Concepts & Parameters
* **Remote URL Structure**: The specific URL format required by `triggerRemoteJob`, constructed as `jenkins://${resolvedPath.getInstanceId()}/${jobPath}`.
* **`parameterFactories`**: A list used within the triggering pipeline to define and pass parameters (names and values) to the remote job.
* **Pipeline Parameters**: Variables defined at the beginning of a pipeline (e.g., within a `parameters {}` block) that allow the pipeline to accept input values, such as strings defined with a default value and description.
* **`@NonCPS`**: An annotation required on certain methods (like those handling ACLs) to prevent serialization issues during pipeline execution.

# Notes
* Authentication mapping must be "trusted controllers" SYSTEM <-> SYSTEM Mapping https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/client-and-managed-controllers/trigger-jobs-across-controllers
* The controllers must be re-provisioned after authentication mappings on the Operations Center. Documentation says "reconnect," but it must be re-provisioning https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping#_change_the_authentication_mapping_strategy

# Links
* https://docs.cloudbees.com/docs/cloudbees-ci-kb/latest/client-and-managed-controllers/trigger-jobs-across-controllers
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/secure/authentication-mapping#_change_the_authentication_mapping_strategy
* https://docs.cloudbees.com/docs/cloudbees-ci/latest/cloud-admin-guide/trigger-restrictions
