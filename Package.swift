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
    targets: [
        .binaryTarget(
            name: "TafiCloud",
            path: "release/xcframework/TafiCloud.xcframework"
        ),
    ]
)