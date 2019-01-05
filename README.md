# CircularProgress

circular progress

## Preview

![preview](./images/preview.gif)

![preview](./images/preview_indeterminate.gif)

## Usage

### xml

You can create circular progress in xml like this(remeber to add xmlns:app="http://schemas.android.com/apk/res-auto"):

```xml

<com.jb.cp.CircularProgress
        android:layout_width="64dp"
        android:layout_height="64dp"
        app:indeterminate="true"
        app:stroke_width="4dp" />
		
```

**NOTE** : layout_height and layout_width should be same size.

And here are all the attributes
```xml

<resources>

    <declare-styleable name="CircularProgress">
        <attr name="color_progress" format="color" />
        <attr name="color_indicator" format="color" />
        <attr name="color_indicator_background" format="color" />
        <attr name="stroke_width" format="dimension" />
        <attr name="indeterminate" format="boolean" />
    </declare-styleable>

</resources>

```

### java

You have two methods for setting the progress:

```java

// Sets the value,
// and the circular progress will smoothly animate to that value with the speed.

setProgress(int value);
setProgress(int value, long speed);

```

```java

//Sets the value, and the circular progress will instantly move to that value.
circularProgress.setInstantProgress(int value)

```
