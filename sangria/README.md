# sangria-exercise

## 疑問点

### 型引数の 1, 2 つ目の違いとは
```scala
  val ProductType: ObjectType[Unit, Product]
  val QueryType: ObjectType[ProductRepo, Unit]
```

`case class ObjectType[Ctx, Val: ClassTag]` のように 2 つの型引数をとる。
* `Val`: `resolve()` によって返される値を表し、`Context` の一部として `resolve()` に渡される
* `Ctx`: 実行全体に渡って流れるなんらかのコンテキストオブジェクトを表す。ほとんどの場合変化しないもの。DB にアクセスできるサービスやリポジトリオブジェクトが典型的な例
