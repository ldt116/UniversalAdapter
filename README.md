# UniversalAdapter

**Note: This work is under construction.**
------------------------------------------

Yet another adapter for **RecyclerView** on Android, or perhaps, the last one you need. With `UniversalAdapter`, you don't need to write a **RecyclerView** adapter ever again, including its' **ViewHolder**.

## Features

* No need to write adapter
* No need to write viewholders
* Supports sectioned data with `categorizer`
* Designed with `callback` for every binding function. So you can handle for long operations.
* `ImageView` load from local drawble resource or remote url via [Ion](https://github.com/koush/ion).
* Customized binder is availabled for customized view.

## Setup

You can get via `jCenter()` in Gradle:

```groovy
compile '...'
```

## Example  

### Hello world"

In this first example, we create a list for an array of 10 element  "A".."J".

Let's start with the item layout first

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:orientation="horizontal">

    <ImageView
        android:id="@+id/thumb"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:src="@mipmap/ic_launcher"/>

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"/>
</LinearLayout>
```

In the `Fragment` code, first we create the builder pattern for the adapter as

```java
UniversalAdapter<String> adapter = UniversalAdapter.Builder.with("A","B","C","D","E","F","G","H","I","J")
```

Here, we have `String` is the data type for each item. You can find other example with `ArrayList` and customize data type later.

Then, we need to declare the layout of each item by using `itemLayout`

```java
.itemLayout(R.layout.item_simple)
```

Next step, we need to guide "how the data is display" or "binding step". In this example we bind the `TextView` with id `R.id.tvTitle` with the text at that position. In order to accomplish this, we need a `ViewBinder` for binding data to a particular view. There are ViewBinders which are already implemented for you to use. In this case, we will use `TextViewBinder`. Once again, `TextViewBinder` is provided with Builder pattern, which help you easily to create an instance without much effort.

    Note: I am trying to make ViewBinders which cover all general View widgets in the meanwhile. So if you find widgets do not have binder, feel free to make a PR or creating an issue.

```java
.bind(TextViewBinder.Builder.with(R.id.tvTitle, String.class)
    .text(new OnBindAsyncAction<String, String>() {
        @Override
        public void map(int position, String data, DataCallback<String> callback) {
            callback.onResult(data);
        }
    }))
```
The `TextViewBinder.Builder.with(R.id.tvTitle, String.class)` creates a `TextViewBinder` with id `R.id.tvTitle` and `String.class` is the data of each item which must be the same class. 

The `.text(..)` is corresponding the action `setText` for the `TextView`, you need to point the title you want to set in function `void map(int position, String data, DataCallback<String> callback)` with

* `position` is the position of current item in the adapter.
* `data` is the corresponding data of current item in the adapter.
* `callback` provide you the method to return the data will be taken action (in this case, the data will be call as `setText(data)`. As we want the title will be the same as the data, so will call `callback.onResult(data)`

There is a question, why dont just simplify the `map` to something give direct like `String map(position, data)`. The answer is with `callback` approach, you don't get ANR in case you need a long time process here (such as download image from the internet, you can find more in other example).

In conclusion, the full fragment code is as below:

```java
public class SimpleAdapterFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);
        UniversalAdapter<String> adapter = UniversalAdapter.Builder.with("A","B","C","D","E","F","G","H","I","J")
                .itemLayout(R.layout.item_simple)
                .bind(TextViewBinder.Builder.with(R.id.tvTitle, String.class)
                        .text(new OnBindAsyncAction<String, String>() {
                            @Override
                            public void map(int position, String data, DataCallback<String> callback) {
                                callback.onResult(data);
                            }
                        }))
                .build();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        return rv;
    }
}
```

And here is what we got

![Example Simple](doc/screenshot/example-simple.png)

You can check the complete examples in the [example]() project

### Columns aka Grid

![Example Grid](doc/screenshot/example-grid.png)

## Changelog
Change log can be found [here](changelog).

## TODO

Here is the list for future work

* Binder for common widget views such as `Button`, `Spinner`.

Acknowledgements
----------------

3rd party libraries used in `UniversalAdapter`:

* [Google Guava](https://github.com/google/guava)
* [Ion](https://github.com/koush/ion)


Contribution
------------ 

Please fork this repository and contribute back using [pull requests](https://github.com/ldt116/UniversalAdapter/pulls).

Any contributions, large or small, major features, bug fixes, additional language translations, unit/integration tests are welcomed and appreciated but will be thoroughly reviewed and discussed.


License
-------
    
    MIT License
    
    Copyright (c) 2017 ThuanLe
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.