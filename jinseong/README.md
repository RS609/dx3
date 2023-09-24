## Persistence layer에서 DAO를 구현할 때 JpaRepository를 상속받지 않고 Repository를 상속받는 이유

[이 글](https://creampuffy.tistory.com/179)을 적극 참조하여 재해석함.

<img width="762" alt="스크린샷 2023-09-24 오후 9 03 46" src="https://github.com/RS609/dx3/assets/52629158/72300233-6c2a-4075-8264-1b62682050e3">

### 1. 객체의 책임과 역할을 표현하기 위해 필요한 메시지만 개방할 수 있다.

객체지향의 관점에서 메시지는 협력의 수단이다. 
어떤 객체가 어떤 메시지를 받을 수 있는지는 클래스의 메서드를 보면 어떤 메시지를 개방하는지 알 수 있다.
JpaRepository는 많은 인터페이스를 상속받고 있다. 당장 JpaRepository를 상속받는 클래스에게
필요 없는 메시지까지 개방하는 경우가 있을 수 있다. 따라서 아무 메세지도 제공해주지 않는 Repository를 받는 것이
객체지향 관점에서 좋다. 클래스의 명세만 봐도 이 객체가 어떤 메시지를 개방하고 있고, 어떤 역할을 수행하는지 한 눈에 파악하기 쉽기 때문이다.

### 2. 인터페이스의 명세적 특성을 지키는 것이 무의미해진다.

인터페이스를 구현(implements)한 클래스는 인터페이스의 명세를 강제로 따라가야 한다는 특성을 가진다.
하지만 불필요하게 많은 인터페이스를 구현하기 때문에 명세를 지킨다는 의미가 희석됨.

### 3. 테스트 더블로써 활용할 때 불필요하게 많은 메서드를 구현해야 한다.

당장 테스트에 필요하지 않은 메서드도 모두 구현해야 한다.
만약 `findAll()` 메서드만 필요한 경우라면,

**JpaRepository를 구현할 경우**

```kotlin
class MemberDataDaoTest {
    val dao: MemberDataDao = object : MemberDataDao {
        override fun <S : MemberData?> save(entity: S): S {
            TODO("Not yet implemented")
        }
        
        override fun <S : MemberData?> saveAll(entities: MutableIterable<S>): MutableList<S> {
            TODO("Not yet implemented")
        }
        
        override fun findById(id: String): Optional<MemberData> {
            TODO("Not yet implemented")
        }
        // ...
        // 추가적으로 30개 이상의 메서드를 구현해야 한다.
    }
}
```

**Repository를 구현할 경우**

```kotlin
class MemberDataDaoTest {
    val dao: MemberDataDao = object : MemberDataDao {
        override fun findAll(): List<MemberData> {
            TODO("Not yet implemented")
        }
    }
}
```

## 알게된 JPA Annotation 정리

### `@EmbeddedId`

```java
@Entity
public class Order {
    @EmbeddedId
    private OrderNo number;
}
```

```java
@Embeddable
public class OrderNo implements Serializable {
    @Column(name = "order_number")
    private String number;
}
```

### `@Access`

```java
@Entity
@Access(AccessType.FIELD)
public class Order { }
```

### `@AttributeOverrides`, `@AttributeOverride`

```java
@Embeddable
public class Orderer {
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "orderer_id"))
    )
    private MemberId memberId;
}
```

### `@Converter`, `@Convert`

```java
@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Money money) {
        return money == null ? null : money.getValue();
    }

    @Override
    public Money convertToEntityAttribute(Integer value) {
        return value == null ? null : new Money(value);
    }
}
```

```java
@Entity
public class Order {
    @Convert(converter = MoneyConverter.class)
    @Column(name = "total_amounts")
    private Money totalAmounts;
}
```

### `@ElementCollection`, `@CollectionTable`, `@OrderColumn`

```java
@Entity
public class Order {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_line", joinColumns = @JoinColumn(name = "order_number"))
    @OrderColumn(name = "line_idx")
    private List<OrderLine> orderLines;
}
```

### `@SecondaryTable`

```java
@Entity
@Table(name = "article")
@SecondaryTable(
        name = "article_content",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "id")
)
public class Article { }
```

### `@Inheritance`

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Table(name = "image")
public abstract class Image { }
```

### `@DiscriminatorColumn`

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Table(name = "image")
public abstract class Image { }
```

### `@DiscriminatorValue`

```java
@Entity
@DiscriminatorValue("II")
public class InternalImage extends Image { }
```
