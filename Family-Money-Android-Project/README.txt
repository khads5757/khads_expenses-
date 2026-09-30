BUILD THE APK (free, no Android Studio needed) - about 10 minutes
1. Create a free account at github.com, then New repository (name: family-money, Private is fine).
2. Upload EVERYTHING from this folder, including the hidden .github folder, and commit.
   If .github is hard to upload: Add file > Create new file, name it  .github/workflows/build.yml  and paste the contents of build.yml.
3. Open the Actions tab > "Build APK" and wait ~5 minutes for the green tick.
4. Open the run > Artifacts > FamilyMoney-APK > unzip > app-debug.apk.
5. Copy app-debug.apk to your phone, tap it, allow "Install unknown apps", install.
6. On first open allow SMS permission. The app scans the last 14 days of bank SMS and shows "Import bank SMS (N new)".
