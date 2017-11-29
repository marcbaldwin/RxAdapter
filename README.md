# RxAdapter
[![](https://jitpack.io/v/marcbaldwin/RxAdapter.svg)](https://jitpack.io/#marcbaldwin/RxAdapter)

Rx wrapper for RecyclerView Adapters

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
	implementation 'com.github.marcbaldwin:RxAdapter:1.4.0'
}
```

## Example

```Kotlin

val adapter = Adapter().apply {

	//// Register your view holders

	registerViewHolder(HeaderViewHolder::class.java, { parent ->
		HeaderViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
		)
	})

	registerViewHolder(DateViewHolder::class.java, { parent ->
		DateViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
		)
	})

	//// Add your items

	// Header
	section {
		item(HeaderViewHolder::class.java) {
			binder = { viewHolder ->
					viewHolder.title.text = R.string.todo_list_title
			}
		}
	}

	// Items
	section {
		items(DateViewHolder::class.java, items) {
			id = { todoItem -> todoItem.id }
			binder = { todoItem, viewHolder ->
				viewHolder.title.text = todoItem.title
			}
		}
	}

}

//// Set your recycler view's adapter

recyclerView.adapter = adapter

```

## Contributors

Marc Baldwin [@marcbaldwin](https://github.com/marcbaldwin)
