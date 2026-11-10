#!/usr/bin/env bash
set -e

echo "Building DocenteApp Web (Wasm/JS) for Vercel deployment..."
chmod +x ./gradlew
./gradlew :app:webApp:wasmJsBrowserDistribution --no-daemon

echo "Web distribution build complete in app/webApp/build/dist/wasmJs/productionExecutable"
