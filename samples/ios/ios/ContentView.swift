//
//  ContentView.swift
//  ios
//
//  Created by Funyin Kash on 6/19/24.
//

import SwiftUI
import TafiCloud

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello, world!")
        }
        .padding()
    }
}

#Preview {
	let client = Taficloud(apiKey: "")
	client.uploadBase64(file: <#T##String#>, folder: <#T##String#>) { <#MediaFile?#>, <#(any Error)?#> in
		<#code#>
	}
    ContentView()
}
