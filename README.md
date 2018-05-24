# gradle-dep-stackoverflow

This project has one direct dependency, `org.eclipse.platform:org.eclipse.swt:3.106.3`, whose POM has one dependency:

```xml
<dependency>
  <groupId>org.eclipse.platform</groupId>
  <artifactId>org.eclipse.swt.${osgi.platform}</artifactId>
  <version>[3.106.3,3.106.3]</version>
</dependency>
```

To make this work, you have to replace `${osgi.platform}` with something like `win32.win32.x86_64` or `cocoa.macosx.x86_64`.  We do that like this in our `build.gradle`:

```gradle
String PLATFORM = 'win32.win32.x86_64'
configurations.all {
  resolutionStrategy.eachDependency { DependencyResolveDetails details ->
    def requested = details.requested
    if (requested.name.contains('${osgi.platform}')) {
      details.useTarget("${requested.group}:${requested.name.replace('${osgi.platform}', PLATFORM)}:${requested.version}")
    }
  }
}
```

If you run `gradlew jar`, it builds just fine.  But, if you try to use the [`gradle-dependency-analyze` plugin](https://github.com/wfhartford/gradle-dependency-analyze) by running `gradlew analyzeDependencies`, then you get this error:

```
* Exception is:
java.lang.StackOverflowError
  at com.google.common.cache.LocalCache$Segment.preWriteCleanup(LocalCache.java:3426)
  at com.google.common.cache.LocalCache$Segment.storeLoadedValue(LocalCache.java:3113)
  at com.google.common.cache.LocalCache$Segment.getAndRecordStats(LocalCache.java:2351)
  at com.google.common.cache.LocalCache$Segment.loadSync(LocalCache.java:2318)
  at com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2280)
  at com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2195)
  at com.google.common.cache.LocalCache.get(LocalCache.java:3934)
  at com.google.common.cache.LocalCache$LocalManualCache.get(LocalCache.java:4736)
  at org.gradle.internal.resources.AbstractResourceLockRegistry.getOrRegisterResourceLock(AbstractResourceLockRegistry.java:43)
  at org.gradle.internal.work.DefaultWorkerLeaseService$WorkerLeaseLockRegistry.getResourceLock(DefaultWorkerLeaseService.java:205)
  at org.gradle.internal.work.DefaultWorkerLeaseService.getWorkerLease(DefaultWorkerLeaseService.java:80)
  at org.gradle.internal.work.DefaultWorkerLeaseService.getWorkerLease(DefaultWorkerLeaseService.java:87)
  at org.gradle.internal.work.DefaultWorkerLeaseService.getWorkerLease(DefaultWorkerLeaseService.java:44)
  at org.gradle.internal.operations.DefaultBuildOperationQueue.<init>(DefaultBuildOperationQueue.java:53)
  at org.gradle.internal.operations.DefaultBuildOperationQueueFactory.create(DefaultBuildOperationQueueFactory.java:31)
  at org.gradle.internal.progress.DefaultBuildOperationExecutor.executeInParallel(DefaultBuildOperationExecutor.java:133)
  at org.gradle.internal.progress.DefaultBuildOperationExecutor.runAll(DefaultBuildOperationExecutor.java:115)
  at org.gradle.api.internal.artifacts.ivyservice.resolveengine.artifact.ParallelResolveArtifactSet$VisitingSet.visit(ParallelResolveArtifactSet.java:60)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.sort(DefaultResolvedDependency.java:127)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getModuleArtifacts(DefaultResolvedDependency.java:106)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:112)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:114)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:114)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:114)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:114)
  at org.gradle.api.internal.artifacts.DefaultResolvedDependency.getAllModuleArtifacts(DefaultResolvedDependency.java:114)
```
