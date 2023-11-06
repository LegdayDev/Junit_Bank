# Junit Bank App

### Jpa LocalDateTime 자동으로 생성하는 법

- ``@EnableJpaAuditing``(Main 클래스) 

- ``@EntityListeners(AuditingEntityListener.class)`` (Entity 클래스)
- 
    ```java
    @CreatedDate //INSERT 시 자동생성
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //INSERT or, UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    ```
  
