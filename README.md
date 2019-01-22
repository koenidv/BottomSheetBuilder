# BottomSheetBuilder
[![](https://jitpack.io/v/koenidv/BottomSheetBuilder.svg)](https://jitpack.io/#koenidv/BottomSheetBuilder)
![](https://img.shields.io/badge/minSdkVersion-14-blue.svg)

A very easy to use builder for BottomSheetDialogs with support for custom styles.

## Download
```gradle
dependencies {
    implementation 'com.github.koenidv:BottomSheetBuilder:v1.1'
}
```

## Quick start
```java
BottomSheetBuilder bottomSheet = new BottomSheetBuilder(context);
bottomSheet
    .setTitle(String title)
    .addItems(String[] strings, int[] icons)
    .setOnItemClickListener(onItemClickListener listener)
    .show();
```

For the entire documentation, please refer to the [Wiki](https://github.com/koenidv/BottomSheetBuilder/wiki).
