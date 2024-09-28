Pod::Spec.new do |spec|
    spec.name                     = 'TafiCloud'
    spec.version                  = '0.0.5'
    spec.homepage                 = 'https://docs.taficloud.com'
    spec.source                   = { :git => 'https://github.com/magnavisio/TafiCloudLib.git', :tag => '0.0.5'}
    spec.authors                  = 'MagnaVisio'
    spec.license                  = { :type => 'MIT', :file => 'LICENSE' }
    spec.summary                  = 'TafiCloud Library for iOS'
    spec.vendored_frameworks      = 'build/xcframework/TafiCloud.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.0'


    if !Dir.exist?('build/xcframework/TafiCloud.xcframework') || Dir.empty?('build/xcframework/TafiCloud.xcframework')
        raise "

        Kotlin framework 'TafiCloudIOS' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :library:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':library',
        'PRODUCT_MODULE_NAME' => 'TafiCloud',
    }
                
    spec.script_phases = [
        {
            :name => 'Build TafiCloud',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end