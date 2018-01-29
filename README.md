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
	implementation 'com.github.marcbaldwin:RxAdapter:1.9.0'
}
```

## Example

```Kotlin

val adapter = Adapter().apply {

	//// Register your view holders

	registerViewHolder(HeaderViewHolder::class.java, R.layout.item_header)
	registerViewHolder(DateViewHolder::class.java, R.layout.item_header)

	//// Add your items

	// Header
	section {
		item(HeaderViewHolder::class.java) {
			binder = {
				title.text = R.string.todo_list_title
			}
		}
	}

	// Items
	section {
		items(DateViewHolder::class.java, items) {
			id = { todoItem -> todoItem.id }
			binder = { todoItem ->
				title.text = todoItem.title
			}
			onClick = { todoItem ->
				// Do something with the item
			}
		}
	}

	// Placeholder (Only visible if no items 😎)
	section {
		visible = items.map { it.isEmpty }
		binder = {
			title.text = R.string.no_items
		}
		onClick = { todoItem ->
			// Do something with the item
		}
	}
}

//// Set your recycler view's adapter

recyclerView.adapter = adapter

```

## Contributors

- Marc Baldwin [@marcbaldwin](https://github.com/marcbaldwin)
- Chetan Padia [@chetbox](https://github.com/chetbox)
