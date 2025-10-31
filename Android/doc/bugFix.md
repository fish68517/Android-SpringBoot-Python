### 1. 我在一个APP申请的高德地图 KEY，结果被另一个APP使用，导致我的APP无法使用该KEY，是什么原因？
这种情况通常是由于两个APP使用了相同的包名（Package Name）和签名证书（Signing Certificate）。
在 Android 系统中，API Key 通常是绑定到特定的包名和签名证书的。如果两个 APP 共享相同的包名和签名证书，那么它们就会共享同一个 API Key，
这可能导致冲突和无法使用的情况。
报错信息中提到的 "INVALID_USER_SCODE" 就是由于这种冲突引起的。
// 定位失败: 7, KEY错误 请到 http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/ 查看错误码说明,
// 错误详细信息:auth fail:INVALID_USER_SCODE#SHA1AndPackage#66:99:CA:9A:AA:E0:F8:DE:B7:48:55:79:B6:EC:45:47:B4:DF:40:DD:com.archive.app#gsid#033040110065176187911983400030650663549#csid#87de83a295e34a24901561c158693b81#0701#pm110011

### 2. 如何获取这个app 的 SHA1 值？
要获取 Android 应用的 SHA1 值，可以使用以下方法：
1. 打开 Android Studio
2. 选择 "Gradle" 选项卡（通常在右侧）
3. 展开项目的 Gradle 任务，找到 "Tasks" -> "android" -> "signingReport"
4. 双击 "signingReport" 任务运行它
5. 在 "Run" 窗口中，你会看到生成的签名报告，其中包含 SHA1 值。它通常显示在 "Variant: debug" 或 "Variant: release" 部分下。
另外，你也可以使用命令行工具 `keytool` 来获取 SHA1 值：
```bash
keytool -list -v -keystore <path-to-your-keystore> -alias <your-alias>
```
将 `<path-to-your-keystore>` 替换为你的 keystore 文件的路径，`<your-alias>` 替换为你的密钥别名。运行该命令后，你会在输出中找到 SHA1 值。

### 但是  "Tasks" -> "android" -> "signingReport" 并没有Android 选项卡
如果在 Android Studio 中没有看到 "Gradle" 选项卡，可能是因为它被隐藏了。你可以尝试以下方法来显示它：
1. 在 Android Studio 的右侧边栏，寻找一个小的 "Gradle" 标签。如果找不到，可以尝试点击右上角的 "View" 菜单，
2. 然后选择 "Tool Windows" -> "Gradle"。
2. 如果仍然找不到，可以尝试重置 Android Studio 的布局。点击 "Window" 菜单，选择 "Restore Default Layout"。
3. 如果以上方法都不奏效，可以尝试通过快捷键打开 Gradle 窗口。通常，Windows 上的快捷键是 `Alt + 9`，Mac 上是 `Cmd + 9`。
4. 确保你的项目已经正确加载并且 Gradle 构建成功。如果项目有错误，Gradle 选项卡可能不会显示。
### 实在不行的话 在 terminal 里面执行 keytool 命令
PS D:\Acode\Android\complete\AA项目归档\毕设-Android-Springboot-Python\Android> .\gradlew signingReport

> Task :app:signingReport
Variant: debug
Config: debug
Store: C:\Users\Administrator\.android\debug.keystore
Alias: AndroidDebugKey
MD5: D1:7E:B7:59:62:40:03:CF:C1:81:B8:92:24:8D:12:84
SHA1: 66:99:CA:9A:AA:E0:F8:DE:B7:48:55:79:B6:EC:45:47:B4:DF:40:DD
SHA-256: BC:97:59:68:75:C9:1C:07:71:91:0D:9E:C3:DD:3B:EC:C9:AA:43:F7:66:B4:B8:FB:10:B1:F6:C0:33:F8:68:A8
Valid until: 2055年9月28日星期二
----------
Variant: release
Variant: release
Config: null
Store: null
Alias: null
----------
Variant: debugAndroidTest
Config: debug
Store: C:\Users\Administrator\.android\debug.keystore
Alias: AndroidDebugKey
MD5: D1:7E:B7:59:62:40:03:CF:C1:81:B8:92:24:8D:12:84
SHA1: 66:99:CA:9A:AA:E0:F8:DE:B7:48:55:79:B6:EC:45:47:B4:DF:40:DD
SHA-256: BC:97:59:68:75:C9:1C:07:71:91:0D:9E:C3:DD:3B:EC:C9:AA:43:F7:66:B4:B8:FB:10:B1:F6:C0:33:F8:68:A8
Valid until: 2055年9月28日星期二
----------

BUILD SUCCESSFUL in 15s
1 actionable task: 1 executed

