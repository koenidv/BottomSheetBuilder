# BottomSheetBuilder
[![](https://jitpack.io/v/koenidv/BottomSheetBuilder.svg)](https://jitpack.io/#koenidv/BottomSheetBuilder)
![](https://img.shields.io/badge/minSdkVersion-14-blue.svg)

A very easy to use builder for BottomSheetDialogs with support for custom styles.

## Download
```gradle
dependencies {
    implementation 'com.github.koenidv:BottomSheetBuilder:Tag'
}
```

## Quick start
```java
BottomSheetBuilder bottomsheet = new BottomSheetBuilder(context);
bottomsheet.setTitle(String title);
bottomsheet.addItems(String[] strings, int[] icons);
bottomsheet.setOnItemClickListener(onItemClickListener listener);
bottomsheet.show();
```

For the entire documentation, please refer to the [Wiki](https://github.com/koenidv/BottomSheetBuilder/wiki).
