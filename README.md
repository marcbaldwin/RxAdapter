# RxAdapter
[![](https://jitpack.io/v/marcbaldwin/RxAdapter.svg)](https://jitpack.io/#marcbaldwin/RxAdapter)

Rx wrapper for ```RecyclerView``` that allows you to create an ```Adapter``` from Rx observables.

## Getting started

1. Add the [JitPack](https://jitpack.io) repository to your project's build.gradle
```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add a dependency to ***RxAdapter***
```
dependencies {
	implementation 'com.github.marcbaldwin:RxAdapter:4.0.0'
}
```

## Example

```Kotlin

val adapter = RxAdapter().apply {

  // Supports stable IDS if ids specified
  setHasStableIds(true)

  // Register your view holders
  registerViewHolder(HeaderViewHolder::class.java, R.layout.item_header)
  registerViewHolder(DateViewHolder::class.java, R.layout.item_header)

  // Header
  section {
    item(HeaderViewHolder::class.java, id = HEADER_ID) {
      binder = { title.text = R.string.todo_list_title }
    }
  }

  // Items
  section {
    items(DateViewHolder::class.java, items) {
      id = { todoItem -> todoItem.id }
      binder = { todoItem -> /* Bind item to the view holder */ }
      onClick = { todoItem -> /* Do something with the item */ }
    }
	}

  // Placeholder (Only visible if no items 😎)
  section {
    visible = items.map { it.isEmpty }
    item(HeaderViewHolder::class.java, id = PLACEHOLDER_ID) {
      binder = { title.text = R.string.no_items }
    }
  }

  // Optional item (Only visible if unwrapped value is not null)
  section {
    optionalItem(
      HeaderViewHolder::class.java,
      Observable.just(Optional<String>(null)),
      unwrap = { it.value },
      id = 3
    ) {
      binder = { text -> title.text = text }
    }
  }
}

// Set your recycler view's adapter
recyclerView.adapter = adapter

// Start the adapter
adapter.start()

// Stop the adapter
adapter.stop()

// Recycle all existing views
recyclerView.adapter = null

```

## Contributors

- Marc Baldwin [@marcbaldwin](https://github.com/marcbaldwin)
- Chetan Padia [@chetbox](https://github.com/chetbox)
