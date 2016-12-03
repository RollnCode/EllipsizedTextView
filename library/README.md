# EllipsizedTextView Library

----------
EllipsizedTextView cuts your text if it takes more lines than you need.
## Getting Started
### Dependency
Include the dependency [adasdDownload (.aar)](https://github.com/RollnCodeGit/ToolTipPopup/blob/master/app/libs/tooltippopup.aar) and place it into your libs directory:
```groovy
allprojects {
    repositories {
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}

// ...

dependencies {
    compile (name:'ellipsizedtextview', ext:'aar')
    compile 'com.android.support:appcompat-v7:24.2.1'
}
```

### Usage
You can use EllipsizedTextView in your layout like normal TextView and also set maxLines and postfix if needed. Here is an example:

    <com.rollncode.ellipsizedtextview.EllipsizedTextView
            android:id="@+id/el_info"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:maxLines="2"
            android:text="We create real masterpieces in software world. Roll'n'Code is a custom mobile app
            development company specializes in developing mobile applications for iOS, Android and web development."
            app:postfix="more"/>

You can also set OnEllipsizedTextViewListener to know when user click on text or on postfix.
