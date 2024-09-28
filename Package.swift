// swift-tools-version: 6.0
import PackageDescription

let package = Package(
    name: "TafiCloud",
    platforms: [.iOS(.v11)],
    products: [
        .library(
            name: "TafiCloud",
            targets: ["TafiCloud"]
        ),
    ],
    dependencies: [],
    targets: [
        .binaryTarget(
            name: "TafiCloud",
            path: "release/xcframework/TafiCloud.xcframework"
        ),
    ]
)