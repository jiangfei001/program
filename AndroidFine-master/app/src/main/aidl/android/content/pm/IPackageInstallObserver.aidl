// IPackageInstallObserver.aidl
package android.content.pm;

// Declare any non-default types here with import statements
// https://github.com/android/platform_frameworks_base/tree/master/core/java/android/content/pm
interface IPackageInstallObserver {
    void packageInstalled(in String packageName, int returnCode);
}
